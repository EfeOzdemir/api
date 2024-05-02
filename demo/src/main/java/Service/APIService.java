package Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {

    private final RestTemplate restTemplate;

    public APIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchDataFromAPI(String endpoint) {
        return restTemplate.getForObject(endpoint, String.class);
    }
}