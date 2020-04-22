package com.bd2.unidad2.repository.exceptions;

/**
 * Purpose: Excepcion lanzada cuando el usuario no cumple con los requerimientos minimos.
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class InvalidUserInformation extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 3791411411194286487L;

public InvalidUserInformation() {
  }

  public InvalidUserInformation(String message) {
    super(message);
  }
}
