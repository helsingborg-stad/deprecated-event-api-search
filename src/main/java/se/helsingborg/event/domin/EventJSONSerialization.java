package se.helsingborg.event.domin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.helsingborg.event.util.JSONUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kalle
 * @since 2015-10-25 15:24
 */
public class EventJSONSerialization {

  DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

  public String marshalDateTime(Long timestamp) {
    if (timestamp == null) {
      return null;
    }
    return utcFormat.format(new Date(timestamp));
  }

  public JSONObject marshalEvent(Event event) throws JSONException {
    JSONObject json = new JSONObject(new LinkedHashMap(11));

    json.put("eventId", event.getEventId());
    json.put("url", event.getUrl());
    json.put("name", event.getName());
    json.put("description", event.getDescription());
    json.put("created", marshalDateTime(event.getCreatedEpochMilliseconds()));
    json.put("modified", marshalDateTime(event.getModifiedEpochMilliseconds()));
    json.put("image", event.getImageURL());

    if (event.getLocation() != null) {
      json.put("location", marshalLocation(event.getLocation()));
    }
    if (event.getOffers() != null && !event.getOffers().isEmpty()) {
      JSONArray jsonOffers = new JSONArray(new ArrayList(event.getOffers().size()));
      for (Offer offer : event.getOffers()) {
        jsonOffers.put(marshalOffer(offer));
      }
      json.put("offers", jsonOffers);
    }

    if (event.getShows() != null && !event.getShows().isEmpty()) {
      JSONArray jsonShows = new JSONArray(new ArrayList(event.getShows().size()));
      for (Show show : event.getShows()) {
        jsonShows.put(marshalShow(show));
      }
      json.put("shows", jsonShows);
    }

    if (event.getTags() != null && !event.getTags().isEmpty()) {
      JSONArray jsonTags = new JSONArray(new ArrayList(event.getTags().size()));
      for (String tag : event.getTags()) {
        jsonTags.put(tag);
      }
      json.put("tags", jsonTags);
    }

    return json;
  }

  public Event unmarshalEvent(JSONObject json) throws JSONException, ParseException {

    if (json == null) {
      return null;
    }

    Event event = new Event();

    event.setEventId(JSONUtil.optLong(json, "eventId"));


    event.setUrl(JSONUtil.optString(json, "url"));

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
      event.setTags(new LinkedHashSet<String>(jsonTags.length()));
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

  public JSONObject marshalLocation(Location location) throws JSONException {

    if (location == null) {
      return null;
    }

    JSONObject json = new JSONObject(new LinkedHashMap(6));

    json.put("email", location.getEmail());
    if (location.getGeo() != null) {
      json.put("geo", marshalGeo(location.getGeo()));
    }

    json.put("name", location.getName());
    if (location.getPostalAddress() != null) {
      json.put("address", marshalPostalAddress(location.getPostalAddress()));
    }

    json.put("telephone", location.getTelephone());
    json.put("url", location.getUrl());

    return json;

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
    location.setUrl(JSONUtil.optString(json, "url"));

    return location;

  }

  public JSONObject marshalGeo(Geo geo) throws JSONException {
    if (geo == null) {
      return null;
    }

    return geo.accept(new GeoVisitor<JSONObject>() {
      @Override
      public JSONObject visit(GeoCoordinates geoCoordinates) {
        try {
          return marshalGeoCoordinates(geoCoordinates);
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
      }
    });
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

  public JSONObject marshalGeoCoordinates(GeoCoordinates geoCoordinates) throws JSONException {
    if (geoCoordinates == null) {
      return null;
    }

    JSONObject json = new JSONObject(new LinkedHashMap(3));

    json.put("latitude", geoCoordinates.getLatitude());
    json.put("longitude", geoCoordinates.getLongitude());
    json.put("projection", geoCoordinates.getProjection());

    return json;
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

  public JSONObject marshalPostalAddress(PostalAddress postalAddress) throws JSONException {
    if (postalAddress == null) {
      return null;
    }

    JSONObject json = new JSONObject(new LinkedHashMap(5));
    json.put("name", postalAddress.getName());
    json.put("streetAddress", postalAddress.getStreetAddress());
    json.put("postalCode", postalAddress.getPostalCode());
    json.put("addressLocality", postalAddress.getAddressLocality());
    json.put("addressCountry", postalAddress.getAddressCountry());

    return json;

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

  public JSONObject marshalOffer(Offer offer) throws JSONException {
    if (offer == null) {
      return null;
    }

    final JSONObject json = new JSONObject(new LinkedHashMap(4));

    if (offer.getPrice() != null) {
      json.put("price", offer.getPrice().accept(new PriceVisitor<Void>() {
        @Override
        public Void visit(SinglePrice price) {
          try {
            json.put("priceCurrency", price.getCurrency());
            json.put("price", price.getPrice());
          } catch (JSONException je) {
            throw new RuntimeException(je);
          }
          return null;
        }

        @Override
        public Void visit(AlternatingPrice price) {
          try {
            json.put("priceCurrency", price.getCurrency());
            json.put("lowPrice", price.getLowPrice());
            json.put("highPrice", price.getHighPrice());
          } catch (JSONException je) {
            throw new RuntimeException(je);
          }
          return null;
        }
      }));
    }

    if (offer.getSeller() != null) {
      json.put("seller", marshalOrganization(offer.getSeller()));
    }

    return json;

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

  public JSONObject marshalOrganization(Organization organization) throws JSONException {
    if (organization == null) {
      return null;
    }

    JSONObject json = new JSONObject(new LinkedHashMap(6));
    json.put("url", organization.getUrl());
    json.put("name", organization.getName());
    json.put("description", organization.getDescription());
    json.put("email", organization.getEmail());
    json.put("telephone", organization.getTelephone());

    if (organization.getPostalAddress() != null) {
      json.put("address", marshalPostalAddress(organization.getPostalAddress()));
    }

    return json;
  }

  public Organization unmarshalOrganization(JSONObject json) throws JSONException {

    if (json == null) {
      return null;
    }

    Organization organization = new Organization();

    organization.setUrl(JSONUtil.optString(json, "url"));
    organization.setName(JSONUtil.optString(json, "name"));
    organization.setDescription(JSONUtil.optString(json, "description"));
    organization.setEmail(JSONUtil.optString(json, "email"));
    organization.setTelephone(JSONUtil.optString(json, "telephone"));
    organization.setPostalAddress(unmarshalPostalAddress(JSONUtil.optJSONObject(json, "address")));

    return organization;

  }

  public JSONObject marshalShow(Show show) throws JSONException {
    if (show == null) {
      return null;
    }

    JSONObject json = new JSONObject(new LinkedHashMap(4));
    if (show.getStatus() != null) {
      json.put("status", show.getStatus().name());
    }
    json.put("note", show.getNote());
    json.put("startDate", marshalDateTime(show.getStartTimeEpochMilliseconds()));
    if (show.getEndTimeEpochMilliseconds() != null) {
      json.put("endDate", marshalDateTime(show.getEndTimeEpochMilliseconds()));
    }

    return json;
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
