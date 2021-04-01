// by J16D
// Manual Dodge & Ground dodge
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
//FIRE TYPE
CONST_INT FIRETYPE_MELEE        0
CONST_INT FIRETYPE_INSTANT_HIT  1
CONST_INT FIRETYPE_PROJECTILE   2
CONST_INT FIRETYPE_AREA_EFFECT  3
CONST_INT FIRETYPE_CAMERA       4
CONST_INT FIRETYPE_USE          5

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_evb
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu
LVAR_INT i p iChar fx_system sfx anim_seq
LVAR_FLOAT fCurrentTime x[2] y[2] z[2] zAngle
LVAR_INT iTempVar counter 
LVAR_INT LRStick UDStick

GET_PLAYER_CHAR 0 player_actor

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_CHAR_REALLY_IN_AIR player_actor

                    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   //~k~~PED_LOCK_TARGET~
                        IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
                        //IF GOSUB is_not_char_playing_dodge_anims
                            GOSUB assign_task_dodge_anim
                        ENDIF
                    ENDIF
                    IF GOSUB is_char_around_player
                        IF NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION player_actor INCLUDE_ANIMS_PRIMARY
                        //IF GOSUB is_not_char_playing_dodge_anims
                            IF IS_CHAR_PLAYING_ANIM player_actor "DILDO_1"
                                WHILE IS_CHAR_PLAYING_ANIM player_actor "DILDO_1"
                                    GET_CHAR_ANIM_CURRENT_TIME player_actor "DILDO_1" (fCurrentTime)
                                    IF fCurrentTime >= 0.370 // frame 10/27
                                        IF GOSUB is_char_in_front_char
                                            IF IS_BUTTON_PRESSED PAD1 CIRCLE    // ~k~~PED_FIREWEAPON~
                                            AND IS_BUTTON_PRESSED PAD1 SQUARE   // ~k~~PED_JUMPING~
                                                GOSUB assign_task_perfect_dodge_c
                                            ENDIF
                                        ENDIF
                                    ENDIF
                                    WAIT 0
                                ENDWHILE
                            ENDIF
                        ENDIF
                    ENDIF

                ENDIF
            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
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

is_char_in_front_char:
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.0 0.25 (x[1] y[1] z[1])
        IF LOCATE_CHAR_ANY_MEANS_2D iChar x[1] y[1] 1.0 1.0 FALSE
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_char_around_player:
    counter = 0
    WHILE 5 > counter
        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
        IF GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 1.5 0 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
        counter += 1
    ENDWHILE
    RETURN_FALSE
RETURN

set_z_angle:
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
        GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (zAngle)
        SET_CHAR_HEADING player_actor zAngle
        zAngle += 180.0
        SET_CHAR_HEADING iChar zAngle
    ENDIF
RETURN
//---------------------------------------------------------

//-+---------------------dodge-anim-------------------------
assign_task_perfect_dodge_c:
    IF NOT IS_CHAR_FALLEN_ON_GROUND iChar 
    AND NOT IS_CHAR_PLAYING_ANY_SCRIPT_ANIMATION iChar INCLUDE_ANIMS_PRIMARY
    //IF GOSUB is_lying_on_the_floor
        GOSUB set_z_angle
        GOSUB assign_task_dodge_front_c
        WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
            WAIT 0
        ENDWHILE
    ENDIF
RETURN

assign_task_dodge_front_c:
    GOSUB REQUEST_Animations
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    SET_CHAR_COLLISION iChar FALSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.50 0.0 (x[0] y[0] z[0])
    SET_CHAR_COORDINATES_SIMPLE iChar x[0] y[0] z[0]
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
        IF  IS_CHAR_PLAYING_ANIM iChar "dodge_front_c_hitb"
            WHILE IS_CHAR_PLAYING_ANIM iChar "dodge_front_c_hitb"
                WAIT 0
            ENDWHILE
        ENDIF
    ENDIF
RETURN
//---------------------------------------------------------

//---------------------------------------------------------
assign_task_dodge_anim:
    //-+--evade anims
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF LRStick > 0 
        IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
            CLEO_CALL setCharViewPointToCamera 0 player_actor
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dodge_right" "spider") 37.0 (0 1 1 0) -1 0 0
            //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_right" "spider") 37.0 (0 1 1 0) -1
            WAIT 0
            GOSUB play_sfx_b
            WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                WAIT 0
            ENDWHILE
        ENDIF
    ELSE
        IF 0 > LRStick
            IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                CLEO_CALL setCharViewPointToCamera 0 player_actor
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dodge_left" "spider") 37.0 (0 1 1 0) -1 0 0
                //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_left" "spider") 24.0 (0 1 1 0) -1
                WAIT 0
                GOSUB play_sfx_b
                WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    WAIT 0
                ENDWHILE
            ENDIF
        ENDIF
    ENDIF
    IF 0 > UDStick //Forward
        IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
            CLEO_CALL setCharViewPointToCamera 0 player_actor
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dodge_front" "spider") 33.0 (0 1 1 0) -1 0 0
            //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_front" "spider") 24.0 (0 1 1 0) -1
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "dodge_front" 1.15
            GOSUB play_sfx_b
            WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                WAIT 0
            ENDWHILE
        ENDIF
    ELSE
        IF UDStick > 0 // Backward
            IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                CLEO_CALL setCharViewPointToCamera 0 player_actor
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                TASK_PLAY_ANIM_WITH_FLAGS player_actor ("dodge_back" "spider") 34.0 (0 1 1 0) -1 0 0
                //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("dodge_back" "spider") 34.0 (0 1 1 0) -1
                WAIT 0
                GOSUB play_sfx_b
                WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    WAIT 0
                ENDWHILE
            ENDIF
        ENDIF
    ENDIF
RETURN
//---------------------------------------------------------

/*  Not needed anymore, thanks to cleo+
is_Not_Playing_Anim:
    IF DOES_CHAR_EXIST iChar
        IF NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")
        //AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")
        AND NOT IS_CHAR_PLAYING_ANIM iChar "knife_hit_3"
        AND NOT IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_lying_on_the_floor:
    IF DOES_CHAR_EXIST iChar
        IF IS_CHAR_PLAYING_ANIM iChar "KO_skid_front"
        OR IS_CHAR_PLAYING_ANIM iChar "KO_skid_back"
        OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_L"
        OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_R"
        OR IS_CHAR_PLAYING_ANIM iChar "knife_hit_3"
        OR IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
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
*/

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
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_h.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx PLAY
    ENDIF
    WAIT 0
RETURN

play_sfx_b:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (iTempVar)
    SWITCH iTempVar
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_c.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_d.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dodge_e.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY
            ENDIF
            BREAK
    ENDSWITCH
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

//-+-- CALL SCM HELPERS
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
