package com.suryoday.aocpv.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.suryoday.aocpv.pojo.FileDB;
import com.suryoday.aocpv.pojo.ResponseFile;
import com.suryoday.aocpv.pojo.ResponseMessage;
import com.suryoday.aocpv.service.FilesStorageService;

@RestController
@RequestMapping({"/aocp/files1"})
public class FilesController {
  @Autowired
  FilesStorageService storageService;
  
  @PostMapping({"/upload"})
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      this.storageService.store(file);
      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    } 
  }
  
  @GetMapping({"/files"})
  public ResponseEntity<List<ResponseFile>> getListFiles() {
    List<ResponseFile> allfiles = new ArrayList<>();
    Stream<FileDB> files = this.storageService.getAllFiles();
    for (Iterator<FileDB> name = files.iterator(); name.hasNext(); ) {
      FileDB dbFile = name.next();
      byte[] b = dbFile.getData();
      String encoded = Base64.getEncoder().encodeToString(b);
      System.out.println(encoded);
      System.out.println(dbFile.getId());
      System.out.println(dbFile.getName());
      String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(dbFile.getId()).toUriString();
      ResponseFile r = new ResponseFile();
      r.setName(dbFile.getName());
      r.setUrl(encoded);
      r.setSize((dbFile.getData()).length);
      r.setType(dbFile.getType());
      allfiles.add(r);
    } 
    System.out.println(allfiles.size());
    System.out.println(allfiles.toString());
    return ResponseEntity.status(HttpStatus.OK).body(allfiles);
  }
  
  @GetMapping({"/files/{id}"})
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    FileDB fileDB = this.storageService.getFile(id);
    return ((ResponseEntity.BodyBuilder)ResponseEntity.ok()
      .header("Content-Disposition", new String[] { "attachment; filename=\"" + fileDB.getName() + "\"" })).body(fileDB.getData());
  }
}
