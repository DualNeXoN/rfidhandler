package rfidhandler.entity.animal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import rfidhandler.App;
import rfidhandler.entity.owner.Owner;
import rfidhandler.entity.owner.OwnerHandler;
import rfidhandler.entity.rfid.RfidUid;
import rfidhandler.entity.vaccine.Vaccine;
import rfidhandler.entity.vaccine.VaccineHandler;

public class Animal {
	
	private int id;
	private RfidUid rfid;
	private String name = null;
	private Date dob = null;
	private String type = null;
	private Image image = null;
	private int ownerId;
	private Owner owner = null;
	private LinkedList<Vaccine> vaccines = null;
	
	public Animal(Builder builder) {
		this.id = builder.id;
		this.rfid = builder.rfid;
		this.name = builder.name;
		this.dob = builder.dob;
		this.type = builder.type;
		this.image = builder.image;
		this.ownerId = builder.ownerId;
	}
	
	public int getId() {
		return id;
	}
	
	public RfidUid getRfid() {
		return rfid;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getDob() {
		return dob;
	}
	
	public String getDobFormatted() {
		if(dob != null) return dob.toString();
		else return "unknown";
	}
	
	public String getType() {
		return type;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Owner getOwner() {
		if(owner == null) owner = OwnerHandler.loadOwner(ownerId);
		return owner;
	}
	
	public LinkedList<Vaccine> getVaccines() {
		refreshVaccines();
		return vaccines;
	}
	
	public void refreshVaccines() {
		vaccines = VaccineHandler.loadVaccines(this);
	}
	
	public static class Builder {
		
		private int id;
		private RfidUid rfid;
		
		private String name = null;
		private Date dob = null;
		private String type = null;
		private Image image = App.NO_IMAGE;
		private int ownerId;
		
		public Builder(int id, RfidUid rfid) {
			this.id = id;
			this.rfid = rfid;
		}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withDob(Date dob) {
			this.dob = dob;
			return this;
		}
		
		public Builder withType(String type) {
			this.type = type;
			return this;
		}
		
		public Builder withImage(Blob blob) {
			try {
				InputStream in = blob.getBinaryStream();
				this.image = SwingFXUtils.toFXImage(ImageIO.read(in), null);
				in.close();
			} catch (SQLException | IOException e) {}
			return this;
		}
		
		public Builder withOwner(int ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		public Animal build() {
			return new Animal(this);
		}
		
	}
	
}
