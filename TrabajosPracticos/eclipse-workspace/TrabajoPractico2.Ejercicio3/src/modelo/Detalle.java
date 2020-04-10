package modelo;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="DETALLE")
public class Detalle implements Serializable{
	private static final long serialVersionUID = 1545593860994203236L;
	@Id
	@JoinColumn(nullable=false,name="NRO")
	@OneToOne
	private Factura factura;
	@Id
	@JoinColumn(nullable=false,name="ID")
	@OneToOne
	private Producto producto;
	@Column(nullable=false,name="CANTIDAD")
	private int cantidad;
	@Column(nullable=false,name="PRECIO")
	private double precio;
	
	public int persist () {
		return Gestor.getInstance().persist(this);
	}
	
	public Detalle() {
		
	}
	
	public Detalle(Factura factura, Producto producto, int cantidad, double precio) {
		this.factura = factura;
		this.producto = producto;
		this.cantidad = cantidad; 
		this.precio = precio;
	}
	
	public Factura getFactura() {
		return factura;
	}
	
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		return "Detalle [factura=" + factura + ", producto=" + producto + ", cantidad=" + cantidad + ", precio="
				+ precio + "]";
	}
}