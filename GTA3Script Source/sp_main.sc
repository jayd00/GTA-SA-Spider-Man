// by J16D
// Main swing script
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

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_main
LVAR_INT player_actor toggleSpiderMod isFuncEnabled
LVAR_INT sideSwing  // 0:center || 1:left || 2:right
LVAR_INT baseObject iWebActor
LVAR_INT LRStick UDStick
LVAR_FLOAT fProgress ftimera
LVAR_FLOAT fMaxSwingPeriod x y z fVelX fVelY fVelZ
LVAR_FLOAT fLongitude fCharSpeed fAmplitude
LVAR_FLOAT fSyncMaxAngle fSyncMinAngle fSyncAngle
LVAR_FLOAT fXAnglePlayerAir fYAnglePlayerAir fZAnglePlayerAir
LVAR_INT randomVal sfx checkSfx iTempVar

GET_PLAYER_CHAR 0 player_actor

IF GOSUB does_scripts_exist
//-+-- Start External Threads   //Main Scripts
    //Main Scripts
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dw.cs"      // Draw Indicator | webstrike & Stealth
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_ev.cs"      // Fight - Dodge (near & far distance)
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_evb.cs"     // Manual Dodge & Sliding dodge
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_hud.cs"     // Main Hud script
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_lf.cs"      // Life Regeneration
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_lvl.cs"     // Level Control
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_mb.cs"      // On ground/jump script
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_me.cs"      // Melee Combo X4 | Air Combo x4 | Swing kick
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_ml.cs"      // Web zip (air / lamp / object)
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_mm.cs"      // Gadget Select Script
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_po.cs"      // Powers
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_st.cs"      // Stealth (Hidden player) AI visibility
    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_wep.cs"     // Auto-Fill Web Ammo
ENDIF
    //Reservoirs    //additional
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_res.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_res.cs"     // Reservoir Scripts
    ENDIF
    //3D Blips    //additional  - this might consume fps
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_3d.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_3d.cs"     // 3D Blips
    ENDIF

//-+-- Start Internal Threads
    STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_cam_internalThread   // Camera Script
    STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_tmp_internalThread   // Fix 01 Animation / BRASSKNUCKLE (Web Shooter) 

//WRITE_MEMORY 0xC1704C 1 105 FALSE   //60fps
CLEO_CALL disableZvelocityLimit 0 1
GOSUB set_properties
GOSUB REQUEST_webAnimations
GOSUB REQUEST_SwingAnimations
//SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE

main_loop:
    SHUT_CHAR_UP player_actor TRUE
    //SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE
    
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE

            IF CLEO_CALL isActorInWater 0 player_actor
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WHILE  CLEO_CALL isActorInWater 0 player_actor
                    WAIT 0
                ENDWHILE
            ENDIF

            IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -5.0) (1 1 0 0 0)
                //fix
                IF IS_CHAR_PLAYING_ANIM player_actor ("WEAPON_crouch")
                    CLEAR_CHAR_TASKS player_actor
                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                ENDIF
                GET_CHAR_MOVE_STATE player_actor (iTempVar)
                // Jump From Building
                IF IS_BUTTON_PRESSED PAD1 CROSS  // ~k~~PED_SPRINT~
                //AND IS_CHAR_PLAYING_ANIM player_actor ("sprint_spider")   //sprint_civi
                AND iTempVar = MOVE_STATE_SPRINTING
                    IF GOSUB is_not_player_playing_swing_anims
                        IF CLEO_CALL isSideOfBuildingInOffset 0 player_actor (0.0 2.0 -0.5) (0.0 -1.5 -1.5)
                            GOSUB REQUEST_SwingAnimations
                            fVelY = 1.75
                            fVelZ = 2.15
                            fAmplitude = 6.0
                            CLEAR_CHAR_TASKS player_actor
                            CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
                            GOSUB TASK_PLAY_AcrobaticAnimation
                            WAIT 0  
                        ENDIF
                    ENDIF
                ENDIF
                //-+-- Wall Check
                IF CLEO_CALL isClearInSight 0 player_actor (0.0 1.5 0.0) (1 0 0 0 0)    //Front
                    IF CLEO_CALL is_Collision_wall_found 0 player_actor 1 (1 0 0 0 0) // 0:center || 1:left || 2:right
                        GOSUB found_left_wall
                    ELSE
                        IF CLEO_CALL is_Collision_wall_found 0 player_actor 2 (1 0 0 0 0) // 0:center || 1:left || 2:right
                            GOSUB found_right_wall
                        ENDIF
                    ENDIF
                ELSE
                    GOSUB found_front_wall
                ENDIF

                //-+-- MOUSE CONTROL
                IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   // ~k~~PED_LOCK_TARGET~
                OR IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    //do nothing
                ELSE
                    IF GOSUB is_player_playing_anims
                        //do nothing
                    ELSE
                        IF GOSUB is_not_player_playing_webstrike_anims
                            GOSUB setCharAngleAir
                        ENDIF
                    ENDIF
                ENDIF

                //-+-- SWING
                IF IS_BUTTON_PRESSED PAD1 CROSS                 // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                AND NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   // ~k~~PED_LOCK_TARGET~

                    IF GOSUB is_not_player_playing_anims

                        GET_CLEO_SHARED_VAR varMouseControl (isFuncEnabled) // is Mouse Enabled                            
                        IF isFuncEnabled = 0
                            RESTORE_CAMERA
                        ENDIF
                        GOSUB REQUEST_SwingAnimations
                        GOSUB REQUEST_webAnimations
                        GOSUB destroyWeb
                        GOSUB createWeb
                        // Force Alternative SWING
                        GET_CLEO_SHARED_VAR varAlternativeSwing (isFuncEnabled) //is Alternative Swing enabled
                        IF isFuncEnabled = 1   // True
                            GENERATE_RANDOM_FLOAT_IN_RANGE 20.0 35.0 (fSyncMaxAngle)    //27.0 33.0
                            //fSyncMaxAngle = 30.0
                            CLEO_CALL getCollisionAroundPlayer 0 player_actor (fLongitude sideSwing)
                        ELSE    // False
                            CLEO_CALL getSyncAngleFromBuildingInSightOfChar 0 player_actor (fLongitude fSyncMaxAngle sideSwing)
                        ENDIF
                        //PRINT_FORMATTED_NOW "ANGLE:(%f)" 1000 fSyncMaxAngle
                        fSyncMinAngle = (fSyncMaxAngle * -1.0)
                        CLEO_CALL simplePendulumPeriod 0 fLongitude /*fgravity*/145.0 /*fperiod*/(fMaxSwingPeriod)  //return time for web-swing
                        //0x863984 - [float] Gravity (default value: 1.0f/125.0f = 0.008f)
                        GET_CHAR_SPEED player_actor (fCharSpeed)
                        fAmplitude = (1.025 * fCharSpeed)
                        CLAMP_FLOAT fAmplitude 0.0 50.0 (fAmplitude)
                        GOSUB getXangleRotationInAir    //Calculate Spider rotation left/right
                        IF sideSwing = 0
                            GET_CLEO_SHARED_VAR varSwingBuilding (isFuncEnabled) //Is Forced Swing Without Buildings
                            IF isFuncEnabled = 1
                                GENERATE_RANDOM_INT_IN_RANGE 1 3 (sideSwing)    // 0:center || 1:left || 2:right
                            ELSE
                                WAIT 100
                                GOTO noswing
                            ENDIF
                        ENDIF
                        
                        GENERATE_RANDOM_INT_IN_RANGE 1 6 (randomVal)    //random animation Swing (6 types by side)
                        //Fix animation D
                        IF randomVal = 3
                            GENERATE_RANDOM_FLOAT_IN_RANGE 1.150 1.450  (fMaxSwingPeriod)
                            //fMaxSwingPeriod = 1.150                               
                        ENDIF
                        checkSfx = TRUE
                                                            //FOR TEST
                                                            //sideSwing = 1
                                                            //randomVal = 0
                        //----
                        timera = 0
                        WHILE  IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~

                            CSET_LVAR_FLOAT_TO_LVAR_INT (ftimera) timera
                            ftimera = (ftimera / 1000.0)
                            IF fMaxSwingPeriod >= ftimera
                                CLEO_CALL linearInterpolation 0 (0.0 fMaxSwingPeriod ftimera) (fSyncMaxAngle fSyncMinAngle) (fSyncAngle)
                                CLEO_CALL linearInterpolation 0 (fSyncMaxAngle fSyncMinAngle fSyncAngle) (0.0 1.0) (fProgress)
                                CLEO_CALL velocityPendulum 0 fLongitude fSyncAngle (fVelY fVelZ)
                                CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY fVelZ) fAmplitude
                                IF fSyncAngle > -5.0
                                    fAmplitude +=@ 0.2 // dt - 0.15
                                ELSE
                                    fAmplitude -=@ 0.1 // dt
                                ENDIF
                                CLAMP_FLOAT fAmplitude 0.0 50.0 (fAmplitude)
                                IF  IS_BUTTON_PRESSED PAD1 SQUARE   //~k~~PED_JUMPING~
                                    fAmplitude +=@ 0.2 // dt - 0.15
                                ENDIF
                                
                                //PRINT_FORMATTED_NOW "ANGLE:(%f) AMP:(%f)" 1 fSyncAngle fAmplitude     //debug
                                GOSUB getZangleRotationInAir    //Calculate Spider Z Rotation left/right
                                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                                GOSUB TASK_PLAY_SwingAnimation
                                IF fProgress >= 0.07
                                    GOSUB attachWeb
                                    IF checkSfx = TRUE
                                        GOSUB playWebSound
                                        checkSfx = FALSE
                                    ENDIF
                                ENDIF
                                /*
                                IF fAmplitude > 40.0
                                    SHAKE_CAM 1
                                ENDIF
                                */
                                
                                /*IF fAmplitude >= 40.0
                                    CLEO_CALL setCameraLerpFov 0 fAmplitude
                                ELSE
                                    GET_CAMERA_FOV (fVelX)
                                    CAMERA_SET_LERP_FOV 85.0 fVelX 1 FALSE
                                ENDIF*/
                                
                                //SET_OBJECT_HEADING baseObject fZAnglePlayerAir
                            ELSE
                                //end swing
                                //SET_OBJECT_COORDINATES baseObject 0.0 0.0 0.0
                                fXAnglePlayerAir = 0.0
                                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)    
                                IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E")
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F")
                                    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A_R")
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B_R")
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C_R")
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D_R")
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E_R")
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F_R")

                                        GOSUB moveWeb
                                        GOSUB TASK_PLAY_SwingEnd
                                        GOSUB playEndWebSound
                                        GET_CHAR_SPEED player_actor (fCharSpeed)
                                        fVelY = (1.15 * fCharSpeed) //1.25
                                        fVelZ = (1.00 * fCharSpeed) //1.01
                                        fAmplitude = (1.05 * fCharSpeed) //(1.5 * fCharSpeed)
                                        timerb = 0
                                        WHILE 100 > timerb
                                            CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY fVelZ) fAmplitude
                                            WAIT 0
                                        ENDWHILE
                                        WAIT 100
                                        WHILE  IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                                            GOSUB set_fall_animation
                                            WAIT 0
                                        ENDWHILE
                                        GOTO noswing

                                    ENDIF
                                ENDIF
                            ENDIF
                            
                            //-+-- Ground Check
                            IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -1.5) (1 0 0 0 0)
                            ELSE
                                GET_CLEO_SHARED_VAR varFixGround (isFuncEnabled) //is Enabled Ground
                                IF isFuncEnabled = 1
                                    fVelZ += 5.0
                                    CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY fVelZ) fAmplitude
                                    // simulates is in air  -avoid stuck in ground
                                    LVAR_INT pStruct
                                    GET_PED_POINTER player_actor (pStruct)
                                    WRITE_STRUCT_OFFSET pStruct 0x46C BYTE 0    // ONFOOT_STATE = AIR
                                ENDIF
                            ENDIF

                            //-+-- Wall Check
                            IF CLEO_CALL isClearInSight 0 player_actor (0.0 1.5 0.0) (1 0 0 0 0)    //Front
                                IF CLEO_CALL is_Collision_wall_found 0 player_actor 1 (1 0 0 0 0) // 0:center || 1:left || 2:right
                                    CLEAR_CHAR_TASKS player_actor
                                    GOSUB moveWeb
                                    GOSUB found_left_wall
                                    GOTO noswing
                                ELSE
                                    IF CLEO_CALL is_Collision_wall_found 0 player_actor 2 (1 0 0 0 0) // 0:center || 1:left || 2:right
                                        CLEAR_CHAR_TASKS player_actor
                                        GOSUB moveWeb
                                        GOSUB found_right_wall
                                        GOTO noswing
                                    ENDIF
                                ENDIF
                            ELSE
                                CLEAR_CHAR_TASKS player_actor
                                GOSUB moveWeb
                                GOSUB found_front_wall
                                GOTO noswing
                            ENDIF

                            //-+-- water check
                            IF CLEO_CALL isActorInWater 0 player_actor
                                CLEAR_CHAR_TASKS player_actor
                                GOTO noswing
                            ENDIF
                            WAIT 0
                        ENDWHILE

                        checkSfx = FALSE
                        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E")
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F")
                            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_B_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_D_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_E_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_E_F_R")

                                IF GOSUB is_not_player_playing_webstrike_anims

                                    fXAnglePlayerAir = 0.0
                                    SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)    
                                    GOSUB moveWeb
                                    GOSUB REQUEST_SwingAnimations               
                                    GOSUB playEndWebSound
                                    GOSUB TASK_PLAY_SwingFailAnimation

                                ENDIF                            

                            ENDIF
                        ENDIF

                    ENDIF
                    noswing:
                    checkSfx = FALSE
                    //SET_CHAR_ROTATION player_actor (0.0 0.0 fZAnglePlayerAir)
                    GOSUB moveWeb

                ENDIF
                //-+-- END SWING

            ELSE
                //FIX STUCK (sp_ml)
                //-+-- MOUSE CONTROL
                IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   // ~k~~PED_LOCK_TARGET~
                OR IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    //do nothing
                ELSE
                    IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")       //Angle control 
                        GOSUB setCharAngleAir
                    ENDIF
                ENDIF

            ENDIF

            // SET fall animation
            GOSUB set_fall_animation
        ELSE
            REMOVE_ANIMATION "spider"
            REMOVE_ANIMATION "mweb"
            REMOVE_AUDIO_STREAM sfx
            GOSUB restore_properties
            CLEO_CALL disableZvelocityLimit 0 0
            GOSUB destroyWeb
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF

    ENDIF

    //reset
    IF IS_KEY_PRESSED VK_TAB
    AND IS_KEY_PRESSED VK_SPACE
    AND IS_KEY_PRESSED VK_BACK
        WHILE   IS_KEY_PRESSED VK_BACK
            WAIT 0
        ENDWHILE
        SET_CHAR_COLLISION player_actor TRUE
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        RESTORE_CAMERA
        RESTORE_CAMERA_JUMPCUT
    ENDIF

    WAIT 0
