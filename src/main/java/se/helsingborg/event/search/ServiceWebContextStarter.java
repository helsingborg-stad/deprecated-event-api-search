package se.helsingborg.event.search;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author kalle
 * @since 2015-10-24 11:30
 */
public class ServiceWebContextStarter implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      Service.getInstance().open();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    try {
      Service.getInstance().close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
