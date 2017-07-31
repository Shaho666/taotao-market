<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table cellpadding="5" style="margin-left: 30px" id="itemParamAddTable"
	class="itemParam">
	<tr>
		<td>商品类目:</td>
		<td>
			<a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">编辑类目</a> 
			<input type="text" id="ename" name="ename" style="width: 280px;" readonly="readonly"></input>
			<input type="hidden" id="eid" name="eid" style="width: 280px;"></input>
		</td>
	</tr>
	<tr class="hide addGroupTr">
		<td>规格参数:</td>
		<td>
			<ul id="_params">
				<li><a href="javascript:void(0)"
					class="easyui-linkbutton addGroup">添加分组</a></li>
			</ul>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton submit">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton close">关闭</a></td>
	</tr>
</table>
<div class="itemParamAddTemplate" style="display: none;">
	<li class="param">
		<ul>
			<li><input class="easyui-textbox" style="width: 150px;"
				name="group" />&nbsp;<a href="javascript:void(0)"
				class="easyui-linkbutton addParam" title="添加参数"
				data-options="plain:true,iconCls:'icon-add'"></a></li>
			<li><span>|-------</span><input style="width: 150px;"
				class="easyui-textbox" name="param" />&nbsp;<a
				href="javascript:void(0)" class="easyui-linkbutton delParam"
				title="删除" data-options="plain:true,iconCls:'icon-cancel'"></a></li>
		</ul>
	</li>
</div>
<script style="">
	$(function() {

		var selectedNode = $("#itemParamList").datagrid("getSelected");

		var itemCatName = selectedNode.itemCatName;
		var itemId = selectedNode.id;
		$("#eid").val(itemId);
		$("#ename").val(itemCatName);

		var paramData = selectedNode.paramData;
		var json = JSON.parse(paramData);

		/////////////////////////////////////////////////
		$.each(json, function(i, e) {
			var _this = $(".addGroup");
			var temple = $(".itemParamAddTemplate li").eq(0).clone();
			temple.find('input').eq(0).val(e.group);
			temple.find('li').eq(1).remove();
			_this.parent().parent().append(temple);
			temple.find(".addParam").click(function() {
				var li = $(".itemParamAddTemplate li").eq(2).clone();
				li.find(".delParam").click(function() {
					$(this).parent().remove();
				});
				li.appendTo($(this).parentsUntil("ul").parent());
			});
			/* temple.find(".delParam").click(function() {
				_this.parent().remove();
			}); */
			
			$.each(e.params, function(i2, e2) {
				var input_addParam = temple.find(".addParam");
				var li = $(".itemParamAddTemplate li").eq(2).clone();
				li.children().eq(1).val(e2);
				li.find(".delParam").click(function() {
					//input_addParam.parent().remove();
					li.remove();
				});
				li.appendTo(input_addParam.parentsUntil("ul").parent());
			});			
			
		});
		/////////////////////////////////////////////////
		
		/* $.each(json, function(i, e) {
			var temple = $(".itemParamAddTemplate li").eq(1).clone();
			temple.children().eq(0).val(e.group);
			//$(".addGroup").parent().parent().append(temple);
			$("#_params").append(temple);

			temple.find(".addParam").click(function() {
				var li = $(".itemParamAddTemplate li").eq(2).clone();
				li.find(".delParam").click(function() {
					$(this).parent().remove();
				});
				li.appendTo($(this).parentsUntil("ul"));
			});

			$.each(e.params, function(i2, e2) {
				var input_addParam = temple.find(".addParam");
				var li = $(".itemParamAddTemplate li").eq(2).clone();
				li.children().eq(1).val(e2);
				li.find(".delParam").click(function() {
					//input_addParam.parent().remove();
					li.remove();
				});
				li.appendTo(input_addParam.parentsUntil("ul"));
			});
		}); */
		/////////////////////////////////////////
		$(".addGroupTr").show();

		/* 		TAOTAO.initItemCat({
		 fun:function(node){
		 $(".addGroupTr").hide().find(".param").remove();
		 //  判断选择的目录是否已经添加过规格
		 $.getJSON("/item/param/query/itemcatid/" + node.id,function(data){
		 if(data.status == 200 && data.data){
		 $.messager.alert("提示", "该类目已经添加，请选择其他类目。", undefined, function(){
		 $("#itemParamAddTable .selectItemCat").click();
		 });
		 return ;
		 }
		 $(".addGroupTr").show();
		 });
		 }
		 }); */

		$(".addGroup").click(function() {
			var temple = $(".itemParamAddTemplate li").eq(0).clone();
			$(this).parent().parent().append(temple);
			temple.find(".addParam").click(function() {
				var li = $(".itemParamAddTemplate li").eq(2).clone();
				li.find(".delParam").click(function() {
					$(this).parent().remove();
				});
				li.appendTo($(this).parentsUntil("ul").parent());
			});
			temple.find(".delParam").click(function() {
				$(this).parent().remove();
			});
		});

		$("#itemParamAddTable .close").click(function() {
			$(".panel-tool-close").click();
		});

		$("#itemParamAddTable .submit").click(
				function() {
					var params = [];
					var groups = $("#itemParamAddTable [name=group]");
					groups.each(function(i, e) {
						var p = $(e).parentsUntil("ul").parent().find(
								"[name=param]");
						var _ps = [];
						p.each(function(_i, _e) {
							var _val = $(_e).siblings("input").val();
							if ($.trim(_val).length > 0) {
								_ps.push(_val);
							}
						});
						var _val = $(e).siblings("input").val();
						if ($.trim(_val).length > 0 && _ps.length > 0) {
							params.push({
								"group" : _val,
								"params" : _ps
							});
						}
					});
					var url = "/item/param/update/"
							+ $("#itemParamAddTable [name=eid]").val();
					$.post(url, {
						"paramData" : JSON.stringify(params)
					}, function(data) {
						if (data.status == 200) {
							$.messager.alert('提示', '更新商品规格成功!', undefined,
									function() {
										$(".panel-tool-close").click();
										$("#itemParamList").datagrid("reload");
									});
						}
					});
				});
	});
</script>