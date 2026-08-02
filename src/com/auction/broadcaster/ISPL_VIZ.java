package com.auction.broadcaster;

import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.Statistics;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
import com.auction.model.NameSuper;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class ISPL_VIZ extends Scene{

	private String status;
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "ISPL_VIZ";
	public Data data = new Data();
	public String which_graphics_onscreen = "BG";
	public int current_layer = 2, whichSide = 1;

	private String base_path = "IMAGE*/Default/Essentials/Base";
	private String text_path = "IMAGE*/Default/Essentials/Text";
	private String logo_path = "IMAGE*/Default/Essentials/TeamLogo/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	public ISPL_VIZ() {
		super();
	}

	public ISPL_VIZ(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Data updateData(Scene scene, Auction auction, Auction session_curr_bid,AuctionService auctionService, PrintWriter print_writer) throws InterruptedException
	{
		if(which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE")) {
			populatePlayerProfile(true,print_writer, "", data.getPlayer_id(),auctionService.getAllStats(),auction, 
					session_curr_bid,auctionService, session_selected_broadcaster);
			
			
			if(data.getPreviousBid() < session_curr_bid.getCurrentPlayers().getSoldForPoints()) {
				BidChangeOn(print_writer, session_curr_bid, data.getWhichside());
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*CurrentBid$Info START \0");
				TimeUnit.MILLISECONDS.sleep(1000);
				BidChangeOn(print_writer, session_curr_bid, 1);
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*CurrentBid$Info SHOW 0.0 \0");
				
				data.setPreviousBid(session_curr_bid.getCurrentPlayers().getSoldForPoints());
			}
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, Auction session_curr_bid, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws InterruptedException, NumberFormatException, IllegalAccessException {
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SINGLE_PURSE": case "POPULATE-TOP_SOLD":
		case "POPULATE-L3-NAMESUPER": case "POPULATE-TOP_SOLD_TEAM": case "POPULATE-IDENT":
			switch (session_selected_broadcaster.toUpperCase()) {
			case "ISPL_VIZ":
				if(which_graphics_onscreen != "") {
					switch(which_graphics_onscreen) {
					case "PLAYERPROFILE": 
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER":
					case "TOP_SOLD_TEAM": case "IDENT":
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					}
				}
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-L3-INFOBAR":
					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
					break;
				default:
					scenes.add(new Scene(valueToProcess.split(",")[0],"0"));
					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-FF-PLAYERPROFILE":
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[1]));
					data.setBid_Start_or_not(false);
					populatePlayerProfile(false,print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),
							auctionService.getAllStats(),auction, session_curr_bid,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-REMAINING_PURSE_ALL":
					populateRemainingPurse(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-SQUAD":
					populateSquad(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService,session_selected_broadcaster);
					break;
				case "POPULATE-TOP_SOLD":
					populateTopSold(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-IDENT":
					populateIdent(print_writer, valueToProcess.split(",")[0],session_selected_broadcaster);
					break;
				case "POPULATE-TOP_SOLD_TEAM":
					populateTopSoldTeam(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-L3-NAMESUPER":
					populateNameSuper(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService, session_selected_broadcaster);
					break;
				}
			}
			
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "ISPL_VIZ":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-CURR_BID":
					print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side1$txt_Price*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid())  + "\0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_CurrentBid START \0");
					data.setBid_Start_or_not(true);
					break;
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					data.setBid_Start_or_not(false);
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$BasePrice SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$CurrentBid SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_CurrentBid SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_Unsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All START \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo START \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice START \0");
					which_graphics_onscreen = "PLAYERPROFILE";
					break;
				case "ANIMATE-IN-SQUAD":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "SQUAD";
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "REMAINING_PURSE_ALL";
					break;
				case "ANIMATE-IN-SINGLE_PURSE":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "SINGLE_PURSE";
					break;
				case "ANIMATE-IN-TOP_SOLD":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD";
					break;
				case "ANIMATE-IN-TOP_SOLD_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD_TEAM";
					break;
				case "ANIMATE-IN-NAMESUPER":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "NAMESUPER";
					break;
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "IDENT";
					break;
				
				case "CLEAR-ALL":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$BasePrice SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$CurrentBid SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_CurrentBid SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_Unsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In SHOW 0.0 \0");
		               
		               which_graphics_onscreen = "";
					break;
				
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "INFOBAR":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,current_layer);
						which_graphics_onscreen = "";
						break;
					
					
					case "PLAYERPROFILE":
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice CONTINUE \0");
						which_graphics_onscreen = "";
						data.setPreviousBid(300000);
						break;
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER": case "TOP_SOLD_TEAM": case "IDENT":
						AnimateOutGraphics(print_writer, whatToProcess.toUpperCase());
						which_graphics_onscreen = "";
						break;
					}
					break;
				}
				
			}
			
			
		}
		return null;
	}

	public void populateIdent(PrintWriter print_writer,String viz_scene,String session_selected_broadcaster) {
		print_writer.println("-1 RENDERER*TREE*$Main$txt_Info2*GEOM*TEXT SET "+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$txt_Info2*GEOM*TEXT SET "+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.600 \0");
	}
	
	public void populateNameSuper(PrintWriter print_writer,String viz_scene, int nameSuperId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		print_writer.println("-1 RENDERER*TREE*$Main$SelectPointer*FUNCTION*Omo*vis_con SET 1 \0");
		print_writer.println("-1 RENDERER*TREE*$Main$SelectPointer*FUNCTION*Omo*vis_con SET 1 \0");
		for(NameSuper ns : auctionService.getNameSupers()) {
			if(ns.getNamesuperId() == nameSuperId) {
				if(ns.getSurname() != null) {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_LastName*GEOM*TEXT SET "+ ns.getSurname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_LastName*GEOM*TEXT SET "+ "" + "\0");
				}
				if(ns.getSubLine() != null) {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_Text1*GEOM*TEXT SET "+ ns.getSubLine() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_Text1*GEOM*TEXT SET "+ ns.getSubLine() + "\0");
				}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.300 \0");
	}
	public void populatePlayerProfile(boolean is_this_updating,PrintWriter print_writer,String viz_scene, int playerId,List<Statistics> stats, Auction auction, Auction session_curr_bid,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(session_curr_bid.getCurrentPlayers() != null) {
			if(data.isBid_Start_or_not() == false) {
				if(playerId == session_curr_bid.getCurrentPlayers().getPlayerId()) {
					if(session_curr_bid.getCurrentPlayers().getSoldForPoints() > 300000) {
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_CurrentBid START \0");
						data.setBid_Start_or_not(true);
					}
				}
			}
//			if(data.isBid_Start_or_not()) {
//				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side1$txt_Price*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(session_curr_bid.getCurrentPlayers().getSoldForPoints()) + "\0");
//			}
			if(data.isPlayer_sold_or_unsold() == false) {
				for(int i=auction.getPlayers().size()-1; i >= 0; i--) {
					if(playerId == auction.getPlayers().get(i).getPlayerId()) {
						if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side2$Img_Text1*TEXTURE*IMAGE SET " + text_path + "2/" + "GREEN" + "\0");
							print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_BasePrice$Side2$txt_Unit*GEOM*TEXT SET "+ "" + "\0");
							print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_BasePrice$Side2$txt_Price*GEOM*TEXT SET "+ auctionService.getTeams().get(auction.getPlayers().get(i).getTeamId() - 1).getTeamName4() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side2$txt_Price*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(auction.getPlayers().get(i).getSoldForPoints())  + "\0");
							
							print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side2$txt_Header*GEOM*TEXT SET "+ "SOLD FOR" + "\0");
							
							if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
								print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_BasePrice$Side2$txt_Header*GEOM*TEXT SET "+ "SOLD TO" + "\0");
							}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_BasePrice$Side2$txt_Header*GEOM*TEXT SET "+ "RTM TO" + "\0");
							}
							
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$BasePrice START \0");
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change$CurrentBid START \0");
							if(data.isBid_Start_or_not() == false) {
								if(auction.getPlayers().get(i).getSoldForPoints() == 300000) {
									print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_CurrentBid START \0");
									data.setBid_Start_or_not(true);
								}
							}
							data.setPlayer_sold_or_unsold(true);
						}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_Unsold START \0");
							data.setPlayer_sold_or_unsold(true);
						}
						break;
					}
				}
			}
		}
		if(is_this_updating == false) {
			data.setPlayer_sold_or_unsold(false);
			if(auctionService.getAllPlayer().get(playerId - 1).getTicker_name() != null) {
				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$NameGrp$Img_Text1$txt_Name*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getTicker_name() + " \0");
				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$NameGrp$Img_Text1$txt_Name*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getTicker_name() + " \0");
				
			}else {

				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$NameGrp$Img_Text1$txt_Name*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getFirstname() + " \0");
				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$NameGrp$Img_Text1$txt_Name*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getFirstname() + " \0");
			}
			if(auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$Side1$txt_Info*GEOM*TEXT SET " + "BATTER - "+ auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$Side1$txt_Info*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + " - " + auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
			}
			print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_PlayerInfo$WhiteBase*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_BasePrice$Side1$txt_Price*GEOM*TEXT SET "+ "3" + "\0");
			
		}
		if(is_this_updating == false) {
//			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All SHOW 0.0 \0");
//			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo SHOW 0.0 \0");
//			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice SHOW 0.0 \0");
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_Graphics SHOW 0.0 \0");
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png Anim_Graphics$Anim_All 0.500 Anim_Graphics$Anim_All$In 0.500 "
					+ "Anim_Graphics$Anim_PlayerInfo 1.200 Anim_Graphics$Anim_PlayerInfo$In 1.200 Anim_Graphics$Anim_BasePrice 1.200 Anim_Graphics$Anim_BasePrice$In 1.200 \0");
		}
		
	}
	
	public void populateTopSoldTeam(PrintWriter print_writer,String viz_scene,int team_id , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int row = 0;
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
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
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
	        		
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.200 \0");
	}
	
	public void populateTopSold(PrintWriter print_writer,String viz_scene, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ "BUYS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
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
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.700 \0");
	}
	
	public void populateRemainingPurse(PrintWriter print_writer,String viz_scene, Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int total = 0;
		int row = 0;
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Data$SubHeaderGrp$txt_PurseValueHead*GEOM*TEXT SET "+ "PURSE REMAINING" + "\0");
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_LastName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName3() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$img_Logo*TEXTURE*IMAGE SET "+ logo_path + match.getTeam().get(i).getTeamName4() + "\0");
			
			if(match.getPlayers() != null ) {
				for(int j=0; j <= match.getPlayers().size()-1; j++) {
					if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
						row = row + 1;
						total = total + match.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_SquadSize*GEOM*TEXT SET "+ row + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total))  + "\0");
			if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "LAKH" + "\0");
			}else if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
			}
			row = 0;
			total = 0;
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.300 \0");
	}
	public void populateRemainingPurseSingle(PrintWriter print_writer,String viz_scene,int team_id , Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		
		
		for(int j=0; j <= match.getPlayers().size()-1; j++) {
			if(match.getPlayers().get(j).getTeamId() == team_id) {
				row = row + match.getPlayers().get(j).getSoldForPoints();
			}
		}
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + logo_path + 
				match.getTeam().get(team_id-1).getTeamName4() + AuctionUtil.PNG_EXTENSION + ";");
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName " + 
				(Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - row) + ";");
		row = 0;
		
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		TimeUnit.SECONDS.sleep(1);
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
	}
	public void populateSquad(PrintWriter print_writer,String viz_scene,int team_id , Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		
		
		int row = 0;
		for(int i = 1; i <= 20; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
		}
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		
		if(match.getPlayers() != null) {
			for(int j=0; j <= match.getPlayers().size()-1; j++) {
				if(match.getPlayers().get(j).getTeamId() == team_id) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$Base$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$NameAll$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$Base$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$NameAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_FirstName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getFirstname() + "\0");
					if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()!=null) {
						if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().toUpperCase() + "\0");
							
						}else {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()
									+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().charAt(0) +"Z" + "\0");
						}

					}else {
						if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().toUpperCase() + "\0");
							
						}else {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().charAt(0) +"Z" + "\0");
						}
						//print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ " - "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().substring(0, 3).toUpperCase() + "\0");
					}
					
					
					if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								System.out.println(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase());
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
				}
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.900 \0");
	}
	
	public void BidChangeOn(PrintWriter print_writer, Auction session_curr_bid, int whichSide) {
		if(data.isBid_Start_or_not()) {
			print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side" + whichSide + "$txt_Price*GEOM*TEXT SET "+ 
					AuctionFunctions.ConvertToLakh(session_curr_bid.getCurrentPlayers().getSoldForPoints()) + "\0");
			print_writer.println("-1 RENDERER*TREE*$Gfx$Gfx_CurrentBid$Side" + whichSide + "$Img_Text1*TEXTURE*IMAGE SET " + text_path + "1/" + "ISPL" + "\0");
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
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	
}