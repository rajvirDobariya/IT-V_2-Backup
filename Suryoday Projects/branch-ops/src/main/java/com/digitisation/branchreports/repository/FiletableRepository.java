package com.digitisation.branchreports.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.model.Filetable;

@Transactional
@Repository
public interface FiletableRepository extends JpaRepository<Filetable, Long> {

}
