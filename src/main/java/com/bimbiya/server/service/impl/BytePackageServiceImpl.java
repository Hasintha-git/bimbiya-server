package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.BytePackageRequestDTO;
import com.bimbiya.server.dto.response.BytePackageResponseDTO;
import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.BytePackageIngredients;
import com.bimbiya.server.entity.BytePackageIngredientsId;
import com.bimbiya.server.entity.Ingredients;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.BytePackageIngredientsRepository;
import com.bimbiya.server.repository.BytePackageRepository;
import com.bimbiya.server.repository.IngredientsRepository;
import com.bimbiya.server.repository.specifications.BytePackageSpecification;
import com.bimbiya.server.service.BytePackageService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.ClientPotionEnum;
import com.bimbiya.server.util.enums.ClientStatusEnum;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BytePackageServiceImpl implements BytePackageService {

    private BytePackageRepository bytePackageRepository;
    private BytePackageIngredientsRepository bytePackageIngredientsRepository;
    private IngredientsRepository ingredientsRepository;
    private BytePackageSpecification bytePackageSpecification;
    private ModelMapper modelMapper;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());
            List<SimpleBaseDTO> portionStatus = Stream.of(ClientPotionEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

            //get user role list
            List<Ingredients> data = ingredientsRepository.findAllByStatusCode(Status.active);
            List<SimpleBaseDTO> ingredientsList = data.stream().map(userRole -> {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO();
                return EntityToDtoMapper.mapIngredientDropdown(simpleBaseDTO, userRole);
            }).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("portionList", portionStatus);
            refData.put("ingredientsList", ingredientsList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Object getBytePackageFilterList(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(bytePackageRequestDTO.getSortColumn()) && Objects.nonNull(bytePackageRequestDTO.getSortDirection()) &&
                    !bytePackageRequestDTO.getSortColumn().isEmpty() && !bytePackageRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        bytePackageRequestDTO.getPageNumber(), bytePackageRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(bytePackageRequestDTO.getSortDirection()), bytePackageRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(bytePackageRequestDTO.getPageNumber(), bytePackageRequestDTO.getPageSize());
            }

            List<BytePackage> bytePackageList = ((Objects.isNull(bytePackageRequestDTO.getBytePackageSearchDTO())) ? bytePackageRepository.findAll
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    bytePackageRepository.findAll(bytePackageSpecification.generateSearchCriteria(bytePackageRequestDTO.getBytePackageSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(bytePackageRequestDTO.getBytePackageSearchDTO())) ? bytePackageRepository.count
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted)) :
            bytePackageRepository.count(bytePackageSpecification.generateSearchCriteria(bytePackageRequestDTO.getBytePackageSearchDTO()));

            List<BytePackageResponseDTO> collect = bytePackageList.stream()
                    .map(byt -> EntityToDtoMapper.mapBytePackage(byt))
                    .collect(Collectors.toList());

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
    public ResponseEntity<Object> findBytePackageById(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        try {
            BytePackage bytePackage = Optional.ofNullable(bytePackageRepository.findByPackageIdAndStatusNot(bytePackageRequestDTO.getPackageId(), ClientStatusEnum.DELETED.getCode())).orElse(
                    null
            );

            if (Objects.isNull(bytePackage)) {
                return responseGenerator.generateErrorResponse(bytePackageRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.BYTEPACKAGE_NOT_FOUND, new
                                Object[]{bytePackageRequestDTO.getPackageId()},locale);
            }

            BytePackageResponseDTO bytePackageResponseDTO = EntityToDtoMapper.mapBytePackage(bytePackage);

            setBannerImage(bytePackage, bytePackageResponseDTO);

            List<BytePackageIngredients> bytePackageIngredients = Optional.ofNullable(bytePackageIngredientsRepository.findAllByBytePackage(bytePackage)).orElse(null);

            if (Objects.nonNull(bytePackageIngredients)) {
                List<Long> byteIngredientsResponseDTOList= new ArrayList<>();
                for (BytePackageIngredients bytePackageIngredient : bytePackageIngredients) {
                    byteIngredientsResponseDTOList.add(bytePackageIngredient.getIngredients().getIngredientsId());
                }

                bytePackageResponseDTO.setIngredientList(byteIngredientsResponseDTOList);

            }

            return responseGenerator
                    .generateSuccessResponse(bytePackageRequestDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.BYTEPACKAGE_SUCCESSFULLY_FIND, locale, bytePackageResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    public static BytePackageResponseDTO setBannerImage(BytePackage bytePackage,BytePackageResponseDTO bytePackageResponseDTO) {
        if (Objects.nonNull(bytePackage.getImg())) {
            String encode = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(bytePackage.getImg()));
            bytePackageResponseDTO.setImg(encode);
        }
        return bytePackageResponseDTO;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> trendingPackagesList(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        // need dev with order
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> saveBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        try{
            BytePackage bytePackage = Optional.ofNullable(bytePackageRepository.findByMealNameAndStatusNot(bytePackageRequestDTO.getMealName(), ClientStatusEnum.DELETED.getCode()))
                    .orElse(null);

            if (Objects.nonNull(bytePackage)) {
                return responseGenerator
                        .generateErrorResponse(bytePackageRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.BYTEPACKAGE_MEALNAME_ALREADY_EXIST, new Object[] {bytePackageRequestDTO.getMealName()},
                                locale);
            }

            bytePackage= new BytePackage();
            DtoToEntityMapper.mapBytePackage(bytePackage,bytePackageRequestDTO, true);
            bytePackage.setCreatedTime(new Date());
            bytePackage.setLastUpdatedTime(new Date());

            bytePackageRepository.save(bytePackage);

            if (Objects.nonNull(bytePackageRequestDTO.getIngredientList())) {
                for (Long iId : bytePackageRequestDTO.getIngredientList()) {
                    Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(iId, Status.deleted)).orElse(null);
                    BytePackageIngredients bytePackageIngredients = new BytePackageIngredients();
                    BytePackageIngredientsId bytePackageIngredientsId = new BytePackageIngredientsId();
                    bytePackageIngredients.setBytePackage(bytePackage);
                    bytePackageIngredients.setIngredients(ingredients);

                    bytePackageIngredientsId.setBytePackage(bytePackage.getPackageId());
                    bytePackageIngredientsId.setIngredients(iId);
                    bytePackageIngredients.setId(bytePackageIngredientsId);
                    bytePackageIngredientsRepository.save(bytePackageIngredients);
                }
            }

            return responseGenerator.generateSuccessResponse(bytePackageRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.INGREDIENT_SUCCESSFULLY_SAVE, locale, new Object[] {bytePackageRequestDTO.getMealName()});
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
    public ResponseEntity<Object> editBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        try{
            BytePackage bytePackage = Optional.ofNullable(bytePackageRepository.findByPackageIdAndStatusNot(bytePackageRequestDTO.getPackageId(), ClientStatusEnum.DELETED.getCode()))
                    .orElse(null);

            if (Objects.isNull(bytePackage)) {
                return responseGenerator
                        .generateErrorResponse(bytePackageRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.BYTEPACKAGE_NOT_FOUND, new Object[] {bytePackageRequestDTO.getMealName()},
                                locale);
            }


            Optional<BytePackage> uniPackage = Optional.ofNullable(bytePackageRepository.findByMealNameAndStatusNotAndPackageIdNot(bytePackageRequestDTO.getMealName(), ClientStatusEnum.DELETED.getCode(), bytePackageRequestDTO.getPackageId()))
                    .orElse(null);

            if (Objects.nonNull(uniPackage)) {
                return responseGenerator
                        .generateErrorResponse(bytePackageRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.BYTEPACKAGE_MEALNAME_ALREADY_EXIST, new Object[] {bytePackageRequestDTO.getMealName()},
                                locale);
            }


            DtoToEntityMapper.mapBytePackage(bytePackage,bytePackageRequestDTO, false);
            bytePackage.setLastUpdatedTime(new Date());

            bytePackageRepository.save(bytePackage);

            bytePackageIngredientsRepository.deleteAllByBytePackage(bytePackage);

            if (Objects.nonNull(bytePackageRequestDTO.getIngredientList())) {
                for (Long iId : bytePackageRequestDTO.getIngredientList()) {
                    Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(iId, Status.deleted)).orElse(null);
                    BytePackageIngredients bytePackageIngredients = new BytePackageIngredients();
                    BytePackageIngredientsId bytePackageIngredientsId = new BytePackageIngredientsId();
                    bytePackageIngredients.setBytePackage(bytePackage);
                    bytePackageIngredients.setIngredients(ingredients);

                    bytePackageIngredientsId.setBytePackage(bytePackage.getPackageId());
                    bytePackageIngredientsId.setIngredients(iId);
                    bytePackageIngredients.setId(bytePackageIngredientsId);
                    bytePackageIngredientsRepository.save(bytePackageIngredients);
                }
            }

            return responseGenerator.generateSuccessResponse(bytePackageRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.BYTEPACKAGE_SUCCESSFULLY_UPDATE, locale, new Object[] {bytePackageRequestDTO.getMealName()});
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
    public ResponseEntity<Object> deleteBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        try{
            BytePackage bytePackage = Optional.ofNullable(bytePackageRepository.findByPackageIdAndStatusNot(bytePackageRequestDTO.getPackageId(), ClientStatusEnum.DELETED.getCode()))
                    .orElse(null);

            if (Objects.isNull(bytePackage)) {
                return responseGenerator
                        .generateErrorResponse(bytePackageRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.BYTEPACKAGE_NOT_FOUND, new Object[] {bytePackageRequestDTO.getMealName()},
                                locale);
            }

            bytePackageIngredientsRepository.deleteAllByBytePackage(bytePackage);
            bytePackage.setStatus(ClientStatusEnum.DELETED.getCode());
            bytePackageRepository.save(bytePackage);


            return responseGenerator.generateSuccessResponse(bytePackageRequestDTO, HttpStatus.OK,
                    ResponseCode.DELETE_SUCCESS, MessageConstant.BYTEPACKAGE_SUCCESSFULLY_DELETE, locale, new Object[] {bytePackageRequestDTO.getMealName()});
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
