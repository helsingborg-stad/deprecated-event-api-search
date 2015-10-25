package se.helsingborg.event.search.servlet;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.helsingborg.event.SystemErrorManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;

/**
 * @author kalle
 * @since 15/10/15 06:13
 */
public abstract class JSONPostServlet extends HttpServlet {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    if (!"application/json".equalsIgnoreCase(request.getHeader("Content-Type"))) {
      throw new RuntimeException("Request header 'Content-Type' must be 'application/json'");
    }

    response.setHeader("Access-Control-Allow-Origin", "*");

    response.setContentType("application/json");
    response.setCharacterEncoding("utf8");

    String jsonRequestString = IOUtils.toString(request.getInputStream(), "UTF8");

    if (log.isDebugEnabled()) {
      log.debug("Incoming request: " + jsonRequestString);
    }


    JSONObject jsonRequestObject;
    try {
      jsonRequestObject = new JSONObject(new JSONTokener(jsonRequestString));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }

    try {
      doProcess(jsonRequestObject, request, response);
    } catch (Exception e) {
      SystemErrorManager.getInstance().log(e);
      log.error("Caught exception", e);

      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      pw.close();

      response.setStatus(500);
      response.setContentType("text/plain");
      response.getWriter().write(sw.toString());

    }


  }

  protected abstract void doProcess(JSONObject json, HttpServletRequest request, HttpServletResponse response) throws Exception;


}
