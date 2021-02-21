package e2log.configrpc;


import com.google.protobuf.Any;
import io.grpc.stub.StreamObserver;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService(interceptors ={TracingServerInterceptor.class})
public class ConfigRpcQueryService extends ConfigrpcServiceGrpc.ConfigrpcServiceImplBase {
  
  private final ConfigurationRepo configurationRepo;
  
  @Autowired
  public ConfigRpcQueryService(ConfigurationRepo configurationRepo) {
    this.configurationRepo = configurationRepo;
  }
  
  @Override
  public void findString(FindStringRequest request, StreamObserver<FindStringResponse> responseObserver) {
    log.debug("findString({})", request);
    val response = FindStringResponse.newBuilder();
    val key = request.getKey();
    val respContent = KeyString.newBuilder().setKey(key);
    
    val maybeJsonString = request.hasJsonPath() ? configurationRepo.queryObjectByDocumentKeyAndJsonPath(key, request.getJsonPath().getValue()) :
                                                   configurationRepo.fetchDocumentByKey(key);
    
    maybeJsonString.ifPresent(respContent::setValue);
    response.setKv(respContent);
    
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }
  
  @Override
  public void findObject(FindObjectRequest request, StreamObserver<FindObjectResponse> responseObserver) {
    log.debug("findObject({})", request);
//    val key = request.getKey();
//
//    val maybeObject = configurationRepo.findObjectByGid(key);
//
//    val response = FindObjectResponse.newBuilder();
//    val respContent = KeyObject.newBuilder().setKey(key);
//
//    maybeObject.ifPresent(r -> respContent.setValue(Any.pack(r)).build());
//    response.setKv(respContent);
//
//    responseObserver.onNext(response.build());
//    responseObserver.onCompleted();
  }
}
