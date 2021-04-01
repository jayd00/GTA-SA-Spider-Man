// by J16D
// Gadget Select Script
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Others
CONST_INT delay_mstime          100

//TYPE SPIDER-WEAP
CONST_INT weap_web_shoot 1
CONST_INT weap_concussive_blast 2
CONST_INT weap_impact_web 3
CONST_INT weap_spyder_drone 4
CONST_INT weap_electric_web 5
CONST_INT weap_suspension_matrix 6
CONST_INT weap_web_bomb 7
CONST_INT weap_trip_mine 8

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_mm
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu   //1:true 0: false
LVAR_INT iTempVar iTempVar2 LRStick UDStick iWidth iHeight
LVAR_FLOAT fWidth fHeight fSizeX fSizeY
LVAR_INT sfx[3]

GET_PLAYER_CHAR 0 player_actor
iTempVar = 1
SET_CLEO_SHARED_VAR varIdWebWeapon iTempVar     //1-8
GOSUB load_texture_files
GOSUB load_sfx_menu
USE_TEXT_COMMANDS TRUE
USE_TEXT_COMMANDS FALSE
SET_PLAYER_DISPLAY_VITAL_STATS_BUTTON player FALSE

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
    
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1 // PED_ANSWER_PHONE, PED_FIREWEAPON_ALT
                AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
                    iTempVar2 = 0   //show sfx
                    GOSUB play_sfx_menu
                    SET_PLAYER_CONTROL_PAD_MOVEMENT PAD1 FALSE  //disables player movement without camera
                    SET_TIME_SCALE 0.1

                    WHILE IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1 // PED_ANSWER_PHONE, PED_FIREWEAPON_ALT
                    AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
                        
                        GOSUB drawItems
                        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                        IF LRStick > 0  //LEFT
                        //OR 0 > UDStick  //UP
                            iTempVar += 1
                            CLEO_CALL max_min_value_int 0 iTempVar 8 1 (iTempVar)
                            SET_CLEO_SHARED_VAR varIdWebWeapon iTempVar       // 1-8
                            iTempVar2 = 1   //change sfx
                            GOSUB play_sfx_menu
                        ELSE
                            IF 0 > LRStick  //RIGHT
                            //OR UDStick > 0  //DOWN
                                iTempVar -= 1
                                CLEO_CALL max_min_value_int 0 iTempVar 8 1 (iTempVar)
                                SET_CLEO_SHARED_VAR varIdWebWeapon iTempVar       // 1-8
                                iTempVar2 = 1   //change sfx
                                GOSUB play_sfx_menu
                            ENDIF
                        ENDIF

                        timera = 0
                        WHILE TRUE
                            IF IS_BUTTON_PRESSED PAD1 LEFTSTICKX     //GO_LEFT / GO_RIGHT
                            //OR IS_BUTTON_PRESSED PAD1 LEFTSTICKY     //GO_FORWARD / GO_BACK
                                IF delay_mstime > timera
                                    GOSUB drawItems
                                ELSE
                                    BREAK
                                ENDIF
                                IF  IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
                                    BREAK
                                ENDIF
                            ELSE
                                BREAK
                            ENDIF
                        ENDWHILE

                    ENDWHILE
                    SET_PLAYER_CONTROL_PAD_MOVEMENT PAD1 TRUE  //restore player movement
                    SET_TIME_SCALE 1.0
                    iTempVar2 = 2   //close sfx
                    GOSUB play_sfx_menu
                    WAIT 0
                    SET_TIME_SCALE 1.0
                    SET_PLAYER_CONTROL_PAD_MOVEMENT PAD1 TRUE  //restore player movement

                ELSE
                    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
                        SET_TIME_SCALE 1.0
                        WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1 // PED_LOCK_TARGET
                            WAIT 0
                        ENDWHILE
                    ENDIF
                ENDIF

            ENDIF
        
        ELSE
            USE_TEXT_COMMANDS TRUE
            USE_TEXT_COMMANDS FALSE
            SET_PLAYER_DISPLAY_VITAL_STATS_BUTTON player TRUE
            WAIT 50
            REMOVE_AUDIO_STREAM sfx[0]
            REMOVE_AUDIO_STREAM sfx[1]
            REMOVE_AUDIO_STREAM sfx[2]
            REMOVE_TEXTURE_DICTIONARY
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
RETURN

