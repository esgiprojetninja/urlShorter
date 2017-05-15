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
            <a class="btn btn-danger btn-xs" href="<%= request.getContextPath()%>/logout">Logout</a>
        <% }  else  {%>
            <p>You can create an account in order to save and manage your urls.</p>
            <div class="row">
                <div class="col-sm-6">
                    <h4>Login</h4>
                    <form action="<%= request.getContextPath()%>/login" method="post">
                        <div class="for-group">
                            <input type="text" name="name" placeholder="Name..." class="form-control">
                        </div>
                        <div class="for-group">
                            <input type="password" name="pwd" placeholder="Password..." class="form-control">
                        </div>
                        <button class="btn btn-primary" type="submit">Login</button>
                    </form>
                </div>
                <div class="col-sm-6">
                    <h4>Subscribe</h4>
                    <p>
                        <% if (request.getParameter("userExist") != null &&
                                request.getParameter("userExist").compareTo("true") == 0) { %>
                        Name already exist
                        <% } else if (request.getParameter("emptyInput") != null &&
                                request.getParameter("emptyInput").compareTo("true") == 0) { %>
                        Please fill both fields
                        <% } else if (request.getParameter("userSaved") != null &&
                                request.getParameter("userSaved").compareTo("true") == 0) { %>
                        User Saved
                        <% } else if (request.getParameter("userSaved") != null &&
                                request.getParameter("userSaved").compareTo("false") == 0) { %>
                        Problem while saving user
                        <% } %>
                    </p>
                    <form action="<%= request.getContextPath()%>/subscribe" method="post">
                        <div class="for-group">
                            <input type="text" name="name" placeholder="Name..." class="form-control">
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