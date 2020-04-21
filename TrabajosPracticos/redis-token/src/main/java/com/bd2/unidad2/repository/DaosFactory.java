package com.bd2.unidad2.repository;

public interface DaosFactory {

    UsuarioDao getInstanceOfUsuarioDao();

    MailValidationDao getInstanceOfMailValidationDao();

    TokenDao getInstanceOfTokenDao();

    LoginDao getInstanceOfLoginDao();
}
