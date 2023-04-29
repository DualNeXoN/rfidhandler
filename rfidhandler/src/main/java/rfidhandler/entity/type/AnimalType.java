package rfidhandler.entity.type;

public class AnimalType {
	
	private int id;
	private String name;
	
	public AnimalType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
}
