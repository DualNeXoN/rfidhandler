package rfidhandler.entity.animal;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import rfidhandler.db.DBConnection;
import rfidhandler.entity.animal.Animal.Builder;
import rfidhandler.entity.rfid.RfidUid;
import rfidhandler.entity.type.AnimalType;

public abstract class AnimalHandler {
	
	public static Animal loadAnimal(RfidUid uid) {
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT animal.id, animal.name, animal.dob, animal.image, animal.owner_id, animal_type.name FROM animal INNER JOIN animal_type ON animal.animal_type_id=animal_type.id WHERE animal.uid=?");
            stmt.setString(1, uid.getUidString());
            ResultSet result = stmt.executeQuery();
            Animal animal = null;
            while(result.next()) {
            	
            	Builder builder = new Animal.Builder(result.getInt("animal.id"), uid)
            				.withName(result.getString("animal.name"))
            				.withType(result.getString("animal_type.name"))
            				.withOwner(result.getInt("animal.owner_id")
            			);
            	
            	if(result.getBlob("animal.image") != null) {
            		builder.withImage(result.getBlob("animal.image"));
            	}
            	
            	if(result.getDate("animal.dob") != null) {
            		builder.withDob(result.getDate("animal.dob"));
            	}
            	
            	animal = builder.build();
            	
            }
            stmt.close();
            result.close();
            connection.close();
            return animal;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return null;
	}
	
	public static boolean changeImage(Animal animal, Blob blob) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE animal SET image=? WHERE id=?");
            stmt.setBlob(1, blob);
            stmt.setInt(2, animal.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return false;
	}
	
	public static boolean changeName(Animal animal, String name) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE animal SET name=? WHERE id=?");
            stmt.setString(1, name);
            stmt.setInt(2, animal.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return false;
	}
	
	public static boolean changeOwner(Animal animal, int ownerId) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE animal SET owner_id=? WHERE id=?");
            stmt.setInt(1, ownerId);
            stmt.setInt(2, animal.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return false;
	}
	
	public static boolean changeDob(Animal animal, Date dob) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE animal SET dob=? WHERE id=?");
            stmt.setDate(1, dob);
            stmt.setInt(2, animal.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return false;
	}
	
	public static boolean changeType(Animal animal, int typeId) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE animal SET animal_type_id=? WHERE id=?");
            stmt.setInt(1, typeId);
            stmt.setInt(2, animal.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return false;
	}
	
	public static ArrayList<AnimalType> getTypes() {
		ArrayList<AnimalType> list = new ArrayList<>();
		
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM animal_type");
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
            	list.add(new AnimalType(result.getInt("id"), result.getString("name")));
            }
            stmt.close();
            result.close();
            connection.close();
        } catch(Exception exception) {
            System.out.println(exception);
        }
		
		return list;
	}
	
	public static boolean createAnimal(String uid, String name, Date dob, int animalTypeId) {
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO animal (uid, name, dob, animal_type_id) VALUES (?, ?, ?, ?)");
            stmt.setString(1, uid);
            stmt.setString(2, name);
            stmt.setDate(3, dob);
            stmt.setInt(4, animalTypeId);
            
            stmt.execute();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
		
		return false;
	}
	
}
