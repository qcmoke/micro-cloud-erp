package com.qcmoke.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWriterUtil {
    private static final Log logger = LogFactory.getLog(ResponseWriterUtil.class);

    /**
     * @param httpServletResponse HttpServletResponse
     * @param status              相应状态码
     * @param respBean            RespBean
     */
    public static void writeJson(HttpServletResponse httpServletResponse, final int status, RespBean respBean) {
        try {
            httpServletResponse.setStatus(status);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(new ObjectMapper().writeValueAsString(respBean));
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.info("writeJson异常：e=" + e.getMessage());
        }
    }


    /**
     * @param respBean RespBean
     */
    public static void writeJson(RespBean respBean) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletResponse httpServletResponse = requestAttributes.getResponse();
            if (httpServletResponse != null) {
                ResponseWriterUtil.writeJson(httpServletResponse, respBean.getStatus(), respBean);
            }
        }
    }

}
