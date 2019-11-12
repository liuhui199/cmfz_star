<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<c:set var="app" value="${pageContext.request.contextPath}"></c:set>
<script>
    $(function () {
        $("#article-search").click(function () {
            $.ajax({
                url: "${app}/article/search",
                type: "post",
                dataType: "json",
                data: "content=" + $("#article-input").val(),
                success: function (data) {
                    $("#article-search-show").empty();
                    $.each(data, function (i, article) {
                        var tr = $("<tr>" +
                            "<td>" + article.name + "</td>" +
                            "<td>" + article.author + "</td>" +
                            "<td>" + article.brief + "</td>" +
                            "<td><a class='btn btn-danger'>详情</a></td>)" + "</tr>");
                        $("#article-search-show").append(tr);
                    })
                }
            })
        })
    })
</script>
<div class="row">
    <div class="col-sm-2"></div>
    <div class="col-sm-6">
        <div class="input-group">
            <input type="text" class="form-control" id="article-input" placeholder="请输入关键字...">
            <span class="input-group-btn">
                <button class="btn btn-primary" type="button" id="article-search">搜索一下</button>
            </span>
        </div>
    </div>
    <div class="col-sm-4"></div>
    <table class="table table-hover" id="article-search-show"></table>
</div>