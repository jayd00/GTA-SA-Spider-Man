// by J16D
// Car Chase script - BETA Stable - under develpment (300+ tests)
// Format:
//      STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_cc.cs" {x_coord_float} {y_coord_float} {z_coord_float}   //start coordinates
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//-+-- Mission constants
CONST_INT ON_GROUND_PURSUIT 1
CONST_INT SUCCED_PURSUIT 2
CONST_INT FAIL_PURSUIT 3

SCRIPT_START
{
SCRIPT_NAME sp_cc
LVAR_FLOAT v1 v2 zAngle  // xVar yVar zVar  // in
LVAR_INT player_actor char[2] baseObject iWebActor iWebActorR obj veh iSfx[2] iBlipVeh anim_seq
LVAR_FLOAT fCurrentTime fTempVar fRandomVal x[2] y[2] z[2] xAngle
LVAR_INT LRStick UDStick iRandomVal
LVAR_INT flag_enemy1_killed flag_enemy2_killed enemys_kills_counter iTempVar

GET_PLAYER_CHAR 0 player_actor
USE_TEXT_COMMANDS FALSE
GOSUB REQUEST_Animations
GOSUB REQUEST_Web_Animations
GOSUB load_and_create_entities

WHILE IS_CAR_WAITING_FOR_WORLD_COLLISION veh
    WAIT 0
ENDWHILE
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_cd.cs"
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_cd.cs" veh char[0] char[1]  // police_chase
ENDIF
flag_enemy1_killed = FALSE
flag_enemy2_killed = FALSE

WHILE TRUE
    IF IS_PLAYER_PLAYING 0
        GET_CLEO_SHARED_VAR varStatusSpiderMod (iTempVar)
        IF iTempVar = 1  //TRUE

            IF DOES_VEHICLE_EXIST veh
                IF NOT IS_CAR_UPSIDEDOWN veh
                AND NOT IS_CAR_DEAD veh
                    
                    IF DOES_CHAR_EXIST char[0]
                        IF IS_CHAR_IN_ANY_CAR char[0]
                            GET_CLEO_SHARED_VAR varInMenu (iTempVar)
                            IF iTempVar = 0     //1:true 0: false

                                GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh 0.0 0.0 1.0 (x[0] y[0] z[0])
                                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
                                GOSUB draw_on_screen_distance_char_veh
                                
                                IF LOCATE_CHAR_DISTANCE_TO_CAR player_actor veh 15.0
                                    GOSUB draw_car_indicator
                                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                    //AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                        GOSUB start_char_sequence_car
                                        IF iRandomVal = SUCCED_PURSUIT
                                            //create a new car with thugs
                                            GOTO mission_passed
                                        ELSE
                                            IF iRandomVal = FAIL_PURSUIT
                                                GOTO mission_failed
                                            ENDIF                   
                                        ENDIF
                                    ENDIF
                                ELSE
                                    IF LOCATE_CHAR_DISTANCE_TO_CAR player_actor veh 60.0
                                        GOSUB draw_target_vehicle
                                    ENDIF
                                ENDIF
                            
                            ENDIF
                        ELSE
                            GOTO mission_on_foot_battle_init
                        ENDIF
                    ENDIF

                ELSE
                    //CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                    GOTO mission_on_foot_battle_init
                ENDIF

                IF NOT LOCATE_CHAR_DISTANCE_TO_CAR player_actor veh 250.0
                    GOTO mission_failed
                ENDIF
            ENDIF

        ELSE
            GOTO cancel_mission
        ENDIF

    ENDIF
	WAIT 0
ENDWHILE

//-+---------------------------------------------------
cancel_mission:
    GOSUB mission_cleanup_A
GOTO mission_cleanup_B

// Mission failed
mission_failed:
    GOSUB mission_cleanup_A
    //GET_CLEO_SHARED_VAR varInMenu (iTempVar)
    //IF iTempVar = 0     //1:true 0: false
        WHILE NOT IS_PLAYER_PLAYING 0
            WAIT 0
        ENDWHILE
        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT

        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 3  //{id}
            WAIT 2000
        ENDIF
    //ENDIF
GOTO mission_cleanup_B

// mission passed
mission_passed:
    GOSUB mission_cleanup_A
    WAIT 0
    //GET_CLEO_SHARED_VAR varInMenu (iTempVar)
    //IF iTempVar = 0     //1:true 0: false
        LRStick = 0     // Stollen Vehicle XP  (recicled vars)
        UDStick = 0     // Combat XP

        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT

        IF iRandomVal = SUCCED_PURSUIT
            LRStick = 50     // Stollen Vehicle XP
            UDStick = 0      // Combat XP
        ELSE
            IF enemys_kills_counter >= 2
                LRStick = 35     // Stollen Vehicle XP
                UDStick = 30      // Combat XP
            ELSE
                IF flag_enemy1_killed = TRUE
                    IF flag_enemy2_killed = TRUE
                        LRStick = 35     // Stollen Vehicle XP
                        UDStick = 30      // Combat XP
                    ELSE
                        LRStick = 35     // Stollen Vehicle XP
                        UDStick = 15      // Combat XP
                    ENDIF
                ELSE
                    IF flag_enemy2_killed = TRUE
                        LRStick = 35     // Stollen Vehicle XP
                        UDStick = 15      // Combat XP
                    ELSE
                        LRStick = 35     // Stollen Vehicle XP
                        UDStick = 0      // Combat XP
                    ENDIF            
                ENDIF
            ENDIF
        ENDIF
        iRandomVal = 0 // Total XP
        iRandomVal += LRStick  // Total XP
        iRandomVal += UDStick  // Total XP
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 2 iRandomVal LRStick UDStick    //{id} {total xp} {stolen veh xp} {combat xp}
            WAIT 2000
        ENDIF
        SET_CLEO_SHARED_VAR varStatusLevelChar iRandomVal   //set value of +250

        GET_CLEO_SHARED_VAR varCarChaseProgress (iRandomVal)
        iRandomVal ++
        SET_CLEO_SHARED_VAR varCarChaseProgress iRandomVal
        WRITE_INT_TO_INI_FILE iRandomVal "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_cchase"
    //ENDIF
GOTO mission_cleanup_B

// mission cleanup
mission_cleanup_A:
    CLEO_CALL getDecisionMaker 0 (iTempVar)
        REMOVE_DECISION_MAKER iTempVar
        CLEO_CALL storeDecisionMaker 0 0x0

    CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor

    IF DOES_BLIP_EXIST iBlipVeh
        REMOVE_BLIP iBlipVeh
    ENDIF
    IF DOES_CHAR_EXIST char[0]
        MARK_CHAR_AS_NO_LONGER_NEEDED char[0]
    ENDIF
    IF DOES_CHAR_EXIST char[1]
        MARK_CHAR_AS_NO_LONGER_NEEDED char[1]
    ENDIF
    IF DOES_OBJECT_EXIST obj
        DELETE_OBJECT obj
    ENDIF
    IF DOES_VEHICLE_EXIST veh
        MARK_CAR_AS_NO_LONGER_NEEDED veh
    ENDIF
    REMOVE_ANIMATION "mweb"
    REMOVE_ANIMATION "spider"
    //USE_TEXT_COMMANDS FALSE
    //REMOVE_TEXTURE_DICTIONARY
    //REMOVE_AUDIO_STREAM iSfx[0]
    REMOVE_AUDIO_STREAM iSfx[1]

    iTempVar = 0    //flag_player_on_mission
    SET_CLEO_SHARED_VAR varOnmission iTempVar        // 0:OFF || 1:ON
    WAIT 0
RETURN

mission_cleanup_B:
    REMOVE_AUDIO_STREAM iSfx[0]
    USE_TEXT_COMMANDS FALSE
    WAIT 0
    REMOVE_TEXTURE_DICTIONARY
    WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT
//-+---------------------------------------------------

//-+---------------------------------------------------
mission_on_foot_battle_init:
IF DOES_BLIP_EXIST iBlipVeh
	REMOVE_BLIP iBlipVeh
ENDIF
IF DOES_OBJECT_EXIST obj
	DELETE_OBJECT obj
ENDIF
IF DOES_OBJECT_EXIST baseObject
	DELETE_OBJECT baseObject
ENDIF
IF DOES_VEHICLE_EXIST veh
	MARK_CAR_AS_NO_LONGER_NEEDED veh
ENDIF

// leve car && kill player
IF DOES_CHAR_EXIST char[0]
	IF NOT IS_CHAR_DEAD char[0]
		CLEAR_CHAR_TASKS char[0]
		IF IS_CHAR_IN_ANY_CAR char[0]
			OPEN_SEQUENCE_TASK anim_seq
				TASK_LEAVE_ANY_CAR char[0]
				TASK_KILL_CHAR_ON_FOOT char[0] player_actor
			CLOSE_SEQUENCE_TASK anim_seq
		ELSE
			OPEN_SEQUENCE_TASK anim_seq
				TASK_KILL_CHAR_ON_FOOT char[0] player_actor
			CLOSE_SEQUENCE_TASK anim_seq
		ENDIF
		PERFORM_SEQUENCE_TASK char[0] anim_seq
		WAIT 0
		CLEAR_SEQUENCE_TASK anim_seq
		WAIT 0

        ADD_BLIP_FOR_CHAR char[0] (iBlipVeh)
        CHANGE_BLIP_COLOUR iBlipVeh RED
        CHANGE_BLIP_DISPLAY iBlipVeh BLIP_ONLY
	ELSE
		enemys_kills_counter ++
		flag_enemy1_killed = TRUE
	ENDIF
ELSE
	flag_enemy1_killed = TRUE
ENDIF

IF DOES_CHAR_EXIST char[1]
	IF NOT IS_CHAR_DEAD char[1]

		CLEAR_CHAR_TASKS char[1]
		IF IS_CHAR_IN_ANY_CAR char[1]
			OPEN_SEQUENCE_TASK anim_seq
				TASK_LEAVE_ANY_CAR char[1]
				TASK_KILL_CHAR_ON_FOOT char[1] player_actor
			CLOSE_SEQUENCE_TASK anim_seq
		ELSE
			OPEN_SEQUENCE_TASK anim_seq
				TASK_KILL_CHAR_ON_FOOT char[1] player_actor
			CLOSE_SEQUENCE_TASK anim_seq
		ENDIF
		PERFORM_SEQUENCE_TASK char[1] anim_seq
		WAIT 0
		CLEAR_SEQUENCE_TASK anim_seq
		WAIT 0

        ADD_BLIP_FOR_CHAR char[1] (baseObject)
        CHANGE_BLIP_COLOUR baseObject RED
        CHANGE_BLIP_DISPLAY baseObject BLIP_ONLY
	ELSE
		enemys_kills_counter ++
		flag_enemy2_killed = TRUE
	ENDIF
ELSE
	flag_enemy2_killed = TRUE
ENDIF
SET_CHAR_IS_TARGET_PRIORITY player_actor TRUE

mission_on_foot_battle:
IF IS_PLAYER_PLAYING 0

	IF DOES_CHAR_EXIST char[0]
	    IF flag_enemy1_killed = FALSE
            IF NOT IS_CHAR_DEAD char[0]
                //I wanted to avoid this, but it is necesary
                IF NOT IS_CHAR_DOING_ANY_IMPORTANT_TASK char[0] INCLUDE_ANIMS_PRIMARY
                    GET_SCRIPT_TASK_STATUS char[0] 0x618 (iRandomVal) // PERFORM_SEQUENCE_TASK
                    IF iRandomVal = 7  //-1
                        GET_SCRIPT_TASK_STATUS char[0] 0x5E2 (iRandomVal) // TASK_KILL_CHAR_ON_FOOT
                        IF iRandomVal = 7  //-1
                            OPEN_SEQUENCE_TASK anim_seq
                                TASK_KILL_CHAR_ON_FOOT -1 player_actor
                            CLOSE_SEQUENCE_TASK anim_seq
                            PERFORM_SEQUENCE_TASK char[0] anim_seq
                            CLEAR_SEQUENCE_TASK anim_seq
                            WAIT 0
                        ENDIF
                    ENDIF

                ENDIF

            ELSE
                enemys_kills_counter ++
                flag_enemy1_killed = TRUE
                IF DOES_BLIP_EXIST iBlipVeh
                    REMOVE_BLIP iBlipVeh
                ENDIF
            ENDIF
        ENDIF
    ELSE
        flag_enemy1_killed = TRUE
    ENDIF
	IF DOES_CHAR_EXIST char[1]
        IF flag_enemy2_killed = FALSE
            IF NOT IS_CHAR_DEAD char[1]
                //I wanted to avoid this, but it is necesary
                IF NOT IS_CHAR_DOING_ANY_IMPORTANT_TASK char[1] INCLUDE_ANIMS_PRIMARY
                    GET_SCRIPT_TASK_STATUS char[1] 0x618 (iRandomVal) // PERFORM_SEQUENCE_TASK
                    IF iRandomVal = 7  //-1
                        GET_SCRIPT_TASK_STATUS char[1] 0x5E2 (iRandomVal) // TASK_KILL_CHAR_ON_FOOT
                        IF iRandomVal = 7  //-1
                            OPEN_SEQUENCE_TASK anim_seq
                                TASK_KILL_CHAR_ON_FOOT -1 player_actor
                            CLOSE_SEQUENCE_TASK anim_seq
                            PERFORM_SEQUENCE_TASK char[1] anim_seq
                            CLEAR_SEQUENCE_TASK anim_seq
                            WAIT 0
                        ENDIF
                    ENDIF
                ENDIF

            ELSE
                enemys_kills_counter ++
                flag_enemy2_killed = TRUE
                IF DOES_BLIP_EXIST baseObject
                    REMOVE_BLIP baseObject
                ENDIF
            ENDIF
        ENDIF
    ELSE
        flag_enemy2_killed = TRUE
    ENDIF

	IF enemys_kills_counter >= 2
        GOSUB delete_blips
		GOTO mission_passed
	ELSE
        IF flag_enemy1_killed = TRUE
        AND flag_enemy2_killed = TRUE 
            GOSUB delete_blips
    		GOTO mission_passed
        ELSE
            IF DOES_CHAR_EXIST char[0]
                IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor char[0] 250.0
                    GOSUB delete_blips
                    GOTO mission_failed
                ENDIF
            ENDIF
        ENDIF
    ENDIF
ELSE
    GOTO mission_failed
ENDIF
	WAIT 0
GOTO mission_on_foot_battle

delete_blips:
    IF DOES_BLIP_EXIST iBlipVeh
        REMOVE_BLIP iBlipVeh
    ENDIF
    IF DOES_BLIP_EXIST baseObject
        REMOVE_BLIP baseObject
    ENDIF
RETURN
//-+---------------------------------------------------

start_char_sequence_car:
CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
IF NOT LOCATE_CHAR_DISTANCE_TO_CAR player_actor veh 3.0
    GOSUB in_air_zip_to_point
ELSE
    iRandomVal = TRUE
ENDIF
IF iRandomVal = FALSE
    GOTO endSequence
ENDIF
GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh 0.0 0.0 1.0 (x[0] y[0] z[0])
//SET_CHAR_COLLISION player_actor TRUE
SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]

