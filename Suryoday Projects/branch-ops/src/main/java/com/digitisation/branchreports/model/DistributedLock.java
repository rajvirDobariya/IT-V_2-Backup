package com.digitisation.branchreports.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "distributed_lock")
public class DistributedLock {

    @Id
    private String name;

    private LocalDateTime lockUntil;
    private LocalDateTime lockedAt;
    private String lockedBy;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getLockUntil() {
		return lockUntil;
	}
	public void setLockUntil(LocalDateTime lockUntil) {
		this.lockUntil = lockUntil;
	}
	public LocalDateTime getLockedAt() {
		return lockedAt;
	}
	public void setLockedAt(LocalDateTime lockedAt) {
		this.lockedAt = lockedAt;
	}
	public String getLockedBy() {
		return lockedBy;
	}
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

    // Getters and Setters
    
    
}
