// by J16D
// On ground/jump script (Temporary removed -Cinematic Jump)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_mb
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod
LVAR_INT baseObject iWebActor iObjArms sfx
LVAR_INT taskJump randomVal iTempVar
LVAR_FLOAT x y z fVelX fVelY fVelZ
LVAR_FLOAT fCharSpeed fAmplitude fZAnglePlayerAir currentTime

GET_PLAYER_CHAR 0 player_actor
SET_PLAYER_JUMP_BUTTON player FALSE

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 // TRUE
            IF GOSUB is_not_char_playing_car_missions_anims

                IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 1 0 0 0)   //solid car actor object particle
                    //----------------------------------- if $player is on Air
                    IF CLEO_CALL isActorInWater 0 player_actor
                        GOSUB resetCharInWater
                    ENDIF
                ELSE
                    //----------------------------------- if $player is on Ground
                    GOSUB reset_char_falling_ground

                    IF IS_BUTTON_PRESSED PAD1 CROSS  // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE        // ~k~~PED_FIREWEAPON~
                    AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall"

                        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("fall_land_F")
                           
                            IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                                IF GOSUB does_skill_Charge_Jump_enabled
                                    GET_CHAR_MOVE_STATE player_actor (randomVal)
                                    IF randomVal = MOVE_STATE_SPRINTING
                                        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")
                                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_C")
                                            GOSUB assign_task_charge_jump
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF

                        ELSE
                            iTempVar = 0
                            WHILE IS_CHAR_PLAYING_ANIM player_actor ("fall_land_F")
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_land_F") (currentTime)
                                IF currentTime >= 0.34
                                    IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                                    AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~                                
                                        iTempVar = 1    //enable quick recovery
                                        BREAK
                                    ENDIF
                                ENDIF
                                WAIT 0
                            ENDWHILE
                            IF iTempVar = 1    //enable quick recovery
                                IF NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_C")
                                    
                                    IF GOSUB does_skill_Quick_Recovery_enabled
                                        GOSUB assign_task_jump_rolling_ground
                                        WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                            WAIT 0
                                        ENDWHILE
                                    ENDIF

                                ENDIF
                            ENDIF

                        ENDIF

                    ELSE

                        IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                        AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~  //compatibility evade-anims
                        
                            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_B")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_B")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("punch_to_air_p_a")

                                IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
                                //IF GOSUB is_not_char_playing_dodge_anims

                                    GOSUB assign_task_jump
                                    WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                        WAIT 0
                                    ENDWHILE

                                ENDIF

                            ENDIF
                        ENDIF

                    ENDIF
                    /*  
                    //Temporary removed
                    IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                    AND IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                    AND IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~

                        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_B")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
                            GOSUB assign_task_cinematic_jump
                        ENDIF

                    ENDIF
                    */

                ENDIF                
            
            ENDIF
        ELSE
            // Release files
            REMOVE_ANIMATION "spider"
            REMOVE_ANIMATION "mweb"
            REMOVE_AUDIO_STREAM sfx
            SET_PLAYER_JUMP_BUTTON player TRUE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop

