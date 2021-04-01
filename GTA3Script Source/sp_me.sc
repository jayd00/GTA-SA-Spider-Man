// by J16D
// Melee Combo X4 | Air Combo x4 | Swing kick
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Enemy Levels
CONST_FLOAT mass_lvl0 70.0      //default  ||---^--- Air Combo
CONST_FLOAT mass_lvl1 100.0
CONST_FLOAT mass_lvl2 120.0     //---^--- Swing Kick || Combo X4
CONST_FLOAT mass_lvl3 150.0
CONST_FLOAT mass_lvl4 200.0

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_me
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu
LVAR_INT i p iChar sfx fx_system iWebActor baseObject
LVAR_FLOAT fCurrentTime x[2] y[2] z[2] zAngle fTempVar
LVAR_INT iTempVar iTempVar2 counter anim_seq
LVAR_FLOAT timed_Key

GET_PLAYER_CHAR 0 player_actor
timed_Key = 0.0
GOSUB REQUEST_FightAnimations

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                
                IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (1 1 1 0 0)
                    IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -6.0) (1 1 1 0 0)
                    ELSE
                        IF GOSUB does_skill_Swing_Kick_enabled
                            // Swing Kick
                            IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                                ADD_TIMED_VAL_TO_FLOAT_LVAR (timed_Key) 1.0
                                IF timed_Key > 25.0
                                    CLEAR_CHAR_TASKS player_actor
                                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_kick_1" "dildo") 74.0 (0 1 1 0) -1
                                    WAIT 0
                                    SET_CHAR_ANIM_SPEED player_actor "swing_kick_1" 1.5
                                    GOSUB assign_air_swing_kick_task
                                    WHILE IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                                        WAIT 0
                                    ENDWHILE
                                    timed_Key = 0.0
                                    WAIT 0
                                ENDIF
                            ELSE
                                timed_Key = 0.0
                            ENDIF
                        ENDIF
                    ENDIF
                    //SET_DARKNESS_EFFECT TRUE 200
                ELSE
                    //COMBO x4  | variants x4
                    IF IS_CHAR_PLAYING_ANIM player_actor "dildo_3"
                        WHILE IS_CHAR_PLAYING_ANIM player_actor "dildo_3"
                            GET_CHAR_ANIM_CURRENT_TIME player_actor "dildo_3" (fCurrentTime)
                            IF fCurrentTime > 0.469 // frame 15/32
                                IF IS_BUTTON_PRESSED PAD1 CIRCLE        // ~k~~PED_FIREWEAPON~
                                    GOSUB assign_task_final_combo
                                    GOSUB delay_fight_combo
                                    IF GOSUB is_char_in_front_of_player
                                        GOSUB set_z_angle_char
                                        DAMAGE_CHAR iChar 10 TRUE
                                        GOSUB play_sfx_final_hit_ground_combo
                                        CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.2) 4 (fx_system)
                                        PLAY_AND_KILL_FX_SYSTEM fx_system
                                        SHAKE_CAM 50
                                        IF IS_PC_USING_JOYPAD
                                            SHAKE_PAD PAD1 100 1000
                                        ENDIF
                                        IF NOT IS_CHAR_DEAD iChar
                                            IF GOSUB does_skill_Bunker_Buster_enabled
                                                GOSUB assign_task_fall_floor
                                            ELSE
                                                CLEO_CALL get_char_mass 0 iChar (fTempVar)
                                                IF mass_lvl2 >= fTempVar
                                                    GOSUB assign_task_fall_floor
                                                ENDIF
                                            ENDIF
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                            WAIT 0
                        ENDWHILE
                    ENDIF

                    //Punch to air
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        ADD_TIMED_VAL_TO_FLOAT_LVAR (timed_Key) 1.0
                        IF timed_Key > 30.0
                            IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
                                CLEAR_CHAR_TASKS player_actor
                                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("ground_to_air" "dildo") 53.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED player_actor "ground_to_air" 1.4
                                GOSUB create_fx_on_ground
                                GOSUB assign_ground_to_air_combo_task
                                WHILE IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                                    WAIT 0
                                ENDWHILE
                                timed_Key = 0.0
                                WAIT 0
                            ENDIF
                        ENDIF
                    ELSE
                        timed_Key = 0.0
                    ENDIF
                
                ENDIF

            ENDIF
        ELSE
            GOSUB destroyWeb
            REMOVE_AUDIO_STREAM sfx
            REMOVE_ANIMATION "dildo"
            REMOVE_ANIMATION "mweb"
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

