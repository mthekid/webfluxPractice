package com.starlab.moon.webfluxdemo.serivce;

import com.starlab.moon.webfluxdemo.domain.Cart;
import com.starlab.moon.webfluxdemo.domain.CartItem;
import com.starlab.moon.webfluxdemo.domain.Item;
import com.starlab.moon.webfluxdemo.repository.CartRepository;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public CartService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> addToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findAny()
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(cart);
                })
                .orElseGet( () -> this.itemRepository.findById(id)
                    .map(CartItem::new)
                .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                .map(cartItem -> cart)))
            .flatMap(this.cartRepository::save);
    }

//    public Flux<Item> search(String partialName, String partialDescription, boolean useAnd) {
//        if(partialName != null) {
//            if(partialDescription != null) {
//                if(useAnd) {
//                    return re
//                }
//            }
//        }
//    }
}
