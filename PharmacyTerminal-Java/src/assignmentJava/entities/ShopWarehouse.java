package assignmentJava.entities;

import assignmentJava.Order;
import assignmentJava.exceptions.InvalidOrderException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShopWarehouse extends Entity {
    /**
     * This maps each product to the quantity available at this shop/warehouse.
     * If a certain product does not exist in this map, then it is implied that its quantity
     * is zero at this shop/warehouse.
     */
    private final HashMap<Product, Integer> inventory = new HashMap<>();
    /**
     * The zipcode where this shop/warehouse exists.
     */
    private Integer zipcode;

    public ShopWarehouse(String name, Integer zipcode) {
        super(name);
        this.setZipcode(zipcode);
    }

    /**
     * Getter method for the zipcode
     *
     * @return The zipcode of this shop/warehouse
     */
    public Integer getZipcode() {
        return zipcode;
    }

    /**
     * Setter method for the zipcode that is accessible only from within
     * the ShopWarehouse class so that those who use the class cannot modify
     * the zipcode intentionally or otherwise.
     *
     * @param zipcode The zipcode for this shop/warehouse
     */
    private void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Add a product to this shop/warehouse's inventory with the specified quantity.
     *
     * @param product  The product to be added
     * @param quantity The quantity of the product to be added
     */
    public void addProduct(@NotNull Product product, @NotNull Integer quantity) {
        assert quantity >= 0 : "Negative quantity supplied";
        inventory.merge(product, quantity, Integer::sum);
    }
    public void removeProduct(@NotNull Product product){
        inventory.remove(product);
    }
    /**
     * Get the inventory of this shop/warehouse.
     *
     * @return An immutable view of the inventory (product->quantity) map
     * of this shop/warehouse.
     */
    public Map<Product, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    /**
     * This method decrements the quantity of a certain product that is available in
     * the inventory based on the order.
     *
     * @param order The order object containing information about the order placed.
     * @throws InvalidOrderException When the shop/warehouse does not possess the required quantity of the product as
     *                               demanded by the order.
     */
    public void fulfillOrder(@NotNull Order order) throws InvalidOrderException {
        Product productOrdered = order.getProductOrdered();
        if (!inventory.containsKey(productOrdered) || inventory.get(productOrdered).equals(0)
            || order.getQuantity().compareTo(inventory.get(productOrdered)) > 0)
            throw new InvalidOrderException("The shop/warehouse does not have this product at the required quantity.");
        else {
            inventory.put(productOrdered, inventory.get(productOrdered) - order.getQuantity());
            if (inventory.get(productOrdered).equals(0)) inventory.remove(productOrdered);
        }
    }
}
