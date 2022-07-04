package com.qk.plugin.javadoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.qk.plugin.javadoc.config.JavaDocConfiguration;
import com.qk.plugin.javadoc.generator.JavaDocGenerator;
import com.qk.plugin.javadoc.generator.impl.ClassJavaDocGenerator;
import com.qk.plugin.javadoc.operation.JavaDocWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * DocGenerateAction
 *
 * @Author: Kun Qian
 * @since 2022/6/24 18:03
 */
public class DocGenerateAction extends AnAction {
  private static final Logger LOGGER = Logger.getInstance(DocGenerateAction.class);

  private final JavaDocWriter writer;

  public DocGenerateAction() {
    this.writer = ApplicationManager.getApplication().getService(JavaDocWriter.class);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    DumbService dumbService = DumbService.getInstance(e.getProject());
    if (dumbService.isDumb()) {
      dumbService.showDumbModeNotification("JavaDoc plugin is not available during indexing");
      return;
    }

    Editor editor = CommonDataKeys.EDITOR.getData(e.getDataContext());
    PsiFile file = CommonDataKeys.PSI_FILE.getData(e.getDataContext());

    if (editor == null || file == null) {
      LOGGER.error("Cannot get com.intellij.openapi.editor.Editor, com.intellij.psi.PsiFile");
      Messages.showErrorDialog("JavaDoc plugin is not available", JavaDocConfiguration.PLUGIN_TITLE);
      return;
    }
    processFile(file);
  }

  protected void processFile(PsiFile file) {
    JavaDocGenerator<PsiClass> generator = new ClassJavaDocGenerator(file.getProject());
    file.getVirtualFile().getPath();

    for (PsiClass element : getClasses(file)) {
      PsiDocComment javaDoc = generator.generate(element, file.getVirtualFile());
      if (javaDoc != null) {
        writer.write(javaDoc, element);
      }
    }
  }

  private List<PsiClass> getClasses(PsiElement element) {
    List<PsiClass> classElements = PsiTreeUtil.getChildrenOfTypeAsList(element, PsiClass.class);
    List<PsiClass> elements = new LinkedList<>(classElements);
    for (PsiClass classElement : classElements) {
      elements.addAll(getClasses(classElement));
    }
    return elements;
  }
}
