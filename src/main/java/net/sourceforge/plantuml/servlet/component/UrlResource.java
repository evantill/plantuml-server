package net.sourceforge.plantuml.servlet.component;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Data
@RequiredArgsConstructor
public final class UrlResource {
    @NonNull
    private final URL url;
    private final int readTimeout;

    @FunctionalInterface
    interface Reader<T> {
        T read(InputStream is) throws IOException;
    }

    public <T> T readContent(Reader<T> reader) throws IOException {
        HttpURLConnection con = getConnection(url);
        try (InputStream img = con.getInputStream()) {
            return reader.read(img);
        } finally {
            con.disconnect();
        }
    }

    /**
     * Get open http connection from URL.
     *
     * @param url URL to open connection
     * @return open http connection
     * @throws IOException if an input or output exception occurred
     */
    private HttpURLConnection getConnection(URL url) throws IOException {
        if (url.getProtocol().startsWith("https")) {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(readTimeout);
            // printHttpsCert(con);
            con.connect();
            return con;
        } else {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(readTimeout);
            con.connect();
            return con;
        }
    }
}
