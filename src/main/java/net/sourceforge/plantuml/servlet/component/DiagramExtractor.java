package net.sourceforge.plantuml.servlet.component;

import java.io.IOException;
import java.util.Optional;

public interface DiagramExtractor {

    record Diagram(String content) {
    }

    class ExtractionException extends IOException {
        public ExtractionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    Optional<Diagram> extract() throws ExtractionException;
}
