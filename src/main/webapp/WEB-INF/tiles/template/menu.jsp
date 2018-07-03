<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<title></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<!-- Stylesheets -->
<link href="${pageContext.request.contextPath}/${pageContext.request.contextPath}/resource/css/demo.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/${pageContext.request.contextPath}/resource/css/bootstrap.min.css" rel="stylesheet">
<!-- Font awesome icon -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/${pageContext.request.contextPath}/resource/css/font-awesome.min.css">
<!-- bdjobs css -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/bdjobs-style.css">
<!-- jQuery UI -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
<!-- Calendar -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/fullcalendar.css">
<!-- prettyPhoto -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/prettyPhoto.css">
<!-- Star rating -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/rateit.css">
<!-- Date picker -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/bootstrap-datetimepicker.min.css">
<!-- CLEditor -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery.cleditor.css">
<!-- Data tables -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.css">
<!-- Bootstrap toggle -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery.onoff.css">
<!-- Main stylesheet -->
<link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet">

<link href="${pageContext.request.contextPath}/resource/css/custom.css" rel="stylesheet">
<!-- Widgets stylesheet -->
<link href="${pageContext.request.contextPath}/resource/css/widgets.css" rel="stylesheet">
<!-- Widgets stylesheet -->
<link href="${pageContext.request.contextPath}/resource/css/bootstrap-table.css" rel="stylesheet">
<!-- Widgets stylesheet -->
<link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.css" rel="stylesheet">
<!-- Widgets stylesheet -->
<link href="${pageContext.request.contextPath}/resource/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/css/jquery.steps.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/css/main.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/css/normalize.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resource/js/respond.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/jquery-1.7.2.js"></script>
<!--[if lt IE 9]>
  <script src="${pageContext.request.contextPath}/resource/js/html5shiv.js"></script>
  <![endif]-->
<link
	href="${pageContext.request.contextPath}/https://cdn.jsdelivr.net/bootstrap.timepicker/0.2.6/css/bootstrap-timepicker.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/https://cdn.jsdelivr.net/bootstrap.timepicker/0.2.6/css/bootstrap-timepicker.css"
	rel="stylesheet">



<!-- Favicon -->
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/img/favicon.ico">

<!--for new Dashboard-->
<link href="${pageContext.request.contextPath}/resource/css/style.min.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<style>
#clock {
	font-family: 'Open Sans', sans-serif;
	font-size: 1em;
	font-weight: bold;
	background-color: #0CF;
	color: #fff;
	padding: 7px 50px;
	position: relative;
	top: 100px;
	left: 100px;
}
</style>
	<script
		src="${pageContext.request.contextPath}/resource/js/sweetalert.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/js/additional-methods.min.js"></script>
<script>
	$(document).ready(function() {
		DisplayCurrentTime();
	});

	function DisplayCurrentTime() {
		var dt = new Date();
		var refresh = 1000; //Refresh rate 1000 milli sec means 1 sec
		var cDate = (dt.getMonth() + 1) + "/" + dt.getDate() + "/"
				+ dt.getFullYear();
		document.getElementById('cTime').innerHTML = cDate + "  "
				+ dt.toLocaleTimeString();
		window.setTimeout('DisplayCurrentTime()', refresh);
	}
</script>
</head>



<body>

	<input type="hidden" id="cTime">
	<div class="content">

		<%
			// Set refresh, autoload time as 5 seconds
			response.setIntHeader("Refresh", 5);
		%>

		<!-- Sidebar -->
		<div class="sidebar">
			<div class="sidebar-dropdown">
				<a href="#">Navigation</a>
			</div>

			<!--- Sidebar navigation -->
			<!-- If the main navigation has sub navigation, then add the class "has_sub" to "li" of main navigation. -->

			<ul id="nav">
				<!-- Main menu with font awesome icon -->

				<li class="open"><a href="${pageContext.request.contextPath}/"><i
						class="fa fa-home"></i>Dashboard</a></li>			
				<sec:authorize
					access="hasRole('ROLE_ADMIN')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Super Admin<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/monthlySalRpt.html">Monthly Salary Report</a></li>
							<li><a href="${pageContext.request.contextPath}/yearlySalRpt.html">Yearly Salary Report</a></li>
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Company<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newCompanyForm">Add New Company</a></li>
							<li><a href="${pageContext.request.contextPath}/companyList">Company List</a></li>
							
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>User Manage<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/addUser.htm">Add New User</a></li>
							
						</ul></li>
				</sec:authorize>
				
				<c:if test="${!empty company}">					
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Settings<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newDistrictForm.htm">District</a></li>
							<li><a href="${pageContext.request.contextPath}/getBloodGroup">Blood Group</a></li>
							<li><a href="${pageContext.request.contextPath}/newReligionForm.htm">Religion</a></li>
							<li><a href="${pageContext.request.contextPath}/newBranchForm.htm">Branch</a></li>
							<li><a href="${pageContext.request.contextPath}/newDesignationForm.htm">Designation</a></li>
							<li><a href="${pageContext.request.contextPath}/newDeptForm.htm">Department</a></li>
							<li><a href="${pageContext.request.contextPath}/newWeekendForm.htm">Weekend</a></li>
							<li><a href="${pageContext.request.contextPath}/newHolidayForm.htm">Holiday</a></li>
							<li><a href="${pageContext.request.contextPath}/newSettingsForm.htm">System Settings</a></li>
							<li><a href="${pageContext.request.contextPath}/newPaymentMonthForm.htm">Payment Month</a></li>
							<li><a href="${pageContext.request.contextPath}/newPaySiteForm.htm">PaySite</a></li>
							<li><a href="${pageContext.request.contextPath}/newWorkLocationForm.htm">Work Location</a></li>
							
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Employees<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newEmployeeForm.htm">Add New Employee</a></li>
							<li><a href="${pageContext.request.contextPath}/employeeList.htm">Employee List</a></li>							
							<li><a href="${pageContext.request.contextPath}/newSalaryForm.htm">Assign Salary</a></li>
							<li><a href="${pageContext.request.contextPath}/salaryList.htm">Salary List</a></li>
							<li><a href="${pageContext.request.contextPath}/newDisverseForm.htm">Add Disburse</a></li>
							<li><a href="${pageContext.request.contextPath}/getAllDisburse.htm">Disburse List</a></li>
