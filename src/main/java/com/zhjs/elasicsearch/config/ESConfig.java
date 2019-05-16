package com.zhjs.elasicsearch.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zhjs on 2019/5/7.
 */

@Component
public class ESConfig {


    @Value("${es_url}")
    private String esUrl;

    @Value("${es_port}")
    private Integer esPort;

    @Value("${es_cluster_name}")
    private String esClusterName;

    @Bean
    public TransportClient esClient() throws UnknownHostException {
        TransportAddress address = new TransportAddress(InetAddress.getByName(esUrl),esPort);
        Settings settings = Settings.builder()
                .put("cluster.name", esClusterName)
                .put("client.transport.sniff", true)
                .build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(address);
        return client;
    }
}
