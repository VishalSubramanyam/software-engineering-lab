package assignmentJava.gui.panels;

import assignmentJava.Network;
import assignmentJava.entities.Product;
import assignmentJava.entities.ShopWarehouse;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.regex.Pattern;

/**
 * This class represents a panel that is used for adding a product to a shop/warehouse.
 * It has components for receiving info regarding the product to be added, its quantity,
 * and the shop/warehouse to which it should be added.
 * The corresponding method in the {@link assignmentJava.entities.ShopWarehouse} for
 * performing this action is executed.
 */
public class AddInventoryShopPanel extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    /**
     * A combo box for selecting the product to be added.
     */
    private final ComboBox<String> productComboBox = new ComboBox<>();
    /**
     * A combox box for selecting the destination shop/warehouse.
     */
    private final ComboBox<String> shopComboBox = new ComboBox<>();

    /**
     * Tells us if no valid product or shop/warehouse has been selected.
     */
    private boolean invalidState = false;

    public AddInventoryShopPanel(Network network) {
        this.network = network;

        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Main panel for holding everything else
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        Panel formPanel = new Panel(new GridLayout(2)).setLayoutData(LinearLayout.createLayoutData(
                LinearLayout.Alignment.Center));

        updateComboBoxes();
        // Allow only numerical values
        TextBox quantityTextBox = new TextBox("0").setValidationPattern(Pattern.compile("[0-9]+"));
        Button setButton = new Button("Add", () -> {
            if (invalidState || quantityTextBox.getText().isEmpty()) {
                new ExceptionDialog(new Exception(
                        "Select a valid product and shop/warehouse along with a quantity.")).showDialog(
                        (WindowBasedTextGUI) getTextGUI());
            } else {
                try {
                    // Split using regular expressions
                    Integer productUniqueID = Integer.valueOf(productComboBox.getSelectedItem().split("-")[0]);
                    Integer shopUniqueID = Integer.valueOf(
                            shopComboBox.getSelectedItem().split("-")[0]);

                    Product currentProduct = network.getProducts().get(productUniqueID);
                    ShopWarehouse currentShop = network.getShops().get(shopUniqueID);
                    Integer quantity = Integer.valueOf(quantityTextBox.getText());

                    currentShop.addProduct(currentProduct, quantity);

                    new MessageDialogBuilder()
                            .setTitle("Success")
                            .setText("Product has has been added to the shop's inventory")
                            .addButton(MessageDialogButton.OK)
                            .build()
                            .showDialog((WindowBasedTextGUI) getTextGUI());
                    quantityTextBox.setText("0"); // Setting the text inside a text box
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the necessary components we just created to the form panel, which
        // in turn will be added to the main component panel.
        formPanel.addComponent(new Label("Product:"));
        formPanel.addComponent(productComboBox);
        formPanel.addComponent(new Label("Shop/warehouse:"));
        formPanel.addComponent(shopComboBox);
        formPanel.addComponent(new Label("Quantity"));
        formPanel.addComponent(quantityTextBox);

        contentPanel.addComponent(formPanel);
        contentPanel.addComponent(
                setButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));
        this.addComponent(contentPanel.withBorder(Borders.doubleLineReverseBevel()));
    }

    /**
     * Called when this panel has been added to a container (such as a Window).
     * Update the combo boxes to reflect any changes in the network.
     *
     * @param container The container to which this panel was added.
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
        shopComboBox.clearItems();

        /*
        Add all the corresponding entries to the combo box(es) by looping through the
        entities that the network provides.
         */
        for (Product product : this.network.getProducts().values()) {
            productComboBox.addItem(product.getUniqueId().toString() + "-" + product.getName());
        }

        for (ShopWarehouse shopWarehouse : this.network.getShops().values()) {
            shopComboBox.addItem(shopWarehouse.getUniqueId().toString() + "-" + shopWarehouse.getName());
        }

        /*
        If no valid entity exists in the network, add an entry to the combo box(es) that
        informs the user of that siutation.
         */
        if (productComboBox.getItemCount() == 0) {
            productComboBox.addItem("No product available");
            invalidState = true;
        }
        if (shopComboBox.getItemCount() == 0) {
            shopComboBox.addItem("No shop/warehouse available");
            invalidState = true;
        }
    }
}
