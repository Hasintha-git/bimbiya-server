package com.bimbiya.server.mapper;

import com.bimbiya.server.dto.ResponseDTO;
import com.bimbiya.server.dto.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@Log4j2
public class ResponseGenerator {
    private ModelMapper modelMapper;

    private MessageSource messageSource;

    @Value("${error.component.name}")
    private String errorComponent;


    @Autowired
    public ResponseGenerator(ModelMapper modelMapper, MessageSource messageSource) {
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    public ResponseEntity<Object> generateSuccessResponse(Object requestBean, HttpStatus httpStatus,
                                                          String responseCode, String responseDescription, Locale language,
                                                          Object dataObject) {

        //Setting the common fields of response object
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        ResponseDTO responseDTO = modelMapper.map(requestBean, ResponseDTO.class);
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setResponseCode(responseCode);
        responseDTO.setResponseDescription(messageSource.getMessage(responseDescription, null, language));
        responseDTO.setData(dataObject);
        return ResponseEntity.status(httpStatus).body(responseDTO);
    }

    public ResponseEntity<Object> generateSuccessResponse(Object requestBean, HttpStatus httpStatus,
                                                          String responseCode, String responseDescription, Locale language,
                                                          Object dataObject, Long fullCount) {

        //Setting the common fields of response object
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        ResponseDTO responseDTO = modelMapper.map(requestBean, ResponseDTO.class);
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setResponseCode(responseCode);
        responseDTO.setResponseDescription(messageSource.getMessage(responseDescription, null, language));
        responseDTO.setData(dataObject);
        responseDTO.setFullCount(fullCount);
        return ResponseEntity.status(httpStatus).body(responseDTO);
    }

    public ResponseEntity<Object> generateSuccessResponse(HttpStatus httpStatus,
                                                          String responseCode, String responseDescription, Locale language,
                                                          Object dataObject) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseCode(responseCode);
        responseDTO.setResponseDescription(messageSource.getMessage(responseDescription, null, language));
        responseDTO.setData(dataObject);
        return ResponseEntity.status(httpStatus).body(responseDTO);
    }

    public ResponseEntity<Object> generateSuccessResponse(Object requestBean, HttpStatus httpStatus,
                                                          String responseCode, String responseDescription, Locale language) throws Exception {
        //Setting the common fields of response object
//        ResponseDTO responseDTO = modelMapper.map(requestBean, ResponseDTO.class);

        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setResponseCode(responseCode);
        responseDTO.setResponseDescription(messageSource.getMessage(responseDescription, null, language));
        return ResponseEntity.status(httpStatus).body(responseDTO);
    }


    public ResponseEntity<Object> generateErrorResponse(Object requestBean, HttpStatus httpStatus, String errorCode,
                                                        String errorDescriptionPropertyName, Locale language) {

        //Setting the common fields of response object
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorDescription(messageSource.getMessage(errorDescriptionPropertyName, null, language));
        errorResponse.setErrorComponent(errorComponent);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> generateErrorResponse(HttpStatus httpStatus, String errorCode,
                                                        String errorDescriptionPropertyName, Locale language) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorDescription(messageSource.getMessage(errorDescriptionPropertyName, null, language));
        errorResponse.setErrorComponent(errorComponent);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> generateErrorResponse(Object requestBean, HttpStatus httpStatus, String errorCode,
                                                        String errorMessage) {

        //Setting the common fields of response object
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ErrorResponse errorResponse = modelMapper.map(requestBean, ErrorResponse.class);

        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorDescription(errorMessage);
        errorResponse.setErrorComponent(errorComponent);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> generateErrorResponse(Object requestBean, HttpStatus httpStatus, String errorCode,
                                                        String errorDescriptionPropertyName, Object[] params, Locale language) {

        //Setting the common fields of response object
        ErrorResponse errorResponse = requestBean != null ? modelMapper.map(requestBean, ErrorResponse.class) : new ErrorResponse();
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorDescription(messageSource.getMessage(errorDescriptionPropertyName, params, language));
        errorResponse.setErrorComponent(errorComponent);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> generateNoContentResponse() {
        return ResponseEntity.noContent().build();
    }

    public ErrorResponse generateExceptionErrorResponse(HttpServletRequest request, Exception ex) {
        log.error("Exception Message - {} ", ex.getLocalizedMessage());
        log.error("Exception - {} ", ex);
        ErrorResponse errorResponse = null;
        String req;
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader((request.getInputStream()), StandardCharsets.UTF_8))) {
            req = reader.lines().collect(Collectors.joining());

        } catch (Exception e) {
            log.error(e);
        }
        return errorResponse;
    }

}
