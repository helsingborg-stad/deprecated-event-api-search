package se.helsingborg.event.search;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

/**
 * @author kalle
 * @since 2015-10-25 20:22
 */
public class LocalPersistence {

  private File file;
  private JSONObject jsonObject;

  public JSONObject getJSONObject() {
    return jsonObject;
  }

  public void open() throws Exception {
    if (!file.exists()) {
      jsonObject = new JSONObject();
      jsonObject.put("created", System.currentTimeMillis());
      commit();
    } else {
      jsonObject = new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(file), "UTF8")));
    }
  }

  public void commit() throws Exception {
    Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF8");
    jsonObject.write(out);
    out.close();
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
