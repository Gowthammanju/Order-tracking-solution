/**
 * Project: mars-backend-challenge-template
 *
 * <p>Copyright (c) 2018 Trusted Shops GmbH All rights reserved.
 */
package com.etrusted.interview.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etrusted.interview.demo.entity.User;

/**
 * OrderRepository
 *
 * @author created by trumga2 8 Sep 2018 10:52:18
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
