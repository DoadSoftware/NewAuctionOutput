package com.auction.broadcaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.PlayerCount;
import com.auction.model.Statistics;
import com.auction.model.StatsType;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class KCL_BIGSCREEN extends Scene{

	private String status, side2ValueToProcess = "";
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "KCL_BIGSCREEN";
	public Data data = new Data();
	public String which_graphics_onscreen = "",which_data="", rtm_googly_on_screen = "",which_stat = "";
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1, rowHighlight = 1,prevRowHighlight = 1, rtmGooglyWhichSide = 1;
	public int player_id = 0,team_id=0,player_number=0;
	public int zoneSize = 0, current_index = 0,side2val = 0;
	private boolean update_gfx = false;
	
	List<Player> squad = new ArrayList<Player>();
	List<String> data_str = new ArrayList<String>();
	List<PlayerCount> player_count = new ArrayList<PlayerCount>();

	private String base_path = "IMAGE*/Default/Essentials/Base/";
	private String text_path = "IMAGE*/Default/Essentials/Text/";
	
	private String base_path_1 = "IMAGE*/Default/Essentials/Base1/";
	private String text_path_1 = "IMAGE*/Default/Essentials/Text1/";
	private String base_path_2 = "IMAGE*/Default/Essentials/Base2/";
	private String text_path_2 = "IMAGE*/Default/Essentials/Text2/";
	
	private String logo_base = "IMAGE*/Default/Essentials/LogoBase/";
	private String logo_path = "IMAGE*/Default/Essentials/Logos/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	private List <Team> Bid_Team = new ArrayList<Team>();
	
	public boolean isProfileStatsOnScreen = false, isFlipperonScreen = false, isSBStatsonScreen = false;
	
	public KCL_BIGSCREEN() {
		super();
	}

	public KCL_BIGSCREEN(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Data updateData(Auction auction, Auction session_curr_bid,AuctionService auctionService, PrintWriter print_writer) throws NumberFormatException, Exception
	{
		if(update_gfx == true) {
			if(which_graphics_onscreen.equalsIgnoreCase("IDENT")) {
				populateIdent(true,print_writer,whichSideNotProfile,session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
				populateProfileFF(true,print_writer, whichSideNotProfile, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
						auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_TOP_BUYS_AUCTION")) {
				populateFFTopBuysAuction(true,print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUYS_AUCTION")) {
				populateFFFiveTopBuysAuction(true,print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUY_TEAM")) {
				populateFFTopFiveBuysTeam(true,print_writer, whichSideNotProfile, Integer.valueOf(side2ValueToProcess.split(",")[0]), auction, 
						auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_TOP_BUY_TEAM")) {
				populateFFTopBuysTeam(true,print_writer, whichSideNotProfile, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_SQUAD_TEAM")) {
				populateFFSquadTeam(true,print_writer, whichSideNotProfile, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("FF_SQUAD_ROLE_TEAM")) {
				populateFFSquadRoleTeam(true,print_writer, whichSideNotProfile, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("REMAINING_PURSE_ALL")) {
				populateRemainingPurse(true,print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				populateSquad(true,print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], 
						whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("SQUAD_ANIMATION")) {
				populateSquadAnimation(true,print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], 
						whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
			}else if(which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS")) {
				populateZoneSquad(true,print_writer, side2ValueToProcess.split(",")[0], whichSideNotProfile, 
						auction, auctionService, session_selected_broadcaster);
			}
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, Auction session_curr_bid, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws Exception {
		System.out.println(whatToProcess.toUpperCase());
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SINGLE_PURSE": case "POPULATE-TOP_SOLD":
		case "POPULATE-L3-NAMESUPER": case "POPULATE-TOP_SOLD_TEAM": case "POPULATE-IDENT": case "POPULATE-RTM_AVAILABLE": case "POPULATE-RTM_ENABLED":
		case "POPULATE-GOOGLY_POWER": case "POPULATE-PROFILE_STATS": case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD":
		case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-LOF_REMAINING_SLOT":
		case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-FF_RTM_AND_PURSE_REMAINING": case "POPULATE-FF_TOP_BUYS_AUCTION":
		case "POPULATE-FF_TOP_BUY_TEAM": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": case "POPULATE-FF_ICONIC_PLAYERS": case "POPULATE-LT_ICONIC_PLAYERS": 
		case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-PLAYERPROFILE_LT_STATS": case "POPULATE-LOF_SQUAD": case "POPULATE-LOF_SQUAD_REMAIN":
		case "POPULATE-L3-FLIPPER": case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-ZONE_PLAYERS_FULL": case "POPULATE-TEAM_CURR_BID": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":

		case "POPULATE-FF_SINGLEPURSE_TEAM": case "POPULATE-FF_FIVE_TOP_BUY_TEAM": case "POPULATE-ZONEWISE_PLAYERS_SOLD": case "POPULATE-FLIPPER_SQUAD": case "POPULATE-FF_SQUAD_TEAM":
		case "POPULATE-FF_SQUAD_ROLE_TEAM":case "POPULATE-LOF_TEAM_BID_AUCTION": case "POPULATE-L3-FLIPPER_TEXT": case "POPULATE-PROFILE_FF": case "POPULATE-SQUAD_ANIMATION":

			switch (session_selected_broadcaster.toUpperCase()) {
			case "KCL_BIGSCREEN":
				if(which_graphics_onscreen != "") {
					switch(which_graphics_onscreen) {
					case "PLAYERPROFILE": 
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER":
					case "TOP_SOLD_TEAM": case "IDENT":case "FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-SINGLEPURSE_TEAM":
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					}
				}
				switch (whatToProcess.toUpperCase()) {
				
				case "POPULATE-PLAYERPROFILE_FF":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populatePlayerProfileFF(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PROFILE_FF":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[0]));
					side2ValueToProcess = valueToProcess;
					populateProfileFF(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;	
				case "POPULATE-REMAINING_PURSE_ALL":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateRemainingPurse(false,print_writer, whichSideNotProfile,auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSquad(false,print_writer, Integer.valueOf(valueToProcess.split(",")[0]),valueToProcess.split(",")[1], whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-SQUAD_ANIMATION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSquadAnimation(false,print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-TOP_SOLD":
					populateTopSold(false,print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-IDENT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateIdent(false,print_writer,whichSideNotProfile,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-TOP_SOLD_TEAM":
					populateTopSoldTeam(false,print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), 
							auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFRTMAndPurseRemaining(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_TOP_BUYS_AUCTION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopBuysAuction(false,print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFFiveTopBuysAuction(false,print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_TOP_BUY_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopBuysTeam(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_SQUAD_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFSquadTeam(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_SQUAD_ROLE_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFSquadRoleTeam(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopFiveBuysTeam(false,print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_SINGLEPURSE_TEAM":	
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSingleTeam(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-FF_ICONIC_PLAYERS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateFFIconicPlayers(false,print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-ZONE_PLAYERS_FULL":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					if((!which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS") && !which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_FULL")) || 
							(!valueToProcess.equalsIgnoreCase("undefined") && !side2ValueToProcess.equalsIgnoreCase(valueToProcess))) {
						squad.clear();
						current_index = 0;
						
						if(valueToProcess.split(",")[0].equalsIgnoreCase("A") || valueToProcess.split(",")[0].equalsIgnoreCase("B")) {
							squad = auction.getPlayersList().stream()
								    .filter(ply -> ply.getCategory().equalsIgnoreCase(valueToProcess.split(",")[0]))
								    .collect(Collectors.toList());
						}else {
							squad = auction.getPlayersList().stream()
								    .filter(ply -> ply.getCategory().contains(valueToProcess.split(",")[0]))
								    .collect(Collectors.toList());
						}
						
						Iterator<Player> squadIterator = squad.iterator();

						if(whatToProcess.equalsIgnoreCase("POPULATE-ZONE_PLAYERS_STATS")) {
							while (squadIterator.hasNext()) {
							    Player sq = squadIterator.next();						    
							    for (Player ply : auction.getPlayers()) {
							        if (sq.getPlayerId() == ply.getPlayerId() &&
							            (ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM) || ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD))) {						                
							            squadIterator.remove();
							            break;
							        }
							    }
							}
						}
						
						zoneSize = squad.size();
						side2ValueToProcess = valueToProcess;
					}
					populateZoneSquad(false,print_writer, side2ValueToProcess.split(",")[0], whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				}
			}
		case "ANIMATE-OUT-PROFILE": case "ANIMATE-OUT-RTM_GOOGLY":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-RTM_ENABLED": case "ANIMATE-IN-GOOGLY_POWER": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD": case "ANIMATE-IN-SQUAD-PLAYER": 
		case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING":
		case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-TEAM_CURR_BID": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD": case "ANIMATE-IN-FLIPPER_SQUAD":
		case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM":	case "ANIMATE-IN-LOF_TEAM_BID_AUCTION": case "ANIMATE-IN-FLIPPER_TEXT": case "ANIMATE-IN-PROFILE_FF": case "ANIMATE-IN-SQUAD_ANIMATION":

		case "ANIMATE-IN-LOF_SQUAD": case "ANIMATE-IN-LOF_SQUAD_REMAIN":

		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE": case "ANIMATE-IN-ZONE-PLAYER_FULL":
		case "ANIMATE-IN-LT_ICONIC_PLAYERS": case "ANIMATE-IN-PLAYERPROFILE_LT": case "ANIMATE-IN-PLAYERPROFILE_LT_STATS": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":case "ANIMATE-IN-FF_SQUAD_TEAM":
			
		case "ANIMATE-SINGLEPURSE_TEAM":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "KCL_BIGSCREEN":
				switch (whatToProcess.toUpperCase()) {
				
				case "ANIMATE-IN-TOP_SOLD":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD";
					break;
				case "ANIMATE-IN-TOP_SOLD_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD_TEAM";
					break;
				case "ANIMATE-IN-PLAYERPROFILE_LT_STATS":
					ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
					
					if(side2ValueToProcess.split(",").length != 2) {
						which_stat = "";
					}else {
						which_stat = side2ValueToProcess.split(",")[1];
					}
					
					ChangeOnLTStats(print_writer, 1, player_id, side2ValueToProcess.split(",")[0],
							side2ValueToProcess.split(",").length != 2 ? 0 : auctionService.getStatsTypes().stream()
							        .filter(st -> st.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[1]))
							        .findAny().map(StatsType::getStats_id).orElse(0), auctionService.getAllStats(), auction, 
							auctionService, session_selected_broadcaster);	
					TimeUnit.MILLISECONDS.sleep(2000);
					cutBack(print_writer, which_graphics_onscreen, whatToProcess);
					which_graphics_onscreen = "PLAYERPROFILE_LT";
					break;
					
				//FF	
				case "ANIMATE-IN-IDENT": case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
				case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
				case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-ZONE-PLAYER_FULL": case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":case "ANIMATE-IN-FF_SQUAD_TEAM":
				case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM": case "ANIMATE-IN-PROFILE_FF": case "ANIMATE-IN-SQUAD_ANIMATION": case "ANIMATE-SINGLEPURSE_TEAM":
					update_gfx = true;
					
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials START\0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header START\0");
						switch(whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId START\0");
							which_graphics_onscreen = "IDENT";
							break;
						case "ANIMATE-SINGLEPURSE_TEAM":	
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$SinglePurse START\0");
							which_graphics_onscreen = "ANIMATE-SINGLEPURSE_TEAM";
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-PROFILE_FF":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile START\0");
							which_graphics_onscreen = "PLAYERPROFILE_FF";
							break;
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining START\0");
							which_graphics_onscreen = "FF_RTM_AND_PURSE_REMAINING";
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-FF_SQUAD_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_New START\0");
							which_graphics_onscreen = "FF_SQUAD_TEAM";
							break;
						case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_Category START\0");
							which_graphics_onscreen = "FF_SQUAD_ROLE_TEAM";
							break;
						case "ANIMATE-IN-REMAINING_PURSE_ALL":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining START\0");
							which_graphics_onscreen = "REMAINING_PURSE_ALL";
							break;
						case "ANIMATE-IN-SQUAD":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad START\0");
							which_graphics_onscreen = "SQUAD";
							break;
						case "ANIMATE-IN-SQUAD_ANIMATION":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad START\0");
							which_graphics_onscreen = "SQUAD_ANIMATION";
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-ZONE-PLAYER_FULL":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad START\0");
							which_graphics_onscreen = "ZONE-PLAYER_STATS";
							break;
						case "ANIMATE-IN-FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers START\0");
							which_graphics_onscreen = "FF_ICONIC_PLAYERS";
							break;
						}
					}else {
						System.out.println("which_graphics_onscreen = " + which_graphics_onscreen + "      whatToProcess = " + whatToProcess);
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							populateIdent(true,print_writer,1,session_selected_broadcaster);
							break;
						case "ANIMATE-SINGLEPURSE_TEAM":
							populateSingleTeam(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), auction, auctionService, valueToProcess);
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							populatePlayerProfileFF(true,print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-PROFILE_FF":
							populateProfileFF(true,print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-REMAINING_PURSE_ALL":
							populateRemainingPurse(true,print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD":
							populateSquad(true,print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], 
									1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD_ANIMATION":
							populateSquadAnimation(true,print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], 
									1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-ZONE-PLAYER_FULL":
							populateZoneSquad(true,print_writer, side2ValueToProcess.split(",")[0], 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							populateFFRTMAndPurseRemaining(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							populateFFTopBuysAuction(true,print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							populateFFFiveTopBuysAuction(true,print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							populateFFTopFiveBuysTeam(true,print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							populateFFTopBuysTeam(true,print_writer, 1, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_SQUAD_TEAM":
							populateFFSquadTeam(true,print_writer, 1, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM":
							populateFFSquadRoleTeam(true,print_writer, 1, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_ICONIC_PLAYERS":
							populateFFIconicPlayers(true,print_writer, whichSide, auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
				
				case "CLEAR-ALL":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Bid_Value SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats*ANIMATION*KEY*$A1*VALUE SET 0.0 0.0 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats*ANIMATION*KEY*$A2*VALUE SET 0.0 40.0 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF SHOW 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper SHOW 0\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Loop SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*EndFlare SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Audio SHOW 0.0 \0");
					
					rtm_googly_on_screen = "";
		            which_graphics_onscreen = "";
		            side2ValueToProcess ="";
		            rtmGooglyWhichSide = 1;
		            whichSideNotProfile = 1;
		            data.setBid_Start_or_not(false);
		            data.setData_on_screen(false);
		            isProfileStatsOnScreen = false;
					data.setBid_result("");
					isFlipperonScreen = false;
					isSBStatsonScreen = false;
					
					TimeUnit.MILLISECONDS.sleep(1000);
					
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side1$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Select_GraphicsType*FUNCTION*Omo*vis_con SET 10\0");
					
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials START\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$BS_Logo START\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Loop START \0");
					which_graphics_onscreen = "BG";
					break;
				case "ANIMATE-OUT-PROFILE":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Essentials CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$CenterData CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Right_Data CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats CONTINUE\0");
					
					if(isProfileStatsOnScreen) {
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						isProfileStatsOnScreen = false;
					}
					
					if(!rtm_googly_on_screen.isEmpty()) {
						switch (rtm_googly_on_screen) {
						case "RTM":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out CONTINUE\0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0\0");
							break;

						case "GOOGLY":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out CONTINUE\0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0\0");
							break;
						}
						rtm_googly_on_screen = "";
					}
					
					TimeUnit.MILLISECONDS.sleep(1200);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug SHOW 0\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold SHOW 0.0 \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					data.setBid_Start_or_not(false);
					data.setPlayer_sold_or_unsold(false);
					data.setData_on_screen(false);
					data.setBid_result("");
					break;
				case "ANIMATE-OUT-RTM_GOOGLY":
					switch (rtm_googly_on_screen) {
					case "RTM":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0\0");
						break;

					case "GOOGLY":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0\0");
						break;
					}
					rtm_googly_on_screen = "";
					break;
				
				case "ANIMATE-OUT":
					System.out.println("which_graphics_onscreen = " + which_graphics_onscreen);
					switch(which_graphics_onscreen) {
					case "LT_ICONIC_PLAYERS":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					case "PLAYERPROFILE_LT":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Essentials CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$TopData CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$BottomData CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Logo CONTINUE\0");
						
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
					case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
					case "LOF_TEAM_BID_AUCTION":
						switch (which_graphics_onscreen) {
						case "LOF_REMAINING_PURSE": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING":
						case "ZONEWISE_PLAYERS_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "LOF_SQUAD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "LOF_TEAM_BID_AUCTION":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$PurseRemaining_Small CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "LOF_TOP_SOLD": 
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "SQUAD-PLAYER":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category CONTINUE\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight CONTINUE REVERSE\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight CONTINUE REVERSE \0");
							break;
						case "LOF_SQUAD_SIZE_CATEGORY_WISE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team CONTINUE \0");
							break;
						}
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Essentials CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Header CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$SubHeader CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "IDENT": case "PLAYERPROFILE_FF": case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
					case "SQUAD": case "REMAINING_PURSE_ALL": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION":
					case "FF_FIVE_TOP_BUY_TEAM":case "FF_SQUAD_TEAM": case "FF_SQUAD_ROLE_TEAM": case "ANIMATE-SINGLEPURSE_TEAM": case "SQUAD_ANIMATION":
					case "ZONE-PLAYER_FULL":	
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side2$Select_GraphicsType*FUNCTION*Omo*vis_con SET 10\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side2$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
						
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Header START\0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$BS_Logo START\0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Loop START \0");
						
						switch (which_graphics_onscreen) {
						case "IDENT":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$MatchId START\0");
							break;
						case "PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile START\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys START\0");
							break;
						case "FF_SQUAD_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_New START\0");
							break;
						case "FF_SQUAD_ROLE_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_Category START\0");
							break;
						case "REMAINING_PURSE_ALL":case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$PurseRemaining START\0");
							break;
						case "SQUAD": case "ZONE-PLAYER_STATS": case "SQUAD_ANIMATION": case "ZONE-PLAYER_FULL":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad START\0");
							break;
						case "FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$IconPlayers START\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuysImage START\0");
							break;
						case "ANIMATE-SINGLEPURSE_TEAM":	
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$SinglePurse START\0");
							break;
						}
						
						TimeUnit.MILLISECONDS.sleep(2000);
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Select_GraphicsType*FUNCTION*Omo*vis_con SET 10\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side1$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
						
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$BS_Logo SHOW 2.480\0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change SHOW 0\0");
//						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile SHOW 0\0");
//						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Logo SHOW 0\0");
//						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus SHOW 0\0");
						
						switch (which_graphics_onscreen) {
						case "IDENT":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 0\0");
							break;
						case "PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 0\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys SHOW 0\0");
							break;
						case "FF_SQUAD_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_New SHOW 0\0");
							break;
						case "FF_SQUAD_ROLE_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_Category SHOW 0\0");
							break;
						case "REMAINING_PURSE_ALL":case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining SHOW 0\0");
							break;
						case "SQUAD": case "ZONE-PLAYER_STATS": case "SQUAD_ANIMATION": case "ZONE-PLAYER_FULL":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 0\0");
							break;
						case "FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers SHOW 0\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage SHOW 0\0");
							break;
						case "ANIMATE-SINGLEPURSE_TEAM":	
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$SinglePurse SHOW 0\0");
							break;
						}
						
						which_graphics_onscreen = "BG";
						break;
					case "NAMESUPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Essentials CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$TopData CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$BottomData CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						break;
					case "FLIPPER": case "FLIPPER_SQUAD": case "FLIPPER_TEXT":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(500);
						if(isSBStatsonScreen) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats*ANIMATION*KEY*$A1*VALUE SET 0.0 40.0 0.0 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
							TimeUnit.MILLISECONDS.sleep(1000);
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats*ANIMATION*KEY*$A2*VALUE SET 0.0 40.0 0.0 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats SHOW 1.120\0");
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats*ANIMATION*KEY*$A1*VALUE SET 0.0 0.0 0.0 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						}
						
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Flipper SHOW 0\0");
						isFlipperonScreen = false;
						break;
						
//					case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER": case "TOP_SOLD_TEAM":
//						AnimateOutGraphics(print_writer, whatToProcess.toUpperCase());
//						which_graphics_onscreen = "";
//						break;
					}
					break;
				}
				
			}
		}
		return null;
	}
	
	private void populateSingleTeam(PrintWriter print_writer, int whichSide, Integer valueOf,
			Auction match, AuctionService auctionService, String session_selected_broadcaster2) throws Exception {
		
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataKCLInZone(match,valueOf);
		int total =0;
		List<String> count = new ArrayList<String>();
		
		match.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(match.getTeam(), 
				match.getPlayers(), match.getPlayersList(),session_selected_broadcaster));
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 11\0");
		
		for(Team team:auctionService.getTeams()) {
			
			if(team.getTeamId() == valueOf) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
						+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
						+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
						+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET " + team.getTeamName1() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET SQUAD\0");
				
				//row1
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row1" +
						"$txt_Title*GEOM*TEXT SET SQUAD SIZE\0");
				//row2
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row2" +
						"$txt_Title*GEOM*TEXT SET PURSE REMAINING\0");
				//totalpurse
				
				if(match.getPlayers() != null ) {
					for(int j=0; j <= match.getPlayers().size()-1; j++) {
						if(match.getPlayers().get(j).getTeamId() == valueOf) {
							total = total + match.getPlayers().get(j).getSoldForPoints();
						}
					}
				}
				
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row2" +
						"$txt_Value*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh((Integer.valueOf(team.getTeamTotalPurse()) - total)) + " K \0");
				
				for (int i = 1; i <= match.getTeamZoneList().size(); i++) {
					PlayerCount teamZone = match.getTeamZoneList().get(i - 1); 
				    if (teamZone.getTeamId() == valueOf) {
				        
				        print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row1" +
								"$txt_Text*GEOM*TEXT SET " + teamZone.getPlayers() + "\0");

				        for(int k=1; k<=2; k++) {
				        	count = new ArrayList<String>();
				        	int j=2;
				        	int cTotal = 0;
				        	
				        	for (Map.Entry<String, Integer> entry : teamZone.getCategory().entrySet()) {
				        		if(entry.getKey().trim().contains("C")) {
				        			cTotal += entry.getValue();
				        		}else {
				        			count.add(entry.getKey() + "-" + entry.getValue());
				        		}
				        	}
				        	count.add(2, "C" + "-" + cTotal);
				        	for(int s=0;s<=count.size()-1;s++) {
				        		j++;
				        		
				        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row" + j +
										"$txt_Title*GEOM*TEXT SET CATEGORY " + count.get(s).split("-")[0] + "\0");
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$SinglePurse$DataAll$Row" + j +
										"$txt_Text*GEOM*TEXT SET " + count.get(s).split("-")[1] + "\0");
				        	}
				         }
				    } 
				}
			}
		}
	}
	private void populateFFSquadRoleTeam(boolean is_this_updating,PrintWriter print_writer, int whichSide, Integer teaam_id,
			Auction auction, AuctionService auctionService, String session_selected_broadcaster2) throws Exception {
		auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(auction.getTeam(), 
		        auction.getPlayers(), auction.getPlayersList(),session_selected_broadcaster));
		
		PlayerCount team = auction.getTeamZoneList().stream().filter(tm->tm.getTeamId() == teaam_id).findAny().orElse(null);
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET " + team.getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET " + "" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET SQUAD\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		for(int i= 1 ; i<=24; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + i +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
		}
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 7\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row1" +
				"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row1"+
				"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "ICON" + "\0");
		
		int row = 2;
		
		Player player = team.getPlayerCategoryWise().get("ICON").get(0);
		if(player==null) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(player)+ "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + player.getFull_name() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$Value*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(player.getSoldForPoints()) + " L \0");
			
		}
		
		row = row + 1;
		
		List<Player> players = team.getPlayer().stream().filter(pl -> pl.getIconic().equalsIgnoreCase("NO") && pl.getRole().equalsIgnoreCase("Batsman"))
				.collect(Collectors.toList()) ;
		
		if(players != null && players.size() >0) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "BATTERS" + "\0");
			
			row = row + 1;
			
			for(Player ply : players) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
				row++;	
			}
		}
		
		if(row==8) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
			row = row + 1;
		}
		
		players = team.getPlayer().stream().filter(pl -> pl.getIconic().equalsIgnoreCase("NO") && pl.getRole().equalsIgnoreCase("Bat/Keeper")||
				pl.getRole().equalsIgnoreCase("Wicket-Keeper")) .collect(Collectors.toList());
		
		if(players != null && players.size() >0) {
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "WICKET - KEEPERS" + "\0");
			
			row = row + 1;
			
			for(Player ply : players) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
				row++;	
			}
			
		}
		
		if(row==16) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
			row = row + 1;
		}

		players = team.getPlayer().stream().filter(pl ->  pl.getIconic().equalsIgnoreCase("NO") && 
				pl.getRole().equalsIgnoreCase("All-Rounder")) .collect(Collectors.toList());
		
		if(players != null && players.size() >0) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "ALL - ROUNDERS" + "\0");
			
			row = row + 1;
			
			for(Player ply : players) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
				row++;	
			}
		}
		
		players = team.getPlayer().stream().filter(pl -> pl.getIconic().equalsIgnoreCase("NO") &&  pl.getRole().equalsIgnoreCase("Bowler")&& 
				PlayerRole(pl).equalsIgnoreCase("SPIN")) .collect(Collectors.toList());
		
		if(players != null && players.size() >0) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "SPINNERS" + "\0");
			
			row = row + 1;
			
			for(Player ply : players) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
				row++;	
			}		
		}
		
		players =  team.getPlayer().stream().filter(pl ->  pl.getIconic().equalsIgnoreCase("NO") && pl.getRole().equalsIgnoreCase("Bowler")&& 
				PlayerRole(pl).equalsIgnoreCase("SEAM")) .collect(Collectors.toList());
		
		if(players != null && players.size() >0) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "PACERS" + "\0");
			
			row = row + 1;
			
			for(Player ply :players) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_Category$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
				row++;	
			}
		}
		
	}

	private void populateFFSquadTeam(boolean is_this_updating,PrintWriter print_writer, int whichSide, Integer teaam_id,
			Auction auction, AuctionService auctionService, String session_selected_broadcaster2) throws Exception {
		
		auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(auction.getTeam(), 
		        auction.getPlayers(), auction.getPlayersList(),session_selected_broadcaster));
		
		PlayerCount team = auction.getTeamZoneList().stream().filter(tm->tm.getTeamId() == teaam_id).findAny().orElse(null);
		
		List<Player> players ;
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET " + team.getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET " + "" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET SQUAD\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 9\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$img_Player*TEXTURE*IMAGE SET "+ photo_path +
				team.getPlayerCategoryWise().get("ICON").get(0).getPhotoName().trim() + AuctionUtil.PNG_EXTENSION + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");

		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$txt_FirstName*GEOM*TEXT SET "+
				(team.getPlayerCategoryWise().get("ICON").get(0).getFirstname().trim()== null ? "" :team.getPlayerCategoryWise().get("ICON").get(0).getFirstname().trim()) + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$txt_LastName*GEOM*TEXT SET "+
				(team.getPlayerCategoryWise().get("ICON").get(0).getSurname().trim()== null ? "" :team.getPlayerCategoryWise().get("ICON").get(0).getSurname().trim()) + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$txt_Category*GEOM*TEXT SET "+
				(team.getPlayerCategoryWise().get("ICON").get(0).getCategory().trim()) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$Price$Value*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$txt_Value*GEOM*TEXT SET "+
				AuctionFunctions.ConvertToLakh(team.getPlayerCategoryWise().get("ICON").get(0).getSoldForPoints()) + " L \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$IconPlayer1$img_Icon*TEXTURE*IMAGE SET " 
				+ setPlayerRole(team.getPlayerCategoryWise().get("ICON").get(0)) + "\0");
		
		for(int i= 1 ; i<=22; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + i +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
		}
		int row = 1;
		
		players = team.getPlayerCategoryWise().get("SENIOR");
		
		if(players!= null && players.size() >0) {
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "SENIOR" + "\0");
			
			for(Player ply : players ) {
				row ++;
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name()+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Value$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
			}
			row = row + 1;
		}
		
		players = team.getPlayerCategoryWise().get("EMERGING");
		
		if(players!= null && players.size() >0) {
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "EMERGING" + "\0");
			
			for(Player ply : players) {
				row ++;
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name()+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Value$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
			}
			
			row =row +1;
		}
		
		
		if(row == 11) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + (row + 1 ) +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 0\0");
			row =row +1;
		}
		
		players = team.getPlayerCategoryWise().get("DEVELOPMENT");
		
		if(players!= null && players.size() >0) {
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
					"$SquadCategoryData$Title$txt_Title*GEOM*TEXT SET " + "DEVELOPMENT" + "\0");
			
			for(Player ply : players) {
				row ++;
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$SlectDataStyle*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(ply)+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_FirstName*GEOM*TEXT SET " + ply.getFull_name()+ "\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$txt_LastName*GEOM*TEXT SET \0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Squad_New$DataAll$Row" + row +
						"$SquadCategoryData$PlayerName$Value$txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + " L \0");
				
			}			
		}
	}
	
	public void ChangeOn(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException {
		
		switch (which_graphics_onscreen.toUpperCase()) {
		//FF	
		case "IDENT": case "PLAYERPROFILE_FF": case "REMAINING_PURSE_ALL": case "SQUAD": case "FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-SINGLEPURSE_TEAM":
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": case "SQUAD_ANIMATION":
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":case "FF_SQUAD_TEAM":case "FF_SQUAD_ROLE_TEAM": case "BG": case "ZONE-PLAYER_FULL":
			//print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Header START\0");
			switch (which_graphics_onscreen.toUpperCase()) {
			case "BG":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$BS_Logo START\0");
				break;
			case "IDENT":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$MatchId$Change_Out START\0");
				break;
			case "PLAYERPROFILE_FF":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile$Change_Out START\0");
				break;
			case "REMAINING_PURSE_ALL":case "FF_RTM_AND_PURSE_REMAINING":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$PurseRemaining$Change_Out START\0");
				break;
			case "SQUAD": case "SQUAD_ANIMATION":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad$Change_Out START\0");
				break;
			case "ZONE-PLAYER_STATS": case "ZONE-PLAYER_FULL":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad START\0");
				break;	
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys$Change_Out START\0");
				break;
			case "FF_SQUAD_TEAM":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_New$Change_Out START\0");
				break;
			case "FF_SQUAD_ROLE_TEAM":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_Category$Change_Out START\0");
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuysImage$Change_Out START\0");
				break;
			case "ANIMATE-SINGLEPURSE_TEAM":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$SinglePurse$Change_Out START\0");
				break;	
			case "FF_ICONIC_PLAYERS":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$IconPlayers START\0");
				break;
			}
			break;
		}
		
		
		
		TimeUnit.MILLISECONDS.sleep(700);
		
		switch (whatToProcess.toUpperCase()) {
		
		//FF	
		case "ANIMATE-IN-ZONE-PLAYER_FULL": case "ANIMATE-IN-ZONE-PLAYER_STATS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad$Change_In START\0");
			break;
		case "ANIMATE-IN-IDENT":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$MatchId$Change_In START\0");
			break;
		case "ANIMATE-SINGLEPURSE_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$SinglePurse$Change_In START\0");
			break;	
		case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-PROFILE_FF":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile$Change_In  START\0");
			}
			break;
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_SQUAD_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_New$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_Category$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuysImage$Change_In START\0");
			break;
			
		case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$PurseRemaining$Change_In  START\0");
			break;
		case "ANIMATE-IN-SQUAD":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad$Change_In  START\0");
			}
			break;
		case "ANIMATE-IN-SQUAD_ANIMATION":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad$Change_In  START\0");
			break;	
		case "ANIMATE-IN-FF_ICONIC_PLAYERS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$IconPlayers$Change_In  START\0");
			break;
			
		}
		TimeUnit.MILLISECONDS.sleep(2000);
	}
	public void cutBack(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException { 
		
		
		switch (whatToProcess.toUpperCase()) {
		
		//FF
		case "ANIMATE-IN-IDENT": case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": 
		case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_ICONIC_PLAYERS": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-ZONE-PLAYER_FULL":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":case "ANIMATE-IN-FF_SQUAD_TEAM": case "ANIMATE-IN-PROFILE_FF":
		case "ANIMATE-IN-SQUAD_ANIMATION":	case "ANIMATE-SINGLEPURSE_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header SHOW 2.480\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Header SHOW 0.0\0");
			switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$MatchId SHOW 0.0\0");
					break;
				case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-PROFILE_FF":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");
					break;
				case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-SQUAD_ANIMATION":
				case "ANIMATE-IN-ZONE-PLAYER_FULL":	
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break; 
				case "ANIMATE-IN-FF_SQUAD_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_New SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_New SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_SQUAD_ROLE_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_Category SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_Category SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuysImage SHOW 0.0\0");
					break;
				case "ANIMATE-SINGLEPURSE_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$SinglePurse SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$SinglePurse SHOW 0.0\0");
					break;	
				case "ANIMATE-IN-FF_ICONIC_PLAYERS":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers SHOW 2.480\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break;
			}
			break;
		}
		
		
		switch (which_graphics_onscreen.toUpperCase()) {
		
		//FF
		case "IDENT":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 0.0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$MatchId SHOW 0.0\0");
			break;
		case "ANIMATE-SINGLEPURSE_TEAM":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$SinglePurse SHOW 0.0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$SinglePurse SHOW 0.0\0");
			break;	
			
		case "PLAYERPROFILE_FF":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_FF") || 
					!whatToProcess.equalsIgnoreCase("ANIMATE-IN-PROFILE_FF")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 0.0\0");
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
			}
			break;
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
			break;
		case "FF_SQUAD_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_SQUAD_TEAM")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_New SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_New SHOW 0.0\0");
			break;
		case "FF_SQUAD_ROLE_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_SQUAD_ROLE_TEAM")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad_Category SHOW 0.0\0");	
			}
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad_Category SHOW 0.0\0");
			break;
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$TopBuysImage SHOW 0.0\0");
			break;
			
		case "REMAINING_PURSE_ALL":case "FF_RTM_AND_PURSE_REMAINING":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-REMAINING_PURSE_ALL") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining SHOW 0.0\0");
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");	
			}
			break;
		case "SQUAD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad SHOW 0.0\0");
			}
			break;
		case "SQUAD_ANIMATION":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD_ANIMATION")) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Squad SHOW 0.0\0");
			}
			break;	
		case "FF_ICONIC_PLAYERS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers SHOW 0.0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$IconPlayers SHOW 0.0\0");
			break;
		}
		prevRowHighlight = rowHighlight;
	}

	public void populateIdent(boolean is_this_updating,PrintWriter print_writer,int which_side,String session_selected_broadcaster) {
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header1$txt_Header1*GEOM*TEXT SET " 
				+ "PLAYER" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header2$txt_Header2*GEOM*TEXT SET " 
				+ "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info1$txt_Info*GEOM*TEXT SET " 
				+ "KABADDI CHAMPIONS LEAGUE" + "\0");
		String venue = "";
	    try {
	        venue = new String(Files.readAllBytes(Paths.get("C:/Sports/Auction/MatchIdent.txt"))).trim();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info2$txt_Info*GEOM*TEXT SET "
				+ venue.toUpperCase() + "\0");
	}
	
	public void populateProfileFF(boolean is_this_updating,PrintWriter print_writer,int which_side, int playerId, String show_stats, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
			
			data.setPlayer_sold_or_unsold(false);
			
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile SHOW 0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Logo SHOW 0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus SHOW 0\0");
			
		}
		
		
		
		if(!auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("A") && 
				!auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("B")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
					+ "$select_Stars*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
					 "CATEGORY C\0");
			if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("2")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 2\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("3")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 3\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("4")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 4\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("5")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 5\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 1\0");
			}
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
					+ "$select_Stars*FUNCTION*Omo*vis_con SET 0\0");
			if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("U19")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET UNDER 19\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
					 "CATEGORY" 	+ " " + auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
			}
		}
		
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType1$txt_Header*GEOM*TEXT SET " + "KCL AUCTION 2025" + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
					+ "*FUNCTION*Omo*vis_con SET 1\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
					+ "*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ "" + "\0");
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice"
				+ "*ACTIVE SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$img_Player"
				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName().trim() + AuctionUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$PlayerRole$txt_Role*GEOM*TEXT SET " + 
				(auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") ? "BATTER" : 
					auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase())  + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "$Side1$Select_Value*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$Side1$"
				+ "Select_Value$txt_Title*GEOM*TEXT SET BASE PRICE\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$Side1$"
				+ "Select_Value$txt_Value*GEOM*TEXT SET " +  
				AuctionFunctions.ConvertToLakh(Double.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice()+"000")).replace(".00", "")+ " K"+"\0");
		
