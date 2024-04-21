package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import com.plantapp.api.core.model.dto.UserDto;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import com.plantapp.api.core.repository.LikeRepository;
import com.plantapp.api.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommunitySharingRepository communitySharingRepository;

    public List<UserDto> getUsersWhoLikeCommunitySharingById(Long id) {
        if(!communitySharingRepository.isExistsById(id))
            throw new CommunitySharingNotFoundException("Community sharing with id not found!");
        return likeRepository.getUsersWhoLike(id);
    }

    @Transactional(readOnly = true)
    public void likeCommunitySharingById(Long id) {
        CommunitySharing communitySharing = communitySharingRepository.findLikesById(id)
                .orElseThrow(() -> new CommunitySharingNotFoundException("Community sharing with id not found!"));

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).get();

        if(communitySharing.isLikedBy(user)) communitySharing.unlikeBy(user);
        else communitySharing.likeBy(user);
    }
}
