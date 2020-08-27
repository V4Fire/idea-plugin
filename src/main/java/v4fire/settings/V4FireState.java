package v4fire.settings;

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.javascript.nodejs.util.NodePackage;
import com.intellij.javascript.nodejs.util.NodePackageRef;
import com.intellij.lang.javascript.linter.JSNpmLinterState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class V4FireState implements JSNpmLinterState<V4FireState> {
    public static final V4FireState DEFAULT = (new Builder()).build();
    @NotNull
    private final NodeJsInterpreterRef myInterpreterRef;
    @NotNull
    private final NodePackageRef myNodePackageRef;
    @Nullable
    private final String myCustomConfigFilePath;
    private final boolean myCustomConfigFileUsed;


    private V4FireState(@NotNull NodeJsInterpreterRef nodePath, @NotNull NodePackageRef nodePackageRef, boolean customConfigFileUsed, @Nullable String customConfigFilePath) {
        super();
        this.myCustomConfigFileUsed = customConfigFileUsed;
        this.myCustomConfigFilePath = customConfigFilePath;
        this.myInterpreterRef = nodePath;
        this.myNodePackageRef = nodePackageRef;
    }

    public boolean isCustomConfigFileUsed() {
        return this.myCustomConfigFileUsed;
    }

    @Nullable
    public String getCustomConfigFilePath() {
        return this.myCustomConfigFilePath;
    }

    @NotNull
    public NodeJsInterpreterRef getInterpreterRef() {
        return this.myInterpreterRef;
    }


    @NotNull
    public NodePackageRef getNodePackageRef() {
        return this.myNodePackageRef;
    }

    public V4FireState withLinterPackage(@NotNull NodePackageRef nodePackageRef) {


        return (new Builder(this)).setNodePackageRef(nodePackageRef).build();
    }

    public V4FireState withInterpreterRef(@NotNull NodeJsInterpreterRef ref) {
        return new V4FireState(ref, this.myNodePackageRef, this.myCustomConfigFileUsed, this.myCustomConfigFilePath);
    }

    public Builder builder() {
        return new Builder(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            V4FireState state = (V4FireState)o;
            if (this.myCustomConfigFileUsed != state.myCustomConfigFileUsed) {
                return false;
            } else if (!this.myInterpreterRef.equals(state.myInterpreterRef)) {
                return false;
            } else if (!Objects.equals(this.myNodePackageRef, state.myNodePackageRef)) {
                return false;
            } else if (!Objects.equals(this.myCustomConfigFilePath, state.myCustomConfigFilePath)) {
                return false;
            }
        }

        return false;
    }

    public int hashCode() {
        int result = this.myInterpreterRef.hashCode();
        result = 31 * result + this.myNodePackageRef.hashCode();
        result = 31 * result + (this.myCustomConfigFilePath != null ? this.myCustomConfigFilePath.hashCode() : 0);
        result = 31 * result + (this.myCustomConfigFileUsed ? 1 : 0);
        return result;
    }

    public String toString() {
        return "V4FireState{myInterpreterRef=" + this.myInterpreterRef + ", myNodePackageRef='" + this.myNodePackageRef + "', myCustomConfigFilePath='" + this.myCustomConfigFilePath + "', myCustomConfigFileUsed='" + this.myCustomConfigFileUsed + "'}";
    }

    public static class Builder {
        private boolean myCustomConfigFileUsed;
        private String myCustomConfigFilePath;
        private NodeJsInterpreterRef myInterpreterRef;
        private NodePackageRef myNodePackageRef;


        public Builder() {
            this.myCustomConfigFileUsed = false;
            this.myCustomConfigFilePath = "";
            this.myInterpreterRef = NodeJsInterpreterRef.createProjectRef();
            this.myNodePackageRef = NodePackageRef.create(new NodePackage(""));
        }

        public Builder(@NotNull V4FireState state) {
            super();
            this.myCustomConfigFileUsed = false;
            this.myCustomConfigFilePath = "";
            this.myInterpreterRef = NodeJsInterpreterRef.createProjectRef();
            this.myNodePackageRef = NodePackageRef.create(new NodePackage(""));
            this.myCustomConfigFileUsed = state.isCustomConfigFileUsed();
            this.myCustomConfigFilePath = state.getCustomConfigFilePath();
            this.myInterpreterRef = state.getInterpreterRef();
            this.myNodePackageRef = state.getNodePackageRef();
        }

        public Builder setCustomConfigFileUsed(boolean customConfigFileUsed) {
            this.myCustomConfigFileUsed = customConfigFileUsed;
            return this;
        }

        public Builder setCustomConfigFilePath(String customConfigFilePath) {
            this.myCustomConfigFilePath = customConfigFilePath;
            return this;
        }

        public Builder setNodePath(NodeJsInterpreterRef nodePath) {
            this.myInterpreterRef = nodePath;
            return this;
        }

        public Builder setNodePackageRef(NodePackageRef nodePackageRef) {
            this.myNodePackageRef = nodePackageRef;
            return this;
        }


        public V4FireState build() {
            return new V4FireState(this.myInterpreterRef, this.myNodePackageRef, this.myCustomConfigFileUsed, this.myCustomConfigFilePath);
        }
    }
}