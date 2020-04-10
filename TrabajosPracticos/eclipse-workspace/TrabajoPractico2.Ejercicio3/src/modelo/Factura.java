package modelo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FACTURA")
public class Factura implements Serializable{
	private static final long serialVersionUID = 619903870720941971L;
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY,generator = "GEN_NRO_FACTURA")
	@Column(nullable=false,name="NRO",columnDefinition="INTEGER")
	private int nro;
	@Column(nullable=false,name="IMPORTE",columnDefinition="DOUBLE")
	private double importe;
	@Column(nullable=false,name="ESTADO",columnDefinition="INTEGER")
	private byte estado;
	@Column(nullable=false,name="FECHA",columnDefinition="DATE DEFAULT CURRENT_DATE")
	private Date fecha;
	
	private Cliente cliente;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Factura() {
		
	}
	
	public Factura(int nro, double importe, byte estado, Date fecha, Cliente cliente) {
		this.nro = nro;
		this.importe = importe;
		this.estado = estado;
		this.fecha = fecha;
		this.cliente = cliente;
	}
	
	public int getNro() {
		return nro;
	}

	public void setNro(int nro) {
		this.nro = nro;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public byte getEstado() {
		return estado;
	}

	public void setEstado(byte estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Factura [nro=" + nro + ", importe=" + importe + ", estado=" + estado + ", fecha=" + fecha + ", cliente="
				+ cliente + "]";
	}
}
