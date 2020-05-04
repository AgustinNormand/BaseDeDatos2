package ModelController;

import javax.persistence.*;

@Entity
@Table(name = "TEST")
public class Test {
	
	@Id
	@Column(name = "ID_TEST")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + "]";
	}
	
	

}