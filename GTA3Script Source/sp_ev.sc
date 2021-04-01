// by J16D
// Fight - Dodge (near & far distance)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Size
CONST_INT BYTE                 1
CONST_INT WORD                 2
CONST_INT DWORD                4
//MP3 STATES    // 0:stop / 1:play / 2:pause / 3:resume
CONST_INT STOP      0
CONST_INT PLAY      1
CONST_INT PAUSE     2
CONST_INT RESUME    3

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_ev
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu
LVAR_INT i p iChar fx_system sfx sfx_B anim_seq
LVAR_FLOAT fCurrentTime x[2] y[2] z[2] zAngle fDistance
LVAR_INT iTempVar wi

GET_PLAYER_CHAR 0 player_actor
//SET_CLEO_SHARED_VAR varDodgeSensitive 1       // 0:Default, only by hit || 1:Availabe While Fighting

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
    
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_CHAR_REALLY_IN_AIR player_actor

                    IF GOSUB does_skill_Perfect_Dodge_enabled

                        IF GOSUB is_not_char_playing_dodge_anims
                        //IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_BOTH
                            i = 0
                            WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
                                IF DOES_CHAR_EXIST iChar
                                AND NOT IS_CHAR_DEAD iChar
                                AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                                    IF NOT IS_CHAR_IN_ANY_CAR iChar
                                    AND NOT IS_CHAR_ON_ANY_BIKE iChar
                                    AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                                        
                                        IF LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 20.0
                                            GET_CHAR_KILL_TARGET_CHAR iChar (p)
                                            IF IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor p
                                            OR IS_CHAR_USING_GUN iChar
                                                CREATE_FX_SYSTEM_ON_CHAR SP_SENSE player_actor (0.16 0.0 0.0) 4 (fx_system)
                                                ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 5  //5:head
                                                PLAY_AND_KILL_FX_SYSTEM fx_system
                                            ENDIF
                                        ENDIF

                                        IF NOT IS_CHAR_USING_GUN iChar 

                                            IF GOSUB does_skill_Dodge_Window_enabled
                                                IF IS_CHAR_FIGHTING iChar
                                                    CREATE_FX_SYSTEM_ON_CHAR SP_SENSEB player_actor (0.16 0.0 0.0) 4 (fx_system)
                                                    ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 5  //5:head
                                                    PLAY_AND_KILL_FX_SYSTEM fx_system
                                                    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~
                                                        IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                                            GOSUB assign_task_perfect_dodge
                                                        ENDIF
                                                    ENDIF
                                                ENDIF
                                            ELSE
                                                IF GOSUB is_char_playing_fight_anims
                                                    CREATE_FX_SYSTEM_ON_CHAR SP_SENSEB player_actor (0.16 0.0 0.0) 4 (fx_system)
                                                    ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 5  //5:head
                                                    PLAY_AND_KILL_FX_SYSTEM fx_system
                                                    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~
                                                        IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                                            GOSUB assign_task_perfect_dodge
                                                        ENDIF
                                                    ENDIF
                                                ENDIF
                                            ENDIF

                                        ELSE
                                            //IF CLEO_CALL is_char_firing_weapon 0 iChar
                                            IF IS_CHAR_SHOOTING iChar
                                                //CREATE_FX_SYSTEM_ON_CHAR SP_SENSE player_actor (0.16 0.0 0.0) 4 (fx_system)
                                                CREATE_FX_SYSTEM_ON_CHAR SP_SENSEB player_actor (0.16 0.0 0.0) 4 (fx_system)
                                                ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 5  //5:head
                                                PLAY_AND_KILL_FX_SYSTEM fx_system

                                                IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~

                                                    IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                                    AND NOT IS_BUTTON_PRESSED 0 CROSS   // ~k~~PED_SPRINT~

                                                        IF IS_CHAR_ON_SCREEN iChar
                                                        AND HAS_CHAR_SPOTTED_CHAR player_actor iChar
                                                            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[0] y[0] z[0])
                                                            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[1] y[1] z[1])
                                                            GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                                                            IF fDistance >= 5.0
                                                                GOSUB assign_task_perfect_dodge_b
                                                                BREAK
                                                            ELSE
                                                                GOSUB assign_task_perfect_dodge
                                                                BREAK
                                                            ENDIF
                                                        ENDIF

                                                    ENDIF
                                                ENDIF
                                            ENDIF
                                        
                                        ENDIF

                                    ENDIF
                                ENDIF
                            ENDWHILE
                        ENDIF
                
                    ENDIF
                
                ENDIF
            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            REMOVE_AUDIO_STREAM sfx_B
            REMOVE_ANIMATION "spider"
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

