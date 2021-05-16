// by J16D
// Missions Summary "B"
// in Colaboration with GTA Loyalty
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

/* GUIDE
INCLUDED:
    ID:1  ||MISSION FAIL
    ID:2  ||CAZANDO FELINOS SAMS-M2 PASSED
    ID:3  ||CALLS IDENTIFICATOR
FORMAT:
    ID:1
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" {id}
    ID:2
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" {id} {total xp} {mission xp} {combat xp}
    ID:3
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" {id} {character_id}
    ID:4
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" {id} {mission_id} {text1_id} {text2_id}
*/

SCRIPT_START
{
SCRIPT_NAME sp_prtb
LVAR_INT idVar  //in
LVAR_INT iTempVar2 iTempVar3 iTempVar4      //in
//---
LVAR_INT toggleSpiderMod isInMainMenu audio_line_is_active is_radar_enabled
LVAR_INT counter idPowers sfx r g b iTempVar
LVAR_FLOAT sx sy px py

USE_TEXT_COMMANDS TRUE
SWITCH idVar
    CASE 1  //ID:1  ||MISSION FAIL
        // IN: {id}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_mission_fail
        timera = 0
        WHILE TRUE
            GOSUB draw_summary_mission_fail
            GOSUB draw_key_press_mission_fail
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
    CASE 2  //ID:2  ||CAZANDO FELINOS M2 PASSED
        // IN: {id} {total xp} {mission xp} {combat xp}
        GOSUB play_sfx_mission_end
        GOSUB load_textures_sams_m2
        timera = 0
        WHILE TRUE
            GOSUB draw_sams_m2_mission_succesful
            GOSUB draw_sams_m2_key_press_succesful
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
    CASE 3  //ID:3  ||CALLS IDENTIFICATOR
        GOSUB load_textures_calls_cellphone
        WHILE TRUE
            GET_CLEO_SHARED_VAR varHudRadar (is_radar_enabled)
            IF is_radar_enabled = 1     // 0:OFF || 1:ON
                px = 568.0
                py = 330.0
                GOSUB draw_calls_cellphone
                CLEO_CALL generate_anim_bars_speech 0 (533.0 330.0) (16 202 211 150)    //(46 117 156 150)
            ELSE
                px = 60.0
                py = 310.0
                GOSUB draw_calls_cellphone
                CLEO_CALL generate_anim_bars_speech 0 (25.0 310.0) (16 202 211 150)    //(46 117 156 150)
            ENDIF
            GOSUB readVars
            IF audio_line_is_active = 0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        BREAK
    CASE 4  //ID:4  || Main Mission label
        // IN: {id} {mission_id} {text1_id} {text2_id}
        GOSUB load_textures_mission_label
        timera = 0
        WHILE TRUE
            px = 568.0
            py = 120.0
            GOSUB draw_mission_labels
            IF timera > 6000
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
    GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
RETURN

//-+----------------------------------- 
play_sfx_mission_end:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\event_ended.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\event_ended.mp3" (sfx)
        SET_AUDIO_STREAM_STATE sfx 1
        WAIT 0
    ENDIF
RETURN
//-+----------------------------------- 

//-+----------------------------------- 
load_textures_mission_fail:
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

draw_summary_mission_fail:
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

draw_key_press_mission_fail:
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

//-+----------------------------------- SAMS - Mission 2 - Chasing Balck Cat
load_textures_sams_m2:
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

draw_sams_m2_mission_succesful:
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

draw_sams_m2_key_press_succesful:
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
load_textures_calls_cellphone:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spsams
        LOAD_SPRITE idCallBack "calls_b"
        LOAD_SPRITE idCallBC "call_bc"
        LOAD_SPRITE idCallMJ "call_mj"
        LOAD_SPRITE idCallJJJ "call_jjj"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_calls_cellphone:
    sx = 125.0
    sy = 80.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idCallBack (px py) (sx sy) (255 255 255 170)
    SWITCH iTempVar2
        CASE 0  //Mary Jane
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idCallMJ (px py) (sx sy) (255 255 255 235)
            BREAK
        CASE 1  //BLack Cat
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idCallBC (px py) (sx sy) (255 255 255 235)
            BREAK
        CASE 2  //JJ Jameson
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idCallJJJ (px py) (sx sy) (255 255 255 235)
            BREAK
    ENDSWITCH
RETURN
//-+-----------------------------------

//-+-----------------------------------
load_textures_mission_label:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spsams
        LOAD_SPRITE idMainMission "m_main"
        LOAD_SPRITE idSideMission "m_side"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_mission_labels:
    // IN: {id} {mission_id} {text1_id} {text2_id}
    //iTempVar2 = mission_id
    //iTempVar3 = text1_id
    //iTempVar4 = text2_id
    sx = 140.0
    sy = 90.0
    SWITCH iTempVar2    //mission_id
        CASE 0  //Main Mission
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idMainMission (px py) (sx sy) (255 255 255 250)
            BREAK
        CASE 1  //Side Mission
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idSideMission (px py) (sx sy) (255 255 255 250)
            BREAK
    ENDSWITCH
    px = 568.0
    py = 106.0
    GET_FIXED_XY_ASPECT_RATIO 140.0 20.0 (sx sy)
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (px py) (sx sy) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) iTempVar3 19 0.0 0.0
    USE_TEXT_COMMANDS FALSE
    py = 127.5
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (px py) (sx sy) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) iTempVar4 15 -30.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN
//-+-----------------------------------

}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL generate_anim_bars_speech 0 (posx posy) (r g b a)
generate_anim_bars_speech:
    LVAR_FLOAT posx posy    //in
    LVAR_INT r g b a   //in
    LVAR_FLOAT fSize1 fSize2 fSize3 fSize4 fSize5
    LVAR_FLOAT fRand1 fRand2 fRand3 fRand4 fRand5
    LVAR_FLOAT fPos1 fPos2 fPos3 fPos4 fPos5
    LVAR_INT counter
    counter = 0
    WHILE 12 >= counter
        fSize1 = 100.0
        GENERATE_RANDOM_FLOAT_IN_RANGE 1.0 fSize1 (fRand1)
        fSize1 -= fRand1
        fSize1 /= 100.0
        fSize1 *= -15.0
        fPos1 = fSize1
        fPos1 /= 2.0
        fPos1 += posy
        USE_TEXT_COMMANDS FALSE
        DRAW_RECT posx fPos1 3.0 fSize1 r g b a
        posx += 3.5
        ++counter
    ENDWHILE
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
    STRING_FORMAT gxt "JDSM%i" textId
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
    STRING_FORMAT gxt "JDSM%i" textId
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

CONST_INT varAudioActive     	45    // 0:OFF || 1:ON  ||global var to check -spech- audio playing

// Textures ID
CONST_INT tPBBackInfo 4
CONST_INT tPBBackFail 5
CONST_INT tPBFailA 6
CONST_INT tPBFailB 7
CONST_INT idMapIcon5 8

CONST_INT idCallBack 19
CONST_INT idCallBC 20
CONST_INT idCallMJ 21
CONST_INT idCallJJJ 22

CONST_INT idMainMission 23
CONST_INT idSideMission 24