//		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
//				+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
		
		if(auction.getPlayers() != null) {
			if(data.isPlayer_sold_or_unsold() == false) {
				for(Player auc : auction.getPlayers()) {
					if(auc.getPlayerId() == playerId) {
						if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value*FUNCTION*Omo*vis_con SET 2\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status*FUNCTION*Omo*vis_con SET 1\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$select_TeamLogo*FUNCTION*Omo*vis_con SET 1\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side1$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side2$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" K"+"\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$TeamLogo$"
									+ "Side2$img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
							
							data.setPlayer_sold_or_unsold(true);
							
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus START \0");
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Logo START \0");
						}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
									+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value$txt_Title*GEOM*TEXT SET SOLD-RTM\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" K"+"\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
									+ "img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
									+ "img_LogoBase*TEXTURE*IMAGE SET "+logo_base+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
						}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value*FUNCTION*Omo*vis_con SET 2\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status*FUNCTION*Omo*vis_con SET 2\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$select_TeamLogo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side1$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side2$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							
							data.setPlayer_sold_or_unsold(true);
							
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus START \0");
						}
						break;
					}
				}
			}
		}
		
		
		if(show_stats.equalsIgnoreCase("with_info")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 1\0");
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 0\0");
		}
		
		//if(auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam() != null) {
		if(show_stats.equalsIgnoreCase("with_info")) {
			int stats_id = side2ValueToProcess.split(",").length != 3 ? 0 : auctionService.getStatsTypes().stream()
		        .filter(stat -> stat.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[2]))
		        .findAny().map(StatsType::getStats_id).orElse(0);
				
		
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == stats_id) {
					
					
					if(auctionService.getStatsTypes().get(stats_id - 1).getStats_short_name().equalsIgnoreCase("FC")) {
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
								"F-C CAREER" + "\0");
					}else if(auctionService.getStatsTypes().get(stats_id - 1).getStats_short_name().equalsIgnoreCase("LIST A")) {
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
								"LIST A CAREER" + "\0");
					}else if(auctionService.getStatsTypes().get(stats_id - 1).getStats_short_name().equalsIgnoreCase("DT20")) {
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
								"T20 CAREER" + "\0");
					}else if(auctionService.getStatsTypes().get(stats_id - 1).getStats_short_name().equalsIgnoreCase("MCA T20s")) {
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
								"MCA T20s  24-25" + "\0");
					}
					
					
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}else {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET AGE\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
			
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
//					+ auctionService.getAllPlayer().get(playerId - 1).getAge() + "\0");
		}
	}
	
	public void populatePlayerProfileFF(boolean is_this_updating,PrintWriter print_writer,int which_side, int playerId, String show_stats, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
			data.setPlayer_sold_or_unsold(false);
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Profile SHOW 0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Logo SHOW 0\0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus SHOW 0\0");
			
			
		}
		
		
		if(!auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("A") && 
				!auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("B")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
					+ "$select_Stars*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
					 "CATEGORY C\0");
			if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("2")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 2\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("3")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 3\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("4")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 4\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().contains("5")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 5\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
						+ "$select_Stars$select_StarNumber*FUNCTION*Omo*vis_con SET 1\0");
			}
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category"
					+ "$select_Stars*FUNCTION*Omo*vis_con SET 0\0");
			if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("U19")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET UNDER 19\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
					 "CATEGORY" 	+ " " + auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType1$txt_Header*GEOM*TEXT SET " + "KCL AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice"
				+ "*ACTIVE SET 1\0");
		
