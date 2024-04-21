package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import com.plantapp.api.core.model.dto.CSharingDto;
import com.plantapp.api.core.model.request.NewSharingRequest;
import com.plantapp.api.core.model.response.PagedResponse;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommunitySharingService {

    private final CloudStorageService cloudStorageService;
    private final CommunitySharingRepository communitySharingRepository;

    public PagedResponse<CSharingDto> getAllCommunitySharing(Pageable pageable, String userId) {
        Page<CSharingDto> paged = communitySharingRepository.findAllSharing(userId, pageable);
        return PagedResponse.<CSharingDto>builder()
                .content(paged.getContent())
                .currentPage(paged.getNumber())
                .pageSize(paged.getSize())
                .totalPages(paged.getTotalPages())
                .hasNext(paged.getTotalPages() > (paged.getNumber() + 1))
                .build();
    }

    public CSharingDto getCommunitySharingById(Long postId, String userId) {
        return communitySharingRepository.findById(postId, userId)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing not found!"));
    }

    public CSharingDto createCommunitySharing(NewSharingRequest newSharingRequest) {
        try {
            String imageKey = cloudStorageService.generateRandomKey();
            String imageURL = cloudStorageService.generatePublicUrl(imageKey);

            CompletableFuture<Boolean> uploadStatus = cloudStorageService.upload(newSharingRequest.image(), imageKey);
            CommunitySharing communitySharing = CommunitySharing.builder()
                    .title(newSharingRequest.title()).content(newSharingRequest.content()).imageUrl(imageURL).build();
            communitySharing = communitySharingRepository.save(communitySharing);

            if (uploadStatus.join())
                return CSharingDto.fromCommunitySharing(communitySharing);
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCommunitySharingById(Long id) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer res = communitySharingRepository.deleteByIdAndCreatedById(id, user);

        if (res == 0) {
            throw new AccessDeniedException("Access denied. Unauthorized access.");
        }
    }

}