//-+---GOSUB HELPERS
//-+--------------------------MAIN----------------------------
does_skill_Quick_Recovery_enabled:
    GET_CLEO_SHARED_VAR varSkill3c1 (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

does_skill_Charge_Jump_enabled:
    GET_CLEO_SHARED_VAR varSkill3c2 (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

reset_char_falling_ground:
    //-+--Reset $player if is falling on Ground
    IF GOSUB is_not_char_playing_swing_anims
        IF GOSUB is_not_char_playing_webstrike_anims
            IF GOSUB is_not_char_playing_dodge_anims
                IF GOSUB is_char_playing_anims_near_ground
                    GOSUB resetCharInGround
                ENDIF
                //GET_CHAR_SPEED player_actor fCharSpeed
                //PRINT_FORMATTED_NOW "speed:(%f)" 1 fCharSpeed
            ENDIF
        ENDIF
    ENDIF
RETURN

assign_task_charge_jump:
    //-+-- Charge Jump
    GOSUB REQUEST_SwingAnimations
    GET_CHAR_SPEED player_actor fCharSpeed
    fVelY = 2.5 //5
    fVelZ = 1.5 //4
    fAmplitude = fCharSpeed * 2.0
    CLAMP_FLOAT fAmplitude 0.0 15.0 (fAmplitude)
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\jump1.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1    //play 
    ENDIF
    SHAKE_PAD PAD1 150 1200
    OPEN_SEQUENCE_TASK taskJump
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("jump_launch_B" "spider") 10.0 (0 1 1 0) -1
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("jump_glide_B" "spider") 39.0 (0 1 1 0) -2
    CLOSE_SEQUENCE_TASK taskJump
    PERFORM_SEQUENCE_TASK player_actor taskJump
    WAIT 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_B")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_B") (currentTime)
        IF currentTime >= 0.90
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    WAIT 100
    CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
    WAIT 50
    CLEAR_SEQUENCE_TASK taskJump
RETURN

assign_task_jump_rolling_ground:
    //-+-- Jump while rolling in ground
    GOSUB REQUEST_SwingAnimations
    GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
    GET_CHAR_SPEED player_actor fCharSpeed
    fVelY = 2.5
    fVelZ = 2.25
    fAmplitude = 10.0
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_F" "spider") 40.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "jump_glide_F" 1.65
    CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
    SET_CHAR_HEADING player_actor fZAnglePlayerAir
    WAIT 35
    GOSUB playSFXSound
RETURN

assign_task_jump:
    //-+-- Jump
    GOSUB REQUEST_SwingAnimations
    GET_CHAR_SPEED player_actor fCharSpeed
    fVelY = fCharSpeed * 0.2
    fVelZ = 2.0
    fAmplitude = 6.0
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_A" "spider") 6.0 (0 1 1 0) -1
    WAIT 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_A") (currentTime)
        IF currentTime >= 0.98
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_A" "spider") 23.0 (0 1 1 0) -2
    CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
    WAIT 50
RETURN

/* Temporary removed
assign_task_cinematic_jump:
    //-+-- Cinematic Jump
    GOSUB REQUEST_SwingAnimations
    GOSUB REQUEST_WebAnimations
    GET_CHAR_SPEED player_actor fCharSpeed
    IF fCharSpeed > 7.0
        GOSUB destroyWeb
        GOSUB createWeb
        fVelY = (1.5 * fCharSpeed)  //0.8
        fVelZ = (1.6 * fCharSpeed)  //0.9
        fAmplitude = 1.05
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x y z)
        z += 0.11
        SET_CHAR_COORDINATES_SIMPLE player_actor x y z
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
        CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_C" "spider") 65.0 (0 1 1 0) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "jump_launch_C" 1.5
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_C")
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_C") (currentTime)
            IF currentTime > 0.4
            AND 0.5 > currentTime
                CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ)  fAmplitude
            ENDIF
            IF currentTime > 0.85
                fAmplitude = 2.0 //1.70 //2.5
                CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ)  fAmplitude
                BREAK
            ENDIF
            IF currentTime >= 0.188
            AND 0.844 > currentTime
                GOSUB attachWeb
            ELSE    
                DETACH_OBJECT baseObject (0.1 0.0 0.0) 0
            ENDIF
            WAIT 0
        ENDWHILE
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_C" "spider") 15.0 (0 1 1 0) -2
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "jump_glide_C" 1.5
        WAIT 50
        GOSUB destroyWeb
    ENDIF
RETURN
*/
//-+----------------------------------------------------------

//-+-----------------------GET--------------------------
readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod toggleSpiderMod
RETURN

REQUEST_SwingAnimations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_SwingAnimations

REQUEST_WebAnimations:
    IF NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "mweb"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_WebAnimations
//-+----------------------------------------------------------

//-+-----------------------SET--------------------------
resetCharInGround:
    GOSUB REQUEST_SwingAnimations
    CLEAR_CHAR_TASKS player_actor
    //GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    randomVal = 2
    GOSUB TASK_PLAY_land
    WAIT 0
RETURN

resetCharInWater:
    //-+-- RESET CHAR IN WATER
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    WHILE  CLEO_CALL isActorInWater 0 player_actor
        WAIT 0
    ENDWHILE
RETURN

TASK_PLAY_land:
    IF  IS_BUTTON_PRESSED 0 CROSS   // ~k~~PED_SPRINT~
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_land_F" "spider") 1000.0 (0 1 1 0) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "fall_land_F" 1.35
    ELSE
        SWITCH randomVal
            CASE 0
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_land_A" "spider") 8.0 (0 1 1 0) -1
                WAIT 0
                BREAK
            CASE 1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_land_B" "spider") 21.0 (0 1 1 0) -1
                WAIT 0
                BREAK
            CASE 2
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_land_C" "spider") 26.0 (0 1 1 0) -1
                WAIT 0
                SET_CHAR_ANIM_SPEED player_actor "fall_land_C" 2.0
                BREAK
        ENDSWITCH
        /*
        GET_CHAR_SPEED player_actor (fCharSpeed)
        IF fCharSpeed > 30.0
            CLEO_CALL setSmokeFX 0 player_actor (0.0 0.0 -0.85) 30.0
        ENDIF
        */
    ENDIF
RETURN

playSFXSound:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1    //play 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN
//-+----------------------------------------------------------

//-+-----------------------CHECKS--------------------------
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

is_not_char_playing_webstrike_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_a")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out_b")
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_not_char_playing_swing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_A")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_B")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_C")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_D")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_E")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_F")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_A")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_B")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_C")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_D")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_E")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_F")
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_not_char_playing_car_missions_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_01")
            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_front")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_fall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_center")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_left")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_right")
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_char_playing_anims_near_ground:
    //-+-- RESET CHAR FALL IN GROUND
    IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A")
    OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B")
    OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C")
    OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D")
    OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E")
    OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F")
        RETURN_TRUE
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("fall_glide_C")
        OR IS_CHAR_PLAYING_ANIM player_actor ("fall_glide_D")
        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_A")
        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_B")
        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_C")
        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_D")
        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_E")
            RETURN_TRUE
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A_R")
            OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B_R")
            OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C_R")
            OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D_R")
            OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E_R")
            OR IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F_R")
                RETURN_TRUE
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor ("swing_fail_A")
                OR IS_CHAR_PLAYING_ANIM player_actor ("swing_fail_B")   
                OR IS_CHAR_PLAYING_ANIM player_actor ("swing_fail_C")
                OR IS_CHAR_PLAYING_ANIM player_actor ("swing_fail_D")
                OR IS_CHAR_PLAYING_ANIM player_actor ("swing_fail_E")                                
                OR IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_C")
                    RETURN_TRUE
                ELSE
                    IF IS_CHAR_PLAYING_ANIM player_actor ("fall_glide_A")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("fall_glide_B")
                    //IF IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")
                    //OR IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_B")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("webZip_A")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("webZip_A_R")   
                    OR IS_CHAR_PLAYING_ANIM player_actor ("webZip_B")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("webZip_B_R")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
                        RETURN_TRUE
                    ELSE
                        IF IS_CHAR_PLAYING_ANIM player_actor ("run_wall_jump_R_A")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("run_wall_jump_L_A")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("run_wall_jump_R_B")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("run_wall_jump_L_B")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_Build_A")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("jump_Build_B")                                             
                            RETURN_TRUE
                        ELSE
                            IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_l_in")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_left")   
                            OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_l_out")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_r_in")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_right")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_r_out")
                                RETURN_TRUE
                            ELSE
                                IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_b_in")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_back")   
                                OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_b_out")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_f_in")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_front")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_f_out")
                                    RETURN_TRUE
                                ELSE
                                    IF IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_L")
                                    OR IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_R")
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
        ENDIF
    ENDIF
