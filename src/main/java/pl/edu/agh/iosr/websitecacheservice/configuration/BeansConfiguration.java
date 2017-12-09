package pl.edu.agh.iosr.websitecacheservice.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Value("${rabbit.address}")
    private String brokerAddress;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerAddress);
        return factory;
    }

    @Bean
    public AmazonS3 storageClientFactory() {
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();//withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        return s3client;
    }
}
