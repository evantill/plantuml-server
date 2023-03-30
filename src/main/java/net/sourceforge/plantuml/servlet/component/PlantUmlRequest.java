package net.sourceforge.plantuml.servlet.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Typesafe accessors to request parameters.
 */
@Data
@RequiredArgsConstructor
public final class PlantUmlRequest {
    private static final String TEXT = "text";
    public static final String URL = "url";
    public static final String METADATA = "metadata";

    @NonNull
    private final HttpServletRequest request;

    /**
     * @deprecated Use {@link #optionalParameter(String)} instead.
     */
    @Deprecated
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    private Optional<String> optionalParameter(String name) {
        return Optional.ofNullable(request.getParameter(name));
    }

    private Optional<String> optionalNonEmptyParameter(String name) {
        return optionalParameter(name).map(String::trim).filter(v -> !v.isEmpty());
    }

    /**
     * @deprecated use {@link #getMetadataUrl()} instead.
     */
    @Deprecated
    public Optional<String> getMetadata() {
        return optionalParameter(METADATA);
    }

    public Optional<URL> getMetadataUrl() {
        var meta=optionalParameter(METADATA);
        if(meta.isPresent()){
            try{
                return Optional.of(new URL(meta.get()));
            } catch (MalformedURLException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }


    /**
     * @deprecated use {@link #getCleanedTextParameter()} instead;
     */
    @Deprecated
    public Optional<String> getText() {
        return optionalParameter(TEXT);
    }

    public Optional<String> getCleanedTextParameter() {
        return optionalNonEmptyParameter(TEXT);
    }


    /**
     * @deprecated use {@link #getCleanedUrlParameter()} instead;
     */
    @Deprecated
    public Optional<String> getUrl() {
        return optionalParameter(URL);
    }

    public Optional<String> getCleanedUrlParameter() {
        return optionalNonEmptyParameter(URL);
    }

    public Optional<String> getRequestURI() {
        return Optional.of(request.getRequestURI());
    }

    /**
     * Use (or add) methods on this class to hide the request.
     * This method will be removed after refactoring.
     */
    @Deprecated
    public HttpServletRequest getRequest() {
        return request;
    }

    public boolean isUml() {
        String requestUri = request.getRequestURI();
        return requestUri.startsWith("/uml/") && !requestUri.endsWith("/uml/");
    }
}
