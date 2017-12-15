package pl.edu.agh.iosr.websitecacheservice.configuration;

import com.amazonaws.annotation.Beta;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.iosr.websitecacheservice.processing.WebsiteProcessor;

@Configuration
public class BeansConfiguration {

    @Value("${rabbit.address}")
    private String rabbitAddress;
    @Value("${rabbit.username}")
    private String rabbitUsername;
    @Value("${rabbit.password}")
    private String rabbitPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitAddress);
        factory.setUsername(rabbitUsername);
        factory.setPassword(rabbitPassword);
        return factory;
    }

    @Bean
    public AmazonS3 storageClientFactory() {
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();//withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        return s3client;
    }
}
