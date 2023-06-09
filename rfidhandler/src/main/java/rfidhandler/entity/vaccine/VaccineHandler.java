package rfidhandler.entity.vaccine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;

import rfidhandler.db.DBConnection;
import rfidhandler.entity.animal.Animal;
import rfidhandler.entity.vaccine.Vaccine.Builder;

public abstract class VaccineHandler {
	
	public static LinkedList<Vaccine> loadVaccines(Animal animal) {
		
		LinkedList<Vaccine> list = new LinkedList<>();
		
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vaccination WHERE animal_id=? ORDER BY time DESC");
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
	
	public static boolean updateVaccine(Vaccine vaccine) {
		
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("UPDATE vaccination SET description=?, time=? WHERE id=?");
            stmt.setString(1, vaccine.getDescription());
            stmt.setTimestamp(2, vaccine.getTimestamp());
            stmt.setInt(3, vaccine.getId());
            
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
		
		return false;
	}
	
	public static boolean addVaccine(int animalId, Timestamp timestamp, String description) {
		
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO vaccination (animal_id, time, description) VALUES (?, ?, ?)");
            stmt.setInt(1, animalId);
            stmt.setTimestamp(2, timestamp);
            stmt.setString(3, description);
            
            stmt.execute();
            
            stmt.close();
            connection.close();
            return true;
        } catch(Exception exception) {
            System.out.println(exception);
        }
		
		return false;
	}
	
	public static boolean deleteVaccine(Vaccine vaccine) {
		
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM vaccination WHERE id=?");
            stmt.setInt(1, vaccine.getId());
            
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
