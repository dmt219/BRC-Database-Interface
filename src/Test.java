import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class Test {
    static PreparedStatement selectAllCustomers;

    public static void main(String[] args) {
        int customerId;
        Connection con;
        while (true) {
            try {
                Scanner scan = new Scanner(System.in);
                //get the username and password
                System.out.println("Enter your username");
                String username = scan.next();
                System.out.println("Enter your password");
                String password = scan.next();
                //connect
                con = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241", username, password);
                selectAllCustomers = con.prepareStatement("SELECT CUS_NAME FROM CUSTOMER where CUS_ID=?");
                break;
            } catch (Exception e) {
                System.out.println("Wrong username/password");
            }
        }
        System.out.println("Connection succesfully made");
        try{
            selectAllCustomers.setInt(1,51);
            ResultSet res = selectAllCustomers.executeQuery();
            if(res.next())
                System.out.println(res.getString("CUS_NAME"));
        }catch(Exception e){System.out.println("Error");};


        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }

}
