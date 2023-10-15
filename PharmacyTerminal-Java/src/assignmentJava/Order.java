package assignmentJava;

import assignmentJava.entities.Customer;
import assignmentJava.entities.DeliveryAgent;
import assignmentJava.entities.Product;
import assignmentJava.entities.ShopWarehouse;
import assignmentJava.exceptions.InvalidOrderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Order {
    private static int orderCount = 0;

    public enum OrderStatus {UNPROCESSED, PROCESSED, FAILED}

    // A unique order ID for every order
    private final Integer orderID = orderCount++;

    // Pre-processing: Variables that are available to us before we start processing the order.
    private Product productOrdered;
    private Integer quantity;
    private Integer zipcode;
    private Customer customer;

    // Post-processing: Variables that are available to us after the order has been processed.
    private DeliveryAgent deliveryAgent = null;
    private @Nullable ShopWarehouse shopWarehouse = null;
    private @NotNull OrderStatus orderStatus = OrderStatus.UNPROCESSED;

    public Order(@NotNull Product productOrdered, @NotNull Integer quantity, @NotNull Customer customer) {
        this.setProductOrdered(productOrdered);
        this.setQuantity(quantity);
        this.setZipcode(customer.getZipcode());
        this.setCustomer(customer);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Integer getOrderID() {
        return this.orderID;
    }

    /**
     * Fail this order.
     */
    public void failOrder() {
        this.orderStatus = OrderStatus.FAILED;
    }

    /**
     * Process this order by assigning the given delivery agent and shop/warehouse
     * to this order and relaying this information to those entities.
     * @param deliveryAgent The delivery agent
     * @param shopWarehouse The shop/warehouse
     * @throws InvalidOrderException when the given order is malformed
     */
    public void processOrder(@NotNull DeliveryAgent deliveryAgent, @NotNull ShopWarehouse shopWarehouse)
            throws InvalidOrderException {
        if (!deliveryAgent.getZipcode().equals(shopWarehouse.getZipcode()))
            throw new InvalidOrderException("Delivery agent and shop/warehouse aren't in the same area.");
        if (!deliveryAgent.getZipcode().equals(customer.getZipcode()))
            throw new InvalidOrderException("Delivery agent and customer aren't in the same area.");

        this.setDeliveryAgent(deliveryAgent);
        this.setShopWarehouse(shopWarehouse);
        deliveryAgent.fulfillOrder(this);
        shopWarehouse.fulfillOrder(this);

        orderStatus = OrderStatus.PROCESSED;
    }

    public Integer getQuantity() {
        return quantity;
    }

    private void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public @Nullable ShopWarehouse getShopWarehouse() {
        return shopWarehouse;
    }

    private void setShopWarehouse(@NotNull ShopWarehouse shopWarehouse) {
        this.shopWarehouse = shopWarehouse;
    }

    public Product getProductOrdered() {
        return productOrdered;
    }

    private void setProductOrdered(Product productOrdered) {
        this.productOrdered = productOrdered;
    }

    public @Nullable DeliveryAgent getDeliveryAgent() {
        return deliveryAgent;
    }

    private void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public Customer getCustomer() {
        return customer;
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getZipcode() {
        return this.zipcode;
    }

    private void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }
}
