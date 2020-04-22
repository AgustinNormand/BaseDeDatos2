
public class Cliente {
	private int id;
	private String descr;
	public Cliente(int id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
	public int getId() {
		return id;
	}

	public String getDescr() {
		return descr;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", descr=" + descr + "]";
	}
	
}
