package com.suryoday.hastakshar.serviceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.hastakshar.pojo.HastMaster;
import com.suryoday.hastakshar.pojo.HastReqStatus;
import com.suryoday.hastakshar.pojo.HastUserLog;
import com.suryoday.hastakshar.pojo.HastUserName;
import com.suryoday.hastakshar.repository.MasterRepo;
import com.suryoday.hastakshar.repository.ReqStatusRepo;
import com.suryoday.hastakshar.repository.UserLogRepo;
import com.suryoday.hastakshar.repository.UserNameRepo;
import com.suryoday.hastakshar.service.SaveReconRequestService;

@Service
public class SaveReconRequestServiceImpl implements SaveReconRequestService {

	private static Logger logger = LoggerFactory.getLogger(SaveReconRequestServiceImpl.class);

	@Autowired
	private ReqStatusRepo reqstatusrepo;

	@Autowired
	private UserNameRepo usernamerepo;

	@Autowired
	private EmailNotifictionService emailNotifictionService;

	@Autowired
	private UserLogRepo userlogrepo;

	@Autowired
	private MasterRepo masterRepo;

	@Override
	public JSONObject saveReconRequestData(JSONObject jsonRequest) {
		String txnType = jsonRequest.getJSONObject("Data").getString("transactionTypes");
		String txnDescription = jsonRequest.getJSONObject("Data").getString("transactionDescription");
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String accountNo = jsonRequest.getJSONObject("Data").getString("accountNo");

		HastMaster fetchTxnTypeTxnDescp = masterRepo.fetchTxnTypeTxnDescp(txnType, txnDescription);

		// String amount = jsonRequest.getJSONObject("Data").getString("amount");
		String approver1 = fetchTxnTypeTxnDescp.getApprover1();
		String approver2 = fetchTxnTypeTxnDescp.getApprover2();
		String approver3 = fetchTxnTypeTxnDescp.getApprover3();
		String approver4 = fetchTxnTypeTxnDescp.getApprover4();
		String approver5 = fetchTxnTypeTxnDescp.getApprover5();
		String department = fetchTxnTypeTxnDescp.getDepartment();
		String nature = fetchTxnTypeTxnDescp.getNature();

		String applictioNO = generateApplicationNumber(nature, empId);
		String product = fetchTxnTypeTxnDescp.getProduct();

		String keyword = fetchTxnTypeTxnDescp.getKeyword();
		String requestFlow = fetchTxnTypeTxnDescp.getRequestFlow();
		// Set and save data to Request Table
		HastReqStatus hastreqstatus = new HastReqStatus();
		// hastreqstatus.setAmount(amount);
		hastreqstatus.setApprover1(approver1);
		hastreqstatus.setApprover2(approver2);
		hastreqstatus.setApprover3(approver3);
		hastreqstatus.setApprover4(approver4);
		hastreqstatus.setApprover5(approver5);
		hastreqstatus.setDepartment(department);
		hastreqstatus.setRequestBy(empId);
		hastreqstatus.setNature(nature);
		hastreqstatus.setProduct(product);
		hastreqstatus.setStatus("INITIATED");
		hastreqstatus.setTransactionDescription(txnDescription);
		hastreqstatus.setTransactionTypes(txnType);
		hastreqstatus.setRequestFlow(requestFlow);
		String requestState = getStateByEmpId(empId);
		hastreqstatus.setRequestState(requestState);
		LocalDateTime currentTime = LocalDateTime.now();
		hastreqstatus.setCreateDate(currentTime);
		hastreqstatus.setUpdateDate(currentTime);
		int appLength = applictioNO.length();
		JSONObject response = new JSONObject();

		if (appLength != 0) {
			hastreqstatus.setApplictioNO(applictioNO);
			hastreqstatus.setAccountNo(accountNo);
			hastreqstatus.setKeyword(keyword);

			reqstatusrepo.save(hastreqstatus);
			HastUserLog hastuserlog = new HastUserLog();
			hastuserlog.setApplictioNO(applictioNO);
			hastuserlog.setStatus("INITIATED");
			hastuserlog.setUpdateDate(currentTime);
			hastuserlog.setEmpId(empId);
			if (requestFlow.equals("PARALLEL")) {
				logger.info("******* new request email trigger");
				if (!approver1.equals("NA") && !approver1.equals(null)) {
					String app1Email = getEmail(approver1);
					emailNotifictionService.NewReqEmail(app1Email, accountNo, applictioNO, nature, txnType,
							txnDescription, department, empId);
				}
				if (!approver2.equals("NA") && !approver2.equals(null)) {
					String app1Email = getEmail(approver2);
					emailNotifictionService.NewReqEmail(app1Email, accountNo, applictioNO, nature, txnType,
							txnDescription, department, empId);
				}
				if (!approver3.equals("NA") && !approver3.equals(null)) {
					String app1Email = getEmail(approver3);
					emailNotifictionService.NewReqEmail(app1Email, accountNo, applictioNO, nature, txnType,
							txnDescription, department, empId);
				}
				if (!approver4.equals("NA") && !approver4.equals(null)) {
					String app1Email = getEmail(approver4);
					emailNotifictionService.NewReqEmail(app1Email, accountNo, applictioNO, nature, txnType,
							txnDescription, department, empId);
				}
				if (!approver5.equals("NA") && !approver5.equals(null)) {
					String app1Email = getEmail(approver5);
					emailNotifictionService.NewReqEmail(app1Email, accountNo, applictioNO, nature, txnType,
							txnDescription, department, empId);
				}
			}
			userlogrepo.save(hastuserlog);
			response.put("ApplictioNO", applictioNO);
			response.put("Nature", nature);
			response.put("AccountNo", accountNo);
			response.put("ApproverName", approver1);
			response.put("CreatedDate", currentTime);
			response.put("Departement", department);
			response.put("TransactionType", txnType);
			response.put("TransactionDescription", txnDescription);

		} else {
			response.put("message", "please enter appliction number!");
			response.put("status", "error");
		}
		return response;
	}

