package Repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository<DataEntity> extends JpaRepository<DataEntity, Long> {
}
