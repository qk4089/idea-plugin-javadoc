package com.qk.plugin.javadoc.config.impl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.qk.plugin.javadoc.config.JavaDocConfiguration;
import com.qk.plugin.javadoc.config.JavaDocConfiguration.JavaDocSetting;
import com.qk.plugin.javadoc.exception.SetupTemplateException;
import com.qk.plugin.javadoc.template.DocTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JavaDocConfigurationImpl
 *
 * @Author: Kun Qian
 * @since 2022/6/27 10:48
 */
@State(name = JavaDocConfiguration.COMPONENT_NAME, storages = @Storage(JavaDocConfiguration.COMPONENT_CONFIG_PATH))
public class JavaDocConfigurationImpl implements JavaDocConfiguration, PersistentStateComponent<JavaDocSetting> {
  private static final Logger LOGGER = Logger.getInstance(JavaDocConfigurationImpl.class);

  private JavaDocSetting settings;
  private DocTemplateManager templateManager;
  private boolean loadedStoredConfig = false;

  /**
   * Instantiates a new Java doc configuration object.
   */
  public JavaDocConfigurationImpl() {
    templateManager = ApplicationManager.getApplication().getService(DocTemplateManager.class);
    initSettings();
  }

  private void initSettings() {
    if (!loadedStoredConfig) {
      // setup default values
      settings = new JavaDocSetting();

      settings.setKeepOldValue(false);
      settings.setClassTemplate(templateManager.getClassTemplateText());
    }
  }

  @Override
  public JavaDocSetting getSettings() {
    return settings;
  }

  @Override
  public void setupTemplates() {
    try {
      templateManager.setClassTemplate(settings.getClassTemplate());
    } catch (SetupTemplateException e) {
      LOGGER.error(e);
      Messages.showErrorDialog("Javadocs plugin is not available, cause: " + e.getMessage(), PLUGIN_TITLE);
    }
  }

  @Nullable
  @Override
  public JavaDocSetting getState() {
    loadedStoredConfig = true;
    return settings;
  }

  @Override
  public void loadState(@NotNull JavaDocSetting javaDocSettings) {
    loadedStoredConfig = true;
    settings = javaDocSettings;
    setupTemplates();
  }
}
