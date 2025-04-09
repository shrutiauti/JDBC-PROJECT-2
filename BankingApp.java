package com.BankingSys;

import java.nio.channels.NonReadableChannelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.ServerPreparedQueryTestcaseGenerator;

public class BankingApp {
	
   private static final String url="jdbc:mysql://localhost:3306/BankingSystem";
   private static final String user="root";
   private static final String password="shruti";
	
   

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		  Class.forName("com.mysql.jdbc.Driver");
		Connection connection=DriverManager.getConnection(url, user, password);
		Scanner scanner=new Scanner(System.in);
		
		User user=new User(connection,scanner);
		Accounts accounts=new Accounts(connection,scanner);
		AccountManager accountManager=new AccountManager(connection,scanner);
			
			String email;
			long account_number;
			
			while(true) {
				System.out.println("*** WEILCOME TO BANIING SYSTEM ***");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println("Enter your choice :");
				int choice1=scanner.nextInt();
				
				switch(choice1){
				case 1:
					user.registration();
					break;
				case 2:
					email=user.login();
					if(email!=null) {
						System.out.println();
						System.out.println("User Logged In!");
						
						if(!accounts.account_exist(email)) {
							System.out.println();
							System.out.println("1. Open a new bank account ");
							System.out.println("2. Exit");
							
							if(scanner.nextInt() == 1) {
								account_number=accounts.open_account(email);
								System.out.println("account created successful !");
								System.out.println("your account number is :"+account_number);
							}else {
								break;
							}
						}
						
						account_number=accounts.getAccount_number(email);
						int choice2=0;
						
						while(choice2 !=0) {
							System.out.println();
							System.out.println("1. Debit money");
							System.out.println("2. Creadit money");
							System.out.println("3. Transfer money");
							System.out.println("4. check balance ");
							System.out.println("5. Log Out");
							System.out.println("Enter your Choice :");
							
							choice2=scanner.nextInt();
							
							switch(choice2) {
							case 1:
								accountManager.debit_money(account_number);
								break;
								
							case 2:
								accountManager.credit_money(account_number);
								break;
								
							case 3:
								accountManager.transfer_money(account_number);
								break;
								
							case 4:
								accountManager.getBalance(account_number);
								break;
								
							case 5:
								break;
								
							default:
								System.out.println("Enter valid choice!");
								break;
								
							}

						}
					}
					else {
						System.out.println("Incorrect email or password!");
					}
				case 3:
					System.out.println("THANK YOU FOR USING BANKING SYSTEM");
					System.out.println("Existing system!");
					return;
					
				default:
					System.out.println("Enter valid choice !");
					break;
				}

			}

	}

}