does_skill_Perfect_Dodge_enabled:
    GET_CLEO_SHARED_VAR varSkill2 (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

does_skill_Dodge_Window_enabled:
    GET_CLEO_SHARED_VAR varSkill2a (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_char_in_front_player:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.35 0.25 (x[1] y[1] z[1])
    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
    AND LOCATE_CHAR_ANY_MEANS_2D iChar x[1] y[1] 1.0 1.0 FALSE
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_char_behind_player:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 -1.35 0.25 (x[1] y[1] z[1])
    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
    AND LOCATE_CHAR_ANY_MEANS_2D iChar x[1] y[1] 1.0 1.0 FALSE
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_char_right_side_player:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 1.35 0.0 0.25 (x[1] y[1] z[1])
    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
    AND LOCATE_CHAR_ANY_MEANS_2D iChar x[1] y[1] 1.0 1.0 FALSE
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_char_left_side_player:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor -1.35 0.0 0.25 (x[1] y[1] z[1])
    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
    AND LOCATE_CHAR_ANY_MEANS_2D iChar x[1] y[1] 1.0 1.0 FALSE
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

set_z_angle_player:
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
        GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (zAngle)
        SET_CHAR_HEADING player_actor zAngle
    ENDIF
RETURN
//---------------------------------------------------------

//-+---------------------dodge-anim-------------------------
assign_task_perfect_dodge_b:
    IF GOSUB is_not_char_playing_dodge_anims
    //IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
        GOSUB set_z_angle_player
        GENERATE_RANDOM_INT_IN_RANGE 0 5 (iTempVar)
        SWITCH iTempVar
            CASE 0
                GOSUB assign_task_dodge_front_b
                BREAK
            CASE 1
                GOSUB assign_task_dodge_right_b
                BREAK
            CASE 2
                GOSUB assign_task_dodge_left_b
                BREAK
            CASE 3
                GOSUB assign_task_dodge_back_b
                BREAK
            CASE 4
                GOSUB assign_task_dodge_back_b_b
                BREAK
        ENDSWITCH
        WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
            WAIT 0
        ENDWHILE
        SET_TIME_SCALE 1.0
        SET_CHAR_HEADING player_actor zAngle
    ENDIF
RETURN

assign_task_dodge_front_b:
    GOSUB REQUEST_Animations
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_back_c" "spider") 51.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_back_c" 1.5 //1.35
    SET_TIME_SCALE 0.75
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_back_c"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_back_c" (fCurrentTime)
        IF fCurrentTime > 0.320 //frame 16/50
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.480 //frame 24/50
            SET_TIME_SCALE 1.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_dodge_right_b:
    GOSUB REQUEST_Animations
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_right_c" "spider") 51.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_right_c" 1.5 //1.25
    SET_TIME_SCALE 0.75
    iTempVar = 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_right_c"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_right_c" (fCurrentTime)
        IF fCurrentTime > 0.200 //frame 10/50
            GOSUB shoot_web_thread
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.360 //frame 18/50
            SET_TIME_SCALE 1.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_dodge_left_b:
    GOSUB REQUEST_Animations
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_left_c" "spider") 51.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_left_c" 1.5 //1.25
    SET_TIME_SCALE 0.75
    iTempVar = 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_left_c"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_left_c" (fCurrentTime)
        IF fCurrentTime > 0.200 //frame 10/50
            GOSUB shoot_web_thread
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.360 //frame 18/50
            SET_TIME_SCALE 1.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_dodge_back_b:
    GOSUB REQUEST_Animations
    //GOSUB set_z_angle_player
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_back_d" "spider") 53.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_back_d" 2.0 //1.5 //1.45
    SET_TIME_SCALE 0.75
    iTempVar = 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_back_d"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_back_d" (fCurrentTime)
        IF fCurrentTime > 0.192 //frame 10/52
            GOSUB shoot_web_thread
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.462 //frame 24/52
            SET_TIME_SCALE 1.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_dodge_back_b_b:
    GOSUB REQUEST_Animations
    //GOSUB set_z_angle_player
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_back_e" "spider") 45.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_back_e" 1.5 //1.25
    SET_TIME_SCALE 0.75
    iTempVar = 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_back_e"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_back_e" (fCurrentTime)
        IF fCurrentTime >= 0.364 //frame 16/44
            GOSUB shoot_web_thread
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.455 //frame 20/44
            SET_TIME_SCALE 1.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

shoot_web_thread:
    IF iTempVar = 0
        //FX
        IF IS_CHAR_PLAYING_ANIM player_actor "dodge_right_c"
        OR IS_CHAR_PLAYING_ANIM player_actor "dodge_back_e"
            CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (105.0 0.0 0.0) 4 (fx_system)
            ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 25 //Right Hand
            PLAY_AND_KILL_FX_SYSTEM fx_system
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor "dodge_left_c"
                CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (105.0 0.0 0.0) 4 (fx_system)
                ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 35 //Left Hand
                PLAY_AND_KILL_FX_SYSTEM fx_system
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor "dodge_back_d"
                    CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (105.0 0.0 0.0) 4 (fx_system)
                    ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 25 //Right Hand
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                    CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (105.0 0.0 0.0) 4 (fx_system)
                    ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_system player_actor 35 //Left Hand
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                ENDIF
            ENDIF
        ENDIF
        GOSUB playWebShootSfx
        //Web shoot
        IF DOES_CHAR_EXIST iChar
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wa.cs"
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\wa.cs" iChar
            ENDIF
        ELSE
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wa.cs"
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\wa.cs" -1
            ENDIF
        ENDIF
        iTempVar = 1
    ENDIF
RETURN

playWebShootSfx:
    REMOVE_AUDIO_STREAM sfx_B
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot1.mp3" (sfx_B)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx_B player_actor
        SET_AUDIO_STREAM_STATE sfx_B PLAY
    ENDIF
RETURN
//---------------------------------------------------------

//---------------------------------------------------------
assign_task_perfect_dodge:
    //IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
    IF GOSUB is_not_char_playing_dodge_anims
        IF GOSUB is_char_in_front_player
            GOSUB assign_task_dodge_front
        ELSE
            GENERATE_RANDOM_INT_IN_RANGE 0 4 (iTempVar)
            IF iTempVar > 0
                IF GOSUB is_char_behind_player
                    GOSUB assign_task_dodge_behind
                ELSE
                    IF GOSUB is_char_right_side_player
                        GOSUB assign_task_dodge_right
                    ELSE
                        IF GOSUB is_char_left_side_player
                            GOSUB assign_task_dodge_left
                        ENDIF
                    ENDIF
                ENDIF
            ELSE
                GOSUB set_z_angle_player
                GOSUB assign_task_dodge_back_b
            ENDIF
        ENDIF
        WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
            WAIT 0
        ENDWHILE
        SET_TIME_SCALE 1.0
    ENDIF
RETURN

assign_task_dodge_front:
    GOSUB REQUEST_Animations
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    SET_CHAR_COLLISION iChar FALSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.40 0.0 (x[0] y[0] z[0])
    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]

    //CLEO_CALL setCharViewPointToCamera 0 player_actor
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_front_b" "spider") 34.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_front_b" 1.35
    SET_TIME_SCALE 0.75
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_front_b"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_front_b" (fCurrentTime)
        IF fCurrentTime >= 0.182 //frame 6/33
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime >= 0.364 //frame 12/33
            SET_TIME_SCALE 1.0
            SET_CHAR_COLLISION iChar TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION iChar TRUE

    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_front_b"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_front_b" (fCurrentTime)
        IF IS_BUTTON_PRESSED PAD1 CIRCLE        // ~k~~PED_FIREWEAPON~
            iTempVar = 1
            IF fCurrentTime >= 0.848  //frame 28/33
                BREAK
            ENDIF
        ELSE
            iTempVar = 0
            IF fCurrentTime >= 0.970  //frame 32/33
                BREAK
            ENDIF
        ENDIF
        WAIT 0
    ENDWHILE

    IF iTempVar = 1
        IF DOES_CHAR_EXIST iChar
            CLEAR_CHAR_TASKS iChar
            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dodge_front_b_hit" "spider") 48.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup_front" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
        ENDIF
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_front_b_ha" "spider") 40.0 (0 1 1 0) -1
        WAIT 0
        CLEAR_SEQUENCE_TASK anim_seq
        IF IS_CHAR_PLAYING_ANIM player_actor "dodge_front_b_ha"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_front_b_ha"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_front_b_ha" (fCurrentTime)
                IF fCurrentTime >= 0.154  //frame 6/39
                AND 0.179 >= fCurrentTime    //frame 7/39
                    IF iTempVar = 1
                        GOSUB play_sfx_hit
                        iTempVar = 2
                        DAMAGE_CHAR iChar 5 TRUE
                        CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.2) 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                    ENDIF
                ENDIF
                IF fCurrentTime >= 0.410    //frame 16/39
                AND 0.436 >= fCurrentTime   //frame 17/39
                    IF iTempVar = 2
                        GOSUB play_sfx_hit
                        iTempVar = 1
                        DAMAGE_CHAR iChar 5 TRUE
                        CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.2) 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                    ENDIF
                ENDIF
                IF fCurrentTime >= 0.769  //frame 30/39
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
        ELSE
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS iChar
            IF IS_CHAR_SCRIPT_CONTROLLED iChar
                MARK_CHAR_AS_NO_LONGER_NEEDED iChar
            ENDIF
        ENDIF

    ENDIF
    /*
    WAIT 0
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        GET_PED_POINTER iChar (iTempVar)
        iTempVar += 0x484 // m_nCreatedBy
        READ_MEMORY iTempVar 1 FALSE (iTempVar)
        IF iTempVar = 2    //1 - game, 2 - script, 3 - ?
            TASK_KILL_CHAR_ON_FOOT iChar player_actor
            PRINT_FORMATTED_NOW "KILL" 1000
        ENDIF
    ENDIF
    */

