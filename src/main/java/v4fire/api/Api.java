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
import v4fire.settings.V4FireConfiguration;
import v4fire.settings.V4FireState;
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

    public void makeBlock(String path, Data data) throws ExecutionException {
        this.run(new Params(path, "make", "block", data));
    }

    public void makePage(String path, Data data) throws ExecutionException {
        this.run(new Params(path, "make", "page", data));
    }

    public void rename(String path, Data data) throws ExecutionException {
        this.run(new Params(path, "rename", "", data));
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

        switch (params.command) {
            case "make":
                commandLine.addParameter(params.subject);
                commandLine.addParameter(params.data.newName);
                break;

            case "rename":
                commandLine.addParameter(params.data.oldName);
                commandLine.addParameter(params.data.newName);
                break;
        }

        commandLine.addParameter(params.path);

        if (!params.data.extend.isEmpty()) {
            commandLine.addParameter("--extend");
            commandLine.addParameter(params.data.extend);
        }

        if (!params.data.template.isEmpty()) {
            commandLine.addParameter("--template");
            commandLine.addParameter(params.data.template);
        }

        commandLine.addParameter("--reporter");
        commandLine.addParameter("json");

        Result result = new Result();
        System.out.println(commandLine.toString());

        try {
            ProcessOutput out = ProgressManager.getInstance().runProcessWithProgressSynchronously(
                    () -> NodeRunner.execute(commandLine, TIME_OUT),
                    "Node execution", false, project
            );

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
            showError(result.errorOutput);
        }

        Output.Response response = null;

        try {
            try {
                System.out.println(result.output);
                response = Output.parse(result.output);
            } catch (Exception ignored) {
                log.error(result.output);
            }

            if (response != null && response.data != null && response.data.message != null) {
                showError(response.data.message);
            }


            if (response == null || response.status) {
                log.info("stylus passed");
                return response;
            }
        } finally {
            VirtualFileManager.getInstance().asyncRefresh(null);
        }

        return response;
    }

    private void showError(String errorOutput) {
        JOptionPane.showMessageDialog(
                null,
                errorOutput, "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Nullable
    private String getBinFile() {
        String exePath = V4FireExeFinder.getPath(project, getState(project));

        if (exePath == null || exePath.isEmpty()) {
            log.info("V4Fire is not installed");
            return null;
        }

        return exePath;
    }

    public static V4FireState getState(Project project) {
        return V4FireConfiguration.getInstance(project).getExtendedState().getState();
    }

    private class Result {
        String errorOutput;
        String output;
        boolean isOk;
    }

    private class Params {
        @NotNull String command;
        @NotNull String subject;
        @NotNull String path;
        @NotNull Data data;

        public Params(
                String __path,
                String __command,
                String __subject,
                Data __data
        ) {
            path = __path;
            command = __command;
            subject = __subject;
            data = __data;
        }
    }
}
