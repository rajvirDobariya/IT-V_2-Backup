package com.suryoday.aocpv.serviceImp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.Benificier;
import com.suryoday.aocpv.pojo.Preapproval;
import com.suryoday.aocpv.repository.AocpvRepository;
import com.suryoday.aocpv.service.AocpService;

@Service
public class AocpvServiceImp implements AocpService {
	
	  
	    String userName = "";
		String password = "";
		String url = "";
		
		//String userName = "sunsmart";
		//String password = "sunsmart@123";
//		

		//String url = "jdbc:sqlserver://10.20.72.17:16333;databaseName=SuryodayMBDB;encrypt=false;sslProtocol=TLSv1.2";
		


	@Autowired
	AocpvRepository aocpvrepo;
	


	@Override
	public JSONObject checkappversion(String bm) {
		// TODO Auto-generated method stub
		
		String msg="";
		JSONObject Response = new JSONObject();
		String Description="";
		
		 org.json.JSONObject jk = new org.json.JSONObject(bm);
		
		 org.json.JSONObject jm = jk.getJSONObject("Data");
		 
		 
		 
		 Benificier bb= new Benificier();
		 bb.setSequence(jm.getString("BeneficiarySequence"));
		 bb.setCustomerId(jm.getString("CustomerNumber"));
         bb.setType(jm.getString("BeneficiaryType"));
      
         
		
         Connection con = null;
   		ResultSet rs = null;
         
   		try {

  			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
  			
  			// con = JDBCHelper.getConnection();
  			con = DriverManager.getConnection(url, userName, password);
  			//System.out.println("Driver name: a");
  			if (con != null) {
  				DatabaseMetaData dm = (DatabaseMetaData) con.getMetaData();
  				//System.out.println("Driver name: " + dm.getDriverName());
  				//System.out.println("Driver version: " + dm.getDriverVersion());
  				//System.out.println("Product name: " + dm.getDatabaseProductName());
  				//System.out.println("Product version: " + dm.getDatabaseProductVersion());

  				con.setAutoCommit(false); 
  				Statement s1 = con.createStatement();
  				
  				
  				
  				    
  					//delete  record 
  				String sql ="delete from tbl_benificier where customer_id='"+bb.getCustomerId()+"' and  type='"+bb.getType()+"' and sequence='"+bb.getSequence()+"'";
  					  					
  					System.out.println(sql);
  					int result = s1.executeUpdate(sql);
  			         
  					 con.commit(); 
  			         if (result > 0)
  			         {
  						msg="suceesfully";
  						JSONObject Data = new JSONObject();
  						Data.put("Status", "0");
  						Response.put("Data", Data);
  						
  						
  						
  			         }
  			            else
  			            {
  			            	
  						msg="suceesfully";
  						JSONObject Data = new JSONObject();
  						Data.put("Status", "1");
  						Response.put("Data", Data);
  			  
  						
  			            // closing connection
  			           
  			            }


  				con.close();

  				}
  				

  			
  		} 
  		catch (Exception e) {
  			
  			    e.printStackTrace();
  			    msg="Server Error";
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



	public String loadpropertyfile() throws Exception {
		String sConfigFile = "props/dbconfig.properties";

		InputStream in = AocpvServiceImp.class.getClassLoader().getResourceAsStream(sConfigFile);
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



	@Override
	public List<Preapproval> preapprovallist() {
		// TODO Auto-generated method stub
        List<Preapproval> a= new ArrayList<Preapproval>();
		
		System.out.println("insdie service Imple");
		a= aocpvrepo.findAlla();
		System.out.println(a.toString());
		return a;
	}


}
