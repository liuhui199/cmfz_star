<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<c:set var="app" value="${pageContext.request.contextPath}"></c:set>

<script>
    $(function () {
        $("#album-table").jqGrid({
            url : "${app}/album/selectAll",
            datatype : "json",
            colNames : [ '编号', '专辑名', '封面', '章节数', '作者','简介','上传时间'],
            colModel : [
                {name : 'id',hidden:true,editable:false},
                {name : 'name',editable:true},
                {name : 'cover',editable:true,edittype:"file",formatter:function (value,option,rows) {
                        return "<img style='width:100px;height:60px;' src='${app}/images/"+rows.cover+"'>";
                    }},
                {name : 'count',editable:true},
                {name : 'starId',editable:true,edittype:"select",editoptions:{dataUrl:"${app}/star/getAllStar"},
                    formatter:function (value, option, rows) {
                       return  rows.star.realname;
                    }},
                {name : 'brief',editable:true},
                {name : 'createDate',}
            ],
            height:500,
            autowidth:true,
            styleUI:"Bootstrap",
            rowNum : 3,
            rowList : [ 3,5,10 ],
            pager : '#album-page',
            sortname : 'id',
            viewrecords : true,
            multiselect : false,
            editurl:"${app}/album/edit",
            subGrid:true,
            caption : "所有专辑列表",

            subGridRowExpanded : function(subgrid_id, id) {
                var subgrid_table_id, pager_id;
                subgrid_table_id = subgrid_id + "_t";
                pager_id = "p_" + subgrid_table_id;
                $("#" + subgrid_id).html(
                    "<table id='" + subgrid_table_id + "' class='scroll'></table>" +
                    "<div id='" + pager_id + "' class='scroll'></div>");
                jQuery("#" + subgrid_table_id).jqGrid(
                    {
                        url : "${app}/chapter/selectAll?albumId=" + id,
                        datatype : "json",
                        colNames : [ '编号', '章节名', '歌手', '时长','大小','上传日期',"在线播放" ],
                        colModel : [
                            {name : "id",hidden: true,editable:false},
                            {name : "name",editable:true,edittype:"file"}, //文件
                            {name : "singer",editable:true},
                            {name : "sizes"},
                            {name : "duration"},
                            {name : "createDate"},
                            {name : "operation",width:300,formatter:function (value,option,rows) {
                                    return "<audio controls>\n" +
                                        "  <source src='${app}/music/"+rows.name+"' >\n" +
                                        "</audio>";
                                }}
                        ],
                        styleUI:"Bootstrap",
                        rowNum : 3,
                        rowList : [ 3,5,10 ],
                        pager : pager_id,
                        autowidth:true,
                        height : '100%',
                        editurl:"${app}/chapter/edit?albumId="+id
                    });
                jQuery("#" + subgrid_table_id).jqGrid('navGrid',
                    "#" + pager_id, {
                        edit : true,
                        add : true,
                        del : true,
                        search: false
                    },{
                        //控制修改
                        closeAfterEdit:true,
                        beforeShowForm:function (fmt) {
                            fmt.find("#name").attr("disabled",true);
                        }
                    },{  //添加
                        closeAfterAdd: true,
                        afterSubmit:function (response) {
                            var status = response.responseJSON.status;
                            if(status){
                                var cid = response.responseJSON.message;
                                $.ajaxFileUpload({
                                    url:"${app}/chapter/upload",
                                    type: "post",
                                    fileElementId: "name",
                                    data:{id:cid,albumId:id},
                                    success:function () {
                                        $("#"+subgrid_table_id).trigger("reloadGrid");
                                    }
                                })
                            }
                            return "123";
                        }
                    });
            },
        }).navGrid("#album-page", {
            edit : true,
            add : true,
            del : true,
            search:false
        },{
            //控制修改
            closeAfterEdit:true,
            beforeShowForm:function (fmt) {
                fmt.find("#cover").attr("disabled",true);
            }
        },{
           closeAfterAdd:true,
           afterSubmit:function (data) {
               var status = data.responseJSON.status;
               var message = data.responseJSON.message;
               if(status){
                   $.ajaxFileUpload({
                       url:"${app}/album/upload",
                       data:{id:message},
                       fileElementId:"cover",
                       type:"post",
                       success:function () {
                           $("#album-table").trigger("reloadGrid");
                       }
                   })
               }
               return "xxx";
           }
        });
    })
</script>
<!--页头-->
<div class="panel panel-heading" style="margin-top: -20px;">
    <h1>展示所有专辑</h1>
</div>
<table id="album-table"></table>
<div id="album-page" style="height: 40px"></div>