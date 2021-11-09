package com.starlab.moon.webfluxdemo;

import com.starlab.moon.webfluxdemo.domain.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Item("Alf alarm clock" ,"alf alarm clock", 19.99));
            mongo.save(new Item("Smurf TV tray", "smurf Tv tray", 24.99));
        };
    }
}
