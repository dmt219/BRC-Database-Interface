import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class Main {
    /* Set of prepared statements for customers to interact with the database */
    static PreparedStatement newCus;
    static PreparedStatement currCus;
    static PreparedStatement listAllProds;
    static PreparedStatement listAllDrinks;
    static PreparedStatement listAllFoods;
    static PreparedStatement listAllDrugs;
    static PreparedStatement payMethod;
    static PreparedStatement purchase;
    static PreparedStatement virtual;
    static PreparedStatement physical;
    static PreparedStatement ship;
    static PreparedStatement takeOut;
    static PreparedStatement confirm;

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
        try{
            newCus= con.prepareStatement("INSERT INTO CUSTOMER VALUES (DEFAULT,?,?)");
        }
        catch (Exception e) {
        System.err.println("Error creating prepared statement");
        e.printStackTrace();
        }

        System.out.println("Enter 0 if you are a customer. Enter 1 if you are the manager");
        int role;
        Scanner sc = new Scanner(System.in);
        while(true) {
            role = sc.nextInt();
            if(role!=0 && role!=1){
                System.out.println("You can only enter 0 or 1");
            }
            else{
                break;
            }
        }
        if(role==0){
            System.out.println("Enter 0 if you are a returning customer, Enter 1 if you are an existing customer");
            int cus;
            while(true) {
                cus = sc.nextInt();
                if(cus!=0 && cus!=1){
                    System.out.println("You can only enter 0 or 1");
                }
                else{
                    break;
                }
            }
            if(cus==1){

            }
            else if (cus==0){

            }
        }
        else if(role==1){

        }



        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }
}