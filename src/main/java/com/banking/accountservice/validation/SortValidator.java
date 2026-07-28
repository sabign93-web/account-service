package com.banking.accountservice.validation;

import com.banking.accountservice.exception.InvalidSortFieldException;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component

public class SortValidator {

    public void validate(

            Sort sort,
            Set<String> allowedFields
    ) {

        for (Sort.Order order : sort) {

            String property = order.getProperty();

            if (!allowedFields.contains(property)) {

                throw new InvalidSortFieldException(

                        "Invalid sort field: " + property

                );

            }

        }

    }

}
