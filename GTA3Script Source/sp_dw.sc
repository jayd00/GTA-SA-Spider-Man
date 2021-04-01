// by J16D
// Draw Indicator | webstrike & Stealth
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
//TYPE SPIDER-WEAP
CONST_INT weap_web_shoot 1
CONST_INT weap_concussive_blast 2
CONST_INT weap_impact_web 3
CONST_INT weap_spyder_drone 4
CONST_INT weap_electric_web 5
CONST_INT weap_suspension_matrix 6
CONST_INT weap_web_bomb 7
CONST_INT weap_trip_mine 8

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_dw
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu   //1:true 0: false
LVAR_INT LRStick UDStick is_web_thrown iTempVar randomVal total_dmg
LVAR_INT hInput iChar sfx fx_web anim_seq fx_system
LVAR_FLOAT x[3] y[3] z[3] v1 v2 
LVAR_FLOAT fCurrentTime zAngle xAngle fDistance

GET_PLAYER_CHAR 0 player_actor

CLEO_CALL disableGreenTriangles 0 ()
GOSUB loadTextures
GOSUB REQUEST_Animations
SET_PLAYER_CYCLE_WEAPON_BUTTON player FALSE
SET_PLAYER_FIRE_BUTTON player FALSE

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor

        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                IF GOSUB is_not_player_playing_anims

                    GOSUB draw_indicator_to_target_char
                    GOSUB assign_task_webstrike

                    IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -3.0) (1 1 0 0 0)
                        //in air
                        //not done yet --- web shoot on air
                    ELSE
                        //on ground
                        IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~ 
                            WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~ 
                                GOSUB draw_indicator_to_target_char
                                GET_CLEO_SHARED_VAR varAimSetup (iTempVar) // 0:Manual Aim || 1:Auto Aim
                                IF iTempVar = 1
                                    GOSUB set_auto_aim
                                ELSE
                                    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
                                    IF iTempVar = weap_trip_mine
                                    OR iTempVar = weap_web_bomb
                                    OR iTempVar = weap_suspension_matrix
                                        //draw indicator
                                        CLEO_CALL getXYZAimCoords 0 player_actor 50.0 (x[0] y[0] z[0])
                                        GOSUB draw_crosshair
                                    ELSE
                                        x[0] = 0.0
                                        y[0] = 0.0
                                        z[0] = 0.0
                                    ENDIF
                                ENDIF
                                GOSUB assign_task_webshoot

                                WAIT 0
                            ENDWHILE
                        ELSE
                            x[0] = 0.0
                            y[0] = 0.0
                            z[0] = 0.0
                            GOSUB assign_task_webshoot
                        ENDIF

                    ENDIF

                ENDIF
            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            REMOVE_ANIMATION "spider"
            SET_PLAYER_FIRE_BUTTON player TRUE
            SET_PLAYER_CYCLE_WEAPON_BUTTON player TRUE
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            REMOVE_TEXTURE_DICTIONARY
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT   
        ENDIF

    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

loadTextures:
    CONST_INT idTip1 19
    CONST_INT idTip2 20
    CONST_INT idCrossHair 21
    CONST_INT idLockb 22
    CONST_INT idLockc 23
    CONST_INT idLockd 24
    CONST_INT idLocke 25
    CONST_INT idLockf 26

    LOAD_TEXTURE_DICTIONARY spaim
    LOAD_SPRITE idCrossHair "crosshair"
    LOAD_SPRITE idLockb "ilockb"
    LOAD_SPRITE idLockc "ilockc"
    LOAD_SPRITE idLockd "ilockd"
    LOAD_SPRITE idLocke "ilocke"
    LOAD_SPRITE idLockf "ilockf"
    LOAD_SPRITE idTip1 "htip1"
    LOAD_SPRITE idTip2 "htip2"
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

is_not_player_playing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_B")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_C")

        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpA")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpB")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpC")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpZ")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpD")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("pkJumpE")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jumpToWall")

            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_A")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_idle_B")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("walk_wall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("run_wall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_jump_fall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_B")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")

                IF NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")
                //AND NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("airToLampA")
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("airToLampB")
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("airToLampC")
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_F")

                    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")

                        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_00")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_00")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_front")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_fall")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_center")

                            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_left")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_right")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_L")

                                IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_ground")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("yank_object")

                                    RETURN_TRUE
                                    RETURN
                                ENDIF
                            ENDIF

                        ENDIF

                    ENDIF

                ENDIF
            ENDIF

        ENDIF

    ENDIF
    RETURN_FALSE
RETURN

is_not_char_playing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground")
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")
    //AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")
    //AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_b")
    AND NOT IS_CHAR_PLAYING_ANIM iChar "knife_hit_3"
    AND NOT IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

draw_crosshair:
    CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) FALSE FALSE (v1 v2) (x[1] y[1])
    GET_FIXED_XY_ASPECT_RATIO 30.0 30.0 (x[1] y[1])
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idCrossHair (v1 v2) (x[1] y[1]) (255 255 255 200)
RETURN

