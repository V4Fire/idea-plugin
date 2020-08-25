package v4fire.settings;

import com.intellij.javascript.nodejs.util.JSLinterPackage;
import com.intellij.lang.javascript.linter.JSLinterConfiguration;
import com.intellij.lang.javascript.linter.JSLinterInspection;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import v4fire.V4FireInspection;

@State(name = "V4FireConfiguration", storages = @Storage("v4fire/v4fire.xml"))
public class V4FireConfiguration extends JSLinterConfiguration<V4FireState> {
    private static final String V4FIRE_ELEMENT_NAME = "v4fire";
    private static final String IS_CUSTOM_CONFIG_FILE_USED_ATTRIBUTE_NAME = "use-custom-config-file";
    private static final String CUSTOM_CONFIG_FILE_PATH_ATTRIBUTE_NAME = "custom-config-file-path";

    private final JSLinterPackage myPackage;

    public V4FireConfiguration(@NotNull Project project) {
        super(project);
        myPackage = new JSLinterPackage(project, "v4fire", true);
    }

    @NotNull
    public static V4FireConfiguration getInstance(@NotNull final Project project) {
        return JSLinterConfiguration.getInstance(project, V4FireConfiguration.class);
    }

    @Override
    protected void savePrivateSettings(@NotNull V4FireState state) {
        storeLinterLocalPaths(state);
    }

    @NotNull
    @Override
    protected V4FireState loadPrivateSettings(@NotNull V4FireState state) {
        V4FireState.Builder builder = new V4FireState.Builder(state);
        restoreLinterLocalPaths(builder);
        return builder.build();
    }

    @Override
    protected @NotNull Class<? extends JSLinterInspection> getInspectionClass() {
        return V4FireInspection.class;
    }

    @Nullable
    @Override
    protected Element toXml(@NotNull V4FireState state) {
        final Element root = new Element(V4FIRE_ELEMENT_NAME);
        if (state.isCustomConfigFileUsed()) {
            root.setAttribute(IS_CUSTOM_CONFIG_FILE_USED_ATTRIBUTE_NAME, Boolean.TRUE.toString());
        }

        final String customConfigFilePath = state.getCustomConfigFilePath();

        if (!StringUtil.isEmptyOrSpaces(customConfigFilePath)) {
            root.setAttribute(CUSTOM_CONFIG_FILE_PATH_ATTRIBUTE_NAME, FileUtil.toSystemIndependentName(customConfigFilePath));
        }

        storeLinterLocalPaths(state);
        return root;
    }

    @NotNull
    @Override
    protected V4FireState fromXml(@NotNull Element element) {
        final V4FireState.Builder builder = new V4FireState.Builder();

        builder.setCustomConfigFileUsed(Boolean.parseBoolean(element.getAttributeValue(IS_CUSTOM_CONFIG_FILE_USED_ATTRIBUTE_NAME)));
        String customConfigFilePath = StringUtil.notNullize(element.getAttributeValue(CUSTOM_CONFIG_FILE_PATH_ATTRIBUTE_NAME));
        builder.setCustomConfigFilePath(FileUtil.toSystemDependentName(customConfigFilePath));


        restoreLinterLocalPaths(builder);
        return builder.build();
    }

    private void restoreLinterLocalPaths(V4FireState.Builder builder) {
        myPackage.readOrDetect();
        builder.setNodePath(myPackage.getInterpreter());
        builder.setNodePackageRef(myPackage.getPackage());
    }

    private void storeLinterLocalPaths(V4FireState state) {
        myPackage.force(state.getInterpreterRef(), state.getNodePackageRef());
    }

    @NotNull
    @Override
    protected V4FireState getDefaultState() {
        return V4FireState.DEFAULT;
    }
}