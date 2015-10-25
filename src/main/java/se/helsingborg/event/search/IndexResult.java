package se.helsingborg.event.search;

/**
 * @author kalle
 * @since 2015-10-25 13:33
 */
public class IndexResult {

  private float score;
  private long eventId;


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
