package com.bimbiya.server.annotation.advisor;

import com.bimbiya.server.dto.response.ErrorResponse;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.util.ResponseCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerController
{
    @Autowired
    private ResponseGenerator responseGenerator;


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                HttpServletRequest request, Locale locale)
    {
        log.error(ex);
        ErrorResponse errorResponse = responseGenerator.generateExceptionErrorResponse(request, ex);
        errorResponse.setErrorCode(ResponseCode.GET_SUCCESS);
        errorResponse.setErrorDescription(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
