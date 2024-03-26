package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.CommunitySharing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunitySharingRepository extends JpaRepository<CommunitySharing, Long> {
}
