<%@ page import="models.Url" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="Header.jsp" %>
<div class="row">
    <div class="col-sm-6">
        <h2>Save an url: </h2>
        <p><%= request.getAttribute("message")%></p>
        <form action="<%= request.getContextPath()%>/" method="post">
            <div class="form-group">
                <input type="text" placeholder="url..." name="url" class="form-control">
            </div>
            <button type="submit" class="btn btn-success">save</button>
        </form>
    </div>
    <div class="col-sm-6">
        <% if (request.getSession().getAttribute("user_id") != null) { %>
            <h3>Hello <%= request.getSession().getAttribute("user_name")%></h3>
            <button class="btn btn-danger btn-xs">Logout</button>
        <% }  else  {%>
            <p>You can create an account in order to save and manage your urls.</p>
            <div class="row">
                <div class="col-sm-6">
                    <p>Login</p>
                    <form action="<%= request.getContextPath()%>/login" method="post">
                        <div class="for-group">
                            <input type="text" name="email" placeholder="Email..." class="form-control">
                        </div>
                        <div class="for-group">
                            <input type="password" name="pwd" placeholder="Password..." class="form-control">
                        </div>
                        <button class="btn btn-primary" type="submit">Login</button>
                    </form>
                </div>
                <div class="col-sm-6">
                    <p>Subscribe</p>
                    <form action="<%= request.getContextPath()%>/subscribe" method="post">
                        <div class="for-group">
                            <input type="text" name="email" placeholder="Email..." class="form-control">
                        </div>
                        <div class="for-group">
                            <input type="password" name="pwd" placeholder="Password..." class="form-control">
                        </div>
                        <button class="btn btn-primary" type="submit">Subscribe</button>
                    </form>
                </div>
            </div>
        <% } %>
    </div>
</div>

<div>
    <h2>Url list</h2>
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
</div>

<%@include file="Footer.jsp"%>