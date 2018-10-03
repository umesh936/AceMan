package in.sminfo.tool.mgmt.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import in.sminfo.tool.mgmt.exception.GenericRuntimeException;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.exception.LoginNotAllowedException;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to handling the exception from all the controller which
 * were not handled in the controller class.
 *
 * If we add any custom exceptions / annotated exception let throw the exception
 * and handle by the spring framework.
 *
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private static final String CLASS_NAME = "GlobalControllerExceptionHandler";

	/**
	 * This exception handler is used to handle the Exception class.
	 *
	 * @param e
	 * @param request
	 * @return
	 */
	private static final String method_defaultErrorHandler = ":DefaultErrorHandler() ";

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<String> defaultErrorHandler(Exception e, WebRequest request) {
		log.error(CLASS_NAME + method_defaultErrorHandler + e.getMessage(), e);
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostConstruct
	public void post() {
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	String handleNotFound(HttpServletRequest req, HttpServletResponse res,
			HttpRequestMethodNotSupportedException exception) {
		log.error("Request: " + req.getRequestURL() + " raised " + exception, exception);
		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return exception.getMessage();
	}

	@ExceptionHandler(DataRetrievalFailureException.class)
	public @ResponseBody String handleError(HttpServletRequest req, HttpServletResponse res,
			DataRetrievalFailureException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return "Not found";
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public @ResponseBody String handleError(HttpServletRequest req, HttpServletResponse res,
			DataIntegrityViolationException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		Throwable cause = exception.getCause();
		String message = cause.getMessage();
		if (cause instanceof ConstraintViolationException) {
			message = ((ConstraintViolationException) cause).getConstraintName();
		}
		res.setStatus(HttpServletResponse.SC_CONFLICT);
		return message;
	}

	@ExceptionHandler(GenericRuntimeException.class)
	public @ResponseBody String handleError(HttpServletRequest req, HttpServletResponse res,
			GenericRuntimeException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return exception.getMessage();
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public @ResponseBody String handleError(HttpServletRequest req, HttpServletResponse res,
			ObjectNotFoundException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return "Not found";
	}

	@ExceptionHandler(InvalidRequestException.class)
	public @ResponseBody String handleInvalidRequest(HttpServletRequest req, HttpServletResponse res,
			InvalidRequestException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return "redirect:/login?error=Invalid%20Session";
	}

	@ExceptionHandler(LoginNotAllowedException.class)
	public @ResponseBody String handleLoginNotAllowed(HttpServletRequest req, HttpServletResponse res,
			InvalidRequestException exception) {
		log.error("Request: " + req.getRequestURL() + " raised execption", exception);
		res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return "redirect:/login?error=" + exception.getLocalizedMessage();
	}

}