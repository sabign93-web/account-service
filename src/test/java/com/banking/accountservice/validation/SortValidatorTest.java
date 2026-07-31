package com.banking.accountservice.validation;

import com.banking.accountservice.entity.Account_;

import com.banking.accountservice.exception.InvalidSortFieldException;

import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Sort;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SortValidatorTest {

    private final SortValidator sortValidator = new SortValidator();

    @Test

    void shouldNotThrowExceptionWhenAllSortFieldsAreAllowed() {

        Sort sort = Sort.by(

                Sort.Order.asc(Account_.OWNER_NAME),

                Sort.Order.desc(Account_.BALANCE)

        );

        Set<String> allowedFields = Set.of(

                Account_.OWNER_NAME,

                Account_.BALANCE,

                Account_.IBAN,

                Account_.STATUS,

                Account_.CREATED_AT,

                Account_.UPDATED_AT

        );

        assertDoesNotThrow(() ->

                sortValidator.validate(sort, allowedFields)

        );

    }

    @Test

    void shouldThrowExceptionWhenSortFieldIsNotAllowed() {

        Sort sort = Sort.by(

                Sort.Order.asc("salary")

        );

        Set<String> allowedFields = Set.of(

                Account_.OWNER_NAME,

                Account_.BALANCE,

                Account_.IBAN

        );

        assertThrows(

                InvalidSortFieldException.class,

                () -> sortValidator.validate(sort, allowedFields)

        );

    }

    @Test

    void shouldThrowExceptionWhenOneOfMultipleSortFieldsIsNotAllowed() {

        Sort sort = Sort.by(

                Sort.Order.asc(Account_.OWNER_NAME),

                Sort.Order.desc("salary")

        );

        Set<String> allowedFields = Set.of(

                Account_.OWNER_NAME,

                Account_.BALANCE,

                Account_.IBAN

        );

        assertThrows(

                InvalidSortFieldException.class,

                () -> sortValidator.validate(sort, allowedFields)

        );

    }

    @Test

    void shouldNotThrowExceptionWhenSortIsUnsorted() {

        Sort sort = Sort.unsorted();

        Set<String> allowedFields = Set.of(

                Account_.OWNER_NAME,

                Account_.BALANCE,

                Account_.IBAN,

                Account_.STATUS,

                Account_.CREATED_AT,

                Account_.UPDATED_AT

        );

        assertDoesNotThrow(() ->

                sortValidator.validate(sort, allowedFields)

        );

    }

}