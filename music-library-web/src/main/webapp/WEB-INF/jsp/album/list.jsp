<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:message key="album.title" var="title"/>
<my:pagetemplate title="${title}">
<jsp:attribute name="body">

	<my:a href="/album/new" class="btn btn-primary">
		<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
		<fmt:message key="albumCreate.title"/>
	</my:a>

	<table class="table">
		<thead>
		<tr>
			<th><fmt:message key="album.id"/></th>
			<th><fmt:message key="album.name"/></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${albums}" var="album">
			<tr>
				<td>${album.id}</td>
				<td><c:out value="${album.name}"/></td>
				<td>
					<my:a href="/album/view/${album.id}" class="btn btn-primary">
						<fmt:message key="album.detail"/>
					</my:a>
				</td>
				<td>
					<my:a href="/album/update/${album.id}" class="btn btn-primary">
						<fmt:message key="album.edit"/>
					</my:a>
				</td>
				<td>
					<form method="post" action="${pageContext.request.contextPath}/album/delete/${album.id}">
						<button type="submit" class="btn btn-primary"><fmt:message key="album.delete"/></button>
					</form>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</jsp:attribute>
</my:pagetemplate>
