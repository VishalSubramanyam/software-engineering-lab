package assignmentJava.gui;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * A table that basically deletes the row that is currently
 * selected when the delete key is pressed.
 * @param <V>
 */
public class DeletionTable<V> extends Table<V> {
    /**
     * The action that has to be performed when the delete key
     * is pressed.
     */
    private final Runnable deleteAction;

    public DeletionTable(Runnable deleteAction, String... args) {
        super(args);
        this.deleteAction = deleteAction;
    }

    /**
     * The keystroke handler of this table.
     * @param keyStroke
     * @return
     */
    @Override
    public Result handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Delete) {
            try {
                deleteAction.run();
                return Result.HANDLED;
            } catch (Exception e) {
                e.printStackTrace();
                return Result.UNHANDLED;
            }
        } else return super.handleKeyStroke(keyStroke);
    }
}
