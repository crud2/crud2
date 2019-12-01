package org.crud2.autoengine.web;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServletInputStreamWrapper extends ServletInputStream {
    private ServletInputStream servletInputStream;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private String content;

    public ServletInputStreamWrapper(ServletInputStream servletInputStream) {
        this.servletInputStream = servletInputStream;
    }

    public boolean isFinished() {
        return this.servletInputStream.isFinished();
    }

    public boolean isReady() {
        return this.servletInputStream.isReady();
    }

    public void setReadListener(ReadListener readListener) {
        this.servletInputStream.setReadListener(readListener);
    }

    public int read() throws IOException {
        int b = this.servletInputStream.read();
        if (b != -1) {
            this.byteArrayOutputStream.write(b);
        }

        return b;
    }

    public String getContent() throws UnsupportedEncodingException {
        if (this.content == null) {
            this.content = this.byteArrayOutputStream.toString("UTF-8");
        }

        return this.content;
    }

}