draw_indicator_to_target_char:
    IF CLEO_CALL get_target_char_from_char 0 player_actor 35.0 (iChar)
        IF GOSUB is_not_char_playing_anims
            CLEO_CALL getActorBonePos 0 iChar 3 (x[0] y[0] z[0])    //BONE_SPINE1
            CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (v1 v2) (x[1] y[1])
            GET_FIXED_XY_ASPECT_RATIO 36.0 36.0 (x[1] y[1])
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 -0.75 0.15 (x[2] y[2] z[2])

            IF GOSUB is_spider_hud_enabled
                //GET_FIXED_XY_ASPECT_RATIO 120.0 60.0 (x[0] y[0])
                x[0] = 90.00
                y[0] = 56.00
                USE_TEXT_COMMANDS FALSE
                SET_SPRITES_DRAW_BEFORE_FADE TRUE
                DRAW_SPRITE idTip2 (50.0 420.0) (x[0] y[0]) (255 255 255 200)
            ENDIF

            IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
                IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[2] y[2] z[2] 50.0
                AND NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[2] y[2] z[2] 10.0
                    USE_TEXT_COMMANDS FALSE
                    SET_SPRITES_DRAW_BEFORE_FADE TRUE
                    DRAW_SPRITE idLockf (v1 v2) (x[1] y[1]) (255 255 255 230)
                    IF GOSUB is_spider_hud_enabled
                        IF IS_PC_USING_JOYPAD
                            iTempVar = 713  //~k~~VEHICLE_ENTER_EXIT~ ~s~Web Strike Takedown
                            CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ELSE
                            iTempVar = 714  //~h~F ~s~Web Strike Takedown
                            CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ENDIF
                    ENDIF
                ELSE
                    USE_TEXT_COMMANDS FALSE
                    SET_SPRITES_DRAW_BEFORE_FADE TRUE
                    DRAW_SPRITE idLockb (v1 v2) (x[1] y[1]) (255 255 255 220)
                ENDIF
            ELSE
                IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[2] y[2] z[2] 0.75
                    USE_TEXT_COMMANDS FALSE
                    SET_SPRITES_DRAW_BEFORE_FADE TRUE
                    DRAW_SPRITE idLockf (v1 v2) (x[1] y[1]) (255 255 255 230)
                    IF GOSUB is_spider_hud_enabled
                        IF IS_PC_USING_JOYPAD
                            iTempVar = 713  //~k~~VEHICLE_ENTER_EXIT~ ~s~Web Strike Takedown
                            CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ELSE
                            iTempVar = 714  //~h~F ~s~Web Strike Takedown
                            CLEO_CALL GUI_DrawHelperText 0 (65.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ENDIF
                    ENDIF
                ELSE
                    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
                        USE_TEXT_COMMANDS FALSE
                        SET_SPRITES_DRAW_BEFORE_FADE TRUE
                        DRAW_SPRITE idLockb (v1 v2) (x[1] y[1]) (255 255 255 220)
                    ELSE
                        IF GOSUB does_skill_Surprise_Attack_is_enabled
                            USE_TEXT_COMMANDS FALSE
                            SET_SPRITES_DRAW_BEFORE_FADE TRUE
                            DRAW_SPRITE idLockc (v1 v2) (x[1] y[1]) (255 255 255 220)
                        ELSE
                            USE_TEXT_COMMANDS FALSE
                            SET_SPRITES_DRAW_BEFORE_FADE TRUE
                            DRAW_SPRITE idLockb (v1 v2) (x[1] y[1]) (255 255 255 220)
                        ENDIF
                    ENDIF
                    IF GOSUB is_spider_hud_enabled
                        IF IS_PC_USING_JOYPAD
                            iTempVar = 711  //~k~~VEHICLE_ENTER_EXIT~ ~s~Web Strike
                            CLEO_CALL GUI_DrawHelperText 0 (55.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ELSE
                            iTempVar = 712  //~h~F ~s~Web Strike
                            CLEO_CALL GUI_DrawHelperText 0 (55.0 420.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF

        ENDIF
    ENDIF
RETURN

is_spider_hud_enabled:
    GET_CLEO_SHARED_VAR varHUD (iTempVar)
    IF iTempVar = 1     // 0:OFF || 1:ON            
        GET_CLEO_SHARED_VAR varHudRadar (iTempVar)  //display indicator only if radar is enabled
        IF iTempVar = 1
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

//-+---------------------auto-aim-------------------------
set_auto_aim:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF GOSUB is_not_char_playing_anims

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

                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[0] y[0] z[0])
                        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
                        SET_CHAR_HEADING player_actor zAngle
                        
                    ENDIF

                ENDIF
            ENDIF
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[0] y[0] z[0])

        ENDIF
    ENDIF
RETURN
//----------------------------------------------------------


//-+---------------------web-shoot-------------------------
assign_task_webshoot:
    //-+- WebShoot
    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2        // ~k~~PED_CYCLE_WEAPON_RIGHT~/
    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
    AND NOT IS_BUTTON_PRESSED PAD1 TRIANGLE         // ~k~~VEHICLE_ENTER_EXIT~
    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1    //~k~~PED_ANSWER_PHONE~/ ~k~~PED_FIREWEAPON_ALT~
        GOSUB process_shot_web
        WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/  
            WAIT 0
        ENDWHILE
    ENDIF
RETURN

process_shot_web:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampA")  //fix
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
    
        GET_CLEO_SHARED_VAR varWeapAmmo (iTempVar)  // check ammo
        IF iTempVar > 0
            //task throw web-weap
            GOSUB task_play_anim_shoot_web
            /// Adding effect and sound
            GOSUB play_web_fx_sfx
            //creating new threads for each
            GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
            SWITCH iTempVar
                CASE weap_web_shoot
                    GOSUB assing_weap_id1_action
                    BREAK
                CASE weap_concussive_blast
                    GOSUB assing_weap_id2_action
                    BREAK
                CASE weap_impact_web
                    GOSUB assing_weap_id3_action
                    BREAK
                CASE weap_spyder_drone
                    GOSUB assing_weap_id4_action
                    BREAK
                CASE weap_electric_web
                    GOSUB assing_weap_id5_action
                    BREAK
                CASE weap_suspension_matrix
                    GOSUB assing_weap_id6_action
                    BREAK
                CASE weap_web_bomb
                    GOSUB assing_weap_id7_action
                    BREAK
                CASE weap_trip_mine
                    GOSUB assing_weap_id8_action
                    BREAK
            ENDSWITCH
        ELSE
            // sound fx -emtpy-
            GOSUB playEmptyAmmoWebShootSfx
            WAIT 0
        ENDIF
    
    ENDIF
RETURN

task_play_anim_shoot_web:
    IF IS_CHAR_PLAYING_ANIM player_actor ("knife_IDLE")
    OR IS_CHAR_PLAYING_ANIM player_actor ("WEAPON_knifeidle")
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
        IF randomVal = 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("m_wshootR" "spider") 18.0 (0 1 1 0) -1   //Right Hand
            is_web_thrown = FALSE
        ELSE
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("m_wshootL" "spider") 18.0 (0 1 1 0) -1   //Left Hand
            is_web_thrown = FALSE
        ENDIF
        //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("m_wshoot" "spider") 24.0 (0 1 1 0) -1   //two webs
        //is_web_thrown = FALSE
        //is_web_thrown = FALSE
    ELSE
        GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
        IF randomVal = 0
            TASK_PLAY_ANIM_SECONDARY player_actor "m_wshoot_p" "spider" 18.0 (0 1 1 0) -1   //Right Hand
            is_web_thrown = FALSE
        ELSE
            TASK_PLAY_ANIM_SECONDARY player_actor "m_wshoot_p_L" "spider" 18.0 (0 1 1 0) -1 //Left Hand
            is_web_thrown = FALSE
        ENDIF
    ENDIF
    GET_CLEO_SHARED_VAR varWeapAmmo (iTempVar)
    iTempVar -= 1
    SET_CLEO_SHARED_VAR varWeapAmmo iTempVar
    WAIT 0
RETURN

assing_weap_id1_action:     //weap_web_shoot
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF IS_CHAR_FALLEN_ON_GROUND iChar
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wa_1.cs"
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\wa_1.cs" iChar
            ENDIF
        ELSE
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wa.cs"
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\wa.cs" iChar
            ENDIF
        ENDIF
    ELSE
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wa.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\wa.cs" -1
        ENDIF
    ENDIF
    WAIT 50
RETURN

assing_weap_id2_action:     //weap_concussive_blast
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wb.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\wb.cs"
    ENDIF
    WAIT 50
RETURN

assing_weap_id3_action:     //weap_impact_web
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wc.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\wc.cs" iChar
        ENDIF
    ELSE
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wc.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\wc.cs" -1
        ENDIF
    ENDIF
    WAIT 50
RETURN

assing_weap_id4_action:     //weap_spyder_drone
    GET_CLEO_SHARED_VAR varWeapAmmo (iTempVar)  // check ammo
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wd.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\wd.cs" iTempVar
    ENDIF
    WAIT 50
RETURN

assing_weap_id5_action:     //weap_electric_web
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF NOT IS_CHAR_FALLEN_ON_GROUND iChar    
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\we.cs"
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\we.cs" iChar
            ENDIF
        ENDIF
    ELSE
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\we.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\we.cs" -1
        ENDIF
    ENDIF
    WAIT 50
RETURN

assing_weap_id6_action:     //weap_suspension_matrix
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wf.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\wf.cs" x[0] y[0] z[0]
    ENDIF
    WAIT 50
RETURN

assing_weap_id7_action:     //weap_web_bomb
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wg.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\wg.cs" x[0] y[0] z[0]
    ENDIF
    WAIT 50
RETURN

assing_weap_id8_action:     //weap_trip_mine
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\wh.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\wh.cs" x[0] y[0] z[0]
    ENDIF
    WAIT 50
RETURN

play_web_fx_sfx:
    IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootR")  //Right Hand
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("m_wshootR")
            GET_CHAR_ANIM_CURRENT_TIME player_actor "m_wshootR" (fCurrentTime)
            IF  fCurrentTime >= 0.444   //frame 8
                IF is_web_thrown = FALSE
                    GOSUB playWebShootSfx
                    CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (90.0 0.0 0.0) 4 (fx_web)
                    ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_web player_actor 25 //Right Hand
                    PLAY_AND_KILL_FX_SYSTEM fx_web
                    is_web_thrown = TRUE
                ENDIF
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
    ELSE 
        IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootL")  //Left Hand
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("m_wshootL")
                GET_CHAR_ANIM_CURRENT_TIME player_actor "m_wshootL" (fCurrentTime)
                IF  fCurrentTime >= 0.444   //frame 8
                    IF is_web_thrown = FALSE
                        GOSUB playWebShootSfx
                        CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (90.0 0.0 0.0) 4 (fx_web)
                        ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_web player_actor 35 //Left Hand
                        PLAY_AND_KILL_FX_SYSTEM fx_web
                        is_web_thrown = TRUE
                    ENDIF
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p")     //Right Hand
                WHILE IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p")
                    GET_CHAR_ANIM_CURRENT_TIME player_actor "m_wshoot_p" (fCurrentTime)
                    IF  fCurrentTime >= 0.444   //frame 8
                        IF is_web_thrown = FALSE
                            GOSUB playWebShootSfx
                            CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (90.0 0.0 0.0) 4 (fx_web)
                            ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_web player_actor 25 //Right Hand
                            PLAY_AND_KILL_FX_SYSTEM fx_web
                            is_web_thrown = TRUE
                        ENDIF
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p_L")   //Left Hand
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p_L")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor "m_wshoot_p_L" (fCurrentTime)
                        IF  fCurrentTime >= 0.444   //frame 8
                            IF is_web_thrown = FALSE
                                GOSUB playWebShootSfx
                                CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_WEB_FLASH player_actor (0.0 0.0 0.0) (90.0 0.0 0.0) 4 (fx_web)
                                ATTACH_FX_SYSTEM_TO_CHAR_BONE fx_web player_actor 35 //Left Hand
                                PLAY_AND_KILL_FX_SYSTEM fx_web
                                is_web_thrown = TRUE
                            ENDIF
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                ENDIF
            ENDIF
        ENDIF
    ENDIF
RETURN

playWebShootSfx:
    REMOVE_AUDIO_STREAM sfx
    GET_CLEO_SHARED_VAR varIdWebWeapon (iTempVar)
    SWITCH iTempVar
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 4
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 5
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot5.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 6
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot6.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 7
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot7.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 8
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot8.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
    ENDSWITCH
    WAIT 0
RETURN

playEmptyAmmoWebShootSfx:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\no_shots.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx PLAY
    ENDIF
    WAIT 0
RETURN
//----------------------------------------------------------

//-+---------------------web-strike-------------------------
assign_task_webstrike:
    //-+- WebStrike
    //IF IS_BUTTON_PRESSED PAD1 CIRCLE        // ~k~~PED_FIREWEAPON~
    IF IS_BUTTON_PRESSED PAD1 TRIANGLE    // ~k~~VEHICLE_ENTER_EXIT~
    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE        // ~k~~PED_FIREWEAPON~
    AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2   // ~k~~PED_CYCLE_WEAPON_RIGHT~/
    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1    //~k~~PED_ANSWER_PHONE~/ ~k~~PED_FIREWEAPON_ALT~
        IF IS_CHAR_REALLY_IN_AIR player_actor
            IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
                //----------------------------------- Jump from Lamp
                GOSUB process_jump_pole
                GOSUB process_web_strike_air
            ELSE
                GOSUB process_web_strike_air
            ENDIF
        ELSE
            GOSUB process_web_strike_ground
        ENDIF
        WHILE IS_BUTTON_PRESSED PAD1 TRIANGLE    // ~k~~VEHICLE_ENTER_EXIT~
            WAIT 0
        ENDWHILE
    ENDIF
RETURN

process_jump_pole:
    GOSUB REQUEST_Animations
    y[2] = 1.0
    z[2] = 1.5
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_webstrike_start" "spider") 17.0 (0 1 1 1) -2
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) 7.0

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_webstrike_start")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_webstrike_start") (fCurrentTime)
        IF fCurrentTime >= 0.938  //frame 15/16
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    WAIT 0
RETURN

