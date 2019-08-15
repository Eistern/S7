package com.example.demoreport.hazlecast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazlecastConfiguration {
    @Bean
    public Config getHazlecastConfig() {
        Config config = new Config();
        config.addMapConfig(new MapConfig().setTimeToLiveSeconds(-1).setName("ConfMap"));
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPortAutoIncrement(true);
        networkConfig.setPort(3301);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.getJoin().getTcpIpConfig().addMember("127.0.0.1");
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        return config;
    }
}
