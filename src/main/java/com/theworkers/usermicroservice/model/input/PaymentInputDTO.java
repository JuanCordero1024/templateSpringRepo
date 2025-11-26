package com.theworkers.usermicroservice.model.input;

import lombok.Data;

@Data
public class PaymentInputDTO {
    private String nameCard;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvv;
}
