package se.helsingborg.event.search.servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import se.helsingborg.event.search.IndexRequest;
import se.helsingborg.event.search.IndexResult;
import se.helsingborg.event.search.IndexResults;
import se.helsingborg.event.search.Service;
import se.helsingborg.event.search.query.JSONQuerySerialization;
import se.helsingborg.event.util.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author kalle
 * @since 2015-10-25 14:19
 */
public class SearchServlet extends JSONPostServlet {

  @Override
  protected void doProcess(JSONObject json, HttpServletRequest request, HttpServletResponse response) throws Exception {

    IndexRequest indexRequest = new IndexRequest();
    indexRequest.setJsonOutput(JSONUtil.optBoolean(json, "jsonOutput", false));
    indexRequest.setScoring(JSONUtil.optBoolean(json, "scoring", true));
    indexRequest.setReference(JSONUtil.optString(json, "reference"));
    indexRequest.setStartIndex(JSONUtil.optInteger(json, "startIndex", 0));
    indexRequest.setLimit(JSONUtil.optInteger(json, "limit", 100));
    indexRequest.setQuery(new JSONQuerySerialization().parse(json.getJSONObject("query")));

    IndexResults indexResults = Service.getInstance().getIndexManager().search(indexRequest);

    JSONObject jsonResults = new JSONObject(new LinkedHashMap());
    jsonResults.put("reference", indexRequest.getReference());
    jsonResults.put("totalNumberOfSearchResults", indexResults.getTotalNumberOfSearchResults());
    jsonResults.put("startIndex", indexRequest.getStartIndex());
    if (indexResults.getIndexResults() != null && !indexResults.getIndexResults().isEmpty()) {
      JSONArray jsonSearchResults = new JSONArray(new ArrayList(indexResults.getIndexResults().size()));
      for (IndexResult indexResult : indexResults.getIndexResults()) {
        if (indexRequest.isJsonOutput()) {
          jsonSearchResults.put(new JSONObject(new JSONTokener(indexResult.getJson())));
        } else {
          jsonSearchResults.put(indexResult.getEventId());
        }
      }
      jsonResults.put("searchResults", jsonSearchResults);
    }
    jsonResults.write(response.getWriter());


  }

}
