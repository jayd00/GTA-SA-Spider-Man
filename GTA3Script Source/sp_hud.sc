// by J16D
// Main Hud script (Unfinished, there are some code-fragments unused)
// Spider-Man Mod for GTA SA c.2018 - 2021 
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

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
//POWERS
CONST_INT web_blossom 1
CONST_INT holo_decoy 2
CONST_INT bullet_proof 3
CONST_INT spider_bro 4
CONST_INT negative_shockwave 5
CONST_INT electric_punch 6
CONST_INT rock_out 7
CONST_INT blur_projector 8
CONST_INT low_gravity 9
CONST_INT iron_arms 10
CONST_INT defence_shield 11
CONST_INT spirit_fire 12

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_hud
WAIT 0
LVAR_INT player_actor toggleSpiderMod toggleHUD idPowers isInMainMenu   //1:true 0: false
LVAR_INT is_in_interior widescreen_status hud_mode is_hud_enabled is_opening_door //enter_status
LVAR_INT iTempVar idTex counter iMaxAmmo
LVAR_FLOAT sx sy pl_max_health pl_health
LVAR_INT pl_money cur_hours cur_mins wanted_lvl
LVAR_FLOAT max_breath_lvl fBreath_lvl fTempVar
LVAR_INT iTempVar2 iTempVar3
LVAR_FLOAT pl_armour pl_max_armour

GET_PLAYER_CHAR 0 player_actor
GOSUB loadHudTextures
is_opening_door = FALSE
is_hud_enabled = TRUE
CLEO_CALL get_screen_aspect_ratio 0 iTempVar    // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
CLEO_CALL storeCurrentAspectRatio 0 iTempVar

GET_CLEO_SHARED_VAR varHudRadar (iTempVar)
CLEO_CALL storeHudRadar 0 iTempVar
GET_CLEO_SHARED_VAR varHudHealth (iTempVar)
CLEO_CALL storeHudHealth 0 iTempVar
GET_CLEO_SHARED_VAR varHudAmmo (iTempVar)
CLEO_CALL storeHudAmmo 0 iTempVar
GET_CLEO_SHARED_VAR varHudMoney (iTempVar)
CLEO_CALL storeHudMoney 0 iTempVar
GET_CLEO_SHARED_VAR varHudTime (iTempVar)
CLEO_CALL storeHudClockTime 0 iTempVar
GET_CLEO_SHARED_VAR varHudBreath (iTempVar)
CLEO_CALL storeHudBreath 0 iTempVar
GET_CLEO_SHARED_VAR varHudArmour (iTempVar)
CLEO_CALL storeHudArmour 0 iTempVar
GET_CLEO_SHARED_VAR varHudWantedS (iTempVar)
CLEO_CALL storeHudWantedLevel 0 iTempVar

