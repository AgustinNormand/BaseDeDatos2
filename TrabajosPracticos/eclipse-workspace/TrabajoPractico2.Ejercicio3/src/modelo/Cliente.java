package modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity(name = "Cliente")
@Table(name = "Cliente")
public class Cliente implements Serializable{
	private static final long serialVersionUID = -5871670784249780534L;
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@OneToMany(mappedBy = "cliente")
	private List<Factura> facturas;
	
	//@OneToOne(cascade = { CascadeType.ALL } ) //Me deja ingresar el CLIENTE si la DIRECCION todavia no esta cargada en la DB, y guarda todo junto.
	@JoinColumn(name = "ID_DIRECCION")
	private Direccion direccion;

	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Cliente() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", facturas=" + facturas + ", direccion=" + direccion + "]";
	}
}
