package com.plantapp.api.core.model.projections;

import java.time.Instant;

public interface CommunitySharingProjection {

    Long getId();
    String getTitle();
    String getContent();
    UserProjection getCreatedBy();
    String getImageUrl();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    Integer getLikeCount();
}
