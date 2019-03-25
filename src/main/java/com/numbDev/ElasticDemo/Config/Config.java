package com.numbDev.ElasticDemo.Config;

import com.numbDev.ElasticDemo.Base.Repo.DateRepository;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableJpaRepositories(basePackageClasses = DateRepository.class)
@EnableTransactionManagement
public class Config implements TransactionManagementConfigurer {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;
    private final DataSource dataSource;

    public Config(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public RestHighLevelClient client() throws UnknownHostException {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(host, port)).setRequestConfigCallback(
                builder -> builder.setSocketTimeout(100000).setConnectTimeout(100000)
                    .setConnectionRequestTimeout(100000)).setMaxRetryTimeoutMillis(100000));

//        Settings settings = Settings.builder()
//            .put("cluster.name", "my-cluster").build();
//        TransportClient client = new PreBuiltTransportClient(settings);
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//            .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        return client;
//        TransportClient client = null;
//        try {
//            System.out.println("host:"+ host+"port:"+port);
//            client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return client;
    }
}
