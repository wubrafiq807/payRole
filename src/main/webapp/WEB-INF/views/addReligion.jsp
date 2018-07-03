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

<c:url var="findURL" value="/checkReligion.htm" />

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
	var yourUrl = '<c:out value="${findURL}"/>';
	
	$(document).ready(function() {
	    $('#table1').DataTable();
	    
	    $('.req-save-update-btn').click(function() {
			var flag = false, deptCheckResp = false;
			$(".req-save-update-btn").prop('disabled', true);

			if ($('#religionName').val().trim().length > 0) {
				flag = false;
			} else {
				flag = true;
				$(".req-save-update-btn").prop('disabled',false);
				w2alert("Please Input Religion.");
				return flag;
			}
			
			if (!flag) {
				deptCheckResp = checkReligion();
			}

			if (deptCheckResp) {
				$(".req-save-update-btn").prop('disabled',true);
				$('#religionForm').submit();
			} else {
				$(".req-save-update-btn").prop('disabled',false);
				$('#religionName').value="";
				$('#religionName').focus();
				w2alert("Religion already exist!!");    	 		
				return flag;
			}
		});
	    
	} );
		

function checkReligion(){	
	var res=false;
	var deptName=$('#name');
	   	 $.ajax({
	    	  type: "post",	    	 
	    	  url: yourUrl,
	    	  async: false,    
	    	  data:'religionName=' + $('#religionName').val(),	    	  
	    	  success: function(response){
	    	 	if (response=="false"){
	    	 		res = false;
	    	 	} else {
	    	 		res = true;
	    	 	}
	    	 	              
	    	  },
	    	  error: function(){   
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
		<c:choose>
			<c:when test="${edit}">
				<h2 class="pull-left" style="color: #5BBC2E;">Edit Religion</h2>
			</c:when>
			<c:otherwise>
				<h2 class="pull-left" style="color: #5BBC2E;">Add New Religion</h2>
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
							<form:form id="religionForm" cssClass="form-horizontal" method="POST" modelAttribute="religionForm"
								action="${pageContext.request.contextPath}/saveReligion.htm"
								class="form-horizontal" role="form">
								
								<form:input type="hidden" path="id" value="${religion.id}"
									readonly="true" class="form-control" />

								<div class="form-group">
									<label class="col-lg-2 control-label">Religion:</label>
									<div class="col-lg-5">
										<form:input id="religionName" path="religionName" value="${religion.religionName}"
											class="form-control"/>
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
													href="${pageContext.request.contextPath}/newReligionForm.htm">Cancel</a>
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
								<div class="pull-left">Religion Details</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="table-responsive">
									<c:if test="${!empty religionList}">
										<table class="table table-striped table-bordered table-hover"
											id="table1">
											<thead>
												<tr style="background-color: #5cb85c; color: white">
													<th class="text-center">SL No.</th>
													<th class="text-center">Religion Name</th>
													<th class="text-center">Action</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${religionList}" var="religion" varStatus="slNo">
													<tr>
														<td class="text-center"><c:out value="${slNo.count}" /></td>
														<td class="text-center"><c:out value="${religion.religionName}" /></td>
														<td class="text-center">
															<a class="btn btn-primary custom" href="editReligion.htm?id=${religion.id}">Edit</a> 
															<a class="btn btn-danger custom" href="deleteReligion.htm?id=${religion.id}"
															onclick="return confirm('Are you sure you want to delete?')">Delete</a>
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