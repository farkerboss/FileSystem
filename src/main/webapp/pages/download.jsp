<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
        <base href="/">
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>下载文件</title>
</head>
<body>
	<c:if test="${file eq null }">
	<a href="/showData?uuid=${uuid }">查看</a>
	</c:if>
	<c:if test="${file ne null }">
	<table class="table">
					<thead>
						<tr>
							<th>文件名</th>
							<th>文件大小</th>
							<th>文件类型</th>
							<th>上传时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						
						<tr>
							<td>${file.fname }</td>
							<td>${file.fsize }</td>
							<td>${file.ftype }</td>
							<td>${file.time }</td>
							<td><a href="/download?uuid=${uuid }">下载</a></td>
						</tr>
						
					</tbody>
				</table>
	</c:if>
</body>
</html>