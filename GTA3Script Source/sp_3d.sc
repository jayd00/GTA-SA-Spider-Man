// by J16D
// 3D for Blips used in this mod
// Spider-Man Mod for GTA SA c.2018 - 2021
// Documentation:
// https://gtaforums.com/topic/871359-blipmarker-struct/
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME sp_3d
LVAR_INT toggleSpiderMod isInMainMenu is_hud_enabled is_in_interior
LVAR_INT flag_player_on_mission iTempVar

// These must be the same as icon ID'S  ->listed at end
CONST_INT idMapIcon0b 41    //RADAR_SPRITE_WAYPOINT
CONST_INT idMapIcon5 19     //RADAR_SPRITE_ENEMYATTACK
CONST_INT idMapIcon7 58     //RADAR_SPRITE_GANG_B
CONST_INT idMapIcon8 26     //RADAR_SPRITE_MCSTRAP
CONST_INT idMapIcon9 61     //RADAR_SPRITE_GANG_N
CONST_INT idMapIcon10 55    //RADAR_SPRITE_IMPOUND

LOAD_TEXTURE_DICTIONARY spaim
LOAD_SPRITE idMapIcon0b "mk0_b"     //Way Point
LOAD_SPRITE idMapIcon5 "mk5"        //Car Chase / Crimes
LOAD_SPRITE idMapIcon7 "mk7"        //Thug Hideouts
LOAD_SPRITE idMapIcon8 "mk0"        //Main Mission WayPoint
LOAD_SPRITE idMapIcon9 "mk9"        //Street Crimes
LOAD_SPRITE idMapIcon10 "mk10"      //Surveillance Towers

//ADD_SPRITE_BLIP_FOR_COORD -1691.91 1104.3081 94.0312 RADAR_SPRITE_GANG_N (iEventBlip) //RADAR_SPRITE_WAYPOINT
WHILE TRUE
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                GOSUB hudCheck
                GOSUB activeInteriorCheck
                IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                OR IS_ON_CUTSCENE 
                OR is_hud_enabled = FALSE
                OR is_in_interior > 0 
                    USE_TEXT_COMMANDS FALSE // don't show textures
                ELSE
                    IF flag_player_on_mission = 0   //Only on open world
                        CLEO_CALL draw_3d_blip 0 ()
                    ENDIF
                ENDIF
            ENDIF
        ELSE
            USE_TEXT_COMMANDS FALSE // don't show textures
            USE_TEXT_COMMANDS FALSE // don't show textures
            WAIT 0
            REMOVE_TEXTURE_DICTIONARY
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
RETURN

hudCheck:
    READ_MEMORY 0xBA6769 4 FALSE (iTempVar)
    IF iTempVar = FALSE
        is_hud_enabled = FALSE
    ELSE
        is_hud_enabled = TRUE
    ENDIF
RETURN

activeInteriorCheck:
    GET_AREA_VISIBLE (is_in_interior)
RETURN