RETURN

assign_task_dodge_behind:
    GOSUB REQUEST_Animations
    SET_CHAR_COLLISION iChar FALSE
    //CLEO_CALL setCharViewPointToCamera 0 player_actor
    //GOSUB set_z_angle_player
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_back_b" "spider") 51.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_back_b" 1.5  //1.35
    SET_TIME_SCALE 0.75
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_back_b"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_back_b" (fCurrentTime)
        IF fCurrentTime > 0.320 //frame 16/50
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.480 //frame 24/50
            SET_TIME_SCALE 1.0
            SET_CHAR_COLLISION iChar TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION iChar TRUE
RETURN

assign_task_dodge_right:
    GOSUB REQUEST_Animations
    SET_CHAR_COLLISION iChar FALSE
    //CLEO_CALL setCharViewPointToCamera 0 player_actor
    //GOSUB set_z_angle_player
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_right_b" "spider") 46.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_right_b" 1.5 //1.25
    SET_TIME_SCALE 0.75
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_right_b"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_right_b" (fCurrentTime)
        IF fCurrentTime > 0.267 //frame 12/45
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.511 //frame 23/45
            SET_TIME_SCALE 1.0
            SET_CHAR_COLLISION iChar TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION iChar TRUE
RETURN

assign_task_dodge_left:
    GOSUB REQUEST_Animations
    SET_CHAR_COLLISION iChar FALSE
    //CLEO_CALL setCharViewPointToCamera 0 player_actor
    //GOSUB set_z_angle_player
    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_left_b" "spider") 46.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_left_b" 1.5 //1.25
    SET_TIME_SCALE 0.75
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_left_b"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_left_b" (fCurrentTime)
        IF fCurrentTime > 0.267 //frame 12/45
            SET_TIME_SCALE 0.35
        ENDIF
        IF fCurrentTime > 0.511 //frame 23/45
            SET_TIME_SCALE 1.0
            SET_CHAR_COLLISION iChar TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION iChar TRUE
