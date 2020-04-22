package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose:
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class IncorrectToken extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1747204087816082834L;

public IncorrectToken() {
  }

  public IncorrectToken(String message) {
    super(message);
  }
}
