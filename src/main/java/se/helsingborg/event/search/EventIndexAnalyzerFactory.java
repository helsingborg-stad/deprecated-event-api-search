package se.helsingborg.event.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2015-10-24 11:56
 */
public class EventIndexAnalyzerFactory  {

  public static Analyzer factory() {
    Map<String, Analyzer> fieldAnalyzers = new HashMap<>();
    return new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), fieldAnalyzers);
  }

}
