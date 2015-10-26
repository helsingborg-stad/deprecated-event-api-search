package se.helsingborg.event.search;

import org.json.JSONObject;
import se.helsingborg.event.MockedPrimaryPersistence;
import se.helsingborg.event.PrimaryPersistence;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.util.JSONUtil;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author kalle
 * @since 2015-10-25 16:03
 */
public class IndexUpdateManager {

  // todo: job that checks for updates since previous request.
  // todo: requires local persistence to keep track of when the previous request was made

  private long millisecondsDelayBetweenUpdate = TimeUnit.MINUTES.toMillis(1);

  public void run() throws Exception {

    PrimaryPersistence primaryPersistence = new MockedPrimaryPersistence();
    primaryPersistence.connect();
    try {
      while (true) {
        Thread.sleep(millisecondsDelayBetweenUpdate);
        long started = System.currentTimeMillis();
        long previousTimestampListModifiedEvents = JSONUtil.optLong(Service.getInstance().getLocalPersistence().getJSONObject(), "previousTimestampListModifiedEvents", 0l);

        for (Iterator<Event> events = primaryPersistence.listUpdated(previousTimestampListModifiedEvents); events.hasNext(); ) {
          Event event = events.next();
          Service.getInstance().getIndexManager().updateIndex(event);
        }

        Service.getInstance().getLocalPersistence().getJSONObject().put("previousTimestampListModifiedEvents", started);
        Service.getInstance().getLocalPersistence().commit();

      }
    } finally {
      primaryPersistence.disconnect();
    }
  }


}
