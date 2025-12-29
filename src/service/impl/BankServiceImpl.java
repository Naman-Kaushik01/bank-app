package service.impl;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();

        // CHANGE LATER --> 10 + 1 = AC11
//        String accountNumber = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account = new Account(accountNumber , accountType , (double) 0,customerId);

        //SAVE
        accountRepository.saveAccount(account);
        return accountNumber;

    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found :"+accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.DEPOSIT);
        transactionRepository.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found :"+accountNumber));
        if(account.getBalance() < amount)
            throw new RuntimeException("Insufficient balance");
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, Double amount, String note) {
        if(fromAccount.equals(toAccount))
            throw new RuntimeException("From account and To account are the same");
        Account from = accountRepository.findByNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("Account not found :"+fromAccount));
        Account to = accountRepository.findByNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("Account not found :"+toAccount));
        if(from.getBalance() < amount)
            throw new RuntimeException("Insufficient balance");
        to.setBalance(to.getBalance() + amount);
        from.setBalance(from.getBalance() - amount);

        Transaction fromTransaction = new Transaction(from.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.TRANSFER_OUT);
        transactionRepository.add(fromTransaction);

        Transaction toTransaction = new Transaction(to.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.TRANSFER_IN);
        transactionRepository.add(toTransaction);

    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        return transactionRepository.findByAccount(accountNumber).stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }


    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d" , size);
    }
}
