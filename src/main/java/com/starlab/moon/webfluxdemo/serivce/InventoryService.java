package com.starlab.moon.webfluxdemo.serivce;

import com.starlab.moon.webfluxdemo.domain.Item;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class InventoryService {

    private ItemRepository itemRepository;
    private ReactiveFluentMongoOperations fluentMongoOperations;


    public InventoryService(ItemRepository itemRepository, ReactiveFluentMongoOperations fluentMongoOperations) {
        this.itemRepository = itemRepository;
        this.fluentMongoOperations = fluentMongoOperations;
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

    public Flux<Item> searchByFluentExample(String name, String description) {
        return fluentMongoOperations.query(Item.class)
                .matching(query(where("TV tray").is(name).and("Smurf").is(description)))
                .all();
    }
}
