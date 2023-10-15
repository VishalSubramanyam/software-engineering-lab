package assignmentJava.gui.panels;

import assignmentJava.Network;
import assignmentJava.entities.Product;
import assignmentJava.entities.ShopWarehouse;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

/**
 * This is a class that represents the panel responsible for collecting
 * information via the user interface about the shop/warehouse to be analyzed on
 * the connected network and displaying its inventory.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class ViewInventoryShopPanel extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    private final ComboBox<String> shopComboBox = new ComboBox<>();
    private final Table<String> table = new Table<>("Unique ID", "Product", "Quantity");
    private boolean invalidState = false;

    public ViewInventoryShopPanel(Network network) {
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

        // Add the necessary components we just created to the form panel, which
        // in turn will be added to the main component panel.
        formPanel.addComponent(new Label("Shop/warehouse:"));
        formPanel.addComponent(shopComboBox);

        updateComboBox();
        updateTable();

        shopComboBox.addListener((selectedIndex, previousSelection) -> {
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
            Integer shopUniqueID = Integer.valueOf(shopComboBox.getSelectedItem().split("-")[0]);
            ShopWarehouse currentShop = network.getShops().get(shopUniqueID);

            for (Product product : currentShop.getInventory().keySet()) {
                tableModel.addRow(
                        product.getUniqueId().toString(),
                        product.getUniqueId().toString() + "-" + product.getName(),
                        currentShop.getInventory().get(product).toString());
            }
        }
    }

    private boolean isInvalidState() {
        return invalidState = invalidState || network.getShops().size() == 0;
    }

    /**
     * Update the combo boxes to reflect any changes in the network.
     */
    private void updateComboBox() {
        invalidState = false;
        shopComboBox.clearItems();

        /*
        Add all the corresponding entries to the combo box(es) by looping through the
        entities that the network provides.
         */
        for (ShopWarehouse shop : this.network.getShops().values()) {
            shopComboBox.addItem(shop.getUniqueId().toString() + "-" + shop.getName());
        }

        /*
        If no valid entity exists in the network, add an entry to the combo box(es) that
        informs the user of that siutation.
         */
        if (shopComboBox.getItemCount() == 0) {
            shopComboBox.addItem("No shop/warehouse available");
            invalidState = true;
        }
        shopComboBox.invalidate();
    }
}
