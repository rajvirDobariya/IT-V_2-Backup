package com.suryoday.dsaOnboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DeviationDetailsResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;

@Repository
public interface DsaOnboardMemberRepository extends JpaRepository<DsaOnboardMember, Long> {

	@Query(value = "SELECT a from DsaOnboardMember a where a.applicationNo =:applicationNo and a.member =:member")
	Optional<DsaOnboardMember> getByApplicationNoAndMember(long applicationNo, String member);

	@Query(value = "SELECT a from DsaOnboardMember a where a.applicationNo =:applicationno")
	List<DsaOnboardMember> getByApplicationNo(long applicationno);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DeviationDetailsResponse(l.name, l.member, l.nameMatch, l.faceMatch, l.nameMatchPercent, l.faceMatchPercent, l.ekycVerify, l.ekycDoneBy, l.gstNo, l.gstNoVerify, l.aadharNo, l.aadharNoVerify,l.panCardNo, l.panCardVerify) from DsaOnboardMember l where l.applicationNo =:applicationNo")
	List<DeviationDetailsResponse> getDeviationDetails(long applicationNo);

	@Query(value = "SELECT a from DsaOnboardMember a where a.applicationNo =:applicationno and a.isPrimaryMember =:isPrimary")
	Optional<DsaOnboardMember> findByApplicationNoAndIsPrimary(long applicationno, String isPrimary);

}
