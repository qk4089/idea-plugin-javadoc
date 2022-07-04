package com.qk.plugin.javadoc.generator.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.vcsUtil.VcsUtil;
import com.qk.plugin.javadoc.config.JavaDocConfiguration.JavaDocSetting;
import com.qk.plugin.javadoc.model.JavaDoc;
import com.qk.plugin.javadoc.utils.JavaDocUtils;
import freemarker.template.Template;
import git4idea.history.GitFileHistory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AbstractJavaDocGenerator
 *
 * @Author: Kun Qian
 * @since 2022/6/25 14:09
 */
public class ClassJavaDocGenerator extends AbstractJavaDocGenerator<PsiClass> {
  private final static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
  private final static String CURRENT_USER = sysUser();

  /**
   * Instantiates a new Class java doc generator.
   *
   * @param project the Project
   */
  public ClassJavaDocGenerator(@NotNull Project project) {
    super(project);
  }

  @Nullable
  @Override
  protected JavaDoc doGenerate(@NotNull PsiClass element, @Nullable VirtualFile file) {
    JavaDocSetting configuration = this.configuration.getSettings();
    if (configuration != null && configuration.isKeepOldValue()) {
      return null;
    }
    Template template = docTemplateManager.getClassTemplate();
    Map<String, Object> params = getParams(docTemplateManager.getClassTemplateText(), file);
    params.put("NAME", element.getName());
    JavaDoc javadoc = JavaDocUtils.toJavaDoc(docTemplateProcessor.merge(template, params), psiElementFactory);
    // merge old java doc
    PsiElement firstElement = element.getFirstChild();
    if (firstElement instanceof PsiDocComment) {
      javadoc = JavaDocUtils.mergeJavaDocs(JavaDocUtils.createJavaDoc((PsiDocComment) firstElement), javadoc);
    }
    return javadoc;
  }

  private Map<String, Object> getParams(String template, VirtualFile file) {
    Map<String, Object> params = new HashMap<>();
    List<VcsFileRevision> revision = getVcsFileRevision(file);
    if (template.contains("${AUTHOR}")) {
      params.put("AUTHOR", CURRENT_USER);
    }
    if (template.contains("${DATE}")) {
      params.put("DATE", FORMAT.format(LocalDateTime.now()));
    }
    if (template.contains("${FIRST_COMMIT_USER}")) {
      params.put("FIRST_COMMIT_USER", revision.size() > 0 ? revision.get(revision.size() - 1).getAuthor() : CURRENT_USER);
    }
    if (template.contains("${FIRST_COMMIT_DATE}")) {
      params.put("FIRST_COMMIT_DATE", FORMAT.format(revision.size() > 0 ? revision.get(revision.size() - 1).getRevisionDate().toInstant() : LocalDateTime.now()));
    }
    if (template.contains("${LATEST_COMMIT_USER}")) {
      params.put("LATEST_COMMIT_USER", revision.size() > 0 ? revision.get(0).getAuthor() : CURRENT_USER);
    }
    if (template.contains("${LATEST_COMMIT_DATE}")) {
      params.put("LATEST_COMMIT_DATE", FORMAT.format(revision.size() > 0 ? revision.get(0).getRevisionDate().toInstant() : LocalDateTime.now()));
    }
    // todo support most frequently submitted
    return params;
  }

  @NotNull
  private List<VcsFileRevision> getVcsFileRevision(VirtualFile file) {
    try {
      return GitFileHistory.collectHistory(project, VcsUtil.getFilePath(file));
    } catch (VcsException ignored) {
      return Collections.emptyList();
    }
  }

  private static String sysUser() {
    String user = System.getenv("USER");
    return StringUtils.isNoneBlank(user) ? formatUserName(user) : "Admin";
  }

  private static String formatUserName(@NotNull String user) {
    String tmp = user.trim().split("@")[0].replace(".", " ");
    return StringUtils.capitalize(tmp).replaceAll("[A-Z]", " $0");
  }
}