GUI_TextFormat_Text:
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL draw_3d_blip 0 ()
draw_3d_blip:
    LVAR_INT iPoolStart id_blip iBlipIcon counter iTempVar
    LVAR_FLOAT x y z x1 y1 z1 fDistance v1 v2 v3
    counter = 0
    WHILE 174 > counter     //Pool size: 175 items
        iPoolStart = counter * 0x28    //Item size: 0x28
        iPoolStart += 0xBA86F0
        iTempVar = iPoolStart + 36      //BYTE byteIcon; // 36
        READ_MEMORY iTempVar 1 FALSE (iBlipIcon)
        IF iBlipIcon = RADAR_SPRITE_WAYPOINT
        OR iBlipIcon = RADAR_SPRITE_GANG_B
        OR iBlipIcon = RADAR_SPRITE_ENEMYATTACK
        OR iBlipIcon = RADAR_SPRITE_MCSTRAP
        OR iBlipIcon = RADAR_SPRITE_GANG_N
        OR iBlipIcon = RADAR_SPRITE_IMPOUND
            iTempVar = iPoolStart + 8      //float fPosX; // 8
            READ_MEMORY iTempVar 4 FALSE (x)
            iTempVar = iPoolStart + 12      //float fPosY; // 12
            READ_MEMORY iTempVar 4 FALSE (y)
            iTempVar = iPoolStart + 16      //float fPosZ; // 16
            READ_MEMORY iTempVar 4 FALSE (z)
            //z = 1000.0
            //GET_GROUND_Z_FOR_3D_COORD (x y z) (z)
            //z += 0.6
            z += 0.6
            GET_OFFSET_FROM_CAMERA_IN_WORLD_COORDS 0.0 0.0 0.0 (x1 y1 z1)
            GET_DISTANCE_BETWEEN_COORDS_3D (x y z) (x1 y1 z1) (fDistance)
            IF fDistance > 10.0
            AND 300.0 > fDistance
                CONVERT_3D_TO_SCREEN_2D (x y z) TRUE TRUE (v1 v2) (v3 v3)
                GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
                USE_TEXT_COMMANDS FALSE
                SET_SPRITES_DRAW_BEFORE_FADE FALSE
                DRAW_SPRITE iBlipIcon (v1 v2) (v3 v3) (255 255 255 235)
                v1 -= 6.0
                v2 += 6.0
                iTempVar =# fDistance
                GOSUB GUI_TextFormat_Text
                USE_TEXT_COMMANDS FALSE
                DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
            ENDIF
            //PRINT_FORMATTED_NOW "xyz: %.2f %.2f %.2f icon:%d" 1 x y z iBlipIcon
        ENDIF
        counter++
    ENDWHILE
CLEO_RETURN 0
}
/*
Structure: RadarMarker
Item size: 0x28 (40 bytes)
Pool start: 0xBA86F0
Pool size: 175 items

struct RadarMarker {
DWORD dwColourID; // 0
DWORD* pEntity; // 4
float fPosX; // 8
float fPosY; // 12
float fPosZ; // 16
short wFlag; // 20
short _wAlign; // 22
float fUnknown; // 24 (either 1.0 or 5.0)
DWORD dwIconSize; // 28
DWORD *pEnterExit; // 32
BYTE byteIcon; // 36
BYTE byteFlags; // 37
BYTE byteType; // 38
BYTE _bAlign; // 39
};[/table]
*/


//-+---CONSTANTS--------------------
//GLOBAL_CLEO_SHARED_VARS
//100 slots - range 0 to 99
CONST_INT varStatusSpiderMod    0     //1= Mod activated || 0= Mod Deactivated
CONST_INT varHUD                1     //1= Activated     || 0= Deactivated
CONST_INT varMusic              2     //1= Music On	    || 0= Music Off

CONST_INT varHudRadar           3     //sp_hud - MSpiderJ16Dv7
CONST_INT varHudHealth          4     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudAmmo            5     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudMoney           6     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudTime            7     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudBreath          8     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudArmour          9     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudWantedS         10    //sp_hud    ||1= Activated     || 0= Deactivated

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:thug hidouts || 4:street crimes || 5:boss2
CONST_INT varCrimesProgress     12    //for stadistics ||MSpiderJ16Dv7
CONST_INT varPcampProgress      13    //for stadistics ||MSpiderJ16Dv7
CONST_INT varCarChaseProgress   14    //for stadistics ||MSpiderJ16Dv7
CONST_INT varScrewBallProgress  15    //for stadistics ||MSpiderJ16Dv7
CONST_INT varBackpacksProgress  16    //for stadistics ||MSpiderJ16Dv7
CONST_INT varLandmarksProgress  17    //for stadistics ||MSpiderJ16Dv7

CONST_INT varAlternativeSwing   20    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varSwingBuilding      21    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFixGround          22    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varMouseControl       23    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varAimSetup           24    // 0:Manual Aim || 1:Auto Aim //sp_dw
CONST_INT varPlayerCanDrive     25    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFriendlyN          26    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress

CONST_INT varInMenu             40    //1= On Menu       || 0= Menu Closed
CONST_INT varMapLegendLandMark  43    //Show: 1= enable   || 0= disable
CONST_INT varMapLegendBackPack  44    //Show: 1= enable   || 0= disable

CONST_INT varSkill1             50    //sp_dw    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2             51    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2a            52    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3             53    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3a            54    //sp_ml    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3b            55    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c            56    //sp_main  ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c1           57    //sp_mb    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c2           58    //sp_mb    ||1= Activated     || 0= Deactivated

