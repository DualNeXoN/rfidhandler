package rfidhandler.entity.vaccine;

import java.sql.Timestamp;

import rfidhandler.entity.animal.Animal;

public class Vaccine {
	
	private int id;
	private Animal animal;
	private Timestamp timestamp;
	private String description;
	
	public Vaccine(Builder builder) {
		this.id = builder.id;
		this.animal = builder.animal;
		this.timestamp = builder.timestamp;
		this.description = builder.description;
	}
	
	public int getId() {
		return id;
	}
	
	public Animal getAnimal() {
		return animal;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static class Builder {
		
		private int id;
		private Animal animal;
		private Timestamp timestamp;
		
		private String description = null;
		
		public Builder(int id, Animal animal, Timestamp timestamp) {
			this.id = id;
			this.animal = animal;
		}
		
		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Vaccine build() {
			return new Vaccine(this);
		}
		
	}
	
}
