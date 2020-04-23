package com.bd2.unidad2.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bd2.unidad2.model.Usuario;
import com.bd2.unidad2.repository.UsuarioDao;
import com.bd2.unidad2.repository.exceptions.InvalidUserInformation;
import com.bd2.unidad2.repository.exceptions.UserAlreadyExists;

import io.lettuce.core.api.sync.RedisCommands;

public class UsuarioDaoImpl implements UsuarioDao{
	ConnectionManager connectionManager = ConnectionManager.getInstance("localhost",6379,"masterkey");
	RedisCommands<String, String> con = connectionManager.getSyncConnection();


	@Override
	public Usuario alta(Usuario usuario){
		Usuario usuarioReturn = null;
		if (con.exists(usuario.getEmail()) == 0) {
			if (mailValido(usuario.getEmail())) {
			con.hset(usuario.getEmail(), "Dni", usuario.getDni());
			con.hset(usuario.getEmail(),"Nombre",usuario.getNombre());
			con.hset(usuario.getEmail(),"Apellido",usuario.getApellido());
			con.hset(usuario.getEmail(), "Email", usuario.getEmail());
			con.hset(usuario.getEmail(),"CreatedAt", usuario.getCreatedAt());
			con.hset(usuario.getEmail(),"Password", usuario.getPassword());
			con.hset(usuario.getEmail(), "Estado", "Activo");
			con.hset(usuario.getEmail(), "Token", "");
			//con.hset(usuario.getEmail(), "Conexiones", );
			con.expire(usuario.getEmail(), 70560*60);
			usuarioReturn = new Usuario(con.hgetall(usuario.getEmail()),con.ttl(usuario.getEmail())==-1);
			}
			else throw new InvalidUserInformation("El email ingresado no es valido");
		} else
			throw new UserAlreadyExists("Ya existe el usuario con el Mail "+usuario.getEmail());
		return usuarioReturn;
	}

	private boolean mailValido(String email) {
		boolean result = false;
		if (email.contains("@") && (email.contains(".") && validateChars(email))) {
			result = true;
		}
		return result;
	}
	
	private boolean validateChars(String email) {
		boolean result = true;
		ArrayList<String> chars = new ArrayList<>();
		chars.add("/");
		chars.add("!");
		chars.add("#");
		chars.add("$");
		chars.add("%");
		chars.add("^");
		chars.add("&");
		chars.add("*");
		chars.add("(");
		chars.add(")");
		chars.add("-");
		chars.add("=");
		chars.add("?");
		chars.add("|");
		chars.add("\"");
		chars.add("]");
		chars.add("[");
		chars.add("}");
		chars.add("{");
		chars.add("|");
		chars.add(">");
		chars.add("<");
		chars.add("_");
		int index = 0;
		while (result && index  < chars.size()) {
			if (email.contains(chars.get(index++)))
				result = false;
		}
		return result;
	}

	@Override
	public Usuario modificacion(Usuario usuario) {
		Usuario usuarioReturn = null;
		if (con.exists(usuario.getEmail()) == 1){
			con.hset(usuario.getEmail(),"Dni", usuario.getDni());
			con.hset(usuario.getEmail(),"Nombre", usuario.getNombre());
			con.hset(usuario.getEmail(),"Apellido", usuario.getApellido());
			con.hset(usuario.getEmail(),"CreatedAt", "");
			con.hset(usuario.getEmail(),"Password", usuario.getPassword());
			con.hset(usuario.getEmail(),"mailValidated", "");
			
			usuarioReturn = new Usuario(
									con.hgetall(usuario.getEmail())
								    ,con.ttl(usuario.getEmail())==-1
									);
		}
		return usuarioReturn;
	}

	@Override
	public Usuario baja(Usuario usuario) {
		Usuario usuarioReturn = null;
		if (con.exists(usuario.getEmail()) == 1) {
			con.hset(usuario.getEmail(), "Estado", "Inactivo");
			usuarioReturn = new Usuario(con.hgetall(usuario.getEmail()),con.ttl(usuario.getEmail())==-1);
		}
		return usuarioReturn;
	}

	@Override
	public List<LocalDateTime> getAllConnectionPerMail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Usuario> getUsuarioByEmail(String email) {
		Usuario usuarioReturn = null;
		if (con.exists(email) == 1) 
			if (con.hget(email, "Estado").equals("Activo"))
				usuarioReturn = new Usuario(con.hgetall(email),con.ttl(email)==-1);
		return Optional.ofNullable(usuarioReturn);
	}

	@Override
	public Optional<Usuario> getByDni(String dni) {
		Usuario usuarioReturn = null;
		for (String clave : con.keys("*")) {
			if (dni.equals(con.hget(clave, "Dni"))){
				String email = con.hget(clave, "Email");
				usuarioReturn = new Usuario(con.hgetall(email),con.pttl(email)==-1);
				break;
			}
		}
		return Optional.ofNullable(usuarioReturn);
	}

}
