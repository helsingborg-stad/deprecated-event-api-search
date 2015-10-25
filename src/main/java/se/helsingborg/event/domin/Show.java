package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:23
 */
public class Show {

  private long startTimeEpochMilliseconds;
  private Long endTimeEpochMilliseconds;
  private ShowStatus status;
  private String note;

  public long getStartTimeEpochMilliseconds() {
    return startTimeEpochMilliseconds;
  }

  public void setStartTimeEpochMilliseconds(long startTimeEpochMilliseconds) {
    this.startTimeEpochMilliseconds = startTimeEpochMilliseconds;
  }

  public Long getEndTimeEpochMilliseconds() {
    return endTimeEpochMilliseconds;
  }

  public void setEndTimeEpochMilliseconds(Long endTimeEpochMilliseconds) {
    this.endTimeEpochMilliseconds = endTimeEpochMilliseconds;
  }

  public ShowStatus getStatus() {
    return status;
  }

  public void setStatus(ShowStatus status) {
    this.status = status;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
