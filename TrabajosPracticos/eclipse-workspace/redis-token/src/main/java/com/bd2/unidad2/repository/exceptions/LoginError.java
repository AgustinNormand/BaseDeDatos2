package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class LoginError extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -4709767996467161920L;

public LoginError() {
  }

  public LoginError(String message) {
    super(message);
  }
}
