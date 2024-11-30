package com.digitisation.branchreports.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.model.Filetable;
import com.digitisation.branchreports.repository.FiletableRepository;
import com.digitisation.branchreports.service.FiletableService;

@Service
public class FiletableServiceImpl implements FiletableService {

	@Autowired
	private FiletableRepository filetableRepository;

	@Override
	public Filetable save(Filetable filetable) {
		return filetableRepository.save(filetable);
	}

	@Override
	public Filetable getFiletableById(Long id) {
		return filetableRepository.findById(id)
				.orElseThrow(() -> new DigitizationException("Document not foune with id :" + id));
	}

	@Override
	public List<Filetable> getAll() {
		return filetableRepository.findAll();
	}

	@Override
	public void saveAll(List<Filetable> documents) {
		filetableRepository.saveAll(documents);
	}

}