RETURN
//-+----------------------------------------------------------

/* Temporary removed (Cinematic Jump)
//-+-----------------------WEB CODE--------------------------
attachWeb:
    IF DOES_CHAR_EXIST iWebActor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webGoUp" "mweb") 65.0 (0 1 1 1) -2
        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webGoUp") currentTime
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webGoUp") 1          
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
RETURN

createWeb:
    REQUEST_MODEL 1598
    LOAD_SPECIAL_CHARACTER 9 wmt
    LOAD_ALL_MODELS_NOW
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_OBJECT_EXIST baseObject
        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.1
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
        SET_OBJECT_HEADING baseObject fZAnglePlayerAir        
    ENDIF
    MARK_MODEL_AS_NO_LONGER_NEEDED 1598
    UNLOAD_SPECIAL_CHARACTER 9
RETURN

destroyWeb:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DETACH_OBJECT baseObject (0.0 0.0 0.0) 0
        SET_OBJECT_COORDINATES baseObject (0.0 0.0 -15.0)
        DELETE_OBJECT baseObject
    ENDIF
RETURN
*/
//-+----------------------------------------------------------
}
SCRIPT_END

//-+---CALL SCM HELPERS
{
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer   //in
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
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT scplayer
    LVAR_FLOAT x y z
    LVAR_INT isSolid isCar isActor isObject isParticle
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
//CLEO_CALL isActorInWater 0 player_actor
isActorInWater:
    LVAR_INT scplayer   //in
    LVAR_FLOAT x y z height
    IF DOES_CHAR_EXIST scplayer
        GET_CHAR_COORDINATES scplayer (x y z)
        GET_WATER_HEIGHT_AT_COORDS x y TRUE (height)
        IF height > z
            RETURN_TRUE
            CLEO_RETURN 0
        ENDIF
    ENDIF
    RETURN_FALSE
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




/* Removed
    //----------------------------------- Powers
    //IRON ARMS
    IF IS_KEY_PRESSED VK_KEY_U
        WHILE IS_KEY_PRESSED VK_KEY_U
            WAIT 0
        ENDWHILE
        GOSUB REQUEST_SwingAnimations
        GOSUB createIronArms
        TASK_PICK_UP_OBJECT player_actor iObjArms (0.0 -0.05 -0.15) (Back_of_Neck 16) "NULL" "NULL" 1

        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("animation_16" "spider") 26.0 (0 1 1 0) -1
        WAIT 5
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("animation_16")
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("animation_16") (currentTime)
            IF currentTime > 0.99
                BREAK
            ELSE
                ///GOSUB attachIronArms
                IF DOES_OBJECT_EXIST iObjArms
                    PLAY_OBJECT_ANIM iObjArms ("animation_15" "spider") 60.0 1 0
                    SET_OBJECT_ANIM_CURRENT_TIME iObjArms "animation_15" currentTime
                    SET_OBJECT_ANIM_SPEED iObjArms "animation_15" 0.0
                ENDIF                            
            ENDIF
            WAIT 0
        ENDWHILE
        timera = 0
        WHILE 5000 > timera
            WAIT 0
        ENDWHILE
        CLEAR_CHAR_TASKS player_actor
        GOSUB destroyIronArms

    ENDIF
    //PLAY_OBJECT_ANIM iObjArms ("animation_15" "spider") 60.0 1 0

//-+-----------------------IRON ARMS--------------------------
attachIronArms:
    IF DOES_OBJECT_EXIST iObjArms
        PLAY_OBJECT_ANIM iObjArms ("animation_15" "spider") 60.0 1 0
        SET_OBJECT_ANIM_CURRENT_TIME iObjArms "animation_15" currentTime
        SET_OBJECT_ANIM_SPEED iObjArms "animation_15" 0.0
    ENDIF
RETURN

createIronArms:
    REQUEST_MODEL 6011 //ID_OBJECT //Iron Spider Arms
    LOAD_ALL_MODELS_NOW
    IF NOT DOES_OBJECT_EXIST iObjArms
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 2.0 -30.0 (x y z)
        CREATE_OBJECT_NO_SAVE 6011 x y z FALSE FALSE (iObjArms)
        SET_OBJECT_COLLISION iObjArms FALSE
        SET_OBJECT_RECORDS_COLLISIONS iObjArms FALSE
        SET_OBJECT_PROOFS iObjArms (1 1 1 1 1)
    ENDIF
    MARK_MODEL_AS_NO_LONGER_NEEDED 6011 //ID_OBJECT
RETURN

destroyIronArms:
    IF DOES_OBJECT_EXIST iObjArms
        DETACH_OBJECT iObjArms (0.0 0.0 0.0) 0
        SET_OBJECT_COORDINATES iObjArms (0.0 0.0 -15.0)
        DELETE_OBJECT iObjArms
    ENDIF
RETURN
//-+----------------------------------------------------------
*/




/*
CONST_INT Chest               1 
CONST_INT Neck                2 
CONST_INT Left_Shoulder       3 
CONST_INT Right_Shoulder      4 
CONST_INT Left_Hand           5 
CONST_INT Right_Hand          6 
CONST_INT Root                7 
CONST_INT Left_Foot           9 
CONST_INT Right_Foot          10
CONST_INT Right_Knee          11
CONST_INT Left_Elbow          13
CONST_INT Right_Elbow         14
CONST_INT Left_Clavicle       15
CONST_INT Right_Clavicle      16
CONST_INT Back_of_Neck        17
CONST_INT Mouth_or_Chin       18
*/
/*
// bones
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
Bone IDs
ID  Bone
1  Spine
2  Head
3  Left upper arm
4  Right upper arm
5  Left hand
6  Right hand
7  Left thigh
8  Right thigh
9  Left foot
10  Right foot
11  Right calf
12  Left calf
13  Left forearm
14  Right forearm
15  Left clavicle (shoulder)
16  Right clavicle (shoulder)
17  Neck
18  Jaw 
*/
