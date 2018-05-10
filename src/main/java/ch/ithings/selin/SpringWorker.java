package ch.ithings.selin;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*;
import ch.ithings.selin.vertx.service.EnvironmentAsyncService;

/**
 * A worker verticle, exposing the {@link EnvironmentAsyncService} over the event bus.
 *
 * Since it is a worker verticle, it is perfectly fine for the registered service to delegate calls to backend Spring beans.
 *
 * @author Thomas Pham
 */
@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class SpringWorker extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(SpringWorker.class);

  @Autowired
  EnvironmentAsyncService environmentAsyncService;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    new ServiceBinder(vertx).setAddress(EnvironmentAsyncService.ADDRESS).register(EnvironmentAsyncService.class, environmentAsyncService).completionHandler(ar ->{
      if (ar.succeeded()) {
        LOG.info("SpringWorker started");
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }

}
