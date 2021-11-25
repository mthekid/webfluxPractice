package com.starlab.moon.webfluxdemo.serivce;

import com.starlab.moon.webfluxdemo.domain.Cart;
import com.starlab.moon.webfluxdemo.domain.CartItem;
import com.starlab.moon.webfluxdemo.domain.Item;
import com.starlab.moon.webfluxdemo.repository.CartRepository;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class InventoryService {

    private ItemRepository itemRepository;

    private CartRepository cartRepository;

    InventoryService(ItemRepository repository,
                     CartRepository cartRepository) {
        this.itemRepository = repository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    public Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    public Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }

    public Flux<Item> getItems() {
        return Flux.empty();
    }

    public Flux<Item> search(String partialName, String partialDescription, boolean useAnd) {
        if(partialName != null) {
            if(partialDescription != null) {
                if(useAnd) {
                    return itemRepository.findByNameContainingAndDescriptionContainingAllIgnoreCase(
                            partialName, partialDescription
                    );
                } else {
                    return itemRepository.findByNameContainingOrDescriptionContainingAllIgnoreCase(
                            partialName, partialDescription
                    );
                }
            } else {
                return itemRepository.findByNameContaining(partialName);
            }
        } else {
            if(partialDescription != null) {
                return itemRepository.findByDescriptionContainingIgnoreCase(partialDescription);
            } else {
                return itemRepository.findAll();
            }
        }
    }

    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ?
                ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny().withStringMatcher(StringMatcher.CONTAINING))
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);

        return itemRepository.findAll(probe);
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId)) //
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        }) //
                        .orElseGet(() -> {
                            return this.itemRepository.findById(itemId) //
                                    .map(item -> new CartItem(item)) //
                                    .map(cartItem -> {
                                        cart.getCartItems().add(cartItem);
                                        return cart;
                                    });
                        }))
                .flatMap(cart -> this.cartRepository.save(cart));
    }

    public Mono<Cart> removeOneFromCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findAny()
                .map(cartItem -> {
                    cartItem.decrement();
                    return Mono.just(cart);
                }).orElse(Mono.empty()))
                .map(cart -> new Cart(cart.getId(), cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getQuantity() > 0)
                    .collect(Collectors.toList())))
                .flatMap(cart -> this.cartRepository.save(cart));
    }

//    public Flux<Item> searchByFluentExample(String name, String description) {
//        return fluentMongoOperations.query(Item.class)
//                .matching(query(where("TV tray").is(name).and("Smurf").is(description)))
//                .all();
//    }
}
