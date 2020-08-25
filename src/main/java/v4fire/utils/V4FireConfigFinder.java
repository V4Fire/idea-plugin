package v4fire.utils;

import com.intellij.openapi.project.Project;

import java.io.File;

public class V4FireConfigFinder {
    static public String findPath(Project project, File workingDir) {
        String cwd = project.getBasePath();

        File localConfig = NodeFinder.resolvePath(workingDir, V4FireLinterConfigFileType.STYLINTRC, "", "");

        if (localConfig.exists()) {
            return localConfig.getAbsolutePath();
        }

        assert cwd != null;
        File globalConfig = NodeFinder.resolvePath(new File(cwd), V4FireLinterConfigFileType.STYLINTRC, "", "");

        if (globalConfig.exists()) {
            return globalConfig.getAbsolutePath();
        }

        return null;
    }
}