main_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE

            IF isInMainMenu = 0     //1:true 0: false
                
                IF toggleHUD = 1  // 0:OFF || 1:ON

                    IF GOSUB is_radar_enabled
                        CLEO_CALL getCurrentAspectRatio 0 (iTempVar)
                        SWITCH iTempVar // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                            CASE 1  //16:9
                                CLEO_CALL setRadarPostion 0 800.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                BREAK
                            CASE 2  //4:3
                                CLEO_CALL setRadarPostion 0 575.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                BREAK
                            CASE 3  //16:10
                                CLEO_CALL setRadarPostion 0 725.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                BREAK
                            CASE 4  //5/4
                                CLEO_CALL setRadarPostion 0 525.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                BREAK
                            DEFAULT
                                //This new line will make screens 1366x768 (16:8.9956076) compatible
                                CLEO_CALL setRadarPostion 0 800.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                ///CLEO_CALL setRadarPostion 0 575.0 85.0 125.0 70.0   //Left|Top|Width|Height
                                BREAK
                        ENDSWITCH
                    ENDIF
                    WHILE toggleHUD = 1  // 0:OFF || 1:ON
                        GOSUB readVars
                        GOSUB hudCheck
                        GOSUB openDoorCheck
                        GOSUB activeInteriorCheck
                        IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                        OR IS_ON_CUTSCENE 
                        //OR IS_HUD_VISIBLE 
                        OR is_hud_enabled = FALSE
                        OR is_opening_door = TRUE
                            USE_TEXT_COMMANDS FALSE // don't show textures
                        ELSE
                            IF is_in_interior = 0
                                USE_TEXT_COMMANDS FALSE
                                IF GOSUB is_health_bar_enabled
                                    GOSUB drawHealth
                                ENDIF
                                IF GOSUB is_ammo_enabled
                                    GOSUB drawSpiderAmmoWeap
                                ENDIF
                                IF GOSUB is_money_enabled
                                    GOSUB drawMoneyTextDisplay
                                ENDIF
                                IF GOSUB is_clock_time_enabled
                                    GOSUB drawTimeTextDisplay
                                ENDIF
                                IF GOSUB is_breath_bar_enabled
                                    GOSUB drawBreathBarDisplay
                                ENDIF
                                IF GOSUB is_armour_bar_enabled
                                    GOSUB drawArmourBarDisplay
                                ENDIF
                                IF GOSUB are_wanted_stars_enabled
                                    GOSUB drawWantedStarDisplay
                                ENDIF
                                IF GOSUB is_any_hud_option_enabled
                                    DISPLAY_HUD FALSE
                                ELSE
                                    DISPLAY_HUD TRUE
                                ENDIF
                                //GOSUB drawHitCounter
                            ENDIF
                        ENDIF

                        IF isInMainMenu = 1     //1:true 0: false
                            USE_TEXT_COMMANDS FALSE
                            WHILE isInMainMenu = 1     //1:true 0: false
                                GOSUB readVars
                                IF toggleSpiderMod = 0 //FALSE
                                    GOTO end_hud_script
                                ENDIF
                                WAIT 0
                            ENDWHILE
                        ENDIF
                        IF toggleSpiderMod = 0 //FALSE
                            GOTO end_hud_script
                        ENDIF
                        WAIT 0
                    ENDWHILE

                    IF GOSUB is_radar_enabled
                        CLEO_CALL getCurrentAspectRatio 0 (iTempVar)
                        IF 4 >= iTempVar
                            //GOSUB resetGameHUD
                            CLEO_CALL setRadarPostion 0 40.0 104.0 94.0 76.0   //Left|Top|Width|Height  ||Default
                        ENDIF
                    ENDIF
                ENDIF
            ELSE
                USE_TEXT_COMMANDS FALSE
                WHILE isInMainMenu = 1     //1:true 0: false
                    GOSUB readVars
                    IF toggleSpiderMod = 0 //FALSE
                        GOTO end_hud_script
                    ENDIF
                    WAIT 0
                ENDWHILE  
            ENDIF
        
        ELSE
            end_hud_script:
            USE_TEXT_COMMANDS FALSE
            IF GOSUB is_radar_enabled
                CLEO_CALL getCurrentAspectRatio 0 (iTempVar)
                IF 4 >= iTempVar
                    //GOSUB resetGameHUD
                    CLEO_CALL setRadarPostion 0 40.0 104.0 94.0 76.0   //Left|Top|Width|Height  ||Default
                ENDIF
            ENDIF
            //DISPLAY_HUD TRUE
            USE_TEXT_COMMANDS FALSE
            WAIT 25
            REMOVE_TEXTURE_DICTIONARY
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

/*
removeGameHUD:
    DISPLAY_HUD FALSE
    DISPLAY_ZONE_NAMES FALSE
    DISPLAY_CAR_NAMES FALSE
RETURN
resetGameHUD:
    DISPLAY_HUD TRUE
    DISPLAY_ZONE_NAMES TRUE
    DISPLAY_CAR_NAMES TRUE
RETURN
*/


//-+-------------------------- checks ----------------------
is_radar_enabled:
    //GET_CLEO_SHARED_VAR varHudRadar (iTempVar3)
    CLEO_CALL getHudRadar 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_health_bar_enabled:
    //GET_CLEO_SHARED_VAR varHudHealth (iTempVar3)
    CLEO_CALL getHudHealth 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_ammo_enabled:
    //GET_CLEO_SHARED_VAR varHudAmmo (iTempVar3)
    CLEO_CALL getHudAmmo 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_money_enabled:
    //GET_CLEO_SHARED_VAR varHudMoney (iTempVar3)
    CLEO_CALL getHudMoney 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_clock_time_enabled:
    //GET_CLEO_SHARED_VAR varHudTime (iTempVar3)
    CLEO_CALL getHudClockTime 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_breath_bar_enabled:
    //GET_CLEO_SHARED_VAR varHudBreath (iTempVar3)
    CLEO_CALL getHudBreath 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_armour_bar_enabled:
    //GET_CLEO_SHARED_VAR varHudArmour (iTempVar3)
    CLEO_CALL getHudArmour 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

are_wanted_stars_enabled:
    //GET_CLEO_SHARED_VAR varHudWantedS (iTempVar3)
    CLEO_CALL getHudWantedLevel 0 (iTempVar3)
    IF iTempVar3 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_any_hud_option_enabled:
    IF GOSUB is_health_bar_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB is_ammo_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB is_money_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB is_clock_time_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB is_breath_bar_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB is_armour_bar_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    IF GOSUB are_wanted_stars_enabled
        RETURN_TRUE
        RETURN
    ENDIF
    RETURN_FALSE
RETURN

//------------------------------------------------
readVars:
    GET_CLEO_SHARED_VAR varHUD (toggleHUD)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

activeInteriorCheck:
    GET_AREA_VISIBLE (is_in_interior)
RETURN

