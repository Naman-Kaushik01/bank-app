package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    private final Validation<String> validateName = name ->{
        if(name == null || name.isBlank()) throw new ValidationException("Name is null or blank");
    };
    private final Validation<String> validateEmail = email ->{
        if(email == null || !email.contains("@")) throw new ValidationException("Email is null or blank");
    };

    @Override
    public String openAccount(String name, String email, String accountType) {

        validateName.validate(name); //Adding validation
        validateEmail.validate(email);
        //Create Customer
        String customerId = UUID.randomUUID().toString();
        Customer c = new Customer(customerId, name, email);
        customerRepository.save(c);


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
                .orElseThrow(() -> new AccountNotFoundException("Account not found :"+accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.DEPOSIT);
        transactionRepository.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found :"+accountNumber));
        if(account.getBalance() < amount)
            throw new InsufficientFundException("Insufficient balance");
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber()
                ,amount, UUID.randomUUID().toString() , note , LocalDateTime.now(), Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, Double amount, String note) {
        if(fromAccount.equals(toAccount))
            throw new ValidationException("From account and To account are the same");
        Account from = accountRepository.findByNumber(fromAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account not found :"+fromAccount));
        Account to = accountRepository.findByNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account not found :"+toAccount));
        if(from.getBalance() < amount)
            throw new InsufficientFundException("Insufficient balance");
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

    @Override
    public List<Account> searchAccountByCustomerName(String q) {
        String query = (q == null) ? "" : q.toLowerCase();
        List<Account> result = new ArrayList<>();
        for(Customer c : customerRepository.findALl()){
            if(c.getName().toLowerCase().contains(query)){
                result.addAll(accountRepository.findByCustomerID(c.getId()));
            }
        }
      result.sort(Comparator.comparing(Account::getAccountNumber));
        //Using Streams

//        return customerRepository.findALl().stream()
//                .filter(c -> c.getName().toLowerCase().contains(q))
//                .flatMap(c -> accountRepository.findByCustomerID(c.getId()).stream())
//                .sorted(Comparator.comparing(Account::getAccountNumber))
//                .collect(Collectors.toList());

        return result;
    }



    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d" , size);
    }
}
