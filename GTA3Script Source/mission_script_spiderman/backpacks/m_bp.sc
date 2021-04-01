// by J16D
// Backpacks
// Format:
//      STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_bp.cs"
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME m_bp
LVAR_INT player_actor toggleSpiderMod flag_player_on_mission isInMainMenu
LVAR_INT iTempVar counter iObj iCleoBlip iTempVar2 iTempVar3 iSfx
LVAR_FLOAT x y z x2 y2 z2 v1 v2

GET_PLAYER_CHAR 0 player_actor

CLEO_CALL store_backpack_current_progress 0 ()
CLEO_CALL save_backpack_map_coords 0 ()
GOSUB create_backpacks
GOSUB loadTextures
GOSUB add_map_blips

main_loop:
    IF IS_PLAYER_PLAYING 0
        IF NOT IS_ON_MISSION
            GOSUB readVars
            IF toggleSpiderMod = 1  //TRUE
                IF isInMainMenu = 0     //1:true 0: false

                    counter = 0
                    WHILE 9 >= counter
                        CLEO_CALL get_backpack_objects 0 counter iObj
                        IF DOES_OBJECT_EXIST iObj
                            IF LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 2.0
                                GOSUB draw_indicator
                                GOSUB draw_help_tip

                                IF IS_BUTTON_PRESSED PAD1 TRIANGLE    // ~k~~VEHICLE_ENTER_EXIT~
                                    //Objects
                                    DELETE_OBJECT iObj
                                    CLEO_CALL store_backpack_objects 0 counter 0x0

                                    //Cleo Blips
                                    CLEO_CALL get_backpack_blips 0 counter (iCleoBlip)
                                    REMOVE_CLEO_BLIP iCleoBlip
                                    CLEO_CALL store_backpack_blips 0 counter 0x0

                                    //save values
                                    CLEO_CALL add_progress_backpacks 0 counter ()
                                    CLEO_CALL get_current_backpack_progress 0 (iTempVar)
                                    WRITE_INT_TO_INI_FILE iTempVar "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_bpacks"
                                    SET_CLEO_SHARED_VAR varBackpacksProgress iTempVar
                                    GOSUB draw_found_backpack

                                    WHILE  IS_BUTTON_PRESSED PAD1 TRIANGLE    // ~k~~VEHICLE_ENTER_EXIT~
                                        WAIT 0
                                    ENDWHILE                                    
                                ENDIF
                            ENDIF
                        ENDIF
                        counter ++
                    ENDWHILE

                ENDIF
            ELSE
                GOSUB remove_map_objects_and_blips
                USE_TEXT_COMMANDS FALSE
                WAIT 0
                REMOVE_TEXTURE_DICTIONARY
                WAIT 0
                TERMINATE_THIS_CUSTOM_SCRIPT
            ENDIF
        ENDIF 
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

loadTextures:
    CONST_INT idLockd 4
    CONST_INT idLocke 5
    CONST_INT idTip2 6
    CONST_INT idMapIcon3 7
    CONST_INT idMapIcon5 8
    
    LOAD_TEXTURE_DICTIONARY spaim
    LOAD_SPRITE idLockd "ilockd"
    LOAD_SPRITE idLocke "ilocke"
    LOAD_SPRITE idTip2 "htip2"
    LOAD_SPRITE idMapIcon3 "mk3"   //backpack 
    LOAD_SPRITE idMapIcon5 "mk5"    //Crime    
RETURN

draw_indicator:
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 0.0 -0.4 (x2 y2 z2)
    CONVERT_3D_TO_SCREEN_2D (x2 y2 z2) TRUE TRUE (v1 v2) (z z)
    GET_FIXED_XY_ASPECT_RATIO 60.0 60.0 (x y)
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idLockd (v1 v2) (x y) (255 255 255 230)

    IF IS_PC_USING_JOYPAD
        iTempVar = 652  //~k~~VEHICLE_ENTER_EXIT~
        CLEO_CALL GUI_DrawHelperText 0 (v1 v2) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ELSE
        iTempVar = 653  //~h~F
        CLEO_CALL GUI_DrawHelperText 0 (v1 v2) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ENDIF
RETURN

draw_help_tip:
    //GET_FIXED_XY_ASPECT_RATIO 120.0 60.0 (x[0] y[0])
    x = 90.00
    y = 56.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idTip2 (50.0 420.0) (x y) (255 255 255 200)

    IF IS_PC_USING_JOYPAD
        iTempVar = 650  //~k~~VEHICLE_ENTER_EXIT~ ~s~Web Backpack
        CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ELSE
        iTempVar = 651  //~h~F ~s~Web Backpack
        CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ENDIF
RETURN

