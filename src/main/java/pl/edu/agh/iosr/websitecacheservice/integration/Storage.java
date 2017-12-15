package pl.edu.agh.iosr.websitecacheservice.integration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Storage {
    public static final String BUCKET_NAME = "gonzo251.iosr";

    private static final String TMP_PATH = "tmp.html";
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final AmazonS3 s3client;

    public void saveCachedWebsite(String id, String content) {
        try {
            File file = createFile(content);
            if (!s3client.doesBucketExist(BUCKET_NAME)) {
                s3client.createBucket(BUCKET_NAME);
            }
            s3client.putObject(BUCKET_NAME, id, file);
            cleanFile(file);
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            logger.error("Error Message: " + ace.getMessage());
        }
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
