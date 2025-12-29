package repository;

import domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private final Map<String , List<Transaction>> txByAccount = new HashMap<>();

    //if key is already present directly add(transaction) will work else new transaction will be created
    public void add(Transaction transaction) {
        txByAccount.computeIfAbsent(transaction.getAccountNumber() ,
               k -> new ArrayList<>()).add(transaction);
    }
}
