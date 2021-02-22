package e2log.beans;

import com.google.protobuf.TypeRegistry;
import com.google.protobuf.util.JsonFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProtobufJsonConversion {
  
  private JsonFormat.Parser jsonParser;
  private JsonFormat.Printer jsonPrinter;
  
  @PostConstruct
  void init(){
    jsonParser = createJsonParser();
    jsonPrinter = createJsonPrinter();
  }
  
  @Bean
  public JsonFormat.Parser getJsonParser(){
    return jsonParser;
  }
  
  @Bean
  public JsonFormat.Printer getJsonPrinter(){
    return jsonPrinter;
  }
  
  private static JsonFormat.Parser createJsonParser() {
    // Need to ensure we add any possible protobuf types that could be Any (i.e EventData field)
    TypeRegistry registry = TypeRegistry.newBuilder()
//                                        .add(SomeProtoConfigurationModelClassCompany.getDescriptor())
                                        .build();
    
    return JsonFormat.parser()
                     .ignoringUnknownFields()
                     .usingTypeRegistry(registry);
  }
  
  private static JsonFormat.Printer createJsonPrinter() {
    // Need to ensure we add any possible protobuf types that could be Any (i.e EventData field)
    TypeRegistry registry = TypeRegistry.newBuilder()
//                                        .add(SomeProtoConfigurationModelClassCompany.getDescriptor())
                                        .build();
    
    return JsonFormat.printer()
                     .usingTypeRegistry(registry)
                     .omittingInsignificantWhitespace();
  }
  
}
