package net.sourceforge.plantuml.servlet.component;

import lombok.NonNull;
import net.sourceforge.plantuml.code.Transcoder;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Textual diagram source from "url" parameter
 */
public class UrlParamExtractor extends EncodedDiagramExtractor {

    @NonNull
    private final PlantUmlRequest request;

    public UrlParamExtractor(@NonNull Transcoder transcoder, @NonNull PlantUmlRequest request) {
        super(transcoder);
        this.request = request;
    }

    @Override
    protected Optional<String> extractEncoded() throws ExtractionException {
        return request.getCleanedUrlParameter().flatMap(this::extractLastPart);
    }

    /**
     * Regex pattern to fetch last part of the URL.
     */
    private static final Pattern URL_PATTERN = Pattern.compile("^.*[^a-zA-Z0-9\\-\\_]([a-zA-Z0-9\\-\\_]+)");

    private Optional<String> extractLastPart(String url) {
        // Catch the last part of the URL if necessary
        final Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }

}
