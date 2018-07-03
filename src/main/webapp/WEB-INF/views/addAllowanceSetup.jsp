<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<!--   <title>Dashboard - Lexicon Merchandise</title> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<link href="<c:url value='/resource/w2ui/w2ui-1.5.rc1.css' />"
	rel="stylesheet"></link>
<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.js' />"></script>
<link href="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.css' />"
	rel="stylesheet"></link>
<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.js' />"></script>


<link
	href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.dataTables.min.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.12.3.js"></script>
<script
	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script
	src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script
	src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>

<c:url var="findURL" value="/checkAllowanceSetup.htm" />

<style>
.add-on, textarea, input[type="text"], input[type="password"], input[type="datetime"],
	input[type="datetime-local"], input[type="date"], input[type="month"],
	input[type="time"], input[type="week"], input[type="number"], input[type="email"],
	input[type="url"], input[type="search"], input[type="tel"], input[type="color"],
	input[type="file"], .uneditable-input {
	border-color: #4CAF50 !important;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border-radius: 2px;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
}
</style>
<script>
	var yourUrl = '<c:out value="${findURL}"/>';

	$(document).ready(function() {
		$('#table1').DataTable();

		$('.req-save-update-btn').click(function() {
			var flag = false, deptCheckResp = false;
			$(".req-save-update-btn").prop('disabled', true);

			if ($('#name').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				$(".req-save-update-btn").prop('disabled', false);
				w2alert("Please Input Name.");
				return flag;
			}

			if ($('#allowUnit').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				$(".req-save-update-btn").prop('disabled', false);
				w2alert("Please Select Input.");
				return flag;
			}

			if ($('#allowAmount').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				$(".req-save-update-btn").prop('disabled', false);
				w2alert("Please Input Amount.");
				return flag;
			}

			if (!flag) {
				deptCheckResp = checkAllownce();
			}

			if (deptCheckResp) {
				$(".req-save-update-btn").prop('disabled', true);
				$('#allowanceSetupForm').submit();
			} else {
				$(".req-save-update-btn").prop('disabled', false);
				$('#name').value = "";
				$('#name').focus();
				w2alert("Allownce Setup  already exist!!");
				return flag;
			}
		});

	});

	function checkAllownce() {
		var res = false;
		var deptName = $('#name');
		if ($('.name_old').val() != $('#name').val()) {

			$.ajax({
				type : "post",
				url : yourUrl,
				async : false,
				data : 'name=' + $('#name').val(),
				success : function(response) {
					if (response == "false") {
						res = false;
					} else {
						res = true;
					}

				},
				error : function() {
					res = false;
					w2alert("Server Error!!");
				}
			});
		} else {

			res = true;
		}

		return res;
	}

	function isNumeric(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	}
</script>
<script type="text/javascript">

	
	$("#allowAmount").on("keypress",function(){
		
		alert(1);
	});
</script>

