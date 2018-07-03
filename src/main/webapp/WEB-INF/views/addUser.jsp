<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

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
	

<%--<!-- Stylesheets -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <!-- Font awesome icon -->
  <link rel="stylesheet" href="css/font-awesome.min.css"> 
  <!-- jQuery UI -->
  <link rel="stylesheet" href="css/jquery-ui.css"> 
  <!-- Calendar -->
  <link rel="stylesheet" href="css/fullcalendar.css">
  <!-- prettyPhoto -->
  <link rel="stylesheet" href="css/prettyPhoto.css">  
  <!-- Star rating -->
  <link rel="stylesheet" href="css/rateit.css">
  <!-- Date picker -->
  <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
  <!-- CLEditor -->
  <link rel="stylesheet" href="css/jquery.cleditor.css"> 
  <!-- Data tables -->
  <link rel="stylesheet" href="css/jquery.dataTables.css"> 
  <!-- Bootstrap toggle -->
  <link rel="stylesheet" href="css/jquery.onoff.css">
  <!-- Main stylesheet -->
  <link href="css/style.css" rel="stylesheet">
  <!-- Widgets stylesheet -->
  <link href="css/widgets.css" rel="stylesheet">   
  
  <script src="js/respond.min.js"></script>
   <script src="js/jquery-1.7.2.js"></script>
  <!--[if lt IE 9]>
  <script src="js/html5shiv.js"></script>
  <![endif]-->

  <!-- Favicon -->
  <link rel="shortcut icon" href="img/favicon/favicon.png">--%>
<!-- <style>
		#userForm label{
			display: inline-block;
			width: 100px;
			text-align: right;
		}
		#submit{
			padding-left: 100px;
		}
		#userForm div{
			margin-top: 1em;
		}
		textarea{
			vertical-align: top;
			height: 5em;
		}
			
		.error{
			display: none;
			margin-left: 10px;
		}		
		
		.error_show{
			color: red;
			margin-left: 10px;
		}
		
		input.invalid, textarea.invalid{
			border: 2px solid red;
		}
		
		input.valid, textarea.valid{
			border: 2px solid green;
		}
	</style>-->

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

<c:url var="findURL" value="/checkUser.htm" />
<script>
	var yourUrl = '<c:out value="${findURL}"/>';

	$(document).ready(function() {
		$('#table1').DataTable();
	});
	
	$(function() {
		$('#userForm').attr('autocomplete', 'off');
		//$('#uName').val();
		//$('#pass').val("");
	});

	$(document)
			.ready(
					function() {
						
						

						$('#submit').click(function() {
							return validateForm();
						});

						function validateForm() {

							var nameReg = /^[A-Za-z]+$/;
							var numberReg = /^[0-9]+$/;
							var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;

							var uName = $('#uName').val();
							var pass = $('#pass').val();
							var officeId = $('#officeId').val();
							var email = $('#email').val();
							var fName = $('#fName').val();
							var lName = $('#lName').val();
							var desc = $('#desc').val();

							var inputVal = new Array(uName, pass, officeId,
									email, fName, lName, desc);

							var inputMessage = new Array("User Name",
									"Password", "Office Id", "Email address",
									"First Name", "Last Name",
									"Description");

							$('.error').hide();

							if (inputVal[0] == "") {
								$('#uName').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[0] + '</span>');
								return false;
							}
							/*    else if(!nameReg.test(uName)){
							       $('#uName').after('<span class="error" style="color:red"> Letters only</span>');
							       return false;
							   } */

							if (inputVal[1] == "") {
								$('#pass').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[1] + '</span>');
								return false;
							} else if (pass.length < 6) {
								$('#pass').after(
										'<span class="error" style="color:red"> Please a valid '
												+ inputMessage[1]
												+ ' not less than 8 </span>');
								return false;
							}

							if (inputVal[2] == "") {
								$('#officeId').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[2] + '</span>');
								return false;
							}

							if (inputVal[3] == "") {
								$('#email').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[3] + '</span>');
								return false;
							} else if (!emailReg.test(email)) {
								$('#email')
										.after(
												'<span class="error" style="color:red"> Please enter a valid email address</span>');
								return false;
							}

							
							if (inputVal[4] == "") {
								$('#fName').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[5] + '</span>');
								return false;
							}
							if (inputVal[5] == "") {
								$('#lName').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[6] + '</span>');
								return false;
							}
							if (inputVal[6] == "") {
								$('#desc').after(
										'<span class="error" style="color:red"> Please enter '
												+ inputMessage[7] + '</span>');
								return false;
							}
							return true;
						}

					});

	function checkUser(username) {

		var s = "";
		var userVal = document.getElementById(username);
		//alert(document.getElementById(propertyId).value);
		$.ajax({
			type : "post",
			//url: "http://localhost:8080/SpringExamples/employee/add.htm",
			url : yourUrl,
			async : false,
			data : 'username=' + $("#" + username).val(),

			success : function(response) {				
				if (response == "false") {
					w2alert("Username already exist Choose another!!");
					userVal.value = "";
					userVal.focus();
				}
				s = $.parseJSON(response);
			},
			error : function() {
				// alert('Error while request..');
			}
		});

		return s;

	}

	jQuery.fn.filterByText = function(textbox, selectSingleMatch) {
		return this.each(function() {
			var select = this;
			var options = [];
			$(select).find('option').each(function() {
				options.push({
					value : $(this).val(),
					text : $(this).text()
				});
			});
			$(select).data('options', options);
			$(textbox).bind(
					'change keyup',
					function() {
						var options = $(select).empty().data('options');
						var search = $(this).val().trim();
						var regex = new RegExp(search, "gi");

						$.each(options, function(i) {
							var option = options[i];
							if (option.text.match(regex) !== null) {
								$(select).append(
										$('<option>').text(option.text).val(
												option.value));
							}
						});
						if (selectSingleMatch === true
								&& $(select).children().length === 1) {
							$(select).children().get(0).selected = true;
						}
					});
		});
	};

	$(function() {
		$('#empId').filterByText($('#textbox'), false);
		$("empId option").click(function() {
			w2alert(1);
		});
	});
