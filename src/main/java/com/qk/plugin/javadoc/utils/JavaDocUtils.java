package com.qk.plugin.javadoc.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.qk.plugin.javadoc.model.JavaDoc;
import com.qk.plugin.javadoc.transformation.JavaDocBuilder;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * JavaDocUtils
 *
 * @Author: Kun Qian
 * @since 2022/6/27 09:34
 */
public class JavaDocUtils {
  /**
   * Convert java doc.
   *
   * @param javadoc the Javadoc
   * @return the String
   */
  @NotNull
  public static String convertJavaDoc(@NotNull JavaDoc javadoc) {
    JavaDocBuilder builder = new JavaDocBuilder();
    builder.createDefaultJavaDoc(javadoc);
    return builder.build();
  }

  /**
   * Merge java docs.
   *
   * @param oldJavaDoc the Old java doc
   * @param newJavaDoc the New java doc
   * @return the Java doc
   */
  @NotNull
  public static JavaDoc mergeJavaDocs(@NotNull JavaDoc oldJavaDoc, @NotNull JavaDoc newJavaDoc) {
    List<String> description = oldJavaDoc.getDescription();
    if (descriptionIsEmpty(description)) {
      description = newJavaDoc.getDescription();
    }
    if (description.size() > 2 && !description.get(description.size() - 2).contains("\n")) {
      description.add("\n");
    }
    Map<String, Collection<String>> newTags = newJavaDoc.getTags();
    Map<String, Collection<String>> tags = new LinkedHashMap<>(oldJavaDoc.getTags());
    tags.remove("author");
    tags.remove("author:");
    tags.remove("author :");
    newTags.forEach((key, value) -> {
      if (!tags.containsKey(key)) {
        tags.put(key, value);
      }
    });
    return new JavaDoc(description, tags);
  }

  /**
   * Creates the java doc.
   *
   * @param docComment the Doc comment
   * @return the Java doc
   */
  @NotNull
  public static JavaDoc createJavaDoc(@NotNull PsiDocComment docComment) {
    return new JavaDoc(
        findDocDescription(docComment),
        findDocTags(docComment));
  }

  /**
   * Find java doc description.
   *
   * @param docComment the Doc comment
   * @return the description
   */
  @NotNull
  public static List<String> findDocDescription(@NotNull PsiDocComment docComment) {
    List<String> descriptions = new LinkedList<>();
    PsiElement[] descriptionElements = docComment.getDescriptionElements();
    for (PsiElement descriptionElement : descriptionElements) {
      descriptions.add(descriptionElement.getText());
    }
    return descriptions;
  }

  /**
   * Find doc tags.
   *
   * @param docComment the Doc comment
   * @return the javadoc tags
   */
  @NotNull
  public static Map<String, Collection<String>> findDocTags(@NotNull PsiDocComment docComment) {
    Map<String, Collection<String>> tags = new LinkedHashMap<>();
    PsiDocTag[] docTags = docComment.getTags();
    for (PsiDocTag docTag : docTags) {
      String tag = docTag.getText().split("\n")[0].replace("@" + docTag.getName(), "").trim();
      tags.computeIfAbsent(docTag.getName(), k -> new HashSet<>()).add(tag);
    }
    return tags;
  }

  /**
   * Converts string to java doc.
   *
   * @param javaDocText       the Java doc text
   * @param psiElementFactory the Psi element factory
   * @return the Java doc
   */
  @NotNull
  public static JavaDoc toJavaDoc(@NotNull String javaDocText, @NotNull PsiElementFactory psiElementFactory) {
    PsiDocComment javaDocComment = psiElementFactory.createDocCommentFromText(javaDocText);
    return createJavaDoc(javaDocComment);
  }

  private static boolean descriptionIsEmpty(List<String> description) {
    boolean result = true;
    if (!CollectionUtils.isEmpty(description)) {
      for (String item : description) {
        result = result && StringUtils.isBlank(item);
      }
    }
    return result;
  }
}