create_backpacks:
    REQUEST_MODEL 1580  //drug_red
    LOAD_ALL_MODELS_NOW

    counter = 0
    WHILE 9 >= counter
        CLEO_CALL get_backpack_map_coords 0 counter (x y z)
        IF NOT x = 0.0
        AND NOT y = 0.0
        AND NOT z = 0.0
            
            CLEO_CALL get_existing_backpack 0 counter (iTempVar)
            IF iTempVar = 0 // if backpack isn't collected
                CREATE_OBJECT_NO_OFFSET 1580 x y z iObj
                SET_OBJECT_DYNAMIC iObj FALSE
                SET_OBJECT_COLLISION_DAMAGE_EFFECT iObj FALSE
                SET_OBJECT_PROOFS iObj TRUE TRUE TRUE TRUE TRUE
                DONT_REMOVE_OBJECT iObj
                SWITCH counter  //set rotation for objects
                    CASE 0
                        SET_OBJECT_ROTATION iObj 0.0 0.0 90.0 //wall
                        BREAK
                    CASE 1
                        SET_OBJECT_ROTATION iObj -90.0 0.0 0.0 //ground
                        BREAK
                    CASE 2
                        SET_OBJECT_ROTATION iObj 0.0 0.0 270.0 //wall
                        BREAK
                    CASE 3
                        SET_OBJECT_ROTATION iObj 0.0 0.0 90.0 //wall
                        BREAK
                    CASE 4
                        SET_OBJECT_ROTATION iObj 0.0 0.0 180.0 //wall
                        BREAK
                    CASE 5
                        SET_OBJECT_ROTATION iObj -90.0 0.0 180.0 //ground
                        BREAK
                    CASE 6
                        SET_OBJECT_ROTATION iObj 0.0 0.0 270.0 //wall
                        BREAK
                    CASE 7
                        SET_OBJECT_ROTATION iObj -90.0 0.0 0.0 //ground
                        BREAK
                    CASE 8
                        SET_OBJECT_ROTATION iObj 0.0 0.0 270.0 //wall
                        BREAK
                    CASE 9
                        SET_OBJECT_ROTATION iObj 0.0 0.0 270.0 //wall
                        BREAK
                    DEFAULT
                        BREAK
                ENDSWITCH
                CLEO_CALL store_backpack_objects 0 counter iObj
            ENDIF
        ENDIF
        counter ++
        WAIT 0
    ENDWHILE
    WAIT 0
    MARK_MODEL_AS_NO_LONGER_NEEDED 1580

    //SET_OBJECT_ROTATION iObj -90.0 0.0 0.0 //ground
    //SET_OBJECT_ROTATION iObj 0.0 0.0 0.0 //vertical
RETURN

add_map_blips: //CLEO+
    counter = 0
    WHILE 9 >= counter
        CLEO_CALL get_backpack_map_coords 0 counter (x y z)
        IF NOT x = 0.0
        AND NOT y = 0.0
        AND NOT z = 0.0
            CLEO_CALL get_existing_backpack 0 counter (iTempVar)
            IF iTempVar = 0 // if backpack isn't collected
                ADD_CLEO_BLIP -7 x y TRUE 255 255 255 245 (iCleoBlip)
                CLEO_CALL store_backpack_blips 0 counter iCleoBlip
            ELSE
                CLEO_CALL store_backpack_blips 0 counter 0x0
            ENDIF
        ELSE
            CLEO_CALL store_backpack_blips 0 counter 0x0
        ENDIF
        counter ++
        WAIT 0
    ENDWHILE
RETURN

remove_map_objects_and_blips: //CLEO+
    counter = 0
    WHILE 9 >= counter
        CLEO_CALL get_backpack_objects 0 counter (iObj)
        IF DOES_OBJECT_EXIST iObj
            //Objects
            DELETE_OBJECT iObj
            CLEO_CALL store_backpack_objects 0 counter 0x0

            //Cleo Blips
            CLEO_CALL get_backpack_blips 0 counter (iCleoBlip)
            REMOVE_CLEO_BLIP iCleoBlip
            CLEO_CALL store_backpack_blips 0 counter 0x0
        ENDIF
        counter ++
        WAIT 0
    ENDWHILE
RETURN

draw_found_backpack:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 4 counter   //{id} {backpack id}
        WAIT 2000
    ENDIF
    iTempVar = 25     //+25 XP
    SET_CLEO_SHARED_VAR varStatusLevelChar iTempVar   //set value of +25 xp
RETURN
}
SCRIPT_END

{
//CLEO_CALL store_backpack_objects 0 counter object
store_backpack_objects:
    LVAR_INT counter iObj //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    IF DOES_OBJECT_EXIST iObj
        WRITE_MEMORY pActiveItem 4 iObj FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL get_backpack_objects 0 counter (object)
get_backpack_objects:
    LVAR_INT counter //in
    LVAR_INT pActiveItem pTempVar iObj
    GET_LABEL_POINTER bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iObj)
    IF NOT DOES_OBJECT_EXIST iObj
        CLEO_RETURN 0 -1
    ENDIF
