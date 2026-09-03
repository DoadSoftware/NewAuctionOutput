<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise Screen</title>

<script src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js'/>"></script>
<script src="<c:url value='/webjars/bootstrap/5.3.8/js/bootstrap.bundle.min.js'/>"></script>
<script src="<c:url value='/resources/javascript/index.js'/>"></script>

<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.8/css/bootstrap.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.7.2/css/all.min.css'/>"/>
		
</head>
<body onload="initialiseForm('initialise')">
<form:form name="initialise_form" autocomplete="off" action="output" method="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
          <div id="initialise_div" style="display:none;">
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_configuration_file" class="col-sm-4 col-form-label text-left">Select Configuration </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_configuration_file" name="select_configuration_file" 
			      		class="brower-default custom-select custom-select-sm" onchange="processUserSelection(this)">
			          	<option value=""></option>
						<c:forEach items = "${configuration_files}" var = "config">
				          	<option value="${config.name}">${config.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="configuration_file_name" class="col-sm-4 col-form-label text-left">Configuration File Name </label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="configuration_file_name" name="configuration_file_name"
		             	class="form-control form-control-sm floatlabel"></input>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_cricket_matches" class="col-sm-4 col-form-label text-left">Select Auction File </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_cricket_matches" name="select_cricket_matches" 
			      		class="browser-default custom-select custom-select-sm">
						<c:forEach items = "${match_files}" var = "match">
				          	<option value="${match.name}">${match.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_broadcaster" class="col-sm-4 col-form-label text-left">Select Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_broadcaster" name="select_broadcaster" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="VCL">VCL</option>
			      		<option value="PSL">PSL</option>
			      		<option value="PWL">PWL</option>
			      		<option value="KCL">KCL</option>
			      		<option value="KCL_BIGSCREEN">KCL BIG SCREEN</option>
			      		<option value="VCL_BIGSCREEN">VCL BIG SCREEN</option>
			      		<option value="VIZ_ISPL_2024">VIZ ISPL 2024</option>
			      	    <option value="ISPL_VIZ">ISPL VIZ</option>
			          <option value="HANDBALL">HANDBALL</option>
			          <option value="UTT_VIZ">UTT</option>
			          <option value="UTT_BIGSCREEN">UTT BIG SCREEN</option>
			          <option value="MUMBAI_T20_VIZ">MUMBAI T20</option>
			          <option value="RALLY">RALLY</option>
			          <option value="MUMBAI_T20_BIGSCREEN">MUMBAI T20 BIG SCREEN</option>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="which_category" class="col-sm-4 col-form-label text-left">Select Category </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="which_category" name="which_category" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="Men"> </option>
			          <option value="Men">Men</option>
			          <option value="Women">Women</option> 
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="which_layer" class="col-sm-4 col-form-label text-left">Which Layer </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="which_layer" name="which_layer" class="browser-default custom-select custom-select-sm"
			      		onchange="processUserSelection(this)">
			          <option value="Front_layer">FRONT</option>
			         <!-- <option value="DOAD_In_House_Everest">DOAD In House Everest</option> --> 
			      </select>
			    </div>
			  </div>
			  <div id="vizPortNumber_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizPortNumber" class="col-sm-4 col-form-label text-left">Viz Port Number 
			    	<i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizPortNumber" name="vizPortNumber" value="${session_Configurations.portNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
		              <label id="vizPortNumber-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  <div id="vizIPAddress_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="vizIPAddress" class="col-sm-4 col-form-label text-left">Viz IP Address 
			    	<i class="fas fa-asterisk fa-sm text-danger" style="font-size: 7px;"></i></label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="vizIPAddress" name="vizIPAddress" value="${session_Configurations.ipAddress}"
		             		class="form-control form-control-sm floatlabel" value="localhost"></input>
		              <label id="vizIPAddress-validation" style="color:red; display: none;"></label> 
			    </div>
			  </div>
			  &nbsp;
		    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
		  		name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)">
		  		<i class="fas fa-film"></i> Load Scene</button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>