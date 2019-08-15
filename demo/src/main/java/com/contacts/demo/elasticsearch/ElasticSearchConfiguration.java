package com.contacts.demo.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.contacts.demo.elasticsearch")
public class ElasticSearchConfiguration {

    @Bean
    public Client client() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        return transportClient;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
}
