package com.paymentapi.service;

import com.paymentapi.request.OrderRequest;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private APIContext context;

    public Payment createPayment(final OrderRequest order,  final String cancelUrl, final String successUrl) throws PayPalRESTException {

        final Amount amount = new Amount();
        amount.setCurrency(order.getCurrency());
        amount.setTotal(order.getPrice());

        final Transaction transaction = new Transaction();
        transaction.setDescription(order.getDescription());
        transaction.setAmount(amount);

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        final Payer payer = new Payer();
        payer.setPaymentMethod(order.getMethod());

        final Payment payment = new Payment();
        payment.setIntent(order.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        final RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(context);
    }

    public Payment executePayment(final String paymentId, final String payerId) throws PayPalRESTException {

        final Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        return payment.execute(context, paymentExecute);
    }
}
