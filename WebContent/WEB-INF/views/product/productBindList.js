$(function(){
	//分页
	var productMap={};
	var optionStr;
	var pageSize;//每一页页的条数
	var pageNo;//当前页
	var url;
	var keyword;//关键词
	var search_status;//材料来源
	var search_msource;;
	//加载模板内容
	var productListTemplate=$("#productListTemplate").html();
	//使用mustache模板加载内容
	Mustache.parse(productListTemplate);
	//渲染分页列表
	loadProductList();
	//点击刷新
	$(".research").click(function(e){
		e.preventDefault();
		$("#productPage.pageNo").val(1);
		loadProductList();
	});
	//定义分页函数
	function loadProductList(urlnew){
		//获取当前页码要调用的条数
		pageSize=$("#pageSize").val();
		//获取当前页
		pageNo=$("#productPage.pageNo").val() || 1;
		if(urlnew){
			url=urlnew;
		}else{
			url="/product/productBindList.json"
		}
		keyword=$("#keyword").val();
		search_status=$("#search_status").val();
		search_msource = $("#search_msource").val();
		$.ajax({
			url:url,
			data:{
				pageNo:pageNo,
				pageSize:pageSize,
				keyword:keyword,
				search_status:search_status,
				search_msource:search_msource,
			},
			type:"POST",
			success:function(result){
				renderProductListAndPage(result,url);
			}
		});
	}
	function renderProductListAndPage(result,url){
		if (result.ret) {
			//再次初始化查询条件
			url = "/product/productBindList.json";
			keyword = $("#keyword").val();
			search_status = $("#search_status").val();
			search_msource = $("#search_msource").val();
			//如果查询到数据库中有符合条件的order列表
			if (result.data.total > 0) {
				//为订单赋值--在对orderlisttemplate模板进行数据填充--视图渲染
				var rendered = Mustache.render(
						productListTemplate,//<script id="orderListTemplate" type="x-tmpl-mustache">
								{
									"productList" : result.data.data,//{{#orderList}}--List-(result.data.data-list<MesOrder>)
									"showStatus" : function() {
										return this.productStatus == 1 ? '有效'
												: (this.productStatus == 0 ? '无效'
														: '删除');
									},
									"bold" : function() {
										return function(text, render) {
											var status = render(text);
											if (status == '有效') {
												return "<span class='label label-sm label-success'>有效</span>";
											} else if (status == '无效') {
												return "<span class='label label-sm label-warning'>无效</span>";
											} else {
												return "<span class='label'>删除</span>";
											}
										}
									}
					
				});
				$.each(result.data.data,function(i,product){
					productMap[product.id] = product;
					
				});
				$('#productList').html(rendered);
			} else {
				$('#productList').html('');
			}
			bindProductClick();//更新操作
			var pageSize = $("#pageSize").val();
			var pageNo = $("#productPage .pageNo").val() || 1;
			//渲染页码
			renderPage(
					url,
					result.data.total,
					pageNo,
					pageSize,
					result.data.total > 0 ? result.data.data.length : 0,
					"productPage", loadProductList);
		} else {
			showMessage("获取材料列表", result.msg, false);
		}
	}
	//点击绑定
	function bindProductClick(){
		   $(".product-bind").click(function(e) {
			//阻止默认事件
         e.preventDefault();
			//阻止事件传播
         e.stopPropagation();
			//获取planid
         //var leftweight=$(this).attr("data-weight");
         var productId = $(this).attr("data-id");
         window.location.href="/product/productBind.page?id="+productId;
			return;
     });
	 }  

});