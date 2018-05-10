package ch.ithings.selin;

import ch.ithings.selin.vertx.service.EnvironmentAsyncService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thomas Pham
 */
@Component
public class WebShellVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WebShellVerticle.class);

  private EnvironmentAsyncService environmentAsyncService;

  @Override
  public void start(Future future) throws Exception {
    environmentAsyncService = new ServiceProxyBuilder(vertx).setAddress(EnvironmentAsyncService.ADDRESS).build(EnvironmentAsyncService.class);
    ShellService service = ShellService.create(vertx,
            new ShellServiceOptions().setHttpOptions(
                    new HttpTermOptions().
                            setHost("0.0.0.0").
                            setPort(2222)
            )
    );
    
    Command helloWorld = CommandBuilder.command("hello-world").
        processHandler(process -> {
          process.write("hello world\n");
          process.end();
        }).build(vertx);
    CommandRegistry.getShared(vertx).registerCommand(helloWorld);
    
    service.start(ar -> {
      if (ar.succeeded()) {
        LOG.info("WebShell started");
        future.complete();
      } else {
        LOG.error("oops, something went wrong during WebShell initialization", ar.cause());
        future.fail(ar.cause());
      }
    });

  }
}
