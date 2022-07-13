package com.qk.plugin.javadoc.generator.impl;

import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsFileRevisionEx;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
  private final static String SYSTEM_ADMIN = "Chiro Wang";
  private final static String CURRENT_USER;

  static {
    String user = System.getenv("USER");
    CURRENT_USER = StringUtils.isNoneBlank(user) ? formatUser(user) : SYSTEM_ADMIN;
  }

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
      params.put("FIRST_COMMIT_USER", formatUser(getLast(revision, VcsFileRevisionEx::getAuthorEmail)));
    }
    if (template.contains("${FIRST_COMMIT_DATE}")) {
      params.put("FIRST_COMMIT_DATE", formatDateTime(getLast(revision, VcsFileRevision::getRevisionDate)));
    }
    if (template.contains("${LATEST_COMMIT_USER}")) {
      params.put("LATEST_COMMIT_USER", formatUser(getFirst(revision, VcsFileRevisionEx::getAuthorEmail)));
    }
    if (template.contains("${LATEST_COMMIT_DATE}")) {
      params.put("LATEST_COMMIT_DATE", formatDateTime(getFirst(revision, VcsFileRevision::getRevisionDate)));
    }
    // todo support most frequently submitted
    return params;
  }

  @NotNull
  private List<VcsFileRevision> getVcsFileRevision(VirtualFile file) {
    try {
      return GitFileHistory.collectHistory(project, VcsUtil.getFilePath(file));
    } catch (Throwable ignored) {
      return Collections.emptyList();
    }
  }

  @NotNull
  private static String formatUser(@Nullable String user) {
    if (StringUtils.isBlank(user)) {
      return CURRENT_USER;
    }
    String tmp = user.trim().split("@")[0].replace(".", " ");
    if(!tmp.contains(" ")) {
      tmp = StringUtils.capitalize(tmp).replaceAll("[A-Z]", " $0").trim();
    }
    tmp = StringUtils.capitalize(tmp.split(" ")[0]) + " " + StringUtils.capitalize(tmp.split(" ")[1]);
    return tmp.equals("Craig Zhang") ? SYSTEM_ADMIN : tmp;
  }

  @NotNull
  private static String formatDateTime(@Nullable Date date) {
    return FORMAT.format(date != null ? date.toInstant() : LocalDateTime.now());
  }

  @Nullable
  static <R> R getFirst(List<VcsFileRevision> list, Function<VcsFileRevisionEx, R> function) {
    return list.size() > 0 ? function.apply((VcsFileRevisionEx) list.get(0)) : null;
  }

  @Nullable
  static <R> R getLast(List<VcsFileRevision> list, Function<VcsFileRevisionEx, R> function) {
    return list.size() > 0 ? function.apply((VcsFileRevisionEx) Iterables.getLast(list)) : null;
  }
}
