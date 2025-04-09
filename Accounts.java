package com.BankingSys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	
     private Connection connection;
     private Scanner scanner;
	
	public Accounts(Connection connection, Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
	}
	public long open_account(String email) throws SQLException {
		if(!account_exist(email))
		{
			String open_account_query="insert into accounts(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
			scanner.nextLine();
			System.out.println("Enter the full name");
			String full_name=scanner.nextLine();
  			System.out.println("Enter the inital ammount :");
			double ammount=scanner.nextDouble();
			System.out.println("Enter the security pin");
			String security_pin=scanner.next();
			scanner.nextLine();
			
			long account_number=generateAccountNumber();
			PreparedStatement ps =connection.prepareStatement(open_account_query);
			ps.setLong(1,account_number );
			ps.setString(2, full_name);
			ps.setString(3, email);
			ps.setDouble(4, account_number);
			ps.setString(5, security_pin);
			
			int rowsaffected=ps.executeUpdate();
			if(rowsaffected>0) {
				return account_number;
			}else {
				System.out.println("account creation failed!");
			}
		}
		return 0;
		
	}
	private long generateAccountNumber() throws SQLException
	{
		Statement statement=connection.createStatement();
		ResultSet rs=statement.executeQuery("select account_number from Accounts ORDER BY account_number DESC LIMIT 1");
		if(rs.next()) {
			long last_account_no=rs.getLong("account_number");
			return last_account_no+1;
		}else {
			return 10000100;
		}
	}
	boolean account_exist(String email) throws SQLException {
		String query="select account_number from Accounts where email=?";
		PreparedStatement ps =connection.prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;

		}
	}
	
	public long getAccount_number(String email) throws SQLException {
		String query="select account_number from accounts where email = ?";
		PreparedStatement ps =connection.prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs =ps.executeQuery();
		
		if(rs.next()) {
			return rs.getLong("account_number");
		}else {
			System.out.println("account number doesnt exist");
		}
		return 0;
		
	}

}
