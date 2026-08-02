package com.auction.broadcaster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.PlayerCount;
import com.auction.model.Statistics;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
import com.auction.model.Flipper;
import com.auction.model.NameSuper;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class UTT_VIZ extends Scene{

	private String status, side2ValueToProcess = "";
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "UTT_VIZ";
	public Data data = new Data();
	public String which_graphics_onscreen = "",which_data="",which_team="", rtm_googly_on_screen = "",which_crwaler_onscreen;
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1, rowHighlight = 1,prevRowHighlight = 1, rtmGooglyWhichSide = 1;
	public int player_id = 0,team_id=0,player_number=0;
	public int zoneSize = 0, current_index = 0;
	public int whichSideCrawler=1;
	public Statistics Statistics ;
	List<Player> squad = new ArrayList<Player>();
	List<String> data_str = new ArrayList<String>();
	List<PlayerCount> player_count = new ArrayList<PlayerCount>();

	private String logo_path = "IMAGE*/Default/Essentials/TeamBadges/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String flag_path = "IMAGE*/Default/Essentials/Flags/";
	//private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	private String photo_mini_path  = "C:\\Images\\AUCTION\\Photos_mini\\";
	private String photo_ff_path  = "C:\\Images\\AUCTION\\Photos_ff\\";
	private String photo_lof_path  = "C:\\Images\\AUCTION\\LOF\\";
	
	public boolean isProfileStatsOnScreen = false;
	
	public UTT_VIZ() {
		super();
	}

	public UTT_VIZ(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Data updateData(Auction auction, Auction session_curr_bid,AuctionService auctionService, PrintWriter print_writer) throws InterruptedException
	{
		if(data.isData_on_screen()) {
			if(data.isPlayer_sold_or_unsold() == false) {
				populatePlayerProfile(true,print_writer, 2, data.getPlayer_id(),"",auctionService.getAllStats(), auction, 
						session_curr_bid, auctionService, session_selected_broadcaster);
			}
			
			if(data.getPreviousBid() < session_curr_bid.getCurrentPlayers().getSoldForPoints() || 
					data.getPreviousBid() > session_curr_bid.getCurrentPlayers().getSoldForPoints()) {
				data.setPreviousBid(session_curr_bid.getCurrentPlayers().getSoldForPoints());
				
				BidChangeOn(print_writer, session_curr_bid, data.getWhichside());
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid$Side1 START \0");
				TimeUnit.MILLISECONDS.sleep(1000);
				BidChangeOn(print_writer, session_curr_bid, 1);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid$Side1 SHOW 0.0 \0");
			}
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, Auction session_curr_bid, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws Exception {
		System.out.println(whatToProcess.toUpperCase());
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":case "POPULATE-PROFILE_STATS_CHANGE":
		case "POPULATE-L3-FLIPPER":case "POPULATE-PROFILE_STATS":case "POPULATE-CURR_BID":case "POPULATE-IDENT":case "POPULATE-PLAYERPROFILE_FF":
		case "POPULATE-FF_RTM_AND_PURSE_REMAINING":case "POPULATE-L3-NAMESUPER":case "POPULATE-RTM_AVAILABLE":case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
		case "POPULATE-FLIPPER_SQUAD": case "POPULATE-TEAMS": case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-TEAMS_DETAILS": 
		case "POPULATE-FF_RETAIN_PLAYERS":
			
		case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE":
		case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-LOF_SQUAD":
		
		//crawl
		case "POPULATE-CRAWLE_SQUAD":
		case "POPULATE-CRAWL-PURSE_REMAINING": case "POPULATE-CRAWL-SQUAD_SIZE": case "POPULATE-CRAWL_TOP_SOLD": 
		case "POPULATE-CRAWLER_TEAM_TOP_SOLD": case "POPULATE-L3-CRWLERFREETEXT":		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "UTT_VIZ":
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-FF-PLAYERPROFILE":
					data.setWithPlayerPhoto(valueToProcess.split(",")[1].equalsIgnoreCase("With_Photo") ? 1 : 0);
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[0]));
					populatePlayerProfile(false,print_writer,whichSide,Integer.valueOf(valueToProcess.split(",")[0]),valueToProcess.split(",")[2] ,auctionService.getAllStats(),
							auction, session_curr_bid,auctionService, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					processPreview(print_writer, whatToProcess, whichSide);
					break;
					
				case "POPULATE-PLAYERPROFILE_FF":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					Statistics = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[0])).findAny().orElse(null);
					populatePlayerProfileFF(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PROFILE_STATS_CHANGE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateProfileChange(print_writer,whichSideNotProfile, auctionService, auction);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-ZONE_PLAYERS_STATS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					if ((!which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS")) || (!valueToProcess.equalsIgnoreCase("undefined") &&
						     !side2ValueToProcess.equalsIgnoreCase(valueToProcess))) {

						    squad.clear();
						    current_index = 0;
						    String categoryFilter = valueToProcess.split(",")[0].trim();
						    
						    squad = new ArrayList<>(auction.getPlayersList());
						    squad.removeIf(player -> 
						        player.getCategory() == null || 
						        !player.getCategory().trim().equalsIgnoreCase(categoryFilter)
						    );

						    if (whatToProcess.equalsIgnoreCase("POPULATE-ZONE_PLAYERS_STATS")) {
						        Iterator<Player> squadIterator = squad.iterator();
						        while (squadIterator.hasNext()) {
						            Player sq = squadIterator.next();
						            for (Player ply : auction.getPlayers()) {
						                if (sq.getPlayerId() == ply.getPlayerId() &&
						                    (ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM) || ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) ||
						                     ply.getSoldOrUnsold().equalsIgnoreCase("RETAIN") ||ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD))) {
						                    squadIterator.remove(); // safe removal
						                    break;
						                }
						            }
						        }
						    }

						    zoneSize = squad.size();
						    side2ValueToProcess = valueToProcess;
						}
					System.out.println("size - " + squad.size());
					populateZoneSquad(print_writer, side2ValueToProcess.split(",")[0], whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-TEAMS_DETAILS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					System.out.println("HELLO");
					populateTeamsDetails(print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_RETAIN_PLAYERS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateFFRetainPlayers(print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-TEAMS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateTeamDetails(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSquad(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-IDENT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateIdent(print_writer,whichSideNotProfile,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-LOF_TOP_SOLD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateLofTopSold(print_writer,whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_TEAM_TOP_SOLD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofTeamTopSold(print_writer,Integer.valueOf(valueToProcess.split(",")[0]),whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_REMAINING_PURSE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofRemainingPurse(print_writer, valueToProcess.split(",")[0],whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					player_number = 0;
					squad.clear();
					
					team_id = Integer.valueOf(valueToProcess.split(",")[0]);
					side2ValueToProcess = valueToProcess;
					populateLofSquad(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_SQUAD_SIZE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofSquadSize(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;	
				case "POPULATE-LOF_REMAINING_SLOT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofRemainingSlot(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;	
				case "POPULATE-CRAWL-PURSE_REMAINING":
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateCrawlerRemainingPurse(print_writer, valueToProcess.split(",")[0],whichSideCrawler, auction,auctionService, session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
				
				case "POPULATE-CRAWL-SQUAD_SIZE":
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateCrawlerSquadSize(print_writer, whichSideCrawler, auction,auctionService,session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
				case "POPULATE-CRAWL_TOP_SOLD":
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateCrawlerTopSold(print_writer,whichSideCrawler, auction,auctionService, session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
				case "POPULATE-CRAWLER_TEAM_TOP_SOLD":	
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateCrawlTeamTopSold(print_writer,Integer.valueOf(valueToProcess.split(",")[0]),whichSideCrawler, auction,auctionService, session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
				case "POPULATE-L3-CRWLERFREETEXT":	
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateFreetextcrawler(print_writer, whichSideCrawler,Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService, 
							session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
				case "POPULATE-CRAWLE_SQUAD":
					whichSideCrawler = (which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty() ? 2 : 1);
					side2ValueToProcess = valueToProcess;
					populateCrawlerSquad(print_writer,Integer.valueOf(valueToProcess.split(",")[0]),whichSideCrawler, auction,auctionService, session_selected_broadcaster);
					processPreviewCrawler(print_writer, whatToProcess, whichSideCrawler);
					break;
					
				case "POPULATE-L3-FLIPPER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFlipper(print_writer, whichSideNotProfile, valueToProcess, auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FLIPPER_SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFlipperSquad(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess), auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-L3-NAMESUPER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateNameSuper(print_writer, whichSideNotProfile,Integer.valueOf(valueToProcess.split(",")[0]),valueToProcess.split(",")[1], auction,auctionService, 
							session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-RTM_AVAILABLE":
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						rtmGooglyWhichSide = 2;
					}else {
						rtmGooglyWhichSide = 1;
					}
					populateRTMAvailable(print_writer, rtmGooglyWhichSide, auction, auctionService);
					processPreview(print_writer, whatToProcess, rtmGooglyWhichSide);
					break;
				case "POPULATE-PROFILE_STATS":
					if(isProfileStatsOnScreen) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateProfileStats(print_writer, valueToProcess, whichSideNotProfile, auction, auctionService);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
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
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					which_data = valueToProcess.split(",")[0];
					which_team = "WithTeam";
					populateFFFiveTopBuysAuction(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					which_data = valueToProcess.split(",")[1];
					which_team = "WithoutTeam";
					populateFFTopFiveBuysTeam(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				}
			}
		case "ANIMATE-OUT-PROFILE": case "ANIMATE-OUT-RTM_GOOGLY": case "ANIMATE-OUT-RTM_AVAILABLE": case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-OUT-ALL_GFX":
		
		case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT":case "ANIMATE-IN-CURR_BID": case "ANIMATE-OUT-CRAWLER":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT": case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-FLIPPER": 
		case "ANIMATE-IN-TEAM_CURR_BID": case "ANIMATE-IN-PROFILE_STATS_CHANGE": case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": 
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM": 
		case "ANIMATE-IN-FLIPPER_SQUAD": case "ANIMATE-IN-TEAMS": case "ANIMATE-IN-TEAMS_DETAILS": case "ANIMATE-IN-FF_RETAIN_PLAYERS":

		case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
		case "ANIMATE-IN-SQUAD-PLAYER": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": 
		case "ANIMATE-IN-LOF_RTM_REMAINING": case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE": case "ANIMATE-IN-LOF_SQUAD": 
		case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
			
		case "ANIMATE-IN-CRAWL_REMAINING_PURSE": case "ANIMATE-IN-CRAWL_SQUAD_SIZE": case "ANIMATE-IN-CRAWL_REMAINING_RMT": 
		case "ANIMATE-IN-CRAWLER_TEAM_TOP_SOLD": case "ANIMATE-IN-CRAWL_TOP_SOLD": 	case "ANIMATE-IN-CRAWL_SQUAD":
			
			switch (session_selected_broadcaster.toUpperCase()) {
			case "UTT_VIZ":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-OUT-ALL_GFX":
					switch(which_graphics_onscreen) {
					case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
					case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
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
					
					case "PLAYERPROFILE_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					
					case "TEAMS_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Team_Info CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Team_Info SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					case "TEAMS_DETAILS":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Teams_Details CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Teams_Details SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "IDENT":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": case "SQUAD": 
					case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM": case "FF_RETAIN_PLAYERS":
						switch (which_graphics_onscreen) {
						case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details CONTINUE\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys CONTINUE\0");
							break;
						case "SQUAD": 
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad CONTINUE\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys CONTINUE\0");
							break;
						case "ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Unsold_YTBD CONTINUE\0");
							break;
						case "FF_RETAIN_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Retained CONTINUE\0");
							break;
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes SHOW 0\0");
						break;
					case "NAMESUPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Essentials CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Image CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Data CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						break;
					case "FLIPPER": case "FLIPPER_SQUAD":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Flipper SHOW 0\0");
						break;
					}
					
					// Animate Out - crwaler
					switch(which_crwaler_onscreen) {
					case "CRAWL_REMAINING_PURSE": case "CRAWL_SQUAD_SIZE": case "CRAWL_TOP_SOLD": case "CRAWLER_TEAM_TOP_SOLD": 
					case "CRAWL_SQUAD": case "FREEETEXTCRAWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl$In_Out CONTINUE \0");
						which_crwaler_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(1200);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl SHOW 0.0\0");
						break;
					}
					
					if(data.isData_on_screen()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CenterData CONTINUE\0");
						if(isProfileStatsOnScreen) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats CONTINUE\0");
							isProfileStatsOnScreen = false;
						}

						if(data.isPlayer_sold_or_unsold() == false) {
							if(data.isBid_Start_or_not()) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid CONTINUE \0");
								data.setBid_Start_or_not(false);
							}
						}else {
							if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
							}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
							}
						}
						if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM CONTINUE \0");
						}
						
						TimeUnit.MILLISECONDS.sleep(1200);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar SHOW 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX SHOW 0.0 \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
						data.setBid_Start_or_not(false);
						data.setPlayer_sold_or_unsold(false);
						data.setData_on_screen(false);
						data.setBid_result("");
						rtm_googly_on_screen = "";
					}
					break;
				
				case "ANIMATE-OUT-PLAYER_STAT":
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats CONTINUE\0");
						isProfileStatsOnScreen = false;
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats SHOW 0.0\0");
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-IN-TEAM_CURR_BID":
					if(data.isData_on_screen()) {
						if(isProfileStatsOnScreen) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats START \0");
							TimeUnit.MILLISECONDS.sleep(1000);
							
							if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PROFILE_STATS")) {
								populateProfileStats(print_writer, side2ValueToProcess, 1, auction, auctionService);
							}else if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-TEAM_CURR_BID")) {
								populateTeamCurrentBid(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, session_curr_bid, auctionService);
							}
							
							TimeUnit.MILLISECONDS.sleep(1000);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats SHOW 0.800\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats SHOW 0\0");
							
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats START \0");
							isProfileStatsOnScreen = true;
						}
						
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS_CHANGE":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Stats START\0");
					TimeUnit.MILLISECONDS.sleep(1000);
					populateProfileChange(print_writer,1, auctionService, auction);
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Stats SHOW 0.0\0");

					break;
				case "ANIMATE-IN-CURR_BID":
					populateCurrentBid(print_writer, 1);
					if(data.getBid_result().equalsIgnoreCase("GAVEL") || data.getBid_result().equalsIgnoreCase("BID")) {
						if(!data.isBid_Start_or_not()) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX START \0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid START \0");
						}
					}
						
					data.setBid_Start_or_not(true);
					data.setBid_result("BID");
					
//					TimeUnit.MILLISECONDS.sleep(2500);
//					populateCurrentBid(print_writer, 1);
//					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					break;
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CenterData START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Audio START\0");
					if(isProfileStatsOnScreen == false) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats START\0");
						isProfileStatsOnScreen = true;
					}
					
					if(data.isPlayer_sold_or_unsold() == false) {
						data.setBid_result("GAVEL");
					}else {
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Main$Right_Data$Side1$Sold_Unsold$Select*FUNCTION*Omo*vis_con SET  0\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold$In_Out$Sold START \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Main$Right_Data$Side1$Sold_Unsold$Select*FUNCTION*Omo*vis_con SET  1\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold$In_Out$Unsold START \0");
						}
					}
					data.setData_on_screen(true);
					break;
				//Flipper
				case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-FLIPPER_SQUAD":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll START \0");
					which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					break;
				//crawl
				case "ANIMATE-IN-CRAWL_REMAINING_PURSE": case "ANIMATE-IN-CRAWL_SQUAD_SIZE": case "ANIMATE-IN-CRAWLER_TEAM_TOP_SOLD": 
				case "ANIMATE-IN-CRAWL_SQUAD": case "ANIMATE-IN-CRAWL_TOP_SOLD": case "ANIMATE-IN-FREEETEXTCRAWLER": 
					if(which_crwaler_onscreen != null && !which_crwaler_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Crawl START \0");
						
						TimeUnit.MILLISECONDS.sleep(500);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-CRAWL_REMAINING_PURSE":
							populateCrawlerRemainingPurse(print_writer, side2ValueToProcess.split(",")[0],1, auction,auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-CRAWL_SQUAD_SIZE":
							populateCrawlerSquadSize(print_writer, 1, auction,auctionService,session_selected_broadcaster);
							break;
						case "ANIMATE-IN-CRAWLER_TEAM_TOP_SOLD":
							populateCrawlTeamTopSold(print_writer,Integer.valueOf(side2ValueToProcess.split(",")[0]),1, auction,auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-CRAWL_SQUAD":	
							populateCrawlerSquad(print_writer,Integer.valueOf(side2ValueToProcess.split(",")[0]),1, auction,auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FREEETEXTCRAWLER":	
							populateFreetextcrawler(print_writer, 1,Integer.valueOf(side2ValueToProcess.split(",")[0]), auction,auctionService, 
									session_selected_broadcaster);
							break;
						case "ANIMATE-IN-CRAWL_TOP_SOLD":
							populateCrawlerTopSold(print_writer,1, auction,auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Crawl SHOW 0.0 \0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl$In_Out START \0");
					}
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side1$Crawl*GEOM*start INVOKE\0");
					which_crwaler_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					break;
				//lof
				case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
				case "ANIMATE-IN-SQUAD-PLAYER": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING":
				case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE": case "ANIMATE-IN-LOF_SQUAD": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD": 
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Header START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$SubHeader START \0");
						
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-LOF_REMAINING_PURSE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo START \0");
							which_graphics_onscreen = "LOF_REMAINING_PURSE";
							break;
							
						case "ANIMATE-IN-LOF_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys START \0");
							which_graphics_onscreen = "LOF_TOP_SOLD";
							break;
						case "ANIMATE-IN-LOF_SQUAD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad START \0");
							which_graphics_onscreen = "LOF_SQUAD";
							break;
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys_Team START \0");
							which_graphics_onscreen = "LOF_TEAM_TOP_SOLD";
							break;
						case "ANIMATE-IN-SQUAD-PLAYER":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side"+whichSide+"$"+rowHighlight +" START \0");
							for(int i=rowHighlight;i<=8;i++) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side"+whichSide+"$"+(i+1)+" START \0");
							}
							prevRowHighlight = rowHighlight;
							which_graphics_onscreen = "SQUAD-PLAYER";
							break;
						case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad_Size START \0");
							which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
							break;
						case "ANIMATE-IN-LOF_RTM_REMAINING": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name START \0");
							which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team START \0");
							which_graphics_onscreen = "LOF_SQUAD_SIZE_CATEGORY_WISE";
							break;
						}
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-LOF_REMAINING_PURSE":
							populateLofRemainingPurse(print_writer, side2ValueToProcess, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_TOP_SOLD":
							populateLofTopSold(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_SQUAD":
							populateLofSquad(print_writer, team_id, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							populateLofTeamTopSold(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD-PLAYER":
							//populateLofSquadSizeCategoryWise(print_writer,  Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_REMAINING_SLOT":
							populateLofRemainingSlot(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
							//populateLofZoneWisePlayerSold(print_writer, 1, side2ValueToProcess, auction, auctionService, session_selected_broadcaster);	
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE":
							populateLofSquadSize(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_RTM_REMAINING":
							//populateLofRTMRemaining(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
							//populateLofSquadSizeCategoryWiseOnly(print_writer,  Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
					
				//LT
				case "ANIMATE-IN-NAMESUPER": 
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Image START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Data START \0");
						
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-NAMESUPER": 
							populateNameSuper(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]),side2ValueToProcess.split(",")[1], auction, auctionService, 
									session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
				//FF
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Loop START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
					which_graphics_onscreen = "IDENT";
					break;
				case "ANIMATE-IN-PLAYERPROFILE_FF":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Loop START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
					which_graphics_onscreen = "PLAYERPROFILE_FF";
					break;
				case "ANIMATE-IN-TEAMS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Team_Info START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Loop START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
					which_graphics_onscreen = "TEAMS_FF";
					break;
				case "ANIMATE-IN-TEAMS_DETAILS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Teams_Details START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Loop START\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
					which_graphics_onscreen = "TEAMS_DETAILS";
					break;
					
					
				case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
				case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-SQUAD":case "ANIMATE-IN-ZONE-PLAYER_STATS":
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
				case "ANIMATE-IN-FF_RETAIN_PLAYERS":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials START\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header START\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Loop START\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						
						switch(whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details START\0");
							which_graphics_onscreen = "FF_RTM_AND_PURSE_REMAINING";
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-SQUAD":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad START\0");
							which_graphics_onscreen ="SQUAD";
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Unsold_YTBD START\0");
							which_graphics_onscreen = "ZONE-PLAYER_STATS";
							break;
						case "ANIMATE-IN-FF_RETAIN_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Retained START\0");
							which_graphics_onscreen = "FF_RETAIN_PLAYERS";
							break;
						}
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							populateIdent(print_writer,1,session_selected_broadcaster);
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							Statistics = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == Integer.valueOf(side2ValueToProcess.split(",")[0])).findAny().orElse(null);
							populatePlayerProfileFF(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-ZONE-PLAYER_FULL":
							populateZoneSquad(print_writer, side2ValueToProcess.split(",")[0], 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD":
							populateSquad(print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]), 
									1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							populateFFRTMAndPurseRemaining(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							populateFFTopBuysAuction(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							populateFFFiveTopBuysAuction(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							populateFFTopFiveBuysTeam(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), 
									auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(1500);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
					
				case "ANIMATE-IN-RTM_AVAILABLE":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM START\0");
					rtm_googly_on_screen = "RTM";
					break;
				case "CLEAR-ALL":
					resetData(print_writer);
					
					rtm_googly_on_screen = "";
		            which_graphics_onscreen = "";
		            side2ValueToProcess ="";
		            rtmGooglyWhichSide = 1;
		            whichSideNotProfile = 1;
		            data.setBid_Start_or_not(false);
		            data.setData_on_screen(false);
		            isProfileStatsOnScreen = false;
					data.setBid_result("");
					break;
				case "ANIMATE-OUT-PROFILE":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Audio START\0");
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CenterData CONTINUE\0");
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats CONTINUE\0");
						isProfileStatsOnScreen = false;
					}

					if(data.isPlayer_sold_or_unsold() == false) {
						if(data.isBid_Start_or_not()) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid CONTINUE \0");
							data.setBid_Start_or_not(false);
						}
					}else {
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
						}
					}
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM CONTINUE \0");
					}
					
					TimeUnit.MILLISECONDS.sleep(1200);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar SHOW 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
					data.setBid_Start_or_not(false);
					data.setPlayer_sold_or_unsold(false);
					data.setData_on_screen(false);
					data.setBid_result("");
					rtm_googly_on_screen = "";
					break;
				case "ANIMATE-OUT-RTM_AVAILABLE":
					switch (rtm_googly_on_screen) {
					case "RTM":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM SHOW 0\0");
						break;
					}
					rtm_googly_on_screen = "";
					break;
				case "ANIMATE-OUT-CRAWLER":
					switch(which_crwaler_onscreen) {
					case "CRAWL_REMAINING_PURSE": case "CRAWL_SQUAD_SIZE": case "CRAWL_TOP_SOLD": case "CRAWLER_TEAM_TOP_SOLD": 
					case "CRAWL_SQUAD": case "FREEETEXTCRAWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl$In_Out CONTINUE \0");
						which_crwaler_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(1200);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl SHOW 0.0\0");
						break;
					}
					break;	
				
				case "ANIMATE-OUT":
					System.out.println(which_graphics_onscreen);
					switch(which_graphics_onscreen) {
					case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
					case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
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
					
					case "PLAYERPROFILE_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile CONTINUE\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					
					case "TEAMS_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Team_Info CONTINUE\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Team_Info SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					case "TEAMS_DETAILS":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Teams_Details CONTINUE\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Teams_Details SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "IDENT":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident CONTINUE\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": case "SQUAD": 
					case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM": case "FF_RETAIN_PLAYERS":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Audio START\0");
						switch (which_graphics_onscreen) {
						case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details CONTINUE\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys CONTINUE\0");
							break;
						case "SQUAD": 
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad CONTINUE\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys CONTINUE\0");
							break;
						case "ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Unsold_YTBD CONTINUE\0");
							break;
						case "FF_RETAIN_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Retained CONTINUE\0");
							break;
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes SHOW 0\0");
						break;
					case "NAMESUPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Essentials CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Image CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Data CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						break;
					case "FLIPPER": case "FLIPPER_SQUAD":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Flipper SHOW 0\0");
						break;
					}
					break;
				}
				
			}
		}
		return null;
	}
	private void populateProfileChange(PrintWriter print_writer, int whichSideNotProfile2, AuctionService auctionService, Auction auction) {
		
		if(side2ValueToProcess.equalsIgnoreCase("with_data")) {			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$SelectStyle*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$1$txt_StatHead"
					+ "*GEOM*TEXT SET WR - WEEK 15\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$1$txt_StatValue"
					+ "*GEOM*TEXT SET " + (Statistics.getRank().equalsIgnoreCase("NA") ? "-" : Statistics.getRank()) + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$2$txt_StatHead"
					+ "*GEOM*TEXT SET STYLE\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$2$txt_StatValue"
					+ "*GEOM*TEXT SET " + Statistics.getStyle() + " " + Statistics.getGrip() + "\0");
			
		}else if(side2ValueToProcess.equalsIgnoreCase("with_info")) {
			Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$SelectStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style2$txt_StatValue"
					+ "*GEOM*TEXT SET " + "CATEGORY: " + getCategory(player.getCategory()) + "\0");
			
		}
	}

	public void resetData(PrintWriter print_writer) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
		
		//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
	
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0.0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll SHOW 0.0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Crawl SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Crawl SHOW 0.0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Loop SHOW 0.0 \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Profile SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes SHOW 0.0 \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes SHOW 0.0 \0");
	}
	
	public void ChangeOn(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException {
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
		case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
			if(whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER") && 
					whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader START \0");
			}
			break;
		}
		
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad_Size$Change_Out START \0");
			break;
		case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_Out START \0");
			break;
		case "LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo$Change_Out START \0");
			break;
		case "LOF_TEAM_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys_Team START \0");
			break;
		case "LOF_SQUAD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad START \0");
			break;
		case "LOF_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys START \0");
			break;
		case "SQUAD-PLAYER":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$" + prevRowHighlight + " CONTINUE REVERSE\0");
				for(int i=prevRowHighlight;i<=8;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$" + i + " CONTINUE REVERSE\0");
				}
				prevRowHighlight = 0;
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$" + prevRowHighlight + " CONTINUE REVERSE\0");
				for(int i=1;i<=8;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$" + i + " CONTINUE REVERSE\0");
				}
			}
			break;
		case "LOF_SQUAD_SIZE_CATEGORY_WISE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team START \0");
			break;
		
		//FF	
		case "PLAYERPROFILE_FF": 
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Profile START\0");
		break;
		case "SQUAD": case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": 
		case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header START\0");
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
				break;
			case "ZONE-PLAYER_STATS":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Unsold_YTBD START\0");
				break;
			case "SQUAD":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
				break;
			case "FF_RTM_AND_PURSE_REMAINING":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details$Change_Out START\0");
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
				break;
			}
			break;
		
		//Flipper	
		case "FLIPPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Header START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Text START \0");
			break;
			
		//LT
		case "NAMESUPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird START \0");
			break;
		case "PLAYERPROFILE_LT":
			if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_LT_STATS")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData START \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$TopData START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData START \0");
			}
			break;
		}
		TimeUnit.MILLISECONDS.sleep(700);
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad_Size$Change_In START \0");
			break;
		
		case "ANIMATE-IN-LOF_RTM_REMAINING": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_In START \0");
			break;
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo$Change_In START \0");
			break;
		case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys_Team START \0");
			break;
		 case "ANIMATE-IN-LOF_SQUAD":
			 if(!which_graphics_onscreen.equalsIgnoreCase("LOF_SQUAD")) {
				 print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad START \0");
			 }
			break;
		case "ANIMATE-IN-LOF_TOP_SOLD": 
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys START \0");
			break;
		case "ANIMATE-IN-SQUAD-PLAYER":
			if(!whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category START \0");
				TimeUnit.MILLISECONDS.sleep(2200);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side2$"+rowHighlight +" START\0");
				for(int i=rowHighlight;i<=8;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side2$"+(i+1)+" START\0");
				}
			}else {
				TimeUnit.MILLISECONDS.sleep(1000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$"+rowHighlight +" START\0");
				for(int i=rowHighlight;i<=8;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$"+(i+1)+" START\0");
				}
			}
			break;
		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
			if(!whichGraphicOnScreen.equalsIgnoreCase("LOF_SQUAD_SIZE_CATEGORY_WISE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team START \0");
			}
			break;
		//FF	
		case "ANIMATE-IN-IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
			break;
		case "ANIMATE-IN-PLAYERPROFILE_FF":
			if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
			}
			break;
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_In START\0");
			break;
		case "ANIMATE-IN-SQUAD":
			if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
			}
			break;
			
		}
	}
	public void cutBack(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException { 
		
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD": case "LOF_REMAINING_PURSE":
		case "LOF_SQUAD": case "LOF_TEAM_TOP_SOLD": case "LOF_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_SQUAD_SIZE_CATEGORY_WISE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader SHOW 0\0");
			break;
		}
		
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_RTM_REMAINING": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
			break;
		case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad_Size SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad_Size SHOW 0\0");
			break;
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
			break;
		case "ANIMATE-IN-LOF_TEAM_TOP_SOLD": 
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys_Team SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys_Team SHOW 0\0");
			break;
		case "ANIMATE-IN-LOF_SQUAD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad SHOW 0\0");
			break;
		case "ANIMATE-IN-LOF_TOP_SOLD": 
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys SHOW 0\0");
			break;
		case "ANIMATE-IN-SQUAD-PLAYER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category SHOW 0\0");
			if(!whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$" + rowHighlight + " SHOW 0.800\0");
				for(int i=(rowHighlight+1);i<=8;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$" + i + " SHOW 1.000\0");
				}
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side2 SHOW 0.0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side2 SHOW 0.0\0");
			}
			break;
		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team SHOW 2.200\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team SHOW 0\0");
			break;
		
		//FF
		case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": 
	    case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header SHOW 0.0\0");
			switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-SQUAD":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break; 
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys SHOW 0.0\0");
					break;
				case "ANIMATE-IN-ZONE-PLAYER_STATS": 
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Unsold_YTBD SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Unsold_YTBD SHOW 0.0\0");
					break;
			}
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_SLOT") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE") 
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_RTM_REMAINING")
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-ZONEWISE_PLAYERS_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			}
			break;
		case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_SLOT") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE") 
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_RTM_REMAINING")
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-ZONEWISE_PLAYERS_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Squad_Size SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad_Size SHOW 0\0");
			}
			break;
		case "LOF_REMAINING_PURSE": 
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_SLOT") 
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_RTM_REMAINING")
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-ZONEWISE_PLAYERS_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			}
			break;
		case "LOF_SQUAD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Squad SHOW 0\0");
			break;
		case "LOF_TEAM_TOP_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TEAM_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys_Team SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys_Team SHOW 0\0");
			}
			break;
		case "LOF_TOP_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Top_Buys SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Top_Buys SHOW 0\0");
			}
			break;
		case "SQUAD-PLAYER":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category SHOW 0\0");
			}
			break;
		case "LOF_SQUAD_SIZE_CATEGORY_WISE":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team SHOW 0\0");
			}
			break;
		
		//Flipper	
		case "FLIPPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Header SHOW 0.0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Text SHOW 0.0\0");
			break;
			
		//LT
		case "NAMESUPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0.0\0");
			break;
		
		//FF
		case "FF_RTM_AND_PURSE_REMAINING":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details SHOW 0.00\0");	
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details SHOW 0.0\0");
			break;
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
			break;
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys SHOW 0.0\0");
			break;
			
		case "SQUAD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad SHOW 0.0\0");
			}
			break;
		}
		prevRowHighlight = rowHighlight;
	}

	public void populateIdent(PrintWriter print_writer,int which_side,String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Header$Header_Out$Header_In$Text$txt_Title1*GEOM*TEXT SET " 
				+ "PLAYER" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Header$Header_Out$Header_In$Text$txt_Title2*GEOM*TEXT SET " 
				+ "AUCTION" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Text$Info1_Out$Info1_in$txt_Info1*GEOM*TEXT SET " 
				+ "BUTTERFLY UTT SEASON 7" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Text$Info2_Out$Info2_In$txt_Info2*GEOM*TEXT SET " 
				+ "HOTEL SAHARA STAR - MUMBAI" + "\0");
		
	}
	
	public void populateCurrentBid(PrintWriter print_writer,int which_side) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$CurrentPrice$Price$Side1$txt_CurrentPrice*GEOM*TEXT SET " 
				+ AuctionFunctions.ConvertToLakh(data.getPreviousBid(),true) + "L\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$CurrentPrice$Price$Side2$txt_CurrentPrice*GEOM*TEXT SET " 
				+ AuctionFunctions.ConvertToLakh(data.getPreviousBid(),true) + "L\0");
	}
	public void BidChangeOn(PrintWriter print_writer, Auction session_curr_bid, int which_side) {
		if(data.isBid_Start_or_not()) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side1$CurrentPrice$Price$Side" + which_side + "$txt_CurrentPrice*GEOM*TEXT SET " 
					+ AuctionFunctions.ConvertToLakh(data.getPreviousBid(),true) + "L\0");
		}
	}
	public void PlayerSoldOrUnsold(PrintWriter print_writer, Auction auction, int playerId,int which_side) {
		for(int i=auction.getPlayers().size()-1; i >= 0; i--) {
			if(playerId == auction.getPlayers().get(i).getPlayerId()) {
				
				//Auction Result
				if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$Sold$Price$txt_SoldValue"
							+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(auction.getPlayers().get(i).getSoldForPoints(),true) + "L TOKENS" + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$Sold$Logo$img_TeamBadges"
							+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$TeamName$txt_TeamName"
							+ "*GEOM*TEXT SET " + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName1() + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$Sold$Price$txt_Sold*GEOM*TEXT SET " + "SOLD TO" + " \0");
					
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Right_Data$Side" + which_side + "$Unsold$Info$txt_Info*GEOM*TEXT SET " + "UNSOLD" + " \0");
					
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}
				break;
			}
		}
	}
	
	public void populateLofRemainingSlot(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle2$txt_Header1*GEOM*TEXT SET " + "BUTTERFLY UTT" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle2$txt_Header2*GEOM*TEXT SET " + "SEASON 7 PLAYER AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SLOTS REMAINING" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "EVENT" + "\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$txt_Value*GEOM*TEXT SET " 
					+ (6-squadSize) + " \0");
			squadSize = 0;
		}
	}
	
	public void populateLofSquadSize(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle2$txt_Header1*GEOM*TEXT SET " + "BUTTERFLY UTT" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle2$txt_Header2*GEOM*TEXT SET " + "SEASON 7 PLAYER AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD SIZE" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "EVENT" + "\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Squad_Size$Row" + row + "$txt_Value*GEOM*TEXT SET " 
					+ squadSize + " \0");
			squadSize = 0;
		}
	}
	public void populateLofSquad(PrintWriter print_writer,int team_id,int which_side, Auction match,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		data_str.clear();
		List<Player> retained = new ArrayList<Player>();
		
		data_str = AuctionFunctions.getSquadDataUTTZone(match,team_id);
		
		if(match.getPlayers() != null) {
			for(Player player : match.getPlayers()) {
				if(player.getTeamId() == team_id) {
					if(player.getSoldOrUnsold().equalsIgnoreCase("RETAIN") ||
							player.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM) ||
							player.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						
						retained.add(player);
					}
				}
			}
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ match.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ match.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4\0");
		
		if (retained != null) {				
			for(Player ply : retained) {
				row++;
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Data$txt_FirstName"
						+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(ply.getPlayerId()-1).getFirstname() + " \0");
	    		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Data$txt_LastName"
						+ "*GEOM*TEXT SET " +auctionService.getAllPlayer().get(ply.getPlayerId()-1).getSurname() + " \0");
	    		
	    		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$txt_Value"
						+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints(),false) + "L" + " \0");
	    		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$MainBase"
	    				+ "*TEXTURE*IMAGE SET "+ photo_lof_path + auctionService.getAllPlayer().get(ply.getPlayerId()-1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
	    		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Retained_Grp*ACTIVE SET " 
	    				+ (ply.getSoldOrUnsold().equalsIgnoreCase("RETAIN")? "1" : "0") + "\0");		
			}
			for(String Str:data_str) {
				if(Str.matches("IND_M|IND_F|INT_M|INT_F")) {
					row++;
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$txt_Value*GEOM*TEXT SET  \0");
					if(Str.equalsIgnoreCase("IND_M") || Str.equalsIgnoreCase("IND_F")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Sticker$txt_LastName"
								+ "*GEOM*TEXT SET " + (Str.equalsIgnoreCase("IND_M") ? "INDIAN MALE" : "INDIAN FEMALE") + " \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Sticker$Select*FUNCTION*Omo*vis_con SET 1\0");
					}else if(Str.equalsIgnoreCase("INT_M") || Str.equalsIgnoreCase("INT_F")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Sticker$txt_LastName"
								+ "*GEOM*TEXT SET " + (Str.equalsIgnoreCase("INT_M") ? "FOREIGN MALE" : "FOREIGN FEMALE") + " \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Squad$Row" + row + "$PlayerGrp$Sticker$Select*FUNCTION*Omo*vis_con SET 0\0");
					}
					
				}
			}
		}
	}
	public void populateLofTopSold(PrintWriter print_writer,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 2\0");
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		for(int i=1; i<=5; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + i + "*ACTIVE SET 0\0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle2$txt_Header1*GEOM*TEXT SET " + " BUTTERFLY UTT" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle2$txt_Header2*GEOM*TEXT SET " + "SEASON 7 PLAYER AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "TOP BUYS (TOKENS)" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 5) {
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "$Select_DataType$PlayerGrp$txt_Team"
	    					+ "*GEOM*TEXT SET " + auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "$Select_DataType$PlayerGrp$txt_FirstName"
	    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "$Select_DataType$PlayerGrp$txt_LastName"
	    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + " \0");
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints(),false) + "L" + " \0");
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys$Row" + row + "$Select_DataType$PlayerGrp$MainBase"
	        				+ "*TEXTURE*IMAGE SET "+ photo_lof_path + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        	}
			}
		}
	}
	public void populateLofTeamTopSold(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 3\0");
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		for(int i=1; i<=5; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + i + "*ACTIVE SET 0\0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "TOP BUYS (TOKENS)" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 5) {
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$Select_DataType$PlayerGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$Select_DataType$PlayerGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$Select_DataType$PlayerGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$Select_DataType$PlayerGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFull_name() + " \0");
	        		}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints(),false) + "L" + " \0");
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Top_Buys_Team$Row" + row + "$Select_DataType$PlayerGrp$MainBase"
	        				+ "*TEXTURE*IMAGE SET "+ photo_lof_path + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        	}
			}
		}
	}
	public void populateLofRemainingPurse(PrintWriter print_writer,String which_type,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int total = 0;
	
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$HeaderStyle2$txt_Header1*GEOM*TEXT SET " + "BUTTERFLY UTT" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$HeaderStyle2$txt_Header2*GEOM*TEXT SET " + "SEASON 7 PLAYER AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "PURSE REMAINING (TOKENS)" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 1\0");
		
