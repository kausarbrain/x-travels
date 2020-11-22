package com.xtravels.repository;
import com.xtravels.models.Location;
import com.xtravels.models.Post;
import com.xtravels.models.PrivacyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByOrderByNameAsc();

    Optional<Location> findByName(String name);

}
