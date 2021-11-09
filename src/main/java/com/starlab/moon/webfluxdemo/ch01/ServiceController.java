//package com.starlab.moon.webfluxdemo.ch01;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//@RestController
//public class ServiceController {
//
//    private final KitchenService kitchen;
//
//    public ServiceController(KitchenService kitchen) {
//        this.kitchen = kitchen;
//    }
//
//    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<Dish> serveDishes() {
//        return this.kitchen.getDishes();
//    }
//
//    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<Dish> deliverDishes() {
//        return this.kitchen.getDishes()
//                .map(dish -> Dish.deliver(dish));
//    }
//}
