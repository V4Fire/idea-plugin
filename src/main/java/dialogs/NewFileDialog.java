package dialogs;

import com.intellij.execution.ExecutionException;
import com.sun.istack.NotNull;
import v4fire.api.Data;

import javax.swing.*;
import java.awt.event.*;

public class NewFileDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    public JTextField name;
    private JComboBox template;
    private JComboBox extend;

    public NewFileDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(e);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @NotNull
    public NewFilePopup.Callback onOkHandler;

    private void onOK(ActionEvent e) {
        // add your code here
        try {
            Data data = new Data();

            data.newName = name.getText();
            data.extend = extend.getSelectedItem().toString().toLowerCase();
            data.template = template.getSelectedItem().toString().toLowerCase();

            onOkHandler.callback(data);
        } catch (ExecutionException error) {
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(NewFilePopup.Callback onOk, String name) {
        NewFileDialog dialog = new NewFileDialog();
        dialog.name.setText(name);
        dialog.onOkHandler = onOk;
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
