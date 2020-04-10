package modelo;

import java.io.Serializable;
import java.util.List;

public class Proveedor implements Serializable{
	private static final long serialVersionUID = 3425899765540828838L;

	private int id;
	
	private String nombre;
	
	private List<Producto> productosQueProvee;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}
	
	public Proveedor() {
		
	}
	
	public Proveedor(int id, String nombre, List<Producto> productosQueProvee) {
		this.id = id;
		this.nombre = nombre;
		this.productosQueProvee = productosQueProvee;
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
		return "Proveedor [id=" + id + ", nombre=" + nombre + ", productosQueProvee=" + productosQueProvee + "]";
	}
}
