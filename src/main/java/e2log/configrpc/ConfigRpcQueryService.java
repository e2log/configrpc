package e2log.configrpc;


import io.grpc.Status;
import io.grpc.StatusException;
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
    val key = request.getKey();

    try {
      val maybeJsonString = request.hasJsonPath() ? configurationRepo.queryObjectByDocumentKeyAndJsonPath(key, request.getJsonPath().getValue()) :
        configurationRepo.fetchDocumentByKey(key);

      maybeJsonString.ifPresentOrElse(value -> {
          // Build and return the protobuf response
          val respContent = KeyString.newBuilder().setKey(key).setValue(value);
          val response = FindStringResponse.newBuilder().setKv(respContent);
          responseObserver.onNext(response.build());
          responseObserver.onCompleted();
        }, () ->
          // Return exception because expected value was not found for provided key
          responseObserver.onError(
            new StatusException(Status.NOT_FOUND.withDescription("Cannot find value with key=" + key))
          )
      );
    } catch(IllegalArgumentException e) {
      // Wrapping IAE exception and returning appropriate gRpc status via onError
      responseObserver.onError(new StatusException(Status.INVALID_ARGUMENT.withDescription(e.getMessage())));
    }
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
