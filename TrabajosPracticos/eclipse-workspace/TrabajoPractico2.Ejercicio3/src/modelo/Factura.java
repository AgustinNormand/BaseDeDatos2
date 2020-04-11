package modelo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Factura")
@Table(name = "FACTURA")
public class Factura implements Serializable{
	private static final long serialVersionUID = 619903870720941971L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY,generator = "GEN_NRO_FACTURA")
	@Column(nullable = false, name = "NRO")
	private int nro;
	
	@Column(nullable = false, name = "IMPORTE")
	private double importe;
	
	@Column(nullable = false, name = "ESTADO")
	private byte estado;
	
	@Column(nullable = false, name = "FECHA")
	private Date fecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;
	
	@OneToMany(mappedBy = "factura")
	private List<Detalle> detalles;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Factura() {
		
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
