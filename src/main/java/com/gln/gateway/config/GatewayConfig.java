package com.gln.gateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${gateway.rewards:}")
    private List<String> rewardsStrList;

    @Value("${gateway.games:}")
    private List<String> gamesStrList;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        System.out.println(rewardsStrList);
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        for (String rewardsStr : rewardsStrList) {
            routes.route(rewards -> rewards.path(rewardsStr).uri("lb://rewards-service")).build();
        }
        for (String gamesStr : gamesStrList) {
            routes.route(rewards -> rewards.path(gamesStr).uri("lb://games-service")).build();
        }
        return routes.build();
    }
}
