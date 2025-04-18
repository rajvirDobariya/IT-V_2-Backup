package com.suryoday.roaocpv.others;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.html2pdf.HtmlConverter;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public class PDFSanction {

	private String image;
	AocpvLoanCreation loanCreation;

	public PDFSanction(AocpvLoanCreation loanCreation, String image) {
		this.image = image;
		this.loanCreation = loanCreation;

	}

	public byte[] exportSanctionLetter(HttpServletResponse response) throws Exception {

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		String OutputFileName = "";
		String pdfresponse = "";
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		OutputFileName = x.onePager + loanCreation.getApplicationNo() + "_sanctionletter.pdf";

		StringBuilder htmlString = new StringBuilder();
		pdfresponse = reportPdf(htmlString);
		HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
		document.close();
		byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));
		return inFileBytes;
	}

	private String reportPdf(StringBuilder htmlString) {
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
//		String name="Aher Babaji Shivaji";
		htmlString.append(new String("<!DOCTYPE html>\r\n" + "<html>\r\n" + "\r\n" + "<head>\r\n" + "    <style>\r\n"
				+ "        table,\r\n" + "        th,\r\n" + "        td {\r\n"
				+ "            border: 1px solid black;\r\n" + "            border-collapse: collapse;\r\n"
				+ "        }\r\n" + "    </style>\r\n" + "</head>\r\n" + "\r\n" + "<body>\r\n"
				+ "    <div class=\"logo\" style=\"text-align: right;\">\r\n"
				+ "        <img style=\"width: 20%;\" src=\"" + x.LOGO + "logo.jpg\">\r\n" + "    </div>\r\n"
				+ "    <div class=\"container\">\r\n" + "        <h3 style=\"color: brown;\">SANCTION LETTER</h3>\r\n"
				+ "        <br>\r\n" + "        <p>Customer ID &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;____"
				+ loanCreation.getCustomerId() + "___ <span class=\"tab\"\r\n"
				+ "                style=\"padding-left: 50px;\">Loan Account No&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;__"
				+ loanCreation.getLoanAccoutNumber() + "___</p>\r\n"
				+ "        <p>Customer Name:<span class=\"tab\" style=\"padding-left: 10px;\">__"
				+ loanCreation.getCustomerName() + "__</span></p>\r\n"
				+ "        <p>Suryoday Small finance Bank Ltd is glad to sanction to you a loan of `__"
				+ loanCreation.getSanctionedLoanAmount() + "__ at rate of</p>\r\n" + "        <p>interest @_"
				+ loanCreation.getRateOfInterest() + "_ per annum repayable in __" + loanCreation.getTenure()
				+ "___ monthly instalments of `__" + loanCreation.getSancationEMI() + "__ </p>\r\n"
				+ "        <p>(more specifically as mentioned in Sample Repayment Schedule provided overleaf) for the purpose </p>\r\n"
				+ "        <p>mentioned in your Loan Application Form, subject to payment of the following charges by you by way</p>\r\n"
				+ "        <p>of deduction from the loan amount sanctioned.</p>\r\n"
				+ "        <p style=\"padding-left: 50px;\">a) Loan Processing Fee (LPF) :_____________</p>\r\n"
				+ "        <p style=\"padding-left: 50px;\">b) Applicable Tax(es) :_____________</p>\r\n"
				+ "        <p>You have applied/proposed for:</p>\r\n"
				+ "        <p>□ Credit shield insurance for you and/or your spouse, under Master Insurance Policy held by the Bank, the\r\n"
				+ "            premium of `_____________ and applicable taxes of `_____________Total `_____________</p>\r\n"
				+ "        <p>□ HospiCash insurance for you and/or your spouse, under Master Insurance Policy held by the Bank, the premium\r\n"
				+ "            of `_____________ and applicable taxes of `_____________Total `____________</p>\r\n"
				+ "        <p>□ Natural Calamity insurance under Master Insurance Policy held by the Bank, the premium of `_____________\r\n"
				+ "            and applicable taxes of `_____________Total `_____________</p>\r\n" + "        <br>\r\n"
				+ "        <p>Applicable insurance amounts of the insurance policies selected are payable by you by way of deduction from\r\n"
				+ "            the loan amount sanctioned.Please note, the insurance will be subject to acceptance of the proposal by the\r\n"
				+ "            insurance company and the terms and conditions governing the Master Insurance Policy.</p>\r\n"
				+ "        <p>I understand that Bank has authority to debit any of my Savings /Current accounts maintained with Suryoday\r\n"
				+ "            Small finance Bank Ltd from\r\n"
				+ "            time to time towards EMI as per the Standing Instruction (SI) provided and in case of non-availability of\r\n"
				+ "            funds in my Savings /Current\r\n"
				+ "            accounts maintained with Suryoday Small finance Bank Ltd, the Bank will have the right to file a case under\r\n"
				+ "            section 25 of The Payment and\r\n" + "            settlement System Act 2007</p>\r\n"
				+ "        <p style=\"color: darkgrey;\">The effective annualized interest rate and the instalment payable by you is stated\r\n"
				+ "            in the Key Fact sheet mentioned below</p>\r\n" + "\r\n"
				+ "        <div style=\"display:inline-block; width: 90%;\">\r\n"
				+ "            <div class=\"box\" style=\"width:200px; height: 65px; border: 1px solid black;float: left; text-align: center;\">\r\n"
				+ "                <br>\r\n"
				+ "            <img style=\"width: 50%;\" src=\"D:\\FInalUAT\\src\\main\\resources\\static\\sign.jpg\">  \r\n"
				+ "            </div>\r\n"
				+ "            <div class=\"box\" style=\"height: 65px; width:300px;  border: 1px solid black;float: right; text-align: center; padding-top: 10px;\">\r\n"
				+ "                <img style=\"width: 40%;\" src=\"D:\\FInalUAT\\src\\main\\resources\\static\\sign.jpg\"> \r\n"
				+ "            </div>\r\n" + "        </div>\r\n" + "\r\n" + "        <p>Date:</p>\r\n"
				+ "        <h3 style=\"color: brown;\">KEY FACTSHEET</h3>\r\n"
				+ "        <table style=\"width: 90%;\">\r\n" + "            <tr>\r\n"
				+ "                <th style=\"text-align: center;\">Sr No</th>\r\n"
				+ "                <th style=\"text-align: center;\">Parameter</th>\r\n"
				+ "                <th colspan=\"2\"></th>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">1</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Loan amount (amount to be disbursed to the borrower)\r\n"
				+ "                    (In Rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; font-weight: bold;\">₹60,000</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">2</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Interest rate</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">28%</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">3</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Total Interest charge during the entire tenure of the\r\n"
				+ "                    loan (In Rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">18987.99</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">4</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Other up-front charges (Break-up of each component to\r\n"
				+ "                    be given below (In Rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">3422.48</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">(a)</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Processing fees (In Rupees) (including GST)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">1800</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">(b)</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Insurance charges (In Rupees) b(I) +b(II) + b(III)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">1,572. 48</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n" + "                <td></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">I. credit shield insurance</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">1,572. 48</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n" + "                <td></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">II. Hospital Daily cash (Hospi Cash)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\"></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">III. Griha Raksha (Natural Calamities)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">50</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">(c)</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Others (If any) (In Rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">5</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Net disbursed amount (In Rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">56577.52</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">6</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Total amount to be paid by the borrower (sum of 1, 3 &\r\n"
				+ "                    4 (in rupees)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">82410.47</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">7</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Effective annualized interest rate (In percentage)\r\n"
				+ "                    (computed on net disbursed amount using IRR and reducing balance\r\n"
				+ "                    amount)</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">33.43%</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">8</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Loan term - monthly instalments</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">24</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">9</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Repayment frequency by the borrower</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">Monthly</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">10</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Number of instalments of repayment</td>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center; color: darkgrey;\">24</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td rowspan=\"2\" style=\"text-align: center; color: darkgrey;\">11</td>\r\n"
				+ "                <td rowspan=\"2\" style=\"text-align: center; color: darkgrey;\">Amount of each instalment of repayment (In\r\n"
				+ "                    Rupees)</td>\r\n"
				+ "                <td colspan=\"1\" style=\"text-align: center; color: darkgrey;\">Regular Instalment</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">3300</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Regular Instalment</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">3300</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"2\" style=\"text-align: center;\">Detail about contingent charges</td>\r\n"
				+ "                <td></td>\r\n" + "                <td style=\"text-align: center;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">12</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Borrower shall not be charged any penalty on prepayment\r\n"
				+ "                    of loan at any time</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\"></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">13</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Penal charges in case of delayed payment (if any)</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\"></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">14</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">Other charges (if any)</td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\"></td>\r\n"
				+ "                <td style=\"text-align: center; color: darkgrey;\">NA</td>\r\n"
				+ "            </tr>\r\n" + "        </table>\r\n"
				+ "        <h3 style=\"text-align: center; color: brown;\"><u>DETAILED REPAYMENT SCHEDULE</u></h3>\r\n"
				+ "        <table style=\"width: 90%;\">\r\n" + "            <tr>\r\n"
				+ "                <th style=\"text-align: center;\">Installment No</th>\r\n"
				+ "                <th style=\"text-align: center;\">Outstanding Principal</th>\r\n"
				+ "                <th style=\"text-align: center;\">Payment</th>\r\n"
				+ "                <th style=\"text-align: center;\">Interest</th>\r\n"
				+ "                <th style=\"text-align: center;\">Principal</th>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">1</td>\r\n"
				+ "                <td style=\"text-align: center;\">60000</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1400</td>\r\n"
				+ "                <td style=\"text-align: center;\">1900</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">2</td>\r\n"
				+ "                <td style=\"text-align: center;\">58100</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1356</td>\r\n"
				+ "                <td style=\"text-align: center;\">1944</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">3</td>\r\n"
				+ "                <td style=\"text-align: center;\">56156</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1310</td>\r\n"
				+ "                <td style=\"text-align: center;\">1990</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">4</td>\r\n"
				+ "                <td style=\"text-align: center;\">54166</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1264</td>\r\n"
				+ "                <td style=\"text-align: center;\">2036</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">5</td>\r\n"
				+ "                <td style=\"text-align: center;\">52130</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1216</td>\r\n"
				+ "                <td style=\"text-align: center;\">2084</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">6</td>\r\n"
				+ "                <td style=\"text-align: center;\">50046</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1168</td>\r\n"
				+ "                <td style=\"text-align: center;\">2132</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">7</td>\r\n"
				+ "                <td style=\"text-align: center;\">47914</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1118</td>\r\n"
				+ "                <td style=\"text-align: center;\">2182</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">8</td>\r\n"
				+ "                <td style=\"text-align: center;\">45732</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1067</td>\r\n"
				+ "                <td style=\"text-align: center;\">2233</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">9</td>\r\n"
				+ "                <td style=\"text-align: center;\">43499</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">1015</td>\r\n"
				+ "                <td style=\"text-align: center;\">2285</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">10</td>\r\n"
				+ "                <td style=\"text-align: center;\">41214</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">962</td>\r\n"
				+ "                <td style=\"text-align: center;\">2338</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">11</td>\r\n"
				+ "                <td style=\"text-align: center;\">38876</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">907</td>\r\n"
				+ "                <td style=\"text-align: center;\">2393</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">12</td>\r\n"
				+ "                <td style=\"text-align: center;\">36483</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">851</td>\r\n"
				+ "                <td style=\"text-align: center;\">2449</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">13</td>\r\n"
				+ "                <td style=\"text-align: center;\">34034</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">794</td>\r\n"
				+ "                <td style=\"text-align: center;\">2506</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">14</td>\r\n"
				+ "                <td style=\"text-align: center;\">31528</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">736</td>\r\n"
				+ "                <td style=\"text-align: center;\">2564</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">15</td>\r\n"
				+ "                <td style=\"text-align: center;\">28964</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">676</td>\r\n"
				+ "                <td style=\"text-align: center;\">2624</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">16</td>\r\n"
				+ "                <td style=\"text-align: center;\">26340</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">615</td>\r\n"
				+ "                <td style=\"text-align: center;\">2685</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">17</td>\r\n"
				+ "                <td style=\"text-align: center;\">23654</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">552</td>\r\n"
				+ "                <td style=\"text-align: center;\">2748</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">18</td>\r\n"
				+ "                <td style=\"text-align: center;\">20906</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">488</td>\r\n"
				+ "                <td style=\"text-align: center;\">2812</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">19</td>\r\n"
				+ "                <td style=\"text-align: center;\">18094</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">422</td>\r\n"
				+ "                <td style=\"text-align: center;\">2878</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">20</td>\r\n"
				+ "                <td style=\"text-align: center;\">15216</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">355</td>\r\n"
				+ "                <td style=\"text-align: center;\">2945</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">21</td>\r\n"
				+ "                <td style=\"text-align: center;\">12271</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">286</td>\r\n"
				+ "                <td style=\"text-align: center;\">3014</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">22</td>\r\n"
				+ "                <td style=\"text-align: center;\">9258</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">216</td>\r\n"
				+ "                <td style=\"text-align: center;\">3084</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">23</td>\r\n"
				+ "                <td style=\"text-align: center;\">6174</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">144</td>\r\n"
				+ "                <td style=\"text-align: center;\">3156</td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td style=\"text-align: center;\">24</td>\r\n"
				+ "                <td style=\"text-align: center;\">6174</td>\r\n"
				+ "                <td style=\"text-align: center;\">3300</td>\r\n"
				+ "                <td style=\"text-align: center;\">70</td>\r\n"
				+ "                <td style=\"text-align: center;\">3018</td>\r\n" + "            </tr>\r\n"
				+ "        </table>\r\n" + "        <h3 style=\"color: brown;\">ASSET CLASSIFICATION</h3>\r\n"
				+ "        <p>As per Income Recognition, Asset Classification and Provisioning (IRACP) norms of RBI, below is example based\r\n"
				+ "            on which your loan might be classified as SMA / NPA</p>\r\n"
				+ "        <p>Example: If due date of a loan account is March 31, 2021, and full dues are not received before the Bank runs\r\n"
				+ "            the day-end process for this\r\n"
				+ "            date, the date of overdue shall be March 31, 2021. If it continues to remain overdue, then this account\r\n"
				+ "            shall get tagged as SMA-1 upon\r\n"
				+ "            running day-end process on April 30, 2021 i.e. upon completion of 30 days of being continuously overdue.\r\n"
				+ "            Accordingly, the date of SMA-1\r\n"
				+ "            classification for that account shall be April 30, 2021. Similarly, if the account continues to remain\r\n"
				+ "            overdue, it shall get tagged as SMA-2\r\n"
				+ "            upon running day-end process on May 30, 2021 and if continues to remain overdue further, it shall get\r\n"
				+ "            classified as NPA upon running day-\r\n"
				+ "            end process on June 29, 2021.</p>\r\n"
				+ "        <p style=\"font-weight: bold;\">Illustration of SMA/NPA classification dates</p>\r\n"
				+ "        <table style=\"width: 40%;\">\r\n"
				+ "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>Loan Disbursement </td>\r\n" + "                <td>24-Mar-21</td>\r\n"
				+ "            </tr>\r\n" + "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>First Due</td>\r\n" + "                <td>31-Mar-21</td>\r\n"
				+ "            </tr>\r\n" + "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>SMA - 0</td>\r\n" + "                <td>Upto 29-April-21</td>\r\n"
				+ "            </tr>\r\n" + "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>SMA - 1</td>\r\n" + "                <td>30-Apr-21</td>\r\n"
				+ "            </tr>\r\n" + "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>SMA - 2</td>\r\n" + "                <td>30-May-21</td>\r\n"
				+ "            </tr>\r\n" + "            <tr style=\"text-align: center; height: 40px;\">\r\n"
				+ "                <td>NPA Date</td>\r\n" + "                <td>29-Jun-21</td>\r\n"
				+ "            </tr>\r\n" + "        </table>\r\n" + "    </div>\r\n" + "</body>\r\n" + "\r\n"
				+ "</html>"));
		return htmlString.toString();
	}
}
