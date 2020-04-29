import java.lang.annotation.*; 
/**
 * Declaracion de anotacion propia que sirve para que otra clase
 * inyecte referencia a archivos secuenciales de entrada y salida
 * a ser utilizados en un proceso de tipo batch
 * 
 * @author G.Cherencio
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited  // jugar con esta anotacion y la clase MiProceso11
@interface Procesar
{
	String archivoEntrada() default "";
	String archivoSalida() default "";
	String archivoError() default "";
}
