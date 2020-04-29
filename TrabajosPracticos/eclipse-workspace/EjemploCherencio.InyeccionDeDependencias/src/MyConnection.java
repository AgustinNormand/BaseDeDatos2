
import java.lang.annotation.*;
/**
 * Anotacion para instanciar (a traves de reflection) con una 
 * coneccion a b.d. firebird 2.5
 * 
 * @author G. Cherencio
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyConnection
{
}
