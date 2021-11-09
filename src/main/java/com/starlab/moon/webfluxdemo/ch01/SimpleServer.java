//package com.starlab.moon.webfluxdemo.ch01;
//
//import com.starlab.moon.webfluxdemo.ch01.Dish;
//import com.starlab.moon.webfluxdemo.ch01.KitchenService;
//import reactor.core.publisher.Flux;
//
//public class SimpleServer {
//    private final KitchenService kitchen;
//
//    SimpleServer(KitchenService kitchen) {
//        this.kitchen = kitchen;
//    }
//
//    Flux<Dish> doingMyJob() {
//        return this.kitchen.getDishes()
//                .map(dish -> Dish.deliver(dish));
//    }
//}
