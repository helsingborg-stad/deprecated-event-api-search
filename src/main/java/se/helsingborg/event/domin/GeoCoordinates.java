package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:33
 */
public class GeoCoordinates extends Geo {

  private double latitude;
  private double longitude;

  public GeoCoordinates() {
  }

  public GeoCoordinates(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public <R> R accept(GeoVisitor<R> visitor) {
    return visitor.visit(this);
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
