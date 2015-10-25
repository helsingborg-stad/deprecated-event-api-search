package se.helsingborg.event.domin;

import java.util.List;
import java.util.Set;

/**
 * @author kalle
 * @since 2015-10-25 11:21
 */
public class Event {

  private Long eventId;
  private String name;
  private String description;
  private String imageURL;
  private List<Show> shows;
  private Location location;
  private List<Offer> offers;

  private Set<String> tags;


  private long createdEpochMilliseconds;
  private long modifiedEpochMilliseconds;


  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public List<Show> getShows() {
    return shows;
  }

  public void setShows(List<Show> shows) {
    this.shows = shows;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public List<Offer> getOffers() {
    return offers;
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  public long getCreatedEpochMilliseconds() {
    return createdEpochMilliseconds;
  }

  public void setCreatedEpochMilliseconds(long createdEpochMilliseconds) {
    this.createdEpochMilliseconds = createdEpochMilliseconds;
  }

  public long getModifiedEpochMilliseconds() {
    return modifiedEpochMilliseconds;
  }

  public void setModifiedEpochMilliseconds(long modifiedEpochMilliseconds) {
    this.modifiedEpochMilliseconds = modifiedEpochMilliseconds;
  }
}
