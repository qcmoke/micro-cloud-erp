package com.qcmoke.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcmoke.common.vo.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qcmoke
 */
public class ResponseWriterUtil {
    private static final Log logger = LogFactory.getLog(ResponseWriterUtil.class);

    /**
     * @param httpServletResponse HttpServletResponse
     * @param status              相应状态码
     * @param result              RespBean
     */
    public static <T> void writeJson(HttpServletResponse httpServletResponse, final int status, Result<T> result) {
        ServletOutputStream outputStream = null;
        try {
            httpServletResponse.setStatus(status);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            outputStream = httpServletResponse.getOutputStream();
            outputStream.write(new ObjectMapper().writeValueAsString(result).getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.info("writeJson异常：e=" + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param result RespBean
     */
    public static <T> void writeJson(Result<T> result) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletResponse httpServletResponse = requestAttributes.getResponse();
            if (httpServletResponse != null) {
                ResponseWriterUtil.writeJson(httpServletResponse, result.getStatus(), result);
            }
        }
    }

}
