package com.bd2.unidad2.repository.impl;

import com.bd2.unidad2.repository.DaosFactory;
import com.bd2.unidad2.repository.LoginDao;
import com.bd2.unidad2.repository.MailValidationDao;
import com.bd2.unidad2.repository.TokenDao;
import com.bd2.unidad2.repository.UsuarioDao;

import io.lettuce.core.api.sync.RedisCommands;

public class DaosImplementationFactory implements DaosFactory {

	public DaosImplementationFactory(RedisCommands<String, String> con) {}

	@Override
	public UsuarioDao getInstanceOfUsuarioDao() {
		// TODO Auto-generated method stub
		return new UsuarioDaoImpl();
	}

	@Override
	public MailValidationDao getInstanceOfMailValidationDao() {
		// TODO Auto-generated method stub
		return new MailValidationDaoImpl();
	}

	@Override
	public TokenDao getInstanceOfTokenDao() {
		// TODO Auto-generated method stub
		return new TockenDaoImpl();
	}

	@Override
	public LoginDao getInstanceOfLoginDao() {
		// TODO Auto-generated method stub
		return new LoginDaoImpl();
	}
	
}
