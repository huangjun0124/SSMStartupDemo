<%--
  Created by IntelliJ IDEA.
  User: junguoguo
  Date: 2019/7/21
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>模拟抢红包</title>
    <script type="text/javascript"  src="https://code.jquery.com/jquery-3.2.0.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            // 模拟 2500 个异步请求并发
            var max = 2500;
            for (var i=1; i <= max; i++){
                $.post({
                    url:"./grab?redPacketId=1&userId=" + i,
                    success:function(result){

                    }
                });
            }
        })
    </script>
</head>
<body>

</body>
</html>