//		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
//					+ "*FUNCTION*Omo*vis_con SET 1\0");
//		}else {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
//					+ "*FUNCTION*Omo*vis_con SET 0\0");
//		}
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
				+ "*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Id_Grp$txt_StatHead"
				+ "*GEOM*TEXT SET KCL ID\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Id_Grp$txt_StatValue"
				+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getPlayerNumber() + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ "" + "\0");
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$img_Player"
				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName().trim() + AuctionUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$PlayerRole$txt_Role*GEOM*TEXT SET " + 
				(auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") ? "BATTER" : 
					auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase())  + "\0");
		
		
//		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$"
//				+ "$select_Stars*FUNCTION*Omo*vis_con SET 0\0");
//		if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("U19")) {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET UNDER 19\0");
//		}else {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
//				 "CATEGORY" 	+ " " + auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
//		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "Select_Value$txt_Title*GEOM*TEXT SET BASE PRICE\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "Select_Value$txt_Value*GEOM*TEXT SET " +  
				AuctionFunctions.ConvertToLakh(Double.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice()+"000"))+ " K"+"\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
				+ "TeamLogo$select_TeamLogo*FUNCTION*Omo*vis_con SET 0\0");
		
		if(auction.getPlayers() != null) {
			if(data.isPlayer_sold_or_unsold() == false) {
				for(Player auc : auction.getPlayers()) {
					if(auc.getPlayerId() == playerId) {
						if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value*FUNCTION*Omo*vis_con SET 2\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status*FUNCTION*Omo*vis_con SET 1\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$select_TeamLogo*FUNCTION*Omo*vis_con SET 1\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side1$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side2$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" K"+"\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$TeamLogo$"
									+ "Side2$img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
							
							data.setPlayer_sold_or_unsold(true);
							
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus START \0");
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$Logo START \0");
							
						}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
									+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value$txt_Title*GEOM*TEXT SET SOLD-RTM\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
									+ "Select_Value$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" K"+"\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
									+ "img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
									+ "img_LogoBase*TEXTURE*IMAGE SET "+logo_base+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
						}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value*FUNCTION*Omo*vis_con SET 2\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$BasePrice$"
									+ "Side2$Select_Value$Status$select_Status*FUNCTION*Omo*vis_con SET 2\0");
							
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$select_TeamLogo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side1$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Profile$"
									+ "TeamLogo$Side2$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
							
							data.setPlayer_sold_or_unsold(true);
							
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$AuctionStaus START \0");
						}
						break;
					}
				}
			}
		}
		
		
		if(show_stats.equalsIgnoreCase("with_info")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 1\0");
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 0\0");
		}
		
		//if(auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam() != null) {
		if(show_stats.equalsIgnoreCase("with_info")) {
			int stats_id = side2ValueToProcess.split(",").length != 3 ? 0 : auctionService.getStatsTypes().stream()
		        .filter(stat -> stat.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[2]))
		        .findAny().map(StatsType::getStats_id).orElse(0);
				
		
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == stats_id) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}else {
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET AGE\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
//			
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
//					+ auctionService.getAllPlayer().get(playerId - 1).getAge() + "\0");
		}
	}
	
	public void populateFFRTMAndPurseRemaining(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws Exception {
		
		int total = 0;
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$txt_Header1*GEOM*TEXT SET KABADDI CHAMPIONS LEAGUE\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$txt_SubHeader*GEOM*TEXT SET AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Title$txt_Title2*GEOM*TEXT SET SLOTS REMAINING\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Title$txt_Title3*GEOM*TEXT SET PURSE REMAINING\0");
		
		auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(auction.getTeam(), 
				auction.getPlayers(), auction.getPlayersList(),session_selected_broadcaster));
		int row = 0;
		for(PlayerCount tm : auction.getTeamZoneList()) {
			row++;
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$Logo$img_LogoBase*TEXTURE*IMAGE SET "+logo_base+tm.getTeamName4()+"\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$Logo$img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+tm.getTeamName4()+"\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$txt_TeamFirstName*GEOM*TEXT SET "+tm.getTeamName2()+"\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$txt_TeamLastName*GEOM*TEXT SET " + tm.getTeamName3()+"\0");
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$txt_SquadSize*GEOM*TEXT SET " +(14-tm.getPlayers())+"\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team"+ row + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
//			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$Team"+row+"$txt_Value*GEOM*TEXT SET "+
//					AuctionFunctions.ConvertToLakh(tm.getRemaingPurse())+" L\0");
			
			if(auction.getPlayers() != null ) {
				for(int j=0; j <= auction.getPlayers().size()-1; j++) {
					if(auction.getPlayers().get(j).getTeamId() == tm.getTeamId()) {
						total = total + auction.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team" + row + "$Value$txt_Value"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - total)) + " K" + "\0");
			
			if((Integer.valueOf(tm.getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team" 
						+ row + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}else if((Integer.valueOf(tm.getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team" 
						+ row + "$Value$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team" 
						+ row + "$Value$txt_Value*GEOM*TEXT SET " + "-" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRemaining$DataAll$Team" 
						+ row + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}
			total = 0;
		}
		
	}
	public void populateFFTopBuysAuction(boolean is_this_updating,PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET KABADDI CHAMPIONS LEAGUE AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0");
		
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				if(row < 9 && checkIconPlayer(plyr.getPlayerId(), auctionService)) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
	        		}
	        		
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*ACTIVE SET 0\0");
				}
			}
		}
	}
	public void populateFFFiveTopBuysAuction(boolean is_this_updating,PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}

		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET "
				+ "KCL AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8\0");
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		
		for(int i =1;i<=5;i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + i + 
					"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				if(row < 6 && checkIconPlayer(plyr.getPlayerId(), auctionService)) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
							"*ACTIVE SET 1\0");
					if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+""+"\0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
	        		
//	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//		        				"$txt_Category*GEOM*TEXT SET "+"UNDER 19"+"\0");
//	        		}else {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//		        				"$txt_Category*GEOM*TEXT SET CATEGORY: "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
//	        		}
	        		
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("A") || 
	        				auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("B")) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$txt_Category*GEOM*TEXT SET CATEGORY: "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$txt_Category*GEOM*TEXT SET CATEGORY: C\0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$Data1$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp"
	        				+ "$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_TeamLogo"
							+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$Icon*ACTIVE SET 0\0");
				}
			}
		}
	}
	public void populateFFTopFiveBuysTeam(boolean is_this_updating,PrintWriter print_writer, int whichSide, int team_id, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == team_id).findAny().orElse(null);
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET " + team.getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET " + "" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET TOP BUYS\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8\0");
		
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		
		for(int i =1;i<=5;i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + i + 
					"*ACTIVE SET 0\0");
		} 
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				if(row < 6 && checkIconPlayer(plyr.getPlayerId(), auctionService)) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
							"*ACTIVE SET 1\0");
					if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+""+"\0");
	        		}
	        		
