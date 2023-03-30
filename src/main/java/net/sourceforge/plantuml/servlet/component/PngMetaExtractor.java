package net.sourceforge.plantuml.servlet.component;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.png.MetadataTag;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

/**
 * Extract textual diagram from png image metadata.
 */
public final class PngMetaExtractor extends EncodedDiagramExtractor {
    private static final String METADATA_TAG = "plantuml";
    private static final int DEFAULT_READ_TIMEOUT=10000;//10 seconds
    @NonNull
    private final PlantUmlRequest request;

    public PngMetaExtractor(@NonNull Transcoder transcoder, @NonNull PlantUmlRequest request) {
        super(transcoder);
        this.request = request;
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected Optional<String> extractEncoded() throws ExtractionException {
        return request.getMetadataUrl().map(this::toUrlResource).map(this::sneakyReadResource);
    }

    private UrlResource toUrlResource(URL url) {
        return new UrlResource(url, DEFAULT_READ_TIMEOUT);
    }

    @SneakyThrows(ExtractionException.class)
    private String sneakyReadResource(UrlResource resource) {
        return readResource(resource);
    }

    private String readResource(UrlResource resource) throws ExtractionException {
        try {
            return resource.readContent(this::extractDiagramFromPngMetadata);
        } catch (IOException e) {
            throw new ExtractionException("error extracting diagram from resource " + resource, e);
        }
    }

    private String extractDiagramFromPngMetadata(InputStream pngImage) throws IOException {
        MetadataTag metadataTag = new MetadataTag(pngImage, METADATA_TAG);
        return metadataTag.getData();
    }

}
