package com.suryoday.CustomerIntraction.Pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_intraction_member")
public class CustomerIntractionMember {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String memberName;
	private long memberId;
	private String role;
	private long createdBy;
	private String meetingNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public String getMeetingNumber() {
		return meetingNumber;
	}

	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}

	@Override
	public String toString() {
		return "CustomerInteractionMember [id=" + id + ", memberName=" + memberName + ", memberId=" + memberId
				+ ", role=" + role + ", createdBy=" + createdBy + ", meetingNumber=" + meetingNumber + "]";
	}

}
