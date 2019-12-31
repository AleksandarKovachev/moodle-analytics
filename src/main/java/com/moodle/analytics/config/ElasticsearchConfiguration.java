package com.moodle.analytics.config;

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

/**
 * ElasticSearch configuration
 * 
 * @author aleksandar.kovachev
 *
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.podcasses.repository")
public class ElasticsearchConfiguration {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	@Value("${elasticsearch.clustername}")
	private String clusterName;

	@Bean
	public Client client() throws UnknownHostException {
		Settings elasticsearchSettings = Settings.builder().put("client.transport.sniff", true)
				.put("cluster.name", clusterName).build();
		TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
		return client;
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() throws Exception {
		return new ElasticsearchTemplate(client());
	}

}
