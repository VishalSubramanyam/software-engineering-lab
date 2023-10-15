package assignmentJava.gui.panels.OrderPanels;

import assignmentJava.Network;
import assignmentJava.Order;
import assignmentJava.exceptions.InvalidOrderException;
import assignmentJava.gui.DeletionTable;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * This is a class that represents the panel responsible for displaying
 * information and deleting unprocessed orders via the user interface
 * about the unprocessed orders on the connected network.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class ViewDeleteProcessUnprocessedOrdersPanel extends Panel {
    /**
     * The network to which this panel is connected.
     */
    private final Network network;
    Table<String> table = new DeletionTable<>(
            this::deleteAction, "Order ID", "Customer", "Zipcode", "Product", "Quantity") {
        @Override
        public Result handleKeyStroke(KeyStroke keyStroke) {
            if (keyStroke.getKeyType() == KeyType.Character) {
                try {
                    if (keyStroke.getCharacter().equals('P') || keyStroke.getCharacter().equals('p')) {
                        network.processOrder(
                                Integer.valueOf(table.getTableModel().getRow(table.getSelectedRow()).get(0)));
                        updateTable();
                        new MessageDialogBuilder()
                                .setText("Order processed successfully.")
                                .setTitle("Success")
                                .addButton(MessageDialogButton.Close)
                                .build()
                                .showDialog((WindowBasedTextGUI) getTextGUI());
                        return Result.HANDLED;
                    }
                } catch (InvalidOrderException e) {
                    updateTable();
                    new ExceptionDialog(e).showDialog((WindowBasedTextGUI) getTextGUI());
                } catch (IndexOutOfBoundsException e) {
                    updateTable();
                    new ExceptionDialog(new Exception("Select a valid entry to process.")).showDialog(
                            (WindowBasedTextGUI) getTextGUI());
                }
            }
            return super.handleKeyStroke(keyStroke);
        }
    };

    public ViewDeleteProcessUnprocessedOrdersPanel(Network network) {
        this.network = network;
        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)).setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        contentPanel.addComponent(
                new Panel().addComponent(new Label("Press Delete key after selecting the entry to delete it."))
                           .addComponent(new Label("Press P after selecting the order to process it."))
                           .withBorder(Borders.doubleLineBevel()));
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

    private void deleteAction() {
        try {
            Integer orderID
                    = Integer.valueOf(table.getTableModel().getRow(table.getSelectedRow()).get(0));
            try {
                network.deleteUnprocessedOrder(orderID);
                updateTable();
                displayDeletedDialog();
            } catch (InvalidOrderException e) {
                new ExceptionDialog(e).showDialog((WindowBasedTextGUI) getTextGUI());
            }
        } catch (IndexOutOfBoundsException e) {
            new ExceptionDialog(new Exception("Please select a valid entry.")).showDialog(
                    (WindowBasedTextGUI) getTextGUI());
        }
    }

    private void displayDeletedDialog() {
        new MessageDialogBuilder()
                .setTitle("Success")
                .setText("The unprocessed order has been deleted.")
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
        for (Order unprocessedOrder : network.getUnprocessedOrders().values()) {
            tableModel.addRow(
                    unprocessedOrder.getOrderID().toString(),
                    unprocessedOrder.getCustomer().getUniqueId() + "-" + unprocessedOrder.getCustomer().getName(),
                    unprocessedOrder.getZipcode().toString(),
                    unprocessedOrder.getProductOrdered().getUniqueId() + "-" + unprocessedOrder.getProductOrdered()
                                                                                               .getName(),
                    unprocessedOrder.getQuantity().toString());
        }
        table.invalidate();
    }


}
