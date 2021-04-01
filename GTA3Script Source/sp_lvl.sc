// by J16D
// Level Control
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_lvl
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT toggleSpiderMod isInMainMenu isLvlup
LVAR_INT iSavedLevel iLevelUp iOldLevel iMaxTimeVarAnimation sfx
LVAR_FLOAT fSavedLevel fReward fCurrentLevel fCopyCurrentLevel
LVAR_FLOAT coordX[1] coordY[1] sizeX[2] sizeY[2]

READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (iSavedLevel)
READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setB" (fCurrentLevel)
CLEO_CALL unlock_spierman_suits 0 iSavedLevel

CONST_INT tLevelUpIcon 120
LOAD_TEXTURE_DICTIONARY spmn
LOAD_SPRITE tLevelUpIcon "lvli"

iLevelUp = FALSE

main_loop:
    IF IS_PLAYER_PLAYING player 
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isLvlup > 0
                READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (iSavedLevel)
                READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setB" (fCurrentLevel)
                iOldLevel = iSavedLevel
                fCopyCurrentLevel = fCurrentLevel
                //calculating
                CSET_LVAR_FLOAT_TO_LVAR_INT (fReward) isLvlup
                CLAMP_FLOAT fReward 0.0 2500.0 (fReward)

                //CLEO_CALL max_min_value_float 0 fReward 2500.0 0.0 (fReward)    // Max Reward +2500
                fCopyCurrentLevel += fReward    //New Max Value per-time
                //Time for bar-animation
                IF 2501.0 > fReward
                    iMaxTimeVarAnimation = 6500
                ELSE
                    IF 2000.0 > fReward
                        iMaxTimeVarAnimation = 3500
                    ELSE
                        IF 1000.0 > fReward
                            iMaxTimeVarAnimation = 2500
                        ENDIF
                    ENDIF
                ENDIF
                isLvlup =# fReward
                // bar-Animation
                timera = 0
                WHILE iMaxTimeVarAnimation > timera
                    fCurrentLevel +=@ 10.0
                    IF fCurrentLevel >= 1000.0
                        iLevelUp = TRUE
                        ++iSavedLevel
                        fCurrentLevel = 0.0
                        fCopyCurrentLevel -= 1000.0
                        CLEO_CALL unlock_spierman_suits 0 iSavedLevel
                    ENDIF
                    CLAMP_FLOAT fCurrentLevel 0.0 fCopyCurrentLevel (fCurrentLevel)
                    //CLEO_CALL max_min_value_float 0 fCurrentLevel fCopyCurrentLevel 0.0 (fCurrentLevel)
                    IF NOT IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                        GOSUB draw_level_progress
                    ENDIF
                    GOSUB readVars
                    IF isInMainMenu = 1     //1:true 0: false
                    OR toggleSpiderMod = 0
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                CLAMP_INT iSavedLevel 1 50 (iSavedLevel)    // Max LVL 50
                //CLEO_CALL limit_max_min_value_int 0 iSavedLevel 50 1 (iSavedLevel)   // Max LVL 50
                WRITE_INT_TO_INI_FILE iSavedLevel "CLEO\SpiderJ16D\config.ini" "config" "setA"
                WRITE_FLOAT_TO_INI_FILE fCurrentLevel "CLEO\SpiderJ16D\config.ini" "config" "setB"
                //to menu
                SET_CLEO_SHARED_VAR varLevelChar iSavedLevel
                SET_CLEO_SHARED_VAR varStatusLevelChar 0

                GOSUB readVars
                IF isInMainMenu = 1     //1:true 0: false
                OR toggleSpiderMod = 0
                    GOTO end_draw_level
                ENDIF

                IF iLevelUp = TRUE
                    GOSUB play_sfx_sound
                    timera = 0
                    WHILE 3000 > timera
                        IF NOT IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                            GOSUB draw_level_up
                        ENDIF
                        GOSUB readVars
                        IF isInMainMenu = 1     //1:true 0: false
                        OR toggleSpiderMod = 0
                            SET_AUDIO_STREAM_STATE sfx 0    //stop 
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                    WAIT 0
                    iLevelUp = FALSE
                    REMOVE_AUDIO_STREAM sfx
                ENDIF

                end_draw_level:

                /*
                //Tropy Unlcok Suits
                IF NOT iOldLevel = iSavedLevel

                    SWITCH iSavedLevel
                        CASE 3  //Group #0 ID: 1
                            WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit1"
                            BREAK
                        CASE 5  //Group #1 ID: 4-5
                            WRITE_INT_TO_INI_FILE 4465 "CLEO\SpiderJ16D\config.ini" "CODE" "suit4"
                            WRITE_INT_TO_INI_FILE 7952 "CLEO\SpiderJ16D\config.ini" "CODE" "suit5"
                            BREAK
                        CASE 7  //Group #2 ID: 6-7
                            WRITE_INT_TO_INI_FILE 8431 "CLEO\SpiderJ16D\config.ini" "CODE" "suit6"
                            WRITE_INT_TO_INI_FILE 5761 "CLEO\SpiderJ16D\config.ini" "CODE" "suit7"
                            BREAK
                        CASE 9  //Group #3 ID: 8-9
                            WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit8"
                            WRITE_INT_TO_INI_FILE 6784 "CLEO\SpiderJ16D\config.ini" "CODE" "suit9"
                            BREAK
                        CASE 12 //Group #4 ID: 10-11
                            WRITE_INT_TO_INI_FILE 3897 "CLEO\SpiderJ16D\config.ini" "CODE" "suit10"
                            WRITE_INT_TO_INI_FILE 4837 "CLEO\SpiderJ16D\config.ini" "CODE" "suit11"
                            BREAK
                        CASE 15 //Group #5 ID: 12-13
                            WRITE_INT_TO_INI_FILE 7913 "CLEO\SpiderJ16D\config.ini" "CODE" "suit12"
                            WRITE_INT_TO_INI_FILE 1937 "CLEO\SpiderJ16D\config.ini" "CODE" "suit13"
                            BREAK
                        CASE 17 //Group #6 ID: 14-15
                            WRITE_INT_TO_INI_FILE 8319 "CLEO\SpiderJ16D\config.ini" "CODE" "suit14"
                            WRITE_INT_TO_INI_FILE 6743 "CLEO\SpiderJ16D\config.ini" "CODE" "suit15"
                            BREAK
                        CASE 19 //Group #7 ID: 16-17
                            WRITE_INT_TO_INI_FILE 4627 "CLEO\SpiderJ16D\config.ini" "CODE" "suit16"
                            WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit17"
                            BREAK
                        CASE 23 //Group #8 ID: 18-19
                            WRITE_INT_TO_INI_FILE 7147 "CLEO\SpiderJ16D\config.ini" "CODE" "suit18"
                            WRITE_INT_TO_INI_FILE 9636 "CLEO\SpiderJ16D\config.ini" "CODE" "suit19"
                            BREAK
                        CASE 28 //Group #9 ID: 20-21
                            WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit20"
                            WRITE_INT_TO_INI_FILE 8525 "CLEO\SpiderJ16D\config.ini" "CODE" "suit21"
                            BREAK
                        CASE 33 //Group #10 ID: 22-23
                            WRITE_INT_TO_INI_FILE 7898 "CLEO\SpiderJ16D\config.ini" "CODE" "suit22"
                            WRITE_INT_TO_INI_FILE 1232 "CLEO\SpiderJ16D\config.ini" "CODE" "suit23"
                            BREAK
                        CASE 36 //Group #11 ID: 24-25
                            WRITE_INT_TO_INI_FILE 7319 "CLEO\SpiderJ16D\config.ini" "CODE" "suit24"
                            WRITE_INT_TO_INI_FILE 6731 "CLEO\SpiderJ16D\config.ini" "CODE" "suit25"
                            BREAK
                        CASE 38 //Group #12 ID: 26-27   //extra
                            WRITE_INT_TO_INI_FILE 1973 "CLEO\SpiderJ16D\config.ini" "CODE" "suit26"
                            WRITE_INT_TO_INI_FILE 2846 "CLEO\SpiderJ16D\config.ini" "CODE" "suit27"
                            BREAK
                        CASE 40 //Group #13 ID: 28-30   //extra
                            WRITE_INT_TO_INI_FILE 1917 "CLEO\SpiderJ16D\config.ini" "CODE" "suit28"
                            WRITE_INT_TO_INI_FILE 3734 "CLEO\SpiderJ16D\config.ini" "CODE" "suit29"
                            WRITE_INT_TO_INI_FILE 4913 "CLEO\SpiderJ16D\config.ini" "CODE" "suit30"
                            BREAK
                        DEFAULT
                            BREAK
                    ENDSWITCH

                ENDIF
                */


            ENDIF
            /* For testing
            IF IS_KEY_PRESSED VK_KEY_L
                GOSUB draw_level_progress
            ENDIF
            
            IF IS_KEY_PRESSED VK_KEY_T
                WHILE IS_KEY_PRESSED VK_KEY_T
                    WAIT 0
                ENDWHILE
                SET_CLEO_SHARED_VAR varStatusLevelChar 1500
            ENDIF
            */
            
        ELSE
            USE_TEXT_COMMANDS FALSE
            REMOVE_TEXTURE_DICTIONARY
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop

