package com.suryoday.roaocpv.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.ROAOCPVErrorResponseService;

@Service
public class ROAOCPVErrorResponseServiceImpl implements ROAOCPVErrorResponseService{

	
	@Override
	public String getError(String kycRes, long applicationno) {
		int errorcode = Integer.parseInt(ROAOCPVErrorResponseServiceImpl.KycRespCall(kycRes,applicationno));
		String jsonresponse = "";
		switch (errorcode) {
		case 300:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"300\",\"Description\":\"Biometric data did not match\"}}";
			break;
		case 330:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"330\",\"Description\":\"Biometrics locked by Aadhaar holder\"}}";
			break;
		case 527:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"527\",\"Description\":\"Invalid “mc” code under Meta tag\"}}";
			break;
		case 561:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"561\",\"Description\":\"Request expired\"}}";
			break;
		case 565:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"565\",\"Description\":\"AUA license has expired\"}}";
			break;
		case 951:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"951\",\"Description\":\"Biometric lock related technical error\"}}";
			break;
		case 998:
			jsonresponse = "{\"Data\":{\"ResponseCode\":\"998\",\"Description\":\"Invalid Aadhaar Number\"}}";
			break;
//		default:
//			jsonresponse = "{\"Data\":{\"ResponseCode\":\"300\",\"Description\":\"Data Error\"}}";
		}
		System.out.println("bio_data_eror_code"+jsonresponse);
	//	JSONObject response=new JSONObject(jsonresponse);
		return jsonresponse;
	}
private static String KycRespCall(String HmsData,long applicationno) {
	ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
	  x.getappprop();
	  x.bypassssl();
		byte[] valueDecoded = Base64.decodeBase64(HmsData);
		String Rar = new String(valueDecoded);
		
		Document doc = convertStringToXMLDocument(Rar);
		
		

		writeXmlDocumentToFile(doc, x.temp+""+applicationno+".xml");

		String SendResponseCode = new String(RarRespCall(applicationno));
		return SendResponseCode;

	}

	private static String RarRespCall(long applicationno) {
		ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
		  x.getappprop();
        x.bypassssl();
		String kycRes = "";
		String Rar = "";
		try {
			
			File stocks = new File(x.temp+""+applicationno+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("KycRes");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					kycRes = getValue("Rar", element).toString();
					byte[] valueDecoded = Base64.decodeBase64(kycRes);
					Rar = new String(valueDecoded);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		
		Document doc = convertStringToXMLDocument(Rar);
		applicationno++;
		writeXmlDocumentToFile(doc,x.temp+""+applicationno+".xml");

		String SendResponseCode = new String(ErrorCall(applicationno));
		return SendResponseCode;

	}

	private static String ErrorCall(long applicationno) {
		String SendResponseCode = "";
		ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
		  x.getappprop();
        x.bypassssl();
		try {
			File stocks = new File(x.temp+""+applicationno+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("AuthRes");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String ResponseCode = new String(element.getAttribute("err"));
					SendResponseCode = ResponseCode;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SendResponseCode;
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

	public static String convertXmlDomToString(Document xmlDocument) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();

			StringWriter writer = new StringWriter();

			transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

			return writer.getBuffer().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
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