ATTACH_CHAR_TO_OBJECT player_actor obj (0.0 0.0 0.0) 0 360.0 WEAPONTYPE_BRASSKNUCKLE //WEAPONTYPE_UNARMED
CLEO_CALL setAngleFromCarToChar 0 veh player_actor
IF IS_CAR_UPSIDEDOWN veh
OR IS_CAR_IN_WATER veh
    GOTO endSequence
ENDIF
GOSUB REQUEST_Animations
CLEAR_CHAR_TASKS player_actor
CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_idle_Z" "spider") 101.0 (1 0 0 0) -1
WAIT 0
ATTACH_CAMERA_TO_VEHICLE veh (1.0 -5.25 1.55) (0.15 0.0 0.1) 0.0 2 // Center-Right

WHILE TRUE
    CLEO_CALL setAngleFromCarToChar 0 veh player_actor
    //GET_CAR_SPEED veh (fTempVar)
    //PRINT_FORMATTED_NOW "vel: %f.2" 1 fTempVar

    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > LRStick  //Left
    AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
    AND IS_CHAR_PLAYING_ANIM player_actor "c_idle_Z"

        // Move $Player to Left Side of the Car
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_left_A_00" "spider") 51.0 (0 0 0 1) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "c_left_A_00" 1.45
        WHILE IS_CHAR_PLAYING_ANIM player_actor "c_left_A_00"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_left_A_00") (fCurrentTime)
            IF fCurrentTime > 0.5
                ATTACH_CAMERA_TO_VEHICLE veh (-1.85 -4.65 1.40) (0.15 0.0 0.1) -3.0 2 // Left
            ENDIF
            IF fCurrentTime > 0.98
                BREAK
            ENDIF
            CLEO_CALL setAngleFromCarToChar 0 veh player_actor
            IF IS_CAR_UPSIDEDOWN veh
            OR IS_CAR_DEAD veh
            OR IS_CAR_IN_WATER veh
                GOTO endSequence
            ENDIF
            IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                GOTO die_in_mission
            ENDIF
            WAIT 0
        ENDWHILE

        // IDLE animation for $Player in Left Side of the Car
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_left_A_01" "spider") 151.0 (1 0 0 0) -1
        WAIT 0

        WHILE TRUE
            CLEO_CALL setAngleFromCarToChar 0 veh player_actor
            IF IS_CAR_UPSIDEDOWN veh
            OR IS_CAR_DEAD veh
            OR IS_CAR_IN_WATER veh
                GOTO endSequence
            ENDIF
            IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                GOTO die_in_mission
            ENDIF
            IF HAS_CHAR_BEEN_DAMAGED_BY_WEAPON player_actor WEAPONTYPE_ANYWEAPON
                CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
                GET_CAR_SPEED veh (fTempVar)
                IF fTempVar > 16.0
                    GOSUB player_hitted_by_weap
                    IF iRandomVal > 1
                        GOTO endSequence_cancel_forced
                    ELSE
                        IF iRandomVal = 1
                            WAIT 0
                            BREAK
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF

            CLEO_CALL getDataJoystick 0 (LRStick UDStick)
            IF LRStick > 0  //Right
            AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
            AND IS_CHAR_PLAYING_ANIM player_actor "c_left_A_01"

                // Move $Player to Center of the Car
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_left_A_02" "spider") 30.0 (0 0 0 1) -1
                WAIT 0
                SET_CHAR_ANIM_SPEED player_actor "c_left_A_02" 1.45
                WHILE IS_CHAR_PLAYING_ANIM player_actor "c_left_A_02"
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_left_A_02") (fCurrentTime)
                    IF fCurrentTime > 0.5
                        ATTACH_CAMERA_TO_VEHICLE veh (1.0 -5.25 1.55) (0.15 0.0 0.1) 0.0 2 // Center-Right
                    ENDIF
                    IF fCurrentTime > 0.98
                        BREAK
                    ENDIF
                    CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                    IF IS_CAR_UPSIDEDOWN veh
                    OR IS_CAR_DEAD veh
                    OR IS_CAR_IN_WATER veh
                        GOTO endSequence
                    ENDIF
                    IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                        GOTO die_in_mission
                    ENDIF
                    WAIT 0
                ENDWHILE
                // IDLE animation for $Player - Center of the Car
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_idle_Z" "spider") 101.0 (1 0 0 0) -1
                WAIT 0

                BREAK
            ELSE

                GET_CAR_SPEED veh (fTempVar)
                IF fTempVar >= 22.5

                    IF NOT DOES_CHAR_EXIST char[1]

                        IF IS_CHAR_PLAYING_ANIM player_actor "c_left_A_01"
                        AND IS_CHAR_PLAYING_ANIM char[0] "CAR_sit"

                            CLEO_CALL draw_key_command_sequence 0

                            IF IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                            AND NOT IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
                                // $Player has pressed "FIRE KEY" to kill driver
                                GOSUB REQUEST_Web_Animations
                                GOSUB destroyTwoWebs
                                GOSUB createTwoWebs
                                GET_CAR_SPEED veh (fTempVar)
                                fTempVar -= 5.0
                                // actors animations
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_left_B_00" "spider") 207.0 (0 0 0 1) -1
                                CLEAR_CHAR_TASKS char[0]
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE char[0] ("c_left_B_01" "spider") 96.0 (0 0 0 0) -1
                                //web animations
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_left_B_00" "mweb") 96.0 (0 1 1 1) -1
                                SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_left_B_00") 0
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_left_B_00_1" "mweb") 207.0 (0 1 1 1) -1
                                SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_left_B_00_1") 0

                                WAIT 0
                                SET_CHAR_ANIM_SPEED player_actor "c_left_B_00" 1.45
                                SET_CHAR_ANIM_SPEED char[0] "c_left_B_01" 1.45
                                
                                ATTACH_OBJECT_TO_CAR baseObject veh (0.0 0.0 0.0) (0.0 0.0 0.0)

                                WHILE IS_CHAR_PLAYING_ANIM player_actor "c_left_B_00"
                                    ATTACH_OBJECT_TO_CAR baseObject veh (0.0 0.0 0.0) (0.0 0.0 0.0)

                                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_left_B_00") (fCurrentTime)
                                    SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_left_B_00_1") fCurrentTime
                                    IF IS_CHAR_PLAYING_ANIM iWebActor "w_right_B_00_1"
                                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_right_B_00_1") fCurrentTime
                                    ELSE
                                        IF flag_enemy1_killed = TRUE
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_right_B_00_1" "mweb") 207.0 (0 1 1 1) -1
                                            SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_right_B_00_1") 0
                                        ENDIF
                                    ENDIF

                                    IF 0.146 > fCurrentTime  //frame 30
                                        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 -5.25 1.55) (x[0] y[0] z[0])
                                        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.5) (x[1] y[1] z[1])
                                        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                                    ELSE
                                        IF 0.340 > fCurrentTime  //frame 70
                                            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (-1.0 -3.5 2.0) (x[0] y[0] z[0])
                                            SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                                            CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
                                            POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                                        ELSE
                                            IF 0.413 > fCurrentTime  //frame 85
                                                GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (-1.0 -7.0 1.35) (x[0] y[0] z[0])
                                                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                                                CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
                                                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                                            ELSE
                                                IF 0.485 > fCurrentTime  //frame 100
                                                    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 -7.80 1.0) (x[0] y[0] z[0])
                                                    SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                                                    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 0.5) (x[1] y[1] z[1])
                                                    POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                                                ELSE
                                                    //SHAKE_PAD PAD1 2000 500
                                                    ATTACH_CAMERA_TO_VEHICLE veh (1.0 -7.80 0.65) (0.5 0.0 0.1) -6.0 2
                                                ENDIF
                                            ENDIF
                                        ENDIF
                                    ENDIF
                                    IF fCurrentTime >= 0.267     //frames 55-60 /206
                                    AND 0.291 >= fCurrentTime
                                        SET_TIME_SCALE 0.35
                                    ELSE
                                        SET_TIME_SCALE 1.0
                                    ENDIF
                                    //Slow down speed of vehicle
                                    IF fCurrentTime >= 0.631     //frames 130-186 /206
                                    AND 0.903 > fCurrentTime
                                        CLEO_CALL linearInterpolation 0 (0.631 0.903 fCurrentTime) (fTempVar 0.0) (fRandomVal)
                                        SET_CAR_FORWARD_SPEED veh fRandomVal
                                    ELSE
                                        //Stop vehicle
                                        IF  fCurrentTime >= 0.903    //frame 186/206
                                            SET_CAR_MISSION veh 0
                                            SET_CAR_DRIVING_STYLE veh DRIVINGMODE_STOPFORCARS
                                        ENDIF
                                    ENDIF
                                    IF fCurrentTime >= 0.98
                                        BREAK
                                    ENDIF
                                    IF DOES_CHAR_EXIST char[0]
                                        IF IS_CHAR_PLAYING_ANIM char[0] "c_left_B_01"  //This is because animation has different size (96 frames)
                                            GET_CHAR_ANIM_CURRENT_TIME char[0] ("c_left_B_01") (fCurrentTime)
                                            IF IS_CHAR_PLAYING_ANIM iWebActor "w_left_B_00"
                                                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_left_B_00") fCurrentTime
                                            ENDIF
                                            IF fCurrentTime >= 0.98
                                                CLEO_CALL getBoneOffset 0 char[0] 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
                                                WARP_CHAR_FROM_CAR_TO_COORD char[0] x[1] y[1] z[1]
                                                CLEAR_CHAR_TASKS char[0]
                                                CLEAR_CHAR_TASKS_IMMEDIATELY char[0]
                                                //TASK_KILL_CHAR_ON_FOOT char[0] player_actor
                                                WAIT 0
                                                DELETE_CHAR char[0]
                                                flag_enemy1_killed = TRUE
                                            ENDIF
                                        /*ELSE
                                            IF NOT IS_CHAR_PLAYING_ANIM char[0] "c_left_B_01"
                                                // this is to play web animation for right hand (after throw char thru window)
                                                //IF NOT IS_CHAR_PLAYING_ANIM iWebActor "w_right_B_00_1"
                                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_right_B_00_1" "mweb") 207.0 (0 1 1 1) -1
                                                    SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_right_B_00_1") 0
                                                //ENDIF
                                            ENDIF*/
                                        ENDIF
                                    ENDIF
                                    CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                                    IF IS_CAR_UPSIDEDOWN veh
                                    OR IS_CAR_DEAD veh
                                    OR IS_CAR_IN_WATER veh
                                        GOSUB destroyTwoWebs
                                        GOTO endSequence
                                    ENDIF
                                    IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                                        GOTO die_in_mission
                                    ENDIF
                                    WAIT 0
                                ENDWHILE

                                GOSUB destroyTwoWebs
                                WAIT 0
                                GOTO endSequence_succeed
                            
                            ENDIF
                        
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF

            WAIT 0
        ENDWHILE
        CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor

    ELSE
        IF LRStick > 0  //Right
        AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
        AND IS_CHAR_PLAYING_ANIM player_actor "c_idle_Z"
            
            // Move $Player to Right Side of the Car
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_right_A_00" "spider") 51.0 (0 0 0 1) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "c_right_A_00" 1.45

            WHILE IS_CHAR_PLAYING_ANIM player_actor "c_right_A_00"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_right_A_00") (fCurrentTime)
                IF fCurrentTime > 0.5
                    ATTACH_CAMERA_TO_VEHICLE veh (1.85 -4.65 1.40) (0.15 0.0 0.1) 3.0 2 // Right
                ENDIF
                IF fCurrentTime > 0.98
                    BREAK
                ENDIF
                CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                IF IS_CAR_UPSIDEDOWN veh
                OR IS_CAR_DEAD veh
                OR IS_CAR_IN_WATER veh
                    GOTO endSequence
                ENDIF
                IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                    GOTO die_in_mission
                ENDIF
                WAIT 0
            ENDWHILE

            // IDLE animation for $Player in Right Side of the Car
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_right_A_01" "spider") 151.0 (1 0 0 0) -1
            WAIT 0

            WHILE TRUE
                CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                IF IS_CAR_UPSIDEDOWN veh
                OR IS_CAR_DEAD veh
                OR IS_CAR_IN_WATER veh
                    GOTO endSequence
                ENDIF
                IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                    GOTO die_in_mission
                ENDIF
                IF HAS_CHAR_BEEN_DAMAGED_BY_WEAPON player_actor WEAPONTYPE_ANYWEAPON
                    CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
                    GET_CAR_SPEED veh (fTempVar)
                    IF fTempVar > 16.0
                        GOSUB player_hitted_by_weap
                        IF iRandomVal > 1
                            GOTO endSequence_cancel_forced
                        ELSE
                            IF iRandomVal = 1
                                WAIT 0
                                BREAK
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

                CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                IF 0 > LRStick  //Left
                AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~    
                AND IS_CHAR_PLAYING_ANIM player_actor "c_right_A_01"

                    // Move $Player to Center of the Car
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_right_A_02" "spider") 30.0 (0 0 0 1) -1
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "c_right_A_02" 1.35
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "c_right_A_02"
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_right_A_02") (fCurrentTime)
                        IF fCurrentTime > 0.483    //frame 14/29
                            ATTACH_CAMERA_TO_VEHICLE veh (1.0 -5.25 1.55) (0.15 0.0 0.1) 0.0 2 // Center-Right
                        ENDIF
                        IF fCurrentTime >= 0.966 //frame 28/29
                            BREAK
                        ENDIF
                        CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                        IF IS_CAR_UPSIDEDOWN veh
                        OR IS_CAR_DEAD veh
                        OR IS_CAR_IN_WATER veh
                            GOTO endSequence
                        ENDIF
                        IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                            GOTO die_in_mission
                        ENDIF
                        WAIT 0
                    ENDWHILE

                    // IDLE animation for $Player - Center of the Car
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_idle_Z" "spider") 101.0 (1 0 0 0) -1
                    WAIT 0
                    BREAK
                ELSE

                    GET_CAR_SPEED veh (fTempVar)
                    IF fTempVar >= 22.5
                    
                        IF DOES_CHAR_EXIST char[1]
                            
                            IF IS_CHAR_PLAYING_ANIM player_actor "c_right_A_01"
                            AND IS_CHAR_PLAYING_ANIM char[1] "CAR_sitp"
                                
                                CLEO_CALL draw_key_command_sequence 0
                                
                                IF IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                                AND NOT IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
                                    // $Player has pressed "FIRE KEY" to kill passenger
                                    GOSUB REQUEST_Web_Animations
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    ATTACH_CAMERA_TO_VEHICLE veh (2.0 -3.0 2.25) (0.5 0.0 0.1) 0.0 2 // Right-Kill PEd
                                    //actors animations
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_right_B_00" "spider") 93.0 (0 0 0 1) -1
                                    CLEAR_CHAR_TASKS char[1]
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE char[1] ("c_right_B_01" "spider") 93.0 (0 0 0 1) -1
                                    //web animations
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_right_B_00" "mweb") 93.0 (0 1 1 1) -1
                                    SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_right_B_00") 0
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_right_B_00" "mweb") 93.0 (0 1 1 1) -1
                                    SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_right_B_00") 0
                                    WAIT 0
                                    SET_CHAR_ANIM_SPEED player_actor "c_right_B_00" 1.45
                                    SET_CHAR_ANIM_SPEED char[1] "c_right_B_01" 1.45

                                    ATTACH_OBJECT_TO_CAR baseObject veh (0.0 0.0 0.0) (0.0 0.0 0.0)
                                    iRandomVal = 0 // temp check for playing sfx

                                    WHILE IS_CHAR_PLAYING_ANIM player_actor "c_right_B_00"
                                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_right_B_00") (fCurrentTime)

                                        IF IS_CHAR_PLAYING_ANIM iWebActor "w_right_B_00"
                                            SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_right_B_00") fCurrentTime
                                        ENDIF
                                        IF IS_CHAR_PLAYING_ANIM iWebActorR "w_right_B_00"
                                            SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_right_B_00") fCurrentTime
                                        ENDIF
                                        IF fCurrentTime >= 0.163  //frame 15/92
                                            IF iRandomVal = 0
                                                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
                                                iRandomVal = 1
                                            ENDIF
                                        ENDIF
                                        IF fCurrentTime > 0.602  //frame 
                                            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (1.0 -5.25 1.55) (x[0] y[0] z[0])
                                            SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                                            CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
                                            POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                                        ENDIF
                                        IF fCurrentTime >= 0.989 //frame 91/92
                                            BREAK
                                        ENDIF
                                        CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                                        IF IS_CAR_UPSIDEDOWN veh
                                        OR IS_CAR_DEAD veh
                                        OR IS_CAR_IN_WATER veh
                                            GOTO endSequence
                                        ENDIF
                                        IF NOT IS_CHAR_HEALTH_GREATER player_actor 1
                                            GOTO die_in_mission
                                        ENDIF
                                        WAIT 0
                                    ENDWHILE

                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_idle_Z" "spider") 101.0 (1 0 0 0) -1
                                    ATTACH_CAMERA_TO_VEHICLE veh (1.0 -5.25 1.55) (0.15 0.0 0.1) 0.0 2 // Center-Right

                                    GOSUB destroyTwoWebs
                                    CLEO_CALL getBoneOffset 0 char[1] 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
                                    WARP_CHAR_FROM_CAR_TO_COORD char[1] x[1] y[1] z[1]
                                    CLEAR_CHAR_TASKS char[1]
                                    DELETE_CHAR char[1]
                                    flag_enemy2_killed = TRUE
                                    IF DOES_CHAR_EXIST char[0]
                                        TASK_DRIVE_BY char[0] player_actor -1 (0.0 0.0 0.0) 50.0 (6 0) 60   //6 - from the window but only to the back of the view
                                    ENDIF
                                    WAIT 0
                                    BREAK
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
            CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
        ENDIF            
    ENDIF

    IF IS_BUTTON_PRESSED PAD1 SQUARE            // ~k~~PED_JUMPING~
    AND NOT IS_BUTTON_PRESSED PAD1 CROSS        // ~k~~PED_SPRINT~
    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE       // ~k~~PED_FIREWEAPON~
    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_LEFT~/
        //----------------------------------- Jump
        GOSUB REQUEST_Animations
        DETACH_CHAR_FROM_CAR player_actor
        //SET_CHAR_COLLISION player_actor TRUE
        CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
        SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
        y[1] = -0.5
        z[1] = 2.0
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_A" "spider") 6.0 (0 1 1 0) -1
        WAIT 0
        CLEO_CALL setCharVelocity 0 player_actor (0.0 y[1] z[1]) 8.0
        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_A") (fCurrentTime)
            IF fCurrentTime >= 0.800  //frame 4/5
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_A" "spider") 23.0 (0 1 1 0) -2
        WAIT 0
        iRandomVal = 1  //fall_ground:1 || succed_killed:2 || fail:3
        RETURN
    ENDIF
    IF HAS_CHAR_BEEN_DAMAGED_BY_WEAPON player_actor WEAPONTYPE_ANYWEAPON
        CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
        GET_CAR_SPEED veh (fTempVar)
        IF fTempVar > 16.0
            GOSUB player_hitted_by_weap
            IF iRandomVal > 1
                GOTO endSequence_cancel_forced
            ENDIF
        ENDIF
    ENDIF

    IF IS_CAR_DEAD veh
        IF DOES_CHAR_EXIST char[0]
            IF IS_CHAR_DEAD char[0]
                flag_enemy1_killed = TRUE
            ENDIF
        ELSE
            flag_enemy1_killed = TRUE
        ENDIF
        IF DOES_CHAR_EXIST char[1]
            IF IS_CHAR_DEAD char[1]
                flag_enemy2_killed = TRUE
            ENDIF
        ELSE
            flag_enemy2_killed = TRUE
        ENDIF
        GOTO endSequence_succeed
    ENDIF
    IF IS_CAR_UPSIDEDOWN veh
    OR IS_CAR_IN_WATER veh
        GOTO endSequence
    ENDIF
    IF IS_CHAR_DEAD player_actor
    OR HAS_CHAR_BEEN_ARRESTED player_actor
        iRandomVal = 3  //fall_ground:1 || succed_killed:2 || fail:3
        RETURN
    ENDIF
    /* force quit
    IF IS_KEY_PRESSED VK_KEY_I
        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT
        WHILE   IS_KEY_PRESSED VK_KEY_I
            WAIT 0
        ENDWHILE  
        GOTO endSequence
    ENDIF*/
    WAIT 0
