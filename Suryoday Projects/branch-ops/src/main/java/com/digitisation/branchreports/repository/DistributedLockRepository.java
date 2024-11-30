package com.digitisation.branchreports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitisation.branchreports.model.DistributedLock;

public interface DistributedLockRepository extends JpaRepository<DistributedLock, String> {
}
