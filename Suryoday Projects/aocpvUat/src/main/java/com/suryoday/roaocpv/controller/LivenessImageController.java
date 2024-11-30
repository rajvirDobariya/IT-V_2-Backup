package com.suryoday.roaocpv.controller;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.roaocpv.service.LivenessImageService;

@RestController
@RequestMapping(value = "/roaocpv")
public class LivenessImageController {

	@Autowired
	LivenessImageService livenessImageService;

	@Autowired
	AocpvImageService aocpvImageService;

	@RequestMapping(value = "/livenessOfImage", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> faceMatch(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-Correlation-ID", X_Correlation_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		long applicationNoInLong = Long.parseLong(applicationNo);
		AocpvImages aocpvImage = aocpvImageService.getByApplicationNoAndName(applicationNoInLong, documentType);

		byte[] images = aocpvImage.getImages();
		String encoded = Base64.getEncoder().encodeToString(images);

		JSONObject livenessOfimage = livenessImageService.checkLivenessOfImage(encoded, Header);

		HttpStatus h = HttpStatus.OK;
		if (livenessOfimage != null) {
			String Data2 = livenessOfimage.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
