package v4fire.api;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import v4fire.TypeCheck;
import v4fire.utils.NodeRunner;
import v4fire.utils.V4FireExeFinder;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


public class Api {
    private Project project;

    public Api(Project __project) {
        project = __project;
    }

    public void makeBlock(String path, String name) throws ExecutionException {
        this.run(new Params(path, "make", "block", name));
    }

    public void makePage(String path,String name) throws ExecutionException {
        this.run(new Params(path, "make", "page", name));
    }

    public void rename(String path, String name, String newName) throws ExecutionException {
        this.run(new Params(path, "rename", "", name, newName));
    }

    private static final Logger log = Logger.getInstance(Api.class);
    private static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(120L);
    private static final int FILES_NOT_FOUND = 66;

    private Output.Response run(Params params) throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine
                .withCharset(StandardCharsets.UTF_8)
                .setWorkDirectory(project.getBasePath());

        commandLine.setExePath(getBinFile());
        commandLine.addParameter(params.command);

        if (!params.subject.isEmpty()) {
            commandLine.addParameter(params.subject);
        }

        commandLine.addParameter(params.name);

        if (!params.newName.isEmpty()) {
            commandLine.addParameter(params.newName);
        }

        commandLine.addParameter(params.path);
        commandLine.addParameter("--reporter");
        commandLine.addParameter("json");

        Result result = new Result();
        System.out.println(commandLine.toString());

        try {
            final ProcessOutput[] outNode = {null};

            ProgressManager.getInstance().runProcessWithProgressSynchronously(
                    () -> {
                        try {
                            outNode[0] = ReadAction.compute(() -> NodeRunner.execute(commandLine, TIME_OUT));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }, "Node execution", false, project
            );

            ProcessOutput out = outNode[0];
            result.errorOutput = out.getStderr();

            try {
                if (out.getExitCode() != FILES_NOT_FOUND) {
                    result.output = out.getStdout();
                    result.isOk = true;
                }
            } catch (Exception e) {
                log.error(out.getStdout());
                result.errorOutput = out.getStdout();
            }
        } catch (Exception e) {
            result.errorOutput = e.toString();
        }

        if (!result.errorOutput.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    result.errorOutput, "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        Output.Response response = null;

        try {
            System.out.println(result.output);
            response = Output.parse(result.output);
        } catch (Exception ignored) {
            log.error(result.output);
        }

        VirtualFileManager.getInstance().asyncRefresh(null);

        if (response == null || response.status) {
            log.info("stylus passed");
            return response;
        }

        return response;
    }

    @Nullable
    private String getBinFile() {
        String exePath = V4FireExeFinder.getPath(project, TypeCheck.getState(project));

        if (exePath == null || exePath.isEmpty()) {
            log.info("V4Fire is not installed");
            return null;
        }

        return exePath;
    }

    private class Result {
        String errorOutput;
        String output;
        boolean isOk;
    }

    private class Params {
        @NotNull String command;
        @NotNull String subject;
        @NotNull String name;
        @NotNull String newName;
        @NotNull String path;

        public Params(
                String __path,
                String __command,
                String __subject,
                String __name
        ) {
            this(__path, __command, __subject, __name, "");
        };

        public Params(
                String __path,
                String __command,
                String __subject,
                String __name,
                String __newName
        ) {
            path = __path;
            command = __command;
            subject = __subject;
            name = __name;
            newName = __newName;
        }
    }
}
