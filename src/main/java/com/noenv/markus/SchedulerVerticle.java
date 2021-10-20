package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;

public final class SchedulerVerticle extends AbstractVerticle {

  @Override
  public Completable rxStart() {
    return Completable.fromAction(this::schedule);
  }

  private void schedule() {
    super.vertx.periodicStream(1_000).toObservable()
      .doOnNext(this::execute)
      .subscribe();
  }

  private void execute(final long ignore) {
    super.vertx.eventBus().publish(Constants.EB_ADDRESS_SCHEDULER, null, Constants.LOCAL);
  }
}
