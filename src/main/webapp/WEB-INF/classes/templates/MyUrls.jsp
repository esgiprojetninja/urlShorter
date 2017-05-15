<%@ page import="models.Url" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="Header.jsp"%>
    <h1>Hello <%= request.getSession().getAttribute("user_name")%></h1>
    <table class="table">
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
<%@include file="Footer.jsp"%>

