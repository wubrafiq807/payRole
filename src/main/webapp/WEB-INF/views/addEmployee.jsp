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
<script
	src="${pageContext.request.contextPath}/resource/js/additional-methods.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/js/resizeImage.js"></script>
<style type="text/css">
<
c:url var ="findURL " value ="/checkDepartment.htm " /> <style>.add-on,
	textarea, input[type="text"], input[type="password"], input[type="datetime"],
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
	
</script>

</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<c:choose>
			<c:when test="${edit}">
				<h2 class="pull-left" style="color: #5BBC2E;">Edit Employee</h2>
			</c:when>
			<c:otherwise>
				<h2 class="pull-left" style="color: #5BBC2E;">Add New Employee</h2>
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

							<br />

							<!-- Form starts.  -->
							<form:form id="employeeForm" cssClass="form-horizontal"
								method="POST" modelAttribute="employeeForm" name="reg"
								action="${pageContext.request.contextPath}/saveEmployee.htm"
								class="form-horizontal" role="form"
								enctype="multipart/form-data">
								<form:hidden id="id" path="id" value="${employee.id}"
									class="form-control" />


								<c:if test="${not  edit }">
									<div class="form-group">
										<label class="col-lg-2 control-label">Employee Id <span
											style="color: red">*</span></label>
										<div class="col-lg-5">
											<form:input id="employeeId" path="employeeId"
												value="${employee.employeeId}" class="form-control" />

											<span class="employeeId_valid_err" style="color: red"></span>
											<input type="hidden" value="0" id="employeeIdAlready">
										</div>
									</div>

								</c:if>

								<div class="form-group">
									<label class="col-lg-2 control-label">Employee Name <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="fullname" path="fullname"
											value="${employee.fullname}" class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Father Name <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="fatherName" path="fatherName"
											value="${employee.fatherName}" class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Mother Name <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="motherName" path="motherName"
											value="${employee.motherName}" class="form-control" />
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Date of Birth <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<input type="eu-date" value="${employee.dateOfBirth}" class="form-control" id="dateOfBirth" name="dateOfBirth">
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Joining Date <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<input type="eu-date" value="${employee.joiningDate}" class="form-control" id="joiningDate" name="joiningDate">										
									</div>
								</div>
								
								

								<div class="form-group">
									<label class="col-lg-2 control-label">Email Address <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="emailAddress" path="emailAddress"
											value="${employee.emailAddress}" class="form-control" />
										<span class="email_valid_err" style="color: red"></span> <input
											type="hidden" value="0" id="emailAlready"> <input
											type="hidden" id="email_update"
											value="${employee.emailAddress}" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Personal Phone</label>
									<div class="col-lg-5">
										<form:input id="personalPhone" path="personalPhone"
											value="${employee.personalPhone}" class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Work Phone <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="workPhone" path="workPhone"
											value="${employee.workPhone}" class="form-control" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Present Address <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<textarea id="presentAddress" name="presentAddress"
											 class="form-control" >${employee.presentAddress}</textarea>
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Permanent Address
										<span style="color: red">*</span>
									</label>
									<div class="col-lg-5">
										<textarea id="permanentAddress" name="permanentAddress"
											 class="form-control" >${employee.permanentAddress}</textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Bank Acc. No. <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="bankAccNo" path="bankAccNo"
											value="${employee.bankAccNo}" class="form-control" />
									</div>
								</div>


								<c:choose>
									<c:when test="${edit}">
										<div class="form-group">
											<label class="col-lg-2 control-label">Gender <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<c:choose>
													<c:when test="${employee.gender=='Male'}">
														<form:radiobutton path="gender" value="Male"
															checked="checked" />
										Male
										<form:radiobutton path="gender" value="Female" />
										Female
											
										</c:when>
													<c:otherwise>
														<form:radiobutton path="gender" value="Male" />
										Male
										<form:radiobutton path="gender" value="Female"
															checked="checked" />
										Female
											
										</c:otherwise>
												</c:choose>

											</div>
										</div>

									</c:when>
									<c:otherwise>

										<div class="form-group">
											<label class="col-lg-2 control-label">Gender <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<form:radiobutton path="gender" value="Male" />
												Male
												<form:radiobutton path="gender" value="Female" />
												Female
											</div>
										</div>
									</c:otherwise>
								</c:choose>

								<c:choose>
									<c:when test="${edit}">
										<div class="form-group">
											<label class="col-lg-2 control-label">Merital Status
												<span style="color: red">*</span>
											</label>
											<div class="col-lg-5 ">
												<c:choose>
													<c:when test="${employee.meritalStatus=='Single'}">
														<form:radiobutton path="meritalStatus" value="Single"
															checked="checked" />
										Single
										<form:radiobutton path="meritalStatus" value="Married" />
										Married
											
										</c:when>
													<c:otherwise>
														<form:radiobutton path="meritalStatus" value="Single" />
										Single
										<form:radiobutton path="meritalStatus" value="Married"
															checked="checked" />
										Married
											
										</c:otherwise>
												</c:choose>

											</div>
										</div>

									</c:when>
									<c:otherwise>

										<div class="form-group">
											<label class="col-lg-2 control-label">Marital Status
												<span style="color: red">*</span>
											</label>
											<div class="col-lg-5 ">
												<form:radiobutton path="meritalStatus" value="Single"
													checked="checked" />
												Single
												<form:radiobutton path="meritalStatus" value="Married" />
												Married
											</div>
										</div>
									</c:otherwise>
								</c:choose>

								<div class="form-group">
									<label class="col-lg-2 control-label">Blood Group <span
										style="color: red">*</span></label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="bloodGroupId">
											<form:option value="">Select</form:option>
											<c:forEach items="${bloodGroupList}" var="bloodGroup">

												<c:if test="${employee.bloodGroup.id eq bloodGroup.id}">
													<form:option value="${bloodGroup.id}" selected="selected">${bloodGroup.name}</form:option>
												</c:if>

												<c:if test="${employee.bloodGroup.id ne bloodGroup.id}">
													<form:option value="${bloodGroup.id}">${bloodGroup.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">District </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="districtId">
											<form:option value="">Select</form:option>
											<c:forEach items="${districtListTotal}" var="district">

												<c:if test="${employee.district.id eq district.id}">
													<form:option value="${district.id}" selected="selected">${ district.name}</form:option>
												</c:if>

												<c:if test="${employee.district.id ne district.id}">
													<form:option value="${district.id}">${district.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Department </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="departmentId">
											<form:option value="">Select</form:option>
											<c:forEach items="${departmentList}" var="department">

												<c:if test="${employee.department.id eq department.id}">
													<form:option value="${department.id}" selected="selected">${department.name}</form:option>
												</c:if>

												<c:if test="${employee.department.id ne department.id}">
													<form:option value="${department.id}">${department.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Designation </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="designationId">
											<form:option value="">Select</form:option>
											<c:forEach items="${designationList}" var="designation">

												<c:if test="${employee.designation.id eq designation.id}">
													<form:option value="${designation.id}" selected="selected">${designation.name}</form:option>
												</c:if>

												<c:if test="${employee.designation.id ne designation.id}">
													<form:option value="${designation.id}">${designation.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Religion </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="religionId">
											<form:option value="">Select</form:option>
											<c:forEach items="${religionList}" var="religion">

												<c:if test="${employee.religion.id eq religion.id}">
													<form:option value="${religion.id}" selected="selected">${religion.religionName}</form:option>
												</c:if>

												<c:if test="${employee.religion.id ne religion.id}">
													<form:option value="${religion.id}">${religion.religionName}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">PaySite </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="paySiteId">
											<form:option value="">Select</form:option>
											<c:forEach items="${paySiteList}" var="paySite">

												<c:if test="${employee.paySite.id eq paySite.id}">
													<form:option value="${paySite.id}" selected="selected">${paySite.name}</form:option>
												</c:if>

												<c:if test="${employee.paySite.id ne paySite.id}">
													<form:option value="${paySite.id}">${paySite.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Work Location </label>
									<div class="col-lg-5 ">
										<form:select class="form-control" path="workLocationId">
											<form:option value="">Select</form:option>
											<c:forEach items="${workLocationList}" var="workLocation">

												<c:if test="${employee.workLocation.id eq workLocation.id}">
													<form:option value="${workLocation.id}" selected="selected">${workLocation.name}</form:option>
												</c:if>

												<c:if test="${employee.workLocation.id ne workLocation.id}">
													<form:option value="${workLocation.id}">${workLocation.name}</form:option>
												</c:if>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<c:choose>
									<c:when test="${edit}">
										<div class="form-group">
											<label class="col-lg-2 control-label">Status <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<c:choose>
													<c:when test="${employee.status == 1}">
														<form:radiobutton path="status" value="1"  checked="checked" /> Active
														<input type="radio" name="status" value="0"> Inactive									
													</c:when>
													<c:otherwise>
														<form:radiobutton path="status" value="1"/> Active
														<%-- <form:radiobutton path="status" value="0" /> Inactive --%>
														<input type="radio" name="status" value="0" checked="checked" > Inactive
													</c:otherwise>
												</c:choose>

											</div>
										</div>

									</c:when>
									<c:otherwise>

										<div class="form-group">
											<label class="col-lg-2 control-label">Status <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<form:radiobutton path="status" value="1" checked="checked" />
												Active
												<input type="radio" name="status" value="0" > Inactive
											</div>
										</div>
									</c:otherwise>
								</c:choose>



								<div class="form-group">
									<label class="col-lg-2 control-label">Employee Image</label> <input
										type="hidden" value="" id="imageUplodable" name="companyLogo">
									<div class="col-lg-5">
										<input id="companyLogo" type="file" name="file"
											value="${employee.logo}" class="image-upload form-control" />
										<div class="images">
											<c:if test="${edit}">
												<img width="200" height="150"
													src="${pageContext.request.contextPath}/photo/${employee.employeeId}">
										</div>
								</c:if>
						</div>


					</div>






					<div class="form-group">
						<c:choose>
							<c:when test="${edit}">
								<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
									<button type="submit" name="submit_btn" value="update"
										class="btn btn-sm btn-primary btn-success req-save-update-btn">
										Update</button>
								</div>
								<div class="col-lg-1 col-xs-4">
									<a class="btn btn-sm btn-danger"
										href="${pageContext.request.contextPath}/newDeptForm.htm">Cancel</a>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
									<button type="submit" name="submit_btn" value="save"
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


		<!-- Table End-->

	</div>
	</div>
	</div>
	<!-- table ends -->
	<div class="clearfix"></div>

</body>


<script>
	$(document).ready(function() {
		$(".image-upload").ImageResize({
			maxWidth : 200,
			onImageResized : function(imageData) {
				$(".images").html($("<img/>", {
					src : imageData
				}));
				//console.log(imageData)
				$("#imageUplodable").val(imageData);
			}
		});
	});
	$(function() {
		$('#emailAddress')
				.on(
						"change",
						function() {
							if ($('#email_update').val() != $('#emailAddress')
									.val()) {
								jQuery
										.ajax({
											type : "POST",
											url : "${pageContext.request.contextPath}/checkUnicKEmailForEmployee",
											dataType : 'json',
											data : {
												emailAddress : $(
														'#emailAddress').val()
											},
											success : function(res) {
												if (res.companyInfo != null) {
													$('#emailAlready').val(1);
													$(".email_valid_err")
															.text(
																	"Email Address Already in Use.");
												} else {
													$(".email_valid_err").text(
															"");
													$('#emailAlready').val(0);
												}
											}
										});
							} else {
								$(".email_valid_err").text("");
								$('#emailAlready').val(0);
							}

						});
		$('#employeeId')
				.on(
						"change",
						function() {
							jQuery
									.ajax({
										type : "POST",
										url : "${pageContext.request.contextPath}/checkUnickEmployeeId",
										dataType : 'json',
										data : {
											employeeId : $('#employeeId').val()
										},
										success : function(res) {
											if (res.companyInfo != null) {
												$('#employeeIdAlready').val(1);
												$(".employeeId_valid_err")
														.text(
																"Employee Id Already in Use.");
											} else {
												$(".employeeId_valid_err")
														.text("");
												$('#employeeIdAlready').val(0);
											}
										}
									});
						});

		$('.req-save-update-btn')
				.on(
						"click",
						function(e) {

							if ($('#emailAlready').val() == 1) {
								e.preventDefault();
								$(".email_valid_err").text(
										"Email Address Already in Use.");
							} else {
								$(".email_valid_err").text("");
							}

							if ($('#employeeIdAlready').val() == 1) {
								e.preventDefault();
								$(".employeeId_valid_err")
										.text(
												"Employee ID  Already in Use Please try another one.");
							} else {
								$(".employeeId_valid_err").text("");
							}
						});
		// Initialize form validation on the registration form.
		// It has the name attribute "registration"
		$("form[name='reg']").validate({
			// Specify validation rules
			rules : {
				// The key name on the left side is the name attribute
				// of an input field. Validation rules are defined
				// on the right side
				employeeId : "required",
				fullname : "required",
				fatherName : "required",
				motherName : "required",
				workPhone : "required",
				presentAddress : "required",
				permanentAddress : "required",
				gender : "required",
				meritalStatus : "required",				
				phone : "required",
				status : "required",
				districtId : "required",
				departmentId : "required",
				designationId : "required",
				paySiteId : "required",
				workLocationId : "required",
				religionId : "required",
				bloodGroupId : "required",
				bankAccNo : "required",
				joiningDate : "required",
				dateOfBirth : "required",
				emailAddress : {
					required : true,
					email : true
				}
				

			},

			// Make sure the form is submitted to the destination defined
			// in the "action" attribute of the form when valid
			submitHandler : function(form) {
				form.submit();
			}
		});
	});
</script>

<script>
	$(function() {
		var month = (new Date()).getMonth() + 1;
		var year = (new Date()).getFullYear();

		$('input[type=eu-date]').w2field('date', {
			format : 'yyyy-mm-dd'
		});
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
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.js' />"></script>

<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.js' />"></script>
</html>