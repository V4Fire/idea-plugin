package v4fire.settings;

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterField;
import com.intellij.javascript.nodejs.util.NodePackageField;
import com.intellij.javascript.nodejs.util.NodePackageRef;
import com.intellij.lang.javascript.linter.AutodetectLinterPackage;
import com.intellij.lang.javascript.linter.ui.JSLinterConfigFileTexts;
import com.intellij.lang.javascript.linter.ui.JSLinterConfigFileView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.SwingHelper;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import v4fire.V4FireBundle;

import javax.swing.*;
import java.util.Collections;

public final class V4FirePanel {
    private static final JSLinterConfigFileTexts CONFIG_TEXTS = getConfigTexts();

    private final JSLinterConfigFileView myConfigFileView;
    private final boolean myFullModeDialog;
    private final boolean myAddLeftIndent;
    private final NodeJsInterpreterField myNodeInterpreterField;
    private final NodePackageField myNodePackageField;

    public V4FirePanel(@NotNull Project project, boolean fullModeDialog, boolean addLeftIndent) {
        myConfigFileView = new JSLinterConfigFileView(project, CONFIG_TEXTS, null);
        myFullModeDialog = fullModeDialog;
        myAddLeftIndent = addLeftIndent;
        myConfigFileView.setAdditionalConfigFilesProducer(() -> V4FireUtil.findAllConfigsInScope(project));
        myNodeInterpreterField = new NodeJsInterpreterField(project, false);
        myNodePackageField = AutodetectLinterPackage.createNodePackageField(
                Collections.singletonList(V4FireUtil.PACKAGE_NAME),
                myNodeInterpreterField, myConfigFileView
        );
    }


    @NotNull
    public JComponent createComponent() {

        final FormBuilder nodeFieldsWrapperBuilder = FormBuilder.createFormBuilder()
                .setHorizontalGap(UIUtil.DEFAULT_HGAP)
                .setVerticalGap(UIUtil.DEFAULT_VGAP);

        if (myAddLeftIndent) {
            nodeFieldsWrapperBuilder.setFormLeftIndent(UIUtil.DEFAULT_HGAP);
        }

        nodeFieldsWrapperBuilder.addLabeledComponent("&Node interpreter:", myNodeInterpreterField)
                .addLabeledComponent("V4Fire package:", myNodePackageField);

        FormBuilder builder = FormBuilder.createFormBuilder()
                .setHorizontalGap(UIUtil.DEFAULT_HGAP)
                .setVerticalGap(UIUtil.DEFAULT_VGAP);

        if (myAddLeftIndent) {
            builder.setFormLeftIndent(UIUtil.DEFAULT_HGAP);
        }

        JPanel panel = builder.addComponent(nodeFieldsWrapperBuilder.getPanel())
                .addComponent(myConfigFileView.getComponent())
                .addSeparator(4)
                .addVerticalGap(4)
                .getPanel();

        final JPanel centerPanel = SwingHelper.wrapWithHorizontalStretch(panel);

        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        return centerPanel;
    }

    public void handleEnableStatusChanged(boolean enabled) {
        NodePackageRef selectedRef = myNodePackageField.getSelectedRef();
        if (selectedRef == AutodetectLinterPackage.INSTANCE) {
            myConfigFileView.setEnabled(false);
        }

        myConfigFileView.onEnabledStateChanged(enabled);
    }

    @NotNull
    public V4FireState getState() {
        final V4FireState.Builder builder = new V4FireState.Builder()
                .setNodePath(myNodeInterpreterField.getInterpreterRef())
                .setNodePackageRef(myNodePackageField.getSelectedRef())
                .setCustomConfigFileUsed(myConfigFileView.isCustomConfigFileUsed())
                .setCustomConfigFilePath(myConfigFileView.getCustomConfigFilePath());


        return builder.build();
    }

    public void setState(@NotNull V4FireState state) {
        myNodeInterpreterField.setInterpreterRef(state.getInterpreterRef());
        myNodePackageField.setSelectedRef(state.getNodePackageRef());

        myConfigFileView.setCustomConfigFileUsed(state.isCustomConfigFileUsed());
        myConfigFileView.setCustomConfigFilePath(StringUtil.notNullize(state.getCustomConfigFilePath()));

        resizeOnSeparateDialog();
    }

    private void resizeOnSeparateDialog() {
        if (myFullModeDialog) {
            myNodeInterpreterField.setPreferredWidthToFitText();
            myConfigFileView.setPreferredWidthToComponents();
        }
    }

    private static JSLinterConfigFileTexts getConfigTexts() {
        return new JSLinterConfigFileTexts(
                V4FireBundle.message("v4fire.inspection.group.name"),
                "When linting a Stylus file, V4Fire looks for v4fire.json or v4fire.yaml " +
                        "starting from the file's folder and then moving up to the filesystem root" +
                        " or in the user's home directory.",
                "Select StLint configuration file (*.json|*.yaml)");
    }
}
