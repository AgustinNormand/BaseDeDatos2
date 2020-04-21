package com.bd2.unidad2.repository;

import com.bd2.unidad2.model.Usuario;
import com.bd2.unidad2.repository.exceptions.InvalidUserInformation;
import com.bd2.unidad2.repository.exceptions.UserAlreadyExists;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Purpose: Define las operacion que debe ser capas de realizar la capa de 'persistencia'
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public interface UsuarioDao {

  /**
   * Guarda los atributos en la BD y devuelve el objeto con el token seteado.
   *
   * @param usuario usuario a modificar
   * @return mismo usuario con los datos que tiene en la BD
   * @throws InvalidUserInformation si el usuario no cumple los requisitos para ser guardado
   * @throws UserAlreadyExists si ya existe el mail o DNI
   */
  public Usuario alta(Usuario usuario);

  /**
   * Modifica ciertos campos del usuario en la BD.
   * Si se modifica el password se borra el token y el usuario se debe volver a loguear.
   *
   * @param usuario usuario a modificar
   * @return usuario con los mismos datos que tiene en la BD
   * @throws InvalidUserInformation si los nuevos datos incumplen un requisito
   */
  public Usuario modificacion(Usuario usuario);

  /**
   * baja logica, no elimina datos realmente.
   *
   * @param usuario usuario a ser dado de baja
   * @return usuario con estado cambiado
   */
  public Usuario baja(Usuario usuario);

  /**
   * Obtine todos los registros de conexion del usuario identificado con el mail.
   *
   * @param email que identifica al usuario
   * @return lista de fechas en las que el usuario se conecto.
   */
  public List<LocalDateTime> getAllConnectionPerMail(String email);

  /**
   * dado un mail retorna los datos de ese usuario si es que existe
   *
   * @param email del usuario a buscar
   * @return Usuario si el tiene el mail asociado
   */
  public Optional<Usuario> getUsuarioByEmail(String email);

  /**
   * Busca un usuario utilizando el DNI del mismo.
   *
   * @param dni del usuario a buscar
   * @return un usuario si lo encuentra, sino un nulo.
   */
  public Optional<Usuario> getByDni(String dni);

}
