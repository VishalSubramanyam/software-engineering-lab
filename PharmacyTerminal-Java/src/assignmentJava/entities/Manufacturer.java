package assignmentJava.entities;

import assignmentJava.exceptions.ExistingEntityException;
import assignmentJava.exceptions.NonExistentEntityException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manufacturer extends Entity {
    /**
     * The list of products manufactured by this manufacturer.
     */
    private final @NotNull ArrayList<Product> productList = new ArrayList<>();

    public Manufacturer(@NotNull String name) {
        super(name);
    }

    /**
     * Add a product to the list of products that the manufacturer makes.
     * If the product list already contains the product to be added,
     * a checked exception is raised that needs to be handled.
     *
     * @param product Product to be added to the product list of this manufacturer
     */
    public void addProduct(@NotNull Product product) {
        if (!productList.contains(product)) {
            productList.add(product);
        }
    }

    /**
     * Remove a product from the list of products that the manufacturer makes.
     *
     * @param product Product to be removed from the product list of this manufacturer
     * @throws NonExistentEntityException If the product list does not contain the product, checked exception
     *                                    is raised that needs to be handled.
     */
    public void deleteProduct(@NotNull Product product) throws NonExistentEntityException {
        if (!productList.contains(product)) throw new NonExistentEntityException(product.getUniqueId());
        else {
            productList.remove(product);
            product.setProductManufacturer(null);
        }
    }

    /**
     * Get an immutable list of products under this manufacturer
     *
     * @return Am immutable list of products
     */
    public @NotNull List<Product> getProductList() {
        return Collections.unmodifiableList(productList);
    }
}
