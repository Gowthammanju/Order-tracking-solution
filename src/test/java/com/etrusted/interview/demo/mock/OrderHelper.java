package com.etrusted.interview.demo.mock;

import com.etrusted.interview.demo.entity.PaymentType;
import com.etrusted.interview.demo.rest.dto.OrderRequest;

public interface OrderHelper {

	static OrderRequest createMockedOrderRequest() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setShopURL("testexampleshop");
		orderRequest.setOrderReference("tx1234");
		orderRequest.setPaymentType(PaymentType.CASH_ON_DELIVERY);
		orderRequest.setFirstName("testfirstname");
		orderRequest.setLastName("testlastname");
		orderRequest.setAddress("34 test address ,Bonn, Germany");
		orderRequest.setEmail("test@address.com");
		return orderRequest;
	}

	static OrderRequest createRequestWithDifferentShopUrl() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setShopURL("test1exampleshop");
		orderRequest.setOrderReference("text1tx1234");
		return orderRequest;
	}

	static OrderRequest createRequestWithNewTransaction() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setOrderReference("tx1234");
		orderRequest.setPaymentType(PaymentType.CREDIT_CARD);
		return orderRequest;
	}


	static OrderRequest createRequestWithOutShopUrl() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setShopURL(null);
		return orderRequest;
	}

	static OrderRequest createRequestWithOutOrderReference() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setOrderReference(null);
		return orderRequest;
	}

	static OrderRequest createRequestWithOutAddress() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setAddress("");
		return orderRequest;
	}

	static OrderRequest createRequestWithOutEmail() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setEmail("");
		return orderRequest;
	}

	static OrderRequest createRequestWithOutPaymentType() {
		OrderRequest orderRequest = createMockedOrderRequest();
		orderRequest.setPaymentType(null);
		return orderRequest;
	}

	static OrderRequest createRequestWithOutAddressAndPaymentTypePaypal() {
		OrderRequest orderRequest = createRequestWithOutAddress();
		orderRequest.setPaymentType(PaymentType.PAYPAL);
		orderRequest.setShopURL("test2exampleshop");
		orderRequest.setOrderReference("text2tx1234");
		return orderRequest;
	}

}
