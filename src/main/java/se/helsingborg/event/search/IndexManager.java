package se.helsingborg.event.search;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.helsingborg.event.domin.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kalle
 * @since 2015-10-24 11:44
 */
public class IndexManager {

  private static final Logger log = LoggerFactory.getLogger(IndexManager.class);


  public static final String FIELD_EVENT_IDENTITY_INDEXED = "Event#identity";
  public static final String FIELD_EVENT_IDENTITY_VALUE = "Event#identity[value]";

  public static final String FIELD_EVENT_CREATED = "Event#created";
  public static final String FIELD_EVENT_MODIFIED = "Event#modified";


  // todo: future, index bboxes rather than coordinates in order to query for polygons.
//  public static final String FIELD_EVENT_LOCATION_GEO_SOUTH_LATITUDE = "Event.location.geo#south latitude";
//  public static final String FIELD_EVENT_LOCATION_GEO_WEST_LONGITUDE = "Event.location.geo#west longitude";
//  public static final String FIELD_EVENT_LOCATION_GEO_NORTH_LATITUDE = "Event.location.geo#north latitude";
//  public static final String FIELD_EVENT_LOCATION_GEO_EAST_LONGITUDE = "Event.location.geo#east longitude";

  public static final String FIELD_EVENT_LOCATION_GEO_LATITUDE = "Event.location.geo#latitude";
  public static final String FIELD_EVENT_LOCATION_GEO_LONGITUDE = "Event.location.geo#longitude";


  public static final String FIELD_EVENT_TAG = "Event#tag";
  public static final String FIELD_EVENT_NAME = "Event#name";
  public static final String FIELD_EVENT_DESCRIPTION = "Event#description";

  public static final String FIELD_EVENT_LOCATION_NAME = "Event.location#name";

  public static final String FIELD_EVENT_LOCATION_POSTAL_ADDRESS_NAME = "Event.location.postalAddress#name";
  public static final String FIELD_EVENT_LOCATION_POSTAL_ADDRESS_STREET_ADDRESS = "Event.location.postalAddress#street address";
  public static final String FIELD_EVENT_LOCATION_POSTAL_ADDRESS_POSTAL_CODE = "Event.location.postalAddress#postal code";
  public static final String FIELD_EVENT_LOCATION_POSTAL_ADDRESS_POSTAL_TOWN = "Event.location.postalAddress#postal town";
  public static final String FIELD_EVENT_LOCATION_POSTAL_ADDRESS_COUNTRY = "Event.location.postalAddress#postal country";

  public static final String FIELD_EVENT_SHOW_STATUS = "Event.show#status";

  public static final String FIELD_EVENT_SHOW_START_DATE_TIME = "Event.show#start date time";
  public static final String FIELD_EVENT_SHOW__END_DATE_TIME = "Event.show#end date time";

  private File dataPath;

  private Directory directory;
  private IndexWriter indexWriter;
  private SearcherManager searcherManager;

  public void open() throws Exception {

    log.info("Starting up...");

    if (dataPath == null) {
      dataPath = new File(Service.getInstance().getDataPath(), "lucene");
      log.warn("Event index data path not set, defaulting to " + dataPath.getAbsolutePath());
    }

    if (!dataPath.exists() && !dataPath.mkdirs()) {
      log.error("Could not mkdirs " + dataPath.getAbsolutePath());
    }

    directory = FSDirectory.open(dataPath.toPath());

    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(EventIndexAnalyzerFactory.factory());
    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

    indexWriter = new IndexWriter(directory, indexWriterConfig);
    searcherManager = new SearcherManager(indexWriter, true, new SearcherFactory());

    // todo: if index is empty then reconstruct

    log.info("Started.");
  }

  public void close() throws Exception {
    log.info("Closing...");

    searcherManager.close();
    indexWriter.close();
    directory.close();

    log.info("Closed.");
  }


