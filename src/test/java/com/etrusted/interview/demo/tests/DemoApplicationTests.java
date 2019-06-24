package com.etrusted.interview.demo.tests;

import static com.etrusted.interview.demo.mock.OrderHelper.createMockedOrderRequest;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithDifferentShopUrl;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutAddress;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutAddressAndPaymentTypePaypal;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutOrderReference;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutShopUrl;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutEmail;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithOutPaymentType;
import static com.etrusted.interview.demo.mock.OrderHelper.createRequestWithNewTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.rest.dto.OrderRequest;
import com.etrusted.interview.demo.rest.dto.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	public static final ObjectMapper objectMapper = new ObjectMapper();

	static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	public static List<Order> retrieveOrderList(String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, Order.class);
		return objectMapper.readValue(json, typeReference);
	}

	private void performPostSuccessOrders(Long expectedId, OrderRequest orderRequest) throws Exception {
		this.mockMvc
				.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated()).andDo(mvcResult -> {
					String json = mvcResult.getResponse().getContentAsString();
					OrderResponse or = objectMapper.readValue(json, OrderResponse.class);
					assertEquals(expectedId, or.getId());
				});
	}

	private void performPostIsBadRequestOrders(OrderRequest orderRequest) throws Exception {
		this.mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderRequest))).andExpect(status().isBadRequest());
	}

	@Test
	public void storeOrdersSuccessTestcase() throws Exception {
		performPostSuccessOrders(new Long("3"), createMockedOrderRequest());
		performPostSuccessOrders(new Long("4"), createRequestWithDifferentShopUrl());
		performPostSuccessOrders(new Long("5"), createRequestWithOutAddressAndPaymentTypePaypal());
		performPostSuccessOrders(new Long("6"), createRequestWithNewTransaction());
	}

	@Test
	public void storeOrdersBadRequestTestcase() throws Exception {
		performPostIsBadRequestOrders(createRequestWithOutShopUrl());
		performPostIsBadRequestOrders(createRequestWithOutOrderReference());
		performPostIsBadRequestOrders(createRequestWithOutAddress());
		performPostIsBadRequestOrders(createRequestWithOutEmail());
		performPostIsBadRequestOrders(createRequestWithOutPaymentType());
	}

	@Test
	public void retrieveOrderByShopNameTestcase() throws Exception {
		this.mockMvc.perform(get("/order/shopurl/superexampleshop.com")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(mvcResult -> {
					String json = mvcResult.getResponse().getContentAsString();
					List<Order> myOrderDetails = retrieveOrderList(json);
					assertTrue(myOrderDetails.size() == 2);
					assertEquals("tx345678", myOrderDetails.get(0).getOrderReference());
					assertEquals("tx5678", myOrderDetails.get(1).getOrderReference());

				});
	}

	@Test
	public void retrieveOrderByNonExistShopNameTestcase() throws Exception {
		this.mockMvc.perform(get("/order/shopurl/invalidshop.com")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void retrieveOrderInvalidUrlTestcase() throws Exception {
		this.mockMvc.perform(get("/orders/sh")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void postOrderInvalidUrlTestcase() throws Exception {
		this.mockMvc
				.perform(post("/tests").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createMockedOrderRequest())))
				.andExpect(status().isNotFound());
	}

	@Test
	public void postOrderWithOutBodyTestcase() throws Exception {
		this.mockMvc.perform(post("/orders")).andExpect(status().isInternalServerError());
	}

}
