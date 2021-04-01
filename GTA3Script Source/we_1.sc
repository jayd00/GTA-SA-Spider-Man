// by J16D
// Sub Script of sp_po.sc (electric Punch)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME we_1
LVAR_INT pChar  //passed 
WAIT 0
LVAR_INT player_actor p i iChar iObj fx_web anim_seq sfx iTempVar randomVal
LVAR_FLOAT x[3] y[3] z[3] fDistance zAngle xAngle fCurrentDistance fCurrentTime

GET_PLAYER_CHAR 0 player_actor
iChar = pChar
DAMAGE_CHAR iChar 5 TRUE

IF DOES_CHAR_EXIST iChar
AND NOT IS_CHAR_DEAD iChar
    GOSUB task_play_action
ELSE
    WAIT 50
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF

SET_AUDIO_STREAM_STATE sfx 0
IF DOES_CHAR_EXIST iChar
    IF CLEO_CALL is_char_gang_ped 0 iChar
        MARK_CHAR_AS_NO_LONGER_NEEDED iChar
    ENDIF
ENDIF
REMOVE_AUDIO_STREAM sfx
REMOVE_ANIMATION "spider"
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

task_play_action:
    GOSUB set_char_zangle
    GOSUB assign_new_task_anim
    timera = 0
    CLEO_CALL create_electric_fx_on_char 0 iChar
    GOSUB playElectricWebShootSfx
    IF NOT IS_CHAR_DEAD iChar
        DAMAGE_CHAR iChar 15 TRUE
    ENDIF

    WHILE TRUE
        IF timera > 50
            CLEO_CALL create_electric_fx_on_char 0 iChar
            timera = 0
        ENDIF
        IF DOES_CHAR_EXIST iChar
            IF IS_CHAR_PLAYING_ANIM iChar ("sp_we_a")
                GET_CHAR_ANIM_CURRENT_TIME iChar ("sp_we_a") (fCurrentTime)
                IF fCurrentTime > 0.972     //frame 175
                    BREAK
                ENDIF
                GOSUB assign_task_near_chars
            ELSE
                BREAK
            ENDIF
            IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 35.0
                BREAK
            ENDIF
            IF IS_CHAR_DEAD iChar
                BREAK
            ENDIF
        ELSE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE

    timera = 0
    WHILE 2500 > timera
        GOSUB show_fx_for_last_chars
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_STATE sfx 0
    WAIT 0
RETURN

assign_task_near_chars:
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i pChar)
        IF DOES_CHAR_EXIST pChar
        AND NOT IS_CHAR_DEAD pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR iChar pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor pChar
            IF NOT IS_CHAR_IN_ANY_CAR pChar
            AND NOT IS_CHAR_ON_ANY_BIKE pChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE pChar
                IF LOCATE_CHAR_DISTANCE_TO_CHAR iChar pChar 3.0

                    IF CLEO_CALL is_not_playing_powers_anims 0 pChar
                        GOSUB assign_new_task_anim_b
                        IF NOT IS_CHAR_DEAD pChar
                            DAMAGE_CHAR pChar 15 TRUE
                        ENDIF
                    ENDIF

                    CLEO_CALL create_electric_fx_on_char 0 pChar
                    GOSUB show_fx_c
                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
RETURN

show_fx_for_last_chars:
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i pChar)
        IF DOES_CHAR_EXIST pChar
        AND NOT IS_CHAR_DEAD pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR iChar pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor pChar
            IF NOT IS_CHAR_IN_ANY_CAR pChar
            AND NOT IS_CHAR_ON_ANY_BIKE pChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE pChar
                IF IS_CHAR_PLAYING_ANIM pChar ("sp_we_a")
                    CLEO_CALL create_electric_fx_on_char 0 pChar
                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
RETURN

assign_new_task_anim:
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    WAIT 0
    GOSUB REQUEST_Animations
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    OPEN_SEQUENCE_TASK anim_seq
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("sp_we_a" "spider") 181.0 (0 1 1 0) -1
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("sp_we_b" "spider") 56.0 (0 1 1 0) -1
    CLOSE_SEQUENCE_TASK anim_seq
    PERFORM_SEQUENCE_TASK iChar anim_seq
    WAIT 0
    CLEAR_SEQUENCE_TASK anim_seq
    WAIT 0
    SET_CHAR_HEADING iChar zAngle
RETURN

