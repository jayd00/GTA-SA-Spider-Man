// by J16D
// Hit Counter v1 - events CLEO+
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0
CONST_INT reset_time 4000   //MS

SCRIPT_START
{
NOP
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
LVAR_FLOAT x y z
LVAR_INT eventArgVar char iHitCounter pEntity iEntityType j

main_loop:
IF IS_PLAYER_PLAYING player
    IF TEST_CHEAT "YT"
        SET_SCRIPT_EVENT_CHAR_DAMAGE ON task_assign eventArgVar
        WAIT 1000
        WHILE NOT TEST_CHEAT "YT"
            IF timera > reset_time
                iHitCounter = 0
            ENDIF
            CLEO_CALL GUI_display_text 0 40.0 140.0 1 2    //PARAMS: posX posY id_text id_format_text
            IF iHitCounter > 0
                CLEO_CALL GUI_display_number 0 40.0 150.0 2 1 iHitCounter  //PARAMS: posX posY id_text id_format iNumber
            ELSE
                CLEO_CALL GUI_display_text 0 40.0 150.0 3 1    //PARAMS: posX posY id_text id_format_text
            ENDIF
            WAIT 0
        ENDWHILE
        SET_SCRIPT_EVENT_CHAR_DAMAGE OFF task_assign eventArgVar
        WAIT 1500
    ENDIF
ENDIF
    WAIT 0
GOTO main_loop  

task_assign:
IF GET_CHAR_DAMAGE_LAST_FRAME eventArgVar (pEntity j j z)
    IF pEntity > 0x0
    AND NOT pEntity = player_actor
        GET_ENTITY_TYPE pEntity (iEntityType)
        IF iEntityType = ENTITY_TYPE_PED
            GET_PED_REF pEntity (char)
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS char 0.0 0.0 0.0 (x y z)
            ADD_BLOOD x y z 0.0 0.0 0.0 150 char
            iHitCounter++
            timera = 0
        ENDIF
    ENDIF
ENDIF
RETURN_SCRIPT_EVENT
}
SCRIPT_END

//CALL SCM HELPERS
{
//CLEO_CALL GUI_display_text 0 posX posY id_text id_format_text
GUI_display_text:
    LVAR_FLOAT x y  //in
    LVAR_INT textId formatId   //in
    LVAR_TEXT_LABEL gxt
    STRING_FORMAT gxt "CMB_%i" textId
    CLEO_CALL GUI_text_format 0 formatId
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT (x y) $gxt
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_display_number 0 posX posY id_text id_format iNumber
GUI_display_number:
    LVAR_FLOAT x y  //in
    LVAR_INT textId formatId iNumber //in
    LVAR_TEXT_LABEL gxt
    STRING_FORMAT gxt "CMB_%i" textId
    CLEO_CALL GUI_text_format 0 formatId
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT_WITH_NUMBER (x y) $gxt iNumber
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_text_format 0 id_format 
GUI_text_format:
    LVAR_INT formatId   //in
    LVAR_FLOAT c d
    SWITCH formatId
        CASE 1
            GOSUB GUI_TextFormat_number
            BREAK
        CASE 2
            GOSUB GUI_TextFormat_text
            BREAK
        DEFAULT
            GOSUB GUI_TextFormat_text
            BREAK            
    ENDSWITCH
CLEO_RETURN 0

GUI_TextFormat_number:
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (c d)
    SET_TEXT_SCALE c d
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_CENTRE TRUE
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_text:
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (c d)
    SET_TEXT_SCALE c d
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_CENTRE TRUE
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
