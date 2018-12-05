import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class AmenitiesGeneration {
    Connection conn;
    Random ran;

    AmenitiesGeneration(Connection theConn) {
        conn = theConn;
        ran = new Random();
    }

    public void generateAmenities(int toGenerate) {
        String query = "INSERT INTO coffee_amenities (coffeeID, amenity) VALUES (?,?)";
        for (int i = 0; i < toGenerate; ++i) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1,getCoffeeID());
                stmt.setString(2, getAmenity());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCoffeeID() {
        int theID = 0;
        String query = "SELECT coffeeID FROM coffeeshops WHERE coffeeID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            int randomIDguess = ran.nextInt(450);
            stmt.setInt(1, randomIDguess);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                //it's a valid ID!
                theID = randomIDguess;
            } else {
                //go again
                theID = randomIDguess;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Got coffee ID");
        return theID;
    }

    public String getAmenity() {
        String[] possible = {"Live Music","Power Outlets","Vegan Milk","Pastries","Food","Outdoor Seating","Pet Friendly"};
        return possible[ran.nextInt(7)];
    }
}
