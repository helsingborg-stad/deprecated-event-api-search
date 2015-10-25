package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:38
 */
public class AlternatingPrice extends Price {

  private Float lowPrice;
  private Float highPrice;

  @Override
  public <R> R accept(PriceVisitor<R> visitor) {
    return visitor.visit(this);
  }

  public Float getLowPrice() {
    return lowPrice;
  }

  public void setLowPrice(Float lowPrice) {
    this.lowPrice = lowPrice;
  }

  public Float getHighPrice() {
    return highPrice;
  }

  public void setHighPrice(Float highPrice) {
    this.highPrice = highPrice;
  }
}
