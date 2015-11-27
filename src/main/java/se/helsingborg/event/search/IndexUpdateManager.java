package se.helsingborg.event.search;

import org.json.JSONObject;
import se.helsingborg.event.PrimaryPersistence;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.domin.EventJSONSerialization;
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

  private PrimaryPersistence primaryPersistence;

  public void run() throws Exception {

    primaryPersistence.connect();
    try {

      EventJSONSerialization serialization = new EventJSONSerialization();

      while (true) {
        Thread.sleep(millisecondsDelayBetweenUpdate);
        long started = System.currentTimeMillis();
        long previousTimestampListModifiedEvents = JSONUtil.optLong(Service.getInstance().getLocalPersistence().getJSONObject(), "previousTimestampListModifiedEvents", 0l);

        for (Iterator<JSONObject> jsonEvents = primaryPersistence.listUpdated(previousTimestampListModifiedEvents); jsonEvents.hasNext(); ) {
          JSONObject jsonEvent = jsonEvents.next();

          Event event = serialization.unmarshalEvent(jsonEvent);
          Service.getInstance().getIndexManager().updateIndex(event, jsonEvent);
        }

        Service.getInstance().getLocalPersistence().getJSONObject().put("previousTimestampListModifiedEvents", started);
        Service.getInstance().getLocalPersistence().commit();

      }
    } finally {
      primaryPersistence.disconnect();
    }
  }

  public PrimaryPersistence getPrimaryPersistence() {
    return primaryPersistence;
  }

  public void setPrimaryPersistence(PrimaryPersistence primaryPersistence) {
    this.primaryPersistence = primaryPersistence;
  }

  public long getMillisecondsDelayBetweenUpdate() {
    return millisecondsDelayBetweenUpdate;
  }

  public void setMillisecondsDelayBetweenUpdate(long millisecondsDelayBetweenUpdate) {
    this.millisecondsDelayBetweenUpdate = millisecondsDelayBetweenUpdate;
  }
}
