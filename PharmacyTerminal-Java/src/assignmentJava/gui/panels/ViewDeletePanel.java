package assignmentJava.gui.panels;

import assignmentJava.Network;
import assignmentJava.entities.*;
import assignmentJava.exceptions.NonExistentEntityException;
import assignmentJava.gui.DeletionTable;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.TableModel;

/**
 * This is a generic class that represents the panel responsible for viewing
 * and deleting entities via the user interface and relay that deletion to
 * the connected network.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class ViewDeletePanel<T> extends Panel {
    DeletionTable<String> table;
    final Class<T> thisClass;

    /**
     * The network to which this panel is connected.
     */
    private final Network network;

    public ViewDeletePanel(Network network, Class<T> thisClass) {
        this.thisClass = thisClass;
        this.network = network;

        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        final Panel contentPanel = new Panel();
        // Perform center alignment
        contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        if (thisClass == Manufacturer.class) {
            table = new DeletionTable<>(this::deleteAction, "Unique ID", "Manufacturer");
        } else if (thisClass == Product.class) {
            table = new DeletionTable<>(this::deleteAction, "Unique ID", "Product", "Manufacturer");
        } else if (thisClass == DeliveryAgent.class) {
            table = new DeletionTable<>(this::deleteAction, "Unique ID", "Delivery Agent", "Zipcode",
                                        "Number of products delivered");
        } else if (thisClass == Customer.class) {
            table = new DeletionTable<>(this::deleteAction, "Unique ID", "Customer", "Zipcode");
        } else if (thisClass == ShopWarehouse.class) {
            table = new DeletionTable<>(this::deleteAction, "Unique ID", "Shop/Warehouse", "Zipcode");
        }

        contentPanel.addComponent(
                new Panel().addComponent(new Label("Press Delete key after selecting the entry to delete it."))
                           .withBorder(Borders.doubleLineBevel()),
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        contentPanel.addComponent(new EmptySpace(TerminalSize.ONE));
        contentPanel.addComponent(table, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        this.addComponent(contentPanel.withBorder(Borders.doubleLineReverseBevel()));
    }

    private void deleteAction() {
        try {
            Integer uniqueID
                    = Integer.valueOf(table.getTableModel().getRow(table.getSelectedRow()).get(0));
            try {
                deleteEntity(uniqueID);
                updateTable();
                displayDeletedDialog();
            } catch (NonExistentEntityException e) {
                new ExceptionDialog(e).showDialog((WindowBasedTextGUI) getTextGUI());
            }
        } catch (IndexOutOfBoundsException e) {
            new ExceptionDialog(new Exception("Please select a valid entry.")).showDialog(
                    (WindowBasedTextGUI) getTextGUI());
        }
    }

    /**
     * Called when this panel has been added to a container (such as a Window).
     * Update the combo boxes to reflect any changes in the network.
     *
     * @param container The container to which this panel was added.
     */
    @Override
    public void onAdded(Container container) {
        updateTable();
        super.onAdded(container);
    }

    private void deleteEntity(Integer uniqueID) throws NonExistentEntityException, IndexOutOfBoundsException {
        if (uniqueID == null) throw new IndexOutOfBoundsException();
        if (thisClass == Manufacturer.class) {
            network.removeManufacturer(network.getManufacturers().get(uniqueID));
        } else if (thisClass == Product.class) {
            network.removeProduct(network.getProducts().get(uniqueID));
        } else if (thisClass == Customer.class) {
            network.removeCustomer(network.getCustomers().get(uniqueID));
        } else if (thisClass == ShopWarehouse.class) {
            network.removeShop(network.getShops().get(uniqueID));
        } else if (thisClass == DeliveryAgent.class) {
            network.removeDeliveryAgent(network.getDeliveryAgents().get(uniqueID));
        }
    }

    private void displayDeletedDialog() {
        String entity = "ERROR";
        if (thisClass == Manufacturer.class) {
            entity = "manufacturer";
        } else if (thisClass == Product.class) {
            entity = "product";
        } else if (thisClass == ShopWarehouse.class) {
            entity = "shop/warehouse";
        } else if (thisClass == Customer.class) {
            entity = "customer";
        } else if (thisClass == DeliveryAgent.class) {
            entity = "delivery agent";
        }
        new MessageDialogBuilder()
                .setTitle("Success")
                .setText("The " + entity + " has been deleted.")
                .addButton(MessageDialogButton.OK)
                .build()
                .showDialog((WindowBasedTextGUI) getTextGUI());
    }

    /**
     * Update the entries in the table by first clearing it, and then
     * adding the necessary entries through a loop by checking the network
     * or the corresponding fields in the Entities.
     */
    private void updateTable() {
        TableModel<String> tableModel = table.getTableModel();
        tableModel.clear();
        if (thisClass == Manufacturer.class) {
            for (Manufacturer manufacturer : network.getManufacturers().values()) {
                tableModel.addRow(
                        manufacturer.getUniqueId().toString(),
                        manufacturer.getName());
            }
        } else if (thisClass == Product.class) {
            for (Product product : network.getProducts().values()) {
                tableModel.addRow(
                        product.getUniqueId().toString(),
                        product.getName(),
                        (product.getProductManufacturer()
                         == null) ? "Not assigned" : product.getProductManufacturer().getName());
            }
        } else if (thisClass == DeliveryAgent.class) {
            for (DeliveryAgent deliveryAgent : network.getDeliveryAgents().values()) {
                tableModel.addRow(
                        deliveryAgent.getUniqueId().toString(),
                        deliveryAgent.getName(),
                        deliveryAgent.getZipcode().toString(),
                        deliveryAgent.getNumberOfProductsDelivered().toString());
            }
        } else if (thisClass == Customer.class) {
            for (Customer customer : network.getCustomers().values()) {
                tableModel.addRow(
                        customer.getUniqueId().toString(),
                        customer.getName(),
                        customer.getZipcode().toString());
            }
        } else if (thisClass == ShopWarehouse.class) {
            for (ShopWarehouse shopWarehouse : network.getShops().values()) {
                tableModel.addRow(
                        shopWarehouse.getUniqueId().toString(),
                        shopWarehouse.getName(),
                        shopWarehouse.getZipcode().toString());
            }
        }
        table.invalidate();
    }
}
