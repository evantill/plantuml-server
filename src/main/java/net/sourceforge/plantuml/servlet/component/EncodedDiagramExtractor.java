package net.sourceforge.plantuml.servlet.component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;

import java.util.Optional;

@RequiredArgsConstructor
abstract class EncodedDiagramExtractor implements DiagramExtractor {

    @NonNull
    private final Transcoder transcoder;

    @SuppressWarnings("RedundantThrows")
    @Override
    public Optional<Diagram> extract() throws ExtractionException {
        return extractEncoded().map(this::sneakyDecode).map(Diagram::new);
    }

    protected abstract Optional<String> extractEncoded() throws ExtractionException;

    @SneakyThrows(ExtractionException.class)
    private String sneakyDecode(String encoded) {
        return decode(encoded);
    }

    private String decode(String encoded) throws ExtractionException {
        try {
            return transcoder.decode(encoded);
        } catch (NoPlantumlCompressionException e) {
            throw new ExtractionException("decoding " + encoded + " error:" + e.getMessage(), e);
        }
    }
}
