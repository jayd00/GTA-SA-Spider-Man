// by J16D
// Trip Mine
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT max_time 8000 //ms
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wh
LVAR_FLOAT v1 v2 v3 //passed
WAIT 0
LVAR_INT player_actor p i iChar pChar iObj sfx fx_system iTempVar fx_direction
LVAR_FLOAT x[4] y[4] z[4] fDistance zAngle xAngle v4 fRandomVal

GET_PLAYER_CHAR 0 player_actor
REQUEST_MODEL 1598  //beachball
LOAD_ALL_MODELS_NOW
//CREATE_OBJECT 1598 0.0 0.0 0.0 (iObj)
CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (iObj)
SET_OBJECT_PROOFS iObj 1 1 1 1 1 //BP FP EP CP MP
SET_OBJECT_MASS iObj 0.001
SET_OBJECT_COLLISION iObj FALSE
SET_OBJECT_RECORDS_COLLISIONS iObj FALSE
SET_OBJECT_SCALE iObj 0.05
MARK_MODEL_AS_NO_LONGER_NEEDED 1598

//set angle - direction
IF NOT v1 = 0.0
AND NOT v2 = 0.0
AND NOT v3 = 0.0
    x[0] = v1
    y[0] = v2
    z[0] = v3
    iTempVar = 1
ELSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 1.0 0.0) (x[0] y[0] z[0])
    iTempVar = 0
ENDIF
GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
xAngle *= -1.0
GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
SET_CHAR_HEADING player_actor zAngle
WAIT 0

IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootR")  //Right Hand
OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p")     //Right Hand
    CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //BONE_RIGHTHAND
    SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
ELSE
    IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootL")  //Left Hand
    OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p_L")   //Left Hand
        CLEO_CALL getActorBonePos 0 player_actor 35 (x[2] y[2] z[2])    //BONE_LEFTHAND
        SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
    ELSE
        ATTACH_OBJECT_TO_CHAR iObj player_actor (0.0 0.5 0.25) (0.0 0.0 0.0)
        DETACH_OBJECT iObj (0.0 0.0 0.0) FALSE
    ENDIF
ENDIF

SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
SET_OBJECT_COLLISION iObj TRUE
SET_OBJECT_RECORDS_COLLISIONS iObj TRUE
IF iTempVar = 0
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 4.0 0.0) (x[0] y[0] z[0])
    CLEO_CALL setObjectVelocityTo 0 iObj (x[0] y[0] z[0]) 30.0
    WAIT 25
ENDIF

IF NOT HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
    WHILE NOT HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
        IF NOT iTempVar = 0 
            CLEO_CALL setObjectVelocityTo 0 iObj (x[0] y[0] z[0]) 50.0
            IF HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
                BREAK
            ENDIF
        ENDIF
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    IF GOSUB is_not_char_playing_anims
                        IF LOCATE_CHAR_DISTANCE_TO_OBJECT iChar iObj 1.25
                            GOSUB task_play_action
                            GOTO end_scr_b
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
            IF HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
                BREAK
            ENDIF
        ENDWHILE
        WAIT 0
    ENDWHILE
ENDIF
SET_OBJECT_COLLISION iObj FALSE
SET_OBJECT_VISIBLE iObj FALSE
SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle

IF CLEO_CALL has_object_collided_in_offset_or_sides 0 iObj 1.0
    CLEO_CALL setZangleObjectWall 0 iObj (0.5 3.0 0.0) (-0.5 3.0 0.0) 90.0
    fx_direction = 2    //horizontal
ELSE
    SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
    fx_direction = 1    //vertical
ENDIF

