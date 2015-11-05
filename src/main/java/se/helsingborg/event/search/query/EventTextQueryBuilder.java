package se.helsingborg.event.search.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import se.helsingborg.event.search.EventIndexAnalyzerBuilder;
import se.helsingborg.event.search.IndexManager;

/**
 * @author kalle
 * @since 2015-11-05 22:47
 */
public class EventTextQueryBuilder {

  private Analyzer analyzer;

  private float tieBreakerMultiplier = 0f;

  private float nameBoost = 3f;
  private float descriptionBoost = 1f;
  private float tagBoost = 2f;


  private String text;

  public Query build() throws Exception {

    if (analyzer == null) {
      analyzer = new EventIndexAnalyzerBuilder().build();
    }

    DisjunctionMaxQuery query = new DisjunctionMaxQuery(tieBreakerMultiplier);

    query.add(tagQueryFactory());
    query.add(nameQueryFactory());

    return query;
  }

  public String getText() {
    return text;
  }

  public EventTextQueryBuilder setText(String text) {
    this.text = text;
    return this;
  }

  public float getTieBreakerMultiplier() {
    return tieBreakerMultiplier;
  }

  public EventTextQueryBuilder setTieBreakerMultiplier(float tieBreakerMultiplier) {
    this.tieBreakerMultiplier = tieBreakerMultiplier;
    return this;
  }

  private Query nameQueryFactory() throws Exception {
    Query query = textQueryFactory(IndexManager.FIELD_EVENT_NAME);
    query.setBoost(nameBoost);
    return query;
  }

  private Query descriptionQueryFactory() throws Exception {
    Query query = textQueryFactory(IndexManager.FIELD_EVENT_DESCRIPTION);
    query.setBoost(descriptionBoost);
    return query;
  }

  private Query textQueryFactory(String field) throws Exception {

    BooleanQuery.Builder query = new BooleanQuery.Builder();

    TokenStream ts = analyzer.tokenStream(field, text);
    ts.reset();
    CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);

    while (ts.incrementToken()) {
      TermQuery termQuery = new TermQuery(new Term(IndexManager.FIELD_EVENT_TAG, charTermAttribute.toString()));
      query.add(termQuery, BooleanClause.Occur.SHOULD);
    }

    return query.build();

  }

  private Query tagQueryFactory() throws Exception {

    BooleanQuery.Builder query = new BooleanQuery.Builder();

    // todo whitespace analyzer?
    TokenStream ts = new StandardAnalyzer().tokenStream(IndexManager.FIELD_EVENT_TAG, text);
    ts = new LowerCaseFilter(ts);
    ShingleFilter shingleFilter = new ShingleFilter(ts);
    shingleFilter.setFillerToken(" ");
    shingleFilter.setMinShingleSize(1);
    shingleFilter.setMaxShingleSize(3);
    shingleFilter.reset();

    CharTermAttribute charTermAttribute = shingleFilter.addAttribute(CharTermAttribute.class);

    while (shingleFilter.incrementToken()) {
      TermQuery termQuery = new TermQuery(new Term(IndexManager.FIELD_EVENT_TAG, charTermAttribute.toString()));
      termQuery.setBoost(tagBoost);
      query.add(termQuery, BooleanClause.Occur.SHOULD);
    }

    return query.build();

  }


}
