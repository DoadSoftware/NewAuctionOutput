<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Output Screen</title>
	
	<script src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js'/>"></script>
	<script src="<c:url value='/webjars/bootstrap/5.3.8/js/bootstrap.bundle.min.js'/>"></script>
	<script src="<c:url value='/webjars/select2/4.1.0/dist/js/select2.min.js'/>"></script>
	<script src="<c:url value='/resources/javascript/index.js'/>"></script>
	
	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.8/css/bootstrap.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/select2/4.1.0/dist/css/select2.min.css'/>"/>
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.7.2/css/all.min.css'/>"/>

  <script type="text/javascript">
	$(document).on("keydown", function(e){
	  
	  if($('#waiting_modal').hasClass('show')) {
		  e.cancelBubble = true;
		  e.stopImmediatePropagation();
    	  e.preventDefault();
		  return false;
	  }
	  
      var evtobj = window.event? event : e;
      
      switch(e.target.tagName.toLowerCase())
      {
      case "input": case "textarea":
    	 break;
      default:
	      var whichKey = '';
		  var validKeyFound = false;
		  
		  if(evtobj.key != 'Tab'){
    		  e.preventDefault();
    	  }
	    
	      if(evtobj.ctrlKey) {
	    	  whichKey = 'Control';
	      }
	      if(evtobj.altKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Alt';
	    	  } else {
	        	  whichKey = 'Alt';
	    	  }
	      }
	      if(evtobj.shiftKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Shift';
	    	  } else {
	        	  whichKey = 'Shift';
	    	  }
	      }
	      
		  if(evtobj.keyCode) {
	    	  if(whichKey) {
	    		  if(!whichKey.includes(evtobj.key)) {
	            	  whichKey = whichKey + '_' + evtobj.key;
	    		  }
	    	  } else {
	        	  whichKey = evtobj.key;
	    	  }
		  }
		  validKeyFound = false;
		  if (whichKey.includes('_')) {
			  whichKey.split("_").forEach(function (this_key) {
				  switch (this_key) {
				  case 'Control': case 'Shift': case 'Alt':
					break;
				  default:
					validKeyFound = true;
					break;
				  }
			  });
		   } else {
			  if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
				  validKeyFound = true;
			  }
		   }
			  
		   if(validKeyFound == true) {
			   console.log('whichKey = ' + whichKey);
			   processUserSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
		   }
	      }
	  });
 	setInterval(() => {
 		processAuctionProcedures('READ-MATCH-AND-POPULATE');		
	}, 1000);
  </script>
