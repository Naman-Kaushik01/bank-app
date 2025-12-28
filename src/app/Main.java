package app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        System.out.println("Welcome to Console Bank");
        while (running) {
            System.out.println("""
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5)Account Statement
                6) List Accounts
                7) Search Amounts By Customer Name
                0) EXIT
                """);
            System.out.println("Enter your choice: ");
            String choice = sc.nextLine().trim();
            System.out.println("Your choice is " + choice);

            switch (choice) {
                case "0" -> running = false;
            }
        }



    }
}
