package com.suryoday.twowheeler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerUserLog;

@Repository
public interface TwowheelerUserLogRepository extends JpaRepository<TwowheelerUserLog, Long>{

}
