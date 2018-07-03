<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<c:url var="monthlyDeductionList" value="/getMonthlyDeductionList.htm" />
<c:url var="saveMonthlyDeductionList" value="/saveMonthlyDeductionList.htm" />

<!-- <link rel="stylesheet" type="text/css" href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
<!-- <link rel="stylesheet" type="text/css"
	href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
	
<link href="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.css" rel="stylesheet">


<style type="text/css">
td img {
	display: block;
	margin-left: auto;
	margin-right: auto;
}

.centered {
	width: 50px;
	margin: 0px, auto, 0px, auto;
}
</style>



<script type="text/javascript">
	function goBack() {
		window.history.back();
	}

	$(document).ready(function() {
		$('#table1').DataTable({
			dom : 'Bfrtip',
			buttons : [ 'copy', 'csv', 'excel', 'print', {
				extend : 'pdfHtml5',
				orientation : 'landscape',
				pageSize : 'LEGAL'
			} ]
		});

	});
</script>

</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Additional Deduction
			Entry Sheet for ${paymentMonth.paymentMonthName}, ${paymentMonth.payYear}</h2>
		<div class="clearfix"></div>
	</div>
	<input type="hidden" value="${paymentMonth.id}" id="paymentMonthId">
	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">

				<!-- Table -->

				<div class="row">

					<div class="col-md-12">

						<div class="widget">

							<div class="widget-head">
								<div class="pull-left">Additional Deduction Entry Sheet for
									${paymentMonth.paymentMonthName}, ${paymentMonth.payYear}</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content table-responsive">
							
								<div id="grid" style="width: 100%; height: 500px;"></div>
								<br>
								<button class="w2ui-btn" onclick="showChanged()">Save</button>
								<button class="w2ui-btn" id="deductionFinalSubmit">Final Submit</button>

							</div>
						</div>
					</div>
					
					<form action="${pageContext.request.contextPath}/deductionFinalSubmit.htm" method="post" hidden="true" id="deductionFinalSubmitForm">
						<input type="hidden" value="${paymentMonth.id}" name="paymentMonthId">
					</form>

				</div>

			</div>

		</div>

	</div>
	<!-- table ends -->
	<!-- Mainbar ends -->
	<div class="clearfix"></div>
	
	<script type="text/javascript">
	var monthlyDeductionListURL = '<c:out value="${monthlyDeductionList}"/>';
	var saveMonthlyDeductionListURL = '<c:out value="${saveMonthlyDeductionList}"/>';
	
	var resultData = new Array();
	var paymentMonthId = $('#paymentMonthId').val();
	

	$(function () {
		
		loadTableData();
	});

	function showChanged() {	
		var tableData = w2ui['grid'].getChanges();
		if(tableData.length > 0){
			$.ajax({
			    type : "post",
			    url : saveMonthlyDeductionListURL,
			    async : false,
			    data : JSON.stringify({deductionSheetList:w2ui['grid'].getChanges()}),
			    contentType : "application/json",
			    success : function(response) {
			    	var result = JSON.parse(response);
			    	if(result == 'success'){
			    		w2alert("Operation Successfull...");
			    		location.reload();
			    		w2alert("Please Wait...");
			    	}else{
			    		w2alert("Please try again after sometimes!");
			    	}
			    	
			    },
			    error : function() {
			    	w2alert("Server Error!!!");
			    }
			});	
		}	       
	}
	
	function loadTableData(){
		
		$.ajax({
		    type : "post",
		    url : monthlyDeductionListURL,
		    async : false,
		    data : JSON.stringify({paymentMonthId:paymentMonthId}),
		    contentType : "application/json",
		    success : function(response) {
		    	result = JSON.parse(response);			
		    	//w2alert(result);
		    	resultData = result;
		    	//var jsonObj = $.parseJSON('[' + response + ']');
		    },
		    error : function() {
		    }
		   });
		
		$('#grid').w2grid({ 
	        name: 'grid', 
	        show: { 
	            toolbar: true,
	            footer: true
	        },
	        multiSearch: true,
	        searches: [
	            { field: 'employee.fullname', caption: 'Employee Name', type: 'text' },
	            { field: 'employee.employeeId', caption: 'Employee ID.', type: 'text' },
	            { field: 'employee.department.name', caption: 'Department', type: 'text' },
	            { field: 'employee.designation.name', caption: 'Designation', type: 'text' }
	        ],
	        columns: [                
	            { field: 'slNo', caption: 'SL. No.', size: '60px', sortable: true, resizable: false },
	            { field: 'recid', caption: 'Row ID', size: '70px', sortable: true, resizable: false },
	            { field: 'employee.fullname', caption: 'Employee', size: '200px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.employeeId', caption: 'Employee ID', size: '100px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.department.name', caption: 'Department', size: '120px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.designation.name', caption: 'Designation', size: '120px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'fineAmount', caption: 'Fine Amount', size: '100px', sortable: true, resizable: true,
	                editable: { type: 'float', min: 0.0, max: 100000.0 }
	            },
	            { field: 'deductStamp', caption: 'Deduct Stamp', size: '100px', sortable: true, resizable: true,
	                editable: {  type: 'float', min: 0.0, max: 100000.0 }
	            },
	            { field: 'remarks', caption: 'Remarks', sortable: true, resizable: true, 
	                editable: { type: 'text' }
	            }
	        ],
	        records: resultData
	    }); 
		
	}
	
	$(document).on("click", "#deductionFinalSubmit", function(){
		var tableData = w2ui['grid'].getChanges();
		if(tableData.length > 0){
			w2alert("Your Changes Are Not Saved. Please Click Save Button First.");
		} else {
			w2confirm('After final submit you can\'t change any data forever, Do you Agree?', function btn(answer) {
			    if(answer == 'Yes'){		    	
					$('#deductionFinalSubmitForm').submit();
					w2alert("Please Wait...");
			    } 
			});
		}
	});

	</script>
	
	<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script> -->
	<!-- <script type="text/javascript" src="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>
</body>
</html>