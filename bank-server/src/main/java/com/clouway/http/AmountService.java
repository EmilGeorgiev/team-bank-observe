package com.clouway.http;

import com.clouway.core.CurrentAmount;
import com.clouway.core.CurrentUser;
import com.clouway.core.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

/**
 * Created by emil on 14-9-26.
 */
@At("/amount")
@Service()
public class AmountService {

    private final Provider<CurrentUser> clientName;
    private final UserRepository userRepository;

    @Inject
    public AmountService(Provider<CurrentUser> clientName, UserRepository userRepository) {

        this.clientName = clientName;

        this.userRepository = userRepository;
    }

    @Post
    public Reply<?> getCurrentAmount() {

        CurrentAmount amount = userRepository.getAmountBy(clientName.get().getName());

        return Reply.with(amount).as(Json.class);

    }

}
