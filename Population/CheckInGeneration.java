import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/** @Author: Murphy Studebaker
 * Project: Cascara Coffee Application
 *
 * This class populates a MySQL datatable with the following schema:
 * checkins(INT PK UNIQUE checkinID, INT FK userID, INT FK coffeeID, DOUBLE coffeeScore,
 * DOUBLE wifiScore, BOOLEAN outlets, BOOLEAN seating, VARCHAR(45) order, TIME time)
 * */
public class CheckInGeneration {
    private Connection conn = null;
    private Random ran;

    public CheckInGeneration(Connection conn) {
        this.conn = conn;
        ran = new Random();
        ran.setSeed(713687);
    }

    public void generateCheckIn(int toGenerate) {
        String query = "INSERT INTO checkins (userID, coffeeID, coffeeScore, wifiScore, order_item, submit_time, atmosphere) VALUES (?,?,?,?,?,?,?)";
        System.out.println("Starting generation");
        try {
            for (int i = 0; i < toGenerate; ++i) {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, getUserID());
                int coffeeID = getCoffeeID();
                stmt.setInt(2, coffeeID);
                stmt.setDouble(3, Math.round(100*(5*ran.nextDouble()))/100);
                stmt.setDouble(4, Math.round(100*(5*ran.nextDouble()))/100);
                stmt.setString(5, getMenuItem(coffeeID));
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                stmt.setTimestamp(6,currentTime);
                stmt.setString(7,getAtmosphere());
                stmt.executeUpdate();
                System.out.println("Executed update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("generation failed" + e.getMessage());
        }
    }


    public int getUserID() {
        String query = "SELECT userID FROM users WHERE total_checkins = ?";
        int userID = 0;
        int checkinQuery = ran.nextInt(1000+1);

        //keep generating a random checkin number until we get a match for an ID
        while(true) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, checkinQuery);
                ResultSet results = stmt.executeQuery();

                if (results.next()) {
                    userID = results.getInt(1);
                    break;
                } else {
                    //no match found, go for another loop
                    checkinQuery = ran.nextInt(1000+1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Got user ID");
        return userID;
    }

    public String getMenuItem(int coffeeID) {
        String query = "SELECT item FROM menus WHERE coffeeID = ? GROUP BY item";
        String order = "";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,coffeeID);
            ResultSet results = stmt.executeQuery();
            ArrayList<String> possibleOrders = new ArrayList<>();
            while(results.next()) {
                possibleOrders.add(results.getString("item"));
            }
            if (possibleOrders.size() <= 0) {
                return null;
            } else {
                int randomIndex = ran.nextInt(possibleOrders.size());
                order = possibleOrders.get(randomIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Got menu item");
        return order;
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

    public String getAtmosphere() {
        String[] atmosphere = {"Minimalist","Art Chique","Industrial","Cozy"};
        System.out.println("Got atmosphere");
        return atmosphere[ran.nextInt(4)];
    }
}
