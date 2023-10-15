package assignmentJava;

import assignmentJava.entities.*;
import assignmentJava.exceptions.ExistingEntityException;
import assignmentJava.exceptions.InvalidOrderException;
import assignmentJava.exceptions.NonExistentEntityException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This is the backbone class of the program.
 * Contains all the necessary entities and orders and provides
 * methods to allow interoperability between these classes.
 * Handles order processing.
 */
public class Network {
    /**
     * Mapping a manufacturer's unique ID to the {@link Manufacturer} object present in this system.
     */
    private final @NotNull Map<Integer, Manufacturer> manufacturers = new HashMap<>();
    /**
     * Mapping a customer's unique ID to the {@link Customer} object present in this system.
     */
    private final @NotNull Map<Integer, Customer> customers = new HashMap<>();
    /**
     * Mapping a shop/warehouse's unique ID to the {@link ShopWarehouse} object present in this system.
     */
    private final @NotNull Map<Integer, ShopWarehouse> shops = new HashMap<>();
    /**
     * Mapping a delivery agent's unique ID to the {@link DeliveryAgent} object present in this system.
     */
    private final @NotNull Map<Integer, DeliveryAgent> deliveryAgents = new HashMap<>();
    /**
     * Mapping a product's unique ID to the {@link Product} object present in this system.
     */
    private final @NotNull Map<Integer, Product> products = new HashMap<>();
    /**
     * A list of unique IDs that have been created so far.
     * When a new entity is added to the system, its unique ID will be added
     * to this list. When the entity is deleted, its unique ID will continue
     * to stay in this list, since future additions should NOT have unique IDs
     * that belonged to deleted entities.
     *
     * @deprecated Not needed right now since the system uses auto-generated unique IDs
     * that are guaranteed to be distinct.
     */
    private final @NotNull List<Integer> uniqueIDs = new ArrayList<>();
    private final @NotNull Map<Integer, Order> unprocessedOrders = new HashMap<>();
    private final @NotNull Map<Integer, Order> processedFailedOrders = new HashMap<>();

    public @NotNull Map<Integer, Manufacturer> getManufacturers() {
        return Collections.unmodifiableMap(manufacturers);
    }

    public @NotNull Map<Integer, Customer> getCustomers() {
        return Collections.unmodifiableMap(customers);
    }

    public @NotNull Map<Integer, ShopWarehouse> getShops() {
        return Collections.unmodifiableMap(shops);
    }

    public @NotNull Map<Integer, DeliveryAgent> getDeliveryAgents() {
        return Collections.unmodifiableMap(deliveryAgents);
    }

    public @NotNull Map<Integer, Product> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public List<Order> getOrderHistory() {
        List<Order> orderList = new ArrayList<>(unprocessedOrders.values());
        orderList.addAll(processedFailedOrders.values());
        return Collections.unmodifiableList(orderList);
    }

    public Map<Integer, Order> getUnprocessedOrders() {
        return Collections.unmodifiableMap(unprocessedOrders);
    }

    public Map<Integer, Order> getProcessedFailedOrders() {
        return Collections.unmodifiableMap(processedFailedOrders);
    }

    /**
     * Delete an unprocessed order by removing it from the unprocessed orders map.
     * @param orderID The order ID of the unprocessed order to be deleted
     * @throws InvalidOrderException When no such order exists.
     */
    public void deleteUnprocessedOrder(Integer orderID) throws InvalidOrderException {
        if (!unprocessedOrders.containsKey(orderID))
            throw new InvalidOrderException("Trying to delete a non-existent order.");
        else unprocessedOrders.remove(orderID);
    }

    /**
     * Adds the given product to this network, by adding it to the {@link Network#products}
     * @param product The product to be added.
     * @throws ExistingEntityException When the given product exists already
     */
    public void addProduct(@NotNull Product product) throws ExistingEntityException {
        if (uniqueIDs.contains(product.getUniqueId())) throw new ExistingEntityException(product.getUniqueId());
        else {
            products.put(product.getUniqueId(), product);
            uniqueIDs.add(product.getUniqueId());
        }
    }

