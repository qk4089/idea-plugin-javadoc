package com.qk.plugin.javadoc.ui;

import com.intellij.ui.table.JBTable;
import com.qk.plugin.javadoc.config.JavaDocConfiguration.JavaDocSetting;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * ConfigPanelGUI
 *
 * @Author: Kun Qian
 * @since 2022/6/27 13:59
 */
public class ConfigPanelGUI extends JPanel {
  private JPanel panel;
  private JCheckBox keep;
  private JTextArea template;
  private JTable variables;

  private final JavaDocSetting settings;

  public final static Map<String, String> keywords = new LinkedHashMap<>() {{
    put("${NAME}", "class name");
    put("${AUTHOR}", "current user");
    put("${DATE}", "current time");
    put("${FIRST_COMMIT_USER}", "first commit author");
    put("${FIRST_COMMIT_DATE}", "first commit time");
    put("${LATEST_COMMIT_USER}", "latest commit author");
    put("${LATEST_COMMIT_DATE}", "latest commit time");
  }};

  public ConfigPanelGUI(JavaDocSetting settings) {
    this.settings = settings;
    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
  }

  public boolean isModified() {
    return settings.isKeepOldValue() != keep.isSelected() ||
        !settings.getClassTemplate().equals(template.getText());
  }

  public void apply() {
    settings.setKeepOldValue(keep.isSelected());
    settings.setClassTemplate(template.getText());
  }

  public void reset() {
    keep.setSelected(settings.isKeepOldValue());
    template.setText(settings.getClassTemplate());
  }

  public void disposeUIResources() {
  }

  private void createUIComponents() {
    DefaultTableModel tableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    Object[][] data = keywords.entrySet().stream()
        .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
        .toArray(Object[][]::new);
    tableModel.setDataVector(data, new String[]{"Variable", "Describe"});
    variables = new JBTable(tableModel);
    variables.setRowHeight(25);
    variables.setShowGrid(false);
    variables.setCellSelectionEnabled(true);

    variables.getTableHeader().setFont(new Font(null, Font.PLAIN, 15));
    TableColumn column = variables.getColumnModel().getColumn(0);
    column.setMinWidth(160);
    column.setMaxWidth(260);
  }
}
