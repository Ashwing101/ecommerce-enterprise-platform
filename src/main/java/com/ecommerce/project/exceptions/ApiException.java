package com.ecommerce.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ApiException extends  RuntimeException {

    private  static  final  long  serialVersionID = 1l;


    public ApiException(String message) {
        super(message);
    }



}
