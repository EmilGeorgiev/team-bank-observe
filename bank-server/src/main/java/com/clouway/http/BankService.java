package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

/**
 * Created by emil on 14-9-25.
 */
@At("/bankService")
@Service
public class BankService {

    private final BankRepository bankRepository;
    private final CurrentUser currentUser;

    @Inject
    public BankService(BankRepository bankRepository, Provider<CurrentUser> currentUserProvider) {

        this.bankRepository = bankRepository;
        this.currentUser = currentUserProvider.get();

    }

    @At("/deposit")
    @Post
    public Reply<?> deposit(Request request) {

        Amount amount = request.read(Amount.class).as(Json.class);

        TransactionInfo info = bankRepository.deposit(amount.getAmount());

        return Reply.with(info).as(Json.class);
    }

    @At("/withdraw")
    @Post
    public Reply<?> withdraw(Request request) {

        Amount amount = request.read(Amount.class).as(Json.class);

        TransactionInfo info = bankRepository.withdraw(amount.getAmount());

        return Reply.with(info).as(Json.class);
    }

    @At("/getAmount")
    @Post
    public Reply<?> getCurrentAmount() {

        Amount amount = bankRepository.getAmountBy(currentUser.getName());

        return Reply.with(amount).as(Json.class);

    }
}
