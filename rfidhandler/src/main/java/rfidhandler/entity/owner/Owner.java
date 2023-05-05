package rfidhandler.entity.owner;

public class Owner {
	
	private int id;
	private String name = null;
	private String address = null;
	
	public Owner(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.address = builder.address;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		return name + " (" + address + ")";
	}
	
	public static class Builder {
		
		private int id;
		
		private String name = null;
		private String address = null;
		
		public Builder(int id) {
			this.id = id;
		}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withAddress(String address) {
			this.address = address;
			return this;
		}
		
		public Owner build() {
			return new Owner(this);
		}
		
	}
	
}
