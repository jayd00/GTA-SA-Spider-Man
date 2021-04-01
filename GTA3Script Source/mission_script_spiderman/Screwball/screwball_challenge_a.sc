// by J16D
// Custom Mission (CM)
// Spider-Man Mod for GTA SA c.2019
// ScrewBall Challenge PEM
// Original Version: Shine GUI - Cleo - Sanny Builder for Tuning Mod V2
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// Edited by J16D Version: CLEO - gta3script

/*
WRITE_MEMORY 0xA476AC 4 (1) FALSE //$ONMISSION = 1
READ_MEMORY 0xA476AC 4 FALSE (onmission) 
*/

//-+---CONSTANTS--------------------
//Size
CONST_INT BYTE                 1
CONST_INT WORD                 2
CONST_INT DWORD                4
CONST_INT SIZE_RWMEMORY        8
CONST_INT SIZE_VECTOR          12
CONST_INT SIZE_QUAT            16
CONST_INT SIZE_COLPOINT_DATA   44
CONST_INT SIZE_MATRIX          64
// font style
CONST_INT FONT_GOTHIC          0
CONST_INT FONT_SUBTITLES       1
CONST_INT FONT_MENU            2
CONST_INT FONT_PRICEDOWN       3
//MP3 STATES    // 0:stop / 1:play / 2:pause / 3:resume
CONST_INT STOP      0
CONST_INT PLAY      1
CONST_INT PAUSE     2
CONST_INT RESUME    3
//GLOBAL_CLEO_SHARED_VARS
CONST_INT varStatusSpiderMod    110
CONST_INT varAlternativeSwing   111
CONST_INT varSwingBuilding      112
CONST_INT varFixGround          113
CONST_INT varMouseControl       114
CONST_INT varHUD                116
CONST_INT varLevelChar          117 
CONST_INT varStatusLevelChar    119 
CONST_INT varInMenu             120 
CONST_INT varMusic              121 

//Textures
CONST_INT tCrosshair    60
CONST_INT objCrosshair  61
CONST_INT tPhotoBomb    62
CONST_INT tPBback       63
CONST_INT tPBScrewballNot  64
CONST_INT tPBbar0       65
CONST_INT tPBbar1       66
CONST_INT tPBScrewBallToon  67
CONST_INT tPBbCounter       68
CONST_INT tPBbCounterBlue   69
CONST_INT tPBBackTimer      70
CONST_INT tPBBackScore      71
CONST_INT tPBBackInfo       72
CONST_INT tPBBackScoreB     73
CONST_INT tPBSBack1         74
CONST_INT tPBSBack2         75
CONST_INT tPBSBack3         76
CONST_INT tPBSBack1Active   77
CONST_INT tPBSBack2Active   78
CONST_INT tPBSBack3Active   79
CONST_INT iconSuccess       80

MISSION_START
	GOSUB mission_start_init
	IF HAS_DEATHARREST_BEEN_EXECUTED
		GOSUB mission_failed
	ENDIF
	GOSUB mission_cleanup
MISSION_END

