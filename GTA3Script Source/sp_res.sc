// by J16D
// Reservoir Scripts
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Textures
CONST_INT objCrosshair  61
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_res
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu rwCrosshair

GET_PLAYER_CHAR 0 player_actor
REQUEST_ANIMATION "spider"
REQUEST_ANIMATION "mweb"
LOAD_TEXTURE_DICTIONARY scrb
LOAD_SPRITE objCrosshair "ilock"
GET_TEXTURE_FROM_SPRITE objCrosshair (rwCrosshair)
LOAD_ALL_MODELS_NOW

STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_tta_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttb_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttc_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttd_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_tte_InternalThread rwCrosshair
WAIT 0

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 0 //FALSE
            REMOVE_ANIMATION "spider"
            REMOVE_ANIMATION "mweb"
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            REMOVE_TEXTURE_DICTIONARY
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE
}
SCRIPT_END

//-+--- Thread 1
{
// iPhotoBombCamID = 0
sp_tta_InternalThread:      //Reservoir 1
LVAR_INT rwCrosshair    //in
LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2]
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal 
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_ON_MISSION 
                    //Tower
                    x[1] = -2022.26
                    y[1] = 13.982
                    z[1] = 61.60
                    //-- Coords
                    x[0] = x[1] 
                    y[0] = y[1] - 9.982        
                    z[0] = z[1] - 1.0
                    IF IS_CHAR_REALLY_IN_AIR player_actor
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 8.0
                            GOSUB draw_indicator_r1
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                zAngle = 360.0
                                SET_CHAR_HEADING player_actor zAngle
                                GOSUB destroyTwoWebs_r1
                                GOSUB createTwoWebs_r1
                                    zAngle = 360.0
                                    GOSUB animSequence_r1
                                GOSUB destroyTwoWebs_r1       
                                WAIT 500     
                            ENDIF
                        ELSE
                            x[0] = x[1] 
                            y[0] = y[1] + 9.982        
                            z[0] = z[1] - 1.0
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 8.0
                                GOSUB draw_indicator_r1
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    zAngle = 180.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs_r1
                                    GOSUB createTwoWebs_r1
                                        zAngle = 180.0
                                        GOSUB animSequence_r1
                                    GOSUB destroyTwoWebs_r1
                                    WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

animSequence_r1:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    GOSUB playWebSound_r1
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                    //CLEO_CALL addForceToChar 0 player_actor 0.0 3.0 0.0 3.5    //0.0 0.01 0.0
                    //CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    GET_CAMERA_FOV fFov
                    CAMERA_SET_LERP_FOV 105.0 fFov 1 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound_r1
                ENDIF
                GOSUB add_force_to_char_r1
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char_r1:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator_r1:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            DRAW_TEXTURE_PLUS rwCrosshair DRAW_EVENT_BEFORE_HUD (v1 v2) (40.0 40.0) 0.0 0.0 TRUE 0 0 (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs_r1:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs_r1:
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

playSFXSound_r1:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound_r1:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
}

//-+--- Thread 2
{
// iPhotoBombCamID = 1
sp_ttb_InternalThread:      //Reservoir 2
LVAR_INT rwCrosshair    //in
LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2]
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal 
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_ON_MISSION 
                    //Tower
                    x[1] = -2192.0 
                    y[1] = 389.789 
                    z[1] = 64.624
                    //-- Coords
                    x[0] = x[1] - 9.982
                    y[0] = y[1]         
                    z[0] = z[1] - 0.70
                    IF IS_CHAR_REALLY_IN_AIR player_actor
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                            GOSUB draw_indicator_r2
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                zAngle = 270.0
                                SET_CHAR_HEADING player_actor zAngle
                                GOSUB destroyTwoWebs_r2
                                GOSUB createTwoWebs_r2
                                    zAngle = 270.0
                                    GOSUB animSequence_r2
                                GOSUB destroyTwoWebs_r2       
                                WAIT 500     
                            ENDIF
                        ELSE
                            x[0] = x[1] + 9.982  
                            y[0] = y[1]       
                            z[0] = z[1] - 0.70
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                                GOSUB draw_indicator_r2
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    zAngle = 90.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs_r2
                                    GOSUB createTwoWebs_r2
                                        zAngle = 90.0
                                        GOSUB animSequence_r2
                                    GOSUB destroyTwoWebs_r2
                                    WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

