package com.ecommerce.project.exceptions;


import com.ecommerce.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> myMethodArgumnetNotValidException(MethodArgumentNotValidException exception){
            Map<String, String> response = new HashMap<>();
            exception.getBindingResult().getAllErrors().forEach(err -> {
                String fieldName  = ((FieldError)err).getField();
                String message = err.getDefaultMessage(); // We can have default message also on the basis of
                // fieldName, This provides api response in a consistent
                response.put(fieldName, message);

            });
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException ex){
            String message = ex.getMessage();
            APIResponse apiResponse = new APIResponse(message,false);


        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

         @ExceptionHandler(ApiException.class)
         public ResponseEntity<APIResponse> myAPIException(ApiException ex){

        String message = ex.getMessage();

             APIResponse apiResponse = new APIResponse(message,false);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


}
