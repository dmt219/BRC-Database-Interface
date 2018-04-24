import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class SignIn{
    public static void main(String[] args){
        Connection con;
        while(true){
            try{
                Scanner scan = new Scanner(System.in);
                //get the username and password
                System.out.println("Enter your username");
                String username = scan.next();
                System.out.println("Enter your password");
                String password = scan.next();
                //connect
                con = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241",username,password);
                break;
            }catch (Exception e) {System.out.println("Wrong username/password");
            }
        }
        System.out.println("Connection succesfully made");
    }
}