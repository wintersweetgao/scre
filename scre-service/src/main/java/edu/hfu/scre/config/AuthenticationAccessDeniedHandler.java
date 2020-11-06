package edu.hfu.scre.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import edu.hfu.scre.util.HttpTool;

@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		accessDeniedException.printStackTrace();
//		Map<String,String> mess=new HashMap<>();
//		mess.put("mess", "权限不足，请联系管理员!");
		HttpTool.writeToClient(response, "权限不足，请联系管理员!");
	}

}
