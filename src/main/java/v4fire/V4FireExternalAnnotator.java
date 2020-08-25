package v4fire;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.javascript.linter.*;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import v4fire.settings.V4FireConfigurable;
import v4fire.settings.V4FireInput;
import v4fire.settings.V4FireState;

public class V4FireExternalAnnotator extends JSLinterWithInspectionExternalAnnotator<V4FireState, V4FireInput> {
    private static final V4FireExternalAnnotator INSTANCE_FOR_BATCH_INSPECTION = new V4FireExternalAnnotator();

    @NotNull
    public static V4FireExternalAnnotator getInstanceForBatchInspection() {
        return INSTANCE_FOR_BATCH_INSPECTION;
    }

    @SuppressWarnings("unused")
    public V4FireExternalAnnotator() {
        this(true);
    }

    public V4FireExternalAnnotator(boolean onTheFly) {
        super(onTheFly);
    }

    @Override
    protected @NotNull UntypedJSLinterConfigurable createSettingsConfigurable(@NotNull Project project) {
        return new V4FireConfigurable(project);
    }

    @Override
    protected Class<? extends JSLinterConfiguration<V4FireState>> getConfigurationClass() {
        return null;
    }

    @Override
    protected Class<? extends JSLinterInspection> getInspectionClass() {
        return V4FireInspection.class;
    }

    @Override
    protected boolean acceptPsiFile(@NotNull PsiFile psiFile) {
        return false;
    }

    @Nullable
    @Override
    protected V4FireInput createInfo(@NotNull PsiFile psiFile, V4FireState v4FireState, EditorColorsScheme editorColorsScheme) {
        return null;
    }

    @Override
    public void apply(@NotNull PsiFile psiFile, @Nullable JSLinterAnnotationResult jsLinterAnnotationResult, @NotNull AnnotationHolder annotationHolder) {

    }

    @Override
    protected void cleanNotification(@NotNull V4FireInput v4FireInput) {

    }
}