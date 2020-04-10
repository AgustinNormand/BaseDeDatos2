package modelo;

import java.util.List;

public class Cliente {
	
	private int id;
	
	private String nombre;
	
	private List<Factura> facturas;

	public void persist() {
		Gestor.getInstance().persist(this);
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