draw_level_progress:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (263.75 55.0) (12.5 15.0) (57 110 132 210) (0.5) (1 1 1 1) (255 255 253 210) 123 5 0.0  // XP
    CLEO_CALL GUI_DrawBox_WithNumber 0 (285.0 57.5) (30.0 10.0) (49 96 133 210) 122 6 0.0 isLvlup  //+~1~
    CLEO_CALL barFunc 0 fCurrentLevel coordX[0] (sizeX[0] sizeY[0])
    DRAW_RECT (320.0 50.0) (100.0 5.5) (7 202 190 100)              //sides
    DRAW_RECT (320.0 50.0) (100.0 sizeY[0]) (49 96 133 210)         //blue background
    DRAW_RECT (coordX[0] 50.0) (sizeX[0] sizeY[0]) (7 202 190 210)  //bar
    USE_TEXT_COMMANDS FALSE
RETURN
 
draw_level_up:
    IF 1250 > timera
        CLEO_CALL GUI_DrawBox_WithNumber 0 (320.0 80.0) (100.0 40.0) (58 68 92 200) 124 7 0.0 iOldLevel      //LEVEL ~1~
    ELSE
        CLEO_CALL GUI_DrawBox_WithNumber 0 (320.0 80.0) (100.0 40.0) (58 68 92 200) 124 8 0.0 iSavedLevel    //LEVEL ~1~
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO (60.0 60.0) (sizeX[1] sizeY[1])
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE tLevelUpIcon (320.0 50.0) (sizeX[1] sizeY[1]) (255 255 255 200)
RETURN

