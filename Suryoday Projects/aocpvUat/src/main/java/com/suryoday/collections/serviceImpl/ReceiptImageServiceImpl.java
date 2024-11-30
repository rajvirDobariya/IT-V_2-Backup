package com.suryoday.collections.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.collections.pojo.ReceiptImage;
import com.suryoday.collections.repository.ReceiptImageRepoitory;
import com.suryoday.collections.service.ReceiptImageService;

@Service
public class ReceiptImageServiceImpl implements ReceiptImageService {

	@Autowired
	ReceiptImageRepoitory imageRepoitory;

	@Override
	public String saveImageReceipt(ReceiptImage images, MultipartFile files) {

		Optional<ReceiptImage> optional = imageRepoitory.getByReceipt(images.getReceiptNo());
		if (optional.isPresent()) {
			ReceiptImage rcptImg = optional.get();
			images.setId(rcptImg.getId());
		}
		byte[] byteArr = new byte[0];
		byte[] bytes = new byte[0];
		try {
			byteArr = files.getBytes();
			InputStream inputStream = new ByteArrayInputStream(byteArr);
			bytes = IOUtils.toByteArray(inputStream);
			System.out.println("Input Stream  " + inputStream);
			System.out.println("Bytes  " + byteArr);
			System.out.println("Bytes 2  " + bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		images.setImages(bytes);

		imageRepoitory.save(images);
		return "Image Saved Successfully !!!!!!!!!!!!!!!!!!";
	}

	@Override
	public List<ReceiptImage> findAllDetails2(String receiptNo) {

		List<ReceiptImage> list = imageRepoitory.findDetailsReceipt2(receiptNo);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

}