RETURN
//---------------------------------------------------------

//---------------------------------------------------------
is_char_playing_fight_anims:
    IF IS_CHAR_PLAYING_ANIM iChar "FightA_1"
    OR IS_CHAR_PLAYING_ANIM iChar "FightA_2"
    OR IS_CHAR_PLAYING_ANIM iChar "FightA_3"
    OR IS_CHAR_PLAYING_ANIM iChar "FightA_G"
        RETURN_TRUE
    ELSE
        IF IS_CHAR_PLAYING_ANIM iChar "FightB_1"
        OR IS_CHAR_PLAYING_ANIM iChar "FightB_2"
        OR IS_CHAR_PLAYING_ANIM iChar "FightB_3"
        OR IS_CHAR_PLAYING_ANIM iChar "FightB_G"
            RETURN_TRUE
        ELSE
            IF IS_CHAR_PLAYING_ANIM iChar "FightD_1"
            OR IS_CHAR_PLAYING_ANIM iChar "FightD_2"
            OR IS_CHAR_PLAYING_ANIM iChar "FightD_3"
            OR IS_CHAR_PLAYING_ANIM iChar "FightD_G"
                RETURN_TRUE
            ELSE
                IF IS_CHAR_PLAYING_ANIM iChar "DILDO_1"
                OR IS_CHAR_PLAYING_ANIM iChar "DILDO_2"
                OR IS_CHAR_PLAYING_ANIM iChar "DILDO_3"
                OR IS_CHAR_PLAYING_ANIM iChar "DILDO_G"
                    RETURN_TRUE
                ELSE
                    IF IS_CHAR_PLAYING_ANIM iChar "knife_1"
                    OR IS_CHAR_PLAYING_ANIM iChar "knife_2"
                    OR IS_CHAR_PLAYING_ANIM iChar "knife_3"
                    OR IS_CHAR_PLAYING_ANIM iChar "knife_4"
                    OR IS_CHAR_PLAYING_ANIM iChar "knife_G"
                        RETURN_TRUE
                    ELSE
                        IF IS_CHAR_PLAYING_ANIM iChar "sword_1"
                        OR IS_CHAR_PLAYING_ANIM iChar "sword_2"
                        OR IS_CHAR_PLAYING_ANIM iChar "sword_3"
                        OR IS_CHAR_PLAYING_ANIM iChar "sword_4"
                            RETURN_TRUE
                        ELSE
                            IF IS_CHAR_PLAYING_ANIM iChar "Bat_1"
                            OR IS_CHAR_PLAYING_ANIM iChar "Bat_2"
                            OR IS_CHAR_PLAYING_ANIM iChar "Bat_3"
                            OR IS_CHAR_PLAYING_ANIM iChar "Bat_4"
                            OR IS_CHAR_PLAYING_ANIM iChar "Flower_attack"
                                RETURN_TRUE
                            ELSE
                                RETURN_FALSE
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
RETURN

