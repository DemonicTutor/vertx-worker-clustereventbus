Reproducer to show a concurrency issue when using EventBus from a Worker Verticle in a clustered deployment.

```
java.util.ConcurrentModificationException: null
	at java.base/java.util.HashMap.computeIfAbsent(HashMap.java:1220)
	at io.vertx.core.eventbus.impl.clustered.Serializer.queue(Serializer.java:64)
	at io.vertx.core.eventbus.impl.clustered.Serializer.lambda$queue$1(Serializer.java:67)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:833)
```

Run/Debug config in IntelliJ requires vm-options to be set: `-Dvertx.logger-delegate-factory-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Droot.level=INFO --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -Djava.net.preferIPv4Stack=true -Xms512M -Xmx512M`