timera = 0
WHILE max_time > timera
    IF DOES_OBJECT_EXIST iObj
        fDistance = 0.0
        IF fx_direction = 1     //vertical
            CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION SP_LASER iObj (0.0 0.0 0.0) (0.0 0.0 90.0) 4 (fx_system)
            PLAY_AND_KILL_FX_SYSTEM fx_system
            WHILE 8.0 > fDistance
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 fDistance) (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (0 0 1 0 0)     //buildings|cars|characters|objects|particles
                    //PRINT_FORMATTED_NOW "Found Char x:%.1f y:%.1f z:%.1f" 1000 x[1] y[1] z[1]     //debug
                    IF GOSUB found_char_in_coords
                        GENERATE_RANDOM_INT_IN_RANGE 0 2 (iTempVar)
                        GOSUB play_Sfx
                        GOSUB assign_task_fall_floor
                        GOTO end_scr
                    ENDIF
                    BREAK
                ENDIF
                fDistance +=@ 0.1
            ENDWHILE
        ELSE        //horizontal
            CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION SP_LASER iObj (0.0 0.0 0.0) (0.0 90.0 0.0) 4 (fx_system)
            PLAY_AND_KILL_FX_SYSTEM fx_system
            WHILE 8.0 > fDistance
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 fDistance 0.0) (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (0 0 1 0 0)     //buildings|cars|characters|objects|particles
                    //PRINT_FORMATTED_NOW "Found Char x:%.1f y:%.1f z:%.1f" 1000 x[1] y[1] z[1]   //debug
                    IF GOSUB found_char_in_coords
                        GENERATE_RANDOM_INT_IN_RANGE 0 2 (iTempVar)
                        GOSUB play_Sfx
                        GOSUB assign_task_attach_char_to_wall
                        GOTO end_scr
                    ENDIF
                    BREAK
                ENDIF
                fDistance +=@ 0.1
            ENDWHILE
        ENDIF
        IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 35.0
            GOTO end_scr_b
        ENDIF
    ELSE
        GOTO end_scr_b
    ENDIF
    WAIT 0
ENDWHILE

end_scr:
IF DOES_CHAR_EXIST pChar
    GOSUB create_object_web_b
    GET_CHAR_HEADING pChar (zAngle)
    IF IS_CHAR_PLAYING_ANIM pChar "ko_wall"
        zAngle += 180.0
    ENDIF
    timera = 0
    WHILE 4000 > timera
        IF DOES_OBJECT_EXIST iObj
            CLEO_CALL attachObjectToActorOnBone 0 pChar iObj (0.1 0.0 0.0) 1  //root_bone
            IF IS_CHAR_PLAYING_ANIM pChar "ko_wall"
                SET_OBJECT_ROTATION iObj 0.0 0.0 zAngle
            ELSE
                IF IS_CHAR_PLAYING_ANIM pChar "sp_wh_b"
                    SET_OBJECT_ROTATION iObj 90.0 0.0 zAngle
                ENDIF
            ENDIF
        ENDIF
        IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 35.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    IF NOT IS_CHAR_DEAD pChar
        IF IS_CHAR_PLAYING_ANIM pChar "sp_wh_b"
        OR IS_CHAR_PLAYING_ANIM pChar "ko_wall"
            CLEAR_CHAR_TASKS_IMMEDIATELY pChar
            DAMAGE_CHAR pChar 100 TRUE
        ELSE
            SET_CHAR_COLLISION pChar TRUE
            CLEAR_CHAR_TASKS_IMMEDIATELY pChar
        ENDIF
    ENDIF
ENDIF

end_scr_b:
IF DOES_OBJECT_EXIST iObj
    DELETE_OBJECT iObj
ENDIF
REMOVE_AUDIO_STREAM sfx
REMOVE_ANIMATION "spider"
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

assign_task_fall_floor:
    IF DOES_CHAR_EXIST pChar
        IF NOT IS_CHAR_SCRIPT_CONTROLLED pChar
            IF CLEO_CALL is_char_gang_ped 0 pChar
                MARK_CHAR_AS_NEEDED pChar
            ENDIF
        ENDIF
        GOSUB REQUEST_Animations
        CLEAR_CHAR_TASKS pChar
        CLEAR_CHAR_TASKS_IMMEDIATELY pChar
        TASK_DIE_NAMED_ANIM pChar "sp_wh_b" "spider" 35.0 4000
        //TASK_PLAY_ANIM_NON_INTERRUPTABLE pChar ("sp_wh_b" "spider") 35.0 (0 1 1 1) -1
        WAIT 0
    ENDIF
RETURN

assign_task_attach_char_to_wall:
    IF DOES_CHAR_EXIST pChar
        IF NOT IS_CHAR_SCRIPT_CONTROLLED pChar
            IF CLEO_CALL is_char_gang_ped 0 pChar
                MARK_CHAR_AS_NEEDED pChar
            ENDIF
        ENDIF
        GOSUB REQUEST_Animations
        GOSUB set_char_zangle
        CLEAR_CHAR_TASKS pChar
        CLEAR_CHAR_TASKS_IMMEDIATELY pChar
        TASK_PLAY_ANIM_NON_INTERRUPTABLE pChar ("sp_wh_a" "spider") 5.0 (1 1 1 0) -2
        WAIT 0
        GOSUB throw_char_velocity
        timera = 0
        WHILE 1500 > timera
            GOSUB drawOnScreenLine
            // simulates is in air  -avoid stuck in ground
            GET_PED_POINTER pChar (iTempVar)
            WRITE_STRUCT_OFFSET iTempVar 0x46C 1 0    // ONFOOT_STATE = AIR
            CLEO_CALL setCharVelocityTo 0 pChar (x[0] y[0] z[0]) 25.0

            IF CLEO_CALL has_char_collision_in_offset_or_sides 0 pChar 0.75 //0.65
                IF NOT IS_CHAR_PLAYING_ANIM pChar "ko_wall"
                    SET_CHAR_COLLISION pChar FALSE
                    CLEO_CALL setZangleCharWall 0 pChar (0.2 -10.0 0.0) (-0.2 -10.0 0.0) 90.0 //-90.0
                    //GENERATE_RANDOM_FLOAT_IN_RANGE 0.0 0.10 (fRandomVal)
                    //CLEO_CALL setCoordsInter 0 pChar (0.0 3.0 0.0) 0.02
                    CLEAR_CHAR_TASKS pChar
                    CLEAR_CHAR_TASKS_IMMEDIATELY pChar
                    TASK_DIE_NAMED_ANIM pChar "ko_wall" "spider" 125.0 5000
                    //TASK_PLAY_ANIM_NON_INTERRUPTABLE pChar ("ko_wall" "spider") 26.0 (0 1 1 1) -1
                    WAIT 0
                    BREAK
                ENDIF
            ENDIF
            IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor pChar 35.0
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
RETURN

set_char_zangle:
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar (0.0 0.0 0.25) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING pChar zAngle
RETURN

throw_char_velocity:
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 -2.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar 0.0 0.0 0.5 (x[1] y[1] z[1])
    SET_CHAR_COORDINATES_SIMPLE pChar x[1] y[1] z[1]
    CLEO_CALL setCharVelocityTo 0 pChar (x[0] y[0] z[0]) 25.0
RETURN

task_play_action:
    IF DOES_OBJECT_EXIST iObj
        GOSUB attach_trip_mine_to_char
        timera = 0
        WHILE max_time > timera
            CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION SP_LASER iObj (0.0 0.0 0.0) (0.0 90.0 0.0) 4 (fx_system)   //horizontal
            PLAY_AND_KILL_FX_SYSTEM fx_system
            fDistance = 0.0
            WHILE 8.0 > fDistance
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 fDistance 0.0) (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (0 0 1 0 0)     //buildings|cars|characters|objects|particles
                    //PRINT_FORMATTED_NOW "Found Char x:%.1f y:%.1f z:%.1f" 1000 x[1] y[1] z[1]   //debug
                    IF GOSUB found_char_in_coords
                        GENERATE_RANDOM_INT_IN_RANGE 0 2 (iTempVar)
                        GOSUB play_Sfx
                        GOSUB assign_task_attach_char_to_char
                        RETURN
                    ELSE
                        BREAK
                    ENDIF
                ENDIF
                fDistance +=@ 0.1
            ENDWHILE
            IF DOES_CHAR_EXIST iChar
                IF GOSUB is_char_playing_anims
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE

    ENDIF
RETURN

attach_trip_mine_to_char:
    GET_OBJECT_HEADING iObj (zAngle)
    zAngle += 180.0
    ATTACH_OBJECT_TO_CHAR iObj iChar (0.0 0.3 0.0) (0.0 0.0 0.0)
    SET_OBJECT_HEADING iObj zAngle
    SET_OBJECT_VISIBLE iObj FALSE
    WAIT 0
RETURN

assign_task_attach_char_to_char:
    IF DOES_CHAR_EXIST pChar
        IF DOES_OBJECT_EXIST iObj
            DELETE_OBJECT iObj
        ENDIF
        IF NOT IS_CHAR_SCRIPT_CONTROLLED pChar
            IF CLEO_CALL is_char_gang_ped 0 pChar
                MARK_CHAR_AS_NEEDED pChar
            ENDIF
        ENDIF
        IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
            IF CLEO_CALL is_char_gang_ped 0 iChar
                MARK_CHAR_AS_NEEDED iChar
            ENDIF
        ENDIF
        GOSUB REQUEST_Animations
        GOSUB assign_task_fly_thru
        GOSUB set_angle_direction
        GOSUB show_fx
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 -0.1 0.25 (x[2] y[2] z[2])
        SET_CHAR_COORDINATES_SIMPLE iChar x[2] y[2] z[2]
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar 0.0 -0.1 0.25 (x[3] y[3] z[3])
        SET_CHAR_COORDINATES_SIMPLE pChar x[3] y[3] z[3]

        WHILE TRUE
            CLEO_CALL getActorBonePos 0 iChar 2 (x[0] y[0] z[0])    //BONE_PELVIS
            GOSUB drawOnScreenLine
            GOSUB set_char_velocity_to_char
            IF LOCATE_CHAR_DISTANCE_TO_CHAR iChar pChar 1.25
                SET_CHAR_COLLISION iChar FALSE
                SET_CHAR_COLLISION pChar FALSE
                BREAK
            ENDIF
        ENDWHILE
        GOSUB assign_task_hit_floor

        GOSUB create_object_web_b
        SET_OBJECT_SCALE iObj 2.0
        GOSUB set_web_object_postion

        timera = 0
        WHILE 4000 > timera
            WAIT 0
        ENDWHILE
        IF NOT IS_CHAR_DEAD iChar
            CLEAR_CHAR_TASKS iChar
            DAMAGE_CHAR iChar 100 TRUE
        ENDIF
        IF NOT IS_CHAR_DEAD pChar
            CLEAR_CHAR_TASKS pChar
            DAMAGE_CHAR pChar 100 TRUE
        ENDIF
        IF DOES_OBJECT_EXIST iObj
            DELETE_OBJECT iObj
        ENDIF
        WAIT 0
    ENDIF
RETURN

drawOnScreenLine:
    CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (v1 v2) (x[1] y[1])
    CLEO_CALL getActorBonePos 0 pChar 2 (x[1] y[1] z[1])    //BONE_PELVIS
    CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v3 v4) (x[1] y[1])
    CLEO_CALL drawline 0 (v3 v4) (v1 v2) 0.5 (255 255 255 255)