GOTO main_loop

does_skill_Air_Tricks_enabled:
    GET_CLEO_SHARED_VAR varSkill3c (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

set_fall_animation:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
        IF IS_CHAR_PLAYING_ANIM player_actor "FALL_BACK"
        OR IS_CHAR_PLAYING_ANIM player_actor "FALL_FRONT"
        OR IS_CHAR_PLAYING_ANIM player_actor "FALL_GLIDE"
        OR IS_CHAR_PLAYING_ANIM player_actor "FALL_FALL"
            GOSUB REQUEST_SwingAnimations
            GOSUB TASK_PLAY_FallAnimation
        ELSE
            //Air Tricks
            IF GOSUB does_skill_Air_Tricks_enabled
                GOSUB REQUEST_SwingAnimations
                GOSUB setAcrobaticAnimationsInAir
            ENDIF
        ENDIF
    ENDIF
RETURN

setCharAngleAir:
    GET_CLEO_SHARED_VAR varMouseControl isFuncEnabled // is Mouse Enabled
    IF isFuncEnabled = 1
        IF IS_KEY_PRESSED VK_KEY_B
        ELSE
            CLEO_CALL setCharViewPointToCamera 0 player_actor
        ENDIF
    ELSE
        GOSUB getZangleRotationInAirStick    //Calculate Spider Z Rotation left/right
        SET_CHAR_ROTATION player_actor (0.0 0.0 fZAnglePlayerAir)
    ENDIF
RETURN

is_not_player_playing_webstrike_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_a")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_b")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out_b")
            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_c")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_in_c")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("webstrike_air_out_c")
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_not_player_playing_swing_anims:
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
                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
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
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_L")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("wall_corner_R")
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("groundToLampA")

                                RETURN_TRUE
                                RETURN
                            ENDIF

                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

is_player_playing_anims:
    IF IS_CHAR_PLAYING_ANIM player_actor ("webZip_A")       //Angle controlled in sp_ml
    OR IS_CHAR_PLAYING_ANIM player_actor ("webZip_A_R")     //Angle controlled in sp_ml
    OR IS_CHAR_PLAYING_ANIM player_actor ("jump_glide_F")    //Angle controlled in sp_mb
    OR IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")      //Angle controlled in tower_script_mission
        RETURN_TRUE
        RETURN
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")  //Angle controlled in sp_ml
        OR IS_CHAR_PLAYING_ANIM player_actor ("airToLampA")     //Angle controlled in sp_ml
        OR IS_CHAR_PLAYING_ANIM player_actor ("airToLampB")     //Angle controlled in sp_ml
        OR IS_CHAR_PLAYING_ANIM player_actor ("airToLampB_B")   //Angle controlled in sp_ml
            RETURN_TRUE
            RETURN            
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN


//webstrike_air_out_c

