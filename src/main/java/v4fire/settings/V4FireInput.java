package v4fire.settings;

import com.intellij.lang.javascript.linter.JSLinterInput;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class V4FireInput extends JSLinterInput<V4FireState> {
    protected V4FireInput(@NotNull PsiFile psiFile, @NotNull V4FireState state, @Nullable EditorColorsScheme colorsScheme) {
        super(psiFile, state, colorsScheme);
    }
}
