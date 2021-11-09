package com.starlab.moon.webfluxdemo.controller;

import com.starlab.moon.webfluxdemo.domain.Cart;
import com.starlab.moon.webfluxdemo.domain.CartItem;
import com.starlab.moon.webfluxdemo.repository.CartRepository;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import com.starlab.moon.webfluxdemo.serivce.CartService;
import com.starlab.moon.webfluxdemo.serivce.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private ItemRepository itemRepository;
    private CartRepository cartRepository;

    private CartService cartService;
    private InventoryService inventoryService;

    public HomeController(ItemRepository itemRepository, CartRepository cartRepository, CartService cartService, InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
    }


    private int count = 0;
    @GetMapping
    Mono<Rendering> home() {
        System.out.println("debug count check " + count++);
        return Mono.just(Rendering.view("home.html")
        .modelAttribute("items",
                this.itemRepository.findAll())
        .modelAttribute("cart",
                this.cartRepository.findById("My Cart")
        .defaultIfEmpty(new Cart("My Cart"))).build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.cartService.addToCart("My Cart", id).thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html")
        .modelAttribute("results", inventoryService.searchByExample(name, description, useAnd)).build());
    }
}
