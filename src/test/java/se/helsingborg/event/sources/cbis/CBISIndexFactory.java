package se.helsingborg.event.sources.cbis;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.domin.EventJSONSerialization;
import se.helsingborg.event.search.Service;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Removes any previous index and fills it with CBIS export data
 *
 * @author kalle
 * @since 2015-11-28 00:09
 */
public class CBISIndexFactory {

  public static void main(String[] args) throws Exception {

    Service.getInstance().setDataPath(new File("data"));
    FileUtils.deleteQuietly(Service.getInstance().getDataPath());

    Service.getInstance().open();
    try {

      EventJSONSerialization jsonSerialization = new EventJSONSerialization();

      CBISExportReader cbis = new CBISExportReader(new InputStreamReader(CBISIndexFactory.class.getResourceAsStream("/CBIS-export/evenemang.csv"), "UTF8"));
      long identity = 0;
      Event event;
      while ((event = cbis.readEvent()) != null) {
        event.setEventId(identity++);
        JSONObject jsonEvent = jsonSerialization.marshalEvent(event);
        Service.getInstance().getIndexManager().updateIndex(event, jsonEvent);
      }
      Service.getInstance().getIndexManager().commit();

    } finally {
      Service.getInstance().close();
    }


  }

}

