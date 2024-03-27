package com.plantapp.api.core.controller;

import com.plantapp.api.core.dto.APIResponse;
import com.plantapp.api.core.dto.NewSharingRequest;
import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.service.CommunitySharingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Community API", description = "Community sharing api endpoints.")
@RestController
@RequestMapping(path = "/community")
@RequiredArgsConstructor
public class CommunitySharingController {

    private final CommunitySharingService communitySharingService;

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Creates a new community sharing.")
    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CommunitySharing> saveCommunitySharing(@ModelAttribute @Valid NewSharingRequest newSharingRequest) {
        return new APIResponse<>(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), communitySharingService.createCommunitySharing(newSharingRequest));
    }

    @Operation(summary = "Gets all community sharings.")
    @GetMapping(path = "/")
    public APIResponse<List<CommunitySharing>> getAllCommunitySharing() {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), communitySharingService.getAllCommunitySharing());
    }

    @Operation(summary = "Gets a community sharing with id.")
    @GetMapping(value = "/{id}")
    public APIResponse<CommunitySharing> getCommunitySharing(@NotNull @PathVariable Long id) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), communitySharingService.getCommunitySharing(id));
    }

    @Operation(summary = "Get users who likes the community sharing with id.")
    @GetMapping(value = "/{id}/likes")
    public APIResponse<List<User>> getLikesOfCommunitySharing(@NotNull @PathVariable Long id) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                communitySharingService.getCommunitySharingLikedBys(id));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Likes a community sharing if it is not liked, unlikes if it is liked.")
    @PostMapping(value = "/{id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeCommunitySharing(@NotNull @PathVariable Long id) {
        communitySharingService.likeCommunitySharing(id);
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Deletes a community sharing with id.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommunitySharing(@NotNull @PathVariable Long id) {
        communitySharingService.deleteCommunitySharing(id);
    }
}
