package v4fire.actions;

import com.intellij.openapi.project.Project;
import dialogs.NewFilePopup;

import java.io.File;

public class RenameBlockAction extends BaseAction {
    @Override
    protected boolean isSuitFolder(String path) {
        return path.matches(".*/src/.*/(b|p)-[a-z0-9-]+$");
    }

    @Override
    protected void callAction(String path, Project project) {
        File file = new File(path);
        String oldName = file.getName();

        NewFilePopup.open((String newName) -> {
            api.rename(file.getParent(), oldName, newName);
        }, oldName, false);
    }
}