animSequence_r2:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    /*WAIT 0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0*/
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    GOSUB playWebSound_r2
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6 //1.2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                    //CLEO_CALL addForceToChar 0 player_actor 0.0 3.0 0.0 3.5    //0.0 0.01 0.0
                    //CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    GET_CAMERA_FOV fFov
                    CAMERA_SET_LERP_FOV 105.0 fFov 1 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound_r2
                ENDIF
                GOSUB add_force_to_char_r2
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char_r2:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator_r2:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            DRAW_TEXTURE_PLUS rwCrosshair DRAW_EVENT_BEFORE_HUD (v1 v2) (40.0 40.0) 0.0 0.0 TRUE 0 0 (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs_r2:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs_r2:
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

playSFXSound_r2:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound_r2:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
}

//-+--- Thread 3
{
// iPhotoBombCamID = 2
sp_ttc_InternalThread:      //Reservoir 3
LVAR_INT rwCrosshair    //in
LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2]
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal 
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_ON_MISSION
                    //Tower
                    x[1] = -1873.89
                    y[1] = 900.735
                    z[1] = 65.2756
                    //-- Coords
                    x[0] = x[1] 
                    y[0] = y[1] - 9.982        
                    z[0] = z[1] - 0.65
                    IF IS_CHAR_REALLY_IN_AIR player_actor
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                            GOSUB draw_indicator_r3
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                zAngle = 360.0
                                SET_CHAR_HEADING player_actor zAngle
                                GOSUB destroyTwoWebs_r3
                                GOSUB createTwoWebs_r3
                                    zAngle = 360.0
                                    GOSUB animSequence_r3
                                GOSUB destroyTwoWebs_r3       
                                WAIT 500     
                            ENDIF
                        ELSE
                            x[0] = x[1] 
                            y[0] = y[1] + 9.982        
                            z[0] = z[1] - 0.65
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                                GOSUB draw_indicator_r3
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    zAngle = 180.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs_r3
                                    GOSUB createTwoWebs_r3
                                        zAngle = 180.0
                                        GOSUB animSequence_r3
                                    GOSUB destroyTwoWebs_r3
                                    WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

animSequence_r3:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    /*
    WAIT 0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0*/
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    GOSUB playWebSound_r3
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6 //1.2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                    //CLEO_CALL addForceToChar 0 player_actor 0.0 3.0 0.0 3.5    //0.0 0.01 0.0
                    //CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    GET_CAMERA_FOV fFov
                    CAMERA_SET_LERP_FOV 105.0 fFov 1 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound_r3
                ENDIF
                GOSUB add_force_to_char_r3
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char_r3:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator_r3:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            DRAW_TEXTURE_PLUS rwCrosshair DRAW_EVENT_BEFORE_HUD (v1 v2) (40.0 40.0) 0.0 0.0 TRUE 0 0 (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs_r3:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs_r3:
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

playSFXSound_r3:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound_r3:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
}

//-+--- Thread 4
{
// iPhotoBombCamID = 3
sp_ttd_InternalThread:      //Reservoir 4
LVAR_INT rwCrosshair    //in
LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2]
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal 
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_ON_MISSION 
                    //Tower
                    x[1] = -1812.37 
                    y[1] = 1039.22 
                    z[1] = 82.0859
                    //-- Coords
                    x[0] = x[1] - 9.982
                    y[0] = y[1]         
                    z[0] = z[1] - 0.65
                    IF IS_CHAR_REALLY_IN_AIR player_actor
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                            GOSUB draw_indicator_r4
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                zAngle = 270.0
                                SET_CHAR_HEADING player_actor zAngle
                                GOSUB destroyTwoWebs_r4
                                GOSUB createTwoWebs_r4
                                    zAngle = 270.0
                                    GOSUB animSequence_r4
                                GOSUB destroyTwoWebs_r4     
                                WAIT 500     
                            ENDIF
                        ELSE
                            x[0] = x[1] + 9.982  
                            y[0] = y[1]       
                            z[0] = z[1] - 0.65
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                                GOSUB draw_indicator_r4
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    zAngle = 90.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs_r4
                                    GOSUB createTwoWebs_r4
                                        zAngle = 90.0
                                        GOSUB animSequence_r4
                                    GOSUB destroyTwoWebs_r4
                                    WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

