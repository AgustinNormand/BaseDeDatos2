package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class UserAlreadyExists extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -242760875962388977L;

public UserAlreadyExists(String msg) {
    super(msg);
  }
}
