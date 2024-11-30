package com.suryoday.collections.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.collections.pojo.ReceiptImage;

@Repository
public interface ReceiptImageRepoitory extends JpaRepository<ReceiptImage, Integer>{

	@Query(value = "SELECT a from ReceiptImage a where a.receiptNo =:receipt")
	Optional<ReceiptImage> getByReceipt(String receipt);

	@Query(value = "SELECT a from ReceiptImage a where a.receiptNo =:receiptNo")
	List<ReceiptImage> findDetailsReceipt2(String receiptNo); 

}
