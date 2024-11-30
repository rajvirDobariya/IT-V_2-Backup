package com.suryoday.aocpv.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.FileDB;

@Service
public interface FilesStorageService {

	public FileDB store(MultipartFile file);

	public FileDB getFile(String id);

	public Stream<FileDB> getAllFiles();

}
