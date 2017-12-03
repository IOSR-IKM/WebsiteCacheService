package pl.edu.agh.iosr.websitecacheservice.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    private static final String BROKER_ADDRESS = "localhost";

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(BROKER_ADDRESS);
        return factory;
    }

    @Bean
    public AmazonS3 storageClientFactory() {
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();//withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        return s3client;
    }
}