//		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$RupeeSymbol"
//				+ "*GEOM*TEXT SET " + "" + " \0");
//		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$txt_Title"
//				+ "*GEOM*TEXT SET " + "TOKENS (LAKH)" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "EVENT" + "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		for(int i=0; i <= auction.getTeam().size()-1; i++) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + auction.getTeam().get(i).getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + auction.getTeam().get(i).getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(i).getTeamName4() + " \0");
			
			if(which_type.equalsIgnoreCase("logo")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$Select_DataType"
						+ "*FUNCTION*Omo*vis_con SET 1 \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$Select_DataType"
						+ "*FUNCTION*Omo*vis_con SET 0 \0");
			}
			
			if(auction.getPlayers() != null ) {
				for(int j=0; j <= auction.getPlayers().size()-1; j++) {
					if(auction.getPlayers().get(j).getTeamId() == auction.getTeam().get(i).getTeamId()) {
						total = total + auction.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			if((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + 
						"$txt_Value*GEOM*TEXT SET " + "-" + " \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$LOF$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.ConvertToLakh((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total),false) + "L \0");
			}
			total = 0;
		}
	}
	
	public void populatePlayerProfile(boolean is_this_updating, PrintWriter print_writer, int which_side, int playerId, String dataType,  List<Statistics> stats, Auction auction, 
			Auction session_curr_bid, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(session_curr_bid.getCurrentPlayers() != null) {
			if(data.isData_on_screen() == true) {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
					
					if(data.isPlayer_sold_or_unsold() == true) {
						if(data.getBid_result() != null && !data.getBid_result().isEmpty()) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side2$Select*FUNCTION*Omo*vis_con SET 1\0");
							
							if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Main$Right_Data$Side1$Sold_Unsold$Select*FUNCTION*Omo*vis_con SET  0\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid CONTINUE \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX CONTINUE REVERSE\0");
							}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Main$Right_Data$Side1$Sold_Unsold$Select*FUNCTION*Omo*vis_con SET  1\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold_Unsold CONTINUE \0");
							}
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats START \0");
							TimeUnit.MILLISECONDS.sleep(1500);
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side1$Select*FUNCTION*Omo*vis_con SET 1\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats SHOW 0.0 \0");
						}
					}
				}
			}else {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
				}
			}
		}
		
		if(is_this_updating == false) {
			String Container = data.getWithPlayerPhoto()==0 ? "$Without_Image" : "$With_Image";;
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + "$Select_Style*FUNCTION*Omo*vis_con SET "
					+ data.getWithPlayerPhoto() + "\0");
			
			if(data.getWithPlayerPhoto() == 1) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side"+ which_side + Container + "$ImageGrp$ImageGrp_Out$"
						+ "img_PlayerImage*TEXTURE*IMAGE SET " + photo_ff_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container + "$TopTextGrp$txt_Name*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getFull_name() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container +  "$TopTextGrp$txt_Age*GEOM*TEXT SET " + 
					(auctionService.getAllPlayer().get(playerId - 1).getAge()== null ? "" : auctionService.getAllPlayer().get(playerId - 1).getAge()+" YEARS" ) + " \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side"+ which_side + Container + "$BottomLine$Flag$"
					+ "img_Flag*TEXTURE*IMAGE SET "+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container +  
					"$BottomLine$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1 \0");
			
			data.setPreviousBid(Integer.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice()));
			if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("2000")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container +  "$BottomLine$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 20L TOKENS\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("1200")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container + "$BottomLine$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 12L TOKENS\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("700")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container + "$BottomLine$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 7L TOKENS\0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterData$Side" + which_side + Container + "$BottomLine$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 3L TOKENS\0");
			}
			
			for(int i=1;i<=2;i++) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i  + "$Player_Info$PlayerInfo$txt_Name*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getFull_name() + " \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i +  "$Player_Info$PlayerInfo$txt_Age*GEOM*TEXT SET " + 
						(auctionService.getAllPlayer().get(playerId - 1).getAge()== null ? "" : auctionService.getAllPlayer().get(playerId - 1).getAge()+" YEARS" ) + " \0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side"+ i + "$Player_Info$Flag$FlagAll$"
						+ "img_Flag*TEXTURE*IMAGE SET "+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");
				
				if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("2000")) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i +  "$Player_Info$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 20L TOKENS\0");
				}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("1200")) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i + "$Player_Info$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 12L TOKENS\0");
				}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("700")) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i + "$Player_Info$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 7L TOKENS\0");
				}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$Side" + i + "$Player_Info$BasePrice$txt_BasePriceValue*GEOM*TEXT SET 3L TOKENS\0");
				}
			}
			
			populateProfileStats(print_writer, dataType, which_side, auction, auctionService);
		}
	}
	
	public void populatePlayerProfileFF(PrintWriter print_writer,int which_side, int playerId, String show_stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		player_id = playerId;
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Header$txt_Header*GEOM*TEXT SET " + "PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Header$txt_PlayerName*GEOM*TEXT SET " 
				+ auctionService.getAllPlayer().get(playerId - 1).getFull_name() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$Select*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$SingleLine$txt_TeamName*GEOM*TEXT SET " 
				+ auctionService.getAllPlayer().get(playerId - 1).getNationality() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$Flag$img_Flag*TEXTURE*IMAGE SET " 
				+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$1$img_Image*TEXTURE*IMAGE SET " 
				+ photo_ff_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$2$img_Image*TEXTURE*IMAGE SET " 
				+ photo_ff_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$3$img_Image*TEXTURE*IMAGE SET " 
				+ photo_ff_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("2000")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 20L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("1200")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 12L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("700")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 7L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 3L TOKENS\0");
		}
		
		if(show_stats.equalsIgnoreCase("with_data") || show_stats.equalsIgnoreCase("with_info")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats*ACTIVE SET 1\0");
			switch (show_stats) {
			case "with_data":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$SelectStyle*FUNCTION*Omo*vis_con SET 0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$1$txt_StatHead"
						+ "*GEOM*TEXT SET WR - WEEK 15\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$1$txt_StatValue"
						+ "*GEOM*TEXT SET " + (Statistics.getRank().equalsIgnoreCase("NA") ? "-" : Statistics.getRank()) + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$2$txt_StatHead"
						+ "*GEOM*TEXT SET STYLE\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$2$txt_StatValue"
						+ "*GEOM*TEXT SET " + Statistics.getStyle() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$3$txt_StatHead"
						+ "*GEOM*TEXT SET GRIP\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$3$txt_StatValue"
						+ "*GEOM*TEXT SET " + Statistics.getGrip() + "\0");
				
				break;
			case "with_info":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$SelectStyle*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style2$txt_StatValue"
						+ "*GEOM*TEXT SET " + "CATEGORY: " + getCategory(auctionService.getAllPlayer().get(playerId - 1).getCategory()) + "\0");
				break;
			}
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats*ACTIVE SET 0\0");
		}
	}
		
	public void populateFFRTMAndPurseRemaining(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int rtmUsed=0,squadSize=0,totalAmountSpent=0,row=0;
		
		System.out.println("COMING INSIDE");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 4\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
				+ "$Select_Graphics$Team_Details$Slect_ColumnNumber*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamName*GEOM*TEXT SET " + "PLAYER AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$2_Column$List1$0$txt_Name*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$2_Column$List2$0$txt_Name*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$2_Column$List3$0$txt_Name*GEOM*TEXT SET PURSE REM. (TOKENS)\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
//				+ "$Team_Details$2_Column$List4$0$Select_LineNumber*FUNCTION*Omo*vis_con SET 1\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
//				+ "$Team_Details$3_Column$List4$0$txt_Info*GEOM*TEXT SET IN LAKH\0");
		
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
					+ "$Team_Details$2_Column$List1$" + row + "$txt_Name*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
			
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId() && auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					rtmUsed++;
				}
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
					totalAmountSpent += auc.getSoldForPoints();
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
					+ "$Team_Details$2_Column$List2$" + row + "$txt_Name*GEOM*TEXT SET " + squadSize + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide 
					+ "$Team_Details$2_Column$List3$" + row + "$txt_Name*GEOM*TEXT SET " + AuctionFunctions.
					ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent),false) + "L" + "\0");
			
			rtmUsed = 0;
			totalAmountSpent = 0;
			squadSize = 0;
		}
	}
	public void populateFFTopBuysAuction(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" 
				+ whichSide + "$Change_Out$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide 
				+ "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" 
		+ whichSide + "$Change_Out$Select_HeaderStyle$Style3$txt_TeamName*GEOM*TEXT SET " + "AUCTION     TOP 5 BUY" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide 
				+ "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide 
				+ "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Team*GEOM*TEXT SET " + "TEAM" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Price*GEOM*TEXT SET " + "PRICE (TOKENS)" + " \0");
		
		
		
		
		/*print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET ISPL AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0"); */
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 8) break; 
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints(),false)+"L"+"\0");
        		
			}
		}
	}
	public void populateFFFiveTopBuysAuction(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		
		  print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$Players*FUNCTION*Omo*vis_con SET " 
		    		+ (which_data.equalsIgnoreCase("WithImage") ? "0" : "1") + "\0");
		  
		if(which_data.equalsIgnoreCase("WithoutImage")){
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
					+ which_data+"$Select_Type*FUNCTION*Omo*vis_con SET "+ (which_team.equalsIgnoreCase("WithoutTeam")? "0": 1) +"\0");	
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Team*GEOM*TEXT SET " + "TEAM" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Price*GEOM*TEXT SET " + "PRICE (TOKENS)" + " \0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamName*GEOM*TEXT SET " + "TOP BUYS" + " \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 5) break;
				String midPath = which_data.trim().equalsIgnoreCase("WithImage") 
					    ? which_data + "$" + row + "$" + which_team 
					    : which_data + "$" + which_team + "$" + row;
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$" + (which_data.equalsIgnoreCase("WithImage") ? "TopLine$txt_TopLine": "txt_Name")+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name() + " \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$" + (which_data.equalsIgnoreCase("WithImage") ? 
						"BottomLine$txt_TopLine": "txt_Price") +"*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints(),(which_data.equalsIgnoreCase("WithImage") ? true : false)) 
						+ "L" + (which_data.equalsIgnoreCase("WithImage") ? " TOKENS": "") + " \0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$Flag$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getNationality().trim().replace(" ", "_") + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$"+(which_data.equalsIgnoreCase("WithImage") ? "TeamNameGrp$txt_TopLine": "txt_Team") + "*GEOM*TEXT SET " + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4() + " \0");

				if(which_data.equalsIgnoreCase("WithImage")){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"+ midPath + "$img_Player*TEXTURE*IMAGE SET " 
							+ photo_mini_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
							"$BottomLine$txt_RTM*GEOM*TEXT SET " + (plyr.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "") + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
							+  which_data + "$" + row +"$Select_Style*FUNCTION*Omo*vis_con SET "+(which_team.equalsIgnoreCase("WithoutTeam")? "0": 1)+"\0");	
	        			
				}
        		
			}
		}
	}
	public void populateFFTopFiveBuysTeam(PrintWriter print_writer, int whichSide, int team_id, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		Auction session_auction = auction;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList(),session_selected_broadcaster));
		
		PlayerCount team = session_auction.getTeamZoneList().stream().filter(ply->ply.getTeamId()==team_id).findAny().orElse(null);
		top_sold = team.getPlayer();
				
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0\0");	
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$txt_Title*GEOM*TEXT SET " + "TOP BUYS" + "\0");
		
		if(team.getTeamName1().equalsIgnoreCase("AHMEDABAD APL PIPERS")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style1$txt_TeamName"
					+ "*GEOM*TEXT SET " + team.getTeamName2() + "\r\n" + team.getTeamName3() + " \0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style1$txt_TeamName"
					+ "*GEOM*TEXT SET " + team.getTeamName1() + " \0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Side" + whichSide + "$Style1$Data$MoveHeader*TRANSFORMATION*POSITION*Y SET "
				+ (team.getTeamName1().equalsIgnoreCase("U MUMBA TT") || team.getTeamName1().equalsIgnoreCase("DEMPO GOA CHALLENGERS") ? "62.0" : "0.0") +"\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$img-TeamBadges"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		
		 print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$Players*FUNCTION*Omo*vis_con SET " 
		    		+ (which_data.equalsIgnoreCase("WithImage") ? "0" : "1") + "\0");
		 
		if(which_data.equalsIgnoreCase("WithoutImage")){
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
					+ which_data+"$Select_Type*FUNCTION*Omo*vis_con SET "+ (which_team.equalsIgnoreCase("WithoutTeam")? "0": 1) +"\0");	
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Price*GEOM*TEXT SET " + "PRICE (TOKENS)" + " \0");
			
		}
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				
				if(row > 5) break;
				String midPath = which_data.trim().equalsIgnoreCase("WithImage") 
					    ? which_data + "$" + row + "$" + which_team 
					    : which_data + "$" + which_team + "$" + row;
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$" + (which_data.equalsIgnoreCase("WithImage") ? "TopLine$txt_TopLine": "txt_Name")+ "*GEOM*TEXT SET " + plyr.getFull_name() + " \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$" + (which_data.equalsIgnoreCase("WithImage") ? 
						"BottomLine$txt_TopLine": "txt_Price") +"*GEOM*TEXT SET " +  AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints(),(which_data.equalsIgnoreCase("WithImage") ? true : false)) + 
						"L" + (which_data.equalsIgnoreCase("WithImage") ? " TOKENS": "") + " \0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath +
						"$Flag$img_Flag*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality().trim().replace(" ", "_") + "\0");
				
				if(which_data.equalsIgnoreCase("WithImage")){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"+ midPath + "$img_Player"+ "*TEXTURE*IMAGE SET " 
							+ photo_mini_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
							"$BottomLine$txt_RTM*GEOM*TEXT SET " + (plyr.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "") + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
							+  which_data + "$" + row +"$Select_Style*FUNCTION*Omo*vis_con SET 0\0");	
	        			
				}
			}
		}
	 }
	
	public void populateNameSuper(PrintWriter print_writer, int whichSide, int nameSuperId , String logoe, Auction auction, AuctionService auctionService, String session_selected_broadcaster) {
		
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Age*GEOM*TEXT SET \0");
		NameSuper nameSuper = auctionService.getNameSupers().stream().filter(ns->ns.getNamesuperId() == nameSuperId).findAny().orElse(null);
		
		if(logoe.equalsIgnoreCase("WITHOUT_LOGO")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$Logo*FUNCTION*Omo*vis_con SET 0\0");
		}else {
			if(nameSuper.getSponsor() != null) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$Logo*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$img_TeamBadges*TEXTURE*IMAGE SET " 
						+ logo_path + nameSuper.getSponsor() + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$Logo*FUNCTION*Omo*vis_con SET 0\0");
			}
		}
		
		
		if(nameSuper.getSurname() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Name*GEOM*TEXT SET " 
					+ nameSuper.getFirstname() + " " + nameSuper.getSurname() +"\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Name*GEOM*TEXT SET " 
					+ nameSuper.getFirstname() + "\0");
		}
		if(nameSuper.getSubLine() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$BottomLine$txt_BasePrice*GEOM*TEXT SET " 
					+ nameSuper.getSubLine() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$BottomLine$txt_BasePrice*GEOM*TEXT SET \0");
		}
		
		
	}
	
	public void populateCrawlerRemainingPurse(PrintWriter print_writer,String which_type,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int total = 0;
		
		String crawler_Data = "";
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + "PURSE REMAINING" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + "" + " \0");
		
		for(int i=0; i <= auction.getTeam().size()-1; i++) {
			
			crawler_Data = crawler_Data + auction.getTeam().get(i).getTeamName1() + ": ";
			
			if(auction.getPlayers() != null ) {
				for(int j=0; j <= auction.getPlayers().size()-1; j++) {
					if(auction.getPlayers().get(j).getTeamId() == auction.getTeam().get(i).getTeamId()) {
						total = total + auction.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			if((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				crawler_Data = crawler_Data + "-";
			}else {
				crawler_Data = crawler_Data + AuctionFunctions.ConvertToLakh((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total),true) + "L" + "\n";
			}
			total = 0;
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");
		
	}
	public void populateCrawlerSquadSize(PrintWriter print_writer, int which_side , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		String crawler_Data = "";
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + "SQUAD SIZE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + "" + " \0");
		
		for(Team tm : auction.getTeam()) {
			crawler_Data = crawler_Data + tm.getTeamName1() + ": ";
			
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
				}
			}
			crawler_Data = crawler_Data + squadSize + "\n";
			squadSize = 0;
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");
		
		if(which_side == 1) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*start INVOKE\0");
		}
		
	}
	
	public void populateCrawlerSquad(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		String crawler_Data = "";
		squad.clear();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					squad.add(plyr);
				}
			}
		}
		Collections.sort(squad,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + auction.getTeam().get(team_id-1).getTeamName1() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + "SQUAD" + " \0");
		
		
		for(int m=0; m<= squad.size() - 1; m++) {
			if(squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)
					|| squad.get(m).getSoldOrUnsold().equalsIgnoreCase("RETAIN")) {
				crawler_Data = crawler_Data + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getFull_name() + 
						(squad.get(m).getSoldOrUnsold().equalsIgnoreCase("RETAIN") ? " (RETAINED)": "") + ": " +
			             AuctionFunctions.ConvertToLakh(squad.get(m).getSoldForPoints(),true)+"L" + "\n";
			}
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");
		
	}
	public void populateFreetextcrawler(PrintWriter print_writer, int which_side, int FlipperId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
	
	String crawler_Data = "";
	for(Flipper flipper : auctionService.getFlipper()) {
		if(flipper.getFlipperId() == FlipperId) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + flipper.getHeader() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + "" + " \0");
			
			crawler_Data = crawler_Data + flipper.getSubLine() + "\n";
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");
	}
	
	}

	public void populateCrawlerTopSold(PrintWriter print_writer,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		String crawler_Data = "";
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + "TOP BUYS" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + "" + " \0");
		
		for(int m=0; m < top_sold.size(); m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 10) {
	        		
	        		crawler_Data = crawler_Data + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFull_name() + ": " +
	        				AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints(),true) + "L" +" ( "+ 
	        				auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + " )\n";
	        	}
	        }
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");
		
	}
	public void populateCrawlTeamTopSold(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
	
        int row = 0;
        String crawler_Data = "";
        List<Player> top_sold = new ArrayList<Player>();

        if(auction.getPlayers() != null) {
        	for(Player plyr : auction.getPlayers()){
        		if(plyr.getTeamId() == team_id) {
        			top_sold.add(plyr);
        		}
        	}
        }

        Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());

        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header1*GEOM*TEXT SET " + "TOP BUYS" + " \0");
        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlHeader$Side" + which_side + "$txt_Header2*GEOM*TEXT SET " + auction.getTeam().get(team_id-1).getTeamName4() + " \0");

        for(int m=0; m<= top_sold.size() - 1; m++) {
        	if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
        		row = row + 1;
        		if(row <= 10) {
        			crawler_Data = crawler_Data + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFull_name() + ": "+ 
        					AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints(),true) + "L" + "\n";
        		}
        	}
        }
        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*text SET " + crawler_Data + " \0");
        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + which_side + "$Crawl*GEOM*build INVOKE\0");

	}
	
	public void populateFlipper(PrintWriter print_writer, int whichSide, String value, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row=0,totalAmountSpent=0;
		switch (value.toLowerCase()) {
		case "top_buys":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + "TOP BUYS" + "\0");
			List<Player> top_sold = new ArrayList<Player>();
			if(auction.getPlayers() != null) {
				top_sold = auction.getPlayers();
			}
			Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
			for(Player plyr : top_sold) {
				row = row + 1;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + plyr.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$FlagAll$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality().trim().replace(" ", "_") + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints(),false) + "L TOKENS" + "\0");
			}
			
			for(int i=row+1;i<=8;i++) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + i + "*ACTIVE SET 0\0");
			}
			break;
		case "remain_purse":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + "PURSE REMAINING" + "\0");
			for(Team tm : auction.getTeam()) {
				row++;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
				
				for(Player auc : auction.getPlayers()) {
					if(tm.getTeamId() == auc.getTeamId()) {
						totalAmountSpent += auc.getSoldForPoints();
					}
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent),false) + "L TOKENS" + "\0");
				
				totalAmountSpent = 0;
			}
			break;
		}
	}
	public void populateFlipperSquad(PrintWriter print_writer, int whichSide, int teamId, Auction auction, AuctionService auctionService, String session_selected_broadcaster) throws Exception {
		
		int row=0;
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataUTTZone(auction,team_id);
		Auction session_auction = auction;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList(),session_selected_broadcaster));
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + 
				auction.getTeam().get(teamId-1).getTeamName1() + "\0");
		PlayerCount teamZone = session_auction.getTeamZoneList().stream().filter(tz -> tz.getTeamId() == teamId).findFirst().orElse(null);
		if (teamZone != null) {
			for(Player ply : teamZone.getPlayer()) {
				row++;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$FlagAll$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + ply.getNationality().trim().replace(" ", "_") + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh(ply.getSoldForPoints(),false) + "L TOKENS" + "\0");
			}
