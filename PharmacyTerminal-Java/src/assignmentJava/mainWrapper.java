package assignmentJava;

import assignmentJava.gui.MainTUI;

/**
 * A do-nothing wrapper for the creating of the main textual
 * user interface (TUI).
 */
public class mainWrapper {
    public static void main(String[] args) {
        try {
            new MainTUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
