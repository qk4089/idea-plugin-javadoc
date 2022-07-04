package com.qk.plugin.javadoc.model;

import org.jetbrains.annotations.NotNull;

/**
 * JavaDocElements
 *
 * @Author: Kun Qian
 * @since 2022/6/27 09:52
 */
public enum JavaDocElements {
  STARTING("/*"),
  ENDING("/"),
  NEW_LINE("\n"),
  TAG_START("@"),
  LINE_START("*"),
  WHITE_SPACE(" ");

  private String presentation;

  /**
   * Instantiates a new Java doc elements.
   *
   * @param value the value
   */
  JavaDocElements(String value) {
    presentation = value;
  }

  /**
   * Gets the presentation.
   *
   * @return the Presentation
   */
  @NotNull
  public String getPresentation() {
    return presentation;
  }
}
