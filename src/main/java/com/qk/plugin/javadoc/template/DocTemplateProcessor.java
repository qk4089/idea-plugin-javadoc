package com.qk.plugin.javadoc.template;

import freemarker.template.Template;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * DocTemplateProcessor
 *
 * @Author: Kun Qian
 * @since 2022/6/27 17:01
 */
public interface DocTemplateProcessor {
  /**
   * Merge.
   *
   * @param template the Template
   * @param params   the Params
   * @return the String
   */
  @NotNull
  String merge(@NotNull Template template, @NotNull Map<String, Object> params);
}
