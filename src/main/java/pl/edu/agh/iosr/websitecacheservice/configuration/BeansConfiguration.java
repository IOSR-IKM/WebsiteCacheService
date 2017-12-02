package pl.edu.agh.iosr.websitecacheservice.configuration;

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
}
