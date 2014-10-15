package com.clouway.http;

import com.clouway.core.*;
import com.clouway.http.fake.FakeRequestReader;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;

import static com.clouway.custommatcher.ReplyMatcher.contains;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by emil on 14-9-24.
 */
public class BankServiceTest {

    private BankService bankService;
    private FakeRequestReader fakeRequestReader;
    private TransactionStatus transactionStatus;
    private Double amount;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private BankRepository bankRepository;

    @Mock
    private Request request;

    @Mock
    private BankValidator validator;

    @Mock
    private SiteMap siteMap;

    @Before
    public void setUp() {

        amount = 100d;

        CurrentUser currentUser = new CurrentUser("Ivan");

        bankService = new BankService(bankRepository, validator, siteMap);

        fakeRequestReader = new FakeRequestReader(currentUser.name, amount);

        transactionStatus = new TransactionStatus(message("Success"), amount(100).doubleValue());

    }

    @Test
    public void depositAmount() {

        context.checking(new Expectations() {{

            oneOf(request).read(Double.class);
            will(returnValue(fakeRequestReader));

            oneOf(validator).transactionIsValid(amount);
            will(returnValue(true));

            oneOf(bankRepository).deposit(amount(100d));
            will(returnValue(transactionStatus));
        }
        });

        Reply<?> reply = bankService.deposit(request);

        assertThat(reply, contains(transactionStatus));

    }

    @Test
    public void tryToDepositNegativeAmount() {

        context.checking(new Expectations(){
            {
                oneOf(request).read(Double.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).transactionIsValid(amount);
                will(returnValue(false));

                oneOf(siteMap).transactionError();
                will(returnValue("error"));
            }
        });
        Reply<?>reply = bankService.deposit(request);
        assertThat(reply, contains("error"));
    }

    @Test
    public void withdrawAmount() {

        context.checking(new Expectations() {{

            oneOf(request).read(Double.class);
            will(returnValue(fakeRequestReader));

            oneOf(validator).transactionIsValid(amount);
            will(returnValue(true));

            oneOf(bankRepository).withdraw(amount(100d));
            will(returnValue(transactionStatus));
        }
        });

        Reply<?> reply = bankService.withdraw(request);

        assertThat(reply, contains(transactionStatus));
    }

    @Test
    public void withdrawFailed() {

        context.checking(new Expectations(){
            {
                oneOf(request).read(Double.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).transactionIsValid(amount);
                will(returnValue(false));

                oneOf(siteMap).transactionError();
                will(returnValue("error"));
            }
        });

        Reply<?>reply = bankService.withdraw(request);

        assertThat(reply, contains("error"));
    }

    @Test
    public void getCurrentAmountOnUser() {

        context.checking(new Expectations() {{

            oneOf(bankRepository).getAmount();
            will(returnValue(5.1d));
        }
        });

        Reply<?> reply = bankService.getCurrentAmount();

        assertThat(reply, contains(5.1d));

    }

    private String message(String message) {
        return message;
    }

    private BigDecimal amount(double amount) {
        return new BigDecimal(amount);
    }

}