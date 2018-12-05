import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class CoffeeAtmosphereGeneration {
    Connection conn;
    Random ran;

    CoffeeAtmosphereGeneration(Connection theConn) {
        conn = theConn;
        ran = new Random();
    }

    public void generateAll(){
        String query = "SELECT DISTINCT coffeeID, atmosphere FROM checkins";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet results = stmt.executeQuery();
            // insert every
            while(results.next()) {
                int id = results.getInt("coffeeID");
                String atmosphere = results.getString("atmosphere");
                String query2 = "INSERT INTO coffee_atmosphere (coffeeID, atmosphere) VALUES (?,?)";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setInt(1,id);
                stmt2.setString(2,atmosphere);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
