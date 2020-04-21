package com.bd2.unidad2.repository;

/**
 * Purpose: Interfaz que separa el "evento recibido por mail" de su implementacion en la BD
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public interface MailValidationDao {

  /**
   * Metodo que cambia el estado del usuario indicando que ya valido el mail.
   *
   * @param mail que fue validado.
   */
  public void validatedMail(String mail);

}