RETURN

assign_task_hit_floor:
    TASK_DIE_NAMED_ANIM iChar "sp_wh_b" "spider" 125.0 4000
    TASK_DIE_NAMED_ANIM pChar "sp_wh_b" "spider" 125.0 4000
    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("sp_wh_b" "spider") 35.0 (0 1 1 1) -1
    //TASK_PLAY_ANIM_NON_INTERRUPTABLE pChar ("sp_wh_b" "spider") 35.0 (0 1 1 1) -1
    WAIT 0
    WHILE IS_CHAR_PLAYING_ANIM iChar "sp_wh_b"
        GET_CHAR_ANIM_CURRENT_TIME iChar "sp_wh_b" (fRandomVal)
        IF fRandomVal >= 0.441  //frame 15
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_fly_thru:
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("sp_wh_a" "spider") 5.0 (1 1 1 0) -2
    WAIT 0
    CLEAR_CHAR_TASKS pChar
    CLEAR_CHAR_TASKS_IMMEDIATELY pChar
    TASK_PLAY_ANIM_NON_INTERRUPTABLE pChar ("sp_wh_a" "spider") 5.0 (1 1 1 0) -2
    WAIT 0
RETURN

set_web_object_postion:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar (0.0 0.0 0.0) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_OBJECT_ROTATION iObj 90.0 0.0 zAngle
    GET_COORD_FROM_ANGLED_DISTANCE x[1] y[1] zAngle 0.5 (x[0] y[0])
    GET_GROUND_Z_FOR_3D_COORD (x[0] y[0] z[0]) (z[0])
    z[0] += 0.3
    SET_OBJECT_COORDINATES_AND_VELOCITY iObj (x[0] y[0] z[0])
