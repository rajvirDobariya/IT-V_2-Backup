package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="TwoWheeler_PaymentDetails")
public class TwowheelerPaymentDetails {
	@Id
	private String paymentId;
	private String applicationId;
	private String paymentStatus;
	@Lob
	private String paymentRequest;
	@Lob
	private String paymentResponse;
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(String paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
	public String getPaymentResponse() {
		return paymentResponse;
	}
	public void setPaymentResponse(String paymentResponse) {
		this.paymentResponse = paymentResponse;
	}
	@Override
	public String toString() {
		return "TwowheelerPaymentDetails [paymentId=" + paymentId + ", applicationId=" + applicationId
				+ ", paymentStatus=" + paymentStatus + ", paymentRequest=" + paymentRequest + ", paymentResponse="
				+ paymentResponse + "]";
	}
	
	
}
