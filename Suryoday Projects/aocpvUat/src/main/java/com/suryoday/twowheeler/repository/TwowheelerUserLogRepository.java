package com.suryoday.twowheeler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerUserLog;

@Repository
public interface TwowheelerUserLogRepository extends JpaRepository<TwowheelerUserLog, Long> {

	List<TwowheelerUserLog> getByApplicationNo(String applicationNo);

}
