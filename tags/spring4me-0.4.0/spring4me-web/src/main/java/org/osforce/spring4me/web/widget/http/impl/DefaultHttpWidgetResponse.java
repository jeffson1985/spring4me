/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.web.widget.http.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.osforce.spring4me.web.widget.http.HttpWidgetResponse;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:30:48 AM
 */
public class DefaultHttpWidgetResponse extends HttpServletResponseWrapper
        implements HttpWidgetResponse {

    private PrintWriter writer;
    private ByteArrayOutputStream content = new ByteArrayOutputStream();

    public DefaultHttpWidgetResponse(HttpServletResponse response) {
        super(response);
        //
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ResponseServletOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new ResponsePrintWriter(getCharacterEncoding());
        }
        return new PrintWriter(content, true);
    }

    public String getResponseAsString() {
    	return content.toString();
    }

    private class ResponseServletOutputStream extends ServletOutputStream {

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }
    }

    private class ResponsePrintWriter extends PrintWriter {

        private ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(content, characterEncoding));
        }

        @Override
        public void write(char buf[], int off, int len) {
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.flush();
        }
    }
}


