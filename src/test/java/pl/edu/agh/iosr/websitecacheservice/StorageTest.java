package pl.edu.agh.iosr.websitecacheservice;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.iosr.websitecacheservice.integration.Storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageTest {

    private static final String ID = "FOO";
    private static final String CONTENT = "CONTENT";

    @Mock
    private AmazonS3 s3Client;

    private Storage storage;

    @Before
    public void setup() {
        initMocks(this);
        storage = new Storage(s3Client);
    }

    @Test
    public void shouldReturnSiteWhenPresent() {
        when(s3Client.doesObjectExist(Storage.BUCKET_NAME, ID)).thenReturn(true);
        when(s3Client.getObjectAsString(Storage.BUCKET_NAME, ID)).thenReturn(CONTENT);

        assertTrue(storage.getCachedWebsite(ID).equals(CONTENT));
    }

    @Test
    public void shouldReturnNullWhenSiteNotPresent() {
        when(s3Client.doesObjectExist(Storage.BUCKET_NAME, ID)).thenReturn(false);

        assertNull(storage.getCachedWebsite(ID));
    }
}
