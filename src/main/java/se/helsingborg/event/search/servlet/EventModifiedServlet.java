package se.helsingborg.event.search.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.helsingborg.event.SystemErrorManager;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.search.PrimaryPersistence;
import se.helsingborg.event.search.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kalle
 * @since 2015-10-24 14:39
 */
public class EventModifiedServlet extends HttpServlet {

  private static final Logger log = LoggerFactory.getLogger(EventModifiedServlet.class);

  private static Pattern identityPattern = Pattern.compile(".+/([0-9]+)$");

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Matcher matcher = identityPattern.matcher(request.getRequestURI());
    if (!matcher.matches()) {
      throw new RuntimeException("Request URI does not match " + identityPattern.pattern());
    }
    long eventId = Long.valueOf(matcher.group(1));

    Event event;
    PrimaryPersistence wordpress = new PrimaryPersistence();
    try {
      wordpress.connect();
      try {
        event = wordpress.getEvent(eventId);
      } finally {
        wordpress.disconnect();
      }

      Service.getInstance().getIndexManager().updateIndex(event);

    } catch (Exception e) {
      log.error("Exception caught when WP repported updated Event.", e);
      SystemErrorManager.getInstance().log("Exception caught when WP repported updated Event.", e);
    }

  }
}