does_skill_Swing_Kick_enabled:
    GET_CLEO_SHARED_VAR varSkill3 (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

does_skill_Bunker_Buster_enabled:
    GET_CLEO_SHARED_VAR varSkill3b (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

//-+-----------------------AIR_SWING_KICK-----------------------------
assign_air_swing_kick_task:
    IF IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
        GOSUB REQUEST_webAnimations
        GOSUB destroyWeb
        GOSUB createWeb
        
        IF DOES_CHAR_EXIST iWebActor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_swing_kick_1" "mweb") 41.0 (0 1 1 1) -2
            WAIT 0
            SET_CHAR_ANIM_SPEED iWebActor "w_swing_kick_1" 1.5
        ENDIF
        IF DOES_OBJECT_EXIST baseObject
            ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
        ENDIF

        WHILE IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
            GET_CHAR_ANIM_CURRENT_TIME player_actor "swing_kick_1" (fCurrentTime)
            IF fCurrentTime >= 0.315    // frame 23/73
            AND 0.411 >= fCurrentTime   // frame 30/73
                IF GOSUB is_char_in_front_of_player
                    SHAKE_CAM 80
                    IF IS_PC_USING_JOYPAD
                        SHAKE_PAD PAD1 100 1000
                    ENDIF
                    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
                        IF CLEO_CALL is_char_gang_ped 0 iChar
                            MARK_CHAR_AS_NEEDED iChar
                        ENDIF
                    ENDIF
                    GOSUB set_z_angle_char
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.7 0.0 (x[0] y[0] z[0])
                    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                    CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.8) 4 (fx_system)
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                    GOSUB play_sfx_final_hit_ground_combo
                    DAMAGE_CHAR iChar 10 TRUE
                    IF NOT IS_CHAR_DEAD iChar
                        IF GOSUB does_skill_Bunker_Buster_enabled
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("swing_kick_hit_1" "dildo") 51.0 (0 1 1 0) -1
                            WAIT 0
                            SET_CHAR_ANIM_SPEED iChar "swing_kick_hit_1" 1.5
                        ELSE
                            CLEO_CALL get_char_mass 0 iChar (fTempVar)
                            IF mass_lvl2 >= fTempVar //default
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("swing_kick_hit_1" "dildo") 51.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "swing_kick_hit_1" 1.5
                            ENDIF
                        ENDIF
                    ELSE
                        CLEAR_CHAR_TASKS iChar
                    ENDIF
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
        IF IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "swing_kick_1" (fCurrentTime)
                IF fCurrentTime >= 0.548    //40/73
                    GOSUB destroyWeb
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
    ELSE
        GOSUB destroyWeb
    ENDIF
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF IS_CHAR_PLAYING_ANIM iChar "swing_kick_hit_1"
            WHILE IS_CHAR_PLAYING_ANIM iChar "swing_kick_hit_1"
                GET_CHAR_ANIM_CURRENT_TIME iChar "swing_kick_hit_1" (fCurrentTime)
                IF fCurrentTime >= 0.960    //48/50
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
            WAIT 50
        ENDIF
    ENDIF
    GOSUB destroyWeb
RETURN

destroyWeb:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

createWeb:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598  
        LOAD_SPECIAL_CHARACTER 9 wms
        LOAD_ALL_MODELS_NOW
        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1582

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) (iWebActor)
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle
        UNLOAD_SPECIAL_CHARACTER 9
    ENDIF
RETURN
//-+-------------------------------------------------------------