    /**
     * Removes the given product from this network.
     * @param product The product to be removed.
     * @throws NonExistentEntityException When the given product doesn't exist in the network
     */
    public void removeProduct(@NotNull Product product) throws NonExistentEntityException {
        if (!products.containsKey(product.getUniqueId())) {
            throw new NonExistentEntityException(product.getUniqueId());
        } else {
            products.remove(product.getUniqueId());
            if(product.getProductManufacturer() != null) product.getProductManufacturer().deleteProduct(product);
            for (ShopWarehouse shop : shops.values()) {
                shop.removeProduct(product);
            }
        }
    }

    /**
     * Add the given manufacturer to the system.
     * @param manufacturer The manufacturer to be added
     * @throws ExistingEntityException When this manufacturer already exists.
     */
    public void addManufacturer(@NotNull Manufacturer manufacturer) throws ExistingEntityException {
        if (uniqueIDs.contains(manufacturer.getUniqueId()))
            throw new ExistingEntityException(manufacturer.getUniqueId());
        else {
            manufacturers.put(manufacturer.getUniqueId(), manufacturer);
            uniqueIDs.add(manufacturer.getUniqueId());
        }
    }

    /**
     * Removes the given manufacturer from this network.
     * @param manufacturer The manufacturer to be removed.
     * @throws NonExistentEntityException When the given manufacturer doesn't exist in the network
     */
    public void removeManufacturer(@NotNull Manufacturer manufacturer) throws NonExistentEntityException {
        if (!manufacturers.containsKey(manufacturer.getUniqueId())) {
            throw new NonExistentEntityException(manufacturer.getUniqueId());
        } else {
            for (Product product : List.copyOf(manufacturer.getProductList())) {
                manufacturer.deleteProduct(product);
            }
            manufacturers.remove(manufacturer.getUniqueId());
        }
    }

    /**
     * Adds the given shop/warehouse from this network.
     * @param shopWarehouse The shop/warehouse to be added.
     * @throws ExistingEntityException When the given shop/warehouse already exists in the network
     */
    public void addShop(@NotNull ShopWarehouse shopWarehouse) throws ExistingEntityException {
        if (uniqueIDs.contains(shopWarehouse.getUniqueId()))
            throw new ExistingEntityException(shopWarehouse.getUniqueId());
        else {
            shops.put(shopWarehouse.getUniqueId(), shopWarehouse);
            uniqueIDs.add(shopWarehouse.getUniqueId());
        }
    }

    /**
     * Removes the given shop/warehouse from this network.
     * @param shopWarehouse The shop/warehouse to be removed.
     * @throws NonExistentEntityException When the given shop/warehouse doesn't exist in the network
     */
    public void removeShop(@NotNull ShopWarehouse shopWarehouse) throws NonExistentEntityException {
        if (shops.remove(shopWarehouse.getUniqueId()) == null) {
            throw new NonExistentEntityException(shopWarehouse.getUniqueId());
        }
    }

    /**
     * Adds the given customer from this network.
     * @param customer The customer to be added.
     * @throws ExistingEntityException When the given customer already exists in the network
     */
    public void addCustomer(@NotNull Customer customer) throws ExistingEntityException {
        if (uniqueIDs.contains(customer.getUniqueId())) throw new ExistingEntityException(customer.getUniqueId());
        else {
            customers.put(customer.getUniqueId(), customer);
            uniqueIDs.add(customer.getUniqueId());
        }
    }

    /**
     * Removes the given customer from this network.
     * @param customer The customer to be removed.
     * @throws NonExistentEntityException When the given customer doesn't exist in the network
     */
    public void removeCustomer(@NotNull Customer customer) throws NonExistentEntityException {
        if (customers.remove(customer.getUniqueId()) == null) {
            throw new NonExistentEntityException(customer.getUniqueId());
        }
    }

    /**
     * Adds the given delivery agent from this network.
     * @param deliveryAgent The delivery agent to be added.
     * @throws ExistingEntityException When the given delivery agent already exists in the network
     */
    public void addDeliveryAgent(@NotNull DeliveryAgent deliveryAgent) throws ExistingEntityException {
        if (uniqueIDs.contains(deliveryAgent.getUniqueId()))
            throw new ExistingEntityException(deliveryAgent.getUniqueId());
        else {
            deliveryAgents.put(deliveryAgent.getUniqueId(), deliveryAgent);
            uniqueIDs.add(deliveryAgent.getUniqueId());
        }
    }

