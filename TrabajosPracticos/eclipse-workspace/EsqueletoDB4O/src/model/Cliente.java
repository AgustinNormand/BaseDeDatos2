package model;

public class Cliente {
	
	private int codigo;
	
	private String nombre;
	
	private double saldo;
	
	public Cliente(int codigo, String nombre, double saldo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.saldo = saldo;
	}



	public int getCodigo() {
		return codigo;
	}



	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public double getSaldo() {
		return saldo;
	}



	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}



	@Override
	public String toString() {
		return "Cliente [codigo=" + codigo + ", nombre=" + nombre + ", saldo=" + saldo + "]";
	}

}
