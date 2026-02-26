package com.crud;

import java.sql.*;
import java.util.Scanner;



public class Student {

    static String URL = "jdbc:mysql://localhost:3306/college1";
    static String USER = "root";
    static String PASSWORD = "SANJAY@4VZ24MC099";

    public static void main(String[] args) throws SQLException {
    	Connection con=null;
    	PreparedStatement ps=null;
    	Scanner sc=new Scanner(System.in);
    	System.out.println("enter id: ");
    	int a=sc.nextInt();
    	System.out.println("enter name: ");
    	String b=sc.next();
    	System.out.println("enter marks: ");
    	int m=sc.nextInt();
    	System.out.println("enter grade: ");
    	String c=sc.next();
		try {
			con = DriverManager.getConnection(URL,USER,PASSWORD);
			String s="insert into student values(?,?,?,?)";
			ps=con.prepareStatement(s);
			ps.setInt(1, a);
			ps.setString(2, b);
			ps.setInt(3, m);
			ps.setString(4, c);
			
			ps.executeUpdate();
			System.out.println("data inserted..");
	    	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		
			ps.close();
			con.close();
			sc.close();
		}
		
    	

    	
    }
}