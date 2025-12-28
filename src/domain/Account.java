package domain;

public class Account {
    private String accountNumber;
    private String customerID;
    private double balance;
    private String accountType;

    public Account(String accountNumber, String customerID, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.customerID = customerID;
        this.balance = balance;
        this.accountType = accountType;
    }
}
