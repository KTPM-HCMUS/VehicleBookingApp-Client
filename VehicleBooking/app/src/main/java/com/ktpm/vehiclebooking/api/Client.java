package com.ktpm.vehiclebooking.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.SettableFuture;
import com.ktpm.vehiclebooking.model.UserTemp;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class Client {
    private final WebClient webClient = WebClient.create(Vertx.vertx(),
            new WebClientOptions().setVerifyHost(false).setSsl(false).setMaxPoolSize(1));
    public Client() {
        Runtime.getRuntime().addShutdownHook(new Thread(webClient::close));
    }
    public Map<String, Object> login(String userId, String password) throws ExecutionException, InterruptedException {
        SettableFuture<Map<String, Object>> result = SettableFuture.create();
        ImmutableMap<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("userId", userId)
                .put("password", password)
                .build();
        webClient.post(8080, "34.72.120.102", "/login")
                .timeout(2000)
                .putHeader("content-type", "application/json; charset=utf-8")
                .sendJson(new JsonObject(map), response -> {
                    if (response.succeeded() && response.result().statusCode() == 200) {
                        result.set(response.result().bodyAsJsonObject().getMap());
                    } else {
                        result.set(ImmutableMap.of());
                    }
                });

        return result.get();
    }

    public Map<String, Object> revokeRefreshToken(String userId, String token) throws ExecutionException, InterruptedException {
        SettableFuture<Map<String, Object>> result = SettableFuture.create();
        ImmutableMap<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("userId", userId)
                .build();
        webClient.post(8080, "34.72.120.102", "/v1/revokeToken")
                .timeout(2000)
                .bearerTokenAuthentication(token)
                .putHeader("content-type", "application/json; charset=utf-8")
                .sendJson(new JsonObject(map), response -> {
                    if (response.succeeded() && response.result().statusCode() == 200) {
                        result.set(response.result().bodyAsJsonObject().getMap());
                    } else {
                        result.set(ImmutableMap.of());
                    }
                });

        return result.get();
    }
}