</head>
<body>
<form:form name="output_form" autocomplete="off" action="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
            <!--   <h3 class="mb-0">${licence_expiry_message}</h3>  -->
           </div>
          <div class="card-body">
          
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  	<!--  <label class="col-sm-4 col-form-label text-left">${licence_expiry_message} </label> 
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.matchFileName} </label> -->
			    <label class="col-sm-4 col-form-label text-left">IP Address: ${session_selected_ip} </label>
			    <label class="col-sm-4 col-form-label text-left">Port Number: ${session_port} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster} </label>
			    
			    <label id="player_name" class="col-sm-4 col-form-label text-left">NAME :</label>
			    <label id="player_status" class="col-sm-4 col-form-label text-left">STATUS :</label>
			    <label id="player_last_year_team" class="col-sm-4 col-form-label text-left">LAST YEAR TEAM :</label>
			    
			    <div class="custom-toggle">
			        <label for="audioOnOrOff">
			            <i class="fas fa-volume-up"></i> Audio
			            <input type="checkbox" id="audioOnOrOff" name="audioOnOrOff" onclick="processUserSelection(this)">
			            <span class="slider"></span>
			        </label>
			    </div>
			     
			    <!--
			    <div class="left">
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)"> AnimateOut (-)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_profile_graphic_btn" id="animateout_profile_graphic_btn" onclick="processUserSelection(this)"> AnimateOut SCOREBUG (=) </button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_profile_stat_graphic_btn" id="animateout_profile_stat_graphic_btn" onclick="processUserSelection(this)"> Animate out Stats (9)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="clearall_graphic_btn" id="clearall_graphic_btn" onclick="processUserSelection(this)"> Clear All (Space) </button>
				-->
			  </div>
			  
			  <div id="auction_div" class="col-sm-12" style="margin-top:10px;">
			  <c:if test="${(session_selected_broadcaster == 'KCL')}">
				<table class="table table-bordered table-hover" style="font-size: 1.4rem; background-color: #e0f7fa; width: 100%; max-width: 100%;">
				    <thead>
				      <tr style="background-color: #219ebc; color: white; font-weight: 700;">
				        <th>Team</th>
				        <th>A</th>
				        <th>B</th>
				        <th>C</th>
				      </tr>
				    </thead>
			    <tbody id="zone_table_body">
			      <tr><td>Team 1</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 2</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 3</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 4</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 5</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 6</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 7</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 8</td><td>0</td><td>0</td><td>0</td></tr>
			    </tbody>
			  </table>	
  			</c:if>
			  <c:if test="${(session_selected_broadcaster == 'PWL')}">
				<table class="table table-bordered table-hover" style="font-size: 1.4rem; background-color: #e0f7fa; width: 100%; max-width: 100%;">
				    <thead>
				      <tr style="background-color: #219ebc; color: white; font-weight: 700;">
				        <th>Team</th>
				        <th>A+</th>
				        <th>A</th>
				        <th>B</th>
				        <th>C</th>
				      </tr>
				    </thead>
			    <tbody id="zone_table_body">
			      <tr><td>Team 1</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 2</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 3</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 4</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 5</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 6</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			     </tbody>
			  </table>	
  			</c:if>
			  <c:if test="${(session_selected_broadcaster == 'VIZ_ISPL_2024')}">
				<table class="table table-bordered table-hover" style="font-size: 1.4rem; background-color: #e0f7fa; width: 100%; max-width: 100%;">
				    <thead>
				      <tr style="background-color: #219ebc; color: white; font-weight: 700;">
				        <th>Team</th>
				        <th>North Zone</th>
				        <th>East Zone</th>
				        <th>South Zone</th>
				        <th>West Zone</th>
				        <th>Central Zone</th>
				        <th>U19</th>
				      </tr>
				    </thead>
			    <tbody id="zone_table_body">
			      <tr><td>Team 1</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 2</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 3</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 4</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 5</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 6</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 7</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 8</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			    </tbody>
			  </table>	
  			</c:if>
  			<!--<c:if test="${(session_selected_broadcaster == 'MUMBAI_T20_VIZ')}">
				<table class="table table-bordered table-hover" style="font-size: 1.4rem; background-color: #e0f7fa; width: 100%; max-width: 100%;">
				    <thead>
				      <tr style="background-color: #219ebc; color: white; font-weight: 700;">
				        <th>Team</th>
				        <th>North Zone</th>
				        <th>East Zone</th>
				        <th>South Zone</th>
				        <th>West Zone</th>
				      </tr>
				    </thead>
			    <tbody id="zone_table_body">
			      <tr><td>Team 1</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 2</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 3</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 4</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 5</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 6</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 7</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			      <tr><td>Team 8</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			    </tbody>
			  </table>	
  			</c:if>
			</div>
			    <!-- 
			    <label class="col-sm-4 col-form-label text-left" style="font-size: 20px;">SCOREBUG</label>
			    <div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="playerprofile_graphic_btn" id="playerprofile_graphic_btn" onclick="processUserSelection(this)"> SCOREBUG (F1) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="curr_bid_section" id="curr_bid_section" onclick="processUserSelection(this)"> Populate Current bid (1) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="profile_stats_graphic_btn" id="profile_stats_graphic_btn" onclick="processUserSelection(this)"> Profile stats (F9) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> LT </label>
			  	<div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="namesuper_graphic_btn" id="namesuper_graphic_btn" onclick="processUserSelection(this)"> Name Super (F7) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="rtm_available_graphic_btn" id="rtm_available_graphic_btn" onclick="processUserSelection(this)"> RTM Available (F8) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="rtm_enabled_graphic_btn" id="rtm_enabled_graphic_btn" onclick="processUserSelection(this)"> RTM Enabled (8) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="googly_power_graphic_btn" id="googly_power_graphic_btn" onclick="processUserSelection(this)"> Googly Power (E) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> LOF </label>
			  	<div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_top_sold_graphic_btn" id="lof_top_sold_graphic_btn" onclick="processUserSelection(this)"> Lof Top Sold (F6) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_remaining_purse_graphic_btn" id="lof_remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Lof Remaining Purse (F10) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_top_sold_team_graphic_btn" id="lof_top_sold_team_graphic_btn" onclick="processUserSelection(this)"> Lof Team Top Sold (F11) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_Player_graphic_btn" id="squad_Player_graphic_btn" onclick="processUserSelection(this)"> Lof Team Player Category (F12) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> FULLFRAMES </label>
			  	<div class="left">
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_graphic_btn" id="remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse All (F2) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_graphic_btn" id="squad_graphic_btn" onclick="processUserSelection(this)"> Squad (F3) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ident_graphic_btn" id="ident_graphic_btn" onclick="processUserSelection(this)"> Ident (F4) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_graphic_btn" id="top_sold_graphic_btn" onclick="processUserSelection(this)"> Top Sold Auction (F5) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_team_graphic_btn" id="top_sold_team_graphic_btn" onclick="processUserSelection(this)"> Top Sold Team (Q) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ff_playerprofile_graphic_btn" id="ff_playerprofile_graphic_btn" onclick="processUserSelection(this)"> ProfilePlayer FF (W) </button>
			  	</div>
			  	
			  	
			  	
			   
			    
				<!--<c:if test="${(session_selected_second_broadcaster == 'ISPL')}">
  					<div class="left">
				
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="playerprofile_graphic_btn" id="playerprofile_graphic_btn" onclick="processUserSelection(this)"> PlayerProfile </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_graphic_btn" id="remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse All </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_single_graphic_btn" id="remaining_purse_single_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse Single </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_graphic_btn" id="top_sold_graphic_btn" onclick="processUserSelection(this)"> Top Sold </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_graphic_btn" id="squad_graphic_btn" onclick="processUserSelection(this)"> Squad </button>	
			  	
			  	</div>	
  				 </c:if> -->
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
<input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.which_key_press}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" name="selected_which_layer" id="selected_which_layer" value="${selected_layer}"></input>
</form:form>
</body>
</html>