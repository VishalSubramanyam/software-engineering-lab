package assignmentJava.gui.dialogs;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.util.List;

/**
 * A dialog window for display exceptions that have been caught by
 * the program.
 */
public class ExceptionDialog extends DialogWindow {
    final Panel contents = new Panel(new LinearLayout(Direction.VERTICAL));

    public ExceptionDialog(Exception e) {
        super("Error");
        Label exceptionMessage = new Label(e.getMessage());
        contents.addComponent(exceptionMessage, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        contents.addComponent(
                new Button("Ok", this::close), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        this.setComponent(contents);
        this.setHints(List.of(Hint.CENTERED));
    }
}