//	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//	        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
//	        		}else {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//	        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
//	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
	        		
//	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//		        				"$txt_Category*GEOM*TEXT SET "+"UNDER 19"+"\0");
//	        		}else {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
//		        				"$txt_Category*GEOM*TEXT SET CATEGORY: "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
//	        		}
	        		
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("A") || 
	        				auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("B")) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$txt_Category*GEOM*TEXT SET CATEGORY: "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
		        				"$txt_Category*GEOM*TEXT SET CATEGORY: C\0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$Data2$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp"
	        				+ "$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	    					"$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(auctionService.getAllPlayer().get(plyr.getPlayerId()-1)) +" \0");
				}
			}
		}
	}
	
	public void populateFFTopBuysTeam(boolean is_this_updating,PrintWriter print_writer, int whichSide, int teamId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}	
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET " + team.getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET " + "" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET TOP BUYS\0");
			
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET CATEGORY\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0");
		
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == teamId) {
					top_sold.add(plyr);
				}
			}
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				if(row < 9 && checkIconPlayer(plyr.getPlayerId(), auctionService)) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
	        		}
//	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
//	        		}else {
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
//	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
//	        		}
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
	    			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$"
	    					+ "Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
	    			
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET UNDER 19\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET CATEGORY: "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
	        		}
	        		
	        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().contains("C")) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET CATEGORY: C\0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
	        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "
	    					+setPlayerRole(auctionService.getAllPlayer().get(plyr.getPlayerId()-1))+" \0");
				}
			}
		}
	}
	public void populateFFIconicPlayers(boolean is_this_updating,PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET ICON PLAYERS\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET T20 MUMBAI LEAGUE\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6\0");
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Players$Player" + i + "*ACTIVE SET 0\0");
		}
		
		for(Player player : auction.getPlayersList()) {
			if(player.getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				row = row + 1;
				if(row <= 8) {
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + "*ACTIVE SET 1\0");
					if(player.getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET "+player.getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET \0");
	        		}
					
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + player.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + "$txt_Category*GEOM*TEXT SET \0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
							"$Icon$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(player) + "\0");
				 
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data1$txt_Value*GEOM*TEXT SET "+
							AuctionFunctions.ConvertToLakh(Double.valueOf(player.getBasePrice()+"000"))+"L"+"\0");
					for(Player plyr : top_sold) {
						if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							if(player.getPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$"
										+ "Price*ACTIVE SET 0\0");
								
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$"
										+ "txt_Value*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_TeamLogo"
										+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_LogoBase"
										+ "*TEXTURE*IMAGE SET " + logo_base + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
								print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$txt_Category*GEOM*TEXT SET " + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1() + "\0");
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void populateLTIconicPlayers(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$Header$NameGrp$txt_Title*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$Header$NameGrp$txt_Title*GEOM*TEXT SET " + "ICON PLAYERS" + "\0");
		
		for(Player player : auction.getPlayersList()) {
			if(player.getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				row = row + 1;
				if(row <= 8) {
					if(player.getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET "+player.getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET \0");
	        		}
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$ImageGrp$Select_TeamLogo"
							+ "*FUNCTION*Omo*vis_con SET 0\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + player.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$txt_Category*GEOM*TEXT SET " +
							player.getCategory().toUpperCase() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
							"$Icon$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(player) + "\0");
					 
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data1$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(Double.valueOf(player.getBasePrice()+"000")).replace(".00", "")+"L"+"\0");
					for(Player plyr : top_sold) {
						if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							if(player.getPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$ImageGrp$Select_TeamLogo"
										+ "*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data2$Price*ACTIVE SET 0\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data2$txt_Value*GEOM*TEXT SET "
										+ AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" K"+"\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data2$txt_TeamName*GEOM*TEXT SET "
										+ auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
								
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + "$ImageGrp$img_TeamLogo"
										+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4() + "\0");
								break;
							}
						}
					}
				}
			}
		}
	}
	public void populatePlayerProfileLT(PrintWriter print_writer,int which_side, int playerId, String show_stats,int st, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int team_id = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		//System.out.println(top_sold.size());
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
		}
		
		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
				"KABADDI CHAMPIONS LEAGUE" + "\0");
		
		if(show_stats.equalsIgnoreCase("category")) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLOGO\0");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
		}
		else if(show_stats.equalsIgnoreCase("player")) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLOGO\0");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					"T20 MUMBAI LEAGUE" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			for(Player plyr : top_sold) {
				System.out.println("plyr = " + plyr.getPlayerId() + " id = " + playerId);
				if(Integer.valueOf(plyr.getPlayerId()) == Integer.valueOf(playerId)) {
					System.out.println("plyrinside = " + plyr.getPlayerId() + " id = " + playerId);
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " K" + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName4() + "\0");
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE - UNSOLD" + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLOGO\0");
						
					}
				}
			}
		}
		else if(show_stats.equalsIgnoreCase("prevteam")) {
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			team_id = auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam();
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
					"ISPL SEASON 1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
					auction.getTeam().get(team_id-1).getTeamName1() + "\0");
			
			String price = auctionService.getAllPlayer().get(playerId - 1).getLastYearPrice() + "000";
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(Double.valueOf(price)) + " K" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("stats")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
			
			if(which_stat.equalsIgnoreCase("FC")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET F-C CAREER\0");
			}else if(which_stat.equalsIgnoreCase("LIST A")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET LIST A CAREER\0");
			}else if(which_stat.equalsIgnoreCase("DT20")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET T20 CAREER\0");
			}else if(which_stat.equalsIgnoreCase("MCA T20s")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET MCA T20s  24-25\0");
			}
			
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == st) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}
	}
	public void ChangeOnLTStats(PrintWriter print_writer,int which_side, int playerId, String show_stats,int sta, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int team_id = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
		}
		
		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		if(show_stats.equalsIgnoreCase("category")) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
		}
		else if(show_stats.equalsIgnoreCase("player")) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					"KABADDI CHAMPIONS LEAGUE" + "\0");
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			for(Player plyr : top_sold) {
				if(plyr.getPlayerId() == playerId) {
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " K" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName4() + "\0");
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE - UNSOLD" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
						
					}
				}
