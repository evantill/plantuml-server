package net.sourceforge.plantuml.servlet.component;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Extract textual diagram from `text request parameter`
 */
@Data
@RequiredArgsConstructor
public final class TextReqParamExtractor implements DiagramExtractor {
    @NonNull
    private final PlantUmlRequest request;

    @Override
    public Optional<Diagram> extract() {
        return request.getCleanedTextParameter().map(Diagram::new);
    }
}
