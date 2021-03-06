package uk.ac.ebi.spot.goci.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

/**
 * Created by emma on 27/04/2016.
 *
 * @author emma
 *         <p>
 *         Simple util class to build a url
 */
@Service
public class RestUrlBuilder {

    @NotNull @Value("${ensembl.server}")
    private String server;

    public String createUrl(String endpoint, String query) {
        String url = getServer().concat(endpoint).concat(query);
        return url;
    }

    public RestTemplate getRestTemplate() {
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson message converter
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        return restTemplate;
    }

    public String getServer() {
        return server;
    }
}
