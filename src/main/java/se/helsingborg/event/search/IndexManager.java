package se.helsingborg.event.search;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author kalle
 * @since 2015-10-24 11:44
 */
public class IndexManager {

  private static final Logger log = LoggerFactory.getLogger(IndexManager.class);

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

    directory = FSDirectory.open(dataPath);

    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_45, EventIndexAnalyzerFactory.factory());
    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

    indexWriter = new IndexWriter(directory, indexWriterConfig);
    searcherManager = new SearcherManager(indexWriter, true, new SearcherFactory());

    log.info("Started.");
  }

  public void close() throws Exception {
    log.info("Closing...");

    searcherManager.close();
    indexWriter.close();
    directory.close();

    log.info("Closed.");
  }

  public File getDataPath() {
    return dataPath;
  }

  public void setDataPath(File dataPath) {
    this.dataPath = dataPath;
  }

  

}