<!-- 							<li><a href="${pageContext.request.contextPath}/getAIForm">Upload Employee</a></li> -->
<!-- 							<li><a href="${pageContext.request.contextPath}/aiList">Upload Increment</a></li> -->
						</ul></li>
				</sec:authorize>	
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Allowance<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newAllowanceSetupForm.htm">Allowance Setup</a></li>
							<!-- <li><a href="${pageContext.request.contextPath}/getAIForm">Allowance Distribute</a></li>
							<li><a href="#">Allowance Log</a></li> --> 							
							<li><a href="${pageContext.request.contextPath}/newAddAllowanceForm.htm">Additional Allowance</a></li>
							<li><a href="${pageContext.request.contextPath}/getAIForm">Upload Additional Allowance</a></li>
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Deduction<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newDeductionSetupForm.htm">Deduction Setup</a></li>
							<li><a href="${pageContext.request.contextPath}/newAbsenceDeductionorm.htm">Absence Deduction Form</a></li> 
							<li><a href="${pageContext.request.contextPath}/absenceDeductionList.htm">Absence Deduction List</a></li>
							<li><a href="${pageContext.request.contextPath}/newAdvanceForm.htm"> Advance Deduction Form</a></li>	
							<li><a href="${pageContext.request.contextPath}/newAdvanceListForm.htm"> Advance Deduction List</a></li>						
							<li><a href="${pageContext.request.contextPath}/newAddDeductionForm.htm">Additional Deduction</a></li>
							<li><a href="#">Upload Additional Deduction</a></li>
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Salary<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/empSalHistProcessForm.htm">Salary Process</a></li>
							<li><a href="${pageContext.request.contextPath}/">Send Mail</a></li>
							<li><a href="${pageContext.request.contextPath}/newAdditionalSalaryForm.htm">Additional Salary</a></li>
							<li><a href="${pageContext.request.contextPath}/newAdditionalSalaryListForm.htm">Additional Salary List</a></li>
							<li><a href="${pageContext.request.contextPath}/newCashPaymentEmployeeForm.htm">Cash Payment Form</a></li>
							<li><a href="${pageContext.request.contextPath}/newCashPaymentEmployeeListForm.htm">Cash Payment List</a></li>
							<li><a href="${pageContext.request.contextPath}/newFixedIncomeTaxForm.htm">Fixed Income Tax</a></li>
							<li><a href="${pageContext.request.contextPath}/fixedIncomeTaxList.htm">Fixed Income Tax List</a></li>
						</ul></li>
				</sec:authorize>		
				
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Salary Reports<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/getAIForm">Pay Slip</a></li>
							<li><a href="${pageContext.request.contextPath}/aiList">Bank Sheet</a></li>
							<li><a href="${pageContext.request.contextPath}/salaryReportForm.htm">Salary Sheet</a></li>
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Provident Fund<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/newAddPFProfitForm.htm">PF Profit</a></li>
<!-- 							<li><a href="${pageContext.request.contextPath}/aiList">Check Balance</a></li> -->
<!-- 							<li><a href="${pageContext.request.contextPath}/getAIForm">PF Balance Ledger</a></li> -->
<!-- 							<li><a href="${pageContext.request.contextPath}/aiList">PF Profit Ledger</a></li> -->
<!-- 							<li><a href="${pageContext.request.contextPath}/getAIForm">PF Profit</a></li> -->
<!-- 							<li><a href="${pageContext.request.contextPath}/aiList">Profit Distribute</a></li> -->
						</ul></li>
				</sec:authorize>
				
				<sec:authorize
					access="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')">
					<li class="has_sub"><a href="#"><i class="fa fa-database"></i>Reports<span
							class="pull-right"><i class="fa fa-chevron-right"></i></span></a>
						<ul>
							<li><a href="${pageContext.request.contextPath}/getAIForm">Yearly Company Reports</a></li>
							<li><a href="${pageContext.request.contextPath}/aiList">Yearly Employee Reports</a></li>
							<li><a href="${pageContext.request.contextPath}/getAIForm">Yearly Individual Tax</a></li>
						</ul></li>
				</sec:authorize>
				</c:if>
			</ul>

		</div>

		<!-- Sidebar ends -->
		<!-- Main bar -->



		<!-- Mainbar ends -->
		<div class="clearfix"></div>

	</div>
	<!-- Content ends -->

	<!-- Footer starts -->
	<!-- <footer>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
            Copyright info
            <p class="copy">Copyright &copy; 2012 | <a href="${pageContext.request.contextPath}/http://lexiconbd.org" target="_blank">Lexicon_Merchandise</a> </p>
      </div>
    </div>
  </div>
