package modelo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Producto")
@Table(name = "PRODUCTO")
public class Producto implements Serializable{
	private static final long serialVersionUID = -882788105809591721L;
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "DESCR", length = 50)
	private String descr;
	
	@Column(name = "STOCK")
	private int stock;
	
	@Column(name = "PRECIO_BASE")
	private double precioBase;
	
	@Column(name = "PRECIO_COSTO")
	private double precioCosto;
	
	@ManyToMany
	@JoinColumn(table = "PROD_PROV", name = "ID_PROVEEDOR", nullable = false)
	private List<Proveedor> proveedores;
	
	@OneToMany(mappedBy = "producto")
	private List<Detalle> detalles;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}

	public double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", descr=" + descr + ", stock=" + stock + ", precioBase=" + precioBase
				+ ", precioCosto=" + precioCosto + ", proveedores=" + proveedores + "]";
	}
}
