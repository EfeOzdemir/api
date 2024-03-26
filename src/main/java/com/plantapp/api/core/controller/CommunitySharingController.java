package com.plantapp.api.core.controller;

import com.plantapp.api.core.dto.NewSharingRequest;
import com.plantapp.api.core.service.CommunitySharingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "community")
@RequiredArgsConstructor
public class CommunitySharingController {

    private final CommunitySharingService communitySharingService;

    @PostMapping(value = "share")
    public void share(@ModelAttribute @Valid NewSharingRequest newSharingRequest) {
        communitySharingService.share(newSharingRequest);
    }

}
