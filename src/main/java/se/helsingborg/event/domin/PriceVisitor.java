package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:42
 */
public interface PriceVisitor<R> {

  public abstract R visit(SinglePrice price);
  public abstract R visit(AlternatingPrice price);

}