setAcrobaticAnimationsInAir:
    IF  IS_BUTTON_PRESSED PAD1 TRIANGLE   // ~k~~VEHICLE_ENTER_EXIT~
        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
        IF 0 > LRStick  //Left
        AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
            IF GOSUB is_not_player_playing_falling_anims
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_l_in" "spider") 9.0 (0 1 1 0) -2
                WAIT 0
                //SET_CHAR_ANIM_SPEED player_actor "fall_acrob_l_in" 1.5
                WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_l_in"
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_l_in") (fProgress)
                    IF fProgress >= 0.99
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_left" "spider") 17.0 (1 1 1 0) -2
                WAIT 0
                WHILE   0 > LRStick  //Left
                AND IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_left"
                    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                    WAIT 0
                ENDWHILE
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_l_out" "spider") 9.0 (0 1 1 0) -2
                WAIT 0
                WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_l_out"
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_l_out") (fProgress)
                    IF fProgress >= 0.99
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                CLEAR_CHAR_TASKS player_actor
            ENDIF
        ELSE
            IF LRStick > 0  //Right
            AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
                IF GOSUB is_not_player_playing_falling_anims
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_r_in" "spider") 9.0 (0 1 1 0) -2
                    WAIT 0
                    //SET_CHAR_ANIM_SPEED player_actor "fall_acrob_r_in" 1.5
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_r_in"
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_r_in") (fProgress)
                        IF fProgress >= 0.99
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_right" "spider") 17.0 (1 1 1 0) -2
                    WAIT 0
                    WHILE   LRStick > 0  //Right
                    AND IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_right"
                        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                        WAIT 0
                    ENDWHILE
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_r_out" "spider") 9.0 (0 1 1 0) -2
                    WAIT 0
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_r_out"
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_r_out") (fProgress)
                        IF fProgress >= 0.99
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                    CLEAR_CHAR_TASKS player_actor
                ENDIF
            ELSE
                IF UDStick > 0  //Down
                AND IS_BUTTON_PRESSED PAD1 LEFTSTICKY  //~k~~GO_FORWARD~ / ~k~~GO_BACK~
                    IF GOSUB is_not_player_playing_falling_anims
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_b_in" "spider") 9.0 (0 1 1 0) -2
                        WAIT 0
                        //SET_CHAR_ANIM_SPEED player_actor "fall_acrob_b_in" 1.5
                        WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_b_in"
                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_b_in") (fProgress)
                            IF fProgress >= 0.99
                                BREAK
                            ENDIF
                            WAIT 0
                        ENDWHILE
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_back" "spider") 17.0 (1 1 1 0) -2
                        WAIT 0
                        WHILE   UDStick > 0  //Down
                        AND IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_back"
                            CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                            WAIT 0
                        ENDWHILE
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_b_out" "spider") 9.0 (0 1 1 0) -2
                        WAIT 0
                        WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_b_out"
                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_b_out") (fProgress)
                            IF fProgress >= 0.99
                                BREAK
                            ENDIF
                            WAIT 0
                        ENDWHILE
                        CLEAR_CHAR_TASKS player_actor
                    ENDIF
                ELSE
                    IF 0 > UDStick  //Up
                    AND IS_BUTTON_PRESSED PAD1 LEFTSTICKY  //~k~~GO_FORWARD~ / ~k~~GO_BACK~
                        IF GOSUB is_not_player_playing_falling_anims
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_f_in" "spider") 9.0 (0 1 1 0) -2
                            WAIT 0
                            //SET_CHAR_ANIM_SPEED player_actor "fall_acrob_f_in" 1.5
                            WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_f_in"
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_f_in") (fProgress)
                                IF fProgress >= 0.99
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_front" "spider") 17.0 (1 1 1 0) -2
                            WAIT 0
                            WHILE   0 > UDStick  //Up
                            AND IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_front"
                                CLEO_CALL getDataJoystick 0 (LRStick UDStick)
                                WAIT 0
                            ENDWHILE
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_acrob_f_out" "spider") 9.0 (0 1 1 0) -2
                            WAIT 0
                            WHILE IS_CHAR_PLAYING_ANIM player_actor "fall_acrob_f_out"
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_f_out") (fProgress)
                                IF fProgress >= 0.99
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE
                            CLEAR_CHAR_TASKS player_actor
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
RETURN

is_not_player_playing_falling_anims:
    IF IS_CHAR_PLAYING_ANIM player_actor "fall_glide_A"
    OR IS_CHAR_PLAYING_ANIM player_actor "fall_glide_B"
    OR IS_CHAR_PLAYING_ANIM player_actor "fall_glide_C"
    OR IS_CHAR_PLAYING_ANIM player_actor "fall_glide_D"
        RETURN_TRUE
        RETURN
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor "swing_E_A"
        OR IS_CHAR_PLAYING_ANIM player_actor "swing_E_C"
        OR IS_CHAR_PLAYING_ANIM player_actor "swing_E_A_R"
        OR IS_CHAR_PLAYING_ANIM player_actor "swing_E_C_R"
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

set_properties:
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE
    SHUT_CHAR_UP player_actor TRUE
    SET_PLAYER_NEVER_GETS_TIRED 0 TRUE
RETURN
    //WRITE_MEMORY 0xB793DC 4 1000.0 FALSE   // 0xB793DC - [float] Muscle
    //WRITE_MEMORY 0xB793D4 4 900.0 FALSE    //[float] Fat stat
    //SHUT_PLAYER_UP 0 TRUE
    //WRITE_MEMORY 0x96916C 1 1 FALSE     //0x96916C - Mega Jump
/*
0xB793D4 - [float] Fat stat
0xB793D8 - [float] Stamina stat
0xB793DC - [float] Muscle stat
Fat and muscles will be visible only on CJ, but on the other characters will be only the effect. (running time, Higher damage when you punch )
*/

restore_properties:
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE FALSE FALSE
    SET_PLAYER_NEVER_GETS_TIRED 0 FALSE
    SHUT_CHAR_UP player_actor FALSE
    //SHUT_PLAYER_UP 0 FALSE
    //WRITE_MEMORY 0x96916C 1 0 FALSE     //0x96916C - Mega Jump
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

REQUEST_webAnimations:
    IF NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "mweb"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_webAnimations

does_scripts_exist:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dw.cs"
    AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_ev.cs"
    AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_evb.cs"
    AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_hud.cs"
    AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_lf.cs"
    AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_lvl.cs"
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_mb.cs"
        AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_me.cs"
        AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_ml.cs"
        AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_mm.cs"
        AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_po.cs"
        AND DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_wep.cs"
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_st.cs"
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

playWebSound:
    REMOVE_AUDIO_STREAM sfx
    //GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF     
        BREAK      
        CASE 4
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF         
        BREAK      
        //CASE 5
        DEFAULT
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF         
        BREAK               
    ENDSWITCH
RETURN

playEndWebSound:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web2_f.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF        
        BREAK
        //CASE 2
        DEFAULT
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web3_f.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx PLAY 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN

playSfxWall:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\e_wall.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx PLAY 
        SET_AUDIO_STREAM_LOOPED sfx TRUE
    ENDIF
RETURN

moveWeb:
    IF DOES_OBJECT_EXIST baseObject
        DETACH_OBJECT baseObject (0.0 0.0 0.0) 0
        SET_OBJECT_COORDINATES baseObject (0.0 0.0 -15.0)
    ENDIF
RETURN

attachWeb:
    IF DOES_CHAR_EXIST iWebActor
        SWITCH sideSwing    // 0:center || 1:left || 2:right
            CASE 0
                BREAK
            CASE 1
                SWITCH randomVal
                    CASE 0
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_A" "mweb") 83.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_A") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_A") STOP  
                        BREAK
                    CASE 1
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_B" "mweb") 91.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_B") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_B") STOP  
                        BREAK
                    CASE 2
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_C" "mweb") 66.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_C") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_C") STOP 
                        BREAK
                    CASE 3
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_D" "mweb") 36.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_D") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_D") STOP                     
                        BREAK
                    CASE 4
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_E" "mweb") 51.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_E") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_E") STOP 
                        BREAK
                    CASE 5
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingL_F" "mweb") 26.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingL_F") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingL_F") STOP 
                        BREAK                        
                ENDSWITCH
                BREAK
            CASE 2
                SWITCH randomVal
                    CASE 0
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_A" "mweb") 88.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_A") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_A") STOP  
                        BREAK
                    CASE 1
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_B" "mweb") 70.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_B") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_B") STOP  
                        BREAK
                    CASE 2
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_C" "mweb") 66.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_C") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_C") STOP 
                        BREAK
                    CASE 3
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_D" "mweb") 36.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_D") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_D") STOP 
                        BREAK
                    CASE 4
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_E" "mweb") 91.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_E") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_E") STOP 
                        BREAK    
                    CASE 5
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webSwingR_F" "mweb") 26.0 (0 1 1 1) -2
                        SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webSwingR_F") fProgress
                        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webSwingR_F") STOP 
                        BREAK                    
                ENDSWITCH
                BREAK
        ENDSWITCH
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
RETURN

attachWebB:
    IF DOES_OBJECT_EXIST baseObject
        GET_CHAR_COORDINATES player_actor (x y z)
        SET_OBJECT_COORDINATES baseObject (x y z)
        GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
        SET_OBJECT_HEADING baseObject fZAnglePlayerAir      
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
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) (iWebActor)
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
        SET_OBJECT_HEADING baseObject fZAnglePlayerAir
        UNLOAD_SPECIAL_CHARACTER 9
    ENDIF
RETURN

createWeb_b:
    IF NOT DOES_CHAR_EXIST iWebActor
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
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
        SET_OBJECT_HEADING baseObject fZAnglePlayerAir
        UNLOAD_SPECIAL_CHARACTER 9
    ENDIF
RETURN

destroyWeb:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

TASK_PLAY_SwingAnimation:
    SWITCH sideSwing    // 0:center || 1:left || 2:right
        CASE 1
            SWITCH randomVal
                CASE 0
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_A" "spider") 83.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_A") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_A") STOP
                    BREAK
                CASE 1
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_B" "spider") 91.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_B") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_B") STOP
                    BREAK
                CASE 2
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_C" "spider") 26.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_C") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_C") STOP
                    BREAK
                CASE 3
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_D" "spider") 36.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_D") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_D") STOP
                    BREAK                    
                CASE 4
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_E" "spider") 51.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_E") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_E") STOP
                    BREAK  
                CASE 5
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_L_F" "spider") 26.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_L_F") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_L_F") STOP
                    BREAK                    
            ENDSWITCH
            BREAK
        CASE 2
            SWITCH randomVal
                CASE 0
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_A" "spider") 88.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_A") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_A") STOP  
                    BREAK
                CASE 1
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_B" "spider") 71.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_B") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_B") STOP  
                    BREAK
                CASE 2
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_C" "spider") 26.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_C") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_C") STOP
                    BREAK
                CASE 3
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_D" "spider") 36.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_D") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_D") STOP
                    BREAK
                CASE 4
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_E" "spider") 51.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_E") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_E") STOP
                    BREAK
                CASE 5
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_R_F" "spider") 26.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_R_F") fProgress
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_R_F") STOP
                    BREAK
            ENDSWITCH
            BREAK
        DEFAULT
            BREAK
    ENDSWITCH
RETURN

