package modelo;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Detalle")
@Table(name = "DETALLE")
@NamedQueries({
	@NamedQuery(name="SelectFromDetalle",query="SELECT d FROM Detalle d"),
	//@NamedQuery(name="SelectFromDetalleWhere",query="SELECT d FROM Detalle d WHERE d.NRO LIKE :NRO")
})
public class Detalle implements Serializable{
	private static final long serialVersionUID = 1545593860994203236L;
	
	@Id
	@JoinColumn(nullable = false, name = "NRO", table = "FACTURA")
	@OneToOne
	private Factura factura;
	
	@Id
	@JoinColumn(nullable = false, name = "ID", table = "PRODUCTO")
	@OneToOne
	private Producto producto;
	
	@Column(nullable = false, name = "CANTIDAD")
	private int cantidad;
	
	@Column(nullable = false, name = "PRECIO")
	private double precio;
	
	public int persist () {
		return Gestor.getInstance().persist(this);
	}
	
	public Detalle() {
		
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