//-+-----------------------AIR_COMBO_A-----------------------------
assign_ground_to_air_combo_task:
    IF IS_CHAR_PLAYING_ANIM player_actor "ground_to_air"
        WHILE IS_CHAR_PLAYING_ANIM player_actor "ground_to_air"
            GET_CHAR_ANIM_CURRENT_TIME player_actor "ground_to_air" (fCurrentTime)
            IF fCurrentTime >= 0.308    // frame 16/52
            AND 0.346 >= fCurrentTime   // frame 18/52
                IF GOSUB is_char_in_front_of_player
                    SHAKE_CAM 80
                    IF IS_PC_USING_JOYPAD
                        SHAKE_PAD PAD1 100 1000
                    ENDIF
                    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
                        IF CLEO_CALL is_char_gang_ped 0 iChar
                            MARK_CHAR_AS_NEEDED iChar
                        ENDIF
                    ENDIF
                    GOSUB set_z_angle_char
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.0 0.0 (x[0] y[0] z[0])
                    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                    DAMAGE_CHAR iChar 5 TRUE
                    iTempvar = 0
                    GOSUB play_sfx_air_combos_hit
                    CREATE_FX_SYSTEM_ON_CHAR SP_HIT_U iChar (0.0 0.0 0.8) 4 (fx_system)
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                    IF NOT IS_CHAR_DEAD iChar
                        IF GOSUB does_skill_Bunker_Buster_enabled
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("ground_to_air_hit" "dildo") 40.0 (0 1 1 0) -1
                            WAIT 0
                            SET_CHAR_ANIM_SPEED iChar "ground_to_air_hit" 1.4
                        ELSE
                            CLEO_CALL get_char_mass 0 iChar (fTempVar)
                            IF mass_lvl0 >= fTempVar //default
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("ground_to_air_hit" "dildo") 40.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "ground_to_air_hit" 1.4
                            ENDIF
                        ENDIF
                    ELSE
                        CLEAR_CHAR_TASKS iChar
                        RETURN
                    ENDIF
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
        WAIT 0
        IF DOES_CHAR_EXIST iChar
            IF NOT IS_CHAR_PLAYING_ANIM iChar "ground_to_air_hit"
                CLEAR_CHAR_TASKS iChar
                RETURN
            ENDIF
        ENDIF
        //Combo 1 - Jump Hit
        IF IS_CHAR_PLAYING_ANIM player_actor "ground_to_air"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "ground_to_air"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "ground_to_air" (fCurrentTime)
                IF fCurrentTime >= 0.750    // frame 39/52
                AND 0.788 >= fCurrentTime   // frame 41/52
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_1" "dildo") 29.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_1" 1.45
                    ELSE
                        GOTO fail_combo_air_0
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_1"
            iTempVar = 1
            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_1"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_1" (fCurrentTime)
                IF fCurrentTime >= 0.500    //14/28
                    IF iTempVar = 1
                        GET_CHAR_HEALTH iChar (iTempVar2)
                        IF 10 >= iTempVar2
                            iTempVar = 5    //finish hit -sfx
                        ELSE
                            iTempVar = 1    //normal hit -sfx
                            DAMAGE_CHAR iChar 5 TRUE
                        ENDIF
                        GOSUB play_sfx_air_combos_hit
                        CLEO_CALL getActorBonePos 0 iChar 6 (x[0] y[0] z[0])    //6: BONE_HEAD
                        CREATE_FX_SYSTEM SP_HIT_U x[0] y[0] z[0] 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                        SWITCH iTempVar
                            CASE 1
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_1" "dildo") 21.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_hit_1" 1.45
                                BREAK
                            CASE 5
                                CLEAR_CHAR_TASKS player_actor
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_1" "dildo") 42.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_1" 1.45

                                CLEAR_CHAR_TASKS iChar
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_fail" "dildo") 30.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_fail" 1.45
                                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_fail"
                                        GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_fail" (fCurrentTime)
                                        IF fCurrentTime >= 0.966   //28/29
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                                DAMAGE_CHAR iChar 30 TRUE
                                WAIT 0
                                RETURN
                                BREAK
                        ENDSWITCH
                        iTempVar = 2
                    ENDIF
                ENDIF
                //Combo 2
                IF fCurrentTime >= 0.893    //25/28
                AND 1.000 > fCurrentTime   //28/28
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_2" "dildo") 22.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_2" 1.45
                    ELSE
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_1" "dildo") 41.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_1" 1.45
                        GOTO fail_combo_air_1
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_2"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_2"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_2" (fCurrentTime)
                IF fCurrentTime >= 0.286    //6/21
                    IF iTempVar = 2
                        GET_CHAR_HEALTH iChar (iTempVar2)
                        IF 10 >= iTempVar2
                            iTempVar = 5    //finish hit -sfx
                        ELSE
                            iTempVar = 2    //normal hit -sfx
                            DAMAGE_CHAR iChar 5 TRUE
                        ENDIF
                        GOSUB play_sfx_air_combos_hit
                        CLEO_CALL getActorBonePos 0 iChar 6 (x[0] y[0] z[0])    //6: BONE_HEAD
                        CREATE_FX_SYSTEM SP_HIT_R x[0] y[0] z[0] 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                        SWITCH iTempVar
                            CASE 2
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_2" "dildo") 22.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_hit_2" 1.45
                                BREAK
                            CASE 5
                                CLEAR_CHAR_TASKS player_actor
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_2" "dildo") 39.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_2" 1.45

                                CLEAR_CHAR_TASKS iChar
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_finish_1" "dildo") 48.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_finish_1" 1.45
                                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_finish_1"
                                        GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_finish_1" (fCurrentTime)
                                        IF fCurrentTime >= 0.958   //46/48
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                                DAMAGE_CHAR iChar 30 TRUE
                                RETURN
                                BREAK
                        ENDSWITCH
                        iTempVar = 3
                    ENDIF
                ENDIF
                //Combo 3
                IF fCurrentTime >= 0.857    //18/21
                AND 1.000 > fCurrentTime   //21/21
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_3" "dildo") 14.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_3" 1.45
                    ELSE
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_2" "dildo") 39.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_2" 1.45
                        GOTO fail_combo_air_2
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_3"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_3"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_3" (fCurrentTime)
                IF fCurrentTime >= 0.462    //6/13
                    IF iTempVar = 3
                        GET_CHAR_HEALTH iChar (iTempVar2)
                        IF 10 >= iTempVar2
                            iTempVar = 5    //finish hit -sfx
                        ELSE
                            iTempVar = 3    //normal hit -sfx
                            DAMAGE_CHAR iChar 5 TRUE
                        ENDIF
                        GOSUB play_sfx_air_combos_hit
                        CLEO_CALL getActorBonePos 0 iChar 6 (x[0] y[0] z[0])    //6: BONE_HEAD
                        CREATE_FX_SYSTEM SP_HIT_L x[0] y[0] z[0] 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                        SWITCH iTempVar
                            CASE 3
                                GENERATE_RANDOM_INT_IN_RANGE 0 2 (iTempVar)
                                //iTempVar = 1
                                IF iTempVar = 0
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_3" "dildo") 22.0 (0 1 1 0) -1
                                    WAIT 0
                                    SET_CHAR_ANIM_SPEED iChar "air_combo_hit_3" 1.45
                                ELSE
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_3b" "dildo") 25.0 (0 1 1 0) -1
                                    WAIT 0
                                    SET_CHAR_ANIM_SPEED iChar "air_combo_hit_3b" 1.45
                                ENDIF
                                BREAK
                            CASE 5
                                CLEAR_CHAR_TASKS player_actor
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_3" "dildo") 48.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_3" 1.45

                                CLEAR_CHAR_TASKS iChar
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_finish_2" "dildo") 48.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_finish_2" 1.45
                                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_finish_2"
                                    GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_finish_2" (fCurrentTime)
                                    IF fCurrentTime >= 0.958   //46/48
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                                DAMAGE_CHAR iChar 30 TRUE
                                WAIT 0
                                RETURN
                                BREAK
                        ENDSWITCH
                        //iTempVar = 4
                        WAIT 10
                    ENDIF
                ENDIF
                //Combo 4
                IF fCurrentTime >= 0.846   //11/13
                AND 1.000 > fCurrentTime   //13/13
                    IF IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                        //IF IS_CHAR_PLAYING_ANIM iChar "air_combo_hit_3"
                        IF iTempVar = 0
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_4" "dildo") 73.0 (0 1 1 0) -1
                            WAIT 0
                            SET_CHAR_ANIM_SPEED player_actor "air_combo_4" 1.45                        
                        ELSE
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_4b" "dildo") 84.0 (0 1 1 0) -1
                            WAIT 0
                            SET_CHAR_ANIM_SPEED player_actor "air_combo_4b" 1.45
                        ENDIF
                        iTempVar = 4
                    ELSE
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("air_combo_fall_3" "dildo") 48.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "air_combo_fall_3" 1.45
                        GOTO fail_combo_air_3
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_4"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_4"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_4" (fCurrentTime)
                IF fCurrentTime >= 0.194    //14/72
                    IF iTempVar = 4
                        GET_CHAR_HEALTH iChar (iTempVar2)
                        IF 30 >= iTempVar2
                            iTempVar = 5    //finish hit -sfx
                        ELSE
                            iTempVar = 4    //normal hit -sfx
                            DAMAGE_CHAR iChar 30 TRUE   //++ damage final hit
                        ENDIF
                        GOSUB play_sfx_air_combos_hit
                        CLEO_CALL getActorBonePos 0 iChar 6 (x[0] y[0] z[0])    //6: BONE_HEAD
                        CREATE_FX_SYSTEM SP_HIT x[0] y[0] z[0] 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                        SWITCH iTempVar
                            CASE 4
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_4" "dildo") 42.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_hit_4" 1.45
                                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_hit_4"
                                        GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_hit_4" (fCurrentTime)
                                        IF fCurrentTime >= 0.951   //39/41
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                                WAIT 50
                                BREAK
                            CASE 5
                                CLEAR_CHAR_TASKS iChar
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_finish_3" "dildo") 41.0 (0 1 1 0) -1
                                WAIT 0
                                SET_CHAR_ANIM_SPEED iChar "air_combo_finish_3" 1.45
                                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_finish_3"
                                        GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_finish_3" (fCurrentTime)
                                        IF fCurrentTime >= 0.950   //38/40
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                                DAMAGE_CHAR iChar 50 TRUE
                                WAIT 0
                                BREAK
                        ENDSWITCH
                        iTempVar = 0
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
            WAIT 50
            RETURN
        ENDIF
        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_4b"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_4b"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_4b" (fCurrentTime)
                IF fCurrentTime >= 0.205    //17/83
                    IF iTempVar = 4
                        GET_CHAR_HEALTH iChar (iTempVar2)
                        IF 30 >= iTempVar2
                            iTempVar = 5    //finish hit -sfx
                        ELSE
                            iTempVar = 4    //normal hit -sfx
                            DAMAGE_CHAR iChar 30 TRUE   //++ damage final hit
                        ENDIF
                        GOSUB play_sfx_air_combos_hit
                        CLEO_CALL getActorBonePos 0 iChar 6 (x[0] y[0] z[0])    //6: BONE_HEAD
                        CREATE_FX_SYSTEM SP_HIT x[0] y[0] z[0] 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system

                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_hit_4b" "dildo") 51.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED iChar "air_combo_hit_4b" 1.45
                        WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_hit_4b"
                            GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_hit_4b" (fCurrentTime)
                            IF fCurrentTime >= 0.960   //48/50
                                BREAK
                            ENDIF
                            WAIT 0
                        ENDWHILE
                        IF iTempVar = 5
                            DAMAGE_CHAR iChar 50 TRUE
                        ELSE
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                        ENDIF
                        WAIT 50
                        iTempVar = 0
                    ENDIF
                ENDIF
                WAIT 0
            ENDWHILE
        ENDIF
        WAIT 50
        RETURN
        
        fail_combo_air_0:
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_fail" "dildo") 30.0 (0 1 1 0) -1
            WAIT 1
            SET_CHAR_ANIM_SPEED iChar "air_combo_fail" 1.45
            IF IS_CHAR_PLAYING_ANIM iChar "air_combo_fail"
                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_fail"
                    GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_fail" (fCurrentTime)
                    IF fCurrentTime >= 0.931    //27/29
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                WAIT 0
            ENDIF
            WAIT 50
        RETURN

        fail_combo_air_1:
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_fail_1" "dildo") 31.0 (0 1 1 0) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED iChar "air_combo_fail_1" 1.45
            IF IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_1"
                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_1"
                    GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_fail_1" (fCurrentTime)
                    IF fCurrentTime >= 0.933    //28/30
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                WAIT 0
            ENDIF
            WAIT 50
        RETURN

        fail_combo_air_2:
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_fail_2" "dildo") 30.0 (0 1 1 1) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED iChar "air_combo_fail_2" 1.45
            IF IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_2"
                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_2"
                    GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_fail_2" (fCurrentTime)
                    IF fCurrentTime >= 0.931    //27/29
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                WAIT 0
            ENDIF
            WAIT 50
        RETURN

        fail_combo_air_3:
            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("air_combo_fail_3" "dildo") 29.0 (0 1 1 1) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED iChar "air_combo_fail_3" 1.45
            IF IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_3"
                WHILE IS_CHAR_PLAYING_ANIM iChar "air_combo_fail_3"
                    GET_CHAR_ANIM_CURRENT_TIME iChar "air_combo_fail_3" (fCurrentTime)
                    IF fCurrentTime >= 0.929    //26/28
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("getup" "ped") 42.0 (0 1 1 0) -1
                WAIT 0
            ENDIF
            WAIT 50
        RETURN

    ENDIF
