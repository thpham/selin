package ch.ithings.selin.vertx.service;

import ch.ithings.selin.data.service.EnvironmentService;
import ch.ithings.selin.data.entity.Environment;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import ch.ithings.selin.vertx.service.EnvironmentAsyncService;

/**
 * Implements the {@link EnvironmentAsyncService}, delegating calls to the transactional {@link EnvironmentService}.
 *
 * @author Thomas Pham
 */
@Component
public class EnvironmentAsyncServiceImpl implements EnvironmentAsyncService {

  @Autowired
  EnvironmentService bookService;

  @Override
  public void add(Environment book, Handler<AsyncResult<Environment>> resultHandler) {
    Environment saved = bookService.save(book);
    Future.succeededFuture(saved).setHandler(resultHandler);
  }

  @Override
  public void getAll(Handler<AsyncResult<List<Environment>>> resultHandler) {
    List<Environment> all = bookService.getAll();
    Future.succeededFuture(all).setHandler(resultHandler);
  }
}
