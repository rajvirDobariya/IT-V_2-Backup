package com.suryoday.customerOnboard.others;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component
public class EncryptionInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
//    	String key = request.getHeader("X-Session-ID");
//    	String encryptedRequestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//    	org.json.JSONObject encryptJSONObject = new org.json.JSONObject(encryptedRequestBody);
//		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
//		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
//		JSONObject requestBody=new JSONObject(decryptContainerString);
//		ObjectMapper objectMapper = new ObjectMapper();
//		String req = objectMapper.writeValueAsString(requestBody);
//		 HttpServletRequest newRequest = new CustomHttpServletRequestWrapper(request, req);
        return true; // Continue with the request
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Intercept outgoing responses
        // Apply encryption logic if necessary
        // For example, encrypt response body
        // Modify the response accordingly
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cleanup tasks if needed
    }
    
}
