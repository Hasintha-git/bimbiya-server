package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.OrderRequestDTO;
import com.bimbiya.server.dto.response.OrderDetailsResponseDTO;
import com.bimbiya.server.dto.response.OrderResponseDTO;
import com.bimbiya.server.entity.Order;
import com.bimbiya.server.entity.OrderDetail;
import com.bimbiya.server.entity.Product;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.OrderDetailRepository;
import com.bimbiya.server.repository.OrderRepository;
import com.bimbiya.server.repository.ProductRepository;
import com.bimbiya.server.repository.UserRepository;
import com.bimbiya.server.repository.specifications.OrderSpecification;
import com.bimbiya.server.service.OrderService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.ClientOrderStatus;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

/**
 * @author Hasintha_S
 * @date 12/17/2023.
 */

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderSpecification orderSpecification;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientOrderStatus.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    public Object getOrderFilterList(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(orderRequestDTO.getSortColumn()) && Objects.nonNull(orderRequestDTO.getSortDirection()) &&
                    !orderRequestDTO.getSortColumn().isEmpty() && !orderRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        orderRequestDTO.getPageNumber(), orderRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(orderRequestDTO.getSortDirection()), orderRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(orderRequestDTO.getPageNumber(), orderRequestDTO.getPageSize());
            }

            Page<Order> orders =orderRepository.findAll(orderSpecification.generateSearchCriteria(orderRequestDTO.getOrderFilterDTO()), pageRequest);

            Long fullCount = orderRepository.count(orderSpecification.generateSearchCriteria(orderRequestDTO.getOrderFilterDTO()));

            List<OrderResponseDTO> collect = orders.stream()
                    .map(EntityToDtoMapper::mapOrder)
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
    public ResponseEntity<Object> findOrderById(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        try {
            Order order = orderRepository.findById(orderRequestDTO.getOrderId()).orElse(
                    null
            );

            if (Objects.isNull(order)) {
                return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.ORDER_NOT_FOUND, new
                                Object[]{orderRequestDTO.getOrderId()},locale);
            }

            List<OrderDetail> allByOrder = orderDetailRepository.findAllByOrder(order);

            if (Objects.isNull(allByOrder)) {
                return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.ORDER_DETAILS_NOT_FOUND, new
                                Object[]{orderRequestDTO.getOrderId()},locale);
            }

            OrderResponseDTO orderResponseDTO = EntityToDtoMapper.mapOrder(order);
            List<OrderDetailsResponseDTO> orderDetails= new ArrayList<>();
            for (OrderDetail orderDetail : allByOrder) {
                OrderDetailsResponseDTO responseDTO = EntityToDtoMapper.mapOrderDetails(orderDetail);
                orderDetails.add(responseDTO);
            }

            orderResponseDTO.setOrderDetails(orderDetails);

            orderResponseDTO.setUsername(order.getSystemUser().getUsername());
            orderResponseDTO.setFullName(order.getSystemUser().getFullName());
            orderResponseDTO.setEmail(order.getSystemUser().getEmail());
            orderResponseDTO.setMobileNo(order.getSystemUser().getMobileNo());
            orderResponseDTO.setAddress(order.getSystemUser().getAddress());
            orderResponseDTO.setCity(order.getSystemUser().getCity());

            log.info("ORDER RESPONSE >>>>>>>>>"+orderResponseDTO);

            return responseGenerator
                    .generateSuccessResponse(orderRequestDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.ORDER_FIND, locale, orderResponseDTO);
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
    public ResponseEntity<Object> orderProcessingUpdate(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        try {
            Order order = orderRepository.findById(orderRequestDTO.getOrderId()).orElse(
                    null
            );

            if (Objects.isNull(order)) {
                return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.ORDER_NOT_FOUND, new
                                Object[]{orderRequestDTO.getOrderId()},locale);
            }

            order.setStatus(Status.valueOf(orderRequestDTO.getStatus()));
            order.setLastUpdatedTime(new Date());
            order.setLastUpdatedUser(orderRequestDTO.getActiveUser());
            orderRepository.save(order);

            return responseGenerator
                    .generateSuccessResponse(orderRequestDTO, HttpStatus.OK, ResponseCode.UPDATE_SUCCESS,
                            MessageConstant.ORDER_UPDATED, locale, null);
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
    public ResponseEntity<Object> newOrder(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatus(orderRequestDTO.getUserId(), Status.active)).orElse(
                    null
            );

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.GET_SUCCESS, MessageConstant.USER_NOT_FOUND, new
                                Object[]{orderRequestDTO.getUserId()},locale);
            }

            Product product = Optional.ofNullable(productRepository.findByPackageIdAndStatus(orderRequestDTO.getProductId(), Status.active)).orElse(
                    null
            );

            if (Objects.isNull(product)) {
                return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.BYTEPACKAGE_NOT_FOUND, new
                                Object[]{orderRequestDTO.getProductId()},locale);
            }


            // save order
            Order order = new Order();
            order.setSystemUser(systemUser);
            order.setStatus(Status.pending);
            order.setPersonCount(orderRequestDTO.getPersonCount());
            order.setScheduledTime(orderRequestDTO.getScheduledTime());

            DtoToEntityMapper.mapOrder(order,orderRequestDTO);

            orderRepository.save(order);

            // save order details
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);

            DtoToEntityMapper.mapOrderDetails(orderDetail,orderRequestDTO);

            orderDetailRepository.save(orderDetail);

            return responseGenerator
                    .generateSuccessResponse(orderRequestDTO, HttpStatus.OK, ResponseCode.SAVED_SUCCESS,
                            MessageConstant.ORDER_PLACED, locale, null);

        } catch (EntityNotFoundException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
