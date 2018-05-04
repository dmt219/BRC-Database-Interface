import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class Test {
    static PreparedStatement selectAllCustomers;
    static PreparedStatement credit;


    public static void main(String[] args) {
        int customerId;
        Connection con;
        while (true) {
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241", "dmt219", "maimai123");
                selectAllCustomers = con.prepareStatement("SELECT CUS_NAME FROM CUSTOMER where CUS_ID=?");
                credit=con.prepareStatement("INSERT INTO credit VALUES(?,?)");

                break;
            } catch (Exception e) {
                System.out.println("Wrong username/password");
            }
        }
        System.out.println("Connection succesfully made");
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("Please enter your card numbers without space");
            long cred=sc.nextLong();
            credit.setInt(1,204);
            credit.setLong(2,cred);
            int res = credit.executeUpdate();
            if(res!=0) {
                System.out.println("Your card is registered");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }

}