//				break;
			}
		}
		else if(show_stats.equalsIgnoreCase("prevteam")) {
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().equalsIgnoreCase("Batsman")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET BATTER\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
			}
			
			team_id = auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam();
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
					"ISPL SEASON 1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
					auction.getTeam().get(team_id-1).getTeamName1() + "\0");
			
			String price = auctionService.getAllPlayer().get(playerId - 1).getLastYearPrice() + "000";
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(Double.valueOf(price)) + " K" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("stats")) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$Logo$Side" + which_side + "$img_Logo*TEXTURE*IMAGE SET " + logo_path + "TLogo\0");
			
			if(which_stat.equalsIgnoreCase("FC")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET F-C CAREER\0");
			}else if(which_stat.equalsIgnoreCase("LIST A")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET LIST A CAREER\0");
			}else if(which_stat.equalsIgnoreCase("DT20")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET T20\0");
			}else if(which_stat.equalsIgnoreCase("MCA T20s")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET MCA T20s  24-25\0");
			}
			
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == sta) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}
	}
	
	public void populateTopSoldTeam(boolean is_this_updating,PrintWriter print_writer,String viz_scene,int team_id , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int row = 0;
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		
		for(int i = 1; i <= 5; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TopText*GEOM*TEXT SET "+ "TOP BUYS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "T20 MUMBAI 2025 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "T20 MUMBAI 2025 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auction.getTeam().get(team_id-1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auction.getTeam().get(team_id-1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$AllBase$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase("SOLD")) {
				row = row + 1;
	        	if(row <= 5) {
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1 \0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerFirstName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerLastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + "\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerFirstName*GEOM*TEXT SET "+ "" + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerLastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        		}
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints())  + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
	        		
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ 
	        				setPlayerRole(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1)) + "\0");
	        	}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.200 \0");
	}
	public void populateTopSold(boolean is_this_updating,PrintWriter print_writer,String viz_scene, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ "BUYS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "T20 MUMBAI 2025 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "T20 MUMBAI 2025 PLAYER AUCTION" + "\0");
		for(int i=1; i<=10; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0 \0");
		}
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase("SOLD")) {
				row = row + 1;
	        	if(row <= 10) {
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1 \0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_TeamName*GEOM*TEXT SET "+ auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName1() + "\0");
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() + "\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        		}
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints())  + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Logo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ setPlayerRole(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1)) + "\0");
	        		
	        	}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.700 \0");
	}
	public void populateRemainingPurse(boolean is_this_updating,PrintWriter print_writer, int which_side, Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int total = 0;
		int row = 0;
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType3$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + "KABADDI CHAMPIONS LEAGUE" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType3$Header$txt_Header2"
				+ "*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType3$SubHeader$txt_SubHeader"
				+ "*GEOM*TEXT SET " + "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title2*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title3*GEOM*TEXT SET PURSE REMAINING\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamFirstName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamLastName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName3() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(i).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_LogoBase*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(i).getTeamName4() + "\0");
			
			if(match.getPlayers() != null ) {
				for(int j=0; j <= match.getPlayers().size()-1; j++) {
					if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
						row = row + 1;
						total = total + match.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$txt_SquadSize*GEOM*TEXT SET " + row + "\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1) + "$Value$txt_Value"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total)) + " K" + "\0");
			
			if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}else if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$txt_Value*GEOM*TEXT SET " + "-" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}
			row = 0;
			total = 0;
		}
	}
	
	public void populateSquad(boolean is_this_updating,PrintWriter print_writer,int team_id,String sub, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int row = 0;
		
		int total = 0;
		
		
		for(int j=0; j <= match.getPlayers().size()-1; j++) {
			if(match.getPlayers().get(j).getTeamId() == team_id) {
				total = total + match.getPlayers().get(j).getSoldForPoints();
			}
		}
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET " + "" + "\0");
		
		if(sub.equalsIgnoreCase("PurseRemaining")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
					+ "$txt_SubHeader*GEOM*TEXT SET PURSE REMAINING : " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - total)) + " K\0");
			
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
					+ "$txt_SubHeader*GEOM*TEXT SET SQUAD\0");
			
		}
