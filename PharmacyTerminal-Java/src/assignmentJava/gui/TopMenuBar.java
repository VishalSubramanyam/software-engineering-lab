package assignmentJava.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is a Menu Bar that listens to the key presses inside the main window
 * and triggers various menus.
 * Activating menus will trigger a change in the main panel of the main window.
 * Each menu has an associated {@link Runnable} that is executed when the Menu is activated.
 * Runnables have been replaced with lambda expressions where possible to simplify the code.
 */
public class TopMenuBar extends MenuBar implements WindowListener {
    final Menu fileMenu = new Menu("File");
    final Menu manufacturerMenu = new Menu("Manufacturer");
    final Menu customerMenu = new Menu("Customer");
    final Menu deliveryAgentMenu = new Menu("Delivery Agent");
    final Menu orderMenu = new Menu("Order");
    final Menu productMenu = new Menu("Product");
    final Menu shopMenu = new Menu("Shop/Warehouse");
    /**
     * @see MainTUI#programPanels
     */
    Map<panelList, Panel> programPanels;

    private WindowBasedTextGUI textGUI;

    public TopMenuBar(WindowBasedTextGUI textGUI) {
        this.setTextGUI(textGUI);
        this.addAllMenus();
    }

    public void setPanels(Map<panelList, Panel> programPanels) {
        this.programPanels = programPanels;
    }

    /**
     * Utility method for adding all the different menus to this menu bar.
     */
    private void addAllMenus() {
        fileMenuCreation();
        manufacturerMenuCreation();
        customerMenuCreation();
        deliveryAgentMenuCreation();
        shopMenuCreation();
        productMenuCreation();
        orderMenuCreation();
    }

