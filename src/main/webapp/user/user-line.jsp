<%@page pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <title>Document</title>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 1000px;height:500px;"></div>
<script>
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        title: {
            //标题部分
            text: '上半年用户注册趋势'
        },
        //移入，移出在柱状图会显示相关提示
        tooltip: {},
        legend: {
            //中间的表示
            data:['男','女']
        },
        //横坐标
        xAxis: {
            data: ["一月份","二月份","三月份","四月份","五月份","六月份"]
        },
        //纵坐标
        yAxis: {},
        series: [{
            name: '男',
            type: 'line',//bar:柱状图
            //date 数据的展示，数据的高低也可以成为多少
            //data: [1,2,3,4,5,6]
        },{
            name: '女',
            type: 'line',//bar:柱状图
            //data: [1,2,3,4,5,6]
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $.ajax({
        url:"${pageContext.request.contextPath}/user/line",
        type:"get",
        datatype:"json",
        success:function (data) {
            //弹框alert("data");
            //开始渲染数据
            myChart.setOption({
                // xAxis: {
                //     data: data.name
                // },
                // yAxis:{},
                series: [{
                    name: '男',
                    type: 'bar',//bar；柱状图。line折线图
                    //后台的键，通过键找值
                    data: data.man
                },{
                    name: '女',
                    type: 'bar',//bar柱状图
                    // 后台的键，通过键找值
                    data:  data.woman
                }]
            });
        }
    })

</script>

</body>
</html>