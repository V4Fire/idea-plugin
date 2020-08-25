package v4fire;

import com.intellij.lang.javascript.linter.JSLinterInspection;
import com.intellij.lang.javascript.linter.JSLinterWithInspectionExternalAnnotator;
import org.jetbrains.annotations.NotNull;

public class V4FireInspection extends JSLinterInspection {
    @NotNull
    @Override
    protected JSLinterWithInspectionExternalAnnotator getExternalAnnotatorForBatchInspection() {
        return V4FireExternalAnnotator.getInstanceForBatchInspection();
    }
}