  public void updateIndex(Event event) throws Exception {

    List<Document> documents = new ArrayList<>();

    String eventIdString = String.valueOf(event.getEventId());

    if (event.getShows() != null && !event.getShows().isEmpty()) {
      for (Show show : event.getShows()) {
        documents.add(documentFactory(event, show));
      }
    } else {
      documents.add(documentFactory(event));
    }

    Term identityTerm = new Term(FIELD_EVENT_IDENTITY_INDEXED, eventIdString);
    indexWriter.updateDocuments(identityTerm, documents);


  }


  private Document documentFactory(Event event) throws Exception {

    String eventIdString = String.valueOf(event.getEventId());

    final Document document = new Document();

    document.add(new NumericDocValuesField(FIELD_EVENT_IDENTITY_VALUE, event.getEventId()));
    document.add(new StringField(FIELD_EVENT_IDENTITY_INDEXED, eventIdString, Field.Store.NO));

    document.add(new LongField(FIELD_EVENT_CREATED, event.getCreatedEpochMilliseconds(), StoredField.Store.NO));
    document.add(new LongField(FIELD_EVENT_MODIFIED, event.getModifiedEpochMilliseconds(), StoredField.Store.NO));



    if (event.getName() != null) {
      document.add(new TextField(FIELD_EVENT_NAME, event.getName(), Field.Store.NO));
    }
    if (event.getDescription() != null) {
      document.add(new TextField(FIELD_EVENT_DESCRIPTION, event.getDescription(), Field.Store.NO));
    }

    if (event.getTags() != null && !event.getTags().isEmpty()) {
      for (String tag : event.getTags()) {
        document.add(new StringField(FIELD_EVENT_TAG, tag, Field.Store.NO));
      }


    }

    if (event.getLocation() != null) {

      if (event.getLocation().getName() != null) {
        document.add(new TextField(FIELD_EVENT_LOCATION_NAME, event.getLocation().getName(), Field.Store.NO));
      }

      if (event.getLocation().getPostalAddress() != null) {

        if (event.getLocation().getPostalAddress().getName() != null) {
          document.add(new TextField(FIELD_EVENT_LOCATION_POSTAL_ADDRESS_NAME, event.getLocation().getPostalAddress().getName(), Field.Store.NO));
        }
        if (event.getLocation().getPostalAddress().getStreetAddress() != null) {
          document.add(new TextField(FIELD_EVENT_LOCATION_POSTAL_ADDRESS_STREET_ADDRESS, event.getLocation().getPostalAddress().getStreetAddress(), Field.Store.NO));
        }
        if (event.getLocation().getPostalAddress().getPostalCode() != null) {
          document.add(new TextField(FIELD_EVENT_LOCATION_POSTAL_ADDRESS_POSTAL_CODE, event.getLocation().getPostalAddress().getPostalCode(), Field.Store.NO));
        }
        if (event.getLocation().getPostalAddress().getAddressLocality() != null) {
          document.add(new TextField(FIELD_EVENT_LOCATION_POSTAL_ADDRESS_POSTAL_TOWN, event.getLocation().getPostalAddress().getAddressLocality(), Field.Store.NO));
        }
        if (event.getLocation().getPostalAddress().getAddressCountry() != null) {
          document.add(new TextField(FIELD_EVENT_LOCATION_POSTAL_ADDRESS_COUNTRY, event.getLocation().getPostalAddress().getAddressCountry(), Field.Store.NO));
        }

      }

      if (event.getLocation().getGeo() != null) {
        event.getLocation().getGeo().accept(new GeoVisitor<Void>() {
          @Override
          public Void visit(GeoCoordinates geoCoordinates) {

            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_LATITUDE, geoCoordinates.getLatitude(), StoredField.Store.NO));
            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_LONGITUDE, geoCoordinates.getLongitude(), StoredField.Store.NO));

            // todo: future, index bboxes rather than coordinates in order to query for polygons.
//            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_SOUTH_LATITUDE, geoCoordinates.getLatitude(), StoredField.Store.NO));
//            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_WEST_LONGITUDE, geoCoordinates.getLongitude(), StoredField.Store.NO));
//            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_NORTH_LATITUDE, geoCoordinates.getLatitude(), StoredField.Store.NO));
//            document.add(new DoubleField(FIELD_EVENT_LOCATION_GEO_EAST_LONGITUDE, geoCoordinates.getLongitude(), StoredField.Store.NO));

            return null;
          }
        });
      }

    }


    return document;

  }

  private Document documentFactory(Event event, Show show) throws Exception {

    Document document = documentFactory(event);

    document.add(new StringField(FIELD_EVENT_SHOW_STATUS, show.getStatus() != null ? show.getStatus().name() : ShowStatus.scheduled.name(), StoredField.Store.NO));

    document.add(new LongField(FIELD_EVENT_SHOW_START_DATE_TIME, show.getStartTimeEpochMilliseconds(), StoredField.Store.NO));
    if (show.getEndTimeEpochMilliseconds() != null) {
      document.add(new LongField(FIELD_EVENT_SHOW__END_DATE_TIME, show.getEndTimeEpochMilliseconds(), StoredField.Store.NO));
    } else {
      // todo: what sort of queries is required?
    }



    return document;

  }


  public IndexResults search(IndexRequest indexRequest) throws Exception {

    final AtomicInteger totalNumberOfSearchResults = new AtomicInteger();

    /** Multiple index points for the same event, i.e. shows on multiple dates. */
    final Map<Long, Float> eventIdsAndScore = new HashMap<>();

    IndexSearcher indexSearcher = searcherManager.acquire();
    try {


      indexSearcher.search(indexRequest.getQuery(), new Collector() {
        @Override
        public LeafCollector getLeafCollector(final LeafReaderContext leafReaderContext) throws IOException {
          return new LeafCollector() {
            Scorer scorer;
            NumericDocValues identityValues;

            @Override
            public void setScorer(Scorer scorer) throws IOException {
              this.scorer = scorer;
            }

            @Override
            public void collect(int doc) throws IOException {
              totalNumberOfSearchResults.incrementAndGet();
              if (identityValues == null) {
                identityValues = leafReaderContext.reader().getNumericDocValues(FIELD_EVENT_IDENTITY_VALUE);
              }
              float score = scorer.score();
              long eventId = identityValues.get(doc);
              Float previousScore = eventIdsAndScore.get(eventId);
              if (previousScore == null || score > previousScore) {
                eventIdsAndScore.put(eventId, score);
              }
            }
          };
        }

        @Override
        public boolean needsScores() {
          return true;
        }

      });


    } finally {
      searcherManager.release(indexSearcher);
    }


    List<Map.Entry<Long, Float>> eventIdAndScoresOrdered = new ArrayList<>(eventIdsAndScore.entrySet());
    Collections.sort(eventIdAndScoresOrdered, new Comparator<Map.Entry<Long, Float>>() {
      @Override
      public int compare(Map.Entry<Long, Float> o1, Map.Entry<Long, Float> o2) {
        return o1.getValue().compareTo(o2.getValue());
      }
    });

    IndexResults indexResults = new IndexResults();
    indexResults.setTotalNumberOfSearchResults(totalNumberOfSearchResults.get());
    indexResults.setIndexResults(new ArrayList<IndexResult>(indexRequest.getLimit()));
    for (int i = indexRequest.getStartIndex(); i < indexRequest.getLimit() + indexRequest.getStartIndex() && i < eventIdsAndScore.size(); i++) {
      Map.Entry<Long, Float> eventIdAndScore = eventIdAndScoresOrdered.get(i);
      IndexResult indexResult = new IndexResult();
      indexResult.setScore(eventIdAndScore.getValue());
      indexResult.setEventId(eventIdAndScore.getKey());
      indexResults.getIndexResults().add(indexResult);
    }

    return indexResults;

  }


  public File getDataPath() {
    return dataPath;
  }

  public void setDataPath(File dataPath) {
    this.dataPath = dataPath;
  }


}