readVars:
    GET_CLEO_SHARED_VAR varStatusLevelChar (isLvlup)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

play_sfx_sound:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\lvl_up.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\lvl_up.mp3" (sfx)
        SET_AUDIO_STREAM_STATE sfx 1
    ENDIF
RETURN
}
SCRIPT_END


//-+---------------------------CALL-SCM-HELPERS------------------------------
{
//CLEO_CALL max_min_value_float 0 fValue fMax fMin (fValue) 
max_min_value_float:
    LVAR_FLOAT fValue
    LVAR_FLOAT fMax fMin
    IF fValue > fMax
        fValue = fMax
    ENDIF
    IF fValue < fMin
        fValue = fMin
    ENDIF
CLEO_RETURN 0 fValue
}
{
//CLEO_CALL limit_max_min_value_int 0 iValue iMax iMin (iValue) 
limit_max_min_value_int:
    LVAR_INT iValue
    LVAR_INT iMax iMin
    IF iValue > iMax
        iValue = iMax
    ENDIF
    IF iValue < iMin
        iValue = iMin
    ENDIF
CLEO_RETURN 0 iValue
}
{
//CLEO_CALL barFunc 0 /*size*/1000.0 /*pos*/posX (/*size*/sizeX sizeY)
barFunc:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2]
    LVAR_FLOAT xSize ySize
    var[1] = sizeBar
    var[1] /= 1000.0 //fresX
    //var[1] *= 300.0
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 12.0) (xSize ySize)  //var[1] = 100
    var[1] *= 100.0
    xSize = var[1]
    ySize = 4.98
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 270.0 //270+(100/2)= 320
CLEO_RETURN 0 var[0] xSize ySize
}
{
//CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (1920.0 1080.0) (sizX sizY)
GetXYSizeInScreenScaleByUserResolution:
    LVAR_FLOAT x y // In
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresX /= 640.0
    x /= fresX
    fresY /= 448.0
    y /= fresY
CLEO_RETURN 0 (x y)
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
//CLEO_CALL getRewardsInfo 0 /*mission*/0 /*rewards*/ rew1 rew2 rew3
getRewardsInfo:
    LVAR_INT counter    //In
    LVAR_TEXT_LABEL _lName _lstringVar
    LVAR_INT iCounterValues 
    LVAR_INT rew1 rew2 rew3
    STRING_FORMAT (_lName)"rew%i" counter
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "rewards" $_lName (_lstringVar)
        IF SCAN_STRING $_lstringVar "%i %i %i" iCounterValues (rew1 rew2 rew3)
            PRINT_FORMATTED_NOW "rew: %i" 1000 iCounterValues
        ELSE
            rew1 = 0
            rew2 = 0
            rew3 = 0
        ENDIF
    ELSE
        PRINT_FORMATTED_NOW "ERROR: Can't Read Rewards" 1500
        WAIT 1500
        CLEO_RETURN 0 rew1 rew2 rew3
    ENDIF
CLEO_RETURN 0 rew1 rew2 rew3
}
{
//CLEO_CALL setRewardsInfo 0 /*mission*/0 /*rewards*/ rew1 rew2 rew3
setRewardsInfo:
    LVAR_INT counter    //In
    LVAR_INT rew1 rew2 rew3
    LVAR_TEXT_LABEL _lName _lvarRew
    STRING_FORMAT (_lName)"rew%i" counter
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        STRING_FORMAT (_lvarRew) "%i %i %i" rew1 rew2 rew3
        WRITE_STRING_TO_INI_FILE $_lvarRew "cleo\SpiderJ16D\config.ini" "rewards" $_lName
        PRINT_FORMATTED_NOW "Done!" 1000
    ELSE
        PRINT_FORMATTED_NOW "ERROR: Can't Write Rewards" 1500
        WAIT 1500
        CLEO_RETURN 0
    ENDIF
CLEO_RETURN 0
}