TASK_PLAY_SwingEnd:
    //CLEAR_CHAR_TASKS player_actor
    SWITCH sideSwing    // 0:center || 1:left || 2:right
        CASE 1
            SWITCH randomVal
                CASE 0
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_A" "spider") 69.0 (0 1 1 1) -2      //swing_E_A  69.0  1.3
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_A" 1.3
                    BREAK
                CASE 1
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_B" "spider") 70.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_B" 1.3
                    BREAK
                CASE 2
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_C" "spider") 91.0 (0 1 1 1) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_C" 1.4 //1.35
                    BREAK
                CASE 3
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_D" "spider") 51.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_D" 1.4
                    BREAK
                CASE 4
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_E" "spider") 101.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_E" 1.4
                    BREAK
                CASE 5
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_E" "spider") 101.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_E" 1.4
                    //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_F" "spider") 50.0 (0 1 1 0) -2
                    //WAIT 0
                    //SET_CHAR_ANIM_SPEED player_actor "swing_E_F" 1.4
                    BREAK
            ENDSWITCH
            BREAK
        CASE 2
            SWITCH randomVal
                CASE 0
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_A_R" "spider") 104.0 (0 1 1 1) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_A_R" 1.35
                    BREAK
                CASE 1
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_B_R" "spider") 118.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_B_R" 1.4
                    BREAK
                CASE 2
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_C_R" "spider") 92.0 (0 1 1 1) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_C_R" 1.4
                    BREAK
                CASE 3
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_D_R" "spider") 51.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_D_R" 1.4  //1.199 
                    BREAK
                CASE 4
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_E_R" "spider") 101.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_E_R" 1.4  //1.199 
                    BREAK
                CASE 5
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_E_R" "spider") 101.0 (0 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "swing_E_E_R" 1.4  //1.199 
                    //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_E_F_R" "spider") 50.0 (0 1 1 0) -2
                    //WAIT 0
                    //SET_CHAR_ANIM_SPEED player_actor "swing_E_F_R" 1.4
                    BREAK
            ENDSWITCH
            BREAK
        DEFAULT
            BREAK
    ENDSWITCH
RETURN

TASK_PLAY_SwingFailAnimation:
    GENERATE_RANDOM_INT_IN_RANGE 0 5 (randomVal)    //random animation (5 types)
    //randomVal = 3
    SWITCH randomVal
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_fail_A" "spider") 26.0 (0 1 1 0) -2     
            BREAK
        CASE 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_fail_B" "spider") 26.0 (0 1 1 0) -2     // left
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "swing_fail_B" 1.25    
            BREAK
        CASE 2
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_fail_C" "spider") 26.0 (0 1 1 0) -2      // right  
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "swing_fail_C" 1.25    
            BREAK
        CASE 3
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_fail_D" "spider") 26.0 (0 1 1 0) -2      // Left
            WAIT 0
            BREAK   
        //CASE 4
        DEFAULT
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("swing_fail_E" "spider") 26.0 (0 1 1 0) -2      // right  
            WAIT 0
            BREAK         
    ENDSWITCH
RETURN

TASK_PLAY_FallAnimation:
    //GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    randomVal = 2
    SWITCH randomVal
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_glide_A" "spider") 101.0 (0 1 1 1) -2     
            WAIT 0
            BREAK
        CASE 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_glide_B" "spider") 101.0 (0 1 1 1) -2
            WAIT 0     
            SET_CHAR_ANIM_SPEED player_actor "fall_glide_D" 1.15
            BREAK
        CASE 2
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_glide_C" "spider") 151.0 (0 1 1 1) -2
            WAIT 0
            SET_CHAR_ANIM_SPEED player_actor "fall_glide_C" 1.15
            BREAK
        //CASE 3
        DEFAULT
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("fall_glide_D" "spider") 151.0 (0 1 1 1) -2  
            WAIT 0      
            SET_CHAR_ANIM_SPEED player_actor "fall_glide_D" 1.15
            BREAK            
    ENDSWITCH
RETURN

TASK_PLAY_AcrobaticAnimation:
    GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
    SWITCH randomVal
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_Build_A" "spider") 37.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_Build_A")
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_Build_A") (fProgress)
                IF fProgress >= 0.85
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
        //CASE 1
        DEFAULT
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_Build_B" "spider") 115.0 (0 1 1 1) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_Build_B")
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_Build_B") (fProgress)
                IF fProgress >= 0.40
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
    ENDSWITCH
RETURN

TASK_PLAY_JumpWallRight:
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    //randomVal = 1
    SWITCH randomVal 
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_R_A" "spider" 25.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_R_A"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_R_A") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (-10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.90
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
        CASE 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_R_B" "spider" 23.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_R_B"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_R_B") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (-10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.70
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
        //CASE 2
        DEFAULT
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_R_A" "spider" 25.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_R_A"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_R_A") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (-10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.90
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
    ENDSWITCH
RETURN

TASK_PLAY_JumpWallLeft:
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    //randomVal = 1
    SWITCH randomVal 
        CASE 0
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_L_A" "spider" 25.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_L_A"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_L_A") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.90
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
        CASE 1
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_L_B" "spider" 23.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_L_B"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_L_B") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.70
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK
        //CASE 2
        DEFAULT
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_jump_L_A" "spider" 25.0 (0 1 1 0) -2
            WAIT 0
            WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_jump_L_A"
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_jump_L_A") (fProgress)
                IF 0.35 > fProgress
                    CLEO_CALL addForceToChar 0 player_actor (10.0 fVelY 1.0) fAmplitude            
                ENDIF
                IF fProgress >= 0.90
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            BREAK            
    ENDSWITCH
RETURN

getXangleRotationInAir:
    SWITCH sideSwing // 0:center || 1:left || 2:right
        CASE 0
            fXAnglePlayerAir = 0.0
            BREAK
        CASE 1
            IF 8.1 > fLongitude
                fXAnglePlayerAir = (fSyncMaxAngle / -3.0) //-2.5  //-3.0
            ELSE
                IF 10.1 > fLongitude
                    fXAnglePlayerAir = (fSyncMaxAngle / -2.0)   //-3.0
                ELSE
                    IF 15.1 > fLongitude
                        fXAnglePlayerAir = (fSyncMaxAngle / -1.6)   //-3.0
                    ENDIF                
                ENDIF
            ENDIF
            BREAK
        CASE 2
            IF 8.1 > fLongitude
                fXAnglePlayerAir = (fSyncMaxAngle / 3.0) //-2.5  //-3.0
            ELSE
                IF 10.1 > fLongitude
                    fXAnglePlayerAir = (fSyncMaxAngle / 2.0)   //-3.0
                ELSE
                    IF 15.1 > fLongitude
                        fXAnglePlayerAir = (fSyncMaxAngle / 1.6)   //-3.0
                    ENDIF                
                ENDIF
            ENDIF        
            //fXAnglePlayerAir = (fSyncMaxAngle / 2.0)    //3.0
            BREAK
    ENDSWITCH
RETURN

getZangleRotationInAir:
    GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > LRStick  //Right
        fZAnglePlayerAir +=@ 0.50 //1.0 //0.5
    ELSE
        IF LRStick > 0  //Left
            fZAnglePlayerAir -=@ 0.50 //1.0 //0.5
        ELSE
            SWITCH sideSwing // 0:center || 1:left || 2:right
                CASE 0
                    fZAnglePlayerAir = fZAnglePlayerAir
                    BREAK
                CASE 1
                    fZAnglePlayerAir +=@ 0.085 //0.1 //0.015  
                    BREAK
                CASE 2
                    fZAnglePlayerAir -=@ 0.085 //0.1 //0.015
                    BREAK
            ENDSWITCH
        ENDIF
    ENDIF
RETURN

getZangleRotationInAirStick:
    GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    IF 0 > LRStick  //Right
        fZAnglePlayerAir +=@ 3.0 //0.5
    ELSE
        IF LRStick > 0  //Left
            fZAnglePlayerAir -=@ 3.0 //0.5
        ENDIF
    ENDIF
RETURN

found_front_wall:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_B"
    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_A"
    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_A"
    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_B"                        
        IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
            GOSUB spideyFrontWall
            WAIT 0
        ENDIF
    ENDIF
RETURN

found_right_wall:
    IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
        GOSUB spideyFrontWallRight
        WAIT 0
    ENDIF
RETURN

found_left_wall:
    IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
        GOSUB spideyFrontWallLeft
        WAIT 0
    ENDIF
RETURN

