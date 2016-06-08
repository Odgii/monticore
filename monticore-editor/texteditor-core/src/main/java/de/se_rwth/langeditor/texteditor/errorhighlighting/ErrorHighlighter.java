package de.se_rwth.langeditor.texteditor.errorhighlighting;

import java.util.Set;

import javax.annotation.Nullable;

import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import de.se_rwth.commons.SourcePosition;
import de.se_rwth.langeditor.injection.TextEditorScoped;
import de.se_rwth.langeditor.modelstates.ModelState;
import de.se_rwth.langeditor.modelstates.ObservableModelStates;
import de.se_rwth.langeditor.util.Misc;

@TextEditorScoped
public class ErrorHighlighter {
  
  private final IAnnotationModel annotationModel;
  
  private final Set<Annotation> annotations = Sets.newConcurrentHashSet();
  
  @Inject
  public ErrorHighlighter(@Nullable IAnnotationModel annotationModel, IStorage storage,
      ObservableModelStates observableModelStates) {
    this.annotationModel = annotationModel;
    if (annotationModel != null) {
      observableModelStates.getModelStates().stream()
          .filter(modelState -> modelState.getStorage().equals(storage))
          .forEach(this::acceptModelState);
      observableModelStates.addStorageObserver(storage, this::acceptModelState);
    }
  }
  
  public void acceptModelState(ModelState modelState) {
    for (Annotation annotation : annotations) {
      annotationModel.removeAnnotation(annotation);
      annotations.remove(annotation);
    }
    displaySyntaxErrors(modelState);
    displayAdditionalErrors(modelState);
  }
  
  private void displaySyntaxErrors(ModelState modelState) {
    ImmutableMultimap<Interval, String> syntaxErrors = modelState.getSyntaxErrors();
    for (Interval interval: syntaxErrors.keys()) {
      for (String message : syntaxErrors.get(interval)) {
        Display.getDefault().asyncExec(() -> displayError(interval, message));
      }
    }
  }
  
  private void displayAdditionalErrors(ModelState modelState) {
    Multimap<SourcePosition, String> additionalErrors = modelState.getAdditionalErrors();
    for (SourcePosition position: additionalErrors.keys()) {
      for (String message : additionalErrors.get(position)) {
        int index = Misc.convertLineAndColumnToLinearIndex(
            modelState.getContent(), position.getLine(), position.getColumn());
        Interval interval = new Interval(index, index);
        Display.getDefault().asyncExec(() -> displayError(interval, message));
      }
    }
  }
  
  private void displayError(Interval interval, String message) {
    int startIndex = interval.a;
    int stopIndex = interval.b + 1;
    Annotation annotation =
        new Annotation("org.eclipse.ui.workbench.texteditor.error", false, message);
    annotations.add(annotation);
    annotationModel.addAnnotation(annotation, new Position(startIndex, stopIndex - startIndex));
  }
}
