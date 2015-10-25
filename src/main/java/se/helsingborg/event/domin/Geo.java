package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:33
 */
public abstract class Geo {

  private String projection = "WGS84";

  public abstract <R> R accept(GeoVisitor<R> visitor);

  public String getProjection() {
    return projection;
  }

  public void setProjection(String projection) {
    this.projection = projection;
  }
}