hudCheck:
    READ_MEMORY 0xBA6769 4 FALSE (hud_mode)
    IF hud_mode = FALSE
        is_hud_enabled = FALSE
    ELSE
        is_hud_enabled = TRUE
    ENDIF
RETURN

/*
cutsceneCheck:
    IF HAS_CUTSCENE_LOADED
        IF NOT HAS_CUTSCENE_FINISHED
            is_in_widescreen = TRUE
        ENDIF
    ELSE
        is_in_widescreen = FALSE
    ENDIF
RETURN

wideScreenCheck:
    READ_MEMORY 0xB6F065 4 FALSE (widescreen_status)
    IF widescreen_status = 1
        is_in_widescreen = TRUE
    ELSE
        is_in_widescreen = FALSE    
    ENDIF
RETURN
*/
openDoorCheck:
    READ_MEMORY 0x96A7CC 4 FALSE (iTempVar2)
    IF iTempVar2 = 1
    OR iTempVar2 = 2
        is_opening_door = TRUE
    ELSE
        is_opening_door = FALSE
    ENDIF
RETURN
//-+--------------------------------------------------------

//-+-------------------- Texture Load ----------------------
loadHudTextures:
    //TEXTURES
    CONST_INT idHealthLow 10
    CONST_INT idStars 11
    //Health bar
    CONST_INT idHB 15
    CONST_INT idHBa 16
    CONST_INT idHealthBar0 17
    CONST_INT idHealthBar1 18
    CONST_INT idHealthBar2 19
    CONST_INT idHealthBar3 20
    CONST_INT idHealthBar4 21
    CONST_INT idHealthBar5 22
    CONST_INT idHealthBar6 23
    CONST_INT idHealthBar7 24
    CONST_INT idHealthBar8 25
    CONST_INT idHealthBar9 26
    CONST_INT idHealthBar10 27
    CONST_INT idHealthBar11 28
    CONST_INT idHealthBar12 29
    //Armour Bar
    CONST_INT idWA 35
    CONST_INT idWA_a 36
    CONST_INT idWA_b 37
    CONST_INT idWA_c 38
    CONST_INT idAmmoW1 39
    CONST_INT idAmmoW2 40
    CONST_INT idAmmoW3 41
    CONST_INT idAmmoW4 42
    CONST_INT idAmmoW5 43
    CONST_INT idAmmoW6 44
    CONST_INT idAmmoW7 45
    CONST_INT idAmmoW8 46
    CONST_INT idAmmoW9 47
    CONST_INT idAmmoW10 48
    //Armour Weap
    CONST_INT idWeap1 49
    CONST_INT idWeap2 50
    CONST_INT idWeap3 51
    CONST_INT idWeap4 52
    CONST_INT idWeap5 53
    CONST_INT idWeap6 54
    CONST_INT idWeap7 55
    CONST_INT idWeap8 56
    //Armour Power
    CONST_INT idSPPowerBP 57
    CONST_INT idSPPowerBPJ 58
    CONST_INT idSPPowerDS 59
    CONST_INT idSPPowerEP 60
    CONST_INT idSPPowerHD 61
    CONST_INT idSPPowerIA 62
    CONST_INT idSPPowerLG 63
    CONST_INT idSPPowerNS 64
    CONST_INT idSPPowerRO 65
    CONST_INT idSPPowerSB 66
    CONST_INT idSPPowerSF 67
    CONST_INT idSPPowerWB 68
    CONST_INT idSPPowerNULL 69
    //Armour Power Circle
    CONST_INT idPowerBar1 75
    CONST_INT idPowerBar2 76
    CONST_INT idPowerBar3 77
    CONST_INT idPowerBar4 78
    CONST_INT idPowerBar5 79
    CONST_INT idPowerBar6 80
    CONST_INT idPowerBar7 81
    CONST_INT idPowerBar8 82

    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY sphud
        //wanted star & background red
        LOAD_SPRITE idStars "st1"
        LOAD_SPRITE idHealthLow "splhealth"
        //Health bar
        LOAD_SPRITE idHBa "h_bar1"
        LOAD_SPRITE idHB "h_bar"
        LOAD_SPRITE idHealthBar0 "h0"
        LOAD_SPRITE idHealthBar1 "h1"
        LOAD_SPRITE idHealthBar2 "h2"
        LOAD_SPRITE idHealthBar3 "h3"
        LOAD_SPRITE idHealthBar4 "h4"
        LOAD_SPRITE idHealthBar5 "h5"
        LOAD_SPRITE idHealthBar6 "h6"
        LOAD_SPRITE idHealthBar7 "h7"
        LOAD_SPRITE idHealthBar8 "h8"
        LOAD_SPRITE idHealthBar9 "h9"
        LOAD_SPRITE idHealthBar10 "h10"
        LOAD_SPRITE idHealthBar11 "h11"
        LOAD_SPRITE idHealthBar12 "h12"
        //Armour bar
        LOAD_SPRITE idWA "a_bar"
        LOAD_SPRITE idWA_a "a_bara"
        LOAD_SPRITE idWA_b "a_barb"
        LOAD_SPRITE idWA_c "a_barc"
        LOAD_SPRITE idAmmoW1 "a1"
        LOAD_SPRITE idAmmoW2 "a2"
        LOAD_SPRITE idAmmoW3 "a3"
        LOAD_SPRITE idAmmoW4 "a4"
        LOAD_SPRITE idAmmoW5 "a5"
        LOAD_SPRITE idAmmoW6 "a6"
        LOAD_SPRITE idAmmoW7 "a7"
        LOAD_SPRITE idAmmoW8 "a8"
        LOAD_SPRITE idAmmoW9 "a9"
        LOAD_SPRITE idAmmoW10 "a10"
        //Armour Weap
        LOAD_SPRITE idWeap1 "wa1"
        LOAD_SPRITE idWeap2 "wa2"
        LOAD_SPRITE idWeap3 "wa3"
        LOAD_SPRITE idWeap4 "wa4"
        LOAD_SPRITE idWeap5 "wa5"
        LOAD_SPRITE idWeap6 "wa6"
        LOAD_SPRITE idWeap7 "wa7"
        LOAD_SPRITE idWeap8 "wa8"
        //Armour Power
        LOAD_SPRITE idSPPowerWB "p_wb"
        LOAD_SPRITE idSPPowerHD "p_hd"
        LOAD_SPRITE idSPPowerBP "p_bp"
        LOAD_SPRITE idSPPowerSB "p_sb"
        LOAD_SPRITE idSPPowerNS "p_ns"
        LOAD_SPRITE idSPPowerEP "p_ep"
        LOAD_SPRITE idSPPowerRO "p_ro"
        LOAD_SPRITE idSPPowerBPJ "p_bpj"
        LOAD_SPRITE idSPPowerLG "p_lg"
        LOAD_SPRITE idSPPowerIA "p_ia"
        LOAD_SPRITE idSPPowerDS "p_ds"
        LOAD_SPRITE idSPPowerSF "p_sf"
        LOAD_SPRITE idSPPowerNULL "p_null"
        //Armour Power Circle
        LOAD_SPRITE idPowerBar1 "c_b_1"
        LOAD_SPRITE idPowerBar2 "c_b_2"
        LOAD_SPRITE idPowerBar3 "c_b_3"
        LOAD_SPRITE idPowerBar4 "c_b_4"
        LOAD_SPRITE idPowerBar5 "c_b_5"
        LOAD_SPRITE idPowerBar6 "c_b_6"
        LOAD_SPRITE idPowerBar7 "c_b_7"
        LOAD_SPRITE idPowerBar8 "c_b_8"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN
