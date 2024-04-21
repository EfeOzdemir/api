package com.plantapp.api.core.controller;

import com.plantapp.api.core.model.dto.CSharingDto;
import com.plantapp.api.core.model.request.NewSharingRequest;
import com.plantapp.api.core.model.response.APIResponse;
import com.plantapp.api.core.model.response.PagedResponse;
import com.plantapp.api.core.service.CommunitySharingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@Tag(name = "Community API", description = "Community sharing api endpoints.")
@RestController
@RequestMapping(path = "/community")
@RequiredArgsConstructor
public class CommunitySharingController {

    private final CommunitySharingService communitySharingService;

    @Operation(summary = "Gets all community sharings.")
    @GetMapping(path = "/")
    public APIResponse<PagedResponse<CSharingDto>> getAllCommunitySharing(@Nullable @RequestParam("user_id") String userId, Pageable pageable) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), communitySharingService.getAllCommunitySharing(pageable, userId));
    }

    @Operation(summary = "Gets a community sharing with id.")
    @GetMapping(value = "/{id}")
    public APIResponse<CSharingDto> getCommunitySharing(@NotNull @PathVariable Long id, @Nullable @RequestParam("user_id") String userId) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), communitySharingService.getCommunitySharingById(id, userId));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Creates a new community sharing.")
    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CSharingDto> saveCommunitySharing(@ModelAttribute @Valid NewSharingRequest newSharingRequest) {
        return new APIResponse<>(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), communitySharingService.createCommunitySharing(newSharingRequest));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Deletes a community sharing with id.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommunitySharing(@NotNull @PathVariable Long id) {
        communitySharingService.deleteCommunitySharingById(id);
    }
}
