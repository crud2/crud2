package org.crud2.autoengine.web;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ReusableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    private ServletInputStreamWrapper wrapperInputStream;

    public ReusableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        this.wrapperInputStream = new ServletInputStreamWrapper(request.getInputStream());
    }

    public ServletInputStream getInputStream() throws IOException {
        return this.wrapperInputStream;
    }

    public String getRequestContent() throws UnsupportedEncodingException {
        return this.wrapperInputStream.getContent();
    }

    public HttpServletRequest getWrappedRequest() {
        return this.request;
    }
}