{
//-+---------------------------------Mission Start-(0)-------------------------------------
LVAR_INT flag_player_on_mission
LVAR_INT flag_screwball_mission_passed
// usefull vars
LVAR_INT player_actor
CONST_INT player 0
// Variables for mission
LVAR_INT baseObject iWebActor iWebActorR 
LVAR_FLOAT x[3] y[3] z[3]
LVAR_FLOAT v1 v2 sizeX sizeY szBarX szBarY
LVAR_FLOAT fDistance fCharSpeed currentTime zAngle
LVAR_INT randomVal
LVAR_INT flag_photo_mode flag_was_key_pressed
LVAR_INT iLanguage
LVAR_INT iSfx[3]
LVAR_FLOAT fScoreCounter
LVAR_INT iScoreCounter
LVAR_INT counter
LVAR_FLOAT cx[5] cy[5] cz[5] czAngle[10]
LVAR_INT iCheckpoint[5] iBlip[5]
LVAR_INT flag_event_msg_voice	// 0:false||1:true
LVAR_INT iCountDownNumber
LVAR_INT iTotalScore iPlusScore iMissionScore iExtraTimeScore
LVAR_FLOAT fTotalScore
LVAR_INT iTotalTime cTimerb_A iMinutes iSeconds iRemainTime 
LVAR_INT iPhotoBombCamID
LVAR_INT ultimateScore
LVAR_INT spectacularScore
LVAR_INT amazingScore
LVAR_INT toggleMusic

mission_start_init:
// Set
flag_player_on_mission = 1
REGISTER_MISSION_GIVEN
WAIT 0

SCRIPT_NAME pem

WRITE_MEMORY 0xA476AC 4 (flag_player_on_mission) FALSE 	// $ONMISSION = 1
GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 1     // 0:false||1:true
flag_was_key_pressed = 0    // 0:false||1:true
fScoreCounter = 0.0
randomVal = 0
iMissionScore = 0
iExtraTimeScore = 0
iTotalScore = 0
ultimateScore = 13500   // Minimum score 13500  Ultimate level
spectacularScore = 11500   // Minimum score 11500  Spectacular level
amazingScore = 9500    // Minimum score 9500   Amazing Level
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "LANG" (iLanguage)   // 0:SPA ||1:ENG

    //START
    DO_FADE 1000 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
    WAIT 100
    // load files
    GOSUB loadGeneralFiles
    // get coords info
    GOSUB getGeneralCoords
    //set char position
    REQUEST_COLLISION -2177.4756 -118.3089
    WAIT 5
    CLEO_CALL SetCharPosSimple 0 player_actor (-2177.4756 -118.3089 61.81)
    SET_CHAR_HEADING player_actor 308.3596
    WAIT 5
    //TASK_TOGGLE_DUCK player_actor TRUE
    FREEZE_CHAR_POSITION player_actor TRUE
    SET_PLAYER_CONTROL 0 FALSE
    SET_CHAR_HEADING player_actor 308.3596
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
    //create web
    GOSUB createTwoWebs
    WAIT 100
    // add marker and check point
    counter = 0
    GOSUB addRaceCheckpoint
    WAIT 0
    DO_FADE 1000 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
    WAIT 0
    //PLAY BACKGROUND MUSIC

    ///audio start counter
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION x[0] y[0] z[0] 1056
    iCountDownNumber = 3
    timera = 0
    WHILE 1000 > timera
        GOSUB draw_countDown_number
        WAIT 0
    ENDWHILE
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION x[0] y[0] z[0] 1056
    iCountDownNumber = 2
    timera = 0
    WHILE 1000 > timera
        GOSUB draw_countDown_number
        WAIT 0
    ENDWHILE
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION x[0] y[0] z[0] 1056
    iCountDownNumber = 1
    timera = 0
    WHILE 1000 > timera
        GOSUB draw_countDown_number
        WAIT 0
    ENDWHILE
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION x[0] y[0] z[0] 1057
    WAIT 0
    FREEZE_CHAR_POSITION player_actor FALSE
    SET_PLAYER_CONTROL 0 TRUE

    //timer 
    iTotalTime = 110000  // 110 sec
    timerb = 0      //timer
    //first     (0)
    iPhotoBombCamID = 0
    flag_event_msg_voice = 0    // 0:false||1:true
    checkpointA_loop:
    IF IS_PLAYER_PLAYING player

        IF IS_KEY_PRESSED VK_KEY_N
            WHILE IS_KEY_PRESSED VK_KEY_N
                GOSUB draw_total_score
                GOSUB draw_key_press
                WAIT 0
            ENDWHILE
        ENDIF

        GOSUB draw_timer
        //origi - cx[0] cy[0] cz[0] czAngle[0]	|| czAngle[5]
        x[1] = cx[0] 	//-2022.26
        y[1] = cy[0]	//13.982
        z[1] = cz[0]	//61.60
        czAngle[5] = czAngle[0]
        czAngle[5] -= 180.0
        //-- Coords
        x[0] = x[1] 
        y[0] = y[1] - 9.982        
        z[0] = z[1] - 1.0
        IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[1] y[1] z[1]) (60.0 60.0 60.0) FALSE 
            IF flag_event_msg_voice = 0 	// 0:false||1:true
                flag_event_msg_voice = 1 	// 0:false||1:true
                GOSUB sfxStartDialogue
            ENDIF
            GOSUB draw_photo_bomb
        ENDIF

        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
            IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                GOSUB draw_indicator
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                    zAngle = czAngle[0]		//360.0
                    SET_CHAR_HEADING player_actor zAngle
                        zAngle = czAngle[0]		//360.0
                        GOSUB animSequence
                    GOTO reset_vars_to_checkpointB
                ENDIF
            ELSE
                x[0] = x[1] 
                y[0] = y[1] + 9.982        
                z[0] = z[1] - 1.0
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                    GOSUB draw_indicator
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                        zAngle = czAngle[5] 	//180.0
                        SET_CHAR_HEADING player_actor zAngle
                            zAngle = czAngle[5] 	//180.0
                            GOSUB animSequence
                        GOTO reset_vars_to_checkpointB
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
        IF timerb > iTotalTime
            GOTO mission_failed
        ENDIF
    ELSE
        GOTO mission_failed
    ENDIF
    WAIT 0
    GOTO checkpointA_loop

    // Second   (1)
    reset_vars_to_checkpointB:
    iPlusScore =# fTotalScore
    iMissionScore += iPlusScore

    counter = 0
    GOSUB deleteRaceCheckpoint
    GOSUB sfxEndDialogue
    flag_event_msg_voice = 0 	// 0:false||1:true

    //create next checkpoint
    iPhotoBombCamID = 1
    counter = 1
    GOSUB addRaceCheckpoint

    checkpointB_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB draw_timer
        //tw_a1 - cx[1] cy[1] cz[1] czAngle[1]	|| czAngle[6]
        x[1] = cx[1]
        y[1] = cy[1]
        z[1] = cz[1]
        czAngle[6] = czAngle[1]
        czAngle[6] += 180.0
        //-- Coords
        x[0] = x[1] - 9.982
        y[0] = y[1]         
        z[0] = z[1] - 0.7
        IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[1] y[1] z[1]) (60.0 60.0 60.0) FALSE 
            IF flag_event_msg_voice = 0 	// 0:false||1:true
                flag_event_msg_voice = 1 	// 0:false||1:true
                GOSUB sfxStartDialogue
            ENDIF
            GOSUB draw_photo_bomb
        ENDIF
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
            IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                GOSUB draw_indicator
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                    zAngle = czAngle[6]	
                    SET_CHAR_HEADING player_actor zAngle
                        zAngle = czAngle[6]	
                        GOSUB animSequence
                    GOTO reset_vars_to_checkpointC
                ENDIF
            ELSE
                x[0] = x[1] + 9.982  
                y[0] = y[1]
                z[0] = z[1] - 0.7
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                    GOSUB draw_indicator
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                        zAngle = czAngle[1] 
                        SET_CHAR_HEADING player_actor zAngle
                            zAngle = czAngle[1]
                            GOSUB animSequence
                        GOTO reset_vars_to_checkpointC
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
        IF timerb > iTotalTime
            GOTO mission_failed
        ENDIF
    ELSE
        GOTO mission_failed
    ENDIF
        WAIT 0
    GOTO checkpointB_loop

    // Third    (2)
    reset_vars_to_checkpointC:
    iPlusScore =# fTotalScore
    iMissionScore += iPlusScore
    counter = 1
    GOSUB deleteRaceCheckpoint
    GOSUB sfxEndDialogue
    flag_event_msg_voice = 0 	// 0:false||1:true

    iPhotoBombCamID = 2
    counter = 2
    GOSUB addRaceCheckpoint

    checkpointC_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB draw_timer
        //tw_a2 - cx[2] cy[2] cz[2] czAngle[2]	|| czAngle[7]
        x[1] = cx[2]
        y[1] = cy[2]
        z[1] = cz[2]
        czAngle[7] = czAngle[2]
        czAngle[7] -= 180.0
        //-- Coords
        x[0] = x[1] 
        y[0] = y[1] - 9.982        
        z[0] = z[1] - 0.65
        IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[1] y[1] z[1]) (60.0 60.0 60.0) FALSE 
            IF flag_event_msg_voice = 0 	// 0:false||1:true
                flag_event_msg_voice = 1 	// 0:false||1:true
                GOSUB sfxStartDialogue
            ENDIF
            GOSUB draw_photo_bomb
        ENDIF
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
            IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                GOSUB draw_indicator
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                    zAngle = czAngle[2]	
                    SET_CHAR_HEADING player_actor zAngle
                        zAngle = czAngle[2]	
                        GOSUB animSequence
                    GOTO reset_vars_to_checkpointD
                ENDIF
            ELSE
                x[0] = x[1] 
                y[0] = y[1] + 9.982        
                z[0] = z[1] - 0.65
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                    GOSUB draw_indicator
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                        zAngle = czAngle[7] 
                        SET_CHAR_HEADING player_actor zAngle
                            zAngle = czAngle[7]
                            GOSUB animSequence
                        GOTO reset_vars_to_checkpointD
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
        IF timerb > iTotalTime
            GOTO mission_failed
        ENDIF
    ELSE
        GOTO mission_failed
    ENDIF
        WAIT 0
    GOTO checkpointC_loop


    // Fourth   (3)
    reset_vars_to_checkpointD:
    iPlusScore =# fTotalScore
    iMissionScore += iPlusScore

    counter = 2
    GOSUB deleteRaceCheckpoint
    GOSUB sfxEndDialogue
    flag_event_msg_voice = 0 	// 0:false||1:true

    iPhotoBombCamID = 3
    counter = 3
    GOSUB addRaceCheckpoint

    checkpointD_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB draw_timer
        //tw_a3 - cx[3] cy[3] cz[3] czAngle[3]	|| czAngle[8]
        x[1] = cx[3]
        y[1] = cy[3]
        z[1] = cz[3]
        czAngle[8] = czAngle[3]
        czAngle[8] -= 180.0
        //-- Coords
        x[0] = x[1] - 9.982
        y[0] = y[1]  
        z[0] = z[1] - 0.65
        IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[1] y[1] z[1]) (60.0 60.0 60.0) FALSE 
            IF flag_event_msg_voice = 0 	// 0:false||1:true
                flag_event_msg_voice = 1 	// 0:false||1:true
                GOSUB sfxStartDialogue
            ENDIF
            GOSUB draw_photo_bomb
        ENDIF
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
            IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                GOSUB draw_indicator
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                    zAngle = czAngle[3]	
                    SET_CHAR_HEADING player_actor zAngle
                        zAngle = czAngle[3]	
                        GOSUB animSequence
                    GOTO reset_vars_to_checkpointE
                ENDIF
            ELSE
                x[0] = x[1] + 9.982  
                y[0] = y[1]
                z[0] = z[1] - 0.65
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                    GOSUB draw_indicator
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                        zAngle = czAngle[8] 
                        SET_CHAR_HEADING player_actor zAngle
                            zAngle = czAngle[8]
                            GOSUB animSequence
                        GOTO reset_vars_to_checkpointE
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
        IF timerb > iTotalTime
            GOTO mission_failed
        ENDIF
    ELSE
        GOTO mission_failed
    ENDIF
        WAIT 0
    GOTO checkpointD_loop

    //Fifth     (4)
    reset_vars_to_checkpointE:
    iPlusScore =# fTotalScore
    iMissionScore += iPlusScore

    counter = 3
    GOSUB deleteRaceCheckpoint
    GOSUB sfxEndDialogue
    flag_event_msg_voice = 0 	// 0:false||1:true

    iPhotoBombCamID = 4
    counter = 4
    GOSUB addRaceCheckpoint

    checkpointE_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB draw_timer
        //tw_a4 - cx[4] cy[4] cz[4] czAngle[4]	|| czAngle[9]
        x[1] = cx[4]
        y[1] = cy[4]
        z[1] = cz[4]
        czAngle[9] = czAngle[4]
        czAngle[9] -= 180.0
        //-- Coords
        x[0] = x[1] 
        y[0] = y[1] - 9.982        
        z[0] = z[1] - 0.70
        IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[1] y[1] z[1]) (60.0 60.0 60.0) FALSE 
            IF flag_event_msg_voice = 0 	// 0:false||1:true
                flag_event_msg_voice = 1 	// 0:false||1:true
                GOSUB sfxStartDialogue
            ENDIF
            GOSUB draw_photo_bomb
        ENDIF
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
            IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                GOSUB draw_indicator
                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                    zAngle = czAngle[9]	
                    SET_CHAR_HEADING player_actor zAngle
                        zAngle = czAngle[9]	
                        GOSUB animSequence
                    GOTO finish_mission
                ENDIF
            ELSE
                x[0] = x[1] 
                y[0] = y[1] + 9.982        
                z[0] = z[1] - 0.70
                IF LOCATE_CHAR_ANY_MEANS_3D player_actor (x[0] y[0] z[0]) (10.0 10.0 10.0) FALSE 
                    GOSUB draw_indicator
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                        zAngle = czAngle[4] 
                        SET_CHAR_HEADING player_actor zAngle
                            zAngle = czAngle[4]
                            GOSUB animSequence
                        GOTO finish_mission
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
        IF timerb > iTotalTime
            GOTO mission_failed
        ENDIF
    ELSE
        GOTO mission_failed
    ENDIF
        WAIT 0
    GOTO checkpointE_loop

