package com.yunnzh.cms.web.result;


import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * <p>All rights Reserved, Designed By HQYG.</p>
 *
 * @Copyright: Copyright(C) 2016.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/1217:50
 */
public class ResponseCacheWrapper extends HttpServletResponseWrapper {

    private final FastByteArrayOutputStream byteArrayOutputStream = new FastByteArrayOutputStream(1024);
    private final ServletOutputStream servletOutputStream = new CachedServletOutputStream();

    public ResponseCacheWrapper(HttpServletResponse response) {
        super(response);
    }

    private class CachedServletOutputStream extends ServletOutputStream{

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            byteArrayOutputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byteArrayOutputStream.write(b, off, len);
        }
    }

    private class CachedPrintWriter extends PrintWriter{

        public CachedPrintWriter(String characterEncoding) throws FileNotFoundException, UnsupportedEncodingException {
            super(new OutputStreamWriter(byteArrayOutputStream, characterEncoding));
        }
    }
}