	public String getStateByEmpId(String empId) {
		String empState = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empState = "Employee Data Not found";
		} else {
			empState = ((nameList.get(0).getEmpState() == null) ? null : nameList.get(0).getEmpState());
		}
		return empState;
	}

	public boolean isNumeric(String inupt) {
		// Check Input is number then True else False using regular expression.
		return inupt.matches("\\d+");
	}

	public String getEmail(String empId) {
		String empName = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empName = "Name not found";
		} else {
			empName = nameList.get(0).getEmpEmailid();
		}
		return empName;
	}

	@Override
	public JSONArray fetchReconList(JSONObject jsonRequest) {

		String accountNo = jsonRequest.getJSONObject("Data").getString("AccountNo");
		// String role = jsonRequest.getJSONObject("Data").getString("role");
		JSONArray array = new JSONArray();

		List<HastReqStatus> list = reqstatusrepo.fetchReconList(accountNo);
		array = callList(list);
		return array;

	}

	public String generateApplicationNumber(String nature, String empId) {
		// Assuming getNature() and getRequestBy() are methods that retrieve the value
		// of nature and requestBy respectively

		String shortNature = null;
		if ("Financial".equals(nature)) {
			shortNature = "FIN";
		} else {
			shortNature = "NOFIN";
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMdHHmmssSSS");
		String currentDate = dateFormat.format(date);

		String randApplicationNo = shortNature + currentDate + empId;

		return randApplicationNo;
	}

	public JSONArray callList(List<HastReqStatus> list) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject innerData = new JSONObject();
			innerData.put("applictioNO", list.get(i).getApplictioNO());
			innerData.put("amount", list.get(i).getAmount());
			innerData.put("department", list.get(i).getDepartment());
			innerData.put("transactionTypes", list.get(i).getTransactionTypes());
			innerData.put("transactionDescription", list.get(i).getTransactionDescription());
			innerData.put("product", list.get(i).getProduct());
			innerData.put("policyNo", list.get(i).getPolicyNo());
			innerData.put("approver1", list.get(i).getApprover1());
			innerData.put("approver2", list.get(i).getApprover2());
			innerData.put("approver3", list.get(i).getApprover3());
			innerData.put("approver4", list.get(i).getApprover4());
			innerData.put("approver5", list.get(i).getApprover5());
			innerData.put("requestBy", list.get(i).getRequestBy());
			innerData.put("remark", list.get(i).getRemark());
			innerData.put("status", list.get(i).getStatus());
			innerData.put("nature", list.get(i).getNature());
			innerData.put("createDate", list.get(i).getCreateDate());
			innerData.put("requestflow", list.get(i).getRequestFlow());
			innerData.put("requestState", list.get(i).getRequestState());
			if (list.get(i).getLan() == null) {
				innerData.put("lan", "NA");
			} else {
				innerData.put("lan", list.get(i).getLan());
			}
			if (list.get(i).getCIF() == null) {
				innerData.put("cif", "NA");
			} else {
				innerData.put("cif", list.get(i).getCIF());
			}
			if (list.get(i).getAccountNo() == null) {
				innerData.put("accountNo", "NA");
			} else {
				innerData.put("accountNo", list.get(i).getAccountNo());
			}
			if (list.get(i).getKeyword() == null) {
				innerData.put("keyword", "NA");
			} else {
				innerData.put("keyword", list.get(i).getKeyword());
			}
			List<HastUserLog> list1 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover1());
			List<HastUserLog> list2 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover2());
			List<HastUserLog> list3 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover3());
			List<HastUserLog> list4 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover4());
			List<HastUserLog> list5 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover5());
			if (list.get(i).getApprover1().length() != 0 && !list1.isEmpty()) {
				innerData.put("approver1status", list.get(i).getApprover1() + "|" + list1.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover1()));
				innerData.put("approverStatus1", list1.get(0).getStatus());
				innerData.put("approver1EmpName", getEmpName(list.get(i).getApprover1()));
			} else {
				innerData.put("approver1status",
						list.get(i).getApprover1() + "|PENDING|" + getEmpName(list.get(i).getApprover1()));
				innerData.put("approverStatus1", "PENDING");
				innerData.put("approver1EmpName", getEmpName(list.get(i).getApprover1()));
			}
			if (list.get(i).getApprover2().length() != 0 && !list2.isEmpty()) {
				innerData.put("approver2status", list.get(i).getApprover2() + "|" + list2.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover2()));
				innerData.put("approverStatus2", list2.get(0).getStatus());
				innerData.put("approver2EmpName", getEmpName(list.get(i).getApprover2()));
			} else {
				innerData.put("approver2status",
						list.get(i).getApprover2() + "|PENDING|" + getEmpName(list.get(i).getApprover2()));
				innerData.put("approverStatus2", "PENDING");
				innerData.put("approver2EmpName", getEmpName(list.get(i).getApprover2()));
			}
			if (list.get(i).getApprover3().length() != 0 && !list3.isEmpty()) {
				innerData.put("approver3status", list.get(i).getApprover3() + "|" + list3.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover3()));
				innerData.put("approverStatus3", list3.get(0).getStatus());
				innerData.put("approver3EmpName", getEmpName(list.get(i).getApprover3()));
			} else {
				innerData.put("approver3status",
						list.get(i).getApprover3() + "|PENDING|" + getEmpName(list.get(i).getApprover3()));
				innerData.put("approverStatus3", "PENDING");
				innerData.put("approver3EmpName", getEmpName(list.get(i).getApprover3()));
			}
			if (list.get(i).getApprover4().length() != 0 && !list4.isEmpty()) {
				innerData.put("approver4status", list.get(i).getApprover4() + "|" + list4.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover4()));
				innerData.put("approverStatus4", list4.get(0).getStatus());
				innerData.put("approver4Status", getEmpName(list.get(i).getApprover4()));
			} else {
				innerData.put("approver4status",
						list.get(i).getApprover4() + "|PENDING|" + getEmpName(list.get(i).getApprover4()));
				innerData.put("approverStatus4", "PENDING");
				innerData.put("approver4Status", getEmpName(list.get(i).getApprover4()));
			}
			if (list.get(i).getApprover5().length() != 0 && !list5.isEmpty()) {
				innerData.put("approver5status", list.get(i).getApprover5() + "|" + list5.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover5()));
				innerData.put("approverStatus5", list5.get(0).getStatus());
				innerData.put("approver5Status", getEmpName(list.get(i).getApprover5()));
			} else {
				innerData.put("approver5status",
						list.get(i).getApprover5() + "|PENDING|" + getEmpName(list.get(i).getApprover5()));
				innerData.put("approverStatus5", "PENDING");
				innerData.put("approver5Status", getEmpName(list.get(i).getApprover5()));
			}
			array.put(innerData);
		}
		return array;
	}

	public String getEmpName(String empId) {
		String empName = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empName = "Name not found";
		} else {
			empName = nameList.get(0).getEmpName();
		}
		return empName;
	}

}
