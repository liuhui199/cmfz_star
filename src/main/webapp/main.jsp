<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<c:set var="app" value="${pageContext.request.contextPath}"></c:set>
<html lang="en">
<head>
    <title>后台管理系统</title>
    <%--引入bootstrap的样式--%>
    <link rel="stylesheet" href="${app}/statics/boot/css/bootstrap.min.css">
    <link rel="stylesheet" href="${app}/statics/jqgrid/css/trirand/ui.jqgrid-bootstrap.css">
    <script src="${app}/statics/boot/js/jquery-3.3.1.min.js"></script>
    <script src="${app}/statics/boot/js/bootstrap.min.js"></script>

    <%--引入jqgrid--%>
    <script src="${app}/statics/jqgrid/js/trirand/i18n/grid.locale-cn.js"></script>
    <script src="${app}/statics/jqgrid/js/trirand/jquery.jqGrid.min.js"></script>


    <script src="${app}/statics/jqgrid/js/ajaxfileupload.js"></script>
    <%-- 引入kindeditor 编辑器 --%>
    <script charset="utf-8" src="${app}/kindeditor/kindeditor-all-min.js"></script>
    <script charset="utf-8" src="${app}/kindeditor/lang/zh-CN.js"></script>

    <%-- 引入echars --%>
    <script src="echarts/echarts.min.js"></script>
</head>
<body>
<%--顶部导航栏--%>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="#">持明法州后台管理系统</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">欢迎:${login.nickname}</a></li>
                <li><a href="${app}/code/quit">退出登录</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<%--中间栅格系统--%>
<div class="row">
    <%--左侧手风琴--%>
    <div class="col-sm-2">
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingOne">
                    <h4 class="panel-title text-center">
                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                            <h4>轮播图管理</h4>
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body text-center">
                        <a href="javascript:$('#content-layout').load('${app}/banner/banner.jsp');" class="btn btn-default">所有轮播图</a>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTwo">
                    <h4 class="panel-title text-center">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                            <h4>专辑管理</h4>
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                    <div class="panel-body text-center">
                        <a href="javascript:$('#content-layout').load('${app}/album/album.jsp');" class="btn btn-default">所有专辑</a>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingThree">
                    <h4 class="panel-title text-center">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                            <h4>文章管理</h4>
                        </a>
                    </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                    <div class="panel-body text-center">
                        <a href="javascript:$('#content-layout').load('article/article.jsp')" class="btn btn-default">所有文章</a>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingFour">
                    <h4 class="panel-title text-center">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                            <h4>用户管理</h4>
                        </a>
                    </h4>
                </div>
                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFour">
                    <div class="panel-body text-center">
                        <a href="javascript:$('#content-layout').load('${app}/user/user.jsp')" class="btn btn-default">所有用户</a>
                        <a href="javascript:$('#content-layout').load('${app}/user/user-line.jsp')" class="btn btn-default">注册趋势</a>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingFive">
                    <h4 class="panel-title text-center">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFive" aria-expanded="false" aria-controls="collapseFive">
                            <h4>明星管理</h4>
                        </a>
                    </h4>
                </div>
                <div id="collapseFive" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFive">
                    <div class="panel-body text-center">
                        <a href="javascript:$('#content-layout').load('${app}/star/star.jsp');" class="btn btn-default">所有明星</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%--右侧展示部分--%>
    <div class="col-sm-10" id="content-layout">
        <div class="jumbotron" style="padding-left: 200px;">
            <h3>欢迎来到持明法州后台管理系统</h3>
        </div>
        <img src="${app}/images/36.jpg" style="width: 1510px;height: 600px;" alt="">
    </div>
</div>
<%--页脚--%>
<div class="panel panel-footer text-center">
    持明法州@百知教育 2019.10.28
</div>

</body>
</html>