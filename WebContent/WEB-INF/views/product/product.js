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
			url="/product/product.json"
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
				//search_status:search_status,
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
			url = "/product/product.json";
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
										return this.productStatus == 1 ? '已入库'
												: (this.productStatus == 0 ? '未入库'
														: '删除');
									},
									"bold" : function() {
										return function(text, render) {
											var status = render(text);
											if (status == '已入库') {
												return "<span class='label label-sm label-success'>已入库</span>";
											} else if (status == '未入库') {
												return "<span class='label label-sm label-warning'>未入库</span>";
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
			showMessage("获取订单列表", result.msg, false);
		}
	}
	$(".batchInsert-btn").click(
			function() {
				$.ajax({
					url :  "/product/insert.json",
					data :  $("#productForm").serializeArray(),
					type : 'POST',
					success : function(result) {
						//数据执行成功返回的消息
						 location.href = "/product/product.page";
					}
				});
				
			});		
	
	//修改数据
	function bindProductClick(){
	$(".product-edit").click(function(e) {
		//阻止默认事件
        e.preventDefault();
		//阻止事件传播
        e.stopPropagation();
		//获取planid
        var productId = $(this).attr("data-id");
		//弹出plan的修改弹窗 
        $("#dialog-productUpdate-form").dialog({
            model: true,
            title: "编辑钢材",
            open: function(event, ui) {
         	    $(".ui-dialog").css("width","600px");
                $(".ui-dialog-titlebar-close", $(this).parent()).hide();
              	//将form表单中的数据清空，使用jquery转dom对象
                $("#productUpdateForm")[0].reset();
              	//拿到map中以键值对，id-plan对象结构的对象,用来向form表单中传递数据
                var targetOrder = productMap[productId];
              	//如果取出这个对象
                if (targetOrder) {
					/////////////////////////////////////////////////////////////////
					$("#input-id2").val(targetOrder.id);
					$("#input-productImgid2").val(targetOrder.productImgid);
					$("#input-productMaterialname2").val(targetOrder.productMaterialname);
					$("#input-productMaterialsource2").val(targetOrder.productMaterialsource);
					$("#input-productTargetweight2").val(targetOrder.productTargetweight);
					$("#input-productIrontypeweight2").val(targetOrder.productIrontypeweight);
					$("#input-productIrontype2").val(targetOrder.productIrontype);
					$("#input-productRemark2").val(targetOrder.productRemark);
					$("#input-productRealweight2").val(targetOrder.productRealweight);
					$("#input-productLeftweight2").val(targetOrder.productLeftweight);
					$("#input-furnacenumber2").val(targetOrder.furnacenumber);
					/////////////////////////////////////////////////////////////////
                }
            },
            buttons : {
                "更新": function(e) {
                    e.preventDefault();
                    updateOrder(false, function (data) {
                        $("#dialog-productUpdate-form").dialog("close");
        				$("#productPage .pageNo").val(1);
        				loadProductList();
                    }, function (data) {
                        showMessage("更新材料", data.msg, false);
                    })
                },
                "取消": function (data) {
                    $("#dialog-productUpdate-form").dialog("close");
                }
            }
        });
    });
   } 

	function updateOrder(isCreate, successCallbak, failCallbak) {
		$.ajax({
			url : "/product/update.json",
			data :  $("#productUpdateForm").serializeArray(),
			type : 'POST',
			success : function(result) {
				//数据执行成功返回的消息
				if (result.ret) {
					loadProductList(); // 带参数回调
					//带参数回调
					if (successCallbak) {
						successCallbak(result);
					}
				} else {
					//执行失败后返回的内容
					if (failCallbak) {
						failCallbak(result);
					}
				}
			}
		});
	}
	
})