process_web_strike_air:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF GOSUB is_not_char_playing_anims
            IF NOT IS_CHAR_FALLEN_ON_GROUND iChar    
                GOSUB REQUEST_Animations
                GOSUB assign_task_webstrike_air_in

                GOSUB set_xzAngle_in_air
                GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (z[2]) //max distance

                IF GOSUB does_skill_Surprise_Attack_is_enabled
                    CLEO_CALL get_damage_distance 0 z[2] (total_dmg)    //damage proportional to distance
                ELSE
                    total_dmg = 10
                ENDIF

                IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in")
                OR IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
                OR IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
                OR IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_d")
                    GOSUB delay_task_webstrike_air_in
                    //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.5 (x[0] y[0] z[0])

                    //IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[1] y[1] z[1] 1.5
                    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 2.0
                        IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
                            GOSUB assign_task_webstrike_air_finish
                        ELSE
                            IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_d")
                                GOSUB assign_task_webstrike_air_finish_b
                            ELSE
                                IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
                                    GOSUB assign_task_webstrike_air_far
                                ELSE
                                    GOSUB assign_task_webstrike_air_near
                                ENDIF
                            ENDIF
                        ENDIF

                        //WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[1] y[1] z[1] 2.0
                        WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 2.0
                            GOSUB set_xzAngle_in_air
                            //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.5 (x[0] y[0] z[0])
                            IF IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)   //(isSolid isCar isActor isObject isParticle)
                                CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) 60.0
                            ELSE
                                CLEAR_CHAR_TASKS player_actor
                                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                                GOTO end_webStrike_air
                            ENDIF
                            GET_DISTANCE_BETWEEN_COORDS_3D (x[1] y[1] z[1]) (x[0] y[0] z[0]) (fDistance)
                            IF fDistance >= 2.0
                                IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.0) (1 1 0 0 0)
                                ELSE
                                    CLEAR_CHAR_TASKS player_actor
                                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                                    GOTO end_webStrike_air
                                ENDIF
                            ENDIF
                            /*
                            IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_c")
                                CLEO_CALL linearInterpolation 0 (z[2] 4.0 fDistance) (0.0 1.0) (fCurrentTime)   //frame 0-23/23
                                IF fCurrentTime >= 1.0
                                    fCurrentTime = 1.0
                                ENDIF
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_c" "spider") 24.0 (0 1 1 1) -2
                                SET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_c") fCurrentTime
                                SET_CHAR_ANIM_PLAYING_FLAG player_actor ("webstrike_air_c") FALSE       
                            ENDIF
                            */
                            WAIT 0
                        ENDWHILE
                    ENDIF
                    //SET_CHAR_COLLISION iChar FALSE

                    GET_CHAR_HEADING player_actor (zAngle)
                    CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) 0.0
                    CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) 0.0
                    CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.1) 4 (fx_web)
                    PLAY_AND_KILL_FX_SYSTEM fx_web

                    GOSUB assign_task_webstrike_air_out
                    SET_CHAR_HEADING player_actor zAngle
                    zAngle += 180.0
                    SET_CHAR_HEADING iChar zAngle
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.5 0.25 (x[0] y[0] z[0])
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]

                    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_out_c")
                        //SET_CHAR_COLLISION iChar TRUE
                        CLEAR_CHAR_TASKS iChar
                        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                        TASK_DIE_NAMED_ANIM iChar ("wt_air_hit_c" "spider") 23.0 -1
                        WAIT 0
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.25 -0.5 (x[0] y[0] z[0])   //0.415
                        SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE

                        iTempVar = 3
                        GOSUB playWebStrikeSfx

                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.75 -0.5 (x[0] y[0] z[0])   //0.415

                            WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_out_c")
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_out_c") (fCurrentTime)
                                IF fCurrentTime >= 0.143    //frame 6/42
                                    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                                    FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE

                        SET_CHAR_HEADING iChar zAngle
                        IF iTempVar = 3
                            GENERATE_RANDOM_INT_IN_RANGE 0 3 (iTempVar)
                            IF iTempVar = 1
                                CLEO_CALL set_camera_rotate_around_char 0 player_actor
                            ENDIF
                            iTempVar = 3
                        ENDIF
                        WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_out_c")
                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_out_c") (fCurrentTime)
                            IF fCurrentTime >= 0.976    //frame 41/42
                                BREAK
                            ENDIF
                            WAIT 0
                        ENDWHILE
                        //SET_CHAR_COLLISION iChar TRUE
                    ELSE
                        IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_out_d")
                            CLEAR_CHAR_TASKS iChar
                            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                            TASK_DIE_NAMED_ANIM iChar ("wt_ground_hit_d" "spider") 23.0 -1
                            WAIT 0
                            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.50 -0.5 (x[0] y[0] z[0])   //0.415
                            SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                            FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE
                            SET_CHAR_HEADING iChar zAngle
                            WAIT 50
                            iTempVar = 3
                            GOSUB playWebStrikeSfx
                        ELSE
                            CLEAR_CHAR_TASKS iChar
                            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("HIT_R" "PED") 19.0 (0 1 1 0) -1
                            WAIT 0
                            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.7 0.0 (x[0] y[0] z[0])
                            SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                            FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE
                            DAMAGE_CHAR iChar total_dmg TRUE
                            SET_CHAR_HEADING iChar zAngle
                            iTempVar = 2
                            GOSUB playWebStrikeSfx
                            SET_CHAR_COLLISION iChar TRUE
                            WAIT 0
                            SET_CHAR_COLLISION iChar TRUE
                        ENDIF
                    ENDIF
                    WAIT 50

                    end_webStrike_air:

                ENDIF

            ENDIF
        ENDIF
    ENDIF
