<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = "";
    String password = "";
    //获取当前站点的所有Cookie
    Cookie[] cookies = request.getCookies();
    for (int i = 0; i < cookies.length; i++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
        if ("username".equals(cookies[i].getName())) {
            username = cookies[i].getValue();
        } else if ("password".equals(cookies[i].getName())) {
            password = cookies[i].getValue();
        }
    }
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SSMDemo Login</title>
    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/resources/style/css/floating-label.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
<form id="form" class="form-signin" method="post" action="${pageContext.request.contextPath}/login/doLogin">
    <div class="text-center">

        <c:if test="${requestScope.message != null}">
            <div class="alert alert-danger" role="alert">
                登录错误:${requestScope.message}  请重新登录
            </div>
        </c:if>
    </div>
    <div class="text-center mb-4">
        <img class="mb-4" src="${pageContext.request.contextPath}/resources/images/cat.svg" alt="" width="72"
             height="72">
        <h1 class="h3 mb-3 font-weight-normal">SSM Startup Demo</h1>
        <p>Using <code>Bootstrap demo</code> to create a beautiful log in page like this </p>
    </div>

    <div class="form-label-group">
        <input type="text" id="username" name="username" value="<%=username%>" class="form-control"
               placeholder="User Name" required autofocus>
        <label for="username">用户名</label>
    </div>

    <div class="form-label-group">
        <input type="password" id="userpass" name="password" value="<%=password%>" class="form-control"
               placeholder="Password" required>
        <label for="userpass">Password</label>
    </div>

    <div class="checkbox mb-3">
        <label>
            <input type="checkbox" id="rememberme" name="rememberme"/> 记住密码
        </label>
    </div>
    <button id="submit-btn" class="btn btn-lg btn-primary btn-block" type="submit" >登录</button>
    <p class="mt-5 mb-3 text-muted text-center">&copy;2019 - today</p>
</form>
</body>
</html>
