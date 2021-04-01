// by J16D
// Thug Hideouts
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Enemy Levels
CONST_FLOAT mass_lvl0 70.0      //default  ||---^--- Air Combo
CONST_FLOAT mass_lvl1 100.0
CONST_FLOAT mass_lvl2 120.0     //---^--- Swing Kick || Combo X4
CONST_FLOAT mass_lvl3 150.0
CONST_FLOAT mass_lvl4 200.0

CONST_INT delay_restart_mission 20000   //20 sec

SCRIPT_START
{
SCRIPT_NAME m_th
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod flag_player_on_mission isInMainMenu
LVAR_INT iDecisionHate iBlip iEventBlip
LVAR_INT char_thug counter kills_counter iRandomVal iRandomVal2
LVAR_FLOAT fPedMass

GET_PLAYER_CHAR 0 player_actor

start:
ADD_SPRITE_BLIP_FOR_COORD -1691.91 1104.3081 94.0312 RADAR_SPRITE_GANG_B (iEventBlip) //RADAR_SPRITE_WAYPOINT
//ADD_SPRITE_BLIP_FOR_CONTACT_POINT -1691.91 1104.3081 94.0312 RADAR_SPRITE_NONE (iEventBlip)

WHILE TRUE
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor -1691.91 1104.3081 94.0312 1.25 1.25 1.25 TRUE
                //IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor 0.0 0.0 0.0 1.25 1.25 1.25 TRUE
                    IF flag_player_on_mission = 0
                        REMOVE_BLIP iEventBlip
                        BREAK
                    ELSE    
                        PRINT_FORMATTED_NOW "Finish your current mission first!" 2000
                        WAIT 2000
                    ENDIF
                ENDIF
            ENDIF
        ELSE
            IF DOES_BLIP_EXIST iEventBlip
                REMOVE_BLIP iEventBlip
            ENDIF
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT            
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

//start mission
flag_player_on_mission = 3  //3:criminal
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

GOSUB sub_Fade_600ms_and_Lock_Controls
GOSUB create_enemys
TASK_TOGGLE_DUCK player_actor TRUE

SKIP_CUTSCENE_START
    CAMERA_RESET_NEW_SCRIPTABLES
    SET_FIXED_CAMERA_POSITION -1698.9778 1120.7269 87.0 0.0 0.0 0.0 
    POINT_CAMERA_AT_POINT -1697.9586 1136.3705 86.0 2 
    GOSUB sub_Fade_in_500ms
    PRINT_NOW SP_A01 4500 1
    WAIT 4500
    GOSUB sub_Fade_out_500ms
    SET_FIXED_CAMERA_POSITION -1644.989 1177.3522 98.0173 0.0 0.0 0.0 
    POINT_CAMERA_AT_POINT -1665.83 1162.2998 89.7195 2 
    GOSUB sub_Fade_in_500ms
    PRINT_NOW SP_A02 4500 1
    WAIT 4500
    GOSUB sub_Fade_out_500ms
    SET_FIXED_CAMERA_POSITION -1702.0538 1187.0404 94.9048 0.0 0.0 0.0 
    POINT_CAMERA_AT_POINT -1690.355 1174.5048 92.7196 2 
    GOSUB sub_Fade_in_500ms
    PRINT_NOW SP_A03 6000 1
    WAIT 6000
SKIP_CUTSCENE_END
GOSUB sub_Fade_out_500ms
GOSUB sub_Fade_500ms_and_Restore_Controls
kills_counter = 0

main_loop:
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE

            counter = 0
            WHILE 9 >= counter
                CLEO_CALL get_stored_char 0 counter (char_thug)
                IF DOES_CHAR_EXIST char_thug
                    IF IS_CHAR_DEAD char_thug
                        CLEO_CALL get_stored_marker 0 counter (iBlip)
                        REMOVE_BLIP iBlip
                        CLEO_CALL store_marker 0 counter 0x0
                        MARK_CHAR_AS_NO_LONGER_NEEDED char_thug
                        CLEO_CALL store_char 0 counter 0x0
                        kills_counter ++
                    ENDIF
                ENDIF
                counter ++
            ENDWHILE
            IF kills_counter >= 10
                GOTO mission_passed
            ENDIF
            IF HAS_CHAR_BEEN_ARRESTED player_actor
            OR IS_PLAYER_DEAD 0
            OR NOT IS_CHAR_HEALTH_GREATER player_actor 0
                GOTO mission_failed
            ENDIF
            IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor (-1669.8311 1144.74 86.6016) 250.0   //center of stage
                GOTO mission_failed
            ENDIF

            IF isInMainMenu = 1     //1:true 0: false
                WHILE isInMainMenu = 1     //1:true 0: false
                    GOSUB readVars
                    WAIT 0
                ENDWHILE
                WHILE GET_FADING_STATUS
                    WAIT 0
                ENDWHILE
                WAIT 1000
            ENDIF
        ELSE
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            GOTO mission_failed
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

mission_passed:
    REMOVE_DECISION_MAKER iDecisionHate
    WAIT 0
    flag_player_on_mission = 0
    SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

    kills_counter *= 15
    iRandomVal = 100
    iRandomVal2 = 0
    iRandomVal2 = iRandomVal + kills_counter
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 5 iRandomVal2 iRandomVal kills_counter    //{id} {total xp} {mission xp} {combat xp}
        WAIT 2000
    ENDIF
    SET_CLEO_SHARED_VAR varStatusLevelChar iRandomVal2   //set value of +250

    GET_CLEO_SHARED_VAR varPcampProgress (iRandomVal)
    iRandomVal ++
    SET_CLEO_SHARED_VAR varPcampProgress iRandomVal
    WRITE_INT_TO_INI_FILE iRandomVal "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_pcamp"

    WAIT delay_restart_mission
GOTO start

mission_failed:
    //clear
    counter = 0
    WHILE 9 >= counter
        CLEO_CALL get_stored_char 0 counter (char_thug)
        IF DOES_CHAR_EXIST char_thug
            DELETE_CHAR char_thug
            CLEO_CALL get_stored_marker 0 counter (iBlip)
            REMOVE_BLIP iBlip
        ENDIF
        CLEO_CALL store_marker 0 counter 0x0
        CLEO_CALL store_char 0 counter 0x0
        counter ++
    ENDWHILE
    REMOVE_DECISION_MAKER iDecisionHate
    WAIT 0
    WHILE NOT IS_PLAYER_PLAYING 0
        WAIT 0
    ENDWHILE
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
    flag_player_on_mission = 0
    SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 3  //{id}
        WAIT 2000
    ENDIF
    USE_TEXT_COMMANDS FALSE
    WAIT delay_restart_mission
GOTO start

sub_Fade_out_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN

sub_Fade_in_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN 

sub_Fade_600ms_and_Lock_Controls:
    CLEAR_PRINTS 
    CLEAR_HELP
    USE_TEXT_COMMANDS FALSE
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 600 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
    SWITCH_WIDESCREEN TRUE
    SET_PLAYER_CONTROL 0 FALSE
    USE_TEXT_COMMANDS FALSE
RETURN

sub_Fade_500ms_and_Restore_Controls:
    CLEAR_PRINTS 
    CLEAR_HELP
    USE_TEXT_COMMANDS FALSE
    SWITCH_WIDESCREEN FALSE
    RESTORE_CAMERA_JUMPCUT
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
    SET_PLAYER_CONTROL 0 TRUE
RETURN

create_enemys:
    LOAD_SPECIAL_CHARACTER 1 thug1 
    LOAD_SPECIAL_CHARACTER 2 thug2
    LOAD_SPECIAL_CHARACTER 3 thug3
    LOAD_SPECIAL_CHARACTER 4 thug4
    REQUEST_MODEL COLT45    //346   -1
    REQUEST_MODEL SILENCED  //347   -2
    REQUEST_MODEL DESERT_EAGLE  //348   -3
    REQUEST_MODEL CHROMEGUN //349   -4
    REQUEST_MODEL SAWNOFF   //350   -5
    REQUEST_MODEL SHOTGSPA  //351   -6
    REQUEST_MODEL MICRO_UZI //352   -7
    REQUEST_MODEL MP5LNG    //353   -8
    REQUEST_MODEL AK47      //355   -9
    REQUEST_MODEL TEC9      //372   -10
    LOAD_ALL_MODELS_NOW

    /*
    0 - "m_empty.ped"
    1 - "m_norm.ped"
    2 - "m_tough.ped"
    3 - "m_weak.ped"
    4 - "m_steal.ped"
    */
    LOAD_CHAR_DECISION_MAKER 4 (iDecisionHate)
    SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 0 PEDTYPE_MISSION1 PEDTYPE_MISSION1

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 0
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1698.6819 1127.2992 86.6016) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0

        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 40
        SET_CHAR_ACCURACY char_thug 30

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 15.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1698.3851 1117.7393 86.6016 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1684.0898 1116.6372 86.6016 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1683.3862 1129.6735 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1698.582 1136.0569 86.6016 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 1    //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 1
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1649.2991 1136.7148 86.5937) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 50
        SET_CHAR_ACCURACY char_thug 20

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 15.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1649.0149 1129.494 86.5937 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1672.6896 1130.7982 86.4861 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1672.2252 1143.196 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1649.9282 1143.3505 86.5937 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 1     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 2     //beside stair
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1694.5657 1170.8976 75.6554) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 30
        SET_CHAR_ACCURACY char_thug 50

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 10.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1699.3973 1171.0394 75.6554 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1698.649 1141.6716 75.6562 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1692.0955 1142.1012 75.6562 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1694.5657 1170.8976 75.6554 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 1     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 3 //down level
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1675.6033 1164.6887 86.4922) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 50
        SET_CHAR_ACCURACY char_thug 40

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 10.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1669.775 1164.4895 86.6016 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1674.3361 1164.7688 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1676.5773 1170.338 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1686.0588 1170.225 86.4922 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1676.5773 1170.338 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1674.3361 1164.7688 86.4922 "NONE" "NONE"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 1     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 4     //down metal base
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1650.902 1170.7266 86.5937) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 30
        SET_CHAR_ACCURACY char_thug 50

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 10.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1681.8866 1171.0469 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1681.822 1175.1694 86.6025 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1690.1141 1175.1654 82.0447 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1681.822 1175.1694 86.6025 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1681.8866 1171.0469 86.4922 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1650.902 1170.7266 86.5937 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 1     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 5     //up metal base
        CREATE_CHAR PEDTYPE_MISSION1 SPECIAL02 (-1656.3177 1171.1821 94.3633) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 50
        SET_CHAR_ACCURACY char_thug 20

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 5.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1660.4424 1171.1586 94.3633 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1656.3177 1171.1821 94.3633 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1653.8029 1168.1649 94.3633 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 10.0 1     //gossip

        GIVE_WEAPON_TO_CHAR char_thug WEAPONTYPE_AK47 99999
        SET_CURRENT_CHAR_WEAPON char_thug WEAPONTYPE_AK47

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 6     //center base 
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1662.1475 1152.2502 86.5937) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 40
        SET_CHAR_ACCURACY char_thug 40

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 15.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1650.188 1152.2352 86.5937 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1662.1475 1152.2502 86.5937 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1662.9115 1160.5542 86.5937 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1650.5955 1159.7738 86.5937 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1662.9115 1160.5542 86.5937 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1662.1475 1152.2502 86.5937 "NONE" "NONE"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 15.0 2     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip



        /// these enemys are in first level
        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 7 //side ped
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1660.6641 1169.7325 75.6641) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 50
        SET_CHAR_ACCURACY char_thug 20

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 8.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1650.022 1167.5667 75.6641 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1650.7262 1161.2994 75.6641 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1663.4991 1161.0304 75.661 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1664.0121 1154.1305 75.6525 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1652.2919 1153.4297 75.6641 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1664.0121 1154.1305 75.6525 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1663.4991 1161.0304 75.661 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1650.7262 1161.2994 75.6641 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1650.022 1167.5667 75.6641 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1660.6641 1169.7325 75.6641 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 10.0 2     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip


        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 8 //center ped
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1660.6112 1126.3444 75.6632) (char_thug)
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 30
        SET_CHAR_ACCURACY char_thug 40

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 8.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1660.6112 1126.3444 75.6632 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1661.1565 1128.8835 75.6632 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1650.8799 1129.9279 75.6632 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1654.2883 1130.3009 75.6632 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1654.7383 1144.7512 75.6641 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1654.9586 1135.5588 75.6632 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1661.652 1135.1593 75.6632 "NONE" "NONE"
            EXTEND_PATROL_ROUTE -1660.6112 1126.3444 75.6632 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 10.0 2     //gossip

        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip



        GOSUB generate_random_ped
        GOSUB generate_random_weapon
    counter = 9 //vigilant ped
        CREATE_CHAR PEDTYPE_MISSION1 iRandomVal (-1645.8336 1154.5962 75.707) (char_thug)
        SET_CHAR_HEADING char_thug 0.4546
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN char_thug TRUE TRUE TRUE
        CLEO_CALL set_char_mass 0 char_thug fPedMass //default=70.0
        SET_CHAR_DECISION_MAKER char_thug iDecisionHate

        SET_CHAR_HEALTH char_thug 200
        SET_CHAR_SHOOT_RATE char_thug 30
        SET_CHAR_ACCURACY char_thug 40

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE char_thug 5.0     //Sets the range within which the char responds to events
        FLUSH_PATROL_ROUTE 
            EXTEND_PATROL_ROUTE -1645.8336 1154.5962 75.707 "ROADCROSS" "PED"
            EXTEND_PATROL_ROUTE -1645.728 1142.2817 75.707 "ROADCROSS" "PED"
        TASK_FOLLOW_PATROL_ROUTE char_thug 4 3  //4:walk_mode | 3:route_mode
        SET_INFORM_RESPECTED_FRIENDS char_thug 20.0 2     //gossip

        iRandomVal2 = WEAPONTYPE_AK47
        GIVE_WEAPON_TO_CHAR char_thug iRandomVal2 99999
        SET_CURRENT_CHAR_WEAPON char_thug iRandomVal2

        CLEO_CALL store_char 0 counter char_thug

        ADD_BLIP_FOR_CHAR char_thug (iBlip)
        SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
        CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
        CLEO_CALL store_marker 0 counter iBlip

    UNLOAD_SPECIAL_CHARACTER 1
    UNLOAD_SPECIAL_CHARACTER 2
    UNLOAD_SPECIAL_CHARACTER 3
    UNLOAD_SPECIAL_CHARACTER 4
    MARK_MODEL_AS_NO_LONGER_NEEDED AK47
    MARK_MODEL_AS_NO_LONGER_NEEDED COLT45
    MARK_MODEL_AS_NO_LONGER_NEEDED SILENCED
    MARK_MODEL_AS_NO_LONGER_NEEDED DESERT_EAGLE
    MARK_MODEL_AS_NO_LONGER_NEEDED CHROMEGUN
    MARK_MODEL_AS_NO_LONGER_NEEDED SAWNOFF
    MARK_MODEL_AS_NO_LONGER_NEEDED SHOTGSPA
    MARK_MODEL_AS_NO_LONGER_NEEDED MICRO_UZI
    MARK_MODEL_AS_NO_LONGER_NEEDED MP5LNG
    MARK_MODEL_AS_NO_LONGER_NEEDED TEC9
