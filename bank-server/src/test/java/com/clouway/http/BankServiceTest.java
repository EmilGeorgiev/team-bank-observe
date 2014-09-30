package com.clouway.http;

import com.clouway.core.TransactionAmount;
import com.clouway.core.BankRepository;
import com.clouway.core.CurrentUser;
import com.clouway.core.TransactionInfo;
import com.clouway.http.fake.FakeRequestReader;
import com.google.inject.util.Providers;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.clouway.custommatcher.ReplyMatcher.contains;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by emil on 14-9-24.
 */
public class BankServiceTest {

    private BankService bankService;
    private FakeRequestReader fakeRequestReader;
    private TransactionInfo transactionInfo;
    private TransactionAmount transactionAmount;
    private CurrentUser currentUser;


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private BankRepository bankRepository = null;

    @Mock
    private Request request = null;

    @Before
    public void setUp() {

        currentUser = new CurrentUser("Ivan");
        transactionAmount = new TransactionAmount(100d);
        bankService = new BankService(bankRepository, Providers.of(currentUser));
        fakeRequestReader = new FakeRequestReader(currentUser.getName(), transactionAmount.getAmount());
        transactionInfo = new TransactionInfo(message("Success"), amount(100d));

    }

    @Test
    public void depositAmount() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).read(TransactionAmount.class);
            will(returnValue(fakeRequestReader));

            oneOf(bankRepository).deposit(clientName("Ivan"), amount(100d));
            will(returnValue(transactionInfo));
        }
        });

        Reply<?> reply = bankService.deposit(request);

        assertThat(reply, contains(transactionInfo));

    }

    @Test
    public void withdrawAmount() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).read(TransactionAmount.class);
            will(returnValue(fakeRequestReader));

            oneOf(bankRepository).withdraw(clientName("Ivan"), amount(100d));
            will(returnValue(transactionInfo));
        }
        });

        Reply<?> reply = bankService.withdraw(request);

        assertThat(reply, contains(transactionInfo));
    }

    private String message(String message) {
        return message;
    }

    private double amount(double amount) {
        return amount;
    }

    private String clientName(String name) {
        return name;
    }
}