ENDWHILE

player_hitted_by_weap:
    //$Player hitted by weapon
    //If is in center of vehicle
    IF IS_CHAR_PLAYING_ANIM player_actor "c_idle_Z"
        //setSquenceFall
        ATTACH_CAMERA_TO_VEHICLE veh (1.0 -7.80 2.0) (0.5 0.0 0.1) -6.0 2
        iRandomVal = 0  // 0:NO|1:YES   | >1 cancel process 
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_hit_center" "spider") 31.0 (0 0 0 1) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "c_hit_center" 1.45
        WHILE IS_CHAR_PLAYING_ANIM player_actor "c_hit_center"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_hit_center") (fCurrentTime)
            IF fCurrentTime >= 0.333     //frames 10-28 /30
            AND 0.933 >= fCurrentTime
                SET_TIME_SCALE 0.35
                IF iRandomVal = 0
                    //draw indicator
                    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 1.0) (x[0] y[0] z[0])
                    CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (x[1] y[1])
                    GET_FIXED_XY_ASPECT_RATIO (30.0 30.0) (x[1] y[1])
                    USE_TEXT_COMMANDS FALSE
                    DRAW_SPRITE objCrosshair (v1 v2) (x[1] y[1]) (255 255 255 220)
                    CLEO_CALL draw_key_fast_back 0
                ENDIF
                // Press Preview Weap Key while playing 10-28 frame time 
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND iRandomVal = 0
                    iRandomVal = 1
                    GOSUB playKeySound
                ENDIF
            ELSE
                SET_TIME_SCALE 1.0
            ENDIF
            IF fCurrentTime >= 0.967 //frame 29/30
                BREAK
            ENDIF
            //Set Angle
            CLEO_CALL setAngleFromCarToChar 0 veh player_actor
            WAIT 0
        ENDWHILE
    ELSE
        //If is in right side of vehicle
        IF IS_CHAR_PLAYING_ANIM player_actor "c_right_A_01"
            //setSquenceFall
            ATTACH_CAMERA_TO_VEHICLE veh (1.0 -7.80 2.0) (0.5 0.0 0.1) -6.0 2
            iRandomVal = 0  // 0:NO|1:YES   | >1 cancel process 
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_hit_right" "spider") 31.0 (0 0 0 1) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "c_hit_right" 1.45
            WHILE IS_CHAR_PLAYING_ANIM player_actor "c_hit_right"
                CLEO_CALL draw_key_fast_back 0
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_hit_right") (fCurrentTime)
                IF fCurrentTime >= 0.333     //frames 10-28 /30
                AND 0.933 >= fCurrentTime
                    SET_TIME_SCALE 0.35
                    IF iRandomVal = 0
                        //draw indicator
                        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 1.0) (x[0] y[0] z[0])
                        CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (x[1] y[1])
                        GET_FIXED_XY_ASPECT_RATIO (35.0 35.0) (x[1] y[1])
                        USE_TEXT_COMMANDS FALSE
                        DRAW_SPRITE objCrosshair (v1 v2) (x[1] y[1]) (255 255 255 220)
                    ENDIF
                    // Press Preview Weap Key while playing 10-28 frame time 
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND iRandomVal = 0
                        iRandomVal = 1
                        GOSUB playKeySound
                    ENDIF
                ELSE
                    SET_TIME_SCALE 1.0
                ENDIF
                IF fCurrentTime >= 0.967 //frame 29/30
                    BREAK
                ENDIF
                //Set Angle
                CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                WAIT 0
            ENDWHILE
        ELSE
            //If is in left side of vehicle
            IF IS_CHAR_PLAYING_ANIM player_actor "c_left_A_01"
                //setSquenceFall
                ATTACH_CAMERA_TO_VEHICLE veh (1.0 -7.80 2.0) (0.5 0.0 0.1) -6.0 2
                iRandomVal = 0  // 0:NO|1:YES   | >1 cancel process 
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_hit_left" "spider") 31.0 (0 0 0 1) -1
                WAIT 0
                SET_CHAR_ANIM_SPEED player_actor "c_hit_left" 1.45

                WHILE IS_CHAR_PLAYING_ANIM player_actor "c_hit_left"
                    CLEO_CALL draw_key_fast_back 0
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_hit_left") (fCurrentTime)
                    IF fCurrentTime >= 0.333     //frames 10-28 /30
                    AND 0.933 >= fCurrentTime
                        SET_TIME_SCALE 0.35
                        IF iRandomVal = 0
                            //draw indicator
                            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 1.0) (x[0] y[0] z[0])
                            CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (x[1] y[1])
                            GET_FIXED_XY_ASPECT_RATIO (35.0 35.0) (x[1] y[1])
                            USE_TEXT_COMMANDS FALSE
                            DRAW_SPRITE objCrosshair (v1 v2) (x[1] y[1]) (255 255 255 220)
                        ENDIF
                        // Press Preview Weap Key while playing 10-28 frame time 
                        IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                        AND iRandomVal = 0
                            iRandomVal = 1
                            GOSUB playKeySound
                        ENDIF
                    ELSE
                        SET_TIME_SCALE 1.0
                    ENDIF
                    IF fCurrentTime >= 0.967 //frame 29/30
                        BREAK
                    ENDIF
                    //Set Angle
                    CLEO_CALL setAngleFromCarToChar 0 veh player_actor
                    WAIT 0
                ENDWHILE

            ELSE
                iRandomVal = 0  // 0:NO|1:YES   | >1 cancel process 
                RETURN
            ENDIF
        ENDIF
    ENDIF

    IF iRandomVal = 1
        GOSUB REQUEST_Web_Animations
        GOSUB destroyLeftWeb
        GOSUB createLeftWeb
        //if jump-key has been pressed - Return to Center of vehicle
        ATTACH_CAMERA_TO_VEHICLE veh (1.0 -8.80 2.0) (0.5 0.0 0.1) -6.0 2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_hit_front" "spider") 57.0 (0 0 0 1) -1
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_front_veh" "mweb") 57.0 (0 1 1 1) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "c_hit_front" 1.45
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_front_veh") 0

        ATTACH_OBJECT_TO_CAR baseObject veh (0.0 0.0 0.0) (0.0 0.0 0.0)

        GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomVal)
        GOSUB playWebSound

        WHILE IS_CHAR_PLAYING_ANIM player_actor "c_hit_front"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_hit_front") (fCurrentTime)
            SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_front_veh") fCurrentTime
            IF fCurrentTime >= 0.982 // frame 55/56
                BREAK
            ENDIF
            //Set Angle
            CLEO_CALL setAngleFromCarToChar 0 veh player_actor
            WAIT 0
        ENDWHILE
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_idle_Z" "spider") 101.0 (1 0 0 0) -1
        ATTACH_CAMERA_TO_VEHICLE veh (1.0 -5.25 1.55) (0.15 0.0 0.1) 0.0 2 // Center-Right
        WAIT 0
        CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
        GOSUB destroyLeftWeb
        iRandomVal = 1
    ELSE
        //if jump-key has Not been pressed - Fall on floor
        ATTACH_CAMERA_TO_VEHICLE veh (1.0 -9.80 2.0) (0.5 0.0 0.1) -6.0 2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("c_hit_fall" "spider") 26.0 (0 0 0 1) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "c_hit_fall" 1.45

        WHILE IS_CHAR_PLAYING_ANIM player_actor "c_hit_fall"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("c_hit_fall") (fCurrentTime)
            IF fCurrentTime >= 0.962 // frame 25/26
                SET_CHAR_ANIM_PLAYING_FLAG player_actor ("c_hit_fall") 0
                BREAK
            ENDIF
            //Set Angle
            CLEO_CALL setAngleFromCarToChar 0 veh player_actor
            WAIT 0
        ENDWHILE
        GET_CHAR_HEADING player_actor (zAngle)
        SET_CHAR_HEADING player_actor zAngle
        WAIT 0
        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT
        iRandomVal = 2
        //GOTO endSequence_cancel_forced
    ENDIF
