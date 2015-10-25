package se.helsingborg.event.domin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.helsingborg.event.util.JSONUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author kalle
 * @since 2015-10-25 15:24
 */
public class EventJSONSerialization {

  public Event unmarshalEvent(JSONObject json) throws JSONException, ParseException {

    if (json == null) {
      return null;
    }

    Event event = new Event();

    event.setEventId(JSONUtil.optLong(json, "eventId"));


    event.setName(JSONUtil.optString(json, "name"));
    event.setDescription(JSONUtil.optString(json, "description"));

    event.setCreatedEpochMilliseconds(JSONUtil.optUTC(json, "created"));
    event.setModifiedEpochMilliseconds(JSONUtil.optUTC(json, "modified"));

    event.setImageURL(JSONUtil.optString(json, "image"));
    event.setLocation(unmarshalLocation(JSONUtil.optJSONObject(json, "location")));

    JSONArray jsonOffers = JSONUtil.optJSONArray(json, "offers");
    if (jsonOffers != null) {
      event.setOffers(new ArrayList<Offer>(jsonOffers.length()));
      for (int i = 0; i < jsonOffers.length(); i++) {
        event.getOffers().add(unmarshalOffer(jsonOffers.getJSONObject(i)));
      }
    }

    JSONArray jsonShows = JSONUtil.optJSONArray(json, "shows");
    if (jsonShows != null) {
      event.setShows(new ArrayList<Show>(jsonShows.length()));
      for (int i = 0; i < jsonShows.length(); i++) {
        event.getShows().add(unmarshalShow(jsonShows.getJSONObject(i)));
      }
    }

    JSONArray jsonTags = JSONUtil.optJSONArray(json, "tags");
    if (jsonTags != null) {
      event.setTags(new HashSet<String>(jsonTags.length()));
      for (int i = 0; i < jsonTags.length(); i++) {
        String tag = jsonTags.getString(i);
        if (tag != null) {
          tag = tag.trim();
          event.getTags().add(tag);
        }
      }
    }

    return event;

  }

  public Location unmarshalLocation(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    Location location = new Location();
    location.setEmailAddress(JSONUtil.optString(json, "emailAddress"));
    location.setGeo(unmarshalGeo(JSONUtil.optJSONObject(json, "geo")));
    location.setName(JSONUtil.optString(json, "name"));
    location.setPostalAddress(unmarshalPostalAddress(JSONUtil.optJSONObject(json, "address")));
    location.setTelephone(JSONUtil.optString(json, "telephone"));
    location.setURL(JSONUtil.optString(json, "url"));

    return location;

  }

  public Geo unmarshalGeo(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    if (json.has("latitude")) {
      return unmarshalGeoCoordinates(json);
    } else {
      throw new RuntimeException();
    }

  }

  public GeoCoordinates unmarshalGeoCoordinates(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    GeoCoordinates geoCoordinates = new GeoCoordinates();

    geoCoordinates.setLatitude(JSONUtil.optDouble(json, "latitude"));
    geoCoordinates.setLongitude(JSONUtil.optDouble(json, "longitude"));
    geoCoordinates.setProjection(JSONUtil.optString(json, "projection", "WGS84"));

    return geoCoordinates;

  }

  public PostalAddress unmarshalPostalAddress(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    PostalAddress postalAddress = new PostalAddress();
    postalAddress.setName(JSONUtil.optString(json, "name"));
    postalAddress.setStreetAddress(JSONUtil.optString(json, "streetAddress"));
    postalAddress.setPostalCode(JSONUtil.optString(json, "postalCode"));
    postalAddress.setPostalTown(JSONUtil.optString(json, "postalTown"));
    postalAddress.setCountry(JSONUtil.optString(json, "country"));
    return postalAddress;

  }


  public Offer unmarshalOffer(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    Offer offer = new Offer();

    offer.setURL(JSONUtil.optString(json, "URL"));
    offer.setName(JSONUtil.optString(json, "name"));
    offer.setDescription(JSONUtil.optString(json, "description"));
    offer.setEmail(JSONUtil.optString(json, "email"));
    offer.setTelephone(JSONUtil.optString(json, "telephone"));
    offer.setPostalAddress(unmarshalPostalAddress(JSONUtil.optJSONObject(json, "postalAddress")));

    return offer;

  }

  public Show unmarshalShow(JSONObject json) throws JSONException, ParseException {

    if (json == null) {
      return null;
    }

    Show show = new Show();

    show.setStatus(ShowStatus.valueOf(JSONUtil.optString(json, "status", ShowStatus.scheduled.name())));
    show.setNote(JSONUtil.optString(json, "note"));
    show.setStartTimeEpochMilliseconds(JSONUtil.optUTC(json, "startTime"));
    show.setEndTimeEpochMilliseconds(JSONUtil.optUTC(json, "endTime"));

    return show;

  }


}
