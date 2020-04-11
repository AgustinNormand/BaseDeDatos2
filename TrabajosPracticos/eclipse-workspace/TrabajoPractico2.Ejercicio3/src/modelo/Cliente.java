package modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name = "Cliente")
public class Cliente {
	@Id
	@Column(name = "ID")
	private int id;
	@Column(name = "NOMBRE")
	private String nombre;
	@OneToMany
	private List<Factura> facturas;
	@OneToOne(cascade = { CascadeType.ALL } ) //Me deja ingresar el CLIENTE si la DIRECCION todavia no esta cargada en la DB, y guarda todo junto.
	private Direccion direccion;

	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Cliente() {
		
	}
	
	public Cliente(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", facturas=" + facturas + "]";
	}

}