//		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
//				+ "$txt_SubHeader*GEOM*TEXT SET PURSE REMAINING : " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - total)) + " K\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		
		
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataKCLInZone(match,team_id);
		
		List<String> count = new ArrayList<String>();
		
		Auction session_auction = match;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList(),session_selected_broadcaster));
		
		
		for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
			PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 
		    if (teamZone.getTeamId() == team_id) {
		        
		     // Team Total Value 
		        print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$txt_SquadSize"
		        	+ "*GEOM*TEXT SET " + teamZone.getPlayers() + "\0");

		        for(int k=1; k<=2; k++) {
		        	count = new ArrayList<String>();
		        	int j=0;
		        	int cTotal = 0;
		        	
		        	for (Map.Entry<String, Integer> entry : teamZone.getCategory().entrySet()) {
		        		if(entry.getKey().trim().contains("C")) {
		        			cTotal += entry.getValue();
		        		}else {
		        			count.add(entry.getKey() + "-" + entry.getValue());
		        		}
		        	}
		        	count.add(2, "C" + "-" + cTotal);
		        	for(int s=0;s<=count.size()-1;s++) {
		        		j++;
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Zone*GEOM*TEXT SET " + count.get(s).split("-")[0] + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Value*GEOM*TEXT SET " + count.get(s).split("-")[1] + "\0");
		        	}
		        	
		        	
		        	print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category" 
			            		+"*FUNCTION*Grid*num_row SET 3\0");
		        }
		    } 
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$4" 
        		+"*ACTIVE SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");
		
		for(int k=0;k<=data_str.size()-1;k++) {
			row = row + 1;
			if(data_str.get(k).equalsIgnoreCase("A") || data_str.get(k).equalsIgnoreCase("B") || data_str.get(k).equalsIgnoreCase("C") || 
					data_str.get(k).equalsIgnoreCase("ZONE")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
				
				switch(data_str.get(k)) {
				case "A":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "A" + "\0");
					break;
				case "B":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "B" + "\0");
					break;
				case "C":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "C" + "\0");
					break;
				case "ZONE":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + row + "\0");
					break;
				}
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				for(Player plyr : match.getPlayersList()) {
					if(plyr.getPlayerId() == Integer.valueOf(data_str.get(k))) {
						if(plyr.getSurname() != null) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
						}else {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + "" + "\0");
						}
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
								+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
						
						if(plyr.getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET UNDER 19\0");
						}else {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET CATEGORY: " + plyr.getCategory().toUpperCase() + "\0");
						}
						
						if(plyr.getCategory().contains("C")) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET CATEGORY: C\0");
						}
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon"
								+ "*ACTIVE SET 0\0");
								
