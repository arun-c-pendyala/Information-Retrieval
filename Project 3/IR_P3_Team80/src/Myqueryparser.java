


package queryparser;

import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.ExtendedDismaxQParser;
import org.apache.solr.search.ExtendedDismaxQParserPlugin;
import org.apache.solr.search.QParser;


public class Myqueryparser extends ExtendedDismaxQParserPlugin {
  private static final String[] defined_weights = {"text_en", "text_de", "text_ru", "tweet_hashtags"};
  
  @Override
  public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    ModifiableSolrParams customParams = new ModifiableSolrParams();
    
    if (qstr.contains("lang:en")) {
        defined_weights[0] = "text_en^2.5";
        qstr = qstr.replace("lang:en", "");
      }
    if (qstr.contains("lang:de")) {
        defined_weights[1] = "text_de^2.5";
        qstr = qstr.replace("lang:de", "");
      }
    if (qstr.contains("text_ru")) {
        defined_weights[2] = "text_ru^2.5";
        qstr = qstr.replace("text_ru", "");
      }
    
      defined_weights[3] = "tweet_hashtags^1";
    
    
   
    
    customParams.add(DisMaxParams.QF,defined_weights);
    params = SolrParams.wrapAppended(params, customParams);
    return new ExtendedDismaxQParser(qstr, localParams, params, req);
  }
}
