package se.helsingborg.event.sources.cbis;

import org.json.JSONException;
import se.helsingborg.event.domin.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kalle
 * @since 2015-11-05
 */
public class CBISExportReader {

  private static Pattern occasionsPattern = Pattern.compile("Date: (\\d\\d\\d\\d-\\d\\d-\\d\\d) StartTime (\\d\\d:\\d\\d)( EndTime (\\d\\d:\\d\\d))?");

  private BufferedReader input;

  private Map<String, Integer> indexByHeader;
  private Map<Integer, String> headerByIndex;

  public CBISExportReader(Reader input) throws IOException {
    this.input = new BufferedReader(input);

    String[] headers = readColumns();
    indexByHeader = new HashMap<>(headers.length);
    headerByIndex = new HashMap<>(headers.length);
    for (int i = 0; i < headers.length; i++) {
      indexByHeader.put(headers[i], i);
      headerByIndex.put(i, headers[i]);
    }
  }

  private String[] readColumns() throws IOException {
    String line = input.readLine();
    if (line == null) {
      return null;
    }
    String[] columns = line.split("\t");
    for (int i = 0; i < columns.length; i++) {
      columns[i] = columns[i].replaceAll("^\"(.+)\"$", "$1").trim();
      if (columns[i].isEmpty()) {
        columns[i] = null;
      }
    }
    return columns;
  }

  private String getColumnValue(String columnName, String[] columnValues) {
    return columnValues[indexByHeader.get(columnName)];
  }

  private String[] columns;

  public String[] getColumns() {
    return columns;
  }

  public Event readEvent() throws IOException, ParseException {
    columns = readColumns();
    if (columns == null) {
      return null;
    }
    Event event = new Event();

    event.setName(getColumnValue("Name", columns));

    {
      String categories = getColumnValue("Categories", columns);
      if (categories != null) {
        Set<String> tags = new HashSet<>();
        for (String category : categories.split(",")) {
          category = category.trim();
          if (!category.isEmpty()) {
            tags.add(category);
          }
        }
        if (!tags.isEmpty()) {
          event.setTags(tags);
        }
      }
    }

    event.setDescription(getColumnValue("Description", columns));
    if (event.getDescription() == null) {
      event.setDescription(getColumnValue("Introduction", columns));
    }

    event.setUrl(getColumnValue("EventUrl", columns));

    {
      String image = getColumnValue("Image", columns);
      if (image != null) {
        image = image.replaceFirst("^Url: (.+) ProducedBy.+$", "$1").trim();
        if (!image.isEmpty()) {
          event.setImageURL(image);
        }
      }
    }

    {
      String latitude = getColumnValue("Latitude", columns);
      if (latitude != null) {
        String longitude = getColumnValue("Longitude", columns);
        if (longitude != null) {
          if (!"0".equals(latitude) && !"0".equals(longitude)) {
            if (event.getLocation() == null) {
              event.setLocation(new Location());
            }
            latitude = latitude.replaceFirst(",", ".");
            longitude = longitude.replaceFirst(",", ".");
            event.getLocation().setGeo(new GeoCoordinates(Double.valueOf(latitude), Double.valueOf(longitude)));
          } else {
            // todo geocode if address available.
          }
        } else {
          // todo warn
        }
      }
    }

    {
      String occasions = getColumnValue("Occasions", columns);
      if (occasions != null) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        event.setShows(new ArrayList<Show>());
        for (String occasion : occasions.split("\\|")) {
          occasion = occasion.trim();
          if (!occasion.isEmpty()) {
            Matcher matcher = occasionsPattern.matcher(occasion);
            if (!matcher.find()) {
              throw new RuntimeException();
            }
            Show show = new Show();
            show.setStatus(ShowStatus.scheduled);
            show.setStartTimeEpochMilliseconds(sdf.parse(matcher.group(1) + " " + matcher.group(2)).getTime());
            if (matcher.group(4) != null) {
              show.setEndTimeEpochMilliseconds(sdf.parse(matcher.group(1) + " " + matcher.group(4)).getTime());
            }
            event.getShows().add(show);
          }
        }
      }
    }

    {
      String phoneNumber = getColumnValue("PhoneNumber", columns);
      if (phoneNumber != null) {
        if (event.getLocation() == null) {
          event.setLocation(new Location());
        }
        // todo phone number normalizer?
        event.getLocation().setTelephone(phoneNumber);
      }
    }

    {
      String postalCode = getColumnValue("PostalCode", columns);
      if (postalCode != null) {
        if (event.getLocation() == null) {
          event.setLocation(new Location());
        }
        if (event.getLocation().getPostalAddress() == null) {
          event.getLocation().setPostalAddress(new PostalAddress());
        }
        event.getLocation().getPostalAddress().setPostalCode(postalCode);
      }
    }

    {
      String streetAddress1 = getColumnValue("StreetAddress1", columns);
      if (streetAddress1 != null) {
        if (event.getLocation() == null) {
          event.setLocation(new Location());
        }
        if (event.getLocation().getPostalAddress() == null) {
          event.getLocation().setPostalAddress(new PostalAddress());
        }
        event.getLocation().getPostalAddress().setStreetAddress(streetAddress1);

      }
    }

    {
      String cityAddress = getColumnValue("CityAddress", columns);
      if (cityAddress != null) {
        if (event.getLocation() == null) {
          event.setLocation(new Location());
        }
        if (event.getLocation().getPostalAddress() == null) {
          event.getLocation().setPostalAddress(new PostalAddress());
        }
        event.getLocation().getPostalAddress().setAddressLocality(cityAddress);

      }
    }

    {
      String website = getColumnValue("Website", columns);
      if (website != null) {
        if (event.getLocation() == null) {
          event.setLocation(new Location());
        }
        event.getLocation().setUrl(website);
      }
    }

    {
      String price = getColumnValue("Price", columns);
      if (price != null) {
        System.currentTimeMillis();
        System.out.println(price);
      }
    }

    return event;
  }


  public void close() throws IOException {
    input.close();
  }


}
