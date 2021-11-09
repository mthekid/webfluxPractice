package com.starlab.moon.webfluxdemo.repository;

import com.starlab.moon.webfluxdemo.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart, String> {

}
