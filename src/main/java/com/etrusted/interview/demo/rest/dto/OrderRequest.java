package com.etrusted.interview.demo.rest.dto;

import org.apache.commons.lang3.StringUtils;

import com.etrusted.interview.demo.entity.PaymentType;
import com.etrusted.interview.demo.exception.ParameterMissingException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private String shopURL;
	private String orderReference;
	private PaymentType paymentType;
	private String firstName;
	private String lastName;
	private String email;
	private String address;

	public void isValidate() throws ParameterMissingException {

		if (StringUtils.isBlank(this.shopURL)) {
			throw new ParameterMissingException("Shop Url cannot be missing or empty");
		}

		if (StringUtils.isBlank(this.orderReference)) {
			throw new ParameterMissingException("orderReference cannot be missing or empty");
		}

		if (null == this.paymentType) {
			throw new ParameterMissingException("paymentType cannot be missing");
		}

		if (StringUtils.isBlank(this.email)) {
			throw new ParameterMissingException("email cannot be missing or empty");
		}

		if (PaymentType.CASH_ON_DELIVERY == paymentType && StringUtils.isBlank(this.address)) {
			throw new ParameterMissingException("address cannot be missing or empty in cash on delivery payment type");
		}
	}

}
