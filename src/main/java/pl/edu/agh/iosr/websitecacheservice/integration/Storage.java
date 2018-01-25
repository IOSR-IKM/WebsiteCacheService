package pl.edu.agh.iosr.websitecacheservice.integration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Storage {
    public static final String BUCKET_NAME = "gonzo251.iosr";

    private static final String TMP_PATH = "tmp.html";
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final AmazonS3 s3client;
    private Map<String, String> cache;

    public void saveCachedWebsite(String id, String content) {
        if(cache == null)
            cache = new HashMap<>();

        try {
            saveToS3(id, content);
            if (!cache.isEmpty())
            {
                for(String cacheId : cache.keySet())
                {
                    saveToS3(cacheId, cache.get(cacheId));
                }
            }
        } catch (Exception e) {
            logger.error(" [X] Something wrong with S3 connection, caching query for the future");
            cache.put(id, content);
        }
    }

    public void saveToS3(String id, String content) throws Exception {
        File file = createFile(content);
        if (!s3client.doesBucketExist(BUCKET_NAME)) {
            s3client.createBucket(BUCKET_NAME);
        }
        s3client.putObject(BUCKET_NAME, id, file);
        logger.error(" [X] Website saved");
        cleanFile(file);
    }

    public String getCachedWebsite(String id) {
        if (s3client.doesObjectExist(BUCKET_NAME, id))
            return s3client.getObjectAsString(BUCKET_NAME, id);
        else
            return null;
    }

    private File createFile(String content) {
        File file = new File(TMP_PATH);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return file;
    }

    private void cleanFile(File file) {
        if (!file.delete()) {
            logger.error("Cannot delete file: " + file.getName());
        }
    }
}
