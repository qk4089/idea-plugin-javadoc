package com.qk.plugin.javadoc.generator;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JavaDocGenerator
 *
 * @Author: Kun Qian
 * @since 2022/6/25 11:50
 */
public interface JavaDocGenerator<T extends PsiElement> {
  /**
   * Generate java docs.
   *
   * @param element the Element
   * @return the Psi doc comment
   */
  @Nullable
  PsiDocComment generate(@NotNull T element, @Nullable VirtualFile file);
}