RETURN

set_xzAngle_in_air:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 1.5 (x[0] y[0] z[0])
    ELSE
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.25 (x[0] y[0] z[0])
    ENDIF
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
    xAngle *= -1.0
    SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle
    zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle
RETURN

assign_task_webstrike_air_near:
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_a" "spider") 31.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "webstrike_air_a" 1.3
RETURN

assign_task_webstrike_air_far:
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_b" "spider") 9.0 (0 1 1 1) -2
    WAIT 0
    //SET_CHAR_ANIM_SPEED player_actor "webstrike_air_b" 1.3
RETURN

assign_task_webstrike_air_finish:
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_c" "spider") 24.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "webstrike_air_c" 2.0
RETURN

assign_task_webstrike_air_finish_b:
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_d" "spider") 24.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "webstrike_air_d" 2.0
RETURN

assign_task_webstrike_air_in:
    //IF NOT LOCATE_CHAR_ANY_MEANS_3D player_actor x[2] y[2] z[2] 0.8 0.6 1.0 FALSE
    IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
        IF z[2] > 20.0
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_in_b" "spider") 16.0 (0 1 1 1) -2
            WAIT 1
        ELSE
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_in" "spider") 21.0 (0 1 1 1) -2
            WAIT 1
        ENDIF
    ELSE
        IF GOSUB does_skill_Surprise_Attack_is_enabled
            //PRINT_FORMATTED_NOW "NOT LOOKING" 1000
            GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
            IF randomVal > 0
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_in_c" "spider") 13.0 (0 1 1 1) -2
                WAIT 1
                SET_CHAR_COLLISION iChar FALSE
            ELSE
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_in_d" "spider") 13.0 (0 1 1 1) -2
                WAIT 1
            ENDIF
        ELSE
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_in_b" "spider") 16.0 (0 1 1 1) -2
            WAIT 1
        ENDIF
    ENDIF
