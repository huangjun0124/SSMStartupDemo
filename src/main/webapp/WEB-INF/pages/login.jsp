<%--
  Created by IntelliJ IDEA.
  User: junguoguo
  Date: 2019/7/27
  Time: 7:07
  To change this template use File | Settings | File Templates.
--%>
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
    <title>SSM DEO 登录页面</title>
    <script type="text/javascript"  src="${pageContext.request.contextPath}/resources/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            <%--登录验证--%>
            $("#submit-btn").click(function () {
                var user = $("#user_login").val();
                var password = $("#user_pass").val();
                if(user==="") {
                    alert("用户名不可为空!");
                } else if(password===""){
                    alert("密码不可为空!");
                } else {
                    $.ajax({
                        async: false,//同步，待请求完毕后再执行后面的代码
                        type: "POST",
                        url: '${pageContext.request.contextPath}/login/doLogin',
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        data: $("#loginForm").serialize(),
                        dataType: "json",
                        success: function (data) {
                            var ret = JSON.parse(data) ;
                            if(ret.code===-1) {
                                alert(ret.msg);
                            } else {
                                window.location.href="${pageContext.request.contextPath}/swagger";
                            }
                        },
                        error: function () {
                            alert("数据获取失败")
                        }
                    })
                }
            })
        })
    </script>
</head>
<body>
<form name="loginForm" id="loginForm"  method="post">
    <input type="text" name="username" id="user_login"
           class="input" value="<%=username%>" size="20" required/></label>
    <input type="password" name="password" id="user_pass"
           class="input" value="<%=password%>" size="20" required/>
    <input name="rememberme" type="checkbox" id="rememberme" value="1" /> 记住密码
    <input type="button" name="wp-submit" id="submit-btn" class="button button-primary button-large" value="登录" />
</form>
</body>
</html>
