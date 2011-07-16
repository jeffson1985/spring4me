package org.osforce.spring4me.web.widget.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.3.0
 * @create Jul 12, 2011 - 9:23:33 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class HttpWidgetResponse extends HttpServletResponseWrapper {

	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;
	
	public HttpWidgetResponse(HttpServletResponse response) {
		super(response);
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new WidgetOutputStream(outputStream);
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(new OutputStreamWriter(
				outputStream, getCharacterEncoding()), true);
	}
	
	public String getResponseAsString() {
		String text = "";
		try {
			text = this.outputStream.toString(getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}
}
