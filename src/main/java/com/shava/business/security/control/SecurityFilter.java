package com.shava.business.security.control;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shava.business.security.entity.UserInfo;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter(urlPatterns = { "/pages/*" }, filterName = "SecurityFilter")
public class SecurityFilter implements Filter {

	@Inject
	UserInfo userInfo;

	private static Logger log = Logger.getLogger("SecurityFilter");

	private final String urlLogin = "/login.xhtml";

	private void redirectLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		if (isAjaxRequest(req)) {
			HttpServletResponse response = (HttpServletResponse) resp;
			response.getWriter().print(xmlPartialRedirectToPage(req, urlLogin));
			response.flushBuffer();
		} else {
			if (!resp.isCommitted()) {
				resp.sendRedirect(req.getContextPath() + urlLogin);
			}
		}
	}

	private String xmlPartialRedirectToPage(HttpServletRequest request, String page) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<partial-response><redirect url=\"").append(request.getContextPath()).append(urlLogin)
				.append("\"/></partial-response>");
		return sb.toString();
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	/**
	 * Default constructor.
	 */
	public SecurityFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		log.info("request type-----------------" + req.getMethod());
		log.info("URL :" + req.getServletPath());
		// If validate attribute is false, then filterChain.doFilter
		request.setCharacterEncoding("UTF-8");
		/** * Set the default response content type and encoding */
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		/* don't allow caching */
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "No-cache");
		// to allow framing of this content only by this site
		res.setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
		res.setDateHeader("Expires", 0);
		String strUri = req.getServletPath();
		if (strUri.equals(urlLogin) || isStaticResource(strUri)) {
			filterChain.doFilter(request, response);
		} else {
			// Check User login on memory
			if (userInfo != null && userInfo.getId() != null) {
				filterChain.doFilter(request, response);
			} else {
				redirectLogin(req, res);
			}
		}

	}

	private boolean isStaticResource(String uri) {
		boolean result = false;
		if (!uri.endsWith("xhtml")) {
			result = true;
		}
		return result;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
