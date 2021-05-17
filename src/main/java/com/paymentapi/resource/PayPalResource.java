package com.paymentapi.resource;

import com.paymentapi.request.OrderRequest;
import com.paymentapi.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PayPalResource {

    @Autowired
   private PayPalService service;

    public static final String SUCCESS_URL = "payment/success";

    public static final String CANCEL_URL = "payment/cancel";

    @Value("${application.url}")
    private String host;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/payment")
    public String payment(@ModelAttribute("order") OrderRequest order) {
        try {

            final Payment payment = service.createPayment(order, host + CANCEL_URL,host + SUCCESS_URL);

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/payment/cancel")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping("/payment/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
           final Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
}
