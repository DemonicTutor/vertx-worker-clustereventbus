package com.noenv.markus;

import io.vertx.core.eventbus.DeliveryOptions;

final class Constants {

  static final String EB_ADDRESS_SCHEDULER = "eb_scheduler";
  static final String EB_ADDRESS_DRAIN = "eb_drain";

  static final DeliveryOptions LOCAL = new DeliveryOptions().setLocalOnly(true);
  static final DeliveryOptions DISTRIBUTED = new DeliveryOptions().setLocalOnly(false);

  private Constants() {}
}
