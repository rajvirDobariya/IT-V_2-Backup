package com.suryoday.dsaOnboard.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaImage;

@Repository
public interface DsaImageRepository extends JpaRepository<DsaImage, Long>{

	@Query(value = "SELECT a from DsaImage a where a.applicationNo =:applicationno and a.documenttype =:Documenttype")
	Optional<DsaImage> getByApplicationNoAndDocumentType(String applicationno, String Documenttype);

	@Query(value = "SELECT a from DsaImage a where a.applicationNo =:applicationNo")
	List<DsaImage> getByApplicationNo(String applicationNo);

	@Query(value = "select * from [dsa_image_table] where [application_no] =:applicationNo and [member] =:member and [documenttype] in ('customerPhoto','ekyc_photo')",nativeQuery = true)
	List<DsaImage> getByApplicationNoAndMember(String applicationNo, String member);

	@Query(value = "SELECT a from DsaImage a where a.applicationNo =:applicationno and a.documenttype =:Documenttype and a.member =:member1")
	Optional<DsaImage> getByApplicationNoAndDocumentTypeAndMember(String applicationno, String Documenttype,
			String member1);

	@Query(value = "SELECT a from DsaImage a where a.applicationNo =:applicationno and a.documenttype =:Documenttype")
	List<DsaImage> fetchByDocumentType(String applicationno, String Documenttype);

	@Query(value = "SELECT a.documenttype from DsaImage a where a.applicationNo =:applicationno ")
	List<String> getDocumentTypes(String applicationno);
	
	@Query(value = "SELECT a.documenttype from DsaImage a where a.applicationNo =:applicationNo")
	Set<String> getDocumentTypesWeb(String applicationNo);
}
