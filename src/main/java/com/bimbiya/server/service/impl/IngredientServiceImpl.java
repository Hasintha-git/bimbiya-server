package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import com.bimbiya.server.dto.response.IngredientsResponseDTO;
import com.bimbiya.server.entity.Ingredients;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.IngredientsRepository;
import com.bimbiya.server.repository.specifications.IngredientSpecification;
import com.bimbiya.server.service.IngredientService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
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
public class IngredientServiceImpl implements IngredientService {

    private IngredientsRepository ingredientsRepository;
    private IngredientSpecification ingredientSpecification;
    private ModelMapper modelMapper;
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
    @Transactional
    public Object getIngredientFilterList(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(ingredientsRequestDTO.getSortColumn()) && Objects.nonNull(ingredientsRequestDTO.getSortDirection()) &&
                    !ingredientsRequestDTO.getSortColumn().isEmpty() && !ingredientsRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        ingredientsRequestDTO.getPageNumber(), ingredientsRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(ingredientsRequestDTO.getSortDirection()), ingredientsRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(ingredientsRequestDTO.getPageNumber(), ingredientsRequestDTO.getPageSize());
            }

            List<Ingredients> ingredientsList = ((Objects.isNull(ingredientsRequestDTO.getIngredientSearchDTO())) ? ingredientsRepository.findAll
                    (ingredientSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    ingredientsRepository.findAll(ingredientSpecification.generateSearchCriteria(ingredientsRequestDTO.getIngredientSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(ingredientsRequestDTO.getIngredientSearchDTO())) ? ingredientsRepository.count
                    (ingredientSpecification.generateSearchCriteria(Status.deleted)) :
                    ingredientsRepository.count(ingredientSpecification.generateSearchCriteria(ingredientsRequestDTO.getIngredientSearchDTO()));

            List<IngredientsResponseDTO> collect = ingredientsList.stream()
                    .map(ing -> modelMapper.map(ing, IngredientsResponseDTO.class))
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
    public ResponseEntity<Object> findIngredientById(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        try {
            Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(ingredientsRequestDTO.getId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(ingredients)) {
                return responseGenerator.generateErrorResponse(ingredientsRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.INGREDIENT_NOT_FOUND, new
                                Object[]{ingredientsRequestDTO.getId()},locale);
            }

            IngredientsResponseDTO ingredientsResponseDTO = EntityToDtoMapper.mapIngredient(ingredients);

            return responseGenerator
                    .generateSuccessResponse(ingredientsRequestDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.INGREDIENT_SUCCESSFULLY_FIND, locale, ingredientsResponseDTO);
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
    public ResponseEntity<Object> saveIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        try{
            Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsNameAndStatusCodeNot(ingredientsRequestDTO.getIngredientsName(), Status.deleted))
                    .orElse(null);
            
            if (Objects.nonNull(ingredients)) {
                return responseGenerator
                        .generateErrorResponse(ingredientsRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.INGREDIENT_NAME_ALREADY_EXIST, new Object[] {ingredientsRequestDTO.getIngredientsName()},
                                locale);
            }

            ingredients= new Ingredients();
            DtoToEntityMapper.mapIngredient(ingredients,ingredientsRequestDTO, true);
            ingredients.setCreatedTime(new Date());
            ingredients.setLastUpdatedTime(new Date());

            ingredientsRepository.save(ingredients);
            return responseGenerator.generateSuccessResponse(ingredientsRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.INGREDIENT_SUCCESSFULLY_SAVE, locale, new Object[] {ingredientsRequestDTO.getIngredientsName()});
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
    public ResponseEntity<Object> editIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        try{
            Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(ingredientsRequestDTO.getId(), Status.deleted))
                    .orElse(null);

            if (Objects.isNull(ingredients)) {
                return responseGenerator
                        .generateErrorResponse(ingredientsRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.INGREDIENT_NOT_FOUND, new Object[] {ingredientsRequestDTO.getIngredientsName()},
                                locale);
            }

            DtoToEntityMapper.mapIngredient(ingredients,ingredientsRequestDTO, false);
            ingredients.setLastUpdatedTime(new Date());

            ingredientsRepository.save(ingredients);
            return responseGenerator.generateSuccessResponse(ingredientsRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.INGREDIENT_SUCCESSFULLY_UPDATE, locale, new Object[] {ingredientsRequestDTO.getIngredientsName()});
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
    public ResponseEntity<Object> deleteIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        try{
            Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(ingredientsRequestDTO.getId(), Status.deleted))
                    .orElse(null);

            if (Objects.isNull(ingredients)) {
                return responseGenerator
                        .generateErrorResponse(ingredientsRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.INGREDIENT_NOT_FOUND, new Object[] {ingredientsRequestDTO.getIngredientsName()},
                                locale);
            }

            ingredients.setStatusCode(Status.deleted);
            ingredients.setLastUpdatedTime(new Date());

            ingredientsRepository.save(ingredients);
            return responseGenerator.generateSuccessResponse(ingredientsRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.INGREDIENT_SUCCESSFULLY_DELETE, locale, new Object[] {ingredientsRequestDTO.getIngredientsName()});
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
