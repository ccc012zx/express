<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            $('#query').click(function () {
                var expNumbers1 = $('#expNumbers1').val();
                var expNumbers2 = $('#expNumbers2').val();
                var expNumbers3 = $('#expNumbers3').val();
                var expNumbers = expNumbers1+","+expNumbers2+","+expNumbers3;
                if(expNumbers1!=null||expNumbers1!=undefined||expNumbers1!=""){
                    var username = $('#username').val();
                    var timeDay = $('#timeDay').val();
                    alert(username);
                    $.ajax({
                        type: 'POST',
                        url: "/express/exp/queryAndInsert",
                        //dataType: 'json',
                        //contentType: "application/json",
                        data: {"expNumbers": expNumbers,"username":username,"timeDay":timeDay},
                        success: function (data) {
                            alert('数据加载成功' + data.message);
                            if (confirm('打开新页面之后，修改url最后的验证码')) {
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
                            }
                        },
                        error: function (xhr, type) {
                            alert('数据加载失败');
                            console.log(type);
                        }
                    });
                }

            });
            $('#').click(function () {
                var expNumbers1 = $('#expNumbers1').val();
                var expNumbers2 = $('#expNumbers2').val();
                var expNumbers3 = $('#expNumbers3').val();
                var expNumbers = expNumbers1+","+expNumbers2+","+expNumbers3;
                alert(expNumbers);
                $.ajax({
                    type: 'GET',
                    url: "/express/exp/query",
                    //dataType: 'json',
                    //contentType: "application/json",
                    data: {"expNumbers": expNumbers},
                    success: function (data) {
                        alert('数据加载成功' + data.message);
                        if (confirm('打开新页面之后，修改url最后的验证码')) {
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
                        }
                        ;
                    },
                    error: function (xhr, type) {
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
        <td>所有人</td>
        <td>扫描时间</td>
    </tr>
    <tr>
        <td>
            <textarea id="expNumbers1" style="height: 100px;width: 550px">668281406619,668281414095,668281402657,668281408515,668283899349,668283904236,668283902294,668283891707,668283897685,668283900534,668283904269</textarea>
        </td>
        <td>
            <select id="username">
                <option value="zhangjieyu">张捷瑜</option>
                <option value="zhouyang">周扬</option>
                <option value="hujing">胡静</option>
            </select>
        </td>
        <td>
            <select id="timeDay">
                <option value="3">发货第三天</option>
                <option value="4">发货第四天</option>
                <option value="5">发货第五天</option>
            </select>
        </td>
    </tr>   <tr>
    <td>
        <textarea id="expNumbers2" style="height: 100px;width: 550px"></textarea>
    </td>
    <td>
        <select>
            <option value="zhangjieyu">张捷瑜</option>
            <option value="zhouyang">周扬</option>
            <option value="hujing">胡静</option>
        </select>
    </td>
    <td>
        <select>
            <option value="3">发货第三天</option>
            <option value="4">发货第四天</option>
            <option value="5">发货第五天</option>
        </select>
    </td>
</tr>
    <tr>
        <td>
            <textarea id="expNumbers3" style="height: 100px;width: 550px"></textarea>
        </td>
        <td>
            <select>
                <option value="zhangjieyu">张捷瑜</option>
                <option value="zhouyang">周扬</option>
                <option value="hujing">胡静</option>
            </select>
        </td>
        <td>
            <select>
                <option value="3">发货第三天</option>
                <option value="4">发货第四天</option>
                <option value="5">发货第五天</option>
            </select>
        </td>
    </tr>
    <tr>
        <td>
            <input type="button" id="query" value="一键查询以上所有快递单"/>
        </td>
    </tr>
</table>
</body>
</html>
