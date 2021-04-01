
// by J16D
// Main Menu
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

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

//Main Title 100
CONST_INT iUpStart      0
CONST_INT iUpAfterStart 1
CONST_INT iUpEnd        8
CONST_INT iUpPrevEnd    7

CONST_INT menSelSuit 1
CONST_INT menPowerSuit 2
CONST_INT menConfigSuit 3

SCRIPT_START
{
NOP
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
CONST_INT player 0

LVAR_INT toggleSpiderMod
LVAR_INT iPanel iPanelB
LVAR_INT iActive
LVAR_INT iActiveRow iActiveCol
LVAR_INT iRow iColumn
LVAR_INT iSelectedSuit iSelectedPower

LVAR_FLOAT xCoord yCoord xSize ySize x y z fCurrentLevel fZAnglePlayerAir
LVAR_INT iSetCamera counter pUnlockCode iTempVar
LVAR_INT iBaseObject iBaseActor
LVAR_TEXT_LABEL _lName

LVAR_INT iMaxRowQuantity
LVAR_INT idTexture
LVAR_INT isCharRendered iFireHead

ADD_TEXT_LABEL SP_LDMK "landmarks"
ADD_TEXT_LABEL SP_BPCK "backpacks"
ADD_TEXT_LABEL SP_CRMS "crimes"
//---+-------------------INIT VARS LOADING GAME------------------------
iTempVar = 0
SET_CLEO_SHARED_VAR varStatusSpiderMod iTempVar     // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varMusic iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varInMenu iTempVar              // 0:OFF || 1:ON
CLEO_CALL storeStatusSpiderMod 0 iTempVar

iTempVar = 1
/*
READ_INT_FROM_INI_FILE "CLEO\MaxirpSpiderHud.ini" "Activation" "ENABLE_RADAR" (iTempVar)
SET_CLEO_SHARED_VAR varHudRadar iTempVar               // 0:OFF || 1:ON
READ_INT_FROM_INI_FILE "CLEO\MaxirpSpiderHud.ini" "Activation" "ENABLE_HP_BARS" (iTempVar)
SET_CLEO_SHARED_VAR varHudHealth iTempVar               // 0:OFF || 1:ON
READ_INT_FROM_INI_FILE "CLEO\MaxirpSpiderHud.ini" "Activation" "ENABLE_MONEY" (iTempVar)
SET_CLEO_SHARED_VAR varHudMoney iTempVar               // 0:OFF || 1:ON
*/
SET_CLEO_SHARED_VAR varHudRadar iTempVar  
SET_CLEO_SHARED_VAR varHudHealth iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHudMoney iTempVar               // 0:OFF || 1:ON
//iTempVar = 1
SET_CLEO_SHARED_VAR varHudAmmo iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHudTime iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHudBreath iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHudArmour iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHudWantedS iTempVar               // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varHUD iTempVar       // 0:OFF || 1:ON

//Statistics
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_crimin" (iTempVar)
SET_CLEO_SHARED_VAR varCrimesProgress iTempVar
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_pcamp" (iTempVar)
SET_CLEO_SHARED_VAR varPcampProgress iTempVar
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_cchase" (iTempVar)
SET_CLEO_SHARED_VAR varCarChaseProgress iTempVar
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_screwb" (iTempVar)
SET_CLEO_SHARED_VAR varScrewBallProgress iTempVar
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_bpacks" (iTempVar)
SET_CLEO_SHARED_VAR varBackpacksProgress iTempVar
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_lmarks" (iTempVar)
SET_CLEO_SHARED_VAR varLandmarksProgress iTempVar

/*
CLEO_CALL get_current_backpack_progress 0 (iTempVar)
SET_CLEO_SHARED_VAR varBackpacksProgress iTempVar
*/
CLEO_CALL save_map_coords 0 1   //||landmarks:1 ||backpacks:2 ||crimes:3
CLEO_CALL save_map_coords 0 2   //||landmarks:1 ||backpacks:2 ||crimes:3

//Options 
iTempVar = 0
SET_CLEO_SHARED_VAR varAlternativeSwing iTempVar    // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varAimSetup iTempVar        // 0:Manual Aim || 1:Auto Aim
SET_CLEO_SHARED_VAR varPlayerCanDrive iTempVar      // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varThrowVehDoors iTempVar      // 0:OFF || 1:ON
CLEO_CALL storeAlternativeItem 0 iTempVar
CLEO_CALL storeAutoAimItem 0 iTempVar
CLEO_CALL storeSpiderDriveCarsItem 0 iTempVar
CLEO_CALL storeThrowVehDoorsItem 0 iTempVar

iTempVar = 1
SET_CLEO_SHARED_VAR varSwingBuilding iTempVar       // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varFixGround iTempVar           // 0:OFF || 1:ON
SET_CLEO_SHARED_VAR varFriendlyN iTempVar           // 0:OFF || 1:ON
CLEO_CALL storeSwingBuildingItem 0 iTempVar
CLEO_CALL storeFixGroundItem 0 iTempVar
CLEO_CALL storeFriendlyNeighborhoodItem 0 iTempVar
IF IS_PC_USING_JOYPAD
    iTempVar = 0
ELSE
    iTempVar = 1
ENDIF
SET_CLEO_SHARED_VAR varMouseControl iTempVar        // 0:OFF || 1:ON
CLEO_CALL storeMouseControlItem 0 iTempVar
iTempVar = 1    //starter item
CLEO_CALL storeSkillItem 0 iTempVar
CLEO_CALL get_skill_data_and_save_on_memory 0 ()

//Other Settings
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (iTempVar)
SET_CLEO_SHARED_VAR varLevelChar iTempVar
iTempVar = 0
//SET_CLEO_SHARED_VAR varStatusLevelChar iTempVar
SET_CLEO_SHARED_VAR varIdPowers iTempVar    // NONE
SET_CLEO_SHARED_VAR varPowersProgress 100   //Default: 100 Full
CLEO_CALL StorePowerSuitItem 0 iTempVar //No power Selected

iSelectedSuit = 2   //by Default Skin 2 - Classic Suit (Damaged)
CLEO_CALL StoreSuitItem 0 iSelectedSuit

//Map Legend
iTempVar = 0
SET_CLEO_SHARED_VAR varMapLegendLandMark iTempVar    //Show: 1= enable   || 0= disable
iTempVar = 1
SET_CLEO_SHARED_VAR varMapLegendBackPack iTempVar    //Show: 1= enable   || 0= disable

//xCoord = 320.0
//yCoord = 224.0
//CLEO_CALL storeScreenCoords 0 xCoord yCoord
//CLEO_CALL getScreenCoords 0 (xCoord yCoord)
///---------------------------------------------------------------------
iTempVar = 0    // 1:is key combination pressed 

//---+------------------------- START -------------------------------
start:
    IF IS_PLAYER_PLAYING player
        IF NOT IS_CHAR_IN_ANY_CAR player_actor

            IF IS_PC_USING_JOYPAD 
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOCK   // ~k~~PED_DUCK~
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOCK  // ~k~~PED_LOOKBEHIND~
                    WHILE IS_BUTTON_PRESSED PAD1 LEFTSHOCK   // ~k~~PED_DUCK~
                        WAIT 0
                    ENDWHILE
                    WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOCK  // ~k~~PED_LOOKBEHIND~
                        WAIT 0
                    ENDWHILE
                    iTempVar = 1     // 1:is key combination pressed 
                ELSE
                    iTempVar = 0     // 1:is key combination pressed 
                ENDIF
            ELSE
                IF IS_KEY_PRESSED 17   //Ctrl (both) 17 
                AND IS_KEY_PRESSED VK_KEY_O
                    WHILE IS_KEY_PRESSED 17   //Ctrl (both) 17 
                        WAIT 0
                    ENDWHILE
                    WHILE IS_KEY_PRESSED VK_KEY_O
                        WAIT 0
                    ENDWHILE
                    //FIX  disabled menu for specific situations
                    GET_CLEO_SHARED_VAR varOnmission (counter) // flag_player_on_mission ||0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                    SWITCH counter
                        CASE 2   //car chase
                            IF GOSUB is_not_char_playing_car_missions_anims
                                IF  GOSUB is_not_char_playing_wall_anims
                                    iTempVar = 1     // 1:is key combination pressed
                                ELSE
                                    iTempVar = 0     // 1:is key combination pressed 
                                    PRINT_FORMATTED_NOW "~r~Can't open menu at this moment." 1500
                                    WAIT 1500
                                ENDIF
                            ELSE 
                                iTempVar = 0     // 1:is key combination pressed 
                                PRINT_FORMATTED_NOW "~r~Can't open menu at this moment." 1500
                                WAIT 1500
                            ENDIF
                            BREAK
                        DEFAULT  //all others
                            IF GOSUB is_not_char_playing_wall_anims
                                iTempVar = 1     // 1:is key combination pressed 
                            ELSE
                                iTempVar = 0     // 1:is key combination pressed 
                                PRINT_FORMATTED_NOW "~r~Can't open menu at this moment." 1500
                                WAIT 1500
                            ENDIF
                            BREAK
                    ENDSWITCH
                ELSE
                    iTempVar = 0     // 1:is key combination pressed 
                ENDIF
            ENDIF

            GET_CLEO_SHARED_VAR varOnmission (counter)  //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
            IF iTempVar = 1     // 1:is key combination pressed

                SET_CLEO_SHARED_VAR varInMenu 1     // // 0:OFF || 1:ON
                SET_FADING_COLOUR 0 0 0
                DO_FADE 500 FADE_OUT
                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                    IF IS_CHAR_IN_ANY_CAR player_actor
                        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                    ENDIF
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x y z)
                    GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
                    CLEO_CALL storeCharWorldXYZCoords 0 x y z fZAnglePlayerAir

                WHILE GET_FADING_STATUS 
                    WAIT 0
                ENDWHILE
                    //GET_CURRENT_WEATHER (iTempVar)
                    //CLEO_CALL save_weather_info 0 iTempVar
                    //FORCE_WEATHER_NOW SUNNY_SF
                    //SET_EXTRA_COLOURS 33 TRUE
                    
                    //WRITE_MEMORY 0xB7C484 2 1 FALSE   //_forceInteriorWeather
                    READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (iTempVar)
                    SET_CLEO_SHARED_VAR varLevelChar iTempVar
                    READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setB" (fCurrentLevel)
                    CLEO_CALL store_backpack_current_progress 0 ()
                    CLEO_CALL store_landmark_current_progress 0 ()

                SET_AREA_VISIBLE 14
                SET_CHAR_AREA_VISIBLE player_actor 14
                LOAD_SCENE 0.0 0.0 1100.0
                GOSUB set_init_parameters
                GOSUB load_all_needed_files
                GOSUB lock_player_controls

                CLEO_CALL store_SFX_Menu 0
                IF NOT iFireHead = 0
                    KILL_FX_SYSTEM iFireHead
                    iFireHead = 0
                ENDIF
                WAIT 0
                DO_FADE 500 FADE_IN
                WAIT 500
                CLEO_CALL GUI_Pulse_Reset 0
                //CLEO_CALL freezeGame 0 (1)
                GOTO show_menu

            ENDIF

        ENDIF
    ELSE
        IF NOT iFireHead = 0
            KILL_FX_SYSTEM iFireHead
            iFireHead = 0
        ENDIF
        CLEO_CALL getStatusSpiderMod 0 (toggleSpiderMod)
        IF toggleSpiderMod = 1
            WAIT 5
            WHILE NOT IS_PLAYER_PLAYING player
                WAIT 0
            ENDWHILE
            WHILE GET_FADING_STATUS
                WAIT 0
            ENDWHILE
            GOSUB setSkin
            GOSUB setWalkstyle

                IF iSelectedSuit = 20   //Spirit Spider
                    IF IS_FX_SYSTEM_AVAILABLE_WITH_NAME SP_BFLAME
                        IF NOT iFireHead = 0
                            KILL_FX_SYSTEM iFireHead
                            iFireHead = 0
                        ENDIF
                        CREATE_FX_SYSTEM_ON_CHAR SP_BFLAME player_actor (0.06 0.0 0.01) 1 (iFireHead)
                        ATTACH_FX_SYSTEM_TO_CHAR_BONE iFireHead player_actor 5  //5:head
                        PLAY_FX_SYSTEM iFireHead
                    ENDIF
                ENDIF

            WAIT 0
        ENDIF
        WHILE GET_FADING_STATUS
            WAIT 0
        ENDWHILE
    ENDIF
    WAIT 0
GOTO start

is_not_char_playing_car_missions_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_01")
            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_front")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_fall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_center")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_left")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_right")
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_not_char_playing_wall_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_A")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_B")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_C")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_D")
        RETURN_TRUE
        RETURN
    ENDIF
    RETURN_FALSE
RETURN

///---------------------------------------------------------------------
CONST_INT iPanel_null_settings 0
CONST_INT iPanel_hud_settings 1

//---+------------------------- CONTROL SCRIPT--------------------------
show_menu:
    GOSUB drawItems
    SWITCH iPanel
        CASE idOptions_l

            SWITCH iPanelB
                DEFAULT //NULL  iPanel_null_settings
                    CLEO_CALL getDataJoystickUpDown 0 (112 110 iActiveRow) (iActiveRow)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        SWITCH iActiveRow
                            CASE 110    // activate Spiderman MOD
                                CLEO_CALL getStatusSpiderMod 0 (iTempVar)
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                CLEO_CALL storeStatusSpiderMod 0 iTempVar
                                toggleSpiderMod = iTempVar     // 0:OFF || 1:ON 
                                //SET_CLEO_SHARED_VAR varStatusSpiderMod toggleSpiderMod       // 0:OFF || 1:ON
                                BREAK
                            CASE 111    // activate HUD
                                iPanelB = iPanel_hud_settings
                                BREAK
                            CASE 112    // activate MUSIC
                                GET_CLEO_SHARED_VAR varMusic iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varMusic iTempVar       // 0:OFF || 1:ON
                                BREAK
                        ENDSWITCH
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    //Force Quit
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        GOTO closeMenu
                    ENDIF
                    BREAK

                CASE iPanel_hud_settings
                    CLEO_CALL getDataJoystickUpDown 0 (138 131 iActiveRow) (iActiveRow)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        SWITCH iActiveRow
                            CASE 131
                               GET_CLEO_SHARED_VAR varHudRadar iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudRadar iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 132
                               GET_CLEO_SHARED_VAR varHudHealth iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudHealth iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 133
                               GET_CLEO_SHARED_VAR varHudAmmo iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudAmmo iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 134
                                GET_CLEO_SHARED_VAR varHudMoney iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudMoney iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 135
                                GET_CLEO_SHARED_VAR varHudTime iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudTime iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 136
                                GET_CLEO_SHARED_VAR varHudBreath iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudBreath iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 137
                                GET_CLEO_SHARED_VAR varHudArmour iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudArmour iTempVar       // 0:OFF || 1:ON
                                BREAK
                            CASE 138
                                GET_CLEO_SHARED_VAR varHudWantedS iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varHudWantedS iTempVar       // 0:OFF || 1:ON
                                BREAK
                        ENDSWITCH
                        IF GOSUB is_any_hud_element_enabled
                            iTempVar = 1
                            SET_CLEO_SHARED_VAR varHUD iTempVar       // 0:OFF || 1:ON
                        ELSE
                            iTempVar = 0
                            SET_CLEO_SHARED_VAR varHUD iTempVar       // 0:OFF || 1:ON
                        ENDIF
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    BREAK
            ENDSWITCH

            IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                WHILE   IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                    GOSUB drawItems
                ENDWHILE
                IF GOSUB is_any_hud_element_enabled
                    iTempVar = 1
                    SET_CLEO_SHARED_VAR varHUD iTempVar       // 0:OFF || 1:ON
                ELSE
                    iTempVar = 0
                    SET_CLEO_SHARED_VAR varHUD iTempVar       // 0:OFF || 1:ON
                ENDIF
                GOSUB reset_panel_parameters
                CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
            ENDIF
            BREAK

        CASE idMap_l
            //id - 170 - 174
            CONST_INT iPanel_config_map_legend 1
            SWITCH iPanelB
                DEFAULT //NULL  iPanel_null_settings
                    IF IS_BUTTON_PRESSED PAD1 DPADRIGHT            // ~k~~CONVERSATION_YES~
                        iPanelB = iPanel_config_map_legend
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 DPADRIGHT            // ~k~~CONVERSATION_YES~
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    //Force Quit
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        GOTO closeMenu
                    ENDIF
                    BREAK

                CASE iPanel_config_map_legend
                    CLEO_CALL getDataJoystickUpDown 0 (174 170 iActiveRow) (iActiveRow)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        SWITCH iActiveRow
                            CASE 171
                                GET_CLEO_SHARED_VAR varMapLegendLandMark iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varMapLegendLandMark iTempVar       //Show: 1= enable   || 0= disable                         
                                BREAK
                            CASE 172
                               GET_CLEO_SHARED_VAR varMapLegendBackPack iTempVar
                                IF iTempVar = 1
                                    iTempVar = 0
                                ELSE
                                    iTempVar = 1
                                ENDIF
                                SET_CLEO_SHARED_VAR varMapLegendBackPack iTempVar       //Show: 1= enable   || 0= disable    
                                BREAK
                            DEFAULT
                                //CASE 170  //side missions
                                //CASE 173  //crimes
                                //CASE 174  //black cat stakeouts
                                BREAK
                        ENDSWITCH
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    //back to main map panel
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        iPanelB = 0
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    BREAK
            ENDSWITCH
            BREAK

        CASE idSuits_l
            SWITCH iPanelB
                DEFAULT //NULL
                    CLEO_CALL getDataJoystickUpDown 0 (3 1 iActive) (iActive)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        SWITCH iActive
                            CASE menSelSuit
                                iPanelB = menSelSuit
                                BREAK
                            CASE menPowerSuit
                                iPanelB = menPowerSuit
                                BREAK
                            CASE menConfigSuit
                                iPanelB = menConfigSuit
                                BREAK
                        ENDSWITCH
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                        iSetCamera = TRUE
                    ENDIF
                    //Force Quit
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        GOTO closeMenu
                    ENDIF
                    BREAK

                CASE menSelSuit
                    CLEO_CALL GetDataJoystickMatrix5x5 0 (5 1 iActiveCol)(6 1 iActiveRow) (iActiveCol iActiveRow)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        CLEO_CALL play_SFX_Menu 0 12  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success
                        GOSUB setSkin
                        GOSUB setWalkstyle
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                        iSetCamera = TRUE
                    ENDIF
                    BREAK

                CASE menPowerSuit
                    CLEO_CALL GetDataJoystickMatrix5x5 0 (5 1 iActiveCol)(3 1 iActiveRow) (iActiveCol iActiveRow)
                    IF iActiveRow = 3
                        CLAMP_INT iActiveCol 1 2 (iActiveCol)   //Limit 2 power on 3rd row
                    ENDIF
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        IF NOT iSelectedPower = 10 //Iron Arms Power (Temporary locked, isn't ready)
                            CLEO_CALL play_SFX_Menu 0 12  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success
                            CLEO_CALL StorePowerSuitItem 0 iSelectedPower    //define power according to selection
                            SET_CLEO_SHARED_VAR varIdPowers iSelectedPower
                        ENDIF
                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                        iSetCamera = TRUE
                    ENDIF
                    BREAK

                CASE menConfigSuit
                    CLEO_CALL getDataJoystickUpDown 0 (107 100 iActiveRow) (iActiveRow)
                    IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        SWITCH iActiveRow
                            CASE idAlternativeSwing_l   // 100
                                GOSUB set_config_alternative_swing
                                BREAK
                            CASE idSwingBuildings_l     // 101
                                GOSUB set_config_simulate_building_web_attach
                                BREAK
                            CASE idFixGround_l      //102
                                GOSUB set_config_fix_ground_collision
                                BREAK
                            CASE idControlMouse_l       //103
                                GOSUB set_config_mouse_control
                                BREAK
                            CASE idAutoAim_l    //104
                                GOSUB set_config_auto_aim
                                BREAK
                            CASE idSpiderCanDrive_l     //105
                                GOSUB set_config_spider_can_drive
                                BREAK
                            CASE idFriendlyN_l      //106
                                GOSUB set_config_friendly_neighborhood
                                BREAK
                            CASE idThrowDoors_l
                                GOSUB set_config_throw_veh_doors
                                BREAK
                        ENDSWITCH
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   

                        WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            GOSUB drawItems
                        ENDWHILE
                        iSetCamera = TRUE
                    ENDIF
                    BREAK

            ENDSWITCH

            IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                WHILE   IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                    GOSUB drawItems
                ENDWHILE
                GOSUB reset_panel_parameters
                CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
            ENDIF
            BREAK

        CASE idSkills_l

            CLEO_CALL GetDataJoystickMatrix5x5 0 (5 1 iActiveCol)(3 1 iActiveRow) (iActiveCol iActiveRow)
            IF iActiveCol = 1
            OR iActiveCol = 3
                CLAMP_INT iActiveRow 1 1 (iActiveRow)   //Limit 1
            ENDIF
            IF iActiveCol = 2
            OR iActiveCol = 4
                CLAMP_INT iActiveRow 1 2 (iActiveRow)   //Limit 2
            ENDIF
            IF iActiveCol = 5
                CLAMP_INT iActiveRow 1 3 (iActiveRow)   //Limit 3
            ENDIF
            IF IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                CLEO_CALL getSkillItem 0 (counter)
                SWITCH counter    //recicled var
                    CASE 1
                        GOSUB set_skill1
                        BREAK
                    CASE 2
                        GOSUB set_skill2
                        BREAK
                    CASE 3
                        GOSUB set_skill3
                        BREAK
                    CASE 4
                        GOSUB set_skill2a
                        BREAK
                    CASE 5
                        GOSUB set_skill3a
                        BREAK
                    CASE 6
                        GOSUB set_skill3b
                        BREAK
                    CASE 7
                        GOSUB set_skill3c
                        BREAK
                    CASE 8
                        GOSUB set_skill3c1
                        BREAK
                    CASE 9
                        GOSUB set_skill3c2
                        BREAK
                ENDSWITCH
                CLEO_CALL store_skill_item_on_memory 0 counter iTempVar
                CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                WHILE IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    GOSUB drawItems
                ENDWHILE
            ENDIF
            BREAK

        CASE idCharacters_l
            CLEO_CALL GetDataJoystickMatrix5x5 0 (5 1 iActiveCol)(2 1 iActiveRow) (iActiveCol iActiveRow)
            BREAK

        CASE idMoves_l
            //CLEO_CALL getDataJoystickUpDown 0 (225 210 iActiveRow) (iActiveRow)
            //id - 225 - 210
            CONST_INT iPanel_config_moves_primary_controls 1
            SWITCH iPanelB
                DEFAULT //NULL  iPanel_null_settings
                    CLEO_CALL getDataJoystickUpDown 0 (225 210 iActiveRow) (iActiveRow)

                    IF IS_BUTTON_PRESSED PAD1 DPADRIGHT            // ~k~~CONVERSATION_YES~
                        iPanelB = iPanel_config_moves_primary_controls
                        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 DPADRIGHT            // ~k~~CONVERSATION_YES~
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    //Force Quit
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        GOTO closeMenu
                    ENDIF
                    BREAK

                CASE iPanel_config_moves_primary_controls
                    //back to main map panel
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                        iPanelB = 0
                        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
                        WHILE IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
                            GOSUB drawItems
                        ENDWHILE
                    ENDIF
                    BREAK
            ENDSWITCH

            BREAK 

        CASE idVersionInfo_l
            BREAK

    ENDSWITCH

    //Move
    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2 // PED_CYCLE_WEAPON_LEFT
        iPanel --
        CLEO_CALL GUI_Pulse_Reset 0
        GOSUB reset_panel_parameters
        CLEO_CALL play_SFX_Menu 0 4  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success     
    ENDIF
    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2    //PED_CYCLE_WEAPON_RIGHT
        iPanel ++
        CLEO_CALL GUI_Pulse_Reset 0
        GOSUB reset_panel_parameters
        CLEO_CALL play_SFX_Menu 0 4  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
    ENDIF
    CLAMP_INT iPanel 1 7 (iPanel)
    // Finalize update
    CLEO_CALL GUI_SetAtiveGXT 0 (iPanel)()
    CLEO_CALL GUI_Pulse_Update 0 (0)()
    USE_TEXT_COMMANDS FALSE

    //Delay key_pressed
    timera = 0
    WHILE TRUE
        IF IS_BUTTON_PRESSED 0 LEFTSHOULDER2    //PED_CYCLE_WEAPON_LEFT
        OR IS_BUTTON_PRESSED 0 RIGHTSHOULDER2   //PED_CYCLE_WEAPON_RIGHT
        OR IS_BUTTON_PRESSED 0 LEFTSTICKX   //LEFT/RIGHT      
        OR IS_BUTTON_PRESSED 0 LEFTSTICKY   //FORWARD/BACKWARD
            IF 300 > timera  //70
                GOSUB drawItems
            ELSE
                BREAK
            ENDIF
        ELSE
            BREAK
        ENDIF
    ENDWHILE

    //Rotate Actor Menu
    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1 // PED_ANSWER_PHONE, PED_FIREWEAPON_ALT
        IF DOES_CHAR_EXIST iBaseActor
            GET_CHAR_HEADING iBaseActor (fZAnglePlayerAir)
            fZAnglePlayerAir +=@ 5.0
            SET_CHAR_HEADING iBaseActor fZAnglePlayerAir
        ENDIF
    ENDIF
    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
        IF DOES_CHAR_EXIST iBaseActor
            GET_CHAR_HEADING iBaseActor (fZAnglePlayerAir)
            fZAnglePlayerAir -=@ 5.0
            SET_CHAR_HEADING iBaseActor fZAnglePlayerAir
        ENDIF
    ENDIF
    //Quit menu
    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
        //PRINT_HELP_STRING "Force Quit..."
        CLEO_CALL play_SFX_Menu 0 0  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
        GOTO closeMenu
    ENDIF

    //WAIT 0