//						if(plyr.getIconic().equalsIgnoreCase("YES")){
//							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
//									+ "*ACTIVE SET 1\0");
//						}else {
//							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
//									+ "*ACTIVE SET 0\0");
//						}
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
								+ "*ACTIVE SET 0\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
								"$Icon$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(plyr) + "\0");
					}
				}
			}
		}
	}
	
	public void populateSquadAnimation(boolean is_this_updating,PrintWriter print_writer,int team_id,String sub, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int row = 0;
		
		int total = 0;
		
		
		for(int j=0; j <= match.getPlayers().size()-1; j++) {
			if(match.getPlayers().get(j).getTeamId() == team_id) {
				total = total + match.getPlayers().get(j).getSoldForPoints();
			}
		}
		
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName1() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET " + "" + "\0");
		
		if(sub.equalsIgnoreCase("PurseRemaining")) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
					+ "$txt_SubHeader*GEOM*TEXT SET PURSE REMAINING : " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - total)) + " K\0");
			
		}else {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
					+ "$txt_SubHeader*GEOM*TEXT SET SQUAD\0");
			
		}
		
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		
		
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataKCLInZone(match,team_id);
		
		List<String> count = new ArrayList<String>();
		
		Auction session_auction = match;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList(),session_selected_broadcaster));
		
		for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
			PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 
		    if (teamZone.getTeamId() == team_id) {
		        
		     // Team Total Value 
		        print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$txt_SquadSize"
		        	+ "*GEOM*TEXT SET " + teamZone.getPlayers() + "\0");

		        for(int k=1; k<=2; k++) {
		        	count = new ArrayList<String>();
		        	int j=0;
		        	int cTotal = 0;
		        	
		        	for (Map.Entry<String, Integer> entry : teamZone.getCategory().entrySet()) {
		        		if(entry.getKey().trim().contains("C")) {
		        			cTotal += entry.getValue();
		        		}else {
		        			count.add(entry.getKey() + "-" + entry.getValue());
		        		}
		        	}
		        	count.add(2, "C" + "-" + cTotal);
		        	for(int s=0;s<=count.size()-1;s++) {
		        		j++;
		        		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Zone*GEOM*TEXT SET " + count.get(s).split("-")[0] + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Value*GEOM*TEXT SET " + count.get(s).split("-")[1] + "\0");
		        	}
		        	
		        	
		        	print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category" 
			            		+"*FUNCTION*Grid*num_row SET 3\0");
		        }
		    } 
		}
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$4" 
        		+"*ACTIVE SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");
		
		for(int k=0;k<=data_str.size()-1;k++) {
			row = row + 1;
			if(data_str.get(k).equalsIgnoreCase("A") || data_str.get(k).equalsIgnoreCase("B") || data_str.get(k).equalsIgnoreCase("C") || 
					data_str.get(k).equalsIgnoreCase("ZONE")) {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
				
				switch(data_str.get(k)) {
				case "A":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "A" + "\0");
					break;
				case "B":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "B" + "\0");
					break;
				case "C":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "C" + "\0");
					break;
				case "ZONE":
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + row + "\0");
					break;
				}
			}else {
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				for(Player plyr : match.getPlayersList()) {
					if(plyr.getPlayerId() == Integer.valueOf(data_str.get(k))) {
						if(plyr.getSurname() != null) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
						}else {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + "" + "\0");
						}
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
								+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
						
						if(plyr.getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET UNDER 19\0");
						}else {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET CATEGORY: " + plyr.getCategory().toUpperCase() + "\0");
						}
						
						if(plyr.getCategory().contains("C")) {
							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET CATEGORY: C\0");
						}
						
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon"
								+ "*ACTIVE SET 0\0");
								
//						if(plyr.getIconic().equalsIgnoreCase("YES")){
//							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
//									+ "*ACTIVE SET 1\0");
//						}else {
//							print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
//									+ "*ACTIVE SET 0\0");
//						}
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
								+ "*ACTIVE SET 0\0");
						print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
								"$Icon$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(plyr) + "\0");
					}
				}
			}
		}
	}
	public void populateZoneSquad(boolean is_this_updating,PrintWriter print_writer,String ZoneName, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		if(is_this_updating == false) {
			
			if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
				update_gfx = false;
			}
		}
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET CATEGORY: " + ZoneName.toUpperCase() + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET " + "" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
				+ "$txt_SubHeader*GEOM*TEXT SET " + "REMAINING PLAYERS" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "TLogo" + "\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "TLogo" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");

		for(int i=1;i<=18;i++) {
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Blank$NameGrp$txt_LastName*GEOM*TEXT SET " + "" + "\0");
		}
	    for(int k = current_index; k<= squad.size()-1 ;k++) {
		   Player plyr = squad.get(k);
				row = row + 1;
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				
				if(plyr.getSurname() != null) {
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + "" + "\0");
				}
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon"
						+ "*ACTIVE SET 0\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
						+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
						+ "*GEOM*TEXT SET " + "" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
						+ "*ACTIVE SET 0\0");
				print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
						"$Icon$img_Icon*TEXTURE*IMAGE SET " + setPlayerRole(plyr) + "\0");
				
				if(row == 14) {
					break;
				}
			}
		if(which_side == 1) {
			current_index =(current_index + 14);
		}
	}

	
	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException
	{
		
		switch(whichGraphic) {
		case "FFPLAYERPROFILE":	
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
			break;
		
		case "RESET":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reset START \0");
			break;
		
		}	
	}	
	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");	
	}
	
	public void processPreview(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			if(data.isData_on_screen() && !whatToProcess.equalsIgnoreCase("POPULATE-FF-PLAYERPROFILE")) {
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Right_Data 2.500 anim_ScoreBug$In_Out$Right_Data$In 1.720 anim_ScoreBug$In_Out$CenterData 2.500 anim_ScoreBug$In_Out$CenterData$BasePriceHead 2.500 "
						+ "anim_ScoreBug$In_Out$CenterData$BasePriceHead$in 1.600 ";
			}
			if(isProfileStatsOnScreen) {
				previewCommand =previewCommand + "anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760 ";
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF-PLAYERPROFILE":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Right_Data 2.500 anim_ScoreBug$In_Out$Right_Data$In 1.720 anim_ScoreBug$In_Out$CenterData 2.500 anim_ScoreBug$In_Out$CenterData$BasePriceHead 2.500 "
						+ "anim_ScoreBug$In_Out$CenterData$BasePriceHead$in 1.600";
				break;
			case "POPULATE-PROFILE_STATS": case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = previewCommand + "anim_RTM$In_Out 0.500 anim_RTM$In_Out$Essentials 0.500 anim_RTM$In_Out$Essentials$In 0.500 anim_RTM$In_Out$Text$In 0.500";
				break;
			case "POPULATE-GOOGLY_POWER":
				previewCommand = previewCommand + "anim_Googly$In_Out 0.500 anim_Googly$In_Out$In 0.500";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": 
			case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": 
			case "POPULATE-LOF_SQUAD": case "POPULATE-ZONEWISE_PLAYERS_SOLD":case "POPULATE-LOF_TEAM_BID_AUCTION":
				previewCommand = previewCommand + "anim_LOF$In_Out 2.000 anim_LOF$In_Out$Essentials 2.000 anim_LOF$In_Out$Essentials$In 1.100 "
						+ "anim_LOF$In_Out$Header 2.000 anim_LOF$In_Out$Header$In 1.200 anim_LOF$In_Out$SubHeader 2.000 anim_LOF$In_Out$SubHeader$In 1.300 "
						+ "anim_LOF$In_Out$Main 2.000 ";
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING":
				case "POPULATE-ZONEWISE_PLAYERS_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$RemainingPurse 2.000 anim_LOF$In_Out$Main$RemainingPurse$In 2.000 "
							+ "anim_LOF$In_Out$Main$Name 2.000 anim_LOF$In_Out$Main$Name$In 1.100 anim_LOF$In_Out$Main$Logo 2.000 anim_LOF$In_Out$Main$Logo$In 1.100";
					break;
				case "POPULATE-LOF_TEAM_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$TopBuysTeam 2.000 anim_LOF$In_Out$Main$TopBuysTeam$In 1.700";
					break;
				case "POPULATE-LOF_TEAM_BID_AUCTION":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$PurseRemaining_Small 2.000 anim_LOF$In_Out$Main$PurseRemaining_Small$In 1.700";
					break;
				case "POPULATE-LOF_SQUAD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$Squad 2.000 anim_LOF$In_Out$Main$Squad$In 1.960";
					break;
				case "POPULATE-LOF_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$TopBuys 2.000 anim_LOF$In_Out$Main$TopBuys$In 1.700";
					break;
				case "POPULATE-SQUAD-PLAYER":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Category 2.000 anim_LOF$In_Out$Main$SquadSize_Category$In 1.900 "
							+ "CtegoryHighlight$Side1$"+rowHighlight +" 1.200";
					for(int i = rowHighlight; i<=7; i++) {
						previewCommand = previewCommand + " MoveForCatHighlight$Side1$"+(i+1) +" 1.200";
					}
					break;
				case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Team 2.000 anim_LOF$In_Out$Main$SquadSize_Team$In 1.880";
					break;
				}
				break;
			}
		}else {
			if(data.isData_on_screen() && !whatToProcess.equalsIgnoreCase("POPULATE-FF-PLAYERPROFILE")) {
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Right_Data 2.500 anim_ScoreBug$In_Out$Right_Data$In 1.720 anim_ScoreBug$In_Out$CenterData 2.500 "
						+ "anim_ScoreBug$In_Out$CenterData$BasePriceHead 2.500 anim_ScoreBug$In_Out$CenterData$BasePriceHead$in 1.600 ";
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING":
			case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD": case "SQUAD-PLAYER":case "LOF_TEAM_BID_AUCTION":
				previewCommand = previewCommand + "Change_LOF$Header$Change_Out 0.500 Change_LOF$Header$Change_In 1.000 Change_LOF$SubHeader$Change_Out 0.500 "
						+ "Change_LOF$SubHeader$Change_In 1.000 ";
				break;
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_Out 0.860 Change_LOF$Name$Change_Out 0.600 Change_LOF$Logo$Change_Out 0.600 ";
				break;
			case "LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$TopBuysTeam$Change_Out 0.680 ";
				break;
			case "LOF_TEAM_BID_AUCTION":
				previewCommand = previewCommand + "Change_LOF$PurseRemaining_Small$Change_Out 0.660 ";
				break;
			case "LOF_SQUAD":
				previewCommand = previewCommand + "Change_LOF$Squad$Change_Out 0.680 ";
				break;
			case "LOF_TOP_SOLD": 
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_Out 0.680 ";
				break;
			case "SQUAD-PLAYER":
				if(!whatToProcess.equalsIgnoreCase("POPULATE-SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_Out 0.800 CtegoryHighlight$Side1$" + prevRowHighlight + " 1.200 ";
					for(int i=prevRowHighlight;i<=7;i++) {
						previewCommand = previewCommand + "MoveForCatHighlight$Side1$" + (i+1) + " 1.000 ";
					}
				}
//				else {
//					previewCommand = previewCommand + "CtegoryHighlight$Side1$" + prevRowHighlight + " 0.0";
//					for(int i=prevRowHighlight;i<=7;i++) {
//						previewCommand = previewCommand + " MoveForCatHighlight$Side1$" + (i+1) + " 0.0";
//					}
//				}
				break;
			case "LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_Out 0.660 ";
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS": case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "Change$Stats$Change_Out 1.000 Change$Stats$Change_In 1.300";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING":
			case "POPULATE-ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_In 1.720 Change_LOF$RemainingPurse$Change_In$In 1.720 Change_LOF$Name$Change_In 1.100 "
						+ "Change_LOF$Name$Change_In$In 1.100 Change_LOF$Logo$Change_In 1.100 Change_LOF$Logo$Change_In$In 1.100";
				break;
			case "POPULATE-LOF_TEAM_TOP_SOLD": 
				previewCommand = previewCommand + "Change_LOF$TopBuysTeam$Change_In 1.700 Change_LOF$TopBuysTeam$Change_In$In 1.700";
				break;
			case "POPULATE-LOF_TEAM_BID_AUCTION":
				previewCommand = previewCommand + "Change_LOF$PurseRemaining_Small$Change_In 1.640";
				break;
			case "POPULATE-LOF_SQUAD": case "POPULATE-LOF_SQUAD_REMAIN":
				previewCommand = previewCommand + "Change_LOF$Squad$Change_In 1.960 Change_LOF$Squad$Change_In$In 1.960";
				break;
			case "POPULATE-LOF_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_In 1.700 Change_LOF$TopBuys$Change_In$In 1.700";
				break;
			case "POPULATE-SQUAD-PLAYER":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_In 1.900 Change_LOF$SquadSize_Category$Change_In$In 1.900 "
							+ "CtegoryHighlight$Side2$" + rowHighlight + " 1.200";
					for(int i=rowHighlight;i<=7;i++) {
						previewCommand = previewCommand + " MoveForCatHighlight$Side2$" + (i+1) + " 1.000";
					}
				}
//				else {
//					previewCommand = previewCommand + " CtegoryHighlight$Side1$" + rowHighlight + " 1.200";
//					for(int i=rowHighlight;i<=7;i++) {
//						previewCommand = previewCommand + " MoveForCatHighlight$Side1$" + (i+1) + " 1.000";
//					}
//				}
				break;
			case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_In 1.640 Change_LOF$SquadSize_Team$Change_In$In 1.640";
				break;
			}	
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
		
	}
	public void processPreviewFullFrames(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT": case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-FF_RTM_AND_PURSE_REMAINING": case "POPULATE-FF_TOP_BUYS_AUCTION": 
			case "POPULATE-FF_TOP_BUY_TEAM": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SQUAD": case "POPULATE-FF_ICONIC_PLAYERS":
			case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":case "POPULATE-FF_SQUAD_TEAM":
			case "POPULATE-FF_SQUAD_ROLE_TEAM": case "POPULATE-PROFILE_FF": case "POPULATE-FF_SINGLEPURSE_TEAM":
				previewCommand = "anim_Fullframe$In_Out 2.480 anim_Fullframe$In_Out$Essentials 2.480 anim_Fullframe$In_Out$Essentials$In 1.300 ";
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-IDENT":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Main$MatchId 2.480 anim_Fullframe$In_Out$Main$MatchId$In 2.340";
					break;
				case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-PROFILE_FF":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Profile 2.480 anim_Fullframe$In_Out$Main$Profile$In 2.180";
					break;
//				case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
//					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
//							+ "anim_Fullframe$In_Out$Main$PurseRTM 2.480 anim_Fullframe$In_Out$Main$PurseRTM$In 2.100";
//					break;
				case "POPULATE-FF_TOP_BUYS_AUCTION": case "POPULATE-FF_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$TopBuys 2.480 anim_Fullframe$In_Out$Main$TopBuys$In 2.300";
					break;
				case "POPULATE-FF_SQUAD_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Squad_New 2.480 anim_Fullframe$In_Out$Main$Squad_New$In 2.300";
					break;
				case "POPULATE-FF_SQUAD_ROLE_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Squad_Category 2.480 anim_Fullframe$In_Out$Main$Squad_Category$In 2.300";
					break;
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$TopBuysImage 2.480 anim_Fullframe$In_Out$Main$TopBuysImage$In 1.880";
					break;
				case "POPULATE-REMAINING_PURSE_ALL":case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$PurseRemaining 2.480 anim_Fullframe$In_Out$Main$PurseRemaining$In 2.100";
					break;
				case "POPULATE-SQUAD": case "POPULATE-ZONE_PLAYERS_STATS":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Squad 2.480 anim_Fullframe$In_Out$Main$Squad$In 2.000";
					break;
				case "POPULATE-FF_ICONIC_PLAYERS":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$IconPlayers 2.480 anim_Fullframe$In_Out$Main$IconPlayers$In 2.000";
					break;
				case "POPULATE-FF_SINGLEPURSE_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$SinglePurse 2.480 anim_Fullframe$In_Out$Main$SinglePurse$In 1.900";
					break;
				}
				break;
			}
		}else {
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$MatchId 2.340 Change$MatchId$Change_Out 0.700 Change$MatchId$Change_In 2.340 ";
				break;
			case "PLAYERPROFILE_FF":
				previewCommand = "anim_Fullframe$In_Out$Header 0.0 anim_Fullframe$In_Out$Header$In 0.0 Change$Profile 2.180 "
						+ "Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$TopBuys 2.300 Change$TopBuys$Change_Out 0.820 ";
				break;
			case "FF_SQUAD_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$Squad_New 2.300 Change$Squad_New$Change_Out 0.820 ";
				break;
			case "FF_SQUAD_ROLE_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$Squad_Category 2.300 Change$Squad_Category$Change_Out 0.820 ";
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$TopBuysImage 1.880 Change$TopBuysImage$Change_Out 0.920 ";
				break;
			case "REMAINING_PURSE_ALL":case "FF_RTM_AND_PURSE_REMAINING":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$PurseRemaining 2.100 Change$PurseRemaining$Change_Out 0.740 Change$PurseRemaining$Change_In 2.100 ";
				break;
			case "SQUAD": case "ZONE-PLAYER_STATS":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$Squad 2.000 Change$Squad$Change_Out 0.560 Change$Squad$Change_In 2.000 ";
				break;
			case "FF_ICONIC_PLAYERS":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$IconPlayers 2.000 Change$IconPlayers$Change_Out 0.560 Change$IconPlayers$Change_In 2.000 ";
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT":
				previewCommand = previewCommand + "Change$MatchId 2.340 Change$MatchId$Change_Out 0.700 Change$MatchId$Change_In 2.340 ";
				break;
			case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-PROFILE_FF":
				if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
					previewCommand = previewCommand + "Change$Profile 2.180 Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				}
				break;
			case "POPULATE-REMAINING_PURSE_ALL":case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
				if(!which_graphics_onscreen.equalsIgnoreCase("REMAINING_PURSE_ALL") && !which_graphics_onscreen.equalsIgnoreCase("FF_RTM_AND_PURSE_REMAINING")) {
					previewCommand = previewCommand + "Change$PurseRemaining 2.100 Change$PurseRemaining$Change_Out 0.740 Change$PurseRemaining$Change_In 2.100 ";
				}
				break;
			case "POPULATE-SQUAD": case "POPULATE-ZONE_PLAYERS_STATS":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD") && !which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS")) {
					previewCommand = previewCommand + "Change$Squad 2.000 Change$Squad$Change_Out 0.560 Change$Squad$Change_In 2.000 ";
				}
				break;
