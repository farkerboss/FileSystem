<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="/">
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>文件列表</title>
<link rel="stylesheet" type="text/css"
	href="bootstrap-3.3.7/css/bootstrap.min.css" />
</head>
<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<nav class="navbar navbar-default" role="navigation">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target="#bs-example-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span><span
							class="icon-bar"></span><span class="icon-bar"></span><span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">文件列表</a>
				</div>

				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<form class="navbar-form navbar-left" role="search" action="/searchFile">
						<button type="submit" class="btn btn-default">Submit</button>
					</form>
					
				</div>

				</nav>
				<c:if test="${files ne null }">
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
						<c:forEach items="${files }" var="file" >
						<tr>
							<td>${file.fname }</td>
							<td>${file.fsize }</td>
							<td>${file.ftype }</td>
							<td>${file.time }</td>
							<td><a href="/download?uuid=${file.id}">下载</a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>