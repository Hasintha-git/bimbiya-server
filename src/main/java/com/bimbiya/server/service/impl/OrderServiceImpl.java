package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import com.bimbiya.server.dto.request.OrderRequestDTO;
import com.bimbiya.server.dto.response.OrderResponseDTO;
import com.bimbiya.server.entity.Order;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.OrderRepository;
import com.bimbiya.server.repository.specifications.OrderSpecification;
import com.bimbiya.server.service.OrderService;
import com.bimbiya.server.util.enums.ClientStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private OrderSpecification orderSpecification;
    private ResponseGenerator responseGenerator;

    @Override
    public Object getReferenceData() {
        try {
            Map<String, Object> refData = new HashMap<>();

            //get status
            List<SimpleBaseDTO> defaultStatus = Stream.of(ClientStatusEnum.values()).map(statusEnum -> new SimpleBaseDTO(statusEnum.getCode(), statusEnum.getDescription())).collect(Collectors.toList());

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
    public ResponseEntity<Object> findOrderById(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Object> orderProcessingUpdate(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        return null;
    }
}