//-+------------------------------IMGUI-EXTRA-------------------------------------
// This code is outdated.
{
GUI_DrawBoxOutline_WithText:
/*
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) OutlineSize (1.4) OutlineSides (1 1 0 1) OutlineRGBA (200 200 200 200) TextID -1 FormatID 1 Padding 3.0
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v8 = outline size
v9 = outline side top    
v10 = outline side right
v11 = outline side bottom
v12 = outline side left
v13 = outline r    
v14 = outline g
v15 = outline b
v16 = outline a
v17 = textid
v18 = formatid
v19 = padding
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_FLOAT v8
LVAR_INT v9 v10 v11 v12 v13 v14 v15 v16 v17 v18
LVAR_FLOAT v19
LVAR_FLOAT v20 v21 v22 v23 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Outlines
IF v12 = TRUE  //outline side left
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 -= v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v9 = TRUE  //outline side top
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 -= v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v10 = TRUE  //outline side right
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 += v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v11 = TRUE  //outline side bottom
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 += v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT (v20 v21) $vTextA
ENDIF
CLEO_RETURN 0

GUI_DrawBoxOutline_VarsBoxToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
    v23 = v3
RETURN
}
{
GUI_DrawBox_WithNumber:
/*
//CLEO_CALL GUI_DrawBox_WithNumber 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) TextID -1 FormatID 1 Padding 3.0 number 5
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v17 = textid
v18 = formatid
v19 = padding
v8 = number
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_INT v17 v18
LVAR_FLOAT v19
LVAR_INT v8
LVAR_FLOAT v20 v21 v22 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBox_VarsBoxNumberToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT_WITH_NUMBER (v20 v21) $vTextA v8
ENDIF
CLEO_RETURN 0

GUI_DrawBox_VarsBoxNumberToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
RETURN
}
{
GUI_SetTextFormatByID:
//CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
LVAR_INT iID
LVAR_FLOAT fPadding
LVAR_INT ptAlpha
LVAR_FLOAT vtAlpha
SWITCH iID
    CASE 1
        GOSUB GUI_TextFormat_ItemMenu
        fPadding = 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_ItemMenu_Active
        fPadding = 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu
        fPadding = 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active
        fPadding = 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_MediumMenu
        fPadding = 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_MediumMenu_Numbers
        fPadding = 5.0
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_BigMenu_Numbers
        fPadding = 6.0
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_BigMenu_Numbers_Color
        fPadding = 6.0
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_MediumTitle
        fPadding = 5.0
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_SmallItemMenu_Colour
        fPadding = 5.0
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_SmallItemMenu
        fPadding = 5.0
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_MediumItemTitle
        fPadding = 4.5
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_TextReward1_Colour
        fPadding = 4.5
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_TextReward2_Colour
        fPadding = 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_TextReward3_Colour
        fPadding = 4.5
        BREAK
ENDSWITCH
CLEO_RETURN 0 fPadding

GUI_TextFormat_ItemMenu:            //1   Title Finish Awards   (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.25 1.2084
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_ItemMenu_Active:     //2 Title Blue Medium (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.16 0.7734
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu:           //3 small letters (for score) 
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu_Active:    //4  format for numbers (for score) white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.692
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
RETURN

GUI_TextFormat_MediumMenu:          //5  format XP level text
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.21 1.015
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 15 236 198 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_MediumMenu_Numbers:  //6  format XP level Numbers
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9184
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 211 226 224 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_BigMenu_Numbers:     //7     numbers white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 252 252 255 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_BigMenu_Numbers_Color:   //8     numbers blue
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 35 237 193 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_MediumTitle:         //9     Text medium format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN    

GUI_TextFormat_SmallItemMenu_Colour:     //10 Title Blue Small (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallItemMenu:       //11    Text small format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_MediumItemTitle:     //12     Text Medium / Names
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_TextReward1_Colour:     //13    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 59 36 23 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward2_Colour:     //14    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 12 60 78 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward3_Colour:     //15    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 72 63 17 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN
}
///--------------------------------UNLOCK-----------------------------
//READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (iSavedLevel)
{
//CLEO_CALL unlock_spierman_suits 0 /*Level*/iLevel
unlock_spierman_suits:
    LVAR_INT iSavedLevel    //In
    IF iSavedLevel > 2
        SWITCH iSavedLevel
            CASE 3  //Group #0 ID: 1
                WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit1"
                BREAK
            CASE 5  //Group #1 ID: 4-5
                WRITE_INT_TO_INI_FILE 4465 "CLEO\SpiderJ16D\config.ini" "CODE" "suit4"
                WRITE_INT_TO_INI_FILE 7952 "CLEO\SpiderJ16D\config.ini" "CODE" "suit5"
                BREAK
            CASE 7  //Group #2 ID: 6-7
                WRITE_INT_TO_INI_FILE 8431 "CLEO\SpiderJ16D\config.ini" "CODE" "suit6"
                WRITE_INT_TO_INI_FILE 5761 "CLEO\SpiderJ16D\config.ini" "CODE" "suit7"
                BREAK
            CASE 9  //Group #3 ID: 8-9
                WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit8"
                WRITE_INT_TO_INI_FILE 6784 "CLEO\SpiderJ16D\config.ini" "CODE" "suit9"
                BREAK
            CASE 12 //Group #4 ID: 10-11
                WRITE_INT_TO_INI_FILE 3897 "CLEO\SpiderJ16D\config.ini" "CODE" "suit10"
                WRITE_INT_TO_INI_FILE 4837 "CLEO\SpiderJ16D\config.ini" "CODE" "suit11"
                BREAK
            CASE 15 //Group #5 ID: 12-13
                WRITE_INT_TO_INI_FILE 7913 "CLEO\SpiderJ16D\config.ini" "CODE" "suit12"
                WRITE_INT_TO_INI_FILE 1937 "CLEO\SpiderJ16D\config.ini" "CODE" "suit13"
                BREAK
            CASE 17 //Group #6 ID: 14-15
                WRITE_INT_TO_INI_FILE 8319 "CLEO\SpiderJ16D\config.ini" "CODE" "suit14"
                WRITE_INT_TO_INI_FILE 6743 "CLEO\SpiderJ16D\config.ini" "CODE" "suit15"
                BREAK
            CASE 19 //Group #7 ID: 16-17
                WRITE_INT_TO_INI_FILE 4627 "CLEO\SpiderJ16D\config.ini" "CODE" "suit16"
                WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit17"
                BREAK
            CASE 23 //Group #8 ID: 18-19
                WRITE_INT_TO_INI_FILE 7147 "CLEO\SpiderJ16D\config.ini" "CODE" "suit18"
                WRITE_INT_TO_INI_FILE 9636 "CLEO\SpiderJ16D\config.ini" "CODE" "suit19"
                BREAK
            CASE 28 //Group #9 ID: 20-21
                WRITE_INT_TO_INI_FILE 9999 "CLEO\SpiderJ16D\config.ini" "CODE" "suit20"
                WRITE_INT_TO_INI_FILE 8525 "CLEO\SpiderJ16D\config.ini" "CODE" "suit21"
                BREAK
            CASE 33 //Group #10 ID: 22-23
                WRITE_INT_TO_INI_FILE 7898 "CLEO\SpiderJ16D\config.ini" "CODE" "suit22"
                WRITE_INT_TO_INI_FILE 1232 "CLEO\SpiderJ16D\config.ini" "CODE" "suit23"
                BREAK
            CASE 36 //Group #11 ID: 24-25
                WRITE_INT_TO_INI_FILE 7319 "CLEO\SpiderJ16D\config.ini" "CODE" "suit24"
                WRITE_INT_TO_INI_FILE 6731 "CLEO\SpiderJ16D\config.ini" "CODE" "suit25"
                BREAK
            CASE 38 //Group #12 ID: 26-27   //extra
                WRITE_INT_TO_INI_FILE 1973 "CLEO\SpiderJ16D\config.ini" "CODE" "suit26"
                WRITE_INT_TO_INI_FILE 2846 "CLEO\SpiderJ16D\config.ini" "CODE" "suit27"
                BREAK
            CASE 40 //Group #13 ID: 28-30   //extra
                WRITE_INT_TO_INI_FILE 1917 "CLEO\SpiderJ16D\config.ini" "CODE" "suit28"
                WRITE_INT_TO_INI_FILE 3734 "CLEO\SpiderJ16D\config.ini" "CODE" "suit29"
                WRITE_INT_TO_INI_FILE 4913 "CLEO\SpiderJ16D\config.ini" "CODE" "suit30"
                BREAK
            DEFAULT
                BREAK
        ENDSWITCH
    ENDIF
CLEO_RETURN 0
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



/*
    LVAR_INT iGroupLocked[11]
    //Group #1 ID: 4-5
    iGroupLocked[0]
    //Group #2 ID: 6-7
    iGroupLocked[1]
    //Group #3 ID: 8-9
    iGroupLocked[2]
    //Group #4 ID: 10-11
    iGroupLocked[3]
    //Group #5 ID: 12-13
    iGroupLocked[4]
    //Group #6 ID: 14-15
    iGroupLocked[5]
    //Group #7 ID: 16-17
    iGroupLocked[6]
    //Group #8 ID: 18-19
    iGroupLocked[7]
    //Group #9 ID: 20-21
    iGroupLocked[8]
    //Group #10 ID: 22-23
    iGroupLocked[9]
    //Group #11 ID: 24-25
    iGroupLocked[10]
    //Group ID: 26-30 //EXTRA
*/

/*
                READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setA" (fSavedLevel)
                READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "setB" (fCurrentLevel)
                iOldLevel =# fSavedLevel
                iOldLevel /= 1000
                fCopyCurrentLevel = fCurrentLevel   //100
                //calculating
                CSET_LVAR_FLOAT_TO_LVAR_INT (fReward) isLvlup
                fCopyCurrentLevel += fReward    //New Max Value per-time    750+500=1250
                //Time for bar-animation
                IF fCopyCurrentLevel > 1000.0
                    iMaxTimeVarAnimation = 3500
                ELSE
                    IF 1000.0 > fCopyCurrentLevel
                         iMaxTimeVarAnimation = 2500
                    ENDIF
                ENDIF
                //save data with reward
                fSavedLevel += fReward  //1750+500=1750
                // bar-Animation
                timera = 0
                WHILE iMaxTimeVarAnimation > timera
                    fCurrentLevel +=@ 10.0
                    IF fCurrentLevel >= 1000.0
                        iLevelUp = TRUE
                        fCurrentLevel = 0.0
                        fCopyCurrentLevel -= 1000.0
                    ENDIF
                    CLEO_CALL max_min_value_float 0 fCurrentLevel fCopyCurrentLevel 0.0 (fCurrentLevel)
                    GOSUB draw_level_progress
                    WAIT 0
                ENDWHILE
                CLEO_CALL max_min_value_float 0 fSavedLevel 50000.0 1000.0 (fSavedLevel)    // Max LVL 50
                WRITE_FLOAT_TO_INI_FILE fSavedLevel "CLEO\SpiderJ16D\config.ini" "config" "setA"
                WRITE_FLOAT_TO_INI_FILE fCurrentLevel "CLEO\SpiderJ16D\config.ini" "config" "setB"
                //to menu
                iSavedLevel =# fSavedLevel
                iSavedLevel /= 1000
                SET_CLEO_SHARED_VAR varLevelChar iSavedLevel
                SET_CLEO_SHARED_VAR varStatusLevelChar 0
                IF iLevelUp = TRUE
                    timera = 0
                    WHILE 3000 > timera
                        GOSUB draw_level_up
                        WAIT 0
                    ENDWHILE
                ENDIF
*/