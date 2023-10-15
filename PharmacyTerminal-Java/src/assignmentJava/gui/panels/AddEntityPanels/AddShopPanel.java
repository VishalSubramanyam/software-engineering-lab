package assignmentJava.gui.panels.AddEntityPanels;

import assignmentJava.Network;
import assignmentJava.entities.ShopWarehouse;
import assignmentJava.exceptions.ExistingEntityException;
import assignmentJava.gui.dialogs.ExceptionDialog;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.regex.Pattern;

/**
 * This is a class that represents the panel responsible for collecting
 * information via the user interface about the shop/warehouse to be added to
 * the connected network.
 * It contains various other panels for layout reasons that segment the
 * interface into logical and aesthetic components.
 */
public class AddShopPanel extends Panel {
    public AddShopPanel(Network network) {
        /*
        Setting the layout manager for the panel. Defines how the components
        are laid out inside the panel.
        Eg: Grid layout, linear layout, absolute layout
         */
        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        final Panel formPanel = new Panel();
        // Perform center alignment
        formPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        formPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Panel textPanel = new Panel();
        textPanel.setLayoutManager(new GridLayout(2));

        textPanel.addComponent(new Label("Name of the shop/warehouse:"));
        // Accepts only alphabetical and whitespace input
        TextBox nameInput = new TextBox().setValidationPattern(Pattern.compile("[ a-zA-Z]+"));
        textPanel.addComponent(nameInput);
        textPanel.addComponent(new Label("Zipcode:"));
        // Accepts only numerical input
        TextBox zipcodeInput = new TextBox().setValidationPattern(Pattern.compile("[0-9]+"));
        textPanel.addComponent(zipcodeInput);

        Button addButton = new Button("Add", () -> {
            // Get the text inside the text box
            String nameInputContent = nameInput.getText();
            // Check if the input is valid
            if (!nameInputContent.isBlank() && !zipcodeInput.getText().isBlank()) {
                /**
                 * The ExistingEntityException will never be thrown when the uniqueID is automatically
                 * generated. But this try-catch block has been written so that, in the future, if unique ID
                 * has to be manually entered by the user, an invalid addition can be caught
                 * and handled gracefully.
                 */
                try {
                    network.addShop(new ShopWarehouse(nameInput.getText(), Integer.valueOf(zipcodeInput.getText())));
                    nameInput.setText("");
                    zipcodeInput.setText("");
                    new MessageDialogBuilder()
                            .setText("Shop/warehouse added successfully.")
                            .setTitle("Success")
                            .addButton(MessageDialogButton.Close)
                            .build()
                            .showDialog((WindowBasedTextGUI) getTextGUI());
                } catch (ExistingEntityException e) {
                    /**
                     * Graceful handling of an invalid unique ID entered by the user.
                     * @deprecated Will not be executed since the system generated ID
                     * is guaranteed to be unique.
                     */
                    nameInput.setText("");
                    zipcodeInput.setText("");
                    new ExceptionDialog(e).showDialog((WindowBasedTextGUI) getTextGUI());
                }
            } else {
                new MessageDialogBuilder()
                        .setText("The fields cannot be empty.")
                        .setTitle("Error")
                        .addButton(MessageDialogButton.Retry)
                        .build()
                        .showDialog((WindowBasedTextGUI) getTextGUI());
            }
        });
        // Perform center alignment
        addButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        // Add the necessary components we just created to the form panel, which
        // in turn will be added to the main component panel.
        formPanel.addComponent(textPanel);
        formPanel.addComponent(addButton);

        this.addComponent(formPanel.withBorder(Borders.doubleLineReverseBevel()));
    }
}
