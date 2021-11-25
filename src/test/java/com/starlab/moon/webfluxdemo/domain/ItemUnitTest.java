package com.starlab.moon.webfluxdemo.domain;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ItemUnitTest {

    @Test
    void itemBasicsShouldWork() {
        Item sampleItem = new Item("item1", "Tv Tray", "Alf TV Tray", 19.99);


        assertThat(sampleItem.getId()).isEqualTo("item1");
        assertThat(sampleItem.getName()).isEqualTo("Tv Tray");
        assertThat(sampleItem.getDescription()).isEqualTo("Alf TV Tray");
        assertThat(sampleItem.getPrice()).isEqualTo(19.99);

        Item sampleItem2 = new Item("item1", "Tv Tray", "Alf TV Tray", 19.99);
        assertThat(sampleItem).isEqualTo(sampleItem2);
    }
}
