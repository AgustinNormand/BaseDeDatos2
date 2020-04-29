/**
 * Declaracion de anotacion propia que luego sera incluida como parte de
 * la documentacion de clases usando javadoc
 * 
 * @author G.Cherencio
 * @version 1.0
 */
import java.lang.annotation.*; // necesario para usar anotacion Documented
@Documented
@interface Documentar {
    String autor() default "G.Cherencio";
    String fecha() default "01/01/1980";
    int revision() default 1;
    String ultimaModificacion() default "N/A";
    String ultimaModificacionPor() default "N/A";
    String[] revisores() default {};
}
