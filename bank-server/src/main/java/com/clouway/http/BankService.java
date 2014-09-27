package com.clouway.http;

import com.clouway.core.Client;
import com.clouway.core.BankRepository;
import com.clouway.core.CurrentUser;
import com.clouway.core.TransactionInfo;
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
    private final Provider<CurrentUser> currentUser;

    @Inject
    public BankService(BankRepository bankRepository, Provider<CurrentUser> currentUser) {

        this.bankRepository = bankRepository;
        this.currentUser = currentUser;
    }

    @At("/deposit")
    @Post
    public Reply<?> deposit(Request request) {

        Client client = request.read(Client.class).as(Json.class);

        TransactionInfo info = bankRepository.deposit(currentUser.get().getName(), client.getAmount());

        return Reply.with(info).as(Json.class);
    }

    @At("/withdraw")
    @Post
    public Reply<?> withdraw(Request request) {

        Client client = request.read(Client.class).as(Json.class);

        TransactionInfo info = bankRepository.withdraw(currentUser.get().getName(), client.getAmount());

        return Reply.with(info).as(Json.class);
    }
}