</footer> 	 -->

	<!-- Footer ends -->

	<!-- Scroll to top -->
	<span class="totop"><a href="#"><i class="fa fa-chevron-up"></i></a></span>
	<!--chart js   -->
	<!-- <script src="js/jquery-1.11.1.min.js"></script> -->
	<!-- <script src="js/highcharts.js"></script> -->
	<script src="${pageContext.request.contextPath}/resource/js/Chart.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/Chart1.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/legend.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/demo.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/zingchart.min.js"></script>

	<!-- for add delete row -->
	<script src="${pageContext.request.contextPath}/resource/js/itemSelect.js"></script>
	<!-- JS -->
	<!-- <script src="${pageContext.request.contextPath}/resource/js/jquery.js"></script> -->
	<!-- jQuery -->
	<!-- <script src="${pageContext.request.contextPath}/resource/js/jquery.min.js"></script> jQuery -->
	<!-- <script src="${pageContext.request.contextPath}/resource/js/jquery-3.1.0.min.js"></script> -->
	<!-- jQuery -->
	<script src="${pageContext.request.contextPath}/resource/js/bootstrap.min.js"></script>
	<!-- Bootstrap -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery-ui.min.js"></script>
	<!-- jQuery UI -->
	<script src="${pageContext.request.contextPath}/resource/js/fullcalendar.min.js"></script>
	<!-- Full Google Calendar - Calendar -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.rateit.min.js"></script>
	<!-- RateIt - Star rating -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.prettyPhoto.js"></script>
	<!-- prettyPhoto -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.slimscroll.min.js"></script>
	<!-- jQuery Slim Scroll -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
	<!-- Data tables -->

	<!-- jQuery Flot -->
	<script src="${pageContext.request.contextPath}/resource/js/excanvas.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flot.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flot.resize.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flot.pie.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flot.stack.js"></script>

	<!-- jQuery Notification - Noty -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.noty.js"></script>
	<!-- jQuery Notify -->
	<script src="${pageContext.request.contextPath}/resource/js/themes/default.js"></script>
	<!-- jQuery Notify -->
	<script src="${pageContext.request.contextPath}/resource/js/layouts/bottom.js"></script>
	<!-- jQuery Notify -->
	<script src="${pageContext.request.contextPath}/resource/js/layouts/topRight.js"></script>
	<!-- jQuery Notify -->
	<script src="${pageContext.request.contextPath}/resource/js/layouts/top.js"></script>
	<!-- jQuery Notify -->
	<!-- jQuery Notification ends -->

	<script src="${pageContext.request.contextPath}/resource/js/sparklines.js"></script>
	<!-- Sparklines -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.cleditor.min.js"></script>
	<!-- CLEditor -->
	<script src="${pageContext.request.contextPath}/resource/js/bootstrap-datetimepicker.min.js"></script>
	<!-- Date picker -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.onoff.min.js"></script>
	<!-- Bootstrap Toggle -->
	<script src="${pageContext.request.contextPath}/resource/js/filter.js"></script>
	<!-- Filter for support page -->
	<script src="${pageContext.request.contextPath}/resource/js/custom.js"></script>
	<!-- Custom codes -->
	<script src="${pageContext.request.contextPath}/resource/js/charts.js"></script>
	<!-- Charts & Graphs -->



	<!-- jQuery Unknown -->
	<script src="${pageContext.request.contextPath}/resource/js/jquery.calculation.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/legend.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/respond.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/topRight.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/navAccordion.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/navAccordion.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resource/js/bootstrap-timepicker.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/js/bootstrap-timepicker.min.js"></script>
<!-- <script
		src="https://cdn.jsdelivr.net/bootstrap.timepicker/0.2.6/js/bootstrap-timepicker.js"></script>
	<script
		src="https://cdn.jsdelivr.net/bootstrap.timepicker/0.2.6/js/bootstrap-timepicker.min.js"></script> 
 -->




</body>
</html>

