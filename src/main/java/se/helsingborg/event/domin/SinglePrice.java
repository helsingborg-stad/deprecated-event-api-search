package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:37
 */
public class SinglePrice extends Price {

  private Float value;


  @Override
  public <R> R accept(PriceVisitor<R> visitor) {
    return visitor.visit(this);
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }
}
