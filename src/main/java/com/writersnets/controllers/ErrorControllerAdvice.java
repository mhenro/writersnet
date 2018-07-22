package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.models.exceptions.*;
import com.writersnets.utils.ControllerHelper;
import org.hibernate.StaleStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.io.IOException;

@ControllerAdvice
public class ErrorControllerAdvice {
    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Response<String>> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Bad credentials"), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<Response<String>> objectAlreadyExist(ObjectAlreadyExistException e) {
        return Response.createResponseEntity(2, ControllerHelper.getErrorOrDefaultMessage(e, "Object already exist"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Response<String>> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(3, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Response<String>> emailException(MessagingException e) {
        return Response.createResponseEntity(4, "Problem with sending email. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IsNotPremiumUserException.class)
    public ResponseEntity<?> isNotPremiumUser(IsNotPremiumUserException e) {
        return Response.createResponseEntity(6,ControllerHelper.getErrorOrDefaultMessage(e, "Only a premium user can do this"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return Response.createResponseEntity(7, "Problem with server's file system. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<?> notEnoughMoney(NotEnoughMoneyException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Not enough money for this operation"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Wrong request data"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TextConvertingException.class)
    public ResponseEntity<?> textConvertingError(TextConvertingException e) {
        return Response.createResponseEntity(7, "An error occurred while converting the book text. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> permissionDenied(PermissionDeniedException e) {
        return Response.createResponseEntity(9, ControllerHelper.getErrorOrDefaultMessage(e, "Permission denied"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(StaleStateException.class)
    public ResponseEntity<?> optimisticLockCollision(StaleStateException e) {
        return Response.createResponseEntity(1, "Server is overloaded. Please repeat the operation.", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WrongCaptchaException.class)
    public ResponseEntity<?> wrongCaptcha(WrongCaptchaException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Wrong captcha code"), null, HttpStatus.BAD_REQUEST);
    }
}