RETURN        

set_char_velocity_to_char:
    // simulates is in air  -avoid stuck in ground
    GET_PED_POINTER pChar (iTempVar)
    WRITE_STRUCT_OFFSET iTempVar 0x46C 1 0    // ONFOOT_STATE = AIR
    CLEO_CALL setCharVelocityTo 0 pChar (x[2] y[2] z[2]) 10.0
    GET_PED_POINTER iChar (iTempVar)
    WRITE_STRUCT_OFFSET iTempVar 0x46C 1 0    // ONFOOT_STATE = AIR
    CLEO_CALL setCharVelocityTo 0 iChar (x[3] y[3] z[3]) 10.0
RETURN

set_angle_direction:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS pChar (0.0 0.0 0.25) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING pChar zAngle
    zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle
RETURN

show_fx:
    CREATE_FX_SYSTEM_ON_CHAR SP_HIT_WEB iChar (0.0 0.25 0.5) 4 (fx_system)  //shootlight
    PLAY_AND_KILL_FX_SYSTEM fx_system
    WAIT 0
    CREATE_FX_SYSTEM_ON_CHAR SP_HIT_WEB pChar (0.0 0.25 0.5) 4 (fx_system)  //shootlight
    PLAY_AND_KILL_FX_SYSTEM fx_system
