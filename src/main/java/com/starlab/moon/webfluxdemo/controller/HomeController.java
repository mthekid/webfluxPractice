package com.starlab.moon.webfluxdemo.controller;

import com.starlab.moon.webfluxdemo.domain.Cart;
import com.starlab.moon.webfluxdemo.domain.CartItem;
import com.starlab.moon.webfluxdemo.repository.CartRepository;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import com.starlab.moon.webfluxdemo.serivce.CartService;
import com.starlab.moon.webfluxdemo.serivce.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {


    private InventoryService inventoryService;

    public HomeController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    private int count = 0;
    @GetMapping
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
        .modelAttribute("items",
                this.inventoryService.getInventory())
        .modelAttribute("cart",
                this.inventoryService.getCart("My Cart")
        .defaultIfEmpty(new Cart("My Cart"))).build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.inventoryService.addItemToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html")
        .modelAttribute("results", inventoryService.searchByExample(name, description, useAnd)).build());
    }

    @DeleteMapping("/remove/{id}")
    Mono<String> removeFromCart(@PathVariable String id) {
        return this.inventoryService.removeOneFromCart("My Cart", id)
                .thenReturn("redirect:/");
    }
}
