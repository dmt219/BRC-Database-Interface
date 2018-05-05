import oracle.jdbc.proxy.annotation.Pre;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class Test {
    static PreparedStatement selectAllCustomers;
    static PreparedStatement credit;
    static PreparedStatement checkManager;
    static PreparedStatement updatePhysicalInvent;



    public static void main(String[] args) {
        int customerId;
        Connection con;
        while (true) {
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241", "dmt219", "maimai123");
                selectAllCustomers = con.prepareStatement("SELECT CUS_NAME FROM CUSTOMER where CUS_ID=?");
                credit=con.prepareStatement("INSERT INTO credit VALUES(?,?)");
                checkManager= con.prepareStatement("SELECT password FROM CREDENTIAL WHERE cus_id=1 AND username=?");
                updatePhysicalInvent=con.prepareStatement("UPDATE STORE_INVENTORY SET INVENTORY = ? where STORE_ID=1 and PROD_ID=1 ");


                break;
            } catch (Exception e) {
                System.out.println("Wrong username/password");
            }
        }
        System.out.println("Connection succesfully made");
        try {
            updatePhysicalInvent.setInt(1,2000);
            /*updatePhysicalInvent.setInt(2,1);
            updatePhysicalInvent.setInt(3,1);*/
            int rs = updatePhysicalInvent.executeUpdate();
            if (rs!=0)
                System.out.println("Success");
            else
                System.out.println("No such store or no such products");
        } catch (Exception e) {
            System.out.println("Error inserting to database");
        }
        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }

}
