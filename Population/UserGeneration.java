import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/** This class contains everything needed to populate a user databse with the following schema:
 * users(INT PK UNIQUE userID, STRING first_name, STRING last_name, STRING email, STRING password, INT total_checkins)
 *
 * TO RUN: create an instance of this class in the main method and pass in a connection to your database.
 * Then, call the generateUsers() method with the specified parameter of how many users should be generated.
 */

public class UserGeneration {
    Connection conn = null;
    BufferedReader fnbr = null;
    BufferedReader lnbr = null;
    ArrayList<String> firstNames = new ArrayList<>();
    ArrayList<String> lastNames = new ArrayList<>();
    Random ran = new Random();
    int SEED = 713687;

    public UserGeneration(Connection conn) {
        try {
            fnbr = new BufferedReader(new FileReader("C:\\Users\\Murphy\\IdeaProjects\\CascaraGeneration\\src\\firstnames.txt"));
            lnbr = new BufferedReader(new FileReader("C:\\Users\\Murphy\\IdeaProjects\\CascaraGeneration\\src\\lastnames.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.conn = conn;
        readInNames(fnbr, firstNames);
        readInNames(lnbr, lastNames);
        ran.setSeed(713687);
    }

    public void readInNames(BufferedReader reader, ArrayList<String> nameList) {
        try {
            String line = reader.readLine();
            while (line != null) {
                nameList.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateUsers(int usersToAdd) {
        int userID = 0;
        String fname = "";
        String lname = "";
        String email = "";
        String password = "";

        String query = "INSERT INTO users VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            for(int i = 1; i <= usersToAdd; ++i) {
                userID = getUniqueID();
                fname = firstNames.get(ran.nextInt(5100 + 1));
                lname = lastNames.get(ran.nextInt(5100 + 1));
                email = fname.charAt(0) + lname + Integer.toString(ran.nextInt(20+1)) +"@gmail.com";
                password = getPassword();

                stmt.setLong(1,userID);
                stmt.setString(2, fname);
                stmt.setString(3, lname);
                stmt.setString(4, email);
                stmt.setString(5, password);
                stmt.setInt(6, ran.nextInt(1000 + 1));
                stmt.executeUpdate();
                System.out.println("Added user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUniqueID() {
        int theID = 0;
        // generates a random long 8 numbers in length
        theID = ran.nextInt(10000000) + 10000;

        String query = "SELECT userID FROM users WHERE userID = ?";
        while (true) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, theID);
                ResultSet result = stmt.executeQuery();
                if (!result.next()) {
                    break;
                } else {
                    theID = ran.nextInt(10000000) + 10000;
                }
                // if this works, it means a matching ID exists and we must find another
            } catch(SQLException e){
                // no match exists, we can use this ID
                System.out.println("Generated ID");
                break;
            }
        }
        return theID;
    }

    public String getPassword() {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String numeric = "0123456789";
        String pass = "";
        for (int i = 0; i < 8; ++i) {
            pass += alpha.charAt(ran.nextInt(alpha.length() - 1));
        }
        for (int i = 0; i < 3; ++ i) {
            pass += numeric.charAt(ran.nextInt(numeric.length() - 1));
        }
        return pass;
    }


}
