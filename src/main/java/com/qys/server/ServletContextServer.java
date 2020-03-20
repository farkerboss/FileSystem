package com.qys.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qys.servlet.DownloadServlet;
import com.qys.servlet.SearchFileServlet;
import com.qys.servlet.ShowDataServlet;
import com.qys.servlet.UploadServlet;

public class ServletContextServer {
	private static Logger log = LoggerFactory.getLogger(ServletContextServer.class);

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		// 将所有的servlet注册到容器中
		context.addServlet(new ServletHolder(new UploadServlet()), "/hello");
		context.addServlet(new ServletHolder(new DownloadServlet()), "/download");
		context.addServlet(new ServletHolder(new ShowDataServlet()), "/showData");
		context.addServlet(new ServletHolder(new SearchFileServlet()), "/searchFile");
		server.start();
		server.join();
	}
}
