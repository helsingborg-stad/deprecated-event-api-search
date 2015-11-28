package se.helsingborg.event.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2015-10-24 11:56
 */
public class EventIndexAnalyzerBuilder {

  public Analyzer build() throws Exception {
    Map<String, Analyzer> fieldAnalyzers = new HashMap<>();

    fieldAnalyzers.put(IndexManager.FIELD_EVENT_TAG, tagsAnalyzerFactory());
    fieldAnalyzers.put(IndexManager.FIELD_EVENT_COMBINED_TEXT_NGRAMS, combinedTextNgramsAnalyzerFactory());

    /** standard analyzer with no stop words */
    return new PerFieldAnalyzerWrapper(new StandardAnalyzer(new StringReader("")), fieldAnalyzers);
  }


  public Analyzer tagsAnalyzerFactory() {
    return new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new KeywordTokenizer();
        TokenStream filter = new LowerCaseFilter(tokenizer);
        return new TokenStreamComponents(tokenizer, filter);

      }
    };
  }

  public Analyzer combinedTextNgramsAnalyzerFactory() {
    return new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new KeywordTokenizer();
        TokenStream lowerCaseFilter = new LowerCaseFilter(tokenizer);
        NGramTokenFilter nGramTokenFilter = new NGramTokenFilter(lowerCaseFilter, 3, 5);
        return new TokenStreamComponents(tokenizer, nGramTokenFilter);

      }
    };
  }

}