CLEO_RETURN 0 iObj
}
{
//CLEO_CALL get_existing_backpack 0 counter (iTempVar)
get_existing_backpack:
    LVAR_INT counter   //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
    pActiveItem += counter
    READ_MEMORY (pActiveItem) 1 FALSE (pTempVar)
CLEO_RETURN 0 pTempVar
}
{
//CLEO_CALL get_current_backpack_progress 0 (total_passed)
get_current_backpack_progress:
    LVAR_INT counter pActiveItem iTempVar iTotalPassed
    iTotalPassed = 0
    counter = 0
    WHILE 9 >= counter
        GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
        pActiveItem += counter
        READ_MEMORY (pActiveItem) 1 FALSE (iTempVar)
        IF iTempVar = 1
            iTotalPassed ++
        ENDIF
        counter ++
    ENDWHILE
    /*IF counter = 10
        CLEO_RETURN 0 counter
    ENDIF*/
CLEO_RETURN 0 iTotalPassed
}
{
//CLEO_CALL add_progress_backpacks 0 counter ()
add_progress_backpacks:
    LVAR_INT counter //in
    LVAR_INT pActiveItem iTempVar
    GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
    pActiveItem += counter
    READ_MEMORY (pActiveItem) 1 FALSE (iTempVar)
    IF iTempVar = 0
        WRITE_MEMORY pActiveItem 1 1 FALSE
    ENDIF
    //PRINT_FORMATTED_NOW "value written on slot:%i" 1000 counter //debug
    CLEO_CALL set_backpack_current_progress 0 ()
CLEO_RETURN 0
}
{
//CLEO_CALL set_backpack_current_progress 0 ()
set_backpack_current_progress:
    LVAR_INT iVarTextString
    LVAR_INT a b c d e f g h i j iTemp pActiveItem 
    GET_LABEL_POINTER buffer_backpacks_bytes10 (pActiveItem)
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
    pActiveItem ++
    READ_MEMORY (pActiveItem) 1 FALSE (j)
    GET_LABEL_POINTER bytes32 (iVarTextString)
    STRING_FORMAT (iVarTextString) "%d %d %d %d %d %d %d %d %d %d" a b c d e f g h i j
    WRITE_STRING_TO_INI_FILE $iVarTextString "cleo\SpiderJ16D\config.ini" "events" "m_bpck"
CLEO_RETURN 0
}
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
//CLEO_CALL get_backpack_map_coords 0 counter (x y z)
get_backpack_map_coords:
    LVAR_INT counter   //in
    LVAR_INT pActiveItem pTempVar
    LVAR_FLOAT x y z
    GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
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
{
//CLEO_CALL save_backpack_map_coords 0 ()
save_backpack_map_coords:
    LVAR_TEXT_LABEL _lName
    LVAR_TEXT_LABEL16 _lSection
    LVAR_INT iTemp counter pActiveItem pTempVar iVar iStartCounter iEndCounter
    LVAR_FLOAT x y z
    GET_LABEL_POINTER bytes32 (iVar)
    iStartCounter = 0
    iEndCounter = 9
    counter = iStartCounter
    WHILE iEndCounter >= counter
        IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
            GET_TEXT_LABEL_STRING SP_BPCK (_lSection)
            STRING_FORMAT (_lName)"bpck%i" counter
            READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" $_lSection $_lName (iVar)
            IF NOT SCAN_STRING $iVar "%f %f %f" iTemp (x y z)
                x = 0.0
                y = 0.0
                z = 0.0
            ENDIF
            GET_LABEL_POINTER CoordsBackpacksBuffer (pActiveItem)
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
//CLEO_CALL store_backpack_blips 0 counter iCleoBlip
store_backpack_blips:
    LVAR_INT counter   //in
    LVAR_INT iBlip      //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER buffer_backpack_blips (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    WRITE_MEMORY pActiveItem 4 iBlip FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL get_backpack_blips 0 counter (iCleoBlip)
get_backpack_blips:
    LVAR_INT counter   //in
    LVAR_INT pActiveItem pTempVar iBlip
    GET_LABEL_POINTER buffer_backpack_blips (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iBlip)
CLEO_RETURN 0 iBlip
}


{
//CLEO_CALL GUI_DrawHelperText 0 /*pos*/(320.0 240.0) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawHelperText:
LVAR_FLOAT posX posY    // In
LVAR_INT textId formatId    //in
LVAR_FLOAT paddingLeft paddingTop   //in
LVAR_FLOAT h
LVAR_TEXT_LABEL gxt
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "SP%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    ELSE
        SET_TEXT_CENTRE FALSE
    ENDIF
    posX += paddingLeft
    CLEO_CALL GUI_SetTextFormatByID_b 0 formatId (h)
    posY -= h
    posY += paddingTop
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID_b:
LVAR_INT formatId //In
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Small
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Medium  
        CLEO_RETURN 0 4.0
        BREAK
ENDSWITCH

GUI_TextFormat_Small:   //White
    SET_TEXT_COLOUR 255 255 255 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.14 0.65 //0.12 0.58 //0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Medium:  //White
    SET_TEXT_COLOUR 255 255 255 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.18 0.87 //0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
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

buffer_backpack_blips:  //bytes40 
DUMP
00000000 00000000 00000000 00000000 00000000
00000000 00000000 00000000 00000000 00000000
ENDDUMP

bytes32:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
ENDDUMP

buffer_backpacks_bytes10:
DUMP
00 00 00 00 00 00 00 00 00 00
ENDDUMP

bytes40:
DUMP
00000000 00000000 00000000 00000000 00000000 
00000000 00000000 00000000 00000000 00000000 
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