drawItems:
    GET_CURRENT_RESOLUTION (iWidth iHeight)
    fWidth =# iWidth 
    fHeight =# iHeight
    GET_FIXED_XY_ASPECT_RATIO (fWidth fHeight) (fSizeX fSizeY)
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE gBack70 (320.0 224.0) (fSizeX fSizeY) (255 255 255 255)

    GET_FIXED_XY_ASPECT_RATIO (235.0 235.0) (fSizeX fSizeY)
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE gadgetBase (320.0 224.0) (fSizeX fSizeY) (255 255 255 255)
    GOSUB readVars
    counter = 1     //Textures 1-8 Weapons 
    WHILE 8 >= counter
        IF iTempVar = counter
            USE_TEXT_COMMANDS FALSE
            DRAW_SPRITE iTempVar (320.0 224.0) (fSizeX fSizeY) (255 255 255 255)
        ENDIF
        counter += 1
    ENDWHILE
    //DRAW_SPRITE gIcon59 (320.0 224.0) (fSizeX fSizeY) (255 255 255 255)
    //DRAW_SPRITE gIcon61 (320.0 224.0) (fSizeX fSizeY) (255 255 255 255)
    //USE_TEXT_COMMANDS FALSE
    WAIT 0
RETURN

load_texture_files:
    //LVAR_TEXT_LABEL lSuitName
    LVAR_INT counter
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        //TEXTURES
        CONST_INT gadgetBase 50
        CONST_INT gIcon59 59
        CONST_INT gIcon60 60
        CONST_INT gIcon61 61
        CONST_INT gBack70 70

        LOAD_TEXTURE_DICTIONARY spwp
        LOAD_SPRITE gBack70 "spb0"
        LOAD_SPRITE gadgetBase "spb1"
        /*
        counter = 1     //Textures 1-8 Weapons 
        WHILE 8 >= counter
            STRING_FORMAT (lSuitName)"sw%i" counter
            LOAD_SPRITE counter $lSuitName
            counter += 1
        ENDWHILE
        */
        LOAD_SPRITE 1 "sw1"
        LOAD_SPRITE 2 "sw2"
        LOAD_SPRITE 3 "sw3"
        LOAD_SPRITE 4 "sw4"
        LOAD_SPRITE 5 "sw5"
        LOAD_SPRITE 6 "sw6"
        LOAD_SPRITE 7 "sw7"
        LOAD_SPRITE 8 "sw8"
        //LOAD_SPRITE gIcon59 "sw9"
        //LOAD_SPRITE gIcon61 "sw11"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

load_sfx_menu:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\gadget_show.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\gadget_show.mp3" (sfx[0])
    ELSE
        sfx[0] = -1
    ENDIF
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\gadget_change.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\gadget_change.mp3" (sfx[1])
    ELSE
        sfx[1] = -1
    ENDIF
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\gadget_close.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\gadget_close.mp3" (sfx[2])
    ELSE
        sfx[2] = -1
    ENDIF
RETURN

play_sfx_menu:
    IF NOT sfx[iTempVar2] = -1
        SET_AUDIO_STREAM_STATE sfx[iTempVar2] 1
        SET_AUDIO_STREAM_VOLUME sfx[iTempVar2] 0.4
    ENDIF
RETURN

}
SCRIPT_END

//-+--- CALL SCM HELPERS
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
//CLEO_CALL max_min_value_int 0 iValue iMax iMin (iValue) 
max_min_value_int:
    LVAR_INT iValue
    LVAR_INT iMax iMin
    IF iValue > iMax
        iValue = iMin
    ENDIF
    IF iValue < iMin
        iValue = iMax
        //iValue = (iMax - 1)
    ENDIF
CLEO_RETURN 0 iValue
}


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

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
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
