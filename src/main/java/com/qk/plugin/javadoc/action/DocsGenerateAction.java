package com.qk.plugin.javadoc.action;

import static com.qk.plugin.javadoc.config.JavaDocConfiguration.PLUGIN_TITLE;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

/**
 * DocsGenerateAction
 *
 * @Author: Kun Qian
 * @since 2022/6/29 11:16
 */
public class DocsGenerateAction extends DocGenerateAction {
  private static final Logger LOGGER = Logger.getInstance(DocsGenerateAction.class);

  @Override
  public void actionPerformed(AnActionEvent e) {
    DumbService dumbService = DumbService.getInstance(e.getProject());
    if (dumbService.isDumb()) {
      dumbService.showDumbModeNotification("JavaDoc plugin is not available during indexing");
      return;
    }

    Project project = CommonDataKeys.PROJECT.getData(e.getDataContext());
    VirtualFile[] files = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());

    if (project == null || files == null) {
      LOGGER.error("Cannot get com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile");
      Messages.showErrorDialog("JavaDoc plugin is not available", PLUGIN_TITLE);
      return;
    }
    processFiles(project, files);
  }

  private void processFiles(Project project, VirtualFile[] files) {
    for (VirtualFile virtualFile : files) {
      if (virtualFile.isDirectory()) {
        processFiles(project, virtualFile.getChildren());
      } else {
        PsiFile file = convertToPsiFile(project, virtualFile);
        processFile(file);
      }
    }
  }

  private PsiFile convertToPsiFile(Project project, VirtualFile file) {
    PsiManager manager = PsiManager.getInstance(project);
    return manager.findFile(file);
  }
}
