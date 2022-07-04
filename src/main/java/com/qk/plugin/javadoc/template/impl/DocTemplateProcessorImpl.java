package com.qk.plugin.javadoc.template.impl;

import com.qk.plugin.javadoc.exception.SetupTemplateException;
import com.qk.plugin.javadoc.template.DocTemplateProcessor;
import freemarker.template.Template;
import java.io.StringWriter;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * DocTemplateProcessorImpl
 *
 * @Author: Kun Qian
 * @since 2022/6/27 17:01
 */
public class DocTemplateProcessorImpl implements DocTemplateProcessor {
  @NotNull
  @Override
  public String merge(@NotNull Template template, @NotNull Map<String, Object> params) {
    StringWriter writer = new StringWriter();
    try {
      template.process(params, writer);
      return writer.toString();
    } catch (Exception e) {
      throw new SetupTemplateException(e);
    }
  }
}
