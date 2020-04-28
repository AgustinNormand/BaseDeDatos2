
public class Factura {
	private int nro;
	private int id;
	private double importe;
	public Factura() {
		
	}
	public Factura(int nro, int id, double importe) {
		this.nro = nro;
		this.id = id;
		this.importe = importe;
	}
	
	public int getNro() {
		return nro;
	}
	public int getId() {
		return id;
	}
	public double getImporte() {
		return importe;
	}
	public void setNro(int nro) {
		this.nro = nro;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	@Override
	public String toString() {
		return "Factura [nro=" + nro + ", id=" + id + ", importe=" + importe + "]";
	}
	
	

}