RETURN



die_in_mission:
	CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
    //SET_CHAR_COLLISION player_actor TRUE
    DETACH_CHAR_FROM_CAR player_actor
    //SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    CLEAR_CHAR_TASKS player_actor

    SET_CHAR_HEALTH player_actor 0
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
    iRandomVal = FAIL_PURSUIT
RETURN

endSequence:
	CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
	/*IF DOES_VEHICLE_EXIST veh
        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 -5.75 0.5) (x[1] y[1] z[1])
    ENDIF*/
    //SET_CHAR_COLLISION player_actor TRUE
    DETACH_CHAR_FROM_CAR player_actor
    //SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
    IF flag_enemy1_killed = TRUE
    AND flag_enemy2_killed = TRUE
		iRandomVal = SUCCED_PURSUIT
    ELSE
		iRandomVal = ON_GROUND_PURSUIT
    ENDIF
RETURN

endSequence_succeed:
	CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
    //SET_CHAR_COLLISION player_actor TRUE
    /*
    IF IS_CHAR_DEAD player_actor
        DETACH_CHAR_FROM_CAR player_actor
        CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
        SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
        GOSUB mission_failed
    ELSE
    */
        DETACH_CHAR_FROM_CAR player_actor
        CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
        SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    //ENDIF
    IF flag_enemy1_killed = TRUE
    AND flag_enemy2_killed = TRUE
		iRandomVal = SUCCED_PURSUIT
    ELSE
		iRandomVal = ON_GROUND_PURSUIT
    ENDIF
