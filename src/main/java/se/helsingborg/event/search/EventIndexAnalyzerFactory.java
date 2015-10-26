package se.helsingborg.event.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2015-10-24 11:56
 */
public class EventIndexAnalyzerFactory  {

  public static Analyzer factory() throws Exception {
    Map<String, Analyzer> fieldAnalyzers = new HashMap<>();
    return new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_45, new StringReader("")), fieldAnalyzers);
  }

}
