package de.pkdevel.shitc0inz.model;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolumeService {
	
	private final VolumeRepository repo;
	
	@Autowired
	public VolumeService(final VolumeRepository repo) {
		this.repo = repo;
	}
	
	public Collection<Volume> findAllSymbols() {
		return this.repo.findAll();
	}
}
