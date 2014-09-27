package com.clouway.http;

import com.clouway.core.CurrentAmount;
import com.clouway.core.CurrentUser;
import com.clouway.core.UserRepository;
import com.clouway.custommatcher.ReplyMatcher;
import com.google.inject.util.Providers;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class AmountServiceTest{

    private AmountService amountService;
    private CurrentUser currentUser;
    private CurrentAmount currentAmount;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private UserRepository userRepository = null;

    @Before
    public void setUp() {

        currentUser = new CurrentUser("Ivan");
        currentAmount = new CurrentAmount(20d);
        amountService = new AmountService(Providers.of(currentUser), userRepository);

    }

    @Test
    public void getCurrentAmountOnUser() throws Exception {

        context.checking(new Expectations() {{

            oneOf(userRepository).getAmountBy("Ivan");
            will(returnValue(currentAmount));
        }
        });

        Reply<?> reply = amountService.getCurrentAmount();

        ReplyMatcher<CurrentAmount> matcher = new ReplyMatcher<>();

        assertThat(reply, matcher.matches(currentAmount));

    }
}