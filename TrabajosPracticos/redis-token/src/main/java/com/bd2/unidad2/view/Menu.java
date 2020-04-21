package com.bd2.unidad2.view;

import com.bd2.unidad2.model.Usuario;
import com.bd2.unidad2.repository.LoginDao;
import com.bd2.unidad2.repository.MailValidationDao;
import com.bd2.unidad2.repository.TokenDao;
import com.bd2.unidad2.repository.UsuarioDao;
import com.bd2.unidad2.repository.exceptions.IncorrectToken;
import com.bd2.unidad2.repository.exceptions.InvalidUserInformation;
import com.bd2.unidad2.repository.exceptions.LoginError;
import com.bd2.unidad2.repository.exceptions.UserAlreadyExists;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Purpose: vista simple que da acceso a todas las funcionalidades
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class Menu {

  private Console c;
  private LoginDao loginDao;
  private UsuarioDao usuarioDao;
  private TokenDao tokenDao;
  private MailValidationDao mailValidationDao;
  private String ls = System.lineSeparator();

  public Menu(LoginDao loginDao, UsuarioDao usuarioDao, TokenDao tokenDao, MailValidationDao mailValidationDao) {
    this.loginDao = loginDao;
    this.usuarioDao = usuarioDao;
    this.tokenDao = tokenDao;
    this.mailValidationDao = mailValidationDao;
    this.c = System.console();
  }

  public void menuPrincipal(){
    while(true){
      c.printf(ls);
      c.printf("-- Menu Principal --"+ls);
      c.printf("-- Gestion de Usuarios --"+ls);
      c.printf("----> 1) Nuevo usuario"+ls);
      c.printf("----> 2) Modificar usuario"+ls);
      c.printf("----> 3) Eliminar usuario"+ls);
      c.printf("----> 4) Historial de conexiones"+ls);
      c.printf("----> 5) Buscar por email"+ls);
      c.printf("----> 6) Buscar por DNI"+ls);
      c.printf("-- Login con Usuarios -- "+ls);
      c.printf("----> 7) Log IN"+ls);
      c.printf("----> 8) Log OUT"+ls);
      c.printf("-- Tokens -- "+ls);
      c.printf("----> 9) Obtener un mail mediante un token"+ls);
      c.printf("-- Email -- "+ls);
      c.printf("----> 10) Simular click en correo de validacion"+ls);
      c.printf("-- Salir -- "+ls);
      c.printf("----> 0) cerrar aplicacion"+ls);
      c.printf(ls);
      String opcion = c.readLine();
      c.printf(ls);
      switch (opcion){
        case "0":
          c.printf("Saliendo "+ls);
          return;
        case "1":
          nuevo();
          break;
        case "2":
          modif();
          break;
        case "3":
          elim();
          break;
        case "4":
          hist();
          break;
        case "5":
          buscEmail();
          break;
        case "6":
          buscDni();
          break;
        case "7":
          logIn();
          break;
        case "8":
          logOut();
          break;
        case "9":
          getByToken();
          break;
        case "10":
          simul();
          break;
        default:
          c.printf("opcion no reconocida"+ls);
          break;
      }
    }
  }

  private void simul() {
    this.mailValidationDao.validatedMail(readMail());
    c.printf("Mail validado"+ls);
  }

  private void getByToken() {
    String token = c.readLine("Ingrese el token"+ls);
    try{
      String mail = this.tokenDao.getMailWithToken(token);
      c.printf("El mail es: %s"+ls, mail);
    }catch(IncorrectToken error){
      c.printf("%s ERROR - %s %s"+ls, ConsoleColors.RED, error.getMessage(), ConsoleColors.RESET);
    }
  }

  private void logOut() {
    String token = c.readLine("Ingrese el token"+ls);
    try{
      this.loginDao.logout(token);
      c.printf("LogOut  exitoso");
    }catch(IncorrectToken error){
      c.printf("%s ERROR - %s %s"+ls, ConsoleColors.RED, error.getMessage(), ConsoleColors.RESET);
    }
  }

  private void logIn() {
    try{
      final String token = this.loginDao.login(readMail(), readPass());
      c.printf("El token es: %s",token);
    }catch(LoginError error){
      c.printf("%s ERROR - %s %s"+ls, ConsoleColors.RED, error.getMessage(), ConsoleColors.RESET);
    }
  }

  private void buscDni() {
    String dni = c.readLine("Ingrese el DNI: "+ls);
    Optional<Usuario> optional = this.usuarioDao.getByDni(dni);
    optional.ifPresentOrElse(this::mostrarUsuario, () -> c.printf("No hay Resultados"+ls)
    );
  }

  private void buscEmail() {
    Optional<Usuario> optional = this.usuarioDao.getUsuarioByEmail(readMail());
    optional.ifPresentOrElse(this::mostrarUsuario, () -> c.printf("No hay Resultados"+ls));
  }

  private void mostrarUsuario(Usuario u) {
    c.printf("Usuario "+ls);
    c.printf("- Mail: %s"+ls, u.getEmail());
    c.printf("- Nombre: %s"+ls, u.getNombre());
    c.printf("- Apellido: %s"+ls, u.getApellido());
    c.printf("- DNI: %s"+ls, u.getDni());
    c.printf("- Password: %s"+ls, u.getPassword());
    c.printf("- Valido mail?: %s"+ls, u.isMailValidated()?"SI":"NO");
    c.printf("- Creado en: %s"+ls, u.getCreatedAt());
    c.printf(ls);
  }

  private void hist() {
    List<LocalDateTime> conexiones = this.usuarioDao.getAllConnectionPerMail(readMail());
    if( conexiones.isEmpty() ){
      c.printf("No hay resultados para ese mail"+ls);
    }else{
      c.printf("Las conexiones fueron en los siguientes dias: "+ls);
      conexiones.forEach( fecha -> c.printf("-- %s"+ls,fecha.toString()));
      c.printf(ls);
    }
  }

  private void elim() {
    Usuario u = new Usuario();
    u.setEmail(readMail());
    this.usuarioDao.baja(u);
    c.printf(ls+"Baja exittosa "+ls);
  }

  private void modif() {
    try {
      this.usuarioDao.modificacion(readUsuario());
      c.printf(ls);
      c.printf("Usuario modificado");
      c.printf(ls);
    } catch ( InvalidUserInformation error) {
      c.printf(ls);
      c.printf("%s ERROR - %s %s", ConsoleColors.RED, error.getMessage(), ConsoleColors.RESET);
      c.printf(ls);
    }
  }

  private void nuevo() {
    try{
      this.usuarioDao.alta( readUsuario() );
      c.printf(ls);
      c.printf("Usuario guardado");
      c.printf(ls);
    }catch (UserAlreadyExists | InvalidUserInformation error){
      c.printf(ls);
      c.printf("%s ERROR - %s %s", ConsoleColors.RED, error.getMessage(), ConsoleColors.RESET);
      c.printf(ls);
    }
  }

  private Usuario readUsuario(){
    Usuario u = new Usuario();
    u.setEmail(readMail());
    u.setDni(c.readLine("Ingrese el DNI: "+ls));
    u.setNombre(c.readLine("Ingrese el nombre: "+ls));
    u.setApellido(c.readLine("Ingrese el apellido: "+ls));
    u.setPassword(readPass());
    return u;
  }

  private String readMail(){
    return c.readLine("Ingrese el mail: "+ls);
  }

  private String readPass(){
    final char[] chars = c.readPassword("Ingrese el password: " + ls);
    StringBuilder sb = new StringBuilder();
    for(char c: chars){
      sb.append(c);
    }
    return sb.toString();
  }

}
