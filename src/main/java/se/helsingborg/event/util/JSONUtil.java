package se.helsingborg.event.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author kalle
 * @since 18/10/15 09:11
 */
public class JSONUtil {

  public static Long optUTC(JSONObject json, String attribute) throws JSONException, ParseException {

    if (!json.has(attribute)) {
      return null;
    }

    String string = optString(json, attribute);
    if (string == null) {
      return null;
    } else {

      final DateTimeFormatter formatter2 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
      return formatter2.parseMillis(string);
    }

  }

  public static JSONObject optJSONObject(JSONObject json, String attribute) throws JSONException {
    if (!json.has(attribute)) {
      return null;
    } else {
      return json.getJSONObject(attribute);
    }
  }

  public static JSONArray optJSONArray(JSONObject json, String attribute) throws JSONException {
    if (!json.has(attribute)) {
      return null;
    } else {
      return json.getJSONArray(attribute);
    }
  }

  public static String optString(JSONObject json, String attribute) throws JSONException {
    return optString(json, attribute, null);
  }
  public static String optString(JSONObject json, String attribute, String fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return json.getString(attribute);
    }
  }

  public static Boolean optBoolean(JSONObject json, String attribute) throws JSONException {
    return optBoolean(json, attribute, null);
  }
  public static Boolean optBoolean(JSONObject json, String attribute, Boolean fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return json.getBoolean(attribute);
    }
  }

  public static Integer optInteger(JSONObject json, String attribute) throws JSONException {
    return optInteger(json, attribute, null);
  }
  public static Integer optInteger(JSONObject json, String attribute, Integer fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return json.getInt(attribute);
    }
  }

  public static Long optLong(JSONObject json, String attribute) throws JSONException {
    return optLong(json, attribute, null);

  }
  public static Long optLong(JSONObject json, String attribute, Long fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return json.getLong(attribute);
    }
  }


  public static Float getFloat(JSONObject json, String attribute) throws JSONException {
    if (!json.has(attribute)) {
      throw new JSONException("Missing attribute " + attribute);
    } else {
      return (float) json.getDouble(attribute);
    }
  }

  public static Float optFloat(JSONObject json, String attribute) throws JSONException {
    return optFloat(json, attribute, null);
  }
  public static Float optFloat(JSONObject json, String attribute, Float fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return (float) json.getDouble(attribute);
    }
  }

  public static Double optDouble(JSONObject json, String attribute) throws JSONException {
    return optDouble(json, attribute, null);
  }
  public static Double optDouble(JSONObject json, String attribute, Double fallback) throws JSONException {
    if (!json.has(attribute)) {
      return fallback;
    } else {
      return json.getDouble(attribute);
    }
  }
}