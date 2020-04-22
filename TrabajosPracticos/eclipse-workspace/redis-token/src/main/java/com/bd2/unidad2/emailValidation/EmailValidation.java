package com.bd2.unidad2.emailValidation;

/**
 * Purpose: Intefaz del servicio de validacion de emails.
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public interface EmailValidation {

  /**
   * Envia el correo con el link para validar el email a la cuenta pasada por parametro.
   *
   * @param email al cual mandar el link de validacion
   */
  void sendValidationEmail(String email);

}