</script>



</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<h2 class="pull-left" style="color: #5BBC2E;">User Registration</h2>


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
							<form:form id="userForm" cssClass="form-horizontal" method="POST"  autocomplete="off"
								action="${pageContext.request.contextPath}/saveUser.html"
								class="form-horizontal" role="form">


								<form:input type="hidden" path="id" value="${user.id}"
									readonly="true" class="form-control" />


								<div class="form-group">
									<label class="col-lg-2 control-label"></label>
									<div class="col-lg-5">

										<input id="textbox" type="text" class="form-control"
											placeholder="Search Employee" />
									</div>
								</div>


								<div class="form-group">

									<label class="col-lg-2 control-label">Select Employee</label>

									<div class="col-lg-5">
										<c:if test="${!empty employees}">

											<form:select class="form-control" path="empId" id="empId">
												<c:if test="${!empty user}">
													<form:option value="${user.employee.id}"
														label="${user.employee.fullname}"></form:option>
												</c:if>
												<form:option value="" label="Select"></form:option>
												<c:forEach items="${employees}" var="employee">
													<form:option value="${employee.id}"
														label="${employee.fullname}-(${employee.employeeId})"></form:option>
												</c:forEach>
											</form:select>

										</c:if>
									</div>
								</div>


								<div class="form-group">
									<label class="col-lg-2 control-label">User Name:</label>
									<div class="col-lg-5">
										<form:input id="uName" path="uName" value=""
											class="form-control" onblur="return checkUser('uName');" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Password:</label>
									<div class="col-lg-5">
										<form:password id="pass" path="pass" value=""
											class="form-control" />
									</div>
								</div>





								<div class="form-group">
									<label class="col-lg-2 control-label">Email:</label>
									<div class="col-lg-5">
										<%-- <form:input path="email" id="email" value="${user.email}" class="form-control"/> --%>
										<input type="email" name="email" id="email"
											value="${user.email}" class="form-control">
									</div>
								</div>


								<%-- <div class="form-group">
                                  <label class="col-lg-2 control-label">Office Mobile:</label>
                                  <div class="col-lg-5">
                                   <form:input path="mobile" id="mobile" value="${user.mobile}" class="form-control"/>
                                  </div>
                                </div>  --%>

								<div class="form-group">
									<label class="col-lg-2 control-label">First Name:</label>
									<div class="col-lg-5">
										<form:input id="fName" path="fName" value="${user.fName}"
											class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Last Name:</label>
									<div class="col-lg-5">
										<form:input id="lName" path="lName" value="${user.lName}"
											class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Designation:</label>
									<div class="col-lg-5">
										<form:input id="desc" path="desc" value="${user.desc}"
											class="form-control" />
									</div>
								</div>


								<div class="form-group">

									<label class="col-lg-2 control-label">User Role</label>

									<div class="col-lg-5">
										<c:if test="${!empty userRoles}">

											<form:select class="form-control" path="auth" id="auth"
												name="auth">
												<form:option value="" label="Select"></form:option>
												<c:forEach items="${userRoles}" var="userRole">
													<form:option value="${userRole}"></form:option>
												</c:forEach>
											</form:select>

										</c:if>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Status:</label>
									<div class="col-lg-5">
										<form:select class="form-control" id="status" path="status">
											<c:if test="${!empty user}">
												<form:option value="${user.status}"></form:option>
											</c:if>
											<form:option value="">Select One</form:option>
											<form:option value="1">Active</form:option>
											<form:option value="2">Inactive</form:option>
										</form:select>
									</div>
								</div>


								<div class="form-group" hidden="true">
									<label class="col-lg-2 control-label">Add Date:</label>
									<div class="col-lg-5">
										<form:input id="insertDate" path="insertDate"
											value="${user.insertDate}" class="form-control" />
									</div>
								</div>



								<div class="form-group">
									<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="submit">



										<input type="submit" value="Submit"
											class="btn btn-sm btn-success" />

									</div>
									<div class="col-lg-1 col-xs-4">
										<button type="reset" class="btn btn-sm btn-primary">Reset</button>
									</div>
								</div>
							</form:form>

						</div>

					</div>
					<div class="widget-foot">
						<!-- Footer goes here -->
					</div>
				</div>





				<!-- Table -->

				<div class="row">

					<div class="col-md-12">

						<div class="widget">

							<div class="widget-head">
								<div class="pull-left">User Details</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content">

								<div class="table-responsive">

									<c:if test="${!empty users}">
										<table class="table table-striped table-bordered table-hover"
											id="table1">
											<thead>
												<tr style="background-color: #5cb85c; color: white">
													<th class="text-center">User Name</th>
													<th class="text-center">First Name</th>
													<th class="text-center">Last Name</th>
													<th class="text-center">Designation</th>													
													<th class="text-center">E-mail</th>
													<th class="text-center">Add Date</th>
													<th class="text-center">Action</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${users}" var="user">

													<tr>
														<td class="text-center"><c:out value="${user.uName}" /></td>
														<td class="text-center"><c:out value="${user.fName}" /></td>
														<td class="text-center"><c:out value="${user.lName}" /></td>
														<td class="text-center"><c:out value="${user.desc}" /></td>
														<%-- <td><c:out value="${user.mobile}" /></td> --%>
														<td class="text-center"><c:out value="${user.email}" /></td>
														<td class="text-center">
														<fmt:formatDate pattern = "dd-MM-yyyy HH:mm:ss a" value = "${user.insertDate}" /></td>

														<td class="text-center">
														<c:if test="${user.uName ne 'admin'}">
															<a class="btn btn-primary custom" style="color:white;"
																href="editUser.htm?id=${user.id}">Edit</a> 
														</c:if>
															<%-- <a class="btn btn-danger custom" href="deleteUser.html?id=${user.id}" 
															onclick="return confirm('Are you sure you want to delete?')">Delete</a> --%>
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
				<!-- table ends -->

			</div>

		</div>

	</div>
	<!-- Matter ends -->


	<!-- Mainbar ends -->
	<div class="clearfix"></div>

	<!-- Content ends -->

	<!-- Footer starts -->
	<!-- <footer>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
            Copyright info
            <p class="copy">Copyright &copy; 2012 | <a href="#">Your Site</a> </p>
      </div>
    </div>
  </div>