spideyFrontWallRight:
    GOSUB REQUEST_SwingAnimations
    GET_CHAR_SPEED player_actor (fCharSpeed)
    IF 30.0 > fCharSpeed
        fCharSpeed = 30.0
    ENDIF
    fVelY = (0.50 * fCharSpeed)
    fAmplitude = (0.65 * fCharSpeed)

    CLEO_CALL setZangleCharWall 0 player_actor (10.0 0.2 0.0) (10.0 -0.2 0.0) 0.0
    CLEO_CALL setCoordsInter 0 player_actor (5.0 0.0 0.0) 0.40
    CLEAR_CHAR_TASKS player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_R_A" "spider" 30.0 (0 1 1 0) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "run_wall_R_A" 1.15
    GOSUB playSfxWall

    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_A"
    AND IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
    AND 0 > UDStick //Forward
        IF CLEO_CALL isClearInSight 0 player_actor (2.0 0.0 0.0) (1 0 0 0 0)    //Right
            IF NOT IS_PC_USING_JOYPAD
                GOTO animCornerRight
            ENDIF
        ELSE
            IF CLEO_CALL isClearInSight 0 player_actor (0.0 2.0 0.0) (1 0 0 0 0)    //Front
                CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY 0.15) fAmplitude
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_R_A") (fProgress)
                IF fProgress >= 0.99
                    GOTO animWallRightContinue
                ENDIF
                IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    GOTO endWallRight
                ENDIF
            ELSE
                //PRINT_FORMATTED "Wall Front! (1)" 1000
                GOTO endWallRight
            ENDIF
        ENDIF
        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
        WAIT 0
    ENDWHILE

    animWallRightContinue:
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    WHILE IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
    AND 0 > UDStick //Forward
        IF CLEO_CALL isClearInSight 0 player_actor (2.0 0.25 0.0) (1 0 0 0 0)    //Right
            IF NOT IS_PC_USING_JOYPAD
                GOTO animCornerRight
            ENDIF
        ELSE
            IF CLEO_CALL isClearInSight 0 player_actor (0.0 2.0 0.0) (1 0 0 0 0)    //Front
                IF NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_B"
                    CLEO_CALL setZangleCharWall 0 player_actor (10.0 0.2 0.0) (10.0 -0.2 0.0) 0.0
                    CLEO_CALL setCoordsInter 0 player_actor (5.0 0.0 0.0) 0.40
                    CLEAR_CHAR_TASKS player_actor
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_R_B" "spider" 30.0 (1 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "run_wall_R_B" 1.15
                ELSE
                    CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY 0.15) fAmplitude
                    IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                        GOTO endWallRight
                    ENDIF
                ENDIF
            ELSE
                //PRINT_FORMATTED "Wall Front! (2)" 1000
                GOTO endWallRight
            ENDIF            
        ENDIF
        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
        WAIT 0
    ENDWHILE
    GOTO endWallRight

    animCornerRight:
    IF LRStick > 0  //Right
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 1.5 0.0 -1.0 (x y z)
        SET_AUDIO_STREAM_STATE sfx STOP
        GET_CHAR_HEADING player_actor (fSyncAngle)
        fSyncAngle -= 85.0  //gtasa inverted angles
        CLEAR_CHAR_TASKS player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_corner_R" "spider" 47.0 (0 1 1 1) -2
        //TASK_PLAY_ANIM_WITH_FLAGS player_actor "wall_corner_R" "spider" 47.0 (0 1 1 1) -1 0 1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "wall_corner_R" 1.25
        //GET_OFFSET_FROM_CAMERA_IN_WORLD_COORDS 1.0 0.0 0.0 (x y z)
        WHILE IS_CHAR_PLAYING_ANIM player_actor "wall_corner_R"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("wall_corner_R") (fProgress)
            IF CLEO_CALL isClearInSight 0 player_actor (2.0 0.0 0.0) (1 0 0 0 0)    //Right
                IF fProgress >= 0.277   //frame 13/47
                AND 0.532 >= fProgress  //frame 25/47
                    CLEO_CALL draw_on_screen_web 0 x y z 25 //Right hand
                    USE_TEXT_COMMANDS FALSE
                ENDIF
                IF fProgress >= 0.298
                AND 0.745 >= fProgress   //frame 35/47    //0.809 frame 38
                    CLEO_CALL addForceToChar 0 player_actor (8.0 -1.25 0.7) fAmplitude 
                ENDIF
                IF fProgress >= 0.979 //frame 46/47
                    CLEAR_CHAR_TASKS player_actor
                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                    SET_CHAR_HEADING player_actor fSyncAngle
                    RESTORE_CAMERA_JUMPCUT
                    CLEAR_CHAR_TASKS player_actor
                    BREAK
                ENDIF
            ELSE
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        CLEAR_CHAR_TASKS player_actor
        WAIT 0
        RESTORE_CAMERA
        GOTO endWallRightB
    ENDIF

    endWallRight:
    SET_AUDIO_STREAM_STATE sfx STOP
    CLEAR_CHAR_TASKS player_actor
    GOSUB TASK_PLAY_JumpWallRight
    CLEAR_CHAR_TASKS player_actor

    endWallRightB:
RETURN

spideyFrontWallLeft:
    GOSUB REQUEST_SwingAnimations
    GET_CHAR_SPEED player_actor (fCharSpeed)
    IF fCharSpeed < 30.0
        fCharSpeed = 30.0
    ENDIF    
    fVelY = (0.50 * fCharSpeed)
    fAmplitude = (0.65 * fCharSpeed)

    CLEO_CALL setZangleCharWall 0 player_actor (-10.0 0.2 0.0) (-10.0 -0.2 0.0) 0.0
    CLEO_CALL setCoordsInter 0 player_actor (-5.0 0.0 0.0) 0.40
    CLEAR_CHAR_TASKS player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_L_A" "spider" 30.0 (0 1 1 0) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "run_wall_L_A" 1.15
    GOSUB playSfxWall
    
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    WHILE IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_A"
    AND IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
    AND 0 > UDStick //Forward
        IF CLEO_CALL isClearInSight 0 player_actor (-2.0 0.0 0.0) (1 0 0 0 0)    //Left
            IF NOT IS_PC_USING_JOYPAD
                GOTO animCornerLeft
            ENDIF
        ELSE
            IF CLEO_CALL isClearInSight 0 player_actor (0.0 2.0 0.0) (1 0 0 0 0)    //Front
                CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY 0.15) fAmplitude
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("run_wall_L_A") (fProgress)
                IF fProgress >= 0.99
                    GOTO animWallLeftContinue
                ENDIF
                IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                    GOTO endWallLeft
                ENDIF                
            ELSE
                //PRINT_FORMATTED "Wall Front! (1)" 1000
                GOTO endWallLeft
            ENDIF
        ENDIF
        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
        WAIT 0
    ENDWHILE

    animWallLeftContinue:
    CLEO_CALL getDataJoystick 0 (LRStick UDStick)
    WHILE IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
    AND 0 > UDStick //Forward
        IF CLEO_CALL isClearInSight 0 player_actor (-2.0 0.25 0.0) (1 0 0 0 0)    //Left
            IF NOT IS_PC_USING_JOYPAD
                GOTO animCornerLeft
            ENDIF
        ELSE
            IF CLEO_CALL isClearInSight 0 player_actor (0.0 2.0 0.0) (1 0 0 0 0)    //Front
                IF NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_B"
                    CLEO_CALL setZangleCharWall 0 player_actor (-10.0 0.2 0.0) (-10.0 -0.2 0.0) 0.0
                    CLEO_CALL setCoordsInter 0 player_actor (-5.0 0.0 0.0) 0.40
                    CLEAR_CHAR_TASKS player_actor
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall_L_B" "spider" 30.0 (1 1 1 0) -2
                    WAIT 0
                    SET_CHAR_ANIM_SPEED player_actor "run_wall_L_B" 1.15
                ELSE
                    CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY 0.15) fAmplitude
                    IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                        GOTO endWallLeft
                    ENDIF
                ENDIF
            ELSE
                //PRINT_FORMATTED "Wall Front! (2)" 1000
                GOTO endWallLeft
            ENDIF            
        ENDIF
        CLEO_CALL getDataJoystick 0 (LRStick UDStick)
        WAIT 0
    ENDWHILE
    GOTO endWallLeft

    animCornerLeft:
    IF 0 > LRStick  //Left
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor -1.5 0.0 -1.0 (x y z)
        SET_AUDIO_STREAM_STATE sfx STOP
        GET_CHAR_HEADING player_actor (fSyncAngle)
        fSyncAngle += 85.0  //gtasa inverted angles
        CLEAR_CHAR_TASKS player_actor
        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_corner_L" "spider" 47.0 (0 1 1 1) -2
        //TASK_PLAY_ANIM_WITH_FLAGS player_actor "wall_corner_L" "spider" 47.0 (0 1 1 1) -1 0 1
        WAIT 0
        SET_CHAR_ANIM_SPEED player_actor "wall_corner_L" 1.25
        //GET_OFFSET_FROM_CAMERA_IN_WORLD_COORDS -1.0 0.0 0.0 (x y z)
        WHILE IS_CHAR_PLAYING_ANIM player_actor "wall_corner_L"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("wall_corner_L") (fProgress)
            IF CLEO_CALL isClearInSight 0 player_actor (-2.0 0.0 0.0) (1 0 0 0 0)    //Left
                IF fProgress >= 0.277   //frame 13/47
                AND 0.532 >= fProgress  //frame 25/47
                    CLEO_CALL draw_on_screen_web 0 x y z 35 //Left hand
                    USE_TEXT_COMMANDS FALSE
                ENDIF
                IF fProgress >= 0.298
                AND 0.745 >= fProgress   //frame 35/47    //0.809 frame 38
                    CLEO_CALL addForceToChar 0 player_actor (-8.0 -1.25 0.7) fAmplitude 
                ENDIF
                IF fProgress >= 0.979 //frame 46/47
                    CLEAR_CHAR_TASKS player_actor
                    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                    SET_CHAR_HEADING player_actor fSyncAngle
                    RESTORE_CAMERA_JUMPCUT 
                    CLEAR_CHAR_TASKS player_actor
                    BREAK
                ENDIF
            ELSE
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                RESTORE_CAMERA
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        CLEAR_CHAR_TASKS player_actor
        WAIT 0
        RESTORE_CAMERA
        GOTO endWallLeftB
    ENDIF

    endWallLeft:
    SET_AUDIO_STREAM_STATE sfx STOP
    CLEAR_CHAR_TASKS player_actor
    GOSUB TASK_PLAY_JumpWallLeft
    CLEAR_CHAR_TASKS player_actor

    endWallLeftB:
RETURN

