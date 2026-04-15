package com.ng.runz.repository;

import com.ng.runz.model.Runs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends JpaRepository<Runs,Long>, PagingAndSortingRepository<Runs,Long> {
    Page<Runs> findAllByUserId(Long userId, Pageable pageable);
}
