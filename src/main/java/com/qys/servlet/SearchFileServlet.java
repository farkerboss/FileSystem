package com.qys.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.qys.service.FileService;
import com.qys.service.impl.FileServiceImpl;
import com.qys.util.Const;
import com.qys.util.JsonUtils;
import com.qys.util.RsaUtil;
import com.qys.util.StringConversion;

/**
 * Servlet implementation class SearchFile
 */
public class SearchFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    String sid=request.getHeader("X-SID");
			String signature=request.getHeader("X-SIGNATURE");
			byte[] b=StringConversion.stringToBytes(URLDecoder.decode(signature,"UTF-8"));
			try {
				//如果服务器宕机，就需要重新通过上传文件生成publicKey
				if("".equals(Const.publicKey))
				{
					response.setStatus(503);
					return;

				}
				//将受到的signature进行解密，通过对比解密后的结果来查验客户端的私钥是否正确
			    byte[] a=RsaUtil.decryptByPublicKey(b,Const.publicKey);
			    String decrypt=new String(a);
				if(!decrypt.equals(sid))
						{
							response.setStatus(403);
							return;
						}
				else
				{
					FileService fileService=new FileServiceImpl();
					List<com.qys.domain.File> files=fileService.queryList();
					for(com.qys.domain.File t_file:files)
					{
					//解决中文在url传输过程中的乱码问题
					t_file.setFname(URLEncoder.encode(t_file.getFname(), "UTF-8"));
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					//将日期转化为yyyy-MM-dd格式
					String time=sdf.format(t_file.getCreatetime());
					t_file.setTime(time);
					}
					String result=JsonUtils.objectToJson(files);
					PrintWriter out=response.getWriter();
				    out.write(result.toString());
					   
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.setStatus(410);
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
