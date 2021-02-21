package e2log.configrpc;

import com.google.protobuf.util.JsonFormat;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
/**
 * Data repository for fetching configuration values from the data store.
 * Configuration results are JSON string documents, some methods convert JSON to protobuf.
 */
public class ConfigurationRepo {
  
  private static final String VALUE_BY_KEY_QUERY = "select value from configuration where key=(?)";
  private final JdbcTemplate template;
  private final JsonFormat.Printer jsonPrinter;
  private final JsonFormat.Parser jsonParser;
  
  @Autowired
  public ConfigurationRepo(JdbcTemplate template, JsonFormat.Printer jsonPrinter, JsonFormat.Parser parser) {
    this.template = template;
    this.jsonPrinter = jsonPrinter;
    this.jsonParser = parser;
  }
  
  /**
   * A JSON string value API
   *
   * @return #Optional.empty if the key doesn't exist in the datastore or
   * a JSON String containing the result of the jsonPath expression evaluated against the original JSON document in the data store.
   * */
  public Optional<String> queryObjectByDocumentKeyAndJsonPath(String key, String jsonPath){
    val document = fetchDocumentByKey(key);
    return document.map(json -> {
      return  JsonPath.parse(json)
              .read(jsonPath).toString();
    }
    );
  }
  
//  /**
//   * A typed value API returning a protobuf
//   *
//   * @return #Optional.empty if the key doesn't exist in the datastore or
//   * a protobuf value containing the result of converting the original JSON document in the data store
//   * to a proto model defined in the conf-grpc-api.
//   * */
//  public Optional<Message> findObjectByGid(String companyGid){
//    return fetchTypedConfigByKey(companyGid, CompanyConfig.newBuilder());
//  }
//
//  private Optional<Message> fetchTypedConfigByKey(String key, Message.Builder builder) {
//    try {
//      val json = template.queryForObject("select config::text from company_config where company_gid=(?)", new String[]{key}, String.class);
//      jsonParser.merge(json, builder);
//      return Optional.of(builder.build());
//    }
//    catch (EmptyResultDataAccessException erde){
//      log.warn("Could not find setting using query={} for key={}", "select config::text from company_config where company_gid=(?)", key);
//      return Optional.empty();
//    }
//    catch (InvalidProtocolBufferException invProtobufE){
//      log.error("Could not parse setting value for query={} for key={}", "select config::text from company_config where company_gid=(?)", key, invProtobufE);
//      throw new IllegalStateException(invProtobufE);
//    }
//  }
  
  /**
   * A JSON string value API
   *
   * @return #Optional.empty if the key doesn't exist in the datastore or
   * a JSON String containing the original JSON document in the data store.
   * */
  public Optional<String> fetchDocumentByKey(String key) {
    try {
      val json = template.queryForObject(VALUE_BY_KEY_QUERY, new String[]{key}, String.class);
      return Optional.ofNullable(json);
    }
    catch (EmptyResultDataAccessException erde){
      log.warn("Could not find document using query={} for key={}", VALUE_BY_KEY_QUERY, key);
      return Optional.empty();
    }
  }
  
}