RETURN

delay_task_webstrike_air_in:
    iTempVar = 1
    SET_CHAR_COLLISION player_actor FALSE
    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
            GOSUB set_xzAngle_in_air
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_in_c") (fCurrentTime)
            IF fCurrentTime > 0.500    //frame 6/12
                CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
                GOSUB playWebStrikeSfx
                iTempVar = 0
            ENDIF
            IF fCurrentTime >= 0.917   //frame 11/12
                SET_CHAR_COLLISION player_actor TRUE
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in")
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in")
                GOSUB set_xzAngle_in_air
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_in") (fCurrentTime)
                IF fCurrentTime > 0.250     //frame 5/20
                    CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
                    GOSUB playWebStrikeSfx
                    iTempVar = 0
                ENDIF
                IF fCurrentTime >= 0.950   //frame 19/20
                    SET_CHAR_COLLISION player_actor TRUE
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
                WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
                    GOSUB set_xzAngle_in_air
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_in_b") (fCurrentTime)
                    IF fCurrentTime > 0.333    //frame 5/15
                        CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
                        GOSUB playWebStrikeSfx
                        iTempVar = 0
                    ENDIF
                    IF fCurrentTime >= 0.933   //frame 14/15
                        SET_CHAR_COLLISION player_actor TRUE
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_d")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_d")
                        GOSUB set_xzAngle_in_air
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_air_in_d") (fCurrentTime)
                        IF fCurrentTime > 0.500    //frame 6/12
                            CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
                            GOSUB playWebStrikeSfx
                            iTempVar = 0
                        ENDIF
                        IF fCurrentTime >= 0.917   //frame 11/12
                            SET_CHAR_COLLISION player_actor TRUE
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                ENDIF
            ENDIF
        ENDIF
    ENDIF
    SET_CHAR_COLLISION player_actor TRUE
RETURN

assign_task_webstrike_air_out:
    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_c")
        CLEAR_CHAR_TASKS player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_out_c" "spider") 43.0 (0 1 1 0) -1
        WAIT 0
        SET_CHAR_ROTATION player_actor 0.0 0.0 zAngle
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_d")
            CLEAR_CHAR_TASKS player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_air_out_d" "spider") 37.0 (0 1 1 0) -1
            WAIT 0
            SET_CHAR_ROTATION player_actor 0.0 0.0 zAngle
        ELSE

            IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_b")
                CLEAR_CHAR_TASKS player_actor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_out_b" "spider") 24.0 (0 1 1 0) -1
                WAIT 0
            ELSE
                CLEAR_CHAR_TASKS player_actor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_out" "spider") 21.0 (0 1 1 0) -1
                WAIT 0
                SET_CHAR_ANIM_SPEED player_actor "webstrike_g_out" 1.25
            ENDIF
        ENDIF
    ENDIF
RETURN

