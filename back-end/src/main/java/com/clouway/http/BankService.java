package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

/**
 * Created by emil on 14-9-25.
 */
@At("/amount")
@Service
@Singleton
public class BankService {

    private final BankRepository bankRepository;
    private final BankValidator validator;
    private final SiteMap siteMap;


    @Inject
    public BankService(BankRepository bankRepository, BankValidator validator, SiteMap siteMap) {

        this.bankRepository = bankRepository;
        this.validator = validator;
        this.siteMap = siteMap;
    }

    @At("/deposit")
    @Post
    public Reply<?> deposit(Request request) {

        Double amount = request.read(Double.class).as(Json.class);

        if (validator.transactionIsValid(amount)) {
            TransactionInfo info = bankRepository.deposit(amount);
            return Reply.with(info).as(Json.class);
        }
        return Reply.with(siteMap.transactionError()).error();
    }

    @At("/withdraw")
    @Post
    public Reply<?> withdraw(Request request) {

        Double amount = request.read(Double.class).as(Json.class);

        if (validator.transactionIsValid(amount)){
            TransactionInfo info = bankRepository.withdraw(amount);
            return Reply.with(info).as(Json.class);
        }
        return Reply.with(siteMap.transactionError()).error();
    }

    @Get
    public Reply<?> getCurrentAmount() {

        return Reply.with(bankRepository.getAmount()).ok();
    }
}