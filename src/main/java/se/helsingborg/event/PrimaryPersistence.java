package se.helsingborg.event;

import org.json.JSONObject;
import se.helsingborg.event.domin.Event;

import java.util.Iterator;

/**
 * @author kalle
 * @since 2015-10-24 14:52
 */
public class PrimaryPersistence {

  public void connect() throws Exception {

  }

  public void disconnect() throws Exception {

  }

  public Event getEvent(long eventId) throws Exception {
    throw new UnsupportedOperationException("Not implmented");
  }

  public Iterator<Event> export() throws Exception {
    throw new UnsupportedOperationException("Not implmented");
  }

  public Iterator<Event> listUpdated(long sinceDateTimeEpochMilliseconds) throws Exception {
    throw new UnsupportedOperationException("Not implmented");
  }

}
