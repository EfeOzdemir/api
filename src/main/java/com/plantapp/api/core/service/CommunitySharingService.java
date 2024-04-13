package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import com.plantapp.api.core.model.projections.CommunitySharingProjection;
import com.plantapp.api.core.model.projections.UserProjection;
import com.plantapp.api.core.model.request.NewSharingRequest;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunitySharingService {

    private final CloudStorageService cloudStorageService;
    private final EntityManager entityManager;
    private final CommunitySharingRepository communitySharingRepository;

    public List<CommunitySharingProjection> getAllCommunitySharing(String userId) {
        User userRef = getUserReference(userId);
        return communitySharingRepository.findAllListing(userRef);
    }

    public CommunitySharingProjection getCommunitySharing(Long id, String userId) {
        User userRef = getUserReference(userId);
        return communitySharingRepository.findByIdWithProjection(id, userRef)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing not found!"));
    }

    public List<UserProjection> getUsersWhoLikeCommunitySharingById(Long id) {
        if(!communitySharingRepository.existsById(id))
            throw new CommunitySharingNotFoundException("Community sharing with id not found!");
        return communitySharingRepository.findUsersWhoLikeById(id);
    }

    public CommunitySharingProjection createCommunitySharing(NewSharingRequest newSharingRequest) {
        try {
            String imgUrl = cloudStorageService.upload(newSharingRequest.image(), newSharingRequest.title());
            CommunitySharing communitySharing =
                    CommunitySharing.builder()
                            .title(newSharingRequest.title())
                            .content(newSharingRequest.content())
                            .imageUrl(imgUrl).build();
            communitySharing = communitySharingRepository.save(communitySharing);
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            return pf.createProjection(CommunitySharingProjection.class, communitySharing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void likeCommunitySharing(Long id) {
        Optional<CommunitySharing> communitySharingOptional = communitySharingRepository.findById(id);
        CommunitySharing communitySharing = communitySharingOptional
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = entityManager.getReference(User.class, userId);

        if(communitySharing.isLikedBy(user))
            communitySharing.unlikeBy(user);
        else communitySharing.likeBy(user);
    }

    public void deleteCommunitySharing(Long id) {
        CommunitySharing communitySharing = communitySharingRepository.findById(id)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));
        if(communitySharing.getCreatedBy().getId().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            communitySharingRepository.deleteById(id);
        else
            throw new AccessDeniedException("Access denied!");
    }

    private User getUserReference(String userId) {
        return userId != null ? entityManager.getReference(User.class, userId) : null;
    }

}