<script>
$(document).ready(function() {
    $("#allowAmount,#allowMaximum,#allowMinimum").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
             // Allow: Ctrl/cmd+A
            (e.keyCode == 65 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: Ctrl/cmd+C
            (e.keyCode == 67 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: Ctrl/cmd+X
            (e.keyCode == 88 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
});
// $(document).ready(function(){
	
	
//     $("#allowAmount").keydown(function(){
//     	var val = $('#allowAmount').val();
//     	if (!$.isNumeric(val)) {
//     		$('#allowAmount').val('');
//     	} 
    	
//     });
//     $("#allowMaximum").keypress(function(){
//     	var val = $('#allowMaximum').val();
//     	if (!isNumeric(val)) {
//     		$('#allowMaximum').val('');
//     	} 
//     });
//     $("#allowMinimum").keypress(function(){
//     	var val = $('#allowMinimum').val();
//     	if (!isNumeric(val)) {
//     		$('#allowMinimum').val('');
//     	} 
//     });
// });
</script>
</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<c:choose>
			<c:when test="${edit}">
				<h2 class="pull-left" style="color: #5BBC2E;">Edit Allowance</h2>
			</c:when>
			<c:otherwise>
				<h2 class="pull-left" style="color: #5BBC2E;">Add New Allowance</h2>
			</c:otherwise>
		</c:choose>
		<div class="clearfix"></div>
	</div>
	<!-- Page heading ends -->
	<!-- Matter -->

	<!--   <div class="matter"> -->
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="widget wgreen">
					<div class="widget-head">
						<div class="pull-left"></div>
						<div class="widget-icons pull-right">
							<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
							<a href="#" class="wclose"><i class="fa fa-times"></i></a>
						</div>
						<div class="clearfix"></div>
					</div>

					<div class="widget-content">
						<div class="padd">
							<c:if test="${!empty message}">
								<div style="color: olive; font-size: 20px;">${message}</div>
							</c:if>

							<br /> <input type="hidden" value="${allowanceSetup.name}"
								readonly="true" class="form-control name_old" />
							<!-- Form starts.  -->
							<form:form id="allowanceSetupForm" Class="form-horizontal"
								method="POST" modelAttribute="allowanceSetupForm"
								action="${pageContext.request.contextPath}/saveAllowanceSetup.htm"
								class="form-horizontal" role="form">

								<form:input type="hidden" path="id" value="${allowanceSetup.id}"
									readonly="true" class="form-control" />

								<div class="form-group">
									<label class="col-lg-2 control-label">Allowance Name:</label>
									<div class="col-lg-5">
										<form:input id="name" path="name"
											value="${allowanceSetup.name}" class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Unit:</label>
									<div class="col-lg-5">
										<form:select id="allowUnit" path="allowUnit"
											class="form-control">
											<c:if test="${allowanceSetup.allowUnit=='percentage'}">
												<form:option value="">Select</form:option>
												<form:option value="percentage" selected="selected">Percentage</form:option>
												<form:option value="fixedValue">Fixed Value</form:option>


											</c:if>
											<c:if test="${allowanceSetup.allowUnit=='fixedValue'}">
												<form:option value="">Select</form:option>
												<form:option value="percentage">Percentage</form:option>
												<form:option value="fixedValue" selected="selected">Fixed Value</form:option>


											</c:if>

											<c:if test="${ empty  allowanceSetup.allowUnit }">
												<form:option value="" selected="selected">Select</form:option>
												<form:option value="percentage">Percentage</form:option>
												<form:option value="fixedValue">Fixed Value</form:option>


											</c:if>

										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Amount:</label>
									<div class="col-lg-5">
										<form:input id="allowAmount" path="allowAmount" type="number"
											value="${allowanceSetup.allowAmount}" class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Maximum:</label>
									<div class="col-lg-5">
										<form:input id="allowMaximum" path="allowMaximum"
											type="number" value="${allowanceSetup.allowMaximum}"
											class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Minimum:</label>
									<div class="col-lg-5">
										<form:input id="allowMinimum" path="allowMinimum"
											type="number" value="${allowanceSetup.allowMinimum}"
											class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Status:</label>
									<div class="col-lg-5">


										<c:choose>
											<c:when test="${allowanceSetup.status==1}">
												<form:radiobutton path="status" value="1" checked="checked"
													label="Enable" />
												<form:radiobutton path="status" value="0" label="Disable" />
											</c:when>
											<c:otherwise>
												<form:radiobutton path="status" value="1" label="Enable" />
												<form:radiobutton path="status" value="0" checked="checked"
													label="Disable" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>


								<div class="form-group">
									<c:choose>
										<c:when test="${edit}">
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="button" name="submit_btn" value="update"
													class="btn btn-sm btn-primary btn-success req-save-update-btn">
													Update</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<a class="btn btn-sm btn-danger"
													href="${pageContext.request.contextPath}/newAllowanceSetupForm.htm">Cancel</a>
											</div>
										</c:when>
										<c:otherwise>
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="button" name="submit_btn" value="save"
													class="btn btn-sm btn-primary req-save-update-btn">Save</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<button type="reset" class="btn btn-sm btn-danger ">Reset</button>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</form:form>

						</div>

					</div>
					<div class="widget-foot">
						<!-- Footer goes here -->
					</div>
				</div>

				<!-- Table Start-->
				<div class="row">
					<div class="col-md-12">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">Allowance Details</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="table-responsive">
									<c:if test="${!empty allowanceSetupList}">
										<table class="table table-striped table-bordered table-hover"
											id="table1">
											<thead>
												<tr style="background-color: #5cb85c; color: white">
													<th class="text-center">SL No.</th>
													<th class="text-center">Allowance Name</th>
													<th class="text-center">Unit</th>
													<th class="text-center">Amount</th>
													<th class="text-center">Maximum</th>
													<th class="text-center">Minimum</th>
													<th class="text-center">Status</th>
													<th class="text-center">Action</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${allowanceSetupList}"
													var="allowanceSetup" varStatus="slNo">
													<tr>
														<td class="text-center"><c:out value="${slNo.count}" /></td>
														<td class="text-center"><c:out
																value="${allowanceSetup.name}" /></td>
														<td class="text-center"><c:out
																value="${allowanceSetup.allowUnit}" /></td>
														<td class="text-center"><c:out
																value="${allowanceSetup.allowAmount}" /></td>
														<td class="text-center"><c:out
																value="${allowanceSetup.allowMaximum}" /></td>
														<td class="text-center"><c:out
																value="${allowanceSetup.allowMinimum}" /></td>
														<c:if test="${allowanceSetup.status==1}">
															<c:set var="status" value="Enable" />
														</c:if>
														<c:if test="${allowanceSetup.status==0}">
															<c:set var="status" value="Disable" />
														</c:if>
														<td class="text-center"><c:out value="${status}" /></td>

														<td class="text-center"><a
															class="btn btn-primary custom"
															href="editAllowanceSetup.htm?id=${allowanceSetup.id}">Edit</a>

															<!-- 															<a --> <!-- 															class="btn btn-danger custom" -->
															<%-- 															href="deleteAllowanceSetup.htm?id=${allowanceSetup.id}" --%>
															<!-- 															onclick="return confirm('Are you sure you want to delete?')">Delete</a> -->

														</td>
													</tr>
												</c:forEach>

											</tbody>
										</table>
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Table End-->

			</div>
		</div>
	</div>
	<!-- table ends -->
	<div class="clearfix"></div>

</body>
</html>