package net.sourceforge.plantuml.servlet.component;

import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class FaultBackDiagramExtractor implements DiagramExtractor{

    @NonNull
    private final List<DiagramExtractor> extractors;

    public FaultBackDiagramExtractor(@NonNull List<DiagramExtractor> extractors) {
        this.extractors = extractors;
    }

    public FaultBackDiagramExtractor(DiagramExtractor... extractors) {
        this(List.of(extractors));
        for (DiagramExtractor extractor : extractors) {
            Objects.requireNonNull(extractor);
        }
    }

    @Override
    public Optional<Diagram> extract() throws ExtractionException {
        for (DiagramExtractor extractor : extractors) {
            Optional<Diagram> extracted = extractor.extract();
            if(extracted.isPresent()){
                return extracted;
            }
        }
        return Optional.empty();
    }
}
