package com.starlab.moon.webfluxdemo.serivce;

import com.starlab.moon.webfluxdemo.domain.Cart;
import com.starlab.moon.webfluxdemo.domain.CartItem;
import com.starlab.moon.webfluxdemo.domain.Item;
import com.starlab.moon.webfluxdemo.repository.CartRepository;
import com.starlab.moon.webfluxdemo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static reactor.core.publisher.Mono.when;


@ExtendWith(SpringExtension.class)
class InventoryServiceUnitTest {

    InventoryService inventoryService;

    @MockBean private ItemRepository itemRepository;

    @MockBean private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // 협력자와 상호작용 정의하기. [ 협력자는 위에서 선언한 MockBean으로 CUT이 아닌 외부 의존성을 가진 객체이다. ]
        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);
    }

    @Test
    @DisplayName("비어있는 카트에 하나의 아이템을 추가하고 하나만 소유하고 있는지 체크한다.")
    void addItemToEmptyCartShouldProduceOnceCartItem() {
        inventoryService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create)
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItems()).extracting(CartItem::getQuantity)
                            .containsExactlyInAnyOrder(1);

                    assertThat(cart.getCartItems()).extracting(CartItem::getItem)
                            .containsExactly(new Item("item1", "TV tray", "Alf TV tray", 19.99));

                    return true;
                }).verifyComplete();
    }
}