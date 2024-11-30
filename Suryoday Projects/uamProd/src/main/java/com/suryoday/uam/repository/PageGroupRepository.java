package com.suryoday.uam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.uam.pojo.PageGroup;

@Repository
public interface PageGroupRepository extends JpaRepository<PageGroup, Long>{

	public Optional<PageGroup> findIdByPageName(String pageName);
}
