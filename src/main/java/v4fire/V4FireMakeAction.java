package v4fire;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class V4FireMakeAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        final PsiElement psiFile = e.getData(PlatformDataKeys.PSI_ELEMENT);
        boolean isFileOrDirectory = psiFile instanceof PsiFile || psiFile instanceof PsiDirectory;

        String path = "";

        if (isFileOrDirectory) {
            if (psiFile instanceof PsiFile) {
                path = ((PsiFile)psiFile).getVirtualFile().getParent().getCanonicalPath();
            } else {
                path = ((PsiDirectory)psiFile).getVirtualFile().getCanonicalPath();
            }
        }

        String pattern = ".*/(b|p)-[a-z0-9-]+$";

        e.getPresentation().setEnabledAndVisible(path.matches(pattern));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Using the event, implement an action. For example, create and show a dialog.
    }
}
