package com.suryoday.connector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.connector.pojo.PageGroup;

@Repository
public interface PageGroupRepository extends JpaRepository<PageGroup, Long>{

}
