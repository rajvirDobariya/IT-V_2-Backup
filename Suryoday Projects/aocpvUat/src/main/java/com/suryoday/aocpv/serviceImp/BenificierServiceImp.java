package com.suryoday.aocpv.serviceImp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.suryoday.aocpv.pojo.Benificier;
import com.suryoday.aocpv.repository.BenificierRepository;
import com.suryoday.aocpv.service.BenificierService;

@Service
public class BenificierServiceImp implements BenificierService {

	String userName = "";
	String password = "";
	String url = "";

	// String userName = "sunsmart";
	// String password = "sunsmart@123";
//		

	// String url =
	// "jdbc:sqlserver://10.20.72.17:16333;databaseName=SuryodayMBDB;encrypt=false;sslProtocol=TLSv1.2";

	@Autowired
	BenificierRepository benirepo;

	public BenificierServiceImp() throws Exception {

		String sConfigFile = "props/dbconfig.properties";

		System.out.println("filepath" + sConfigFile);

		Properties prop = new Properties();
		try {
			// load a properties file from class path, inside static method
			prop.load(BenificierServiceImp.class.getClassLoader().getResourceAsStream(sConfigFile));
			// System.out.println(prop);

			// get the property value and print it out
			url = prop.getProperty("url");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			// System.out.println("myUrl" + url);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public List<Benificier> fetchAll() {
		// TODO Auto-generated method stub

		List<Benificier> a = new ArrayList<Benificier>();

		System.out.println("insdie service Imple");
		a = benirepo.findAlla();

		return a;
	}

	public static JSONObject getMasterTable() {
		// String Curdtype="FETCHDUMMY";

		JSONObject fetchdata = new JSONObject();
		String Curdtype = "FETCHALL";
		String dbmsg = "";

		Connection con = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {

			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			String userName = "sa";
			String password = "Vilas@123";

			// String dbURL =
			// "jdbc:sqlserver://10.51.95.161:16333;databaseName=SuryodayMBDB;user=sunsmart;password=sunsmart@123;integratedSecurity=false";

			String url = "jdbc:sqlserver://localhost:1433;databaseName=SuryodayMBDB;encrypt=false;trustServerCertificate=false";

			// con = JDBCHelper.getConnection();
			con = DriverManager.getConnection(url, userName, password);
			// System.out.println("Driver name: a");
			if (con != null) {
				DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
				// System.out.println("Driver name: " + dm.getDriverName());
				// System.out.println("Driver version: " + dm.getDriverVersion());
				// System.out.println("Product name: " + dm.getDatabaseProductName());
				// System.out.println("Product version: " + dm.getDatabaseProductVersion());

				Statement s1 = con.createStatement();

				if (Curdtype.equals("FETCHALL")) {
					// String TokenName= "112521191010012999113021";

					// SELECT query
					// String sql = "SELECT * FROM TB_MB_FAVOURITE1 where
					// MODULETYPE='"+TokenName+"'";

					System.out.println("Inserting records into the table...");
					String sql = "insert into Tbl_Benificier(NAME,TYPE,nick_name,Account) VALUES ('VILAS','2','VILASJ39','989786877898')";
					int result = s1.executeUpdate(sql);

					if (result > 0)
						System.out.println("successfully inserted");

					else
						System.out.println("unsucessful insertion ");

					// closing connection

					con.close();

				} else {

					dbmsg = "CONNECTION FAILED";

					fetchdata.put("Error", dbmsg);

					System.out.println(dbmsg);
					return fetchdata;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

			dbmsg = "CONNECTION FAILED" + e.getMessage().toString();
			System.out.println(dbmsg);
			fetchdata.put("Error", dbmsg);
			return fetchdata;

			// return a;
		} finally {

			// JDBCHelper.closeConnection(con);

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* Ignored */}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}
		return fetchdata;

	}

	@Override
	public JSONObject saveBenificier(String bm, String customerid) {

		System.out.println("save called");

//		String filePath = getClass().getClassLoader().getResource("props/uidai_admin_iris.properties").getPath();
//		Properties prop = new Properties();
//		prop.load(new FileInputStream(new File(filePath)));

		String msg = "";
		JSONObject Response = new JSONObject();
		String Description = "";

		org.json.JSONObject jk = new org.json.JSONObject(bm);
		System.out.println(jk.getJSONObject("Data").getString("Name").toString());
		org.json.JSONObject jm = jk.getJSONObject("Data");

		Benificier bb = new Benificier();
		bb.setName(jm.getString("Name").toString());
		bb.setAccount(jm.getString("Account").toString());
		bb.setNickName(jm.getString("NickName").toString());
		bb.setBankCity(jm.getString("BankCity").toString());
		bb.setCustomerId(customerid);
		bb.setAccountType(jm.getString("AccountType").toString());
		bb.setBankIfsc(jm.getString("BankIfsc").toString());
		bb.setBankBranch(jm.getString("BankBranch").toString());
		bb.setMaxLimit(jm.getString("MaxLimit").toString());
		bb.setBankName(jm.getString("BankName").toString());
		bb.setType(jm.getString("Type"));
		bb.setStatus("V");
		// bb.setAddress1(jm.getString("Address1").toString());

//         bb.setCity(jm.getString("City"));
//         bb.setState(jm.getString("State"));
//         bb.setZip(jm.getString("Zip"));
//         bb.setMobileNumber(jm.getString("MobileNumber"));
//         bb.setEmailId(jm.getString("EmailId"));

		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
		bb.setCreatedDate(timestamp);

		System.out.println(bb);
		if (bb.getType().equals("3")) {

			if (bb.getBankIfsc().equals("")) {

				JSONObject Error = new JSONObject();
				Error.put("Code", "100");
				Description = "IFSC Code Required ";
				Error.put("Description", Description);
				Response.put("Error", Error);
				return Response;
			}

		}
		if (bb.getType().equals("2") || bb.getType().equals("3")) {

			Connection con = null;
			ResultSet rs = null;
			StringBuffer sb = new StringBuffer();
			try {

				// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

				// con = JDBCHelper.getConnection();
				con = DriverManager.getConnection(url, userName, password);
				// System.out.println("Driver name: a");
				if (con != null) {
					DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
					// System.out.println("Driver name: " + dm.getDriverName());
					// System.out.println("Driver version: " + dm.getDriverVersion());
					// System.out.println("Product name: " + dm.getDatabaseProductName());
					// System.out.println("Product version: " + dm.getDatabaseProductVersion());

					Statement s1 = con.createStatement();
					Long numberofcount = 0L;

					List<Benificier> listofrecords = benirepo.findByCustomerId(customerid);

					if (listofrecords.size() == 0) {
						numberofcount = numberofcount + 1;
						bb.setSequence(numberofcount.toString());

						// insert new record
						String sql = "insert into Tbl_Benificier"
								+ "(NAME,nick_name,Account,Account_Type,Bank_Branch,Bank_City,Bank_Ifsc,Max_Limit,Bank_Name,customer_id,sequence,status,type,created_date,address1,city,state,zip,mobile_number,email_id) "
								+ "VALUES ('" + bb.getName() + "','" + bb.getNickName() + "'," + "'" + bb.getAccount()
								+ "','" + bb.getAccountType() + "','" + bb.getBankBranch() + "'," + "'"
								+ bb.getBankCity() + "','" + bb.getBankIfsc() + "','" + bb.getMaxLimit() + "','"
								+ bb.getBankName() + "'," + "'" + bb.getCustomerId() + "','" + bb.getSequence() + "','"
								+ bb.getStatus() + "','" + bb.getType() + "'," + "'" + bb.getCreatedDate() + "','"
								+ bb.getAddress1() + "','" + bb.getCity() + "','" + bb.getState() + "','" + bb.getZip()
								+ "','" + bb.getMobileNumber() + "','" + bb.getEmailId() + "')";

						System.out.println(sql);
						int result = s1.executeUpdate(sql);

						if (result > 0) {
							msg = "suceesfully";
							JSONObject Data = new JSONObject();
							Data.put("Status", "0");
							Response.put("Date", Data);
						} else {

							msg = "suceesfully";
							JSONObject Data = new JSONObject();
							Data.put("Status", "1");
							Response.put("Date", Data);

							// closing connection

						}

						con.close();

					} else {
						JSONObject Error = new JSONObject();
						Error.put("Code", "100");

						for (int i = 0; i < listofrecords.size(); i++) {
							Benificier benifier = listofrecords.get(i);
							if (benifier.getNickName().equals(bb.getNickName())) {
								Description = "Nick Name already in Use ";

								Error.put("Description", Description);
								Response.put("Error", Error);
								return Response;
							} else if (benifier.getAccount().equals(bb.getAccount())) {
								Description = "Beneficiary account number already available, please try with different account number.";
								Error.put("Description", Description);
								Response.put("Error", Error);
								return Response;
							}

						}

						if (Description == "") {
							// get list
							Benificier benifier1 = listofrecords.get(listofrecords.size() - 1);
							Long seq = Long.parseLong(benifier1.getSequence());
							numberofcount = seq + 1;
							bb.setSequence(numberofcount.toString());
							// insert new record
							String sql = "insert into Tbl_Benificier"
									+ "(NAME,nick_name,Account,Account_Type,Bank_Branch,Bank_City,Bank_Ifsc,Max_Limit,Bank_Name,customer_id,sequence,type,status,created_date,address1,city,state,zip,mobile_number,email_id) "
									+ "VALUES ('" + bb.getName() + "','" + bb.getNickName() + "'," + "'"
									+ bb.getAccount() + "','" + bb.getAccountType() + "','" + bb.getBankBranch() + "',"
									+ "'" + bb.getBankCity() + "','" + bb.getBankIfsc() + "','" + bb.getMaxLimit()
									+ "','" + bb.getBankName() + "'," + "'" + bb.getCustomerId() + "','"
									+ bb.getSequence() + "','" + bb.getType() + "','" + bb.getStatus() + "'" + ",'"
									+ bb.getCreatedDate() + "','" + bb.getAddress1() + "','" + bb.getCity() + "','"
									+ bb.getState() + "','" + bb.getZip() + "','" + bb.getMobileNumber() + "','"
									+ bb.getEmailId() + "')";

							System.out.println(sql);
							int result = s1.executeUpdate(sql);

							if (result > 0) {
								msg = "suceesfully";
								JSONObject Data = new JSONObject();
								Data.put("Status", "0");
								Response.put("Date", Data);
							} else {

								msg = "suceesfully";
								JSONObject Data = new JSONObject();
								Data.put("Status", "1");
								Response.put("Date", Data);

								// closing connection

							}

							con.close();

						}

					}

				}

			} catch (Exception e) {

				if (e instanceof ConstraintViolationException) {
					e.printStackTrace();

					JSONObject Error = new JSONObject();
					Error.put("Code", "100");
					Error.put("Description", "Nick Name already in Use2");
					Response.put("Error", Error);

					msg = "Duplicate Entry";
				}
				if (e instanceof SQLServerException) {
					// e.printStackTrace();
					String duplicate = "";
					String a = e.getMessage().toString();

					if (e.getMessage().contains("The duplicate key value is")) {
						System.out.println(a);
						int index1 = a.indexOf("The duplicate key value is");
						System.out.println("availble" + index1);
						System.out.println(a.substring(index1, a.length() - 1));
						Matcher m = Pattern.compile("\\((.*?)\\)").matcher(a);
						while (m.find()) {
							System.out.println(m.group(1));
							duplicate = m.group(1).toString();
						}
					}

					if (bb.getAccount().equals(duplicate)) {
						Description = "Beneficiary account number already available, please try with different account number1.";
					} else if (bb.getNickName().equals(duplicate)) {
						Description = "Nick Name already in Use1";
					}

					JSONObject Error = new JSONObject();
					Error.put("Code", "100");
					Error.put("Description", Description);
					Response.put("Error", Error);

				} else if (e instanceof DataIntegrityViolationException) {
					e.printStackTrace();
					if (e.getMessage().contains("duplicate")) {
						System.out.println("availble");
					}

					JSONObject Error = new JSONObject();
					Error.put("Code", "100");
					Error.put("Description", "Nick Name already in Use2");
					Response.put("Error", Error);

					msg = "Duplicate Entry";
				} else {

					e.printStackTrace();
					msg = "Server Error";
					JSONObject Error = new JSONObject();
					Error.put("Code", "500");
					Error.put("Description", "Server Error");
					Response.put("Error", Error);
				}

				// return a;
			} finally {

				// JDBCHelper.closeConnection(con);

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						/* Ignored */}
				}

				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						/* Ignored */}
				}
			}

		} else {
			JSONObject Error = new JSONObject();
			Error.put("Code", "100");
			Error.put("Description", "Please Enter Valid Account Type");
			Response.put("Error", Error);
		}
		// TODO Auto-generated method stub

		return Response;

	}

	public JSONObject modifyBenificier(String bm, String customerid) {

		String msg = "";
		JSONObject Response = new JSONObject();
		String Description = "";

		org.json.JSONObject jk = new org.json.JSONObject(bm);
		// System.out.println(jk.getJSONObject("Data").getString("Name").toString());
		org.json.JSONObject jm = jk.getJSONObject("Data");

		Benificier bb = new Benificier();
		bb.setSequence(jm.getString("Sequence"));
		// bb.setName(jm.getString("Name").toString());
		// bb.setAccount(jm.getString("Account").toString());
		bb.setNickName(jm.getString("NickName").toString());
		// bb.setBankCity(jm.getString("BankCity").toString());
		bb.setCustomerId(customerid);
		// bb.setAccountType(jm.getString("AccountType").toString());
		bb.setBankIfsc(jm.getString("BankIfsc").toString());
		// bb.setBankBranch(jm.getString("BankBranch").toString());
		bb.setMaxLimit(jm.getString("MaxLimit").toString());
		// bb.setBankName(jm.getString("BankName").toString());
		// bb.setType(jm.getString("Type"));
		bb.setStatus("V");
		// bb.setAddress1(jm.getString("Address1").toString());

		// bb.setCity(jm.getString("City"));
		// bb.setState(jm.getString("State"));
		// bb.setZip(jm.getString("Zip"));
		bb.setMobileNumber(jm.getString("MobileNumber"));
		bb.setEmailId(jm.getString("EmailId"));

		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
		bb.setLastupdateDate(timestamp);

		System.out.println(bb);

		Connection con = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {

			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// con = JDBCHelper.getConnection();
			con = DriverManager.getConnection(url, userName, password);
			// System.out.println("Driver name: a");
			if (con != null) {
				DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
				// System.out.println("Driver name: " + dm.getDriverName());
				// System.out.println("Driver version: " + dm.getDriverVersion());
				// System.out.println("Product name: " + dm.getDatabaseProductName());
				// System.out.println("Product version: " + dm.getDatabaseProductVersion());

				Statement s1 = con.createStatement();
				Long numberofcount = 0L;

				List<Benificier> listofrecords = benirepo.findByCustomerId(customerid);
				System.out.println(listofrecords.toString());
				if (listofrecords.size() == 0) {
					JSONObject Data = new JSONObject();
					Data.put("Status", "No Record Found");
					Response.put("Date", Data);
					return Response;

				} else {
					JSONObject Error = new JSONObject();
					Error.put("Code", "100");

					for (int i = 0; i < listofrecords.size(); i++) {
						Benificier benifier = listofrecords.get(i);
						if (benifier.getNickName().equals(bb.getNickName())) {

							if (benifier.getSequence().equals(bb.getSequence())) {
								System.out.println("match skip" + bb.getNickName());
								Description = "";
							} else {
								System.out.println("match already" + bb.getNickName());
								Description = "Nick Name already in Use ok ";

								Error.put("Description", Description);
								Response.put("Error", Error);
								return Response;

							}

						} else if (benifier.getAccount().equals(bb.getAccount())) {

							if (benifier.getSequence().equals(bb.getSequence())) {
								System.out.println("match skip" + bb.getNickName());
								Description = "";
							} else {

								Description = "Beneficiary account number already available, please try with different account number.";
								Error.put("Description", Description);
								Response.put("Error", Error);
								return Response;
							}

						}

					}

					if (Description == "") {
						// get list
						StringBuilder sql = new StringBuilder();

						sql.append("update Tbl_Benificier set ");

						if (!bb.getNickName().equals("")) {
							sql.append(" nick_name='" + bb.getNickName() + "'");
						}

						if (!bb.getMaxLimit().equals("")) {
							sql.append(",Max_Limit='" + bb.getMaxLimit() + "'");
						}

						if (!bb.getMobileNumber().equals("")) {
							sql.append(",mobile_number='" + bb.getMobileNumber() + "'");
						}

						if (!bb.getEmailId().equals("")) {
							sql.append(",email_id='" + bb.getEmailId() + "'");
						}

						sql.append(",status='A',lastupdate_date='" + bb.getLastupdateDate() + "' where customer_id='"
								+ bb.getCustomerId() + "' and sequence='" + bb.getSequence() + "'");

						// String sql = "update Tbl_Benificier set
						// NAME='"+bb.getName()+"',nick_name='"+bb.getNickName()+"',account='"+bb.getAccount()+"',Account_Type='"+bb.getType()+"',Bank_Branch='"+bb.getBankBranch()+"',Bank_City='"+bb.getBankCity()+"',
						// Bank_Ifsc='"+bb.getBankIfsc()+"',Max_Limit='"+bb.getMaxLimit()+"',Bank_Name='"+bb.getBankName()+"',type='"+bb.getType()+"',
						// status='"+bb.getStatus()+"',lastupdate_date='"+bb.getLastupdateDate()+"'
						// where customer_id='"+bb.getCustomerId()+"' and
						// sequence='"+bb.getSequence()+"'";

						System.out.println(sql.toString());
						int result = s1.executeUpdate(sql.toString());

						if (result > 0) {
							msg = "suceesfully";
							JSONObject Data = new JSONObject();
							Data.put("Status", "0");
							Response.put("Date", Data);
						} else {

							msg = "suceesfully";
							JSONObject Data = new JSONObject();
							Data.put("Status", "1");
							Response.put("Date", Data);

							// closing connection

						}

						con.close();

					}

				}

			}

		} catch (Exception e) {

			if (e instanceof ConstraintViolationException) {
				e.printStackTrace();

				JSONObject Error = new JSONObject();
				Error.put("Code", "100");
				Error.put("Description", "Nick Name already in Use2");
				Response.put("Error", Error);

				msg = "Duplicate Entry";
			}
			if (e instanceof SQLServerException) {
				// e.printStackTrace();
				String duplicate = "";
				String a = e.getMessage().toString();

				if (e.getMessage().contains("The duplicate key value is")) {
					System.out.println(a);
					int index1 = a.indexOf("The duplicate key value is");
					System.out.println("availble" + index1);
					System.out.println(a.substring(index1, a.length() - 1));
					Matcher m = Pattern.compile("\\((.*?)\\)").matcher(a);
					while (m.find()) {
						System.out.println(m.group(1));
						duplicate = m.group(1).toString();
					}
				}

				if (bb.getAccount().equals(duplicate)) {
					Description = "Beneficiary account number already available, please try with different account number1.";
				} else if (bb.getNickName().equals(duplicate)) {
					Description = "Nick Name already in Use1";
				}

				JSONObject Error = new JSONObject();
				Error.put("Code", "100");
				Error.put("Description", Description);
				Response.put("Error", Error);

			} else if (e instanceof DataIntegrityViolationException) {
				e.printStackTrace();
				if (e.getMessage().contains("duplicate")) {
					System.out.println("availble");
				}

				JSONObject Error = new JSONObject();
				Error.put("Code", "100");
				Error.put("Description", "Nick Name already in Use2");
				Response.put("Error", Error);

				msg = "Duplicate Entry";
			} else {

				e.printStackTrace();
				msg = "Server Error";
				JSONObject Error = new JSONObject();
				Error.put("Code", "500");
				Error.put("Description", "Server Error");
				Response.put("Error", Error);
			}

			// return a;
		} finally {

			// JDBCHelper.closeConnection(con);

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* Ignored */}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}

		// TODO Auto-generated method stub

		return Response;

	}

	@Override
	public List<Benificier> FindAllCustomer(String customerid) {
		// TODO Auto-generated method stub

		List<Benificier> a = new ArrayList<Benificier>();

		System.out.println("insdie service Imple");
		a = benirepo.findByCustomerId(customerid);

		return a;

	}

	@Override
	public List<Benificier> viewcustmerbytype(String customerid, String accounttype) {
		// TODO Auto-generated method stub
		List<Benificier> a = new ArrayList<Benificier>();
		String acc_type = "";
		System.out.println("insdie service Imple");

		a = benirepo.viewCustomerId(customerid, accounttype);

		return a;
	}

	@Override
	public JSONObject deleteBenificier(String bm) {
		// TODO Auto-generated method stub

		String msg = "";
		JSONObject Response = new JSONObject();
		String Description = "";

		org.json.JSONObject jk = new org.json.JSONObject(bm);

		org.json.JSONObject jm = jk.getJSONObject("Data");

		Benificier bb = new Benificier();
		bb.setSequence(jm.getString("BeneficiarySequence"));
		bb.setCustomerId(jm.getString("CustomerNumber"));
		bb.setType(jm.getString("BeneficiaryType"));

		Connection con = null;
		ResultSet rs = null;

		try {

			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// con = JDBCHelper.getConnection();
			con = DriverManager.getConnection(url, userName, password);
			// System.out.println("Driver name: a");
			if (con != null) {
				DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
				// System.out.println("Driver name: " + dm.getDriverName());
				// System.out.println("Driver version: " + dm.getDriverVersion());
				// System.out.println("Product name: " + dm.getDatabaseProductName());
				// System.out.println("Product version: " + dm.getDatabaseProductVersion());

				con.setAutoCommit(false);
				Statement s1 = con.createStatement();

				// delete record
				String sql = "delete from tbl_benificier where customer_id='" + bb.getCustomerId() + "' and  type='"
						+ bb.getType() + "' and sequence='" + bb.getSequence() + "'";

				System.out.println(sql);
				int result = s1.executeUpdate(sql);

				con.commit();
				if (result > 0) {
					msg = "suceesfully";
					JSONObject Data = new JSONObject();
					Data.put("Status", "0");
					Response.put("Data", Data);

				} else {

					msg = "suceesfully";
					JSONObject Data = new JSONObject();
					Data.put("Status", "1");
					Response.put("Data", Data);

					// closing connection

				}

				con.close();

			}

		} catch (Exception e) {

			e.printStackTrace();
			msg = "Server Error";
			JSONObject Error = new JSONObject();
			Error.put("Code", "500");
			Error.put("Description", "Server Error");
			Response.put("Error", Error);

			return Response;

			// return a;
		} finally {

			// JDBCHelper.closeConnection(con);

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* Ignored */}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}
		return Response;
	}

	@Override
	public void modifyBenificierStatus() {
		// TODO Auto-generated method stub
		JSONObject Response = new JSONObject();
		// find all record whose status is A
		// and update query to set V
		List<Benificier> listofrecords = benirepo.findByStatus("V");

		if (listofrecords.size() == 0) {
			System.out.println("No Reocrd Found ");
		} else {

			Connection con = null;
			ResultSet rs = null;

			try {

				// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

				// con = JDBCHelper.getConnection();
				con = DriverManager.getConnection(url, userName, password);
				// System.out.println("Driver name: a");
				if (con != null) {
					DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();

					Statement s1 = con.createStatement();
					// update query

					for (int i = 0; i < listofrecords.size(); i++) {

						Benificier bb = listofrecords.get(i);
						bb.getCustomerId();
						bb.getSequence();
						bb.getType();

						Calendar cal = Calendar.getInstance();
						java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
						bb.setLastupdateDate(timestamp);

						String sql = "update Tbl_Benificier set status='A', lastupdate_date='" + bb.getLastupdateDate()
								+ "' where  customer_id='" + bb.getCustomerId() + "' and  type='" + bb.getType()
								+ "' and sequence='" + bb.getSequence() + "'";

						System.out.println(sql);
						int result = s1.executeUpdate(sql);

						if (result > 0) {

							// System.out.println("record updated"+bb.getCustomerId());
						} else {

							// System.out.println("No record Found"+bb.getCustomerId());

							// closing connection

						}

					}

					con.close();

				}
			} catch (Exception e) {

				e.printStackTrace();

				JSONObject Error = new JSONObject();
				Error.put("Code", "500");
				Error.put("Description", "Server Error");
				Response.put("Error", Error);

				// return a;
			} finally {

				// JDBCHelper.closeConnection(con);

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						/* Ignored */}
				}

				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						/* Ignored */}
				}
			}

		}

	}

	public String loadpropertyfile() throws Exception {
		String sConfigFile = "props/dbconfig.properties";

		InputStream in = BenificierServiceImp.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			// File not found! (Manage the problem)
		}
		Properties prop = new Properties();
		prop.load(in);

//		String filePath = getClass().getClassLoader().getResource("props/uidai_admin_iris.properties").getPath();
//		Properties prop = new Properties();
//		prop.load(new FileInputStream(new File(filePath)));

		String url = "";
		url = prop.getProperty("dburl");
		System.out.println("myUrl" + url);

		return url;

	}
}
