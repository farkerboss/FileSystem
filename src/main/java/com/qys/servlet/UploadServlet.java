package com.qys.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.qys.server.ServletContextServer;
import com.qys.service.FileService;
import com.qys.service.impl.FileServiceImpl;
import com.qys.util.AesUtil;
import com.qys.util.Const;
import com.qys.util.RsaUtil;
import com.qys.util.StringConversion;

import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
	  // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  JSONObject result = new JSONObject();
	        try {

	            String filename =  URLDecoder.decode(request.getParameter("filename"),"UTF-8");
	            //获取到当前日期并且以yyyy-MM-dd形式转化为字符串
	            Date date=new Date();
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	            String uploadPath="D:/"+sdf.format(date);
	            InputStream  input = request.getInputStream();
	            File dir=new File(uploadPath);
	            if(!dir.exists())
	            {
	            	dir.mkdir();//需要加日志
	            }
	            File getFile = new File(uploadPath+"/"+filename);
	            String uuid=UUID.randomUUID().toString();
	            result.put("uuid", uuid);
	            String key=uuid.substring(0, 16);
	            //对文件进行加密并返回文件的大小
	            int size=AesUtil.encryptFile(input, getFile, key);
	            Const.publicKey=RsaUtil.getPublicKey();
	            result.put("privateKey", RsaUtil.getPrivateKey());
	           byte[] b =RsaUtil.encryptByPublicKey(key.getBytes(StandardCharsets.UTF_8), Const.publicKey);
	           String envo=StringConversion.bytesToString(b);
	           //将上唇的文件信息保存进数据库
	           com.qys.domain.File file=new com.qys.domain.File();
	           file.setId(uuid);
	           file.setFsize(size+"byte");
	           file.setFtype(filename.substring(filename.lastIndexOf("."), filename.length()));
	           file.setFname(filename);
	           file.setFilepath(uploadPath+"/"+filename);
	           file.setEnvo(envo);
	           file.setCreatetime(new Date());
	           FileService fileService=new FileServiceImpl();
	           fileService.addFile(file);
	        } catch (Exception e) {
	            result.put("success",false);
	            result.put("msg","接受文件失败");
	            e.printStackTrace();
	        }
	       //将json数组写入response body
	       PrintWriter out=response.getWriter();
	       out.write(result.toString());
      
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