RETURN

endSequence_cancel_forced:
	CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor
    DETACH_CHAR_FROM_CAR player_actor
    //SET_CHAR_COLLISION player_actor TRUE
    CLEO_CALL getBoneOffset 0 player_actor 4 (0.0 0.0 0.0) (x[1] y[1] z[1])
    SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    IF flag_enemy1_killed = TRUE
    AND flag_enemy2_killed = TRUE
		iRandomVal = SUCCED_PURSUIT
    ELSE
		iRandomVal = ON_GROUND_PURSUIT
    ENDIF
RETURN

//-+---------------------------------------------------
in_air_zip_to_point:
    GOSUB destroyTwoWebs
    GOSUB createTwoWebs
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampA" "spider") 13.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "airToLampA" 2.0
    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh 0.0 0.0 1.0 (x[0] y[0] z[0])

    GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomVal)
    GOSUB playWebSound

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("airToLampA")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampA") (fCurrentTime)
        IF fCurrentTime >= 0.98  //0.236   // frame
            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampA") 0
            BREAK
        ENDIF
        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh 0.0 0.0 1.0 (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.001 (x[1] y[1] z[1])
        SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
        CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
        SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle
        IF DOES_OBJECT_EXIST baseObject
            SET_OBJECT_ROTATION baseObject xAngle 0.0 zAngle
        ENDIF
        IF NOT IS_CHAR_REALLY_IN_AIR player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            WAIT 0
            iRandomVal = FALSE
            GOSUB destroyTwoWebs
            RETURN
        ENDIF
        WAIT 0
    ENDWHILE
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fTempVar)
    v1 = 1.0 //1.5
    v2 = fTempVar - v1
    IF v2 > 15.0
        iRandomVal = 0   // anim for long distance
    ELSE
        iRandomVal = 1   // anim for short distance
    ENDIF

    timera = 0
    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.25
        WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.25

            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh 0.0 0.0 1.0 (x[0] y[0] z[0])
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
            GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fTempVar)
            fTempVar -= v1
            CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
            GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
            SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle 

            CLEO_CALL linearInterpolation 0 (v2 v1 fTempVar) (0.0 1.0) (fCurrentTime)
            IF fCurrentTime > 1.0
                fCurrentTime = 1.0
            ENDIF
            SWITCH iRandomVal
                CASE 0  // anim for long distance
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampB" "spider") 34.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampB") fCurrentTime
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampB") 0                                                        
                    BREAK
                CASE 1  // anim for short distance
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampB_B" "spider") 44.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampB_B") fCurrentTime
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampB_B") 0
                    BREAK
            ENDSWITCH
            IF DOES_CHAR_EXIST iWebActor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("LA_airToLampA" "mweb") 44.0 (0 1 1 1) -2
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("LA_airToLampA") fCurrentTime
                SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("LA_airToLampA") 0
            ENDIF
            IF DOES_CHAR_EXIST iWebActorR
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("SH_airToLampA" "mweb") 44.0 (0 1 1 1) -2
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("SH_airToLampA") fCurrentTime
                SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("SH_airToLampA") 0  
            ENDIF
            IF DOES_OBJECT_EXIST baseObject
                IF fCurrentTime > 0.279
                //AND 0.302 > currentTime
                AND IS_OBJECT_ATTACHED baseObject
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                ENDIF
            ENDIF

            GET_CHAR_SPEED player_actor (fRandomVal)
            fRandomVal *= 1.020
            CLAMP_FLOAT fRandomVal 30.0 60.0 (fRandomVal)
            CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) fRandomVal
            //PRINT_FORMATTED_NOW "vel:%.1f" 1 fCharSpeed //DEBUG

            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WAIT 0
                iRandomVal = FALSE
                GOSUB destroyTwoWebs
                RETURN
            ENDIF
            IF timera > 4000
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WAIT 0
                iRandomVal = FALSE
                GOSUB destroyTwoWebs
                RETURN
            ENDIF
        ENDWHILE

    ENDIF
    iRandomVal = TRUE
    GOSUB destroyTwoWebs
