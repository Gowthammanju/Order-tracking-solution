/**
 * Project: interview-backend-robert
 *
 * <p>Copyright (c) 2018 Trusted Shops GmbH All rights reserved.
 */
package com.etrusted.interview.demo.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.exception.CommonExceptionHandler;
import com.etrusted.interview.demo.exception.DemoBaseException;
import com.etrusted.interview.demo.exception.ShopMissingException;
import com.etrusted.interview.demo.repository.OrderRepository;
import com.etrusted.interview.demo.repository.ShopRepository;
import com.etrusted.interview.demo.repository.UserRepository;
import com.etrusted.interview.demo.rest.dto.OrderRequest;
import com.etrusted.interview.demo.rest.dto.OrderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * OrderRest
 *
 * @author created by trumga2 27 Mar 2018 11:21:15
 */
@RestController
@Slf4j
public class OrderRestController extends CommonExceptionHandler {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/orders")
	public ResponseEntity<OrderResponse> orderRequest(@RequestBody OrderRequest orderRequest) throws DemoBaseException {
		log.info("0;Received orderRequest request as '{}' ", orderRequest);
		orderRequest.isValidate();

		Shop shop = retrieveOrInsertByShop(orderRequest);
		User user = retrieveOrInsertsByUser(orderRequest);

		Order order = new Order();
		order.setOrderReference(orderRequest.getOrderReference());
		order.setPaymentType(orderRequest.getPaymentType());
		order.setShop(shop);
		order.setUser(user);

		Order savedOrder = orderRepository.save(order);
		log.info("0;Order '{}' is added to order table with the order id  as '{}' ",
				savedOrder, savedOrder.getId());
		return new ResponseEntity<>(new OrderResponse(savedOrder.getId()), HttpStatus.CREATED);
	}

	private Shop retrieveOrInsertByShop(OrderRequest orderRequest) {
		Shop shopByUrl = retrieveShopByShopUrl(orderRequest.getShopURL());
		if (null == shopByUrl) {
			Shop shop = new Shop();
			shop.setUrl(orderRequest.getShopURL());
			shopRepository.save(shop);
			log.info("0;Shop url '{}' is now added to shop table", orderRequest.getShopURL());
			return shop;
		}
		log.info("0;Shop Url '{}' adding to shop table is skipped", orderRequest.getShopURL());
		return shopByUrl;
	}

	private User retrieveOrInsertsByUser(OrderRequest orderRequest) {

		User userByEmail = retrieveUserByEmail(orderRequest.getEmail());

		if (null == userByEmail) {
			User user = new User();
			user.setFirstName(orderRequest.getFirstName());
			user.setLastName(orderRequest.getFirstName());
			user.setEmail(orderRequest.getEmail());
			user.setAddress(orderRequest.getAddress());
			userRepository.save(user);
			log.info("0;User '{}' is now added to user table", orderRequest.getEmail());
			return user;
		}
		log.info("0;User '{}' adding to user table is skipped", orderRequest.getEmail());

		return userByEmail;

	}

	private User retrieveUserByEmail(String email) {
		ExampleMatcher emailIdMatcher = ExampleMatcher.matching().withMatcher("email",
				GenericPropertyMatchers.ignoreCase());
		Example<User> userExample = Example.<User>of(new User(email), emailIdMatcher);

		Optional<User> optionalUser = userRepository.findOne(userExample);
		boolean exists = optionalUser.isPresent();
		log.info("0;User Email id '{}' for orderRequest request is '{}' in database ", email,
				exists ? "is present" : "is not present");
		if (exists) {
			return optionalUser.get();
		} else {
			return null;
		}

	}

	private Shop retrieveShopByShopUrl(String shopurl) {
		ExampleMatcher shopUrlMatcher = ExampleMatcher.matching().withMatcher("url",
				GenericPropertyMatchers.ignoreCase());
		Example<Shop> shopExample = Example.<Shop>of(new Shop(shopurl), shopUrlMatcher);
		Optional<Shop> optionalShop = shopRepository.findOne(shopExample);
		boolean exists = optionalShop.isPresent();
		log.info("0;Shop Url '{}' for orderRequest request is '{}' in database ", shopurl,
				exists ? "is present" : "is not present");
		if (exists) {
			return optionalShop.get();
		} else {
			return null;
		}
	}

	private List<Order> retrieveOrdersByShop(Shop shop) {
		ExampleMatcher shopMatcher = ExampleMatcher.matching().withMatcher("shop",
				GenericPropertyMatchers.ignoreCase());
		Example<Order> orderExample = Example.<Order>of(new Order(shop), shopMatcher);
		return orderRepository.findAll(orderExample);

	}

	@GetMapping("/order/shopurl/{shopurl}")
	public List<Order> retrieveOrderByShopName(@PathVariable String shopurl) throws DemoBaseException {
		log.info("0;Received orderRequest request for shop : '{}' ", shopurl);
		Shop shop = retrieveShopByShopUrl(shopurl);
		if (null == shop)
			throw new ShopMissingException(String.format("%s is not found in database", shopurl));
		List<Order> orderByShopList = retrieveOrdersByShop(shop);
		log.info("0;RetrieveOrderByShopName sucessfully returns the response as '{}' for the shop url '{}' ",
				orderByShopList, shopurl);
		return orderByShopList;

	}

}
