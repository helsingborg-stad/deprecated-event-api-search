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

  private Map<Long, JSONObject> events;

  public void connect() throws Exception {

    events = new HashMap<>();
    for (File file : new File("src/test/resources/events").listFiles()) {
      if (file.getName().endsWith(".json")) {
        System.out.println(file.getAbsoluteFile());
        JSONObject event = new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(file), "UTF8")));
        events.put(event.getLong("eventId"), event);
      }
    }


  }

  public void disconnect() throws Exception {
    events = null;
  }

  public JSONObject getEvent(long eventId) throws Exception {
    return events.get(eventId);
  }

  public Iterator<JSONObject> export() throws Exception {
    return new HashSet<JSONObject>(events.values()).iterator();
  }

  public Iterator<JSONObject> listUpdated(long sinceDateTimeEpochMilliseconds) throws Exception {
    return new HashSet<JSONObject>(events.values()).iterator();
  }

}