process_web_strike_ground:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF GOSUB is_not_char_playing_anims
            IF NOT IS_CHAR_FALLEN_ON_GROUND iChar    
                GOSUB REQUEST_Animations
                GOSUB assign_task_webstrike_ground_in

                GOSUB set_zAngle_on_ground
                GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (z[2]) //max distance
                
                IF GOSUB does_skill_Surprise_Attack_is_enabled
                    CLEO_CALL get_damage_distance 0 z[2] (total_dmg)    //damage proportional to distance
                ELSE
                    total_dmg = 10
                ENDIF

                IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_in")
                    GOSUB delay_task_webstrike_ground_in

                    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[1] y[1] z[1] 1.6

                        IF HAS_CHAR_SPOTTED_CHAR_IN_FRONT iChar player_actor
                            IF z[2] > 15.0
                                GOSUB assign_task_webstrike_ground_far
                            ELSE
                                GOSUB assign_task_webstrike_ground_near
                            ENDIF
                        ELSE
                            IF GOSUB does_skill_Surprise_Attack_is_enabled
                                GOSUB assign_task_webstrike_ground_finish_start
                            ELSE
                                GOSUB assign_task_webstrike_ground_far
                            ENDIF
                        ENDIF

                        WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[1] y[1] z[1] 2.0
                            GOSUB set_zAngle_on_ground
                            IF IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)   //(isSolid isCar isActor isObject isParticle)
                                IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_a")
                                    CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
                                    GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance)
                                    CLEO_CALL linearInterpolation 0 (z[2] 1.5 fDistance) (0.0 1.0) (fCurrentTime)   //frame 0-8/8
                                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_g_a") fCurrentTime
                                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("webstrike_g_a") FALSE       
                                ENDIF
                                // simulates is in air  -avoid stuck in ground
                                GET_PED_POINTER player_actor (iTempVar)
                                WRITE_STRUCT_OFFSET iTempVar 0x46C BYTE 0    // ONFOOT_STATE = AIR
                                CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) 50.0
                            ELSE
                                CLEAR_CHAR_TASKS player_actor
                                GOTO end_webStrike_ground
                            ENDIF
                            WAIT 0
                        ENDWHILE
                    ENDIF
                    
                    CREATE_FX_SYSTEM_ON_CHAR SP_HIT iChar (0.0 0.0 0.1) 4 (fx_web)
                    PLAY_AND_KILL_FX_SYSTEM fx_web
                    //PRINT_FORMATTED_NOW "DMG:%d" 1000 total_dmg

                    GOSUB assign_task_webstrike_ground_out
                    
                    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out_d")
                        CLEAR_CHAR_TASKS iChar
                        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                        TASK_DIE_NAMED_ANIM iChar ("wt_ground_hit_d" "spider") 31.0 -1
                        WAIT 0
                        //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 -0.05 -0.5 (x[0] y[0] z[0])
                        //SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
                        //FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE
                        SET_CHAR_HEADING iChar zAngle
                        WAIT 50
                        iTempVar = 3
                        GOSUB playWebStrikeSfx
                    ELSE
                        iTempVar = 2
                        GOSUB playWebStrikeSfx
                        DAMAGE_CHAR iChar total_dmg TRUE

                        CLEAR_CHAR_TASKS iChar
                        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("HIT_R" "PED") 19.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_HEADING iChar zAngle
                        //--+-- new dodge part added
                        IF  IS_CHAR_PLAYING_ANIM iChar ("HIT_R")
                            WHILE IS_CHAR_PLAYING_ANIM iChar ("HIT_R")
                                GET_CHAR_ANIM_CURRENT_TIME iChar "HIT_R" (fCurrentTime)
                                IF fCurrentTime >= 0.4
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE
                            IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                GOSUB assign_task_dodge_front_c
                                WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                    WAIT 0
                                ENDWHILE
                            ENDIF
                        ENDIF
                        //---------
                    ENDIF

                    WAIT 0
                    end_webStrike_ground:
                ENDIF

            ENDIF
        ENDIF
    ENDIF
RETURN

does_skill_Surprise_Attack_is_enabled:
    GET_CLEO_SHARED_VAR varSkill1 (iTempVar)
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

set_zAngle_on_ground:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING player_actor zAngle
    zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle 
RETURN

assign_task_webstrike_ground_far:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
    z[1] += 2.0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    IF z[2] > 25.0
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_c" "spider") 12.0 (0 1 1 1) -2
        WAIT 0
    ELSE
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_b" "spider") 25.0 (0 1 1 1) -2
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "webstrike_g_b" 1.15
    ENDIF
RETURN

assign_task_webstrike_ground_near:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
    z[1] += 1.0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_a" "spider") 9.0 (0 1 1 1) -2
    WAIT 0
RETURN

assign_task_webstrike_ground_finish_start:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
    z[1] += 1.5
    SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]

    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_d" "spider") 12.0 (0 1 1 1) -2
    WAIT 0
RETURN

assign_task_webstrike_ground_in:
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_in" "spider") 16.0 (0 1 1 1) -1
    WAIT 1
RETURN

delay_task_webstrike_ground_in:
    iTempVar = 1
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_in")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("webstrike_g_in") (fCurrentTime)
        IF fCurrentTime > 0.467 //frame 7/15
            CLEO_CALL draw_line_from_player_bone_to_char_bone 0 player_actor 25 iChar 2     //25:BONE_RIGHTHAND||2:BONE_PELVIS
            GOSUB playWebStrikeSfx
            iTempVar = 0
        ENDIF
        IF fCurrentTime >= 0.933   //frame 14/15
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
RETURN

assign_task_webstrike_ground_out:
    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_c")
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_out_b" "spider") 24.0 (0 1 1 0) -1
        WAIT 0
        //SET_CHAR_ANIM_SPEED player_actor "webstrike_g_out_b" 1.25
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_d")
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_out_d" "spider") 31.0 (0 1 1 0) -1
            WAIT 0
        ELSE
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("webstrike_g_out" "spider") 21.0 (0 1 1 0) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "webstrike_g_out" 1.25
        ENDIF
    ENDIF
RETURN