GOTO show_menu

closeMenu:
    //CLEO_CALL freezeGame 0 (0)
    
    SET_FADING_COLOUR 0 0 0
    DO_FADE 600 FADE_OUT

        //WRITE_MEMORY 0x00B7C484 WORD 0 FALSE   //_forceInteriorWeather
        //CLEAR_EXTRA_COLOURS TRUE
        //CLEO_CALL get_weather_info 0 (iTempVar)
        //FORCE_WEATHER_NOW iTempVar
        //RELEASE_WEATHER
    WHILE GET_FADING_STATUS
        GOSUB drawItems
    ENDWHILE

    SET_AREA_VISIBLE 0
    SET_CHAR_AREA_VISIBLE player_actor 0
    CLEO_CALL store_skills_on_ini 0 ()

    CLEO_CALL getStatusSpiderMod 0 (toggleSpiderMod)
    IF toggleSpiderMod = 1
        toggleSpiderMod = 0     // 0:OFF || 1:ON 
        SET_CLEO_SHARED_VAR varStatusSpiderMod toggleSpiderMod // Turn Off running sp_main thread
        CLEO_CALL storeStatusSpiderMod 0 toggleSpiderMod
        IF NOT iFireHead = 0
            KILL_FX_SYSTEM iFireHead
            iFireHead = 0
        ENDIF
        WAIT 150
        toggleSpiderMod = 1     // 0:OFF || 1:ON 
        SET_CLEO_SHARED_VAR varStatusSpiderMod toggleSpiderMod // Turn On running sp_main thread
        CLEO_CALL storeStatusSpiderMod 0 toggleSpiderMod
        WAIT 150
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_main.cs"

        GOSUB setSkin
        GOSUB setWalkstyle

            IF iSelectedSuit = 20   //Spirit Spider
                IF IS_FX_SYSTEM_AVAILABLE_WITH_NAME SP_BFLAME
                    IF NOT iFireHead = 0
                        KILL_FX_SYSTEM iFireHead
                        iFireHead = 0
                    ENDIF
                    CREATE_FX_SYSTEM_ON_CHAR SP_BFLAME player_actor (0.06 0.0 0.01) 1 (iFireHead)
                    ATTACH_FX_SYSTEM_TO_CHAR_BONE iFireHead player_actor 5  //5:head
                    PLAY_FX_SYSTEM iFireHead
                ENDIF
            ENDIF

        GIVE_MELEE_ATTACK_TO_CHAR player_actor 15 6 //Default

        CLEO_CALL getSpiderDriveCarsItem 0 (iTempVar)
        IF iTempVar = 1
            SET_PLAYER_ENTER_CAR_BUTTON player TRUE
        ELSE
            SET_PLAYER_ENTER_CAR_BUTTON player FALSE
        ENDIF
        CLEO_CALL getFriendlyNeighborhoodItem 0 (iTempVar)
        IF iTempVar = 1
            SET_POLICE_IGNORE_PLAYER player TRUE
        ELSE
            SET_POLICE_IGNORE_PLAYER player FALSE
        ENDIF
        //avoid strampling peds on ground
        WRITE_MEMORY 0x56D271 1 0 FALSE ////0x56D271  byte  (CLocalisation::KickingWhenDown)
    ELSE
        toggleSpiderMod = 0     // 0:OFF || 1:ON 
        IF NOT iFireHead = 0
            KILL_FX_SYSTEM iFireHead
            iFireHead = 0
        ENDIF
        SET_CLEO_SHARED_VAR varStatusSpiderMod toggleSpiderMod // Turn Off running sp_main thread
        CLEO_CALL storeStatusSpiderMod 0 toggleSpiderMod
        SET_PLAYER_MODEL 0 NULL
        WAIT 5
        BUILD_PLAYER_MODEL player
        GOSUB restoreWalkstyle
        WAIT 0
        GIVE_MELEE_ATTACK_TO_CHAR player_actor 4 6 //Default

        REMOVE_WEAPON_FROM_CHAR player_actor WEAPONTYPE_BRASSKNUCKLE
        SET_PLAYER_ENTER_CAR_BUTTON player TRUE
        SET_POLICE_IGNORE_PLAYER player FALSE
        //restore - avoid strampling peds on ground
        WRITE_MEMORY 0x56D271 1 1 FALSE ////0x56D271  byte  (CLocalisation::KickingWhenDown)
    ENDIF

    USE_TEXT_COMMANDS FALSE
    GOSUB deleteChar
    REMOVE_ANIMATION "spider"
    CLEO_CALL release_SFX_Menu 0

    WHILE IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
        WAIT 0
    ENDWHILE
    REMOVE_TEXTURE_DICTIONARY

    GOSUB unlock_player_controls
        CLEO_CALL getCharWorldXYZCoords 0 (x y z fZAnglePlayerAir)
        SET_CHAR_COORDINATES_SIMPLE player_actor x y z 
        SET_CHAR_HEADING player_actor fZAnglePlayerAir
    RESTORE_CAMERA 
    RESTORE_CAMERA_JUMPCUT
    SET_CAMERA_BEHIND_PLAYER 

    DO_FADE 700 FADE_IN
    WAIT 700
    SET_CLEO_SHARED_VAR varInMenu 0     // // 0:OFF || 1:ON

GOTO start
///---------------------------------------------------------------------

///---------------------------------------------------------------------
is_any_hud_element_enabled:
    counter = 3
    WHILE 10 >= counter
        GET_CLEO_SHARED_VAR counter (iTempVar)
        IF iTempVar = 1
            RETURN_TRUE
            RETURN
        ENDIF
        counter ++
    ENDWHILE
    RETURN_FALSE
RETURN
///---------------------------------------------------------------------

///---------------------------------------------------------------------
set_config_alternative_swing:
    CLEO_CALL getAlternativeItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeAlternativeItem 0 iTempVar
    SET_CLEO_SHARED_VAR varAlternativeSwing iTempVar       // 0:OFF || 1:ON
RETURN

set_config_simulate_building_web_attach:
    CLEO_CALL getSwingBuildingItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeSwingBuildingItem 0 iTempVar
    SET_CLEO_SHARED_VAR varSwingBuilding iTempVar       // 0:OFF || 1:ON
RETURN

set_config_fix_ground_collision:
    CLEO_CALL getFixGroundItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeFixGroundItem 0 iTempVar
    SET_CLEO_SHARED_VAR varFixGround iTempVar       // 0:OFF || 1:ON
RETURN

set_config_mouse_control:
    CLEO_CALL getMouseControlItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeMouseControlItem 0 iTempVar
    SET_CLEO_SHARED_VAR varMouseControl iTempVar       // 0:OFF || 1:ON                            
RETURN

set_config_auto_aim:
    CLEO_CALL getAutoAimItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeAutoAimItem 0 iTempVar
    SET_CLEO_SHARED_VAR varAimSetup iTempVar        // 0:Manual Aim || 1:Auto Aim
RETURN

set_config_spider_can_drive:
    CLEO_CALL getSpiderDriveCarsItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeSpiderDriveCarsItem 0 iTempVar
    SET_CLEO_SHARED_VAR varPlayerCanDrive iTempVar          // 0:OFF || 1:ON               
RETURN

set_config_friendly_neighborhood:
    CLEO_CALL getFriendlyNeighborhoodItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeFriendlyNeighborhoodItem 0 iTempVar
    SET_CLEO_SHARED_VAR varFriendlyN iTempVar   // 0:OFF || 1:ON
RETURN

set_config_throw_veh_doors:
    CLEO_CALL getThrowVehDoorsItem 0 (iTempVar)
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    CLEO_CALL storeThrowVehDoorsItem 0 iTempVar
    SET_CLEO_SHARED_VAR varThrowVehDoors iTempVar      // 0:OFF || 1:ON
RETURN
///---------------------------------------------------------------------

///---------------------------------------------------------------------
set_skill1:
    GET_CLEO_SHARED_VAR varSkill1 iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill1 iTempVar       // 0:OFF || 1:ON
RETURN

set_skill2:
    GET_CLEO_SHARED_VAR varSkill2 iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill2 iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3:
    GET_CLEO_SHARED_VAR varSkill3 iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3 iTempVar       // 0:OFF || 1:ON
RETURN

set_skill2a:
    GET_CLEO_SHARED_VAR varSkill2a iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill2a iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3a:
    GET_CLEO_SHARED_VAR varSkill3a iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3a iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3b:
    GET_CLEO_SHARED_VAR varSkill3b iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3b iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3c:
    GET_CLEO_SHARED_VAR varSkill3c iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3c iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3c1:
    GET_CLEO_SHARED_VAR varSkill3c1 iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3c1 iTempVar       // 0:OFF || 1:ON
RETURN

set_skill3c2:
    GET_CLEO_SHARED_VAR varSkill3c2 iTempVar
    IF iTempVar = 1
        iTempVar = 0
    ELSE
        iTempVar = 1
    ENDIF
    SET_CLEO_SHARED_VAR varSkill3c2 iTempVar       // 0:OFF || 1:ON
RETURN
///---------------------------------------------------------------------

//---+------------------------ DRAW GENERAL ITEMS-------------------------------
drawItems:
    //Draw Items
    //GOSUB ProcessGame_LightScene
    GOSUB ProcessGame_and_DrawItems_upper_Panel
    GOSUB ProcessGame_and_DrawItems_Lower_Panel
    SWITCH iPanel
        CASE idOptions_l
            GOSUB ProcessGame_and_DrawItems_SETTINGS
            BREAK
        CASE idMap_l
            GOSUB deleteChar
            GOSUB defaultCameraMenu
            GOSUB ProcessGame_and_DrawMenu_MAP
            GOSUB ProcessGame_and_DrawMenu_MAP_Legend
            BREAK
        CASE idSuits_l
            CLEO_CALL getStatusSpiderMod 0 (iTempVar)
            IF iTempVar = 0 // OFF
                GOSUB DrawWarningInfo_SUITS
            ELSE
                GOSUB renderChar
                GOSUB playActorAnimation
                GOSUB ProcessGame_and_DrawMenu_SUITS
            ENDIF
            BREAK
        CASE idSkills_l
            GOSUB deleteChar
            GOSUB defaultCameraMenu
            GOSUB ProcessGame_and_DrawItems_SKILLS
            GOSUB DrawSelector_SKILLS
            GOSUB ProcessGame_and_DrawMenu_RightPanel_SKILLS
            BREAK
        CASE idCharacters_l
            GOSUB ProcessGame_and_DrawItems_CHARACTERS
            GOSUB DrawSelector_CHARACTER
            GOSUB DrawInfo_CHARACTERS
            BREAK
        CASE idMoves_l
            GOSUB ProcessGame_and_DrawItems_MOVES_and_CONTROLS
            BREAK 
        CASE idVersionInfo_l
            GOSUB ProcessGame_and_DrawItems_STATISTICS
            GOSUB ProcessGame_and_DrawItems_VERSION
            BREAK
    ENDSWITCH
    WAIT 0
RETURN
///---------------------------------------------------------------------

//---+------------------------ DESIGN MAIN MENU------------------------
ProcessGame_LightScene:
    GET_OFFSET_FROM_CAMERA_IN_WORLD_COORDS 0.0 0.0 0.0 (x y z)
    //GET_ACTIVE_CAMERA_COORDINATES (x y z)
    DRAW_LIGHT_WITH_RANGE x y z 100 100 100 20.0
RETURN

ProcessGame_and_DrawItems_upper_Panel:
    iColumn = iUpAfterStart
    xCoord = 87.4285 //92.0
    yCoord = 15.0
    xSize = 54.857    //64.0
    ySize = 30.0
    WHILE iUpPrevEnd >= iColumn     //1:OPTIONS||2:MAP||3:SUITS||4:SKILLS||5:CHARACTERS||6:MOVES||7:VERSION
        IF iColumn = iPanel  //Active Item
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (xSize ySize) (29 38 53 255) (1.8) (1 0 0 0) (0 125 180 200) iColumn 7 (0.0 0.0) //BLUE
        ELSE
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (xSize ySize) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iColumn 7 (0.0 0.0)    //RED
        ENDIF
        iColumn += 1
        xCoord += 54.857
    ENDWHILE
    //SIDES-ARROWS
    IF IS_PC_USING_JOYPAD
        iColumn = 65    //~k~~PED_CYCLE_WEAPON_LEFT~
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (30.0 15.0) (60.0 ySize) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iColumn 5 (0.0 0.0) //~k~~PED_CYCLE_WEAPON_LEFT~
        iColumn = 66    //~k~~PED_CYCLE_WEAPON_RIGHT~
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (474.0 15.0) (60.0 ySize) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iColumn 5 (0.0 0.0) //~k~~PED_CYCLE_WEAPON_RIGHT~
    ELSE
        iColumn = 69    //~h~Q
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (30.0 15.0) (60.0 ySize) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iColumn 5 (0.0 0.0) //~k~~PED_CYCLE_WEAPON_LEFT~
        iColumn = 70    //~h~E
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (474.0 15.0) (60.0 ySize) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iColumn 5 (0.0 0.0) //~k~~PED_CYCLE_WEAPON_RIGHT~
    ENDIF
    //LEVEL_INFO
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 yCoord) (136.0 ySize) (14 20 32 255) (0.5) (0 0 1 0) (0 0 0 240) -1 -1 (0.0 0.0) //BLUE_BACKGROUND_BIG_LEVEL
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (519.0 yCoord) (ySize ySize) (43 56 75 255) (1.8) (1 0 0 0) (28 50 71 255) -1 1 (0.0 0.0) //BACKGROUND_SMALL_LEVEL
    iTempVar = idLevelTITLE_l  //120  // text id
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (519.0 5.0) (ySize 10.0) (44 77 96 255) (1.8) (1 0 0 0) (28 50 71 255) iTempVar 11 (0.0 0.0) //LEVEL
    GET_CLEO_SHARED_VAR varLevelChar (iTempVar)
    CLEO_CALL GUI_DrawBox_WithNumber 0 (519.0 yCoord) (ySize ySize) (0 0 0 0) 121 10 (0.0 -3.0) iTempVar  //~1~   (29 38 53 255)

    // DRAW_LEVEL_PROGRESS
    CLEO_CALL drawBar 0 fCurrentLevel
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (587.0 9.0) (82.5 6.0) (0 0 0 0) (0.25) (1 1 1 1) (19 247 232 200) -1 -1 (0.0 0.0) //LINE_SIDES
    iTempVar =# fCurrentLevel
    CLEO_CALL GUI_DrawBox_WithNumber 0 (553.0 19.0) (15.0 5.0) (29 38 53 0) 121 14 (0.0 0.0) iTempVar  //~1~
    iTempVar = 1000
    CLEO_CALL GUI_DrawBox_WithNumber 0 (572.0 19.0) (15.0 5.0) (29 38 53 0) 125 15 (0.0 0.0) iTempVar  // ~1~ XP
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (587.0 19.0) (83.0 11.0) (0 0 0 0) (0.25) (0 0 1 0) (19 247 232 200) -1 -1 (0.0 0.0) //LINE_DOWN_SIDE

    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawItems_Lower_Panel:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (415.0 438.0) (450.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) -1 -1 (0.0 0.0) //RED_BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (95.0 438.0) (190.0 20.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0) //BLUE_BACKGROUND

    SWITCH iPanel
        CASE idOptions_l
            IF IS_PC_USING_JOYPAD
                iTempVar = idSelect_l   //~x~ SELECT
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ SELECT
                iTempVar = idClose_l    //~o~ CLOSE
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
            ELSE
                iTempVar = 71
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~SELECT
                iTempVar = 18    
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
            ENDIF
            BREAK
        CASE idMap_l
            SWITCH iPanelB
                DEFAULT
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idClose_l    //~o~ CLOSE
                    ELSE
                        iTempVar = 18    //~h~LMB ~s~CLOSE
                    ENDIF
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
                    BREAK
                CASE iPanel_config_map_legend
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idSelect_l   //~x~ SELECT
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ SELECT
                        iTempVar = idClose_l    //~o~ CLOSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
                    ELSE
                        iTempVar = 71
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~SELECT
                        iTempVar = 18    
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
                    ENDIF
                    BREAK
            ENDSWITCH
            BREAK 
        CASE idSuits_l
            SWITCH iPanelB
                CASE menSelSuit
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idEquip_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ EQUIP
                        iTempVar = idBack_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ BACK
                    ELSE
                        iTempVar = 17
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~EQUIP
                        iTempVar = 19
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~BACK
                    ENDIF
                    BREAK
                CASE menPowerSuit
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idEquip_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ EQUIP
                        iTempVar = idBack_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ BACK
                    ELSE
                        iTempVar = 17
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~EQUIP
                        iTempVar = 19
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~BACK
                    ENDIF
                    BREAK
                CASE menConfigSuit
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idSelect_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ SELECT
                        iTempVar = idBack_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ BACK
                    ELSE
                        iTempVar = 71   
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~SELECT
                        iTempVar = 19
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~BACK
                    ENDIF
                    BREAK
                DEFAULT
                    IF IS_PC_USING_JOYPAD
                        iTempVar = idRotate_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (515.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~c~ ROTATE
                        iTempVar = idSelect_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ SELECT
                        iTempVar = idClose_l
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
                    ELSE
                        iTempVar = 16
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (515.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~TAB~s~or~h~CAPSLOCK ~s~ROTATE
                        iTempVar = 71   
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~SELECT
                        iTempVar = 18    
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
                    ENDIF
                    BREAK
            ENDSWITCH
            BREAK
        CASE idSkills_l
            IF IS_PC_USING_JOYPAD
                iTempVar = idSelect_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~x~ SELECT
                iTempVar = idBack_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ BACK
            ELSE
                iTempVar = 71   
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (565.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~SPACE ~s~SELECT
                iTempVar = 19
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~BACK
            ENDIF
            BREAK
        CASE idCharacters_l
            //move L
            IF IS_PC_USING_JOYPAD
                iTempVar = idClose_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
            ELSE
                iTempVar = 18    
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
            ENDIF
            BREAK
        CASE idMoves_l
            IF IS_PC_USING_JOYPAD
                iTempVar = idClose_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
            ELSE
                iTempVar = 18    
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
            ENDIF
            BREAK
        CASE idVersionInfo_l
            IF IS_PC_USING_JOYPAD
                iTempVar = idClose_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~o~ CLOSE
            ELSE
                iTempVar = 18    
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (615.0 438.0) (50.0 20.0) (165 6 22 255) (0.5) (0 1 0 1) (0 0 0 200) iTempVar 7 (0.0 0.0) //~h~LMB ~s~CLOSE
            ENDIF
            BREAK
    ENDSWITCH

    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawItems_Right_Panel:

RETURN
///---------------------------------------------------------------------


//---+------------------ MAIN MENU - SETTINGS -------------------------
ProcessGame_and_DrawItems_SETTINGS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 229.0) (640.0 398.0) (0 0 0 180) (0.5) (0 0 0 0) (236 255 255 0) -1 -1 (0.0 0.0)   //BLACK_BACKGROUND
    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 110.0) (219.428 160.0) (17 93 155 200) (0.5) (0 1 0 1) (236 255 255 100) -1 -1 (0.0 0.0)   //TOP
    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 240.0) (219.428 100.0) (10 51 83 125) (0.5) (0 1 0 1) (236 255 255 100) -1 -1 (0.0 0.0)   //MEDIUM
    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 359.0) (219.428 138.0) (46 87 119 75) (0.5) (1 1 0 1) (236 255 255 100) -1 -1 (0.0 0.0)   //LOW

    //GET_FIXED_XY_ASPECT_RATIO (292.56 426.43) (xSize ySize) //219.42 398.0
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idBackgroundSett (169.714 229.0) (219.42 398.0) (255 255 255 100)

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 229.0) (219.428 398.0) (0 0 0 0) (0.5) (0 1 0 1) (236 255 255 100) -1 -1 (0.0 0.0)   //SIDE LINES

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (122.214 287.0) (75.0 34.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE DIVISION
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (217.214 287.0) (75.0 34.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE DIVISION
    GET_FIXED_XY_ASPECT_RATIO (20.0 20.0) (xSize ySize)
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idSpiderIcon (169.714 307.0) (xSize ySize) (255 255 255 100)

    SWITCH iPanelB
        DEFAULT     //iPanel_null_settings
            //id -> 110 - 112
            CONST_INT iMaxOptions 112
            CONST_INT iMinOptions 110
            yCoord = 100.0
            iRow = iMinOptions

            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 60.0) (195.0 25.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 170) 113 5 (-95.0 0.0)   //CONFIGURATION
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 128.0) (20.0 85.0) (0 0 0 0) (0.5) (0 1 0 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE LEFT

            //GET_FIXED_XY_ASPECT_RATIO (100.0 35.0) (xSize ySize)    //75.0 32.67
            xSize = 50.0
            ySize = 35.0
            WHILE iMaxOptions >= iRow
                //19.428 + 120 + 80 = 219.428
                //<-60 --- +19.428 + (120/2) = 139.428
                IF iRow = iActiveRow  //Active Item
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (139.428 yCoord) (120.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) iRow 5 (-55.0 0.0)
                    SWITCH iRow
                        CASE 110   //Spiderman mod
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 606 19 (0.0 0.0)   //When turned ON, you'll become Spider-Man.
                            BREAK
                        CASE 111   //Toggle HUD
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 607 19 (0.0 0.0)   //When turned ON, HUD elements will show on-screen.
                            BREAK
                        CASE 112   //BackGround Music
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 608 19 (0.0 0.0)   //When turned ON, background music will be played ~n~depending on each situation.
                            BREAK
                    ENDSWITCH
                ELSE
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (139.428 yCoord) (120.0 30.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) iRow 5 (-55.0 0.0)
                ENDIF
                SWITCH iRow
                    CASE 110   //Spiderman mod
                        CLEO_CALL getStatusSpiderMod 0 (iTempVar)
                        BREAK
                    CASE 111   //Toggle HUD
                        GET_CLEO_SHARED_VAR varHUD (iTempVar)
                        BREAK
                    CASE 112   //BackGround Music
                        GET_CLEO_SHARED_VAR varMusic (iTempVar)
                        BREAK
                ENDSWITCH
                IF iTempVar = 1  //ON
                    //<-60 --- +19.428 + 120 + 80/2 = 239.428
                    IF iRow = iActiveRow  //Active Item
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 604 5 (0.0 0.0)   //ON
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ELSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 30.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 604 5 (0.0 0.0)  //OFF
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ENDIF
                ELSE
                    IF iRow = iActiveRow  //Active Item
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 605 5 (0.0 0.0)   //ON
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ELSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 30.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 605 5 (0.0 0.0)  //OFF
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ENDIF
                ENDIF
                iRow ++
                yCoord += 30.0
            ENDWHILE
            USE_TEXT_COMMANDS FALSE
            BREAK

        CASE iPanel_hud_settings
            CONST_INT iMaxHudElements 138
            CONST_INT iMinHudElements 131
            iRow = iMinHudElements

            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 42.5) (195.0 25.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 170) 114 5 (-95.0 0.0)   //HUD ELEMENTS
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 128.0) (20.0 85.0) (0 0 0 0) (0.5) (0 1 0 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE LEFT

            //GET_FIXED_XY_ASPECT_RATIO (100.0 35.0) (xSize ySize)    //75.0 32.67
            yCoord = 70.0
            xSize = 50.0
            ySize = 35.0
            WHILE iMaxHudElements >= iRow
                //19.428 + 120 + 80 = 219.428
                //<-60 --- +19.428 + (120/2) = 139.428
                IF iRow = iActiveRow  //Active Item
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (139.428 yCoord) (120.0 16.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) iRow 5 (-55.0 0.0)
                    SWITCH iRow
                        CASE 131
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 611 19 (0.0 0.0)
                            BREAK
                        CASE 132
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 612 19 (0.0 0.0)
                            BREAK
                        CASE 133
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 613 19 (0.0 0.0)
                            BREAK
                        CASE 134
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 614 19 (0.0 0.0)
                            BREAK
                        CASE 135
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 615 19 (0.0 0.0)
                            BREAK
                        CASE 136
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 616 19 (0.0 0.0)
                            BREAK
                        CASE 137
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 617 19 (0.0 0.0)
                            BREAK
                        CASE 138
                            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (169.714 230.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 618 19 (0.0 0.0)
                            BREAK
                    ENDSWITCH
                ELSE
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (139.428 yCoord) (120.0 16.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) iRow 5 (-55.0 0.0)
                ENDIF
                SWITCH iRow
                    CASE 131
                        GET_CLEO_SHARED_VAR varHudRadar (iTempVar)
                        BREAK
                    CASE 132
                        GET_CLEO_SHARED_VAR varHudHealth (iTempVar)
                        BREAK
                    CASE 133
                        GET_CLEO_SHARED_VAR varHudAmmo (iTempVar)
                        BREAK
                    CASE 134
                        GET_CLEO_SHARED_VAR varHudMoney (iTempVar)
                        BREAK
                    CASE 135
                        GET_CLEO_SHARED_VAR varHudTime (iTempVar)
                        BREAK
                    CASE 136
                        GET_CLEO_SHARED_VAR varHudBreath (iTempVar)
                        BREAK
                    CASE 137
                        GET_CLEO_SHARED_VAR varHudArmour (iTempVar)
                        BREAK
                    CASE 138
                        GET_CLEO_SHARED_VAR varHudWantedS (iTempVar)
                        BREAK
                ENDSWITCH
                IF iTempVar = 1  //ON
                    //<-60 --- +19.428 + 120 + 80/2 = 239.428
                    IF iRow = iActiveRow  //Active Item
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 16.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 604 5 (0.0 0.0)   //ON
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ELSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 16.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 604 5 (0.0 0.0)  //OFF
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ENDIF
                ELSE
                    IF iRow = iActiveRow  //Active Item
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 16.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 605 5 (0.0 0.0)   //ON
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ELSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (239.428 yCoord) (80.0 16.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 605 5 (0.0 0.0)  //OFF
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE idBackgroundArrows (239.428 yCoord) (xSize ySize) (255 255 255 120)
                    ENDIF
                ENDIF
                iRow ++
                yCoord += 16.0
            ENDWHILE
            USE_TEXT_COMMANDS FALSE
            BREAK
    ENDSWITCH

    GET_FIXED_XY_ASPECT_RATIO (250.0 250.0) (xSize ySize)
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idLogoSP (465.0 140.0) (xSize ySize) (255 255 255 240)
RETURN
///---------------------------------------------------------------------
        /*SWITCH iTempVar
            CASE 1  //ON
                BREAK
            CASE 0  //OFF
                BREAK
        ENDSWITCH*/

//---+---------------------- MAIN MENU - MAP--------------------------
ProcessGame_and_DrawMenu_MAP:
    SWITCH iPanelB
        DEFAULT
            //CLEO_CALL storeScreenCoords 0 xCoord yCoord
            CLEO_CALL drawMap 0 (399.0 229.0) (426.43)   //398.0
            //CLEO_CALL drawLandMarks 0 (426.43)
            GET_CLEO_SHARED_VAR varMapLegendLandMark iTempVar
            IF iTempVar = 1 //Show: 1= enable   || 0= disable
                CLEO_CALL drawOnMapIconMarks 0 1 426.43     //||landmarks:1 ||backpacks:2 ||crimes:3
            ENDIF
            GET_CLEO_SHARED_VAR varMapLegendBackPack iTempVar
            IF iTempVar = 1 //Show: 1= enable   || 0= disable
                CLEO_CALL drawOnMapIconMarks 0 2 426.43     //||landmarks:1 ||backpacks:2 ||crimes:3
            ENDIF
            CLEO_CALL drawChar3DCoordsTo2DScreenCords 0 (426.43)
            CLEO_CALL getScreenCoords 0 (xCoord yCoord)
            CLEO_CALL drawDataJoystickPos2DScreenCords 0 xCoord yCoord (426.43)
            USE_TEXT_COMMANDS FALSE    
            BREAK
        CASE iPanel_config_map_legend
            BREAK
    ENDSWITCH
RETURN

ProcessGame_and_DrawMenu_MAP_Legend: //Legend
    SWITCH iPanelB
        DEFAULT
            GOSUB ProcessGame_and_DrawMenu_MAP_Legend_A     //draw left panel
            BREAK
        CASE iPanel_config_map_legend
            GOSUB ProcessGame_and_DrawMenu_MAP_Legend_Settings     //settings legend
            BREAK
    ENDSWITCH
RETURN

ProcessGame_and_DrawMenu_MAP_Legend_A:
    //640-398=242
    //242+398=640
    //242+(398/2)=242+199=441   // new pos map
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (75.0 60.0) (100.0 20.0) (1 206 251 200) (0.5) (0 0 0 0) (0 125 180 150) -1 -1 (0.0 0.0)    //UPPER_ZONE_NAME
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (75.0 60.0) (100.0 20.0) (0 0 0 0) (0.5) (0 0 0 0) (0 125 180 150) 167 7 (-40.0 0.0)    //San Fierro

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (100.0 220.0) (150.0 300.0) (14 20 32 255) (0.5) (1 1 1 1) (0 125 180 150) -1 -1 (0.0 0.0)    //BLUE_BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (100.0 82.5) (150.0 25.0) (19 77 88 50) (0.25) (0 0 1 0) (25 255 251 200) -1 -1 (0.0 0.0)    //empty

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (100.0 390.0) (150.0 20.0) (16 43 52 200) (0.5) (0 0 0 0) (0 125 180 150) 178 5 (0.0 0.0)    //LOWER_PROGRESS ||Show Legend

    GET_FIXED_XY_ASPECT_RATIO 25.0 25.0 (x y)
    //id - 170 - 174
    yCoord = 82.5
    iRow = 170
    WHILE 174 >= iRow
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (100.0 yCoord) (150.0 25.0) (19 77 88 50) (0.25) (0 0 1 0) (25 255 251 200) iRow 17 (-45.0 0.0) //text
        SWITCH iRow
            CASE 170
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon1 (40.0 yCoord) (x y) (255 255 255 210)    //Marker
                BREAK
            CASE 171
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon6 (40.0 yCoord) (x y) (255 255 255 210)    //landMark
                GET_CLEO_SHARED_VAR varLandmarksProgress (iTempVar)
                CLEO_CALL GUI_DrawBox_WithNumber 0 (160.0 yCoord) (30.0 20.0) (0 0 0 1) 126 5 (0.0 0.0) iTempVar  //~1~ / 10
                BREAK
            CASE 172
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon3 (40.0 yCoord) (x y) (255 255 255 210)    //backpack
                GET_CLEO_SHARED_VAR varBackpacksProgress (iTempVar)
                CLEO_CALL GUI_DrawBox_WithNumber 0 (160.0 yCoord) (30.0 25.0) (0 0 0 0) 126 5 (0.0 0.0) iTempVar  //~1~ / 10
                BREAK
            CASE 173
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon5 (40.0 yCoord) (x y) (255 255 255 210)    //Crimes
                BREAK
            CASE 174
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon4 (40.0 yCoord) (x y) (255 255 255 210)    //Black Cat
                BREAK
        ENDSWITCH
        iRow ++
        yCoord += 25.0
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawMenu_MAP_Legend_Settings:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 229.0) (640.0 398.0) (0 0 0 200) (0.5) (0 0 0 0) (236 255 255 0) -1 -1 (0.0 0.0)   //BLACK_BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 100.0) (150.0 40.0) (19 77 88 0) (0.25) (0 0 1 0) (0 125 180 200) 177 13 (0.0 0.0)    //TITLE MAP LEGEND

    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 229.0) (320.0 200.0) (14 20 32 50) (0.5) (1 1 1 1) (0 125 180 10) -1 -1 (0.0 0.0)    //BLUE_BACKGROUND
    //id - 170 - 174
    xCoord = 320.0
    yCoord = 145.0
    iRow = 170
    WHILE 174 >= iRow
        IF iRow = iActiveRow  //Active Item
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (320.0 25.0) (225 225 225 60) (0.25) (1 1 1 1) (0 125 180 50) -1 -1 (0.0 0.0)   //WHITE_BACKGROUND
        ENDIF
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (320.0 25.0) (19 77 88 10) (0.25) (0 0 1 0) (25 255 251 15) iRow 17 (-110.0 0.0) //text
        GET_FIXED_XY_ASPECT_RATIO 25.0 25.0 (x y)
        SWITCH iRow
            CASE 170
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon1 (190.0 yCoord) (x y) (255 255 255 210)    //Marker
                iTempVar = 0
                BREAK
            CASE 171
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon6 (190.0 yCoord) (x y) (255 255 255 210)    //landMark
                GET_CLEO_SHARED_VAR varMapLegendLandMark iTempVar
                BREAK
            CASE 172
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon3 (190.0 yCoord) (x y) (255 255 255 210)    //backpack
                GET_CLEO_SHARED_VAR varMapLegendBackPack iTempVar
                BREAK
            CASE 173
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon5 (190.0 yCoord) (x y) (255 255 255 210)    //Crimes
                iTempVar = 0
                BREAK
            CASE 174
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idMapIcon4 (190.0 yCoord) (x y) (255 255 255 210)    //Black Cat
                iTempVar = 0
                BREAK
        ENDSWITCH
        GET_FIXED_XY_ASPECT_RATIO 10.0 10.0 (x y)
        IF iTempVar = 1  //ON
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE successIcon28 (170.0 yCoord) (x y) (255 255 255 255)
        ELSE
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE successIconE (170.0 yCoord) (x y) (255 255 255 255)
        ENDIF
        iRow ++
        yCoord += 25.0
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN
///---------------------------------------------------------------------


//---+---------------------- MAIN MENU - SUITS--------------------------
DrawWarningInfo_SUITS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 224.0) (320.0 224.0) (14 20 32 255) (0.5) (1 1 1 1) (0 125 180 150) -1 -1 (0.0 0.0)    //BLUE_BACKGROUND
    iTempVar = idWarningTITLE_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 168.0) (320.0 112.0) (164 13 20 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 10 (0.0 0.0)    //TITLE_level format(10)
    iTempVar = idWarningMsg_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 280.0) (320.0 112.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 7 (0.0 0.0)   //TEXT

    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawMenu_SUITS:     
    //DRAW_SUIT
    //GET_FIXED_XY_ASPECT_RATIO (80.0 80.0) (xSize ySize) //60.0 55.0
    xSize = 60.00
    ySize = 74.67
    GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
    READ_MEMORY (iTempVar) 4 FALSE (iSelectedSuit)
    IF iSelectedSuit = 0
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE unknownSuit29 (40.0 80.0) (xSize ySize) (255 255 255 255)
    ELSE
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE iSelectedSuit (40.0 80.0) (xSize ySize) (255 255 255 255)
    ENDIF
    iTempVar = idSuit_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (40.0 46.0) (60.0 10.0) (6 253 244 0) (1.0) (0 0 1 0) (6 253 244 200) iTempVar 1 (-30.0 0.0)   // SUIT

    //DRAW_SUIT_POWER
    GET_LABEL_POINTER GUI_Memory_PowerSuitItem (iTempVar)
    READ_MEMORY (iTempVar) 4 FALSE (iSelectedPower)
    IF iSelectedPower = 0
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE customSuit27 (40.0 155.0) (xSize ySize) (255 255 255 255)
    ELSE
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE pow_background (40.0 155.0) (xSize ySize) (255 255 255 200)    //IMAGE_SUIT-BACKGROUND
        GOSUB get_power_id_by_selected_item
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE iTempVar (40.0 155.0) (xSize ySize) (2 254 252 230)    //IMAGE_SUIT-POWER
    ENDIF
    iTempVar = idSuitPower_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (40.0 121.0) (60.0 10.0) (6 253 244 0) (1.0) (0 0 1 0) (6 253 244 200) iTempVar 1 (-30.0 0.0)   // SUIT POWER

    //DRAW_SUIT_MODS
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE customSuit27 (40.0 230.0) (xSize ySize) (255 255 255 255)
    iTempVar = idSuitMods_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (40.0 196.0) (60.0 10.0) (6 253 244 0) (1.0) (0 0 1 0) (6 253 244 200) iTempVar 1 (-30.0 0.0)   // SUIT MODS

    SWITCH iPanelB
        DEFAULT
            //DRAW_SELECTION
            GOSUB setCameraSuitMain
            //GET_FIXED_XY_ASPECT_RATIO (88.0 100.0) (xSize ySize) //(65.0 74.0)
            xSize = 66.00
            ySize = 93.33
            SWITCH iActive
                CASE menSelSuit
                    USE_TEXT_COMMANDS FALSE
                    DRAW_SPRITE selectSuit26 (40.0 75.5) (xSize ySize) (255 255 255 255)
                BREAK
                CASE menPowerSuit
                    USE_TEXT_COMMANDS FALSE
                    DRAW_SPRITE selectSuit26 (40.0 150.5) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE menConfigSuit
                    USE_TEXT_COMMANDS FALSE
                    DRAW_SPRITE selectSuit26 (40.0 225.5) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
            GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
            READ_MEMORY (iTempVar) 4 FALSE (iSelectedSuit)
            GOSUB ProcessGame_and_DrawMenu_RightPanel_SUITS
            BREAK

        CASE menSelSuit
            GOSUB setCameraSuitMatrix
            GOSUB ProcessGame_and_DrawItems_SUITS
            GOSUB DrawSelector_SUITS
            GOSUB DrawCheckMarks_SUITS
            GOSUB ProcessGame_and_DrawMenu_RightPanel_SUITS
            BREAK

        CASE menPowerSuit
            //GOSUB defaultCameraMenu
            //GOSUB deleteChar
            GOSUB setCameraSuitMatrix
            GOSUB ProcessGame_and_DrawItems_POWER_SUITS
            GOSUB DrawSelector_POWER_SUITS
            GOSUB DrawCheckMarks_POWER_SUITS
            GOSUB ProcessGame_and_DrawMenu_RightPanel_POWER_SUITS
            BREAK

        CASE menConfigSuit
            GOSUB defaultCameraMenu
            GOSUB deleteChar
            GOSUB ProcessGame_and_DrawItems_Config_SUIT
            GOSUB ProcessGame_and_DrawMenu_RightPanel_CONFIG_SUIT
            BREAK
    ENDSWITCH
RETURN

ProcessGame_and_DrawMenu_RightPanel_SUITS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 229.0) (136.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND
    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 398.0) (120.0 40.0) (16 43 52 200) (0.5) (1 1 1 1) (6 253 244 200) -1 -1 (0.0 0.0)   //BLUE_ATT_DEF

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (535.75 105.0) (63.5 15.0) (16 43 52 0) (0.5) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_LEFT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (633.75 105.0) (12.5 15.0) (16 43 52 0) (0.5) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_RIGHT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 97.5) (60.0 15.0) (16 43 52 0) (0.5) (1 1 1 1) (31 181 240 200) -1 -1 (0.0 0.0)   //SIDES_LINES

    iTempVar = idNoPowerText_l    
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.5 122.5) (136.0 15.0) (16 43 52 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 2 (-60.0 0.0)   //NO ASSOCIATED SUIT POWER

    GOSUB DrawInfo_SUITS
    USE_TEXT_COMMANDS FALSE
RETURN

DrawStatusInfo_SUITS:

RETURN

ProcessGame_and_DrawMenu_RightPanel_POWER_SUITS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 229.0) (136.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND

    GOSUB DrawInfo_POWER_SUITS_RightPanel
    GOSUB DrawInfo_SUITS_RightPanel_PowerSuits

    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 398.0) (120.0 40.0) (16 43 52 200) (0.5) (1 1 1 1) (6 253 244 200) -1 -1 (0.0 0.0)   //BLUE_ATT_DEF
    USE_TEXT_COMMANDS FALSE
