package com.qk.plugin.javadoc.generator.impl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;
import com.qk.plugin.javadoc.config.JavaDocConfiguration;
import com.qk.plugin.javadoc.generator.JavaDocGenerator;
import com.qk.plugin.javadoc.model.JavaDoc;
import com.qk.plugin.javadoc.template.DocTemplateManager;
import com.qk.plugin.javadoc.template.DocTemplateProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AbstractJavaDocGenerator
 *
 * @Author: Kun Qian
 * @since 2022/6/25 14:09
 */
public abstract class AbstractJavaDocGenerator<T extends PsiElement> implements JavaDocGenerator<T> {
  protected final Project project;

  protected final JavaDocConfiguration configuration;
  protected final DocTemplateManager docTemplateManager;
  protected final DocTemplateProcessor docTemplateProcessor;
  protected final PsiElementFactory psiElementFactory;

  /**
   * Instantiates a new Abstract java doc generator.
   *
   * @param project the Project
   */
  public AbstractJavaDocGenerator(@NotNull Project project) {
    this.project = project;
    configuration = project.getService(JavaDocConfiguration.class);
    docTemplateManager = ApplicationManager.getApplication().getService(DocTemplateManager.class);
    docTemplateProcessor = ApplicationManager.getApplication().getService(DocTemplateProcessor.class);
    psiElementFactory = PsiElementFactory.getInstance(project);
  }

  @Nullable
  @Override
  public final PsiDocComment generate(@NotNull T element, @Nullable VirtualFile file) {
    PsiDocComment result = null;
    JavaDoc javadoc = doGenerate(element, file);
    if (javadoc != null) {
      result = psiElementFactory.createDocCommentFromText(javadoc.toJavaDoc());
    }
    return result;
  }

  /**
   * Generate java doc.
   *
   * @param element the Element
   * @return the Java doc
   */
  @Nullable
  protected abstract JavaDoc doGenerate(@NotNull T element, @Nullable VirtualFile file);
}
