package ch.ithings.selin.api;

import ch.ithings.selin.data.entity.Environment;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.net.HttpURLConnection.*;
import ch.ithings.selin.vertx.service.EnvironmentAsyncService;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.api.RequestParameter;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;

/**
 * A standard verticle, consuming the {@link EnvironmentAsyncService} over the event bus to expose a REST API.
 *
 */
@Component
public class OpenApiVerticle extends AbstractVerticle {

  HttpServer server;
  private static final Logger LOG = LoggerFactory.getLogger(OpenApiVerticle.class);

  private EnvironmentAsyncService environmentAsyncService;

  @Override
  public void start(Future future) throws Exception {
    environmentAsyncService = new ServiceProxyBuilder(vertx).setAddress(EnvironmentAsyncService.ADDRESS).build(EnvironmentAsyncService.class);

    // Load the api spec. This operation is asynchronous
    OpenAPI3RouterFactory.create(vertx, "selin-api.yml", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.failed()) {
        // Something went wrong during router factory initialization
        Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
        LOG.error("oops, something went wrong during factory initialization", exception);
        future.fail(exception);
      }
      // Spec loaded with success
      OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();
      
      // Add an handler with operationId
      routerFactory.addHandlerByOperationId("listEnvironments", routingContext -> {
        // Load the parsed parameters
        RequestParameters params = routingContext.get("parsedParameters");
        // Handle listPets operation
        RequestParameter limitParameter = params.queryParameter(/* Parameter name */"limit");
        if (limitParameter != null) {
          // limit parameter exists, use it!
          Integer limit = limitParameter.getInteger();
        } else {
          // limit parameter doesn't exist (it's not required).
          // If it's required you don't have to check if it's null!
        }
        listEnvironments(routingContext);
      });
      
      
      
      routerFactory.addHandlerByOperationId("createEnvironment", routingContext -> {
        createEnvironment(routingContext);
      });
      
      // Add a failure handler
      routerFactory.addFailureHandlerByOperationId("createEnvironment", routingContext -> {
        // This is the failure handler
        Throwable failure = routingContext.failure();
        if (failure instanceof ValidationException)
          // Handle Validation Exception
          routingContext.response()
            .setStatusCode(400)
            .end(((ValidationException) failure).getMessage());
      });
      

      // Add a security handler
      routerFactory.addSecurityHandler("api_key", routingContext -> {
        // Handle security here
        routingContext.next();
      });

      // Before router creation you can enable/disable various router factory behaviours
      RouterFactoryOptions factoryOptions = new RouterFactoryOptions()
              .setMountNotImplementedHandler(true)
              .setMountValidationFailureHandler(false) // if true, disable custom failure handler
              .setMountResponseContentTypeHandler(true); // Mount ResponseContentTypeHandler automatically

      // Now you have to generate the router
      Router router = routerFactory.setOptions(factoryOptions).getRouter();

      // Now you can use your Router instance
      server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("0.0.0.0"));
      server.requestHandler(router::accept).listen((ar) -> {
        if (ar.succeeded()) {
          LOG.info("Server started on port " + ar.result().actualPort());
          future.complete();
        } else {
          LOG.error("oops, something went wrong during server initialization", ar.cause());
          future.fail(ar.cause());
        }
      });
    });
  }

  @Override
  public void stop() {
    this.server.close();
  }

  private void createEnvironment(RoutingContext routingContext) {
    Environment LOG = new Environment(routingContext.getBodyAsJson());
    environmentAsyncService.add(LOG, ar -> {
      if (ar.succeeded()) {
        routingContext.response().setStatusCode(HTTP_CREATED).end();
      } else {
        routingContext.fail(ar.cause());
      }
    });
  }

  private void listEnvironments(RoutingContext routingContext) {
    environmentAsyncService.getAll(ar -> {
      if (ar.succeeded()) {
        List<Environment> result = ar.result();
        JsonArray jsonArray = new JsonArray(result);
        routingContext.response().end(jsonArray.encodePrettily());
      } else {
        routingContext.fail(ar.cause());
      }
    });
  }
}
