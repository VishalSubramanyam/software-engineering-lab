package assignmentJava.entities;

import assignmentJava.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryAgent extends Entity {
    /**
     * The zipcode of this delivery agent.
     */
    private Integer zipcode;

    /**
     * The orders that have been fulfilled by this delivery agent.
     */
    private final @NotNull ArrayList<Order> orders = new ArrayList<>();

    /**
     * The total number of products delivery by this agent so far.
     */
    private Integer numberOfProductsDelivered = 0;

    public DeliveryAgent(String name, Integer zipcode) {
        super(name);
        this.setZipcode(zipcode);
    }

    /**
     * Get the number of products delivered.
     *
     * @return Number of products delivered.
     * @see #numberOfProductsDelivered
     */
    public Integer getNumberOfProductsDelivered() {
        return numberOfProductsDelivered;
    }

    /**
     * Get the order history of this delivery agent.
     *
     * @return An immutable view of the order history of this agent.
     */
    public @NotNull List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orders);
    }

    /**
     * Fulfill the given order by adding it to the delivery agent's order list and incrementing
     * the agent's count of delivered products.
     * @param order The order to be added to the delivery agent's list of orders
     */
    public void fulfillOrder(@NotNull Order order) {
        orders.add(order);
        numberOfProductsDelivered += order.getQuantity();
    }

    /**
     * Gets the zipcode where this delivery agent operates.
     * @return The zipcode where this delivery agent operates.
     * @see assignmentJava.entities.DeliveryAgent#zipcode
     */
    public Integer getZipcode() {
        return this.zipcode;
    }

    /**
     * Sets the zipcode where this delivery agent operates.
     * @see assignmentJava.entities.DeliveryAgent#zipcode
     */
    private void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}
