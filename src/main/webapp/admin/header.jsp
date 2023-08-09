<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>withJ Admin</title>
<link rel="stylesheet" href="${contextPath}/admin/css/admin.css">
<script type="text/javascript" src="${contextPath}/admin/product/product.js"></script>
<c:choose>
	<c:when test="${empty sessionScope.workerId}">
		<script type="text/javascript">
			location.href = '/shopping_complete/action/admin/loginCheck/admin_login_form';
		</script>
	</c:when>
</c:choose>
</head>
<body onload="go_ab()">
	<div id="wrap">
		<header>
			<div id="logo">
				<a href="${contextPath}/action/admin/loginCheck/admin_login_form"> <img
					style="width: 800px" src="${contextPath}/admin/images/bar_01.gif"> <img
					src="${contextPath}/admin/images/text.gif">
				</a>
			</div>
			<input class="btn" type="button" value="logout" style="float: right;"
				onClick="location.href='${contextPath}/action/admin/loginCheck/admin_logout'">
		</header>
		<div class="clear"></div>