RETURN

create_fx_on_ground:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.0 0.0 (x[0] y[0] z[0])
    GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
    z[0] += 0.1
    CREATE_FX_SYSTEM SP_PUNCH_GROUND x[0] y[0] z[0] 4 (fx_system)
    PLAY_AND_KILL_FX_SYSTEM fx_system
RETURN

/*
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
*/
//-+-------------------------------------------------------------

//-+-----------------------COMBO_X4-----------------------------
assign_task_final_combo:
    GOSUB REQUEST_FightAnimations
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (iTempVar)
    SWITCH iTempVar
        CASE 0
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dildo_5" "dildo") 38.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dildo_5" "dildo") 38.0 (0 1 1 0) -1
            BREAK
        CASE 1
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dildo_6" "dildo") 42.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dildo_6" "dildo") 42.0 (0 1 1 0) -1
            BREAK
        CASE 2
            //TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dildo_7" "dildo") 34.0 (0 1 1 0) -1 0 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dildo_7" "dildo") 34.0 (0 1 1 0) -1
            BREAK
        CASE 3
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dildo_4" "dildo") 37.0 (0 1 1 0) -1
            BREAK
    ENDSWITCH
    WAIT 5
RETURN

delay_fight_combo:
    IF IS_CHAR_PLAYING_ANIM player_actor "dildo_5"
        WHILE IS_CHAR_PLAYING_ANIM player_actor "dildo_5"
            GET_CHAR_ANIM_CURRENT_TIME player_actor "dildo_5" (fCurrentTime)
            IF fCurrentTime >= 0.459 // frame 17/37
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor "dildo_6"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "dildo_6"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "dildo_6" (fCurrentTime)
                IF fCurrentTime >= 0.366 // frame 15/41
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor "dildo_7"
                WHILE IS_CHAR_PLAYING_ANIM player_actor "dildo_7"
                    GET_CHAR_ANIM_CURRENT_TIME player_actor "dildo_7" (fCurrentTime)
                    IF fCurrentTime >= 0.519 // frame 14/33
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor "dildo_4"
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "dildo_4"
                        GET_CHAR_ANIM_CURRENT_TIME player_actor "dildo_4" (fCurrentTime)
                        IF fCurrentTime >= 0.417 // frame 15/36
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                ENDIF
            ENDIF
        ENDIF
    ENDIF
