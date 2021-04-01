// by J16D
// Stealth (Hidden player) AI visibility
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME sp_st
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu
LVAR_INT target iHour i pChar
LVAR_FLOAT fAmountLight fHiddenVal x y z fMaxLight

GET_PLAYER_CHAR 0 player_actor

main_loop:
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE

            IF IS_CHAR_REALLY_IN_AIR player_actor
                GOSUB enable_hidden_char
            ELSE
                GET_CURRENT_HOUR (iHour)
                IF iHour > 6    //6am
                AND 19 > iHour  //7pm
                    fMaxLight = 0.50
                ELSE
                    fMaxLight = 0.30
                ENDIF

                GET_CHAR_COLLISION_LIGHTING player_actor (fAmountLight)
                IF fMaxLight >= fAmountLight
                    IF IS_CHAR_DUCKING player_actor

                        i = 0
                        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i pChar)
                            IF DOES_CHAR_EXIST pChar
                            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor pChar
                                IF NOT IS_CHAR_IN_ANY_CAR pChar
                                AND NOT IS_CHAR_ON_ANY_BIKE pChar
                                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE pChar

                                    IF LOCATE_CHAR_DISTANCE_TO_CHAR pChar player_actor 8.0
                                        //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar 0.0 0.0 1.0 (x y z)
                                        //DRAW_CORONA x y z 0.20 CORONATYPE_SHINYSTAR FLARETYPE_NONE 255 0 0
                                        IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT pChar player_actor
                                            GOSUB disable_hidden_char
                                        ELSE
                                            GOSUB enable_hidden_char
                                        ENDIF
                                    ELSE
                                        GOSUB enable_hidden_char
                                    ENDIF

                                ENDIF
                            ENDIF
                        ENDWHILE

                    ELSE
                        GOSUB disable_hidden_char
                    ENDIF
                ELSE
                    GOSUB disable_hidden_char
                ENDIF

            ENDIF

        ELSE
            GOSUB disable_hidden_char
            WAIT 0
            USE_TEXT_COMMANDS FALSE
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

enable_hidden_char:
    READ_MEMORY 0x8D2380 4 FALSE (fHiddenVal)
    IF fHiddenVal > 0.0
        fHiddenVal = 0.00
        WRITE_MEMORY 0x8D2380 4 fHiddenVal FALSE    //0x8D2380 this value is used for AI visibility limit, default 0.3
    ENDIF
RETURN

disable_hidden_char:
    READ_MEMORY 0x8D2380 4 FALSE (fHiddenVal)
    IF fHiddenVal = 0.0
        fHiddenVal = 0.30
        WRITE_MEMORY 0x8D2380 4 fHiddenVal FALSE    
    ENDIF
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