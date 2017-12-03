package pl.edu.agh.iosr.websitecacheservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.iosr.websitecacheservice.controller.UrlController;
import pl.edu.agh.iosr.websitecacheservice.integration.Storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlControllerTest {

    private static final String WRONG_ID = "WRONG";
    private static final String GOOD_ID = "OK";
    @Mock
    private Storage storage;

    private UrlController controller;

    @Before
    public void setup()
    {
        initMocks(this);
        controller = new UrlController(storage);
    }

    @Test
    public void shouldReturnSiteWhenPresent()
    {
        //WHEN
        when(storage.getCachedWebsite(GOOD_ID)).thenReturn(GOOD_ID);
        ResponseEntity<String> res = controller.getCachedWebsite(GOOD_ID);

        //THEN
        assertTrue(res.getBody().equals(GOOD_ID));
        assertEquals(res.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldReturn404WhenSiteNotPresent()
    {
        //WHEN
        when(storage.getCachedWebsite(WRONG_ID)).thenReturn(null);
        ResponseEntity<String> res = controller.getCachedWebsite(WRONG_ID);

        //THEN
        assertNull(res.getBody());
        assertEquals(res.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
