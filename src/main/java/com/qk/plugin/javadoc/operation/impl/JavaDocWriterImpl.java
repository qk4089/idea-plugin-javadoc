package com.qk.plugin.javadoc.operation.impl;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.ThrowableRunnable;
import com.qk.plugin.javadoc.operation.JavaDocWriter;
import org.jetbrains.annotations.NotNull;

/**
 * JavaDocWriterImpl
 *
 * @Author: Kun Qian
 * @since 2022/6/25 12:14
 */
public class JavaDocWriterImpl implements JavaDocWriter {
  @Override
  public void write(PsiDocComment javaDoc, @NotNull PsiElement beforeElement) {
    Project project = beforeElement.getProject();
    PsiFile file = beforeElement.getContainingFile();
    WriteCommandAction.Builder builder = WriteCommandAction.writeCommandAction(project, file);
    builder.run(new WriteJavaDocActionImpl(javaDoc, beforeElement));
  }

  private static class WriteJavaDocActionImpl implements ThrowableRunnable<RuntimeException> {
    private final PsiDocComment javaDoc;
    private final PsiElement element;

    /**
     * Instantiates a new Write java doc action impl.
     *
     * @param javaDoc the java doc
     * @param element the element
     */
    protected WriteJavaDocActionImpl(@NotNull PsiDocComment javaDoc, @NotNull PsiElement element) {
      this.javaDoc = javaDoc;
      this.element = element;
    }

    @Override
    public void run() throws RuntimeException {
      if (element.getFirstChild() instanceof PsiDocComment) {
        replaceJavaDoc(element, javaDoc);
      } else {
        addJavaDoc(element, javaDoc);
      }
      ensureWhitespaceAfterJavaDoc(element);
    }

    private static void replaceJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
      deleteJavaDoc(theElement);
      addJavaDoc(theElement, theJavaDoc);
    }

    private static void addJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
      pushPostponedChanges(theElement);
      theElement.getNode().addChild(theJavaDoc.getNode(), theElement.getFirstChild().getNode());
    }

    private static void deleteJavaDoc(PsiElement theElement) {
      pushPostponedChanges(theElement);
      theElement.getFirstChild().delete();
    }

    private static void pushPostponedChanges(PsiElement element) {
      Editor editor = PsiUtilBase.findEditor(element.getContainingFile());
      if (editor != null) {
        PsiDocumentManager.getInstance(element.getProject())
            .doPostponedOperationsAndUnblockDocument(editor.getDocument());
      }
    }

    private void ensureWhitespaceAfterJavaDoc(PsiElement element) {
      // this method is required to create well formatted javadocs in enums
      PsiElement firstChild = element.getFirstChild();
      if (!PsiDocComment.class.isAssignableFrom(firstChild.getClass())) {
        return;
      }
      PsiElement nextElement = firstChild.getNextSibling();
      if (PsiWhiteSpace.class.isAssignableFrom(nextElement.getClass())) {
        return;
      }
      pushPostponedChanges(element);
      element.getNode().addChild(new PsiWhiteSpaceImpl("\n"), nextElement.getNode());
    }
  }
}
