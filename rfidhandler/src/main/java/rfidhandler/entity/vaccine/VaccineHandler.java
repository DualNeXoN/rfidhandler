package rfidhandler.entity.vaccine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import rfidhandler.DBConnection;
import rfidhandler.entity.animal.Animal;
import rfidhandler.entity.vaccine.Vaccine.Builder;

public abstract class VaccineHandler {
	
	public static LinkedList<Vaccine> loadVaccines(Animal animal) {
		
		LinkedList<Vaccine> list = new LinkedList<>();
		
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vaccination WHERE animal_id=?");
            stmt.setInt(1, animal.getId());
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
            	
            	Builder builder = new Vaccine.Builder(result.getInt("id"), animal, result.getTimestamp("time"));
            	
            	if(result.getString("description") != null) {
            		builder.withDescription(result.getString("description"));
            	}
            	
            	list.addLast(builder.build());
            }
            stmt.close();
            result.close();
            connection.close();
            return list;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return null;
	}
	
}
