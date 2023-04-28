package rfidhandler.entity.animal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import rfidhandler.DBConnection;
import rfidhandler.RfidUid;
import rfidhandler.entity.animal.Animal.Builder;

public abstract class AnimalHandler {
	
	public static Animal loadAnimal(RfidUid uid) {
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT animal.id, animal.name, animal.image, animal_type.name FROM animal INNER JOIN animal_type ON animal.animal_type_id=animal_type.id WHERE animal.uid=?");
            stmt.setString(1, uid.getUidString());
            ResultSet result = stmt.executeQuery();
            Animal animal = null;
            while(result.next()) {
            	
            	Builder builder = new Animal.Builder(result.getInt("animal.id"), uid)
            				.withName(result.getString("animal.name"))
            				.withType(result.getString("animal_type.name")
            			);
            	
            	if(result.getBlob("animal.image") != null) {
            		builder.withImage(result.getBlob("animal.image"));
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
	
}
