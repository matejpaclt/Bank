package com.paclt.Bank.app.repository;

import com.paclt.Bank.app.domain.Account;
import com.paclt.Bank.app.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AccountRepositoryTest {
    

        @Test
        public void testFindAccountsByUserId_ReturnsEmptyListWhenFileNotFound() {
            List<Account> accounts = AccountRepository.findAccountsByUserId(999);
            Assertions.assertTrue(accounts.isEmpty());
        }

    }

