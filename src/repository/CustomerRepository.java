package repository;

import domain.Customer;
import domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository {
    private final Map<String , Customer> customersById = new HashMap<>();

    public List<Customer> findALl() {
        return new ArrayList<>(customersById.values());
    }
}
