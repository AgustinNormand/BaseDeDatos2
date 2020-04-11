package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DIRECCION")
public class Direccion {
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "CALLE")
	private String calle;
	
	@Column(name = "NRO")
	private int nro;
	
	@Column(name = "LOCALIDAD")
	private String localidad;
	
	public int persist() {
		return Gestor.getInstance().persist(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getNro() {
		return nro;
	}

	public void setNro(int nro) {
		this.nro = nro;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Override
	public String toString() {
		return "Direccion [id=" + id + ", calle=" + calle + ", nro=" + nro + ", localidad=" + localidad + "]";
	}
}