</footer>  -->

	<!-- Footer ends -->

	<!-- Scroll to top -->
	<%-- 
<span class="totop"><a href="#"><i class="fa fa-chevron-up"></i></a></span> 

<!-- JS -->
<script src="js/jquery.js"></script> <!-- jQuery -->
<script src="js/bootstrap.min.js"></script> <!-- Bootstrap -->
<script src="js/jquery-ui.min.js"></script> <!-- jQuery UI -->
<script src="js/fullcalendar.min.js"></script> <!-- Full Google Calendar - Calendar -->
<script src="js/jquery.rateit.min.js"></script> <!-- RateIt - Star rating -->
<script src="js/jquery.prettyPhoto.js"></script> <!-- prettyPhoto -->
<script src="js/jquery.slimscroll.min.js"></script> <!-- jQuery Slim Scroll -->
<script src="js/jquery.dataTables.min.js"></script> <!-- Data tables -->

<!-- jQuery Flot -->
<script src="js/excanvas.min.js"></script>
<script src="js/jquery.flot.js"></script>
<script src="js/jquery.flot.resize.js"></script>
<script src="js/jquery.flot.pie.js"></script>
<script src="js/jquery.flot.stack.js"></script>

<!-- jQuery Notification - Noty -->
<script src="js/jquery.noty.js"></script> <!-- jQuery Notify -->
<script src="js/themes/default.js"></script> <!-- jQuery Notify -->
<script src="js/layouts/bottom.js"></script> <!-- jQuery Notify -->
<script src="js/layouts/topRight.js"></script> <!-- jQuery Notify -->
<script src="js/layouts/top.js"></script> <!-- jQuery Notify -->
<!-- jQuery Notification ends -->

<script src="js/sparklines.js"></script> <!-- Sparklines -->
<script src="js/jquery.cleditor.min.js"></script> <!-- CLEditor -->
<script src="js/bootstrap-datetimepicker.min.js"></script> <!-- Date picker -->
<script src="js/jquery.onoff.min.js"></script> <!-- Bootstrap Toggle -->
<script src="js/filter.js"></script> <!-- Filter for support page -->
<script src="js/custom.js"></script> <!-- Custom codes -->
<script src="js/charts.js"></script> <!-- Charts & Graphs -->--%>

</body>
</html>