//-+--------------------------------------------------------

//-+-------------------- Health ----------------------------
drawHealth:
    GOSUB getHealthMath
    GOSUB drawRedBackgroundDeath
    //GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 300.00 
    sy = 93.33
    IF idHealthBar2 > idTex
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE idHB (105.0 45.0) (sx sy) (155 30 30 255)
    ELSE
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE idHB (105.0 45.0) (sx sy) (255 255 255 255)
    ENDIF
    GOSUB drawHealthNumber
    // Health bar ID'S (17 - 29)  ||TOTAL 13
    counter = idHealthBar0  //17
    WHILE idHealthBar12 >= counter      //79>=counter
        IF counter = idTex  //draw health bar texture
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE counter (105.0 45.0) (sx sy) (255 255 255 255)
        ENDIF
        counter ++
    ENDWHILE
RETURN

getHealthMath:
    GET_CHAR_HEALTH player_actor (iTempVar)
    CSET_LVAR_FLOAT_TO_LVAR_INT (pl_health) iTempVar
    GET_CHAR_MAX_HEALTH player_actor (pl_max_health)
    pl_health /= pl_max_health
    pl_health *= 100.0
    iTempVar =# pl_health
    IF 7.6923 > pl_health
    AND pl_health >= 0.0
        idTex = idHealthBar0
    ELSE
        IF 15.3846 > pl_health
        AND pl_health >= 7.6923
            idTex = idHealthBar1
        ELSE
            IF 23.0769 > pl_health
            AND pl_health >= 15.3846
                idTex = idHealthBar2
            ELSE
                IF 30.7692 > pl_health
                AND pl_health >= 23.0769
                    idTex = idHealthBar3
                ELSE
                    IF 38.4615 > pl_health
                    AND pl_health >= 30.7692
                        idTex = idHealthBar4
                    ELSE
                        IF 46.1538 > pl_health
                        AND pl_health >= 38.4615
                            idTex = idHealthBar5
                        ELSE
                            IF 53.8461 > pl_health
                            AND pl_health >= 46.1538
                                idTex = idHealthBar6
                            ELSE
                                IF 61.5384 > pl_health
                                AND pl_health >= 53.8461
                                    idTex = idHealthBar7
                                ELSE
                                    IF 69.2307 > pl_health
                                    AND pl_health >= 61.5384
                                        idTex = idHealthBar8
                                    ELSE
                                        IF 76.9230 > pl_health
                                        AND pl_health >= 69.2307
                                            idTex = idHealthBar9
                                        ELSE
                                            IF 84.6153 > pl_health
                                            AND pl_health >= 76.9230
                                                idTex = idHealthBar10
                                            ELSE
                                                IF 92.3076 > pl_health
                                                AND pl_health >= 84.6153
                                                    idTex = idHealthBar11
                                                ELSE
                                                    IF pl_health >= 92.3076
                                                        idTex = idHealthBar12
                                                    ENDIF
                                                ENDIF
                                            ENDIF
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
    //PRINT_FORMATTED_NOW "H:%.2f MaxH: %.2f C:%i" 1 pl_health pl_max_health iTempVar   //debug
