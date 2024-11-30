package com.suryoday.aocpv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}
