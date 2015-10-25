package se.helsingborg.event;

/**
 * @author kalle
 * @since 2015-10-25 14:09
 */
public class SystemErrorManager {

  private static SystemErrorManager instance = new SystemErrorManager();

  public static SystemErrorManager getInstance() {
    return instance;
  }

  private SystemErrorManager(){

  }

  public void log(Exception e) {
    // todo report
  }

  public void log(String message) {
    // todo: report
  }

  public void log(String message, Exception e) {
    // todo: report
  }

}
