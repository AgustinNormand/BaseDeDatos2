package com.bd2.unidad2.repository.impl;

import com.bd2.unidad2.repository.LoginDao;
import com.bd2.unidad2.repository.exceptions.LoginError;
import com.bd2.unidad2.repository.utils.TokenGenerator;

import io.lettuce.core.api.sync.RedisCommands;

public class LoginDaoImpl implements LoginDao {
	ConnectionManager connectionManager = ConnectionManager.getInstance("localhost",6379,"masterkey");
	RedisCommands<String, String> con = connectionManager.getSyncConnection();

	@Override
	public String login(String email, String password) {
		String token = null;
		if (con.exists(email) == 1) {
			String passwd = con.hget(email, "Password");
			if (passwd.equals(password)) {
				String databaseToken = con.hget(email, "Token");
				if (databaseToken == "") {
					token = TokenGenerator.generate();
					con.hset(email, "Token", token);
				} else
					token = databaseToken;
			} else
				throw new LoginError("Contrasenia incorrecta");
		} else
			throw new LoginError("Email incorrecto");				
		return token;
	}

	@Override
	public void logout(String token) {
		// TODO Auto-generated method stub

	}

}
