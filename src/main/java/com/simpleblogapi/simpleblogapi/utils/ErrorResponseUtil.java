package com.simpleblogapi.simpleblogapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleblogapi.simpleblogapi.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponseUtil {

    public static void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));

    }
}
