import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class Main {
    /* Set of prepared statements for customers to interact with the database */
    static PreparedStatement newCus;
    static PreparedStatement currCus;
    static PreparedStatement getMaxID;
    static PreparedStatement newCred;
    static PreparedStatement getProdName;
    static PreparedStatement listAllProds;
    static PreparedStatement listAllProdsOnline;
    static PreparedStatement listAllPhysicalStores;
    static PreparedStatement listAllDrinks;
    static PreparedStatement listAllFoods;
    static PreparedStatement listAllDrugs;
    static PreparedStatement listCart;
    static PreparedStatement addToCart;
    static PreparedStatement getMaxTransacId;
    static PreparedStatement newTransaction;
    static PreparedStatement newVirtualTransaction;
    static PreparedStatement newShip;
    static PreparedStatement newTakeOut;
    static PreparedStatement checkVirtualQuantity;
    static PreparedStatement listallProdsOffline;
    static PreparedStatement checkPhysicalOneQuantity;
    static PreparedStatement checkPhysicalTwoQuantity;
    static PreparedStatement cash;
    static PreparedStatement credit;
    static PreparedStatement debit;
    static PreparedStatement searchProd;
    static PreparedStatement payMethod;
    static PreparedStatement purchase;
    static PreparedStatement virtual;
    static PreparedStatement physical;
    static PreparedStatement ship;
    static PreparedStatement takeOut;
    static PreparedStatement checkEmtpyCart;
    static PreparedStatement checkManager;
    static PreparedStatement listAllCustomers;
    static PreparedStatement updatePhysicalInvent;
    static PreparedStatement getMaxSellID;
    static PreparedStatement getGoods;
    static PreparedStatement checkPhysicalQuantity;
    static PreparedStatement addToStore;



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
                newCus= con.prepareStatement("INSERT INTO CUSTOMER VALUES (?,?,?)");
                newCred= con.prepareStatement("INSERT INTO CREDENTIAL VALUES (?,?,?)");
                getMaxID=con.prepareStatement("SELECT MAX(CUS_ID) FROM CUSTOMER");
                getMaxTransacId=con.prepareStatement("SELECT MAX(TRANSAC_ID) FROM TRANSACTION");
                currCus= con.prepareStatement("SELECT cus_name, password, cus_id FROM CUSTOMER NATURAL JOIN CREDENTIAL WHERE username=?");
                checkManager= con.prepareStatement("SELECT password FROM CREDENTIAL WHERE cus_id=1 AND username=?");
                listAllProds=con.prepareStatement("SELECT * FROM PRODUCTS");
                listAllCustomers=con.prepareStatement("SELECT * FROM CUSTOMER");
                listAllProdsOnline=con.prepareStatement("SELECT PROD_ID, PROD_NAME, INVENTORY FROM STORE_INVENTORY NATURAL JOIN PRODUCTS WHERE STORE_ID=1");
                listAllDrinks=con.prepareStatement("SELECT * FROM DRINK NATURAL JOIN PRODUCTS");
                listallProdsOffline=con.prepareStatement("SELECT STORE_ID, PROD_ID, PROD_NAME, INVENTORY FROM STORE_INVENTORY NATURAL JOIN PRODUCTS WHERE STORE_ID!=1");
                listAllFoods=con.prepareStatement("SELECT * FROM FOOD NATURAL JOIN PRODUCTS");
                listAllDrugs=con.prepareStatement("SELECT * FROM DRUG NATURAL JOIN PRODUCTS");
                newTransaction=con.prepareStatement("INSERT INTO TRANSACTION VALUES(?,?)");
                newVirtualTransaction=con.prepareStatement("INSERT INTO VIRTUAL VALUES(?)");
                newShip=con.prepareStatement("INSERT INTO SHIP VALUES(?)");
                newTakeOut=con.prepareStatement("INSERT INTO TAKEOUT VALUES(?)");
                checkVirtualQuantity=con.prepareStatement("SELECT INVENTORY FROM STORE_INVENTORY WHERE STORE_ID=1 AND PROD_ID=?");
                checkPhysicalOneQuantity=con.prepareStatement("SELECT INVENTORY FROM STORE_INVENTORY WHERE STORE_ID=2 AND PROD_ID=?");
                checkPhysicalTwoQuantity=con.prepareStatement("SELECT INVENTORY FROM STORE_INVENTORY WHERE STORE_ID=3 AND PROD_ID=?");
                checkPhysicalQuantity=con.prepareStatement("SELECT INVENTORY FROM STORE_INVENTORY WHERE STORE_ID=? AND PROD_ID=?");
                listAllPhysicalStores=con.prepareStatement("SELECT * FROM OFFLINE_WAREHOUSE");
                addToCart=con.prepareStatement("INSERT INTO TRANSAC_CONTAIN VALUES(?,?,?)");
                cash=con.prepareStatement("INSERT INTO CASH VALUES(?,?)");
                credit=con.prepareStatement("INSERT INTO credit VALUES(?,?)");
                debit=con.prepareStatement("INSERT INTO debit VALUES(?,?)");
                getProdName=con.prepareStatement("SELECT PROD_NAME FROM PRODUCTS WHERE PROD_ID=?");
                listCart = con.prepareStatement("SELECT PROD_NAME, QUANTITY FROM TRANSAC_CONTAIN NATURAL JOIN PRODUCTS WHERE TRANSAC_ID=?");
                checkEmtpyCart=con.prepareStatement("SELECT QUANTITY FROM TRANSAC_CONTAIN WHERE TRANSAC_ID=? ");
                updatePhysicalInvent=con.prepareStatement("update STORE_INVENTORY SET INVENTORY = ? where STORE_ID=? and PROD_ID=? ");
                getMaxSellID=con.prepareStatement("SELECT MAX(SELL_ID) FROM SELL");
                getGoods=con.prepareStatement("INSERT INTO SELL VALUES(?,?,?,?,?)");
                addToStore=con.prepareStatement("INSERT INTO STORE_INVENTORY VALUES(?,?,?)");


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
        if(role==0) {
            System.out.println("Are you a new customer or a returning customer?");
            System.out.println("0: New Customer");
            System.out.println("1: Returning Customer");
            int cus;
            while (true) {
                cus = sc.nextInt();
                if (cus != 0 && cus != 1) {
                    System.out.println("You can only enter 0 or 1");
                } else {
                    break;
                }
            }
            sc.nextLine();
            String cus_usr = null;
            String cus_pass = null;
            String cus_name = null;
            /*returning*/
            if (cus == 1) {
                System.out.println("Please enter your customer username");
                while (true) {
                    try {
                        String tempPass = null;
                        cus_usr = sc.nextLine();
                        currCus.setString(1, cus_usr);
                        ResultSet rs = currCus.executeQuery();
                        if (rs.next()) {
                            cus_pass = rs.getString("password");
                            cus_name = rs.getString("cus_name");
                            customerId = rs.getInt("cus_id");
                        }
                        System.out.println("Please enter your password");
                        tempPass = sc.nextLine();
                        if (cus_pass.equals(tempPass)) {
                            System.out.println("Hello " + cus_name + " !");
                            break;
                        } else {
                            System.out.println("Wrong password");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid username. Please try again");
                    }
                }
            }
            /*new*/
            else if (cus == 0) {
                try {
                    ResultSet rs = getMaxID.executeQuery();
                    if (rs.next())
                        customerId = rs.getInt(1);
                } catch (Exception e) {
                    System.out.println("Error retrieving ID");
                }
                System.out.println("We would like to help you create your new account");
                System.out.println("Please enter your name");
                String name = sc.nextLine();
                System.out.println("Please enter your address");
                String address = sc.nextLine();
                try {
                    customerId++;
                    newCus.setInt(1, customerId);
                    newCus.setString(2, address);
                    newCus.setString(3, name);
                    int rs = newCus.executeUpdate();
                    if (rs != 0) {
                        System.out.println("Thank you " + name);
                        System.out.println("Please enter your prefered username for future log in");
                        cus_usr = sc.nextLine();
                        System.out.println("Please enter your prefered password");
                        cus_pass = sc.nextLine();
                        try {
                            newCred.setInt(1, customerId);
                            newCred.setString(2, cus_usr);
                            newCred.setString(3, cus_pass);
                            int res = newCred.executeUpdate();
                            if (res != 0)
                                System.out.println("Account created");
                        } catch (Exception e) {
                            System.out.println("Error retrieving ID");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error inserting customer");
                }

            }
            System.out.println("Welcome to BRC Online Marketplace!");
            while (true) {
                System.out.println("Which of these options would you like to access?");
                /*options*/
                System.out.println("0: View products");
                System.out.println("1: Add products to cart");
                System.out.println("2: View Cart");
                System.out.println("3: Checkout");
                int option;
                while (true) {
                    option = sc.nextInt();
                    if (option != 0 && option != 1 && option != 2 && option != 3) {
                        System.out.println("You can only enter 0,1,2 or 3");
                    } else {
                        break;
                    }
                }
                /*view products*/
                if (option == 0) {
                    while (option == 0) {
                        System.out.println("What would you like to view");
                        System.out.println("0: View all products in all stores");
                        System.out.println("1: View all drinks in all stores");
                        System.out.println("2: View all foods in all stores");
                        System.out.println("3: View all drugs in all stores");
                        System.out.println("4: View products available for ship (online warehouse)");
                        System.out.println("5: View products available for takeout and buy at store only (offline warehouse)");
                        System.out.println("6: View physical stores locations");

                        int view;
                        while (true) {
                            view = sc.nextInt();
                            if (view != 0 && view != 1 && view != 2 && view != 3 && view != 4 && view != 5 && view != 6) {
                                System.out.println("You can only enter 0,1,2,3,4,5,6");
                            } else {
                                break;
                            }
                        }

                        //view all products
                        if (view == 0) {
                            try {
                                ResultSet rs = listAllProds.executeQuery();
                                System.out.format("%7s%60s\n", "Products ID", "Products Name");
                                while (rs.next()) {
                                    System.out.format("%7d%60s\n", rs.getInt("PROD_ID"), rs.getString("PROD_NAME"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 1) {
                            try {
                                ResultSet rs = listAllDrinks.executeQuery();
                                System.out.format("%7s%60s\n", "Products ID", "Products Name");
                                while (rs.next()) {
                                    System.out.format("%7d%60s\n", rs.getInt("PROD_ID"), rs.getString("PROD_NAME"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 2) {
                            try {
                                ResultSet rs = listAllFoods.executeQuery();
                                System.out.format("%7s%60s\n", "Products ID", "Products Name");
                                while (rs.next()) {
                                    System.out.format("%7d%60s\n", rs.getInt("PROD_ID"), rs.getString("PROD_NAME"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 3) {
                            try {
                                ResultSet rs = listAllDrugs.executeQuery();
                                System.out.format("%7s%60s\n", "Products ID", "Products Name");
                                while (rs.next()) {
                                    System.out.format("%7d%60s\n", rs.getInt("PROD_ID"), rs.getString("PROD_NAME"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 4) {
                            try {
                                ResultSet rs = listAllProdsOnline.executeQuery();
                                System.out.format("%10s%60s%10s\n", "Products ID", "Products Name", "Inventory");
                                while (rs.next()) {
                                    System.out.format("%10d%60s%10d\n", rs.getInt("PROD_ID"), rs.getString("PROD_NAME"), rs.getInt("INVENTORY"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 5) {
                            try {
                                ResultSet rs = listallProdsOffline.executeQuery();
                                System.out.format("%20s%20s%60s%10s\n", "Store ID", "Products ID", "Products Name", "Inventory");
                                while (rs.next()) {
                                    System.out.format("%20d%20d%60s%10s\n", rs.getInt("STORE_ID"), rs.getInt("PROD_ID"), rs.getString("PROD_NAME"), rs.getInt("Inventory"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        } else if (view == 6) {
                            try {
                                ResultSet rs = listAllPhysicalStores.executeQuery();
                                System.out.format("%7s%60s\n", "Store ID", "Store address");
                                while (rs.next()) {
                                    System.out.format("%7d%60s\n", rs.getInt("STORE_ID"), rs.getString("STORE_ADDRESS"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error listing products");
                            }

                        }
                        System.out.println("Would you like to view a different category?");
                        System.out.println("0: YES");
                        System.out.println("1: NO. Take me to menu");
                        int cat;
                        while (true) {
                            cat = sc.nextInt();
                            if (cat != 0 && cat != 1) {
                                System.out.println("You can only enter 0 or 1");
                            } else {
                                break;
                            }
                        }
                        if (cat == 1) {
                            option=1;
                        }


                    }
                } else if (option == 1) {
                    System.out.println("Welcome to cart window");
                    try {
                        ResultSet rs = getMaxTransacId.executeQuery();
                        if (rs.next())
                            transactionId = rs.getInt(1);
                        transactionId++;
                    } catch (Exception e) {
                        System.out.println("Error retrieving transaction ID");
                    }
                    /*create a new transaction*/
                    try {
                        newTransaction.setInt(1, transactionId);
                        newTransaction.setInt(2, customerId);
                        int rs = newTransaction.executeUpdate();
                    } catch (Exception e) {
                        System.out.println("Error generating new transaction");
                    }

                    System.out.println("Would you like to order ship (only available on online warehouse) or takeout(only available on offline warehouse)");
                    System.out.println("0: Ship");
                    System.out.println("1: Take out");
                    int ship;
                    while (true) {
                        ship = sc.nextInt();
                        if (ship != 0 && ship != 1) {
                            System.out.println("You can only enter 0 or 1");
                        } else {
                            break;
                        }
                    }
                    if (ship == 0) {
                        System.out.println("Redirecting to Store 1 (online warehouse) inventory");
                        while (option == 1) {
                            String prodname = null;
                            int inventory = 0;
                            System.out.println("Please enter the ID of the product you want to purchase");
                            int prodId = sc.nextInt();
                            System.out.println("Please enter the amount you want to buy");
                            int amount = sc.nextInt();
                            /*check if there's enough inventory*/
                            try {
                                checkVirtualQuantity.setInt(1, prodId);
                                ResultSet result = checkVirtualQuantity.executeQuery();
                                if (result.next())
                                    inventory = result.getInt(1);
                                try {
                                    getProdName.setInt(1, prodId);
                                    ResultSet res = getProdName.executeQuery();
                                    if (res.next()) {
                                        prodname = res.getString(1);
                                        if (inventory == 0)
                                            System.out.println("This product is not available for ship. Please check offline warehouse for take out");
                                        else if (inventory < amount) {
                                            System.out.println("Sorry! We only have " + inventory + " of " + prodname + " left! Please buy a different product or enter a smaller amount");
                                        } else {
                                            try {
                                                addToCart.setInt(1, transactionId);
                                                addToCart.setInt(2, prodId);
                                                addToCart.setInt(3, amount);
                                                int rs = addToCart.executeUpdate();
                                                if (rs != 0) {
                                                    System.out.println("You have added " + amount + " of " + prodname + " to your cart");
                                                    updatePhysicalInvent(inventory - amount, 1, prodId);
                                                }


                                            } catch (Exception e) {
                                                System.out.println("Error adding to cart");
                                            }

                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error getting product name");
                                }
                            } catch (Exception e) {
                                System.out.println("Error checking inventory for ship");
                            }

                            System.out.println("Please review your cart");
                            viewCart(transactionId);


                            System.out.println("Would you like to add more items to your cart?");
                            System.out.println("0: YES");
                            System.out.println("1: NO. Take me to menu");
                            int checkout;
                            while (true) {
                                checkout = sc.nextInt();
                                if (checkout != 0 && checkout != 1) {
                                    System.out.println("You can only enter 0 or 1");
                                } else {
                                    break;
                                }
                            }
                            if (checkout == 1)
                                option = 0;

                        }


                    } else if (ship == 1) {
                        System.out.println("Which of the warehouses would you like to purchase from?");
                        System.out.println("0: Store 2: 17 Briar Crest Lane");
                        System.out.println("1: Store 3: 220 Farrington Square");
                        int house;
                        while (true) {
                            house = sc.nextInt();
                            if (house != 0 && house != 1) {
                                System.out.println("You can only enter 0 or 1");
                            } else {
                                break;
                            }
                        }
                        if (house == 0) {
                            System.out.println("Redirecting to Store 2 inventory");
                            while (option == 1) {
                                String prodname = null;
                                int inventory = 0;
                                System.out.println("Please enter the ID of the product you want to purchase");
                                int prodId = sc.nextInt();
                                System.out.println("Please enter the amount you want to buy");
                                int amount = sc.nextInt();
                                /*check if there's enough inventory*/
                                try {
                                    checkPhysicalOneQuantity.setInt(1, prodId);
                                    ResultSet result = checkPhysicalOneQuantity.executeQuery();
                                    if (result.next())
                                        inventory = result.getInt(1);
                                    try {
                                        getProdName.setInt(1, prodId);
                                        ResultSet res = getProdName.executeQuery();
                                        if (res.next()) {
                                            prodname = res.getString(1);
                                            if (inventory == 0)
                                                System.out.println("This product is not available at this store");
                                            else if (inventory < amount) {
                                                System.out.println("Sorry! We only have " + inventory + " of " + prodname + " left! Please buy a different product or enter a smaller amount");
                                            } else {
                                                try {
                                                    addToCart.setInt(1, transactionId);
                                                    addToCart.setInt(2, prodId);
                                                    addToCart.setInt(3, amount);
                                                    int rs = addToCart.executeUpdate();
                                                    if (rs != 0) {
                                                        System.out.println("You have added " + amount + " of " + prodname + " to your cart");
                                                        updatePhysicalInvent(inventory - amount, 2, prodId);

                                                    }


                                                } catch (Exception e) {
                                                    System.out.println("Error adding to cart");
                                                }

                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Error getting product name");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error checking inventory for store");
                                }

                                System.out.println("Please review your cart");
                                viewCart(transactionId);


                                System.out.println("Would you like to add more items to your cart?");
                                System.out.println("0: YES");
                                System.out.println("1: NO. Take me to menu");
                                int checkout;
                                while (true) {
                                    checkout = sc.nextInt();
                                    if (checkout != 0 && checkout != 1) {
                                        System.out.println("You can only enter 0 or 1");
                                    } else {
                                        break;
                                    }
                                }
                                if (checkout == 1) {
                                    option = 0;
                                }


                            }

                        } else if (house == 1) {
                            System.out.println("Redirecting to Store 3 inventory");
                            while (option == 1) {
                                String prodname = null;
                                int inventory = 0;
                                System.out.println("Please enter the ID of the product you want to purchase");
                                int prodId = sc.nextInt();
                                System.out.println("Please enter the amount you want to buy");
                                int amount = sc.nextInt();
                                /*check if there's enough inventory*/
                                try {
                                    checkPhysicalTwoQuantity.setInt(1, prodId);
                                    ResultSet result = checkPhysicalTwoQuantity.executeQuery();
                                    if (result.next())
                                        inventory = result.getInt(1);
                                    try {
                                        getProdName.setInt(1, prodId);
                                        ResultSet res = getProdName.executeQuery();
                                        if (res.next()) {
                                            prodname = res.getString(1);
                                            if (inventory == 0)
                                                System.out.println("This product is not available at this store");
                                            else if (inventory < amount) {
                                                System.out.println("Sorry! We only have " + inventory + " of " + prodname + " left! Please buy a different product or enter a smaller amount");
                                            } else {
                                                try {
                                                    addToCart.setInt(1, transactionId);
                                                    addToCart.setInt(2, prodId);
                                                    addToCart.setInt(3, amount);
                                                    int rs = addToCart.executeUpdate();
                                                    if (rs != 0) {
                                                        System.out.println("You have added " + amount + " of " + prodname + " to your cart");
                                                        updatePhysicalInvent(inventory - amount, 3, prodId);

                                                    }

                                                } catch (Exception e) {
                                                    System.out.println("Error adding to cart");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Error getting product name");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error checking inventory for store");
                                }

                                System.out.println("Please review your cart");
                                viewCart(transactionId);

                                System.out.println("Would you like to add more items to your cart?");
                                System.out.println("0: YES");
                                System.out.println("1: NO. Take me to menu");
                                int checkout;
                                while (true) {
                                    checkout = sc.nextInt();
                                    if (checkout != 0 && checkout != 1) {
                                        System.out.println("You can only enter 0 or 1");
                                    } else {
                                        break;
                                    }
                                }
                                if (checkout == 1) {
                                    option = 0;
                                }


                            }


                        }
                    }

                }
                else if(option==2){
                    System.out.println("This is your cart");
                    viewCart(transactionId);
                }
                else if (option == 3) {
                    /*check out*/
                    /*check out*/
                    /*if empty cart then no need to check out, else enter payment method*/
                    try {
                        checkEmtpyCart.setInt(1, transactionId);
                        ResultSet res = checkEmtpyCart.executeQuery();
                        int empty = 0;
                        if (res.next())
                            empty = res.getInt("Quantity");
                        if (empty == 0) {
                            System.out.println("Thank you for your interest! Come back next time");
                        } else {
                            System.out.println("Choose payment option");
                            System.out.println("0: Cash");
                            System.out.println("1: Credit Card");
                            System.out.println("2: Debit Card");
                            int payment;
                            while (true) {
                                payment = sc.nextInt();
                                if (payment != 0 && payment != 1 && payment != 2) {
                                    System.out.println("You can only enter 0,1 or 2");
                                } else {
                                    break;
                                }
                            }
                            if (payment == 0) {
                                try {
                                    cash.setInt(1, customerId);
                                    cash.setInt(2, customerId);
                                    int rs = cash.executeUpdate();
                                    if (rs != 0) {
                                        System.out.println("Please pay when the shipment arrives");
                                    }
                                } catch (Exception e) {
                                    System.out.println("You have already registered as using cash");
                                }
                            } else if (payment == 1) {
                                try {
                                    System.out.println("Please enter your card numbers without space");
                                    long cred = sc.nextLong();
                                    credit.setInt(1, customerId);
                                    credit.setLong(2, cred);
                                    int rs = credit.executeUpdate();
                                    if (rs != 0) {
                                        System.out.println("Your card is registered");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Your card has already been registered");
                                }
                            } else {
                                try {
                                    System.out.println("Please enter your card numbers without space");
                                    long deb = sc.nextLong();
                                    debit.setInt(1, customerId);
                                    debit.setLong(2, deb);
                                    int rs = debit.executeUpdate();
                                    if (rs != 0) {
                                        System.out.println("Your card is registered");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Your card has already been registered");
                                }
                            }
                            System.out.println("Your order has been placed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        else if(role==1){
            /*there is only one manager in the system , with id =1 in credential table
            username = bholliar0
            password = yqszx80bX
             */
            Scanner scan = new Scanner(System.in);
            String manager_usr=null;
            System.out.println("Please enter your username");
            String manager_pass=null;
            try {
                String tempPass = null;
                manager_usr = scan.nextLine();
                checkManager.setString(1,manager_usr);
                ResultSet rs = checkManager.executeQuery();
                if (rs.next()) {
                    manager_pass = rs.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Please enter your password");
            String tempPass;
            while (true) {
                tempPass = scan.nextLine();
                if (manager_pass.equals(tempPass)) {
                    System.out.println("Logged in");
                    break;
                } else {
                    System.out.println("Wrong password");
                }
            }
            System.out.println("Welcome to BRC Manager Window!");
            while(true){
                System.out.println("Which of these options would you like to access?");
                /*options*/
                System.out.println("0: List all customers");
                System.out.println("1: Register goods from vendors to warehouse");
                System.out.println("2: Log Out");
                int option;
                while (true) {
                    option = sc.nextInt();
                    if (option != 0 && option != 1&& option != 2) {
                        System.out.println("You can only enter 0,1,2");
                    } else {
                        break;
                    }
                }
                if(option==0){
                    listAllCustomers();
                }
                /*if a customer buy at store instead of online, the manager has to register the transaction later into the database*/
                else if(option==1){
                    addGoods();
                }

                else if(option==2){
                    System.out.println("Have a nice day!");
                    break;
                }
            }

        }


        try{
            con.close();
        }catch (Exception e) {e.printStackTrace();}
    }

    public static void listAllCustomers() {
        try {
            ResultSet rs = listAllCustomers.executeQuery();
            System.out.format("%7s%60s\n", "Customers ID", "Name");
            while (rs.next()) {
                System.out.format("%7d%60s\n", rs.getInt("CUS_ID"), rs.getString("CUS_NAME"));
            }
        } catch (Exception e) {
            System.out.println("Error listing customers");
        }
    }

    public static void addGoods(){
        Scanner sc= new Scanner(System.in);
        int sellId=-1;
        try {
            ResultSet rs = getMaxSellID.executeQuery();
            if (rs.next())
                sellId = rs.getInt(1);
            sellId++;
        } catch (Exception e) {
            System.out.println("Error retrieving Sell ID");
        }
        try {
            System.out.println("Enter the vendor id");
            int vendor = sc.nextInt();
            System.out.println("Enter the product id");
            int prodId = sc.nextInt();
            System.out.println("Enter the store id of the store that imports the good");
            int storeId = sc.nextInt();
            System.out.println("Enter the quantity of the goods");
            int quantity = sc.nextInt();
            int invent = getInvent(storeId,prodId);
            getGoods.setInt(1,sellId);
            getGoods.setInt(2,vendor);
            getGoods.setInt(3,storeId);
            getGoods.setInt(4,prodId);
            getGoods.setInt(5,quantity);
            int rs = getGoods.executeUpdate();
            if(rs!=0) {
                int amount = invent + quantity;
                updatePhysicalInvent(amount, storeId, prodId);
            }
        } catch (Exception e) {
            System.out.println("Vendor/Product/Store does not exist");
        }
    }

    public static int getInvent(int storeId, int prodId){
        int invent=0;
        try {
            checkPhysicalQuantity.setInt(1,storeId);
            checkPhysicalQuantity.setInt(2,prodId);
            ResultSet rs = checkPhysicalQuantity.executeQuery();
            if (rs.next())
                invent = rs.getInt(1);
        } catch (Exception e) {
            System.out.println("Error retrieving Sell ID");
        }
        return invent;
    }

    public static void updatePhysicalInvent(int amount , int storeId, int prodId){
        try {
            updatePhysicalInvent.setInt(1,amount);
            updatePhysicalInvent.setInt(2,storeId);
            updatePhysicalInvent.setInt(3,prodId);
            int rs = updatePhysicalInvent.executeUpdate();
            if (rs!=0)
                System.out.println("Inventory of store " + storeId + " on product "+ prodId + " is updated to " + amount);
            else
                addProductsToStore(amount, storeId, prodId);
        } catch (Exception e) {
            System.out.println("Error inserting to database");
        }
    }
    public static void viewCart(int transactionId){
        try {
            listCart.setInt(1, transactionId);
            ResultSet rs = listCart.executeQuery();
            System.out.format("%30s%10s\n", "Product Name", "Quantity");
            while (rs.next()) {
                System.out.format("%30s%10d\n", rs.getString("PROD_NAME"), rs.getInt("QUANTITY"));
            }
        } catch (Exception e) {
            System.out.println("Error listing products");
        }
    }

    public static void addProductsToStore(int amount, int storeId, int prodId){
        try {
            addToStore.setInt(1, storeId);
            addToStore.setInt(2, prodId);
            addToStore.setInt(3, amount);
            int res = addToStore.executeUpdate();
            if(res!=0)
                System.out.println("Inventory of store " + storeId + " on product "+ prodId + " is updated to " + amount);
        }
        catch(Exception e){
            System.out.println("Error inserting products into store");
        }
    }

}