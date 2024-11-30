package com.suryoday.Counterfeit.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.Counterfeit.Exception.ErrorResponse;

public class FilterConfiguration extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equals("OPTIONS")) {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "unatharised Access");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(401);
			errorResponse.setMessage("Unauthorized Access");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getOutputStream().write(responseToSend);
			return;
		} else {
//			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://sarathi.suryodaybank.com");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://sarathi.suryodaybank.co.in");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST");

			// Set other security headers
			response.setHeader("Strict-Transport-Security", "false");
			response.setHeader("X-Frame-Options", "");
			response.setHeader("X-Content-Type-Options", "nosniff");
			response.setHeader("Content-Security-Policy", "src");
			response.setHeader("X-XSS-Protection", "0");

			filterChain.doFilter(request, response);
		}

	}

	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException, JsonProcessingException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}

}
