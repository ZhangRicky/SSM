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
 * �����¼����
 * ��֤���ĵ�ַ:http://lcalhost:8080/web-consumer/SSOAuthCenter		//�õ�ַ������һ����Ŀ��
 * @author Mark
 *
 */
public class SSOLoginFilter implements Filter {

	private Logger logger = Logger.getLogger(SSOLoginFilter.class);
	
	private String ssoServiceURL;	//��֤���ĵĵ�ַ
	
	private String ssoLogin;		//sso��¼ҳ��
	
	private String cookieName;		//cookie��
	
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
		//��ȡ����������·�������в����б�
		String params = request.getQueryString();
		//���������Ϊ�գ�URL=ssoLgon?gotoURL+����·��+����,	�������Ϊ��:URL=ssoLgon?gotoURL+����·��
		String URL = ssoLogin + "?gotoURL="+(params != null ? request.getRequestURL().append("?").append(params).toString() : request.getRequestURL().toString()) ;
		
		Cookie ticket = null;
		Cookie[] cooks = request.getCookies();
		if(cooks != null){
			//����Cookies���õ�����ֵ
			for(Cookie cook : cooks){
				if(cook.getName().equals("cookieName")){
					ticket = cook;
					break;
				}
			}
		}
		//���Ʋ�Ϊ�գ�����֤����
		if(ticket != null){
			validateTicke(request,  response,  chain, ticket,  URL);
		//����ת��-->��¼ҳ��
		}else{
			response.sendRedirect(URL);
		}
		//������˳�ϵͳ
		if(request.getRequestURI().equals(request.getContextPath()+"/logout")){
			//doLogOut();
		}
	
	}

	/*
	 * �������Ƶ�SSOID��֤������
	 */
	private void validateTicke(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Cookie ticket, String uRL) throws IOException, ServletException {
		//NameValuePair���洢������Name/Value��
		NameValuePair[] params = new NameValuePair[2];
		params[0] = new NameValuePair("action", "validateTicket");
		params[1] = new NameValuePair("cookieName", ticket.getValue());
		//ʹ��post����sso-server��֤Ticket����Ч��
		try{
			/**
			 * JSON�а󶨣�	"\"error\":false,\"username\":\"Ricky\""
			 * 				"\"error\":true,\"errorInfo\":\"Ticket is not found!\""
			 */
			JSONObject result = post(request, response, chain,  params);
			if(result.getBoolean("error")){
				response.sendRedirect(uRL);//��ת����¼ҳ��
			}else{
				request.setAttribute("username", result.getString("username"));	//�󶨵�¼�û���
				request.setAttribute("isLogin", true);	//���û���¼״̬
				chain.doFilter(request, response);
			}
			logger.info(result);
		}catch (JSONException e) {
			logger.error("��ȡsso-server�ķ������ݴ���");
			e.printStackTrace();
		} 
	}

	/**
	 * ����Post����sso-server��֤Ticket
	 * @param request
	 * @param response
	 * @param chain
	 * @param params
	 */
	private JSONObject post(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			NameValuePair[] params) throws HttpException, IOException {
		HttpClient post = new HttpClient();
		PostMethod  method = new PostMethod(ssoServiceURL);//���󵽷����ַ
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
