package se.helsingborg.event;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.json.JSONObject;
import se.helsingborg.event.domin.Event;
import se.helsingborg.event.domin.EventJSONSerialization;
import se.helsingborg.event.search.IndexRequest;
import se.helsingborg.event.search.IndexResults;
import se.helsingborg.event.search.Service;
import se.helsingborg.event.sources.cbis.CBISExportReader;

import java.io.File;
import java.io.InputStreamReader;

/**
 * @author kalle
 * @since 2015-11-27 23:27
 */
public class TestService extends TestCase {

  public void test() throws Exception {

    File dataPath = File.createTempFile("hgb.event.search", "data");
    FileUtils.deleteQuietly(dataPath);
    dataPath.mkdirs();

    Service.getInstance().setDataPath(dataPath);
    Service.getInstance().open();
    try {

      EventJSONSerialization jsonSerialization = new EventJSONSerialization();

      CBISExportReader cbis = new CBISExportReader(new InputStreamReader(getClass().getResourceAsStream("/CBIS-export/evenemang.csv"), "UTF8"));
      long identity = 0;
      Event event;
      while ((event = cbis.readEvent()) != null) {
        event.setEventId(identity++);
        JSONObject jsonEvent = jsonSerialization.marshalEvent(event);
        Service.getInstance().getIndexManager().updateIndex(event, jsonEvent);
      }
      Service.getInstance().getIndexManager().commit();

      IndexRequest indexRequest = new IndexRequest();
      indexRequest.setQuery(new MatchAllDocsQuery());
      indexRequest.setStartIndex(0);
      indexRequest.setLimit(100);
      indexRequest.setScoring(true);
      indexRequest.setJsonOutput(true);

      IndexResults indexResults = Service.getInstance().getIndexManager().search(indexRequest);

      System.currentTimeMillis();

    } finally {
      Service.getInstance().close();
    }

    FileUtils.deleteDirectory(dataPath);

  }

}