RETURN

assign_task_fall_floor:
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    SWITCH iTempVar
        CASE 0
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dildo_hit_5" "dildo") 33.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            BREAK
        CASE 1
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dildo_hit_6" "dildo") 19.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            BREAK
        CASE 2
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dildo_hit_7" "dildo") 46.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            BREAK
        CASE 3
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dildo_hit_4" "dildo") 27.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            BREAK
    ENDSWITCH
    PERFORM_SEQUENCE_TASK iChar anim_seq
    SET_CHAR_HEADING iChar zAngle
    WAIT 0
    CLEAR_SEQUENCE_TASK anim_seq
RETURN

is_not_is_playing_anim:
    IF NOT IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
        RETURN_TRUE
        RETURN
    ENDIF
    RETURN_FALSE
RETURN

play_sfx_final_hit_ground_combo:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_b.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1    //play
    ELSE
        ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1136    //SOUND_STAMP_PED 1136
    ENDIF
RETURN
//ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1163    //SOUND_PED_COLLAPSE 1163

play_sfx_air_combos_hit:
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_hit_a.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_a.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.20
            ELSE
                //1135 hit
                //1136 punch B
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 1
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_hit_c.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_c.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.20
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 2
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_hit_d.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_d.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.20
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 3
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_hit_e.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_e.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.20
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 4
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_hit_b.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_b.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.20
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 5
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_finish.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_finish.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                SET_AUDIO_STREAM_VOLUME sfx 0.75
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK            
    ENDSWITCH
