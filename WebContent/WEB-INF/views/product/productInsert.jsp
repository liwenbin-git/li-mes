<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>材料管理</title>
<%@ include file="/common/backend_common.jsp" %>
<script type="text/javascript" src="product.js"></script>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox"
		class="ace ace-switch ace-switch-5" />
	<div class="page-header">
		<h1>
			材料管理 <small><i class="ace-icon fa fa-angle-double-right"></i>
				创建与查询 </small>
		</h1>
	</div>
	<div class="main-content-inner">
		<div class="col-sm-12">
			<div class="col-xs-12">
				<div class="table-header">
					材料列表&nbsp;&nbsp; <!-- <a class="green" href="#"> <i
						class="ace-icon fa fa-plus-circle orange bigger-130 order-add"></i>
					</a>-->
				</div> 
<div id="dialog-plan-form">
		<form id="productForm">
			<table
				class="table table-striped table-bplaned table-hover dataTable no-footer"
				role="grid">
				<input type="hidden" name="id" id="input-Id" value="" />
				<tr>
					<td><label for="productImgid">图号</label></td>
					<td><input id="input-productImgid" type="text" name="productImgid"
						value="" class="text ui-widget-content ui-corner-all"></td>
						<td><label for="productMaterialname">材料名称</label></td>
					<td><input id="input-productMaterialname" type="text"
						name="productMaterialname" value=""
						class="text ui-widget-content ui-corner-all"></td>
				</tr>
				<tr>
					<td><label for="productMaterialsource">材料来源</label></td>
					<td><select id="input-productMaterialsource" name="productMaterialsource"
						data-placeholder="选择类型" style="width: 150px;">
						    <option value="钢材">钢材</option>
							<option value="废料">废料</option>
							<option value="钢锭">钢锭</option>
							<option value="外协件">外协件</option>
							<option value="外购件">外购件</option>
					</select></td>
					<td><label for="productTargetweight">工艺重量</label></td>
					<td><input id="input-productTargetweight" type="number" name="productTargetweight"
						value="" class="text ui-widget-content ui-corner-all"></td>
				</tr>
				<tr>
					<td><label for="productRealweight">投料重量</label></td>
					<td><input id="input-productRealweight" type="number" name="productRealweight"
						value="" class="text ui-widget-content ui-corner-all"></td>
						<td><label for="productLeftweight">剩余重量</label></td>
					<td><input id="input-productLeftweight" type="number" name="productLeftweight"
						value="" class="text ui-widget-content ui-corner-all"></td>
				</tr>
				
				<tr>
					<td><label for="productIrontypeweight">锭型</label></td>
					<td><input id="input-productIrontypeweight" type="number" name="productIrontypeweight"
						value="" class="text ui-widget-content ui-corner-all"></td>
						<td><label for="productIrontype">钢锭类别</label></td>
					<td><input id="input-productIrontype" type="text" name="productIrontype"
						value="" class="text ui-widget-content ui-corner-all"></td>
				</tr>
				<tr>
					<td><label for="productStatus">是否启用</label></td>
					<td><select id="input-productStatus" name="productStatus"
						data-placeholder="选择类型" style="width: 150px;">
						<option value="0">未启用</option>
						<option value="1">启用</option>
					</select></td>
						<td><label for="productRemark">备注</label></td>
					<td><input id="input-productRemark" type="text" name="productRemark"
						value="" class="text ui-widget-content ui-corner-all"></td>
				</tr>
				
				<tr>
					<td><label for="count">批量生成个数</label></td>
					<td><input id="input-count" type="number" name="count"
						value="" class="text ui-widget-content ui-corner-all"></td>
						<td><label >生成材料</label></td>
					<td><button class="btn btn-info fa fa-check batchInsert-btn"
					style="margin-bottom: 6px;" type="button">点击按钮</button></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>