assign_task_dodge_front_c:
    // original in sp_evb -> Manual Dodge & Sliding dodge
    GOSUB REQUEST_Animations
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    SET_CHAR_COLLISION iChar FALSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.50 0.0 (x[0] y[0] z[0])
    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
    FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE FALSE FALSE
    FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN player_actor TRUE FALSE FALSE

    GOSUB play_sfx
    WAIT 0
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("dodge_front_c_hit" "spider") 39.0 (0 1 1 0) -1
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_front_c" "spider") 34.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "dodge_front_c" 1.30
    WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_front_c"
        GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_front_c" (fCurrentTime)
        IF fCurrentTime >= 0.606    //frame 20/33
        AND 0.636 >= fCurrentTime   //frame 21/33
            SET_CHAR_COLLISION iChar TRUE
        ENDIF
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
    SET_CHAR_COLLISION iChar TRUE

    IF iTempVar = 1
        IF DOES_CHAR_EXIST iChar
            CLEAR_CHAR_TASKS iChar
            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("dodge_front_c_hita" "spider") 43.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup_front" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
        ENDIF
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_front_c_ha" "spider") 40.0 (0 1 1 0) -1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "dodge_front_c_ha" 1.15
        CLEAR_SEQUENCE_TASK anim_seq
        IF IS_CHAR_PLAYING_ANIM player_actor "dodge_front_c_ha"
            WHILE IS_CHAR_PLAYING_ANIM player_actor "dodge_front_c_ha"
                GET_CHAR_ANIM_CURRENT_TIME player_actor "dodge_front_c_ha" (fCurrentTime)
                IF fCurrentTime >= 0.154    //frame 6/39
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
            /*
            IF IS_CHAR_SCRIPT_CONTROLLED iChar
                MARK_CHAR_AS_NO_LONGER_NEEDED iChar
            ENDIF
            */
        ENDIF
    ELSE
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("dodge_front_c_hitb" "spider") 16.0 (0 1 1 0) -1
        WAIT 0
        WAIT 0
        WAIT 0
        WAIT 0
        WAIT 0
        WAIT 0
        /*
        IF IS_CHAR_SCRIPT_CONTROLLED iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        ENDIF
        */
    ENDIF
RETURN

playWebStrikeSfx:
    SWITCH iTempVar
        CASE 1
            REMOVE_AUDIO_STREAM sfx
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webStrike.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 2
            REMOVE_AUDIO_STREAM sfx
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_hit_a.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
        CASE 3
            REMOVE_AUDIO_STREAM sfx
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\punch_finish.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\punch_finish.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx PLAY
            ELSE
                ADD_ONE_OFF_SOUND 0.0 0.0 0.0 1130    //SOUND_PUNCH_PED 1130
            ENDIF
            BREAK
    ENDSWITCH
    WAIT 0
RETURN

play_sfx:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_h.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx PLAY
    ENDIF
    WAIT 0
RETURN

play_sfx_hit:
    REMOVE_AUDIO_STREAM sfx
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
//----------------------------------------------------------
}
SCRIPT_END


//-+------CALL SCM HELPERS
//-----checks----------------------------------------------
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT tempPlayer
    LVAR_FLOAT x y z
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT xA yA zA xB yB zB 
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer x y z (xA yA zA)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (xB yB zB)
    IF IS_LINE_OF_SIGHT_CLEAR xB yB zB xA yA zA (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
//-------Set----------------------------------------------
{
//CLEO_CALL setCharVelocityTo 0 scplayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer   //in
    LVAR_FLOAT xIn yIn zIn iAmplitude //in
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

//-------get----------------------------------------------
{
//CLEO_CALL get_damage_distance 0 distance (dmg)
get_damage_distance: 
    LVAR_FLOAT fDistance    //in
    LVAR_FLOAT fCurrentDmg
    LVAR_INT dmg
    CLEO_CALL linearInterpolation 0 (0.0 30.0 fDistance) (10.0 100.0) (fCurrentDmg)
    CSET_LVAR_INT_TO_LVAR_FLOAT (dmg) fCurrentDmg
CLEO_RETURN 0 dmg
}
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
//CLEO_CALL get_target_char_from_char 0 scplayer fMaxDistance (char)
get_target_char_from_char:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMaxDistance  //in
    LVAR_INT p i iChar iNewPed 
    LVAR_FLOAT xScreenSize x[2] y[2] z[2] fDistance v1 v2
    LVAR_INT pedType
    IF DOES_CHAR_EXIST scplayer
        xScreenSize = 50.0
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR scplayer iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    //GET_PED_TYPE char (pedType)
                    //IF NOT pedType = PEDTYPE_COP
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 0.0 0.0) (x[0] y[0] z[0])
                        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                        IF fMaxDistance > fDistance
                            IF IS_CHAR_ON_SCREEN iChar
                            AND IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0)   //(isSolid isCar isActor isObject isParticle)
                                CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
                                GET_DISTANCE_BETWEEN_COORDS_2D (339.0 179.0) (v1 v2) (fDistance)
                                IF xScreenSize >= fDistance
                                    xScreenSize = fDistance
                                    iNewPed = iChar
                                ENDIF
                            ENDIF
                        ENDIF
                    //ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        IF DOES_CHAR_EXIST iNewPed
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
CLEO_RETURN 0 iNewPed
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
//CLEO_CALL getXYZAimCoords 0 scplayer fRange (x y z)
getXYZAimCoords:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fRange   //in
    LVAR_FLOAT x[3] y[3] z[3]
    GET_CHAR_COORDINATES scplayer (x[0] y[0] z[0])
    CLEO_CALL getAimPoint 0 fRange (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[2] y[2] z[2])
    CLEO_CALL getLaserPoint 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (x[0] y[0] z[0])
