package io.plantuml.server;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;

@Log
@Controller
public class CustomizeErrorController implements ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex) {
        log.log(Level.SEVERE,ex,()->"Request: " + req.getRequestURL() + " raised " + ex);
        return "error";
    }
}