RETURN
///---------------------------------------------------------------------

//---+---------------- SUB-MENU - CONFIG SUITS MODS--------------------
ProcessGame_and_DrawItems_Config_SUIT:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 193.75) (260.0 257.5) (2 40 49 150) (0.5) (1 1 1 1) (0 125 180 200) -1 -1 (0.0 0.0)   //BLUE_BACK_MAIN_SUIT_MODS
    iTempVar = idSuitMods_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 80.0) (250.0 15.0) (6 253 244 0) (0.5) (0 0 1 0) (6 253 244 200) iTempVar 2 (-110.0 0.0)   //TITLE_SUIT_MODS
    //----lines side - left
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (81.25 80.0) (7.5 15.0) (6 253 244 0) (0.25) (0 0 1 0) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (SUIT-)
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (77.5 87.5) (2.5 2.5) (6 253 244 200)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (73.75 220.0) (7.5 15.0) (6 253 244 0) (0.25) (0 0 1 0) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (SUIT_MOD-)
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (77.5 227.5) (2.5 2.5) (6 253 244 200)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (85.0 157.5) (15.0 140.0) (6 253 244 0) (0.25) (0 0 0 1) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (VERTICAL+UNION)
    //---------
    //id-> 100 - 106
    CONST_INT iMaxRowConfigSuit 107
    CONST_INT iMinRowConfigSuit 100
    GET_FIXED_XY_ASPECT_RATIO (90.0 35.0) (xSize ySize)
    yCoord = 108.0
    iRow = iMinRowConfigSuit
    WHILE iMaxRowConfigSuit >= iRow
        IF iRow = iActiveRow  //Active Item
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (150.0 yCoord) (130.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) iRow 5 (-50.0 0.0)
        ELSE
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (150.0 yCoord) (130.0 25.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) iRow 5 (-50.0 0.0) //190
        ENDIF
        SWITCH iRow
            CASE idAlternativeSwing_l   //100
                CLEO_CALL getAlternativeItem 0 (iTempVar)
                BREAK
            CASE idSwingBuildings_l     //101
                CLEO_CALL getSwingBuildingItem 0 (iTempVar)
                BREAK
            CASE idFixGround_l          //102
                CLEO_CALL getFixGroundItem 0 (iTempVar)
                BREAK
            CASE idControlMouse_l       //103
                CLEO_CALL getMouseControlItem 0 (iTempVar)
                BREAK
            CASE idAutoAim_l            //104
                CLEO_CALL getAutoAimItem 0 (iTempVar)
                BREAK
            CASE idSpiderCanDrive_l     //105
                CLEO_CALL getSpiderDriveCarsItem 0 (iTempVar)
                BREAK
            CASE idFriendlyN_l          //106
                CLEO_CALL getFriendlyNeighborhoodItem 0 (iTempVar)
                BREAK
            CASE idThrowDoors_l
                CLEO_CALL getThrowVehDoorsItem 0 (iTempVar)
                BREAK
        ENDSWITCH
        IF iTempVar = 1  //ON
            IF iRow = iActiveRow  //Active Item
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (280.0 yCoord) (130.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 604 5 (0.0 0.0)   //ON
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idBackgroundArrows (280.0 yCoord) (xSize ySize) (255 255 255 120)
            ELSE
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (280.0 yCoord) (130.0 30.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 604 5 (0.0 0.0)  //OFF
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idBackgroundArrows (280.0 yCoord) (xSize ySize) (255 255 255 120)
            ENDIF
        ELSE
            IF iRow = iActiveRow  //Active Item
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (280.0 yCoord) (130.0 25.0) (0 0 0 100) (0.5) (0 0 0 0) (49 116 168 200) 605 5 (0.0 0.0)   //ON
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idBackgroundArrows (280.0 yCoord) (xSize ySize) (255 255 255 120)
            ELSE
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (280.0 yCoord) (130.0 30.0) (0 0 0 0) (0.5) (0 0 0 0) (225 225 225 0) 605 5 (0.0 0.0)  //OFF
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idBackgroundArrows (280.0 yCoord) (xSize ySize) (255 255 255 120)
            ENDIF
        ENDIF
        /*SWITCH iTempVar
            CASE 1
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (302.5 yCoord) (25.0 25.0) (0 0 0 190) (0.5) (1 1 1 1) (225 225 225 30) -1 -1 (0.0 0.0)
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE turnOn30 (302.5 yCoord) (20.0 9.0) (255 255 255 255)
                BREAK
            CASE 0
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (302.5 yCoord) (25.0 25.0) (0 0 0 190) (0.5) (1 1 1 1) (225 225 225 30) -1 -1 (0.0 0.0)
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE turnOff31 (302.5 yCoord) (20.0 9.0) (255 255 255 255)
                BREAK
        ENDSWITCH*/
        iRow ++
        yCoord += 25.0
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawMenu_RightPanel_CONFIG_SUIT:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 229.0) (240.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_BIG
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 214.0) (240.0 30.0) (8 192 255 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_SMALL_CENTER
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 403.0) (240.0 50.0) (16 43 52 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_MEDIUM_DOWN

    //214.0-15=199-30=169 size y

    //iMaxRowConfigSuit 107
    //iMinRowConfigSuit 100
    iRow = iMinRowConfigSuit
    WHILE iMaxRowConfigSuit >= iRow
        IF iRow = iActiveRow  //Active Item
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 214.0) (240.0 30.0) (0 0 0 0) (0.25) (0 0 1 0) (0 125 180 200) iActiveRow 5 (-105.0 0.0)   //TEXT
            SWITCH iRow
                CASE idAlternativeSwing_l   //100
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 331 19 (0.0 0.0)   //
                    BREAK
                CASE idSwingBuildings_l     //101
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 332 19 (0.0 0.0)   //
                    BREAK
                CASE idFixGround_l          //102
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 333 19 (0.0 0.0)   //
                    BREAK
                CASE idControlMouse_l       //103
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 334 19 (0.0 0.0)   //
                    BREAK
                CASE idAutoAim_l            //104
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 335 19 (0.0 0.0)   //
                    BREAK
                CASE idSpiderCanDrive_l     //105
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 336 19 (0.0 0.0)   //
                    BREAK
                CASE idFriendlyN_l          //106
                    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 337 19 (0.0 0.0)   //
                    BREAK
                CASE idThrowDoors_l
                    IF IS_PC_USING_JOYPAD
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 338 19 (0.0 0.0)   //
                    ELSE
                        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 100.0) (0 0 0 0) (0.5) (0 0 0 0) (0 0 0 0) 339 19 (0.0 0.0)   //
                    ENDIF
                    BREAK
            ENDSWITCH
        ENDIF
        iRow ++
    ENDWHILE
    //GET_FIXED_XY_ASPECT_RATIO (320.0 181.07) (xSize ySize)    //240.0 169.0  (320.0 320.0) 
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idConfSuitBck (520.0 114.5) (240.0 298.67) (255 255 255 240)     //image red

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (460.0 361.0) (85.0 34.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE DIVISION
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (580.0 361.0) (85.0 34.0) (0 0 0 0) (0.5) (0 0 1 0) (236 255 255 80) -1 -1 (0.0 0.0)   //LINE DIVISION
    GET_FIXED_XY_ASPECT_RATIO (30.0 30.0) (xSize ySize)
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idSpiderIcon (520.0 380.0) (xSize ySize) (255 255 255 100)    
    USE_TEXT_COMMANDS FALSE
RETURN
///---------------------------------------------------------------------

//---+---------------- SUB-MENU - SUITS MATRIX 5X5 --------------------
ProcessGame_and_DrawItems_SUITS:
    //matrix 5x5=25
    CONST_INT iMinRowSuitQuantity 1
    //SUIT MENU
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 234.0) (260.0 337.5) (2 40 49 150) (0.5) (1 1 1 1) (0 125 180 200) -1 -1 (0.0 0.0)   //BLUE_BACK_MAIN_SUITS
    iTempVar = idSuit_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 80.0) (250.0 15.0) (6 253 244 0) (0.5) (0 0 1 0) (6 253 244 200) iTempVar 2 (-110.0 0.0)   //TITLE_SUIT
    
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (77.5 80.0) (15.0 15.0) (6 253 244 0) (0.25) (0 0 1 0) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (77.5 87.5) (2.5 2.5) (6 253 244 200)
    
    //GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize) //40.0 35.0
    xSize = 41.25
    ySize = 51.33
    //1st Row
    iMaxRowQuantity = 5
    idTexture = iMinRowSuitQuantity
    xCoord = 115.0
    yCoord = 120.0
    WHILE iMaxRowQuantity >= idTexture
        GOSUB get_unlock_code_suit
        IF pUnlockCode = iTempVar
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE idTexture (xCoord yCoord) (xSize ySize) (255 255 255 255)
        ELSE
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE unknownSuit29 (xCoord yCoord) (xSize ySize) (255 255 255 255)
        ENDIF
        idTexture ++
        xCoord += 50.0
        SWITCH idTexture
            CASE 6  //2nd Row
                iMaxRowQuantity = 10
                xCoord = 115.0
                yCoord += 50.0
            BREAK
            CASE 11 //3rd Row
                iMaxRowQuantity = 15
                xCoord = 115.0
                yCoord += 50.0
            BREAK
            CASE 16 //4th Row
                iMaxRowQuantity = 20
                xCoord = 115.0
                yCoord += 50.0
            BREAK
            CASE 21 //5th Row
                iMaxRowQuantity = 25
                xCoord = 115.0
                yCoord += 50.0
            BREAK
            CASE 26 //6th Row
                iMaxRowQuantity = 30
                xCoord = 115.0
                yCoord += 50.0
            BREAK
        ENDSWITCH
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

get_unlock_code_suit:
    SWITCH idTexture
        CASE 1
            iTempVar = 9999
            BREAK
        CASE 2
            iTempVar = 1985
            BREAK
        CASE 3
            iTempVar = 3564
            BREAK
        CASE 4
            iTempVar = 4465
            BREAK
        CASE 5
            iTempVar = 7952
            BREAK
        CASE 6
            iTempVar = 8431
            BREAK
        CASE 7
            iTempVar = 5761
            BREAK 
        CASE 8
            iTempVar = 9999
            BREAK
        CASE 9
            iTempVar = 6784
            BREAK
        CASE 10
            iTempVar = 3897
            BREAK
        CASE 11
            iTempVar = 4837
            BREAK
        CASE 12
            iTempVar = 7913
            BREAK
        CASE 13
            iTempVar = 1937
            BREAK
        CASE 14
            iTempVar = 8319
            BREAK
        CASE 15
            iTempVar = 6743
            BREAK
        CASE 16
            iTempVar = 4627
            BREAK
        CASE 17
            iTempVar = 9999
            BREAK
        CASE 18
            iTempVar = 7147
            BREAK
        CASE 19
            iTempVar = 9636
            BREAK
        CASE 20
            iTempVar = 9999
            BREAK
        CASE 21
            iTempVar = 8525
            BREAK
        CASE 22
            iTempVar = 7898
            BREAK
        CASE 23
            iTempVar = 1232
            BREAK
        CASE 24
            iTempVar = 7319
            BREAK
        CASE 25
            iTempVar = 6731
            BREAK
        CASE 26
            iTempVar = 1973
            BREAK
        CASE 27
            iTempVar = 2846
            BREAK
        CASE 28
            iTempVar = 1917
            BREAK
        CASE 29
            iTempVar = 3734
            BREAK
        CASE 30
            iTempVar = 4913
            BREAK
    ENDSWITCH
    CLEO_CALL getSuitInfoUnclock 0 idTexture (pUnlockCode)
RETURN

