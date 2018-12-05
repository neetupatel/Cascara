import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class MenuGeneration {
    private Connection conn = null;
    private Random ran;
    private ArrayList<Double> prices = new ArrayList<>();
    private ArrayList<String> items = new ArrayList<>();

    public MenuGeneration(Connection conn) {
        this.conn = conn;
        ran = new Random();
        ran.setSeed(713687);
        items.add("Peppermint Mocha");
        items.add("Nitro Cold Brew");
        items.add("Chai Tea Latte");
        items.add("Vanilla Cappuccino");
        prices.add(2.75);
        prices.add(3.00);
        prices.add(3.25);
        prices.add(4.25);
        prices.add(4.00);
        prices.add(1.50);
    }

    public void generateMenuItems(int toGenerate) {
        String query = "INSERT INTO menus (coffeeID, item, price) VALUES(?,?,?)";
        for (int i = 0; i < toGenerate; ++i) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, getCoffeeID());
                stmt.setString(2, getItem());
                stmt.setDouble(3,getPrice());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateAll() {
        String querya = "SELECT coffeeID FROM coffeeshops";
        ResultSet coffeeIDs = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(querya);
            coffeeIDs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String queryb = "INSERT INTO menus (coffeeID, item, price) VALUES(?,?,?)";
        try {
            while (coffeeIDs.next()) {
                for (int i = 0; i < 3; ++i) {
                    PreparedStatement stmt = conn.prepareStatement(queryb);
                    stmt.setInt(1, coffeeIDs.getInt("coffeeID"));
                    stmt.setString(2, getItem());
                    stmt.setDouble(3,getPrice());
                    stmt.executeUpdate();
                }
                // add Drip Coffee to every menu
                PreparedStatement stmt = conn.prepareStatement(queryb);
                stmt.setInt(1, coffeeIDs.getInt("coffeeID"));
                stmt.setString(2, "Drip Coffee");
                stmt.setDouble(3,getPrice());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getCoffeeID() {
        int theID = ran.nextInt(500);
        String query = "SELECT coffeeID FROM coffeeshops WHERE coffeeID = ?";
        while(true) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, theID);
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    //ID returns a match
                    break;
                } else {
                    //go again
                    theID = ran.nextInt(500);
                }
            } catch (SQLException e) {
                // something happened
            }
        }
        return theID;
    }

    public String getItem() {
        int ranIndex = ran.nextInt(4);
        return items.get(ranIndex);
    }

    public double getPrice() {
        int ranIndex = ran.nextInt(6);
        return prices.get(ranIndex);
    }
}
