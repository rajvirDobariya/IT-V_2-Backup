package com.suryoday.goNogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.goNogo.pojo.GoNoGoImages;

@Repository
public interface GoNoGoImagesRepository extends JpaRepository<GoNoGoImages, Integer> {

}
