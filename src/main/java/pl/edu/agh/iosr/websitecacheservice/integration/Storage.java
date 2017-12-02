package pl.edu.agh.iosr.websitecacheservice.integration;

import org.springframework.stereotype.Component;

@Component
public class Storage {

    public void saveCachedWebsite(String content) {
        //TODO: Add S3 integration
    }

    public String getCachedWebsite(String id) {
        return "";
    }
}
