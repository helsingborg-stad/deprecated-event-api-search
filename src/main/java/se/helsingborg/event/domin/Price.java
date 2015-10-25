package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:36
 */
public abstract class Price {

  private String currency;

  public abstract <R> R accept(PriceVisitor<R> visitor);


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