DrawCheckMarks_SUITS:
    //GET_FIXED_XY_ASPECT_RATIO (12.0 12.0) (xSize ySize) //10.0 12.0
    xSize = 9.00
    ySize = 11.20
    /// CHECK_MARk
    GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
    READ_MEMORY (iTempVar) 4 FALSE (iTempVar)
    iMaxRowQuantity = 5
    xCoord = 134.0
    yCoord = 134.5
    counter = 1
    WHILE iMaxRowQuantity >= counter
        IF iTempVar = counter
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE successIcon28 (xCoord yCoord) (xSize ySize) (255 255 255 255)
        ENDIF
        counter ++
        xCoord += 50.0
        SWITCH counter
            CASE 6  //2nd Row
                iMaxRowQuantity = 10
                xCoord = 134.0
                yCoord += 50.0
            BREAK
            CASE 11 //3rd Row
                iMaxRowQuantity = 15
                xCoord = 134.0
                yCoord += 50.0
            BREAK
            CASE 16 //4th Row
                iMaxRowQuantity = 20
                xCoord = 134.0
                yCoord += 50.0
            BREAK
            CASE 21 //5th Row
                iMaxRowQuantity = 25
                xCoord = 134.0
                yCoord += 50.0
            BREAK
            CASE 26 //6th Row
                iMaxRowQuantity = 30
                xCoord = 134.0
                yCoord += 50.0
            BREAK
        ENDSWITCH
    ENDWHILE
RETURN

