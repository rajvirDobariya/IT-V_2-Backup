package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerImage;

@Repository
public interface TwowheelerImageRepository extends JpaRepository<TwowheelerImage, Integer> {

	@Query(value = "SELECT a from TwowheelerImage a where a.applicationNo =:applicationno and a.documenttype =:Documenttype")
	List<TwowheelerImage> getByApplicationNoAndDocumentType(String applicationno, String Documenttype);

	@Query(value = "SELECT top 1 a.id from two_wheeler_image_table a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from TwowheelerImage a where a.applicationNo =:applicationNo and a.member =:member")
	List<TwowheelerImage> getByAppNoAndMember(String applicationNo, String member);

	@Query(value = "SELECT a from TwowheelerImage a where a.applicationNo =:applicationNo and a.documenttype =:documentType")
	List<TwowheelerImage> getByAppNoAndMemberAndDocument(String applicationNo, String documentType);

	@Query(value = "select * from [two_wheeler_image_table] where [application_no] =:applicationNo and [member] in ('LOAN_DOCUMENTS_COMPULSORY','LOAN_DOCUMENTS_OPTIONAL')", nativeQuery = true)
	List<TwowheelerImage> getByAppNoAndMemberWeb(String applicationNo);

	@Query(value = "select * from [two_wheeler_image_table] where [application_no] =:applicationNo and [member] in ('SELF_COMPULSORY','SELF_OPTIONAL')", nativeQuery = true)
	List<TwowheelerImage> getSelfdocumentWeb(String applicationNo);

	@Query(value = "select [documenttype] from [two_wheeler_image_table] where [application_no] =:applicationNo and [member] in ('LOAN_DOCUMENTS_COMPULSORY','LOAN_DOCUMENTS_OPTIONAL','SELF_COMPULSORY')", nativeQuery = true)
	List<String> getDocumentTypes(String applicationNo);

	@Query(value = "select [documenttype] from [two_wheeler_image_table] where [application_no] =:applicationNo and [member] in ('LOAN_DOCUMENTS_COMPULSORY','LOAN_DOCUMENTS_OPTIONAL','SELF_COMPULSORY')", nativeQuery = true)
	Set<String> getDocumentTypesWeb(String applicationNo);

	@Query(value = "SELECT a from TwowheelerImage a where a.applicationNo =:applicationNo and a.documenttype =:documenttype")
	Optional<TwowheelerImage> getByApplicationNoAnddocumentType(String applicationNo, String documenttype);

	@Modifying
	@Transactional
	@Query(value = "delete from TwowheelerImage where applicationNo =:applicationno and imageName =:imagename")
	void deleteImage(String applicationno, String imagename);

}