is_not_char_playing_dodge_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_front")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_back")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_right")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_left")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_c")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_right_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_left_b")
            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_b")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_b_ha")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_right_c")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_left_c")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_d")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_e")
                IF NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_c")
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_c_ha")
                    RETURN_TRUE
                    RETURN
                ENDIF
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN
//---------------------------------------------------------

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

play_sfx:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_a.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx PLAY
    ENDIF
    WAIT 0
RETURN

play_sfx_hit:
    REMOVE_AUDIO_STREAM sfx
    //GENERATE_RANDOM_INT_IN_RANGE 0 3 (iTempVar)
    SWITCH iTempVar
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_c.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_d.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_e.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
    ENDSWITCH
    WAIT 0
RETURN
}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL setCharViewPointToCamera 0 player_actor
setCharViewPointToCamera:
    LVAR_INT tempPlayer
    LVAR_FLOAT xPoint yPoint zPoint xPos yPos zPos newAngle
    GET_ACTIVE_CAMERA_POINT_AT xPoint yPoint zPoint
    GET_ACTIVE_CAMERA_COORDINATES xPos yPos zPos
    xPoint = xPoint - xPos
    yPoint = yPoint - yPos
    GET_HEADING_FROM_VECTOR_2D xPoint yPoint (newAngle)
    SET_CHAR_HEADING tempPlayer newAngle
