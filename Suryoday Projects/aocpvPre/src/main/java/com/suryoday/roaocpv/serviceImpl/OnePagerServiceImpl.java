package com.suryoday.roaocpv.serviceImpl;

import java.util.Base64;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.OnePagerService;

@Service
public class OnePagerServiceImpl implements OnePagerService{

	@Autowired
	AocpvImageService aocpvImageService;
	
	String customerPhoto ="";
    String houseImageInside ="";
    String houseImageOutside ="";
    String AO_Selfie ="";
    String RO_Selfie ="";
    String buisnessPhoto ="";
    
    @Autowired
	AocpCustomerDataService aocpCustomerDataService;
    
    @Autowired
	AocpvIncomeService aocpvIncomeService;
    
    @Autowired
	private UserService userService;
    
	@Override
	public String reportPdf(StringBuilder htmlString, long applicationNoInLong) {

		  List<AocpvImages> list=aocpvImageService.fetchImageforOnePager(applicationNoInLong);
		     
		     for(AocpvImages aocpvImages:list) {
		    	 String documenttype = aocpvImages.getDocumenttype();
		    	 if(documenttype.equals("customerPhoto")) {
		    		 customerPhoto=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 else if(documenttype.equals("houseImageInside")) {
		    		 houseImageInside=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 else if(documenttype.equals("houseImageOutside")) {
		    		 houseImageOutside=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 else if(documenttype.equals("AO_Selfie")) {
		    		 AO_Selfie=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 else if(documenttype.equals("RO_Selfie")) {
		    		 RO_Selfie=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 else if(documenttype.equals("buisnessPhoto")) {
		    		 buisnessPhoto=Base64.getEncoder().encodeToString(aocpvImages.getImages());
		    	 }
		    	 
		     }
		     
		ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
        x.getappprop();
        
        AocpCustomer aocpCustomer = aocpCustomerDataService.getByApp(applicationNoInLong);
        User user = userService.fetchById(aocpCustomer.getCreatedby());
       AocpvIncomeDetails incomeDetails= aocpvIncomeService.getByApplicationNoAndmember(applicationNoInLong,"SELF");
       String requestedloanResponse = aocpCustomer.getResponseEmi();
       
       String InterestRate="";
       String Term="";
       String AmountRequested="";
		if(requestedloanResponse!= null) {
			JSONObject jsonObject2=new JSONObject(requestedloanResponse);
			 InterestRate = jsonObject2.getString("INTERESTRATE");
			 Term = jsonObject2.getString("TERM");	
			 AmountRequested = jsonObject2.getString("AMOUNTREQUESTED");
		}
		htmlString.append(new String("<!DOCTYPE html>\r\n"));
		htmlString.append(new String("<html>\r\n"));
		htmlString.append(new String("<head>\r\n"));
		htmlString.append(new String("<style>\r\n"));
		htmlString.append(new String("table,\r\n"
				+ "      th,\r\n"
				+ "      td {\r\n"));
		htmlString.append(new String("border: 1px solid black;\r\n"
				+ "        border-collapse: collapse;\r\n"
				+ "        min-height: 150px\r\n"
				+ "      }\r\n"
		+ "      body {\r\n"
				+ "        vertical-align: middle;\r\n"
				+ "      }\r\n"
				+ "    </style>\r\n"));
		htmlString.append(new String("  </head>\r\n"));
		htmlString.append(new String("  <body>\r\n"));
		htmlString.append(new String(" <div class=\"logo\" style=\"text-align: right;\">"
				+ "    <img style=\"width: 15%;\" src=\""+x.LOGO+"logo.jpg\">\r\n"
				+ " </div>"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <div class=\"container\" style=\"padding-left: 100px;\">\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Consolidated Report</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Sourcing Dtetails</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td>Application ID</td>\r\n"
				+ "        <td> "+applicationNoInLong+"</td>\r\n"
				+ "        <td>Name of the RM</td>\r\n"
				+ "        <td>"+user.getUserName()+"</td>\r\n"
				+ "        <td>Sourcing Name</td>\r\n"
				+ "        <td>"+user.getBranchName()+"</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td>Date of Login </td>\r\n"
				+ "        <td>"+aocpCustomer.getCreationDate()+"</td>\r\n"
				+ "        <td>Name of Sales Manager</td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td>Loan Amount</td>\r\n"
				+ "        <td>"+AmountRequested+"</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td>Branch</td>\r\n"
				+ "        <td>Head Offce</td>\r\n"
				+ "        <td>Sourcing Type</td>\r\n"
				+ "        <td>Branch</td>\r\n"
				+ "        <td>Tenure</td>\r\n"
				+ "        <td>"+Term+"</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Applicant Details</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Member in the application</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Name</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Gender</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Current Age</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Education</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Customer Category</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Main Applicant </td>\r\n"
				+ "        <td>"+aocpCustomer.getName()+"</td>\r\n"
				+ "        <td>"+incomeDetails.getGender()+"</td>\r\n"
				+ "        <td>"+incomeDetails.getAge()+"</td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td>ARCHITECT</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Employment Details</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Member in the application</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Occupation</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Name of the Current <br>Employer/Name of the frm </td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Current Experience/ <br> Stability </td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Total Experience/ <br> Stability </td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Net monthly <br> Income </td>\r\n"
				+ "      </tr>\r\n"
				+ " 	<tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Main Applicant </td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "      </tr>"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Loan Details</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td>Loan Amount <br> Offered</td>\r\n"
				+ "        <td>"+AmountRequested+"</td>\r\n"
				+ "        <td>Offered Tenure <br> (Months)</td>\r\n"
				+ "        <td>"+Term+"</td>\r\n"
				+ "        <td>FOIR (%) </td>\r\n"
				+ "        <td>54</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td>Offered ROI</td>\r\n"
				+ "        <td>"+InterestRate+"</td>\r\n"
				+ "        <td>CAM Score <br> (Internal Score)</td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td>LTV (%) </td>\r\n"
				+ "        <td></td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"4\">Collateral Details</th>\r\n"
				+ "      </tr>\r\n"
				+ " 	<tr>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "        <td></td>\r\n"
				+ "      </tr>"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"2\">Bureau Details</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray; width:20%;\">Member in the <br> application </td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Bureau Score</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Main Applicant</td>\r\n"
				+ "        <td>-100</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>"));
		htmlString.append(new String(" 	<br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"6\">Bank Details</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Member in the <br> application </td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Bank Name</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Bank Branch Name</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Account Type</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">ABB</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Inward cheque <br> return (%) </td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Main Applicant</td>\r\n"
				+ "        <td>Sahebrao Deshmukh Co <br> Operative Bank Ltd </td>\r\n"
				+ "        <td>Mumbai</td>\r\n"
				+ "        <td>Savings</td>\r\n"
				+ "        <td>26,490</td>\r\n"
				+ "        <td>0</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "        <br>\r\n"
				+ "        <br>\r\n"));
		htmlString.append(new String("        <table style=\"width:90%\">\r\n"
				+ "            <tr style=\"background-color: #ffa600;\">\r\n"
				+ "                <th colspan=6>Household Expense</th>\r\n"
				+ "            </tr>\r\n"
				+ "            <tr>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Expense Type</td>\r\n"
				+ "                <td>Food & Utility Expense</td>\r\n"
				+ "                <td>Rent</td>\r\n"
				+ "                <td>Miscellaneous<br>(Transportation)</td>\r\n"
				+ "                <td>Miscellaneous<br>(Medical)</td>\r\n"
				+ "                <td>Monthly Balance</td>\r\n"
				+ "            </tr>\r\n"
				+ "            <tr>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Amount C</td>\r\n"
				+ "                <td>"+aocpCustomer.getFoodAndUtility()+"</td>\r\n"
				+ "                <td>"+aocpCustomer.getRent()+"</td>\r\n"
				+ "                <td>"+aocpCustomer.getTransportation()+"</td>\r\n"
				+ "                <td>"+aocpCustomer.getOther()+"</td>\r\n"
				+ "                <td>"+aocpCustomer.getMonthlyBalance()+"</td>\r\n"
				+ "            </tr>\r\n"
				+ "        </table>"));
		htmlString.append(new String("    <br>\r\n"
				+ "        <br>\r\n"
				+ "        <br>\r\n"));
		htmlString.append(new String("        <table style=\"width:90%\">\r\n"
				+ "            <tr style=\"background-color: #ffa600;\">\r\n"
				+ "                <th colspan=6>Monthly Income</th>\r\n"
				+ "            </tr>\r\n"
				+ "            <tr>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Member in the <br> application </td>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Name</td>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Earning Member</td>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Primary Source Of Income</td>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Monthly Income</td>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Total Monthly Loan EMI</td>\r\n"
				+ "            </tr>\r\n"
				+ "            <tr>\r\n"
				+ "                <td style=\"background-color: lightgray;\">Main Applicant</td>\r\n"
				+ "                <td>"+aocpCustomer.getName()+"</td>\r\n"
				+ "                <td>"+incomeDetails.getEarning()+"</td>\r\n"
				+ "                <td>"+incomeDetails.getPrimarySourceOfIncome()+"</td>\r\n"
				+ "                <td>"+incomeDetails.getMonthlyIncome()+"</td>\r\n"
				+ "                <td>"+incomeDetails.getMonthlyLoanEmi()+"</td>\r\n"
				+ "            </tr>\r\n"
				+ "        </table>"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"3\">Deviations</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">SL No</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Description</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Level of Authority</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">1</td>\r\n"
				+ "        <td>ASMA AKHATAR SHAIKH: Credit Bureau score less than acceptable norms 147</td>\r\n"
				+ "        <td>L4</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"2\">AO Remarks</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray; width:20%;\">SL No</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Remarks</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">1</td>\r\n"
				+ "        <td></td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"));
		htmlString.append(new String("    <br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("    <table style=\"width:90%\">\r\n"
				+ "      <tr style=\"background-color: #ffa600;\">\r\n"
				+ "        <th colspan=\"2\">RO Remarks</th>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray; width:20%;\">SL No</td>\r\n"
				+ "        <td style=\"background-color: lightgray;\">Remarks</td>\r\n"
				+ "      </tr>\r\n"
				+ "      <tr>\r\n"
				+ "        <td style=\"background-color: lightgray;\">1</td>\r\n"
				+ "        <td></td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>\r\n"
				+"<br>\r\n"));
		htmlString.append(new String("<h3>Customer Photograph</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "    <img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+customerPhoto+"'/>"
				+ "</div>"));
		htmlString.append(new String("<br>\r\n"
				+"<br>\r\n"
				+"<br>\r\n"
				+ "    <br>\r\n"
				+ "    <br>\r\n"));
		htmlString.append(new String("<h3>Photograph Of House Inside</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "<img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+houseImageInside+"'/>\r\n"
				+ "</div>\r\n"));
		htmlString.append(new String("<br>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"));
		htmlString.append(new String("<h3>Photograph Of House Outside</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "<img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+houseImageOutside+"'/>\r\n"
				+ "</div>"));
		htmlString.append(new String("<br>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"));
		htmlString.append(new String("<h3>Business Photograph</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "<img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+buisnessPhoto+"'/>\r\n"
				+ "</div>"));
		htmlString.append(new String("<br>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"));
		htmlString.append(new String("<h3>AO Photograph</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "<img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+AO_Selfie+"'/>\r\n"
				+ "</div>"));
		htmlString.append(new String("<br>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"));
		htmlString.append(new String("<h3>RO Photograph</h1>\r\n"
				+ "    <div class=\"img2\" style=\"height: 400px ; width: 75%; text-align: center; border: 1px solid black;\">"
				+ "<img style=\"height: 350px; width: 250px; text-align: center; margin-top: 25px;\"  src='data:image/jpeg;base64,"+RO_Selfie+"'/>\r\n"
				+ "</div>"));
		htmlString.append(new String("</div>\r\n"));
		htmlString.append(new String("  </body>\r\n"));
		htmlString.append(new String("</html>"));
		return htmlString.toString();
	}

}
