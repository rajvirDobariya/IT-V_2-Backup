package com.suryoday.twowheeler.service;

import org.json.JSONObject;

public interface TWPDFService {

	public String sanctionPdf(StringBuilder htmlString, String applicationNo);

	public String loanApplicationFormPdf(StringBuilder htmlString, String applicationNo);

	public JSONObject sendMedia(JSONObject jsonObject, JSONObject Header);

	public String loanAgreementPdf(StringBuilder htmlString, String applicationNo);

}