spideyFrontWall:
    //POINT_CAMERA_AT_CHAR player_actor AIMWEAPON INTERPOLATION
    fXAnglePlayerAir = 0.0
    CLEO_CALL setZangleCharWall 0 player_actor (0.2 10.0 0.0) (-0.2 10.0 0.0) 90.0
    WAIT 0
    GET_CHAR_HEADING player_actor (fZAnglePlayerAir)
    fSyncAngle = fZAnglePlayerAir
    WHILE TRUE
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 1.5) (1 0 0 0 0)
            
            CLEO_CALL getDataJoystick 0 (LRStick UDStick)
            IF LRStick > 0  //Right
            AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
                fXAnglePlayerAir +=@ 1.5
            ELSE
                IF 0 > LRStick  //Left
                AND IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
                    fXAnglePlayerAir -=@ 1.5
                ENDIF
            ENDIF
            //-+--  Wall Run
            IF 0 > UDStick //Forward
            AND IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                FREEZE_CHAR_POSITION player_actor FALSE
                IF NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall"

                    CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.4 //0.60  //0.4
                    CLEAR_CHAR_TASKS player_actor
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall" "spider" 9.0 (1 1 1 0) -2
                    GOSUB playSfxWall
                ELSE
                    CLEO_CALL addForceToChar 0 player_actor (0.0 0.0 20.0) 10.0 //15.0
                    //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 5.0) (x y z)
                    //CLEO_CALL setCameraVerticalWallRun 0 (x y z) 1.5
                ENDIF
                CLAMP_FLOAT fXAnglePlayerAir -45.0 45.0 (fXAnglePlayerAir)
                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                /*
                CLAMP_FLOAT fXAnglePlayerAir -25.0 25.0 (fXAnglePlayerAir)
                fSyncMaxAngle = fXAnglePlayerAir * -1.0
                fYAnglePlayerAir = fSyncMaxAngle
                ABS_LVAR_FLOAT (fYAnglePlayerAir)
                fYAnglePlayerAir *= -0.39999991 //-0.39999
                fZAnglePlayerAir = fSyncAngle
                fZAnglePlayerAir +=@ fSyncMaxAngle
                SET_CHAR_ROTATION player_actor (fYAnglePlayerAir fXAnglePlayerAir fZAnglePlayerAir)
                */
            ELSE
                //-+-- Walk
                IF 0 > UDStick //Forward
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                    //This will only run once

                    IF NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall_B"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall_B"

                        SET_AUDIO_STREAM_STATE sfx STOP
                        CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08
                        IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_A"
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "walk_wall" "spider" 31.0 (1 1 1 0) -2
                            WAIT 0
                            FREEZE_CHAR_POSITION player_actor FALSE
                        ELSE
                            IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_C"
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall_B"
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "walk_wall_B" "spider" 31.0 (1 1 1 0) -2
                                WAIT 0
                                FREEZE_CHAR_POSITION player_actor FALSE
                            ELSE
                                IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_B"
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall_B"
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall_B"
                                    FREEZE_CHAR_POSITION player_actor TRUE
                                    //Transition between idle and walk_B
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("idleToWalkWall_B" "spider") 11.0 (0 1 1 0) -2
                                    WAIT 0
                                    WHILE IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall_B"
                                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("idleToWalkWall_B") (fProgress)
                                        IF fProgress > 0.95
                                            BREAK
                                        ENDIF
                                        WAIT 0
                                    ENDWHILE
                                    //walk_B
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "walk_wall_B" "spider" 31.0 (1 1 1 0) -2
                                    WAIT 0
                                    FREEZE_CHAR_POSITION player_actor FALSE
                                ELSE
                                    IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_D"
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall"                                    
                                        FREEZE_CHAR_POSITION player_actor TRUE
                                        //Transition between idle and walk
                                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("idleToWalkWall" "spider") 11.0 (0 1 1 0) -2
                                        WAIT 0
                                        WHILE IS_CHAR_PLAYING_ANIM player_actor "idleToWalkWall"
                                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("idleToWalkWall") (fProgress)
                                            IF fProgress > 0.95
                                                BREAK
                                            ENDIF
                                            WAIT 0
                                        ENDWHILE
                                        //walk
                                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "walk_wall" "spider" 31.0 (1 1 1 0) -2
                                        WAIT 0
                                        FREEZE_CHAR_POSITION player_actor FALSE
                                    ELSE
                                        //If player stop running, play animation walk
                                        IF IS_CHAR_PLAYING_ANIM player_actor "run_wall"
                                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                                            FREEZE_CHAR_POSITION player_actor TRUE
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "walk_wall" "spider" 31.0 (1 1 1 0) -2
                                            WAIT 0
                                            FREEZE_CHAR_POSITION player_actor FALSE
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ELSE
                        //This will run in a loop - throw player -up- "walking"
                        CLEO_CALL addForceToChar 0 player_actor (0.0 0.0 8.5) 4.0
                        //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 5.0) (x y z)
                        //CLEO_CALL setCameraVerticalWallRun 0 (x y z) 1.0
                    ENDIF
                    CLAMP_FLOAT fXAnglePlayerAir -45.0 45.0 (fXAnglePlayerAir)
                    SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                ELSE
                    //-+-- IDLE
                    IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                    AND NOT IS_BUTTON_PRESSED PAD1 LEFTSTICKY   //~k~~GO_FORWARD~ / ~k~~GO_BACK~
                        FREEZE_CHAR_POSITION player_actor TRUE
                        IF NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_A"
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_B"
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_C"
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_D"
                            SET_AUDIO_STREAM_STATE sfx STOP
                            SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                            CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08
                            CLEAR_CHAR_TASKS player_actor
                            GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
                            SWITCH randomVal
                                CASE 0
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_A" "spider" 11.0 (1 1 1 0) -2
                                    BREAK
                                //CASE 1
                                DEFAULT
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_C" "spider" 11.0 (1 1 1 0) -2
                                    BREAK
                            ENDSWITCH
                            //fXAnglePlayerAir = 0.0
                            timerb = 0
                        ENDIF
                    ELSE

                        //-+-- IDLE
                        FREEZE_CHAR_POSITION player_actor TRUE
                        //If is walking
                        IF IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_A"
                            SET_AUDIO_STREAM_STATE sfx STOP
                            SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                            CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_A" "spider" 11.0 (1 1 1 0) -2
                            timerb = 0
                        ELSE
                            //If is walking
                            IF IS_CHAR_PLAYING_ANIM player_actor "walk_wall_B"
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_C"
                                SET_AUDIO_STREAM_STATE sfx STOP
                                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                                CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_C" "spider" 11.0 (1 1 1 0) -2
                                timerb = 0                                                
                            ELSE
                                //If is Running
                                IF IS_CHAR_PLAYING_ANIM player_actor "run_wall"
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_A"
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_C"
                                    SET_AUDIO_STREAM_STATE sfx STOP
                                    SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                                    CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08
                                    GENERATE_RANDOM_INT_IN_RANGE 0 2 (randomVal)
                                    SWITCH randomVal
                                        CASE 0
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_A" "spider" 11.0 (1 1 1 0) -2
                                            BREAK
                                        //CASE 1
                                        DEFAULT
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_C" "spider" 11.0 (1 1 1 0) -2
                                            BREAK
                                    ENDSWITCH
                                    timerb = 0
                                ELSE
                                    IF timerb > 2000
                                        IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_A"
                                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_B"
                                        AND NOT IS_CHAR_PLAYING_ANIM player_actor "walkWallToIdle"
                                            SET_AUDIO_STREAM_STATE sfx STOP
                                            fXAnglePlayerAir = 0.0
                                            SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                                            CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08 
                                            //Transition between walk to idle
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("walkWallToIdle" "spider") 11.0 (0 1 1 0) -2
                                            WAIT 0
                                            WHILE IS_CHAR_PLAYING_ANIM player_actor "walkWallToIdle"
                                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("walkWallToIdle") (fProgress)
                                                IF fProgress > 0.95
                                                    BREAK
                                                ENDIF
                                                WAIT 0
                                            ENDWHILE
                                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_B" "spider" 51.0 (1 1 1 0) -2
                                            WAIT 0
                                        ELSE
                                            IF IS_CHAR_PLAYING_ANIM player_actor "wall_idle_C"
                                            AND NOT IS_CHAR_PLAYING_ANIM player_actor "wall_idle_D"
                                            AND NOT IS_CHAR_PLAYING_ANIM player_actor "walkBWallToIdle"                                            
                                                SET_AUDIO_STREAM_STATE sfx STOP
                                                fXAnglePlayerAir = 0.0
                                                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                                                CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.0 //0.08 
                                                //Transition between walk_B to idle
                                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("walkBWallToIdle" "spider") 11.0 (0 1 1 0) -2
                                                WAIT 0
                                                WHILE IS_CHAR_PLAYING_ANIM player_actor "walkBWallToIdle"
                                                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("walkBWallToIdle") (fProgress)
                                                    IF fProgress > 0.95
                                                        BREAK
                                                    ENDIF
                                                    WAIT 0
                                                ENDWHILE
                                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_idle_D" "spider" 51.0 (1 1 1 0) -2
                                                WAIT 0    
                                            ENDIF
                                        ENDIF
                                    ENDIF                                                  
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF //end Idle
                ENDIF //End walk
            ENDIF //End Run

            //-+--  Wall fall-JUMP
            IF IS_BUTTON_PRESSED PAD1 SQUARE   //~k~~PED_JUMPING~
            AND NOT IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                SET_AUDIO_STREAM_STATE sfx STOP
                fXAnglePlayerAir = 0.0
                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)
                CLEAR_CHAR_TASKS player_actor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "wall_jump_fall" "spider" 45.0 (0 1 1 0) -2
                WAIT 0
                WHILE IS_CHAR_PLAYING_ANIM player_actor ("wall_jump_fall")
                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("wall_jump_fall") (fProgress)
                    IF fProgress > 0.159
                    AND fProgress < 0.182
                        FREEZE_CHAR_POSITION player_actor FALSE
                        CLEO_CALL addForceToChar 0 player_actor (0.0 -1.5 3.0) 15.0
                    ENDIF
                    IF fProgress >= 0.7
                        GOTO endWall
                    ENDIF
                    WAIT 0
                ENDWHILE  
                GOTO endWall
            ENDIF

            //-+--  Wall reach top of building
            IF CLEO_CALL isClearInSight 0 player_actor (0.0 2.5 0.5) (1 0 0 0 0)

                RESTORE_CAMERA
                SET_AUDIO_STREAM_STATE sfx STOP
                //FREEZE_CHAR_POSITION player_actor TRUE
                fXAnglePlayerAir = 0.0
                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)      
                GET_CHAR_SPEED player_actor (fCharSpeed)
                IF 25.0 > fCharSpeed
                    fCharSpeed = 25.0
                ENDIF 
                fVelY = (2.0 * fCharSpeed)
                fVelZ = (0.03 * fCharSpeed)

                IF IS_BUTTON_PRESSED PAD1 SQUARE   //~k~~PED_JUMPING~
                AND IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                    IF IS_CHAR_PLAYING_ANIM player_actor "run_wall"
                        GOSUB REQUEST_webAnimations
                        GOSUB destroyWeb
                        GOSUB createWeb_b
                        IF IS_PC_USING_JOYPAD
                            SHAKE_PAD PAD1 200 1500
                        ENDIF
                        FREEZE_CHAR_POSITION player_actor FALSE
                        CLEO_CALL addForceToChar 0 player_actor (0.0 1.0 fVelZ) fCharSpeed    //ok
                        //CLEO_CALL addForceToChar 0 player_actor (0.0 5.0 8.0) 15.0 
                        CLEAR_CHAR_TASKS player_actor
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webThrow" "mweb") 56.0 (0 1 1 1) -2
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "jump_wall_top_C" "spider" 81.0 (0 1 1 1) -2
                        WAIT 0
                        SET_CHAR_ANIM_SPEED iWebActor "m_webThrow" 1.35
                        SET_CHAR_ANIM_SPEED player_actor "jump_wall_top_C" 1.35
                        
                        WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_C")
                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_wall_top_C") (fProgress)
                            IF fProgress >= 0.173   //frame 14/80
                            AND  0.200 > fProgress  //frame 16/80
                                IF NOT IS_OBJECT_ATTACHED baseObject
                                    ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (15.0 0.0 0.0)
                                ENDIF
                                //GOSUB attachWebB
                            ENDIF
                            /*
                            IF fProgress >= 0.0
                            AND  0.185 > fProgress                            
                                CLEO_CALL addForceToChar 0 player_actor (0.0 fVelZ fVelZ) fCharSpeed
                                //CLEO_CALL addForceToChar 0 player_actor (0.0 5.0 8.0) 10.0
                            ENDIF
                            */
                            IF fProgress >= 0.185   //frame 16/80
                            AND 0.325 > fProgress   //frame 26/80   //0.309
                                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x y z)
                                SET_CHAR_COORDINATES_SIMPLE player_actor x y z 
                            ELSE
                                IF IS_OBJECT_ATTACHED baseObject
                                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                                ENDIF
                                IF fProgress >= 0.325   //frame 26/80
                                AND 0.450 > fProgress   //frame 36/80
                                    CLEO_CALL addForceToChar 0 player_actor (0.0 fVelY 1.5) fCharSpeed
                                ELSE
                                    IF fProgress >= 0.750 //0.85 //frame 60/80  //0.494    
                                        BREAK
                                    ENDIF
                                ENDIF
                            ENDIF

                            WAIT 0
                        ENDWHILE
                        GOSUB destroyWeb
                        WHILE IS_BUTTON_PRESSED PAD1 SQUARE   //~k~~PED_JUMPING~
                            WAIT 0
                        ENDWHILE
                    ENDIF
                ELSE
                    IF IS_CHAR_PLAYING_ANIM player_actor "run_wall"
                    AND IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                        FREEZE_CHAR_POSITION player_actor FALSE
                        CLEO_CALL addForceToChar 0 player_actor (0.0 5.0 8.0) 15.0
                        CLEAR_CHAR_TASKS player_actor
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "jump_wall_top_A" "spider" 37.0 (0 1 1 0) -1
                        WAIT 0
                        SET_CHAR_ANIM_SPEED player_actor "jump_wall_top_A" 1.35
                        WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")
                            CLEO_CALL addForceToChar 0 player_actor (0.0 5.0 8.0) 10.0
                            GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_wall_top_A") (fProgress)
                            IF fProgress >= 0.5
                                //RESTORE_CAMERA
                                //RESTORE_CAMERA_JUMPCUT
                                BREAK
                            ENDIF
                            WAIT 0
                        ENDWHILE  
                        GOTO endWall       
                    ELSE
                        IF IS_CHAR_PLAYING_ANIM player_actor "walk_wall"
                        OR IS_CHAR_PLAYING_ANIM player_actor "walk_wall_B"
                            FREEZE_CHAR_POSITION player_actor FALSE
                            CLEO_CALL addForceToChar 0 player_actor (0.0 0.5 2.5) 15.0
                            CLEAR_CHAR_TASKS player_actor
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "jump_wall_top_A" "spider" 37.0 (0 1 1 0) -1
                            WAIT 0
                            SET_CHAR_ANIM_SPEED player_actor "jump_wall_top_A" 1.5
                            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_A")
                                CLEO_CALL addForceToChar 0 player_actor (0.0 0.5 2.0) 10.0
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_wall_top_A") (fProgress)
                                IF fProgress >= 0.5
                                    //RESTORE_CAMERA
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE 
                            GOTO endWall 
                            /*
                            FREEZE_CHAR_POSITION player_actor TRUE
                            //TASK_PLAY_ANIM player_actor "jump_wall_top_B" "spider" 76.0 (0 1 1 0) -1
                            //TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "jump_wall_top_B" "spider" 76.0 (0 1 1 0) -1
                            TASK_PLAY_ANIM_WITH_FLAGS player_actor "jump_wall_top_B" "spider" 76.0 (0 1 1 0) -2 (0 0)
                            //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.25 1.25 (x y z)
                            WAIT 0
                            SET_CHAR_ANIM_SPEED player_actor "jump_wall_top_B" 1.5
                            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_wall_top_B")
                                //GET_PED_POINTER player_actor (pStruct)
                                //flags = (pStruct + 0x46C)   
                                //WRITE_MEMORY flags BYTE 3 FALSE  // ONFOOT_STATE = ONFOOT = 3
                                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_wall_top_B") (fProgress)
                                IF fProgress >= 0.98 //0.711 //0.85
                                    FREEZE_CHAR_POSITION player_actor FALSE
                                    RESTORE_CAMERA
                                    BREAK
                                ENDIF
                                WAIT 0
                            ENDWHILE  
                            FREEZE_CHAR_POSITION player_actor FALSE
                            CLEO_CALL addForceToChar 0 player_actor (0.0 1.25 0.75) 10.0
                            GOTO endWall  
                            */
                        ELSE
                            FREEZE_CHAR_POSITION player_actor FALSE
                            CLEAR_CHAR_TASKS player_actor
                            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                            GOTO endWall  
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        
        ELSE
            // Jump for building-obstacles
            IF NOT IS_CHAR_PLAYING_ANIM player_actor "pkJumpE"
                SET_AUDIO_STREAM_STATE sfx STOP
                fXAnglePlayerAir = 0.0
                SET_CHAR_ROTATION player_actor (0.0 fXAnglePlayerAir fZAnglePlayerAir)                    
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "pkJumpE" "spider" 25.0 (1 1 1 0) -2
                timera = 0
                WHILE 500 > timera
                    CLEO_CALL addForceToChar 0 player_actor 0.0 -1.0 1.0 10.0
                    WAIT 0
                ENDWHILE
                timera = 0
                WHILE 500 > timera
                    CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 1.0 10.0
                    WAIT 0
                ENDWHILE
                CLEAR_CHAR_TASKS player_actor
            ENDIF
        ENDIF

        //reset
        IF IS_KEY_PRESSED VK_TAB
        AND IS_KEY_PRESSED VK_SPACE
        AND IS_KEY_PRESSED VK_BACK
            WHILE   IS_KEY_PRESSED VK_BACK
                WAIT 0
            ENDWHILE
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            RESTORE_CAMERA
            RESTORE_CAMERA_JUMPCUT
            GOTO endWall
        ENDIF

        WAIT 0
    ENDWHILE

    endWall:
    RESTORE_CAMERA
    //RESTORE_CAMERA_JUMPCUT
    FREEZE_CHAR_POSITION player_actor FALSE
