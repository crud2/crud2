package org.crud2.autoengine.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WrapperRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String method = httpRequest.getMethod().toUpperCase();
        String contentType = httpRequest.getContentType();
        if (
                (
                        "POST".equals(method) || "PUT".equals(method)
                ) &&
                        (
                                contentType != null &&
                                        (
                                                contentType.toLowerCase().contains("application/json") || contentType.toUpperCase().contains("application/xml")
                                        )
                        )
        ) {
            filterChain.doFilter(new ReusableHttpServletRequestWrapper(httpRequest), servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
