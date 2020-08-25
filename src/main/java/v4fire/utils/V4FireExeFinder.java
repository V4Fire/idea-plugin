package v4fire.utils;
import com.intellij.javascript.nodejs.util.NodePackageRef;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import v4fire.settings.V4FireState;

import java.io.File;
import java.util.Objects;

public class V4FireExeFinder {
    private static final Logger log = Logger.getInstance(V4FireExeFinder.class);

    static public String getPath(Project project, V4FireState state) {
        NodePackageRef pack = state.getNodePackageRef();

        String packagePath = null;

        if (pack.isConstant()) {
            try {
                packagePath = Objects.requireNonNull(pack.getConstantPackage()).getSystemDependentPath();
            } catch (NullPointerException e) {
                log.info(e);
            }
        }

        if (packagePath == null || packagePath.isEmpty()) {
            File tmpDir = NodeFinder.resolvePath(
                    new File(Objects.requireNonNull(project.getBasePath())),
                    "node_modules",
                   "v4fire-cli",
                    ""
            );

            if (tmpDir.exists()) {
                packagePath = tmpDir.getAbsolutePath();
            }
        }

        if (packagePath == null || packagePath.isEmpty()) {
            return null;
        }

        File packageFile = new File(packagePath);

        File exe = NodeFinder.resolvePath(
                packageFile.getParentFile().getAbsoluteFile(),
                ".bin",
                NodeFinder.getBinName("v4fire"),
                ""
        );

        if (exe.exists()) {
            return exe.getAbsolutePath();
        }

        return null;
    }
}
