import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.utfpr.bankapi.exception.WithoutBalanceException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.validations.AvailableBalanceValidation;

@SpringBootTest
public class AvailableBalanceValidationTest {

    @Autowired
    private AvailableBalanceValidation availableBalanceValidation;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        // Configura uma transação com uma conta de origem e um valor
        transaction = new Transaction();
        Account sourceAccount = new Account();
        sourceAccount.setBalance(1000.0);
        sourceAccount.setSpecialLimit(500.0);
        transaction.setSourceAccount(sourceAccount);
        transaction.setAmount(1500.0);
    }

    @Test
    public void testValidateSufficientBalance() {
        // Verifica se a validação não lança WithoutBalanceException para uma transação com saldo suficiente
        assertDoesNotThrow(() -> availableBalanceValidation.validate(transaction));
    }

    @Test
    public void testValidateInsufficientBalance() {
        // Configura a transação com um valor maior do que o saldo disponível na conta de origem
        transaction.setAmount(2000.0);

        // Verifica se a validação lança WithoutBalanceException para uma transação com saldo insuficiente
        assertThrows(WithoutBalanceException.class, () -> availableBalanceValidation.validate(transaction));
    }
}
