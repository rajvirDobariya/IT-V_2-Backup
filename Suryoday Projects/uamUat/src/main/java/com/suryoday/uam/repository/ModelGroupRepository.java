package com.suryoday.uam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.uam.pojo.ModelGroup;

@Repository
public interface ModelGroupRepository extends JpaRepository<ModelGroup, Long>{

	public List<ModelGroup> findByModelGroupName(String modelGroupName);
}
