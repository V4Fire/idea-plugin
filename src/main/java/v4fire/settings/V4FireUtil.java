package v4fire.settings;

import com.intellij.lang.javascript.linter.JSLinterConfigFileUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class V4FireUtil {
    private V4FireUtil() {
    }

    public static final Logger LOG = Logger.getInstance("org.v4fire.V4Fire");
    public static final String PACKAGE_NAME = "v4fire";
    public static final String V4FIRE_JSON = "v4fire.json";

    public static final String TYPESCRIPT_PLUGIN_OLD_PACKAGE_NAME = "v4fire-language-service";
    public static final String TYPESCRIPT_PLUGIN_PACKAGE_NAME = "v4fire";

    public static final String[] CONFIG_FILE_NAMES = new String[]{V4FIRE_JSON, ".v4firerc", "v4fire.yaml", "v4fire.yml"};

    public static boolean isConfigFile(@NotNull VirtualFile file) {
        if (!file.isValid() || file.isDirectory()) {
            return false;
        }
        CharSequence name = file.getNameSequence();
        for (String fileName : CONFIG_FILE_NAMES) {
            if (StringUtil.equals(name, fileName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConfigFiles(@NotNull Project project) {
        return JSLinterConfigFileUtil.hasConfigFiles(project, CONFIG_FILE_NAMES);
    }

    @NotNull
    public static List<VirtualFile> findAllConfigsInScope(@NotNull Project project) {
        return JSLinterConfigFileUtil.findAllConfigs(project, CONFIG_FILE_NAMES);
    }
}