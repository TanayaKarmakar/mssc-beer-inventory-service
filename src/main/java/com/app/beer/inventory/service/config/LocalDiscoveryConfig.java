package com.app.beer.inventory.service.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author t0k02w6 on 15/06/21
 * @project mssc-beer-inventory-service
 */
@Profile("local-discovery")
@EnableDiscoveryClient
@Configuration
public class LocalDiscoveryConfig {
}
