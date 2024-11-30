package com.suryoday.aocpv.repository;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpvImages;

@Repository
public interface AocpvImageRepository extends JpaRepository<AocpvImages, Integer> {

	List<AocpvImages> findByApplicationNo(long applicationNo);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.flowStatus =:flowStatus")
	Optional<AocpvImages> getByApplicationNoAndStatus(long applicationNo, String flowStatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvImages set images =:images where applicationNo =:applicationNo and flowStatus =:flowStatus")
	void updateImage(long applicationNo, String flowStatus, Blob images);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.documenttype =:Documenttype")
	Optional<AocpvImages> getByApplicationNoAndName(long applicationNo, String Documenttype);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvImages set images =:images where applicationNo =:applicationNo and imageName =:imageName")
	void updateImageByName(long applicationNo, String imageName, Blob images);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.documenttype =:documenttype and a.member =:member")
	Optional<AocpvImages> getByApplicationNoMember(long applicationNo, String documenttype, String member);

	@Query(value = "SELECT top 1 a.id from aocpv_images a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a.GeoLocation from AocpvImages a where a.applicationNo =:applicationNo and a.documenttype =:documenttype")
	public Optional<String> getGeoLoactionByAppln(long applicationNo, String documenttype);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.member =:member and a.flowStatus =:flowStatus")
	List<AocpvImages> getByApplicationNoAndMember(long applicationNo, String member, String flowStatus);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.flowStatus =:flowStatus")
	List<AocpvImages> getByApplicationNoAndflowStatus(long applicationNo, String flowStatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvImages set versioncode =:versioncode where applicationNo =:applicationNoInLong")
	void updateVersionCode(long applicationNoInLong, int versioncode);

	@Query(value = "select * from  [aocpv_images_data_history] where [application_no] =:applicationNo and [versioncode] =:versioncode and [member] =:member and flow_status='ID'", nativeQuery = true)
	List<AocpvImages> getByappAndVersion(long applicationNo, String member, int versioncode);

	@Query(value = "select * from  [aocpv_images_data_history] where [application_no] =:applicationNo and flow_status in ('PD','UD') and [versioncode] =:versioncode", nativeQuery = true)
	List<AocpvImages> getByapplicationAndVersion(long applicationNo, int versioncode);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNo and flow_status in ('PD','UD')", nativeQuery = true)
	List<AocpvImages> fetchByAppl(long applicationNo);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNO and [documenttype] in ('customerPhoto','cifPdf','ekyc_aadhar','accountOpenpdf','panCardPhoto')", nativeQuery = true)
	List<AocpvImages> getImageBydocType(long applicationNO);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNoInLong and [documenttype] in ('customerPhoto','ekyc_photo')", nativeQuery = true)
	List<AocpvImages> getImageforComparison(long applicationNoInLong);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNoInLong and [documenttype] in ('customerPhoto','ekyc_photo','panCardPhoto','aadharFront','aadharBack','form60Photo') and [member]='SELF'", nativeQuery = true)
	List<AocpvImages> fetchSelfImage(long applicationNoInLong);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNoInLong and [documenttype] in ('panCardPhoto','aadharFront','form60Photo','aadharBack') and [member] =:member", nativeQuery = true)
	List<AocpvImages> fetchfatherImage(long applicationNoInLong, String member);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNoInLong and [documenttype] in ('utilityBillPhoto','houseImageInside','houseImageOutside','buisnessPhoto')", nativeQuery = true)
	List<AocpvImages> fetchOtherImage(long applicationNoInLong);

	@Query(value = "SELECT distinct a.member from AocpvImages a where a.applicationNo =:applicationNoInLong and a.member !=null")
	List<String> fetchMemberName(long applicationNoInLong);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNO and [documenttype] in ('customerPhoto','houseImageInside','houseImageOutside','AO_Selfie','RO_Selfie','buisnessPhoto')", nativeQuery = true)
	List<AocpvImages> fetchImageforOnePager(long applicationNO);

	@Query(value = "SELECT a from AocpvImages a where a.applicationNo =:applicationNo and a.documenttype =:Documenttype")
	List<AocpvImages> getByApplicationNoAndDocument(long applicationNo, String Documenttype);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNo and [documenttype] in ('Loan_Agreement_Verify','Sanction_letter_verify')", nativeQuery = true)
	List<AocpvImages> getsanctionLetterAndagreement(String applicationNo);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNo and [documenttype] in ('buisnessPhoto','buisnessPhoto_1')", nativeQuery = true)
	List<AocpvImages> fetchBuisnessPhoto(long applicationNo);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNo and [documenttype] in ('utilityBillPhoto','utilityBillPhoto_1')", nativeQuery = true)
	List<AocpvImages> fetchUtilityBillPhoto(long applicationNo);

	@Query(value = "select * from [aocpv_images] where [application_no] =:applicationNoInLong and [documenttype] in ('utilityBillPhoto','houseImageInside','houseImageOutside')", nativeQuery = true)
	List<AocpvImages> fetchUDImage(long applicationNoInLong);
}