CONST_INT varAudioActive     	45    // 0:OFF || 1:ON  ||global var to check -spech- audio playing

/*
//For test
IF CLEO_CALL blip_read 0 58 (x y z) (iBlipIcon)     //RADAR_SPRITE_GANG_B
    GOSUB draw_3d_blip
ENDIF
IF CLEO_CALL blip_read 0 41 (x y z) (iBlipIcon)     //RADAR_SPRITE_WAYPOINT
    GOSUB draw_3d_blip
ENDIF
IF CLEO_CALL blip_read 0 19 (x y z) (iBlipIcon)     //RADAR_SPRITE_ENEMYATTACK
    GOSUB draw_3d_blip
ENDIF

draw_3d_blip:
    z = 800.0
    GET_GROUND_Z_FOR_3D_COORD (x y z) (z)
    z += 1.0
    GET_OFFSET_FROM_CAMERA_IN_WORLD_COORDS 0.0 0.0 0.0 (x1 y1 z1)
    GET_DISTANCE_BETWEEN_COORDS_3D (x y z) (x1 y1 z1) (fDistance)
    IF fDistance > 10.0
    AND 300.0 > fDistance
        CONVERT_3D_TO_SCREEN_2D (x y z) TRUE TRUE (v1 v2) (v3 v3)
        GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        DRAW_SPRITE iBlipIcon (v1 v2) (v3 v3) (255 255 255 235)

        v2 += 10.0
        iTempVar =# fDistance
        GOSUB GUI_TextFormat_Text
        USE_TEXT_COMMANDS FALSE
        DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
    ENDIF
    PRINT_FORMATTED_NOW "xyz: %.2f %.2f %.2f index:%d icon:%d" 2 x y z blip_index iBlipIcon
RETURN

{
//CLEO_CALL blip_read 0 id_blip (x y z) (iBlipIcon)
blip_read:
    LVAR_INT id_blip //in
    LVAR_INT iPoolStart iTempVar iBlipIcon iFlag counter
    LVAR_FLOAT x y z
    counter = 0
    WHILE 174 > counter
        iPoolStart = counter * 0x28    //size
        iPoolStart += 0xBA86F0  
        iTempVar = iPoolStart + 36      //BYTE byteIcon; // 36
        READ_MEMORY iTempVar 1 FALSE (iBlipIcon)
        IF iBlipIcon = id_blip
            iTempVar = iPoolStart + 8      //float fPosX; // 8
            READ_MEMORY iTempVar 4 FALSE (x)
            iTempVar = iPoolStart + 12      //float fPosY; // 12
            READ_MEMORY iTempVar 4 FALSE (y)
            iTempVar = iPoolStart + 16      //float fPosZ; // 16
            READ_MEMORY iTempVar 4 FALSE (z)
            RETURN_TRUE
            CLEO_RETURN 0 x y z iBlipIcon
        ENDIF
        counter++
    ENDWHILE
    RETURN_FALSE
CLEO_RETURN 0 0.0 0.0 0.0 0
}
*/


/*
0: Marker  
1: White_square 
2: Centre
3: Map_here
4: North
5: Airyard
6: Gun
7: Barbers
8: Big_smoke
9: Boatyard
10: Burgershot
11: Bulldozer
12: Cat_pink
13: Cesar
14: Chicken
15: Cj
16: Crash1
17: Diner
18: Emmetgun 
19: Enemyattack 
20: Fire 
21: Girlfriend 
22: Hospital 
23: Loco 
24: Madd Dogg
25: Mafia
26: Mcstrap
27: Mod_garage
28: Ogloc
29: Pizza
30: Police 
31: Property_green
32: Property_red 
33: Race 
34: Ryder
35: Savehouse 
36: School 
37: Mystery 
38: Sweet 
39: Tattoo 
40: Truth 
41: Waypoint 
42: Toreno_ranch 
43: Triads 
44: Triads_casino
45: Tshirt 
46: Woozie 
47: Zero 
48: Date_disco
49: Date_drink 
50: Date_food
51: Truck 
52: Cash 
53: Flag 
54: Gym 
55: Impound
56: Runway_light
57: Runway
58: Gang_b
59: Gang_p
60: Gang_y
61: Gang_n
62: Gang_g 
63: Spray
*/