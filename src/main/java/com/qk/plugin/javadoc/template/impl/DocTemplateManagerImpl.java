package com.qk.plugin.javadoc.template.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.qk.plugin.javadoc.exception.SetupTemplateException;
import com.qk.plugin.javadoc.template.DocTemplateManager;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * JavaDocConfigurationImpl
 *
 * @Author: Kun Qian
 * @since 2022/6/27 11:01
 */
public class DocTemplateManagerImpl implements DocTemplateManager {
  private static final Logger LOGGER = Logger.getInstance(DocTemplateManagerImpl.class);

  private static final String TEMPLATES_PATH = "/templates.xml";
  private static final String CLASS = "class";
  private static final String TEMPLATE = "template";

  private Template classTemplate;
  private final Configuration config;
  private final StringTemplateLoader templateLoader;

  public DocTemplateManagerImpl() {
    templateLoader = new StringTemplateLoader();
    config = new Configuration(Configuration.VERSION_2_3_23);
    config.setDefaultEncoding("UTF-8");
    config.setLocalizedLookup(false);
    config.setTemplateLoader(templateLoader);

    try {
      Document document = new SAXBuilder().build(this.getClass().getResourceAsStream(TEMPLATES_PATH));
      Element root = document.getRootElement();
      if (root != null) {
        Element element = root.getChild(CLASS).getChild(TEMPLATE);
        this.classTemplate = createTemplate(element.getTextTrim());
      }
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  @NotNull
  @Override
  public Template getClassTemplate() {
    return classTemplate;
  }

  @NotNull
  @Override
  public String getClassTemplateText() {
    return extractTemplate(classTemplate);
  }

  @Override
  public void setClassTemplate(@NotNull String template) throws SetupTemplateException {
    try {
      this.classTemplate = createTemplate(template);
    } catch (IOException e) {
      throw new SetupTemplateException(e);
    }
  }

  private Template createTemplate(String templateContent) throws IOException {
    String templateName = CLASS + "_" + TEMPLATE;
    if (templateLoader.findTemplateSource(templateName) != null) {
      config.clearTemplateCache();
    }
    templateLoader.putTemplate(templateName, templateContent);
    return config.getTemplate(templateName);
  }

  private String extractTemplate(Template templateData) {
    Writer writer = new StringWriter();
    try {
      templateData.dump(writer);
    } catch (IOException e) {
      return StringUtils.EMPTY;
    }
    return writer.toString();
  }
}
