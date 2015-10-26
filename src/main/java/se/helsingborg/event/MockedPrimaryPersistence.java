package se.helsingborg.event;

import org.json.JSONObject;
import org.json.JSONTokener;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.domin.EventJSONSerialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kalle
 * @since 2015-10-24 14:52
 */
public class MockedPrimaryPersistence extends PrimaryPersistence {

  public static void main(String[] args) throws Exception {
    MockedPrimaryPersistence persistence = new MockedPrimaryPersistence();
    persistence.connect();

    persistence.disconnect();
  }

  private Map<Long, Event> events;

  public void connect() throws Exception {

    EventJSONSerialization eventJSONSerialization = new EventJSONSerialization();

    events = new HashMap<>();
    for (File file : new File("src/test/resources/events").listFiles()) {
      if (file.getName().endsWith(".json")) {
        System.out.println(file.getAbsoluteFile());
        Event event = eventJSONSerialization.unmarshalEvent(new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(file), "UTF8"))));
        events.put(event.getEventId(), event);
      }
    }


  }

  public void disconnect() throws Exception {
    events = null;
  }

  public Event getEvent(long eventId) throws Exception {
    return events.get(eventId);
  }

  public Iterator<Event> export() throws Exception {
    return new HashSet<Event>(events.values()).iterator();
  }

  public Iterator<Event> listUpdated(long sinceDateTimeEpochMilliseconds) throws Exception {
    return new HashSet<Event>(events.values()).iterator();
  }

}
