package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.EmailSendDTO;
import com.bimbiya.server.dto.request.OrderRequestDTO;
import com.bimbiya.server.dto.request.ProductRequestDTO;
import com.bimbiya.server.dto.response.OrderDetailsResponseDTO;
import com.bimbiya.server.dto.response.OrderResponseDTO;
import com.bimbiya.server.entity.AddToCart;
import com.bimbiya.server.entity.Order;
import com.bimbiya.server.entity.OrderDetail;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.*;
import com.bimbiya.server.repository.specifications.OrderSpecification;
import com.bimbiya.server.service.NotificationService;
import com.bimbiya.server.service.OrderService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.ClientOrderStatus;
import com.bimbiya.server.util.enums.ClientTimeSlot;
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
    private NotificationService notificationService;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private AddToCartRepository addToCartRepository;
    private OrderSpecification orderSpecification;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientOrderStatus.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());
            List<SimpleBaseDTO> timeSlot = Stream.of(ClientTimeSlot.values()).map(slotEnum -> new SimpleBaseDTO(slotEnum.getTimeValue(), slotEnum.getStringValue())).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("timeSlot", timeSlot);

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
            orderResponseDTO.setScheduleTime(ClientTimeSlot.getEnum(String.valueOf(order.getScheduledTime())).getStringValue());

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
                                Object[]{orderRequestDTO.getUserId()}, locale);
            }

            List<AddToCart> cartList = new ArrayList<>();
            for (ProductRequestDTO productRequestDTO : orderRequestDTO.getProduct()) {
                AddToCart addToCart = Optional.ofNullable(addToCartRepository.findAddToCartByBpackage_PackageId(productRequestDTO.getPackageId())).orElse(
                        null
                );

                if (Objects.isNull(addToCart)) {
                    return responseGenerator.generateErrorResponse(orderRequestDTO, HttpStatus.NOT_FOUND,
                            ResponseCode.NOT_FOUND, MessageConstant.BYTEPACKAGE_NOT_FOUND, new
                                    Object[]{productRequestDTO.getPackageId()}, locale);
                }
                cartList.add(addToCart);
            }


            // save order
            Order order = new Order();
            order.setSystemUser(systemUser);
            order.setTotalAmount(orderRequestDTO.getTotal());
            order.setDeliveryCharge(orderRequestDTO.getDeliveryPrice());
            order.setStatus(Status.pending);
            order.setScheduledTime(orderRequestDTO.getScheduledTime());

            DtoToEntityMapper.mapOrder(order, orderRequestDTO);

            Order savedOrder = orderRepository.save(order);
            List<OrderDetail> savedOrderDetail = new ArrayList<>();
            for (AddToCart addToCart : cartList) {
                // save order details
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(addToCart.getBpackage());
                orderDetail.setUnitPrice(addToCart.getPrice());
                orderDetail.setPersonCount(addToCart.getPersonCount());
                orderDetail.setOrder(order);

                savedOrderDetail.add(orderDetailRepository.save(orderDetail));
            }

            if (Objects.nonNull(savedOrderDetail)) {
                EmailSendDTO emailSendDTO = new EmailSendDTO();
                emailSendDTO.setToEmail("bimbiyasl@gmail.com");
                emailSendDTO.setSubject("New Order Received With Order Id:"+order.getId());
                emailSendDTO.setBody(orderBodySet(savedOrder, savedOrderDetail));
                notificationService.orderEmailSent(emailSendDTO,locale);
                emailSendDTO.setToEmail(order.getSystemUser().getEmail());
                emailSendDTO.setSubject("Bimbiya Order Details With Order Id:"+order.getId());
                notificationService.orderEmailSent(emailSendDTO,locale);
            }

            addToCartRepository.deleteAll(cartList);

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

    public String orderBodySet(Order order, List<OrderDetail> orderDetails) {
        StringBuilder body = new StringBuilder();
        body.append("--------------------------------------------------\n")
                .append("                    INVOICE\n")
                .append("--------------------------------------------------\n\n")
                .append("Order ID: ").append(order.getId()).append("\n")
                .append("Order Date: ").append(order.getOrderDate()).append("\n")
                .append("Total Amount: ").append(order.getTotalAmount()).append("\n")
                .append("Delivery Charge: ").append(order.getDeliveryCharge()).append("\n")
                .append("Status: ").append(order.getStatus()).append("\n\n")
                .append("--------------------------------------------------\n")
                .append("                    DETAILS\n")
                .append("--------------------------------------------------\n")
                .append("Product                 |   Unit Price   |  Person Count\n");

        // Append order details
        for (OrderDetail detail : orderDetails) {
            body.append(String.format("%-20s| %-14s| %-12s\n",
                    detail.getProduct().getProductName(),
                    detail.getUnitPrice(),
                    detail.getPersonCount()));
        }

        body.append("\n--------------------------------------------------\n")
                .append("Thank you for your order!\n")
                .append("Best regards,\n")
                .append("Bimbiya Team\n")
                .append("--------------------------------------------------");

        return body.toString();
    }

}
