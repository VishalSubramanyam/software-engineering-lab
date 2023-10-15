package assignmentJava.entities;

public class Product extends Entity {
    /**
     * This product's manufacturer.
     */
    private Manufacturer productManufacturer;

    public Product(String name) {
        super(name);
    }

    /**
     * Sets the manufacturer for this product.
     *
     * @param manufacturer The manufacturer who makes this product
     * @see Product#productManufacturer
     */
    public void setProductManufacturer(Manufacturer manufacturer) {
        productManufacturer = manufacturer;
    }

    /**
     * Get the manufacturer of this product.
     * @return The manufacturer of this product.
     */
    public Manufacturer getProductManufacturer() {
        return productManufacturer;
    }
}
