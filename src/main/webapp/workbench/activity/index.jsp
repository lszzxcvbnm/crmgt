<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
request.getServerPort() + request.getContextPath() + "/";

%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		
		$("#addBtn").click(function(){
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			/*
			操作模态窗口的方式；需要操作的模态窗口的jquey对象,调用modal方法为该方法传递参数 show;打开窗口 hide;关闭窗口

			 */

			//走后台，取得用户信息列表为所有下拉框赋值
			$.ajax({
				url:"workbench/activity/getUserList.dao",
				data:{

				},
				type:"get",
				dataType:"json",
				success:function(data){
                  var html="<option></option>";
                //  遍历出来的每一n,就是一个user对象
					$.each(data,function(i,n){
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-owner").html(html);
                    //将当前登录的用户设置为默认选项
					var id="${user.id}";
					$("#create-owner").val(id);
					//所有者下拉框处理完毕后，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})
		  //为保定事件，执行添加操作
		$("#saveBtn").click(function(){
			$.ajax({
				url:"workbench/activity/save.dao",
				data:{

					"owner" :$.trim($("#create-owner").val()),
					"name" :$.trim($("#create-name").val()),
					"startDate" :$.trim($("#create-startDate").val()),
					"endDate" :$.trim($("#create-endDate").val()),
					"cost"  :$.trim($("#create-cost").val()),
					"description"  :$.trim($("#create-description").val()),



				},
				type:"post",
				dataType:"json",
				success:function(data){
                   if(data.success){
                        //刷新活动列表（局部刷新）
					   /*
					   ($("#activityPage").bs_pagination('getOption', 'currentPage')
					   表示停留在当前页
                       ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                       操作后每页展现已经设置好的条数
                          参数不需要修改，直接使用
					   */
					   pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


					  /* 清空添加操作模态窗口jqure对象提供了submit()方法让我们提交表单，没有提供reset方法重置表单
					   但原生的js提供了，所以我们要将jquery对象转化为dom对象
					   jquery 对象[下标]  转化为dom对象
					   $(dom)            转化为jquery对象

					   */
                        $("#activityAddForm")[0].reset();
					// 关闭添加操作的模态窗口
					   $("#createActivityModal").modal("hide");

				   }else{
                     	alert("添加失败");
				   }
				}
			})
		})
		//页面加载完成以后触发 的一个方法
		pageList(1,2);

		$("#searBtn").click(function(){
			//搜索框中的内容的信息保存起来，保存到隐藏阈值中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))

		})
		//为全选的复选框绑定事件，触发全选事件
		$("#qx").click(function(){
           $("input[name=zx]").prop("checked",this.checked);
		})
		/*$("input[name=zx]").click(function(){
                动态生成的元素不能用普通的绑定事件来操作
		})*/
		/*
		动态生成的元素用On方法来触发
		$(需要绑定元素的有效元素).on(绑定事件的方式，需要绑定元素的jquery对象，回调函数）
		*/
		$("#activityBody").on("click",$("input[name=zx]"),function(){
			$("#qx").prop("checked",$("input[name=zx]").length==$("input[name=zx]:checked").length);
		})
		//为删除按钮绑定事件，执行活动删除
        $("#deleteBtn").click(function () {
           //找到复选框中所有打勾的jquery
			var $xz=$("input[name=zx]:checked");
			if($xz.length==0){

				alert("请选择需要删除的记录");
			}else{

				if(confirm("确定删除所选中的记录吗？") ){
                  //url:workbench/activity/delete.dao

					//拼接参数
					var param="";
					//将$xz中的每一个dom对象遍历出来，取value值，就相当于取到了id值
					for(var i=0;i<$xz.length;i++){
						//$xz[i].value();
						param+="id="+$($xz[i]).val();
						//如果不是最后一个元素，需要在后面追加一个&符
						if(i<$xz.length-1){
							param+="&";
						}
					}
					$.ajax({
						url:"workbench/activity/delete.dao",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){
							if(data.success){
								//删除成功后
								//pageList(1,2);在第一页展现，原有条数
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


							}else{
								alert("删除数据失败");
							}

						}
					})



				}

			}
		})

	     //	为模态窗口绑定事件打开修改操作
		$("#editBtn").click(function(){
          var $xz=$("input[name=zx]:checked");
          if($xz.length==0){
          	alert("请选择需要修改的记录");
		  }else if($xz.length>1){
          	alert("只能选择一条记录修改");

		  }else{
			 var id= $xz.val();

             $.ajax({
				 url:"workbench/activity/getUserListActivity.dao",
				 data:{
				 	"id":id
				 },
				 type :"get",
				 dataType:"json",
				 success:function(data){
					  //处理所有者下拉框
                      var html="<option></option>";
                      $.each(data.uList,function(i,n){
                      	html+="<option value='"+n.id+"'>"+n.name+"<option>";
					  })
					 $("#edit-owner").html(html);
                 //     处理单条活动
					 $("#edit-id").val(data.a.id);
					 $("#edit-name").val(data.a.name);
					 $("#edit-owner").val(data.a.owner);
					 $("#edit-startData").val(data.a.startDate);
					 $("#edit-endData").val(data.a.endDate);
					 $("#edit-cost").val(data.a.cost);
					 $("#edit-description").val(data.a.description);

                    //所有值都填写好之后，打开修改的模态窗口
					 $("#editActivityModal").modal("show");





				 }
			 })
		  }

		})
            //为更新按钮绑定事件，执行市场活动的修改操作
		/*
		在实际项目开发中，一定是按照先做添加，再做修改的这种顺序所以，为了节省开发时间，修改操作一般都是copy添加操作
		*/
		$("#updateBtn").click(function(){
			$.ajax({
				url:"workbench/activity/update.dao",
				data:{
					"id" :$.trim($("#edit-id").val()),
					"owner" :$.trim($("#edit-owner").val()),
					"name" :$.trim($("#edit-name").val()),
					"startDate" :$.trim($("#edit-startData").val()),
					"endDate" :$.trim($("#edit-endData").val()),
					"cost"  :$.trim($("#edit-cost").val()),
					"description"  :$.trim($("#edit-description").val()),



				},
				type:"post",
				dataType:"json",
				success:function(data){
					if(data.success){
						alert($("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						// pageList(1,2);
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//刷新活动列表（局部刷新）

						// 关闭修改操作的模态窗口
						$("#editActivityModal").modal("hide");

					}else{
						alert("修改市场活动失败");
					}
				}
			})
		})
	});
	function pageList(pageNo,pageSize){
		//将全选的复选框去掉勾
		$("qx").prop("checked",false);
		//查询前，将隐藏域中保存的信息取出来，重新赋值到搜素框中
		$("#hidden-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));
		$.ajax({
			url:"workbench/activity/pageList.dao",
			data:{
                "pageNo":pageNo ,
				"pageSize": pageSize,
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner ").val()),
				"startDate": $.trim($("#search-startDate").val()),
				"endDate": $.trim($("#search-endDate").val()),


			},
			type:"get",
			dataType:"json",
			success:function(data){
               /*
               * data:市场活动{{1}{2}}LIstanbu<activiity>alist
               * 插件需要总的数据数
               * */
            var html="";
            //每一个n就是一个市场活动对象
				$.each(data.dataList,function(i,n){
				    html+='<tr class="active">';
					html+='<td><input type="checkbox" name="zx"value="'+n.id+'"/></td>';
				    html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.dao?id='+n.id+'\';">'+n.name+'</a></td>';
					html+='<td>'+n.owner+'</td>';
					html+='<td>'+n.startDate+'</td>';
					html+='<td>'+n.endDate+' </td>';
					html+='</tr>';
				})
				$("#activityBody").html(html);
				//计算总页数
				var totalPages =data.total%pageSize ==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
			//	数据处理完成以后，用前端插件展现分页信息

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
                        //该回调函数是在，点击分页主键的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})

	}

</script>
</head>
<body>
    <input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--
					data-dismiss="modal"表示关闭模态窗口
					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					   <input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startData">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endData">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--
								文本域textarea:
								(1)一定要以标签对的形式来呈现
								（2）textarea:属于表单元素，取值和赋值统一使用。val()形式
								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text"id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text"id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endtDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
					点击创建按钮，观察俩个属性和属性值
					data-toggle="modal"表示触发该按钮，将打开一个模态窗口
                     data-target="#createActivityModal"  表示要打开哪个模态窗口

                     现在我们是以属性和属性值的方式写在了button元素中用来打开模态窗口 但是这样
                     做的问题是；没有办法对按钮的其它功能进行扩充
                     所以未来的实际现木开发中，对于触发模态窗口的操作，一定不要写在元素当中，应该写js代码来操作
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody  id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>