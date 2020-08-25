package v4fire.settings;

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.lang.javascript.linter.AutodetectLinterPackage;
import com.intellij.lang.javascript.linter.JSLinterConfigurable;
import com.intellij.lang.javascript.linter.JSLinterView;
import com.intellij.lang.javascript.linter.NewLinterView;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import v4fire.V4FireBundle;

public class V4FireConfigurable extends JSLinterConfigurable<V4FireState> {
    @NonNls public static final String SETTINGS_JAVA_SCRIPT_LINTERS_V4FIRE = "settings.v4fire";

    public V4FireConfigurable(@NotNull Project project) {
        super(project, V4FireConfiguration.class, false);
    }

    @NotNull
    @Override
    protected JSLinterView<V4FireState> createView() {
        return new NewV4FireView(myProject, getDisplayName(), new V4FirePanel(getProject(), isFullModeDialog(), false));
    }

    @NotNull
    @Override
    public String getId() {
        return SETTINGS_JAVA_SCRIPT_LINTERS_V4FIRE;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return V4FireBundle.message("v4fire.configurable.name");
    }

    private static class NewV4FireView extends NewLinterView<V4FireState> {
        private final V4FirePanel myPanel;

        NewV4FireView(Project project, String displayName, V4FirePanel panel) {
            super(project, displayName, panel.createComponent(), "v4fire.json");
            myPanel = panel;
        }

        @NotNull
        @Override
        protected V4FireState getStateWithConfiguredAutomatically() {
            return V4FireState.DEFAULT
                    .withInterpreterRef(NodeJsInterpreterRef.createProjectRef())
                    .withLinterPackage(AutodetectLinterPackage.INSTANCE);
        }

        @Override
        protected void handleEnabledStatusChanged(boolean enabled) {

            myPanel.handleEnableStatusChanged(enabled);
        }

        @Override
        protected void setState(@NotNull V4FireState state) {
            myPanel.setState(state);
        }

        @NotNull
        @Override
        protected V4FireState getState() {
            return myPanel.getState();
        }
    }
}

