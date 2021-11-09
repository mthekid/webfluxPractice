package com.starlab.moon.webfluxdemo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Item {
    private @Id String id;
    private String name;
    private String description;
    private double price;

    private String distributorRegion;
    private Date releaseDate;
    private int availableUnits;
//    private Point location;
    private boolean active;

    private Item() {}

    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
