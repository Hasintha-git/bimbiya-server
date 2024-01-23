package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.DataTableDTO;
import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.request.ProductRequestDTO;
import com.bimbiya.server.dto.response.ProductResponseDTO;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.mapper.DtoToEntityMapper;
import com.bimbiya.server.mapper.EntityToDtoMapper;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.ProductCategoryRepository;
import com.bimbiya.server.repository.ProductIngredientsRepository;
import com.bimbiya.server.repository.ProductRepository;
import com.bimbiya.server.repository.IngredientsRepository;
import com.bimbiya.server.repository.specifications.BytePackageSpecification;
import com.bimbiya.server.service.ProductService;
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
import org.springframework.data.domain.Pageable;
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
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private ProductIngredientsRepository productIngredientsRepository;
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

            //get product category list
            List<ProductCategory> productCategories = productCategoryRepository.findAllByStatus(Status.active);
            List<SimpleBaseDTO> productCatList = productCategories.stream().map(productCategory -> {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO();
                return EntityToDtoMapper.mapProductCategoryDropdown(simpleBaseDTO, productCategory);
            }).collect(Collectors.toList());

            //set data
            refData.put("statusList", defaultStatus);
            refData.put("portionList", portionStatus);
            refData.put("ingredientsList", ingredientsList);
            refData.put("productCatList", productCatList);

            return refData;

        } catch (Exception e) {
            log.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Object getProductFilterList(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(productRequestDTO.getSortColumn()) && Objects.nonNull(productRequestDTO.getSortDirection()) &&
                    !productRequestDTO.getSortColumn().isEmpty() && !productRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        productRequestDTO.getPageNumber(), productRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(productRequestDTO.getSortDirection()), productRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(productRequestDTO.getPageNumber(), productRequestDTO.getPageSize());
            }

            List<Product> productList = ((Objects.isNull(productRequestDTO.getBytePackageSearchDTO())) ? productRepository.findAll
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    productRepository.findAll(bytePackageSpecification.generateSearchCriteria(productRequestDTO.getBytePackageSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(productRequestDTO.getBytePackageSearchDTO())) ? productRepository.count
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted)) :
            productRepository.count(bytePackageSpecification.generateSearchCriteria(productRequestDTO.getBytePackageSearchDTO()));

            List<ProductResponseDTO> collect = productList.stream()
                    .map(product -> EntityToDtoMapper.mapBytePackage(product, false))
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
    public Object getProductFilterListClient(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try {
            PageRequest pageRequest;

            if (Objects.nonNull(productRequestDTO.getSortColumn()) && Objects.nonNull(productRequestDTO.getSortDirection()) &&
                    !productRequestDTO.getSortColumn().isEmpty() && !productRequestDTO.getSortDirection().isEmpty()) {
                pageRequest = PageRequest.of(
                        productRequestDTO.getPageNumber(), productRequestDTO.getPageSize(),
                        Sort.by(Sort.Direction.valueOf(productRequestDTO.getSortDirection()), productRequestDTO.getSortColumn())
                );
            }else{
                pageRequest = PageRequest.of(productRequestDTO.getPageNumber(), productRequestDTO.getPageSize());
            }

            List<Product> productList = ((Objects.isNull(productRequestDTO.getBytePackageSearchDTO())) ? productRepository.findAll
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted), pageRequest) :
                    productRepository.findAll(bytePackageSpecification.generateSearchCriteria(productRequestDTO.getBytePackageSearchDTO()), pageRequest))
                    .getContent();

            Long fullCount = (Objects.isNull(productRequestDTO.getBytePackageSearchDTO())) ? productRepository.count
                    (bytePackageSpecification.generateSearchCriteria(Status.deleted)) :
                    productRepository.count(bytePackageSpecification.generateSearchCriteria(productRequestDTO.getBytePackageSearchDTO()));

            List<ProductResponseDTO> collect = productList.stream()
                    .map(product -> {
                        ProductResponseDTO productResponseDTO = EntityToDtoMapper.mapBytePackage(product, true);
                        return mapIngredientList(product, productResponseDTO);
                    })
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

    public ProductResponseDTO mapIngredientList(Product product, ProductResponseDTO productResponseDTO) {
        List<BytePackageIngredients> bytePackageIngredients = Optional.ofNullable(productIngredientsRepository.findAllByProduct(product)).orElse(null);

        if (Objects.nonNull(bytePackageIngredients)) {
            List<SimpleBaseDTO> byteIngredientsResponseDTOList= new ArrayList<>();
            for (BytePackageIngredients bytePackageIngredient : bytePackageIngredients) {
                SimpleBaseDTO simpleBaseDTO = new SimpleBaseDTO(bytePackageIngredient.getIngredients().getIngredientsId(), bytePackageIngredient.getIngredients().getIngredientsName());
                byteIngredientsResponseDTOList.add(simpleBaseDTO);
            }

            productResponseDTO.setIngredients(byteIngredientsResponseDTOList);

        }
        return productResponseDTO;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> findProductById(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try {
            Product product = Optional.ofNullable(productRepository.findByPackageIdAndStatusNot(productRequestDTO.getPackageId(), Status.deleted)).orElse(
                    null
            );

            if (Objects.isNull(product)) {
                return responseGenerator.generateErrorResponse(productRequestDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.NOT_FOUND, MessageConstant.BYTEPACKAGE_NOT_FOUND, new
                                Object[]{productRequestDTO.getPackageId()},locale);
            }

            ProductResponseDTO productResponseDTO = EntityToDtoMapper.mapBytePackage(product, true);


            List<BytePackageIngredients> bytePackageIngredients = Optional.ofNullable(productIngredientsRepository.findAllByProduct(product)).orElse(null);

            if (Objects.nonNull(bytePackageIngredients)) {
                List<Long> byteIngredientsResponseDTOList= new ArrayList<>();
                for (BytePackageIngredients bytePackageIngredient : bytePackageIngredients) {
                    byteIngredientsResponseDTOList.add(bytePackageIngredient.getIngredients().getIngredientsId());
                }

                productResponseDTO.setIngredientList(byteIngredientsResponseDTOList);

            }

            return responseGenerator
                    .generateSuccessResponse(productRequestDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.BYTEPACKAGE_SUCCESSFULLY_FIND, locale, productResponseDTO);
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
    public Object trendingPackagesList(String username, Locale locale) throws Exception {
        try {
            int pageSize = 10;
            int pageNumber = 0;

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            List<Product> products = productRepository.findByStatus(Status.active, pageable);


            List<ProductResponseDTO> collect = products.stream()
                    .map(product -> {
                        ProductResponseDTO productResponseDTO = EntityToDtoMapper.mapBytePackage(product, true);
                        return mapIngredientList(product, productResponseDTO);
                    })
                    .collect(Collectors.toList());

            return new DataTableDTO<>(0L, (long) collect.size(), collect, null);
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
    public ResponseEntity<Object> saveProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try{
            Product product = Optional.ofNullable(productRepository.findByProductNameAndStatusNot(productRequestDTO.getMealName(), Status.deleted))
                    .orElse(null);

            if (Objects.nonNull(product)) {
                return responseGenerator
                        .generateErrorResponse(productRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.PRODUCT_MEALNAME_ALREADY_EXIST, new Object[] {productRequestDTO.getMealName()},
                                locale);
            }

            product = new Product();
            DtoToEntityMapper.mapBytePackage(product, productRequestDTO, true);
            product.setCreatedTime(new Date());
            product.setLastUpdatedTime(new Date());

            ProductCategory productCategory = Optional.ofNullable(productCategoryRepository.findByCodeAndStatus(productRequestDTO.getProductCategory(), Status.active))
                    .orElse(null);

            if (Objects.isNull(product)) {
                return responseGenerator
                        .generateErrorResponse(productRequestDTO, HttpStatus.NOT_FOUND,
                                ResponseCode.NOT_FOUND ,  MessageConstant.PRODUCT_NOT_FOUND, new Object[] {productRequestDTO.getMealName()},
                                locale);
            }

            product.setProductCategory(productCategory);

            productRepository.save(product);


            if (Objects.nonNull(productRequestDTO.getIngredientList())) {
                for (Long iId : productRequestDTO.getIngredientList()) {
                    Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(iId, Status.deleted)).orElse(null);
                    BytePackageIngredients bytePackageIngredients = new BytePackageIngredients();
                    BytePackageIngredientsId bytePackageIngredientsId = new BytePackageIngredientsId();
                    bytePackageIngredients.setProduct(product);
                    bytePackageIngredients.setIngredients(ingredients);

                    bytePackageIngredientsId.setBytePackage(product.getPackageId());
                    bytePackageIngredientsId.setIngredients(iId);
                    bytePackageIngredients.setId(bytePackageIngredientsId);
                    productIngredientsRepository.save(bytePackageIngredients);
                }
            }

            return responseGenerator.generateSuccessResponse(productRequestDTO, HttpStatus.OK,
                    ResponseCode.SAVED_SUCCESS, MessageConstant.INGREDIENT_SUCCESSFULLY_SAVE, locale, new Object[] {productRequestDTO.getMealName()});
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
    public ResponseEntity<Object> editProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try{
            Product product = Optional.ofNullable(productRepository.findByPackageIdAndStatusNot(productRequestDTO.getPackageId(), Status.deleted))
                    .orElse(null);

            if (Objects.isNull(product)) {
                return responseGenerator
                        .generateErrorResponse(productRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.BYTEPACKAGE_NOT_FOUND, new Object[] {productRequestDTO.getMealName()},
                                locale);
            }


            Product uniPackage = productRepository.findByProductNameAndStatusNotAndPackageIdNot(productRequestDTO.getMealName(), Status.deleted, productRequestDTO.getPackageId())
                    .orElse(null);

            if (Objects.nonNull(uniPackage)) {
                return responseGenerator
                        .generateErrorResponse(productRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.ALREADY_EXIST ,  MessageConstant.PRODUCT_MEALNAME_ALREADY_EXIST, new Object[] {productRequestDTO.getMealName()},
                                locale);
            }


            DtoToEntityMapper.mapBytePackage(product, productRequestDTO, false);
            product.setLastUpdatedTime(new Date());

            productRepository.save(product);

            productIngredientsRepository.deleteAllByProduct(product);

            if (Objects.nonNull(productRequestDTO.getIngredientList())) {
                for (Long iId : productRequestDTO.getIngredientList()) {
                    Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findIngredientsByIngredientsIdAndStatusCodeNot(iId, Status.deleted)).orElse(null);
                    BytePackageIngredients bytePackageIngredients = new BytePackageIngredients();
                    BytePackageIngredientsId bytePackageIngredientsId = new BytePackageIngredientsId();
                    bytePackageIngredients.setProduct(product);
                    bytePackageIngredients.setIngredients(ingredients);

                    bytePackageIngredientsId.setBytePackage(product.getPackageId());
                    bytePackageIngredientsId.setIngredients(iId);
                    bytePackageIngredients.setId(bytePackageIngredientsId);
                    productIngredientsRepository.save(bytePackageIngredients);
                }
            }

            return responseGenerator.generateSuccessResponse(productRequestDTO, HttpStatus.OK,
                    ResponseCode.UPDATE_SUCCESS, MessageConstant.BYTEPACKAGE_SUCCESSFULLY_UPDATE, locale, new Object[] {productRequestDTO.getMealName()});
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
    public ResponseEntity<Object> deleteProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        try{
            Product product = Optional.ofNullable(productRepository.findByPackageIdAndStatusNot(productRequestDTO.getPackageId(), Status.deleted))
                    .orElse(null);

            if (Objects.isNull(product)) {
                return responseGenerator
                        .generateErrorResponse(productRequestDTO, HttpStatus.CONFLICT,
                                ResponseCode.NOT_FOUND ,  MessageConstant.BYTEPACKAGE_NOT_FOUND, new Object[] {productRequestDTO.getMealName()},
                                locale);
            }

            productIngredientsRepository.deleteAllByProduct(product);
            product.setStatus(Status.deleted);
            productRepository.save(product);


            return responseGenerator.generateSuccessResponse(productRequestDTO, HttpStatus.OK,
                    ResponseCode.DELETE_SUCCESS, MessageConstant.BYTEPACKAGE_SUCCESSFULLY_DELETE, locale, new Object[] {productRequestDTO.getMealName()});
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
