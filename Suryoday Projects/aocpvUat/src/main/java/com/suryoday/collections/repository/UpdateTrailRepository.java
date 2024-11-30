package com.suryoday.collections.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.collections.pojo.UpdateTrailBean;

@Repository
public interface UpdateTrailRepository extends JpaRepository<UpdateTrailBean, Integer> {

	@Query(value = "SELECT a from UpdateTrailBean a where a.accountNo =:accountNo")
	Optional<UpdateTrailBean> getLoanNumber(String accountNo);

}
