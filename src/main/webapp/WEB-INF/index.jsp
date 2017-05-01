<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper>
    <h1>Url Shorter</h1>

    <form action="${pageContext.request.contextPath}/" method="post">
        <input type="text" placeholder="url..." name="url">
        <button type="submit">save</button>
    </form>
</t:wrapper>