import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() throws ProductNotAvailableException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        try {
            shopService.addOrder(productsIds);

            // expected Exception not thrown
            fail("Expected ProductNotAvailableException not thrown, even though product 2 was ordered, that does not exist.");
        } catch (ProductNotAvailableException e) {
            // all good: expected exception was thrown
        }
    }

    @Test
    void updateOrder() throws ProductNotAvailableException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);

        // WHEN
        shopService.updateOrder(order.id(), OrderStatus.COMPLETED);

        // THEN
        List<Order> processingOrders = shopService.findAllOrders(OrderStatus.PROCESSING);
        assertEquals(0, processingOrders.size());
        List<Order> completedOrders = shopService.findAllOrders(OrderStatus.COMPLETED);
        assertEquals(1, completedOrders.size());
    }
}
