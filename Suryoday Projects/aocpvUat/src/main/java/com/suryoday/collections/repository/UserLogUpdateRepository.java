package com.suryoday.collections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.collections.pojo.UserlogUpdate;

@Repository
public interface UserLogUpdateRepository extends JpaRepository<UserlogUpdate, Integer> {

}