RETURN

drawRedBackgroundDeath:
    IF idHealthBar2 >= idTex
        CLEO_CALL getCurrentResolution 0 (sx sy) 
        GET_FIXED_XY_ASPECT_RATIO (sx sy) (sx sy)    //(300.0 93.33)
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE idHealthLow (320.0 224.0) (sx sy) (155 30 30 255)
    ENDIF
RETURN

drawHealthNumber:
    GET_CHAR_HEALTH player_actor (iTempVar)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.24 1.17
    SET_TEXT_WRAPX 640.0
    IF 3 > idTex
        SET_TEXT_COLOUR 155 30 30 255
        SET_TEXT_EDGE 1 (95 18 18 100)
    ELSE
        SET_TEXT_COLOUR 215 225 235 255
        SET_TEXT_EDGE 1 (94 120 137 100)
    ENDIF
    USE_TEXT_COMMANDS FALSE
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DISPLAY_TEXT_WITH_NUMBER 23.5 25.0 NUMBER iTempVar
RETURN
//-+--------------------------------------------------------

//-+-------------------- Web Ammo --------------------------
drawSpiderAmmoWeap:
    //GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 300.00 
    sy = 93.33
    GOSUB drawAmmoBackground
    GOSUB drawCurrentAmmo
    GOSUB drawAmmoIcons

    GOSUB drawPowerCircle
    GOSUB drawCurrentPower
    USE_TEXT_COMMANDS FALSE
    USE_TEXT_COMMANDS TRUE
RETURN
    
drawCurrentAmmo:
    GET_CLEO_SHARED_VAR varWeapAmmo (iTempVar)
    SWITCH iTempVar
        CASE 1
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW1 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 2
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW2 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 3
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW3 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 4
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW4 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 5
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW5 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 6
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW6 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 7
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW7 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 8
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW8 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 9
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW9 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 10
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idAmmoW10 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
    ENDSWITCH
RETURN

drawAmmoBackground:
    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
    IF iTempVar = weap_web_shoot
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE idWA_a (535.0 50.0) (sx sy) (255 255 255 255)   //10ammo
    ELSE
        IF iTempVar = weap_concussive_blast
        OR iTempVar = weap_impact_web
        OR iTempVar = weap_spyder_drone
        OR iTempVar = weap_electric_web
        OR iTempVar = weap_web_bomb
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWA_b (535.0 50.0) (sx sy) (255 255 255 255)   //4ammo
        ELSE
            IF iTempVar = weap_suspension_matrix
            OR iTempVar = weap_trip_mine
                USE_TEXT_COMMANDS FALSE   
                SET_SPRITES_DRAW_BEFORE_FADE TRUE
                DRAW_SPRITE idWA_c (535.0 50.0) (sx sy) (255 255 255 255)   //4ammo            
            ENDIF
        ENDIF
    ENDIF
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idWA (535.0 50.0) (sx sy) (255 255 255 255)
RETURN