RETURN

is_char_playing_anims:
    IF IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")  //Web Shoot
    OR IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    OR IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    OR IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
    OR IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    OR IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_not_char_playing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")  //Web Shoot
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_not_char_playing_anims_b:
    IF NOT IS_CHAR_PLAYING_ANIM pChar ("hit_wshoot_p")  //Web Shoot
    AND NOT IS_CHAR_PLAYING_ANIM pChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM pChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM pChar ("sp_wf_a")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM pChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM pChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

found_char_in_coords:
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i pChar)
        IF DOES_CHAR_EXIST pChar
        AND NOT IS_CHAR_DEAD pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR iChar pChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor pChar
            IF NOT IS_CHAR_IN_ANY_CAR pChar
            AND NOT IS_CHAR_ON_ANY_BIKE pChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE pChar
                
                IF GOSUB is_not_char_playing_anims_b
                    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES pChar x[1] y[1] z[1] 0.75
                        RETURN_TRUE
                        RETURN
                    ENDIF
                ENDIF

            ENDIF
        ENDIF
    ENDWHILE
    RETURN_FALSE
RETURN

create_object_web_b:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    REQUEST_MODEL 6021  //wwg
    LOAD_ALL_MODELS_NOW
    WAIT 0
    //CREATE_OBJECT 6021 0.0 0.0 0.0 (iObj)
    CREATE_OBJECT_NO_SAVE 6021 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_COLLISION iObj FALSE
    MARK_MODEL_AS_NO_LONGER_NEEDED 6021
