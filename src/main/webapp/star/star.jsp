<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<c:set var="app" value="${pageContext.request.contextPath}"></c:set>

<script>
    $(function () {
        $("#star-table").jqGrid({
            url : '${app}/star/selectAll',
            datatype : "json",
            colNames : [ '编号', '艺名', '实名', '照片', '性别','生日'],
            colModel : [
                {name : 'id',hidden:true},
                {name : 'nickname',editable:true},
                {name : 'realname',editable:true},
                {name : 'photo',editable:true,edittype:"file",formatter:function (value,option,rows) {
                        return "<img style='width:100px;height:60px;' src='${app}/images/"+rows.photo+"'>";
                    }},
                {name : 'sex',editable:true,edittype:"select",editoptions:{value:"男:男;女:女"}},
                {name : 'bir',editable:true,edittype:"date"}
            ],
            height:500,
            autowidth:true,
            styleUI:"Bootstrap",
            rowNum : 3,
            rowList : [ 3,5,10 ],
            pager : '#star-page',
            sortname : 'id',
            viewrecords : true,
            editurl:"${app}/star/edit",
            subGrid:true,
            caption : "所有明星列表",

            subGridRowExpanded : function(subgrid_id, id) {
                var subgrid_table_id, pager_id;
                subgrid_table_id = subgrid_id + "_t";
                pager_id = "p_" + subgrid_table_id;
                $("#" + subgrid_id).html(
                    "<table id='" + subgrid_table_id + "' class='scroll'></table>" +
                    "<div id='" + pager_id + "' class='scroll'></div>");
                jQuery("#" + subgrid_table_id).jqGrid(
                    {
                        url : "${app}/user/selectAll?starId=" + id,
                        datatype : "json",
                        colNames : [ '编号', '用户名', '密码', '昵称','头像','省','城市','性别','上传日期' ],
                        colModel : [
                            {name : "id",},
                            {name : "username",},
                            {name : "password",},
                            {name : "nickname",},
                            {name : "photo",editable:true,edittype:"file",formatter:function (value,option,rows) {
                                    return "<img style='width:100px;height:60px;' src='${app}/images/"+rows.photo+"'>";}},
                            {name : "province",},
                            {name : "city",},
                            {name : "sex",},
                            {name : "createDate",}
                        ],
                        rowNum : 2,
                        rowList : [ 2,3,5 ],
                        pager : pager_id,
                        autowidth:true,
                        height : '100%'
                    });
                jQuery("#" + subgrid_table_id).jqGrid('navGrid',
                    "#" + pager_id, {
                        edit : false,
                        add : false,
                        del : false,
                        search: false
                    });
            },
        }).navGrid("#star-page", {
            edit : true,
            add : true,
            del : true,
            search:false
        },{
            //控制修改
            closeAfterEdit:true,
            beforeShowForm:function (fmt) {
                fmt.find("#photo").attr("disabled",true);
            }
        },{
           closeAfterAdd: true,
           afterSubmit:function (data) {
               var status = data.responseJSON.status;
               var message = data.responseJSON.message;
               if(status){
                   $.ajaxFileUpload({
                       url:"${app}/star/upload",
                       data:{id:message},
                       fileElementId: "photo",
                       type:"post",
                       success:function () {
                           $("#star-table").trigger("reloadGrid");
                       }
                   })
               }
               return "123";
           }
        });
    })
</script>
<!--页头-->
<div class="panel panel-heading" style="margin-top: -20px;">
    <h1>展示所有明星</h1>
</div>
<table id="star-table"></table>
<div id="star-page" style="height: 40px"></div>