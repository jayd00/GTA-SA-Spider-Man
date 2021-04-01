// by J16D
// Auto-Fill Web Ammo
// Spider-Man Mod for GTA SA c.2018 - 2021
/*Author Note: there is a bug with this script, when the current "weapon" has more Ammo than the other ones, 
and you change "weapon", this new selected weapon will have the same Ammo as the first one
Seems that the solution is re-made this script with independent ammo counter for each "weapon"*/

//-+---CONSTANTS--------------------
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
SCRIPT_NAME sp_wep
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu id_weap 
LVAR_INT is_delay_set on_mission
LVAR_INT iMaxAmmo iCurrentAmmo curret_weapon_id last_weapon_id
LVAR_INT iTempVar delay

GET_PLAYER_CHAR 0 player_actor
GOSUB getMaxAmmo
GOSUB store_initial_ammo
is_delay_set = FALSE
GET_CLEO_SHARED_VAR varIdWebWeapon (last_weapon_id)

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1 // PED_ANSWER_PHONE, PED_FIREWEAPON_ALT
                    WHILE IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1 // PED_ANSWER_PHONE, PED_FIREWEAPON_ALT
                        GET_CLEO_SHARED_VAR varIdWebWeapon (curret_weapon_id)
                        IF NOT curret_weapon_id = last_weapon_id
                            GET_CLEO_SHARED_VAR varIdWebWeapon (id_weap)    //get current weap
                            CLEO_CALL get_current_ammo 0 id_weap (iCurrentAmmo) //get ammo
                            SET_CLEO_SHARED_VAR varWeapAmmo iCurrentAmmo
                            last_weapon_id = curret_weapon_id
                        ENDIF
                        WAIT 0
                    ENDWHILE
                    GOSUB getMaxAmmo
                    GOSUB setMaxAmmo
                    is_delay_set = FALSE
                ENDIF
                GOSUB getMaxAmmo
                GET_CLEO_SHARED_VAR varWeapAmmo (iCurrentAmmo)
                IF iMaxAmmo > iCurrentAmmo
                    IF is_delay_set = FALSE
                        timera = 0
                        GOSUB get_weapon_delay_reload
                        is_delay_set = TRUE
                    ENDIF
                ENDIF
                IF is_delay_set = TRUE
                    IF timera > delay
                        iCurrentAmmo ++
                        CLAMP_INT iCurrentAmmo 0 iMaxAmmo (iCurrentAmmo)
                        SET_CLEO_SHARED_VAR varWeapAmmo iCurrentAmmo
                        GET_CLEO_SHARED_VAR varIdWebWeapon (id_weap)    //get current weap
                        CLEO_CALL store_current_ammo 0 id_weap iCurrentAmmo
                        is_delay_set = FALSE
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            USE_TEXT_COMMANDS FALSE
            WAIT 100
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF

    ENDIF
    WAIT 0
GOTO main_loop  

//-+-------------------------- GET ----------------------
readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

getMaxAmmo:
    GET_CLEO_SHARED_VAR varIdWebWeapon (id_weap)    //get current weap
    IF id_weap = weap_web_shoot
        iMaxAmmo = 10
    ELSE
        IF id_weap = weap_concussive_blast
        OR id_weap = weap_impact_web
        OR id_weap = weap_spyder_drone
        OR id_weap = weap_electric_web
        OR id_weap = weap_web_bomb
            iMaxAmmo = 4
        ELSE
            IF id_weap = weap_suspension_matrix
            OR id_weap = weap_trip_mine        
                iMaxAmmo = 3
            ENDIF
        ENDIF
    ENDIF
RETURN

get_weapon_delay_reload:
    GET_CLEO_SHARED_VAR varIdWebWeapon (id_weap)    //get current weap
    IF id_weap = weap_web_shoot
        delay = 4000    //ms
    ELSE
        IF id_weap = weap_electric_web
            delay = 10000    //ms
        ELSE
            IF  id_weap = weap_spyder_drone
                delay = 20000    //ms
            ELSE
                delay = 15000    //ms
            ENDIF
        ENDIF
    ENDIF
RETURN

//-+-------------------------- SET ----------------------
setMaxAmmo:
    GET_CLEO_SHARED_VAR varWeapAmmo (iCurrentAmmo)
    IF iCurrentAmmo > iMaxAmmo
        GET_CLEO_SHARED_VAR varIdWebWeapon (id_weap)    //get current weap
        CLEO_CALL get_current_ammo 0 id_weap (iCurrentAmmo) //get ammo
        SET_CLEO_SHARED_VAR varWeapAmmo iCurrentAmmo
    ENDIF
RETURN

store_initial_ammo:
    id_weap = 1
    WHILE 8 >= id_weap
        IF id_weap = weap_web_shoot
            iCurrentAmmo = 10
        ELSE
            IF id_weap = weap_concussive_blast
            OR id_weap = weap_impact_web
            OR id_weap = weap_spyder_drone
            OR id_weap = weap_electric_web
            OR id_weap = weap_web_bomb
                iCurrentAmmo = 4
            ELSE
                IF id_weap = weap_suspension_matrix
                OR id_weap = weap_trip_mine        
                    iCurrentAmmo = 3
                ENDIF
            ENDIF
        ENDIF
        CLEO_CALL store_current_ammo 0 id_weap iCurrentAmmo
        id_weap ++
        WAIT 0
    ENDWHILE
RETURN
}
SCRIPT_END

//-+-----------------CALL SCM HELPERS--------------
{
//CLEO_CALL get_current_ammo 0 id_weapon (ammo)
get_current_ammo:
    LVAR_INT id_weapon //in
    LVAR_INT ammo iOffset pActiveItem
    GET_LABEL_POINTER bytes36 (pActiveItem)
    iOffset = id_weapon
    iOffset *= 4
    pActiveItem += iOffset
    READ_MEMORY pActiveItem 4 FALSE (ammo)  
CLEO_RETURN 0 ammo
}
{
//CLEO_CALL store_current_ammo 0 id_weapon ammo
store_current_ammo:
    LVAR_INT id_weapon ammo //in
    LVAR_INT iOffset pActiveItem
    GET_LABEL_POINTER bytes36 (pActiveItem)
    iOffset = id_weapon
    iOffset *= 4
    pActiveItem += iOffset
    WRITE_MEMORY pActiveItem 4 ammo FALSE
CLEO_RETURN 0
}

bytes36:
DUMP
00 00 00 00 //+0   --empty
00 00 00 00 //+4    //id=1
00 00 00 00 //+8    //id=2
00 00 00 00 //+12   //id=3
00 00 00 00 //+16   //id=4
00 00 00 00 //+20   //id=5
00 00 00 00 //+24   //id=6
00 00 00 00 //+28   //id=7
00 00 00 00 //+32   //id=8
ENDDUMP


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