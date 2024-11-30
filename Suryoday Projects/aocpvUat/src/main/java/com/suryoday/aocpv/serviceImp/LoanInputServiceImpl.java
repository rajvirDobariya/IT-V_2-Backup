package com.suryoday.aocpv.serviceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.poi.EmptyFileException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.excelToDatabase.ExcelToJava;
import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.LoanDetails;
import com.suryoday.aocpv.pojo.PreApprovedList;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.repository.PreApprovedListVikasLoanRepo;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@Service
public class LoanInputServiceImpl implements LoanInputService {

	@Autowired
	private PreApprovedListVikasLoanRepo loanInputRepositery;

	private static Logger logger = LoggerFactory.getLogger(LoanInputServiceImpl.class);

	@Override
	public void save(MultipartFile file) {

		try {

			List<LoanDetails> loanDetails = ExcelToJava.convertExcelToList(file.getInputStream());
//			loanInputRepositery.saveAll(loanDetails);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<PreApprovedListVikasLoan> getAllProduct() {
		return loanInputRepositery.findAll();
	}

	@Override
	public List<LoanDetail> getByIdAndstatus(long branchId, String status, String productCode) {
		if (branchId == 0 || status == null) {
			throw new EmptyFileException();
		}

		return loanInputRepositery.getByIdAndStatus(branchId, status, productCode)
				.orElseThrow(() -> new NoSuchElementException("list is empty"));

	}

	@Override
	public LoanDetail getByCustomerId(long customerId, String status) {
		if (customerId == 0) {
			throw new EmptyFileException();
		}
//		Optional<LoanDetail> optional=null;
//		try {
//		 optional = loanInputRepositery.getByCustomerId(customerId);
//		}
//		catch (IncorrectResultSizeDataAccessException e) {
//			
//			List<LoanDetail> list = loanInputRepositery.getByCustId(customerId);
//			for(LoanDetail details :list) {
//			if(status.equalsIgnoreCase(details.getStatus())) {
//				return details;
//			}
//			}
//			throw new NoSuchElementException("customer not present");
//		}
//		if(optional.isPresent()) {
//			return optional.get();
//		}
//		
//		throw new NoSuchElementException("customer not present");
		return loanInputRepositery.getByCustomerId(customerId)
				.orElseThrow(() -> new NoSuchElementException("customer not present"));
	}

	@Override
	public PreApprovedListVikasLoan getByReferenceNo(String customerID) {
		long custid = Long.parseLong(customerID);
		return loanInputRepositery.getByCustomerID(custid)
				.orElseThrow(() -> new NoSuchElementException("customer not present"));

	}

	@Override
	public PreApprovedListVikasLoan saveSingleData(PreApprovedListVikasLoan loanDetails) {

		return loanInputRepositery.save(loanDetails);
	}

	@Override
	public List<PreApprovedListVikasLoan> getByStatus(String status) {

		return loanInputRepositery.findByStatus(status).orElseThrow(() -> new NoSuchElementException("list is empty"));
	}

	@Override
	public List<LoanDetail> findByMobilePhone(long mobilePhone, long branchIdInLong) {

		return loanInputRepositery.getByMobilePhone(mobilePhone, branchIdInLong)
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));

	}

	@Override
	public List<LoanDetail> findByDate(LocalDate startdate, LocalDate enddate, long branchId) {

		return loanInputRepositery.getByDate(startdate, enddate, branchId)
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));
	}

	@Override
	public PreApprovedListVikasLoan fetchByCustomerId(long customerId) {

		return loanInputRepositery.fetchByCustomerId(customerId)
				.orElseThrow(() -> new NoSuchElementException("NO Record found loan Input"));
	}

	@Override
	public List<PreApprovedList> fetchByDate(String startDate, String endDate, String branchId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate startdate1 = LocalDate.parse(startDate, formatter);
		LocalDate enddate1 = LocalDate.parse(endDate, formatter);
		long branchIdInLong = Long.parseLong(branchId);

		return loanInputRepositery.fetchByDate(startdate1, enddate1, branchIdInLong)
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));
	}

	@Override
	public PreApprovedList fetchByCustomerId(String customerId) {
		long customerIdInLong = Long.parseLong(customerId);

		return loanInputRepositery.getLoanDetailsList(customerIdInLong)
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));
	}

//	}
	@Override
	public void statusChange(long customerId, String status) {
		Optional<LoanDetail> optional = loanInputRepositery.getByCustomerId(customerId);
		if (optional.isPresent()) {
			loanInputRepositery.updateStatus(customerId, status);
		} else {
			throw new NoSuchElementException("NO Record found");
		}
	}

	@Override
	public List<PreApprovedList> fetchTopTenPreApprovedList() {

		return loanInputRepositery.topTenRecord(PageRequest.of(0, 10))
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));
	}

	@Override
	public PreApprovedListVikasLoan fetchByCustomerID(long customerNo) {
//		List<LoanDetails> list =loanInputRepositery.fetchByCustomerId(customerNo);
//		if(list.size() == 0){
//			return null;
//		}
		return loanInputRepositery.fetchByCustomerId(customerNo).orElse(null);
	}

	@Override
	public JSONObject getBRNetLoanDetails(String accountNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();

		URL obj = null;
		try {

			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			logger.debug(x.BASEURL + "fetch/loan/account/details/" + accountNo + "?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "fetch/loan/account/details/" + accountNo + "?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));

			sendResponse = LoanInputgetResponse(accountNo, sendResponse, con, "GET");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private static JSONObject LoanInputgetResponse(String parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		con.setDoOutput(true);
		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("GET request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}

		return sendAuthenticateResponse;

	}

	@Override
	public List<PreApprovedListVikasLoan> fetchPreApprovalMfiData(long customerIdInLong) {

		return loanInputRepositery.getByCustomer(customerIdInLong, "VL")
				.orElseThrow(() -> new NoSuchElementException("No record found"));
	}
}