RETURN

}
SCRIPT_END
REQUIRE funcs.sc

//-+-- Internal Threads (CLEO+)
{
sp_tmp_internalThread:
LVAR_INT player_actor toggleSpiderMod isInMainMenu
LVAR_FLOAT currentTime
GET_PLAYER_CHAR 0 player_actor 

WHILE TRUE
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor

        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_E_A") (currentTime)
                        IF currentTime >= 0.977     //frame 86/88
                            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_E_A") 0     //stop:0
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_E_C") (currentTime)
                        IF currentTime >= 0.978     //frame 88/90
                            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_E_C") 0
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A_R")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("swing_E_A_R")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_E_A_R") (currentTime)
                        IF currentTime >= 0.973     //frame 110/113
                            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_E_A_R") 0
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C_R")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("swing_E_C_R")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("swing_E_C_R") (currentTime)
                        IF currentTime >= 0.978     //frame 88/90
                            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("swing_E_C_R") 0
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_front")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_front")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_front") (currentTime)
                        IF currentTime >= 0.938     //frame 15/16
                            SET_CLEO_SHARED_VAR varStatusLevelChar 2
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_back")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_back")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_back") (currentTime)
                        IF currentTime >= 0.938     //frame 15/16
                            SET_CLEO_SHARED_VAR varStatusLevelChar 4
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_left")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_left")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_left") (currentTime)
                        IF currentTime >= 0.938     //frame 15/16
                            SET_CLEO_SHARED_VAR varStatusLevelChar 2
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF
                IF IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_right")
                    WHILE IS_CHAR_PLAYING_ANIM player_actor ("fall_acrob_right")
                        GET_CHAR_ANIM_CURRENT_TIME player_actor ("fall_acrob_right") (currentTime)
                        IF currentTime >= 0.938     //frame 15/16
                            SET_CLEO_SHARED_VAR varStatusLevelChar 2
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE    
                ENDIF

                IF HAS_CHAR_GOT_WEAPON player_actor WEAPONTYPE_BRASSKNUCKLE
                    SET_CURRENT_CHAR_WEAPON player_actor WEAPONTYPE_BRASSKNUCKLE
                ELSE
                    CONST_INT model_brassknuckle 331    //#BRASSKNUCKLE
                    REQUEST_MODEL model_brassknuckle   
                    LOAD_ALL_MODELS_NOW
                    WAIT 0
                    GIVE_WEAPON_TO_CHAR player_actor WEAPONTYPE_BRASSKNUCKLE 1
                    WAIT 0
                    MARK_MODEL_AS_NO_LONGER_NEEDED model_brassknuckle
                    SET_CURRENT_CHAR_WEAPON player_actor WEAPONTYPE_BRASSKNUCKLE
                ENDIF
            
            ENDIF
        //ELSE
            //USE_TEXT_COMMANDS FALSE
            //WAIT 50
            //TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE
}

