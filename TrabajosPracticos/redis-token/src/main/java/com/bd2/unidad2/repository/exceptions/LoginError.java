package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class LoginError extends RuntimeException {

  public LoginError() {
  }

  public LoginError(String message) {
    super(message);
  }
}