assign_new_task_anim_b:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING pChar zAngle
    IF NOT IS_CHAR_SCRIPT_CONTROLLED pChar
        IF CLEO_CALL is_char_gang_ped 0 pChar
            MARK_CHAR_AS_NEEDED pChar
        ENDIF
    ENDIF
    WAIT 0
    GOSUB REQUEST_Animations
    CLEAR_CHAR_TASKS pChar
    CLEAR_CHAR_TASKS_IMMEDIATELY pChar
    OPEN_SEQUENCE_TASK anim_seq
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("sp_we_a" "spider") 181.0 (0 1 1 0) -1
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("sp_we_b" "spider") 56.0 (0 1 1 0) -1
    CLOSE_SEQUENCE_TASK anim_seq
    PERFORM_SEQUENCE_TASK pChar anim_seq
    WAIT 0
    CLEAR_SEQUENCE_TASK anim_seq
    WAIT 0
    SET_CHAR_HEADING pChar zAngle
RETURN

set_char_zangle:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING player_actor zAngle
    zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle
RETURN

show_fx_c:
    IF DOES_CHAR_EXIST pChar
        CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_ELECTRIC_C pChar (0.0 1.3 0.0) (0.0 90.0 0.0) 4 (fx_web)
        PLAY_AND_KILL_FX_SYSTEM fx_web
    ENDIF
RETURN

playElectricWebShootSfx:
    IF DOES_CHAR_EXIST iChar
        REMOVE_AUDIO_STREAM sfx
        IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot5a.mp3" (sfx)
            SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx iChar
            SET_AUDIO_STREAM_STATE sfx 1
            SET_AUDIO_STREAM_LOOPED sfx TRUE
        ENDIF
        WAIT 0
    ENDIF
RETURN

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations
}
SCRIPT_END

//-+----- CALL SCM HELPERS
{
//CLEO_CALL create_electric_fx_on_char 0 iChar
create_electric_fx_on_char:
    LVAR_INT sChar   //in
    LVAR_INT randomVal iBone electric_fx sphere_fx
    IF DOES_CHAR_EXIST sChar
        GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
        SWITCH randomVal
            CASE 0
                iBone = 5    //5: BONE_NECK
            BREAK
            CASE 1
                iBone = 3    //3: BONE_SPINE1
            BREAK
            CASE 2
                iBone = 1    //1: BONE_PELVIS1
            BREAK
        ENDSWITCH
        CREATE_FX_SYSTEM_ON_CHAR SP_ELECTRIC_B sChar (0.0 0.0 0.0) 4 (electric_fx)
        ATTACH_FX_SYSTEM_TO_CHAR_BONE electric_fx sChar iBone
        PLAY_AND_KILL_FX_SYSTEM electric_fx

        CREATE_FX_SYSTEM_ON_CHAR SP_SPHERE sChar (0.0 0.01 0.15) 4 (sphere_fx)
        PLAY_AND_KILL_FX_SYSTEM sphere_fx
        WAIT 0
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL is_not_playing_powers_anims 0 iChar
is_not_playing_powers_anims:
    LVAR_INT char   //in
    IF DOES_CHAR_EXIST char
        IF NOT IS_CHAR_PLAYING_ANIM char ("sp_we_a")  //Electric Web
        AND NOT IS_CHAR_PLAYING_ANIM char ("sp_we_b")  //Electric Web
        AND NOT IS_CHAR_PLAYING_ANIM char ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
        AND NOT IS_CHAR_PLAYING_ANIM char ("ko_ground") //Web Shoot, Web Bomb
        AND NOT IS_CHAR_PLAYING_ANIM char ("sp_wf_a")  //Suspension Matrix
        AND NOT IS_CHAR_PLAYING_ANIM char ("sp_wh_a")  //Trip Mine
        AND NOT IS_CHAR_PLAYING_ANIM char ("sp_wh_b")  //Trip Mine
            RETURN_TRUE
            CLEO_RETURN 0
        ENDIF        
    ENDIF
    RETURN_FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL is_char_gang_ped 0 iChar
is_char_gang_ped:
    LVAR_INT iChar  //in
    LVAR_INT iPedType
    IF DOES_CHAR_EXIST iChar
        GET_PED_TYPE iChar (iPedType)
        IF iPedType = PEDTYPE_GANG1  //Ballas 1
        OR iPedType = PEDTYPE_GANG2  //CJ Gang
        OR iPedType = PEDTYPE_GANG3  //Los Santos Vagos
        OR iPedType = PEDTYPE_GANG4  // San Fierro Rifa
            RETURN_TRUE
        ELSE
            IF iPedType = PEDTYPE_GANG5  // Da Nang Boys
            OR iPedType = PEDTYPE_GANG6  //Mafia
            OR iPedType = PEDTYPE_GANG7  //Mountain Cloud Triad
            OR iPedType = PEDTYPE_GANG8  //Varrio Los Aztecas
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ENDIF
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
