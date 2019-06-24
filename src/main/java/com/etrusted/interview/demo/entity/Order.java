package com.etrusted.interview.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "order")
@Table(name = "shop_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "order_reference")
	private String orderReference;

	@ManyToOne
	@JoinColumn(name = "user_ref")
	private User user;

	@ManyToOne
	@JoinColumn(name = "shop_ref")
	private Shop shop;

	@Column(name = "payment_type")
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	public Order(Shop shop) {
		this.shop = shop;
	}

}
