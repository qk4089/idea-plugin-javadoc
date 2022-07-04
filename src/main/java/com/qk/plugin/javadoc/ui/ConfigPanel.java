package com.qk.plugin.javadoc.ui;

import static com.qk.plugin.javadoc.config.JavaDocConfiguration.PLUGIN_ID;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.qk.plugin.javadoc.config.JavaDocConfiguration;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ConfigPanel
 *
 * @Author: Kun Qian
 * @since 2022/6/27 10:06
 */
public class ConfigPanel implements SearchableConfigurable, Disposable {
  private ConfigPanelGUI configPanelGUI;
  private final JavaDocConfiguration javaDocConfiguration;

  public ConfigPanel(@NotNull Project project) {
    javaDocConfiguration = project.getService(JavaDocConfiguration.class);
  }

  @Override
  public @NotNull @NonNls String getId() {
    return PLUGIN_ID;
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return PLUGIN_ID;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    configPanelGUI = new ConfigPanelGUI(javaDocConfiguration.getSettings());
    configPanelGUI.reset();
    return configPanelGUI;
  }

  @Override
  public boolean isModified() {
    return configPanelGUI.isModified();
  }

  @Override
  public void apply() {
    configPanelGUI.apply();
    javaDocConfiguration.setupTemplates();
  }

  @Override
  public void reset() {
    configPanelGUI.reset();
  }

  @Override
  public void dispose() {
    configPanelGUI.disposeUIResources();
    configPanelGUI = null;
  }
}
