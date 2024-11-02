package com.example.LibraryAPI;

import java.time.LocalDateTime;

public class CheckoutRequest {

    public CheckoutRequest(LocalDateTime checkoutTime, LocalDateTime returnTime){
        this.checkoutTime = checkoutTime;
        this.returnTime = returnTime;
    }

    private LocalDateTime checkoutTime;
    private LocalDateTime returnTime;



    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }
}
