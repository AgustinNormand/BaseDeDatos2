package modelo;
import java.util.Date;
import java.util.Scanner;

public class Main {
	private static Scanner in = new Scanner(System.in); 
	public static void main(String[] args) {
	}
	private static void insertIntoDetalle() {
		Detalle detalle = new Detalle();
		System.out.println("Ingresar el NRO del DETALLE");
		detalle.setFactura(Gestor.getInstance().selectFacturaWhere(in.nextInt()));
		System.out.println("Ingresar el ID del DETALLE");
		detalle.setProducto(Gestor.getInstance().selectProductoWhere(in.nextInt()));
		System.out.println("Ingresar la CANTIDAD del DETALLE");
		detalle.setCantidad(in.nextInt());
		System.out.println("Ingreasr el PRECIO del DETALLE");
		detalle.setPrecio(in.nextDouble());
		detalle.persist();
	}
	private static void insertIntoProducto() {
		Producto producto = new Producto();
		System.out.println("Ingresar el ID del PRODUCTO");
		producto.setId(in.nextInt());
		System.out.println("Ingresar la DESCRIPCION del PRODUCTO");
		producto.setDescr(in.next());
		System.out.println("Ingresar el STOCK del PRODUCTO");
		producto.setStock(in.nextInt());
		System.out.println("Ingresar el PRECIO BASE del PRODUCTO");
		producto.setPrecioBase(in.nextDouble());
		System.out.println("Ingresar el PRECIO COSTO del PRODUCTO");
		producto.setPrecioCosto(in.nextDouble());
		producto.persist();
	}
	private static void deleteFromFacturaWhere() {
		System.out.println("Ingresar el numero de la factura");
		Factura factura = Gestor.getInstance().selectFacturaWhere(in.nextInt());
		Gestor.getInstance().removeFactura(factura);
		
	}
	private static void selectFacturaWhere() {
		System.out.println("Ingresar el numero de la factura");
		Factura factura = Gestor.getInstance().selectFacturaWhere(in.nextInt());
		if (factura != null)
			System.out.println(factura);
		else
			System.out.println("Factura no encontrada");
	}
	public static void insertarFactura() {
		Factura factura = new Factura();
		System.out.println("Ingresar NRO de FACTURA");
		factura.setNro(in.nextInt());
		System.out.println("Ingresar IMPORTE de FACTURA");
		factura.setImporte(in.nextDouble());
		factura.setFecha(new Date());
		factura.persist();
	}
	public static void query() {
		System.out.println(Gestor.getInstance().selectAllFacturas());
	}
}
