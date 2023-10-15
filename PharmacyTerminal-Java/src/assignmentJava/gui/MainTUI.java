package assignmentJava.gui;

import assignmentJava.Network;
import assignmentJava.entities.*;
import assignmentJava.gui.panels.*;
import assignmentJava.gui.panels.AddEntityPanels.*;
import assignmentJava.gui.panels.OrderPanels.PlaceOrderPanel;
import assignmentJava.gui.panels.OrderPanels.ViewDeleteProcessUnprocessedOrdersPanel;
import assignmentJava.gui.panels.OrderPanels.ViewProcessedFailedOrdersPanel;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainTUI {
    /**
     * The terminal to be used for the textual user interface. The type
     * of terminal determines the operations that are supported by it.
     */
    private final Terminal terminal = new DefaultTerminalFactory().createTerminal();
    /**
     * A screen buffer on top of the terminal that reduces unnecessary redrawing
     * and wastage of CPU cycles.
     */
    private final Screen screen = new TerminalScreen(terminal);
    private final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);

    /**
     * The main window inside which all other components live.
     */
    private final Window window = new BasicWindow("Program");

    /**
     * The network is the backbone of the entire program. It stores the state of the program
     * at any given point, and contains all the entities, order histories, etc.
     */
    private final Network network = new Network();

    /**
     * A map that maps a set of enumerated constants to a set of panels that are embedded inside
     * the main window. This helps in programmatically accessing the various parts of the TUI.
     */
    final Map<panelList, Panel> programPanels = new HashMap<>();

    public MainTUI() throws IOException {
        terminal.setCursorVisible(false);

        screen.startScreen();

        // The menu bar for the main window
        TopMenuBar topMenuBar = new TopMenuBar(textGUI);

        // A single method to create all the required panels.
        createAllPanels();
        topMenuBar.setPanels(this.programPanels);

        // Set the menu bar for this window
        window.setMenuBar(topMenuBar);
        window.addWindowListener(topMenuBar); // The menu bar listens to the key presses made inside the window.

        window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.EXPANDED)); // Window display information

        textGUI.addWindowAndWait(window); // Wait until the main window closes before exiting the program.
        screen.stopScreen();
    }

    /**
     * A single utility method to create all the required panels and map them to
     * their respective enumerated constants in {@link panelList}.
     */
    private void createAllPanels() {
        // Using the map's put method to insert all the panels into the map
        programPanels.put(panelList.ADD_MANUFACTURER, new AddManufacturerPanel(network));
        programPanels.put(panelList.ADD_CUSTOMER, new AddCustomerPanel(network));
        programPanels.put(panelList.ADD_DELIVERY_AGENT, new AddDeliveryAgentPanel(network));
        programPanels.put(panelList.ADD_SHOP, new AddShopPanel(network));
        programPanels.put(panelList.ADD_PRODUCT, new AddProductPanel(network));

        programPanels.put(panelList.VIEW_DELETE_MANUFACTURER,
                          new ViewDeletePanel<>(network, Manufacturer.class));
        programPanels.put(panelList.VIEW_DELETE_CUSTOMERS,
                          new ViewDeletePanel<>(network, Customer.class));
        programPanels.put(panelList.VIEW_DELETE_PRODUCTS,
                          new ViewDeletePanel<>(network, Product.class));
        programPanels.put(panelList.VIEW_DELETE_DELIVERY_AGENTS,
                          new ViewDeletePanel<>(network, DeliveryAgent.class));
        programPanels.put(panelList.VIEW_DELETE_SHOP,
                          new ViewDeletePanel<>(network, ShopWarehouse.class));

        programPanels.put(panelList.SET_PRODUCT_MANUFACTURER, new AssignProductManufacturer(network));
        programPanels.put(panelList.ADD_INVENTORY_SHOP, new AddInventoryShopPanel(network));
        programPanels.put(panelList.VIEW_INVENTORY_SHOP, new ViewInventoryShopPanel(network));
        programPanels.put(panelList.VIEW_PRODUCTS_MANUFACTURER, new ViewProductsManufacturer(network));

        programPanels.put(panelList.PLACE_ORDER, new PlaceOrderPanel(network));
        programPanels.put(panelList.VIEW_DELETE_PROCESS_UNPROCESSED_ORDERS, new ViewDeleteProcessUnprocessedOrdersPanel(network));
        programPanels.put(panelList.VIEW_PROCESSED_FAILED_ORDERS, new ViewProcessedFailedOrdersPanel(network));

        programPanels.put(panelList.VIEW_PURCHASES, new ViewPurchasesCustomer(network));
    }
}
