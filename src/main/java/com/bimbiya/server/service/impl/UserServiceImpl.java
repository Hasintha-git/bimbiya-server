package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.EmailSendDTO;
import com.bimbiya.server.dto.request.UserRequestDTO;
import com.bimbiya.server.dto.response.DashboardResponseDTO;
import com.bimbiya.server.dto.response.UserResponseDTO;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.entity.UserRole;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.OrderDetailRepository;
import com.bimbiya.server.repository.OrderRepository;
import com.bimbiya.server.repository.UserRepository;
import com.bimbiya.server.repository.UserRoleRepository;
import com.bimbiya.server.repository.specifications.UserSpecification;
import com.bimbiya.server.service.NotificationService;
import com.bimbiya.server.service.UserService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.ClientDistrictEnum;
import com.bimbiya.server.util.enums.ClientStatusEnum;
import com.bimbiya.server.util.enums.District;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    private UserRoleRepository userRoleRepository;

    private UserSpecification userSpecification;

    private ModelMapper modelMapper;

    private ResponseGenerator responseGenerator;


    private PasswordEncoder passwordEncoder;
    private NotificationService notificationService;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

            //get district
            List<SimpleBaseDTO> district = Stream.of(ClientDistrictEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

            //get user role list
            List<UserRole> data = userRoleRepository.findAllByStatusCode(Status.active);
            List<SimpleBaseDTO> simpleBaseDTOList = data.stream().map(userRole -> {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO();
                return EntityToDtoMapper.mapUserRoleDropdown(simpleBaseDTO, userRole);
            }).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("districtList", district);
            refData.put("userRoleList", simpleBaseDTOList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Integer sentOtp(String email, Locale locale) throws Exception {
        try {
            EmailSendDTO emailSendDTO = new EmailSendDTO();
            emailSendDTO.setToEmail(email);
            emailSendDTO.setSubject("Bimbiya account verification OTP");

            Random random = new Random();

            // Generate a random 6-digit number
            int min = 100000; // Minimum 6-digit number
            int max = 999999; // Maximum 6-digit number
            int randomNumber = random.nextInt(max - min + 1) + min;


            String body = "Dear User,\n\n"
                    + "Your verification OTP is: " + randomNumber + ".\n\n"
                    + "Best regards,\n"
                    + "Bimbiya Team";
            emailSendDTO.setBody(body);
            notificationService.emailSent(emailSendDTO, locale);

            return randomNumber;
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ResponseEntity<Object> confirmOtp(Integer otp, String userName, Locale locale) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(userName, Status.blocked))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userName, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND, MessageConstant.USER_NOT_FOUND, new Object[]{userName},
                                locale);
            }

            if (otp.equals(systemUser.getOtp())) {

                systemUser.setStatus(Status.active);
                userRepository.save(systemUser);
                return responseGenerator
                        .generateSuccessResponse(userName, HttpStatus.OK, ResponseCode.SAVED_SUCCESS,
                                MessageConstant.SUCCESS_OTP, locale, new Object[]{userName});
            }

            systemUser.setStatus(Status.deleted);
            userRepository.save(systemUser);
            return responseGenerator
                    .generateErrorResponse(userName, HttpStatus.CONFLICT,
                            ResponseCode.NOT_FOUND, MessageConstant.WRONG_OTP, new Object[]{userName},
                            locale);

        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public Object getUserFilterList(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(userRequestDTO.getSortColumn()) && Objects.nonNull(userRequestDTO.getSortDirection()) &&
                    !userRequestDTO.getSortColumn().isEmpty() && !userRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        userRequestDTO.getPageNumber(), userRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(userRequestDTO.getSortDirection()), userRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(userRequestDTO.getPageNumber(), userRequestDTO.getPageSize());
            }

            List<SystemUser> faqList = ((Objects.isNull(userRequestDTO.getUserRequestSearchDTO())) ? userRepository.findAll
                    (userSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    userRepository.findAll(userSpecification.generateSearchCriteria(userRequestDTO.getUserRequestSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(userRequestDTO.getUserRequestSearchDTO())) ? userRepository.count
                    (userSpecification.generateSearchCriteria(Status.deleted)) :
                    userRepository.count(userSpecification.generateSearchCriteria(userRequestDTO.getUserRequestSearchDTO()));

            List<UserResponseDTO> collect = faqList.stream()
                    .map(EntityToDtoMapper::mapUser)
                    .collect(Collectors.toList());

//            return responseGenerator
//                    .generateSuccessResponse(userRequestDTO, HttpStatus.OK, ResponseCode.USER_GET_SUCCESS
//                            , MessageConstant.SUCCESSFULLY_GET, locale, collect, fullCount);

            return new DataTableDTO<>(fullCount, (long) collect.size(), collect, null);

        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        }catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> findUserById(UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.GET_SUCCESS, MessageConstant.USER_NOT_FOUND, new
                                Object[]{userRequestDTO.getId()},locale);
            }

            UserResponseDTO userResponseDTO = EntityToDtoMapper.mapUser(systemUser);
            userResponseDTO.setUserRole(systemUser.getUserRole().getCode());
            userResponseDTO.setUserRoleDescription(systemUser.getUserRole().getDescription());
            return responseGenerator
                    .generateSuccessResponse(userRequestDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, userResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Object> forDashboard(Locale locale) throws Exception {
        try {
            DashboardResponseDTO dashboardResponseDTO = new DashboardResponseDTO();
            dashboardResponseDTO.setDeleteUsers(userRepository.countByStatus(Status.deleted));
            dashboardResponseDTO.setActiveUsers(userRepository.countByStatus(Status.active));
            dashboardResponseDTO.setInactiveUsers(userRepository.countByStatus(Status.inactive));
            dashboardResponseDTO.setRejectedOrders(orderRepository.countAllByStatus(Status.rejected));
            dashboardResponseDTO.setPendingOrders(orderRepository.countAllByStatus(Status.pending));
            dashboardResponseDTO.setCompletedOrders(orderRepository.countAllByStatus(Status.completed));
            dashboardResponseDTO.setProcessingOrders(orderRepository.countAllByStatus(Status.processing));
            dashboardResponseDTO.setShippedOrders(orderRepository.countAllByStatus(Status.shipped));
            dashboardResponseDTO.setTodayOrders(orderRepository.countAllByOrderDate(new Date()));

            return responseGenerator
                    .generateSuccessResponse(dashboardResponseDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, dashboardResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> saveUser(UserRequestDTO userRequestDTO, Locale locale, boolean client) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatusNot(userRequestDTO.getUsername(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST, MessageConstant.USER_NAME_ALREADY_EXIST, new Object[]{userRequestDTO.getUsername()},
                                locale);
            }
            systemUser = Optional.ofNullable(userRepository.findByEmailAndStatusNot(userRequestDTO.getEmail(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EMAIL_ALREADY_EXIST, new Object[] {userRequestDTO.getEmail()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNot(userRequestDTO.getNic(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByMobileNoAndStatusNot(userRequestDTO.getMobileNo(), Status.deleted))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_MOBILE_ALREADY_EXIST, new Object[] {userRequestDTO.getMobileNo()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNot(userRequestDTO.getNic(), Status.deleted))
                    .orElse(null);

            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            UserRole userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCode(userRequestDTO.getUserRole(), Status.active)).orElse(null);
            if (Objects.isNull(userRole)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_ROLE_NOT_FOUND,
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(userRequestDTO.getNic(), Status.deleted))
                    .orElse(new SystemUser());


            Date systemDate = new Date();
            modelMapper.map(userRequestDTO, systemUser);
            if (Objects.nonNull(userRequestDTO.getStatus())) {
                systemUser.setStatus(Status.valueOf(userRequestDTO.getStatus()));
            }else {
                systemUser.setStatus(Status.active);
            }
            systemUser.setPwStatus(Status.active);
            systemUser.setDistrict(District.valueOf(userRequestDTO.getDistrict()));
            systemUser.setPasswordExpireDate(systemDate);
            if (Objects.nonNull(userRequestDTO.getActiveUserName())) {
                systemUser.setCreatedUser(userRequestDTO.getActiveUserName());
                systemUser.setLastUpdatedUser(userRequestDTO.getActiveUserName());
            }else {
                systemUser.setCreatedUser(userRequestDTO.getUsername());
                systemUser.setLastUpdatedUser(userRequestDTO.getUsername());
            }
            systemUser.setCreatedTime(systemDate);
            systemUser.setLastUpdatedTime(systemDate);
            systemUser.setAttempt(0);
            systemUser.setUserRole(userRole);

            String encode = passwordEncoder.encode(userRequestDTO.getPassword());
            systemUser.setPassword(encode);

            userRepository.save(systemUser);

            if (client) {
                Integer randomNumber = sentOtp(systemUser.getEmail(), locale);
                systemUser.setStatus(Status.blocked);
                userRepository.save(systemUser);

                systemUser.setLastUpdatedTime(systemDate);
                systemUser.setOtp(randomNumber);
                userRepository.save(systemUser);

                return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                        ResponseCode.SAVED_SUCCESS, MessageConstant.OTP_SENT, locale, new Object[]{userRequestDTO.getEmail()});
            }
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.USER_SUCCESSFULLY_SAVE, locale, new Object[]{userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> editUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByEmailAndStatusNotAndIdNot(userRequestDTO.getEmail(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_EMAIL_ALREADY_EXIST, new Object[] {userRequestDTO.getEmail()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNotAndIdNot(userRequestDTO.getNic(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByMobileNoAndStatusNotAndIdNot(userRequestDTO.getMobileNo(), Status.deleted,userRequestDTO.getId()))
                    .orElse(null);
            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_MOBILE_ALREADY_EXIST, new Object[] {userRequestDTO.getMobileNo()},
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByNicAndStatusNotAndIdNot(userRequestDTO.getNic(), Status.deleted, userRequestDTO.getId()))
                    .orElse(null);

            if (Objects.nonNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.USER_NIC_ALREADY_EXIST, new Object[] {userRequestDTO.getNic()},
                                locale);
            }

            UserRole userRole = Optional.ofNullable(userRoleRepository.findByCodeAndStatusCode(userRequestDTO.getUserRole(), Status.active)).orElse(null);
            if (Objects.isNull(userRole)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.NOT_FOUND,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_ROLE_NOT_FOUND,
                                locale);
            }

            systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            Date systemDate = new Date();
            systemUser.setLastUpdatedTime(systemDate);
            systemUser.setUserRole(userRole);

//            String encode = passwordEncoder.encode(userRequestDTO.getPassword());
//            systemUser.setPassword(encode);

            DtoToEntityMapper.mapUser(systemUser, userRequestDTO);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.USER_SUCCESSFULLY_UPDATE, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> forgetPassword(UserRequestDTO userRequestDTO, Locale locale) {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            Date systemDate = new Date();
            systemUser.setLastUpdatedTime(systemDate);
            String encode = passwordEncoder.encode(userRequestDTO.getPassword());
            systemUser.setPassword(encode);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.USER_PASSWORD_RESET, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> lockUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            Date systemDate = new Date();
            systemUser.setStatus(Status.locked);
            systemUser.setLastUpdatedTime(systemDate);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.USER_LOCKED, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> unlockUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            Date systemDate = new Date();
            systemUser.setStatus(Status.active);
            systemUser.setLastUpdatedTime(systemDate);

            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.USER_UNLOCKED, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteUser(UserRequestDTO userRequestDTO, Locale locale) {
        try{

            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(userRequestDTO.getId(), Status.deleted))
                    .orElse(null);
            if (Objects.isNull(systemUser)) {
                return responseGenerator
                        .generateErrorResponse(userRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {userRequestDTO.getUsername()},
                                locale);
            }

            systemUser.setStatus(Status.deleted);
            userRepository.save(systemUser);
            return responseGenerator.generateSuccessResponse(userRequestDTO, HttpStatus.OK,
                    ResponseCode.DELETE_SUCCESS, MessageConstant.USER_SUCCESSFULLY_DELETE, locale, new Object[] {userRequestDTO.getUsername()});
        }
        catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
