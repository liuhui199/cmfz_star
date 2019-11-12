<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<c:set var="app" value="${pageContext.request.contextPath}"></c:set>
<script>
    $(function () {
        $("#user-table").jqGrid({
            url: '${app}/user/selectAll',
            datatype: "json",
            colNames: ['编号', '用户名', '密码', '电话', '昵称', '头像', '省', '城市', '性别', '上传日期'],
            colModel: [
                {name: "id", hidden: true},
                {name: "username", editable: true},
                {name: "password", editable: true},
                {name: "phone", editable: true},
                {name: "nickname", editable: true},
                {
                    name: "photo", editable: true, edittype: "file", formatter: function (value, option, rows) {
                        return "<img style='width:100px;height:60px;' src='${app}/images/" + rows.photo + "'>";
                    }
                },
                {name: "province", editable: true},
                {name: "city", editable: true},
                {name: "sex", editable: true},
                {name: "createDate"}
            ],
            height: 500,
            autowidth: true,
            styleUI: "Bootstrap",
            rowNum: 3,
            rowList: [3, 5, 10],
            pager: '#user-page',
            sortname: 'id',
            viewrecords: true,
            editurl: "${app}/user/edit",
            caption: "用户列表",
        }).navGrid("#user-page", {edit: true, add: true, del: true, search: false}, {
            //控制修改
            closeAfterEdit: true,
            beforeShowForm: function (fmt) {
                fmt.find("#photo").attr("disabled", true);
            }
        }, {
            //控制添加
            closeAfterAdd: true,
            afterSubmit: function (data) {
                console.log(data);
                var status = data.responseJSON.status;
                var id = data.responseJSON.message;
                if (status) {
                    $.ajaxFileUpload({
                        url: "${app}/user/upload",
                        type: "post",
                        fileElementId: "photo",
                        data: {id: id},
                        success: function (response) {
                            //自动刷新jqgrid表格
                            $("#user-table").trigger("reloadGrid");
                        }
                    });
                }
                return "123";
            }
        });
    })
</script>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="#">所有用户</a></li>
    <li role="presentation"><a href="${pageContext.request.contextPath}/user/export">导出用户数据</a></li>
</ul>
<table id="user-table"></table>
<div id="user-page" style="height: 40px"></div>