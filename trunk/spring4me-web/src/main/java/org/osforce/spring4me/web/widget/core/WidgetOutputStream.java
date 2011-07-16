package org.osforce.spring4me.web.widget.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.3.0
 * @create Jul 12, 2011 - 10:54:05 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetOutputStream extends ServletOutputStream {
	
	private OutputStream outputStream;
	
	public WidgetOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(int b) throws IOException {
		this.outputStream.write(b);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		this.outputStream.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.outputStream.write(b, off, len);
	}

}
