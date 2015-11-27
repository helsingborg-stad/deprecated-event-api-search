package se.helsingborg.event.search;

import org.apache.lucene.search.Query;

/**
 * @author kalle
 * @since 2015-10-25 13:27
 */
public class IndexRequest {

  private boolean scoring;
  private boolean jsonOutput;
  private String reference;
  private int startIndex;
  private int limit;

  private Query query;

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }

  public boolean isJsonOutput() {
    return jsonOutput;
  }

  public void setJsonOutput(boolean jsonOutput) {
    this.jsonOutput = jsonOutput;
  }

  public boolean isScoring() {
    return scoring;
  }

  public void setScoring(boolean scoring) {
    this.scoring = scoring;
  }
}
