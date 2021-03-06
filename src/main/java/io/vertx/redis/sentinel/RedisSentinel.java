package io.vertx.redis.sentinel;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.net.SocketAddress;
import io.vertx.redis.RedisOptions;
import io.vertx.redis.impl.RedisSentinelClientImpl;

/**
 * Interface for sentinel commands
 * @deprecated
 * @see io.vertx.redis.client.Redis for the new API.
 */
@VertxGen
@Deprecated
public interface RedisSentinel {

  static void create(Vertx vertx, Handler<AsyncResult<RedisSentinel>> handler) {
    RedisSentinelClientImpl.create(vertx, new io.vertx.redis.client.RedisOptions(), handler);
  }

  static void create(Vertx vertx, RedisOptions options, Handler<AsyncResult<RedisSentinel>> handler) {
    RedisSentinelClientImpl.create(vertx,
      // need to convert from the old options to the new one...
      new io.vertx.redis.client.RedisOptions()
        .setNetClientOptions(options)
        .setEndpoint(options.isDomainSocket() ? SocketAddress.domainSocketAddress(options.getDomainSocketAddress()) : SocketAddress.inetSocketAddress(options.getPort(), options.getHost()))
        .setPassword(options.getAuth())
        .setSelect(options.getSelect()),
      handler);
  }

  /**
   * Close the client - when it is fully closed the handler will be called.
   *
   * @param handler
   */
  void close(Handler<AsyncResult<Void>> handler);

  /**
   * Show a list of monitored masters and their state
   *
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel masters(Handler<AsyncResult<JsonArray>> handler);

  /**
   * Show the state and info of the specified master
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel master(String name, Handler<AsyncResult<JsonArray>> handler);

  /**
   * Show a list of slaves for this master, and their state
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel slaves(String name, Handler<AsyncResult<JsonArray>> handler);

  /**
   * Show a list of sentinel instances for this master, and their state
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel sentinels(String name, Handler<AsyncResult<JsonArray>> handler);

  /**
   * Return the ip and port number of the master with that name.
   * If a failover is in progress or terminated successfully for this master
   * it returns the address and port of the promoted slave
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel getMasterAddrByName(String name, Handler<AsyncResult<JsonArray>> handler);


  /**
   * Reset all the masters with matching name.
   * The pattern argument is a glob-style pattern.
   * The reset process clears any previous state in a master (including a failover in pro
   *
   * @param pattern pattern String
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel reset(String pattern, Handler<AsyncResult<Void>> handler);

  /**
   * Force a failover as if the master was not reachable, and without asking for agreement to other Sentinels
   * (however a new version of the configuration will be published so that the other Sentinels
   * will update their configurations)
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel failover(String name, Handler<AsyncResult<String>> handler);

  /**
   * Check if the current Sentinel configuration is able to reach the quorum needed to failover a master,
   * and the majority needed to authorize the failover. This command should be used in monitoring systems
   * to check if a Sentinel deployment is ok.
   *
   * @param name    master name
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel ckquorum(String name, Handler<AsyncResult<String>> handler);

  /**
   * Force Sentinel to rewrite its configuration on disk, including the current Sentinel state.
   * Normally Sentinel rewrites the configuration every time something changes in its state
   * (in the context of the subset of the state which is persisted on disk across restart).
   * However sometimes it is possible that the configuration file is lost because of operation errors,
   * disk failures, package upgrade scripts or configuration managers. In those cases a way to to force Sentinel to
   * rewrite the configuration file is handy. This command works even if the previous configuration file
   * is completely missing.
   *
   * @param handler Handler for the result of this call
   */
  @Fluent
  RedisSentinel flushConfig(Handler<AsyncResult<Void>> handler);
}