RETURN

draw_car_indicator:
    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 1.0) (x[0] y[0] z[0])
    CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (x[1] y[1])
    GET_FIXED_XY_ASPECT_RATIO (35.0 35.0) (x[1] y[1])
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE objCrosshair (v1 v2) (x[1] y[1]) (255 255 255 220)

    //GET_FIXED_XY_ASPECT_RATIO 120.0 60.0 (x[1] y[1])
    x[1] = 90.00
    y[1] = 56.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idTip1 (50.0 400.0) (x[1] y[1]) (255 255 255 200)
    IF IS_PC_USING_JOYPAD
        iRandomVal = 703  //~k~~PED_CYCLE_WEAPON_LEFT~
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iRandomVal 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ELSE
        iRandomVal = 705  //~h~Q
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iRandomVal 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ENDIF
RETURN

draw_target_vehicle:
    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 1.0) (x[0] y[0] z[0])
    CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (x[1] y[1])
    GET_FIXED_XY_ASPECT_RATIO (100.0 100.0) (x[1] y[1])
    USE_TEXT_COMMANDS FALSE
    DRAW_SPRITE idTargetVeh (v1 v2) (x[1] y[1]) (255 255 255 220)
RETURN

createTwoWebs:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (-1.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (1.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (-0.15 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.15 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (fRandomVal)
        SET_OBJECT_HEADING baseObject fRandomVal
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598
        UNLOAD_SPECIAL_CHARACTER 9
    ENDIF
RETURN

destroyTwoWebs:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_CHAR_EXIST iWebActorR
        DELETE_CHAR iWebActorR
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

createLeftWeb:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        GET_CHAR_HEADING player_actor (fRandomVal)
        SET_OBJECT_HEADING baseObject fRandomVal        
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598
        UNLOAD_SPECIAL_CHARACTER 9
    ENDIF
RETURN

destroyLeftWeb:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN


playWebSound:
    REMOVE_AUDIO_STREAM iSfx[1]
    //GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    SWITCH iRandomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (iSfx[1])
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                SET_AUDIO_STREAM_STATE iSfx[1] 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (iSfx[1])
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                SET_AUDIO_STREAM_STATE iSfx[1] 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (iSfx[1])
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                SET_AUDIO_STREAM_STATE iSfx[1] 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (iSfx[1])
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                SET_AUDIO_STREAM_STATE iSfx[1] 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN

playKeySound:
    CONST_INT SFX_VOLUME_ADDRESS 0xB5FCCC
    REMOVE_AUDIO_STREAM iSfx[0]
    IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_a.mp3" (iSfx[0])
        SET_AUDIO_STREAM_STATE iSfx[0] 1 
        READ_MEMORY SFX_VOLUME_ADDRESS 4 FALSE (fRandomVal)      //0xB5FCCC (float 0.0-1.0)
        fRandomVal *= 0.95
        SET_AUDIO_STREAM_VOLUME iSfx[0] fRandomVal
    ENDIF
RETURN

//-+---------------------------------------------------

draw_on_screen_distance_char_veh:
    CONVERT_3D_TO_SCREEN_2D x[0] y[0] z[0] TRUE TRUE (v1 v2) (fRandomVal fRandomVal)
    GET_DISTANCE_BETWEEN_COORDS_3D x[0] y[0] z[0] x[1] y[1] z[1] (fRandomVal)
    iRandomVal =# fRandomVal
    GOSUB GUI_TextFormat_Text
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iRandomVal    //~1~ m

    IF NOT LOCATE_CHAR_DISTANCE_TO_CAR player_actor veh 25.0
        CREATE_FX_SYSTEM_ON_CAR SP_CC veh (0.0 0.0 0.6) 4 (iRandomVal)
        PLAY_AND_KILL_FX_SYSTEM iRandomVal

        GET_FIXED_XY_ASPECT_RATIO 20.0 20.0 (x[1] y[1])
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE FALSE
        v2 -= 8.0
        DRAW_SPRITE idMapIcon5 (v1 v2) (x[1] y[1]) (255 255 255 235)
    ENDIF

/*
CLEO_CALL storeIconItem 0 var
CLEO_CALL getIconItem 0 (var)
CLEO_CALL storeParticleItem 0 var
CLEO_CALL getParticleItem 0 (var)
*/

RETURN

draw_on_screen_fx:


RETURN

GUI_TextFormat_Text:
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
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

REQUEST_Web_Animations:
    IF NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "mweb"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Web_Animations

load_and_create_entities:
    CONST_INT idTip1 10
    CONST_INT objCrosshair 20
    CONST_INT idTargetVeh 31
    CONST_INT idMapIcon5  30

    LOAD_TEXTURE_DICTIONARY spaim
    LOAD_SPRITE objCrosshair "ilock"
    LOAD_SPRITE idTip1 "htip1"
    LOAD_SPRITE idMapIcon5 "mk5"    //Crime
    LOAD_SPRITE idTargetVeh "tn_veh"

    REQUEST_MODEL 1598
    REQUEST_MODEL ADMIRAL

    REQUEST_MODEL COLT45    //346   -1
    REQUEST_MODEL SILENCED  //347   -2
    REQUEST_MODEL DESERT_EAGLE  //348   -3
    REQUEST_MODEL CHROMEGUN //349   -4
    REQUEST_MODEL SAWNOFF   //350   -5
    REQUEST_MODEL SHOTGSPA  //351   -6
    REQUEST_MODEL MICRO_UZI //352   -7
    REQUEST_MODEL MP5LNG    //353   -8
    REQUEST_MODEL TEC9      //372   -9
    LOAD_SPECIAL_CHARACTER 1 gng1   //-1
    LOAD_SPECIAL_CHARACTER 2 gng2   //-2
    LOAD_SPECIAL_CHARACTER 3 gng3   //-3
    LOAD_SPECIAL_CHARACTER 4 gng4   //-4
    LOAD_ALL_MODELS_NOW

    CLEAR_AREA v1 v2 zAngle 50.0 1
    CREATE_CAR ADMIRAL (v1 v2 zAngle) (veh)
        SET_CAR_HEALTH veh 1000
        SET_CAR_PROOFS veh FALSE TRUE TRUE TRUE FALSE
        SET_CAR_TRACTION veh 2.0
        CAR_WANDER_RANDOMLY veh
        SET_CAN_BURST_CAR_TYRES veh TRUE
        SET_CAR_DRIVING_STYLE veh DRIVINGMODE_AVOIDCARS
        SET_CAR_CRUISE_SPEED veh 60.0
        SET_CAR_AVOID_LEVEL_TRANSITIONS veh TRUE
        SET_CAR_CAN_GO_AGAINST_TRAFFIC veh TRUE
        MARK_CAR_AS_CONVOY_CAR veh TRUE
    
    CREATE_OBJECT_NO_SAVE 1598 0.0 1.0 -1.0 FALSE FALSE (obj)
        SET_OBJECT_COLLISION obj FALSE
        SET_OBJECT_SCALE obj 0.1
        SET_OBJECT_VISIBLE obj FALSE
        ATTACH_OBJECT_TO_CAR obj veh (0.0 0.0 0.0) (0.0 0.0 0.0)

    LOAD_CHAR_DECISION_MAKER 4 (iTempVar)
    SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 0 PEDTYPE_MISSION1 PEDTYPE_MISSION1
    CLEO_CALL storeDecisionMaker 0 iTempVar

    //DRIVER
    GOSUB generate_random_ped
    GOSUB generate_random_weapon
    CREATE_CHAR_INSIDE_CAR veh PEDTYPE_MISSION1 iRandomVal (char[0])
        SET_CHAR_DECISION_MAKER char[0] iTempVar
        SET_CHAR_HEALTH char[0] 300
        GIVE_WEAPON_TO_CHAR char[0] LRStick 999999
        SET_CHAR_IS_CHRIS_CRIMINAL char[0] TRUE
        //SET_CHAR_WANTED_BY_POLICE char[0] TRUE
        SET_CHAR_ACCURACY char[0] 45
        SET_CHAR_SHOOT_RATE char[0] 65
        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char[0] 100.0     //Sets the range within which the char responds to events

    //PASSENGER
    GOSUB generate_random_ped
    GOSUB generate_random_weapon
    CREATE_CHAR_AS_PASSENGER veh PEDTYPE_MISSION1 iRandomVal 0 (char[1])
        SET_CHAR_DECISION_MAKER char[1] iTempVar
        SET_CHAR_HEALTH char[1] 300
        GIVE_WEAPON_TO_CHAR char[1] LRStick 999999
        SET_CHAR_IS_CHRIS_CRIMINAL char[1] TRUE
        //SET_CHAR_WANTED_BY_POLICE char[1] TRUE
        SET_CHAR_ACCURACY char[1] 35
        SET_CHAR_SHOOT_RATE char[0] 75
        TASK_DRIVE_BY char[1] player_actor -1 (0.0 0.0 0.0) 100.0 (8 1) 45  //4 1 
        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char[1] 100.0     //Sets the range within which the char responds to events

    ADD_BLIP_FOR_CAR veh (iBlipVeh)
    CHANGE_BLIP_COLOUR iBlipVeh RED
    CHANGE_BLIP_SCALE iBlipVeh 3 
    CHANGE_BLIP_DISPLAY iBlipVeh BLIP_ONLY

    UNLOAD_SPECIAL_CHARACTER 1
    UNLOAD_SPECIAL_CHARACTER 2
    UNLOAD_SPECIAL_CHARACTER 3
    UNLOAD_SPECIAL_CHARACTER 4
    MARK_MODEL_AS_NO_LONGER_NEEDED 1598
    MARK_MODEL_AS_NO_LONGER_NEEDED ADMIRAL
    MARK_MODEL_AS_NO_LONGER_NEEDED COLT45
    MARK_MODEL_AS_NO_LONGER_NEEDED SILENCED
    MARK_MODEL_AS_NO_LONGER_NEEDED DESERT_EAGLE
    MARK_MODEL_AS_NO_LONGER_NEEDED CHROMEGUN
    MARK_MODEL_AS_NO_LONGER_NEEDED SAWNOFF
    MARK_MODEL_AS_NO_LONGER_NEEDED SHOTGSPA
    MARK_MODEL_AS_NO_LONGER_NEEDED MICRO_UZI
    MARK_MODEL_AS_NO_LONGER_NEEDED MP5LNG
    MARK_MODEL_AS_NO_LONGER_NEEDED TEC9
    //REMOVE_DECISION_MAKER iTempVar
RETURN

generate_random_weapon:
    GENERATE_RANDOM_INT_IN_RANGE 1 10 (LRStick)  //9 weapons    
    SWITCH LRStick  //RECICLED VARS
        CASE 1
            LRStick = WEAPONTYPE_PISTOL
            BREAK
        CASE 2
            LRStick = WEAPONTYPE_PISTOL_SILENCED
            BREAK
        CASE 3
            LRStick = WEAPONTYPE_DESERT_EAGLE
            BREAK
        CASE 4
            LRStick = WEAPONTYPE_SHOTGUN
            BREAK
        CASE 5
            LRStick = WEAPONTYPE_SAWNOFF_SHOTGUN
            BREAK
        CASE 6
            LRStick = WEAPONTYPE_SPAS12_SHOTGUN
            BREAK
        CASE 7
            LRStick = WEAPONTYPE_MICRO_UZI
            BREAK
        CASE 8
            LRStick = WEAPONTYPE_MP5
            BREAK
        DEFAULT
            LRStick = WEAPONTYPE_TEC9
            BREAK
    ENDSWITCH
RETURN

generate_random_ped:
    GENERATE_RANDOM_INT_IN_RANGE 1 5 (iRandomVal) //4 peds
    SWITCH iRandomVal
        CASE 1
            iRandomVal = SPECIAL01
            BREAK
        CASE 2
            iRandomVal = SPECIAL02
            BREAK
        CASE 3
            iRandomVal = SPECIAL03
            BREAK
        DEFAULT
            iRandomVal = SPECIAL04
            BREAK
    ENDSWITCH
RETURN
}
SCRIPT_END


//-+------------------------ SET ------------------------
{
//CLEO_CALL setAngleFromCarToChar 0 car char
setAngleFromCarToChar:
    LVAR_INT iVeh
    LVAR_INT iChar
    LVAR_FLOAT fZAngle
    GET_CAR_HEADING iVeh (fZAngle)
    SET_CHAR_HEADING iChar fZAngle
CLEO_RETURN 0
}
//-+------------------------ GET ------------------------
{
//CLEO_CALL linearInterpolation 0 (x0 x1 x) (y0 y1) (y)
linearInterpolation:
LVAR_FLOAT x0   //Min 
LVAR_FLOAT x1   //Max
LVAR_FLOAT x    //Mid
LVAR_FLOAT y0   //Min
LVAR_FLOAT y1   //Max
LVAR_FLOAT result[2]
result[0] = (x1 - x0)
IF result[0] = 0.0
    result[1] = (y0 + y1)
    result[1] /= 2.0
ELSE
    y1 = (y1 - y0)
    x1 = (x1 - x0)
    x = (x - x0)
    result[0] = (y1 / x1)
    result[1] = (result[0] * x)
    result[1] = (result[1] + y0)
ENDIF
CLEO_RETURN 0 result[1]     //y0 + (x - x0) * (y1 - y0)/(x1 - x0) 
}
{
/*
0 > LRStick -> Right || LRStick > 0 -> Left
0 > UDStick -> Up    || UDStick > 0 -> Down
*/
//CLEO_CALL getDataJoystick 0 (LRStick UDStick)
getDataJoystick:
    LVAR_INT LRStick UDStick empty
    GET_POSITION_OF_ANALOGUE_STICKS 0 LRStick UDStick empty empty
CLEO_RETURN 0 LRStick UDStick
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
//CLEO_CALL getBoneOffset 0 player_actor /*bone*/ 6 /*offset*/ 0.0 0.0 0.0 /*store*/ (x y z)
getBoneOffset:
    LVAR_INT hChar
    LVAR_INT iBone
    LVAR_FLOAT xOffset yOffset zOffset
    LVAR_FLOAT v1 v2 v3
    LVAR_INT v4
    LVAR_INT pChar iBoneID
    iBoneID = iBone
    IF DOES_CHAR_EXIST hChar
        v1 = xOffset
        v2 = yOffset
        v3 = zOffset
        GET_PED_POINTER hChar (pChar)
        GET_VAR_POINTER v1 (v4)
        CALL_METHOD 0x5E01C0 /*struct*/pChar /*params*/3 /*pop*/ 0 /*bIncludeAnim*/1 /*BoneID*/iBoneID /*vOffset*/v4    // CPed__getBonePositionWithOffset
    ELSE
        PRINT_FORMATTED_NOW "ERR: wrong use of 'getBoneOffset'" 1000
        WAIT 1000
        v1 = 0.0
        v2 = 0.0
        v3 = 0.0
    ENDIF
CLEO_RETURN 0 v1 v2 v3
 /*
    BONE_PELVIS1             = 1
    BONE_PELVIS              = 2
    BONE_SPINE1              = 3
    BONE_UPPERTORSO          = 4
    BONE_NECK                = 5
    BONE_HEAD2               = 6
    BONE_HEAD1               = 7
    BONE_HEAD                = 8
    BONE_RIGHTUPPERTORSO     = 21
    BONE_RIGHTSHOULDER       = 22
    BONE_RIGHTELBOW          = 23
    BONE_RIGHTWRIST          = 24
    BONE_RIGHTHAND           = 25
    BONE_RIGHTTHUMB          = 26
    BONE_LEFTUPPERTORSO      = 31
    BONE_LEFTSHOULDER        = 32
    BONE_LEFTELBOW           = 33
    BONE_LEFTWRIST           = 34
    BONE_LEFTHAND            = 35
    BONE_LEFTTHUMB           = 36
    BONE_LEFTHIP             = 41
    BONE_LEFTKNEE            = 42
    BONE_LEFTANKLE           = 43
    BONE_LEFTFOOT            = 44
    BONE_RIGHTHIP            = 51
    BONE_RIGHTKNEE           = 52
    BONE_RIGHTANKLE          = 53
    BONE_RIGHTFOOT           = 54
*/
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
{
//CLEO_CALL getXangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*xAngle*/fSyncAngle)
getXangleBetweenPoints:
    LVAR_FLOAT xA yA zA xB yB zB    //in
    LVAR_FLOAT pointY pointZ
    LVAR_FLOAT xAngle
    GET_DISTANCE_BETWEEN_COORDS_2D xA yA xB yB (pointY)
    pointZ = (zA - zB)
    GET_HEADING_FROM_VECTOR_2D pointY pointZ (xAngle)
    //CLEO_CALL ATAN 0 /*2D_Coord*/ pointY pointZ /*angle*/ (xAngle)
    xAngle -= 270.0
    xAngle *= -1.0
CLEO_RETURN 0 xAngle
}
{
//CLEO_CALL setCharVelocityTo 0 iPlayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer    //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
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
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer //in
    LVAR_FLOAT xVel yVel zVel amplitude //in
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer xVel yVel zVel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL draw_key_command_sequence 0
draw_key_command_sequence:
    LVAR_INT iKey
    LVAR_FLOAT x[2] y[2]
    IF IS_PC_USING_JOYPAD
        iKey = 250    // ~k~~PED_FIREWEAPON~
    ELSE
        iKey = 230    // ~h~LMB
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 40.0 25.0 (x[0] y[0])
    USE_TEXT_COMMANDS FALSE
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (480.0 336.0) (x[0] y[0]) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) iKey 18 0.0 -5.0
CLEO_RETURN 0
}
{
//CLEO_CALL draw_key_fast_back 0
draw_key_fast_back:
    LVAR_INT iKey
    LVAR_FLOAT x[2] y[2]
    IF IS_PC_USING_JOYPAD
        iKey = 521    // ~k~~PED_CYCLE_WEAPON_LEFT~
    ELSE
        iKey = 522    // ~h~Q
    ENDIF
    GET_FIXED_XY_ASPECT_RATIO 40.0 25.0 (x[0] y[0])
    USE_TEXT_COMMANDS FALSE
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (480.0 336.0) (x[0] y[0]) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) iKey 18 0.0 -5.0
CLEO_RETURN 0
}

//-+--------------------------------------------
{
//CLEO_CALL storeIconItem 0 var
storeIconItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_IconItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getIconItem 0 (var)
getIconItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_IconItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeParticleItem 0 var
storeParticleItem:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ParticleItem pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getParticleItem 0 (var)
getParticleItem:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_ParticleItem (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeDecisionMaker 0 var
storeDecisionMaker:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER decision_maker_bytes4 pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getDecisionMaker 0 (var)
getDecisionMaker:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER decision_maker_bytes4 (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
//----------------------------------------------

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

decision_maker_bytes4:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_IconItem:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ParticleItem:
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
