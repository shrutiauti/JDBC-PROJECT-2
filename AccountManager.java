package com.BankingSys;

import java.nio.channels.Pipe.SourceChannel;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.chrono.IsoChronology;
import java.util.Scanner;

import javax.sql.RowSet;

public class AccountManager {
	
	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection connection, Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner; 
	}
	
	public void credit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("enter the ammount :");
		double ammount=scanner.nextDouble();
		System.out.println("enter the security pin :");
		String security_pin=scanner.nextLine();
		
		connection.setAutoCommit(false);
		
		if(account_number != 0) {
			PreparedStatement ps =connection.prepareStatement("select * from accounts where account_number =? and security_pin=?");
			ps.setDouble(1, ammount);
			ps.setString(2, security_pin);
			ResultSet rs =ps.executeQuery();
			
			if(rs.next()) {
				String credit_query="update accounts set balance =balance + ? where aacount_number=?";
				PreparedStatement ps1 =connection.prepareStatement(credit_query);
				ps1.setDouble(1, ammount);
				ps.setLong(2, account_number);
				
				int rowAffecetd =ps.executeUpdate();
				if(rowAffecetd>0) {
					System.out.println("Rs"+ammount+"credited successful!");
					connection.commit();
					connection.setAutoCommit(true);
					
				}else {
					System.out.println("Transaction Failed");
					connection.rollback();
					connection.setAutoCommit(true);
				}
				
			}else {
			      System.out.println("Invalid security Pin!");
			}
		}else {
			connection.setAutoCommit(true);
		}
		
	}
	
	public void debit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter the aamount :");
		double amount=scanner.nextDouble();
		System.out.println("Enter the security Pin :");
		String security_pin=scanner.nextLine();
		
		connection.setAutoCommit(false);
		
		if(account_number!=0) {
			PreparedStatement ps=connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
			ps.setLong(1, account_number);
			ps.setString(2, security_pin);
			ResultSet rs =ps.executeQuery();
			
			if(rs.next()) {
				double current_balance=rs.getDouble("balance");
				
				if(amount<=current_balance) {
					String debit_query="update accounts set balance =balance - ? where account_number = ?";
					PreparedStatement ps1 =connection.prepareStatement(debit_query);
					ps1.setDouble(1, amount);
					ps1.setLong(2, account_number);
					int rowsAffected =ps1.executeUpdate();
					if(rowsAffected>0) {
						System.out.println("Rs"+amount+"debited scuccesfully");
						connection.commit();
						connection.setAutoCommit(true);
					}else {
						System.out.println("Transaction failed!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}else {
					System.out.println("Insufficient balance");
				}
			}
			else {
				System.out.println("Invalid pin ");
			}
		}
		
	}
	
	public void transfer_money(long sender_account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter receiver account number :");
		long receiver_account_number=scanner.nextLong();
		
		System.out.println("Enter amount :");
		double amount=scanner.nextDouble();
		
		System.out.println("Enter the security pin :");
		String security_pin=scanner.nextLine();
		
		connection.setAutoCommit(false);
		
		if(sender_account_number !=0 && receiver_account_number !=0) {
			PreparedStatement ps =connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
			ps.setLong(1, sender_account_number);
			ps.setString(2, security_pin);
			ResultSet rs =ps.executeQuery();
			
			if(rs.next()) {
				double current_balanc=rs.getDouble("balance");
						
				if (amount<=current_balanc) {
					String debitQuery="update accounts set balance = balance - ? where account_number = ?";
					String creditQuery="update accounts set balance = balance + ? where account_number = ?";
					
					PreparedStatement ps2 =connection.prepareStatement(creditQuery);
					PreparedStatement ps3 =connection.prepareStatement(debitQuery);
					
					ps2.setDouble(1, amount);
					ps2.setLong(2, receiver_account_number);
					
					ps3.setDouble(1, amount);
					ps3.setLong(2, sender_account_number);
					
					int rowsAffected1=ps2.executeUpdate();
					int rowsAffected2=ps3.executeUpdate();
					
					if(rowsAffected1>0 && rowsAffected2>0) {
						System.out.println("Transaction Successful!");
						connection.commit();
						connection.setAutoCommit(true);
					}else {
						System.out.println("Transaction failed!");
                         connection.commit();
                         connection.setAutoCommit(true);
					}

				}else {
					System.out.println("Insufficient balance");
				}
			}else {
				System.out.println("Invalid security pin");
			}
		}else {
			System.out.println("Invalid account number");
		}
		
	}
	
	public void getBalance(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter the security pin :");
		String security_pin=scanner.nextLine();
		
		PreparedStatement ps =connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
		ps.setLong(1, account_number);
		ps.setString(2, security_pin);
		
		ResultSet rs =ps.executeQuery();
		
		if(rs.next()) {
			double balance=rs.getDouble("balance");
			System.out.println("Balance :"+balance);
		}else {
			System.out.println("Invalid pin   ");
		}
	}

	

}
