package com.project.npp.exceptions;
//Imports necessary for the GlobalExceptionHandler class
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
* Global exception handler for handling various exceptions throughout the application.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {


	/**
	 * Exception handler for CustomerNotFoundException.
	 * 
	 * @return ResponseEntity with a "Customer not found" message and HTTP status code NOT_FOUND.
	 */
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFoundException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for OperatorNotFoundException.
	 * 
	 * @return ResponseEntity with an "Operator not found" message and HTTP status code NOT_FOUND.
	 */
	@ExceptionHandler(OperatorNotFoundException.class)
	public ResponseEntity<String> handleOperatorNotFoundException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for LogNotFoundException.
	 * 
	 * @return ResponseEntity with a "Log not found" message and HTTP status code NOT_FOUND.
	 */
	@ExceptionHandler(LogNotFoundException.class)
	public ResponseEntity<String> handleLogNotFoundException() {
		return new ResponseEntity<>("Log not found", HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for PortRequestNotFoundException.
	 * 
	 * @return ResponseEntity with a "PortRequest not found" message and HTTP status code NOT_FOUND.
	 */
	@ExceptionHandler(PortRequestNotFoundException.class)
	public ResponseEntity<String> handlePortRequestNotFoundException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for RoleNotFoundException.
	 * 
	 * @return ResponseEntity with a "Role not found" message and HTTP status code NOT_FOUND.
	 */
	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<String> handleRoleNotFoundException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
	
	@ExceptionHandler(VerificationDetailsNotFoundException.class)
	public ResponseEntity<String> handleVerificationDetailsNotFoundException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
