package dialogs;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NewFilePopup {
    static String defaultTitle = "New File";

    public static NewFilePopup open(Callback action) {
        return new NewFilePopup(action, "", defaultTitle);
    }

    public static NewFilePopup open(Callback action, String name) {
        return new NewFilePopup(action, name, defaultTitle);
    }

    public static NewFilePopup open(Callback action, String name, String title) {
        return new NewFilePopup(action, name, title);
    }

    protected NewFilePopup(Callback action, String currentName, String title) {
        JPanel panel = new JPanel();


        JTextField field = new JTextField();
        field.setColumns(30);
        PromptSupport.setPrompt("Name", field);

        field.setText(currentName);

        panel.add(field);

        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, field)
                .setTitle(title)
                .setShowBorder(true)
                .setShowShadow(false)
                .setRequestFocus(true)
                .setCancelOnWindowDeactivation(false)
                .createPopup();

        popup.showInFocusCenter();

        field.addActionListener((actionEvent) -> {
            String name = field.getText();

            if (name.length() > 0) {
                popup.cancel();
                try {
                    action.callback(name);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface Callback {
        void callback(String name) throws ExecutionException;
    }
}