drawAmmoIcons:
    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
    SWITCH iTempVar
        CASE 1
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap1 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 2
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap2 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 3
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap3 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 4
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap4 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 5
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap5 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 6
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap6 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 7
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap7 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE 8
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idWeap8 (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
    ENDSWITCH
RETURN

getPowerMath:
    GET_CLEO_SHARED_VAR varPowersProgress (iTempVar)
    CSET_LVAR_FLOAT_TO_LVAR_INT (fTempVar) iTempVar
    IF 7.0 > fTempVar
        idTex = 0
    ELSE
        IF 14.0 > fTempVar
        AND fTempVar >= 7.0
            idTex = idPowerBar1
        ELSE
            IF 26.0 > fTempVar
            AND fTempVar >= 14.0
                idTex = idPowerBar2
            ELSE
                IF 38.0 > fTempVar
                AND fTempVar >= 26.0
                    idTex = idPowerBar3
                ELSE
                    IF 50.0 > fTempVar
                    AND fTempVar >= 38.0
                        idTex = idPowerBar4
                    ELSE
                        IF 62.0 > fTempVar
                        AND fTempVar >= 50.0
                            idTex = idPowerBar5
                        ELSE
                            IF 74.0 > fTempVar
                            AND fTempVar >= 62.0
                                idTex = idPowerBar6
                            ELSE
                                IF 93.0 > fTempVar
                                AND fTempVar >= 74.0
                                    idTex = idPowerBar7
                                ELSE
                                    IF fTempVar >= 93.0
                                        idTex = idPowerBar8
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
RETURN

drawPowerCircle:
    GOSUB getPowerMath
    // Power Circle ID'S (75 - 82)  ||TOTAL 8
    counter = idPowerBar1  //75
    WHILE idPowerBar8 >= counter      //82>=counter
        IF counter = idTex  //draw power circle
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE counter (535.0 50.0) (sx sy) (255 255 255 180)
        ENDIF
        counter ++
    ENDWHILE
RETURN

drawCurrentPower:
    GET_CLEO_SHARED_VAR varIdPowers (idPowers)
    SWITCH idPowers
        CASE web_blossom
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerWB (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE holo_decoy
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerHD (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE bullet_proof
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerBP (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE spider_bro
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerSB (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE negative_shockwave
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerNS (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE electric_punch
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerEP (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE rock_out
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerRO (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE blur_projector
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerBPJ (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE low_gravity
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerLG (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE iron_arms
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerIA (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE defence_shield
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerDS (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        CASE spirit_fire
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerSF (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
        DEFAULT
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSPPowerNULL (535.0 50.0) (sx sy) (255 255 255 255)
            BREAK
    ENDSWITCH
RETURN

/*
getMaxAmmo:
    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
    IF iTempVar = weap_web_shoot
        iMaxAmmo = 10
    ELSE
        IF iTempVar = weap_concussive_blast
        OR iTempVar = weap_impact_web
        OR iTempVar = weap_spyder_drone
        OR iTempVar = weap_electric_web
        OR iTempVar = weap_web_bomb
            iMaxAmmo = 4
        ELSE
            IF iTempVar = weap_suspension_matrix
            OR iTempVar = weap_trip_mine        
                iMaxAmmo = 3
            ENDIF
        ENDIF
    ENDIF
RETURN
*/
//-+--------------------------------------------------------

//-+-------------------- Breath ----------------------------
drawBreathBarDisplay:
    READ_MEMORY 0xB7CDE0 4 FALSE (fBreath_lvl) // time breath
    //GET_INT_STAT 4080 (breath_lvl)  ////return a wrong value
    IF fBreath_lvl > 0.0
    AND IS_CHAR_SWIMMING player_actor
        GOSUB getBreathMath
        CLEO_CALL drawBar 0 50.0 436.0 fBreath_lvl 60.0 2.0 1.0 (150 200 255 120)
    ENDIF
RETURN

getBreathMath:
    GET_FLOAT_STAT 225 (max_breath_lvl) // Lung Capacity
    max_breath_lvl *= 4.0  //1.5
    fBreath_lvl /= max_breath_lvl
    fBreath_lvl *= 100.0
RETURN
//-+--------------------------------------------------------

//-+--------------------- Armour ----------------------------
drawArmourBarDisplay:
    GET_CHAR_ARMOUR player_actor (iTempVar2)
    CSET_LVAR_FLOAT_TO_LVAR_INT (pl_armour) iTempVar2
    IF pl_armour > 0.0
        GOSUB getArmourMath
        CLEO_CALL drawBar 0 50.0 440.0 pl_armour 60.0 2.0 1.0 (255 255 255 120)
    ENDIF
RETURN

getArmourMath:
    GET_PLAYER_MAX_ARMOUR player (iTempVar2)
    CSET_LVAR_FLOAT_TO_LVAR_INT (pl_max_armour) iTempVar2
    pl_armour /= pl_max_armour
    pl_armour *= 100.0
RETURN
//-+--------------------------------------------------------

//-+--------------------- Money ----------------------------
drawMoneyTextDisplay:
    STORE_SCORE player (pl_money)
    IF pl_money > -1
        SET_TEXT_COLOUR 0 255 0 120
    ELSE
        SET_TEXT_COLOUR 255 0 0 120
        pl_money *= -1
    ENDIF
    GOSUB formatText
RETURN

formatText:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.15 0.725
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 17 242 198 200  //6 253 244
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DISPLAY_TEXT_WITH_NUMBER 120.0 435.0 AUICASH pl_money
RETURN
//-+--------------------------------------------------------

//-+--------------------- Time ----------------------------
drawTimeTextDisplay:
    GET_TIME_OF_DAY cur_hours cur_mins
    IF 10 > cur_mins
        GOSUB formatTimeTextOne
    ELSE
        GOSUB formatTimeTextTwo
    ENDIF
RETURN
 
formatTimeTextOne:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.15 0.725
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 140
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DISPLAY_TEXT_WITH_2_NUMBERS 95.0 435.0 TIME_0 cur_hours cur_mins    // ~1~:0~1~
RETURN

formatTimeTextTwo:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.15 0.725
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 140
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DISPLAY_TEXT_WITH_2_NUMBERS 95.0 435.0 TIME cur_hours cur_mins  //  ~1~:~1~
RETURN
//-+--------------------------------------------------------

//-+------------------ Wanted Stars ------------------------
drawWantedStarDisplay:
    STORE_WANTED_LEVEL player (wanted_lvl)
    IF wanted_lvl > 0
        iTempVar2 = 0   //counter
        fTempVar = 430.0
        WHILE wanted_lvl > iTempVar2
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idStars (629.0 fTempVar) (8.5 8.5) (255 255 255 210)
            fTempVar -= 12.0
            iTempVar2 += 1
        ENDWHILE
    ENDIF
RETURN
//-+--------------------------------------------------------



//-+------------------ Hit Counter ------------------------
drawHitCounter:
    //GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 300.00 
    sy = 93.33
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idHBa (105.0 45.0) (sx sy) (255 255 255 255)    // combobox
    /*
    counter = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE counter (counter iChar)
        IF DOES_CHAR_EXIST iChar
        AND NOT IS_CHAR_DEAD iChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
            IF NOT IS_CHAR_IN_ANY_CAR iChar
            AND NOT IS_CHAR_ON_ANY_BIKE iChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar

                IF NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR iChar iPed //RESET
                    GET_CHAR_HEALTH iChar (cur_hours)
                    GET_CHAR_HEALTH iChar (cur_mins)
                    iPed = iChar
                ELSE
                    GET_CHAR_HEALTH iChar (cur_hours)
                    IF NOT cur_hours = cur_mins
                        iHitCounter += 1
                        GET_CHAR_HEALTH iChar (cur_mins)
                        timerb = 0
                    ENDIF
                ENDIF

            ENDIF
        ENDIF
    ENDWHILE
    IF timerb > 4000
        iHitCounter = 0
    ENDIF
    IF iHitCounter > 0
        SET_TEXT_FONT FONT_SUBTITLES
        SET_TEXT_SCALE 0.24 1.17
        SET_TEXT_WRAPX 640.0
        //SET_TEXT_COLOUR 155 30 30 255
        //SET_TEXT_EDGE 1 (95 18 18 100)
        SET_TEXT_COLOUR 215 225 235 255
        SET_TEXT_EDGE 1 (94 120 137 100)
        SET_TEXT_DRAW_BEFORE_FADE TRUE
        DISPLAY_TEXT_WITH_NUMBER 23.5 40.0 NUMBER iHitCounter
    ENDIF
    */
RETURN
//-+--------------------------------------------------------
}
SCRIPT_END

{
//CLEO_CALL get_screen_aspect_ratio 0 var
get_screen_aspect_ratio:
    LVAR_FLOAT val[3] fResX fResY fAspectRatio
    LVAR_INT id
    CLEO_CALL getCurrentResolution 0 (fResX fResY)
    fAspectRatio = fResX
    fAspectRatio /= fResY
    val[0] = 16.0
    val[1] = 9.0
    val[2] = val[0]
    val[2] /= val[1]    //16:9
    IF fAspectRatio = val[2]    //16:9
        id = 1  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
        CLEO_RETURN 0 id
    ELSE
        val[0] = 4.0
        val[1] = 3.0
        val[2] = val[0]
        val[2] /= val[1]    //4:3
        IF fAspectRatio = val[2]    //4:3
            id = 2  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
            CLEO_RETURN 0 id
        ELSE
            val[0] = 16.0
            val[1] = 10.0
            val[2] = val[0]
            val[2] /= val[1]    //16:10
            IF fAspectRatio = val[2]    //16:10
                id = 3  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                CLEO_RETURN 0 id
            ELSE
                val[0] = 5.0
                val[1] = 4.0
                val[2] = val[0]
                val[2] /= val[1]    //5:4
                IF fAspectRatio = val[2]    //5:4
                    id = 4  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                    CLEO_RETURN 0 id
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 0
}

{
//CLEO_CALL drawBar 0 xCoord yCoord fValue xMaxSize yMaxSize fThikness (r g b a)
drawBar:
    LVAR_FLOAT xCoord yCoord fValue xMaxSize yMaxSize fThikness //in
    LVAR_INT r g b a // in
    //fValue must be MAx 100
    LVAR_FLOAT fVal1 xSize ySize fVal2
    fVal1 = 100.0
    fVal1 /= xMaxSize
    fValue /= fVal1
    xSize = xMaxSize
    ySize = yMaxSize
    xSize += fThikness
    ySize += fThikness
    DRAW_RECT xCoord yCoord xSize ySize (0 0 0 a)
    DRAW_RECT xCoord yCoord xMaxSize yMaxSize (200 200 200 a)
    fVal2 = fValue
    fVal2 /= 2.0
    xMaxSize /= 2.0
    xCoord += fVal2
    xCoord -= xMaxSize
    DRAW_RECT xCoord yCoord fValue yMaxSize (r g b a)
    USE_TEXT_COMMANDS FALSE
CLEO_RETURN 0
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
//CLEO_CALL storeCurrentAspectRatio 0 var
storeCurrentAspectRatio:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getCurrentAspectRatio 0 (var)
getCurrentAspectRatio:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudWantedLevel 0 var
storeHudWantedLevel:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_wanted_level_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudWantedLevel 0 (var)
getHudWantedLevel:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_armour_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}

{
//CLEO_CALL storeHudArmour 0 var
storeHudArmour:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_armour_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudArmour 0 (var)
getHudArmour:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_armour_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudBreath 0 var
storeHudBreath:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_breath_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudBreath 0 (var)
getHudBreath:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_breath_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudClockTime 0 var
storeHudClockTime:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_clock_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudClockTime 0 (var)
getHudClockTime:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_clock_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudMoney 0 var
storeHudMoney:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_money_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudMoney 0 (var)
getHudMoney:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_money_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudAmmo 0 var
storeHudAmmo:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_ammo_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudAmmo 0 (var)
getHudAmmo:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_ammo_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudHealth 0 var
storeHudHealth:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_health_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudHealth 0 (var)
getHudHealth:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_health_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudRadar 0 var
storeHudRadar:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_radar_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudRadar 0 (var)
getHudRadar:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_radar_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL setRadarPostion 0 40.0 104.0 94.0 76.0   //Left|Top|Width|Height  ||Default
setRadarPostion:
    LVAR_FLOAT rL rT rW rH    //in
    LVAR_INT pItem iRadarLeft iRadarTop iRadarWidth iRadarHeight iAlpha
    GET_LABEL_POINTER HudRadarAndComponentsCoords (pItem)
    iRadarLeft = pItem
    WRITE_MEMORY iRadarLeft 4 rL FALSE
    iRadarTop = pItem + 0x04
    WRITE_MEMORY iRadarTop 4 rT FALSE
    iRadarWidth = pItem + 0x08
    WRITE_MEMORY iRadarWidth 4 rW FALSE
    iRadarHeight = pItem + 0x0C
    WRITE_MEMORY iRadarHeight 4 rH FALSE
    // Map and sprites position relative to the radar rectangle.
    WRITE_MEMORY 0x005834C2 4 iRadarWidth TRUE
    WRITE_MEMORY 0x005834D4 4 iRadarLeft TRUE
    WRITE_MEMORY 0x005834F6 4 iRadarHeight TRUE
    WRITE_MEMORY 0x00583500 4 iRadarTop TRUE
CLEO_RETURN 0
}
HudRadarAndComponentsCoords:
DUMP
    00002042    // (40.0f) fRadarLeft   
    0000D042    // (104.0f) fRadarTop   
    0000BC42    // (94.0f) fRadarWidth   
    00009842    // (76.0f) fRadarHeight   
    // Unscaled, R* bug.   
    //00008040    // (4.0f) fRadarDiscHorzMargin   
    //00008040    // (4.0f) fRadarDiscVertMargin
ENDDUMP

Screen_AspectRatio:
DUMP
00000000    //id:1 - 16:9  | id:2 - 4:3
ENDDUMP

GUI_Memory_hud_wanted_level_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_armour_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_breath_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_clock_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_money_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_ammo_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_health_item:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_hud_radar_item:
DUMP
00 00 00 00
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


/*
///////////LIST of call scm helpers
CLEO_CALL storeHudRadar 0 iTempVar
CLEO_CALL getHudRadar 0 (var)
CLEO_CALL storeHudHealth 0 var
CLEO_CALL getHudHealth 0 (var)
CLEO_CALL storeHudAmmo 0 var
CLEO_CALL getHudAmmo 0 (var)
CLEO_CALL storeHudMoney 0 var
CLEO_CALL getHudMoney 0 (var)
CLEO_CALL storeHudClockTime 0 var
CLEO_CALL getHudClockTime 0 (var)
CLEO_CALL storeHudBreath 0 var
CLEO_CALL getHudBreath 0 (var)
CLEO_CALL storeHudArmour 0 var
CLEO_CALL getHudArmour 0 (var)
CLEO_CALL storeHudWantedLevel 0 var
CLEO_CALL getHudWantedLevel 0 (var)
*/
