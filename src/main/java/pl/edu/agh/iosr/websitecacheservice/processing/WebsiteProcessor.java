package pl.edu.agh.iosr.websitecacheservice.processing;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.iosr.websitecacheservice.integration.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

@Component
public class WebsiteProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteProcessor.class);

    @Value("${queue.name}")
    private String queueName;
    private final ConnectionFactory connectionFactory;
    private final Storage storage;

    public WebsiteProcessor(ConnectionFactory connectionFactory, Storage storage) {
        this.connectionFactory = connectionFactory;
        this.storage = storage;

        processQueue();
    }

    private void processQueue() {
        Channel channel = null;
        try (Connection connection = connectionFactory.newConnection()) {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            logger.debug("Waiting for messages...");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.debug(" [x] Received '" + message + "'");
                    cacheWebsite(message);
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException | NullPointerException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if(channel != null && channel.isOpen())
                    channel.close();
            } catch (IOException | TimeoutException e) {
                logger.error("Failed to close channel!");
            }
        }
    }

    private void cacheWebsite(String url) {
        try {
            URL website = new URL(url);
            URLConnection websiteConnection = website.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    websiteConnection.getInputStream()));

            String content;
            String tmp;
            StringBuilder sb = new StringBuilder();

            while ((tmp = in.readLine()) != null)
                sb.append(tmp);
            content = sb.toString();
            in.close();

            storage.saveCachedWebsite(url, content);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
