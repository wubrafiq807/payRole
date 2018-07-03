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

<link href="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.css' />"
	rel="stylesheet"></link>


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

<c:url var="findURL" value="/checkHoliday.htm" />

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


	$(document).ready(function() {
		$('#table1').DataTable();

		$('.req-save-update-btn').click(function() {
			var flag = false;var deptCheckResp = false;
 
			if ($('#employeeId').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				w2alert("Please Select Employee.");
				return false;
			}

			if ($('#disverseAmount').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				w2alert("Please Input Disverse Amount.");
				return false;
			}
			
			if ($('#disverseInterest').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				w2alert("Please Input disverse Interest %.");
				return false;
			}
			if ($('#dirverseDate').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				w2alert("Please Input Date %.");
				return false;
			}
						

			if (!flag) {				
				deptCheckResp = checkDueDisverse();
			} 
			if (deptCheckResp) {
			
				$(".req-save-update-btn").prop('disabled', true);
				swal({
					  title: "Are you sure?",
					  text: "Once action perform,this  Employee will purchase disburse amount.The Employee will not purchase again until dues is not paid.!",
					  icon: "warning",
					  buttons: true,
					  dangerMode: true,
					})
					.then((willDelete) => {
					  if (willDelete) {
					    swal("Poof! Your form has been submitted!", {
					      icon: "success",
					    });
					    $('#disverseForm').submit();
					  } else {
					    swal("Form has been canceled by you.!");
					    $(".req-save-update-btn").prop('disabled', false);
					  }
					});
				
			} else {
				$(".req-save-update-btn").prop('disabled', false);
				//$('#name').value = "";
				$('#employeeId').focus();
				w2alert("There is some due disverse for this employee.!!");
				return flag;
			}
		});

		

	});
	
	function checkDueDisverse() {
		var res = false;
		
		var deptName = $('#name');
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/checkDueDisverse.htm",
			async : false,
			data : 'employeeId=' + $('#employeeId').val(),
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

		return res;
	}
</script>

</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<h2 class="pull-left" style="color: #5BBC2E;">Disburse Form</h2>
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

							<br />

							<!-- Form starts.  -->
							<form:form id="disverseForm" method="POST" modelAttribute="disverseForm" action="${pageContext.request.contextPath}/saveDisverseOrUpdate.htm" class="form-horizontal" >

								<div class="form-group">
									<label class="col-lg-2 control-label">Employee :</label>
									<div class="col-lg-5">
										<form:select id="employeeId" path="employeeId"
											class="form-control">
											<form:option value="">Select One</form:option>
											<c:if test="${!empty employeeList}">
												<c:forEach items="${employeeList}" var="employee">
													
												<c:if test="${employee.id==disverse.employee.id}">
													<form:option selected="selected" value="${employee.id}">${employee.fullname} - (${employee.employeeId})</form:option>
												</c:if>
												<c:if test="${employee.id ne disverse.employee.id}">
													<form:option  value="${employee.id}">${employee.fullname} - (${employee.employeeId})</form:option>
												</c:if>
												</c:forEach>
											</c:if>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Disburse Amount:</label>
									<div class="col-lg-5">
										<input type="number" class="form-control" name="disverseAmount"
											value="${disverse.disverseAmount}" id="disverseAmount">
									</div>
								</div>
								
								 <div class="form-group">
									<label class="col-lg-2 control-label">Interest (%):</label>
									<div class="col-lg-5">
										<input type="number" class="form-control" name="disverseInterest"
											value="${disverse.disverseInterest}" id="disverseInterest">
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Date<span
										class="red">*</span>:
									</label> 
									
									<div class="col-lg-5">										
										<input type="eu-date" class="form-control" name="dirverseDate"
														id="dirverseDate" value="${disverse.dirverseDate}">
									</div>
								</div>
								

								<div class="form-group">
									<label class="col-lg-2 control-label">
										Remarks:</label>
									<div class="col-lg-5">
										<input type="text" class="form-control"
											value="${disverse.remarks}" name="remarks"
											id="remarks">
									</div>
								</div>

								<div class="form-group">
									<c:choose>
										<c:when test="${edit}">
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
											<input name="id" type="hidden" value="${disverse.id}">
												<button type="submit" name="submit_btn" value="update"
													class="btn btn-sm btn-primary btn-success req-save-update-btn">
													Update</button>
											</div>											
										</c:when>
										<c:otherwise>
											

											<div class="col-lg-offset-2 col-xs-2" id="">
												<button type="button" name="submit_btn" value="save"
													class="btn btn-sm btn-success req-save-update-btn">Save
													and Go Back To List</button>
											</div>

											<div class="col-xs-4">
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

				<!-- Table End-->

			</div>
		</div>
	</div>
	<!-- table ends -->
	<div class="clearfix"></div>
<script>
$(function() {
	var month = (new Date()).getMonth() + 1;
	var year = (new Date()).getFullYear();
	
	$('input[type=eu-date]').w2field('date',  { format: 'yyyy-mm-dd' });
	
	// US Format
		$('input[type=eu-date1]').w2field('date', {
			format : 'yyyy-mm-dd',
			end : $('input[type=eu-date2]')
		});
		$('input[type=eu-date2]').w2field('date', {
			format : 'yyyy-mm-dd',
			start : $('input[type=eu-date1]')
		});
	});
	
</script>

<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.js' />"></script>
</body>
</html>