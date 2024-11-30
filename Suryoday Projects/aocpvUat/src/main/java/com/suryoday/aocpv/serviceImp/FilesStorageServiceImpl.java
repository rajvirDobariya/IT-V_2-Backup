package com.suryoday.aocpv.serviceImp;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.FileDB;
import com.suryoday.aocpv.repository.FileDBRepository;
import com.suryoday.aocpv.service.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	@Autowired
	private FileDBRepository fileDBRepository;

	@Override
	public FileDB store(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB FileDB = null;
		try {
			FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileDBRepository.save(FileDB);
	}

	@Override
	public FileDB getFile(String id) {
		return fileDBRepository.findById(id).get();
	}

	@Override
	public Stream<FileDB> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}

}
