package com.suryoday.twowheeler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerBankCode;

@Repository
public interface TwowheelerBankCodeRepository extends JpaRepository<TwowheelerBankCode, Long> {

}
