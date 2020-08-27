package v4fire.actions;
import com.intellij.openapi.project.Project;
import dialogs.NewFilePopup;
import v4fire.api.Api;
import v4fire.api.Data;

public class MakeBlockAction extends BaseAction {
    @Override
    protected boolean isSuitFolder(String path) {
        return path.matches(".*/src/.*");
    }

    @Override
    protected void callAction(String path, Project project) {
        NewFilePopup.open((Data data) -> {
            final Api api = new Api(project);
            api.makeBlock(path, data);
        });
    }
}
