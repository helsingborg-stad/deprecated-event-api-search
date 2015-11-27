package se.helsingborg.event.sources.cbis;

import junit.framework.TestCase;
import org.json.JSONObject;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.domin.EventJSONSerialization;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kalle
 * @since 2015-11-05 05:01
 */
public class TestCBISExportReader extends TestCase {

  public void test() throws Exception {

    EventJSONSerialization serialization = new EventJSONSerialization();
    CBISExportReader reader = new CBISExportReader(new InputStreamReader(getClass().getResourceAsStream("/CBIS-export/evenemang.csv"), "UTF8"));
    try {

      List<Event> events = new ArrayList<>();
      Event event;

      while ((event = reader.readEvent()) != null) {

        // assert equality after serialization
        JSONObject jsonEvent = serialization.marshalEvent(event);
        Event event2 = serialization.unmarshalEvent(jsonEvent);
        JSONObject jsonEvent2 = serialization.marshalEvent(event2);
        assertEquals(jsonEvent.toString(), jsonEvent2.toString());

        events.add(event);
      }

      System.currentTimeMillis();

    } finally {
      reader.close();
    }

  }

}
