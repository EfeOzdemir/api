package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import com.plantapp.api.core.model.CSharingDto;
import com.plantapp.api.core.model.UserDto;
import com.plantapp.api.core.model.request.NewSharingRequest;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
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

    @PersistenceContext
    private final EntityManager entityManager;
    private final CloudStorageService cloudStorageService;
    private final CommunitySharingRepository communitySharingRepository;

    public List<CSharingDto> getAllCommunitySharing(String userId) {
        return communitySharingRepository.findAllSharing(userId);
    }

    @Transactional
    public CSharingDto getCommunitySharing(Long id, String userId) {
        CommunitySharing communitySharing = communitySharingRepository.findById(id)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing not found!"));;
        return CSharingDto.of(communitySharing, entityManager.getReference(User.class, userId));
    }

    public List<UserDto> getUsersWhoLikeCommunitySharingById(Long id) {
        if(!communitySharingRepository.existsById(id))
            throw new CommunitySharingNotFoundException("Community sharing with id not found!");
        return communitySharingRepository.findUsersWhoLike(id);
    }

    public CSharingDto createCommunitySharing(NewSharingRequest newSharingRequest) {
        try {
            String imgUrl = cloudStorageService.upload(newSharingRequest.image(), newSharingRequest.title());
            CommunitySharing communitySharing =
                    CommunitySharing.builder()
                            .title(newSharingRequest.title())
                            .content(newSharingRequest.content())
                            .imageUrl(imgUrl).build();
            communitySharing = communitySharingRepository.save(communitySharing);
            return CSharingDto.ofWithDefaults(communitySharing);
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

}
