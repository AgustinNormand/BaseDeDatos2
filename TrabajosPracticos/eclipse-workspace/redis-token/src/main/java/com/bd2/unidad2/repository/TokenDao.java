package com.bd2.unidad2.repository;

import com.bd2.unidad2.repository.exceptions.IncorrectToken;

/**
 * Purpose: Interfaz que abstrae el pedido de un mail utilizando el token de su implementacion en la BD
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public interface TokenDao {

  /**
   * Metodo que busca el mail asociado a ese token.
   *
   * @param token token.
   * @return mail que identifica al usuario que tiene el token.
   * @throws IncorrectToken si el token no esta asociado a ningun mail.
   */
  public String getMailWithToken(String token);

}
