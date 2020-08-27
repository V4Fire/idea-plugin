package v4fire.actions;
import com.intellij.openapi.project.Project;
import dialogs.NewFilePopup;
import v4fire.api.Data;

public class MakePageAction extends BaseAction {
    @Override
    protected boolean isSuitFolder(String path) {
        return path.matches(".*/src/.*");
    }

    @Override
    protected void callAction(String path, Project project) {
        NewFilePopup.open((Data data) -> {
            api.makePage(path, data);
        });
    }
}
