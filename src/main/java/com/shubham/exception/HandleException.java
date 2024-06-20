package com.shubham.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.text.Keymap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandleException {

@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String> > handleException(MethodArgumentNotValidException ex) {
       Map<String, String> errorMessage= new HashMap<>();
       ex.getBindingResult().getFieldErrors().forEach(error->{
           errorMessage.put(error.getField(), error.getDefaultMessage());
       });
       return new ResponseEntity<>(errorMessage , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> dataNotFoundException(ResourceNotFoundException ex){
        Map<String,String> errorMessage=new HashMap<>();
        errorMessage.put("Message", ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