    /**
     * Removes the given delivery agent from this network.
     * @param deliveryAgent The delivery agent to be removed.
     * @throws NonExistentEntityException When the given delivery agent doesn't exist in the network
     */
    public void removeDeliveryAgent(@NotNull DeliveryAgent deliveryAgent) throws NonExistentEntityException {
        if (deliveryAgents.remove(deliveryAgent.getUniqueId()) == null) {
            throw new NonExistentEntityException(deliveryAgent.getUniqueId());
        }
    }

    public void placeOrder(@NotNull Integer productID, @NotNull Integer quantity, @NotNull Integer customerID)
            throws InvalidOrderException {
        if (!products.containsKey(productID) && !customers.containsKey(customerID))
            throw new InvalidOrderException("Invalid order: Product or customer doesn't exist.");
        final Order currentOrder = new Order(products.get(productID), quantity, customers.get(customerID));
        currentOrder.getCustomer().addOrder(currentOrder);
        unprocessedOrders.put(currentOrder.getOrderID(), currentOrder);
    }

    /**
     * Process an order by using the following algorithm.
     * 1. Choose a delivery agent with the least number of product deliveries
     * the same zipcode as the customer.
     * 2. If no such delivery agent exists, throw an exception.
     * 3. Else, continue, by choosing a shop/warehouse in the same zipcode as the customer.
     * 4. A shop/warehouse is chosen only when it has the required number of products and satisfies
     * the zipcode condition.
     * 5. If no such shop/warehouse exists, throw an error.
     * 6. "fulfill" the order by moving the order from the unprocessedOrders map to the processedFailed map,
     * which contains both processed (successful) and failed orders.
     * 7. For an order that can be "processed", call the corresponding fulfill methods on the delivery agent and
     * shop/warehouse objects. This will decrement the inventory of the shop/warehouse, and add this order
     * to the delivery agent's history.
     * @param orderID The order ID of the order that is to be processed.
     * @throws InvalidOrderException When the order cannot be processed or is invalid in some form.
     */
    public void processOrder(@NotNull Integer orderID) throws InvalidOrderException {
        final Order currentOrder = unprocessedOrders.get(orderID);
        final Integer orderZipcode = currentOrder.getZipcode();

        DeliveryAgent chosenDeliveryAgent = null;
        ShopWarehouse chosenShopWarehouse = null;

        for (DeliveryAgent deliveryAgent : deliveryAgents.values()) {
            if (deliveryAgent.getZipcode().equals(orderZipcode)) {

                if (chosenDeliveryAgent == null) chosenDeliveryAgent = deliveryAgent;

                else if (deliveryAgent.getNumberOfProductsDelivered()
                         < chosenDeliveryAgent.getNumberOfProductsDelivered()) {
                    chosenDeliveryAgent = deliveryAgent;
                }
            }
        }
        // if no suitable delivery agent could be found
        if (chosenDeliveryAgent == null) {
            currentOrder.failOrder();
            unprocessedOrders.remove(currentOrder.getOrderID());
            processedFailedOrders.put(currentOrder.getOrderID(), currentOrder);
            throw new InvalidOrderException("No delivery agent available at this zipcode");
        }

        Product currentProduct = currentOrder.getProductOrdered();
        for (ShopWarehouse shopWarehouse : shops.values()) {
            if (shopWarehouse.getInventory().get(currentProduct) == null) continue;
            else if (shopWarehouse.getZipcode().equals(orderZipcode)) {
                if (chosenShopWarehouse == null) chosenShopWarehouse = shopWarehouse;
                else if (chosenShopWarehouse.getInventory().get(currentProduct)
                         < shopWarehouse.getInventory().get(currentProduct)) {
                    chosenShopWarehouse = shopWarehouse;
                }
            }
        }
        // if no suitable shop/warehouse could be found
        if (chosenShopWarehouse == null
            || chosenShopWarehouse.getInventory().get(currentProduct) < currentOrder.getQuantity()) {
            currentOrder.failOrder();
            unprocessedOrders.remove(currentOrder.getOrderID());
            processedFailedOrders.put(currentOrder.getOrderID(), currentOrder);
            throw new InvalidOrderException("No shop/warehouse at this zipcode can satisfy this order");
        } else {
            currentOrder.processOrder(chosenDeliveryAgent, chosenShopWarehouse);
            unprocessedOrders.remove(currentOrder.getOrderID());
            processedFailedOrders.put(currentOrder.getOrderID(), currentOrder);
        }
    }
}
