package modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import controlador.Gestor;
@Entity(name = "Proveedor")
@Table(name = "PROVEEDOR")
public class Proveedor implements Serializable{
	private static final long serialVersionUID = 3425899765540828838L;
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@ManyToMany
	@JoinTable(name = "PROD_PROV", joinColumns = @JoinColumn(name = "ID_PROVEEDOR"), inverseJoinColumns = @JoinColumn(name = "ID_PRODUCTO"))
	private List<Producto> productosQueProvee;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Proveedor() {
		
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

	public List<Producto> getProductosQueProvee() {
		return productosQueProvee;
	}

	public void setProductosQueProvee(List<Producto> productosQueProvee) {
		this.productosQueProvee = productosQueProvee;
	}

	@Override
	public String toString() {
		return "Proveedor [id=" + id + ", nombre=" + nombre + "]";
	}
}