RETURN
//-+-------------------------------------------------------------

//-+-------------------------------------------------------------
set_z_angle_char:
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
        SET_CHAR_HEADING iChar zAngle
        zAngle += 180.0
        SET_CHAR_HEADING player_actor zAngle
        zAngle -= 180.0
    ENDIF
RETURN

is_char_in_front_of_player:
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
        IF DOES_CHAR_EXIST iChar
        AND NOT IS_CHAR_DEAD iChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR iWebActor iChar
            IF NOT IS_CHAR_IN_ANY_CAR iChar
            AND NOT IS_CHAR_ON_ANY_BIKE iChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                IF IS_CHAR_ON_SCREEN iChar 

                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.0 0.25 (x[0] y[0] z[0])
                    IF LOCATE_CHAR_ANY_MEANS_3D iChar x[0] y[0] z[0] 1.25 1.25 1.0 FALSE
                        IF GOSUB is_not_is_playing_anim
                            IF NOT IS_CHAR_FALLEN_ON_GROUND iChar
                                //CLEAR_CHAR_PRIMARY_TASKS iChar
                                //CLEAR_CHAR_SECONDARY_TASKS iChar
                                CLEAR_CHAR_TASKS iChar
                                CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                                //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("NULL" "NULL") 4.0 (1 1 1 1) -1
                                RETURN_TRUE 
                                RETURN
                            ENDIF
                        ENDIF
                    ENDIF

                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
    RETURN_FALSE
