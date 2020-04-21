package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class IncorrectToken extends RuntimeException {

  public IncorrectToken() {
  }

  public IncorrectToken(String message) {
    super(message);
  }
}