animSequence_r4:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    /*WAIT 0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0*/
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    GOSUB playWebSound_r4
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6 //1.2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                    //CLEO_CALL addForceToChar 0 player_actor 0.0 3.0 0.0 3.5    //0.0 0.01 0.0
                    //CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    GET_CAMERA_FOV fFov
                    CAMERA_SET_LERP_FOV 105.0 fFov 1 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound_r4
                ENDIF
                GOSUB add_force_to_char_r4
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char_r4:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator_r4:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            DRAW_TEXTURE_PLUS rwCrosshair DRAW_EVENT_BEFORE_HUD (v1 v2) (40.0 40.0) 0.0 0.0 TRUE 0 0 (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs_r4:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs_r4:
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

playSFXSound_r4:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound_r4:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
}

//-+--- Thread 5
{
// iPhotoBombCamID = 4
sp_tte_InternalThread:      //Reservoir 5
LVAR_INT rwCrosshair    //in
LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2]
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal 
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true

WHILE TRUE
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF NOT IS_ON_MISSION
                    //Tower
                    x[1] = -1589.35
                    y[1] = 951.715
                    z[1] = 34.5971
                    //-- Coords
                    x[0] = x[1]
                    y[0] = y[1] - 9.982        
                    z[0] = z[1] - 0.70
                    IF IS_CHAR_REALLY_IN_AIR player_actor
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                            GOSUB draw_indicator_r5
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                zAngle = 360.0
                                SET_CHAR_HEADING player_actor zAngle
                                GOSUB destroyTwoWebs_r5
                                GOSUB createTwoWebs_r5
                                    zAngle = 360.0
                                    GOSUB animSequence_r5
                                GOSUB destroyTwoWebs_r5 
                                WAIT 500     
                            ENDIF
                        ELSE
                            x[0] = x[1] 
                            y[0] = y[1] + 9.982        
                            z[0] = z[1] - 0.70
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                                GOSUB draw_indicator_r5
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    zAngle = 180.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs_r5
                                    GOSUB createTwoWebs_r5
                                        zAngle = 180.0
                                        GOSUB animSequence_r5
                                    GOSUB destroyTwoWebs_r5
                                    WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

animSequence_r5:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    /*
    WAIT 0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    */
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    GOSUB playWebSound_r5
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6 //1.2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                    //CLEO_CALL addForceToChar 0 player_actor 0.0 3.0 0.0 3.5    //0.0 0.01 0.0
                    //CAMERA_SET_LERP_FOV 100.0 105.0 1000 TRUE
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    GET_CAMERA_FOV fFov
                    CAMERA_SET_LERP_FOV 105.0 fFov 1 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound_r5
                ENDIF
                GOSUB add_force_to_char_r5
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char_r5:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator_r5:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            DRAW_TEXTURE_PLUS rwCrosshair DRAW_EVENT_BEFORE_HUD (v1 v2) (40.0 40.0) 0.0 0.0 TRUE 0 0 (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs_r5:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs_r5:
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

playSFXSound_r5:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound_r5:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
}

//-+---------------------------------CALLSCM HELPERS---------------------------------------
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
//CLEO_CALL setCameraOnChar 0 /*coords*/(0.0 0.0 0.0) /*char*/player_actor /*mode*/1    // 1:normal||0:Photo_mode
setCameraOnChar:
    LVAR_FLOAT xIn yIn zIn  //in
    LVAR_INT scplayer // In
    LVAR_INT randomVal // In
    LVAR_FLOAT x[2] y[2] z[2]
    IF randomVal = 0    
        // Default
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 -2.5 0.75) (x[0] y[0] z[0])  //0.0 -2.5 0.75
        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 2.5 0.25) (x[1] y[1] z[1])
        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
    ELSE
        //PHOTO MODE
        x[0] = xIn + 10.0 //10.0
        y[0] = yIn
        z[0] = zIn
        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
        x[1] = xIn
        y[1] = yIn
        z[1] = zIn
        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL addForceToChar 0 player_actor /*xVel*/0.0 /*yVel*/1.0 /*zVel*/1.0 /*amp*/20.0
addForceToChar:
    LVAR_INT scplayer    //in
    LVAR_FLOAT addXvel addYvel addZvel amplitude    //in
    LVAR_FLOAT distance
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer addXvel addYvel addZvel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0] 
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) (distance)
    x[1] /= distance
    y[1] /= distance
    z[1] /= distance
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
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

//-+-----------------------------------------------------------------------------------------
/*
tw_a1
-2192.0 389.789 64.624
90.9726

tw_a2
-1873.89 900.735 65.2756
359.9484

tw_a3
-1812.37 1039.22 82.0859
270.9562

tw_a4
-1589.35 951.715 34.5971
179.9319
*/
