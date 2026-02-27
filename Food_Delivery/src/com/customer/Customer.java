package com.customer;

import java.sql.*;
import java.util.Scanner;

public class Customer {

    static Connection con = null;
    static PreparedStatement ps;
    static Scanner sc = new Scanner(System.in);

    static String Driver = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/fooddelivery";
    static String username = "root";
    static String password = "SANJAY@4VZ24MC099";

    public static void main(String[] args) {
        try {
            Class.forName(Driver);
            con = DriverManager.getConnection(url, username, password);

            while (true) {
                System.out.println("select your choice....");
                System.out.println("1. Add Customer\n2. Add Food Item\n3. Add Restaurant\n4. Add Order\n5. Add Delivery\n6. Add Order Items\n7.view data \n8.exit");
                System.out.print("enter your Choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: customer(); break;
                    case 2: fooditem(); break;
                    case 3: restaurant(); break;
                    case 4: order(); break;
                    case 5: delivery(); break;
                    case 6: orderitems(); break;
                    case 7: viewData(); break;
                    case 8:
                        con.close(); 
                        System.out.println("Exiting..."); 
                        return;
                    default: System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void customer() {
        System.out.println("Enter name: ");
        String cname = sc.next();
        System.out.println("Enter phone number: ");
        String cphone = sc.next();
        try {
            ps = con.prepareStatement("insert into customer(cname, cphone) values(?, ?)");
            ps.setString(1, cname);
            ps.setString(2, cphone);
            ps.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void fooditem() {
        System.out.println("Enter restaurant id: ");
        int rid = sc.nextInt();
        System.out.println("Enter food name: ");
        String fname = sc.next();
        System.out.println("Enter customer id: ");
        int cid = sc.nextInt();
        try {
            ps = con.prepareStatement("insert into fooditem(fid, fname, cid) values(?, ?, ?)");
            ps.setInt(1, rid);
            ps.setString(2, fname);
            ps.setInt(3, cid);
            ps.executeUpdate();
            System.out.println("Food item added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void restaurant() {
        System.out.println("Enter restaurant id: ");
        int rid = sc.nextInt();
        System.out.println("Enter restaurant name: ");
        String rname = sc.next();
        System.out.println("Enter food id: ");
        int fid = sc.nextInt();
        try {
            
            ps = con.prepareStatement("insert into restaurant(rid, rname, fid) values(?, ?, ?)");
            ps.setInt(1, rid);
            ps.setString(2, rname);
            ps.setInt(3, fid);
            ps.executeUpdate();
            System.out.println("Restaurant added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void order() {
        System.out.println("Enter order id (oid): ");
        int oid = sc.nextInt();
        System.out.println("Enter food id: ");
        int fid = sc.nextInt();
        System.out.println("Enter customer id: ");
        int cid = sc.nextInt();
        try {
            
            ps = con.prepareStatement("insert into orders(oid, fid, cid) values(?, ?, ?)");
            ps.setInt(1, oid);
            ps.setInt(2, fid);
            ps.setInt(3, cid);
            ps.executeUpdate();
            System.out.println("Order placed.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void delivery() {
        System.out.println("Enter delivery agent name: ");
        String dname = sc.next();
        System.out.println("Enter order id (oid): ");
        int oid = sc.nextInt();
        try {
            ps = con.prepareStatement("insert into delivery(dname, oid) values(?, ?)");
            ps.setString(1, dname);
            ps.setInt(2, oid);
            ps.executeUpdate();
            System.out.println("Delivery assigned.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void orderitems() {
        System.out.println("Enter itemsid: ");
        int itemsid = sc.nextInt();
        System.out.println("Enter itemsname: ");
        String itemsname = sc.next();
        System.out.println("Enter restaurant id: ");
        int rid = sc.nextInt();
        try {
            
            ps = con.prepareStatement("insert into orderitems(itemsid, itemsname, rid) values(?, ?, ?)");
            ps.setInt(1, itemsid);
            ps.setString(2, itemsname);
            ps.setInt(3, rid);
            ps.executeUpdate();
            System.out.println("Order items added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    static void viewData() {
        System.out.println("\n1. Customers\n2. Food Items\n3. Restaurants\n4. Orders");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs;

            if (choice == 1) {
                rs = stmt.executeQuery("select * from customer");
                System.out.println("customer details: ");
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("cname") + "  Phone: " + rs.getString("cphone"));
                }
            } 
            else if (choice == 2) {
                rs = stmt.executeQuery("select * from fooditem");
                System.out.println("food details: ");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("fid") + "  Name: " + rs.getString("fname"));
                }
            } 
            else if (choice == 3) {
                rs = stmt.executeQuery("select * from restaurant");
                System.out.println(" restaurant details: ");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("rid") + "  Name: " + rs.getString("rname"));
                }
            } 
            else if (choice == 4) {
                rs = stmt.executeQuery("select * from orders");
                System.out.println(" orders details: ");
                while (rs.next()) {
                    System.out.println("Order ID: " + rs.getInt("oid") + "  Customer ID: " + rs.getInt("cid"));
                }
            } 
            else {
                System.out.println("Invalid choice!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
