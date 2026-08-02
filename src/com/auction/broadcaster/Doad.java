package com.auction.broadcaster;

import java.io.PrintWriter;
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
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class Doad extends Scene{

	private String status;
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "HANDBALL";
	public Data data = new Data();
	public String which_graphics_onscreen = "BG";
	public int current_layer = 2;
	private String logo_path = "C:\\Images\\AUCTION\\Logos\\";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	public Doad() {
		super();
	}

	public Doad(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Data updateData(Scene scene, Auction auction,AuctionService auctionService, PrintWriter print_writer) throws InterruptedException
	{
		if(which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE")) {
			populatePlayerProfile(true,print_writer, "", data.getPlayer_id(),auctionService.getAllStats(),auction,auctionService, session_selected_broadcaster);
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws InterruptedException, NumberFormatException, IllegalAccessException {
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SINGLE_PURSE": case "POPULATE-TOP_SOLD":
			
			switch (session_selected_broadcaster.toUpperCase()) {
			case "HANDBALL":
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-L3-INFOBAR":
					scenes.get(0).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
					break;
				default:
					current_layer = 3 - current_layer;
					scenes.get(1).setWhich_layer(String.valueOf(current_layer));
					scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
					scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-SQUAD":
					populateSquad(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService,session_selected_broadcaster);
					break;
				case "POPULATE-REMAINING_PURSE_ALL":
					populateRemainingPurse(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-SINGLE_PURSE":
					populateRemainingPurseSingle(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), 
							auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-FF-PLAYERPROFILE":
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[1]));
					populatePlayerProfile(false,print_writer,valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),
							auctionService.getAllStats(),auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-TOP_SOLD":
					populateTopSold(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				
				
				}
				//return JSONObject.fromObject(this_doad).toString();
			}
			
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "HANDBALL":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE": case "ANIMATE-IN-TOP_SOLD":
					
					if(which_graphics_onscreen != "" && which_graphics_onscreen != "BG") {
						switch(which_graphics_onscreen) {
						case "PLAYERPROFILE": case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD":
							processAnimation(print_writer, "Out", "START", session_selected_broadcaster,(3-current_layer));
							TimeUnit.SECONDS.sleep(2);
							print_writer.println("LAYER" + (3-current_layer) + "*EVEREST*SINGLE_SCENE CLEAR;");
							break;
						}
					}else if(which_graphics_onscreen == "BG") {
						print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Out START;");
						TimeUnit.SECONDS.sleep(2);
					}
					break;
				}
				
				switch (whatToProcess.toUpperCase()) {
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "PLAYERPROFILE";
					break;
				case "ANIMATE-IN-SQUAD":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "SQUAD";
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "REMAINING_PURSE_ALL";
					break;
				case "ANIMATE-IN-SINGLE_PURSE":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "SINGLE_PURSE";
					break;
				case "ANIMATE-IN-TOP_SOLD":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "TOP_SOLD";
					break;
				
				case "CLEAR-ALL":
					print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
					print_writer.println("LAYER2*EVEREST*SINGLE_SCENE CLEAR;");
					which_graphics_onscreen = "";
					break;
				
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "INFOBAR":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,current_layer);
						which_graphics_onscreen = "";
						break;
					
					
					case "PLAYERPROFILE": case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,current_layer);
						TimeUnit.SECONDS.sleep(2);
						print_writer.println("LAYER" + current_layer + "*EVEREST*SINGLE_SCENE CLEAR;");
//						TimeUnit.SECONDS.sleep(1);
						print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*In START;");
						print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Loop START;");
						
						break;
					}
					break;
				}
				
			}
			
			
		}
		return null;
	}
	public void populatePlayerProfile(boolean is_this_updating,PrintWriter print_writer,String viz_scene, int playerId,List<Statistics> stats, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(auction.getPlayers() != null && auction.getPlayers().size() > 0) {
			if(data.isPlayer_sold_or_unsold() == false) {
				for(int i=auction.getPlayers().size()-1; i >= 0; i--) {
					if(playerId == auction.getPlayers().get(i).getPlayerId()) {
						if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSoldUnsold 1 ;");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSoldPoints " + auction.getPlayers().get(i).getSoldForPoints() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgSoldToTeam " + logo_path + 
									auctionService.getTeams().get(auction.getPlayers().get(i).getTeamId() - 1).getTeamName4() + AuctionUtil.PNG_EXTENSION + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSoldHead SOLD TO;");
							
							TimeUnit.MILLISECONDS.sleep(200);
							print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Result START;");
							data.setPlayer_sold_or_unsold(true);
						}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSoldUnsold 0 ;");
							print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Result START;");
							data.setPlayer_sold_or_unsold(true);
						}
					}
				}
			}
		}
		
		
		if(is_this_updating == false) {
			data.setPlayer_sold_or_unsold(false);
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vPlayerPic 0;");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName " + 
					auctionService.getAllPlayer().get(playerId - 1).getFirstname() + ";");
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + photo_path + 
					auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + ";");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName " + auctionService.getAllPlayer().get(playerId - 1).getSurname() + ";");
			}else {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerLastName " + "" + ";");
			}
			
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRole " + auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + ";"); 	
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAge " + auctionService.getAllPlayer().get(playerId - 1).getAge() + " yrs" + ";");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("STAR")) {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCategory 0 ;");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("FOREIGN")) {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCategory 1 ;");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("ROLE")) {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCategory 2 ;");
			}
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHeight " + auctionService.getAllPlayer().get(playerId - 1).getHeight() + " cm" + ";");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWeight " + auctionService.getAllPlayer().get(playerId - 1).getWeight() + " kgs" + ";");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNationality " + auctionService.getAllPlayer().get(playerId - 1).getNationality() + ";");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBaePoints " + auctionService.getAllPlayer().get(playerId - 1).getBasePrice() + ";");
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$First*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Second*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Third*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Fourth*CONTAINER SET ACTIVE 0;");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFourth03 " + "" + ";");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAchiveHead " + "ACHIEVEMENTS" + ";");
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 0;");
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId) {
					if(stat.getInfo1() == null) {
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 0 ;");
					}else if(stat.getInfo2() == null) {
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$First*CONTAINER SET ACTIVE 1;");
						if(stat.getInfo1().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + stat.getInfo1().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + "" + ";");
						}
						
					}else if(stat.getInfo3() == null) {
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$First*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Second*CONTAINER SET ACTIVE 1;");
						
						if(stat.getInfo1().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + stat.getInfo1().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + "" + ";");
						}
						
						if(stat.getInfo2().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + stat.getInfo2().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + "" + ";");
						}
						
					}else if(stat.getInfo4() == null) {
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$First*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Second*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Third*CONTAINER SET ACTIVE 1;");
						
						if(stat.getInfo1().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + stat.getInfo1().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + "" + ";");
						}
						
						if(stat.getInfo2().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + stat.getInfo2().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + "" + ";");
						}

						if(stat.getInfo3().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird01 " + stat.getInfo3().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird02 " + stat.getInfo3().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird01 " + stat.getInfo3() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird02 " + "" + ";");
						}
						
					}else {
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAchive 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$First*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Second*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Third*CONTAINER SET ACTIVE 1;");
						print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$Achivements$Fourth*CONTAINER SET ACTIVE 1;");
						
						if(stat.getInfo1().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + stat.getInfo1().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst01 " + stat.getInfo1() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirst02 " + "" + ";");
						}
						
						if(stat.getInfo2().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + stat.getInfo2().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond01 " + stat.getInfo2() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSecond02 " + "" + ";");
						}

						if(stat.getInfo3().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird01 " + stat.getInfo3().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird02 " + stat.getInfo3().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird01 " + stat.getInfo3() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThird02 " + "" + ";");
						}
						
						if(stat.getInfo4().contains(",")) {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFourth01 " + stat.getInfo4().split(",")[0] + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFourth02 " + stat.getInfo4().split(",")[1] + ";");
						}else {
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFourth01 " + stat.getInfo4() + ";");
							print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFourth02 " + "" + ";");
						}
					}
				}
			}
			
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
		
	}
	
	public void populateTopSold(PrintWriter print_writer,String viz_scene, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		for(Player plyr : auction.getPlayers()) {
			top_sold.add(plyr);
		}
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			row = row + 1;
        	if(row <= 10) {
        		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayername0" + row + " " + 
        				top_sold.get(m).getFull_name() + ";");
        		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam0" + row + " " + 
        				auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName1() + ";");
        		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPrice0" + row + " " + 
        				top_sold.get(m).getSoldForPoints() + ";");
        		
        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getCategory().equalsIgnoreCase("FOREIGN")) {
    				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInternational0" + row + " 1 ;");
    			}else {
    				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInternational0" + row + " 0 ;");
    			}
        		
        	}
		}
		
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
	
	public void populateRemainingPurse(PrintWriter print_writer,String viz_scene, Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			for(int j=0; j <= match.getPlayers().size()-1; j++) {
				if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
					row = row + match.getPlayers().get(j).getSoldForPoints();
				}
			}
			
			if(match.getTeam().get(i).getTeamId() == 1 || match.getTeam().get(i).getTeamId() == 2) {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName0" + (i+1) + " " + 
						auctionService.getTeams().get(match.getTeam().get(i).getTeamId()-1).getTeamName3() + ";");
			}else {
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName0" + (i+1) + " " + 
						auctionService.getTeams().get(match.getTeam().get(i).getTeamId()-1).getTeamName2() + ";");
			}
			
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgLogo0" + (i+1) + " " + logo_path + 
					auctionService.getTeams().get(match.getTeam().get(i).getTeamId()-1).getTeamName4() + AuctionUtil.PNG_EXTENSION + ";");
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPurse0" + (i+1) + " " + 
					(Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - row) + ";");
			row = 0;
		}
		
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
		int row = 0;
		for(int i = 1; i <= 18; i++) {
			print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$"+ i +"*CONTAINER SET ACTIVE 0;");
		}
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$8_to_14*CONTAINER SET ACTIVE 0;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAbove14 0;");
		
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + logo_path + 
				auctionService.getTeams().get(team_id - 1).getTeamName4() + AuctionUtil.PNG_EXTENSION + ";");
		
		for(int j=0; j <= match.getPlayers().size()-1; j++) {
			if(match.getPlayers().get(j).getTeamId() == team_id) {
				row = row + 1;
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$"+ row +"*CONTAINER SET ACTIVE 1;");
				if(row >=8) {
					print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main$All$8_to_14*CONTAINER SET ACTIVE 1;");
				}
				if(row >=14) {
					print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAbove14 1 ;");
				}
				
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayername0"+ row + " " + match.getPlayers().get(j).getFull_name() + ";");
				print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerRole0"+ row + " " + auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId() - 1).getRole() + ";");
				
				if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId() - 1).getCategory().equalsIgnoreCase("FOREIGN")) {
					print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInternational0" + row + " 1" + ";");
				}else {
					print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vInternational0" + row + " 0" + ";");
				}
			}
		}
		
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
		switch(whichGraphic.toUpperCase()) {
		
		case "FFPLAYERPROFILE":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
			break;
		
		}	
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		switch(which_broadcaster.toUpperCase()) {
		case "HANDBALL":
			switch(which_layer) {
			case 1:
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
				
			case 2:
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				//print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*LOOP START;");	
				break;
			}
			break;
		}
		
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
}