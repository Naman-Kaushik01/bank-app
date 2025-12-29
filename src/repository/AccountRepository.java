package repository;

import domain.Account;
import domain.Customer;

import java.util.*;

public class AccountRepository {
    private final Map<String, Account> accountsByNumber = new HashMap<>();

    public void saveAccount(Account account){
        accountsByNumber.put(account.getAccountNumber(), account);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accountsByNumber.values());
    }

    public Optional<Account> findByNumber(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerID(String customerID) {
        List<Account> result = new ArrayList<>();
        for(Account a : accountsByNumber.values()){
            if(a.getCustomerID().toLowerCase().equals(customerID)){
                result.add(a);
            }
        }
        return result;
    }
}