//Finish
finish_mission:
iPlusScore =# fTotalScore
iMissionScore += iPlusScore
iRemainTime = iTotalTime
iRemaintime -= timerb

IF iRemainTime > 30000  // 30 sec
    iExtraTimeScore = 2300
ELSE
    IF iRemainTime > 20000  // 20 sec
        iExtraTimeScore = 1500
    ELSE
        IF iRemainTime > 10000  // 10 sec
            iExtraTimeScore = 500
        ELSE
            iExtraTimeScore = 0
        ENDIF        
    ENDIF
ENDIF
iTotalScore = iMissionScore
iTotalScore += iExtraTimeScore
LVAR_INT actualScore
READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "score" "sc0" (actualScore)
IF iTotalScore > actualScore
    WRITE_INT_TO_INI_FILE iTotalScore "CLEO\SpiderJ16D\config.ini" "score" "sc0"
ENDIF
counter = 4
GOSUB deleteRaceCheckpoint
GOSUB destroyTwoWebs
//GOSUB sfxEndDialogue
GOSUB sfxFinishMissionDialogue
flag_event_msg_voice = 0 	// 0:false||1:true
WAIT 1000
GOTO mission_passed
//-+-----------------------------------------------------------------------------------------

//-+---------------------------------Mission failed---------------------------------------
mission_failed:
	PRINT_BIG (M_FAIL) 1000 1 	// ~r~MISSION FAILED!
	flag_screwball_mission_passed += 0
    counter = 0
    WHILE 5 > counter
        IF DOES_BLIP_EXIST iBlip[counter]
            REMOVE_BLIP iBlip[counter]
            DELETE_CHECKPOINT iCheckpoint[counter]
        ENDIF
        counter += 1
    ENDWHILE
	MISSION_HAS_FINISHED
	WHILE NOT IS_PLAYER_PLAYING player_actor
		WAIT 0
	ENDWHILE
RETURN
//-+-----------------------------------------------------------------------------------------

//-+---------------------------------mission cleanup---------------------------------------

mission_cleanup:
	flag_player_on_mission = 0
	WRITE_MEMORY 0xA476AC 4 (flag_player_on_mission) FALSE 	// $ONMISSION = 0
    REMOVE_ANIMATION "spider"
    REMOVE_ANIMATION "mweb"
    REMOVE_AUDIO_STREAM iSfx[0]
    REMOVE_AUDIO_STREAM iSfx[1]
    LVAR_INT sfxState
    GET_AUDIO_STREAM_STATE iSfx[2] (sfxState)
    WHILE sfxState = 1  // Playing
        GET_AUDIO_STREAM_STATE iSfx[2] (sfxState)
        WAIT 0
    ENDWHILE
    REMOVE_AUDIO_STREAM iSfx[2]
    USE_TEXT_COMMANDS TRUE
    USE_TEXT_COMMANDS FALSE
    WAIT 0
    REMOVE_TEXTURE_DICTIONARY
	MISSION_HAS_FINISHED
RETURN
//-+-----------------------------------------------------------------------------------------

//-+---------------------------------mission passed---------------------------------------
mission_passed:
	flag_screwball_mission_passed += 1
	PLAYER_MADE_PROGRESS 1

    //Give Rewards    
    ultimateScore = 13500   // Minimum score 13500  Ultimate level
    spectacularScore = 11500   // Minimum score 11500  Spectacular level
    amazingScore = 9500    // Minimum score 9500   Amazing Level
   
    LVAR_INT reward1 reward2 reward3 iExperienceReward
    iExperienceReward = 0
    CLEO_CALL getRewardsInfo 0 /*mission*/0 /*rewards*/ reward1 reward2 reward3

    IF iTotalScore > amazingScore
        IF reward1 = 0
            iExperienceReward += 50
        ENDIF
        reward1 = 1
    ELSE
        reward1 = 0
    ENDIF
    IF iTotalScore > spectacularScore
        IF reward2 = 0
            iExperienceReward += 250
        ENDIF
        reward2 = 1
    ELSE
        reward2 = 0
    ENDIF
    IF iTotalScore > ultimateScore
        IF reward3 = 0
            iExperienceReward += 450
        ENDIF
        reward3 = 1
    ELSE
        reward3 = 0
    ENDIF
    CLEO_CALL setRewardsInfo 0 /*mission*/0 /*rewards*/ reward1 reward2 reward3
    SET_CLEO_SHARED_VAR varStatusLevelChar iExperienceReward

    SET_PLAYER_JUMP_BUTTON player FALSE
    WHILE TRUE  
        GOSUB draw_total_score
        GOSUB draw_key_press
        IF IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
            WHILE IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                WAIT 0
            ENDWHILE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_PLAYER_JUMP_BUTTON player TRUE
    WAIT 1000