CLEO_RETURN 0
}
{
//CLEO_CALL is_char_firing_weapon 0 iChar
//https://forum.mixmods.com.br/f16-utilidades/t919-flags-de-pessoas-e-veiculos
is_char_firing_weapon:
    LVAR_INT scplayer   //in
    LVAR_INT pChar flags
    GET_PED_POINTER scplayer pChar
    flags = pChar + 0x46C   //pedFlags        
    READ_MEMORY flags 4 FALSE (flags)
    IF IS_LOCAL_VAR_BIT_SET_CONST flags 16 //bFiringWeapon 
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
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
show_debug:
    LVAR_INT pStruct pStyle[2]
    GET_PED_POINTER player_actor (pStruct)
    pStruct += 0x72D //[byte] Fighting style # 1
    READ_MEMORY pStruct BYTE FALSE (pStyle[0])
    GET_PED_POINTER player_actor (pStruct)
    pStruct += 0x72E //[byte] Fighting style # 2
    READ_MEMORY pStruct BYTE FALSE (pStyle[1])
    PRINT_FORMATTED_NOW "style1:%d  style2:%d" 2000 pStyle[0] pStyle[1]
RETURN
*/

/*
set_player_fight_group:
    IF IS_CHAR_PLAYING_ANIM player_actor "FightC_3"
        GIVE_MELEE_ATTACK_TO_CHAR player_actor 15 6     //FIGHT_E
        //GOSUB show_debug
        WHILE IS_CHAR_PLAYING_ANIM player_actor "FightC_3"
            WAIT 0
        ENDWHILE
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor "FightKick_B"
        GIVE_MELEE_ATTACK_TO_CHAR player_actor 6 6     //FIGHT_C
        //GOSUB show_debug
        WHILE IS_CHAR_PLAYING_ANIM player_actor "FightKick_B"
            WAIT 0
        ENDWHILE
    ENDIF
RETURN
*/



/*

            IF IS_CHAR_PLAYING_ANIM player_actor "FightC_3"

                WHILE IS_CHAR_PLAYING_ANIM player_actor "FightC_3"
                    GET_CHAR_ANIM_CURRENT_TIME player_actor "FightC_3" (fCurrentTime)
                    IF fCurrentTime > 0.469 // frame 15/32
                        IF IS_BUTTON_PRESSED PAD1 TRIANGLE    // ~k~~VEHICLE_ENTER_EXIT~
                            GOSUB assign_task_final_combo
                            counter = 0
                            IF GOSUB is_char_in_front_of_player
                                IF DOES_CHAR_EXIST iChar
                                    IF LOCATE_CHAR_ANY_MEANS_CHAR_2D player_actor iChar 1.0 2.0 TRUE
                                        GOSUB assign_task_fall_floor
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                    WAIT 0
                ENDWHILE
            ENDIF



assign_task_final_combo:
    GOSUB REQUEST_FightAnimations
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (iTempVar)
    SWITCH iTempVar
        CASE 0
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("FightC_4" "fight_c") 38.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("FightC_4" "fight_c") 38.0 (0 1 1 0) -1
            BREAK
        CASE 1
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("FightC_5" "fight_c") 42.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("FightC_5" "fight_c") 42.0 (0 1 1 0) -1
            BREAK
        CASE 2
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("FightC_6" "fight_c") 34.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("FightC_6" "fight_c") 34.0 (0 1 1 0) -1
            BREAK
    ENDSWITCH
RETURN


assign_task_fall_floor:
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    SWITCH iTempVar
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("HitC_4" "fight_c") 38.0 (0 1 1 0) -1
            BREAK
        CASE 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("HitC_5" "fight_c") 42.0 (0 1 1 0) -1
            BREAK
        CASE 2
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("HitC_6" "fight_c") 34.0 (0 1 1 0) -1
            BREAK
    ENDSWITCH
    WAIT 50
RETURN

*/