RETURN

generate_random_weapon:
    GENERATE_RANDOM_INT_IN_RANGE 1 11 (iRandomVal2)  //10 weapons    
    SWITCH iRandomVal2  //RECICLED VARS
        CASE 1
            iRandomVal2 = WEAPONTYPE_PISTOL
            BREAK
        CASE 2
            iRandomVal2 = WEAPONTYPE_PISTOL_SILENCED
            BREAK
        CASE 3
            iRandomVal2 = WEAPONTYPE_DESERT_EAGLE
            BREAK
        CASE 4
            iRandomVal2 = WEAPONTYPE_SHOTGUN
            BREAK
        CASE 5
            iRandomVal2 = WEAPONTYPE_SAWNOFF_SHOTGUN
            BREAK
        CASE 6
            iRandomVal2 = WEAPONTYPE_SPAS12_SHOTGUN
            BREAK
        CASE 7
            iRandomVal2 = WEAPONTYPE_MICRO_UZI
            BREAK
        CASE 8
            iRandomVal2 = WEAPONTYPE_MP5
            BREAK
        CASE 9
            iRandomVal2 = WEAPONTYPE_AK47
            BREAK
        DEFAULT
            iRandomVal2 = WEAPONTYPE_TEC9
            BREAK
    ENDSWITCH
