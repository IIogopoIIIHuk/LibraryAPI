package com.example.LibraryAPI;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CheckoutRequestTest {

    @Test
    void testCheckoutRequestCreation() {
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = checkoutTime.plusDays(7);

        CheckoutRequest request = new CheckoutRequest(checkoutTime, returnTime);

        assertThat(request).isNotNull();
        assertThat(request.getCheckoutTime()).isEqualTo(checkoutTime);
        assertThat(request.getReturnTime()).isEqualTo(returnTime);
    }

    @Test
    void testSettersAndGetters() {
        CheckoutRequest request = new CheckoutRequest(LocalDateTime.now(), LocalDateTime.now().plusDays(7));

        LocalDateTime newCheckoutTime = LocalDateTime.now().minusDays(1);
        LocalDateTime newReturnTime = LocalDateTime.now().plusDays(10);

        request.setCheckoutTime(newCheckoutTime);
        request.setReturnTime(newReturnTime);

        assertThat(request.getCheckoutTime()).isEqualTo(newCheckoutTime);
        assertThat(request.getReturnTime()).isEqualTo(newReturnTime);
    }

    @Test
    void testDefaultConstructor() {
        CheckoutRequest request = new CheckoutRequest(null, null);

        assertThat(request).isNotNull();
        assertThat(request.getCheckoutTime()).isNull();
        assertThat(request.getReturnTime()).isNull();
    }
}