RETURN

play_Sfx:
    REMOVE_AUDIO_STREAM sfx
    IF DOES_OBJECT_EXIST iObj
        SWITCH iTempVar
            CASE 0
                IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot8a.mp3" (sfx)     //off
                    SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                    SET_AUDIO_STREAM_STATE sfx 1
                ENDIF
                BREAK
            CASE 1
                IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot8b.mp3" (sfx)     //off
                    SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                    SET_AUDIO_STREAM_STATE sfx 1
                ENDIF
                BREAK
        ENDSWITCH
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
//CLEO_CALL getXangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*xAngle*/fSyncAngle)
getXangleBetweenPoints:
    LVAR_FLOAT xA yA zA
    LVAR_FLOAT xB yB zB
    LVAR_FLOAT pointY pointZ
    LVAR_FLOAT xAngle
    GET_DISTANCE_BETWEEN_COORDS_2D xA yA xB yB (pointY)
    pointZ = (zA - zB)
    GET_HEADING_FROM_VECTOR_2D pointY pointZ (xAngle)
    xAngle -= 270.0
CLEO_RETURN 0 xAngle
}
{
//CLEO_CALL getActorBonePos 0 /*actor*/actor /*bone*/0 /*store_to*/var1 var2 var3 
getActorBonePos:
    LVAR_INT scplayer iBone  //in
    LVAR_FLOAT fx fy fz
    LVAR_INT var5 //var6
    GET_PED_POINTER scplayer (scplayer)
    GET_VAR_POINTER (fx) (var5)
    CALL_METHOD 0x5E4280 /*struct*/scplayer /*params*/3 /*pop*/0 /*bUnk*/1 /*nBone*/iBone /*pPoint*/ var5
    /// 0x5E4280 - RwV3d *__thiscall CPed__getBonePosition(RwV3d *vPosition int iBoneID, bool bIncludeAnim)
    /// https://wiki.multitheftauto.com/wiki/GetPedBonePosition
CLEO_RETURN 0 fx fy fz
}
{
//CLEO_CALL setObjectVelocityTo 0 iObj (x y z) Amp
setObjectVelocityTo:
    LVAR_INT iObj   //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    IF DOES_OBJECT_EXIST iObj
        x[1] = xIn
        y[1] = yIn
        z[1] = zIn
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
        x[1] -= x[0]
        y[1] -= y[0]
        z[1] -= z[0]
        GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) fDistance
        x[1] = (x[1] / fDistance)
        y[1] = (y[1] / fDistance)
        z[1] = (z[1] / fDistance)
        x[1] *= iAmplitude
        y[1] *= iAmplitude
        z[1] *= iAmplitude
        SET_OBJECT_DYNAMIC iObj TRUE
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
        WAIT 0
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocityTo 0 scplayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer
    LVAR_FLOAT xIn yIn zIn
    LVAR_FLOAT iAmplitude
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    x[1] = xIn
    y[1] = yIn
    z[1] = zIn
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) fDistance
    x[1] = (x[1] / fDistance)
    y[1] = (y[1] / fDistance)
    z[1] = (z[1] / fDistance)
    x[1] *= iAmplitude
    y[1] *= iAmplitude
    z[1] *= iAmplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.8 
setCoordsInter:
    LVAR_INT scplayer
    LVAR_FLOAT fX fY fZ fDistance
    LVAR_FLOAT x[3] y[3] z[3]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX fY fZ (x[1] y[1] z[1])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[2] y[2] z[2])
    IF NOT x[1] = x[2]
    AND NOT y[1] = y[2]
    AND NOT z[1] = z[2]
         y[2] -= fDistance
         SET_CHAR_COORDINATES_SIMPLE scplayer x[2] y[2] z[2]
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setZangleCharWall 0 player_actor /*xyz*/(0.2 10.0 0.0) /*xyz*/(-0.2 10.0 0.0) 90.0
setZangleCharWall:
    LVAR_INT scplayer           //in
    LVAR_FLOAT fX fY fZ fX2 fY2 fZ2 fFixAngle    //in
    LVAR_FLOAT x[3] y[3] z[3]
    LVAR_FLOAT fAngle
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX fY fZ (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX2 fY2 fZ2 (x[2] y[2] z[2])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[1] y[1] z[1])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[2] y[2] z[2]) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[1] y[1]) (fAngle)
    fAngle += fFixAngle
    //PRINT_FORMATTED_NOW "angle: %.1f" 1000 fAngle
    SET_CHAR_HEADING scplayer fAngle
