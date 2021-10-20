package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.Message;

public final class ProducerVerticle extends AbstractVerticle {

  @Override
  public Completable rxStart() {
    return Completable.fromAction(this::setup);
  }

  private void setup() {
    // consumer uses a DuplicatedContext internally - not sure if this is part of the issue but 100 consumers definitely increase the chances of it happening
    for (int i = 100; 0 < i; i--)
      super.vertx.eventBus().consumer(Constants.EB_ADDRESS_SCHEDULER, this::produce);
  }

  private void produce(final Message<Void> message) {
    // FIXME: this can throw a ConcurrentModificationException on eventloop which cant be recovered from - message is simply lost
    super.vertx.eventBus().send(Constants.EB_ADDRESS_DRAIN, "value", Constants.DISTRIBUTED);
  }
}
