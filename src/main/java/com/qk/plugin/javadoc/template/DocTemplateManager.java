package com.qk.plugin.javadoc.template;

import com.qk.plugin.javadoc.exception.SetupTemplateException;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

/**
 * DocTemplateManager
 *
 * @Author: Kun Qian
 * @since 2022/6/27 10:55
 */
public interface DocTemplateManager {
  @NotNull
  Template getClassTemplate();

  @NotNull
  String getClassTemplateText();

  void setClassTemplate(@NotNull String template) throws SetupTemplateException;
}
