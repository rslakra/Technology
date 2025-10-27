package com.rslakra.imageservice;

import com.rslakra.imageservice.controller.ImageController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ImageServiceApplicationTests {

    private final String TEMP_FOLDER = "/Users/lakra/Downloads/Temp";


    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    WebApplicationContext webApplicationContext;

    /**
     * @return
     */
    private final String getUrlForService(String serviceName) {
        return "http://localhost:" + port + (serviceName.startsWith("/") ? serviceName : "/" + serviceName);
    }

    /**
     * Test for loading all images.
     */
    @Test
    public void shouldReturn200ForLoadAllImages() {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(getUrlForService("loadAllImages"), String.class);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Test for loading all images.
     */
    @Test
    public void shouldReturn404ForDownloadImageId1() {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(getUrlForService("download/1"), String.class);
        Assert.assertEquals(HttpStatus.valueOf(404), responseEntity.getStatusCode());
    }

    @Test
    public void contextLoads() {
    }


}
