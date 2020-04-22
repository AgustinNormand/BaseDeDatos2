package com.bd2.unidad2;

import com.bd2.unidad2.model.Usuario;
import com.bd2.unidad2.repository.*;
import com.bd2.unidad2.repository.exceptions.IncorrectToken;
import com.bd2.unidad2.repository.exceptions.InvalidUserInformation;
import com.bd2.unidad2.repository.exceptions.LoginError;
import com.bd2.unidad2.repository.exceptions.UserAlreadyExists;
import com.bd2.unidad2.repository.impl.ConnectionManager;
import com.bd2.unidad2.repository.impl.DaosImplementationFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bd2.unidad2.Helper.JUAN_PEREZ;
import static com.bd2.unidad2.Helper.PITY_MARTINEZ;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDaoTest {

    //interfaces de los servicios.
    private UsuarioDao usuarioDao;
    private MailValidationDao mailValidationDao;
    private LoginDao loginDao;
    private TokenDao tokenDao;

    //Servidor redis en memoria. Antes de cada test se instancia, por lo que en cada test esta vacio.
    private final int puerto = 4500;
    private final String url = "localhost";
    private RedisServer redisServer;

    //Variables para usar los test
    private Usuario usuario;
    private Optional<Usuario> optionalUsuario;
    private String token;
    private String email;
    private List<LocalDateTime> listaConexiones;


    /**
     * Pasos a realizar antes de cada test.
     * Estos incluyen iniciar el servidor redis, crear las implementacion de las interfaces e inicializar las variables a ser utilizadas en los test.
     * El alumno debera sobreescribir los metodos: configurarServidorRedisTesting() y crearImplementacionesDeServicios()
     *
     * @throws IOException si revienta crear el redis
     */
    @BeforeEach
    public void setUp() throws IOException {
        configurarServidorRedisTesting();
        redisServer.start();
        crearImplementacionesDeServicios();
        InicializarVariables();
    }

    /**
     * Las variables usadas por los test se deben inicializar siempre para que no quede basura de los test anteriores
     */
    private void InicializarVariables() {
        this.usuario = null;
        this.optionalUsuario = Optional.empty();
        this.token = null;
        this.email = null;
        this.listaConexiones = null;
    }

    /**
     * Configurar la instancia de redis y si es necesario el conector al mismo.
     * @throws IOException si revienta detiene el test
     */
    private void configurarServidorRedisTesting() throws IOException {
        redisServer = new RedisServer(puerto);
    }

    /**
     * Se deben crear las implmentaciones a las interfaces.
     */
    private void crearImplementacionesDeServicios() {
        ConnectionManager connectionManager = ConnectionManager.getInstance(url, puerto);
        DaosFactory factory  = new DaosImplementationFactory(connectionManager.getSyncConnection());
        this.usuarioDao = factory.getInstanceOfUsuarioDao();
        this.mailValidationDao = factory.getInstanceOfMailValidationDao();
        this.loginDao = factory.getInstanceOfLoginDao();
        this.tokenDao = factory.getInstanceOfTokenDao();
    }

    /**
     * despues de cada test se cierra la BD
     */
    @AfterEach
    public void tearDown(){
        redisServer.stop();
    }

    @Test
    public void insertarUsuario(){
        dadoUsuario( JUAN_PEREZ() );
        cuandoHagoInsert();
    }

    @Test
    public void insertarUsuarioConMailNoValido_tiraExcepcion_InvalidUserInformation(){
        dadoUsuario( JUAN_PEREZ() );
        dadoQueElMailDeJuanPerezAhoraEs( "mail_que1No_es_correcto" );

        Assertions.assertThrows(
                InvalidUserInformation.class,
                this::cuandoHagoInsert
        );
    }

    @Test
    public void obtenerUsuarioPorMail(){
        dadoQueJuanPerezEstaEnBd();
        cuandoPidousuarioPorMail( JUAN_PEREZ().getEmail() );
        entoncesElUsuarioEsJuanPerez();
    }

    @Test
    public void obtenerUsuarioPorDni(){
        dadoQueJuanPerezEstaEnBd();
        cuandoPidousuarioPorDni( JUAN_PEREZ().getDni() );
        entoncesElOptionalUsuarioEsJuanPerez();
    }

    @Test
    public void obtenerUsuarioPorDni_NoExiste(){
        dadoQueJuanPerezEstaEnBd();
        cuandoPidousuarioPorDni( PITY_MARTINEZ().getDni() );
        entoncesElUsuarioOpcionalEsNulo();
    }

    @Test
    public void obtenerUsuarioPorMail_NoExiste(){
        dadoQueJuanPerezEstaEnBd();
        cuandoPidousuarioPorMail( PITY_MARTINEZ().getEmail());
        entoncesElUsuarioOpcionalEsNulo();
    }

    @Test
    public void agregar2VecesAlMismoUsuario_tiraExcepcion_UserAlreadyExsists(){
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario( JUAN_PEREZ() );

        Assertions.assertThrows(
                UserAlreadyExists.class,
                this::cuandoHagoInsert
        );
    }

    @Test
    public void eliminarUsuario(){
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario( JUAN_PEREZ() );

        cuandoEliminoAlUsuario();

        cuandoPidousuarioPorMail(JUAN_PEREZ().getEmail());
        entoncesElUsuarioOpcionalEsNulo();
    }

    @Test
    public void validarMail(){
        dadoQueJuanPerezEstaEnBd();

        cuandoJuanPerezValidaMail();
        cuandoPidousuarioPorMail( JUAN_PEREZ().getEmail() );

        entoncesUsuarioTieneMailValidado();
    }

    @Test()
    public void cuandoModificoUsuarioQueNoValidoMail_TiraExcepcion_InvalidUserInformation() {
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario(JUAN_PEREZ());
        dadoNuevoNombreAUsuario("Alfonso");

        Assertions.assertThrows(
                InvalidUserInformation.class,
                this::cuandoHagoUpdate
        );
    }

    @Test
    public void updateUser(){
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario( JUAN_PEREZ() );
        dadoNuevoNombreAUsuario( "Alfonso" );
        dadoQueUsuarioValidoMail();

        cuandoHagoUpdate();
        cuandoPidousuarioPorMail( JUAN_PEREZ().getEmail() );

        entoncesUsuarioOpcionalTieneNombre("Alfonso");
    }

    @Test
    public void loginCorrecto(){
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario( JUAN_PEREZ() );

        cuandoHagoLogin();

        entoncesDevolvioUnToken();
    }

    @Test
    public void loginCorrecto2veces_devuelveMismoToken(){
        dadoQueJuanPerezEstaEnBd();
        dadoQueJuanPerezHizoLogin();
        dadoUsuario( JUAN_PEREZ() );

        String primerToken = this.token;
        cuandoHagoLogin();
        String segundoToken = this.token;

        entoncesAmbosTokensSonIgualesYnoNulos(primerToken, segundoToken);
    }

    @Test
    public void loginIncorrecto_tiraExcepcion_LoginError(){
        dadoQueJuanPerezEstaEnBd();
        dadoUsuario( PITY_MARTINEZ() );

        Assertions.assertThrows(
                LoginError.class,
                this::cuandoHagoLogin
        );
    }

    @Test
    public void loginEnBdVacia_tiraExcepcion_loginError(){
        dadoUsuario( PITY_MARTINEZ() );

        Assertions.assertThrows(
                LoginError.class,
                this::cuandoHagoLogin
        );
    }

    @Test
    public void obtenerMailDeUsuarioLogeado(){
        dadoQueJuanPerezEstaEnBd();
        dadoQueJuanPerezHizoLogin();

        cuandoPidoMailUsandoToken();

        entoncesElMailObtenidoEs( JUAN_PEREZ().getEmail() );
    }

    @Test
    public void cuandoPidoMailUsandoTokenDeUnUsuarioDeslogueado_tiraExcepcion_IncorrectToken(){
        dadoQueJuanPerezEstaEnBd();
        dadoQueJuanPerezHizoLogin();

        cuandoHagoLogOut();
        Assertions.assertThrows(
                IncorrectToken.class,
                this::cuandoPidoMailUsandoToken
        );
    }

    @Test
    public void obtenerListaDeConexionesDeUnUsuarioQueNuncaSeConecto(){
        dadoQueJuanPerezEstaEnBd();

        cuandoPidoListaDeConexionesDe( JUAN_PEREZ().getEmail() );

        entoncesListaDeConexionesEstaVacia();
    }

    @Test
    public void obtenerListaDeConexionesQueSeConecto2Veces(){
        dadoQueJuanPerezEstaEnBd();

        dadoQueJuanPerezHizoLogin();
        dadoQueJuanPerezHizoLogOut();

        dadoQueJuanPerezHizoLogin();
        dadoQueJuanPerezHizoLogOut();

        cuandoPidoListaDeConexionesDe( JUAN_PEREZ().getEmail() );

        entoncesListaDeConexionesTiene2Conexiones();
    }

    @Test
    public void obtenerListaDeConexionesDeUsuarioQueNoExiste(){
        dadoQueJuanPerezEstaEnBd();

        cuandoPidoListaDeConexionesDe( PITY_MARTINEZ().getEmail() );

        entoncesListaDeConexionesEstaVacia();
    }

    private void entoncesListaDeConexionesTiene2Conexiones() {
        assertNotNull(listaConexiones);
        assertEquals(2, listaConexiones.size() );
    }

    private void dadoQueJuanPerezHizoLogOut() {
        this.loginDao.logout(this.token);
    }

    private void entoncesListaDeConexionesEstaVacia() {
        assertNotNull(listaConexiones);
        assertTrue( listaConexiones.isEmpty() );
    }

    private void cuandoPidoListaDeConexionesDe(String email) {
        this.listaConexiones = this.usuarioDao.getAllConnectionPerMail(email);
    }

    private void entoncesAmbosTokensSonIgualesYnoNulos(String primerToken, String segundoToken) {
        assertNotNull(primerToken);
        assertEquals(primerToken, segundoToken);
    }

    private void entoncesElMailObtenidoEs(String email) {
        assertEquals(email, this.email);
    }

    private void cuandoPidoMailUsandoToken() {
        this.email = this.tokenDao.getMailWithToken( this.token );
    }

    private void cuandoHagoLogOut() {
        this.loginDao.logout(this.token);
    }

    private void dadoQueJuanPerezHizoLogin() {
        this.token = this.loginDao.login(JUAN_PEREZ().getEmail(), JUAN_PEREZ().getPassword());
    }

    private void entoncesDevolvioUnToken() {
        assertNotNull(this.token);
        assertEquals(60, this.token.length());
    }

    private void cuandoHagoLogin() {
        this.token = this.loginDao.login(usuario.getEmail(), usuario.getPassword());
    }

    private void dadoQueUsuarioValidoMail() {
        this.mailValidationDao.validatedMail( usuario.getEmail() );
    }

    private void entoncesUsuarioTieneMailValidado() {
        assertTrue(optionalUsuario.isPresent());
        Usuario usuario = optionalUsuario.get();
        assertTrue( usuario.isMailValidated() );
    }

    private void cuandoJuanPerezValidaMail() {
        this.mailValidationDao.validatedMail(JUAN_PEREZ().getEmail());
    }

    private void cuandoEliminoAlUsuario() {
        this.usuarioDao.baja(usuario);
    }

    private void entoncesUsuarioOpcionalTieneNombre(String nombre) {
        assertTrue( optionalUsuario.isPresent() );
        assertEquals(nombre, optionalUsuario.get().getNombre() );
    }

    private void dadoNuevoNombreAUsuario(String nuevoNombre) {
        this.usuario.setNombre(nuevoNombre);
    }

    private void entoncesElUsuarioOpcionalEsNulo() {
        assertTrue( this.optionalUsuario.isEmpty() );
    }

    private void cuandoPidousuarioPorDni(String dni) {
        this.optionalUsuario = this.usuarioDao.getByDni(dni);
    }

    private void entoncesElUsuarioEsJuanPerez() {
        assertNotNull(this.usuario);
        assertEquals(this.usuario, JUAN_PEREZ());
    }
    
    private void entoncesElOptionalUsuarioEsJuanPerez() {
        assertEquals(this.optionalUsuario, JUAN_PEREZ());
    }

    private void cuandoPidousuarioPorMail(String email) {
        this.optionalUsuario = this.usuarioDao.getUsuarioByEmail(email);
    }

    private void dadoQueJuanPerezEstaEnBd() {
        dadoUsuario(JUAN_PEREZ());
        cuandoHagoInsert();
    }

    private void dadoQueElMailDeJuanPerezAhoraEs(String nuevoMail) {
        this.usuario.setEmail(nuevoMail);
    }

    private void dadoUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void cuandoHagoInsert() {
        usuarioDao.alta(usuario);
    }

    private void cuandoHagoUpdate() {
        usuarioDao.modificacion(usuario);
    }


}
