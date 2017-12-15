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
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

@Component
public class WebsiteProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteProcessor.class);

    private String queueName;
    private final ConnectionFactory connectionFactory;
    private final Storage storage;

    public WebsiteProcessor(ConnectionFactory connectionFactory, Storage storage,  @Value("${queue.name}") String queueName) {
        this.connectionFactory = connectionFactory;
        this.storage = storage;
        this.queueName = queueName;

        processQueue();
    }

    public void processQueue() {
        logger.debug("Start processing...");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            logger.debug("Waiting for messages...");

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.debug(" [x] Received '" + message + "'");
                    String [] unpacked = message.split(",", 2);
                    cacheWebsite(unpacked[0], unpacked[1]);
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | NullPointerException | TimeoutException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void cacheWebsite(String id, String url) {
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

            storage.saveCachedWebsite(id, content);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
