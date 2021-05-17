package com.paymentapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
