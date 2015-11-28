package se.helsingborg.event.search;

/**
 * @author kalle
 * @since 2015-10-25 13:33
 */
public class SearchResult {

  private float score;
  private long eventId;
  private String json;


  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public float getScore() {
    return score;
  }

  public void setScore(float score) {
    this.score = score;
  }

  public long getEventId() {
    return eventId;
  }

  public void setEventId(long eventId) {
    this.eventId = eventId;
  }
}
