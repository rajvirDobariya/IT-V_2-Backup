package com.suryoday.dsaOnboard.service;

public interface DsaOnBoardPdfService {

	String connectorServiceAgreementPdf(StringBuilder htmlString, String applicationNo);

	String dsaAgreementPdf(StringBuilder htmlString, String applicationNo);

}
