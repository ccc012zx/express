<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function(){
            $('#query').click(function(){
                var expNumbers = $('#expNumbers').text();
                alert(expNumbers);
                $.ajax({
                    type: 'GET',
                    url: "/express/exp/query",
                    //dataType: 'json',
                    //contentType: "application/json",
                    data: {"expNumbers" : expNumbers},
                    success: function(data){
                        alert('数据加载成功'+data.message);
                        if(confirm('打开新页面之后，修改url最后的验证码')){
//                            window.open(data.url);
                            var a = document.createElement('a');
                            a.setAttribute('href', data.url);
                            a.setAttribute('target', 'blank');
//                            a.setAttribute('id', id);
                            // 此处可以做到防止反复添加
//                            if(!document.getElementById(id)) {
                                document.body.appendChild(a);
//                            }
                            a.click();
                        };
                    },
                    error: function(xhr, type){
                        alert('数据加载失败');
                        console.log(type);
                    }
                });
            });
        })
    </script>

</head>
<body>
<h2>Hello World!</h2>

<table>
    <tr>
        <td>输入快递单号，请用分号分割：</td>
    </tr>
    <tr>
        <td>
            <textarea id="expNumbers" style="height: 100px;width: 550px">668281406619,668281414095,668281402657,668281408515,668283899349,668283904236,668283902294,668283891707,668283897685,668283900534,668283904269</textarea>
        </td>
    </tr>
    <tr>
        <td>
            <input type="button" id="query" value="查询"/>
        </td>
    </tr>
</table>
</body>
</html>
