package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.request.AddToCartRequestDTO;
import com.bimbiya.server.dto.response.AddToCartResponseDTO;
import com.bimbiya.server.dto.response.CheckoutResponseDTO;
import com.bimbiya.server.entity.AddToCart;
import com.bimbiya.server.entity.Product;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.AddToCartRepository;
import com.bimbiya.server.repository.ProductRepository;
import com.bimbiya.server.repository.UserRepository;
import com.bimbiya.server.service.AddToCartService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AddToCartServiceImpl implements AddToCartService {

    private AddToCartRepository addToCartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private ResponseGenerator responseGenerator;
    private MessageSource messageSource;
    @Override
    public Object getReferenceData() {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> addToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        try{
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsername(addToCartRequestDTO.getUserName()))
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(MessageConstant
                            .USER_NOT_FOUND, null, locale))).get();

            Product product = Optional.ofNullable(productRepository.findById(addToCartRequestDTO.getPackageId()))
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(MessageConstant
                            .BYTEPACKAGE_NOT_FOUND, null, locale))).get();

            AddToCart addToCart = Optional.ofNullable(addToCartRepository.findBySystemUserAndBpackage(systemUser, product))
                    .orElse(null);

            if (Objects.nonNull(addToCart)) {
                int i = addToCart.getQty() + addToCartRequestDTO.getQty();
                addToCart.setQty(i);
                int personCount = addToCart.getPersonCount() + addToCartRequestDTO.getPersonCount();
                addToCart.setPersonCount(personCount);
                addToCart.setPrice(addToCart.getPrice().add(addToCartRequestDTO.getProductPrice()));

                addToCartRepository.save(addToCart);
                return responseGenerator.generateSuccessResponse(addToCartRequestDTO, HttpStatus.OK,
                        ResponseCode.SAVED_SUCCESS, MessageConstant.ADD_TO_CART_SUCCESS, locale, new Object[] {addToCartRequestDTO.getPackageId()});
            }

            addToCart= new AddToCart();
            DtoToEntityMapper.mapAddToCart(addToCart, addToCartRequestDTO);
            addToCart.setPrice(addToCartRequestDTO.getProductPrice());
            addToCart.setBpackage(product);
            addToCart.setSystemUser(systemUser);
            addToCart.setPersonCount(addToCartRequestDTO.getPersonCount());
//            addToCart.setScheduledTime(addToCartRequestDTO.getScheduledTime());

            addToCartRepository.save(addToCart);
            return responseGenerator.generateSuccessResponse(addToCartRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.ADD_TO_CART_SUCCESS, locale, new Object[] {addToCartRequestDTO.getPackageId()});
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
    public Object getToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        try {

            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(addToCartRequestDTO.getUserName(), Status.active))
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(MessageConstant
                            .USER_NOT_FOUND, null, locale)));


            List<AddToCart> addToCart = Optional.ofNullable(addToCartRepository.findAllBySystemUserAndStatusNot(systemUser, Status.deleted))
                    .orElse(null);

            List<AddToCartResponseDTO> collect = addToCart.stream()
                    .map(EntityToDtoMapper::mapAddToCart)
                    .collect(Collectors.toList());

            return new DataTableDTO<>(0L, (long) addToCart.size(), collect, null);

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
    public ResponseEntity<Object> removeToCart(Long id, Locale locale) throws Exception {
        try {
            AddToCart addToCart = Optional.ofNullable(addToCartRepository.findById(id)).get()
                    .orElse(null);

            if (Objects.isNull(addToCart)) {
                return responseGenerator
                        .generateErrorResponse(id, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND, MessageConstant.ADD_TO_CART_NOT_FOUND, new Object[]{id},
                                locale);
            }

            addToCartRepository.delete(addToCart);
            return responseGenerator.generateSuccessResponse(id, HttpStatus.OK,
                    ResponseCode.DELETE_SUCCESS, MessageConstant.ADD_TO_CART_REMOVED, locale, new Object[]{id});
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Object getCheckoutToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        try {

            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(addToCartRequestDTO.getUserName(), Status.active))
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(MessageConstant
                            .USER_NOT_FOUND, null, locale)));


            List<AddToCart> addToCart = Optional.ofNullable(addToCartRepository.findAllBySystemUserAndStatusNot(systemUser, Status.deleted))
                    .orElse(null);

            List<AddToCartResponseDTO> collect = addToCart.stream()
                    .map(cart -> EntityToDtoMapper.maCartCheckout(cart, addToCartRequestDTO.isCheckout()))
                    .collect(Collectors.toList());

            CheckoutResponseDTO checkoutResponseDTO = new CheckoutResponseDTO();
            checkoutResponseDTO.setUserName(systemUser.getUsername());
            checkoutResponseDTO.setFullName(systemUser.getFullName());
            checkoutResponseDTO.setAddress(systemUser.getAddress());
            checkoutResponseDTO.setCity(systemUser.getCity());
            checkoutResponseDTO.setUserId(systemUser.getId());
            checkoutResponseDTO.setEmail(systemUser.getEmail());

            BigDecimal subTot = BigDecimal.ZERO;
            for (AddToCartResponseDTO addToCartResponseDTO : collect) {
                subTot = subTot.add(addToCartResponseDTO.getPrice());
            }
            checkoutResponseDTO.setSubTotal(subTot);
            checkoutResponseDTO.setDeliveryPrice(BigDecimal.valueOf(699));
            checkoutResponseDTO.setTotal(subTot.add(checkoutResponseDTO.getDeliveryPrice()));

            checkoutResponseDTO.setCartList(collect);
//            return new DataTableDTO<>(0L, (long) collect.size(), checkoutResponseDTO, null);

            return responseGenerator.generateSuccessResponse(addToCartRequestDTO.getUserName(), HttpStatus.OK,
                    ResponseCode.GET_SUCCESS, MessageConstant.GET_TO_CART_SUCCESS, locale, checkoutResponseDTO);
        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
