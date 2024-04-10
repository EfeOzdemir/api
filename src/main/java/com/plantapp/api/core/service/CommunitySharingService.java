package com.plantapp.api.core.service;

import com.plantapp.api.core.dto.NewSharingRequest;
import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunitySharingService {

    private final CloudStorageService cloudStorageService;
    private final EntityManager entityManager;
    private final CommunitySharingRepository communitySharingRepository;

    public List<CommunitySharing> getAllCommunitySharing() {
        return communitySharingRepository.findAll();
    }

    public CommunitySharing getCommunitySharing(Long id) {
        return communitySharingRepository.findById(id).orElseThrow(() ->
                new CommunitySharingNotFoundException("Community sharing with id not found!"));
    }

    public List<User> getCommunitySharingLikedBys(Long id) {
        CommunitySharing communitySharing = communitySharingRepository
                .findById(id).orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));
        return communitySharing.getLikedBy().stream().toList();
    }

    public CommunitySharing createCommunitySharing(NewSharingRequest newSharingRequest) {
        try {
            String imgUrl = cloudStorageService.upload(newSharingRequest.image(), newSharingRequest.title());
            CommunitySharing communitySharing =
                    CommunitySharing.builder()
                            .title(newSharingRequest.title())
                            .content(newSharingRequest.content())
                            .imageUrl(imgUrl).build();
            return communitySharingRepository.save(communitySharing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void likeCommunitySharing(Long id) {
        Optional<CommunitySharing> communitySharingOptional = communitySharingRepository.findById(id);
        CommunitySharing communitySharing = communitySharingOptional
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));

        User user = entityManager.getReference(User.class, SecurityContextHolder.getContext().getAuthentication().getName());

        if(communitySharing.isLikedBy(user)) communitySharing.likeBy(user);
        else communitySharing.unlikeBy(user);
    }

    public void deleteCommunitySharing(Long id) {
        CommunitySharing communitySharing = communitySharingRepository.findById(id)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));
        if(communitySharing.getCreatedBy().getId().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            communitySharingRepository.deleteById(id);
        throw new AccessDeniedException("Access denied!");
    }

}