RETURN

REQUEST_FightAnimations:
    IF NOT HAS_ANIMATION_LOADED "dildo"
        REQUEST_ANIMATION "dildo"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_FightAnimations

REQUEST_webAnimations:
    IF NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "mweb"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_webAnimations

}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT scplayer   //in
    LVAR_FLOAT x y z    //in
    LVAR_INT isSolid isCar isActor isObject isParticle  //in
    LVAR_FLOAT xA yA zA xB yB zB 
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer x y z (xA yA zA)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (xB yB zB)
    IF IS_LINE_OF_SIGHT_CLEAR xB yB zB xA yA zA (isSolid isCar isActor isObject isParticle)
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
            CLEO_RETURN 0
        ELSE
            IF iPedType = PEDTYPE_GANG5  // Da Nang Boys
            OR iPedType = PEDTYPE_GANG6  //Mafia
            OR iPedType = PEDTYPE_GANG7  //Mountain Cloud Triad
            OR iPedType = PEDTYPE_GANG8  //Varrio Los Aztecas
                RETURN_TRUE
                CLEO_RETURN 0
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
CLEO_RETURN 0
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
//CLEO_CALL get_char_mass 0 scplayer (fMass)
get_char_mass:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMass
    LVAR_INT p
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (p)
        p += 0x8C   // CPed.CPhysical.fMass
        READ_MEMORY p 4 FALSE (fMass)
    ENDIF
CLEO_RETURN 0 fMass
}
{
//CLEO_CALL set_char_mass 0 scplayer 70.0   //default=70.0
set_char_mass:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMass    //in
    LVAR_INT p
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (p)
        p += 0x8C   // CPed.CPhysical.fMass
        WRITE_MEMORY p 4 fMass FALSE
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
