package se.helsingborg.event.domin;

/**
 * @author kalle
 * @since 2015-10-25 11:34
 */
public class Offer {

  private Organization seller;
  private Price price;

  public Organization getSeller() {
    return seller;
  }

  public void setSeller(Organization seller) {
    this.seller = seller;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }
}
