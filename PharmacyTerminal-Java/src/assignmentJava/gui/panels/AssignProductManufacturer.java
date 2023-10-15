package assignmentJava.gui.panels;

import assignmentJava.Network;
import assignmentJava.entities.Manufacturer;
import assignmentJava.entities.Product;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

/**
 * This is a class that represents the panel responsible for collecting
 * information via the user interface about the product and manufacturer
 * and link them together.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class AssignProductManufacturer extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    /**
     * A combo box for selecting the product
     */
    private final ComboBox<String> productComboBox = new ComboBox<>();
    /**
     * A combo box for selecting the manufacturer to be assigned for this product
     */
    private final ComboBox<String> manufacturerComboBox = new ComboBox<>();
    private boolean invalidState = false;

    public AssignProductManufacturer(Network network) {
        this.network = network;

        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        Panel formPanel = new Panel(new GridLayout(2)).setLayoutData(LinearLayout.createLayoutData(
                LinearLayout.Alignment.Center));

        updateComboBoxes();

        Button setButton = new Button("Set", () -> {
            if (invalidState) {
                new ExceptionDialog(new Exception("Select a valid product and manufacturer.")).showDialog(
                        (WindowBasedTextGUI) getTextGUI());
            } else {
                try {
                    // Split using regular expressions
                    Integer productUniqueID = Integer.valueOf(productComboBox.getSelectedItem().split("-")[0]);
                    Integer manufacturerUniqueID = Integer.valueOf(
                            manufacturerComboBox.getSelectedItem().split("-")[0]);

                    Product currentProduct = network.getProducts().get(productUniqueID);
                    Manufacturer currentManufacturer = network.getManufacturers().get(manufacturerUniqueID);

                    currentProduct.setProductManufacturer(currentManufacturer);
                    currentManufacturer.addProduct(currentProduct);

                    new MessageDialogBuilder()
                            .setTitle("Success")
                            .setText("Product manufacturer has been set")
                            .addButton(MessageDialogButton.OK)
                            .build()
                            .showDialog((WindowBasedTextGUI) getTextGUI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the necessary components we just created to the form panel, which
        // in turn will be added to the main component panel.
        formPanel.addComponent(new Label("Product:"));
        formPanel.addComponent(productComboBox);
        formPanel.addComponent(new Label("New manufacturer:"));
        formPanel.addComponent(manufacturerComboBox);

        contentPanel.addComponent(formPanel);
        contentPanel.addComponent(
                setButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));
        this.addComponent(contentPanel.withBorder(Borders.doubleLineReverseBevel()));
    }

    /**
     * Called when this panel has been added to a container (such as a Window).
     * Update the combo boxes to reflect any changes in the network.
     *
     * @param container The container to which this has been added.
     */
    @Override
    public synchronized void onAdded(Container container) {
        updateComboBoxes();
        super.onAdded(container);
    }

    /**
     * Update the combo boxes to reflect any changes in the network.
     */
    private void updateComboBoxes() {
        invalidState = false;

        productComboBox.clearItems();
        manufacturerComboBox.clearItems();

        /*
        Add all the corresponding entries to the combo box(es) by looping through the
        entities that the network provides.
         */
        for (Product product : this.network.getProducts().values()) {
            productComboBox.addItem(product.getUniqueId().toString() + "-" + product.getName());
        }

        for (Manufacturer manufacturer : this.network.getManufacturers().values()) {
            manufacturerComboBox.addItem(manufacturer.getUniqueId().toString() + "-" + manufacturer.getName());
        }

        /*
        If no valid entity exists in the network, add an entry to the combo box(es) that
        informs the user of that siutation.
         */
        if (productComboBox.getItemCount() == 0) {
            productComboBox.addItem("No product available");
            invalidState = true;
        }
        if (manufacturerComboBox.getItemCount() == 0) {
            manufacturerComboBox.addItem("No manufacturer available");
            invalidState = true;
        }
    }
}
