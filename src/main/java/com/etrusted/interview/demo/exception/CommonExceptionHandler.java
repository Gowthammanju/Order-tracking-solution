package com.etrusted.interview.demo.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CommonExceptionHandler {
	private static final String ERROR = ":ERROR";

	@ExceptionHandler({ DemoBaseException.class, Exception.class })
	void handleBaseException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setAttribute(DefaultErrorAttributes.class.getName() + ERROR, e);
		sendHttpError(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ParameterMissingException.class)
	void handleMissingParameterException(DemoBaseException e, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setAttribute(DefaultErrorAttributes.class.getName() + ERROR, e);
		sendHttpError(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ShopMissingException.class)
	void handleMissingDatabaseException(DemoBaseException e, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setAttribute(DefaultErrorAttributes.class.getName() + ERROR, e);
		sendHttpError(response, HttpStatus.NOT_FOUND);
	}

	protected void sendHttpError(HttpServletResponse response, HttpStatus code) throws IOException {
		response.sendError(code.value());
	}
}
