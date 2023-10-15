package assignmentJava.entities;

import assignmentJava.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a customer with a zipcode and an associated order history
 */
public class Customer extends Entity {
    /**
     * This customer's zipcode.
     */
    private final Integer zipcode;

    /**
     * The order history of this customer
     */
    private final @NotNull List<Order> orderHistory = new ArrayList<>();

    public Customer(String name, Integer zipcode) {
        super(name);
        this.zipcode = zipcode;
    }

    /**
     * Gets the zipcode of this customer.
     * @see assignmentJava.entities.Customer#zipcode
     * @return The zipcode of this customer.
     */
    public Integer getZipcode() {
        return zipcode;
    }

    /**
     * Adds the given order to this customer's order history.
     * @param order The order to be added.
     */
    public void addOrder(Order order) {
        this.orderHistory.add(order);
    }

    /**
     * Gets this customer's order history.
     * @return This customer's order history.
     */
    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }
}
