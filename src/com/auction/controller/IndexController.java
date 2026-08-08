package com.auction.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auction.broadcaster.Doad;
import com.auction.broadcaster.ISPL;
import com.auction.broadcaster.ISPL_VIZ;
import com.auction.broadcaster.KCL;
import com.auction.broadcaster.KCL_BIGSCREEN;
import com.auction.broadcaster.MUMBAI_T20_BIGSCREEN;
import com.auction.broadcaster.MUMBAI_T20_VIZ;
import com.auction.broadcaster.RALLY;
import com.auction.broadcaster.PSL;
import com.auction.broadcaster.PWL;
import com.auction.broadcaster.UTT_VIZ;
import com.auction.broadcaster.UTT_BIGSCREEN;
import com.auction.broadcaster.VIZ_ISPL_2024;
import com.auction.containers.Configurations;
import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Auction;
import com.auction.model.Flipper;
import com.auction.model.NameSuper;
import com.auction.model.Player;
import com.auction.model.Split;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class IndexController 
{
	@Autowired
	AuctionService auctionService;
	public static Configurations session_Configurations;
	public static Auction session_auction;
	public static Auction session_curr_bid;
	public static Socket session_socket;
	public static Doad this_doad;
	public static ISPL this_ispl;
	public static ISPL_VIZ this_ispl_viz;
	public static UTT_VIZ this_utt_viz;
	public static UTT_BIGSCREEN this_utt_bigscreen;
	public static VIZ_ISPL_2024 this_ispl_viz_2024;
	public static PSL this_psl;
	public static MUMBAI_T20_VIZ this_mumbai_t20_viz;
	public static MUMBAI_T20_BIGSCREEN this_MUMBAI_T20_BIGSCREEN;
	public static RALLY this_rally;
	public static KCL this_kcl;
	public static PWL this_pwl;
	public static KCL_BIGSCREEN this_KCL_BIGSCREEN;
	public static PrintWriter print_writer;
	public static String expiry_date = "2026-12-31";
	public static String error_message = "";
	public static String current_date = "";
	public static String Current_File_Name = "";
	public int current_layer = 1;
	public static long last_match_time_stamp = 0;
	public static long last_bid_time_stamp = 0;
	List<Player> allPlayer = new ArrayList<Player>();
	List<Team> allTeam = new ArrayList<Team>();
	
	List<NameSuper> session_nameSupers = new ArrayList<NameSuper>();
	List<Split> session_split = new ArrayList<Split>();
	List<Flipper> session_flipper = new ArrayList<Flipper>();
	List<Team> session_team = new ArrayList<Team>();
	List<Player> session_player = new ArrayList<Player>();
	public static Scene scene = new Scene();
	List<Auction> auction_file = new ArrayList<Auction>();
	List<Scene> session_selected_scenes = new ArrayList<Scene>();
	Data data = new Data();
	Auction auc = new Auction();
	int whichInning,player_id,team_id,session_port;
	String session_selected_broadcaster,selected_layer,selected_scene,session_selected_ip, 
	viz_scene_path, which_graphics_onscreen,selected_category;
	boolean is_Infobar_on_Screen = false;
	boolean is_director_on_bottom = false;
	boolean is_Ident_on_Screen = false;
	public static ObjectMapper objectMapper = new ObjectMapper(); 
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException, IOException, ParseException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = AuctionFunctions.getOnlineCurrentDate();
		}
		
			model.addAttribute("session_viz_scenes", new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".via") && pathname.isFile();
			    }
			}));
	
			model.addAttribute("match_files", new File(AuctionUtil.AUCTION_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}));
			
			model.addAttribute("configuration_files", new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			
			return "initialise";
	}

	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String outputPage(ModelMap model,
			@RequestParam(value = "configuration_file_name", required = false, defaultValue = "") String configuration_file_name,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "which_category", required = false, defaultValue = "") String which_category,
			@RequestParam(value = "which_layer", required = false, defaultValue = "") String which_layer,
			@RequestParam(value = "which_scene", required = false, defaultValue = "") String which_scene,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber) 
					throws Exception 
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
			
			session_port =  vizPortNumber;
			session_selected_ip = vizIPAddresss;
			
			data = new Data();
			this_doad = new Doad();
			this_ispl = new ISPL();
			this_ispl_viz = new ISPL_VIZ();
			this_ispl_viz_2024 = new VIZ_ISPL_2024();
			this_psl = new PSL();
			this_mumbai_t20_viz = new MUMBAI_T20_VIZ();
			this_rally = new RALLY();
			
			this_MUMBAI_T20_BIGSCREEN = new MUMBAI_T20_BIGSCREEN();
			this_kcl = new KCL();
			this_pwl =new PWL();
			this_KCL_BIGSCREEN = new KCL_BIGSCREEN();
			this_utt_viz = new UTT_VIZ();
			
			this_utt_bigscreen = new UTT_BIGSCREEN();
			session_selected_broadcaster = select_broadcaster;
			selected_layer = which_layer;
			selected_scene = which_scene;
			selected_category = which_category;
			session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
			print_writer = new PrintWriter(session_socket.getOutputStream(), true);
			
			session_Configurations = new Configurations(selectedMatch, select_broadcaster,which_category, vizIPAddresss, vizPortNumber);
			
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			switch (session_selected_broadcaster) {
				
			case "HANDBALL":
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/BG.sum","3")); // Front layer
				session_selected_scenes.add(new Scene("","1"));
				session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*In START;");
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Loop START;");
				this_doad.which_graphics_onscreen = "BG";
				break;
			case "ISPL":
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/BG.sum","3")); // Front layer
				session_selected_scenes.add(new Scene("","1"));
				session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*In START;");
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Loop START;");
				this_doad.which_graphics_onscreen = "BG";
				break;
//			case "ISPL_VIZ":                                          // <-- INSERT THIS NEW CASE HERE
//				session_selected_scenes.add(new Scene("<YOUR_VIZ_SCENE_PATH>","0"));
//				session_selected_scenes.add(new Scene("","1"));
//				session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
//				this_ispl_viz.which_graphics_onscreen = "BG";
//				break;
			case "UTT_BIGSCREEN":
				scene.LoadScene("BIGSCREEN", print_writer, session_Configurations);
				this_utt_bigscreen.which_graphics_onscreen = "";
				this_utt_bigscreen.resetData(print_writer);
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Logo$In_Out START \0");
				break; 
			case "ISPL_VIZ": case "VIZ_ISPL_2024": case "PSL":  case "UTT_VIZ": case "MUMBAI_T20_VIZ": case "MUMBAI_T20_BIGSCREEN": case "KCL": 
			case "KCL_BIGSCREEN": case "PWL": case "RALLY":
				scene.LoadScene("OVERLAYS", print_writer, session_Configurations);
				scene.LoadScene("FULL-FRAMERS", print_writer, session_Configurations);
				switch (session_selected_broadcaster) {
				case "ISPL_VIZ":
					session_selected_scenes.add(new Scene("","0"));
					break;
				case "MUMBAI_T20_VIZ":
					this_mumbai_t20_viz.resetData(print_writer);
					break;
				case "RALLY":
					this_rally.resetData(print_writer);
					break;	
				case "MUMBAI_T20_BIGSCREEN":
					this_MUMBAI_T20_BIGSCREEN.resetData(print_writer);
					break;
				case "KCL":
					this_kcl.resetData(print_writer);
					break;
				case "PWL":
					this_pwl.resetData(print_writer);
					break;
				case "KCL_BIGSCREEN":
					this_KCL_BIGSCREEN.resetData(print_writer);
					break;
				case "PSL":
					this_psl.resetData(print_writer);
					break;
				case "UTT_VIZ":
					this_utt_viz.resetData(print_writer);
					break;
				}
				break;
			}
			
			getDataFromDB();
			
			session_auction = new Auction();
			session_auction = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON), Auction.class);
			session_auction = AuctionFunctions.populateMatchVariables(session_auction, session_player, session_team);
			session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(),session_auction.getPlayers(), 
					session_auction.getPlayersList(),session_selected_broadcaster));
			
			session_curr_bid = new Auction();
			session_curr_bid = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON), Auction.class);
			
			Current_File_Name = selectedMatch;
			model.addAttribute("session_auction", session_auction);
			model.addAttribute("session_port", session_port);
			model.addAttribute("session_selected_ip", session_selected_ip);
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("selected_category", selected_category);
			model.addAttribute("selected_layer", selected_layer);
			model.addAttribute("selected_scene", selected_scene);
			model.addAttribute("licence_expiry_message","Software licence expires on " + 
					new SimpleDateFormat("E, dd MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			return "output";
		}
	}

	@RequestMapping(value = {"/processAuctionProcedures.html"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processAuctionProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws Exception 
	{
		
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
					new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
				return objectMapper.writeValueAsString(session_Configurations);
		case "TURN_ON_OR_OFF_AUDIO":
			System.out.println(whatToProcess + " - " + valueToProcess);
			if(valueToProcess.equalsIgnoreCase("TRUE")) {
				this_ispl_viz_2024.enableAudio = "TRUE";
				this_psl.enableAudio = "TRUE";
				this_kcl.enableAudio = "TRUE";
				this_pwl.enableAudio ="TRUE";
				this_mumbai_t20_viz.enableAudio ="TRUE";
				this_rally.enableAudio ="TRUE";
			}else {
				this_ispl_viz_2024.enableAudio = "FALSE";
				this_psl.enableAudio = "FALSE";
				this_kcl.enableAudio = "FALSE";
				this_pwl.enableAudio ="FALSE";
				this_mumbai_t20_viz.enableAudio ="FALSE";
				this_rally.enableAudio ="FALSE";
			}
			return null;	
		case "RE_READ_DATA":
			getDataFromDB();
			return objectMapper.writeValueAsString(session_auction);
		case "READ-MATCH-AND-POPULATE":
			if(last_match_time_stamp != new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON).lastModified()) {
				session_auction = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON), Auction.class);
				session_auction = AuctionFunctions.populateMatchVariables(session_auction, session_player, session_team);
				session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(),
						session_auction.getPlayers(), session_auction.getPlayersList(),session_selected_broadcaster));
				last_match_time_stamp = new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON).lastModified();
				
				if(session_selected_broadcaster != null) {
					switch (session_selected_broadcaster) {
					case "KCL_BIGSCREEN":
						if(this_KCL_BIGSCREEN.data.isBid_Start_or_not() == true) {
							this_KCL_BIGSCREEN.data.setWhichside(2);
						}
						this_KCL_BIGSCREEN.updateData(session_auction,session_curr_bid,auctionService,print_writer);
						break;
					}
				}
			}
			
			if(last_bid_time_stamp != new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON).lastModified()) {
				session_curr_bid = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON), Auction.class);
				last_bid_time_stamp = new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON).lastModified();
			}
			
			if(session_selected_broadcaster != null) {
				switch (session_selected_broadcaster) {
				case "HANDBALL": case "ISPL":
					this_doad.updateData(session_selected_scenes.get(0), session_auction,auctionService,print_writer);
					break;
				case "ISPL_VIZ":
					if(this_ispl_viz.data.isBid_Start_or_not() == true) {
						this_ispl_viz.data.setWhichside(2);
					}
					this_ispl_viz.updateData(session_selected_scenes.get(0), session_auction,
							session_curr_bid,auctionService,print_writer);
					break;
				case "VIZ_ISPL_2024":
					if(this_ispl_viz_2024.data.isBid_Start_or_not() == true) {
						this_ispl_viz_2024.data.setWhichside(2);
					}
					this_ispl_viz_2024.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
				case "PSL":	
					if(this_psl.data.isBid_Start_or_not() == true) {
						this_psl.data.setWhichside(2);
					}
					this_psl.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
				case "UTT_VIZ":
					if(this_utt_viz.data.isBid_Start_or_not() == true) {
						this_utt_viz.data.setWhichside(2);
					}
					this_utt_viz.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
					
				case "UTT_BIGSCREEN":
					if(this_utt_bigscreen.data.isBid_Start_or_not() == true) {
						this_utt_bigscreen.data.setWhichside(2);
					}
					this_utt_bigscreen.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;	
				case "KCL":
					if(this_kcl.data.isBid_Start_or_not() == true) {
						this_kcl.data.setWhichside(2);
					}
					this_kcl.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
				case "PWL":
					if(this_pwl.data.isBid_Start_or_not() == true) {
						this_pwl.data.setWhichside(2);
					}
					this_pwl.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
				case "MUMBAI_T20_VIZ":
					if(this_mumbai_t20_viz.data.isBid_Start_or_not() == true) {
						this_mumbai_t20_viz.data.setWhichside(2);
					}
					this_mumbai_t20_viz.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;
					
				case "RALLY":
					if(this_rally.data.isBid_Start_or_not() == true) {
						this_rally.data.setWhichside(2);
					}
					this_rally.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;	
						
				case "MUMBAI_T20_BIGSCREEN":
					if(this_MUMBAI_T20_BIGSCREEN.data.isBid_Start_or_not() == true) {
						this_MUMBAI_T20_BIGSCREEN.data.setWhichside(2);
					}
					this_MUMBAI_T20_BIGSCREEN.updateData(session_auction,session_curr_bid,auctionService,print_writer);
					break;	
				}
			}
			
			return objectMapper.writeValueAsString(session_auction);
		
		default:
			System.out.println("whatToProcess = " + whatToProcess + "   " + "valueToProcess = " + valueToProcess);
			if(whatToProcess.contains("_GRAPHICS-OPTIONS")) {
				return objectMapper.writeValueAsString(GetSpecificDataList(whatToProcess));
			}
			switch (session_selected_broadcaster.toUpperCase()) {
			case "HANDBALL": case "ISPL": 
				this_doad.ProcessGraphicOption(whatToProcess, session_auction, auctionService, print_writer, session_selected_scenes, valueToProcess);
			case "ISPL_VIZ":
				this_ispl_viz.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "UTT_VIZ":
				this_utt_viz.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "UTT_BIGSCREEN":
				this_utt_bigscreen.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "VIZ_ISPL_2024":
				this_ispl_viz_2024.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "PSL":
				this_psl.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;				
			case "MUMBAI_T20_VIZ":
				this_mumbai_t20_viz.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "RALLY":
				this_rally.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;	
			case "MUMBAI_T20_BIGSCREEN":
				this_MUMBAI_T20_BIGSCREEN.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;				
			case "KCL":
				this_kcl.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "PWL":
				this_pwl.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case "KCL_BIGSCREEN":
				this_KCL_BIGSCREEN.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			}
			return objectMapper.writeValueAsString(session_auction);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> GetSpecificDataList(String whatToProcess) throws IOException {
		switch (whatToProcess) {
		case "FLIPPER_GRAPHICS-OPTIONS": case "FLIPPER_TEXT_GRAPHICS-OPTIONS": case "CRAWLERFREETEXT_GRAPHICS-OPTIONS":
			return (List<T>) session_flipper;
//		case "NAMESUPER_GRAPHICS-OPTIONS":
//		    return (List<T>) session_nameSupers;
		case "NAMESUPER_GRAPHICS-OPTIONS": case "NAMESUPERSS_GRAPHICS-OPTIONS":
		    return (List<T>) session_nameSupers;
		case "SPLITPIP_GRAPHICS-OPTIONS":  case "SPLIT_GRAPHICS-OPTIONS": 
			return (List<T>) session_split;    
		case "PLAYERPROFILE_GRAPHICS-OPTIONS": case "FF_PLAYERPROFILE_GRAPHICS-OPTIONS": case "LT_PLAYERPROFILE_GRAPHICS-OPTIONS":
		case "PROFILE_GRAPHICS-OPTIONS": case "PLAYERPROFILE_MAIN_GRAPHICS-OPTIONS": case "FF_BIG_PLAYERPROFILE_GRAPHICS-OPTIONS":
		    return (List<T>) session_player;
		case "SQUAD_GRAPHICS-OPTIONS": case "SQUAD_CATEGORY_GRAPHICS-OPTIONS": case "SINGLE_PURSE_GRAPHICS-OPTIONS": case "TOP-SOLD_TEAM_GRAPHICS-OPTIONS": case "GOOGLY_GRAPHICS-OPTIONS":
		case "LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS": case "CRAWL_TOP_SOLD_TEAM_GRAPHICS-OPTIONS": case "SQUAD_PLAYER_GRAPHICS-OPTIONS": case "FF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS":
		case "LOF_SQUAD_SIZE_CATEGORY_WISE_GRAPHICS-OPTIONS": case "LOF_SQUAD_GRAPHICS-OPTIONS": case "TEAM_CURRENT_BID_GRAPHICS-OPTIONS":
		case "FF_TOP_FIVE_SOLD_TEAM_GRAPHICS-OPTIONS": case "SINGLE_GRAPHICS-OPTIONS": case "FLIPPER_SQUAD_GRAPHICS-OPTIONS":case "FF_SQUAD_GRAPHICS-OPTIONS":
		case "FF_SQUAD_ROLE_GRAPHICS-OPTIONS":case "LOF_TEAM_BID_GRAPHICS-OPTIONS": case "CRAWL_SQUAD_GRAPHICS-OPTIONS": case "SQUAD_ANIMATION_GRAPHICS-OPTIONS":
		case "RETAIN_PLAYERS_GRAPHICS-OPTIONS": case "TEAMS_GRAPHICS-OPTIONS":
			System.out.println("Comiong in controller sssss");
		    return (List<T>) session_team;
		case "ZONEWISE_PLAYER_SOLD_GRAPHICS-OPTIONS": case "ZONE-PLAYER_GRAPHICS-OPTIONS":
			Set<String> allCategories = session_auction.getPlayersList().stream()
	        	.map(Player::getCategory).filter(Objects::nonNull).map(String::trim)
	        	.map(String::toUpperCase).filter(category -> !category.equals("ICON"))  // remove ICON
	        	.collect(Collectors.toSet());
		    ArrayList<String> array = new ArrayList<>(allCategories);
		    return (List<T>) array;
		}
	    return null;
	}

	public void getDataFromDB()
	{
		session_flipper = auctionService.getFlipper();
		session_nameSupers = auctionService.getNameSupers();
		session_team = auctionService.getTeams();
		session_split = auctionService.getSplits();
		session_player = auctionService.getAllPlayer();
	}
}