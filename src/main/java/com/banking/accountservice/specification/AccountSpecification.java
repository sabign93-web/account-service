package com.banking.accountservice.specification;

import com.banking.accountservice.entity.Account;
import com.banking.accountservice.enums.AccountStatus;
import com.banking.accountservice.enums.Currency;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

public final class AccountSpecification {

    private AccountSpecification(){}

    public static Specification<Account> hasSearch(String search) {

        if (search == null || search.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {

            Predicate ownerNamePredicate =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("ownerName")),
                            "%" + search.toLowerCase() + "%"
                    );

            Predicate ibanPredicate =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("iban")),
                            "%" + search.toLowerCase() + "%"
                    );

            return criteriaBuilder.or(
                    ownerNamePredicate,
                    ibanPredicate
            );
        };
    }
    public static Specification<Account> hasCurrency(Currency currency) {

        if (currency == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("currency"),
                        currency
                );
    }
    public static Specification<Account> hasStatus(AccountStatus status) {

        if (status == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

}

