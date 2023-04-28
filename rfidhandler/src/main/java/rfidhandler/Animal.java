package rfidhandler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Animal {
	
	private int id;
	private RfidUid rfid;
	private String name = null;
	private String type = null;
	private Image image = null;
	
	public Animal(Builder builder) {
		this.id = builder.id;
		this.rfid = builder.rfid;
		this.name = builder.name;
		this.type = builder.type;
		this.image = builder.image;
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
	
	public String getType() {
		return type;
	}
	
	public Image getImage() {
		return image;
	}
	
	public static class Builder {
		
		private int id;
		private RfidUid rfid;
		
		private String name = null;
		private String type = null;
		private Image image = App.NO_IMAGE;
		
		public Builder(int id, RfidUid rfid) {
			this.id = id;
			this.rfid = rfid;
		}
		
		public Builder withName(String name) {
			this.name = name;
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
		
		public Animal build() {
			return new Animal(this);
		}
		
	}
	
}
