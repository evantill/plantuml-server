package net.sourceforge.plantuml.servlet.component;

import lombok.NonNull;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.servlet.utility.UrlDataExtractor;

import java.util.Optional;

/**
 * Extract textual diagram from encoded content of `/uml/` url path.
 */
public final class UmlReqPathExtractor extends EncodedDiagramExtractor {

    @NonNull
    private final PlantUmlRequest request;

    public UmlReqPathExtractor(@NonNull Transcoder transcoder, @NonNull PlantUmlRequest request) {
        super(transcoder);
        this.request = request;
    }

    @Override
    protected Optional<String> extractEncoded() {
        if(request.isUml()){
            return request.getRequestURI().flatMap(this::extractEncodedFromUrl);
        }else{
            return Optional.empty();
        }
    }

    private Optional<String> extractEncodedFromUrl(String url) {
        Optional<String> encoded = Optional.ofNullable(UrlDataExtractor.getEncodedDiagram(url, null));
        return encoded.filter(v -> !v.isBlank()).map(String::trim);
    }

}
