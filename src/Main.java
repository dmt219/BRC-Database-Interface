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
    static PreparedStatement addToCart;
    static PreparedStatement newTransaction;
    static PreparedStatement searchProd;
    static PreparedStatement payMethod;
    static PreparedStatement purchase;
    static PreparedStatement virtual;
    static PreparedStatement physical;
    static PreparedStatement ship;
    static PreparedStatement takeOut;
    static PreparedStatement confirm;

    public static void main(String[] args){
        int customerId=-1;
        int transactionId=-1;
        Connection con;
        /*establish connection*/
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
                newCus= con.prepareStatement("INSERT INTO CUSTOMER VALUES (DEFAULT,?,?)",Statement.RETURN_GENERATED_KEYS);
                currCus= con.prepareStatement("SELECT CUS_NAME FROM CUSTOMER WHERE CUS_ID=?");
                listAllProds=con.prepareStatement("SELECT * FROM PRODUCTS");
                listAllDrinks=con.prepareStatement("SELECT * FROM DRINK NATURAL JOIN PRODUCTS");
                listAllFoods=con.prepareStatement("SELECT * FROM FOOD NATURAL JOIN PRODUCTS");
                listAllDrugs=con.prepareStatement("SELECT * FROM DRUG NATURAL JOIN PRODUCTS");
                newTransaction=con.prepareStatement("INSERT INTO TRANSACTION VALUES(DEFAULT,?),Statement.RETURN_GENERATED_KEYS");
                addToCart=con.prepareStatement("INSERT INTO TRANSAC_CONTAIN VALUES(?,?,?)");
                break;
            }catch (Exception e) {System.out.println("Wrong username/password");
            }
        }
        System.out.println("Connection succesfully made");
        /*choose role*/
        System.out.println("Which role would you like to sign in as?");
        System.out.println("0: Customer");
        System.out.println("1: Manager");
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
        /*customer*/
        if(role==0){
            System.out.println("Are you a new customer or a returning customer?");
            System.out.println("0: New Customer");
            System.out.println("1: Returning Customer");
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
            /*returning*/
            if(cus==1) {
                System.out.println("Please enter your customer ID");
                while (true) {
                    try {
                        customerId= sc.nextInt();
                        currCus.setInt(1,customerId);
                        ResultSet rs =currCus.executeQuery();
                        if(rs.next())
                        System.out.println("Hello "+ rs.getString("CUS_NAME"));
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid ID. Please try again");
                    }
                }
            }
            /*new*/
            else if (cus==0){
                System.out.println("We would like to help you create your new account");
                System.out.println("Please enter your first name");
                String name = sc.nextLine();
                sc.nextLine();
                System.out.println("Please enter your address");
                String address = sc.nextLine();
                try{
                    newCus.setString(1,name);
                    newCus.setString(2,address);
                    int rs = newCus.executeUpdate();
                    ResultSet res = newCus.getGeneratedKeys();
                    if (res.next()) {
                        customerId=res.getInt(1);
                        System.out.println("Your ID is " + customerId + ". Please remember it for future login");
                    }
                } catch(Exception e){
                    System.out.println("Error inserting customer");
                }

            }
            System.out.println("Welcome to BRC Online Market!");
            System.out.println("Which of these options would you like to access?");
            /*options*/
            System.out.println("0: View products and manage your cart");
            System.out.println("1: Make a purchase");
            int option;
            while(true){
                option = sc.nextInt();
                if(option!=0 && option!=1){
                    System.out.println("You can only enter 0 or 1");
                }
                else{
                    break;
                }
            }
            /*Create a new transaction*/
            try{
                newTransaction.setInt(1,customerId);
                int rs = newTransaction.executeUpdate();
            } catch(Exception e){
                System.out.println("Error creating transaction");
            }

            /*view products*/
            while(option==0){
                System.out.println("What would you like to view");
                System.out.println("0: View all products");
                System.out.println("1: View all drinks");
                System.out.println("2: View all foods");
                System.out.println("3: View all drugs");

                int view;
                while(true){
                    view = sc.nextInt();
                    if(view!=0 && view!=1 && view!=2 && view!=3){
                        System.out.println("You can only enter 0,1,2 or 3");
                    }
                    else{
                        break;
                    }
                }

            //view all products
                if(view==0){
                    try{
                        ResultSet rs = listAllProds.executeQuery();
                        System.out.format("%7s%60s\n","Products ID","Products Name");
                        while(rs.next()) {
                            System.out.format("%7d%60s\n", rs.getInt("PROD_ID"),rs.getString("PROD_NAME"));
                        }
                    } catch(Exception e){
                        System.out.println("Error listing products");
                    }

                }

                else if(view==1){
                    try{
                        ResultSet rs = listAllDrinks.executeQuery();
                        System.out.format("%7s%60s%60s\n","Products ID","Manufacturer","Products Name");
                        while(rs.next()) {
                            System.out.format("%7d%60s%60s\n", rs.getInt("PROD_ID"),rs.getString("MANUFACTURER"),rs.getString("PROD_NAME"));
                        }
                    } catch(Exception e){
                        System.out.println("Error listing products");
                    }

                }

                else if(view==2){
                    try{
                        ResultSet rs = listAllFoods.executeQuery();
                        System.out.format("%7s%60s%60s\n","Products ID","Manufacturer","Products Name");
                        while(rs.next()) {
                            System.out.format("%7d%60s%60s\n", rs.getInt("PROD_ID"),rs.getString("MANUFACTURER"),rs.getString("PROD_NAME"));
                        }
                    } catch(Exception e){
                        System.out.println("Error listing products");
                    }

                }

                else{
                    try{
                        ResultSet rs = listAllDrugs.executeQuery();
                        System.out.format("%7s%60s%60s\n","Products ID","Manufacturer","Products Name");
                        while(rs.next()) {
                            System.out.format("%7d%60s%60s\n", rs.getInt("PROD_ID"),rs.getString("MANUFACTURER"),rs.getString("PROD_NAME"));
                        }
                    } catch(Exception e){
                        System.out.println("Error listing products");
                    }

                }
                System.out.println("Would you like to view a different category?");
                System.out.println("0: YES");
                System.out.println("1: NO. Take me to purchase window");
                int cat;
                while(true){
                    cat = sc.nextInt();
                    if(cat!=0 && cat!=1){
                        System.out.println("You can only enter 0 or 1");
                    }
                    else{
                        break;
                    }
                }
                if(cat==1){
                    option=1;
                }


            }
            while(option==1){
                System.out.println("Welcome to purchase window");
                System.out.println("");
            }







        }
        else if(role==1){
            return;
        }



        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }
}