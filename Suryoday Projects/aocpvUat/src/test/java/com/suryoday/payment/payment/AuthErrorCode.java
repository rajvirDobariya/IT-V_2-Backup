package com.suryoday.payment.payment;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AuthErrorCode {
	public static void main(String[] args) {
		String HmsData = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PEt5Y1JlcyBjb2RlPSIyZGM2MGQ3MDcwYzE0NjlmYWIxMmFkZWNhOTJmMTllYSIgZXJyPSJLLTEwMCIgcmV0PSJOIiB0cz0iMjAyMy0wNS0yM1QxMTo1MTo0My42ODQrMDU6MzAiIHR4bj0iVUtDOjg5ODkzMjIwMjMwNTIzIj48UmFyPlBEOTRiV3dnZG1WeWMybHZiajBpTVM0d0lpQmxibU52WkdsdVp6MGlWVlJHTFRnaVB6NDhRWFYwYUZKbGN5QmpiMlJsUFNJeVpHTTJNR1EzTURjd1l6RTBOamxtWVdJeE1tRmtaV05oT1RKbU1UbGxZU0lnWlhKeVBTSXpNREFpSUdsdVptODlJakEwZXpBeE1EQXlNREV3Wkc0M1RHMUlVVzAwUXpGeFUyaEpWMUU1VTJwT05FSXJka1Z4UzA5U2N6TkpPV3hQTkd4WFJEWmxVMFZHY2pKRk1VUllkbkkxWlU0NGQyTnRMMWR5Uml4QkxHVXpZakJqTkRReU9UaG1ZekZqTVRRNVlXWmlaalJqT0RrNU5tWmlPVEkwTWpkaFpUUXhaVFEyTkRsaU9UTTBZMkUwT1RVNU9URmlOemcxTW1JNE5UVXNNREV3TURBd016QXdNREF3TURBeE1Dd3lMakFzTWpBeU16QTFNak14TVRVeE16QXNNU3d4TERBc01Dd3lMalVzTVRGbFpqVmpPRE0yTnpjNVpHRm1NMlZoTWpVeE1UWXhNell6WkRVNVlqUXdNR0prT1dOaE5URXpOelkyT1RVMk1UUm1NVEZpWXpZM01tVmlNakUyT1N3ME5HUXlZVEk0WXpJM01EbG1Nekl6WVdNd1pEbGtOMkUyTWpBME9HVTNNRFV5TW1Vd1lqZzFNemc1TXpGbVpqRTVOR0kwTXpabVltVXlaamN6WkRaa0xEUTBaREpoTWpoak1qY3dPV1l6TWpOaFl6QmtPV1EzWVRZeU1EUTRaVGN3TlRJeVpUQmlPRFV6T0Rrek1XWm1NVGswWWpRek5tWmlaVEptTnpOa05tUXNUa0VzVGtFc1RrRXNUa0VzVGtFc1RrRXNUa0VzVGtFc1RrRXNUa0VzY21WbmFYTjBaWEpsWkN4RlZrOU1WVlJGTGtGT1JDNHdNREVzTVM0d0xqRXhMRVZXVDB4VlZFVXVSVlpQVEZWVVJTeEpSRVZPVkVrMUxFd3dMRGxSYnpCamJXOUVRbWwyZG10alZXczVVV3RLWnpWbFkxaDBTa1JvVkc0eE5tcGFZMmQwZGtoemRGRTlmU0lnY21WMFBTSnVJaUIwY3owaU1qQXlNeTB3TlMweU0xUXhNVG8xTVRvME15NDRNVElyTURVNk16QWlJSFI0YmowaVZVdERPamc1T0Rrek1qSXdNak13TlRJeklqNDhVMmxuYm1GMGRYSmxJSGh0Ykc1elBTSm9kSFJ3T2k4dmQzZDNMbmN6TG05eVp5OHlNREF3THpBNUwzaHRiR1J6YVdjaklqNDhVMmxuYm1Wa1NXNW1iejQ4UTJGdWIyNXBZMkZzYVhwaGRHbHZiazFsZEdodlpDQkJiR2R2Y21sMGFHMDlJbWgwZEhBNkx5OTNkM2N1ZHpNdWIzSm5MMVJTTHpJd01ERXZVa1ZETFhodGJDMWpNVFJ1TFRJd01ERXdNekUxSWk4K1BGTnBaMjVoZEhWeVpVMWxkR2h2WkNCQmJHZHZjbWwwYUcwOUltaDBkSEE2THk5M2QzY3Vkek11YjNKbkx6SXdNREF2TURrdmVHMXNaSE5wWnlOeWMyRXRjMmhoTVNJdlBqeFNaV1psY21WdVkyVWdWVkpKUFNJaVBqeFVjbUZ1YzJadmNtMXpQanhVY21GdWMyWnZjbTBnUVd4bmIzSnBkR2h0UFNKb2RIUndPaTh2ZDNkM0xuY3pMbTl5Wnk4eU1EQXdMekE1TDNodGJHUnphV2NqWlc1MlpXeHZjR1ZrTFhOcFoyNWhkSFZ5WlNJdlBqd3ZWSEpoYm5ObWIzSnRjejQ4UkdsblpYTjBUV1YwYUc5a0lFRnNaMjl5YVhSb2JUMGlhSFIwY0RvdkwzZDNkeTUzTXk1dmNtY3ZNakF3TVM4d05DOTRiV3hsYm1NamMyaGhNalUySWk4K1BFUnBaMlZ6ZEZaaGJIVmxQbTF4ZDIwMlNVbG9WelZGY1ZOQlRrb3JVWEpYV2taT1ZIaDJlV3hFT0hveE1ISlBRemcyUzFsMFpsRTlQQzlFYVdkbGMzUldZV3gxWlQ0OEwxSmxabVZ5Wlc1alpUNDhMMU5wWjI1bFpFbHVabTgrUEZOcFoyNWhkSFZ5WlZaaGJIVmxQa3gwUjNOM1VIb3pMMjE2YUhJMkwwa3lRWFIzZW1WeE5DdGlZamwxV1RaRVV6RkNlV0p5TldsVlIyTmFVelJIYzA1SmRHRkdOelJVV21KSkt6Qk1jbUZqY0hFd1ltc3lPRXRMYldNS2JGTnpZbWxNYkVKdlkwNHpUaXRpVkZCSWRtbERWR3QwY2s1NldXRmpNVkE0VG5Sd1N5ODFXazEwUVhGcGRsVkhWME54Y0ZkdFFsUmpPRXMwZEU5cldqbHBVekpQVERKVVlWcFRVd29yYWpGd1ZUaDRaREpYTm1VM1IzbFZSMDlUUTJGSU5tcE1aWHBsT1dOV0swNVhaV1Z2UTNGRGRXMTNkbFpyZDNwdVQwcHhVbkV4WW1wM1ltcE5ValU1WVU5amJWRnlXRWt4VmpadkNrZHZNWE01TjFCMFpYSTVRMHhxTjBsMmVuQnFWV1JrU0VsWlVIbG9OVkJOTWpObWVYQXhjMlkxWVV4V1RqbHVlVU5aZURGMVJrRmpTbkpTWmxWV2NWZE9iRFJsVFdKclExUlFaamNLZEZoU2JVTndSSFZHVTBkS1JrZHlWRk5RYUVwa1V5OUtkVTVVUXpFcmNVeE5WSGxUWkhjOVBUd3ZVMmxuYm1GMGRYSmxWbUZzZFdVK1BDOVRhV2R1WVhSMWNtVStQQzlCZFhSb1VtVnpQZz09PC9SYXI+PFNpZ25hdHVyZSB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PFNpZ25lZEluZm8+PENhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy14bWwtYzE0bi0yMDAxMDMxNSIvPjxTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjcnNhLXNoYTEiLz48UmVmZXJlbmNlIFVSST0iIj48VHJhbnNmb3Jtcz48VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz48L1RyYW5zZm9ybXM+PERpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTI1NiIvPjxEaWdlc3RWYWx1ZT5EQ3VnQmg4cDFQY2RaYWVCOU5jdFhBMTZHaGxWTk1yWU9uUTB3djNWNFEwPTwvRGlnZXN0VmFsdWU+PC9SZWZlcmVuY2U+PC9TaWduZWRJbmZvPjxTaWduYXR1cmVWYWx1ZT5Qbm8zaVpCT0RncGdvWUxVL1dzeUMraTZUbGpNd1hsVU1hRUFXV3owYTQ3dklMcm5Kek1PZjBBR1dJL0locFhybnlLSTFEQmd1cTVzCjdGMlhxVUZEZGVNTk1BeS9yczdUVXVmU0V2OWdHN3ZUdHFxN1prVDM1UGlXdzIvSUZNTjVKM21CV2JPejlWRGdjQ0ZvL1B6RnJLaGEKYnN4dis3RVpKZkphcVJicWxYKzNyanAyMlhnNjdQNTI4RkF4eWNUTENpYStGWS9raWR1NzV1aTdOUFNlUTVDWkRNbWNFQTlITTFkdQp4VHlwWlJKeU5Gbzc1cVNnaWgrTFdqdDVpWWlRSHZ6ZnRjT0VuSXJ4VGJQOUxiWkN3MjZ3MU82M1hjZHBST3R1a0MvMU5uN1JEZlQwCjk0ZlU5bU1hcXFzb1FYOWtKZ0dNYVFTZ0J4bDluQUdFSzhtNzRRPT08L1NpZ25hdHVyZVZhbHVlPjxLZXlJbmZvPjxYNTA5RGF0YT48WDUwOVN1YmplY3ROYW1lPjEuMi44NDAuMTEzNTQ5LjEuOS4xPSMxNjE2NjE2NDYxNzU3NDY4MmU3NDYzNDA3NTY5NjQ2MTY5MmU2ZTY1NzQyZTY5NmUsTD1CYW5nYWxvcmUsQ049VUlEQUksTz1VSURBSSxPVT1UZWNobm9sb2d5IENlbnRyZSxTVD1LYXJuYXRha2EsQz1JTjwvWDUwOVN1YmplY3ROYW1lPjxYNTA5Q2VydGlmaWNhdGU+TUlJRnR6Q0NCSitnQXdJQkFnSUdOMENyT0tZdk1BMEdDU3FHU0liM0RRRUJDd1VBTUlIaE1Rc3dDUVlEVlFRR0V3SkpUakVtTUNRRwpBMVVFQ2hNZFZtVnlZWE41Y3lCVVpXTm9ibTlzYjJkcFpYTWdVSFowSUV4MFpDNHhIVEFiQmdOVkJBc1RGRU5sY25ScFpubHBibWNnClFYVjBhRzl5YVhSNU1ROHdEUVlEVlFRUkV3WTBNREF3TWpVeEZEQVNCZ05WQkFnVEMwMWhhR0Z5WVhOb2RISmhNUkl3RUFZRFZRUUoKRXdsV0xsTXVJRTFoY21jeE1qQXdCZ05WQkRNVEtVOW1abWxqWlNCT2J5NGdNakVzSURKdVpDQkdiRzl2Y2l3Z1FtaGhkbTVoSUVKMQphV3hrYVc1bk1Sd3dHZ1lEVlFRREV4TldaWEpoYzNseklGTjFZaUJEUVNBeU1ESXlNQjRYRFRJek1EUXlPREE1TkRBME5sb1hEVEkyCk1EUXlPREE1TkRBME5sb3dnWmd4Q3pBSkJnTlZCQVlUQWtsT01SSXdFQVlEVlFRSUV3bExZWEp1WVhSaGEyRXhHakFZQmdOVkJBc1QKRVZSbFkyaHViMnh2WjNrZ1EyVnVkSEpsTVE0d0RBWURWUVFLRXdWVlNVUkJTVEVPTUF3R0ExVUVBeE1GVlVsRVFVa3hFakFRQmdOVgpCQWNUQ1VKaGJtZGhiRzl5WlRFbE1DTUdDU3FHU0liM0RRRUpBUllXWVdSaGRYUm9MblJqUUhWcFpHRnBMbTVsZEM1cGJqQ0NBU0l3CkRRWUpLb1pJaHZjTkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFMZW5vdUVKM25PMXRwMWpXR3QzZXM4RGMwejdheDZJSWRZZ0wrdHMKblZvT0t4U2xHQm9jVCsvYklUSm5NM2Z4SC9Jby9McUZGRlNyd281OTBDOFZaNnpxb1VHTWpRcDlGVzFNTlZsL0RZOVNJaldraFRLRgo3UzZxaGJyTTQzcHhwUzRqYnhpRzJBTURNdkxYeURvT25VWEZ4VGZ0M3lLMWxaenMremxBT2Q3U0pCVU1sZElDRThMY2FxeXJaMTBBCkc3Z0NDNGRXSWxWVEIrQmZ6ZEFvY2VkTkxXYVFIekJsRUFTcE1HejlvMDNNUEE4T0wzZWpNU3BjNitORGVUV0dodHpZTWY5d0Vram8KOHozRGIweUQrZGwrcWQvZHI0SmdzU20zM08vU0haQWZORU1hcFR6cklRTTB1Uk9kSk9iZVdEcFhOMDg2dHlhdlU1RGZ2elYxTnNVQwpBd0VBQWFPQ0Fib3dnZ0cyTUVBR0ExVWRKUVE1TURjR0Npc0dBUVFCZ2pjVUFnSUdDQ3NHQVFVRkJ3TUVCZ29yQmdFRUFZSTNDZ01NCkJna3Foa2lHOXk4QkFRVUdDQ3NHQVFVRkJ3TUNNQk1HQTFVZEl3UU1NQXFBQ0UwdVdPR0ZMZjZLTUhBR0NDc0dBUVVGQndFQkJHUXcKWWpBZ0JnZ3JCZ0VGQlFjd0FZWVVhSFIwY0RvdkwyOWpjM0F1ZG5OcFoyNHVhVzR3UGdZSUt3WUJCUVVITUFLR01taDBkSEJ6T2k4dgpkM2QzTG5aemFXZHVMbWx1TDNKbGNHOXphWFJ2Y25rdmRuTnBaMjV6ZFdKallUSXdNakl1WTJWeU1JR0xCZ05WSFNBRWdZTXdnWUF3CmRBWUdZSUprWkFJRE1Hb3dMd1lJS3dZQkJRVUhBZ0VXSTJoMGRIQnpPaTh2ZDNkM0xuWnphV2R1TG1sdUwzSmxjRzl6YVhSdmNua3YKWTNCek1EY0dDQ3NHQVFVRkJ3SUNNQ3NhS1VOc1lYTnpJRWxKU1NCUGNtZGhibWw2WVhScGIyNGdVMmxuYm1WeUlFTmxjblJwWm1sagpZWFJsTUFnR0JtQ0NaR1FDQWpBdkJnTlZIUjhFS0RBbU1DU2dJcUFnaGg1b2RIUndjem92TDJOaExuWnphV2R1TG1sdUwyTnliR1J6Cll6SXdNakl3RVFZRFZSME9CQW9FQ0UyLzBQNXdUYnAvTUE0R0ExVWREd0VCL3dRRUF3SUd3REFKQmdOVkhSTUVBakFBTUEwR0NTcUcKU0liM0RRRUJDd1VBQTRJQkFRQVgzWVJhdVdCZXo1a0w0ZERSbUV1TlExN0gvZk9OYlRPZlBpenNKTThjOWlTOWtESmsyYzRmSmxYbgpNMFFRY0tQR1VUbzZmSXVReWlTakNvRU9ycEx1YlZNQ1JZWW91STB0WUk0Z3dKclk2VUxUbVNhNjBHeFp1Zkd3OUN5b0d5OHVtUTlHCkV6aWkzd2tPVG9XdXpBcWFoL0lEUFgyWWxUTDZoZGxlOGRmT0RjYm9UcythdzRod3R0ak93L2VqNCtydmI5U2NXYzliSzZzKzJvcVkKODdnWmppL0VKd21LbHB2NmxPdXJ3NEYrWFJraVM1a2VCNXI0dllFTzJhRFk1NlFyUlMxUGkzclhGMjhyV1Y5R0N6VndmMFBrMjNBTApBWlBKekJsWFk4K214Rnd4RGw0YVNiKzJMM2dUTUg0dnp0eUdhRy9LQ2pmUXdNcXV1b05WMWpaSDwvWDUwOUNlcnRpZmljYXRlPjwvWDUwOURhdGE+PC9LZXlJbmZvPjwvU2lnbmF0dXJlPjwvS3ljUmVzPg==";

		int errorcode = Integer.parseInt(AuthErrorCode.KycRespCall(HmsData));
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
//			jsonresponse = "{\"KycRes\":{\"ResponseCode\":\"300\",\"ResponseMessage\":\"Data Error\"}}";
		}
		System.out.println(jsonresponse);
	}

	private static String KycRespCall(String HmsData) {

		byte[] valueDecoded = Base64.decodeBase64(HmsData);
		String Rar = new String(valueDecoded);

		Document doc = convertStringToXMLDocument(Rar);

		writeXmlDocumentToFile(doc, " XML1.xml");

		String SendResponseCode = new String(RarRespCall());
		return SendResponseCode;

	}

	private static String RarRespCall() {
		String kycRes = "";
		String Rar = "";
		try {
			File stocks = new File(" XML1.xml");
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

		writeXmlDocumentToFile(doc, " XML2.xml");

		String SendResponseCode = new String(ErrorCall());
		return SendResponseCode;

	}

	private static String ErrorCall() {
		String SendResponseCode = "";
		try {
			File stocks = new File(" XML2.xml");
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
