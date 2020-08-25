package v4fire.settings;

import com.intellij.javascript.nodejs.PackageJsonData;
import com.intellij.lang.javascript.linter.JSLinterDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import v4fire.V4FireBundle;

import java.util.Collection;

public final class V4FireDescriptor extends JSLinterDescriptor {
    public static final String PACKAGE_NAME = "v4fire";

    @NotNull
    @Override
    public String getDisplayName() {
        return V4FireBundle.message("v4fire.configurable.name");
    }

    @Override
    public String packageName() {
        return PACKAGE_NAME;
    }

    @Override
    public boolean supportsMultipleRoots() {
        return true;
    }

    @Override
    public boolean hasConfigFiles(@NotNull Project project) {
        return V4FireUtil.hasConfigFiles(project);
    }

    @Override
    public boolean enable(@NotNull Project project, Collection<PackageJsonData> packageJsonFiles) {
        // skip if there is typescript-tslint-plugin
        if (ContainerUtil.or(packageJsonFiles, data ->
                data.isDependencyOfAnyType(V4FireUtil.TYPESCRIPT_PLUGIN_OLD_PACKAGE_NAME) ||
                        data.isDependencyOfAnyType(V4FireUtil.TYPESCRIPT_PLUGIN_PACKAGE_NAME))) {
            return false;
        }
        return super.enable(project, packageJsonFiles);
    }

    @NotNull
    @Override
    public Class<V4FireConfiguration> getConfigurationClass() {
        return V4FireConfiguration.class;
    }
}
