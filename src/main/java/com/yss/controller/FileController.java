package com.yss.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yss.util.AesUtil;
import com.yss.util.Const;
import com.yss.util.JsonUtils;
import com.yss.util.RsaUtil;
import com.yss.util.StringConversion;

@Controller
public class FileController {
	private static Logger log = LoggerFactory.getLogger(FileController.class);
	@RequestMapping("/hello")
	public String index() {
		return "success";
	}
	@RequestMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
		if (file.isEmpty()) {
			return "failure";
		}
		String fileName = file.getOriginalFilename();
		int size = (int) file.getSize();
		log.info(fileName + "-->" + size);
		//上传前先将文件备份
		String path = "D:/backup";
		File dest = new File(path + "/" + fileName);
		if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
			dest.getParentFile().mkdir();
		}
		try {
			file.transferTo(dest); // 保存文件

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		}
		String actionUrl = "http://localhost:8080/hello";
		String fname=URLEncoder.encode(file.getOriginalFilename(),"UTF-8");
		String u1 = actionUrl + "?filename=" +fname;
		URL url = new URL(u1);
		log.debug("文件开始上传，url地址为："+url);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Content-type", "text/html");
		httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.connect();

		OutputStream out = httpURLConnection.getOutputStream();
		DataInputStream in = null;

		in = new DataInputStream(new FileInputStream(dest));
		int bytes = 0;
		byte[] buffer = new byte[1024];
		while ((bytes = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytes);
		}
		out.flush();

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = null;

		try {
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				String tmpLine = null;
				resultBuffer = new StringBuffer();
				while ((tmpLine = reader.readLine()) != null) {
					resultBuffer.append(tmpLine);
					resultBuffer.append("\n");
				}
			} else {
				int code = httpURLConnection.getResponseCode();
				log.error("上传失败,状态码为："+code);
				return "failure";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("上传失败");
			return "failure";
		}

		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failure";
			}
		}
		JSONObject output = JSON.parseObject(resultBuffer.toString());
		String privateKey = output.getObject("privateKey", String.class);
		Const.privatekey = privateKey;
		String uuid = output.getObject("uuid", String.class);
		model.addAttribute("uuid", uuid);
		return "download";

	}

	@RequestMapping("/download")
	public String download(String uuid,Model model) {
		File file = null;
		try {
			// 统一资源
			String actionUrl = "http://localhost:8080/download";
			String u1 = actionUrl + "?uuid=" + uuid;
			URL url = new URL(u1);
			log.debug("开始文件下载,url地址为："+url);
			// 连接类的父类，抽象类
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
			//rsa算法进行加密的时候，字符串标准为16位
			String sid = UUID.randomUUID().toString().substring(0, 16);
			httpURLConnection.setRequestProperty("X-SID", sid);
			byte[] b = RsaUtil.encryptByPrivateKey(sid.getBytes("UTF-8"), Const.privatekey);
			String signature = StringConversion.bytesToString(b);
			httpURLConnection.setRequestProperty("X-SIGNATURE", URLEncoder.encode(signature, "UTF-8"));
			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();
			URLConnection con = url.openConnection();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				/*BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());*/
				//获取响应头信息
				Map headers = httpURLConnection.getHeaderFields();
				Map<String, String> field = new HashMap<String, String>();
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					String val = httpURLConnection.getHeaderField(key);
					field.put(key, val);
				}
				String filename = field.get("filename");
				String path = "D:/server" + File.separatorChar + URLDecoder.decode(filename );
				file = new File(path);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				String envo = field.get("envo");
				//拿到数字信封后，先解密，再拿解密后的密钥去对文件解密
				byte[] decrypt = RsaUtil.decryptByPrivateKey(StringConversion.stringToBytes(envo), Const.privatekey);
				AesUtil.decryptFile(file, httpURLConnection.getInputStream(),new String(decrypt));
				
			}
			else if(httpURLConnection.getResponseCode() == 403)
			{
				return "403";
			}
			else if(httpURLConnection.getResponseCode() == 503)
			{
				return "503";
			}
			else {
				log.error("下载文件失败，响应码为："+httpURLConnection.getResponseCode());
				return "failure";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("下载文件失败");
			return "failure";
		}
		model.addAttribute("uuid", uuid);
		return "success";
	}
	@RequestMapping("/showData")
	public String showData(String uuid,Model model)
	{
		try {
			// 统一资源
			String actionUrl = "http://localhost:8080/showData";
			String u1 = actionUrl + "?uuid=" + uuid;
			URL url = new URL(u1);
			log.debug("开始发送获取文件元数据的请求，url地址为："+url);
			// 连接类的父类，抽象类
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
			String sid = UUID.randomUUID().toString().substring(0, 16);
			httpURLConnection.setRequestProperty("X-SID", sid);
			byte[] b = RsaUtil.encryptByPrivateKey(sid.getBytes("UTF-8"), Const.privatekey);
			String signature = StringConversion.bytesToString(b);
			log.info("sid: "+sid+" signature: "+signature);
			httpURLConnection.setRequestProperty("X-SIGNATURE", URLEncoder.encode(signature, "UTF-8"));
			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();
			URLConnection con = url.openConnection();
			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			StringBuffer resultBuffer = null;

			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				/*BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());*/
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				String tmpLine = null;
				resultBuffer = new StringBuffer();
				while ((tmpLine = reader.readLine()) != null) {
					resultBuffer.append(tmpLine);
					resultBuffer.append("\n");
				}
				JSONObject output = JSON.parseObject(resultBuffer.toString());
				com.yss.domain.File t_file = output.getObject("file", com.yss.domain.File.class);
				//解决中文在url传输过程中乱码的问题
				t_file.setFname(URLDecoder.decode(t_file.getFname(),"UTF-8"));
				model.addAttribute("file", t_file);
				model.addAttribute("uuid", uuid);
			} 
			else if(httpURLConnection.getResponseCode() == 403)
			{
				return "403";
			}
			else if(httpURLConnection.getResponseCode() == 503)
			{
				return "503";
			}
			else {
				log.error("获取文件信息失败，响应码为："+httpURLConnection.getResponseCode());
				return "failure";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		}
		return "download";
	}
	@RequestMapping("/searchFile")
	public String searchFile(Model model)
	{

		if("".equals(Const.privatekey))
		{
			return "fileList";
		}
		try {
			// 统一资源
			String actionUrl = "http://localhost:8080/searchFile";
			URL url = new URL(actionUrl);
			// 连接类的父类，抽象类
			log.info("开始发送请求查看文件列表，url地址为："+url);
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
			String sid = UUID.randomUUID().toString().substring(0, 16);
			httpURLConnection.setRequestProperty("X-SID", sid);
			byte[] b = RsaUtil.encryptByPrivateKey(sid.getBytes("UTF-8"), Const.privatekey);
			String signature = StringConversion.bytesToString(b);
			log.info("sid: "+sid+" signature: "+signature);
			httpURLConnection.setRequestProperty("X-SIGNATURE", URLEncoder.encode(signature, "UTF-8"));
			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();
			URLConnection con = url.openConnection();
			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			StringBuffer resultBuffer = null;

			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				/*BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());*/
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				String tmpLine = null;
				resultBuffer = new StringBuffer();
				while ((tmpLine = reader.readLine()) != null) {
					resultBuffer.append(tmpLine);
					resultBuffer.append("\n");
				}
				List< com.yss.domain.File> files=JsonUtils.jsonToList(resultBuffer.toString(),com.yss.domain.File.class );
				if(files==null||files.size()<=0)
				{
					return "failure";
				}
				//解决中文在url传输过程中乱码的问题
				for(com.yss.domain.File t_file:files)
				{
					t_file.setFname(URLDecoder.decode(t_file.getFname(),"UTF-8"));
				}
				
				model.addAttribute("files", files);
				
			} 
			else if(httpURLConnection.getResponseCode() == 403)
			{
				return "403";
			}
			else if(httpURLConnection.getResponseCode() == 503)
			{
				return "503";
			}
			else {
				log.error("查看文件列表失败，响应码为："+httpURLConnection.getResponseCode());
				return "failure";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("查看文件列表失败");
			return "failure";
		}
		return "fileList";
	}
}
