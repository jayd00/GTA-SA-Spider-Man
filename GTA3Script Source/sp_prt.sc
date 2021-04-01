// by J16D
// Missions Summary
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

/* GUIDE
INCLUDED:
    ID:1  ||SUIT POWER READY
    ID:2  ||CAR CHASE MISSION PASSED
    ID:3  ||CAR CHASE MISSION FAIL
    ID:4  ||BACK PACK FOUND
    ID:5  ||THUG HIDOUTS MISSION PASSED
    ID:6  ||STREET CRIMES MISSION PASSED
FORMAT:
    ID:1
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id}
    ID:2
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id} {total xp} {stolen veh xp} {combat xp}
    ID:3
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id}
    ID:4
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id} {backpack id}
    ID:5
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id} {total xp} {mission xp} {combat xp}
    ID:6
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id} {total xp} {mission xp} {combat xp}

*/

//-+---CONSTANTS--------------------
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

SCRIPT_START
{
SCRIPT_NAME sp_prt
LVAR_INT idVar  //in
LVAR_INT iTempVar2 iTempVar3 iTempVar4      //in
//---
LVAR_INT toggleSpiderMod isInMainMenu
LVAR_INT counter idPowers sfx r g b iTempVar
LVAR_FLOAT sx sy

USE_TEXT_COMMANDS TRUE
SWITCH idVar
    CASE 1   //ID:1  ||SUIT POWER READY
        // IN: {id}
        GOSUB load_textures_powers
        GOSUB play_sfx_power_ready
        GENERATE_RANDOM_INT_IN_RANGE 0 7 (iTempVar)
        IF 3 > iTempVar
            r = 34
            g = 188
            b = 186
        ELSE
            r = 20
            g = 71
            b = 100
        ENDIF
        timera = 0
        WHILE 3500 >= timera
            IF NOT IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                GOSUB drawPowerReady
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 2  //ID:2  ||CAR CHASE MISSION PASSED
        // IN: {id} {total xp} {stolen veh xp} {combat xp}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_car_chase
        timera = 0
        WHILE TRUE
            GOSUB draw_car_chase_mission_succesful
            GOSUB draw_car_chase_key_press_succesful
            IF timera > 2000
                IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        WAIT 0
                    ENDWHILE
                    BREAK
                ENDIF
            ENDIF
            IF timera > 6000   //6 sec
                BREAK
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 3  //ID:3  ||CAR CHASE MISSION FAIL
        // IN: {id}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_car_chase
        timera = 0
        WHILE TRUE
            GOSUB draw_car_chase_summary_fail
            GOSUB draw_car_chase_key_press_fail
            IF timera > 2000
                IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        WAIT 0
                    ENDWHILE
                    BREAK
                ENDIF
            ENDIF
            IF timera > 5000   //5 sec
                BREAK
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 4  //ID:4  ||BACK PACK FOUND
        // IN: {id} {backpack id}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_backpack
        GOSUB select_backpack_texture_by_id
        timera = 0
        WHILE TRUE
            GOSUB draw_backpack_summary
            GOSUB draw_backpack_key_press
            IF timera > 2000
                IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        WAIT 0
                    ENDWHILE
                    BREAK
                ENDIF
            ENDIF
            IF timera > 6000   //6 sec
                BREAK
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 5  //ID:5  ||THUG HIDOUTS MISSION PASSED
        // IN: {id} {total xp} {mission xp} {combat xp}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_thug_hideouts
        timera = 0
        WHILE TRUE
            GOSUB draw_thug_hidouts_mission_succesful
            GOSUB draw_thug_hideouts_key_press_succesful
            IF timera > 2000
                IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        WAIT 0
                    ENDWHILE
                    BREAK
                ENDIF
            ENDIF
            IF timera > 6000   //6 sec
                BREAK
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 6  //ID:6  ||STREET CRIMES MISSION PASSED
        // IN: {id} {total xp} {mission xp} {combat xp}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_street_crimes
        timera = 0
        WHILE TRUE
            GOSUB draw_street_crimes_mission_succesful
            GOSUB draw_street_crimes_key_press_succesful
            IF timera > 2000
                IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        WAIT 0
                    ENDWHILE
                    BREAK
                ENDIF
            ENDIF
            IF timera > 6000   //6 sec
                BREAK
            ENDIF
            GOSUB readVars
            IF isInMainMenu = 1     //1:true 0: false
            OR toggleSpiderMod = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    DEFAULT
        BREAK
ENDSWITCH

USE_TEXT_COMMANDS TRUE
USE_TEXT_COMMANDS FALSE
WAIT 0
USE_TEXT_COMMANDS FALSE
WAIT 0
REMOVE_TEXTURE_DICTIONARY
WAIT 0
REMOVE_AUDIO_STREAM sfx
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

//-+----------------------------------- POWERS (sp_po.sc)
drawPowerReady:
    //GET_FIXED_XY_ASPECT_RATIO (220.0 110.0) (sx sy)
    sx = 165.00 
    sy = 102.67
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idBackTbxB (320.0 55.0) (sx sy) (r g b 220)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idBackTbxA (320.0 55.0) (sx sy) (255 255 255 255)
    //DRAW_SPRITE idBackTbx (320.0 55.0) (sx sy) (255 255 255 255)
    GOSUB drawPowerIcon
RETURN

drawPowerIcon:
    GET_CLEO_SHARED_VAR varIdPowers (idPowers)
    SWITCH idPowers
        CASE web_blossom
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconWB (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE holo_decoy
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconHD (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE bullet_proof
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconBP (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE spider_bro
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconSB (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE negative_shockwave
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconNS (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE electric_punch
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconEP (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE rock_out
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconRO (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE blur_projector
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconBPJ (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE low_gravity
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconLG (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE iron_arms
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconIA (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE defence_shield
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconDS (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
        CASE spirit_fire
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idIconSF (320.0 55.0) (sx sy) (255 255 255 255)
            BREAK
    ENDSWITCH
RETURN

play_sfx_power_ready:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\power_ready.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\power_ready.mp3" (sfx)
        SET_AUDIO_STREAM_STATE sfx 1
        SET_AUDIO_STREAM_VOLUME sfx 0.90
    ENDIF
RETURN

load_textures_powers:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY sptbx
        LOAD_SPRITE idBackTbxA "sp_back_pr_a"
        LOAD_SPRITE idBackTbxB "sp_back_pr_b"
        LOAD_SPRITE idBackTbx "sp_back_pr"
        LOAD_SPRITE idIconBP "sp_icon_bp"
        LOAD_SPRITE idIconBPJ "sp_icon_bpj"
        LOAD_SPRITE idIconDS "sp_icon_ds"
        LOAD_SPRITE idIconEP "sp_icon_ep"
        LOAD_SPRITE idIconHD "sp_icon_hd"
        LOAD_SPRITE idIconIA "sp_icon_ia"
        LOAD_SPRITE idIconLG "sp_icon_lg"
        LOAD_SPRITE idIconNS "sp_icon_ns"
        LOAD_SPRITE idIconRO "sp_icon_ro"
        LOAD_SPRITE idIconSB "sp_icon_sb"
        LOAD_SPRITE idIconSF "sp_icon_sf"
        LOAD_SPRITE idIconWB "sp_icon_wb"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN
//-+-----------------------------------


//-+----------------------------------- CAR CHASE (sp_cc.sc)
play_sfx_mission_end:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\event_ended.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\event_ended.mp3" (sfx)
        SET_AUDIO_STREAM_STATE sfx 1
        WAIT 0
    ENDIF
RETURN

load_textures_car_chase:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spaim
        LOAD_SPRITE idMapIcon5 "mk5"    //Crime
        LOAD_SPRITE tPBBackInfo "btimC"
        LOAD_SPRITE tPBBackFail "btimD"
        LOAD_SPRITE tPBFailA "mfailA"
        LOAD_SPRITE tPBFailB "mfailB"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_car_chase_mission_succesful:
    //iTempVar2     // Total XP
    //iTempVar3     // Stollen Vehicle XP
    //iTempVar4     // Combat XP
    GET_FIXED_XY_ASPECT_RATIO 25.0 25.0 (sx sy)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idMapIcon5 (21.0 110.0) (sx sy) (255 255 255 235)

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 470 18 0.0 0.0 // Stolen Vehicle
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.5 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 474 20 0.0 0.0 // STOPPED

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 115.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 135.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 473 1 0.0 0.0 //  XP SUMMARY

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 145.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 470 19 0.0 0.0  // Stolen Vehicle
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 146.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar3  //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 165.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 471 19 0.0 0.0  // Combat
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 166.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar4    //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 180.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 197.5) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 472 20 0.0 0.0  // TOTAL XP EARNED
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 195.0) (50.0 15.0) (255 255 255 0) 121 21 0.0 0.0 iTempVar2    //~1~

    //GET_FIXED_XY_ASPECT_RATIO 250.0 180.0 (sx sy)
    sx = 187.50
    sy = 168.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackInfo (79.0 165.0) (sx sy) (255 255 255 235)
RETURN

draw_car_chase_key_press_succesful:
    IF IS_PC_USING_JOYPAD
        iTempVar = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        iTempVar = 475    // ~h~LSHIFT ~s~CONTINUE         //164 // ~q~ CONTINUE
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 167.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 230.0) (sx sy) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) iTempVar 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------
//-+----------------------------------- 
draw_car_chase_summary_fail:
    //GET_FIXED_XY_ASPECT_RATIO 220.0 200.0 (sx sy)
    sx = 165.00
    sy = 186.67
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE tPBFailB (79.0 170.0) (sx sy) (255 255 255 180)

    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE tPBFailA (79.0 170.0) (sx sy) (255 255 255 230)

    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackFail (79.0 170.0) (sx sy) (255 255 255 220)
RETURN

draw_car_chase_key_press_fail:
    IF IS_PC_USING_JOYPAD
        iTempVar = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        iTempVar = 475    // ~h~LSHIFT ~s~CONTINUE         //164 // ~q~ CONTINUE
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 130.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 240.0) (sx sy) (19 18 13 0) (1.0) (0 0 0 0) (255 255 253 230) iTempVar 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------


//-+----------------------------------- BACKPACK (m_bp.sc)
load_textures_backpack:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spaim
            LOAD_SPRITE idMapIcon3 "mk3"   //backpack 
            LOAD_SPRITE backpackBack "btimC"
            LOAD_SPRITE idBP1 "ibp01"
            LOAD_SPRITE idBP2 "ibp02"
            LOAD_SPRITE idBP3 "ibp03"
            LOAD_SPRITE idBP4 "ibp04"
            LOAD_SPRITE idBP5 "ibp05"
            LOAD_SPRITE idBP6 "ibp06"
            LOAD_SPRITE idBP7 "ibp07"
            LOAD_SPRITE idBP8 "ibp08"
            LOAD_SPRITE idBP9 "ibp09"
            LOAD_SPRITE idBP10 "ibp10"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

select_backpack_texture_by_id:
    SWITCH iTempVar2
        CASE 0
            iTempVar3 = idBP1
            iTempVar4 = 477 //First Date Menu
            BREAK
        CASE 1
            iTempVar3 = idBP2
            iTempVar4 = 478
            BREAK
        CASE 2
            iTempVar3 = idBP3
            iTempVar4 = 479
            BREAK
        CASE 3
            iTempVar3 = idBP4
            iTempVar4 = 480
            BREAK
        CASE 4
            iTempVar3 = idBP5
            iTempVar4 = 481
            BREAK
        CASE 5
            iTempVar3 = idBP6
            iTempVar4 = 482
            BREAK
        CASE 6
            iTempVar3 = idBP7
            iTempVar4 = 483
            BREAK
        CASE 7
            iTempVar3 = idBP8
            iTempVar4 = 484
            BREAK
        CASE 8
            iTempVar3 = idBP9
            iTempVar4 = 485
            BREAK
        CASE 9
            iTempVar3 = idBP10
            iTempVar4 = 486     //Plushie
            BREAK
    ENDSWITCH
RETURN

draw_backpack_summary:
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE iTempVar3 (78.5 190.0) (130.00 130.00) (255 255 255 200)    //picture

    GET_FIXED_XY_ASPECT_RATIO 22.0 22.0 (sx sy)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idMapIcon3 (25.0 110.0) (sx sy) (255 255 255 235)      //icon backpack

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (75.0 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 476 18 0.0 0.0 // BACKPACK DISCOVERED
    iTempVar = 25   //amount XP
    CLEO_CALL GUI_DrawBox_WithNumber 0 (129.0 104.0) (50.0 15.0) (255 255 255 0) 127 21 0.0 0.0 iTempVar    //~1~ XP     //19
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 110.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (45.0 132.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) iTempVar4 1 0.0 0.0 //  Name backpack

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 230.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE backpackBack (79.0 180.0) (195.0 195.0) (255 255 255 235)    //background image
RETURN

draw_backpack_key_press:
    IF IS_PC_USING_JOYPAD
        itempVar = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        itempVar = 475    // ~h~LSHIFT ~s~CONTINUE         //164 // ~q~ CONTINUE
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 170.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 255.0) (sx sy) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) itempVar 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------

//-+----------------------------------- THUG HIDEOUTS
load_textures_thug_hideouts:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spaim
        LOAD_SPRITE idMapIcon5 "mk5"    //Crime
        LOAD_SPRITE tPBBackInfo "btimC"
        LOAD_SPRITE tPBBackFail "btimD"
        LOAD_SPRITE tPBFailA "mfailA"
        LOAD_SPRITE tPBFailB "mfailB"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_thug_hidouts_mission_succesful:
    //iTempVar2     // Total XP
    //iTempVar3     // Mission completed XP
    //iTempVar4     // Combat XP
    GET_FIXED_XY_ASPECT_RATIO 25.0 25.0 (sx sy)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idMapIcon5 (21.0 110.0) (sx sy) (255 255 255 235)

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 504 18 0.0 0.0 // Thug Hideouts
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.5 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 487 20 0.0 0.0 // COMPLETED!

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 115.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 135.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 473 1 0.0 0.0 //  XP SUMMARY

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 145.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 170 19 0.0 0.0  // Side Missions
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 146.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar3  //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 165.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 471 19 0.0 0.0  // Combat
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 166.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar4    //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 180.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 197.5) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 472 20 0.0 0.0  // TOTAL XP EARNED
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 195.0) (50.0 15.0) (255 255 255 0) 121 21 0.0 0.0 iTempVar2    //~1~

    //GET_FIXED_XY_ASPECT_RATIO 250.0 180.0 (sx sy)
    sx = 187.50
    sy = 168.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackInfo (79.0 165.0) (sx sy) (255 255 255 235)
RETURN

draw_thug_hideouts_key_press_succesful:
    IF IS_PC_USING_JOYPAD
        iTempVar = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        iTempVar = 475    // ~h~LSHIFT ~s~CONTINUE         //164 // ~q~ CONTINUE
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 167.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 230.0) (sx sy) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) iTempVar 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------


//-+----------------------------------- CRIMES
load_textures_street_crimes:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spaim
        LOAD_SPRITE idMapIcon5 "mk5"    //Crime
        LOAD_SPRITE tPBBackInfo "btimC"
        LOAD_SPRITE tPBBackFail "btimD"
        LOAD_SPRITE tPBFailA "mfailA"
        LOAD_SPRITE tPBFailB "mfailB"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_street_crimes_mission_succesful:
    //iTempVar2     // Total XP
    //iTempVar3     // Mission completed XP
    //iTempVar4     // Combat XP
    GET_FIXED_XY_ASPECT_RATIO 25.0 25.0 (sx sy)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idMapIcon5 (21.0 110.0) (sx sy) (255 255 255 235)

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (52.5 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 503 18 0.0 0.0  // Crimes
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.5 105.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 487 20 0.0 0.0 // COMPLETED!

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 115.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 135.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 473 1 0.0 0.0 //  XP SUMMARY

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 145.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 170 19 0.0 0.0  // Side Missions
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 146.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar3  //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 165.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 471 19 0.0 0.0  // Combat
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 166.0) (50.0 15.0) (255 255 255 0) 122 19 0.0 0.0 iTempVar4    //+~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 180.0) (120.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0 0.0  //SIDES_LINES division

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 197.5) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 472 20 0.0 0.0  // TOTAL XP EARNED
    CLEO_CALL GUI_DrawBox_WithNumber 0 (120.0 195.0) (50.0 15.0) (255 255 255 0) 121 21 0.0 0.0 iTempVar2    //~1~

    //GET_FIXED_XY_ASPECT_RATIO 250.0 180.0 (sx sy)
    sx = 187.50
    sy = 168.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackInfo (79.0 165.0) (sx sy) (255 255 255 235)
RETURN

draw_street_crimes_key_press_succesful:
    IF IS_PC_USING_JOYPAD
        iTempVar = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        iTempVar = 475    // ~h~LSHIFT ~s~CONTINUE         //164 // ~q~ CONTINUE
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 167.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 230.0) (sx sy) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) iTempVar 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------

}
SCRIPT_END


//-+--- CALL SCM HELPERS
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

// Thread Memory
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

// Textures ID
CONST_INT tPBBackInfo 4
CONST_INT tPBBackFail 5
CONST_INT tPBFailA 6
CONST_INT tPBFailB 7
CONST_INT idMapIcon5 8

CONST_INT idBackTbxA 10
CONST_INT idBackTbxB 11
CONST_INT idBackTbx 12
CONST_INT idIconWB 13
CONST_INT idIconHD 14
CONST_INT idIconBP 15
CONST_INT idIconSB 16
CONST_INT idIconNS 17
CONST_INT idIconEP 18
CONST_INT idIconRO 19
CONST_INT idIconBPJ 20
CONST_INT idIconLG 21
CONST_INT idIconIA 22
CONST_INT idIconDS 23
CONST_INT idIconSF 24

CONST_INT idMapIcon3 30
CONST_INT backpackBack 31    
CONST_INT idBP1 32
CONST_INT idBP2 33
CONST_INT idBP3 34
CONST_INT idBP4 35
CONST_INT idBP5 36
CONST_INT idBP6 37
CONST_INT idBP7 38
CONST_INT idBP8 39
CONST_INT idBP9 40
CONST_INT idBP10 41

