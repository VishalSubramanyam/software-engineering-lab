package assignmentJava.gui.panels;

import assignmentJava.Network;
import assignmentJava.Order;
import assignmentJava.entities.Customer;
import assignmentJava.entities.DeliveryAgent;
import assignmentJava.entities.Product;
import assignmentJava.entities.ShopWarehouse;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

public class ViewPurchasesCustomer extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    private final ComboBox<String> customerComboBox = new ComboBox<>();
    private final Table<String> table = new Table<>(
            "Order ID", "Product", "Quantity", "Delivery Agent", "Shop/Warehouse", "Status");
    private boolean invalidState = false;

    public ViewPurchasesCustomer(Network network) {
        this.network = network;

        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        Panel formPanel = new Panel(new GridLayout(2)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        // Add the necessary components we just created to the form panel, which
        // in turn will be added to the main component panel.
        formPanel.addComponent(new Label("Customer:"));
        formPanel.addComponent(customerComboBox);

        customerComboBox.addListener((selectedIndex, previousSelection) -> {
            if (selectedIndex >= 0) updateTable();
        });

        contentPanel.addComponent(formPanel);
        contentPanel.addComponent(new EmptySpace(TerminalSize.ONE));
        contentPanel.addComponent(table, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
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
        updateComboBox();
        updateTable();
        super.onAdded(container);
    }

    /**
     * Update the entries in the table by first clearing it, and then
     * adding the necessary entries through a loop by checking the network
     * or the corresponding fields in the Entities.
     */
    private void updateTable() {
        TableModel<String> tableModel = table.getTableModel();
        tableModel.clear();
        if (!isInvalidState()) {
            // Split using regular expressions
            Integer customerUniqueID = Integer.valueOf(customerComboBox.getSelectedItem().split("-")[0]);
            Customer currentCustomer = network.getCustomers().get(customerUniqueID);

            for (Order order : currentCustomer.getOrderHistory()) {
                Product product = order.getProductOrdered();
                DeliveryAgent deliveryAgent = order.getDeliveryAgent();
                ShopWarehouse shopWarehouse = order.getShopWarehouse();
                switch (order.getOrderStatus()) {
                    case PROCESSED -> tableModel.addRow(
                            order.getOrderID().toString(),
                            product.getUniqueId() + "-" + product.getName(),
                            order.getQuantity().toString(),
                            deliveryAgent.getUniqueId() + "-" + deliveryAgent.getName(),
                            shopWarehouse.getUniqueId() + "-" + shopWarehouse.getName(),
                            "Processed");
                    case FAILED -> tableModel.addRow(order.getOrderID().toString(),
                                                     product.getUniqueId() + "-" + product.getName(),
                                                     order.getQuantity().toString(), "", "", "Failed");
                    case UNPROCESSED -> tableModel.addRow(order.getOrderID().toString(),
                                                          product.getUniqueId() + "-" + product.getName(),
                                                          order.getQuantity().toString(), "", "", "Unprocessed");
                }
            }
        }
        table.invalidate(); // Re-draw the table as soon as possible.
    }

    private boolean isInvalidState() {
        return invalidState = invalidState || network.getCustomers().size() == 0;
    }

    /**
     * Update the combo boxes to reflect any changes in the network.
     */
    private void updateComboBox() {
        invalidState = false;
        customerComboBox.clearItems();

        /*
        Add all the corresponding entries to the combo box(es) by looping through the
        entities that the network provides.
         */
        for (Customer customer : this.network.getCustomers().values()) {
            customerComboBox.addItem(customer.getUniqueId().toString() + "-" + customer.getName());
        }

        /*
        If no valid entity exists in the network, add an entry to the combo box(es) that
        informs the user of that siutation.
         */
        if (customerComboBox.getItemCount() == 0) {
            customerComboBox.addItem("No customer available");
            invalidState = true;
        }
        customerComboBox.invalidate();
    }
}