{
sp_cam_internalThread:
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
LVAR_INT toggleSpiderMod isInMainMenu iTempVar iTempVar2
LVAR_FLOAT x[3] y[3] z[3] fRandomVal[2] fAngle[2] fDistance fCurrentTime

WHILE TRUE
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                // Web Blossom Power - Camera
                IF IS_CHAR_PLAYING_ANIM player_actor "pow_web_blossom"
                    fAngle[0] = 180.0
                    fAngle[1] = 35.0
                    fDistance = 6.0     //distance camera
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "pow_web_blossom"
                        GOSUB web_blossom_cam_mode
                        WAIT 0
                    ENDWHILE
                    RESTORE_CAMERA
                    RESTORE_CAMERA_JUMPCUT
                    WAIT 50
                ENDIF

                // Air Combo - Camera
                IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_1"
                    fAngle[0] = 180.0
                    fAngle[1] = 20.0
                    fDistance = 4.5     //distance camera
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_1"
                        GOSUB air_fight_cam_mode
                        WAIT 0
                    ENDWHILE
                    IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_2"
                        WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_2"
                            GOSUB air_fight_cam_mode
                            WAIT 0
                        ENDWHILE
                        IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_3"
                            WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_3"
                                GOSUB air_fight_cam_mode
                                WAIT 0
                            ENDWHILE
                            IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_4"
                                WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_4"
                                    GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_4" (fCurrentTime)
                                    IF fCurrentTime >= 0.625    //45/72
                                        BREAK
                                    ENDIF
                                    GOSUB air_fight_cam_mode
                                    WAIT 0
                                ENDWHILE
                                WAIT 0
                            ELSE
                                IF IS_CHAR_PLAYING_ANIM player_actor "air_combo_4b"
                                    WHILE IS_CHAR_PLAYING_ANIM player_actor "air_combo_4b"
                                        GET_CHAR_ANIM_CURRENT_TIME player_actor "air_combo_4b" (fCurrentTime)
                                        IF fCurrentTime >= 0.663    //55/83
                                            BREAK
                                        ENDIF
                                        GOSUB air_fight_cam_mode
                                        WAIT 0
                                    ENDWHILE
                                    WAIT 0
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                    RESTORE_CAMERA
                    RESTORE_CAMERA_JUMPCUT
                    WAIT 50
                ENDIF
                /*
                // Swing Kick - Camera
                IF IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
                    fAngle[0] = 180.0
                    fAngle[1] = 20.0
                    fDistance = 4.0     //distance camera
                    WHILE IS_CHAR_PLAYING_ANIM player_actor "swing_kick_1"
                        GET_CHAR_ANIM_CURRENT_TIME player_actor "swing_kick_1" (fCurrentTime)
                        IF fCurrentTime >= 0.822 //60/73
                            BREAK
                        ENDIF
                        GOSUB air_fight_cam_mode
                        WAIT 0
                    ENDWHILE
                    RESTORE_CAMERA
                    RESTORE_CAMERA_JUMPCUT
                    WAIT 50
                ENDIF
                */

            ENDIF
        //ELSE
            //WAIT 50
            //TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

web_blossom_cam_mode:
    IF IS_PC_USING_JOYPAD
        CLEO_CALL getDataJoystick 0 (iTempVar iTempVar2)
        CSET_LVAR_FLOAT_TO_LVAR_INT (fRandomVal[0]) iTempVar
        CSET_LVAR_FLOAT_TO_LVAR_INT (fRandomVal[1]) iTempVar2
    ELSE
        GET_PC_MOUSE_MOVEMENT (fRandomVal[0] fRandomVal[1])
    ENDIF
    fRandomVal[0] /= 2.5 //sensivity X
    fRandomVal[1] /= 2.5 //sensivity Y
    IF NOT fRandomVal[0] = 0.0
        fAngle[0] +=@ fRandomVal[0]
    ENDIF
    IF NOT fRandomVal[1] = 0.0
        IF IS_MOUSE_USING_VERTICAL_INVERSION
            fAngle[1] -=@ fRandomVal[1]
        ELSE
            fAngle[1] +=@ fRandomVal[1]
        ENDIF
    ENDIF
    CLAMP_FLOAT fAngle[1] 10.0 50.0 (fAngle[1]) //80.0 50.0     50.0 10.0
    SIN fAngle[0] (x[2])
    COS fAngle[0] (y[2])
    COS fAngle[1] (z[2])
    x[2] *= fDistance
    y[2] *= fDistance
    z[2] *= fDistance
    ATTACH_CAMERA_TO_CHAR_LOOK_AT_CHAR player_actor (x[2] y[2] z[2]) player_actor 0.0 2 //0.5
RETURN

air_fight_cam_mode:
    IF IS_PC_USING_JOYPAD
        CLEO_CALL getDataJoystick 0 (iTempVar iTempVar2)
        CSET_LVAR_FLOAT_TO_LVAR_INT (fRandomVal[0]) iTempVar
        CSET_LVAR_FLOAT_TO_LVAR_INT (fRandomVal[1]) iTempVar2
    ELSE
        GET_PC_MOUSE_MOVEMENT (fRandomVal[0] fRandomVal[1])
    ENDIF
    fRandomVal[0] /= 2.5 //sensivity X
    fRandomVal[1] /= 2.5 //sensivity Y
    IF NOT fRandomVal[0] = 0.0
        fAngle[0] +=@ fRandomVal[0]
    ENDIF
    IF NOT fRandomVal[1] = 0.0
        IF IS_MOUSE_USING_VERTICAL_INVERSION
            fAngle[1] -=@ fRandomVal[1]
        ELSE
            fAngle[1] +=@ fRandomVal[1]
        ENDIF
    ENDIF
    CLEO_CALL set_max_min_cycle_float 0 fAngle[0] 360.0 0.0 (fAngle[0])
    CLAMP_FLOAT fAngle[1] 10.0 30.0 (fAngle[1])
    SIN fAngle[0] (x[2])
    COS fAngle[0] (y[2])
    COS fAngle[1] (z[2])
    x[2] *= fDistance
    y[2] *= fDistance
    z[2] *= fDistance
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (x[2] y[2] z[2]) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 1.0 3.0) (x[1] y[1] z[1])    //camera point_at
    SET_FIXED_CAMERA_POSITION x[0] y[0] z[0] 0.0 0.0 0.0
    POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
RETURN
}










                /*
                IF CLEO_CALL isClearInSight 0 player_actor (0.0 1.0 0.0) (1 0 0 0 0)    //Front
                    IF CLEO_CALL isClearInSight 0 player_actor (-1.0 0.0 0.0) (1 0 0 0 0)   // Left
                        IF CLEO_CALL isClearInSight 0 player_actor (1.0 0.0 0.0) (1 0 0 0 0)    //Right

                            // Jump From Building
                            IF CLEO_CALL isSideOfBuildingInOffset 0 player_actor (0.0 2.0 -0.5) (0.0 -1.5 -1.5)
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

                                        IF IS_BUTTON_PRESSED PAD1 CROSS  // ~k~~PED_SPRINT~
                                        AND IS_CHAR_PLAYING_ANIM player_actor ("sprint_civi")
                                            GOSUB REQUEST_SwingAnimations
                                            fVelY = 1.75
                                            fVelZ = 2.15
                                            fAmplitude = 6.0
                                            CLEAR_CHAR_TASKS player_actor
                                            CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
                                            GOSUB TASK_PLAY_AcrobaticAnimation
                                            WAIT 0  
                                        ENDIF
                                    ENDIF
                                ENDIF

                            ENDIF

                        ELSE
                            //If found wall at right
                            IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                                GOSUB spideyFrontWallRight
                                WAIT 100
                                //GOTO noswing
                            ENDIF
                        ENDIF

                    ELSE
                        //If found wall at left
                        IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                            GOSUB spideyFrontWallLeft
                            WAIT 100
                            //GOTO noswing
                        ENDIF
                    ENDIF
                ELSE
                    // If found wall in front
                    IF NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_B"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_R_A"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_A"
                    AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall_L_B"                        
                        IF IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~
                            GOSUB spideyFrontWall
                            WAIT 100
                            //GOTO noswing
                        ENDIF
                    ENDIF
                ENDIF
                */
