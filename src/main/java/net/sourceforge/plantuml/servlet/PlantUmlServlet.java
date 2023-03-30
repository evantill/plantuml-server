/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */
package net.sourceforge.plantuml.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.api.PlantumlUtils;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.servlet.component.DiagramExtractor.ExtractionException;
import net.sourceforge.plantuml.servlet.component.FaultBackDiagramExtractor;
import net.sourceforge.plantuml.servlet.component.PlantUmlRequest;
import net.sourceforge.plantuml.servlet.component.PngMetaExtractor;
import net.sourceforge.plantuml.servlet.component.TextReqParamExtractor;
import net.sourceforge.plantuml.servlet.component.UmlReqPathExtractor;
import net.sourceforge.plantuml.servlet.component.UrlParamExtractor;
import net.sourceforge.plantuml.servlet.utility.Configuration;
import net.sourceforge.plantuml.servlet.utility.UmlExtractor;
import net.sourceforge.plantuml.servlet.utility.UrlDataExtractor;

import java.io.IOException;
import java.util.Optional;

import static net.sourceforge.plantuml.servlet.component.DiagramExtractor.Diagram;

/**
 * Original idea from Achim Abeling for Confluence macro.
 *
 * This class is the old all-in-one historic implementation of the PlantUml server.
 * See package.html for the new design. It's a work in progress.
 *
 * Modified by Arnaud Roques
 * Modified by Pablo Lalloni
 * Modified by Maxime Sinclair
 */
@WebServlet(
        name = "plantumlservlet",

        urlPatterns = {
                "/welcome",
                "/uml/*",
                "/form",
                "/start/*"
        }
)
@SuppressWarnings("SERIAL")
public class PlantUmlServlet extends HttpServlet {

    /**
     * Default encoded uml text.
     * Bob -> Alice : hello
     */
    private static final String DEFAULT_ENCODED_TEXT = "SyfFKj2rKt3CoKnELR1Io4ZDoSa70000";

    public static final String INDEX_PAGE = "/";

    static {
        OptionFlags.ALLOW_INCLUDE = false;
        if ("true".equalsIgnoreCase(System.getenv("ALLOW_PLANTUML_INCLUDE"))) {
            OptionFlags.ALLOW_INCLUDE = true;
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        PlantUmlRequest plantUmlRequest = new PlantUmlRequest(request);
        Optional<UmlReqPathExtractor.Diagram> diagramFromRequest = extractDiagramFrom(plantUmlRequest);

        // no Text form has been submitted
        if (diagramFromRequest.isEmpty()) {
            redirectNow(request, response, DEFAULT_ENCODED_TEXT);
        } else {
            // diagram index to render
            final int idx = UrlDataExtractor.getIndex(plantUmlRequest.getRequest().getRequestURI());
            // forward to index page
            prepareRequestForDispatch(request, diagramFromRequest.get().content(), idx);
            final RequestDispatcher dispatcher = request.getRequestDispatcher(INDEX_PAGE);
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        PlantUmlRequest adapter = new PlantUmlRequest(request);
        // diagram index to render
        final int idx = UrlDataExtractor.getIndex(request.getRequestURI());

        // encoded diagram source
        String encoded = DEFAULT_ENCODED_TEXT;
        //TODO reveal intention: do not hide the error.
        try {
            Optional<Diagram> diagram = extractDiagramFrom(adapter);
            if (diagram.isPresent()) {
                encoded = getTranscoder().encode(diagram.get().content());
            }
        } catch (Exception e) {
            encoded = DEFAULT_ENCODED_TEXT;
            e.printStackTrace();
        }
        redirectNow(request, response, encoded, idx);
    }

    /**
     * Get textual diagram.
     * Search for textual diagram in following order:
     * 1. Encoded in the url following the `/uml/${encoded}` pattern
     * 2. request parameter "url"
     * 3. PNG image metadata
     * 4. request parameter "text"
     *
     * @param request http plantuml request
     *
     * @return if successful textual diagram source; otherwise empty
     *
     * @throws ExtractionException if an extraction exception occurred
     */
    private Optional<UmlReqPathExtractor.Diagram> extractDiagramFrom(PlantUmlRequest request) throws ExtractionException {
        Transcoder transcoder = getTranscoder();
        UmlReqPathExtractor umlReqPathExtractor =new UmlReqPathExtractor(transcoder,request);
        UrlParamExtractor urlParamExtractor =new UrlParamExtractor(transcoder,request);
        PngMetaExtractor pngMetaExtractor = new PngMetaExtractor(transcoder,request);
        TextReqParamExtractor textParamExtractor = new TextReqParamExtractor(request);
        FaultBackDiagramExtractor extractors = new FaultBackDiagramExtractor(umlReqPathExtractor,urlParamExtractor,pngMetaExtractor,textParamExtractor);
        return extractors.extract();
    }

    /**
     * Prepare request for dispatch and get request dispatcher.
     *
     * @param request http request which will be further prepared for dispatch
     * @param text textual diagram source
     *
     * @throws IOException if an input or output exception occurred
     */
    private void prepareRequestForDispatch(HttpServletRequest request, String text, int idx) throws IOException {
        final String encoded = getTranscoder().encode(text);
        final String index = (idx < 0) ? "" : idx + "/";
        // diagram sources
        request.setAttribute("decoded", text);
        request.setAttribute("index", idx);
        // properties
        request.setAttribute("showSocialButtons", Configuration.get("SHOW_SOCIAL_BUTTONS"));
        request.setAttribute("showGithubRibbon", Configuration.get("SHOW_GITHUB_RIBBON"));
        // image URLs
        final boolean hasImg = !text.isEmpty();
        request.setAttribute("hasImg", hasImg);
        request.setAttribute("imgurl", "png/" + index + encoded);
        request.setAttribute("svgurl", "svg/" + index + encoded);
        request.setAttribute("pdfurl", "pdf/" + index + encoded);
        request.setAttribute("txturl", "txt/" + index + encoded);
        request.setAttribute("mapurl", "map/" + index + encoded);
        // map for diagram source if necessary
        final boolean hasMap = PlantumlUtils.hasCMapData(text);
        request.setAttribute("hasMap", hasMap);
        String map = "";
        if (hasMap) {
            try {
                map = UmlExtractor.extractMap(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("map", map);
    }

    /**
     * Send redirect response to encoded uml text.
     *
     * @param request http request
     * @param response http response
     * @param encoded encoded uml text
     *
     * @throws IOException if an input or output exception occurred
     */
    private void redirectNow(
        HttpServletRequest request,
        HttpServletResponse response,
        String encoded
    ) throws IOException {
        redirectNow(request, response, encoded, null);
    }

    /**
     * Send redirect response to encoded uml text.
     *
     * @param request http request
     * @param response http response
     * @param encoded encoded uml text
     * @param index diagram index
     *
     * @throws IOException if an input or output exception occurred
     */
    private void redirectNow(
        HttpServletRequest request,
        HttpServletResponse response,
        String encoded,
        Integer index
    ) throws IOException {
        final String path;
        if (index == null || index < 0) {
            path = request.getContextPath() + "/uml/" + encoded;
        } else {
            path = request.getContextPath() + "/uml/" + index + "/" + encoded;
        }
        response.sendRedirect(path);
    }

    /**
     * Get PlantUML transcoder.
     *
     * @return transcoder instance
     */
    private Transcoder getTranscoder() {
        return TranscoderUtil.getDefaultTranscoder();
    }


}
