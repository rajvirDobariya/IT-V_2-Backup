package com.suryoday.twowheeler.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.service.TwowheelerAmlService;

@Service
public class TwowheelerAmlServiceImpl implements TwowheelerAmlService {

	private static Logger logger = LoggerFactory.getLogger(TwowheelerAmlServiceImpl.class);

	@Override
	public JSONObject Amlcase(TwoWheelerFamilyMember member) {
		JSONObject sendResponse = new JSONObject();
		JSONObject parentRequest = parentRequest(member);
		URL obj = null;
		try {
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			obj = new URL(x.BASEURL + "customer/aml/query/v2?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "customer/aml/query/v2?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "TAB");
			sendResponse = getResponseData(parentRequest, sendResponse, con, "POST");
			File file = new File("Score.xml");
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;
	}

	private JSONObject parentRequest(TwoWheelerFamilyMember twoWheelerFamilyMember) {

		JSONObject jsonResponse = new JSONObject();
		org.json.JSONObject data = new org.json.JSONObject();
		data.put("BranchId", "");
		data.put("CreationDateTime", LocalDate.now());
		data.put("EntityType", "1");
		data.put("RequestorName", "Suryoday");
		data.put("RequestType", "");

		org.json.JSONObject Customer = new org.json.JSONObject();
		Customer.put("DateOfBirth", twoWheelerFamilyMember.getDob());
		Customer.put("FirstName", twoWheelerFamilyMember.getFirstName());
		Customer.put("LastName", twoWheelerFamilyMember.getLastName());
		Customer.put("MiddleName", twoWheelerFamilyMember.getMiddleName());
		Customer.put("UCIC", "");
		data.put("Customer", Customer);
		String city = "";
		String landmark = "";
		String address_Line1 = "";
		String address_Line2 = "";
		String pincode = "";
		String addressType = "";

//		if(twoWheelerFamilyMember.getAddressArray()!=null) {
//			JSONArray addressArray=new JSONArray(twoWheelerFamilyMember.getAddressArray());	
//			for(int i=0;i<addressArray.length();i++) {
//				JSONObject jsonObject = addressArray.getJSONObject(i);
//				if(jsonObject.getString("addressType").equals("Permanent ADDRESS")) {
//					city=jsonObject.getString("city");
//					landmark=jsonObject.getString("landmark");
//					address_Line1=jsonObject.getString("address_Line1");
//					address_Line2=jsonObject.getString("address_Line2");
//					pincode=jsonObject.getString("pincode");
//					addressType=jsonObject.getString("addressType");
//				}
//			}
//		}

		org.json.JSONArray PostalAddress = new org.json.JSONArray();
		org.json.JSONObject PostalAddress0 = new org.json.JSONObject();
		PostalAddress0.put("AddressLine", address_Line1);
		PostalAddress0.put("BuildingNumber", address_Line2);
		PostalAddress0.put("City", city);
		PostalAddress0.put("Country", "india");
		PostalAddress0.put("CountrySubDivision", "");
		PostalAddress0.put("Department", "");
		PostalAddress0.put("Landmark", landmark);
		PostalAddress0.put("PostCode", pincode);
		PostalAddress0.put("StreetName", "");
		PostalAddress0.put("SubDepartment", "");
		PostalAddress0.put("TownName", "");
		PostalAddress0.put("Type", "");
		org.json.JSONObject GeoLocation = new org.json.JSONObject();
		GeoLocation.put("Latitude", "");
		GeoLocation.put("Longitude", "");
		PostalAddress0.put("GeoLocation", GeoLocation);
		PostalAddress.put(PostalAddress0);
		Customer.put("PostalAddress", PostalAddress);

		org.json.JSONArray Identification_Type = new org.json.JSONArray();

		org.json.JSONObject Identification_Type0 = new org.json.JSONObject();
		Identification_Type0.put("IdentityNumber", twoWheelerFamilyMember.getPanCard());
		Identification_Type0.put("IdentityType", "PAN");
		Identification_Type.put(Identification_Type0);

		org.json.JSONObject Identification_Type1 = new org.json.JSONObject();
		Identification_Type1.put("IdentityNumber", twoWheelerFamilyMember.getAadharCard());
		Identification_Type1.put("IdentityType", "AADHAAR");
		Identification_Type.put(Identification_Type1);

		org.json.JSONObject Identification_Type2 = new org.json.JSONObject();
		Identification_Type2.put("Category", "");
		Identification_Type2.put("Country", "");
		Identification_Type2.put("EmbossedName", "");
		Identification_Type2.put("ExpiryDate", "");
		Identification_Type2.put("IdentityNumber", "");
		Identification_Type2.put("IdentityType", "PASSPORT");
		Identification_Type2.put("IssueDate", "");
		Identification_Type2.put("IssuedBy", "");
		Identification_Type2.put("PlaceOfIssue", "");
		Identification_Type2.put("SubCategory", "");
		Identification_Type.put(Identification_Type2);

		org.json.JSONObject Identification_Type3 = new org.json.JSONObject();
		Identification_Type3.put("Category", "VOTERID");
		Identification_Type3.put("IdentityNumber", "");
		Identification_Type3.put("IdentityType", "OTHER");
		Identification_Type.put(Identification_Type3);

		Customer.put("Identification_Type", Identification_Type);

		jsonResponse.put("Data", data);
		return jsonResponse;
	}

	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {

			InputStream responseStream = con.getResponseCode() / 100 == 2 ? con.getInputStream() : con.getErrorStream();
			Scanner s = new Scanner(responseStream).useDelimiter("\\A");
			String response = s.hasNext() ? s.next() : "";

			JSONObject sendResponse = new JSONObject(response);
//			String ScoreMessage = sendResponse.getJSONObject("Data").getString("ScoreMessage");
//			Document doc = convertStringToXMLDocument(ScoreMessage);
//			writeXmlDocumentToFile(doc, "Score.xml");

//			String Score = "";

//			try {
//				File stocks = new File("Score.xml");
//				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//				Document doc1 = dBuilder.parse(stocks);
//				doc1.getDocumentElement().normalize();
//				NodeList nodes = doc1.getElementsByTagName("Response");
//				for (int i = 0; i < nodes.getLength(); i++) {
//					Node node = nodes.item(i);
//					if (node.getNodeType() == Node.ELEMENT_NODE) {
//						Element element = (Element) node;
//						Score = getValue("Score", element).toString();
//						System.out.println("\"AmlScore\": \""+ Score+"\"");
//					}
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
			JSONObject sendauthenticateResponse1 = new JSONObject(response.toString());
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("POST request not worked");
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

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

	private static Document convertStringToXMLDocument(String xmlString) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		try {

			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void writeXmlDocumentToFile(Document xmlDocument, String fileName) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();

			FileOutputStream outStream = new FileOutputStream(new File(fileName));

			transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
