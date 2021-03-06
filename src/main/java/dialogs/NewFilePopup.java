package dialogs;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jdesktop.swingx.prompt.PromptSupport;
import v4fire.api.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class NewFilePopup {
    public static NewFilePopup open(Callback action) {
        return new NewFilePopup(action, "", true);
    }

    public static NewFilePopup open(Callback action, String name, Boolean newMode) {
        return new NewFilePopup(action, name, newMode);
    }

    protected NewFilePopup(Callback action, String currentName, Boolean newMode) {
        JPanel panel = new JPanel();


        JTextField field = new JTextField();
        field.setColumns(30);
        PromptSupport.setPrompt("Name", field);

        field.setText(currentName);

        panel.add(field);

        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, field)
                .setTitle(newMode ? "New" : "Rename")
                .setShowBorder(true)
                .setShowShadow(false)
                .setRequestFocus(true)
                .setCancelOnWindowDeactivation(false)
                .createPopup();

        if (newMode) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(20, 20));

            try {
                Image img = ImageIO.read(getClass().getResource("/icons/pluginIcon.png"));
                btn.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                System.out.println("error");
                System.out.println(ex.getMessage());
            }

            panel.add(btn);

            btn.addActionListener((actionEvent) -> {
                popup.cancel();
                NewFileDialog.main(action, field.getText());
            });
        }

        popup.showInFocusCenter();

        field.addActionListener((actionEvent) -> {
            String name = field.getText();

            if (name.length() > 0) {
                popup.cancel();

                try {
                    Data data = new Data();
                    data.oldName = currentName;
                    data.newName = name;
                    action.callback(data);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface Callback {
        void callback(Data data) throws ExecutionException;
    }
}
