package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava3.CompletableHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.eventbus.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RunWith(VertxUnitRunner.class)
public class ReproducerTest {

  // ATTENTION: green/red does not matter - test does not assert failure - check the log for exception - counting expected/actual messages just makes the code noisy
  @Test(timeout = 5 * 60 * 60 * 1000)
  public void shouldWork(final TestContext ctx) {
    // GIVEN
    final var schedulerVerticle = SchedulerVerticle.class.getCanonicalName();
    final var schedulerVerticleOptions =  new DeploymentOptions().setWorker(false).setInstances(1);
    final var producerVerticle = ProducerVerticle.class.getCanonicalName();
    final var producerVerticleOptions = new DeploymentOptions().setWorker(true).setInstances(100);

    final Function<Vertx, Completable> blackhole = vertx -> Completable.fromAction(() -> vertx.eventBus().consumer(Constants.EB_ADDRESS_DRAIN, this::consume));
    // SETUP
    Vertx.clusteredVertx(new VertxOptions())
      .flatMapCompletable(
        vertx -> blackhole.apply(vertx)
            .andThen(vertx.rxDeployVerticle(schedulerVerticle, schedulerVerticleOptions).ignoreElement())
            .andThen(vertx.rxDeployVerticle(producerVerticle, producerVerticleOptions).ignoreElement())
      )
      .delay(5, TimeUnit.MINUTES)
      .andThen(Completable.error(new AssertionError("check logs for java.util.ConcurrentModificationException")))
      .subscribe(CompletableHelper.toObserver(ctx.asyncAssertSuccess()));
  }

  private void consume(final Message<String> message) {
    // register consumer for messages to go somewhere - does not matter where they go
  }
}
