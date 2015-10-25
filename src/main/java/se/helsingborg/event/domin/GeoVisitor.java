package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:40
 */
public interface GeoVisitor<R> {

  public abstract R visit(GeoCoordinates geoCoordinates);

}
