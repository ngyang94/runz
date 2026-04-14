package com.ng.runz.repository;

import com.ng.runz.model.Runs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RunRepository extends JpaRepository<Runs,Long> {
    Collection<Runs> findAllByUserId(Long userId);
}
