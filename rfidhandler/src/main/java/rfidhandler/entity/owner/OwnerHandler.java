package rfidhandler.entity.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import rfidhandler.db.DBConnection;
import rfidhandler.entity.owner.Owner.Builder;

public abstract class OwnerHandler {
	
	public static Owner loadOwner(int ownerId) {
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM owner WHERE id=?");
            stmt.setInt(1, ownerId);
            ResultSet result = stmt.executeQuery();
            Owner owner = null;
            while(result.next()) {
            	
            	Builder builder = new Owner.Builder(result.getInt("id"))
            				.withName(result.getString("name"))
            				.withAddress(result.getString("address")
            			);
            	
            	owner = builder.build();
            	
            }
            stmt.close();
            result.close();
            connection.close();
            return owner;
        } catch(Exception exception) {
            System.out.println(exception);
        }
        
        return null;
	}
	
	public static ArrayList<Owner> getAll() {
		ArrayList<Owner> list = new ArrayList<>();
		
        try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM owner");
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
            	
            	Builder builder = new Owner.Builder(result.getInt("id"))
            				.withName(result.getString("name"))
            				.withAddress(result.getString("address")
            			);
            	
            	
            	list.add(builder.build());
            	
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
	
	public static int createOwner(String name, String address) {
		int newId = 1;
		
		try {
            Connection connection = DBConnection.getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO owner (name, address) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, address);
            
            stmt.execute();
            
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
            
            stmt.close();
            connection.close();
            
        } catch(Exception exception) {
            System.out.println(exception);
        }
		
		return newId;
	}
	
}
