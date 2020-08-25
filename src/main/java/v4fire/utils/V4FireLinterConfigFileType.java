package v4fire.utils;

import com.intellij.json.JsonLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import v4fire.V4FireIcons;

import javax.swing.*;

public class V4FireLinterConfigFileType extends LanguageFileType {
    private static final String STYLINTRC_EXT = "v4firerc";
    public static final String STYLINTRC = '.' + STYLINTRC_EXT;

    private V4FireLinterConfigFileType() {
        super(JsonLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "Stylus Linter";
    }

    @NotNull
    public String getDescription() {
        return "Stylus Linter configuration file";
    }

    @NotNull
    public String getDefaultExtension() {
        return STYLINTRC_EXT;
    }

    @NotNull
    public Icon getIcon() {
        assert V4FireIcons.ICON != null;
        return V4FireIcons.ICON;
    }
}