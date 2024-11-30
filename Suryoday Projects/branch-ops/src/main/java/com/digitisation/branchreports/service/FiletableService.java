package com.digitisation.branchreports.service;

import java.util.List;

import com.digitisation.branchreports.model.Filetable;

public interface FiletableService {

	public Filetable save(Filetable docuement);

	public void saveAll(List<Filetable> documents);

	public Filetable getFiletableById(Long reportId);

	public List<Filetable> getAll();

}
