package assignmentJava.gui.panels.OrderPanels;

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

/**
 * This is a class that represents the panel responsible for displaying
 * information via the user interface about the processed/failed orders on
 * the connected network.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class ViewProcessedFailedOrdersPanel extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    Table<String> table = new Table<>(
            "Order ID", "Customer", "Zipcode", "Product", "Quantity", "Delivery Agent", "Shop/Warehouse", "Status");

    public ViewProcessedFailedOrdersPanel(Network network) {
        this.network = network;
        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
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
        for (Order order : network.getProcessedFailedOrders().values()) {
            Product product = order.getProductOrdered();
            DeliveryAgent deliveryAgent = order.getDeliveryAgent();
            ShopWarehouse shopWarehouse = order.getShopWarehouse();
            Customer customer = order.getCustomer();
            switch (order.getOrderStatus()) {
                case PROCESSED -> tableModel.addRow(
                        order.getOrderID().toString(),
                        customer.getUniqueId() + "-" + customer.getName(),
                        order.getZipcode().toString(),
                        product.getUniqueId() + "-" + product.getName(),
                        order.getQuantity().toString(),
                        deliveryAgent.getUniqueId() + "-" + deliveryAgent.getName(),
                        shopWarehouse.getUniqueId() + "-" + shopWarehouse.getName(),
                        "Processed");
                case FAILED -> tableModel.addRow(order.getOrderID().toString(),
                                                 customer.getUniqueId() + "-" + customer.getName(),
                                                 order.getZipcode().toString(),
                                                 product.getUniqueId() + "-" + product.getName(),
                                                 order.getQuantity().toString(), "", "", "Failed");
                case UNPROCESSED -> tableModel.addRow(order.getOrderID().toString(),
                                                      customer.getUniqueId() + "-" + customer.getName(),
                                                      order.getZipcode().toString(),
                                                      product.getUniqueId() + "-" + product.getName(),
                                                      order.getQuantity().toString(), "", "", "Unprocessed");
            }
        }
    }
}
