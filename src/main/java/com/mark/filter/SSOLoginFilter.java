package com.mark.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.json.JSONObject;


import com.alibaba.fastjson.JSONException;


/**
 * 单点登录过滤
 * 认证中心地址:http://lcalhost:8080/web-consumer/SSOAuthCenter		//该地址是另外一个项目中
 * @author Mark
 *
 */
public class SSOLoginFilter implements Filter {

	private Logger logger = Logger.getLogger(SSOLoginFilter.class);
	
	private String ssoServiceURL;	//认证中心的地址
	
	private String ssoLogin;		//sso登录页面
	
	private String cookieName;		//cookie名
	
	/*
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ssoServiceURL = "http://lcalhost:8080/web-consumer/SSOAuthCenter";		//fConfig.getInitParameter("SSOService");
		ssoLogin = "http://passport.ghsau.com:8080/WebSSOAuth/login.jsp";			//fConfig.getInitParameter("SSOLogin");
		cookieName = "SSOID";		//fConfig.getInitParameter("cookieName");
		
	}

	/*
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest  request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		//获取完整的请求路径和所有参数列表
		String params = request.getQueryString();
		//如果参数不为空：URL=ssoLgon?gotoURL+请求路径+参数,	如果参数为空:URL=ssoLgon?gotoURL+请求路径
		String URL = ssoLogin + "?gotoURL="+(params != null ? request.getRequestURL().append("?").append(params).toString() : request.getRequestURL().toString()) ;
		
		Cookie ticket = null;
		Cookie[] cooks = request.getCookies();
		if(cooks != null){
			//遍历Cookies，拿到令牌值
			for(Cookie cook : cooks){
				if(cook.getName().equals("cookieName")){
					ticket = cook;
					break;
				}
			}
		}
		//令牌不为空，就验证令牌
		if(ticket != null){
			validateTicke(request,  response,  chain, ticket,  URL);
		//否则转到-->登录页面
		}else{
			response.sendRedirect(URL);
		}
		//如果是退出系统
		if(request.getRequestURI().equals(request.getContextPath()+"/logout")){
			//doLogOut();
		}
	
	}

	/*
	 * 根据令牌的SSOID验证可用性
	 */
	private void validateTicke(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Cookie ticket, String uRL) throws IOException, ServletException {
		//NameValuePair：存储并串联Name/Value对
		NameValuePair[] params = new NameValuePair[2];
		params[0] = new NameValuePair("action", "validateTicket");
		params[1] = new NameValuePair("cookieName", ticket.getValue());
		//使用post请求到sso-server验证Ticket的有效性
		try{
			/**
			 * JSON中绑定：	"\"error\":false,\"username\":\"Ricky\""
			 * 				"\"error\":true,\"errorInfo\":\"Ticket is not found!\""
			 */
			JSONObject result = post(request, response, chain,  params);
			if(result.getBoolean("error")){
				response.sendRedirect(uRL);//跳转到登录页面
			}else{
				request.setAttribute("username", result.getString("username"));	//绑定登录用户名
				request.setAttribute("isLogin", true);	//绑定用户登录状态
				chain.doFilter(request, response);
			}
			logger.info(result);
		}catch (JSONException e) {
			logger.error("获取sso-server的返回数据错误");
			e.printStackTrace();
		} 
	}

	/**
	 * 发送Post请求到sso-server验证Ticket
	 * @param request
	 * @param response
	 * @param chain
	 * @param params
	 */
	private JSONObject post(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			NameValuePair[] params) throws HttpException, IOException {
		HttpClient post = new HttpClient();
		PostMethod  method = new PostMethod(ssoServiceURL);//请求到服务地址
		method.addParameters(params);
		switch (post.executeMethod(method)) {
		case org.apache.commons.httpclient.HttpStatus.SC_OK:
			return new JSONObject(method.getResponseBodyAsString());
		default:
			return null;
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
