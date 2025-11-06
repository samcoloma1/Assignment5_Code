package org.example.Amazon;

import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests using the real Database + ShoppingCartAdaptor + Amazon.
 * The DB table is cleared before each test.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AmazonIntegrationTest {

    private Database db;
    private ShoppingCartAdaptor cart;

    @BeforeAll
    void setupDb() {
        db = new Database();
        cart = new ShoppingCartAdaptor(db);
    }

    @BeforeEach
    void reset() {
        db.resetDatabase();  // clears "shoppingcart" table
    }

    @AfterAll
    void tearDown() {
        db.close();
    }

    /** A concrete rule for integration: sum of (quantity * pricePerUnit). */
    private PriceRule sumRule() {
        return items -> items.stream()
                .mapToDouble(i -> i.getQuantity() * i.getPricePerUnit())
                .sum();
    }

    @Test
    @DisplayName("specification-based")
    void calculate_uses_real_db_items_and_rule_sum() {
        Amazon amazon = new Amazon(cart, List.of(sumRule()));
        var T = org.example.Amazon.Cost.ItemType.values()[0];

        amazon.addToCart(new Item(T, "Pen", 4, 1.25));      // 5.00
        amazon.addToCart(new Item(T, "Notebook", 2, 3.50)); // 7.00
        amazon.addToCart(new Item(T, "USB", 1, 9.99));      // 9.99

        double total = amazon.calculate();

        assertThat(total).isEqualTo(5.00 + 7.00 + 9.99);
        assertThat(cart.getItems()).hasSize(3);
    }

    @Test
    @DisplayName("structural-based")
    void addToCart_persists_item_and_is_visible_through_adaptor() {
        Amazon amazon = new Amazon(cart, List.of(sumRule()));
        var T = org.example.Amazon.Cost.ItemType.values()[0];

        assertThat(cart.getItems()).isEmpty(); // after reset

        amazon.addToCart(new Item(T, "Cable", 2, 4.0));

        var items = cart.getItems();
        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getName()).isEqualTo("Cable");
        assertThat(items.getFirst().getQuantity()).isEqualTo(2);
        assertThat(items.getFirst().getPricePerUnit()).isEqualTo(4.0);
    }
}