CLEO_RETURN 0
}

//CLEO_CALL setZangleObjectWall 0 iObj (0.5 3.0 0.0) (-0.5 3.0 0.0) 90.0
{
//CLEO_CALL setZangleObjectWall 0 iObj /*xyz*/(0.2 10.0 0.0) /*xyz*/(-0.2 10.0 0.0) 90.0
setZangleObjectWall:
    LVAR_INT iObj           //in
    LVAR_FLOAT fX fY fZ fX2 fY2 fZ2 fFixAngle    //in
    LVAR_FLOAT x[3] y[3] z[3]
    LVAR_FLOAT fAngle
    IF DOES_OBJECT_EXIST iObj
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj fX fY fZ (x[1] y[1] z[1])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj fX2 fY2 fZ2 (x[2] y[2] z[2])
        CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[1] y[1] z[1])
        CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[2] y[2] z[2]) (x[2] y[2] z[2])
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[2] y[2]) (fAngle)
        fAngle += fFixAngle
        //PRINT_FORMATTED_NOW "angle: %.1f" 1000 fAngle
        SET_OBJECT_HEADING iObj fAngle
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL getLaserPoint 0 /*from*/0.0 0.0 0.0 /*to*/1.0 0.0 0.0 /*store_to*/ var1 var2 var3
getLaserPoint:
    LVAR_FLOAT fromX fromY fromZ toX toY toZ    //in
    LVAR_FLOAT resultX resultY resultZ
    LVAR_INT scplayer i
    GET_PLAYER_CHAR 0 scplayer
    GET_PED_POINTER scplayer (i)
    IF GET_COLLISION_BETWEEN_POINTS (fromX fromY fromZ) (toX toY toZ) TRUE TRUE TRUE TRUE FALSE TRUE TRUE TRUE i 0x0 (resultX resultY resultZ i)
    ELSE
        resultX = toX
        resultY = toY
        resultZ = toZ
    ENDIF
