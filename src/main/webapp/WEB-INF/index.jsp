<%@ page import="models.Url" %>
<%@ page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<h1>Url Shorter</h1>
<p>${pageContext.request.getAttribute("message")}</p>
<form action="${pageContext.request.contextPath}/" method="post">
    <input type="text" placeholder="url..." name="url">
    <button type="submit">save</button>
</form>

<div>
    <h2>Url list</h2>
    <table>
        <tr>
            <th>Id</th>
            <th>Base Url</th>
            <th>Short Url</th>
            <th>User Id</th>
        </tr>
        <% for (Url url: (ArrayList<Url>)request.getAttribute("urls")) { %>
            <tr>
                <td>
                    <%= url.getAttributes().get("id").getValue() %>
                </td>
                <td>
                    <%= url.getAttributes().get("base_url").getValue() %>
                </td>
                <td>
                    <%= url.getAttributes().get("shorter_url").getValue() %>
                </td>
                <td>
                    <%= url.getAttributes().get("user_id").getValue() %>
                </td>
            </tr>
        <% } %>
    </table>
</div>