CLEO_RETURN 0 x[0] y[0] z[0]
}
{
//CLEO_CALL getAimPoint 0 /*range*/ 20.0 /*from*/(0.0 0.0 0.0) /*Camera To*/(var1 var2 var3) /*Point to*/ (var4 var5 var6)
getAimPoint:
    LVAR_FLOAT range fromX fromY fromZ  //in
    LVAR_FLOAT camX camY camZ pointX pointY pointZ
    LVAR_INT var1 var2
    GET_VAR_POINTER (camX) (var1)
    GET_VAR_POINTER (pointX) (var2)
    CALL_METHOD 0x514970 /*struct*/0xB6F028 /*params*/6 /*pop*/0 /*pPoint*/var2 /*pCam*/var1 /*fZ*/fromZ /*fY*/fromY /*fX*/fromX /*fRange*/ range
CLEO_RETURN 0 camX camY camZ pointX pointY pointZ
}
{
//CLEO_CALL getLaserPoint 0 /*from*/0.0 0.0 0.0 /*to*/1.0 0.0 0.0 /*store_to*/ var1 var2 var3
getLaserPoint:
    LVAR_FLOAT fromX fromY fromZ toX toY toZ    //in
    LVAR_FLOAT resultX resultY resultZ
    LVAR_INT i scplayer
    GET_PLAYER_CHAR 0 scplayer
    GET_PED_POINTER scplayer i
    IF GET_COLLISION_BETWEEN_POINTS (fromX fromY fromZ) (toX toY toZ) TRUE TRUE TRUE TRUE FALSE TRUE TRUE TRUE i 0x0 (resultX resultY resultZ i)
    ELSE
        resultX = toX
        resultY = toY
        resultZ = toZ
    ENDIF
CLEO_RETURN 0 resultX resultY resultZ
}
{
//CLEO_CALL draw_line_from_player_bone_to_char_bone 0 scplayer boneID char boneID
draw_line_from_player_bone_to_char_bone:
    LVAR_INT scplayer idBoneP iChar idBoneC    //in
    LVAR_FLOAT x[3] y[3] z[3] v1 v2
    IF DOES_CHAR_EXIST scplayer
    AND DOES_CHAR_EXIST iChar
        CLEO_CALL getActorBonePos 0 iChar idBoneC (x[0] y[0] z[0])
        CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (x[2] y[2]) (x[1] y[1])
        CLEO_CALL getActorBonePos 0 scplayer idBoneP (x[1] y[1] z[1])
        CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
        CLEO_CALL drawline 0 v1 v2 x[2] y[2] 0.5 (255 255 255 255)
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
    CLEO_CALL GUI_SetTextFormatByID 0 formatId (h)
    posY -= h
    posY += paddingTop
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID:
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
//CLEO_CALL set_camera_rotate_around_char 0 iChar
set_camera_rotate_around_char:
    CONST_FLOAT pi 3.1415927
    LVAR_INT iChar
    LVAR_FLOAT x[3] y[3] z[3]
    LVAR_FLOAT fAngle xV yV zV fVar
    IF DOES_CHAR_EXIST iChar
        fVar = 5.0
        fAngle = 270.0
        zV = 0.4
        WHILE 495.0 > fAngle
            IF 405.0 > fAngle
                zV -=@ 0.01
                fVar -=@ 0.1
            ELSE
                zV +=@ 0.02
                fVar +=@ 0.1
            ENDIF
            CLAMP_FLOAT zV -3.0 2.0 (zV)
            CLAMP_FLOAT fVar 2.0 5.0 (fVar)
            SIN fAngle (xV)
            COS fAngle (yV)
            xV *= fVar
            yV *= fVar
            ATTACH_CAMERA_TO_CHAR_LOOK_AT_CHAR iChar xV yV zV iChar 0.0 2
            IF fAngle > 315.0
            AND 405.0 > fAngle
                SET_TIME_SCALE 0.5
            ELSE
                SET_TIME_SCALE 1.0
            ENDIF
            fAngle +=@ 4.0
            WAIT 0
        ENDWHILE
        SET_TIME_SCALE 1.0
        RESTORE_CAMERA_JUMPCUT
        SET_CAMERA_BEHIND_PLAYER      
    ENDIF
CLEO_RETURN 0
}
//-------Hacks----------------------------------------------
{
//CLEO_CALL disableGreenTriangles 0 ()
disableGreenTriangles:
LVAR_INT ptr
READ_MEMORY 0x53E20E BYTE 0 (ptr)
IF NOT ptr = 0x90
    WRITE_MEMORY 0x53E20E 5 0x90 TRUE
ELSE
    READ_MEMORY 0x53E1E0 BYTE 0 (ptr)
    IF NOT ptr = 0x90
        WRITE_MEMORY 0x53E1E0 5 0x90 TRUE
    ENDIF
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
create_decision_maker_hate:
    /// https://gtamods.com/wiki/Ped_Event
    /// https://gtamods.com/wiki/Task_IDs_(GTA_SA)
    LVAR_INT iDecMaker
    LOAD_CHAR_DECISION_MAKER 2 (iDecMaker)  //0 - Blank || 1 - Brave || 2 - Violent || 3 - Coward || 4 - Grouped
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDecMaker 36 1000 0.0 100.0 0.0 100.0 0 1 // params: iEvent|iTaskId|fRespect|fHate|fLike|fDislike|iInCar|iOnFoot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDecMaker 37 1001 0.0 100.0 0.0 100.0 0 1 // params: iEvent|iTaskId|fRespect|fHate|fLike|fDislike|iInCar|iOnFoot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDecMaker 36 1016 0.0 100.0 0.0 100.0 0 1 // params: iEvent|iTaskId|fRespect|fHate|fLike|fDislike|iInCar|iOnFoot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDecMaker 9 1001 0.0 75.0 0.0 75.0 1 1 // params: iEvent|iTaskId|fRespect|fHate|fLike|fDislike|iInCar|iOnFoot
    SET_RELATIONSHIP 4 PEDTYPE_GANG1 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG3 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG4 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG5 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG6 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG7 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG8 PEDTYPE_PLAYER1
    SET_RELATIONSHIP 4 PEDTYPE_GANG9 PEDTYPE_PLAYER1
    SET_RELATIONSHIp 4 PEDTYPE_GANG10 PEDTYPE_PLAYER1
RETURN
*/

