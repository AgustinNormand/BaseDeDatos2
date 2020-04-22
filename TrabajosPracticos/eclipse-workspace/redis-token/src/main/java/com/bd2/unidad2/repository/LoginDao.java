package com.bd2.unidad2.repository;

import com.bd2.unidad2.repository.exceptions.IncorrectToken;
import com.bd2.unidad2.repository.exceptions.LoginError;

/**
 * Purpose: Interfaz para el manejo de sesion del usuario. Permite a un usuario loguearse o desloguearse.
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public interface LoginDao {

  /**
   * Valida si el usuario existe y tiene es password provisto.
   * Devuelve un nuevo token(si no existia) para el usuario si el mail y password son correctos.
   * Si el token existe lo devuelve sin cambiar si TTL.
   *
   * @param email email a validar
   * @param password password para ese email
   * @return token si el mail y password son correctos
   *
   * @throws LoginError si usuario o password son incorrectos
   */
  String login(String email, String password);

  /**
   * Borra el token asociado de tal forma que el usuario debera volver a loguearse.
   *
   * @param token a borrar
   * @throws IncorrectToken si no esta asociado a ningun mail
   */
  void logout(String token);
}
