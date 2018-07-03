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

<style>
.add-on, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], input[type="file"], .uneditable-input {
    border-color: #4CAF50!important;
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
			var flag = false;
					
			if ($('#paymentMonthId').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				w2alert("Please Select Payment Month.");
				return flag;
			}
			
			if (!flag) {				
				$('#salaryProcessForm').submit();
			} 
		});
	    
	} );
	  
</script>

</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<h2 class="pull-left" style="color: #5BBC2E;">Salary Process Form</h2>	
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
							<form:form id="salaryProcessForm" cssClass="form-horizontal" method="POST" modelAttribute="salaryProcessForm"
								action="${pageContext.request.contextPath}/salaryProcess.htm"
								class="form-horizontal" role="form">
									
								<div class="form-group">
									<label class="col-lg-2 control-label">Payment Month:</label>
									<div class="col-lg-5">
									
									<form:select id="paymentMonthId" path="paymentMonthId" class="form-control">
										<form:option value="">Select One</form:option>		
										<c:if test="${!empty paymentMonthList}">
											<c:forEach items="${paymentMonthList}" var="paymentMonth">
											<form:option value="${paymentMonth.id}">${paymentMonth.payName}</form:option>
											</c:forEach>
										</c:if>								
									</form:select>
									
									</div>
								</div>

								
								<div class="form-group">									
									<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
										<button type="button" name="submit_btn" value="save"
											class="btn btn-sm btn-primary req-save-update-btn">Select</button>
									</div>
									<div class="col-lg-1 col-xs-4">
										<button type="reset" class="btn btn-sm btn-danger ">Reset</button>
									</div>
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
								<div class="pull-left">Salary Sheet for ${payMonth.paymentMonthName}-${payMonth.payYear}</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content">
								<div class="table-responsive">
									<c:if test="${!empty salaryList}">
										<table class="table table-striped table-bordered table-hover"
											id="table1">
											<thead>
												<tr style="background-color: #5cb85c; color: white">
													<th class="text-center"></th>
													<th class="text-center" colspan="10"> Employee Details</th>
												</tr>
												<tr style="background-color: #5cb85c; color: white">
													<!-- employee info -->
													<th class="text-center">Sl. No.</th>
													<th class="text-center">Employee Name</th>
													<th class="text-center">Employee ID</th>
													<th class="text-center">Designation</th>													
													<th class="text-center">Joining Date</th>
													<th class="text-center">Working Days</th>
													<th class="text-center">Absent Days</th>
													<th class="text-center">Present Days</th>
													<th class="text-center">Last Review Amount</th>
													<!-- earnings -->
													<th class="text-center">Basic Salary</th>
													<th class="text-center">House Rent</th>
													<th class="text-center">Medical</th>
													<th class="text-center">Conveyance</th>
													<th class="text-center">Others Salary</th>
													<th class="text-center">Gross Salary</th>
													<th class="text-center">consolidated Salary</th>
													<th class="text-center">Total Salary</th>
													<th class="text-center">Arrears</th>
													<th class="text-center">Overtime</th>
													<th class="text-center">In-charge</th>
													<th class="text-center">Others</th>
													<th class="text-center">Bonus</th>
													<th class="text-center">Total Earnings</th>
													<!-- deduction -->
													<th class="text-center">Absent Deduction</th>
													<th class="text-center">Fine</th>
													<th class="text-center">P.F</th>
													<th class="text-center">Income Tax</th>
													<th class="text-center">Stamp</th>
													<th class="text-center">Total Disburse</th>
													<th class="text-center">Installment</th>
													<th class="text-center">Due Advance</th>
													<th class="text-center">Total Deduction</th>
													<th class="text-center">Net Salary</th>
													<!-- payment mode -->
													<th class="text-center">Bank</th>
													<th class="text-center">Cash</th>
													<th class="text-center">Total</th>
													<!-- endings -->
													<th class="text-center">Bank A/C No</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${salaryList}" var="salary" varStatus="salRow">

													<tr>
														<!-- employee info -->
														<td class="text-center">${salRow.count}</td>
														<td class="text-center"><c:out value="${salary.empName}" /></td>
														<td class="text-center"><c:out value="${salary.empId}" /></td>
														<td class="text-center"><c:out value="${salary.designation}" /></td>
														<td class="text-center">${salary.joiningDate}</td>
														<td class="text-center"><c:out value="${salary.workingDays}" /></td>
														<td class="text-center"><c:out value="${salary.absenceDays}" /></td>
														<td class="text-center"><c:out value="${salary.presentDays}" /></td>
														<td class="text-center"><c:out value="${salary.lastReviewAmt}" /></td>
														<!-- earnings -->
														<td class="text-center">${salary.basicSalary}</td>
														<td class="text-center">${salary.houseRent}</td>
														<td class="text-center">${salary.medical}</td>
														<td class="text-center">${salary.conveyance}</td>
														<td class="text-center">${salary.otherSalary}</td>
														<td class="text-center">${salary.grossSalary}</td>
														<td class="text-center">${salary.consalitedSalary}</td>
														<td class="text-center">${salary.totalSalary}</td>
														<td class="text-center">${salary.arrear}</td>
														<td class="text-center">${salary.overtime}</td>
														<td class="text-center">${salary.incharge}</td>
														<td class="text-center">${salary.others}</td>
														<td class="text-center">${salary.bonus}</td>
														<td class="text-center">${salary.totalEarning}</td>
														<!-- deduction -->
														<td class="text-center">${salary.abcenceDeduction}</td>
														<td class="text-center">${salary.fineAmount}</td>
														<td class="text-center">${salary.pfAmount}</td>
														<td class="text-center">${salary.incomeTax}</td>
														<td class="text-center">${salary.stamp}</td>
														<td class="text-center">${salary.totalAdvance}</td>
														<td class="text-center">${salary.installment}</td>
														<td class="text-center">${salary.dueAmount}</td>
														<td class="text-center">${salary.totalDeduct}</td>
														<td class="text-center">${salary.netSalary}</td>
														<!-- payment mode -->
														<td class="text-center">${salary.bankPayment}</td>
														<td class="text-center">${salary.cashPayment}</td>
														<td class="text-center">${salary.totalPayment}</td>
														<!-- endings -->
														<td class="text-center">${salary.bankAccNo}</td>
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