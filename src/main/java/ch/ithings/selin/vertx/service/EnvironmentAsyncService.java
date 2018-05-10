package ch.ithings.selin.vertx.service;

import ch.ithings.selin.data.entity.Environment;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Describes a service exposed on the event bus with <a href="http://vertx.io/docs/vertx-service-proxy/java/">Vert.x Service Proxies</a>.
 *
 * @author Thomas Pham
 */
@ProxyGen
public interface EnvironmentAsyncService {

  /**
   * The service address on the Vert.x event bus.
   */
  String ADDRESS = EnvironmentAsyncService.class.getName();

  void add(Environment book, Handler<AsyncResult<Environment>> resultHandler);

  void getAll(Handler<AsyncResult<List<Environment>>> resultHandler);
}
