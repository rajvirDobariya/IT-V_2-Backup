package com.digitisation.branchreports.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_lock")
public class SchedulerLock {

	@Id
	@Column(name = "lock_name", nullable = false)
	private String lockName;

	@Column(name = "locked_at", nullable = false)
	private LocalDateTime lockedAt;

	@Column(name = "locked_by", nullable = false)
	private String lockedBy;

	public SchedulerLock() {
	}

	// Constructor with parameters
	public SchedulerLock(String lockName, LocalDateTime lockedAt, String lockedBy) {
		this.lockName = lockName;
		this.lockedAt = lockedAt;
		this.lockedBy = lockedBy;
	}

	// Getters and Setters
	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
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
}
