package com.plantapp.api.core.service;

import com.plantapp.api.core.dto.NewSharingRequest;
import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.repository.CommunitySharingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunitySharingService {
    private final ImageService imageService;
    private final CommunitySharingRepository communitySharingRepository;


    public void share(NewSharingRequest newSharingRequest) {

        if(newSharingRequest.image().isEmpty())
            throw new RuntimeException();

        try {
            String imgUrl = imageService.saveImage(newSharingRequest.title(), newSharingRequest.image());
            CommunitySharing communitySharing =
                    CommunitySharing.builder()
                            .title(newSharingRequest.title())
                            .content(newSharingRequest.content())
                            .imageUrl(imgUrl)
                            .build();
            communitySharing = communitySharingRepository.save(communitySharing);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}
