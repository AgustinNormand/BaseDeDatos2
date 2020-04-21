package com.bd2.unidad2.emailValidation;

import com.bd2.unidad2.repository.MailValidationDao;

/**
 * Purpose: Envia correo para validar mails y recibe respuestas de validacion
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class EmailValidationService implements EmailValidation {

  private MailValidationDao mailValidationDao;

  @Override
  public void sendValidationEmail(String email) {
    //No hacer nada
  }

  /**
   * Este metodo es llamado cuando el usuario hace click en el link del correo que le llego para
   * validar su email
   *
   * @param email a validar
   */
  public void validateEmail(String email){
    //...
    this.mailValidationDao.validatedMail(email);
    //...
  }
}
