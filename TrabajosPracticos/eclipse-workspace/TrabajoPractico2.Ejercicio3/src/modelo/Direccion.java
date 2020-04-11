package modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Direccion")
@Table(name = "DIRECCION")
public class Direccion implements Serializable{
	private static final long serialVersionUID = -3220465368658502621L;

	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "CALLE")
	private String calle;
	
	@Column(name = "NRO")
	private int nro;
	
	@Column(name = "LOCALIDAD")
	private String localidad;
	
	@OneToOne(mappedBy = "direccion", fetch = FetchType.LAZY)
	private Cliente cliente;
	
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
	
	public Cliente getCliente() {
		return this.cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Direccion [id=" + id + ", calle=" + calle + ", nro=" + nro 
				+ ", localidad=" + localidad + ", cliente=" + cliente.getId() + "]";
	}
}
