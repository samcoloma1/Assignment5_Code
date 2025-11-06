package org.example.Amazon;

import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pure unit tests: mock ShoppingCart + PriceRule(s).
 * No database access here.
 */
public class AmazonUnitTest {

    @Test
    @DisplayName("specification-based")
    void calculate_adds_all_rule_outputs_for_cart_items() {
        // Given a cart that returns a fixed list of items
        ShoppingCart cart = mock(ShoppingCart.class);
        List<Item> items = List.of(
                new Item(org.example.Amazon.Cost.ItemType.values()[0], "A", 2, 5.0),
                new Item(org.example.Amazon.Cost.ItemType.values()[0], "B", 1, 10.0)
        );
        when(cart.getItems()).thenReturn(items);

        // And two rules with known outputs for those items
        PriceRule r1 = mock(PriceRule.class);
        PriceRule r2 = mock(PriceRule.class);
        when(r1.priceToAggregate(items)).thenReturn(12.34);
        when(r2.priceToAggregate(items)).thenReturn(7.66);

        Amazon amazon = new Amazon(cart, List.of(r1, r2));

        // When
        double total = amazon.calculate();

        // Then
        assertThat(total).isEqualTo(12.34 + 7.66);
        verify(r1).priceToAggregate(items);
        verify(r2).priceToAggregate(items);
        verifyNoMoreInteractions(r1, r2);
    }

    @Test
    @DisplayName("structural-based")
    void addToCart_delegates_to_ShoppingCart_add() {
        ShoppingCart cart = mock(ShoppingCart.class);
        PriceRule dummy = mock(PriceRule.class);
        Amazon amazon = new Amazon(cart, List.of(dummy));

        Item item = new Item(org.example.Amazon.Cost.ItemType.values()[0], "Book", 3, 7.5);

        // When
        amazon.addToCart(item);

        // Then
        verify(cart, times(1)).add(item);
        verifyNoMoreInteractions(cart);
    }
}
