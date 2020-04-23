package com.bd2.unidad2.model;

import java.util.Map;
import java.util.Objects;

/**
 * Purpose: Representa a un usuario
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class Usuario {

  private String nombre;
  private String apellido;
  private String dni;
  private String email;
  private String password;
  private Boolean mailValidated = false;
  private String createdAt = "";
  private String estado;

  public Usuario() {
  }
  
  public Usuario(Map<String, String> map, boolean mailValidated) {
	  this.nombre = map.get("Nombre");
	  this.apellido = map.get("Apellido");
	  this.dni = map.get("Dni");
	  this.email = map.get("Email");
	  this.password = map.get("Password");
	  this.createdAt = map.get("CreatedAt");
	  this.estado = map.get("Estado");
	  this.mailValidated = mailValidated;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }
  
  public void setEstado(String estado) {
	  this.estado = estado;
  }
  
  public String getEstado() {
	  return estado;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean isMailValidated() {
    return mailValidated;
  }

  public void setMailValidated(Boolean mailValidated) {
    this.mailValidated = mailValidated;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Usuario usuario = (Usuario) o;
    return email.equals(usuario.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public String toString() {
    return "Usuario{" +
        "nombre='" + nombre + '\'' +
        ", apellido='" + apellido + '\'' +
        ", dni='" + dni + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", mailValidated=" + mailValidated +
        ", createdAt='" + createdAt + '\'' +
        '}';
  }
}
