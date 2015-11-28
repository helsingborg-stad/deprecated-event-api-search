package se.helsingborg.event.search.servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import se.helsingborg.event.search.SearchRequest;
import se.helsingborg.event.search.SearchResult;
import se.helsingborg.event.search.SearchResults;
import se.helsingborg.event.search.Service;
import se.helsingborg.event.search.query.JSONQuerySerialization;
import se.helsingborg.event.util.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author kalle
 * @since 2015-10-25 14:19
 */
public class SearchServlet extends JSONPostServlet {

  @Override
  protected void doProcess(JSONObject json, HttpServletRequest request, HttpServletResponse response) throws Exception {

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setIdentityOutput(JSONUtil.optBoolean(json, "identityOutput", true));
    searchRequest.setEventJsonOutput(JSONUtil.optBoolean(json, "eventJsonOutput", false));
    searchRequest.setScoring(JSONUtil.optBoolean(json, "scoring", true));
    searchRequest.setReference(JSONUtil.optString(json, "reference"));
    searchRequest.setStartIndex(JSONUtil.optInteger(json, "startIndex", 0));
    searchRequest.setLimit(JSONUtil.optInteger(json, "limit", 100));
    searchRequest.setQuery(new JSONQuerySerialization().parse(json.getJSONObject("query")));

    SearchResults searchResults = Service.getInstance().getIndexManager().search(searchRequest);

    PrintWriter out = response.getWriter();

    out.write("{\n");

    if (searchRequest.getReference() != null) {
      out.append("\"reference\": ").append(JSONObject.quote(searchRequest.getReference())).append(",\n");
    }
    out.append("\"totalNumberOfSearchResults\": ").append(String.valueOf(searchResults.getTotalNumberOfSearchResults())).append(",\n");
    out.append("\"startIndex\": ").append(String.valueOf(searchRequest.getStartIndex()));

    if (searchResults.getSearchResults() == null || searchResults.getSearchResults().isEmpty()) {
      out.append("\n");
    } else {
      out.append(",\n");
      out.write("\"searchResults\": [\n");
      for (Iterator<SearchResult> iterator = searchResults.getSearchResults().iterator(); iterator.hasNext(); ) {
        SearchResult searchResult = iterator.next();
        out.write("{");
        boolean needsComma = false;
        if (searchRequest.isScoring()) {
          out.append("\"score\": ").append(String.valueOf(searchResult.getScore()));
          needsComma = true;
        }
        if (searchRequest.isIdentityOutput()) {
          if (needsComma) {
            out.append(", ");
          }
          out.append("\"eventId\": ").append(String.valueOf(searchResult.getEventId()));
          needsComma = true;
        }
        if (searchRequest.isEventJsonOutput()) {
          if (needsComma) {
            out.append(", ");
          }
          out.append("\"event\": ").append(searchResult.getJson());
          needsComma = true;
        }
        out.write("}");
        if (iterator.hasNext()) {
          out.write(",");
        }
        out.write("\n");
      }
      out.write("]\n");
    }

    out.write("}");

  }

}
