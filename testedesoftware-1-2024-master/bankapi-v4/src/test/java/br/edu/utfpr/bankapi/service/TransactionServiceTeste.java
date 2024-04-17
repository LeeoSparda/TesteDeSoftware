import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import br.edu.utfpr.bankapi.dto.TransferDTO;
import br.edu.utfpr.bankapi.dto.WithdrawDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.repository.TransactionRepository;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;
import br.edu.utfpr.bankapi.validations.AvailableBalanceValidation;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private AvailableBalanceValidation availableBalanceValidation;
    private AvailableAccountValidation availableAccountValidation;

    @Before
    public void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        availableBalanceValidation = mock(AvailableBalanceValidation.class);
        availableAccountValidation = mock(AvailableAccountValidation.class);
        transactionService = new TransactionService(transactionRepository, availableBalanceValidation,
                availableAccountValidation);
    }

    @Test
    public void testWithdraw() throws NotFoundException {
        Account account = new Account();
        account.setBalance(1000.0);
        WithdrawDTO withdrawDTO = new WithdrawDTO();
        withdrawDTO.setSourceAccountNumber("sourceAccountNumber");
        withdrawDTO.setAmount(500.0);

        when(availableAccountValidation.validate("sourceAccountNumber")).thenReturn(account);
        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any())).thenReturn(savedTransaction);

        Transaction result = transactionService.withdraw(withdrawDTO);

        assertEquals(TransactionType.WITHDRAW, result.getType());
        assertEquals("sourceAccountNumber", result.getSourceAccount().getNumber());
        assertEquals(500.0, result.getAmount(), 0.001);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void testWithdrawAccountNotFound() throws NotFoundException {
        when(availableAccountValidation.validate("sourceAccountNumber")).thenThrow(new NotFoundException());

        WithdrawDTO withdrawDTO = new WithdrawDTO();
        withdrawDTO.setSourceAccountNumber("sourceAccountNumber");
        withdrawDTO.setAmount(500.0);

        transactionService.withdraw(withdrawDTO);
    }

    @Test
    public void testTransfer() throws NotFoundException {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(1000.0);
        Account receiverAccount = new Account();
        receiverAccount.setBalance(200.0);

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSourceAccountNumber("sourceAccountNumber");
        transferDTO.setReceiverAccountNumber("receiverAccountNumber");
        transferDTO.setAmount(500.0);

        when(availableAccountValidation.validate("sourceAccountNumber")).thenReturn(sourceAccount);
        when(availableAccountValidation.validate("receiverAccountNumber")).thenReturn(receiverAccount);
        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any())).thenReturn(savedTransaction);

        Transaction result = transactionService.transfer(transferDTO);

        assertEquals(TransactionType.TRANSFER, result.getType());
        assertEquals("sourceAccountNumber", result.getSourceAccount().getNumber());
        assertEquals("receiverAccountNumber", result.getReceiverAccount().getNumber());
        assertEquals(500.0, result.getAmount(), 0.001);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void testTransferSourceAccountNotFound() throws NotFoundException {
        when(availableAccountValidation.validate("sourceAccountNumber")).thenThrow(new NotFoundException());

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSourceAccountNumber("sourceAccountNumber");
        transferDTO.setReceiverAccountNumber("receiverAccountNumber");
        transferDTO.setAmount(500.0);

        transactionService.transfer(transferDTO);
    }

    @Test(expected = NotFoundException.class)
    public void testTransferReceiverAccountNotFound() throws NotFoundException {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(1000.0);

        when(availableAccountValidation.validate("sourceAccountNumber")).thenReturn(sourceAccount);
        when(availableAccountValidation.validate("receiverAccountNumber")).thenThrow(new NotFoundException());

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSourceAccountNumber("sourceAccountNumber");
        transferDTO.setReceiverAccountNumber("receiverAccountNumber");
        transferDTO.setAmount(500.0);

        transactionService.transfer(transferDTO);
    }
}
