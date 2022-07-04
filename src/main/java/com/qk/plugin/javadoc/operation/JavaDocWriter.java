package com.qk.plugin.javadoc.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * JavaDocWriter
 *
 * @Author: Kun Qian
 * @since 2022/6/26 00:29
 */
public interface JavaDocWriter {
  /**
   * Write java doc.
   *
   * @param javaDoc       the Java doc
   * @param beforeElement the element to place javadoc before it
   */
  void write(PsiDocComment javaDoc, @NotNull PsiElement beforeElement);
}
