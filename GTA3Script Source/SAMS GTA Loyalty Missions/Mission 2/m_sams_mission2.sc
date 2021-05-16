// by J16D
// in Colaboration with GTA Loyalty
// SAMS: Remastered | Misión #2 - Cazando Felinos
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
NOP
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
lVAR_INT player_actor flag_player_on_mission toggleSpiderMod isInMainMenu
LVAR_INT iEventBlip

GET_PLAYER_CHAR 0 player_actor

start_sc:
WHILE TRUE
   IF IS_PLAYER_PLAYING 0
      GOSUB readVars
      IF toggleSpiderMod = 1
         BREAK
      ENDIF
   ENDIF
   WAIT 0
ENDWHILE
//delay 8 sec
timera = 0
WHILE 8000 > timera 
   WAIT 0
ENDWHILE
//Yellow waypoing - Main Mission
ADD_SPRITE_BLIP_FOR_COORD (-1983.2416 806.5577 92.3203) RADAR_SPRITE_MCSTRAP (iEventBlip) //RADAR_SPRITE_WAYPOINT
WAIT 500

main_loop:
   IF IS_PLAYER_PLAYING 0
      IF LOCATE_STOPPED_CHAR_ON_FOOT_3D player_actor (-1983.2416 806.5577 92.3203) (2.0 2.0 2.0) TRUE
         GOSUB readVars
         IF flag_player_on_mission = 0
         AND NOT IS_ON_MISSION
            REMOVE_BLIP iEventBlip
            CLEAR_HELP
            CLEAR_PRINTS
            USE_TEXT_COMMANDS FALSE
            GOSUB sub_Fade_out_500ms
            flag_player_on_mission = 1  //1:on_mission
            SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
            LOAD_AND_LAUNCH_CUSTOM_MISSION "SpiderJ16D\sams_m2"
            WHILE flag_player_on_mission > 0
               GOSUB readVars
               WAIT 0
            ENDWHILE
            CLEAR_PRINTS
            USE_TEXT_COMMANDS FALSE
            WAIT 3000
            GOSUB readVars
            IF toggleSpiderMod = 1
               ADD_SPRITE_BLIP_FOR_COORD (-1983.2416 806.5577 92.3203) RADAR_SPRITE_MCSTRAP (iEventBlip) //RADAR_SPRITE_WAYPOINT
               WAIT 500
            ELSE
               WAIT 500
               GOTO start_sc
            ENDIF
         ELSE    
            PRINT_FORMATTED_NOW "Finish your current mission first!" 2000
            WAIT 2000
         ENDIF
      ENDIF
      GOSUB readVars
      IF toggleSpiderMod = 0
         IF DOES_BLIP_EXIST iEventBlip
            REMOVE_BLIP iEventBlip
         ENDIF
         WAIT 500
         GOTO start_sc
      ENDIF
   ENDIF
   WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

sub_Fade_out_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN

sub_Fade_in_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN 
}
SCRIPT_END


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
