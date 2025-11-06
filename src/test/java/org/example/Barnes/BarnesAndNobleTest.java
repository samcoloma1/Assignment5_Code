package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Part 1 — Barnes & Noble tests
 * - specification-based
 * - structural-based
 *
 * Uses Mockito (already in pom.xml) for isolation.
 */
class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based")
    void computesTotalPrice_andIssuesPurchases_forValidCart() {
        // Arrange
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        // Two real books with concrete prices
        Book dune = new Book("9780441172719", 20, 100);
        Book lotr = new Book("9780544003415", 25, 100);

        when(db.findByISBN("9780441172719")).thenReturn(dune);
        when(db.findByISBN("9780544003415")).thenReturn(lotr);

        Map<String,Integer> order = Map.of(
                "9780441172719", 2, // 2 * 20 = 40
                "9780544003415", 1  // 1 * 25 = 25
        );

        // Act
        PurchaseSummary ps = bn.getPriceForCart(order);

        // Assert total and no "unavailable" entries
        assertNotNull(ps);
        assertEquals(65, ps.getTotalPrice()); // 40 + 25
        assertTrue(ps.getUnavailable().isEmpty());

        // Verify we attempted to buy the expected quantities
        verify(process).buyBook(dune, 2);
        verify(process).buyBook(lotr, 1);
        verifyNoMoreInteractions(process);
    }

    @Test
    @DisplayName("structural-based")
    void returnsNull_whenOrderIsNull() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        assertNull(bn.getPriceForCart(null));
        verifyNoInteractions(db, process);
    }

    @Test
    @DisplayName("structural-based")
    void emptyOrder_returnsZeroTotal_andNoPurchases() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        PurchaseSummary ps = bn.getPriceForCart(Map.of());

        assertNotNull(ps);
        assertEquals(0, ps.getTotalPrice());
        assertTrue(ps.getUnavailable().isEmpty());
        verifyNoInteractions(process);
    }
}

