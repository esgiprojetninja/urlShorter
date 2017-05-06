<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="url" class="models.Url" scope="request" />
<h1>Url Shorter</h1>
<p>${pageContext.request.getAttribute("message")}</p>
<form action="${pageContext.request.contextPath}/" method="post">
    <input type="text" placeholder="url..." name="url">
    <button type="submit">save</button>
</form>

<div>
    <h2>Url list</h2>
</div>