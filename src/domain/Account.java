package domain;

public class Account {
    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;

    public Account(String accountNumber, String accountType ,Double balance ,String customerId) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerID() {
        return customerId;
    }

    public void setCustomerID(String customerID) {
        this.customerId = customerID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