//			case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
//				previewCommand = previewCommand + "Change$PurseRTM 2.100 Change$PurseRTM$Change_Out 0.740 Change$PurseRTM$Change_In 2.100";
//				break;
			case "POPULATE-FF_TOP_BUYS_AUCTION": case "POPULATE-FF_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$TopBuys$Change_In 2.300";
				break;
			case "POPULATE-FF_SQUAD_TEAM":
				previewCommand = previewCommand + "Change$Squad_New$Change_In 2.300";
				break;
			case "POPULATE-FF_SQUAD_ROLE_TEAM":
				previewCommand = previewCommand + "Change$Squad_Category$Change_In 2.300";
				break;
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$TopBuysImage$Change_In 1.880";
				break;
			case "POPULATE-FF_ICONIC_PLAYERS":
				previewCommand = previewCommand + "Change$IconPlayers 2.000 Change$IconPlayers$Change_Out 0.560 Change$IconPlayers$Change_In 2.000 ";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/FullFrames " + "C:/Temp/Preview.tga " + previewCommand + "\0");
	}
	
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	public void resetData(PrintWriter print_writer) {
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Main$Side1$Select_GraphicsType*FUNCTION*Omo*vis_con SET 10\0");
		print_writer.println("-1 RENDERER*TREE*$gfx_FullFrames$Header$Side1$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials START\0");
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header START\0");
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$BS_Logo START\0");
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Loop START \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side1" + 
				"$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side2" + 
				"$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0");
		
		which_graphics_onscreen = "BG";
	}
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	public String PlayerRole (Player player) {
		String Style = "";
		if(player.getBowlerStyle() != null) {
			if(player.getBowlerStyle().contains("FM") || player.getBowlerStyle().contains("MF")
					 || player.getBowlerStyle().contains("SM")  || player.getBowlerStyle().contains("RF")
					 || player.getBowlerStyle().contains("LF") || player.getBowlerStyle().contains("LM") ||
					 player.getBowlerStyle().contains("F") || player.getBowlerStyle().contains("M")) {
				Style = "SEAM";
			}
			
			if(player.getBowlerStyle().contains("SL") || player.getBowlerStyle().contains("SO") 
					 || player.getBowlerStyle().contains("CH")  || player.getBowlerStyle().contains("LB")
					 || player.getBowlerStyle().contains("LG")  || player.getBowlerStyle().contains("OB")
					 || player.getBowlerStyle().contains("WSL") || player.getBowlerStyle().contains("WSR")) {
				Style = "SPIN";
			}
		}else {
			Style = "SEAM";
		}
		return Style;
	}
	public String setPlayerRole (Player ply) {
		String role = "";
		if(ply.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
			role =   icon_path + "Keeper" ;
		}else if(ply.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
			role =   icon_path + "KeeperAllrounder" ;
		}
		else if(ply.getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
			if(ply.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
				role = icon_path + "Batsman" ;
			}
			else if(ply.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
				role = icon_path + "Batsman_Lefthand" ;
			}
		}else if(ply.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
			if(ply.getBowlerStyle() == null) {
				role =  icon_path + "FastBowler" ;
			}else {
				switch(ply.getBowlerStyle().toUpperCase()) {
				case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
					role = icon_path + "FastBowler" ;
					break;
				case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
					role = icon_path + "SpinBowlerIcon" ;
					break;
				}
			}
		}else if(ply.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
			if(ply.getBowlerStyle() == null) {
				role =  icon_path + "FastBowlerAllrounder" ;
			}else {
				switch(ply.getBowlerStyle().toUpperCase()) {
				case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
					role =  icon_path + "FastBowlerAllrounder" ;
					break;
				case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
					role =  icon_path + "SpinBowlerAllrounder" ;
					break;
				}
			}
		}
		return role;
	}
	public boolean checkIconPlayer(int plyrId, AuctionService auctionService) {
		if(auctionService.getAllPlayer().get(plyrId-1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			return false;
		}else {
			return true;
		}
	}
	
}