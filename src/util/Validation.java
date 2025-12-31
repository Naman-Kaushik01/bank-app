package util;

import domain.Account;
import exceptions.ValidationException;

@FunctionalInterface
public interface Validation<T>{
    void validate(T value) throws ValidationException;
}