    /**
     * The file menu
     */
    private void fileMenuCreation() {
        MenuItem themeItem = new MenuItem("Set Theme", new Runnable() {
            static String currentTheme = "default";

            @Override
            public void run() {
                RadioBoxList<String> themeRadioList = new RadioBoxList<>();
                for (String theme : LanternaThemes.getRegisteredThemes()) {
                    themeRadioList.addItem(theme);
                }
                themeRadioList.setCheckedItem(currentTheme);
                themeRadioList.addListener((selectedIndex, previousSelection) -> getTextGUI().setTheme(
                        LanternaThemes.getRegisteredTheme(currentTheme = themeRadioList.getCheckedItem())));
                BasicWindow messageDialog = new BasicWindow("Set Theme");
                messageDialog.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.MODAL));
                Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
                contentPanel.addComponent(themeRadioList);

                contentPanel.addComponent(new Button("Ok", messageDialog::close).setLayoutData(
                        LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

                messageDialog.setComponent(contentPanel);

                getTextGUI().addWindowAndWait(messageDialog);
            }
        });
        MenuItem aboutItem = new MenuItem(
                "About",
                () -> MessageDialog.showMessageDialog(TopMenuBar.this.getTextGUI(), "About",
                                                      "Author: Vishal Subramanyam - "
                                                      + "20CS10081",
                                                      MessageDialogButton.Close));
        MenuItem exitItem = new MenuItem("Exit", () -> {
            try {
                ((Window) getBasePane()).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        fileMenu.add(themeItem);
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);

        this.add(fileMenu);
    }

    /**
     * The manufacturer menu
     */
    private void manufacturerMenuCreation() {
        MenuItem addManufacturerItem = new MenuItem(
                "Add", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_MANUFACTURER)));
        MenuItem viewDeleteManufacturerItem = new MenuItem(
                "View/Delete", () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_DELETE_MANUFACTURER)));

        MenuItem setProductManufacturerItem = new MenuItem(
                "Set product manufacturer", () -> getBasePane().setComponent(
                programPanels.get(panelList.SET_PRODUCT_MANUFACTURER)));
        MenuItem viewProductsItem = new MenuItem("View manufactured products", () -> getBasePane().setComponent(
                programPanels.get(panelList.VIEW_PRODUCTS_MANUFACTURER)));

        manufacturerMenu.add(addManufacturerItem);
        manufacturerMenu.add(viewDeleteManufacturerItem);
        manufacturerMenu.add(setProductManufacturerItem);
        manufacturerMenu.add(viewProductsItem);
        this.add(manufacturerMenu);
    }

    /**
     * The customer menu
     */
    private void customerMenuCreation() {
        MenuItem addCustomerItem = new MenuItem(
                "Add", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_CUSTOMER)));
        MenuItem viewDeleteCustomersItem = new MenuItem(
                "View/Delete", () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_DELETE_CUSTOMERS)));
        MenuItem viewPurchasesItem = new MenuItem("View order history", () -> getBasePane().setComponent(
                programPanels.get(panelList.VIEW_PURCHASES)));

        customerMenu.add(addCustomerItem);
        customerMenu.add(viewDeleteCustomersItem);
        customerMenu.add(viewPurchasesItem);
        this.add(customerMenu);
    }

    /**
     * The delivery agent menu
     */
    private void deliveryAgentMenuCreation() {
        MenuItem addDeliveryAgentItem = new MenuItem(
                "Add", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_DELIVERY_AGENT)));
        MenuItem viewDeleteDeliveryAgentItem = new MenuItem(
                "View/Delete", () -> getBasePane().setComponent(
                programPanels.get(panelList.VIEW_DELETE_DELIVERY_AGENTS)));

        deliveryAgentMenu.add(addDeliveryAgentItem);
        deliveryAgentMenu.add(viewDeleteDeliveryAgentItem);
        this.add(deliveryAgentMenu);
    }

    /**
     * The shop menu
     */
    private void shopMenuCreation() {
        MenuItem addShopMenuItem = new MenuItem(
                "Add", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_SHOP)));
        MenuItem viewDeleteShopMenuItem = new MenuItem(
                "View/Delete", () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_DELETE_SHOP)));
        MenuItem addInventoryMenuItem = new MenuItem(
                "Add inventory", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_INVENTORY_SHOP)));
        MenuItem viewInventoryMenuItem = new MenuItem(
                "View inventory", () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_INVENTORY_SHOP)));

        shopMenu.add(addShopMenuItem);
        shopMenu.add(viewDeleteShopMenuItem);
        shopMenu.add(addInventoryMenuItem);
        shopMenu.add(viewInventoryMenuItem);
        this.add(shopMenu);
    }

    /**
     * The product menu
     */
    private void productMenuCreation() {
        MenuItem addProductMenuItem = new MenuItem(
                "Add", () -> getBasePane().setComponent(programPanels.get(panelList.ADD_PRODUCT)));
        MenuItem viewDeleteProductMenuItem = new MenuItem(
                "View/Delete", () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_DELETE_PRODUCTS)));

        productMenu.add(addProductMenuItem);
        productMenu.add(viewDeleteProductMenuItem);
        this.add(productMenu);
    }

    /**
     * The order menu
     */
    private void orderMenuCreation() {
        MenuItem placeOrderMenuItem = new MenuItem(
                "Place an order", () -> getBasePane().setComponent(programPanels.get(panelList.PLACE_ORDER)));
        MenuItem deleteUnprocessedOrderMenuItem = new MenuItem(
                "View/delete/process unprocessed orders",
                () -> getBasePane().setComponent(programPanels.get(
                        panelList.VIEW_DELETE_PROCESS_UNPROCESSED_ORDERS)));
        MenuItem viewOrdersMenuItem = new MenuItem(
                "View processed/failed orders",
                () -> getBasePane().setComponent(programPanels.get(panelList.VIEW_PROCESSED_FAILED_ORDERS)));

        orderMenu.add(placeOrderMenuItem);
        orderMenu.add(deleteUnprocessedOrderMenuItem);
        orderMenu.add(viewOrdersMenuItem);
        this.add(orderMenu);
    }

    /**
     * Handler for the event when the window is resized
     * @param window
     * @param oldSize
     * @param newSize
     */
    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
    }

    /**
     * Handler for when the window is moved.
     * @param window
     * @param oldPosition
     * @param newPosition
     */
    @Override
    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
    }

    /**
     * This menu bar handles input from the keyboard to map key combos of the form "Alt - X" to the different menus,
     * where X is one of {F, M, C, D, S, P, O}. Each combo triggers a specific menu.
     * If Ctrl - W is pressed, the panel that's in focus is closed.
     * @param basePane The base pane of this menu bar
     * @param keyStroke Tne {@link com.googlecode.lanterna.input.KeyStroke} that was pressed
     * @param deliverEvent Should this event be delivered to the component that is currently under focus
     */
    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        if (keyStroke.isAltDown()) {
            if (keyStroke.getKeyType() == KeyType.Character) {
                switch (Character.toLowerCase(keyStroke.getCharacter())) {
                    case 'f' -> {
                        fileMenu.takeFocus();
                        fileMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        fileMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 'm' -> {
                        manufacturerMenu.takeFocus();
                        manufacturerMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        manufacturerMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 'c' -> {
                        customerMenu.takeFocus();
                        customerMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        customerMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 'd' -> {
                        deliveryAgentMenu.takeFocus();
                        deliveryAgentMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        deliveryAgentMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 's' -> {
                        shopMenu.takeFocus();
                        shopMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        shopMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 'p' -> {
                        productMenu.takeFocus();
                        productMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        productMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                    case 'o' -> {
                        orderMenu.takeFocus();
                        orderMenu.handleInput(new KeyStroke(KeyType.ArrowDown));
                        orderMenu.handleInput(new KeyStroke(KeyType.Enter));
                    }
                }
            }
            deliverEvent.set(false);
        } else if (keyStroke.isCtrlDown()) {
            if (keyStroke.getKeyType() == KeyType.Character && Character.toLowerCase(keyStroke.getCharacter()) == 'w') {
                basePane.setComponent(null);
            }
            deliverEvent.set(false);
        }
    }

    /**
     * Handler for unhandled input
     * @param basePane
     * @param keyStroke
     * @param hasBeenHandled
     */
    @Override
    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {

    }

    @Override
    public WindowBasedTextGUI getTextGUI() {
        return textGUI;
    }

    private void setTextGUI(WindowBasedTextGUI textGUI) {
        this.textGUI = textGUI;
    }
}


