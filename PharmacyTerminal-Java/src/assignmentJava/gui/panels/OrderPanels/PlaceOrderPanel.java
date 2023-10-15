package assignmentJava.gui.panels.OrderPanels;

import assignmentJava.Network;
import assignmentJava.entities.Customer;
import assignmentJava.entities.Product;
import assignmentJava.exceptions.InvalidOrderException;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.regex.Pattern;

/**
 * This is a class that represents the panel responsible for collecting
 * information via the user interface about the order to be placed on
 * the connected network.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class PlaceOrderPanel extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    private final ComboBox<String> customerComboBox = new ComboBox<>();
    private final ComboBox<String> productComboBox = new ComboBox<>();
    // Accepts only numerical input
    private final TextBox quantityText = new TextBox("0").setValidationPattern(Pattern.compile("[0-9]+"));
    private boolean invalidState = false;

    public PlaceOrderPanel(Network network) {
        this.network = network;

        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel contentPanel = new Panel(new GridLayout(2)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        updateComboBoxes();

        Button placeOrderButton = new Button("Place Order", () -> {
            try {
                // Check if the input is valid
                if (quantityText.getText().isBlank()) throw new InvalidOrderException("Enter a valid quantity");
                // Split using regular expressions
                Integer productUniqueID = Integer.valueOf(productComboBox.getSelectedItem().split("-")[0]);
                Integer customerUniqueID = Integer.valueOf(customerComboBox.getSelectedItem().split("-")[0]);
                Integer quantity = Integer.valueOf(quantityText.getText());
                network.placeOrder(productUniqueID, quantity, customerUniqueID);
                new MessageDialogBuilder()
                        .setText("Order placed successfully.")
                        .setTitle("Success")
                        .addButton(MessageDialogButton.Close)
                        .build()
                        .showDialog((WindowBasedTextGUI) getTextGUI());
                quantityText.setText("0"); // Setting the text inside a text box

            } catch (Exception e) {
                new ExceptionDialog(new Exception("Invalid order")).showDialog((WindowBasedTextGUI) getTextGUI());
            }
        });

        contentPanel.addComponent(new Label("Customer:"));
        contentPanel.addComponent(customerComboBox);
        contentPanel.addComponent(new Label("Product:"));
        contentPanel.addComponent(productComboBox);
        contentPanel.addComponent(new Label("Quantity:"));
        contentPanel.addComponent(quantityText);

        this.addComponent(new EmptySpace(TerminalSize.ONE));
        this.addComponent(contentPanel);
        this.addComponent(new EmptySpace(TerminalSize.ONE));
        this.addComponent(placeOrderButton, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
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
        customerComboBox.clearItems();
        productComboBox.clearItems();

        /*
        Add all the corresponding entries to the combo box(es) by looping through the
        entities that the network provides.
         */
        for (Product product : this.network.getProducts().values()) {
            productComboBox.addItem(product.getUniqueId().toString() + "-" + product.getName());
        }

        for (Customer customer : this.network.getCustomers().values()) {
            customerComboBox.addItem(customer.getUniqueId().toString() + "-" + customer.getName());
        }

        /*
        If no valid entity exists in the network, add an entry to the combo box(es) that
        informs the user of that siutation.
         */
        if (productComboBox.getItemCount() == 0) {
            productComboBox.addItem("No product available");
            invalidState = true;
        }
        if (customerComboBox.getItemCount() == 0) {
            customerComboBox.addItem("No customer available");
            invalidState = true;
        }
    }
}
