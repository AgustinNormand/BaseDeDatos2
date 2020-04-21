package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class UserAlreadyExists extends RuntimeException {

  public UserAlreadyExists(String msg) {
    super(msg);
  }
}
