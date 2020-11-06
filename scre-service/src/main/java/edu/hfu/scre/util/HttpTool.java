package edu.hfu.scre.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpTool {
	private static ObjectMapper objectMapper;
    static {
    	objectMapper = new ObjectMapper();
    }

	public static void writeToClient(HttpServletResponse response,Map<String,String> messMap) throws IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String jsonStr=objectMapper.writeValueAsString(messMap);
		out.write(jsonStr);
		out.flush();
		out.close();
	}
	public static void writeToClient(HttpServletResponse response,String mess) throws IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(mess);
		out.flush();
		out.close();
	}
}
