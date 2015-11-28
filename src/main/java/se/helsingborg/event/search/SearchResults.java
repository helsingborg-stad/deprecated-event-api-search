package se.helsingborg.event.search;

import java.util.List;

/**
 * @author kalle
 * @since 2015-10-25 13:27
 */
public class SearchResults {

  private int totalNumberOfSearchResults;
  private int startIndex;
  private List<SearchResult> searchResults;

  public int getTotalNumberOfSearchResults() {
    return totalNumberOfSearchResults;
  }

  public void setTotalNumberOfSearchResults(int totalNumberOfSearchResults) {
    this.totalNumberOfSearchResults = totalNumberOfSearchResults;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public List<SearchResult> getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(List<SearchResult> searchResults) {
    this.searchResults = searchResults;
  }
}