CLEO_RETURN 0 resultX resultY resultZ
}
{
//CLEO_CALL has_object_collided_in_offset_or_sides 0 iObj 0.65
has_object_collided_in_offset_or_sides:
    LVAR_INT iObj   //in
    LVAR_FLOAT yOffset  //in
    LVAR_FLOAT x[2] y[2] z[2]
    LVAR_INT counter
    IF DOES_OBJECT_EXIST iObj
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 yOffset 0.0 (x[1] y[1] z[1])
        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front
            RETURN_TRUE
            CLEO_RETURN 0
        ELSE
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj -0.5 yOffset 0.0 (x[1] y[1] z[1])
            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front - left
                RETURN_TRUE
                CLEO_RETURN 0
            ELSE
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj -0.5 0.0 0.0 (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //left
                    RETURN_TRUE
                    CLEO_RETURN 0
                ELSE
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.5 yOffset 0.0 (x[1] y[1] z[1])
                    IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front-right
                        RETURN_TRUE
                        CLEO_RETURN 0
                    ELSE
                        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.5 0.0 0.0 (x[1] y[1] z[1])
                        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //right
                            RETURN_TRUE
                            CLEO_RETURN 0
                        ELSE
                            RETURN_FALSE
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL has_char_collision_in_offset_or_sides 0 iChar 0.65
has_char_collision_in_offset_or_sides:
    LVAR_INT iChar   //in
    LVAR_FLOAT yOffset  //in
    LVAR_FLOAT x[2] y[2] z[2]
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 yOffset 0.0 (x[1] y[1] z[1])
        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front
            RETURN_TRUE
        ELSE
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar -0.5 yOffset 0.0 (x[1] y[1] z[1])
            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front - left
                RETURN_TRUE
            ELSE
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar -0.5 0.0 0.0 (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //left
                    RETURN_TRUE
                ELSE
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.5 yOffset 0.0 (x[1] y[1] z[1])
                    IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //front-right
                        RETURN_TRUE
                    ELSE
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.5 0.0 0.0 (x[1] y[1] z[1])
                        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //right
                            RETURN_TRUE
                        ELSE
                            RETURN_FALSE
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL drawline 0 x y x1 y1 fThickness r g b a
drawline:
    LVAR_FLOAT x y x1 y1 fThickness //in
    LVAR_INT r g b a    //in
    LVAR_FLOAT fDistance zAngle
    GET_DISTANCE_BETWEEN_COORDS_2D x y x1 y1 (fDistance)
    x1 -= x
    y1 -= y
    GET_HEADING_FROM_VECTOR_2D x1 y1 (zAngle)
    zAngle += 90.0
    x1 /= 2.0
    y1 /= 2.0
    x += x1
    y += y1
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE_WITH_ROTATION 666 x y fDistance fThickness zAngle r g b a
CLEO_RETURN 0
}
{
//CLEO_CALL attachObjectToActorOnBone 0 char_handle object_attached offset_x offset_y offset_z bone
attachObjectToActorOnBone:
    LVAR_INT char_handle object_attached    //in
    LVAR_FLOAT offset_x offset_y offset_z   //in
    LVAR_INT bone   //in
    LVAR_INT tempi_1 tempi_2
    GET_PED_POINTER char_handle char_handle
    CALL_METHOD 0x532B20 char_handle 0 0 // sub_532B20
    char_handle += 0x18
    READ_MEMORY char_handle 4 0 char_handle
    CALL_FUNCTION_RETURN 0x734A40 1 1 char_handle char_handle // _clumpGetFirstSkinAtomicHAnimHierarchy
    IF NOT char_handle = 0
        CALL_FUNCTION_RETURN 0x7C51A0 2 2 bone char_handle bone // _RpHAnimIDGetIndex
        CALL_FUNCTION_RETURN 0x7C5120 1 1 char_handle char_handle // _RpHAnimHierarchyGetMatrixArray
        bone *= 0x40
        bone += char_handle
        GET_VAR_POINTER offset_x tempi_1
        CALL_FUNCTION 0x54EEF0 4 4 tempi_1 bone 1 tempi_1 // _transformPoints
        SET_OBJECT_HEADING object_attached 0.0
        GET_OBJECT_POINTER object_attached object_attached
        object_attached += 0x14
        READ_MEMORY object_attached 4 0 char_handle
        object_attached -= 0x14
        CALL_METHOD 0x411990 object_attached 0 0 // sub_411990
        CALL_METHOD 0x59AD20 char_handle 1 0 bone // CMatrix__copyTo
        CALL_METHOD 0x4241C0 object_attached 1 0 tempi_1 // CPlaceable__setPosition
        object_attached += 0x18
        READ_MEMORY object_attached 4 0 tempi_2
        IF NOT tempi_2 = 0
            tempi_2 += 0x4
            READ_MEMORY tempi_2 4 0 tempi_2
            tempi_2 += 0x10
            CALL_METHOD 0x59AD70 char_handle 1 0 tempi_2 // CMatrix__copyTo
            object_attached -= 0x18
            CALL_METHOD 0x532B00 object_attached 0 0 // _RwFrameUpdateObject
        ELSE
            object_attached -= 0x18
            CALL_METHOD 0x532B00 object_attached 0 0 // _RwFrameUpdateObject        
        ENDIF
    ELSE
        CLEO_RETURN 0
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
