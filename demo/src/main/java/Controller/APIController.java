package Controller;

import Service.APIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    private final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetchData")
    public void fetchData() {

        String[] endpoints = {
                "http://localhost:8080/api/data1",
                "http://localhost:8080/api/data2",
                "http://localhost:8080/api/data3"
        };

        // her endpointten data çekme
        for (String endpoint : endpoints) {
            String responseData = apiService.fetchDataFromAPI(endpoint);
            System.out.println("Response from " + endpoint + ":");
            System.out.println(responseData);
        }
    }
}