RETURN

generate_random_ped:
    GENERATE_RANDOM_INT_IN_RANGE 1 5 (iRandomVal) //4 peds
    SWITCH iRandomVal
        CASE 1
            iRandomVal = SPECIAL01
            fPedMass = mass_lvl1
            BREAK
        CASE 2
            iRandomVal = SPECIAL02
            fPedMass = mass_lvl2
            BREAK
        CASE 3
            iRandomVal = SPECIAL03
            fPedMass = mass_lvl0
            BREAK
        DEFAULT
            iRandomVal = SPECIAL04
            fPedMass = mass_lvl2
            BREAK
    ENDSWITCH
RETURN
}
SCRIPT_END


{
//CLEO_CALL set_char_mass 0 scplayer 70.0   //default=70.0
set_char_mass:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMass    //in
    LVAR_INT p
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (p)
        p += 0x8C       // CPed.CPhysical.fMass
        WRITE_MEMORY p 4 fMass FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL store_marker 0 counter iMarker
store_marker: 
    LVAR_INT counter iMarker  //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER marker_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    WRITE_MEMORY pActiveItem 4 iMarker FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL get_stored_marker 0 counter (iMarker)
get_stored_marker:
    LVAR_INT counter //in
    LVAR_INT pActiveItem pTempVar iMarker
    GET_LABEL_POINTER marker_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iMarker)
CLEO_RETURN 0 iMarker
}

{
//CLEO_CALL store_char 0 counter iChar
store_char: 
    LVAR_INT counter iChar  //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER char_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    IF DOES_CHAR_EXIST iChar
        WRITE_MEMORY pActiveItem 4 iChar FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}

{
//CLEO_CALL get_stored_char 0 counter (ichar)
get_stored_char:
    LVAR_INT counter //in
    LVAR_INT pActiveItem pTempVar iChar
    GET_LABEL_POINTER char_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iChar)
    IF NOT DOES_CHAR_EXIST iChar
        ichar = -1
    ENDIF
CLEO_RETURN 0 iChar
}

char_buffer_bytes40:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
ENDDUMP

marker_buffer_bytes40:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
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
