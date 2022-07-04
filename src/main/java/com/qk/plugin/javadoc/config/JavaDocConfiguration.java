package com.qk.plugin.javadoc.config;

import org.jetbrains.annotations.Nullable;

/**
 * JavaDocConfiguration
 *
 * @Author: Kun Qian
 * @since 2022/6/24 18:09
 */
public interface JavaDocConfiguration {
  String PLUGIN_ID = "JavaDoc Tool";
  String PLUGIN_TITLE = "JavaDoc Tool Plugin";
  /**
   * The constant COMPONENT_NAME.
   */
  String COMPONENT_NAME = "JavaDocToolConfiguration";
  /**
   * The constant COMPONENT_CONFIG_PATH.
   */
  String COMPONENT_CONFIG_PATH = "javadocs-tool-1.0.0.xml";

  @Nullable
  JavaDocSetting getSettings();

  void setupTemplates();

  class JavaDocSetting {
    private boolean keepOldValue;
    private String classTemplate;

    public boolean isKeepOldValue() {
      return keepOldValue;
    }

    public void setKeepOldValue(boolean keepOldValue) {
      this.keepOldValue = keepOldValue;
    }

    public String getClassTemplate() {
      return classTemplate;
    }

    public void setClassTemplate(String classTemplate) {
      this.classTemplate = classTemplate;
    }
  }
}
