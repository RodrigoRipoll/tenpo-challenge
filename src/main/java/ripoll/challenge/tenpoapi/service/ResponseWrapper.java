package ripoll.challenge.tenpoapi.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private final HttpServletResponse delegate;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        delegate = response;
    }

    public String getResponseBody() {
        return delegate.getCharacterEncoding();
    }
}