//			for(String Str:data_str) {
//				if(Str.matches("IND_M|IND_F|INT_M|INT_F")) {
//					row++;
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 0\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET \0");
//					
//					switch(Str) {
//					case "IND_M":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INDIAN MALE" + "\0");
//						break;
//					case "IND_F":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INDIAN FEMALE" + "\0");
//						break;
//					case "INT_M":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INT. MALE" + "\0");
//						break;
//					case "INT_F":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INT. FEMALE" + "\0");
//						break;
//					}
//				}
//			}
		}
		for(int i=row+1;i<=8;i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + i + "*ACTIVE SET 0\0");
		}
	}
	
	public void populateSquad(PrintWriter print_writer,int team_id, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int row = 0,pruse=0;
		data_str.clear();
		List<Player> retained = new ArrayList<Player>();
		
		data_str = AuctionFunctions.getSquadDataUTTZone(match,team_id);
		
		if(match.getPlayers() != null) {
			for(Player player : match.getPlayers()) {
				if(player.getTeamId() == team_id) {
					if(player.getSoldOrUnsold().equalsIgnoreCase("RETAIN") ||
							player.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM) ||
							player.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						
						retained.add(player);
					}
				}
			}
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0\0");	
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$SubHeader$Select_Subheader*FUNCTION*Omo*vis_con SET 1\0");		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + which_side + "$Select_Graphics*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Side" + which_side + "$Style1$Data$MoveHeader*TRANSFORMATION*POSITION*Y SET "
				+ (match.getTeam().get(team_id-1).getTeamName1().equalsIgnoreCase("U MUMBA TT") || match.getTeam().get(team_id-1).getTeamName1().equalsIgnoreCase("DEMPO GOA CHALLENGERS") 
						? "62.0" : "0.0") +"\0");

		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_Title"
				+ "*GEOM*TEXT SET " +"SQUAD" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_TeamName"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName1()+ "\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_TeamLastName"
