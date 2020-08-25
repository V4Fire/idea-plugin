package v4fire.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import v4fire.api.Api;

public abstract class BaseAction  extends AnAction {
    protected Api api;

    @Nullable
    protected String getPath(@Nullable PsiElement psiFile) {
        if (psiFile == null) {
            return null;
        }

        boolean isFileOrDirectory = psiFile instanceof PsiFile || psiFile instanceof PsiDirectory;

        @Nullable String path = null;

        if (isFileOrDirectory) {
            if (psiFile instanceof PsiFile) {
                path = ((PsiFile)psiFile).getVirtualFile().getParent().getCanonicalPath();
            } else {
                path = ((PsiDirectory)psiFile).getVirtualFile().getCanonicalPath();
            }
        }

        return path;
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        final PsiElement psiFile = e.getData(PlatformDataKeys.PSI_ELEMENT);
        String path = getPath(psiFile);
        e.getPresentation().setEnabledAndVisible(path != null && isSuitFolder(path));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Set the availability based on whether a project is open
        final PsiElement psiFile = e.getData(PlatformDataKeys.PSI_ELEMENT);
        String path = getPath(psiFile);

        final Project project = e.getData(PlatformDataKeys.PROJECT);
        api = new Api(project);

        callAction(path, project);
    }

    protected abstract boolean isSuitFolder(String path);
    protected void callAction(String path, Project project) {};
}
