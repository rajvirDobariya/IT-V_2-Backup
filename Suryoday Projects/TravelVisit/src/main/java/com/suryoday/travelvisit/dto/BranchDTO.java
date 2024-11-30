package com.suryoday.travelvisit.dto;

public class BranchDTO {
    private Long branchId;
    private String branchName;

    // Constructor
    public BranchDTO(Long branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    // Getters and Setters
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