DrawSelector_SUITS:
    //GET_FIXED_XY_ASPECT_RATIO (65.0 65.0) (xSize ySize) //50.0 45.0
    xSize = 48.75
    ySize = 60.67
    USE_TEXT_COMMANDS FALSE
    SWITCH iActiveRow  //Vertical movement
        CASE 1
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320 
                CASE 1
                    iSelectedSuit = 1
                    DRAW_SPRITE selectSuit26 (115.0 120.0) (xSize ySize) (255 255 255 255)        //Image SQUARE selector
                    BREAK
                CASE 2
                    iSelectedSuit = 2
                    DRAW_SPRITE selectSuit26 (165.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 3
                    DRAW_SPRITE selectSuit26 (215.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 4
                    DRAW_SPRITE selectSuit26 (265.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 5
                    DRAW_SPRITE selectSuit26 (315.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 2
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320           
                CASE 1
                    iSelectedSuit = 6
                    DRAW_SPRITE selectSuit26 (115.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 7
                    DRAW_SPRITE selectSuit26 (165.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 8
                    DRAW_SPRITE selectSuit26 (215.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 9
                    DRAW_SPRITE selectSuit26 (265.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 10
                    DRAW_SPRITE selectSuit26 (315.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 3
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320
                CASE 1
                    iSelectedSuit = 11
                    DRAW_SPRITE selectSuit26 (115.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 12
                    DRAW_SPRITE selectSuit26 (165.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 13
                    DRAW_SPRITE selectSuit26 (215.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 14
                    DRAW_SPRITE selectSuit26 (265.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 15
                    DRAW_SPRITE selectSuit26 (315.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 4
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320 
                CASE 1
                    iSelectedSuit = 16
                    DRAW_SPRITE selectSuit26 (115.0 270.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 17
                    DRAW_SPRITE selectSuit26 (165.0 270.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 18
                    DRAW_SPRITE selectSuit26 (215.0 270.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 19
                    DRAW_SPRITE selectSuit26 (265.0 270.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 20
                    DRAW_SPRITE selectSuit26 (315.0 270.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 5 
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320
                CASE 1
                    iSelectedSuit = 21
                    DRAW_SPRITE selectSuit26 (115.0 320.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 22
                    DRAW_SPRITE selectSuit26 (165.0 320.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 23
                    DRAW_SPRITE selectSuit26 (215.0 320.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 24
                    DRAW_SPRITE selectSuit26 (265.0 320.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 25
                    DRAW_SPRITE selectSuit26 (315.0 320.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 6 
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 115 - 165 - 215 - 265 - 315
                //y->|| 100 - 155 - 210 - 265 - 320
                //y->|| 120 - 170 - 220 - 270 - 320
                CASE 1
                    iSelectedSuit = 26
                    DRAW_SPRITE selectSuit26 (115.0 370.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 27
                    DRAW_SPRITE selectSuit26 (165.0 370.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 28
                    DRAW_SPRITE selectSuit26 (215.0 370.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 29
                    DRAW_SPRITE selectSuit26 (265.0 370.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 30
                    DRAW_SPRITE selectSuit26 (315.0 370.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
    ENDSWITCH
RETURN

DrawInfo_SUITS:
    counter = 1
    WHILE 30 >= counter
        IF iSelectedSuit = counter
            SWITCH iSelectedSuit
                CASE 1
                    iTempVar = 9999
                    idTexture = idSuit1_l
                    BREAK
                CASE 2
                    iTempVar = 1985
                    idTexture = idSuit2_l
                    BREAK
                CASE 3
                    iTempVar = 3564
                    idTexture = idSuit3_l
                    BREAK
                CASE 4
                    iTempVar = 4465
                    idTexture = idSuit4_l
                    BREAK
                CASE 5
                    iTempVar = 7952
                    idTexture = idSuit5_l
                    BREAK
                CASE 6
                    iTempVar = 8431
                    idTexture = idSuit6_l
                    BREAK
                CASE 7
                    iTempVar = 5761
                    idTexture = idSuit7_l
                    BREAK 
                CASE 8
                    iTempVar = 9999
                    idTexture = idSuit8_l
                    BREAK
                CASE 9
                    iTempVar = 6784
                    idTexture = idSuit9_l
                    BREAK
                CASE 10
                    iTempVar = 3897
                    idTexture = idSuit10_l
                    BREAK
                CASE 11
                    iTempVar = 4837
                    idTexture = idSuit11_l
                    BREAK
                CASE 12
                    iTempVar = 7913
                    idTexture = idSuit12_l
                    BREAK
                CASE 13
                    iTempVar = 1937
                    idTexture = idSuit13_l
                    BREAK
                CASE 14
                    iTempVar = 8319
                    idTexture = idSuit14_l
                    BREAK
                CASE 15
                    iTempVar = 6743
                    idTexture = idSuit15_l
                    BREAK
                CASE 16
                    iTempVar = 4627
                    idTexture = idSuit16_l
                    BREAK
                CASE 17
                    iTempVar = 9999
                    idTexture = idSuit17_l
                    BREAK
                CASE 18
                    iTempVar = 7147
                    idTexture = idSuit18_l
                    BREAK
                CASE 19
                    iTempVar = 9636
                    idTexture = idSuit19_l
                    BREAK
                CASE 20
                    iTempVar = 9999
                    idTexture = idSuit20_l
                    BREAK
                CASE 21
                    iTempVar = 8525
                    idTexture = idSuit21_l
                    BREAK
                CASE 22
                    iTempVar = 7898
                    idTexture = idSuit22_l
                    BREAK
                CASE 23
                    iTempVar = 1232
                    idTexture = idSuit23_l
                    BREAK
                CASE 24
                    iTempVar = 7319
                    idTexture = idSuit24_l
                    BREAK
                CASE 25
                    iTempVar = 6731
                    idTexture = idSuit25_l
                    BREAK
                CASE 26
                    iTempVar = 1973
                    idTexture = idSuit26_l
                    BREAK
                CASE 27
                    iTempVar = 2846
                    idTexture = idSuit27_l
                    BREAK
                CASE 28
                    iTempVar = 1917
                    idTexture = idSuit28_l
                    BREAK
                CASE 29
                    iTempVar = 3734
                    idTexture = idSuit29_l
                    BREAK
                CASE 30
                    iTempVar = 4913
                    idTexture = idSuit30_l
                    BREAK
            ENDSWITCH
            //GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize) //40.0 35.0
            xSize = 41.25
            ySize = 51.33
            CLEO_CALL getSuitInfoUnclock 0 counter (pUnlockCode)
            IF pUnlockCode = iTempVar
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 63.75) (85.0 17.5) (31 181 240 0) (0.5) (0 0 1 0) (0 125 180 150) idTexture 6 (-42.5 0.0)   //NAME_SUIT (25-49)
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE iSelectedSuit (530.0 72.5) (xSize ySize) (255 255 255 255)    //IMAGE_SUIT-RIGHT_PANEL  
                //DRAW_SUIT_OWNED_EQUIPED (11,14)
                GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
                READ_MEMORY (iTempVar) 4 FALSE (iTempVar)
                IF iTempVar = iSelectedSuit
                    iTempVar = idEquipped_l
                ELSE
                    iTempVar = idOwned_l
                ENDIF
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 37.5) (136.0 15.0) (6 253 244 200) (0.5) (0 0 0 0) (0 125 180 150) iTempVar 5 (-60.0 0.0)   //TEXT_UPPER_WITH_BLUE_BACKGROUND
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 97.5) (55.0 10.0) (16 43 52 200) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 6 (0.0 0.0)   //TEXT_LOWER

                GOSUB renderChar
            ELSE
                idTexture = idSuitUnknown_l
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 63.75) (85.0 17.5) (31 181 240 0) (0.5) (0 0 1 0) (0 125 180 150) idTexture 6 (-42.5 0.0)   //???????
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE unknownSuit29 (530.0 72.5) (xSize ySize) (255 255 255 255)    //IMAGE_SUIT-RIGHT_PANEL    
                iTempVar = idLocked_l   //LOCKED
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 37.5) (136.0 15.0) (6 253 244 200) (0.5) (0 0 0 0) (0 125 180 150) iTempVar 5 (-60.0 0.0)   //TEXT_UPPER_WITH_BLUE_BACKGROUND
                CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 97.5) (55.0 10.0) (16 43 52 200) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 6 (0.0 0.0)   //TEXT_LOWER
                //DRAW BLACK CHARACTER
                CLEO_CALL setActorBrightness 0 iBaseActor 0.0
            ENDIF
            BREAK
        ENDIF
        counter ++
    ENDWHILE

RETURN
///---------------------------------------------------------------------

       
/*
        LOAD_SPRITE pow_background "pow_backg"
        CONST_INT pow71 71
        CONST_INT pow72 72
        CONST_INT pow73 73
        CONST_INT pow74 74
        CONST_INT pow75 75
        CONST_INT pow76 76
        CONST_INT pow77 77
        CONST_INT pow78 78
        CONST_INT pow79 79
        CONST_INT pow80 80
        CONST_INT pow81 81
        CONST_INT pow82 82
*/
//---+---------------- SUB-MENU - POWER SUITS MATRIX 5X2 +2--------------------
ProcessGame_and_DrawItems_POWER_SUITS:
    //matrix 5x2=10 + 2
    //POWER MENU
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 160.0) (260.0 190.0) (2 40 49 150) (0.5) (1 1 1 1) (0 125 180 200) -1 -1 (0.0 0.0)   //BLUE_BACK_MAIN_POWER_SUITS
    iTempVar = idSuitPower_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (215.0 80.0) (250.0 15.0) (6 253 244 0) (0.5) (0 0 1 0) (6 253 244 200) iTempVar 2 (-110.0 0.0)   //TITLE_POWER_SUIT
    //----lines side - left
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (81.25 80.0) (7.5 15.0) (6 253 244 0) (0.25) (0 0 1 0) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (SUIT-)
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (77.5 87.5) (2.5 2.5) (6 253 244 200)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (73.75 145.0) (7.5 15.0) (6 253 244 0) (0.25) (0 0 1 0) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (POWER-)
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (77.5 152.5) (2.5 2.5) (6 253 244 200)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (85.0 120.0) (15.0 65.0) (6 253 244 0) (0.25) (0 0 0 1) (6 253 244 200) -1 -1 (0.0 0.0)   //LINE_SIDE_LEFT (VERTICAL+UNION)
    //---------
    //GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize) //40.0 35.0
    xSize = 41.25
    ySize = 51.33
    //1st Row
    iMaxRowQuantity = 75
    idTexture = 71
    xCoord = 115.0
    yCoord = 120.0
    WHILE iMaxRowQuantity >= idTexture
        USE_TEXT_COMMANDS FALSE
        DRAW_SPRITE pow_background (xCoord yCoord) (xSize ySize) (255 255 255 200)
        GET_LABEL_POINTER GUI_Memory_PowerSuitItem (iTempVar)
        READ_MEMORY (iTempVar) 4 FALSE (iSelectedPower)
        GOSUB get_power_id_by_selected_item
        IF iTempVar = idTexture
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE idTexture (xCoord yCoord) (xSize ySize) (2 254 252 255)
        ELSE
            IF idTexture = 80   //Iron Arms Power (Temporary locked, isn't ready)
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idTexture (xCoord yCoord) (xSize ySize) (255 255 255 200)
            ELSE
                USE_TEXT_COMMANDS FALSE
                DRAW_SPRITE idTexture (xCoord yCoord) (xSize ySize) (41 190 240 210)
            ENDIF
        ENDIF
        idTexture ++
        xCoord += 50.0
        //115 - 165 - 215 - 265 - 315
        SWITCH idTexture
            CASE 76  //2nd Row
                iMaxRowQuantity = 80
                xCoord = 115.0
                yCoord += 50.0
            BREAK
            CASE 81 //3rd Row
                iMaxRowQuantity = 82
                xCoord = 115.0
                yCoord += 50.0
            BREAK
        ENDSWITCH
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

get_power_id_by_selected_item:
    SWITCH iSelectedPower
        CASE 1
            iTempVar = 71
            BREAK
        CASE 2
            iTempVar = 72
            BREAK
        CASE 3
            iTempVar = 73
            BREAK
        CASE 4
            iTempVar = 74
            BREAK
        CASE 5
            iTempVar = 75
            BREAK
        CASE 6
            iTempVar = 76
            BREAK
        CASE 7
            iTempVar = 77
            BREAK 
        CASE 8
            iTempVar = 78
            BREAK
        CASE 9
            iTempVar = 79
            BREAK
        CASE 10
            iTempVar = 80
            BREAK
        CASE 11
            iTempVar = 81
            BREAK
        CASE 12
            iTempVar = 82
            BREAK
        DEFAULT
            iTempVar = pow_null
            BREAK
    ENDSWITCH
RETURN

DrawSelector_POWER_SUITS:
    //GET_FIXED_XY_ASPECT_RATIO (65.0 65.0) (xSize ySize) //50.0 45.0
    xSize = 48.75
    ySize = 60.67
    USE_TEXT_COMMANDS FALSE
    SWITCH iActiveRow  //Vertical movement
        CASE 1
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    iSelectedPower = 1
                    DRAW_SPRITE selectSuit26 (115.0 120.0) (xSize ySize) (255 255 255 255)        //Image SQUARE selector
                    BREAK
                CASE 2
                    iSelectedPower = 2
                    DRAW_SPRITE selectSuit26 (165.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedPower = 3
                    DRAW_SPRITE selectSuit26 (215.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedPower = 4
                    DRAW_SPRITE selectSuit26 (265.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedPower = 5
                    DRAW_SPRITE selectSuit26 (315.0 120.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 2
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    iSelectedPower = 6
                    DRAW_SPRITE selectSuit26 (115.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedPower = 7
                    DRAW_SPRITE selectSuit26 (165.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedPower = 8
                    DRAW_SPRITE selectSuit26 (215.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedPower = 9
                    DRAW_SPRITE selectSuit26 (265.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedPower = 10
                    DRAW_SPRITE selectSuit26 (315.0 170.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 3
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    iSelectedPower = 11
                    DRAW_SPRITE selectSuit26 (115.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedPower = 12
                    DRAW_SPRITE selectSuit26 (165.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedPower = 13
                    DRAW_SPRITE selectSuit26 (215.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedPower = 14
                    DRAW_SPRITE selectSuit26 (265.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedPower = 15
                    DRAW_SPRITE selectSuit26 (315.0 220.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
    ENDSWITCH
RETURN

DrawCheckMarks_POWER_SUITS:
    //GET_FIXED_XY_ASPECT_RATIO (12.0 12.0) (xSize ySize) //10.0 12.0
    xSize = 9.00
    ySize = 11.20    
    /// CHECK_MARk
    GET_LABEL_POINTER GUI_Memory_PowerSuitItem (iTempVar)
    READ_MEMORY (iTempVar) 4 FALSE (iTempVar)
    iMaxRowQuantity = 5
    xCoord = 134.0
    yCoord = 134.5
    counter = 1
    WHILE iMaxRowQuantity >= counter
        IF iTempVar = counter
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE successIcon28 (xCoord yCoord) (xSize ySize) (255 255 255 255)
        ENDIF
        counter ++
        xCoord += 50.0
        SWITCH counter
            CASE 6  //2nd Row
                iMaxRowQuantity = 10
                xCoord = 134.0
                yCoord += 50.0
            BREAK
            CASE 11 //3rd Row
                iMaxRowQuantity = 15
                xCoord = 134.0
                yCoord += 50.0
            BREAK
        ENDSWITCH
    ENDWHILE
RETURN

DrawInfo_POWER_SUITS_RightPanel:
    // DRAW IMAGE HELPER
    //GET_FIXED_XY_ASPECT_RATIO (181.0 181.0) (xSize ySize)
    xSize = 135.75
    ySize = 168.93
    yCoord = 78.5 //114.5
    GOSUB get_texture_helper_id_by_selected_power
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idTexture (572.0 yCoord) (xSize ySize) (255 255 255 255)

    //DRAW POWER SUIT IMAGE
    //GET_FIXED_XY_ASPECT_RATIO (55.0 50.0) (xSize ySize)
    xSize = 41.25
    ySize = 46.67
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE pow_background (530.0 147.5) (xSize ySize) (255 255 255 200)    //IMAGE_SUIT-RIGHT_PANEL-BACKGROUND
    GOSUB get_power_id_by_selected_item

    //GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize)
    xSize = 41.25
    ySize = 51.33
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE iTempVar (530.0 147.5) (xSize ySize) (255 255 255 255)    //IMAGE_POWER_SUIT-RIGHT_PANEL  
    
    //DRAW POWER SUIT NAME
    GOSUB get_text_id_by_selected_power
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 138.75) (85.0 17.5) (31 181 240 0) (0.5) (0 0 1 0) (0 125 180 150) iTempVar 6 (-42.5 0.0)   //NAME_SUIT (25-49)
    //Text Description
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 175.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) counter 7 (0.0 0.0)   //Power Description
    IF IS_PC_USING_JOYPAD
        iTempVar = idPower_KeyPress //Press ~r~~k~~PED_DUCK~ ~s~+ ~r~~k~~PED_LOOKBEHIND~ ~s~to activate
    ELSE
        iTempVar = 451  //Press ~r~C ~s~+ ~r~1 ~s~to activate
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 200.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 7 (0.0 0.0)   //Power KEY_PRESS
RETURN

DrawInfo_SUITS_RightPanel_PowerSuits:
    //iTempVar = idNoPowerText_l
    iTempVar = idUnlocksPowerText_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 227.0) (136.0 30.0) (18 49 58 140) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 2 (-60.0 -3.0)   //THIS SUIT ALSO UNLOCKS

    //DRAW NAME SUIT
    GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
    READ_MEMORY (iTempVar) 4 FALSE (iSelectedSuit)
    GOSUB get_text_name_id_from_selected_SUIT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 250.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) idTexture 5 (-60.0 0.0)   //NAME_SUIT_WITH_BLUE_BACKGROUND
    //--side lines
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (535.75 287.5) (63.5 17.5) (16 43 52 0) (0.25) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_LEFT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (633.75 287.5) (12.5 17.5) (16 43 52 0) (0.25) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_RIGHT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 280.0) (60.0 17.5) (16 43 52 0) (0.25) (1 1 1 1) (31 181 240 200) -1 -1 (0.0 0.0)   //SIDES_LINES
    //---
    //DRAW IMAGE SUIT
    //GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize) //40.0 35.0
    xSize = 41.25
    ySize = 51.33
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE iSelectedSuit (530.0 280.0) (xSize ySize) (255 255 255 255)    //IMAGE_SUIT-RIGHT_PANEL  

    iTempVar = idEquipped_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 280.0) (55.0 12.5) (16 43 52 200) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 6 (0.0 0.0)   //TEXT_LOWER
RETURN

get_texture_helper_id_by_selected_power:
   SWITCH iSelectedPower
        CASE 1
            idTexture = 321
            BREAK
        CASE 2
            idTexture = 322
            BREAK
        CASE 3
            idTexture = 323
            BREAK
        CASE 4
            idTexture = 324
            BREAK
        CASE 5
            idTexture = 325
            BREAK
        CASE 6
            idTexture = 326
            BREAK
        CASE 7
            idTexture = 327
            BREAK 
        CASE 8
            idTexture = 328
            BREAK
        CASE 9
            idTexture = 329
            BREAK
        CASE 10
            idTexture = 330
            BREAK
        CASE 11
            idTexture = 331
            BREAK
        CASE 12
            idTexture = 332
            BREAK
        DEFAULT
            idTexture = 320
            BREAK
    ENDSWITCH
RETURN

get_text_id_by_selected_power:
   SWITCH iSelectedPower
         CASE 1
            iTempVar = idPower1_l
            counter = 421
            BREAK
        CASE 2
            iTempVar = idPower2_l
            counter = 422
            BREAK
        CASE 3
            iTempVar = idPower3_l
            counter = 423
            BREAK
        CASE 4
            iTempVar = idPower4_l
            counter = 424
            BREAK
        CASE 5
            iTempVar = idPower5_l
            counter = 425
            BREAK
        CASE 6
            iTempVar = idPower6_l
            counter = 426
            BREAK
        CASE 7
            iTempVar = idPower7_l
            counter = 427
            BREAK 
        CASE 8
            iTempVar = idPower8_l
            counter = 428
            BREAK
        CASE 9
            iTempVar = idPower9_l
            counter = 429
            BREAK
        CASE 10
            iTempVar = idPower10_l
            counter = 430
            BREAK
        CASE 11
            iTempVar = idPower11_l
            counter = 431
            BREAK
        CASE 12
            iTempVar = idPower12_l
            counter = 432
            BREAK
        DEFAULT
            iTempVar = idNoPower_l
            counter = 420
            BREAK
    ENDSWITCH
RETURN

get_text_name_id_from_selected_SUIT:
    SWITCH iSelectedSuit
        CASE 1
            idTexture = idSuit1_l
            BREAK
        CASE 2
            idTexture = idSuit2_l
            BREAK
        CASE 3
            idTexture = idSuit3_l
            BREAK
        CASE 4
            idTexture = idSuit4_l
            BREAK
        CASE 5
            idTexture = idSuit5_l
            BREAK
        CASE 6
            idTexture = idSuit6_l
            BREAK
        CASE 7
            idTexture = idSuit7_l
            BREAK 
        CASE 8
            idTexture = idSuit8_l
            BREAK
        CASE 9
            idTexture = idSuit9_l
            BREAK
        CASE 10
            idTexture = idSuit10_l
            BREAK
        CASE 11
            idTexture = idSuit11_l
            BREAK
        CASE 12
            idTexture = idSuit12_l
            BREAK
        CASE 13
            idTexture = idSuit13_l
            BREAK
        CASE 14
            idTexture = idSuit14_l
            BREAK
        CASE 15
            idTexture = idSuit15_l
            BREAK
        CASE 16
            idTexture = idSuit16_l
            BREAK
        CASE 17
            idTexture = idSuit17_l
            BREAK
        CASE 18
            idTexture = idSuit18_l
            BREAK
        CASE 19
            idTexture = idSuit19_l
            BREAK
        CASE 20
            idTexture = idSuit20_l
            BREAK
        CASE 21
            idTexture = idSuit21_l
            BREAK
        CASE 22
            idTexture = idSuit22_l
            BREAK
        CASE 23
            idTexture = idSuit23_l
            BREAK
        CASE 24
            idTexture = idSuit24_l
            BREAK
        CASE 25
            idTexture = idSuit25_l
            BREAK
        CASE 26
            idTexture = idSuit26_l
            BREAK
        CASE 27
            idTexture = idSuit27_l
            BREAK
        CASE 28
            idTexture = idSuit28_l
            BREAK
        CASE 29
            idTexture = idSuit29_l
            BREAK
        CASE 30
            idTexture = idSuit30_l
            BREAK
    ENDSWITCH
RETURN

//---+------------------ MAIN MENU - SKILLS -------------------------
ProcessGame_and_DrawItems_SKILLS:
//80-504=424 /3= 141.0 size
//-20-141-20-141-20-141-20
//20+(141/2)=90.5
//251.5
//412.5
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (90.5 220.0) (141.0 290.0) (22 44 55 100) (0.5) (1 1 1 1) (58 158 156 150) -1 -1 (0.0 0.0)     // BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (90.5 90.0) (120.0 30.0) (0 0 0 0) (0.5) (0 0 1 0) (58 158 156 150) 629 13 (0.0 -2.0)     //Innovator

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (251.5 220.0) (141.0 290.0) (22 44 55 100) (0.5) (1 1 1 1) (58 158 156 150) -1 -1 (0.0 0.0)     // BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (251.5 90.0) (120.0 30.0) (0 0 0 0) (0.5) (0 0 1 0) (58 158 156 150) 628 13 (0.0 -2.0)     //Defender
    
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (412.5 220.0) (141.0 290.0) (22 44 55 100) (0.5) (1 1 1 1) (58 158 156 150) -1 -1 (0.0 0.0)     // BACKGROUND
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (412.5 90.0) (120.0 30.0) (0 0 0 0) (0.5) (0 0 1 0) (58 158 156 150) 627 13 (0.0 -2.0)     //Defender

    GET_FIXED_XY_ASPECT_RATIO (40.0 40.0) (xSize ySize)
//Skill Tree # 1
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    xCoord = 90.5
    yCoord = 150.0
    GET_CLEO_SHARED_VAR varSkill1 (iTempVar)
    IF iTempVar = 1 // 0:OFF || 1:ON
        DRAW_SPRITE idSkill1 (xCoord yCoord) (xSize ySize) (41 190 240 245)
    ELSE
        DRAW_SPRITE idSkill1 (xCoord yCoord) (xSize ySize) (170 170 170 200)
    ENDIF

//Skill Tree # 2
    xCoord = 251.5
    yCoord = 175.0
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (xCoord yCoord) (0.25 20.0) (255 255 255 180)

    xCoord = 251.5
    yCoord = 150.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    GET_CLEO_SHARED_VAR varSkill2 (iTempVar)
    IF iTempVar = 1 // 0:OFF || 1:ON
        DRAW_SPRITE idSkill2 (xCoord yCoord) (xSize ySize) (41 190 240 245)
    ELSE
        DRAW_SPRITE idSkill2 (xCoord yCoord) (xSize ySize) (170 170 170 200)
    ENDIF
    //-->
        yCoord += 50.0
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        GET_CLEO_SHARED_VAR varSkill2a (iTempVar)
        IF iTempVar = 1 // 0:OFF || 1:ON
            DRAW_SPRITE idSkill2a (xCoord yCoord) (xSize ySize) (41 190 240 245)
        ELSE
            DRAW_SPRITE idSkill2a (xCoord yCoord) (xSize ySize) (170 170 170 200)
        ENDIF

//Skill Tree # 3
    xCoord = 412.5
    yCoord = 175.0
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (xCoord yCoord) (0.25 20.0) (255 255 255 180) //center
    xCoord = 412.5
    yCoord = 175.0
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (xCoord yCoord) (73.0 0.25) (255 255 255 180) //horizontal
   
    xCoord = 376.0
    yCoord = 180.0
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (xCoord yCoord) (0.25 10.0) (255 255 255 180) //left
    xCoord = 449.0
    yCoord = 180.0
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (xCoord yCoord) (0.25 10.0) (255 255 255 180) //right

    xCoord = 412.5
    yCoord = 150.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    GET_CLEO_SHARED_VAR varSkill3 (iTempVar)
    IF iTempVar = 1 // 0:OFF || 1:ON
        DRAW_SPRITE idSkill3 (xCoord yCoord) (xSize ySize) (41 190 240 245)
    ELSE
        DRAW_SPRITE idSkill3 (xCoord yCoord) (xSize ySize) (170 170 170 200)
    ENDIF
    //-->
        xCoord = 376.0
        yCoord = 200.0
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        GET_CLEO_SHARED_VAR varSkill3a (iTempVar)
        IF iTempVar = 1 // 0:OFF || 1:ON
            DRAW_SPRITE idSkill3a (xCoord yCoord) (xSize ySize) (41 190 240 245)
        ELSE
            DRAW_SPRITE idSkill3a (xCoord yCoord) (xSize ySize) (170 170 170 200)
        ENDIF

        xCoord = 412.5
        yCoord = 200.0
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        GET_CLEO_SHARED_VAR varSkill3b (iTempVar)
        IF iTempVar = 1 // 0:OFF || 1:ON
            DRAW_SPRITE idSkill3b (xCoord yCoord) (xSize ySize) (41 190 240 245)
        ELSE
            DRAW_SPRITE idSkill3b (xCoord yCoord) (xSize ySize) (170 170 170 200)
        ENDIF

        xCoord = 449.0
        yCoord = 225.0
        USE_TEXT_COMMANDS FALSE
        DRAW_RECT (xCoord yCoord) (0.25 20.0) (255 255 255 180) //right1
        xCoord = 449.0
        yCoord = 275.0
        USE_TEXT_COMMANDS FALSE
        DRAW_RECT (xCoord yCoord) (0.25 20.0) (255 255 255 180) //right2

        xCoord = 449.0
        yCoord = 200.0
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        GET_CLEO_SHARED_VAR varSkill3c (iTempVar)
        IF iTempVar = 1 // 0:OFF || 1:ON
            DRAW_SPRITE idSkill3c (xCoord yCoord) (xSize ySize) (41 190 240 245)
        ELSE
            DRAW_SPRITE idSkill3c (xCoord yCoord) (xSize ySize) (170 170 170 200)
        ENDIF
        //-->
            //xCoord = 340.0
            //yCoord = 180.0
            yCoord += 50.0
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            GET_CLEO_SHARED_VAR varSkill3c1 (iTempVar)
            IF iTempVar = 1 // 0:OFF || 1:ON
                DRAW_SPRITE idSkill3c1 (xCoord yCoord) (xSize ySize) (41 190 240 245)
            ELSE
                DRAW_SPRITE idSkill3c1 (xCoord yCoord) (xSize ySize) (170 170 170 200)
            ENDIF

            //xCoord = 340.0
            //yCoord = 220.0
            yCoord += 50.0
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            GET_CLEO_SHARED_VAR varSkill3c2 (iTempVar)
            IF iTempVar = 1 // 0:OFF || 1:ON
                DRAW_SPRITE idSkill3c2 (xCoord yCoord) (xSize ySize) (41 190 240 245)
            ELSE
                DRAW_SPRITE idSkill3c2 (xCoord yCoord) (xSize ySize) (170 170 170 200)
            ENDIF

RETURN

DrawSelector_SKILLS:
    CONST_INT skill_column_a 1
    CONST_INT skill_column_b 2
    CONST_INT skill_column_c 3
    CONST_INT skill_column_d 4
    CONST_INT skill_column_e 5
    GET_FIXED_XY_ASPECT_RATIO (40.0 55.0) (xSize ySize)
    USE_TEXT_COMMANDS FALSE

    SWITCH iActiveCol  //Horizontal movement
        CASE skill_column_a
            SWITCH iActiveRow   //Horizontal movement
                CASE 1
                    counter = 1   // Recicled var 
                    xCoord = 90.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
            BREAK
        CASE skill_column_b
            SWITCH iActiveRow   //Horizontal movement
                CASE 1
                    counter = 2 
                    xCoord = 251.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)      
                    BREAK
                CASE 2
                    counter = 4 
                    //yCoord = 150.0 + 50.0
                    xCoord = 251.5
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
            BREAK
        CASE skill_column_c
            SWITCH iActiveRow   //Horizontal movement
                CASE 1
                    counter = 5 
                    xCoord = 376.0
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
            BREAK
        CASE skill_column_d
            SWITCH iActiveRow   //Horizontal movement
                CASE 1
                    counter = 3 
                    xCoord = 412.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 2
                    counter = 6 
                    xCoord = 412.5
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
            BREAK
        CASE skill_column_e
            SWITCH iActiveRow   //Horizontal movement
                CASE 1
                    counter = 7
                    xCoord = 449.0
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 2
                    counter = 8 
                    xCoord = 449.0
                    yCoord = 250.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 3
                    counter = 9 
                    xCoord = 449.0
                    yCoord = 300.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
            BREAK
    ENDSWITCH



    /*
    CONST_INT skill_row_a 1
    CONST_INT skill_row_b 2
    CONST_INT skill_row_c 3
    CONST_INT skill_row_d 4    
    SWITCH iActiveRow  //Vertical movement
        CASE skill_row_a
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    counter = 1   // Recicled var 
                    xCoord = 90.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)        //Image SQUARE selector
                    BREAK
                CASE 2
                    counter = 2 
                    xCoord = 251.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 3
                    counter = 3 
                    xCoord = 412.5
                    yCoord = 150.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
        BREAK
        CASE skill_row_b
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    counter = 4 
                    //yCoord = 150.0 + 50.0
                    xCoord = 251.5
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 2
                    counter = 5 
                    xCoord = 376.0
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 3
                    counter = 6 
                    xCoord = 412.5
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
                CASE 4
                    counter = 7
                    xCoord = 449.0
                    yCoord = 200.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
        BREAK
        CASE skill_row_c
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    counter = 8 
                    xCoord = 449.0
                    yCoord = 250.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
        BREAK
        CASE skill_row_d
            SWITCH iActiveCol   //Horizontal movement
                CASE 1
                    counter = 9 
                    xCoord = 449.0
                    yCoord = 300.0
                    DRAW_SPRITE selectSuit26 (xCoord yCoord) (xSize ySize) (255 255 255 255)
                    BREAK
            ENDSWITCH
        BREAK
    ENDSWITCH
    */
    CLEO_CALL storeSkillItem 0 counter
RETURN

ProcessGame_and_DrawMenu_RightPanel_SKILLS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 229.0) (136.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND
    GOSUB DrawInfo_SKILLS_RightPanel
    //CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 398.0) (120.0 40.0) (16 43 52 200) (0.5) (1 1 1 1) (6 253 244 200) -1 -1 (0.0 0.0)   //BLUE_ATT_DEF
    USE_TEXT_COMMANDS FALSE
RETURN

DrawInfo_SKILLS_RightPanel:
    CLEO_CALL getSkillItem 0 (counter)
    SWITCH counter
        CASE 1
            iTempVar = idSkill1     //Skill image
            idTexture = idhsk01     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 630 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 631 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 2
            iTempVar = idSkill2     //Skill image
            idTexture = idhsk02     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 632 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 633 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 3
            iTempVar = idSkill3     //Skill image
            idTexture = idhsk03     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 636 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 637 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 4
            iTempVar = idSkill2a     //Skill image
            idTexture = idhsk02a     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 634 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 635 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 5
            iTempVar = idSkill3a     //Skill image
            idTexture = idhsk03a     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 638 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 639 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 6
            iTempVar = idSkill3b     //Skill image
            idTexture = idhsk03b     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 640 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 641 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 7
            iTempVar = idSkill3c     //Skill image
            idTexture = idhsk03c     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 642 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 643 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 8
            iTempVar = idSkill3c1     //Skill image
            idTexture = idhsk03c1     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 644 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 645 7 (0.0 0.0)   //DESCRIPTION
            BREAK
        CASE 9
            iTempVar = idSkill3c2     //Skill image
            idTexture = idhsk03c2     //Helper image
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (572.0 133.0) (136.0 15.0) (42 190 240 200) (0.5) (0 0 0 0) (0 125 180 150) 646 5 (-60.0 0.0)   //NAME_SKILL_WITH_BLUE_BACKGROUND
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (594.0 168.0) (136.0 30.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) 647 7 (0.0 0.0)   //DESCRIPTION
            BREAK
    ENDSWITCH
    // DRAW IMAGE HELPER
    //GET_FIXED_XY_ASPECT_RATIO (181.0 181.0) (xSize ySize)
    xSize = 135.75
    ySize = 168.93
    yCoord = 78.5 //114.5
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idTexture (572.0 yCoord) (xSize ySize) (255 255 255 255)

    //DRAW SKILL IMAGE
    GET_FIXED_XY_ASPECT_RATIO (55.0 55.0) (xSize ySize)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE iTempVar (530.0 175.0) (xSize ySize) (103 177 222 220)    //IMAGE_SKILLS-RIGHT_PANEL  

    //--side lines
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (595.5 165.0) (80.0 17.5) (16 43 52 0) (0.25) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_RIGHT_UP

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (535.75 214.0) (63.5 17.5) (16 43 52 0) (0.25) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_LEFT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (633.75 214.0) (12.5 17.5) (16 43 52 0) (0.25) (1 0 0 0) (31 181 240 200) -1 -1 (0.0 0.0)   //LINE_RIGHT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 207.0) (60.0 17.5) (16 43 52 0) (0.25) (1 1 1 1) (31 181 240 200) -1 -1 (0.0 0.0)   //SIDES_LINES
    iTempVar = idOwned_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (597.5 207.0) (55.0 12.5) (16 43 52 200) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 6 (0.0 0.0)   //TEXT_LOWER
RETURN

//---+------------------ MAIN MENU - CHARACTERS -------------------------
ProcessGame_and_DrawItems_CHARACTERS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (542.0 229.0) (196.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (-1.0 0.0)   //BLUE_BACKGROUND_RIGHT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (542.0 229.0) (196.0 50.0) (14 37 48 200) (0.5) (1 0 1 0) (19 247 232 200) -1 -1 (0.0 0.0)   //BACK_BLUE_FOR_NAMES_WITH_SIDES
    //matrix 5x2=10
    CONST_INT idMinRowCharacterQuantity 40  // Min id Texture
    //Draw image spiderman
    //GET_FIXED_XY_ASPECT_RATIO (37.34 37.5) (xSize ySize) //28.0 35.0
    xSize = 28.00
    ySize = 35.00
    //1st Row
    iMaxRowQuantity = 44    // Max id Texture
    idTexture = idMinRowCharacterQuantity
    xCoord = 478.0
    yCoord = 90.0    
    WHILE iMaxRowQuantity >= idTexture
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        DRAW_SPRITE idTexture (xCoord yCoord) (xSize ySize) (255 255 255 255)
        idTexture += 1
        xCoord += 34.0
        //498 - 528 - 558 - 588 - 618
        SWITCH idTexture
            CASE 45  //2nd Row
                iMaxRowQuantity = 49
                xCoord = 478.0
                yCoord += 42.0
            BREAK
        ENDSWITCH
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

DrawSelector_CHARACTER:
    //GET_FIXED_XY_ASPECT_RATIO (46.67 60.0) (xSize ySize) //35.0 42.0 
    xSize = 35.00
    ySize = 56.00
    SWITCH iActiveRow  //Vertical movement
        CASE 1
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 478 - 512 - 546 - 580 - 614
                //y->|| 90 - 132
                CASE 1
                    iSelectedSuit = 1   // Recicled var 
                    DRAW_SPRITE selectSuit26 (478.0 90.0) (xSize ySize) (255 255 255 255)        //Image SQUARE selector
                    BREAK
                CASE 2
                    iSelectedSuit = 2
                    DRAW_SPRITE selectSuit26 (512.0 90.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 3
                    DRAW_SPRITE selectSuit26 (546.0 90.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 4
                    DRAW_SPRITE selectSuit26 (580.0 90.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 5
                    DRAW_SPRITE selectSuit26 (614.0 90.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
        CASE 2
            SWITCH iActiveCol   //Horizontal movement
                //x->|| 478 - 512 - 546 - 580 - 614
                //y->|| 90 - 132              
                CASE 1
                    iSelectedSuit = 6
                    DRAW_SPRITE selectSuit26 (478.0 132.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 2
                    iSelectedSuit = 7
                    DRAW_SPRITE selectSuit26 (512.0 132.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 3
                    iSelectedSuit = 8
                    DRAW_SPRITE selectSuit26 (546.0 132.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 4
                    iSelectedSuit = 9
                    DRAW_SPRITE selectSuit26 (580.0 132.0) (xSize ySize) (255 255 255 255)
                BREAK
                CASE 5
                    iSelectedSuit = 10
                    DRAW_SPRITE selectSuit26 (614.0 132.0) (xSize ySize) (255 255 255 255)
                BREAK
            ENDSWITCH
        BREAK
    ENDSWITCH
    USE_TEXT_COMMANDS FALSE
RETURN

/*
CONST_INT tMenuChar1    40  //First row
CONST_INT tMenuChar6    45 // Second Row
*/

DrawInfo_CHARACTERS:
    SWITCH iSelectedSuit
        CASE 1
            iTempVar = 181  //Peter Parker / Spider-Man
            idTexture = tMenuCharB1
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 182 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 2
            iTempVar = 183 // Mary Jane Watson
            idTexture = tMenuCharB2
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 184 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 3
            iTempVar = 185 // Wilson Fisk / Kingpin
            idTexture = tMenuCharB3
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 186 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 4
            iTempVar = 187 // Herman Schultz / Shocker
            idTexture = tMenuCharB4
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 188 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 5
            iTempVar = 189 // Miles Morales
            idTexture = tMenuCharB5
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 190 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 6
            iTempVar = 191 // Silver Sablinova / Silver Sable
            idTexture = tMenuCharB6
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 192 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 7
            iTempVar = 193 // Mac Gargan / Scorpion
            idTexture = tMenuCharB7
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 194 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 8
            iTempVar = 195 // Aleksi Sytsevich / Rhino
            idTexture = tMenuCharB8
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 196 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 9
            iTempVar = 197 // Adrian Toomes / Vulture
            idTexture = tMenuCharB9
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 198 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
        CASE 10
            iTempVar = 199 // Mister Negative
            idTexture = tMenuCharB10
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 310.0) (196.0 100.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 200 12 (-98.0 0.0)   //DESCRIPTION_CHARACTERS
            BREAK
    ENDSWITCH
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (556.0 277.0) (196.0 15.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) 180 2 (-98.0 0.0)   //INFO
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (566.5 285.0) (143.0 15.0) (14 37 48 0) (0.5) (1 0 0 0) (19 247 232 200) -1 -1 (0.0 0.0)   //LINE_UP_SIDE

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (542.0 221.5) (196.0 50.0) (14 37 48 0) (0.5) (0 0 0 0) (0 125 180 150) iTempVar 13 (0.0 0.0)   //NAME_CHARACTERS

    GET_FIXED_XY_ASPECT_RATIO (592.0 592.0) (xSize ySize) //444.0 398.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idTexture (222.0 229.0) (xSize ySize) (255 255 255 220)
RETURN
///---------------------------------------------------------------------


//---+------------------ MAIN MENU - MOVES -------------------------
ProcessGame_and_DrawItems_MOVES_and_CONTROLS:
    SWITCH iPanelB
        DEFAULT //Moves
            GOSUB ProcessGame_and_DrawItems_MOVES
            GOSUB ProcessGame_and_DrawMenu_RightPanel_MOVES
            GOSUB DrawSelector_MOVES
            BREAK
        CASE iPanel_config_moves_primary_controls   // Show Primary Controls
            GOSUB ProcessGame_and_DrawMenu_Primary_Controls
            BREAK
    ENDSWITCH
RETURN

ProcessGame_and_DrawItems_MOVES:
    //id-> 210 - 225 
    CONST_INT iMaxOnFootContols 217
    CONST_INT iMaxOnAirControls 225
    CONST_INT iMinOnFootControls 210
    CONST_INT iMinOnAirControls 218

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (200.0 53.0) (170.0 31.0) (0 0 1 0) (0.25) (0 0 1 0) (0 125 180 200) 209 13 (0.0 0.0)   //ON FOOT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (200.0 227.0) (170.0 31.0) (0 0 1 0) (0.25) (0 0 1 0) (0 125 180 200) 208 13 (0.0 0.0)   //ON AIR

    iColumn = iMaxOnFootContols
    iRow = iMinOnFootControls
    yCoord = 80.0
    xCoord = 127.5
    WHILE iColumn >= iRow   //210->225
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (145.0 18.0) (0 0 0 0) (0.25) (0 0 1 0) (0 125 180 200) iRow 5 (0.0 0.0)   //TEXT
        xCoord += 145.0
        IF IS_PC_USING_JOYPAD
            iRow += 40
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (145.0 18.0) (0 0 0 0) (0.25) (0 0 1 0) (0 125 180 200) iRow 5 (0.0 0.0)   //KEYS
            xCoord -= 145.0
            iRow -= 40
        ELSE
            iRow += 20
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (xCoord yCoord) (145.0 18.0) (0 0 0 0) (0.25) (0 0 1 0) (0 125 180 200) iRow 5 (0.0 0.0)   //KEYS
            xCoord -= 145.0
            iRow -= 20
        ENDIF
        IF iRow = iActiveRow  //Active Item
            CLEO_CALL GUI_DrawBoxOutline_WithText 0 (200.0 yCoord) (290.0 18.0) (225 225 225 190) (0.5) (1 1 1 1) (0 125 180 200) -1 -1 (0.0 0.0)   //WHITE_BACKGROUND
        ENDIF
        iRow += 1
        yCoord += 18.0
        IF iRow = iMinOnAirControls   //2nd Set
            iColumn = iMaxOnAirControls
            yCoord += 30.0
        ENDIF
    ENDWHILE

    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawMenu_RightPanel_MOVES:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 229.0) (240.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_BIG
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 403.0) (240.0 50.0) (16 43 52 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_MEDIUM_DOWN

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 214.0) (240.0 30.0) (8 192 255 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_SMALL_CENTER

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 403.0) (150.0 20.0) (16 43 52 0) (0.5) (0 0 0 0) (0 125 180 150) 119 5 (0.0 0.0)    //Show Primary Controls

    USE_TEXT_COMMANDS FALSE
RETURN

DrawSelector_MOVES:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 214.0) (240.0 30.0) (0 0 0 0) (0.25) (0 0 1 0) (0 125 180 200) iActiveRow 5 (-105.0 0.0)   //TEXT
    SWITCH iActiveRow  //210->225
        CASE 210
            iSelectedSuit = 300   // Recicled var 
            BREAK
        CASE 211
            iSelectedSuit = 301
            BREAK
        CASE 212
            iSelectedSuit = 302
            BREAK
        CASE 213
            iSelectedSuit = 303
            BREAK
        CASE 214
            iSelectedSuit = 304
            BREAK
        CASE 215
            iSelectedSuit = 305
            BREAK
        CASE 216
            iSelectedSuit = 306
            BREAK
        CASE 217
            iSelectedSuit = 307
            BREAK
        CASE 218
            iSelectedSuit = 308
            BREAK
        CASE 219
            iSelectedSuit = 309
            BREAK
        CASE 220
            iSelectedSuit = 310
            BREAK
        CASE 221
            iSelectedSuit = 311
            BREAK
        CASE 222
            iSelectedSuit = 312
            BREAK
        CASE 223
            iSelectedSuit = 313
            BREAK
        CASE 224
            iSelectedSuit = 314
            BREAK
        CASE 225
            iSelectedSuit = 315
            BREAK
    ENDSWITCH
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (720.0 407.4) (xSize ySize)   //sizex=240.0 sizey=169.0
    //240.0 169.0
    xSize = 240.0
    ySize = 169.0
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE iSelectedSuit (520.0 114.5) (xSize ySize) (255 255 255 255)        //IMAGE_IN_GAME
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 278.0) (200.0 30.0) (0 0 0 0) (0.25) (0 0 0 0) (0 125 180 200) iSelectedSuit 12 (0.0 0.0)   //DESCRIPTION
RETURN

ProcessGame_and_DrawMenu_Primary_Controls:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 229.0) (640.0 398.0) (0 37 59 220) (0.5) (0 0 0 0) (0 0 0 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 80.0) (140.0 40.0) (0 0 1 0) (0.3) (0 0 1 0) (0 125 180 200) 118 13 (0.0 0.0)   //PRIMARY CONTROLS
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (230.0 80.0) (30.0 40.0) (0 0 0 0) (0.6) (0 0 1 0) (19 247 232 200) -1 -1 (0.0 0.0)   //LINE_DOWN - Left
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (410.0 80.0) (30.0 40.0) (0 0 0 0) (0.6) (0 0 1 0) (19 247 232 200) -1 -1 (0.0 0.0)   //LINE_DOWN - Right

    GET_FIXED_XY_ASPECT_RATIO (600.0 600.0) (xSize ySize) //444.0 398.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idPrimaryControls (320.0 229.0) (xSize ySize) (255 255 255 220)
RETURN

///---------------------------------------------------------------------

//---+------------------ MAIN MENU - VERSION -------------------------
ProcessGame_and_DrawItems_STATISTICS:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (127.5 65.0) (145.0 20.0) (225 225 225 190) (0.5) (1 1 1 1) (0 125 180 200) 501 5 (0.0 0.0) //MISSIONS
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (272.5 65.0) (145.0 20.0) (225 225 225 190) (0.5) (1 1 1 1) (0 125 180 200) 502 5 (0.0 0.0) //TOTAL PASSED

    yCoord = 85.0
    iRow = 503
    WHILE 508 >= iRow
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (127.5 yCoord) (145.0 20.0) (0 0 0 190) (0.5) (1 0 1 0) (225 225 225 30) iRow 5 (0.0 0.0)   //text
        SWITCH iRow
            CASE 503
                GET_CLEO_SHARED_VAR varCrimesProgress (iTempVar)
                BREAK
            CASE 504
                GET_CLEO_SHARED_VAR varPcampProgress (iTempVar)
                BREAK
            CASE 505
                GET_CLEO_SHARED_VAR varCarChaseProgress (iTempVar)
                BREAK
            CASE 506
                GET_CLEO_SHARED_VAR varScrewBallProgress (iTempVar)
                BREAK
            CASE 507
                GET_CLEO_SHARED_VAR varBackpacksProgress (iTempVar)
                BREAK
            CASE 508
                GET_CLEO_SHARED_VAR varLandmarksProgress (iTempVar)
                BREAK
        ENDSWITCH
        CLEO_CALL GUI_DrawBoxOutline_WithText 0 (272.5 yCoord) (145.0 20.0) (0 0 0 190) (0.5) (1 0 1 0) (225 225 225 30) -1 5 (0.0 0.0) // lines sides
        IF iRow > 506
            CLEO_CALL GUI_DrawBox_WithNumber 0 (272.5 yCoord) (145.0 20.0) (0 0 0 0) 126 5 (0.0 -3.0) iTempVar  //~1~ / 10
        ELSE
            IF iRow = 503
                CLEO_CALL GUI_DrawBox_WithNumber 0 (272.5 yCoord) (145.0 20.0) (0 0 0 0) 128 5 (0.0 -3.0) iTempVar  //~1~ / 50
            ELSE
                CLEO_CALL GUI_DrawBox_WithNumber 0 (272.5 yCoord) (145.0 20.0) (0 0 0 0) 121 5 (0.0 -3.0) iTempVar  //~1~
            ENDIF
        ENDIF
        iRow ++
        yCoord += 20.0
    ENDWHILE
    USE_TEXT_COMMANDS FALSE
RETURN

ProcessGame_and_DrawItems_VERSION:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 229.0) (240.0 398.0) (14 20 32 255) (0.5) (0 1 0 1) (0 125 180 150) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_BIG
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 403.0) (240.0 50.0) (16 43 52 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_MEDIUM_DOWN

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 214.0) (240.0 30.0) (8 192 255 200) (0.5) (1 0 0 0) (6 253 244 0) -1 -1 (0.0 0.0)   //BLUE_BACKGROUND_SMALL_CENTER

    GET_FIXED_XY_ASPECT_RATIO (140.0 140.0) (xSize ySize)
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idLogoSP (520.0 115.0) (xSize ySize) (255 255 255 255)        //IMAGE_IN_GAME

    iTempVar = idVersionMsg_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 250.0) (200.0 112.0) (164 13 20 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 10 (0.0 0.0)   //~r~VERSION 1.9 ALPHA
    iTempVar = idAuthorMsg_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 300.0) (200.0 112.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 7 (0.0 0.0)   //AUTHOR_NOTE

    GET_FIXED_XY_ASPECT_RATIO (50.0 50.0) (xSize ySize)
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idSpiderIcon (520.0 403.0) (xSize ySize) (255 255 255 40)   
    iTempVar = idc2019AuthorMsg_l
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (520.0 350.0) (200.0 40.0) (255 255 255 0) (0.5) (0 0 0 0) (31 181 240 200) iTempVar 7 (0.0 0.0)   //by J16D c. 2018-2020
    USE_TEXT_COMMANDS FALSE
RETURN
///---------------------------------------------------------------------

///---------------------------------------------------------------------
defaultCameraMenu:
    //x = 0.0 //Center-Model Position
    //y = 0.0 //Center-Model Position
    //z = 1100.0  //Center-Model Position
    IF iSetCamera = TRUE
        x = 0.0 
        y = 3.0 
        z = 1101.0
        SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
        x = 0.0 
        y = 0.0
        z = 1101.0
        POINT_CAMERA_AT_POINT x y z JUMP_CUT
        iSetCamera = FALSE
    ENDIF
RETURN

setCameraSuitMain:
    IF iSetCamera = TRUE
        IF DOES_CHAR_EXIST iBaseActor
            x = 4.1091 //4.0969
            y = 0.2906  //0.1267
            z = 1097.1   //1102.90
            SET_CHAR_COORDINATES iBaseActor x y z
            SET_CHAR_HEADING iBaseActor 0.0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iBaseActor -0.29 2.4 -0.50 (x y z)  // -0.29 2.4 -0.50
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iBaseActor -0.29 0.0 -0.50 (x y z)   //-0.29 0.0 -0.50
            POINT_CAMERA_AT_POINT x y z JUMP_CUT
        ENDIF
        iSetCamera = FALSE
    ENDIF
RETURN

setCameraSuitMatrix:
    IF iSetCamera = TRUE
        IF DOES_CHAR_EXIST iBaseActor
            x = -6.8413     //-6.8031
            y = 0.3276      //0.2412
            z = 1097.091   //1102.91
            SET_CHAR_COORDINATES iBaseActor x y z
            SET_CHAR_HEADING iBaseActor 0.0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iBaseActor 0.79 2.90 0.1  (x y z)    //0.91 3.0 0.1
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iBaseActor 0.79 0.0 0.0  (x y z)     //0.91 0.0 0.0
            POINT_CAMERA_AT_POINT x y z JUMP_CUT
        ENDIF
        iSetCamera = FALSE
    ENDIF
RETURN

renderChar:
    IF iPanelB = 0
    OR iPanelB = menPowerSuit
        GET_LABEL_POINTER GUI_Memory_SuitItem (iTempVar)
        READ_MEMORY (iTempVar) 4 FALSE (iTempVar)  // SUIT selected
        iSelectedSuit = iTempVar  
    ENDIF
    IF NOT iSelectedSuit = isCharRendered
        GOSUB deleteChar
        counter = 1
        WHILE 30 >= counter
            IF iSelectedSuit = counter
                /*idTexture = counter 
                GOSUB get_unlock_code_suit
                IF pUnlockCode = iTempVar
                ENDIF
                */
                IF NOT iFireHead = 0
                    KILL_FX_SYSTEM iFireHead
                    iFireHead = 0
                ENDIF
                STRING_FORMAT (_lName)"s%i" counter
                LOAD_SPECIAL_CHARACTER 8 $_lName
                LOAD_ALL_MODELS_NOW
                IF iPanelB = 0
                    x = 4.1091  //4.0969
                    y = 0.2906
                    z = 1097.1
                ELSE
                    x = -6.8413
                    y = 0.3276
                    z = 1097.091
                ENDIF
                CREATE_CHAR PEDTYPE_CIVMALE SPECIAL08 0.0 0.0 -10.0 (iBaseActor)
                FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iBaseActor FALSE TRUE TRUE
                SET_CHAR_COLLISION iBaseActor 0
                SET_CHAR_HEADING iBaseActor 0.0
                SET_CHAR_AREA_VISIBLE iBaseActor 14
                UNLOAD_SPECIAL_CHARACTER 8
                isCharRendered = iSelectedSuit
                GOSUB playActorAnimation
                SET_CHAR_COORDINATES iBaseActor x y z

                IF isCharRendered = 20 //Spirit Spider
                    IF IS_FX_SYSTEM_AVAILABLE_WITH_NAME SP_BFLAME
                        IF NOT iFireHead = 0
                            KILL_FX_SYSTEM iFireHead
                            iFireHead = 0
                        ENDIF
                        CREATE_FX_SYSTEM_ON_CHAR SP_BFLAME iBaseActor (0.06 0.0 0.01) 1 (iFireHead)
                        ATTACH_FX_SYSTEM_TO_CHAR_BONE iFireHead iBaseActor 5  //5:head
                        PLAY_FX_SYSTEM iFireHead
                    ENDIF
                ENDIF
                BREAK
            ENDIF
            counter ++
        ENDWHILE
    ENDIF
RETURN

playActorAnimation:
    IF DOES_CHAR_EXIST iBaseActor
        IF iPanelB = 0   // MAIN_SUIT
            IF NOT IS_CHAR_PLAYING_ANIM iBaseActor ("menu_idle_B")
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iBaseActor ("menu_idle_B" "spider") 41.0 (1 1 1 0) -2
            ENDIF
        ELSE
            IF NOT IS_CHAR_PLAYING_ANIM iBaseActor ("menu_idle_A")
            AND NOT IS_CHAR_PLAYING_ANIM iBaseActor ("hang_A")
            AND NOT IS_CHAR_PLAYING_ANIM iBaseActor ("menu_idle_C")
                GENERATE_RANDOM_INT_IN_RANGE 0 7 (iTempVar)
                SWITCH iTempVar
                    CASE 0
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iBaseActor ("menu_idle_C" "spider") 51.0 (1 1 1 0) -2
                        BREAK
                    DEFAULT
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iBaseActor ("menu_idle_A" "spider") 46.0 (1 1 1 0) -2
                        BREAK
                ENDSWITCH
            ENDIF
        ENDIF
    ENDIF
RETURN

deleteChar:
    IF DOES_CHAR_EXIST iBaseActor
        DELETE_CHAR iBaseActor
        isCharRendered = 0
    ENDIF
RETURN

setSkin:
    counter = 1
    WHILE 30 >= counter
        IF iSelectedSuit = counter
            SWITCH iSelectedSuit
                CASE 1
                    iTempVar = 9999
                    BREAK
                CASE 2
                    iTempVar = 1985
                    BREAK
                CASE 3
                    iTempVar = 3564
                    BREAK
                CASE 4
                    iTempVar = 4465
                    BREAK
                CASE 5
                    iTempVar = 7952
                    BREAK
                CASE 6
                    iTempVar = 8431
                    BREAK
                CASE 7
                    iTempVar = 5761
                    BREAK 
                CASE 8
                    iTempVar = 9999
                    BREAK
                CASE 9
                    iTempVar = 6784
                    BREAK
                CASE 10
                    iTempVar = 3897
                    BREAK
                CASE 11
                    iTempVar = 4837
                    BREAK
                CASE 12
                    iTempVar = 7913
                    BREAK
                CASE 13
                    iTempVar = 1937
                    BREAK
                CASE 14
                    iTempVar = 8319
                    BREAK
                CASE 15
                    iTempVar = 6743
                    BREAK
                CASE 16
                    iTempVar = 4627
                    BREAK
                CASE 17
                    iTempVar = 9999
                    BREAK
                CASE 18
                    iTempVar = 7147
                    BREAK
                CASE 19
                    iTempVar = 9636
                    BREAK
                CASE 20
                    iTempVar = 9999
                    BREAK
                CASE 21
                    iTempVar = 8525
                    BREAK
                CASE 22
                    iTempVar = 7898
                    BREAK
                CASE 23
                    iTempVar = 1232
                    BREAK
                CASE 24
                    iTempVar = 7319
                    BREAK
                CASE 25
                    iTempVar = 6731
                    BREAK
                CASE 26
                    iTempVar = 1973
                    BREAK
                CASE 27
                    iTempVar = 2846
                    BREAK
                CASE 28
                    iTempVar = 1917
                    BREAK
                CASE 29
                    iTempVar = 3734
                    BREAK
                CASE 30
                    iTempVar = 4913
                    BREAK
            ENDSWITCH
            CLEO_CALL getSuitInfoUnclock 0 counter (pUnlockCode)
            IF pUnlockCode = iTempVar
                STRING_FORMAT (_lName)"s%i" counter
                LOAD_SPECIAL_CHARACTER 8 $_lName            
                LOAD_ALL_MODELS_NOW
                SET_PLAYER_MODEL 0 SPECIAL08
                CLEO_CALL StoreSuitItem 0 iSelectedSuit
                UNLOAD_SPECIAL_CHARACTER 8
            //ELSE
                //PRINT_FORMATTED_NOW "SUIT LOCKED: %i" 1000 iSelectedSuit //debug
            ENDIF
            BREAK
        ENDIF
        counter ++
    ENDWHILE
RETURN

setWalkstyle:
    iTempVar = 0x609A4E
    WRITE_STRUCT_OFFSET iTempVar 0x0 4 0x90909090
    WRITE_STRUCT_OFFSET iTempVar 0x4 2 0x9090
    //MAKE_NOP iTempVar 4
    //MAKE_NOP iTempVar 2
    /*WRITE_MEMORY iTempVar 4 0x90909090 1
    iTempVar += 0x4
    WRITE_MEMORY iTempVar 2 0x9090 1*/
    SET_ANIM_GROUP_FOR_CHAR player_actor spider
RETURN

restoreWalkstyle:
    iTempVar = 0x609A4E
    WRITE_STRUCT_OFFSET iTempVar 0x0 4 0x04D48689
    WRITE_STRUCT_OFFSET iTempVar 0x4 2 0x0
    /*WRITE_MEMORY iTempVar 4 0x04D48689 1
    iTempVar += 0x4
    WRITE_MEMORY iTempVar 2 0x0 1*/
    SET_ANIM_GROUP_FOR_CHAR player_actor PLAYER
RETURN


///---------------------------------------------------------------------
reset_panel_parameters:
    iSetCamera = TRUE

    iPanelB = 0
    iActiveRow = 1
    iActiveCol = 1    
    iActive = 1
    CLEO_CALL storeScreenCoords 0 399.0 229.0
RETURN

set_init_parameters:
    iPanelB = 0
    iPanel = iUpStart
    iActiveRow = 1
    iActiveCol = 1    
    iActive = 1

    iSetCamera = TRUE
    GOSUB defaultCameraMenu
RETURN

lock_player_controls:
    USE_TEXT_COMMANDS FALSE
    CLEAR_PRINTS
    CLEAR_HELP

    SET_PLAYER_CYCLE_WEAPON_BUTTON 0 FALSE
    //SET_PLAYER_ENTER_CAR_BUTTON player FALSE
    SET_PLAYER_FIRE_BUTTON player FALSE
    SET_PLAYER_CONTROL_PAD_MOVEMENT PAD1 FALSE  //disables player movement without camera

    DISPLAY_RADAR FALSE
    DISPLAY_HUD FALSE
    DISPLAY_ZONE_NAMES FALSE
    DISPLAY_CAR_NAMES FALSE
    //SET_EVERYONE_IGNORE_PLAYER player TRUE
    //SET_POLICE_IGNORE_PLAYER player TRUE
RETURN

unlock_player_controls:
    USE_TEXT_COMMANDS FALSE
    CLEAR_PRINTS
    CLEAR_HELP

    SET_PLAYER_CYCLE_WEAPON_BUTTON 0 TRUE
    SET_PLAYER_FIRE_BUTTON player TRUE

    DISPLAY_RADAR TRUE
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor

    CLEO_CALL getStatusSpiderMod 0 (toggleSpiderMod)
    IF toggleSpiderMod = 0
        //SET_CLEO_SHARED_VAR varHUD 0       // 0:OFF || 1:ON
        DISPLAY_HUD TRUE
        DISPLAY_ZONE_NAMES TRUE
        DISPLAY_CAR_NAMES TRUE
    ELSE
        GET_CLEO_SHARED_VAR varHUD (iTempVar)
        IF iTempVar = 0     // 0:OFF || 1:ON
            DISPLAY_HUD TRUE
            DISPLAY_ZONE_NAMES TRUE
            DISPLAY_CAR_NAMES TRUE
        ENDIF
    ENDIF
    SET_PLAYER_CONTROL_PAD_MOVEMENT PAD1 TRUE  //restore player movement

RETURN

///---------------------------------------------------------------------


load_all_needed_files:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        //TEXTURES
        CONST_INT suit1 1
        CONST_INT suit2 2
        CONST_INT suit3 3
        CONST_INT suit4 4
        CONST_INT suit5 5
        CONST_INT suit6 6
        CONST_INT suit7 7
        CONST_INT suit8 8
        CONST_INT suit9 9
        CONST_INT suit10 10
        CONST_INT suit11 11
        CONST_INT suit12 12
        CONST_INT suit13 13
        CONST_INT suit14 14
        CONST_INT suit15 15
        CONST_INT suit16 16
        CONST_INT suit17 17
        CONST_INT suit18 18
        CONST_INT suit19 19
        CONST_INT suit20 20
        CONST_INT suit21 21
        CONST_INT suit22 22
        CONST_INT suit23 23
        CONST_INT suit24 24
        CONST_INT suit25 25 
        CONST_INT suit26 26 
        CONST_INT suit27 27 
        CONST_INT suit28 28 
        CONST_INT suit29 29 
        CONST_INT suit30 30 

        CONST_INT idMapIcon0    33
        CONST_INT idMapIcon1    34
        CONST_INT idMapIcon2    35
        CONST_INT idMapIcon3    36
        CONST_INT idMapIcon4    37
        CONST_INT idMapIcon5    38
        CONST_INT idMapIcon6    39

        CONST_INT tMenuChar1    40
        CONST_INT tMenuChar2    41
        CONST_INT tMenuChar3    42
        CONST_INT tMenuChar4    43
        CONST_INT tMenuChar5    44
        CONST_INT tMenuChar6    45
        CONST_INT tMenuChar7    46
        CONST_INT tMenuChar8    47
        CONST_INT tMenuChar9    48
        CONST_INT tMenuChar10   49
        CONST_INT tMenuCharB1   50
        CONST_INT tMenuCharB2   51
        CONST_INT tMenuCharB3   52
        CONST_INT tMenuCharB4   53
        CONST_INT tMenuCharB5   54
        CONST_INT tMenuCharB6   55
        CONST_INT tMenuCharB7   56
        CONST_INT tMenuCharB8   57
        CONST_INT tMenuCharB9   58
        CONST_INT tMenuCharB10  59

        CONST_INT selectSuit26  60 
        CONST_INT customSuit27  61
        CONST_INT successIcon28 62
        CONST_INT successIconE  63
        CONST_INT unknownSuit29 64
        CONST_INT turnOn30      65
        CONST_INT turnOff31     66
        CONST_INT idMapTexture  67
        CONST_INT idConfSuitBck 68

        CONST_INT pow_background 69
        CONST_INT pow_null 70
        CONST_INT pow71 71
        CONST_INT pow72 72
        CONST_INT pow73 73
        CONST_INT pow74 74
        CONST_INT pow75 75
        CONST_INT pow76 76
        CONST_INT pow77 77
        CONST_INT pow78 78
        CONST_INT pow79 79
        CONST_INT pow80 80
        CONST_INT pow81 81
        CONST_INT pow82 82

        CONST_INT idPrimaryControls 83

        CONST_INT idLogoSP 89
        CONST_INT idBackgroundSett 90
        CONST_INT idSpiderIcon 91
        CONST_INT idBackgroundArrows 92

        CONST_INT idSkill1 96
        CONST_INT idSkill2 97
        CONST_INT idSkill2a 98
        CONST_INT idSkill3 99
        CONST_INT idSkill3a 100
        CONST_INT idSkill3b 101
        CONST_INT idSkill3c 102
        CONST_INT idSkill3c1 103
        CONST_INT idSkill3c2 104

        CONST_INT idhsk01 110
        CONST_INT idhsk02 111
        CONST_INT idhsk02a 112
        CONST_INT idhsk03 113
        CONST_INT idhsk03a 114
        CONST_INT idhsk03b 115
        CONST_INT idhsk03c 116
        CONST_INT idhsk03c1 117
        CONST_INT idhsk03c2 118

        CONST_INT bck300 300
        CONST_INT bck301 301
        CONST_INT bck302 302
        CONST_INT bck303 303
        CONST_INT bck304 304
        CONST_INT bck305 305
        CONST_INT bck306 306
        CONST_INT bck307 307
        CONST_INT bck308 308
        CONST_INT bck309 309
        CONST_INT bck310 310
        CONST_INT bck311 311
        CONST_INT bck312 312
        CONST_INT bck313 313
        CONST_INT bck314 314
        CONST_INT bck315 315

        CONST_INT bck320 320    //default
        CONST_INT bck321 321
        CONST_INT bck322 322
        CONST_INT bck323 323
        CONST_INT bck324 324
        CONST_INT bck325 325
        CONST_INT bck326 326
        CONST_INT bck327 327
        CONST_INT bck328 328
        CONST_INT bck329 329
        CONST_INT bck330 330
        CONST_INT bck331 331
        CONST_INT bck332 332

        /*counter = 1
        WHILE 25 >= counter
            STRING_FORMAT (_lName)"suit%i" counter
            LOAD_SPRITE counter $_lName
            counter ++
        ENDWHILE*/
        LOAD_TEXTURE_DICTIONARY spmn
        LOAD_SPRITE suit1 "suit1"
        LOAD_SPRITE suit2 "suit2"
        LOAD_SPRITE suit3 "suit3"
        LOAD_SPRITE suit4 "suit4"
        LOAD_SPRITE suit5 "suit5"
        LOAD_SPRITE suit6 "suit6"
        LOAD_SPRITE suit7 "suit7"
        LOAD_SPRITE suit8 "suit8"
        LOAD_SPRITE suit9 "suit9"
        LOAD_SPRITE suit10 "suit10"
        LOAD_SPRITE suit11 "suit11"
        LOAD_SPRITE suit12 "suit12"
        LOAD_SPRITE suit13 "suit13"
        LOAD_SPRITE suit14 "suit14"
        LOAD_SPRITE suit15 "suit15"
        LOAD_SPRITE suit16 "suit16"
        LOAD_SPRITE suit17 "suit17"
        LOAD_SPRITE suit18 "suit18"
        LOAD_SPRITE suit19 "suit19"
        LOAD_SPRITE suit20 "suit20"
        LOAD_SPRITE suit21 "suit21"
        LOAD_SPRITE suit22 "suit22"
        LOAD_SPRITE suit23 "suit23"
        LOAD_SPRITE suit24 "suit24"
        LOAD_SPRITE suit25 "suit25"
        LOAD_SPRITE suit26 "suit26"
        LOAD_SPRITE suit27 "suit27"
        LOAD_SPRITE suit28 "suit28"
        LOAD_SPRITE suit29 "suit29"
        LOAD_SPRITE suit30 "suit30"

        LOAD_SPRITE idLogoSP "splogo"
        LOAD_SPRITE idBackgroundSett "bckstt"
        LOAD_SPRITE idSpiderIcon "splg"
        LOAD_SPRITE idBackgroundArrows "bckarr"
        LOAD_SPRITE idConfSuitBck "csubck"

        LOAD_SPRITE idSkill1 "sk_1"
        LOAD_SPRITE idSkill2 "sk_2"
        LOAD_SPRITE idSkill2a "sk_2a"
        LOAD_SPRITE idSkill3 "sk_3"
        LOAD_SPRITE idSkill3a "sk_3a"
        LOAD_SPRITE idSkill3b "sk_3b"
        LOAD_SPRITE idSkill3c "sk_3c"
        LOAD_SPRITE idSkill3c1 "sk_3c1"
        LOAD_SPRITE idSkill3c2 "sk_3c2"

        LOAD_SPRITE idhsk01 "hsk01"
        LOAD_SPRITE idhsk02 "hsk02"
        LOAD_SPRITE idhsk02a "hsk02a"
        LOAD_SPRITE idhsk03 "hsk03"
        LOAD_SPRITE idhsk03a "hsk03a"
        LOAD_SPRITE idhsk03b "hsk03b"
        LOAD_SPRITE idhsk03c "hsk03c"
        LOAD_SPRITE idhsk03c1 "hsk03c1"
        LOAD_SPRITE idhsk03c2 "hsk03c2"

        LOAD_SPRITE selectSuit26 "sel"
        LOAD_SPRITE customSuit27 "custom"
        LOAD_SPRITE successIcon28 "Success"
        LOAD_SPRITE successIconE "ESuccess"
        LOAD_SPRITE unknownSuit29 "unknown"
        LOAD_SPRITE turnOn30 "tOn"
        LOAD_SPRITE turnOff31 "tOff"
        LOAD_SPRITE idMapTexture "spmap"
        LOAD_SPRITE idMapIcon0 "mk0"    // pointer
        LOAD_SPRITE idMapIcon1 "mk1"    //Marker
        LOAD_SPRITE idMapIcon2 "mk2"    //player pos
        LOAD_SPRITE idMapIcon3 "mk3"   //backpack 
        LOAD_SPRITE idMapIcon4 "mk4"    //catwoman
        LOAD_SPRITE idMapIcon5 "mk5"    //Crime
        LOAD_SPRITE idMapIcon6 "mk6"    //landMark

        LOAD_SPRITE tMenuChar1 "ch1"
        LOAD_SPRITE tMenuChar2 "ch2"
        LOAD_SPRITE tMenuChar3 "ch3"
        LOAD_SPRITE tMenuChar4 "ch4"
        LOAD_SPRITE tMenuChar5 "ch5"
        LOAD_SPRITE tMenuChar6 "ch6"
        LOAD_SPRITE tMenuChar7 "ch7"
        LOAD_SPRITE tMenuChar8 "ch8"
        LOAD_SPRITE tMenuChar9 "ch9"
        LOAD_SPRITE tMenuChar10 "ch10"
        LOAD_SPRITE tMenuCharB1 "bsp1"
        LOAD_SPRITE tMenuCharB2 "bsp2"
        LOAD_SPRITE tMenuCharB3 "bsp3"
        LOAD_SPRITE tMenuCharB4 "bsp4"
        LOAD_SPRITE tMenuCharB5 "bsp5"
        LOAD_SPRITE tMenuCharB6 "bsp6"
        LOAD_SPRITE tMenuCharB7 "bsp7"
        LOAD_SPRITE tMenuCharB8 "bsp8"
        LOAD_SPRITE tMenuCharB9 "bsp9"
        LOAD_SPRITE tMenuCharB10 "bsp10"

        LOAD_SPRITE pow_background "pow_backg"
        LOAD_SPRITE pow_null "pow_null"
        LOAD_SPRITE pow71 "pow_ic_wb"
        LOAD_SPRITE pow72 "pow_ic_hd"
        LOAD_SPRITE pow73 "pow_ic_bp"
        LOAD_SPRITE pow74 "pow_ic_sb"
        LOAD_SPRITE pow75 "pow_ic_sw"
        LOAD_SPRITE pow76 "pow_ic_ep"
        LOAD_SPRITE pow77 "pow_ic_ro"
        LOAD_SPRITE pow78 "pow_ic_bpj"
        LOAD_SPRITE pow79 "pow_ic_lg"
        LOAD_SPRITE pow80 "pow_ic_ia"
        LOAD_SPRITE pow81 "pow_ic_ds"
        LOAD_SPRITE pow82 "pow_ic_sf"

        LOAD_SPRITE idPrimaryControls "mpc_a"
        /*counter = 300
        WHILE 315 >= counter
            STRING_FORMAT (_lName)"bck%i" counter
            LOAD_SPRITE counter $_lName
            counter ++
        ENDWHILE*/
        LOAD_SPRITE bck300 "bck300"
        LOAD_SPRITE bck301 "bck301"
        LOAD_SPRITE bck302 "bck302"
        LOAD_SPRITE bck303 "bck303"
        LOAD_SPRITE bck304 "bck304"
        LOAD_SPRITE bck305 "bck305"
        LOAD_SPRITE bck306 "bck306"
        LOAD_SPRITE bck307 "bck307"
        LOAD_SPRITE bck308 "bck308"
        LOAD_SPRITE bck309 "bck309"
        LOAD_SPRITE bck310 "bck310"
        LOAD_SPRITE bck311 "bck311"
        LOAD_SPRITE bck312 "bck312"
        LOAD_SPRITE bck313 "bck313"
        LOAD_SPRITE bck314 "bck314"
        LOAD_SPRITE bck315 "bck315"

        LOAD_SPRITE bck320 "bck000"
        LOAD_SPRITE bck321 "bck321"
        LOAD_SPRITE bck322 "bck322"
        LOAD_SPRITE bck323 "bck323"
        LOAD_SPRITE bck324 "bck324"
        LOAD_SPRITE bck325 "bck325"
        LOAD_SPRITE bck326 "bck326"
        LOAD_SPRITE bck327 "bck327"
        LOAD_SPRITE bck328 "bck328"
        LOAD_SPRITE bck329 "bck329"
        LOAD_SPRITE bck330 "bck330"
        LOAD_SPRITE bck331 "bck331"
        LOAD_SPRITE bck332 "bck332"

        CLEO_CALL suitUnlock 0
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW

    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

}
SCRIPT_END



//-+-- CALL SCM HELPER
{
//CLEO_CALL storeStatusSpiderMod 0 var
storeStatusSpiderMod:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_toggleSpiderMod pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getStatusSpiderMod 0 (var)
getStatusSpiderMod:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_toggleSpiderMod (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL save_weather_info 0 /*weather*/ var
save_weather_info:
    LVAR_INT current_weather    //IN
    LVAR_INT threadSpace
    GET_LABEL_POINTER WORLD_weather (threadSpace)
    threadSpace += 0x0
    WRITE_MEMORY threadSpace 2 current_weather FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL get_weather_info 0 /*weather*/(var)
get_weather_info:
    LVAR_INT current_weather
    LVAR_INT threadSpace
    GET_LABEL_POINTER WORLD_weather (threadSpace)
    threadSpace += 0x0
    READ_MEMORY (threadSpace) 2 FALSE (current_weather)
CLEO_RETURN 0 current_weather
}

//-----------------------Skills
{
//CLEO_CALL get_skill_status_from_memory 0 counter (iTempVar)
get_skill_status_from_memory:
    LVAR_INT counter   //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER buffer_skills_bytes10 (pActiveItem)
    pActiveItem += counter
    READ_MEMORY (pActiveItem) 1 FALSE (pTempVar)
CLEO_RETURN 0 pTempVar
}
{
//CLEO_CALL store_skill_item_on_memory 0 counter iStatus
store_skill_item_on_memory:
    LVAR_INT counter iStatus //in
    LVAR_INT pActiveItem iTempVar
    GET_LABEL_POINTER buffer_skills_bytes10 (pActiveItem)
    pActiveItem += counter
    WRITE_MEMORY pActiveItem 1 iStatus FALSE
    //PRINT_FORMATTED_NOW "value written on slot:%i" 1000 counter //debug
CLEO_RETURN 0
}
{
//CLEO_CALL store_skills_on_ini 0 ()
store_skills_on_ini:
    LVAR_INT iVarTextString
    LVAR_INT a b c d e f g h i iTemp pActiveItem 
    GET_LABEL_POINTER buffer_skills_bytes10 (pActiveItem)
    pActiveItem ++  //+1
    READ_MEMORY (pActiveItem) 1 FALSE (a)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (b)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (c)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (d)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (e)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (f)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (g)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (h)
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (i)

    GET_LABEL_POINTER bytes32 (iVarTextString)
    STRING_FORMAT (iVarTextString) "%d %d %d %d %d %d %d %d %d" a b c d e f g h i
    WRITE_STRING_TO_INI_FILE $iVarTextString "cleo\SpiderJ16D\config.ini" "skills" "m_sk"
CLEO_RETURN 0
}
{
//CLEO_CALL get_skill_data_and_save_on_memory 0 ()
get_skill_data_and_save_on_memory:
    LVAR_INT iVarTextString
    LVAR_INT a b c d e f g h i iTemp pActiveItem 
    GET_LABEL_POINTER bytes32 (iVarTextString)
    READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "skills" "m_sk" (iVarTextString)
    IF NOT SCAN_STRING $iVarTextString "%d %d %d %d %d %d %d %d %d" iTemp (a b c d e f g h i)
        PRINT_FORMATTED_NOW "ERROR reading file! reinstall! %i" 2000 iTemp
        WAIT 2000
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
    GET_LABEL_POINTER buffer_skills_bytes10 (pActiveItem)
    pActiveItem ++  //+1
    WRITE_MEMORY pActiveItem 1 a FALSE
    SET_CLEO_SHARED_VAR varSkill1 a    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 b FALSE
    SET_CLEO_SHARED_VAR varSkill2 b    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 c FALSE
    SET_CLEO_SHARED_VAR varSkill3 c    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 d FALSE
    SET_CLEO_SHARED_VAR varSkill2a d    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 e FALSE
    SET_CLEO_SHARED_VAR varSkill3a e    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 f FALSE
    SET_CLEO_SHARED_VAR varSkill3b f    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 g FALSE
    SET_CLEO_SHARED_VAR varSkill3c g    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 h FALSE
    SET_CLEO_SHARED_VAR varSkill3c1 h    // 0:OFF || 1:ON
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 i FALSE
    SET_CLEO_SHARED_VAR varSkill3c2 i    // 0:OFF || 1:ON
CLEO_RETURN 0
}
//-----------------------



//-----------------------Map
{
//CLEO_CALL store_backpack_current_progress 0 ()
store_backpack_current_progress:
    LVAR_INT iVarTextString
    LVAR_INT a b c d e f g h i j iTemp pActiveItem 
    GET_LABEL_POINTER bytes32 (iVarTextString)
    READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "events" "m_bpck" (iVarTextString)
    IF NOT SCAN_STRING $iVarTextString "%d %d %d %d %d %d %d %d %d %d" iTemp (a b c d e f g h i j)
        PRINT_FORMATTED_NOW "ERROR reading file! reinstall! %i" 2000 iTemp
        WAIT 2000
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
    GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
    WRITE_MEMORY pActiveItem 1 a FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 b FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 c FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 d FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 e FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 f FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 g FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 h FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 i FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 j FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL store_landmark_current_progress 0 ()
store_landmark_current_progress:
    LVAR_INT iVarTextString
    LVAR_INT a b c d e f g h i j iTemp pActiveItem 
    GET_LABEL_POINTER bytes32 (iVarTextString)
    READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "events" "m_ldmk" (iVarTextString)
    IF NOT SCAN_STRING $iVarTextString "%d %d %d %d %d %d %d %d %d %d" iTemp (a b c d e f g h i j)
        PRINT_FORMATTED_NOW "ERROR reading file! reinstall! %i" 2000 iTemp
        WAIT 2000
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
    GET_LABEL_POINTER buffer_landmarks_bytes10 (pActiveItem)
    WRITE_MEMORY pActiveItem 1 a FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 b FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 c FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 d FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 e FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 f FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 g FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 h FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 i FALSE
    pActiveItem ++
    WRITE_MEMORY pActiveItem 1 j FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL get_existing_backpack 0 idIcon counter (iTempVar)
get_existing_backpack:
    LVAR_INT i counter   //in
    LVAR_INT pActiveItem pTempVar
    SWITCH i   // ||landmarks:1 ||backpacks:2 ||crimes:3
        CASE 1
            GET_LABEL_POINTER buffer_landmarks_bytes10 (pActiveItem)
            BREAK
        CASE 2
            GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
            BREAK
        CASE 3
            GET_LABEL_POINTER buffer_crimes_bytes5 (pActiveItem)
            BREAK
    ENDSWITCH
    pActiveItem += counter
    READ_MEMORY (pActiveItem) 1 FALSE (pTempVar)
CLEO_RETURN 0 pTempVar
}
{
//CLEO_CALL save_map_coords 0 iVal //||landmarks:1 ||backpacks:2 ||crimes:3
save_map_coords:
    LVAR_INT i      //in  ||landmarks:1 ||backpacks:2 ||crimes:3
    LVAR_TEXT_LABEL _lName
    LVAR_TEXT_LABEL16 _lSection
    LVAR_INT iTemp counter pActiveItem pTempVar iVar iStartCounter iEndCounter
    LVAR_FLOAT x y z

    GET_LABEL_POINTER bytes32 (iVar)
    IF i = 3
        iStartCounter = 0
        iEndCounter = 4
    ELSE
        iStartCounter = 0
        iEndCounter = 9
    ENDIF
    counter = iStartCounter
    WHILE iEndCounter >= counter
        IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
            SWITCH i   // ||landmarks:1 ||backpacks:2 ||crimes:3
                CASE 1
                    GET_TEXT_LABEL_STRING SP_LDMK (_lSection)
                    STRING_FORMAT (_lName)"ldmk%i" counter
                    GET_LABEL_POINTER CoordsLandMarksBuffer (pActiveItem)
                    BREAK
                CASE 2
                    GET_TEXT_LABEL_STRING SP_BPCK (_lSection)
                    STRING_FORMAT (_lName)"bpck%i" counter
                    GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
                    BREAK
                CASE 3
                    GET_TEXT_LABEL_STRING SP_BPCK (_lSection)
                    STRING_FORMAT (_lName)"crms%i" counter
                    GET_LABEL_POINTER CoordsBuffer (pActiveItem)
                    BREAK
            ENDSWITCH
            READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" $_lSection $_lName (iVar)
            IF NOT SCAN_STRING $iVar "%f %f %f" iTemp (x y z)
                x = 0.0
                y = 0.0
                z = 0.0
            ENDIF
            //GET_LABEL_POINTER CoordsBuffer (pActiveItem)
            pTempVar = counter
            pTempVar *= 12
            pActiveItem += pTempVar
            WRITE_MEMORY pActiveItem 4 x FALSE
            pActiveItem += 4
            WRITE_MEMORY pActiveItem 4 y FALSE
            pActiveItem += 4
            WRITE_MEMORY pActiveItem 4 z FALSE
        ELSE
            PRINT_FORMATTED_NOW "ERROR coords file not found" 1500
            WAIT 1500
            CLEO_RETURN 0
        ENDIF
        counter ++
    ENDWHILE
CLEO_RETURN 0
}
{
//CLEO_CALL get_map_coords 0 idIcon counter (x y z)
get_map_coords:
    LVAR_INT i counter   //in
    LVAR_INT pActiveItem pTempVar
    LVAR_FLOAT x y z
    SWITCH i   // ||landmarks:1 ||backpacks:2 ||crimes:3
        CASE 1
            GET_LABEL_POINTER CoordsLandMarksBuffer (pActiveItem)
            BREAK
        CASE 2
            GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
            BREAK
        CASE 3
            GET_LABEL_POINTER CoordsBuffer (pActiveItem)
            BREAK
    ENDSWITCH
    //GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
    pTempVar = counter
    pTempVar *= 12
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (x)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (y)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (z)
CLEO_RETURN 0 x y z
}
//-----------------------



{
//CLEO_CALL storeCharWorldXYZCoords 0 x y z
storeCharWorldXYZCoords:
    LVAR_FLOAT fX fY fZ fAngle
    LVAR_INT pActiveItem
    GET_LABEL_POINTER CHAR_world_coords pActiveItem
    WRITE_MEMORY pActiveItem 4 fX FALSE
    pActiveItem += 4
    WRITE_MEMORY pActiveItem 4 fY FALSE
    pActiveItem += 4
    WRITE_MEMORY pActiveItem 4 fZ FALSE
    pActiveItem += 4
    WRITE_MEMORY pActiveItem 4 fAngle FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getCharWorldXYZCoords 0 (x y z)
getCharWorldXYZCoords:
    LVAR_INT pActiveItem 
    LVAR_FLOAT fX fY fZ fAngle
    GET_LABEL_POINTER CHAR_world_coords (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (fX)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (fY)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (fZ)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (fAngle)
CLEO_RETURN 0 fX fY fZ fAngle
}
{
//CLEO_CALL suitUnlock 0         
suitUnlock:
    LVAR_TEXT_LABEL lSuitName
    LVAR_INT iTempUnlockCode counter
    counter = 1
    WHILE 30 >= counter
        STRING_FORMAT (lSuitName)"suit%i" counter
        READ_INT_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "CODE" $lSuitName (iTempUnlockCode)
        CLEO_CALL storeSuitInfoUnclock 0 counter iTempUnlockCode
        counter ++
    ENDWHILE
CLEO_RETURN 0
}
{
//CLEO_CALL playSFXMenuSound 0 /*id_sfx*/4  
play_SFX_Menu:
    LVAR_INT id_Sfx //In
    LVAR_INT sfx player_actor
    GET_PLAYER_CHAR 0 player_actor
    CLEO_CALL get_SFX_Menu 0 id_Sfx /*sound*/sfx
    SET_AUDIO_STREAM_STATE sfx 1
CLEO_RETURN 0
}
{
//CLEO_CALL store_SFX_Menu 0
store_SFX_Menu:
    LVAR_INT sfx1 sfx2 sfx3 sfx4
    LVAR_INT pActiveItem player_actor
    GET_PLAYER_CHAR 0 player_actor
    GET_LABEL_POINTER SFX_sound_effects pActiveItem
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\m_b.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\m_b.mp3" (sfx1)
        WRITE_MEMORY pActiveItem 4 sfx1 FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\m_ma.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\m_ma.mp3" (sfx2)
        WRITE_MEMORY pActiveItem 4 sfx2 FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\m_mb.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\m_mb.mp3" (sfx3)
        WRITE_MEMORY pActiveItem 4 sfx3 FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\m_s.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\m_s.mp3" (sfx4)
        WRITE_MEMORY pActiveItem 4 sfx4 FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL getSFXMenuSound 0 /*ID*/4 /*sound*/sfx
get_SFX_Menu:
    // ID:0 - Sound Back
    // ID:4 - Sound Move -UP
    // ID:8 - Sound Move -Matrix
    // ID:12 - Sound Success
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_sound_effects (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL releaseSFXMenuSounds 0
release_SFX_Menu:
    LVAR_INT sfx1 sfx2 sfx3 sfx4 pActiveItem
    GET_LABEL_POINTER SFX_sound_effects pActiveItem
    READ_MEMORY (pActiveItem) 4 FALSE (sfx1)
    IF sfx1 = 0
    ELSE
        REMOVE_AUDIO_STREAM sfx1
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (sfx2)
    IF sfx2 = 0
    ELSE
        REMOVE_AUDIO_STREAM sfx2
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (sfx3)
    IF sfx3 = 0
    ELSE
        REMOVE_AUDIO_STREAM sfx3
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (sfx4)
    IF sfx4 = 0
    ELSE
        REMOVE_AUDIO_STREAM sfx4
        WRITE_MEMORY pActiveItem 4 0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL getDataJoystickLeftRight 0 (2 1 iActive) (iActive)
getDataJoystickLeftRight:
    LVAR_INT maxValueRow minValueRow iActiveRow  //in values    
    LVAR_INT LRStick UDStick
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > LRStick  //Right
        iActiveRow --
        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
    ELSE
        IF LRStick > 0  //Left
            iActiveRow ++
            CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
        ENDIF
    ENDIF
    CLAMP_INT iActiveRow minValueRow maxValueRow (iActiveRow) 
CLEO_RETURN 0 iActiveRow
}
{
//CLEO_CALL getDataJoystickUpDown 0 (2 1 iActive) (iActive)
getDataJoystickUpDown:
    LVAR_INT maxValueRow minValueRow iActiveRow  //in values    
    LVAR_INT LRStick UDStick
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > UDStick  //Up
        iActiveRow --
        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
    ELSE
        IF UDStick > 0  //Down
            iActiveRow ++
            CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
        ENDIF
    ENDIF
    CLAMP_INT iActiveRow minValueRow maxValueRow (iActiveRow) 
CLEO_RETURN 0 iActiveRow
}
{
//CLEO_CALL GetDataJoystickMatrix5x5 0 (5 1 currentCol)(5 1 currentRow) (iActiveCol iActiveRow)
GetDataJoystickMatrix5x5:
    LVAR_INT maxValueColumn minValueColumn iActiveCol maxValueRow minValueRow iActiveRow  //in values
    LVAR_INT LRStick UDStick
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > LRStick  //Right
        iActiveCol --
        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
    ELSE
        IF LRStick > 0  //Left
            iActiveCol ++
            CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
        ENDIF
    ENDIF
    CLAMP_INT iActiveCol minValueColumn maxValueColumn (iActiveCol) 
    IF 0 > UDStick  //Up
        iActiveRow --
        CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
    ELSE
        IF UDStick > 0  //Down
            iActiveRow ++
            CLEO_CALL play_SFX_Menu 0 8  // ID:0-Sound Back || ID:4-Sound Move-UP || ID:8-Sound Move-Matrix || ID:12-Sound Success   
        ENDIF
    ENDIF
    CLAMP_INT iActiveRow minValueRow maxValueRow (iActiveRow) 
CLEO_RETURN 0 iActiveCol iActiveRow
}
{
/*
0 > LRStick -> Right || LRStick > 0 -> Left
0 > UDStick -> Up    || UDStick > 0 -> Down
*/
//CLEO_CALL getDataJoystick 0 (LRStick UDStick)
getDataJoystick:
    LVAR_INT LRStick UDStick empty
    GET_POSITION_OF_ANALOGUE_STICKS 0 LRStick UDStick empty empty
CLEO_RETURN 0 LRStick UDStick
}
{
//CLEO_CALL StoreAlternativeItem 0 var
storeAlternativeItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_AlternativeItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getAlternativeItem 0 (var)
getAlternativeItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_AlternativeItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeSwingBuildingItem 0 var
storeSwingBuildingItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SwingBuildingsItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getSwingBuildingItem 0 (var)
getSwingBuildingItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SwingBuildingsItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeFixGroundItem 0 var
storeFixGroundItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_FixGroundItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getFixGroundItem 0 (var)
getFixGroundItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_FixGroundItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeMouseControlItem 0 var
storeMouseControlItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_MouseControlItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getMouseControlItem 0 (var)
getMouseControlItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_MouseControlItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeAutoAimItem 0 var
storeAutoAimItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_AutoAimItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getAutoAimItem 0 (var)
getAutoAimItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_AutoAimItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeSpiderDriveCarsItem 0 var
storeSpiderDriveCarsItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SpiderDriveCarsItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getSpiderDriveCarsItem 0 (var)
getSpiderDriveCarsItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SpiderDriveCarsItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeFriendlyNeighborhoodItem 0 var
storeFriendlyNeighborhoodItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_FNItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getFriendlyNeighborhoodItem 0 (var)
getFriendlyNeighborhoodItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_FNItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeThrowVehDoorsItem 0 var
storeThrowVehDoorsItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ThrowDoorsItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getThrowVehDoorsItem 0 (var)
getThrowVehDoorsItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ThrowDoorsItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL getSuitInfoUnclock 0 iSelectedSuit (iTempUnlockCode)
getSuitInfoUnclock:
    LVAR_INT counter
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER SUIT_data_Info (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeSuitInfoUnclock 0 counter iTempUnlockCode
storeSuitInfoUnclock:
    LVAR_INT counter
    LVAR_INT inVal 
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER SUIT_data_Info (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    WRITE_MEMORY pActiveItem 4 inVal FALSE     
CLEO_RETURN 0
}
{
//CLEO_CALL StoreSuitItem 0 var
StoreSuitItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SuitItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL StorePowerSuitItem 0 var
StorePowerSuitItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_PowerSuitItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL storeSkillItem 0 var
storeSkillItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SkillItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getSkillItem 0 (var)
getSkillItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_SkillItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}

{
//CLEO_CALL drawBar 0 /*size*/1000.0
drawBar:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2] xSize ySize
    var[1] = sizeBar
    var[1] /= 1000.0 //fresX
    //var[1] *= 240.0
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 5.0) (xSize ySize)   //80.0 2.07
    var[1] *= 80.0
    xSize = var[1]
    ySize = 2.07
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 547.0 //547+(80/2)= 587
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (587.0 9.0) (80.0 ySize) (49 96 133 210)        //blue background
    USE_TEXT_COMMANDS FALSE
    DRAW_RECT (var[0] 9.0) (xSize ySize) (7 202 190 210)       //bar
CLEO_RETURN 0
}
{
//CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (1920.0 1080.0) (sizX sizY)
GetXYSizeInScreenScaleByUserResolution:
    LVAR_FLOAT x y // In pixels
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresX /= 640.0
    x /= fresX
    fresY /= 448.0
    y /= fresY
CLEO_RETURN 0 (x y) // out scale (640x448)
}
{
//CLEO_CALL getCurrentResolution 0 (fX fY)
getCurrentResolution:
    LVAR_INT iresX iresY
    LVAR_FLOAT fresX fresY
    GET_CURRENT_RESOLUTION (iresX iresY)
    fresX =# iresX
    fresY =# iresY
CLEO_RETURN 0 (fresX fresY)
}
{
//CLEO_CALL drawMap 0 (fX fY) (fSize)
drawMap:
    LVAR_FLOAT fX fY fSizeMap   //in
    LVAR_FLOAT fSizX fSizY
    GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idMapTexture (fX fY) (fSizX fSizY) (255 255 255 210)        //Image MAP  
CLEO_RETURN 0
}
{
//CLEO_CALL drawDataJoystickPos2DScreenCords 0 (fX fY) (fSize)
drawDataJoystickPos2DScreenCords:
    LVAR_FLOAT fCenterX fCenterY fSizeMap   //in
    LVAR_INT LRStick UDStick
    LVAR_FLOAT fLRStick fUDStick fHalfSizeMap fMaxValueX fMaxValueY fMinValueX fMinValueY fX fY
    fX = 399.0
    fY = 229.0
    /*
    fHalfSizeMap = fSizeMap
    fHalfSizeMap /= 2.0
    fMaxValueX = fX
    fMaxValueY = fY
    fMinValueX = fX
    fMinValueY = fY
    fMaxValueX += fHalfSizeMap
    fMaxValueY += fHalfSizeMap
    fMinValueX -= fHalfSizeMap
    fMinValueY -= fHalfSizeMap
    */
    LVAR_FLOAT fSizX fSizY fHalfSizX fHalfSizY
    GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
    fHalfSizX = fSizX
    fHalfSizX /= 2.0
    fHalfSizY = fSizY
    fHalfSizY /= 2.0
    fMaxValueX = fX
    fMaxValueY = fY
    fMinValueX = fX
    fMinValueY = fY
    fMaxValueX += fHalfSizX
    fMaxValueY += fHalfSizY
    fMinValueX -= fHalfSizX
    fMinValueY -= fHalfSizY
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    CSET_LVAR_FLOAT_TO_LVAR_INT (fLRStick) LRStick
    fLRStick /= 100.0
    CSET_LVAR_FLOAT_TO_LVAR_INT (fUDStick) UDStick
    fUDStick /= 100.0
    IF fMaxValueX > fCenterX    // Right Limit
        fCenterX +=@ fLRStick
    ELSE
        fCenterX = fMaxValueX
    ENDIF
    IF fMinValueX > fCenterX    // Left Limit
        fCenterX = fMinValueX
    ELSE
        fCenterX +=@ fLRStick
    ENDIF
    IF fMaxValueY > fCenterY    // Lower Limit
        fCenterY +=@ fUDStick
    ELSE
        fCenterY = fMaxValueY
    ENDIF
    IF fMinValueY > fCenterY    // Upper Limit
        fCenterY = fMinValueY
    ELSE
        fCenterY +=@ fUDStick
    ENDIF
    CLEO_CALL storeScreenCoords 0 fCenterX fCenterY
    //CLEO_CALL getScreenCoords 0 (xCoord yCoord)
    LVAR_FLOAT x y
    GET_FIXED_XY_ASPECT_RATIO 10.0 10.0 (x y)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idMapIcon0 (fCenterX fCenterY) (x y) (255 255 255 210)        //Pointer Icon Map
    //DRAW_RECT (fCenterX fCenterY) (2.0 2.0) (0 255 0 255)
    //MAP coords
    LVAR_FLOAT fGtaX fGtaY //fXCoord fYCoord fWaterHeight fGroundHeight
    fGtaX = 6000.0
    fGtaY = 6000.0
    IF IS_BUTTON_PRESSED PAD1 CROSS // ~k~~PED_FIREWEAPON~
        fCenterX -= fMinValueX
        //fCenterX /= fSizeMap
        fCenterX /= fSizX
        fCenterX *= fGtaX
        LVAR_FLOAT fTemp1
        fTemp1 = fGtaX
        fTemp1 /= 2.0
        fCenterX -= fTemp1
        //Y Coord
        fCenterY -= fMinValueY
        //fCenterY /= fSizeMap
        fCenterY /= fSizY
        fCenterY *= fGtaY
        fTemp1 = fGtaY
        fTemp1 /= 2.0
        fCenterY -= fTemp1
        fCenterY *= -1.0
        PRINT_FORMATTED_NOW "x:%f y:%f" 2000 fCenterX fCenterY //fGroundHeight
        //CLEO_CALL storeScreenCoords 0 fCenterX fCenterY //store markder position

        //LVAR_INT player_actor
        //GET_PLAYER_CHAR 0 player_actor
        //LOAD_SCENE fCenterX fCenterY 100.0
        //REQUEST_COLLISION fCenterX fCenterY
        //GET_GROUND_Z_FOR_3D_COORD fCenterX fCenterY 900.0 (fGroundHeight)
        /*
        GET_WATER_HEIGHT_AT_COORDS fCenterX fCenterY TRUE (fWaterHeight)
        IF fGroundHeight >= fWaterHeight
            //CLEO_CALL SetCharPosSimple 0 player_actor fCenterX fCenterY fGroundHeight
            //SET_CHAR_COORDINATES_NO_OFFSET player_actor fCenterX fCenterY -100.0
            //CLEAR_CHAR_TASKS player_actor
            //CLEAR_EXTRA_COLOURS FALSE
        ENDIF
        */
        //PRINT_FORMATTED_NOW "gh:%f  wh:%f" 1000 fGroundHeight fWaterHeight
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL drawChar3DCoordsTo2DScreenCords 0 (fSize)
drawChar3DCoordsTo2DScreenCords:
    LVAR_FLOAT fSizeMap //in
    LVAR_FLOAT fX fY
    LVAR_FLOAT fHalfSizeMap fGtaX fGtaY x y z
    LVAR_INT player_actor
    GET_PLAYER_CHAR 0 player_actor
    LVAR_FLOAT fMinValueX fMinValueY //fMaxValueX fMaxValueY
    fX = 399.0
    fY = 229.0
    /*
    fHalfSizeMap = fSizeMap
    fHalfSizeMap /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizeMap
    fMinValueY -= fHalfSizeMap
    */
    LVAR_FLOAT fSizX fSizY fHalfSizX fHalfSizY
    GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
    fHalfSizX = fSizX
    fHalfSizX /= 2.0
    fHalfSizY = fSizY
    fHalfSizY /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizX
    fMinValueY -= fHalfSizY
    fGtaX = 6000.0
    fGtaY = 6000.0
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x y z)
    y *= -1.0
    //X Coord
    LVAR_FLOAT fTemp1
    fTemp1 = fGtaX
    fTemp1 /= 2.0
    x += fTemp1
    x /= fGtaX
    //x *= fSizeMap
    x *= fSizX
    x += fMinValueX
    //Y Coord
    fTemp1 = fGtaY
    fTemp1 /= 2.0
    y += fTemp1
    y /= fGtaY
    //y *= fSizeMap
    y *= fSizY
    y += fMinValueY
    LVAR_FLOAT fZAngle
    GET_CHAR_HEADING player_actor (fZAngle)
    fZAngle -= 180.0
    fZAngle *= -1.0
    //DRAW_RECT (x y) (3.0 3.0) (255 0 0 255)
    GET_FIXED_XY_ASPECT_RATIO 17.0 17.0 (fSizX fSizY)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE_WITH_ROTATION idMapIcon2 (x y) (fSizX fSizY) fZAngle (255 255 255 210) //Player Icon POS
    /* X COORD
    x = (((centerX-MinX)/SizeMap)*6000)-3000
    x + 3000 = (((centerX-MinX)/SizeMap)*6000)
    (x + 3000)/6000 = ((centerX-MinX)/SizeMap)
    ((x + 3000)/6000)*SizeMap = (centerX-MinX)
    (((x + 3000)/6000)*SizeMap)+Minx  = (centerX)
    */
CLEO_RETURN 0
}


{
//CLEO_CALL drawOnMapIconMarks 0 idIcon (fSize)     //||landmarks:1 ||backpacks:2 ||crimes:3
drawOnMapIconMarks:
    LVAR_INT i     //in  ||landmarks:1 ||backpacks:2 ||crimes:3
    LVAR_FLOAT fSizeMap //in

    LVAR_FLOAT x y z
    LVAR_FLOAT fX fY fHalfSizeMap fGtaX fGtaY
    LVAR_FLOAT fMinValueX fMinValueY
    LVAR_INT player_actor iTempVar 
    LVAR_INT pActiveItem pTempVar counter iStartCounter iEndCounter

    GET_PLAYER_CHAR 0 player_actor
    fX = 399.0
    fY = 229.0
    /*
    fHalfSizeMap = fSizeMap
    fHalfSizeMap /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizeMap
    fMinValueY -= fHalfSizeMap
    */
    LVAR_FLOAT fSizX fSizY fHalfSizX fHalfSizY
    GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
    fHalfSizX = fSizX
    fHalfSizX /= 2.0
    fHalfSizY = fSizY
    fHalfSizY /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizX
    fMinValueY -= fHalfSizY
    fGtaX = 6000.0
    fGtaY = 6000.0

    IF i = 3
        iStartCounter = 0
        iEndCounter = 4
    ELSE
        iStartCounter = 0
        iEndCounter = 9
    ENDIF
    counter = iStartCounter
    WHILE iEndCounter >= counter
        CLEO_CALL get_map_coords 0 i counter (x y z)
        IF NOT x = 0.0
        AND NOT y = 0.0
        AND NOT z = 0.0
            CLEO_CALL get_existing_backpack 0 i counter (iTempVar)
            IF iTempVar = 0 // if backpack isn't collected
                SWITCH i   // ||landmarks:1 ||backpacks:2 ||crimes:3
                    CASE 1
                        GET_LABEL_POINTER CoordsLandMarksBuffer (pActiveItem)
                        BREAK
                    CASE 2
                        GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
                        BREAK
                    CASE 3
                        GET_LABEL_POINTER CoordsBuffer (pActiveItem)
                        BREAK
                ENDSWITCH
                GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
                y *= -1.0
                //X Coord
                LVAR_FLOAT fTemp1
                fTemp1 = fGtaX
                fTemp1 /= 2.0
                x += fTemp1
                x /= fGtaX
                //x *= fSizeMap
                x *= fSizX
                x += fMinValueX
                //Y Coord
                fTemp1 = fGtaY
                fTemp1 /= 2.0
                y += fTemp1
                y /= fGtaY
                //y *= fSizeMap
                y *= fSizY
                y += fMinValueY
                GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (fSizX fSizY)
                SWITCH i    //||landmarks:1 ||backpacks:2 ||crimes:3
                    CASE 1
                        USE_TEXT_COMMANDS FALSE
                        SET_SPRITES_DRAW_BEFORE_FADE TRUE
                        DRAW_SPRITE idMapIcon6 (x y) (fSizX fSizY) (255 255 255 210)        //LandMark Icon Map
                        BREAK
                    CASE 2
                        USE_TEXT_COMMANDS FALSE
                        SET_SPRITES_DRAW_BEFORE_FADE TRUE
                        DRAW_SPRITE idMapIcon3 (x y) (fSizX fSizY) (255 255 255 210)        //BackPack Icon Map
                        BREAK
                    CASE 3
                        USE_TEXT_COMMANDS FALSE
                        SET_SPRITES_DRAW_BEFORE_FADE TRUE
                        DRAW_SPRITE idMapIcon5 (x y) (fSizX fSizY) (255 255 255 210)        //Crime Icon Map
                        BREAK
                ENDSWITCH

            ENDIF
        ENDIF
        counter ++
    ENDWHILE

    //PRINT_FORMATTED_NOW "rX:%f rY:%f x:%f y:%f" 1 retX retY x y

CLEO_RETURN 0
}




{
//CLEO_CALL drawLandMarks 0 (fSize)
drawLandMarks:
    LVAR_FLOAT fSizeMap
    LVAR_FLOAT x y z
    LVAR_FLOAT fX fY fHalfSizeMap fGtaX fGtaY
    LVAR_FLOAT fMinValueX fMinValueY
    LVAR_INT player_actor

    GET_PLAYER_CHAR 0 player_actor
    fX = 399.0
    fY = 229.0
    /*
    fHalfSizeMap = fSizeMap
    fHalfSizeMap /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizeMap
    fMinValueY -= fHalfSizeMap
    */
    LVAR_FLOAT fSizX fSizY fHalfSizX fHalfSizY
    GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
    fHalfSizX = fSizX
    fHalfSizX /= 2.0
    fHalfSizY = fSizY
    fHalfSizY /= 2.0
    fMinValueX = fX
    fMinValueY = fY
    fMinValueX -= fHalfSizX
    fMinValueY -= fHalfSizY
    fGtaX = 6000.0
    fGtaY = 6000.0

    //CLEO_CALL readIni3DCoords 0 (x y z)
    LVAR_INT pActiveItem pTempVar
    LVAR_INT counter
    LVAR_INT i      //||landmarks:1 ||backpacks:2 ||crimes:3

    i = 1
    WHILE 2 >= i
        counter = 0
        WHILE 9 >= counter
            x = 0.0
            y = 0.0
            IF i = 1    //||landmarks:1 ||backpacks:2 ||crimes:3
                GET_LABEL_POINTER CoordsLandMarksBuffer (pActiveItem)
            ELSE
                GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
            ENDIF
            pTempVar = counter
            pTempVar *= 12
            pActiveItem += pTempVar
            READ_MEMORY (pActiveItem) 4 FALSE (x)
            pActiveItem += 4
            READ_MEMORY (pActiveItem) 4 FALSE (y)
            //pActiveItem += 4
            //READ_MEMORY (pActiveItem) 4 FALSE (z)
            IF NOT x = 0.0
            AND NOT y = 0.0
                GET_FIXED_XY_ASPECT_RATIO (fSizeMap fSizeMap) (fSizX fSizY)
                y *= -1.0
                //X Coord
                LVAR_FLOAT fTemp1
                fTemp1 = fGtaX
                fTemp1 /= 2.0
                x += fTemp1
                x /= fGtaX
                //x *= fSizeMap
                x *= fSizX
                x += fMinValueX
                //Y Coord
                fTemp1 = fGtaY
                fTemp1 /= 2.0
                y += fTemp1
                y /= fGtaY
                //y *= fSizeMap
                y *= fSizY
                y += fMinValueY
                GET_FIXED_XY_ASPECT_RATIO 15.0 15.0 (fSizX fSizY)
                IF i = 1    //||landmarks:1 ||backpacks:2 ||crimes:3
                    USE_TEXT_COMMANDS FALSE
                    SET_SPRITES_DRAW_BEFORE_FADE TRUE
                    DRAW_SPRITE idMapIcon6 (x y) (fSizX fSizY) (255 255 255 210)        //LandMark Icon Map
                ELSE
                    USE_TEXT_COMMANDS FALSE
                    SET_SPRITES_DRAW_BEFORE_FADE TRUE
                    DRAW_SPRITE idMapIcon3 (x y) (fSizX fSizY) (255 255 255 210)        //BackPack Icon Map
                ENDIF
                /*IF 9 >= counter
                ELSE
                ENDIF*/
            ENDIF
            counter ++
        ENDWHILE
        i ++
    ENDWHILE
    //PRINT_FORMATTED_NOW "rX:%f rY:%f x:%f y:%f" 1 retX retY x y
CLEO_RETURN 0
}
{
//CLEO_CALL storeScreenCoords 0 xCoord yCoord
storeScreenCoords:
    LVAR_FLOAT xCoord yCoord
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ScreenCoords pActiveItem
    WRITE_MEMORY pActiveItem 4 xCoord FALSE
    pActiveItem += 4
    WRITE_MEMORY pActiveItem 4 yCoord FALSE
CLEO_RETURN 0 
}
{
//CLEO_CALL getScreenCoords 0 (xCoord yCoord)
getScreenCoords:
    LVAR_FLOAT xCoord yCoord
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ScreenCoords (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (xCoord)  
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (yCoord)  
CLEO_RETURN 0 xCoord yCoord
}
{
//CLEO_CALL setActorBrightness 0 iBaseActor fVar
setActorBrightness:
LVAR_INT iActor //in
LVAR_FLOAT fBrightness
LVAR_INT pActor
IF DOES_CHAR_EXIST iActor
    GET_PED_POINTER iActor (pActor)
    WRITE_STRUCT_OFFSET pActor 0x12C 4 fBrightness
ENDIF
CLEO_RETURN 0
}

//-+--- Shine GUI
{
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*ouline*/(1.4 1 1 1 1 200 200 200 200) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawBoxOutline_WithText:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_FLOAT outlineSize 
LVAR_INT outlineTop outlineRight outlineBottom outlineLeft outlineR outlineG outlineB outlineA textId formatId
LVAR_FLOAT paddingLeft paddingTop

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Outlines
IF outlineLeft = TRUE //outline side left
    f = sizeX / 2.0  
    h = posX - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineTop = TRUE //outline side top
    f = sizeY / 2.0  
    h = posY - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF  
IF outlineRight = TRUE //outline side right
    f = sizeX / 2.0    
    h = posX + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineBottom = TRUE //outline side bottom
    f = sizeY / 2.0  
    h = posY + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //Menu Item
            formatId = 8 //Menu Item ACTIVE
        ENDIF  
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_DrawBox_WithNumber 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0 /*number*/ 5
GUI_DrawBox_WithNumber:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_INT textId formatId
LVAR_FLOAT paddingLeft paddingTop
LVAR_INT iNumber

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //
            formatId = 8 // ACTIVE
        ENDIF
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF  
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT_WITH_NUMBER (posX posY) $gxt iNumber
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID:
LVAR_INT formatId //In
LVAR_INT i
LVAR_FLOAT g
LVAR_FLOAT xSize ySize
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Title3_LeftMenu
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Title4_Suits  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_Title2_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_Subtitle_Medium_Names  
        CLEO_RETURN 0 3.5
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_Title1_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_Title1_Menu_Active  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_Text1_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_Number1_Big_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_Text2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_Text3_Medium  
        CLEO_RETURN 0 3.0
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_Text4_Big  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_Number2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_Number3_Small  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 16
        GOSUB GUI_TextFormat_Title5_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 17
        GOSUB GUI_TextFormat_Text5_List_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 18
        GOSUB GUI_TextFormat_Interface_Main_Title  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 19
        GOSUB GUI_TextFormat_Interface_Normal_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 20
        GOSUB GUI_TextFormat_Interface_A_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 21
        GOSUB GUI_TextFormat_Interface_Number_Big
        CLEO_RETURN 0 0.0
        BREAK
ENDSWITCH

GUI_TextFormat_Title3_LeftMenu:    //1   Title 3 - Suit/SuitMods  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1 
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN    

GUI_TextFormat_Title4_Suits:     //2     Title 4 - Suits Matrix  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.28 1.05 (xSize ySize)    //0.21 0.98
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    /*
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT 2
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 0 0 0 100
    SET_TEXT_SCALE 0.3 1.0
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    */
RETURN

GUI_TextFormat_SmallMenu: //3
    SET_TEXT_COLOUR 240 240 240 255  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_SmallMenu_Active: //4    
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Title2_Menu:  //5   Title 2  (GRAY)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.09 (xSize ySize)    //0.22 1.02
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 200 200 200 255
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Subtitle_Medium_Names:  //6  Text Medium / Names  (LIGHT-BLUE)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 50 180 255 200
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Title1_Menu:   //7  Title 1    (WHITE-GRAY)
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title1_Menu_Active:  //8  Title 1 Active   (LIGHT-BLUE)
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Text1_Small_Colour:  //9   (BLUE)
    SET_TEXT_COLOUR 0 95 160 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number1_Big_Colour:  //10  BIG Numbers for Level (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.74 2.73 (xSize ySize)    //0.55 2.55
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text2_Small_Colour:  //11   LEVEL letters small (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.19 0.7 (xSize ySize)    //0.14 0.65
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text3_Medium:  //12     Text Descriptions  (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text4_Big:   //13     Text Names   (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.38 1.45 (xSize ySize)    //0.28 1.35
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN   

GUI_TextFormat_Number2_Small_Colour:  //14   Level Numbers  (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number3_Small:  //15   Level XP Numbers  (WHITE)
    SET_TEXT_COLOUR 255 255 255 220  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title5_Map:  //16  Title Map   (DARK-BLUE)
    SET_TEXT_COLOUR 43 57 58 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (43 57 58 100)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text5_List_Map:  //17  Text List Map (MAGENTA)
    SET_TEXT_COLOUR 17 242 198 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Main_Title:  //18  Text BIG (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.14 (xSize ySize)    //0.22 1.06
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Normal_Text:  //19  Text (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_A_Text:  //20  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Number_Big:  //21  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.4 1.55 (xSize ySize)    //0.30 1.45
    GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
// --- Functions
{
GUI_GetPulseAlpha:
    LVAR_INT i
    LVAR_FLOAT g
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha i
    READ_MEMORY i 4 FALSE (g)
    i =# g
CLEO_RETURN 0 i
}
{
GUI_SetAtiveGXT:
    LVAR_INT item //In
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Update:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    CONST_FLOAT ItemPulseSpeed 2.0
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    READ_MEMORY pAlpha 4 FALSE (fAlpha)
    READ_MEMORY pStep 1 FALSE (iStep)
    IF iStep = 1
        fAlpha -=@ ItemPulseSpeed  
        IF fAlpha < 180.0
            fAlpha = 180.0
            iStep = 2
        ENDIF
    ELSE //Up
        fAlpha +=@ ItemPulseSpeed  
        IF fAlpha > 255.0
            fAlpha = 255.0
            iStep = 1
        ENDIF
    ENDIF
    WRITE_MEMORY pAlpha 4 fAlpha FALSE
    WRITE_MEMORY pStep 1 iStep FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Reset:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    WRITE_MEMORY pAlpha 4 255.0 FALSE
    WRITE_MEMORY pStep 1 1 FALSE
CLEO_RETURN 0
}
{
StoreActiveItem:
    LVAR_INT item // Item
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}

{
//CLEO_CALL freezeGame 0 (1)
freezeGame:
    LVAR_INT iValue
    SWITCH iValue
        CASE 0
            //DISABLE
            WRITE_MEMORY 0xB7CB49 1 0 FALSE  // freeze
            WRITE_MEMORY 0x53BF8A 2 0xC084 TRUE  // frozen game stuff
            WRITE_MEMORY 0x53BF96 4 0x0298850F TRUE // frozen game stuff
            WRITE_MEMORY 0x53BF9A 2 0x0 TRUE  // frozen game stuff
            //WRITE_MEMORY 0x58FCC2 4 0x4D75C084 TRUE // frozen game text draw
            //WRITE_MEMORY 0x58D4BE 4 0x850FC084 TRUE // frozen game texture draw
            //WRITE_MEMORY 0x58D4C2 4 0x000000AF TRUE // frozen game texture draw  
        BREAK
        CASE 1
            //ENABLE
            WRITE_MEMORY 0xB7CB49 1 1 FALSE  // freeze
            WRITE_MEMORY 0x53BF8A 2 0x9090 TRUE  // frozen game stuff
            WRITE_MEMORY 0x53BF96 4 0x90909090 TRUE // frozen game stuff
            WRITE_MEMORY 0x53BF9A 2 0x9090 TRUE  // frozen game stuff
            //WRITE_MEMORY 0x58FCC2 4 0x90909090 TRUE // frozen game text draw
            //WRITE_MEMORY 0x58D4BE 4 0x90909090 TRUE // frozen game texture draw
            //WRITE_MEMORY 0x58D4C2 4 0x90909090 TRUE // frozen game texture draw   
        BREAK
    ENDSWITCH
CLEO_RETURN 0
}

// Thread Memory
GUI_Memory_toggleSpiderMod:
DUMP
00 00 00 00
ENDDUMP

WORLD_weather:
DUMP
00 00 
00 00
ENDDUMP

CHAR_world_coords:
DUMP
00 00 00 00 //x
00 00 00 00 //y
00 00 00 00 //z
00 00 00 00 //angle
ENDDUMP

buffer_skills_bytes10:
DUMP
00 00 00 00 00 00 00 00 00 00
ENDDUMP

CoordsBuffer:
DUMP
//landmarks
00000000 00000000 00000000  // 12bytes
00000000 00000000 00000000  // 24bytes
00000000 00000000 00000000  // 36bytes
00000000 00000000 00000000  // 48bytes
00000000 00000000 00000000  // 60bytes
/*
00000000 00000000 00000000  // 72bytes
00000000 00000000 00000000  // 84bytes
00000000 00000000 00000000  // 96bytes
00000000 00000000 00000000  // 108bytes
00000000 00000000 00000000  // 120bytes
*/
/*
00000000 00000000 00000000  // 132bytes
00000000 00000000 00000000  // 144bytes
00000000 00000000 00000000  // 156bytes
00000000 00000000 00000000  // 168bytes
00000000 00000000 00000000  // 180bytes
00000000 00000000 00000000  // 192bytes
00000000 00000000 00000000  // 204bytes
00000000 00000000 00000000  // 216bytes
00000000 00000000 00000000  // 228bytes
00000000 00000000 00000000  // 240bytes
00000000 00000000 00000000  // 252bytes
00000000 00000000 00000000  // 264bytes
00000000 00000000 00000000  // 276bytes
*/
/*
//backpacks
00000000 00000000 00000000  // 288bytes
00000000 00000000 00000000  // 300bytes
00000000 00000000 00000000  // 312bytes
*/
ENDDUMP

CoordsLandMarksBuffer:
DUMP
//landmarks
00000000 00000000 00000000  // 12bytes
00000000 00000000 00000000  // 24bytes
00000000 00000000 00000000  // 36bytes
00000000 00000000 00000000  // 48bytes
00000000 00000000 00000000  // 60bytes
00000000 00000000 00000000  // 72bytes
00000000 00000000 00000000  // 84bytes
00000000 00000000 00000000  // 96bytes
00000000 00000000 00000000  // 108bytes
00000000 00000000 00000000  // 120bytes
ENDDUMP

CoordsBackpacksBuffer:
DUMP
//backpacks
00000000 00000000 00000000  // 12bytes
00000000 00000000 00000000  // 24bytes
00000000 00000000 00000000  // 36bytes
00000000 00000000 00000000  // 48bytes
00000000 00000000 00000000  // 60bytes
00000000 00000000 00000000  // 72bytes
00000000 00000000 00000000  // 84bytes
00000000 00000000 00000000  // 96bytes
00000000 00000000 00000000  // 108bytes
00000000 00000000 00000000  // 120bytes
ENDDUMP

bytes32:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
ENDDUMP

buffer_backpacks_bytes10:
DUMP
00 00 00 00 00 00 00 00 00 00
ENDDUMP

buffer_landmarks_bytes10:
DUMP
00 00 00 00 00 00 00 00 00 00
ENDDUMP

buffer_crimes_bytes5:
DUMP
00 00 00 00 00
ENDDUMP

SFX_sound_effects:
DUMP
00 00 00 00 //+0
00 00 00 00 //+4
00 00 00 00 //+8 
00 00 00 00 //+12
ENDDUMP

GUI_Memory_ThrowDoorsItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_FNItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_SpiderDriveCarsItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_AutoAimItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_MouseControlItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_FixGroundItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_SwingBuildingsItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_AlternativeItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_SkillItem:
DUMP
00 00 00 00
ENDDUMP

SUIT_data_Info:
DUMP    // 124 bytes
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00

00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP

GUI_Memory_SuitItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_PowerSuitItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ScreenCoords:
DUMP
00 00 00 00 //x
00 00 00 00 //y
ENDDUMP


GUI_Memory_ActiveItem:
DUMP
00 00 00 00
ENDDUMP
  
GUI_Memory_ItemMenuActive_PulseAlpha:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ItemMenuActive_PulseAlpha_Step:
DUMP
00
ENDDUMP


//-+---CONSTANTS--------------------
//Weather Values
CONST_INT EXTRASUNNY_LA         0
CONST_INT SUNNY_LA              1
CONST_INT EXTRASUNNY_SMOG_LA    2
CONST_INT SUNNY_SMOG_LA         3
CONST_INT CLOUDY_LA             4
CONST_INT SUNNY_SF              5
CONST_INT EXTRASUNNY_SF         6
CONST_INT CLOUDY_SF             7
CONST_INT RAINY_SF              8
CONST_INT FOGGY_SF              9
CONST_INT SUNNY_VEGAS           10
CONST_INT EXTRASUNNY_VEGAS      11
CONST_INT CLOUDY_VEGAS          12
CONST_INT EXTRASUNNY_COUNTRYSIDE 13
CONST_INT SUNNY_COUNTRYSIDE     14
CONST_INT CLOUDY_COUNTRYSIDE    15
CONST_INT RAINY_COUNTRYSIDE     16
CONST_INT EXTRASUNNY_DESERT     17
CONST_INT SUNNY_DESERT          18
CONST_INT SANDSTORM_DESERT      19
//ID_TEXT_LABELS
CONST_INT idArrowLeft_l         0
CONST_INT idOptions_l           1
CONST_INT idMap_l               2
CONST_INT idSuits_l             3
CONST_INT idSkills_l            4
CONST_INT idCharacters_l        5
CONST_INT idMoves_l             6
CONST_INT idVersionInfo_l       7
//CONST_INT idArrowRight_l        7

CONST_INT idSelect_l            67
CONST_INT idRotate_l            8
CONST_INT idEquip_l             9
CONST_INT idClose_l             10
CONST_INT idOwned_l             11
CONST_INT idBack_l              12
CONST_INT idLocked_l            13
CONST_INT idEquipped_l          14
CONST_INT idLevel_l             15
//CONST_INT idDefense_l           16
//CONST_INT idOffense_l           17
//CONST_INT idStealth_l           18
//CONST_INT idTraversal_l         19
CONST_INT idNoPowerText_l       20
CONST_INT idUnlocksPowerText_l  21
CONST_INT idSuit_l              22
CONST_INT idSuitPower_l         23
CONST_INT idSuitMods_l          24
CONST_INT idSuit1_l             25
CONST_INT idSuit2_l             26
CONST_INT idSuit3_l             27
CONST_INT idSuit4_l             28
CONST_INT idSuit5_l             29
CONST_INT idSuit6_l             30
CONST_INT idSuit7_l             31
CONST_INT idSuit8_l             32
CONST_INT idSuit9_l             33
CONST_INT idSuit10_l            34
CONST_INT idSuit11_l            35
CONST_INT idSuit12_l            36
CONST_INT idSuit13_l            37
CONST_INT idSuit14_l            38
CONST_INT idSuit15_l            39
CONST_INT idSuit16_l            40
CONST_INT idSuit17_l            41
CONST_INT idSuit18_l            42
CONST_INT idSuit19_l            43
CONST_INT idSuit20_l            44
CONST_INT idSuit21_l            45
CONST_INT idSuit22_l            46
CONST_INT idSuit23_l            47
CONST_INT idSuit24_l            48
CONST_INT idSuit25_l            49
CONST_INT idSuit26_l            50
CONST_INT idSuit27_l            51
CONST_INT idSuit28_l            52
CONST_INT idSuit29_l            53
CONST_INT idSuit30_l            54
CONST_INT idSuitUnknown_l       59

CONST_INT idWarningTITLE_l      60
CONST_INT idWarningMsg_l        61
CONST_INT idVersionMsg_l        62
CONST_INT idAuthorMsg_l         63
CONST_INT idc2019AuthorMsg_l    64

//Config Panel
CONST_INT idAlternativeSwing_l  100
CONST_INT idSwingBuildings_l    101
CONST_INT idFixGround_l         102
CONST_INT idControlMouse_l      103
CONST_INT idAutoAim_l           104
CONST_INT idSpiderCanDrive_l    105
CONST_INT idFriendlyN_l         106
CONST_INT idThrowDoors_l        107

CONST_INT idActivateTITLE_l     110
CONST_INT idOption2_l           111
CONST_INT idEnableMusic         112
CONST_INT idOption3_l           113

CONST_INT idLevelTITLE_l        120
CONST_INT idLevelNumber_l       121

//Power
CONST_INT idNoPower_l           400
CONST_INT idPower1_l            401
CONST_INT idPower2_l            402
CONST_INT idPower3_l            403
CONST_INT idPower4_l            404
CONST_INT idPower5_l            405
CONST_INT idPower6_l            406
CONST_INT idPower7_l            407
CONST_INT idPower8_l            408
CONST_INT idPower9_l            409
CONST_INT idPower10_l           410
CONST_INT idPower11_l           411
CONST_INT idPower12_l           412

//Text key_press activation
CONST_INT idPower_KeyPress      450



