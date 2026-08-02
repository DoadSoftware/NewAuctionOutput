var match_data,session_auction;
let selectedArray = [];
let teams = [];
var which_GFX = "",selectedSubHeader = "";
let currentTeamIndex = 0,id = 0;  
let teamRotationInterval = null; 


// Function to start rotating teams
function startTeamRotation() {
    teamRotationInterval = setInterval(function() {
        if (teams && teams.length > 0) {
	        // Get the current team based on the index
	         let team = teams[(currentTeamIndex + 1) % teams.length];
	         id = team.teamId;
	        // Move to the next team, circularly
	        currentTeamIndex = (currentTeamIndex + 1) % teams.length;
	        processAuctionProcedures('POPULATE-SQUAD_ANIMATION');
        }
    }, 10000);
}

function stopTeamRotation() {
    if (teamRotationInterval) {
        clearInterval(teamRotationInterval);
        teamRotationInterval = null;
    }
}
 
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		switch(dataToProcess){
		case 'Escape':
			$("#captions_div").show();
			$('#select_graphic_options_div').hide();
			$("#cancel_match_setup_btn").show();
			$("#expiry_message").show();
			$("#auction_div").hide();
			break;
		case ' '://Space
			if(which_GFX === "POPULATE-SQUAD_ANIMATION"){
				stopTeamRotation();
				which_GFX = "";
			 }
			processAuctionProcedures('CLEAR-ALL');
			break;
		case '-'://189
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				if(which_GFX === "POPULATE-SQUAD_ANIMATION"){
					stopTeamRotation();
					which_GFX = "";
				 }
				processAuctionProcedures('ANIMATE-OUT');	
			}
			break;
		case "="://187
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processAuctionProcedures('ANIMATE-OUT-PROFILE');	
			}
			break;
		case '`':
			processAuctionProcedures('ANIMATE-OUT-ALL_GFX');
			break;
			
		case "0"://187
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processAuctionProcedures('ANIMATE-OUT-RTM_GOOGLY');	
			}
			break;
		case "o"://187
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processAuctionProcedures('ANIMATE-OUT-CRAWLER');	
			}
			break;
			
		case 'Alt_r':
			processAuctionProcedures('RE_READ_DATA');
			break;
			
		case '1':
			processAuctionProcedures('POPULATE-CURR_BID');
			break;
		case '2':
			processAuctionProcedures('POPULATE-RTM_PLAYER');
			break;
			
		case 'F1':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('PLAYERPROFILE_MAIN_GRAPHICS-OPTIONS');
			break;
		case 'F2': //FF REMAINING PURSE ALL
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('POPULATE-REMAINING_PURSE_ALL');
			break;
		case 'F3': //FF SQUAD
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('SQUAD_GRAPHICS-OPTIONS');
			break;
		
		case 'Control_e':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('TEAMS_GRAPHICS-OPTIONS');
			break;
		
		case 'r': //FF SQUAD
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			stopTeamRotation();
			which_GFX = "";
			addItemsToList('SQUADCATEGORIES-OPTIONS',null);
			break;
		case 'c': //FF SQUAD
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('FLIPPER_SQUAD_GRAPHICS-OPTIONS');
			break;
			
		case 'F4': //FF IDENT
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('POPULATE-IDENT');
			break;
		case 'Shift_E':
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('POPULATE-TEAMS_DETAILS');
			break;
			
		case 'Alt_a':
			processAuctionProcedures('POPULATE-RULES');
			break;
			
		case 'F5': //FF SQUAD SIZE, RTM AVAILABLE, PURSE REM
			switch ($('#selected_broadcaster').val()){
			case 'PSL':
				break;
			default:
				processAuctionProcedures('POPULATE-FF_RTM_AND_PURSE_REMAINING');
				break;
			}
			break;
		
		case 'F6': //FF TOP BUY AUCTION
			stopTeamRotation();
			which_GFX = "";
			processAuctionProcedures('POPULATE-FF_TOP_BUYS_AUCTION');
			break;
		case 't': //FF 5 TOP BUY AUCTION
			stopTeamRotation();
			which_GFX = "";
			switch ($('#selected_broadcaster').val()){
			case 'UTT_VIZ': case 'UTT_BIGSCREEN':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				addItemsToList('GRAPHICS-FF_FIVE_TOP_BUYS_AUCTION',null);
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-FF_FIVE_TOP_BUYS_AUCTION');
				break;
			}
			break;	
		case 'Alt_b': //FF 5 TOP BUY AUCTION
			stopTeamRotation();
			which_GFX = "";
			switch ($('#selected_broadcaster').val()){
			case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('FF_SQUAD_GRAPHICS-OPTIONS');
				break;
			}
			break;	
			
		case 'Alt_d': //FF 5 TOP BUY AUCTION
			stopTeamRotation();
			which_GFX = "";
			switch ($('#selected_broadcaster').val()){
			case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL': case 'PSL':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('FF_SQUAD_ROLE_GRAPHICS-OPTIONS');
				break;
			}
			break;	
		case 'Control_p':
			processAuctionProcedures('POPULATE-PURSE_SIZE_ALL');
			break;
		case 'Control_u':
			processAuctionProcedures('POPULATE-PURSE_SLOT_ALL');
			break;	
		case 'Control_s': //FF 5 TOP BUY AUCTION
			switch ($('#selected_broadcaster').val()){
			case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('LOF_TEAM_BID_GRAPHICS-OPTIONS');
				break;
			}
			break;
		
		case 'F7': // FF TOP BUY TEAMS
			stopTeamRotation();
			which_GFX = "";
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('FF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'y': //FF 5 TOP BUY AUCTION
			stopTeamRotation();
			which_GFX = "";
		    $("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('FF_TOP_FIVE_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'Control_y': //SINGLE PURSE
			stopTeamRotation();
			which_GFX = "";
		    $("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('SINGLE_GRAPHICS-OPTIONS');
			break;	
			
		case 'F8': //POP UP RTM AVAILABLE
			processAuctionProcedures('POPULATE-RTM_AVAILABLE');
			break;
		case '8': //POP UP RTM ENABLED
			switch ($('#selected_broadcaster').val()){
			case 'UTT_VIZ': case 'UTT_BIGSCREEN':
				if(confirm('Are You Sure To Animate Out? ') == true){
					processAuctionProcedures('ANIMATE-OUT-RTM_AVAILABLE');	
				}
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-RTM_ENABLED');
				break;
			}
			break
			
		case '4': //SCORE BUG Which Team Current Bid
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('TEAM_CURRENT_BID_GRAPHICS-OPTIONS');
			break;
		case '3': //SCORE BUG Which Team Current Bid
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			addItemsToList('PROFILE_FF_STATS-OPTIONS',null);
			break;	
		case 'F9': //SCORE BUG BOTTOM
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			addItemsToList('PROFILE_STATS-OPTIONS',null);
			break;
		case '9': //SCORE BUG ANIMATE OUT BOTTOM
			processAuctionProcedures('ANIMATE-OUT-PLAYER_STAT');
			break;
		case 'F10': //changed
			stopTeamRotation();
			which_GFX = "";
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break
		case 'Control_F10': //changed
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('PROFILE_GRAPHICS-OPTIONS');
			break	
		case 'F11': //FF ICONIC PLAYERS
			switch ($('#selected_broadcaster').val()){
			case 'PSL':
				break;
			case 'UTT_BIGSCREEN':
				stopTeamRotation();
				which_GFX = "";
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('FF_BIG_PLAYERPROFILE_GRAPHICS-OPTIONS');
				break;
			default:
				processAuctionProcedures('POPULATE-FF_ICONIC_PLAYERS');
				break;
			}
			break;
		case 'Shift_R': //FF ICONIC PLAYERS
			switch ($('#selected_broadcaster').val()){
			case 'PSL': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				stopTeamRotation();
				which_GFX = "";
				processAuctionProcedures('RETAIN_PLAYERS_GRAPHICS-OPTIONS');
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-FF_RETAIN_PLAYERS');
				break;
			}
			break;
			
		case 'a': //LOF SLOT REMAINING
			switch ($('#selected_broadcaster').val()){
			case 'PSL':
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-LOF_REMAINING_SLOT');
				break;
			}
			break;
		case 's': // LOF SQUAD SIZE
			switch ($('#selected_broadcaster').val()){
			case 'KCL_BIGSCREEN': case 'UTT_BIGSCREEN': case 'MUMBAI_T20_BIGSCREEN':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('SQUAD_ANIMATION_GRAPHICS-OPTIONS');
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-LOF_SQUAD_SIZE');
				break;
			}
			break;
		case 'd': // LOF RTM REMAINING
			switch ($('#selected_broadcaster').val()){
			case 'KCL': case 'KCL_BIGSCREEN': case 'PWL': case 'PSL':
				alert('GFX IS NOT ACTIVE FOR THIS AUCTION');
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-LOF_RTM_REMAINING');
				break;
			}
			break;
		case 'e': //POP UP GOOGLY POWER
			//$("#captions_div").hide();
			//$("#cancel_match_setup_btn").hide();
			//$("#expiry_message").hide();
			//processAuctionProcedures('GOOGLY_GRAPHICS-OPTIONS');
			processAuctionProcedures('ANIMATE-IN-GOOGLY_POWER');
			break;
		case 'f': //LOF PURSE REMAINING
			switch ($('#selected_broadcaster').val()){
			case 'VIZ_ISPL_2024': case 'PSL': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL': case 'UTT_VIZ':
			case 'MUMBAI_T20_VIZ':
				processAuctionProcedures('POPULATE-LOF_REMAINING_PURSE');
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				addItemsToList('LOF_REMAINING_PURSE-OPTIONS',null);
				break;
			}
			break;
			
		case 'Alt_p': //case 'Alt_f':
			processAuctionProcedures('POPULATE-CRAWL-PURSE_REMAINING');
			break;
		case 'Alt_s':
			processAuctionProcedures('POPULATE-CRAWL-SQUAD_SIZE');
			break;
		case 'g': //LOF TOP BUYS
			processAuctionProcedures('POPULATE-LOF_TOP_SOLD');
			break;
		case 'Alt_q':
			processAuctionProcedures('POPULATE-CRAWL_TOP_SOLD');
			break;		
		case 'h':  //LOF TEAM TOP BUYS
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'Alt_w':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('CRAWL_TOP_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'Alt_e': //LOF SQUAD
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('CRAWL_SQUAD_GRAPHICS-OPTIONS');
			break;
		case 'l': //LOF SQUAD
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('LOF_SQUAD_GRAPHICS-OPTIONS');
			break;
		case '7':
			processAuctionProcedures('POPULATE-LOF_SQUAD_REMAIN');
			break;
			
		case 'j': //LOF SQUAD SIZE CATEGORY WISE HAVING SUB CATEGORY
			switch ($('#selected_broadcaster').val()){
			case 'PSL':
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('SQUAD_PLAYER_GRAPHICS-OPTIONS');
				break;
			}
			break;
		case 'k': //LOF SQUAD SIZE CATEGORY WISE
			switch ($('#selected_broadcaster').val()){
			case 'PSL':
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('LOF_SQUAD_SIZE_CATEGORY_WISE_GRAPHICS-OPTIONS');
				break;
			}
			break;
		case 'z':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('NAMESUPER_GRAPHICS-OPTIONS');
			break;
		case 'x':
			switch ($('#selected_broadcaster').val()){
			case 'UTT_VIZ': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':  case 'KCL': case 'KCL_BIGSCREEN': case 'PWL': case 'UTT_BIGSCREEN':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				addItemsToList('FLIPPER-OPTIONS',null);
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('FLIPPER_GRAPHICS-OPTIONS');
				break;
			}
			break;
		case 'Alt_x':
		    $("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('CRAWLERFREETEXT_GRAPHICS-OPTIONS');
	    	break;	
		case 'v':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('FLIPPER_TEXT_GRAPHICS-OPTIONS');
			break;
		
		case 'm': //LT ICONIC PPLAYERS
			
			switch ($('#selected_broadcaster').val()){
			case 'KCL': case 'PWL': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
				processAuctionProcedures('LOF_SLOT_CHANGEON');
				break;
			default://ISPL
				processAuctionProcedures('POPULATE-LT_ICONIC_PLAYERS');
				break;
			}
			break;
		case 'n': //LT PLAYER PROFILE
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			processAuctionProcedures('LT_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'b': //LT PLAYER PROFILE STATS
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			addItemsToList('LT_PP_STATS-OPTIONS',null);
			break;
		case 'F12':
			stopTeamRotation();
			which_GFX = "";
			switch ($('#selected_broadcaster').val()){
			case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			case 'UTT_VIZ': case'UTT_BIGSCREEN':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('ZONE-PLAYER_GRAPHICS-OPTIONS');
				break;
			case 'PSL':
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				addItemsToList('ZONE-PLAYER-OPTIONS',null);
				break;
			}
			break;	
		case '6': //ZONE PLAYERS CHANGE ON
			processAuctionProcedures('POPULATE-ZONE_PLAYERS_STATS');
			break
			
		case 'Alt_F12':
			stopTeamRotation();
			which_GFX = "";
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			$("#auction_div").hide();
			addItemsToList('ZONE-PLAYER_FULL-OPTIONS',null);
			break;
		case 'p': //ZONE PLAYERS CHANGE ON
			processAuctionProcedures('POPULATE-ZONE_PLAYERS_FULL');
			break
			
		case 'q':
		switch ($('#selected_broadcaster').val()){
			case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				processAuctionProcedures('ZONEWISE_PLAYER_SOLD_GRAPHICS-OPTIONS');
				break;
			default://ISPL
				$("#captions_div").hide();
				$("#cancel_match_setup_btn").hide();
				$("#expiry_message").hide();
				$("#auction_div").hide();
				addItemsToList('ZONEWISE_PLAYER_SOLD-OPTIONS',null);
				break;
			}
			break;
			
		/*case 'F5':
			processAuctionProcedures('POPULATE-TOP_SOLD');
			break;*/
		/*
		case 'q':
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			processAuctionProcedures('TOP-SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'F10': //changed
			$("#captions_div").hide();
			$("#cancel_match_setup_btn").hide();
			$("#expiry_message").hide();
			processAuctionProcedures('FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break */
		
		}
		break;
	}
}
function initialiseForm(whatToProcess,dataToProcess)
{
	session_auction = dataToProcess;
	switch (whatToProcess) {
	case 'initialise':
		processUserSelection($('#select_broadcaster'));
		break;
	case 'UPDATE-MATCH-ON-OUTPUT-FORM':
		if(dataToProcess.players != null){
			if(dataToProcess.players.length > 0){
				document.getElementById('player_name').innerHTML = "NAME : " + dataToProcess.players[dataToProcess.players.length-1].full_name +
				' ('+ dataToProcess.players[dataToProcess.players.length-1].category+ ')';
				document.getElementById('player_status').innerHTML = "STATUS : " + dataToProcess.players[dataToProcess.players.length-1].soldOrUnsold;
				if(dataToProcess.playersList[dataToProcess.players[dataToProcess.players.length-1].playerId-1].lastYearTeam){
					document.getElementById('player_last_year_team').innerHTML = "LAST YEAM TEAM : " + dataToProcess.team[dataToProcess.
						playersList[dataToProcess.players[dataToProcess.players.length-1].playerId-1].lastYearTeam-1].teamName1;
				}else{
					document.getElementById('player_last_year_team').innerHTML = "LAST YEAM TEAM : -";
				}
			}
				const tbody = document.getElementById('zone_table_body');
				
				tbody.innerHTML = "";
				if($('#selected_broadcaster').val().toUpperCase() == 'KCL' || $('#selected_broadcaster').val().toUpperCase() == 'KCL_BIGSCREEN'
				){
					for (let i = 0; i < dataToProcess.teamZoneList.length; i++) {

					    const row = tbody.insertRow();
					    row.style.fontSize = '16px';
					    row.style.fontWeight = '800';
					    row.style.color = '#BC8F8F';
					
					    const team = dataToProcess.teamZoneList[i];
					
					    let categoryList = [];
					    let cTotal = 0;
					
					    for (const key in team.category) {
					        if (key.trim().includes("C")) {
					            cTotal += Number(team.category[key]);
					        } else {
					            categoryList.push({
					                name: key,
					                value: team.category[key]
					            });
					        }
					    }
					
					    categoryList.push({
					        name: "C",
					        value: cTotal
					    });
					
					    row.innerHTML = `
					        <td>${team.teamName1}</td>
					        ${categoryList.map(c => `<td>${c.value}</td>`).join("")}
					    `;
					
					    
					    categoryList.forEach((cat, index) => {
					        if (cat.value >= 8) {
					            row.cells[index + 1].style.backgroundColor = "#E34234";
					            row.cells[index + 1].style.color = "white";
					        }
					    });
					}

				}else if($('#selected_broadcaster').val().toUpperCase() == 'PWL'){
					const order = ["A+", "A", "B", "C"]; // same order as Java

					for (let i = 0; i < dataToProcess.teamZoneList.length; i++) {
					
					    const row = tbody.insertRow();
					    row.style.fontSize = '16px';
					    row.style.fontWeight = '800';
					    row.style.color = '#BC8F8F';
					
					    const team = dataToProcess.teamZoneList[i];
					
					    let categoryList = [];
					    let cTotal = 0;
					
					    
					    for (const key in team.category) {
					        if (key.trim().includes("C")) {
					            cTotal += Number(team.category[key]);
					        } else {
					            categoryList.push({
					                name: key,
					                value: Number(team.category[key])
					            });
					        }
					    }
					
					    categoryList.push({
					        name: "C",
					        value: cTotal
					    });
					
					    categoryList.sort((a, b) => {
					        return order.indexOf(a.name) - order.indexOf(b.name);
					    });
					
					    
					    row.innerHTML = `
					        <td>${team.teamName1}</td>
					        ${categoryList.map(c => `<td>${c.value}</td>`).join("")}
					    `;
					
					   
					    categoryList.forEach((cat, index) => {
					        if (cat.value >= 8) {
					            row.cells[index + 1].style.backgroundColor = "#E34234";
					            row.cells[index + 1].style.color = "white";
					        }
					    });
					}

				}else{
					for (let i = 0; i < dataToProcess.teamZoneList.length; i++) {
				    const row = tbody.insertRow(); // Insert a new row
				    row.style.fontSize = '16px';
				    row.style.fontWeight = '800';
				    row.style.color = '#BC8F8F';
				
				    const north = dataToProcess.teamZoneList[i].northZone || 0;
				    const east = dataToProcess.teamZoneList[i].eastZone || 0;
				    const south = dataToProcess.teamZoneList[i].southZone || 0;
				    const west = dataToProcess.teamZoneList[i].westZone || 0;
				    const central = dataToProcess.teamZoneList[i].centralZone || 0;
				    const u19 = dataToProcess.teamZoneList[i].u19 || 0;
				
				    row.innerHTML = `
				        <td>${dataToProcess.teamZoneList[i].teamName1}</td>
				        <td>${north}</td>
				        <td>${east}</td>
				        <td>${south}</td>
				        <td>${west}</td>
				        <td>${central}</td>
				        <td>${u19}</td>
				    `;
				
				    
				    const zoneValues = [north, east, south, west, central, u19];
				
				    zoneValues.forEach((value, index) => {
				        // index + 1 because cell 0 is the team name
				        if (value >= 8) {
				            row.cells[index + 1].style.backgroundColor = "#E34234";
				            row.cells[index + 1].style.color = "white";
				        }
				    });
				}
				}
		}
		break;
	case 'UPDATE-CONFIG':
		document.getElementById('configuration_file_name').value = $('#select_configuration_file option:selected').val();
		document.getElementById('select_broadcaster').value = dataToProcess.broadcaster;
		document.getElementById('vizIPAddress').value = dataToProcess.ipAddress;
		document.getElementById('which_category').value = dataToProcess.category;
		document.getElementById('vizPortNumber').value = dataToProcess.portNumber;
		break;
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
	case 'select_configuration_file':
		processAuctionProcedures('GET-CONFIG-DATA');
		break;
	case 'audioOnOrOff':
		processAuctionProcedures('TURN_ON_OR_OFF_AUDIO');
		break;
		
	case 'animateout_profile_graphic_btn':
		processAuctionProcedures('ANIMATE-OUT-PROFILE');
		break;
	case 'animateout_profile_stat_graphic_btn':
		processAuctionProcedures('ANIMATE-OUT-PLAYER_STAT');
		break;
	case 'animateout_graphic_btn':
		if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
			processAuctionProcedures('ANIMATE-OUT');	
		}
		break;
	case 'clearall_graphic_btn':
		processAuctionProcedures('CLEAR-ALL');
		break;	
	case 'ident_graphic_btn':
		processAuctionProcedures('POPULATE-IDENT');
		break;
	case 'top_sold_graphic_btn':
		processAuctionProcedures('POPULATE-TOP_SOLD');
		break;
	case 'lof_top_sold_graphic_btn':
		processAuctionProcedures('POPULATE-LOF_TOP_SOLD');
		break;
	case 'remaining_purse_graphic_btn':
		processAuctionProcedures('POPULATE-REMAINING_PURSE_ALL');
		break;
	case 'rtm_available_graphic_btn':
		processAuctionProcedures('POPULATE-RTM_AVAILABLE');
		break;
	case 'rtm_enabled_graphic_btn':
		processAuctionProcedures('POPULATE-RTM_ENABLED');
		break;
	case 'playerprofile_graphic_btn': case 'squad_graphic_btn': case 'remaining_purse_single_graphic_btn': case 'namesuper_graphic_btn': case 'top_sold_team_graphic_btn':
	case 'googly_power_graphic_btn': case 'profile_stats_graphic_btn': case 'lof_remaining_purse_graphic_btn': case 'lof_top_sold_team_graphic_btn':
	case 'squad_Player_graphic_btn': case 'ff_playerprofile_graphic_btn':
	
		$("#captions_div").hide();
		$("#cancel_match_setup_btn").hide();
		$("#expiry_message").hide();
		
		switch ($(whichInput).attr('name')) {
		
		case 'namesuper_graphic_btn':
			processAuctionProcedures('NAMESUPER_GRAPHICS-OPTIONS');
			break;
		case 'playerprofile_graphic_btn':
			processAuctionProcedures('PLAYERPROFILE_MAIN_GRAPHICS-OPTIONS');
			break;
		case 'ff_playerprofile_graphic_btn':
			processAuctionProcedures('FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'squad_graphic_btn':
			processAuctionProcedures('SQUAD_GRAPHICS-OPTIONS');
			break;
		case 'top_sold_team_graphic_btn':
			processAuctionProcedures('TOP-SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'remaining_purse_single_graphic_btn':
			processAuctionProcedures('SINGLE_PURSE_GRAPHICS-OPTIONS');
			break;
		case 'googly_power_graphic_btn':
			processAuctionProcedures('GOOGLY_GRAPHICS-OPTIONS');
			break;
		case 'profile_stats_graphic_btn':
			addItemsToList('PROFILE_STATS-OPTIONS',null);
			break;
		case 'lof_remaining_purse_graphic_btn':
			addItemsToList('LOF_REMAINING_PURSE-OPTIONS',null);
			break;
		case 'lof_top_sold_team_graphic_btn':
			processAuctionProcedures('LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'squad_Player_graphic_btn':
			processAuctionProcedures('SQUAD_PLAYER_GRAPHICS-OPTIONS');
			break;
		}
		break;
		
	case 'populate_namesuper_btn': case 'populate_namesuper_player_btn': case 'populate_playerprofile_btn':	case 'populate_squad_btn': case 'populate_category_btn': case 'populate_Top_Sold_btn': 
	case 'populate_single_purse_btn': case 'curr_bid_section': case 'populate_googly_purse_btn':  case 'populate_teams_btn': case 'populate_profile_stats_btn': case 'populate_single_purse_btn': 
	case 'populate_zonePlayer_full_btn': case 'curr_bid_section': case 'populate_lof_remaining_purse_btn': case 'populate_crawl_Top_Sold_team_btn': case 'populate_Lof_Top_Sold_team_btn': 
	case "populate_squad_Player_btn": case "populate_ffTop5Buys_btn": case 'populate_squad_size_category_wise_btn': case 'populate_ff_playerprofile_btn': case 'populate_ff_Top_Sold_team_btn': 
	case 'populate_lt_playerprofile_btn': case 'populate_lt_playerprofile_stats_btn': case 'populate_Lof_squad_btn': case 'populate_crawle_squad_btn': case 'populate_freetextcrawler_btn': 
	case 'populate_flipper_btn': case 'populate_zonePlayer_stats_btn': case 'populate_team_curr_bid_btn': case 'populate_ff_Top_Five_Sold_team_btn': case 'single_Sold_team_btn': 
	case 'populate_zonewisePlayer_sold_btn':case "populate_profile_Change_stats_btn": case 'populate_flipper_squad_btn':case "populate_Squad_team_btn":case "populate_Squad_Role_team_btn":
	case 'populate_Lof_Team_Bid_btn': case 'populate_profileff_btn': case 'populate_flipper_text_btn': case 'populate_squad_animation_btn': case 'populate_retain_players_squad_btn':
	case 'populate_ff_big_playerprofile_btn':

		processWaitingButtonSpinner('START_WAIT_TIMER');
		switch ($(whichInput).attr('name')) {
		case 'curr_bid_section':
			processAuctionProcedures('POPULATE-CURR_BID');
			break;
		case 'populate_namesuper_btn':
			processAuctionProcedures('POPULATE-L3-NAMESUPER');
			break;
		case 'populate_flipper_btn':
			processAuctionProcedures('POPULATE-L3-FLIPPER');
			break;
		case 'populate_freetextcrawler_btn':
			processAuctionProcedures('POPULATE-L3-CRWLERFREETEXT');
			break;	
		case 'populate_flipper_text_btn':
			processAuctionProcedures('POPULATE-L3-FLIPPER_TEXT');
			break;
		case 'populate_namesuper_player_btn':
			processAuctionProcedures('POPULATE-L3-NAMESUPER-PLAYER');
			break;
		case 'populate_playerprofile_btn':
			processAuctionProcedures('POPULATE-FF-PLAYERPROFILE');
			break;
		case 'populate_ff_playerprofile_btn':
			processAuctionProcedures('POPULATE-PLAYERPROFILE_FF');
			break;
		case 'populate_ff_big_playerprofile_btn':
			processAuctionProcedures('POPULATE-PLAYERPROFILE_FF_BIG');
			break;
		case 'populate_profileff_btn':
			processAuctionProcedures('POPULATE-PROFILE_FF');
			break;
		case 'populate_lt_playerprofile_btn':
			processAuctionProcedures('POPULATE-PLAYERPROFILE_LT');
			break;
		case 'populate_lt_playerprofile_stats_btn':
			processAuctionProcedures('POPULATE-PLAYERPROFILE_LT_STATS');
			break;
		case 'populate_zonePlayer_stats_btn':
			processAuctionProcedures('POPULATE-ZONE_PLAYERS_STATS');
			break;
		case 'populate_zonePlayer_full_btn':
			processAuctionProcedures('POPULATE-ZONE_PLAYERS_FULL');
			break;
		case 'populate_zonewisePlayer_sold_btn':
			processAuctionProcedures('POPULATE-ZONEWISE_PLAYERS_SOLD');
			break;
		case 'populate_flipper_squad_btn':
			processAuctionProcedures('POPULATE-FLIPPER_SQUAD');
			break;
		case 'populate_squad_btn':
			processAuctionProcedures('POPULATE-SQUAD');
			break;
		case 'populate_teams_btn':
			processAuctionProcedures('POPULATE-TEAMS');
			break;
		case 'populate_retain_players_squad_btn':
			processAuctionProcedures('POPULATE-FF_RETAIN_PLAYERS');
			break;
		case 'populate_category_btn':
			processAuctionProcedures('POPULATE-LOF_CATRGORY');
			break;
		case 'populate_Top_Sold_btn':
			processAuctionProcedures('POPULATE-TOP_SOLD_TEAM');
			break;
		case 'populate_single_purse_btn':
			processAuctionProcedures('POPULATE-SINGLE_PURSE');
			break;
		case 'populate_googly_purse_btn':
			processAuctionProcedures('POPULATE-GOOGLY_POWER');
			break;
		case 'populate_profile_stats_btn':
			processAuctionProcedures('POPULATE-PROFILE_STATS');
			break;
		case "populate_profile_Change_stats_btn":
			processAuctionProcedures('POPULATE-PROFILE_STATS_CHANGE');
			break;
		case 'populate_lof_remaining_purse_btn':
			processAuctionProcedures('POPULATE-LOF_REMAINING_PURSE');
			break;
		case 'populate_Lof_Top_Sold_team_btn':
			processAuctionProcedures('POPULATE-LOF_TEAM_TOP_SOLD');
			break;
		case 'populate_crawl_Top_Sold_team_btn':
			processAuctionProcedures('POPULATE-CRAWLER_TEAM_TOP_SOLD');
			break;
		case 'populate_Lof_squad_btn':
			processAuctionProcedures('POPULATE-LOF_SQUAD');
			break;
		case 'populate_squad_animation_btn':
			selectedSubHeader = $('#selectSubHeader option:selected').val();
			processAuctionProcedures('POPULATE-SQUAD_ANIMATION');
			break;
		case 'populate_crawle_squad_btn':
			processAuctionProcedures('POPULATE-CRAWLE_SQUAD');
			break;	
		case "populate_squad_Player_btn":
			processAuctionProcedures('POPULATE-SQUAD-PLAYER');
			break;
		 case "populate_ffTop5Buys_btn":
		 	processAuctionProcedures('POPULATE-FF_FIVE_TOP_BUYS_AUCTION');
		 	break;
		 case 'populate_Lof_Team_Bid_btn':
			 processAuctionProcedures('POPULATE-LOF_TEAM_BID_AUCTION');
		 	break;
		case "populate_squad_size_category_wise_btn":
			processAuctionProcedures('POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE');
			break;
		case 'populate_ff_Top_Sold_team_btn':
			processAuctionProcedures('POPULATE-FF_TOP_BUY_TEAM');
			break;
		case "populate_Squad_team_btn":
			processAuctionProcedures('POPULATE-FF_SQUAD_TEAM');
			break;
		case "populate_Squad_Role_team_btn":
			processAuctionProcedures('POPULATE-FF_SQUAD_ROLE_TEAM');
			break;
		case'populate_ff_Top_Five_Sold_team_btn':
			processAuctionProcedures('POPULATE-FF_FIVE_TOP_BUY_TEAM');
			break;
		case 'single_Sold_team_btn':
		 	processAuctionProcedures('POPULATE-FF_SINGLEPURSE_TEAM');
			break;	
		case 'populate_team_curr_bid_btn':
			processAuctionProcedures('POPULATE-TEAM_CURR_BID');
			break;
		}
		break;
	case 'cancel_match_setup_btn':
		document.output_form.method = 'post';
		document.output_form.action = 'initialise';
	   	document.output_form.submit();
		break;
	case 'cancel_graphics_btn': 
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		$("#cancel_match_setup_btn").show();
		$("#auction_div").show();
		break;
	case 'select_broadcaster':
		switch ($('#select_broadcaster :selected').val().toUpperCase()) {
		case 'HANDBALL': case 'ISPL': case 'PSL':
			$('#vizPortNumber').attr('value','1980');
			$('label[for=which_layer], select#which_layer').hide();
			break;
		case	 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
			$('label[for=which_category], select#which_category').show();
			break;
		}
		break;
	case 'load_scene_btn':
		if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}
      	document.initialise_form.submit();
		break;
	case 'selectTeams':
		switch ($(whichInput).attr('name')) {
		case 'selectTeams':
			addItemsToList('POPULATE-PROFILE',match_data);
			break;
	}
	break;
	/*case 'selectTeams':
		switch ($(whichInput).attr('name')) {
		case 'selectTeams':
			addItemsToList('POPULATE-PROFILE',match_data);
			break;
	}
	break;*/
	}
}
function processAuctionProcedures(whatToProcess)
{
	var valueToProcess;
	switch(whatToProcess) {
	case 'GET-CONFIG-DATA':
		valueToProcess = $('#select_configuration_file option:selected').val();
		break;
	case 'TURN_ON_OR_OFF_AUDIO':
		valueToProcess = $('#audioOnOrOff').is(":checked");
		break;
	case 'READ-MATCH-AND-POPULATE': 
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'POPULATE-L3-NAMESUPER':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ISPL_VIZ':
			valueToProcess = '/Default/LT'+ ',' + $('#selectNameSuper option:selected').val();
			break;
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectNameSuper option:selected').val() + ',' + $('#selectLogo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-L3-FLIPPER':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ':  case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectFlipper option:selected').val();
			break;
		}
		break;
	case 'POPULATE-L3-CRWLERFREETEXT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024':  case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectFlipper option:selected').val();
			break;
		}
		break;
	case 'POPULATE-L3-FLIPPER_TEXT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectFlipper option:selected').val();
			break;
		}
		break;
	
		
	case 'POPULATE-PLAYERPROFILE_FF': case 'POPULATE-PROFILE_FF': case 'POPULATE-PLAYERPROFILE_FF_BIG':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectPlayerName option:selected').val() + ',' + $('#selectShowProfileStats option:selected').val() 
				+ ',' + $('#PlayerData option:selected').val();
			break;
		}
		break;
	case 'POPULATE-PLAYERPROFILE_LT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
			valueToProcess = $('#selectPlayerName option:selected').val() + ',' + $('#selectShowData option:selected').val() + ',' + $('#PlayerData option:selected').val();
			break;
		case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectPlayerName option:selected').val() + ',' + $('#selectLogo option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-PLAYERPROFILE_LT_STATS':  case 'POPULATE-ZONEWISE_PLAYERS_SOLD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
			valueToProcess = $('#selectShowData option:selected').val() + ',' + $('#PlayerData option:selected').val();
			break;
		case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectShowData option:selected').val() + ',' + $('#selectLogo option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-ZONE_PLAYERS_STATS': case 'POPULATE-ZONE_PLAYERS_FULL':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ":  case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectShowData option:selected').val();
			break;
		}
		break;
	case 'POPULATE-FF-PLAYERPROFILE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/PlayerProfile_Pic.sum' + ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/PlayerProfile_Pic.sum' + ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'ISPL_VIZ':
			valueToProcess = '/Default/BidLt'+ ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'VIZ_ISPL_2024': case 'PSL': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectPlayerName option:selected').val();
			break;
		case "UTT_VIZ":
			valueToProcess = $('#selectPlayerName option:selected').val() + ',' + $('#selectShowData option:selected').val() + ',' + $('#selectBottomData option:selected').val();
			break;
		case 'UTT_BIGSCREEN':
			valueToProcess = $('#selectPlayerName option:selected').val() + ',' + $('#selectShowData option:selected').val();
			break;
		}
		break;
	case 'POPULATE-FF_RETAIN_PLAYERS':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'PSL': case 'MUMBAI_T20_VIZ':case 'MUMBAI_T20_BIGSCREEN':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-TEAMS':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case "UTT_VIZ":
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-SQUAD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;	
		case 'ISPL_VIZ': 
			valueToProcess = '/Default/Squad'+ ',' + $('#selectTeamName option:selected').val();
			break;
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val() + ',' + $('#selectSubHeader option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LOF_CATRGORY':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectSubHeader option:selected').val();
			break;
		}
		break;

	case 'POPULATE-FLIPPER_SQUAD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;	
		case 'ISPL_VIZ': 
			valueToProcess = '/Default/Squad'+ ',' + $('#selectTeamName option:selected').val();
			break;
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val() + ',' + $('#selectcrawlerData option:selected').val();
			break;
		}
		break;	
	case 'POPULATE-SINGLE_PURSE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/RemainingPurse_Individual.sum' + ',' + $('#selectTeamName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/RemainingPurse_Individual.sum' + ',' + $('#selectTeamName option:selected').val();
			break;	
		}
		break;
		
	case 'POPULATE-LOF_REMAINING_PURSE':
		switch ($('#selected_broadcaster').val()){
			case 'VIZ_ISPL_2024': case 'PSL':
				valueToProcess = 'name';
				break;
			default://ISPL
				valueToProcess = $('#selectType option:selected').val();
				break;
			}
			break;
	case 'POPULATE-LOF_TEAM_TOP_SOLD': case 'POPULATE-FF_TOP_BUY_TEAM': case 'POPULATE-LOF_SQUAD': case 'POPULATE-TEAM_CURR_BID': 
	case "POPULATE-FF_SQUAD_TEAM":case "POPULATE-FF_SQUAD_ROLE_TEAM": case 'POPULATE-CRAWLER_TEAM_TOP_SOLD': case 'POPULATE-CRAWLE_SQUAD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':  case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-SQUAD_ANIMATION':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'KCL_BIGSCREEN': case 'UTT_BIGSCREEN': case 'MUMBAI_T20_BIGSCREEN':
			let team_index;
			if(which_GFX=== "POPULATE-SQUAD_ANIMATION"){
				team_index = id;
			}else{
				team_index =$('#selectTeamName option:selected').val();
			}
			valueToProcess = team_index + ',' + selectedSubHeader;
			break;	
		}
		break;
		
	case 'POPULATE-FF_FIVE_TOP_BUY_TEAM':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN':
			valueToProcess = $('#selectTeamName option:selected').val()+","+$('#selectImage option:selected').val();
			break;
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':  case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-FF_SINGLEPURSE_TEAM':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN':
			valueToProcess = $('#selectTeamName option:selected').val()+","+$('#selectImage option:selected').val();
			break;
		case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case "POPULATE-SQUAD-PLAYER":
		switch ($('#selected_broadcaster').val().toUpperCase()) {	
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
		valueToProcess = $('#selectImage option:selected').val();
		break;
	case "POPULATE-LOF_TEAM_BID_AUCTION":
			valueToProcess = selectedArray.join(",");
		break;
	case 'POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {	
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
		
	case 'POPULATE-TOP_SOLD_TEAM':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ISPL_VIZ':
			valueToProcess = '/Default/TeamTopBuys'+ ',' + $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-TOP_SOLD': 
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/Top_Buys.sum' ;
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/Top_Buys.sum' ;
			break;
		case 'ISPL_VIZ':
			valueToProcess = '/Default/TopBuys';
			break;	
		}
		break;
	case 'POPULATE-IDENT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ISPL_VIZ':
			valueToProcess = '/Default/Ident';
			break;	
		}
		break;
	case 'POPULATE-REMAINING_PURSE_ALL':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/RemainingPurse_All.sum' ;
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/RemainingPurse_All.sum' ;
			break;	
		case 'ISPL_VIZ':
			valueToProcess = '/Default/RemainingPurse' ;
			break;	
		}
		break;
	case 'POPULATE-GOOGLY_POWER':
		switch ($('#selected_broadcaster').val().toUpperCase()){
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-PROFILE_STATS': 
		switch ($('#selected_broadcaster').val().toUpperCase()){
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ':case 'MUMBAI_T20_BIGSCREEN':  case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectProfileStats option:selected').val() + ',' + $('#PlayerData option:selected').val();
			break;
		}
		break;
	case "POPULATE-PROFILE_STATS_CHANGE":
		switch ($('#selected_broadcaster').val().toUpperCase()){
		case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ':case 'MUMBAI_T20_BIGSCREEN':  case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
			valueToProcess = $('#selectShowProfileStats option:selected').val();
			break;
		}
		break;
	}

	$.ajax({    
        type : 'Get',     
        url : 'processAuctionProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + valueToProcess, 
        dataType : 'json',
        success : function(data) {
			//match_data = data;
			
        	switch(whatToProcess) {
			case 'GET-CONFIG-DATA':
				initialiseForm('UPDATE-CONFIG',data);
				break;
			case 'READ-MATCH-AND-POPULATE': case 'RE_READ_DATA':
				session_auction = data;
				if(data){
					initialiseForm("UPDATE-MATCH-ON-OUTPUT-FORM",data);
				}
				if(whatToProcess == 'RE_READ_DATA'){
					alert('Data is Loaded');
				}
				break;
			case "SQUAD_PLAYER_GRAPHICS-OPTIONS":
				addItemsToList('SQUAD_PLAYER-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'LOF_SQUAD_SIZE_CATEGORY_WISE_GRAPHICS-OPTIONS':
				addItemsToList('LOF_SQUAD_SIZE_CATEGORY_WISE_-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'NAMESUPER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-OPTIONS',data);
				match_data = data;
				break;
			case 'FLIPPER_GRAPHICS-OPTIONS':
				addItemsToList('FLIPPER-OPTIONS',data);
				match_data = data;
				break;
			case 'CRAWLERFREETEXT_GRAPHICS-OPTIONS':
				addItemsToList('FREETEXT-OPTIONS',data);
				match_data = data;
				break;
			case "FLIPPER_TEXT_GRAPHICS-OPTIONS":
				addItemsToList('FLIPPER_TEXT-OPTIONS',data);
				match_data = data;
				break;
			case 'NAMESUPER_PLAYER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER_PLAYER-OPTIONS',data);
				addItemsToList('POPULATE-PLAYER',data);
				match_data = data;
				break;
			case 'PLAYERPROFILE_MAIN_GRAPHICS-OPTIONS':
				addItemsToList('PLAYERPROFILE_MAIN-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case 'PLAYERPROFILE_GRAPHICS-OPTIONS': 
				addItemsToList('PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case 'FF_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('FF_PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case 'FF_BIG_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('FF_BIG_PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case 'PROFILE_GRAPHICS-OPTIONS':
				addItemsToList('PROFILEFF-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case "LT_PLAYERPROFILE_GRAPHICS-OPTIONS":
				addItemsToList('LT_PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case "ZONEWISE_PLAYER_SOLD_GRAPHICS-OPTIONS":	
				addItemsToList('ZONEWISE_PLAYER_SOLD-OPTIONS',data);
				break;
			case 'ZONE-PLAYER_GRAPHICS-OPTIONS':
				addItemsToList('ZONE-PLAYER-OPTIONS',data);
				break;
			case 'SQUAD_GRAPHICS-OPTIONS':  
				addItemsToList('SQUAD-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'TEAMS_GRAPHICS-OPTIONS':
				addItemsToList('TEAMS-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'RETAIN_PLAYERS_GRAPHICS-OPTIONS':
				addItemsToList('RETAIN_PLAYERS-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'FLIPPER_SQUAD_GRAPHICS-OPTIONS':
				addItemsToList('FLIPPER_SQUAD-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
				
			case 'TOP-SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('TOP-SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('LOF_TOP_SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'CRAWL_TOP_SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('CRAWL_TOP_SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'SQUAD_ANIMATION_GRAPHICS-OPTIONS':
				addItemsToList('SQUAD_ANIMATION-OPTIONS',data);
				addItemsToList('POPULATE-TEAM_ANIMATION',data);
				match_data = data;
				break;		
			case 'LOF_SQUAD_GRAPHICS-OPTIONS':
				addItemsToList('LOF_SQUAD-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'CRAWL_SQUAD_GRAPHICS-OPTIONS':
				addItemsToList('CRAWL_SQUAD-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
				
			case 'SINGLE_PURSE_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE_PURSE-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'GOOGLY_GRAPHICS-OPTIONS':
				addItemsToList('GOOGLY-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'FF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('FF_TOP_SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case "FF_SQUAD_GRAPHICS-OPTIONS":
				addItemsToList('FF_SQUAD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case "FF_SQUAD_ROLE_GRAPHICS-OPTIONS":
				addItemsToList('FF_SQUAD_ROLE_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case "LOF_TEAM_BID_GRAPHICS-OPTIONS":
				addItemsToList('LOF_TEAM_BID_OPTIONS',data);
				match_data = data;
				break;
			case 'FF_TOP_FIVE_SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('FF_TOP_FIVE_SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'SINGLE_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'TEAM_CURRENT_BID_GRAPHICS-OPTIONS':
				addItemsToList('TEAM_CURRENT_BID-OPTIONS',null);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'POPULATE-SQUAD_ANIMATION': 
				if (which_GFX == 'POPULATE-SQUAD_ANIMATION') {
					processAuctionProcedures('ANIMATE-IN-SQUAD_ANIMATION');
				}else{
				  if(confirm('Animate In?') == true){
					$('#select_graphic_options_div').empty();
					document.getElementById('select_graphic_options_div').style.display = 'none';
					$("#captions_div").show();
					$("#auction_div").show();
					
					processAuctionProcedures('ANIMATE-IN-SQUAD_ANIMATION');
					which_GFX = 'POPULATE-SQUAD_ANIMATION';
					startTeamRotation(); 
				}
			}
			break;
			case 'POPULATE-SQUAD': case 'POPULATE-LOF_SQUAD': case 'POPULATE-ZONE_PLAYERS_STATS': case 'POPULATE-FF_FIVE_TOP_BUY_TEAM':
			case 'POPULATE-FF_TOP_BUY_TEAM': case 'POPULATE-FF_RETAIN_PLAYERS': case "POPULATE-FF_SQUAD_ROLE_TEAM":
				if(confirm('Animate In?') == true){
					switch(whatToProcess){
					case 'POPULATE-SQUAD':
						processAuctionProcedures('ANIMATE-IN-SQUAD');
						break;
					case 'POPULATE-LOF_SQUAD':
						processAuctionProcedures('ANIMATE-IN-LOF_SQUAD');
						break;
					case 'POPULATE-ZONE_PLAYERS_STATS':
						processAuctionProcedures('ANIMATE-IN-ZONE-PLAYER_STATS');
						break;
					case 'POPULATE-FF_FIVE_TOP_BUY_TEAM':
						processAuctionProcedures('ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM');
						break;
					case 'POPULATE-FF_TOP_BUY_TEAM':
						processAuctionProcedures('ANIMATE-IN-FF_TOP_BUY_TEAM');
						break;
					case 'POPULATE-FF_RETAIN_PLAYERS':
						processAuctionProcedures('ANIMATE-IN-FF_RETAIN_PLAYERS');
						break;
					case "POPULATE-FF_SQUAD_ROLE_TEAM":
						processAuctionProcedures('ANIMATE-IN-FF_SQUAD_ROLE_TEAM');
						break;
					}	
				}		
				break;
			
			case 'POPULATE-FF-PLAYERPROFILE': case 'POPULATE-REMAINING_PURSE_ALL': case 'POPULATE-SINGLE_PURSE': case 'POPULATE-TOP_SOLD': case 'POPULATE-L3-NAMESUPER': 
			case 'POPULATE-TOP_SOLD_TEAM': case 'POPULATE-IDENT': case 'POPULATE-FLIPPER_SQUAD': case 'POPULATE-TEAMS': case 'POPULATE-TEAMS_DETAILS': case 'POPULATE-CURR_BID': 
			case 'POPULATE-RTM_AVAILABLE': case 'POPULATE-RTM_PLAYER': case "POPULATE-PROFILE_STATS_CHANGE": case 'POPULATE-RTM_ENABLED': case 'POPULATE-GOOGLY_POWER': 
			case 'POPULATE-PROFILE_STATS': case 'POPULATE-LOF_REMAINING_PURSE': case 'POPULATE-LOF_TOP_SOLD': case 'POPULATE-LOF_TEAM_TOP_SOLD': 
			case 'POPULATE-CRAWLER_TEAM_TOP_SOLD': case "POPULATE-SQUAD-PLAYER": case 'POPULATE-PLAYERPROFILE_FF': case 'POPULATE-PROFILE_FF': 
			case 'POPULATE-PLAYERPROFILE_FF_BIG': case 'POPULATE-LOF_REMAINING_SLOT': case 'POPULATE-LOF_SQUAD_SIZE': case 'POPULATE-LOF_RTM_REMAINING': 
			case 'POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE': case 'POPULATE-CRAWLE_SQUAD': case 'POPULATE-LOF_SQUAD_REMAIN': case 'POPULATE-L3-CRWLERFREETEXT': 
			case 'POPULATE-L3-FLIPPER': case 'POPULATE-L3-FLIPPER_TEXT':
			
			case 'POPULATE-FF_RTM_AND_PURSE_REMAINING': case 'POPULATE-FF_TOP_BUYS_AUCTION': case 'POPULATE-TEAM_CURR_BID': case 'POPULATE-FF_ICONIC_PLAYERS': 
			case 'POPULATE-FF_FIVE_TOP_BUYS_AUCTION': case 'POPULATE-FF_SINGLEPURSE_TEAM': case "POPULATE-FF_SQUAD_TEAM": case 'POPULATE-LT_ICONIC_PLAYERS': 
			case 'POPULATE-PLAYERPROFILE_LT': case 'POPULATE-PLAYERPROFILE_LT_STATS': case 'POPULATE-ZONEWISE_PLAYERS_SOLD': case "POPULATE-LOF_TEAM_BID_AUCTION": 
			case 'POPULATE-ZONE_PLAYERS_FULL':case 'POPULATE-LOF_CATRGORY':
			
			case 'POPULATE-CRAWL-PURSE_REMAINING': case 'POPULATE-CRAWL-SQUAD_SIZE': case'POPULATE-CRAWL_TOP_SOLD':
			case 'POPULATE-PURSE_SIZE_ALL': case 'POPULATE-PURSE_SLOT_ALL':
			
				if(whatToProcess == 'POPULATE-RTM_ENABLED' || whatToProcess == 'POPULATE-CURR_BID' || whatToProcess == 'POPULATE-RTM_PLAYER')	{
					switch(whatToProcess){
						case 'POPULATE-RTM_ENABLED':
							processAuctionProcedures('ANIMATE-IN-RTM_ENABLED');
							break;
						case 'POPULATE-CURR_BID':
							processAuctionProcedures('ANIMATE-IN-CURR_BID');
							break;
						case 'POPULATE-RTM_PLAYER':
							processAuctionProcedures('ANIMATE-IN-RTM_PLAYER');
							break;
					}
				}else if(whatToProcess == 'POPULATE-PROFILE_STATS' || 
						whatToProcess == 'POPULATE-SQUAD-PLAYER' || 
						whatToProcess == 'POPULATE-LOF_TEAM_BID_AUCTION')	{
					if(confirm('Animate In?') == true){
						switch(whatToProcess) {
						case 'POPULATE-PROFILE_STATS':
							processAuctionProcedures('ANIMATE-IN-PROFILE_STATS');
							break;
						case "POPULATE-SQUAD-PLAYER":
							processAuctionProcedures('ANIMATE-IN-SQUAD-PLAYER');
							break;
						case "POPULATE-LOF_TEAM_BID_AUCTION":
							processAuctionProcedures('ANIMATE-IN-LOF_TEAM_BID_AUCTION');
							break;		
						}
					}
				}else{
					if(confirm('Animate In?') == true){
						$('#select_graphic_options_div').empty();
						document.getElementById('select_graphic_options_div').style.display = 'none';
						$("#captions_div").show();
						$("#auction_div").show();
						
			        	switch(whatToProcess) {
						case 'POPULATE-IDENT':
							processAuctionProcedures('ANIMATE-IN-IDENT');
							break;
						case 'POPULATE-TEAMS_DETAILS':
							processAuctionProcedures('ANIMATE-IN-TEAMS_DETAILS');
							break;
						case 'POPULATE-L3-NAMESUPER':
							processAuctionProcedures('ANIMATE-IN-NAMESUPER');	
							break;
						case 'POPULATE-L3-FLIPPER':
							processAuctionProcedures('ANIMATE-IN-FLIPPER');	
							break;
						case 'POPULATE-L3-CRWLERFREETEXT':
							processAuctionProcedures('ANIMATE-IN-FREEETEXTCRAWLER');	
							break;
						case 'POPULATE-PURSE_SIZE_ALL': 
							processAuctionProcedures('ANIMATE-IN-PURSE_SIZE_ALL');	
							break;
						case 'POPULATE-PURSE_SLOT_ALL':
							processAuctionProcedures('ANIMATE-IN-PURSE_SLOT_ALL');	
							break;
						case 'POPULATE-L3-FLIPPER_TEXT':
							processAuctionProcedures('ANIMATE-IN-FLIPPER_TEXT');	
							break;
						case 'POPULATE-LOF_CATRGORY':
							processAuctionProcedures('ANIMATE-IN-LOF_CATEGORYS');	
							break;
						case 'POPULATE-TOP_SOLD': 
							processAuctionProcedures('ANIMATE-IN-TOP_SOLD');				
							break;
						case 'POPULATE-TOP_SOLD_TEAM':
							processAuctionProcedures('ANIMATE-IN-TOP_SOLD_TEAM');				
							break;
						case 'POPULATE-REMAINING_PURSE_ALL':
							processAuctionProcedures('ANIMATE-IN-REMAINING_PURSE_ALL');				
							break;
						case 'POPULATE-TEAMS':
							processAuctionProcedures('ANIMATE-IN-TEAMS');				
							break;
						case 'POPULATE-FLIPPER_SQUAD':
							processAuctionProcedures('ANIMATE-IN-FLIPPER_SQUAD');				
							break;
						case 'POPULATE-FF-PLAYERPROFILE':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE');				
							break;
						case 'POPULATE-SINGLE_PURSE':
							processAuctionProcedures('ANIMATE-IN-SINGLE_PURSE');				
							break;
						case 'POPULATE-RTM_AVAILABLE':
							processAuctionProcedures('ANIMATE-IN-RTM_AVAILABLE');
							break;
						case 'POPULATE-GOOGLY_POWER':
							processAuctionProcedures('ANIMATE-IN-GOOGLY_POWER');
							break;
						case "POPULATE-PROFILE_STATS_CHANGE":
							 processAuctionProcedures('ANIMATE-IN-PROFILE_STATS_CHANGE');
							 break;
						case 'POPULATE-LOF_REMAINING_PURSE':
							processAuctionProcedures('ANIMATE-IN-LOF_REMAINING_PURSE');				
							break;
							
						case 'POPULATE-CRAWL-PURSE_REMAINING':
							processAuctionProcedures('ANIMATE-IN-CRAWL_REMAINING_PURSE');				
							break;
						case'POPULATE-CRAWL_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-CRAWL_TOP_SOLD');				
							break;	
						case 'POPULATE-CRAWL-SQUAD_SIZE':
							processAuctionProcedures('ANIMATE-IN-CRAWL_SQUAD_SIZE');				
							break;
							
						case 'POPULATE-LOF_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-LOF_TOP_SOLD');				
							break;
						case 'POPULATE-LOF_TEAM_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-LOF_TEAM_TOP_SOLD');				
							break;
						case 'POPULATE-CRAWLER_TEAM_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-CRAWLER_TEAM_TOP_SOLD');				
							break;
						case 'POPULATE-CRAWLE_SQUAD':	
							processAuctionProcedures('ANIMATE-IN-CRAWL_SQUAD');				
							break;
						case 'POPULATE-LOF_SQUAD_REMAIN':
							processAuctionProcedures('ANIMATE-IN-LOF_SQUAD_REMAIN');
							break;
						
						case 'POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE':
							processAuctionProcedures('ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE');
							break;
						case 'POPULATE-PLAYERPROFILE_FF':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE_FF');
							break;
						case 'POPULATE-PLAYERPROFILE_FF_BIG':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE_FF_BIG');
							break;
						case 'POPULATE-PROFILE_FF':
							processAuctionProcedures('ANIMATE-IN-PROFILE_FF');
							break;
						case 'POPULATE-LOF_REMAINING_SLOT':
							processAuctionProcedures('ANIMATE-IN-LOF_REMAINING_SLOT');
							break;
						case 'POPULATE-LOF_SQUAD_SIZE':
							processAuctionProcedures('ANIMATE-IN-LOF_SQUAD_SIZE');
							break;
						case 'POPULATE-LOF_RTM_REMAINING':
							processAuctionProcedures('ANIMATE-IN-LOF_RTM_REMAINING');
							break;
						case 'POPULATE-FF_RTM_AND_PURSE_REMAINING':
							processAuctionProcedures('ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING');
							break;
						case 'POPULATE-FF_TOP_BUYS_AUCTION':
							processAuctionProcedures('ANIMATE-IN-FF_TOP_BUYS_AUCTION');
							break;
						case 'POPULATE-FF_FIVE_TOP_BUYS_AUCTION':
							processAuctionProcedures('ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION');
							break;
						case "POPULATE-LOF_TEAM_BID_AUCTION":
							processAuctionProcedures('ANIMATE-IN-LOF_TEAM_BID_AUCTION');
						case 'POPULATE-FF_TOP_BUY_TEAM':
							processAuctionProcedures('ANIMATE-IN-FF_TOP_BUY_TEAM');
							break;
						case "POPULATE-FF_SQUAD_TEAM":
							processAuctionProcedures('ANIMATE-IN-FF_SQUAD_TEAM');
							break;
						case 'POPULATE-FF_SINGLEPURSE_TEAM':
							processAuctionProcedures('ANIMATE-SINGLEPURSE_TEAM');
							break;	
						case 'POPULATE-FF_ICONIC_PLAYERS':
							processAuctionProcedures('ANIMATE-IN-FF_ICONIC_PLAYERS');
							break;
						case 'POPULATE-LT_ICONIC_PLAYERS':
							processAuctionProcedures('ANIMATE-IN-LT_ICONIC_PLAYERS');
							break;
						case 'POPULATE-PLAYERPROFILE_LT':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE_LT');
							break;
						case 'POPULATE-PLAYERPROFILE_LT_STATS':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE_LT_STATS');
							break;
						case 'POPULATE-ZONE_PLAYERS_FULL':
							processAuctionProcedures('ANIMATE-IN-ZONE-PLAYER_FULL');
							break;
						case 'POPULATE-TEAM_CURR_BID':
							processAuctionProcedures('ANIMATE-IN-TEAM_CURR_BID');
							break;
						case 'POPULATE-ZONEWISE_PLAYERS_SOLD':
							processAuctionProcedures('ANIMATE-IN-ZONEWISE_PLAYERS_SOLD');
							break;
						}
					}
				}
				break;
        	}
			processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var select,option,header_text,div,table,tbody,row,max_cols;
	var cellCount = 0;

	switch (whatToProcess) {
	case 'POPULATE-TEAM_ANIMATION':
		$('#selectTeamName').empty();
		
		teams = dataToProcess.map(function(tm) {
            return { teamId: tm.teamId, teamName: tm.teamName1 };
        });
        
		dataToProcess.forEach(function(tm,index,arr1){
			$('#selectTeamName').append(
					$(document.createElement('option')).prop({
					value: tm.teamId,
					text: tm.teamName1
				}))
		});
		
		break;
	case 'POPULATE-TEAM':
		$('#selectTeamName').empty();
		
		dataToProcess.forEach(function(tm,index,arr1){
			$('#selectTeamName').append(
					$(document.createElement('option')).prop({
					value: tm.teamId,
					text: tm.teamName1
				}))
		});
		
		break;
	case 'POPULATE-PROFILE' :
		$('#selectPlayerName').empty();
		session_auction.playersList.forEach(function(plyr,index,arr1){
			if(plyr.playerId == session_auction.players[session_auction.players.length- 1].playerId){
				$('#selectPlayerName').append(
					$(document.createElement('option')).prop({
					value: plyr.playerId,
					text: plyr.playerNumber + ' - ' + plyr.full_name + ' - ' + plyr.category + ' - ' + plyr.role
				}))
			}
		});
		
		
		dataToProcess.forEach(function(plyr,index,arr1){
			if(plyr.playerId != session_auction.players[session_auction.players.length - 1].playerId){
				$('#selectPlayerName').append(
					$(document.createElement('option')).prop({
					value: plyr.playerId,
					text: plyr.playerNumber + ' - ' + plyr.full_name + ' - ' + plyr.category + ' - ' + plyr.role
				}))
			}
		});
		
		break;
		
	case 'POPULATE-PLAYER':
		$('#selectPlayer').empty();
		if(dataToProcess.homeTeamId ==  $('#selectTeam option:selected').val()){
			dataToProcess.homeSquad.forEach(function(hs,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: hs.playerId,
	                text: hs.full_name
		        }))					
			});
		}
		else {
			dataToProcess.awaySquad.forEach(function(as,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: as.playerId,
	                text: as.full_name
		        }))					
			});
		}
		
		break;
		
	case'NAMESUPER-OPTIONS': case 'NAMESUPER_PLAYER-OPTIONS':  case'PLAYERPROFILE-OPTIONS': case 'SQUADCATEGORIES-OPTIONS': case 'SQUAD-OPTIONS': case 'SINGLE_PURSE-OPTIONS': 
	case 'PLAYERPROFILE_MAIN-OPTIONS': case 'RETAIN_PLAYERS-OPTIONS': case 'TEAMS-OPTIONS':
	case 'TOP-SOLD_TEAM-OPTIONS': case 'GOOGLY-OPTIONS': case 'PROFILE_STATS-OPTIONS': case 'LOF_REMAINING_PURSE-OPTIONS': case 'LOF_TOP_SOLD_TEAM-OPTIONS': 
	case'SQUAD_PLAYER-OPTIONS': case 'FF_PLAYERPROFILE-OPTIONS': case 'FF_TOP_SOLD_TEAM-OPTIONS': case 'LOF_SQUAD_SIZE_CATEGORY_WISE_-OPTIONS': 
	case 'LT_PLAYERPROFILE-OPTIONS': case 'LT_PP_STATS-OPTIONS': case 'LOF_SQUAD-OPTIONS': case 'FLIPPER-OPTIONS': case 'FREETEXT-OPTIONS': case "ZONE-PLAYER-OPTIONS":
	case 'TEAM_CURRENT_BID-OPTIONS': case 'FF_TOP_FIVE_SOLD_TEAM-OPTIONS': case 'SINGLE_TEAM-OPTIONS':  case 'ZONEWISE_PLAYER_SOLD-OPTIONS':case "GRAPHICS-FF_FIVE_TOP_BUYS_AUCTION":
	case 'PROFILE_FF_STATS-OPTIONS': case 'FLIPPER_SQUAD-OPTIONS':case "FF_SQUAD_TEAM-OPTIONS":case "FF_SQUAD_ROLE_TEAM-OPTIONS":case "LOF_TEAM_BID_OPTIONS": 
	case 'PROFILEFF-OPTIONS': case 'FLIPPER_TEXT-OPTIONS': case 'CRAWL_TOP_SOLD_TEAM-OPTIONS': case 'CRAWL_SQUAD-OPTIONS': case 'ZONE-PLAYER_FULL-OPTIONS': case 'SQUAD_ANIMATION-OPTIONS':
	case 'FF_BIG_PLAYERPROFILE-OPTIONS':	
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'PSL':
		case 'UTT_BIGSCREEN': case "UTT_VIZ": case 'MUMBAI_T20_VIZ':  case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
 			
 			/*if(whatToProcess != 'PLAYERPROFILE-OPTIONS'){
				$("#select_graphic_options_div").empty();
				document.getElementById('select_graphic_options_div').style.display = 'none';
				$("#captions_div").show();
			}*/
			
			$('#select_graphic_options_div').empty();
			header_text = document.createElement('h6');

        	let title = whatToProcess
            	.replace('-OPTIONS', '')     // remove suffix
            	.replace(/[-_]+/g, ' ')     // replace - and _ with space
            	.trim();
        	header_text.innerHTML = title;
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case "LOF_TEAM_BID_OPTIONS":
				
					selectedArray = [];
					
					select = document.createElement('select');
					select.id = 'selectTeamName';
					select.name = select.id;
					select.multiple = true; 
					select.style.width = 'auto';
					select.style.height = '200px';
					
					dataToProcess.forEach(function(flip){
						option = document.createElement('option');
						option.value = flip.teamId;
						option.text = flip.teamName1;
						select.appendChild(option);
					});
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					$('#selectTeamName').on('change', function() { 
						const $options = $(this).find('option');
					    $options.each(function() {
					        if ($(this).is(':selected') && !selectedArray.includes($(this).val())) {
					            selectedArray.push($(this).val());
					        } else if (!$(this).is(':selected') && selectedArray.includes($(this).val())) {
					            selectedArray = selectedArray.filter(item => item !== $(this).val());
					        }
					    });	
					  }).trigger('change');
				break;
				case "GRAPHICS-FF_FIVE_TOP_BUYS_AUCTION":
					select = document.createElement('select');
					select.style = 'width:100px';
					select.id = 'selectImage';
					select.name = select.id;
					["WithImage","WithoutImage"].forEach(function(flip){
							option = document.createElement('option');
							option.value = flip;
							option.text = flip;
							select.appendChild(option);
						});
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
				break;
				case 'ZONE-PLAYER-OPTIONS': case 'ZONEWISE_PLAYER_SOLD-OPTIONS': case 'ZONE-PLAYER_FULL-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:100px';
					select.id = 'selectShowData';
					select.name = select.id;
					
					switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'UTT_VIZ': case'UTT_BIGSCREEN':
							option = document.createElement('option');
							option.value = 'IND.M';
							option.text = 'Indian Male';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'IND.F';
							option.text = 'Indian Female';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'INT.M';
							option.text = 'Foreign Male';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'INT.F';
							option.text = 'Foreign Female';
							select.appendChild(option);
							break;
						case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
							
							option = document.createElement('option');
							option.value = 'A+';
							option.text = 'A+';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'A';
							option.text = 'A';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'B';
							option.text = 'B';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'C';
							option.text = 'C';
							select.appendChild(option);
							break;
						case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'PSL':
							option = document.createElement('option');
							option.value = 'EAST ZONE';
							option.text = 'EAST ZONE';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'WEST ZONE';
							option.text = 'WEST ZONE';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'NORTH ZONE';
							option.text = 'NORTH ZONE';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'SOUTH ZONE';
							option.text = 'SOUTH ZONE';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'CENTRAL ZONE';
							option.text = 'CENTRAL ZONE';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'U19';
							option.text = 'UNDER 19';
							select.appendChild(option);
							break;
						default:
							dataToProcess.forEach(function(flip){
								option = document.createElement('option');
								option.value = flip;
								option.text = flip;
								select.appendChild(option);
							});
							break;
						}
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
					break;
				case 'SINGLE_PURSE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'HANDBALL': case 'ISPL':
						select = document.createElement('select');
						select.id = 'selectTeamName';
						select.name = select.id;
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						break;
					}
					break;
				case 'SQUADCATEGORIES-OPTIONS':	
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectSubHeader';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'A_EXTRA';
					option.text = 'A+';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'A';
					option.text = 'A';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'B';
					option.text = 'B';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'C';
					option.text = 'C';
					select.appendChild(option);
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SQUAD-OPTIONS': case 'TOP-SOLD_TEAM-OPTIONS': case 'GOOGLY-OPTIONS': case 'CRAWL_TOP_SOLD_TEAM-OPTIONS': case 'LOF_TOP_SOLD_TEAM-OPTIONS': case'SQUAD_PLAYER-OPTIONS':
				case 'FF_TOP_SOLD_TEAM-OPTIONS': case 'LOF_SQUAD_SIZE_CATEGORY_WISE_-OPTIONS': case 'LOF_SQUAD-OPTIONS': case 'CRAWL_SQUAD-OPTIONS': case 'TEAM_CURRENT_BID-OPTIONS':
				case 'FF_TOP_FIVE_SOLD_TEAM-OPTIONS': case 'SINGLE_TEAM-OPTIONS': case 'FLIPPER_SQUAD-OPTIONS':case "FF_SQUAD_TEAM-OPTIONS":case "FF_SQUAD_ROLE_TEAM-OPTIONS":
				case 'SQUAD_ANIMATION-OPTIONS': case 'RETAIN_PLAYERS-OPTIONS': case 'TEAMS-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'UTT_BIGSCREEN':  case 'PSL': case "UTT_VIZ": case 'MUMBAI_T20_BIGSCREEN': case 'MUMBAI_T20_VIZ':  case 'KCL': case 'PWL': case 'KCL_BIGSCREEN':
							select = document.createElement('select');
							select.id = 'selectTeamName';
							select.name = select.id;
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							if(whatToProcess ==='FF_TOP_FIVE_SOLD_TEAM-OPTIONS'){
								switch ($('#selected_broadcaster').val().toUpperCase()){
									 case "UTT_VIZ": case 'UTT_BIGSCREEN':
										select = document.createElement('select');
										select.style = 'width:100px';
										select.id = 'selectImage';
										select.name = select.id;
										["WithImage"].forEach(function(flip){
												option = document.createElement('option');
												option.value = flip;
												option.text = flip;
												select.appendChild(option);
											});
										select.setAttribute('onchange',"processUserSelection(this)");
										row.insertCell(cellCount).appendChild(select);
										cellCount = cellCount + 1;
										break;
								}	
							}else if(whatToProcess ==='FLIPPER_SQUAD-OPTIONS'){
								select = document.createElement('select');
								select.style = 'width:130px';
								select.id = 'selectcrawlerData';
								select.name = select.id;
								
								option = document.createElement('option');
								option.value = 'squad';
								option.text = 'Squad';
								select.appendChild(option);
								
								option = document.createElement('option');
								option.value = 'top_buys';
								option.text = 'Top 5 Buys';
								select.appendChild(option);
								
								row.insertCell(cellCount).appendChild(select);
								cellCount = cellCount + 1;
							}
							else if(whatToProcess === 'SQUAD-OPTIONS' || whatToProcess === 'SQUAD_ANIMATION-OPTIONS'){
								switch ($('#selected_broadcaster').val().toUpperCase()){
								case 'PWL':
									select = document.createElement('select');
									select.style = 'width:130px';
									select.id = 'selectSubHeader';
									select.name = select.id;
									
									option = document.createElement('option');
									option.value = 'PurseRemaining';
									option.text = 'PURSE REM';
									select.appendChild(option);
									
									option = document.createElement('option');
									option.value = 'Squad';
									option.text = 'SQUAD';
									select.appendChild(option);
									
									row.insertCell(cellCount).appendChild(select);
									cellCount = cellCount + 1;
									break;
								}
							}
							break;
						} 
						break;
				case 'FLIPPER_TEXT-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()){
						case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
							select = document.createElement('select');
							select.style = 'width:130px';
							select.id = 'selectFlipper';
							select.name = select.id;
							
							dataToProcess.forEach(function(flip,index,arr1){
								option = document.createElement('option');
								option.value = flip.flipperId;
								option.text = flip.prompt ;
								select.appendChild(option);
							});
							
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							break;
					}
					break;
				case 'FLIPPER-OPTIONS': case 'FREETEXT-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'DOAD_IN_HOUSE_EVEREST': case 'DOAD_IN_HOUSE_VIZ': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'PSL':
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectFlipper';
						select.name = select.id;
						
						dataToProcess.forEach(function(flip,index,arr1){
							option = document.createElement('option');
							option.value = flip.flipperId;
							option.text = flip.prompt ;
							select.appendChild(option);
						});
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						break;
						
					case "UTT_VIZ":  case 'UTT_BIGSCREEN': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectFlipper';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'top_buys';
						option.text = 'Top Buys';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'remain_purse';
						option.text = 'Purse Remaining';
						select.appendChild(option);
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						break;
					}
					break;
				case'NAMESUPER-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'DOAD_IN_HOUSE_EVEREST': case 'DOAD_IN_HOUSE_VIZ': case 'ISPL_VIZ':  case 'UTT_BIGSCREEN': case 'VIZ_ISPL_2024': case 'PSL': case "UTT_VIZ": case 'MUMBAI_T20_BIGSCREEN': case 'MUMBAI_T20_VIZ': 
				    case 'KCL': case 'KCL_BIGSCREEN': case 'PWL':
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectNameSuper';
						select.name = select.id;
						
						dataToProcess.forEach(function(ns,index,arr1){
							option = document.createElement('option');
							option.value = ns.namesuperId;
							option.text = ns.firstname ;
							select.appendChild(option);
						});
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						select = document.createElement('select');
						select.id = 'selectLogo';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'WITHOUT_LOGO';
						option.text = 'WITHOUT LOGO';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'WITH_LOGO';
						option.text = 'WITH LOGO';
						select.appendChild(option);
					
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'DOAD_IN_HOUSE_EVEREST': 
							select = document.createElement('input');
							select.type = "text";
							select.id = 'namesuperScene';
							select.value = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/LT_NameSuper.sum';
							
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							
							break;
						}
						break;
					}
					break;
				
			case 'NAMESUPER_PLAYER-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'DOAD_IN_HOUSE_EVEREST': case 'DOAD_IN_HOUSE_VIZ':
						select = document.createElement('select');
						select.id = 'selectTeam';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = dataToProcess.homeTeamId;
						option.text = dataToProcess.homeTeam.shortname;
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = dataToProcess.awayTeamId;
						option.text = dataToProcess.awayTeam.shortname;
						select.appendChild(option);
					
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
		
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectCaptainWicketKeeper';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'Captain';
						option.text = 'Captain';
						select.appendChild(option);
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectPlayer';
						select.name = select.id;
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						switch ($('#selected_broadcaster').val().toUpperCase()) {
							case 'DOAD_IN_HOUSE_EVEREST': 
								select = document.createElement('input');
								select.type = "text";
								select.id = 'namesuperplayerScene';
								select.value = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/LT_NameSuper.sum';
								
								row.insertCell(cellCount).appendChild(select);
								cellCount = cellCount + 1;
								
								break;
						}
						break;
				}
				break;
			case 'PLAYERPROFILE_MAIN-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'PSL': case 'UTT_BIGSCREEN':  case "UTT_VIZ": case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'PWL': case 'KCL_BIGSCREEN':
						select = document.createElement('select');
						select.id = 'selectPlayerName';
						select.name = select.id;
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						$(select).select2();
						cellCount = cellCount + 1;
						
						if($('#selected_broadcaster').val().toUpperCase()==='UTT_VIZ' || $('#selected_broadcaster').val().toUpperCase()==='UTT_BIGSCREEN'){
							
							select = document.createElement('select');
							select.style = 'width:150px';
							select.id = 'selectShowData';
							select.name = select.id;
							
							option = document.createElement('option');
							option.value = 'With_Photo';
							option.text = 'With Photo';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'WithOut_Photo';
							option.text = 'WithOut Photo';
							select.appendChild(option);
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
						}
						
						if($('#selected_broadcaster').val().toUpperCase()==='UTT_VIZ'){
							
							select = document.createElement('select');
							select.style = 'width:150px';
							select.id = 'selectBottomData';
							select.name = select.id;
							
							option = document.createElement('option');
							option.value = 'category';
							option.text = 'category';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'Rank';
							option.text = 'Rank';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'Style';
							option.text = 'Style';
							select.appendChild(option);
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
						}
			          break;
	                  	} 
	                break;	
			case 'FF_PLAYERPROFILE-OPTIONS': case 'LT_PLAYERPROFILE-OPTIONS': case 'PROFILEFF-OPTIONS': case 'PLAYERPROFILE-OPTIONS': case 'FF_BIG_PLAYERPROFILE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'PSL': case 'UTT_BIGSCREEN': case "UTT_VIZ": case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL':  case 'PWL': case 'KCL_BIGSCREEN':
						select = document.createElement('select');
						select.id = 'selectPlayerName';
						select.name = select.id;
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						$(select).select2();
						cellCount = cellCount + 1;
						
						if($('#selected_broadcaster').val().toUpperCase()==='UTT_VIZ' || $('#selected_broadcaster').val().toUpperCase()==='UTT_BIGSCREEN'){
							
							select = document.createElement('select');
							select.style = 'width:150px';
							select.id = 'selectShowData';
							select.name = select.id;
							
							option = document.createElement('option');
							option.value = 'With_Photo';
							option.text = 'With Photo';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'WithOut_Photo';
							option.text = 'WithOut Photo';
							select.appendChild(option);
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
						}else if(whatToProcess.toUpperCase()==='LT_PLAYERPROFILE-OPTIONS'){
							
							switch ($('#selected_broadcaster').val().toUpperCase()){
							case 'PSL':
								break;
							default:
								select = document.createElement('select');
								select.style = 'width:150px';
								select.id = 'selectLogo';
								select.name = select.id;
								
								option = document.createElement('option');
								option.value = 'WITH_LOGO';
								option.text = 'WITH LOGO';
								select.appendChild(option);
								
								option = document.createElement('option');
								option.value = 'WITHOUT_LOGO';
								option.text = 'WITHOUT LOGO';
								select.appendChild(option);
								
								select.setAttribute('onchange',"processUserSelection(this)");
								row.insertCell(cellCount).appendChild(select);
								cellCount = cellCount + 1;
								break;
							}
						}
						/*else if(whatToProcess == 'PLAYERPROFILE-OPTIONS' && $('#selected_broadcaster').val().toUpperCase()==='MUMBAI_T20_VIZ'){
							
							select = document.createElement('select');
							select.style = 'width:150px';
							select.id = 'selectShowData';
							select.name = select.id;
							
							option = document.createElement('option');
							option.value = 'With_Photo';
							option.text = 'With Photo';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'WithOut_Photo';
							option.text = 'WithOut Photo';
							select.appendChild(option);
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
						}*/
						switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'UTT_BIGSCREEN': case 'PSL': case "UTT_VIZ": case 'MUMBAI_T20_BIGSCREEN': case 'MUMBAI_T20_VIZ': 
						       switch(whatToProcess){
								case 'FF_PLAYERPROFILE-OPTIONS': case 'PROFILEFF-OPTIONS':
									select = document.createElement('select');
									select.style = 'width:100px';
									select.id = 'selectShowProfileStats';
									select.name = select.id;
									
									switch ($('#selected_broadcaster').val().toUpperCase()){
									case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
										option = document.createElement('option');
										option.value = 'ISPL S-1';
										option.text = 'ISPL S-1';
										select.appendChild(option);
										
										option = document.createElement('option');
										option.value = 'ISPL S-2';
										option.text = 'ISPL S-2';
										select.appendChild(option);
										
										select.setAttribute('onchange',"processUserSelection(this)");
										row.insertCell(cellCount).appendChild(select);
										cellCount = cellCount + 1;
										break;
									case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
										option = document.createElement('option');
										option.value = 'without';
										option.text = 'WithOut Stats';
										select.appendChild(option);
										
										option = document.createElement('option');
										option.value = 'with_info';
										option.text = 'With Stats';
										select.appendChild(option);
									
										select.setAttribute('onchange',"processUserSelection(this)");
										row.insertCell(cellCount).appendChild(select);
										cellCount = cellCount + 1;
										break;
									default:
										option = document.createElement('option');
										option.value = 'with_info';
										option.text = 'Category';
										select.appendChild(option);
										
										option = document.createElement('option');
										option.value = 'with_data';
										option.text = 'Rank & Style';
										select.appendChild(option);
									
										option = document.createElement('option');
										option.value = 'without';
										option.text = 'WithOut Stats';
										select.appendChild(option);
										
										select.setAttribute('onchange',"processUserSelection(this)");
										row.insertCell(cellCount).appendChild(select);
										cellCount = cellCount + 1;
										break;
									}
									
									switch ($('#selected_broadcaster').val().toUpperCase()){
									case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024': case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
										// Second cell: reserved for conditional dropdown
									    const seCell = row.insertCell(cellCount);
									    seCell.id = 'Playerstats'; // So we can target it easily later
									    cellCount++;
									
									    // Add event listener to the main dropdown
									    $(select).on('change', function () {
									        const selectedValue = this.value;
									        const container = document.getElementById('Playerstats');
									
									        // Remove old dropdown if it exists
									        const existing = document.getElementById('PlayerData');
									        if (existing) existing.remove();
									
									        // Show second dropdown only if "stats" is selected
									        if (selectedValue === 'with_info') {
									            const statsDropdown = document.createElement('select');
									            statsDropdown.id = 'PlayerData';
									            statsDropdown.name = 'PlayerData';
									            
									            switch ($('#selected_broadcaster').val().toUpperCase()){
												case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
													['ISPL S-1', 'ISPL S-2'].forEach(value => {
										                const option = document.createElement('option');
										                option.value = value;
										                option.text = value;
										                option.style.fontWeight = 'bold';
										                statsDropdown.appendChild(option);
										            });
													break;
												case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN':
													['MCA T20s', 'DT20', 'FC', 'LIST A'].forEach(value => {
										                const option = document.createElement('option');
										                option.value = value;
										                option.text = value;
										                option.style.fontWeight = 'bold';
										                statsDropdown.appendChild(option);
										            });
													break;
												}
									            
									            container.appendChild(statsDropdown);
									        }
									    });							
									    $(select).trigger('change');
										break;
									}
									break;
								case 'LT_PLAYERPROFILE-OPTIONS':
									select = document.createElement('select');
									select.style = 'width:100px';
									select.id = 'selectShowData';
									select.name = select.id;
									
									const broadcaster = $('#selected_broadcaster').val().toUpperCase();
								    if(broadcaster === 'VIZ_ISPL_2024'){
										['player', 'thisyearteam', 'prevteam', 'ISPL S-1', 'ISPL S-2', 'category'].forEach(value => {
								            const option = document.createElement('option');
								            option.value = value;
								            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
								            select.appendChild(option);
								        });
									}else if(broadcaster === 'PSL'){
										['player', 'PSL CAREER', 'T20I CAREER', 'T20 CAREER'].forEach(value => {
								            const option = document.createElement('option');
								            option.value = value;
								            option.text = value;
								            select.appendChild(option);
								        });
									}
									else if(broadcaster === 'KCL' || broadcaster === 'KCL_BIGSCREEN'|| broadcaster === 'PWL'){
										['player', 'thisyearteam','category'].forEach(value => {
								            const option = document.createElement('option');
								            option.value = value;
								            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
								            select.appendChild(option);
								        });
									} else if(broadcaster === 'MUMBAI_T20_VIZ'){
										['player', 'thisyearteam','stats','category'].forEach(value => {
								            const option = document.createElement('option');
								            option.value = value;
								            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
								            select.appendChild(option);
								        });
									}else {
								        ['player', 'thisyearteam', 'prevteam', 'stats', 'category'].forEach(value => {
								            if ($('#selected_broadcaster').val().toUpperCase() === 'MUMBAI_T20_VIZ' && (value === 'prevteam' || value === 'freetext')) {
										        return; // skip these options for Mumbai
										    }
								            const option = document.createElement('option');
								            option.value = value;
								            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
								            select.appendChild(option);
								        });
								    }
									
									select.setAttribute('onchange',"processUserSelection(this)");
									row.insertCell(0).appendChild(select);
									cellCount = cellCount + 1;
									
								    // Second cell: reserved for conditional dropdown
								    const secCell = row.insertCell(1);
								    secCell.id = 'Playerstats'; // So we can target it easily later
								    cellCount++;
								
								    // Add event listener to the main dropdown
								    $(select).on('change', function () {
								        const selectedValue = this.value;
								        const container = document.getElementById('Playerstats');
								
								        // Remove old dropdown if it exists
								        const existing = document.getElementById('PlayerData');
								        if (existing) existing.remove();
								
								        // Show second dropdown only if "stats" is selected
								        if (selectedValue === 'stats') {
								            const statsDropdown = document.createElement('select');
								            statsDropdown.id = 'PlayerData';
								            statsDropdown.name = 'PlayerData';
								
								            ['MCA T20s','ISPL S-1', 'ISPL S-2'].forEach(value => {
								                const option = document.createElement('option');
								                option.value = value;
								                option.text = value;
								                option.style.fontWeight = 'bold';
								                statsDropdown.appendChild(option);
								            });
								
								            container.appendChild(statsDropdown);
								        }
								    });
								
								    // Trigger initial change in case default value is "stats"
								    $(select).trigger('change');
									
									/////
									break;
							      }
						     break;
				          }
			          break;
	                  	} 
	                break;
			case 'LT_PP_STATS-OPTIONS':
				select = document.createElement('select');
				select.style = 'width:100px';
				select.id = 'selectShowData';
				select.name = select.id;
				
			    if($('#selected_broadcaster').val().toUpperCase() === 'VIZ_ISPL_2024'){
					['player', 'thisyearteam', 'prevteam', 'ISPL S-1', 'ISPL S-2', 'category'].forEach(value => {
			            if ($('#selected_broadcaster').val().toUpperCase() === 'MUMBAI_T20_VIZ' && (value === 'prevteam' || value === 'freetext')) {
					        return; // skip these options for Mumbai
					    }
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}
				else if($('#selected_broadcaster').val().toUpperCase() === 'PSL'){
					['player', 'PSL CAREER', 'T20I CAREER', 'T20 CAREER'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value;
			            select.appendChild(option);
			        });
				}
				else if($('#selected_broadcaster').val().toUpperCase() === 'KCL'){
					['player'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}else if($('#selected_broadcaster').val().toUpperCase() === 'MUMBAI_T20_VIZ'){
					['player', 'thisyearteam', 'prevteam','stats', 'category'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				} else {
			        ['player', 'thisyearteam', 'prevteam', 'stats', 'category'].forEach(value => {
			            if ($('#selected_broadcaster').val().toUpperCase() === 'MUMBAI_T20_VIZ' && (value === 'prevteam' || value === 'freetext')) {
					        return; // skip these options for Mumbai
					    }
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
			    }
				
				select.setAttribute('onchange',"processUserSelection(this)");
				row.insertCell(0).appendChild(select);
				cellCount = cellCount + 1;
				
				// Second cell: reserved for conditional dropdown
			    const secCell = row.insertCell(1);
			    secCell.id = 'Playerstats'; // So we can target it easily later
			    cellCount++;
			
				if($('#selected_broadcaster').val().toUpperCase()==='KCL' || $('#selected_broadcaster').val().toUpperCase()==='PWL'){
							
					select = document.createElement('select');
					select.style = 'width:150px';
					select.id = 'selectLogo';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'WITHOUT_LOGO';
					option.text = 'WITHOUT LOGO';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'WITH_LOGO';
					option.text = 'WITH LOGO';
					select.appendChild(option);
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
				}
			    // Add event listener to the main dropdown
			    $(select).on('change', function () {
			        const selectedValue = this.value;
			        const container = document.getElementById('Playerstats');
			
			        // Remove old dropdown if it exists
			        const existing = document.getElementById('PlayerData');
			        if (existing) existing.remove();
			
			        // Show second dropdown only if "stats" is selected
			        if (selectedValue === 'stats') {
			            const statsDropdown = document.createElement('select');
			            statsDropdown.id = 'PlayerData';
			            statsDropdown.name = 'PlayerData';
			
			            ['MCA T20s','ISPL S-1', 'ISPL S-2'].forEach(value => {
			                const option = document.createElement('option');
			                option.value = value;
			                option.text = value;
			                option.style.fontWeight = 'bold';
			                statsDropdown.appendChild(option);
			            });
			
			            container.appendChild(statsDropdown);
			        }
			    });
			
			    // Trigger initial change in case default value is "stats"
			    $(select).trigger('change');
				break;
			case 'PROFILE_FF_STATS-OPTIONS':
				select = document.createElement('select');
				select.style = 'width:100px';
				select.id = 'selectShowProfileStats';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'with_info';
				option.text = 'Category';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'with_data';
				option.text = 'Rank & Style';
				select.appendChild(option);
				
				select.setAttribute('onchange',"processUserSelection(this)");
				row.insertCell(cellCount).appendChild(select);
				cellCount = cellCount + 1;
				break;	
			case 'PROFILE_STATS-OPTIONS':
			    // First dropdown
			    select = document.createElement('select');
			    select.style = 'width:100px';
			    select.id = 'selectProfileStats';
			    select.name = select.id;
			
			    // Determine which options to show based on broadcaster
			    const broadcaster = $('#selected_broadcaster').val().toUpperCase();
			    if (broadcaster === 'UTT_VIZ' || broadcaster === 'KCL' || broadcaster === 'KCL_BIGSCREEN'  || broadcaster === 'PWL') {
			        ['category','rank', 'style', 'bio'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
			    }else if(broadcaster === 'UTT_BIGSCREEN'){
					['category','rank&style'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}else if(broadcaster === 'VIZ_ISPL_2024'){
					['style', 'freetext', 'prevteam', 'ISPL S-1', 'ISPL S-2', 'category'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}else if(broadcaster === 'PSL'){
					['style', 'PSL CAREER', 'T20I CAREER', 'T20 CAREER', 'freetext'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}else if(broadcaster === 'MUMBAI_T20_VIZ'){
					['style', 'prevteam', 'stats'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
				}  else {
			        ['style', 'freetext', 'prevteam', 'stats', 'category'].forEach(value => {
			            const option = document.createElement('option');
			            option.value = value;
			            option.text = value === 'freetext' ? 'Franchise pick' : value.charAt(0).toUpperCase() + value.slice(1);
			            select.appendChild(option);
			        });
			    }
			
			    // First cell: add main dropdown
			    const selectCell = row.insertCell(cellCount);
			    selectCell.appendChild(select);
			    cellCount++;
			
			    // Second cell: reserved for conditional dropdown
			    const secondCell = row.insertCell(cellCount);
			    secondCell.id = 'Playerstats'; // So we can target it easily later
			    cellCount++;
			
			    // Add event listener to the main dropdown
			    $(select).on('change', function () {
			        const selectedValue = this.value;
			        const container = document.getElementById('Playerstats');
			
			        // Remove old dropdown if it exists
			        const existing = document.getElementById('PlayerData');
			        if (existing) existing.remove();
			
			        // Show second dropdown only if "stats" is selected
			        if (selectedValue === 'stats') {
			            const statsDropdown = document.createElement('select');
			            statsDropdown.id = 'PlayerData';
			            statsDropdown.name = 'PlayerData';
						if(broadcaster === 'PSL'){
							['FC', 'LIST A', 'DT20'].forEach(value => {
				                const option = document.createElement('option');
				                option.value = value;
				                option.text = value;
				                option.style.fontWeight = 'bold';
				                statsDropdown.appendChild(option);
				            });
						}else{
							['MCA T20s','FC', 'LIST A', 'DT20'].forEach(value => {
				                const option = document.createElement('option');
				                option.value = value;
				                option.text = value;
				                option.style.fontWeight = 'bold';
				                statsDropdown.appendChild(option);
				            });
						}
			            container.appendChild(statsDropdown);
			        }
			    });
			
			    // Trigger initial change in case default value is "stats"
			    $(select).trigger('change');
			
			    break;

			case 'LOF_REMAINING_PURSE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
				case 'VIZ_ISPL_2024': case 'PSL': case 'UTT_BIGSCREEN': case "UTT_VIZ": case 'MUMBAI_T20_VIZ': case 'MUMBAI_T20_BIGSCREEN': case 'KCL': case 'KCL_BIGSCREEN': case 'PWL': 
				case 'UTT_VIZ':
					select = document.createElement('select');
					select.id = 'selectType';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'name';
					option.text = 'Team Name';
					select.appendChild(option);
					
					/*option = document.createElement('option');
					option.value = 'logo';
					option.text = 'Team Logo';
					select.appendChild(option);*/
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					$(select).select2();
					cellCount = cellCount + 1;
						
					break;
				}
				break;
			}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case 'FLIPPER-OPTIONS':
				option.name = 'populate_flipper_btn';
			    option.value = 'Populate Flipper';
				break;
			case 'FREETEXT-OPTIONS':
		    	option.name = 'populate_freetextcrawler_btn';
			    option.value = 'Populate Flipper';
				break;
			case 'FLIPPER_TEXT-OPTIONS':
				option.name = 'populate_flipper_text_btn';
			    option.value = 'Populate Flipper Text';
				break;
			case'NAMESUPER-OPTIONS':
			    option.name = 'populate_namesuper_btn';
			    option.value = 'Populate Namesuper';
				break;
			case 'NAMESUPER_PLAYER-OPTIONS':	
				option.name = 'populate_namesuper_player_btn';
			    option.value = 'Populate Namesuper-Player';
				break;
				
			case 'LOF_REMAINING_PURSE-OPTIONS':
				option.name = 'populate_lof_remaining_purse_btn';
			    option.value = 'Populate Lof Remaining Purse';
				break;
			
			case 'PLAYERPROFILE-OPTIONS': case 'PLAYERPROFILE_MAIN-OPTIONS':
			    option.name = 'populate_playerprofile_btn';
			    option.value = 'Populate Playerprofile';
				break;
			case 'FF_PLAYERPROFILE-OPTIONS':
				option.name = 'populate_ff_playerprofile_btn';
			    option.value = 'Populate Playerprofile FF';
				break;
			case 'FF_BIG_PLAYERPROFILE-OPTIONS':
				option.name = 'populate_ff_big_playerprofile_btn';
			    option.value = 'Populate Playerprofile Big FF';
				break;
			case 'PROFILEFF-OPTIONS':
				option.name = 'populate_profileff_btn';
			    option.value = 'Populate profile FF';
				break;
			case 'LT_PLAYERPROFILE-OPTIONS':
				option.name = 'populate_lt_playerprofile_btn';
			    option.value = 'Populate Playerprofile Lt';
				break;
			case 'LT_PP_STATS-OPTIONS':
				option.name = 'populate_lt_playerprofile_stats_btn';
			    option.value = 'Populate Playerprofile Lt Stats';
				break;
			case "ZONE-PLAYER-OPTIONS":
				option.name = 'populate_zonePlayer_stats_btn';
			    option.value = 'Populate zonePlayer Stats';
				break;
			case 'ZONE-PLAYER_FULL-OPTIONS':
				option.name = 'populate_zonePlayer_full_btn';
			    option.value = 'Populate zonePlayer Full';
				break;
			case 'ZONEWISE_PLAYER_SOLD-OPTIONS':
				option.name = 'populate_zonewisePlayer_sold_btn';
			    option.value = 'Populate zoneWisePlayer Sold';
				break;
			case 'FLIPPER_SQUAD-OPTIONS':
				 option.name = 'populate_flipper_squad_btn';
			    option.value = 'Populate Squad';
				break;
			case 'SQUAD-OPTIONS':
			    option.name = 'populate_squad_btn';
			    option.value = 'Populate Squad';
				break;
			case 'TEAMS-OPTIONS':
				 option.name = 'populate_teams_btn';
			    option.value = 'Populate Teams';
				break;
			case 'RETAIN_PLAYERS-OPTIONS':
				option.name = 'populate_retain_players_squad_btn';
			    option.value = 'Populate Reatin Player';
				break;
			case 'SQUADCATEGORIES-OPTIONS':
				 option.name = 'populate_category_btn';
			    option.value = 'Populate Category';
				break;
			case 'TOP-SOLD_TEAM-OPTIONS':
				option.name = 'populate_Top_Sold_btn';
			    option.value = 'Populate Squad';
				break;
			case 'LOF_TOP_SOLD_TEAM-OPTIONS':
				option.name = 'populate_Lof_Top_Sold_team_btn';
			    option.value = 'Populate Team Top Sold';
				break;
			case 'CRAWL_TOP_SOLD_TEAM-OPTIONS':
				option.name = 'populate_crawl_Top_Sold_team_btn';
			    option.value = 'Populate Team Top Sold';
				break;	
			case 'LOF_SQUAD-OPTIONS':
				option.name = 'populate_Lof_squad_btn';
			    option.value = 'Populate LOF Squad';
				break;
			case 'SQUAD_ANIMATION-OPTIONS':
				
				option.name = 'populate_squad_animation_btn';
			    option.value = 'Populate Squad';
				break;
			case 'CRAWL_SQUAD-OPTIONS':	
				option.name = 'populate_crawle_squad_btn';
			    option.value = 'Populate Squad';
				break;
			case 'SINGLE_PURSE-OPTIONS':
				option.name = 'populate_single_purse_btn';
			    option.value = 'Populate Single Purse';
				break;
			case "GRAPHICS-FF_FIVE_TOP_BUYS_AUCTION":
				option.name = 'populate_ffTop5Buys_btn';
			    option.value = 'Populate FF TOP 5 BUYS';
				break;	
			case "LOF_TEAM_BID_OPTIONS":
				option.name = 'populate_Lof_Team_Bid_btn';
			    option.value = 'Populate';
				break;	
			case 'GOOGLY-OPTIONS':
				option.name = 'populate_googly_purse_btn';
			    option.value = 'Googly Power';
				break;
			case 'PROFILE_STATS-OPTIONS':
				option.name = 'populate_profile_stats_btn';
			    option.value = 'Profile Stats';
				break;	
			case 'PROFILE_FF_STATS-OPTIONS':
				option.name = 'populate_profile_Change_stats_btn';
			    option.value = 'Profile Change On';
				break;	
			case "SQUAD_PLAYER-OPTIONS":
				option.name = 'populate_squad_Player_btn';
			    option.value = 'Populate Squad Player';
				break;
			case 'LOF_SQUAD_SIZE_CATEGORY_WISE_-OPTIONS':
				option.name = 'populate_squad_size_category_wise_btn';
			    option.value = 'Populate Squad Category';
				break;
			case 'FF_TOP_SOLD_TEAM-OPTIONS':
				option.name = 'populate_ff_Top_Sold_team_btn';
			    option.value = 'Populate Top buy team';
				break;
			case "FF_SQUAD_TEAM-OPTIONS":
				option.name = 'populate_Squad_team_btn';
			    option.value = 'Populate ';
				break;
			case "FF_SQUAD_ROLE_TEAM-OPTIONS":
				option.name = 'populate_Squad_Role_team_btn';
			    option.value = 'Populate ';
				break;
			case 'FF_TOP_FIVE_SOLD_TEAM-OPTIONS':
				option.name = 'populate_ff_Top_Five_Sold_team_btn';
			    option.value = 'Populate Top buy team';
				break;
			case 'SINGLE_TEAM-OPTIONS':	
				option.name = 'single_Sold_team_btn';
			    option.value = 'Populate Top buy team';
				break;
			case 'TEAM_CURRENT_BID-OPTIONS':
				option.name = 'populate_team_curr_bid_btn';
			    option.value = 'Populate Team CURR Bid';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);
		    	
			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
	}
	document.activeElement.blur();
	
	
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}