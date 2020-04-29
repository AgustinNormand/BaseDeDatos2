import java.lang.annotation.*; 
/**
 * Declaracion de anotacion propia que sirve para anotar los atributos
 * de clases que a su vez estan anotadas como @javax.persistence.Entity
 * y poder darles un nombre de columna a visualizar en JTable
 * Se pueden agregar otros atributos a esta anotacion, como por ejemplo 
 * el ancho por defecto de la columna, color, etc. etc. que hace a la 
 * visualizacion de la misma en la grilla.
 * 
 * @author G.Cherencio
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Columna
{
	String nombre() default "";
}