//				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName3()+ "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$img-TeamBadges"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		
		if (retained != null) {				
			for(Player ply : retained) {
				row++;
				pruse = pruse + ply.getSoldForPoints();
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$Select_PlayerNumber$" + row + 
						"$Select_Style*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$img_Player*TEXTURE*IMAGE SET "+ photo_mini_path + ply.getPhotoName() + AuctionUtil.PNG_EXTENSION  +"\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$Flag$img_Flag*TEXTURE*IMAGE SET "+ flag_path + ply.getNationality().trim().replace(" ", "_") + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$TopLine$txt_TopLine*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$BottomLine$txt_TopLine*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints(),true) + "L TOKENS"+ "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$BottomLine$RTM$txt_RTM*GEOM*TEXT SET " + (ply.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "RET.") + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$BottomLine$RTM*ACTIVE SET " + (ply.getSoldOrUnsold().equalsIgnoreCase("RETAIN") ? 1 : 0) + "\0");
			}
			for(String Str:data_str) {
				if(Str.matches("IND_M|IND_F|INT_M|INT_F")) {
					row++;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$Select_PlayerNumber$" + row + 
							"$Select_Style*FUNCTION*Omo*vis_con SET 1\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$" + row + "$PlayerGrp$WithIcon"
							+ "$img_Icon*TEXTURE*IMAGE SET "+ icon_path + Str  +"\0");
				}
			}
		}
		
		pruse = Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - pruse;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$Select_Subheader$Text$txt_SubHead2*GEOM*TEXT SET " 
				+ AuctionFunctions.ConvertToLakh(pruse,true) + "L TOKENS" + "\0");
	}
	
	public void populateTeamDetails(PrintWriter print_writer, int team_id, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int squadSize = 0, purse = 0, remain_purse = 0;
		
		for(Player auc : match.getPlayers()) {
			if(team_id == auc.getTeamId()) {
				squadSize++;
				purse = purse + auc.getSoldForPoints();
			}
		}
		
		remain_purse = (Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - purse) ;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Team_Info$Team$Side" + which_side + "$Header$txt_Header"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName2()+ "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Team_Info$Team$Side" + which_side + "$Header$txt_PlayerName"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName3()+ "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Team_Info$Team$Side" + which_side + "$Logo$img_Image"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Team_Info$Team$Side" + which_side + "$Squad_Size$txt_Tokens"
				+ "*GEOM*TEXT SET " + squadSize + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Team_Info$Team$Side" + which_side + "$Purse_Remaining$txt_Tokens"
				+ "*GEOM*TEXT SET " +  AuctionFunctions.ConvertToLakh(remain_purse,false) + "L TOKENS" + "\0");
		
	}
	public void populateTeamsDetails(PrintWriter print_writer, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws Exception 
	{
		int squadSize = 0, purse = 0, remain_purse = 0, row = 0;
		
		for(Team tm : match.getTeam()) {
			row = row + 1;
			
			for(Player auc : match.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
					purse = purse + auc.getSoldForPoints();
				}
			}
			
			remain_purse = (Integer.valueOf(tm.getTeamTotalPurse()) - purse) ;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Teams_Details$Team$Teams_Details$Players$Players$" + row + "$PlayerGrp$WithData$txt_Team_Name"
					+ "*GEOM*TEXT SET " + tm.getTeamName1()+ "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Teams_Details$Team$Teams_Details$Players$Players$" + row + "$PlayerGrp$WithData$img_Logo"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Teams_Details$Team$Teams_Details$Players$Players$" + row + "$PlayerGrp$NameGrp$TopLine$txt_TopLine"
					+ "*GEOM*TEXT SET " + "PURSE REM. - " + AuctionFunctions.ConvertToLakh(remain_purse,true) + "L TOKENS" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Teams_Details$Team$Teams_Details$Players$Players$" + row + "$PlayerGrp$NameGrp$BottomLine$txt_TopLine"
					+ "*GEOM*TEXT SET " + "SQUAD - " + squadSize + "\0");
			
			squadSize = 0;
			purse = 0;
			remain_purse = 0;
		}
	}
	public void populateFFRetainPlayers(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		List<Player> retained = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 7\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamName*GEOM*TEXT SET " + "RETAINED\r\nPLAYERS" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		for(Player player : top_sold) {
			if(player.getSoldOrUnsold().equalsIgnoreCase("RETAIN")) {
				retained.add(player);
			}
		}
		
		for(Player player : retained) {
			row = row + 1;
			
			if(player.getSoldOrUnsold().equalsIgnoreCase("RETAIN")) {
				
				if(row <= 4) {
					Player plyr = auction.getPlayersList().stream().filter(pyr -> pyr.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Retained$Select_PlayerNumber$" + row + "$WithData$"
        					+ "NameGrp$TopLine$txt_TopLine*GEOM*TEXT SET " + plyr.getFull_name() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Retained$Select_PlayerNumber$" + row + "$WithData$"
							+ "img_Player*TEXTURE*IMAGE SET " + photo_mini_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Retained$Select_PlayerNumber$" + row + "$WithData$"
							+ "Flag$img_Flag*TEXTURE*IMAGE SET " + logo_path + auctionService.getTeams().get(plyr.getTeamId() - 1).getTeamName4() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Retained$Select_PlayerNumber$" + row + "$WithData$"
							+ "NameGrp$BottomLine$Maxsize$txt_TopLine*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(player.getSoldForPoints(),true)+ "L TOKENS" + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Retained$Select_PlayerNumber$" + row + "$WithData$"
							+ "NameGrp$BottomLine$RTM$txt_RTM*GEOM*TEXT SET\0");
					
				}
			}
		}
	}
	
	public void populateZoneSquad(PrintWriter print_writer,String ZoneName, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		String HeaderValue = "";
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		
		HeaderValue = (ZoneName.equalsIgnoreCase("IND.M") ? "INDIAN MALE" : ZoneName.equalsIgnoreCase("IND.F") ? "INDIAN FEMALE" : 
			ZoneName.equalsIgnoreCase("INT.M") ? "FOREIGN MALE" : "FOREIGN FEMALE") + "\nTO BE AUCTIONED";
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style2$txt_TeamName*GEOM*TEXT SET " + HeaderValue + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style2$img-TeamBadges"
				+ "*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics*FUNCTION*Omo*vis_con SET 5\0");

		for(int i=1;i<=12;i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + i + "*ACTIVE SET 0\0");
		}
	    for(int k = current_index; k<= squad.size()-1 ;k++) {
		   Player plyr = squad.get(k);
			row = row + 1;
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "*ACTIVE SET 1\0");
			
			if(plyr.getSurname() != null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$NameGrp$"
						+ "TopLine$txt_TopLine*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$NameGrp$"
						+ "BottomLine$txt_TopLine*GEOM*TEXT SET " + plyr.getSurname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$NameGrp$"
						+ "TopLine$txt_TopLine*GEOM*TEXT SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$NameGrp$"
						+ "BottomLine$txt_TopLine*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$img_Player"
					+ "*TEXTURE*IMAGE SET " + photo_mini_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Unsold_YTBD$Players$Players$" + row + "$WithData$Flag$"
					+ "img_Flag*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality() + "\0");
			
			if(row == 12) {
				break;
			}
		}
		if(which_side == 1) {
			current_index =(current_index + 12);
		}
	}
	
	public void populateRTMAvailable(PrintWriter print_writer, int whichSide, Auction match, AuctionService auctionService) {
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == 
				match.getPlayers().get(match.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$img_TeamBadges*TEXTURE*IMAGE SET " + logo_path + 
				auctionService.getTeams().get(player.getLastYearTeam()-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$Info$Status$Side" + whichSide + "$txt_Status*GEOM*TEXT SET AVAILABLE\0");
	}
	public void populateRTMEnabled(PrintWriter print_writer, int whichSide) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$Info$Status$Side" + whichSide + "$txt_Status*GEOM*TEXT SET ENABLED\0");
	}
	public void populateProfileStats(PrintWriter print_writer, String whichType, int whichSide, Auction auction, AuctionService auctionService) {
		
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		Statistics stats = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$Select*FUNCTION*Omo*vis_con SET 0\0");
		
		switch (whichType.split(",")[0].toUpperCase()) {
		case "CATEGORY":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET " + "CATEGORY "+"\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					getCategory(player.getCategory()) + "\0");
			break;
		case "RANK":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET " + 
					"WORLD RANK - WEEK 15 " + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					(stats.getRank().equalsIgnoreCase("NA") ? "-" : stats.getRank()) + "\0");
			break;
		case "STYLE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					stats.getStyle() + "\0");
			break;
		case "BIO":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					(stats.getInfo1() != null ? stats.getInfo1() : "") + "\0");
			break;
		case "PREVTEAM":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET "
					+"LAST YEAR TEAM "+"\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					(player.getLastYearTeam()== null ? "-": auction.getTeam().get(player.getLastYearTeam()-1).getTeamName1()) + "\0");
			break;
		}
	}
	public void populateTeamCurrentBid(PrintWriter print_writer, int team_id, int whichSide, Auction auction, Auction current_bid, AuctionService auctionService) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET CURRENT BID\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + 
				auction.getTeam().get(team_id -1).getTeamName1() + "\0");
	}
	
	public static String getCategory(String category) {
	    if (category == null || category.isEmpty()) {
	        return "-";
	    }

	    String[] parts = category.split("\\.");

	    if (parts.length < 2) {
	        return "INVALID";
	    }

	    String nationCode = parts[0].equals("IND") ? "INDIAN" : "FOREIGN";
	    String genderCode = parts[1].equals("M") ? "MALE" : "FEMALE";

	    return nationCode + " " + genderCode;
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
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF-PLAYERPROFILE":
				previewCommand = previewCommand +  "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data 0.800 anim_InfoBar$In_Out$CenterData$Data$In 0.800";
				
				if(isProfileStatsOnScreen == false) {
					previewCommand += " anim_InfoBar$In_Out$BottomStats 0.800 anim_InfoBar$In_Out$BottomStats$In 0.800 ";
				}
				
				if(data.isBid_Start_or_not()) {
					previewCommand += "anim_InfoBar$In_Out$CurrentBid 0.800 anim_InfoBar$In_Out$CurrentBid$In 0.800 ";
				}
				if(data.getBid_result()!=null && (data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM))) {
					previewCommand += "anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$Sold$In 0.800 ";
				}else if(data.getBid_result()!= null && data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					previewCommand += "anim_InfoBar$In_Out$Unsold 0.800 anim_InfoBar$In_Out$UnSold$In 0.800 ";
				}
				break;
			case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$In 0.800";
				break;
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand + "anim_InfoBar$In_Out$BottomStats 0.800 anim_InfoBar$In_Out$BottomStats$In 0.800";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 anim_InfoBar$In_Out$CenterData$Data 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data$In 0.800 anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$RTM$Logo 0.800 "
						+ "anim_InfoBar$In_Out$RTM$Logo$In 0.500 anim_InfoBar$In_Out$RTM$Essentials 0.800 anim_InfoBar$In_Out$RTM$Essentials$In 0.700";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": 
			case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": 
			case "POPULATE-LOF_SQUAD": case "POPULATE-ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "anim_LOF$In_Out$Essentials$In 1.100 anim_LOF$In_Out$Header$In 1.200 anim_LOF$In_Out$SubHeader$In 1.300 "
						+ "anim_LOF$In_Out$Main 2.000 ";
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-ZONEWISE_PLAYERS_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$RemainingPurse$In 2.100 anim_LOF$In_Out$Main$RemainingPurse$In$Name$In 1.100 "
							+ "anim_LOF$In_Out$Main$RemainingPurse$In$Logo$In 1.100";
					break;
				case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$Squad_Size$In 2.100 anim_LOF$In_Out$Main$Squad_Size$In$Name$In 1.100 "
							+ "anim_LOF$In_Out$Main$Squad_Size$In$Logo$In 1.100";
					break;
					
				case "POPULATE-LOF_TEAM_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$Top_Buys_Team$In 2.100 anim_LOF$In_Out$Main$Top_Buys_Team$In$Logo$In 0.900";
					break;
				case "POPULATE-LOF_SQUAD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$Squad$In 2.100 anim_LOF$In_Out$Main$Squad$In$Logo$In 0.900";
					break;
					
				case "POPULATE-LOF_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$Top_Buys$In 2.100 anim_LOF$In_Out$Main$Top_Buys$In$Logo$In 0.900";
					break;
				case "POPULATE-SQUAD-PLAYER":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Category 2.000 anim_LOF$In_Out$Main$SquadSize_Category$In 1.900 "
							+ "CtegoryHighlight$Side1$" + rowHighlight + " 1.200";
					for(int i=(rowHighlight+1);i<=8;i++) {
						previewCommand = previewCommand + " MoveForCatHighlight$Side1$" + i + " 1.200";
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
				previewCommand = previewCommand +  "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data 0.800 anim_InfoBar$In_Out$CenterData$Data$In 0.800 ";
				if(data.isBid_Start_or_not()) {
					previewCommand += "anim_InfoBar$In_Out$CurrentBid 0.800 anim_InfoBar$In_Out$CurrentBid$In 0.800 ";
				}
				if(data.getBid_result()!=null && (data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM))) {
					previewCommand += "anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$Sold$In 0.800 ";
				}else if(data.getBid_result()!= null && data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					previewCommand += "anim_InfoBar$In_Out$Unsold 0.800 anim_InfoBar$In_Out$UnSold$In 0.800 ";
				}
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand+ "Change_InfoBar$BottomStats 0.800 Change_InfoBar$BottomStats$In 0.800";
				break;
			}
			
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
			case "LOF_TEAM_TOP_SOLD": case "LOF_SQUAD": case "LOF_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$Header$Change_Out 0.500 Change_LOF$Header$Change_In 1.000 Change_LOF$SubHeader$Change_Out 0.500 "
						+ "Change_LOF$SubHeader$Change_In 1.000 ";
				break;
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_Out 0.920 Change_LOF$RemainingPurse$Change_Out$Name$Change_Out 0.600 "
						+ "Change_LOF$RemainingPurse$Change_Out$Logo$Change_Out 0.600 ";
				break;
			case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE":
				previewCommand = previewCommand + "Change_LOF$Squad_Size$Change_Out 0.920 Change_LOF$Squad_Size$Change_Out$Name$Change_Out 0.600 "
						+ "Change_LOF$Squad_Size$Change_Out$Logo$Change_Out 0.600 ";
				break;
			case "LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$Top_Buys_Team$Change_Out 0.920 Change_LOF$Top_Buys_Team$Change_Out$Logo$Change_Out 0.500 ";
				break;
			case "LOF_SQUAD":
				previewCommand = previewCommand + "Change_LOF$Squad$Change_Out 0.920 Change_LOF$Squad$Change_Out$Logo$Change_Out 0.500 ";
				break;
			case "LOF_TOP_SOLD": 
				previewCommand = previewCommand + "Change_LOF$Top_Buys$Change_Out 0.920 Change_LOF$Top_Buys$Change_Out$Logo$Change_Out 0.500 ";
				break;
			case "SQUAD-PLAYER":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_Out 0.800 ";
				break;
			case "LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_Out 0.820 ";
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS": case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "Change$Stats$Change_Out 1.000 Change$Stats$Change_In 1.300";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_In 2.100 Change_LOF$RemainingPurse$Change_In$Name$In 1.100 "
						+ "Change_LOF$RemainingPurse$Change_In$Logo$In 1.100 ";
				break;
			case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE":
				previewCommand = previewCommand + "Change_LOF$Squad_Size$Change_In 2.100 Change_LOF$Squad_Size$Change_In$Name$In 1.100 "
						+ "Change_LOF$Squad_Size$Change_In$Logo$In 1.100 ";
				break;
			case "POPULATE-LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$Top_Buys_Team$Change_In 2.100 Change_LOF$Top_Buys_Team$Change_In$Logo$In 0.900 ";
				break;	
			case "POPULATE-LOF_SQUAD":
				previewCommand = previewCommand + "Change_LOF$Squad$Change_In 2.100 Change_LOF$Squad$Change_In$Logo$In 0.900 ";
				break;	
			case "POPULATE-LOF_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$Top_Buys$Change_In 2.100 Change_LOF$Top_Buys$Change_In$Logo$In 0.900 ";
				break;
				
			case "POPULATE-LOF_SQUAD_REMAIN":
				previewCommand = previewCommand + "Change_LOF$TopBuysTeam$Change_In 1.600";
				break;
			case "POPULATE-SQUAD-PLAYER":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_In 1.700 CtegoryHighlight$Side2$" + rowHighlight + " 0.800 ";
				for(int i=(rowHighlight+1);i<=8;i++) {
					previewCommand = previewCommand + "MoveForCatHighlight$Side2$" + i + " 1.000 ";
				}
				break;
			case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_In 1.680 ";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.tga " + previewCommand + "\0");
		
	}
	public void processPreviewFullFrames(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		if (whatToProcess.equalsIgnoreCase("POPULATE-PROFILE_STATS_CHANGE")) {
			previewCommand = "anim_Profile 2.800 anim_Profile$In_Out 1.700 anim_Profile$In_Out$Essentials 1.700 anim_Profile$In_Out$Essentials$In 1.400 "
					+ "anim_Profile$In_Out$Profile 2.800 anim_Profile$In_Out$Profile$In 2.800 Change_Stats 0.700 Change_Stats$Side1 0.700 Change_Stats$Side2 0.700 ";	
		}
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT":
				previewCommand = "anim_Ident$In_Out$Ident$In 1.600 ";
				break;
			case "POPULATE-TEAMS":
				previewCommand = "anim_Team_Info$In_Out 1.700 anim_Team_Info$In_Out$Essentials 1.700 anim_Team_Info$In_Out$Essentials$In 1.400 "
						+ "anim_Team_Info$In_Out$Team_Info 1.700 anim_Team_Info$In_Out$Team_Info$In 1.700 ";
				break;
			case "POPULATE-TEAMS_DETAILS":
				previewCommand = "anim_Teams_Details$In_Out 1.700 anim_Teams_Details$In_Out$Essentials 1.700 anim_Teams_Details$In_Out$Essentials$In 1.400 "
						+ "anim_Teams_Details$In_Out$Team_Info 1.700 anim_Teams_Details$In_Out$Team_Info$In 1.400 anim_Teams_Details$In_Out$Team_Info$In$In 1.400 ";
				break;
				
			case "POPULATE-ZONE_PLAYERS_STATS":
				previewCommand = "anim_Fullframes 2.800 anim_Fullframes$In_Out$Essentials$In 1.400 anim_Fullframes$In_Out$Header$In 1.000 "
						+ "anim_Fullframes$In_Out$Unsold_YTBD$In 1.700";
				break;
			case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
				previewCommand = "anim_Fullframes 2.800 anim_Fullframes$In_Out$Essentials$In 1.700 anim_Fullframes$In_Out$Header$In 1.700 "
						+ "anim_Fullframes$In_Out$Team_Details$In 1.600";
				break;
			case "POPULATE-PLAYERPROFILE_FF":
				previewCommand = "anim_Profile$In_Out$Essentials$In 1.400  anim_Profile$In_Out$Profile$In 1.700";
				break;
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-SQUAD": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
			case "POPULATE-FF_RETAIN_PLAYERS":
				previewCommand = "anim_Fullframes 2.800 anim_Fullframes$In_Out$Essentials$In 1.400 anim_Fullframes$In_Out$Header$In 1.000 ";
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframes$In_Out$Top5_Buys$In 1.500";
					break;
				case "POPULATE-SQUAD":
					previewCommand = previewCommand + "anim_Fullframes$In_Out$Squad$In 1.500";
					break;
				case "POPULATE-FF_RETAIN_PLAYERS":
					previewCommand = previewCommand + "anim_Fullframes$In_Out$Retained$In 1.300";
					break;
				}
				break;
			}
		}else {
			switch (which_graphics_onscreen.toUpperCase()) {
			case "FF_FIVE_TOP_BUYS_AUCTION": case "SQUAD": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":case "FF_RTM_AND_PURSE_REMAINING": 
			case "ZONE-PLAYER_STATS":
				previewCommand = "Change_Fullframes 1.700 Change_Fullframes$Header 1.000 ";
				switch (which_graphics_onscreen.toUpperCase()) {
				case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
					previewCommand = previewCommand + "Change_Fullframes$Top5_Buys 1.500 Change_Fullframes$Top5_Buys$Change_Out 0.600 "
							+ "Change_Fullframes$Top5_Buys$Change_In 1.500 ";
					break;
				case "SQUAD":
					previewCommand = previewCommand + "Change_Fullframes$Squad 1.500 Change_Fullframes$Squad$Change_Out 0.640 "
							+ "Change_Fullframes$Squad$Change_In 1.500 ";
					break;
				case "FF_RTM_AND_PURSE_REMAINING":
					previewCommand = previewCommand + "Change_Fullframes$Team_Details 1.700 Change_Fullframes$Team_Details$Change_Out 0.720 "
							+ "Change_Fullframes$Team_Details$Change_In 1.700 ";
					break;
				case "ZONE-PLAYER_STATS":
					previewCommand = previewCommand + "Change_Fullframes$Unsold_YTBD 1.700 Change_Fullframes$Unsold_YTBD$Change_Out 0.640 "
							+ "Change_Fullframes$Unsold_YTBD$Change_In 1.700 ";
					break;
				}
				break;
			}
			
			System.out.println(whatToProcess + " - " + which_graphics_onscreen);
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				if(!which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUYS_AUCTION") && !which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUY_TEAM")) {
					previewCommand = previewCommand + "Change_Fullframes$Top5_Buys 1.500 Change_Fullframes$Top5_Buys$Change_Out 0.600 "
							+ "Change_Fullframes$Top5_Buys$Change_In 1.500";	
				}
				break;
			case "POPULATE-SQUAD":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
					previewCommand = previewCommand + "Change_Fullframes$Squad 1.500 Change_Fullframes$Squad$Change_Out 0.640 "
							+ "Change_Fullframes$Squad$Change_In 1.500";
				}
				break;
			case"POPULATE-FF_RTM_AND_PURSE_REMAINING":
				previewCommand = previewCommand + "Change_Fullframes$Team_Details 1.500 Change_Fullframes$Team_Details$Change_Out 0.600 "
						+ "Change_Fullframes$Team_Details$Change_In 1.500";
				break;
			case "POPULATE-ZONE_PLAYERS_STATS":
				if(!which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS") && !which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_FULL")) {
					previewCommand = previewCommand + "Change_Fullframes$Unsold_YTBD 1.700 Change_Fullframes$Unsold_YTBD$Change_Out 0.640 "
							+ "Change_Fullframes$Unsold_YTBD$Change_In 1.700 ";
				}
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/gfx_Fullframes " + "C:/Temp/Preview.tga " + previewCommand + "\0");
	}
	public void processPreviewLowerThirds(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-L3-NAMESUPER":
				previewCommand = "anim_LowerThird 1.400 anim_LowerThird$In_Out 0.800 anim_LowerThird$In_Out$CenterData 0.800 "
						+ "anim_LowerThird$In_Out$CenterData$Essentials$In 0.600 anim_LowerThird$In_Out$CenterData$Image$In 0.600 "
						+ "anim_LowerThird$In_Out$CenterData$Data$In 0.600";
				break;
			case "POPULATE-L3-FLIPPER": case "POPULATE-FLIPPER_SQUAD":
				previewCommand = "Flipper$In_Out$In 0.560 Scroll 8.540 ";
				break;
			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "Change_LowerThird 0.900 Change_LowerThird$Change_Out 0.600 Change_LowerThird$Chnage_In 0.900";
				break;
			case "POPULATE-L3-FLIPPER":
				previewCommand = "Flipper$In_Out$In 0.560 Scroll 8.540 ";
				break;
				
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.tga " + previewCommand + "\0");
	}
	
	public void processPreviewCrawler(PrintWriter print_writer, String whatToProcess, int whichSide) throws InterruptedException {
		String previewCommand = "";
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + whichSide + "$Crawl*GEOM*start INVOKE\0");
		
		TimeUnit.MILLISECONDS.sleep(1500);
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-CRAWLE_SQUAD": case "POPULATE-CRAWL-PURSE_REMAINING": case "POPULATE-CRAWL-SQUAD_SIZE": case "POPULATE-CRAWL_TOP_SOLD": 
			case "POPULATE-CRAWLER_TEAM_TOP_SOLD": case "POPULATE-L3-CRWLERFREETEXT":
				previewCommand = "anim_Crawl$In_Out 1.000 anim_Crawl$In_Out$In 1.000";
				break;
			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-CRAWLE_SQUAD": case "POPULATE-CRAWL-PURSE_REMAINING": case "POPULATE-CRAWL-SQUAD_SIZE": case "POPULATE-CRAWL_TOP_SOLD": 
			case "POPULATE-CRAWLER_TEAM_TOP_SOLD": case "POPULATE-L3-CRWLERFREETEXT":
				previewCommand = "Change_Crawl 0.800 Change_Crawl$Header 0.800 Change_Crawl$Crawl 0.800";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.tga " + previewCommand + "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Crawl$CrawlAll$Side" + whichSide + "$Crawl*GEOM*reset INVOKE\0");
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	
}