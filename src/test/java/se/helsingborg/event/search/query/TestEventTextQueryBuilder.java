package se.helsingborg.event.search.query;

import junit.framework.TestCase;
import org.apache.lucene.search.Query;

/**
 * @author kalle
 * @since 2015-11-06 13:43
 */
public class TestEventTextQueryBuilder extends TestCase {

  public void test() throws Exception {

    Query query = new EventTextQueryBuilder().setText("dunkler kulturhus helsingborg").build();

    System.currentTimeMillis();

  }

}
