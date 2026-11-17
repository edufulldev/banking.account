package account.adapter.out.persistence.repository;


import account.adapter.out.persistence.entity.AccountEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountJpaRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("banking_account_test")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    void shouldSaveAndFindAccountByNumber() {

        AccountEntity account = new AccountEntity(UUID.randomUUID(),
                "ACC-TEST-001", "Cliente Teste", new BigDecimal("1000.00"),
                LocalDateTime.now());

        accountJpaRepository.saveAndFlush(account);
        boolean exists = accountJpaRepository.existsByAccountNumber("ACC-TEST-001");

        assertTrue(exists);
    }


    @Test
    void shouldRejectDuplicateAccountNumber() {

        AccountEntity firstAccount = new AccountEntity(UUID.randomUUID(),
                "ACC-DUP-001", "Cliente Um", new BigDecimal("500.00"),
                LocalDateTime.now());

        AccountEntity secondAccount = new AccountEntity(UUID.randomUUID(),
                "ACC-DUP-001", "Cliente Dois",
                new BigDecimal("800.00"), LocalDateTime.now());

        accountJpaRepository.saveAndFlush(firstAccount);

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> accountJpaRepository.saveAndFlush(secondAccount)
        );
    }

}