RETURN
//-+-----------------------------------------------------------------------------------------


//-+---------------------------------GOSUB HELPERS---------------------------------------

animSequence:
    fScoreCounter = 0.0
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    CLEO_CALL SetCharPosSimple 0 player_actor (x[0] y[0] z[0])

    IF flag_photo_mode = 0    
        fCharSpeed += 10.0
        CLEO_CALL max_min_value_float 0 fCharSpeed 12.0 6.0 (fCharSpeed) 
    ELSE        //Default
        fCharSpeed += 13.0
        CLEO_CALL max_min_value_float 0 fCharSpeed 13.0 7.0 (fCharSpeed) 
    ENDIF

    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.2
    SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") STOP
    SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") STOP

    ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        SET_CHAR_HEADING player_actor zAngle
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
        SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime

        IF flag_photo_mode = 1  // 0:false||1:true
            IF currentTime > 0.0        // 0/90
            AND 0.944 > currentTime     // 85/90

                GOSUB draw_screen_items
                GOSUB draw_screen_counter
                GOSUB draw_interact_key_press

            ENDIF
            IF currentTime > 0.444      // 40/90
            AND 0.556 > currentTime     // 50/90
                
                SET_TIME_SCALE 0.35
                IF randomVal = 0
                    GOSUB playSfxBar
                ENDIF

                IF flag_was_key_pressed = 0 // 0:false||1:true
                    CLEO_CALL linearInterpolation 0 (0.444 0.556 currentTime) (0.0 2500.0) (fScoreCounter)
                    CLEO_CALL barFunc 0 fScoreCounter v1 (szBarX szBarY)
                    SET_SPRITES_DRAW_BEFORE_FADE FALSE
                    DRAW_RECT (v1 129.75) (szBarX 6.45) (205 61 155 235)
                    USE_TEXT_COMMANDS FALSE
                    //PRINT_FORMATTED_NOW "pos:%f.00 xs:%f.00 ys:%f.00" 100 v1 sizeX sizeY  //debug

                    IF  IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~
                        //IS_BUTTON_PRESSED PAD1 CIRCLE  // ~k~~PED_FIREWEAPON~
                        
                        flag_was_key_pressed = 1 // 0:false||1:true
                        fTotalScore = fScoreCounter

                        IF randomVal = 1
                            GOSUB playSfxPhoto
                        ENDIF
                        SET_TIME_SCALE 0.0
                        TAKE_PHOTO 1
                        timera = 0
                        WHILE 200 > timera
                            SET_CHAR_HEADING player_actor zAngle
                            GOSUB add_force_to_char
                            GOSUB draw_screen_items
                            GOSUB draw_screen_counter
                            USE_TEXT_COMMANDS FALSE
                            DRAW_RECT (v1 129.75) (szBarX 6.45) (205 61 155 235)          
                            //PRINT_FORMATTED_NOW "TIMEA:%i" 1 timera  //debug                  
                            WAIT 0
                        ENDWHILE

                        timera = 0
                        WHILE 700 > timera
                            SET_CHAR_HEADING player_actor zAngle
                            GOSUB add_force_to_char
                            GOSUB draw_screen_items
                            IF fScoreCounter >= 2290.0
                                GOSUB draw_screwball_toon
                                IF randomVal = 2
                                    GOSUB playSfxPerfectScore
                                ENDIF
                            ELSE
                                GOSUB draw_screen_counter
                            ENDIF
                            USE_TEXT_COMMANDS FALSE
                            DRAW_RECT (v1 129.75) (szBarX 6.45) (205 61 155 235)
                            //PRINT_FORMATTED_NOW "TIMEB:%i" 1 timera  //debug
                            WAIT 0
                        ENDWHILE
                        SET_TIME_SCALE 1.0

                    ENDIF

                ENDIF

            ELSE
                IF flag_was_key_pressed = 0 // 0:false||1:true
                    fScoreCounter = 0.0
                    fTotalScore = fScoreCounter
                ENDIF
                SET_TIME_SCALE 1.0

            ENDIF

        ENDIF

        IF 0.201 > currentTime
            IF flag_photo_mode = 0    //Smooth 
                CLEO_CALL addForceToChar 0 player_actor 0.0 0.01 0.0 3.5
                CAMERA_SET_LERP_FOV 100.0 105.0 500 1   //105.0 100.0
            ELSE                //Default  (for photo mode)          
                CLEO_CALL SetCharPosSimple 0 player_actor (x[0] y[0] z[0])
            ENDIF

        ELSE
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode iPhotoBombCamID   // Mode||1:normal||0:Photo_mode

            IF IS_OBJECT_ATTACHED baseObject
                DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
            ENDIF
            GOSUB add_force_to_char
            IF  currentTime > 0.778
                BREAK
            ENDIF
        ENDIF

        WAIT 0
    ENDWHILE

    SET_OBJECT_COORDINATES baseObject (0.0 0.0 -15.0)
    flag_was_key_pressed = 0 // 0:false||1:true
    randomVal = 0
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
    CAMERA_SET_LERP_FOV 70.0 75.0 500 1   //105.0 100.0

RETURN

//-+---------------------------checkpoints--------------------------------------
addRaceCheckpoint:
    IF NOT DOES_BLIP_EXIST iBlip[counter]
        ADD_BLIP_FOR_COORD (cx[counter] cy[counter] cz[counter]) (iBlip[counter])
        CHANGE_BLIP_COLOUR iBlip[counter] RED
        CHANGE_BLIP_SCALE iBlip[counter] 3
        SET_BLIP_AS_FRIENDLY iBlip[counter] 1
        CHANGE_BLIP_DISPLAY iBlip[counter] BLIP_ONLY
        CREATE_CHECKPOINT 1 (cx[counter] cy[counter] cz[counter]) (cx[counter] cy[counter] cz[counter]) 1.0 (iCheckpoint[counter])
    ENDIF
RETURN

deleteRaceCheckpoint:
    IF DOES_BLIP_EXIST iBlip[counter]
        REMOVE_BLIP iBlip[counter]
        DELETE_CHECKPOINT iCheckpoint[counter]
    ENDIF
RETURN

//-+-------------------------------------------------------------------------

add_force_to_char:
    IF  0.722 > currentTime
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 > currentTime
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

