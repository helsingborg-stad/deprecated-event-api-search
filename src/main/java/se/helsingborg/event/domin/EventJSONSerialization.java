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

    event.setCreatedEpochMilliseconds(JSONUtil.optDateTime(json, "created"));
    event.setModifiedEpochMilliseconds(JSONUtil.optDateTime(json, "modified"));

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
    location.setEmail(JSONUtil.optString(json, "email"));
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
    postalAddress.setAddressLocality(JSONUtil.optString(json, "addressLocality"));
    postalAddress.setAddressCountry(JSONUtil.optString(json, "addressCountry"));
    return postalAddress;

  }


  public Offer unmarshalOffer(JSONObject json) throws JSONException {
    if (json == null) {
      return null;
    }

    Offer offer = new Offer();

    if (json.has("price")) {
      SinglePrice price = new SinglePrice();
      price.setCurrency(JSONUtil.optString(json, "priceCurrency"));
      price.setPrice(JSONUtil.optFloat(json, "price"));
      offer.setPrice(price);

    } else if (json.has("lowPrice")) {
      AlternatingPrice price = new AlternatingPrice();
      price.setCurrency(JSONUtil.optString(json, "priceCurrency"));
      price.setLowPrice(JSONUtil.optFloat(json, "lowPrice"));
      price.setHighPrice(JSONUtil.optFloat(json, "highPrice"));
      offer.setPrice(price);

    }

    offer.setSeller(unmarshalOrganization(JSONUtil.optJSONObject(json, "seller")));

    return offer;

  }

  public Organization unmarshalOrganization(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    Organization organization = new Organization();

    organization.setURL(JSONUtil.optString(json, "url"));
    organization.setName(JSONUtil.optString(json, "name"));
    organization.setDescription(JSONUtil.optString(json, "description"));
    organization.setEmail(JSONUtil.optString(json, "email"));
    organization.setTelephone(JSONUtil.optString(json, "telephone"));
    organization.setPostalAddress(unmarshalPostalAddress(JSONUtil.optJSONObject(json, "address")));

    return organization;

  }

  public Show unmarshalShow(JSONObject json) throws JSONException, ParseException {

    if (json == null) {
      return null;
    }

    Show show = new Show();

    show.setStatus(ShowStatus.valueOf(JSONUtil.optString(json, "status", ShowStatus.scheduled.name())));
    show.setNote(JSONUtil.optString(json, "note"));
    show.setStartTimeEpochMilliseconds(JSONUtil.optDateTime(json, "startDate"));
    show.setEndTimeEpochMilliseconds(JSONUtil.optDateTime(json, "endDate"));

    return show;

  }


}
