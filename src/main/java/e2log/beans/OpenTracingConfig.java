package e2log.beans;

import com.google.gson.Gson;
import io.jaegertracing.Configuration;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OpenTracingConfig {

  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "true")
  @Bean
  public io.opentracing.Tracer jaegerTracer() {
    io.opentracing.contrib.jdbc.TracingDriver.load();

    val config = Configuration.fromEnv("conf");
    log.info(new Gson().toJson(config));
    return config.getTracer();
  }

  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "false", matchIfMissing = true)
  @Bean
  public io.opentracing.Tracer disabledJaegerTracer() {
    return io.opentracing.noop.NoopTracerFactory.create();
  }

  @Bean
  public TracingServerInterceptor ServerInterceptor (io.opentracing.Tracer jaegerTracer){
    return TracingServerInterceptor
      .newBuilder()
      .withTracer(jaegerTracer)
      .build();
  }
  
}
