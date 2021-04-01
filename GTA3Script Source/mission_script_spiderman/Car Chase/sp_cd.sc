// by J16D
// Car Chase SUB script - police_chase
// Format:
//      STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_cd.cs" {iVehicle} {iChar_enemy1} {iChar_enemy2}

SCRIPT_START
{
SCRIPT_NAME sp_cd   //
LVAR_INT veh char1 char2    //in

LVAR_INT copCar[2] copChar[4]
LVAR_FLOAT x[4] y[4] z[4] zAngle
LVAR_INT flag_player_on_mission flag_cop1_killed flag_cop2_killed cop_kills_counter
LVAR_INT iTaskStatus iDecisionHate

IF DOES_VEHICLE_EXIST veh
    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (30.0 -20.0 0.0) (x[2] y[2] z[2])
ELSE
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF
/*x[0] = x[2] + 20.0
x[2] = x[2] - 20.0
y[0] = y[2] - 5.0
y[2] = y[2] - 30.0
GENERATE_RANDOM_FLOAT_IN_RANGE x[0] x[2] (x[2])
GENERATE_RANDOM_FLOAT_IN_RANGE y[0] y[2] (y[2])*/
//GET_NTH_CLOSEST_CAR_NODE (x[2] y[2] z[2]) 2 (x[1] y[1] z[1])
GET_CLOSEST_CAR_NODE_WITH_HEADING x[2] y[2] z[2] (x[1] y[1] z[1]) (zAngle)
GOSUB load_and_create_cops
GOSUB readVars
flag_cop1_killed = FALSE
flag_cop2_killed = FALSE
cop_kills_counter = 0

WHILE flag_player_on_mission > 0
    GOSUB readVars
    //PRINT_FORMATTED_NOW "mission: %i" 1 flag_player_on_mission
    IF DOES_CHAR_EXIST char1
        IF NOT IS_CHAR_IN_ANY_CAR char1
            IF NOT IS_CHAR_DEAD char1
                GET_SCRIPT_TASK_STATUS copChar[0] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                IF iTaskStatus = 7  //-1
                    TASK_KILL_CHAR_ON_FOOT copChar[0] char1
                ENDIF
            ELSE
                IF DOES_CHAR_EXIST char2
                    IF NOT IS_CHAR_IN_ANY_CAR char2
                        IF NOT IS_CHAR_DEAD char2
                            GET_SCRIPT_TASK_STATUS copChar[0] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                            IF iTaskStatus = 7  //-1
                                TASK_KILL_CHAR_ON_FOOT copChar[0] char2
                            ENDIF
                        ELSE
                            BREAK
                        ENDIF
                    ENDIF
                ELSE
                    BREAK
                ENDIF
            ENDIF
        ENDIF
    ENDIF

    IF DOES_CHAR_EXIST char2
        IF NOT IS_CHAR_IN_ANY_CAR char2
            IF NOT IS_CHAR_DEAD char2
                GET_SCRIPT_TASK_STATUS copChar[1] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                IF iTaskStatus = 7  //-1
                    TASK_KILL_CHAR_ON_FOOT copChar[1] char2
                ENDIF
            ELSE
                IF DOES_CHAR_EXIST char1
                    IF NOT IS_CHAR_IN_ANY_CAR char1
                        IF NOT IS_CHAR_DEAD char1
                            GET_SCRIPT_TASK_STATUS copChar[1] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                            IF iTaskStatus = 7  //-1
                                TASK_KILL_CHAR_ON_FOOT copChar[1] char1
                            ENDIF
                        ELSE
                            BREAK
                        ENDIF
                    ENDIF
                ELSE
                    BREAK
                ENDIF
            ENDIF
        ENDIF
    ENDIF

    IF DOES_CHAR_EXIST copChar[0]
        IF IS_CHAR_DEAD copChar[0]
        AND flag_cop1_killed = FALSE
            flag_cop1_killed = TRUE
            MARK_CHAR_AS_NO_LONGER_NEEDED copChar[0]
            cop_kills_counter ++
            BREAK
        ENDIF
    ENDIF
    IF DOES_CHAR_EXIST copChar[1]
        IF IS_CHAR_DEAD copChar[1]
        AND flag_cop2_killed = FALSE
            flag_cop2_killed = TRUE
            MARK_CHAR_AS_NO_LONGER_NEEDED copChar[1]
            cop_kills_counter ++
            BREAK
        ENDIF
    ENDIF

    IF cop_kills_counter >= 2
        BREAK
    ENDIF
    IF IS_PLAYER_PLAYING 0
    ELSE
        BREAK
    ENDIF
    IF DOES_VEHICLE_EXIST veh
        IF NOT LOCATE_CAR_DISTANCE_TO_CAR copcar[0] veh 100.0
            BREAK
        ENDIF
    ELSE
        BREAK
    ENDIF
    WAIT 0
ENDWHILE
//PRINT_FORMATTED_NOW "Police Has Stopped!" 1500
IF DOES_VEHICLE_EXIST copCar[0]
    MARK_CAR_AS_NO_LONGER_NEEDED copCar[0]
ENDIF
IF DOES_CHAR_EXIST copChar[0]
    MARK_CHAR_AS_NO_LONGER_NEEDED copChar[0]
ENDIF
IF DOES_CHAR_EXIST copChar[1]
    MARK_CHAR_AS_NO_LONGER_NEEDED copChar[1]
ENDIF
REMOVE_DECISION_MAKER iDecisionHate
WAIT 10
TERMINATE_THIS_CUSTOM_SCRIPT

readVars:
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
RETURN

load_and_create_cops:
REQUEST_MODEL SFPD1
REQUEST_MODEL COPCARLA
REQUEST_MODEL COLT45
LOAD_ALL_MODELS_NOW

LOAD_CHAR_DECISION_MAKER 4 (iDecisionHate)
SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_COP
SET_RELATIONSHIP 4 PEDTYPE_COP PEDTYPE_MISSION1

CREATE_CAR COPCARLA (x[1] y[1] z[1]) (copCar[0])
SET_CAR_HEALTH copCar[0] 2000
SET_CAR_PROOFS copCar[0] FALSE TRUE TRUE TRUE FALSE
    GET_CAR_HEADING veh (zAngle)
    SET_CAR_HEADING copCar[0] zAngle
    SET_CAR_TRACTION copCar[0] 2.0
    CAR_WANDER_RANDOMLY copCar[0]
    SET_CAN_BURST_CAR_TYRES copCar[0] TRUE
    SET_CAR_DRIVING_STYLE copCar[0] DRIVINGMODE_AVOIDCARS
    SET_CAR_CRUISE_SPEED copCar[0] 60.0
    SET_CAR_AVOID_LEVEL_TRANSITIONS copCar[0] TRUE
    SET_CAR_FOLLOW_CAR copcar[0] veh 10.0
    SWITCH_CAR_SIREN copcar[0] TRUE

CREATE_CHAR_INSIDE_CAR copCar[0] PEDTYPE_COP SFPD1 (copChar[0])
    SET_CHAR_DECISION_MAKER copChar[0] iDecisionHate
    SET_CHAR_HEALTH copChar[0] 100
    GIVE_WEAPON_TO_CHAR copChar[0] WEAPONTYPE_PISTOL 999999
    SET_CHAR_ACCURACY copChar[0] 50
    TASK_DRIVE_BY copChar[0] -1 veh (0.0 0.0 0.0) 100.0 (5 0) 65   //from the window toko to the front of the view
    SET_FOLLOW_NODE_THRESHOLD_DISTANCE copChar[0] 200.0     //Sets the range within which the char responds to events

CREATE_CHAR_AS_PASSENGER copCar[0] PEDTYPE_COP SFPD1 0 (copChar[1])
    SET_CHAR_DECISION_MAKER copChar[1] iDecisionHate
    SET_CHAR_HEALTH copChar[1] 100
    GIVE_WEAPON_TO_CHAR copChar[1] WEAPONTYPE_PISTOL 999999
    SET_CHAR_ACCURACY copChar[1] 50
    TASK_DRIVE_BY copChar[1] char2 -1 (0.0 0.0 0.0) 100.0 (4 1) 100
    //TASK_DRIVE_BY copChar[1] char2 copcar[0] (0.0 0.0 0.0) 150.0 (4 1) 100
    SET_FOLLOW_NODE_THRESHOLD_DISTANCE copChar[1] 200.0     //Sets the range within which the char responds to events

    MARK_MODEL_AS_NO_LONGER_NEEDED SFPD1
    MARK_MODEL_AS_NO_LONGER_NEEDED COPCARRU
    MARK_MODEL_AS_NO_LONGER_NEEDED COLT45
RETURN
}
SCRIPT_END

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