//-+----------------------------------draw codes-------------------------------------
GUI_TextFormat_BigItemText_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.48 2.32
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 254 251 255
    SET_TEXT_EDGE 1 (65 0 82 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_BigItem_Colour_win:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.54 2.60
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 246 255 255 255
    SET_TEXT_EDGE 1 (66 43 173 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_Counter_race_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.48 2.32
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_timer_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.3 1.45
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleSmall_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleMedium_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleBig_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.25 1.2084
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_ScoreLeft_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.692
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleScore_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_TitleScoreMedium_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.16 0.7734
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

draw_photo_bomb:
    IF flag_photo_mode = 1  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CLEO_CALL getScreenXYFrom3DCoords 0 (x[1] y[1] z[1]) (v1 v2)
            CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (25.0 25.0) (sizeX sizeY)
            DRAW_SPRITE tPhotoBomb (v1 v2) (sizeX sizeY) (255 255 255 225)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

draw_indicator:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CLEO_CALL getScreenXYFrom3DCoords 0 (x[1] y[1] z[1]) (v1 v2)
            CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (30.0 30.0) (sizeX sizeY)
            DRAW_SPRITE objCrosshair (v1 v2) (sizeX sizeY) (255 255 255 200)
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

draw_screen_items:
    CLEO_CALL getCurrentResolution 0 (sizeX sizeY)
    CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (sizeX sizeY) (sizeX sizeY)
    DRAW_SPRITE tPBback (320.0 224.0) (sizeX sizeY) (255 255 255 235)
    CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (900.0 180.0) (sizeX sizeY)
    DRAW_SPRITE tPBbar0 (320.0 130.0) (sizeX sizeY) (255 255 255 235)
    USE_TEXT_COMMANDS FALSE
RETURN

draw_screen_counter:
    GOSUB GUI_TextFormat_BigItemText_Colour
    iScoreCounter =# fScoreCounter
    DISPLAY_TEXT_WITH_NUMBER (560.0 237.0) J16D121 iScoreCounter    //~1~
    GOSUB GUI_TextFormat_BigItemText_Colour
    DISPLAY_TEXT (560.0 259.0) J16D80   // OMGs
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (100.0 100.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBbCounter (560.0 258.0) (sizeX sizeY) (255 255 255 235)
    USE_TEXT_COMMANDS FALSE
RETURN

draw_screwball_toon:
    GOSUB GUI_TextFormat_BigItem_Colour_win
    iScoreCounter =# fScoreCounter
    DISPLAY_TEXT_WITH_NUMBER (560.0 237.0) J16D121 iScoreCounter    //~1~
    GOSUB GUI_TextFormat_BigItem_Colour_win
    DISPLAY_TEXT (560.0 259.0) J16D80   // OMGs
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (100.0 100.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBbCounterBlue (560.0 258.0) (sizeX sizeY) (255 255 255 235)

    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (100.0 116.18) (sizeX sizeY)
    DRAW_SPRITE tPBScrewBallToon (560.0 189.0) (sizeX sizeY) (255 255 255 255)
    USE_TEXT_COMMANDS FALSE
RETURN

draw_timer:
    iMinutes = 0
    iSeconds = iTotalTime   //25
    cTimerb_A = timerb          //28
    iSeconds /= 1000
    cTimerb_A /= 1000
    iSeconds -= cTimerb_A
    WHILE iSeconds > 60 
        IF iSeconds >= 60
            iMinutes += 1
            iSeconds += -60
        ENDIF
    ENDWHILE
    iSeconds = iSeconds
    IF 10 > iSeconds
        GOSUB GUI_TextFormat_timer_Colour
        DISPLAY_TEXT_WITH_2_NUMBERS (55.0 138.0) J16D86 iMinutes iSeconds  //~1~:0~1~    
    ELSE
        GOSUB GUI_TextFormat_timer_Colour
        DISPLAY_TEXT_WITH_2_NUMBERS (55.0 138.0) J16D87 iMinutes iSeconds  //~1~:~1~        
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (55.0 135.0) (40.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 83 11 0.0     // EMP Challenge
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (75.0 60.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackScore (54.0 141.0) (sizeX sizeY) (255 255 255 235)   //tPBBackTimer

    //draw score
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (62.0 180.0) (40.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 83 9 0.0     // EMP Challenge
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (45.0 196.0) (40.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 84 10 0.0    // SCORE
    CLEO_CALL GUI_DrawBox_WithNumber 0 (70.0 191.0) (40.0 15.0) (255 255 255 0) 121 4 0.0 iMissionScore  //~1~

    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (95.0 75.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackScore (60.0 190.0) (sizeX sizeY) (255 255 255 235)
    USE_TEXT_COMMANDS FALSE
RETURN

draw_countDown_number:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (320.0 120.0) (50.0 10.0) (16 43 52 200) (0.5) (1 1 1 1) (255 255 255 200) 91 12 0.0 // GET READY
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (350.0 127.5) (10.0 15.0) (16 43 52 0) (0.5) (1 0 0 0) (255 255 255 200) -1 -1 0.0   //LINE RIGHT
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (290.0 127.5) (10.0 15.0) (16 43 52 0) (0.5) (1 0 0 0) (255 255 255 200) -1 -1 0.0   //LINE LEFT
    GOSUB GUI_TextFormat_Counter_race_Colour
    DISPLAY_TEXT_WITH_NUMBER (320.0 145.0) J16D121 iCountDownNumber    //~1~
    USE_TEXT_COMMANDS FALSE
RETURN

draw_total_score:
    //draw Total score
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (50.5 85.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 83 1 0.0  // EMP Challenge
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (57.5 85.0) (65.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0  //SIDES_LINES

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 95.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 85 2 0.0  // SCORE SUMMARY

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 110.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 88 3 0.0  // Time Bonus
    CLEO_CALL GUI_DrawBox_WithNumber 0 (115.0 105.0) (50.0 15.0) (255 255 255 0) 121 4 0.0 iExtraTimeScore  //~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (60.0 125.0) (50.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 89 3 0.0  // Photobomb Bonus
    CLEO_CALL GUI_DrawBox_WithNumber 0 (115.0 120.0) (50.0 15.0) (255 255 255 0) 121 4 0.0 iMissionScore    //~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 140.0) (30.0 12.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 82 2 0.0  // TOTAL SCORE
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (115.0 140.0) (30.0 12.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0  //SIDES_LINES
    CLEO_CALL GUI_DrawBox_WithNumber 0 (115.0 150.0) (50.0 15.0) (255 255 255 0) 121 4 0.0 iTotalScore    //~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 160.0) (90.0 20.0) (255 255 255 0) (0.75) (0 0 1 0) (255 255 255 250) -1 -1 0.0  //SIDES_LINES division

    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (250.0 250.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackInfo (79.0 165.0) (sizeX sizeY) (255 255 255 235)
    IF reward1 = 1
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack2Active (39.0 220.0) (sizeX sizeY) (255 255 255 235)    
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (15.0 15.0) (sizeX sizeY)
        DRAW_SPRITE iconSuccess (39.0 220.0) (sizeX sizeY) (255 255 255 235)
    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack2 (39.0 220.0) (sizeX sizeY) (255 255 255 235)      
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (39.0 200.0) (30.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 92 13 0.0  // AMAZING~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 (39.0 185.0) (30.0 15.0) (255 255 255 0) 121 7 0.0 amazingScore    //~1~
    IF reward2 = 1 
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack1Active (79.0 220.0) (sizeX sizeY) (255 255 255 235)
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (15.0 15.0) (sizeX sizeY)
        DRAW_SPRITE iconSuccess (79.0 220.0) (sizeX sizeY) (255 255 255 235)
    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack1 (79.0 220.0) (sizeX sizeY) (255 255 255 235)  
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (79.0 200.0) (30.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 93 13 0.0  // SPECTACULAR~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 (79.0 185.0) (30.0 15.0) (255 255 255 0) 121 7 0.0 spectacularScore    //~1~
    IF reward3 = 1  
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack3Active (119.0 220.0) (sizeX sizeY) (255 255 255 235)
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (15.0 15.0) (sizeX sizeY)
        DRAW_SPRITE iconSuccess (119.0 220.0) (sizeX sizeY) (255 255 255 235)
    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (50.0 90.0) (sizeX sizeY)
        DRAW_SPRITE tPBSBack3 (119.0 220.0) (sizeX sizeY) (255 255 255 235)
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (119.0 200.0) (30.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 94 13 0.0  // ULTIMATE~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 (119.0 185.0) (30.0 15.0) (255 255 255 0) 121 7 0.0 ultimateScore    //~1~

    USE_TEXT_COMMANDS FALSE      
RETURN

draw_key_press:
    CONST_INT JOYPAD 0
    CONST_INT MOUSE 1
    LVAR_INT idGXT inputType
    CLEO_CALL getInputType 0 (inputType)  ///0=joypad; 1=mouse
    IF inputType = JOYPAD   
        idGXT = 154     // ~k~~PED_JUMPING~ CONTINUE
    ELSE
        IF inputType = MOUSE
            idGXT = 164 // ~q~ CONTINUE
        ENDIF
    ENDIF
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (167.0 20.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (78.0 288.0) (sizeX sizeY) (19 18 13 100) (1.0) (0 0 0 0) (255 255 253 230) idGXT 9 0.0
    USE_TEXT_COMMANDS FALSE   
RETURN

draw_interact_key_press:
    CLEO_CALL getInputType 0 (inputType)  ///0=joypad; 1=mouse
    IF inputType = JOYPAD   
        idGXT = 156     // ~k~~PED_CYCLE_WEAPON_RIGHT~
    ELSE
        IF inputType = MOUSE
            idGXT = 166 // ~v~
        ENDIF
    ENDIF
    //SET_SPRITES_DRAW_BEFORE_FADE TRUE
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (490.0 129.75) (0.0 0.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) idGXT 8 0.0
    USE_TEXT_COMMANDS FALSE
RETURN


//-+---------------------------SFX-------------------------------------------
playSfxBar:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\sfx_pba.mp3"
        REMOVE_AUDIO_STREAM iSfx[0]
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sfx_pba.mp3" (iSfx[0])
        SET_AUDIO_STREAM_STATE iSfx[0] PLAY 
        SET_AUDIO_STREAM_VOLUME iSfx[0] 0.5
        randomVal = 1
    ENDIF
RETURN
playSfxPhoto:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\sfx_pbb.mp3"
        REMOVE_AUDIO_STREAM iSfx[0]
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sfx_pbb.mp3" (iSfx[0])
        SET_AUDIO_STREAM_STATE iSfx[0] PLAY
        SET_AUDIO_STREAM_VOLUME iSfx[0] 0.5
        randomVal = 2
    ENDIF
RETURN               
playSfxPerfectScore:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\sfx_pbc.mp3"
        REMOVE_AUDIO_STREAM iSfx[0]
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sfx_pbc.mp3" (iSfx[0])
        SET_AUDIO_STREAM_STATE iSfx[0] PLAY 
        SET_AUDIO_STREAM_VOLUME iSfx[0] 0.7
        randomVal = 3
    ENDIF
RETURN

sfxStartDialogue:
    LVAR_INT iRandomSfxSound
    SWITCH iLanguage    // 0:SPA ||1:ENG
        CASE 0  //SPA
            REMOVE_AUDIO_STREAM iSfx[1]
            GENERATE_RANDOM_INT_IN_RANGE 0 4 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_A.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_B.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    REMOVE_AUDIO_STREAM iSfx[1]                    
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_C.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF 
                    BREAK
                CASE 3
                    REMOVE_AUDIO_STREAM iSfx[1]
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_D.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF                 
                    BREAK
            ENDSWITCH
            BREAK
        CASE 1  //ENG
            REMOVE_AUDIO_STREAM iSfx[1]
            GENERATE_RANDOM_INT_IN_RANGE 0 4 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_A.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_B.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    REMOVE_AUDIO_STREAM iSfx[1]                    
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_C.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF 
                    BREAK
                CASE 3
                    REMOVE_AUDIO_STREAM iSfx[1]
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_D.mp3" (iSfx[1])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[1] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[1] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[1] 1.0
                    ENDIF                 
                    BREAK
            ENDSWITCH
            BREAK
    ENDSWITCH
RETURN

sfxEndDialogue:
    SWITCH iLanguage    // 0:SPA ||1:ENG
        CASE 0  //SPA
            REMOVE_AUDIO_STREAM iSfx[2]
            GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_e_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_e_B.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_e_C.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
            ENDSWITCH
            BREAK
        CASE 1  //ENG
            REMOVE_AUDIO_STREAM iSfx[2]
            GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_e_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_e_B.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_e_C.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
            ENDSWITCH
            BREAK
    ENDSWITCH
RETURN

sfxFinishMissionDialogue:
    SWITCH iLanguage    // 0:SPA ||1:ENG
        CASE 0  //SPA
            REMOVE_AUDIO_STREAM iSfx[2]
            GENERATE_RANDOM_INT_IN_RANGE 0 4 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_finish_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_finish_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_finish_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
                CASE 3
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\spa_sc_finish_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
            ENDSWITCH
            BREAK
        CASE 1  //ENG
            REMOVE_AUDIO_STREAM iSfx[2]
            GENERATE_RANDOM_INT_IN_RANGE 0 4 (iRandomSfxSound)
            SWITCH iRandomSfxSound
                CASE 0
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_finish_A.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF
                    BREAK
                CASE 1
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_finish_B.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF                
                    BREAK
                CASE 2
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_finish_C.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
                CASE 3
                    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\eng_sc_finish_D.mp3" (iSfx[2])
                        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR iSfx[2] player_actor
                        SET_AUDIO_STREAM_STATE iSfx[2] PLAY 
                        SET_AUDIO_STREAM_VOLUME iSfx[2] 1.0
                    ENDIF 
                    BREAK
            ENDSWITCH
            BREAK
    ENDSWITCH
RETURN

//-+-------------------------------------------------------------------------

//-+--------------------------web_models--------------------------------------
createTwoWebs:
    //IF NOT DOES_CHAR_EXIST iWebActor
    //AND NOT DOES_CHAR_EXIST iWebActorR
    //AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 1 wmt
        LOAD_ALL_MODELS_NOW
        CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL01 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        DONT_REMOVE_CHAR iWebActor
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL01 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        DONT_REMOVE_CHAR iWebActorR
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598
        UNLOAD_SPECIAL_CHARACTER 1
    //ENDIF
RETURN

destroyTwoWebs:
    //IF DOES_CHAR_EXIST iWebActor
    //AND DOES_CHAR_EXIST iWebActorR
    //AND DOES_OBJECT_EXIST baseObject
        DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
        SET_OBJECT_COORDINATES baseObject (0.0 0.0 -15.0)
        DETACH_CHAR_FROM_CAR iWebActor
        DETACH_CHAR_FROM_CAR iWebActorR
        DELETE_CHAR iWebActor
        DELETE_CHAR iWebActorR
        DELETE_OBJECT baseObject
    //ENDIF
RETURN

//-+-------------------------------------------------------------------------

loadGeneralFiles:
	REQUEST_ANIMATION "spider"
	REQUEST_ANIMATION "mweb"
	LOAD_ALL_MODELS_NOW
	LOAD_TEXTURE_DICTIONARY scrb
	LOAD_SPRITE objCrosshair    "ilock"
	LOAD_SPRITE tPhotoBomb      "cam"
	LOAD_SPRITE tPBback         "scr01"
	LOAD_SPRITE tPBScrewballNot "scr00"
	LOAD_SPRITE tPBbar0         "scr02"
	LOAD_SPRITE tPBbar1         "scr03"
	LOAD_SPRITE tPBScrewBallToon "scr04"
	LOAD_SPRITE tPBbCounter     "scr05"
	LOAD_SPRITE tPBbCounterBlue "scr06"
    LOAD_SPRITE tPBBackTimer    "btimB"
    LOAD_SPRITE tPBBackScore    "btim"
    LOAD_SPRITE tPBBackInfo     "btimC"
    LOAD_SPRITE tPBBackScoreB   "btimD"

    LOAD_SPRITE tPBSBack1       "rb1"
    LOAD_SPRITE tPBSBack2       "rb2"
    LOAD_SPRITE tPBSBack3       "rb3"
    LOAD_SPRITE tPBSBack1Active "rb11"
    LOAD_SPRITE tPBSBack2Active "rb22"
    LOAD_SPRITE tPBSBack3Active "rb33"
    LOAD_SPRITE iconSuccess     "Success"
RETURN

getGeneralCoords:
cx[0] = -2022.26
cy[0] = 13.982
cz[0] = 61.60
czAngle[0] = 360.0 
cx[1] = -2192.0 
cy[1] = 389.789 
cz[1] = 64.624
czAngle[1] = 90.0
cx[2] = -1873.89 
cy[2] = 900.735 
cz[2] = 65.2756
czAngle[2] = 360.0 
cx[3] = -1812.37 
cy[3] = 1039.22 
cz[3] = 82.0859
czAngle[3] = 270.0
cx[4] = -1589.35 
cy[4] = 951.715 
cz[4] = 34.5971
czAngle[4] = 180.0
RETURN
//-+-----------------------------------------------------------------------------------------
}

//-+---------------------------------CALLSCM HELPERS---------------------------------------
{
//CLEO_CALL getInputType 0 (input)  ///0=joypad; 1=mouse
getInputType:
    LVAR_INT val
    LVAR_INT hInput
    READ_MEMORY 0xB6EC2E BYTE FALSE (hInput)
CLEO_RETURN 0 hInput
}
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
{
//CLEO_CALL setCameraOnChar 0 /*coords*/(0.0 0.0 0.0) /*char*/player_actor /*mode*/1 /*PB_ID*/idPhotoBomb   // Mode||1:normal||0:Photo_mode
setCameraOnChar:
    LVAR_FLOAT xIn yIn zIn  //In
    LVAR_INT hChar      // In
    LVAR_INT iMode  // In
    LVAR_INT iID        //In
    LVAR_FLOAT x[2] y[2] z[2]
    IF iMode = 0
        // Default
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hChar (0.0 -2.5 0.75) (x[0] y[0] z[0])
        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hChar (0.0 2.5 0.25) (x[1] y[1] z[1])
        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
    ELSE
        //PHOTO MODE
        SWITCH iID
            CASE 0
                x[0] = xIn + 10.0
                y[0] = yIn
                z[0] = zIn
                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                x[1] = xIn
                y[1] = yIn
                z[1] = zIn
                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                BREAK
            CASE 1
                x[0] = xIn
                y[0] = yIn - 10.0
                z[0] = zIn
                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                x[1] = xIn
                y[1] = yIn
                z[1] = zIn
                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                BREAK
            CASE 2
                x[0] = xIn - 10.0
                y[0] = yIn
                z[0] = zIn
                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                x[1] = xIn
                y[1] = yIn
                z[1] = zIn
                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                BREAK
            CASE 3
                x[0] = xIn
                y[0] = yIn + 10.0
                z[0] = zIn
                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                x[1] = xIn
                y[1] = yIn
                z[1] = zIn
                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                BREAK
            CASE 4
                x[0] = xIn + 10.0
                y[0] = yIn
                z[0] = zIn
                SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
                x[1] = xIn
                y[1] = yIn
                z[1] = zIn
                POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
                BREAK
        ENDSWITCH
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL SetCharPosSimple 0 (char x y z)()
SetCharPosSimple:
    LVAR_INT hChar // In
    LVAR_FLOAT x y z // In
    LVAR_INT pPed pMatrix pCoord
    GET_PED_POINTER hChar pPed
    pMatrix = pPed + 0x14
    READ_MEMORY pMatrix 4 FALSE (pMatrix)
    pCoord = pMatrix + 0x30
    WRITE_MEMORY pCoord 4 (x) FALSE
    pCoord += 0x4 
    WRITE_MEMORY pCoord 4 (y) FALSE
    pCoord += 0x4
    WRITE_MEMORY pCoord 4 (z) FALSE
CLEO_RETURN 0 ()
}

{
//CLEO_CALL addForceToChar 0 player_actor /*xVel*/0.0 /*yVel*/1.0 /*zVel*/1.0 /*amp*/20.0
addForceToChar:
    LVAR_INT playerB
    LVAR_FLOAT addXvel
    LVAR_FLOAT addYvel
    LVAR_FLOAT addZvel
    LVAR_FLOAT amplitude
    LVAR_FLOAT distance
    LVAR_FLOAT xPos yPos zPos xPoint yPoint zPoint
    //vector
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS playerB 0.0 0.0 0.0 (xPos yPos zPos)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS playerB addXvel addYvel addZvel (xPoint yPoint zPoint)
    xPoint = (xPoint - xPos)
    yPoint = (yPoint - yPos)
    zPoint = (zPoint - zPos)
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (xPoint yPoint zPoint) distance
    xPoint = (xPoint / distance)
    yPoint = (yPoint / distance)
    zPoint = (zPoint / distance)
    xPoint = (xPoint * amplitude)
    yPoint = (yPoint * amplitude)
    zPoint = (zPoint * amplitude)
    SET_CHAR_VELOCITY playerB xPoint yPoint zPoint
CLEO_RETURN 0
}
{
///CLEO_CALL getScreenXYFrom3DCoords 0 (2488.562, -1666.865, 12.8757)  (v1, v2) 
getScreenXYFrom3DCoords:
    LVAR_FLOAT posX, posY, posZ
    LVAR_FLOAT var3 var4 var5
    LVAR_FLOAT var6 var7 var8
    LVAR_FLOAT var9 var10 var11
    LVAR_FLOAT var12 var13 
    LVAR_INT var14
    LVAR_INT var15 var16 var17
    LVAR_INT var18
    LVAR_FLOAT copy14 copy15
    GET_VAR_POINTER (posX) (var14)  
    GET_VAR_POINTER (var3) (var15)
    GET_VAR_POINTER (var6) (var16)
    GET_VAR_POINTER (var9) (var17)
    CALL_FUNCTION 0X70CE30 6 6 0 0 var17 var16 var15 var14
    var12 = 640.0
    var13 = 448.0
    READ_MEMORY 0xC17044 4 FALSE (var14)
    READ_MEMORY 0xC17048 4 FALSE (var15)
    CSET_LVAR_FLOAT_TO_LVAR_INT copy14 var14
    CSET_LVAR_FLOAT_TO_LVAR_INT copy15 var15
    var12 = (var12 / copy14)
    var13 = (var13 / copy15)
    var3 = (var3 * var12)
    var4 = (var4 * var13)
CLEO_RETURN 0 (var3 var4)
}
{
//CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (640.0 480.0) (sizX sizY)
GetXYSizeInScreen4x3ScaleBy640x480:
    LVAR_FLOAT x y // In
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresY *= 1.33333333
    fresX /= fresY
    x /= fresX
    y /= 1.07142857
CLEO_RETURN 0 (x y)
}
{
//CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (1920.0 1080.0) (sizX sizY)
GetXYSizeInScreenScaleByUserResolution:
    LVAR_FLOAT x y // In
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresX /= 640.0
    x /= fresX
    fresY /= 448.0
    y /= fresY
CLEO_RETURN 0 (x y)
}
{
//CLEO_CALL getCurrentResolution 0 (fX fY)
getCurrentResolution:
    LVAR_INT iresX iresY
    LVAR_FLOAT fresX fresY
    READ_MEMORY 0x00C17044 DWORD FALSE (iresX) // Get current resolution X
    READ_MEMORY 0x00C17048 DWORD FALSE (iresY) // Y
    fresX =# iresX
    fresY =# iresY
CLEO_RETURN 0 (fresX fresY)
}
{
//CLEO_CALL barFunc 0 /*size*/1000.0 /*pos*/posX (/*size*/sizeX sizeY)
barFunc:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2]
    LVAR_FLOAT xSize ySize
    //LVAR_FLOAT fresX fresY
    //CLEO_CALL getCurrentResolution 0 (fresX fresY)
    var[1] = sizeBar
    var[1] /= 2500.0 //fresX
    var[1] *= 668.0
    CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 181.0) (xSize ySize)
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 224.0
CLEO_RETURN 0 var[0] xSize ySize
}
{
//CLEO_CALL max_min_value_float 0 fValue fMax fMin (fValue) 
max_min_value_float:
    LVAR_FLOAT fValue
    LVAR_FLOAT fMax fMin
    IF fValue > fMax
        fValue = fMax
    ENDIF
    IF fValue < fMin
        fValue = fMin
    ENDIF
CLEO_RETURN 0 fValue
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
//CLEO_CALL getRewardsInfo 0 /*mission*/0 /*rewards*/ rew1 rew2 rew3
getRewardsInfo:
    LVAR_INT counter    //In
    LVAR_TEXT_LABEL _lName _lstringVar
    LVAR_INT iCounterValues 
    LVAR_INT rew1 rew2 rew3
    STRING_FORMAT (_lName)"rew%i" counter
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "rewards" $_lName (_lstringVar)
        IF SCAN_STRING $_lstringVar "%i %i %i" iCounterValues (rew1 rew2 rew3)
            //PRINT_FORMATTED_NOW "rew: %i" 1000 iCounterValues //DEBUG
        ELSE
            rew1 = 0
            rew2 = 0
            rew3 = 0
        ENDIF
    ELSE
        PRINT_FORMATTED_NOW "ERROR: Can't Read Rewards" 1500
        WAIT 1500
        CLEO_RETURN 0 rew1 rew2 rew3
    ENDIF
CLEO_RETURN 0 rew1 rew2 rew3
}
{
//CLEO_CALL setRewardsInfo 0 /*mission*/0 /*rewards*/ rew1 rew2 rew3
setRewardsInfo:
    LVAR_INT counter    //In
    LVAR_INT rew1 rew2 rew3
    LVAR_TEXT_LABEL _lName _lvarRew
    STRING_FORMAT (_lName)"rew%i" counter
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        STRING_FORMAT (_lvarRew) "%i %i %i" rew1 rew2 rew3
        WRITE_STRING_TO_INI_FILE $_lvarRew "cleo\SpiderJ16D\config.ini" "rewards" $_lName
        //PRINT_FORMATTED_NOW "Done!" 1000
    ELSE
        PRINT_FORMATTED_NOW "ERROR: Can't Write Rewards" 1500
        WAIT 1500
        CLEO_RETURN 0
    ENDIF
CLEO_RETURN 0
}
//-+-----------------------------------------------------------------------------------------

//-+------------------------------IMGUI-EXTRA-------------------------------------
{
GUI_DrawBoxOutline_WithText:
/*
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) OutlineSize (1.4) OutlineSides (1 1 0 1) OutlineRGBA (200 200 200 200) TextID -1 FormatID 1 Padding 3.0
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v8 = outline size
v9 = outline side top    
v10 = outline side right
v11 = outline side bottom
v12 = outline side left
v13 = outline r    
v14 = outline g
v15 = outline b
v16 = outline a
v17 = textid
v18 = formatid
v19 = padding
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_FLOAT v8
LVAR_INT v9 v10 v11 v12 v13 v14 v15 v16 v17 v18
LVAR_FLOAT v19
LVAR_FLOAT v20 v21 v22 v23 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Outlines
IF v12 = TRUE  //outline side left
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 -= v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v9 = TRUE  //outline side top
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 -= v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v10 = TRUE  //outline side right
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 += v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v11 = TRUE  //outline side bottom
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 += v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT (v20 v21) $vTextA
ENDIF
CLEO_RETURN 0

GUI_DrawBoxOutline_VarsBoxToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
    v23 = v3
RETURN
}
{
GUI_DrawBox_WithNumber:
/*
//CLEO_CALL GUI_DrawBox_WithNumber 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) TextID -1 FormatID 1 Padding 3.0 number 5
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v17 = textid
v18 = formatid
v19 = padding
v8 = number
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_INT v17 v18
LVAR_FLOAT v19
LVAR_INT v8
LVAR_FLOAT v20 v21 v22 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBox_VarsBoxNumberToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT_WITH_NUMBER (v20 v21) $vTextA v8
ENDIF
CLEO_RETURN 0

GUI_DrawBox_VarsBoxNumberToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
RETURN
}
{
GUI_SetTextFormatByID:
//CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
LVAR_INT iID
LVAR_FLOAT fPadding
LVAR_INT ptAlpha
LVAR_FLOAT vtAlpha
SWITCH iID
    CASE 1
        GOSUB GUI_TextFormat_ItemMenu
        fPadding = 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_ItemMenu_Active
        fPadding = 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu
        fPadding = 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active
        fPadding = 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_MediumMenu
        fPadding = 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_MediumMenu_Numbers
        fPadding = 5.0
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_BigMenu_Numbers
        fPadding = 6.0
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_BigMenu_Numbers_Color
        fPadding = 6.0
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_MediumTitle
        fPadding = 5.0
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_SmallItemMenu_Colour
        fPadding = 5.0
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_SmallItemMenu
        fPadding = 5.0
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_MediumItemTitle
        fPadding = 4.5
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_TextReward1_Colour
        fPadding = 4.5
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_TextReward2_Colour
        fPadding = 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_TextReward3_Colour
        fPadding = 4.5
        BREAK
ENDSWITCH
CLEO_RETURN 0 fPadding

GUI_TextFormat_ItemMenu:            //1   Title Finish Awards   (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.25 1.2084
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_ItemMenu_Active:     //2 Title Blue Medium (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.16 0.7734
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu:           //3 small letters (for score) 
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu_Active:    //4  format for numbers (for score) white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.692
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
RETURN

GUI_TextFormat_MediumMenu:          //5  format XP level text
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.21 1.015
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 15 236 198 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_MediumMenu_Numbers:  //6  format XP level Numbers
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9184
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 211 226 224 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_BigMenu_Numbers:     //7     numbers white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 252 252 255 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_BigMenu_Numbers_Color:   //8     numbers blue
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 35 237 193 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_MediumTitle:         //9     Text medium format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN    

GUI_TextFormat_SmallItemMenu_Colour:     //10 Title Blue Small (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallItemMenu:       //11    Text small format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_MediumItemTitle:     //12     Text Medium / Names
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_TextReward1_Colour:     //13    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 59 36 23 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward2_Colour:     //14    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 12 60 78 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward3_Colour:     //15    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 72 63 17 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN
}
