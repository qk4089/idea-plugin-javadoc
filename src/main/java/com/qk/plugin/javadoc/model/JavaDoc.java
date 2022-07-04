package com.qk.plugin.javadoc.model;

import com.qk.plugin.javadoc.utils.JavaDocUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * JavaDoc
 *
 * @Author: Kun Qian
 * @since 2022/6/27 09:32
 */
public class JavaDoc {
  private List<String> description;
  private Map<String, Collection<String>> tags;

  /**
   * Instantiates a new Java doc.
   *
   * @param description the Description
   * @param tags        the Tags
   */
  public JavaDoc(@NotNull List<String> description, @NotNull Map<String, Collection<String>> tags) {
    this.description = description;
    this.tags = tags;
  }

  /**
   * Gets the description.
   *
   * @return the Description
   */
  @NotNull
  public List<String> getDescription() {
    return description;
  }

  /**
   * Gets the tags.
   *
   * @return the Tags
   */
  @NotNull
  public Map<String, Collection<String>> getTags() {
    return tags;
  }

  /**
   * To java doc.
   *
   * @return the String
   */
  @NotNull
  public String toJavaDoc() {
    return JavaDocUtils.convertJavaDoc(this);
  }
}
