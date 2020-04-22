/**
 * Entidad Cliente que pretendo persistir en tabla trcliente
 * de la base de datos firebird 2.0 /var/lib/firebird/2.0/data/tapasv.gdb
 * persisance.xml
 *
debe estar configurado para usar driver jdbc jaybird
 *
debe estar configurado para usar proveedor de persistencia
EclipseLink
 *
debe estar dentro de la carpeta META-INF del archivo jar a ejecutar
 *
 * @author G.Cherencio
 * @version 1.0
 */
import javax.persistence.*;
@Entity
@Table(name="trcliente")
public class Cliente
{
	// instance variables - replace the example below with your own
	@Id
	@Column(nullable=false)
	private int codigo;
	@Column(nullable=false,length=40)
	private String nombre;
	@Column(nullable=true,length=30)
	private String direc;
	@Column(nullable=true)
	private int postal;
	@Column(nullable=true,length=20)
	private String tel;
	/**
	 * Constructor for objects of class Cliente
	 */
	public Cliente()
	{
		// initialise instance variables
	}
	//SETTERS
	public void setCodigo(int c) { codigo=c; }
	public void setNombre(String s) { nombre=s; }
	public void setDirec(String s) { direc=s; }
	public void setPostal(int p) { postal=p; }
	public void setTel(String t) { tel=t; }
	//GETTERS
	public int getCodigo() { return codigo; }
	public String getNombre() { return nombre; }
	public String getDirec() { return direc; }
	public int getPostal() { return postal; }
	public String getTel() { return tel; }
}