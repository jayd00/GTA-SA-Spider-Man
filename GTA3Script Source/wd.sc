// by J16D
// Spyder Drone
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT max_time 9000    //9 sec
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wd
LVAR_INT iCurrentBot    //passed
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu   //1:true 0: false
LVAR_INT iObj iChar fx_jet sfx iSearchLight iTempVar
LVAR_FLOAT x[5] y[5] z[5] xAngle zAngle fDistance fTempVar

GET_PLAYER_CHAR 0 player_actor
GOSUB create_spider_bot
GOSUB get_offset_char
CLEO_CALL setObjectPosSimple 0 iObj x[2] y[2] z[2]
GET_CHAR_HEADING player_actor (zAngle)
SET_OBJECT_HEADING iObj zAngle
iTempVar = 0
GOSUB play_Sfx

timera = 0
WHILE max_time > timera
    GOSUB assign_task_follow
    CLEO_CALL create_searchlight_on_object 0 iSearchLight iObj (iSearchLight)

    IF  CLEO_CALL is_char_hurting_char 0 player_actor (iChar)
        GOSUB assign_task_kill
    ENDIF
    IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 20.0
        BREAK
    ENDIF
    GOSUB readVars
    IF toggleSpiderMod = 0  //FALSE
    OR isInMainMenu = 1     //1:true 0:false
        BREAK
    ENDIF
    IF NOT IS_PLAYER_PLAYING player
        BREAK
    ENDIF
    IF IS_CHAR_IN_ANY_CAR player_actor
        BREAK
    ENDIF
    WAIT 0
ENDWHILE

end_scr:
    IF DOES_SEARCHLIGHT_EXIST iSearchLight
        DELETE_SEARCHLIGHT iSearchLight
    ENDIF
    iTempVar = 1
    GOSUB play_Sfx
    WAIT 850
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
    IF DOES_OBJECT_EXIST iObj
        KILL_FX_SYSTEM fx_jet
        DELETE_OBJECT iObj
    ENDIF
    WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

assign_task_follow:
    IF DOES_OBJECT_EXIST iObj
        GOSUB set_object_angle_player
        GET_DISTANCE_BETWEEN_COORDS_3D (x[2] y[2] z[2]) (x[1] y[1] z[1]) (fDistance)
        IF fDistance > 0.5
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.2 0.0) (x[0] y[0] z[0])
        ELSE
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
        ENDIF
        GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
        z[0] += 1.5
        SLIDE_OBJECT iObj (x[0] y[0] z[0]) (6.0 6.0 6.0) FALSE
    ENDIF
RETURN

assign_task_kill:
    CLEAR_CHAR_LAST_WEAPON_DAMAGE player_actor

    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        IF DOES_OBJECT_EXIST iObj

            update_bot_position:
            CLEO_CALL create_searchlight_on_object 0 iSearchLight iObj (iSearchLight)
            CLEO_CALL get_random_offset 0 2.5 (x[2] y[2] z[2])
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (x[2] y[2] z[2]) (x[3] y[3] z[3])
                GET_CHAR_COORDINATES iChar (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[3] y[3] z[3]) (x[1] y[1] z[1]) (1 1 0 0 0)    //buildings|cars|characters|objects|particles
                    WAIT 0
                    GOTO update_bot_position
                ENDIF
            ELSE
                GOTO end_task_kill
            ENDIF
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (x[2] y[2] z[2]) (x[3] y[3] z[3])
            GOSUB set_object_angle_char_b

            WHILE NOT LOCATE_OBJECT_3D iObj (x[3] y[3] z[3]) (1.5 1.5 1.5) FALSE
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.3 0.0) (x[0] y[0] z[0])
                GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
                z[0] += 1.5
                SLIDE_OBJECT iObj (x[0] y[0] z[0]) (6.0 6.0 6.0) FALSE
                IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 20.0
                    GOTO end_scr
                ENDIF
                IF IS_CHAR_DEAD iChar
                    GOTO end_task_kill
                ENDIF
                IF timera > max_time
                    GOTO end_scr
                ENDIF
                CLEO_CALL create_searchlight_on_object 0 iSearchLight iObj (iSearchLight)
                WAIT 0
            ENDWHILE

            CONST_INT delay_shoot 110
            timerb = 0
            fTempVar = 0.0
            WHILE TRUE
                IF IS_CHAR_DEAD iChar
                    BREAK
                ENDIF
                IF DOES_CHAR_EXIST iChar
                    GOSUB set_object_angle_char
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[3] y[3] z[3])
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.2 0.0) (x[1] y[1] z[1])
                    IF timerb > delay_shoot
                        FIRE_SINGLE_BULLET (x[1] y[1] z[1]) (x[3] y[3] z[3]) 2
                        timerb = 0
                    ENDIF
                ELSE
                    BREAK
                ENDIF
                fTempVar +=@ 1.0
                IF fTempVar > 50.0
                    WAIT 0
                    GOTO update_bot_position
                ENDIF
                IF timera > max_time
                    GOTO end_scr
                ENDIF
                CLEO_CALL create_searchlight_on_object 0 iSearchLight iObj (iSearchLight)
                WAIT 0
            ENDWHILE

            end_task_kill:
        ENDIF
    ENDIF
RETURN

set_object_angle_player:
    GOSUB get_offset_char
    GET_OBJECT_COORDINATES iObj (x[1] y[1] z[1])
    CLEO_CALL getZangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (zAngle)
    //GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[2] y[2]) (zAngle)
    CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (xAngle)
    xAngle *= -1.0
    SET_OBJECT_ROTATION iObj (xAngle 0.0 zAngle)
    SET_OBJECT_HEADING iObj zAngle
RETURN

set_object_angle_char:
    GET_CHAR_COORDINATES iChar (x[2] y[2] z[2])
    GET_OBJECT_COORDINATES iObj (x[1] y[1] z[1])
    CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (xAngle)
    xAngle *= -1.0
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[2] y[2]) (zAngle)
    SET_OBJECT_ROTATION iObj (xAngle 0.0 zAngle)
    SET_OBJECT_HEADING iObj zAngle
RETURN

set_object_angle_char_b:
    GET_OBJECT_COORDINATES iObj (x[1] y[1] z[1])
    CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[3] y[3] z[3]) (xAngle)
    xAngle *= -1.0
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[3] y[3]) (zAngle)
    SET_OBJECT_ROTATION iObj (xAngle 0.0 zAngle)
    SET_OBJECT_HEADING iObj zAngle
RETURN

get_offset_char:
    SWITCH iCurrentBot
        CASE 0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (-1.0 -1.0 0.5) (x[2] y[2] z[2])
            BREAK
        CASE 1
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (1.0 -1.0 0.5) (x[2] y[2] z[2])
            BREAK
        CASE 2
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (-1.0 1.0 0.5) (x[2] y[2] z[2])
            BREAK
        CASE 3
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (1.0 1.0 0.5) (x[2] y[2] z[2])
            BREAK
    ENDSWITCH
RETURN

create_spider_bot:
    REQUEST_MODEL 6022  //pbot
    LOAD_ALL_MODELS_NOW
    CREATE_OBJECT_NO_SAVE 6022 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_PROOFS iObj 1 1 1 1 1    //BP FP EP CP MP
    SET_OBJECT_COLLISION iObj FALSE
    SET_OBJECT_VISIBLE iObj TRUE
    SET_OBJECT_DYNAMIC iObj FALSE
    MARK_MODEL_AS_NO_LONGER_NEEDED 6022
    CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION JETPACK iObj (0.0 0.0 -0.05) (90.0 0.0 0.0) 1 (fx_jet)
    PLAY_FX_SYSTEM fx_jet
RETURN

play_Sfx:
    REMOVE_AUDIO_STREAM sfx
    IF DOES_OBJECT_EXIST iObj
        SWITCH iTempVar
            CASE 0
                IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot4a.mp3" (sfx)
                    SET_PLAY_3D_AUDIO_STREAM_AT_OBJECT sfx iObj
                    SET_AUDIO_STREAM_STATE sfx 1    //play
                    SET_AUDIO_STREAM_LOOPED sfx TRUE
                ENDIF
                BREAK
            CASE 1
                IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot4b.mp3" (sfx)     //off
                    SET_PLAY_3D_AUDIO_STREAM_AT_OBJECT sfx iObj
                    SET_AUDIO_STREAM_STATE sfx 1    //play
                ENDIF
                BREAK
        ENDSWITCH
    ENDIF
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

}
SCRIPT_END

//-+----- CALL SCM HELPERS
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
//CLEO_CALL setObjectPosSimple 0 obj x y z
setObjectPosSimple:
    LVAR_INT hObj // In
    LVAR_FLOAT x y z // In
    LVAR_INT pObj pMatrix pCoord
    IF DOES_OBJECT_EXIST hObj
        GET_OBJECT_POINTER hObj (pObj)
        pMatrix = pObj + 0x14
        READ_MEMORY pMatrix 4 FALSE (pMatrix)
        pCoord = pMatrix + 0x30
        WRITE_MEMORY pCoord 4 (x) FALSE
        pCoord += 0x4 
        WRITE_MEMORY pCoord 4 (y) FALSE
        pCoord += 0x4
        WRITE_MEMORY pCoord 4 (z) FALSE
    ENDIF
CLEO_RETURN 0 ()
}
{
//CLEO_CALL is_char_hurting_char 0 player_actor (iChar)
is_char_hurting_char:
    LVAR_INT scplayer   //in
    LVAR_INT pChar p i iChar
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (pChar)
        pChar += 0x764
        READ_MEMORY pChar 4 FALSE (p)
        IF p > 0
            READ_MEMORY 0x00B74490 4 FALSE (i)
            CALL_METHOD_RETURN 0x4442D0 i /*nparms*/1 /*pop*/0 /*struct*/p /*store*/(iChar)         //FUNC_GET_ACTOR_ID
            IF DOES_CHAR_EXIST iChar
                CLEO_RETURN 0 iChar
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 -1
}
{
//CLEO_CALL get_random_offset 0 fRadius (x y z)
get_random_offset:
    LVAR_FLOAT fRadius  //in
    LVAR_FLOAT x y z fAngle[2] fAmp
    GENERATE_RANDOM_FLOAT_IN_RANGE 20.0 50.0 (fAngle[0])
    SIN fAngle[0] (z)
    z *= fRadius
    COS fAngle[0] (fAmp)
    fAmp *= fRadius
    GENERATE_RANDOM_FLOAT_IN_RANGE 0.0 360.0 (fAngle[1])
    SIN fAngle[1] (y)
    COS fAngle[1] (x)            
    y *= fAmp
    x *= fAmp
    x *= -1.0
CLEO_RETURN 0 x y z
}
{
//CLEO_CALL create_searchlight_on_object 0 iSearchLight iObj (iSearchLight)
create_searchlight_on_object:
    LVAR_INT iSearchLight iObj   //in
    LVAR_FLOAT x[2] y[2] z[2]
    IF DOES_SEARCHLIGHT_EXIST iSearchLight
        DELETE_SEARCHLIGHT iSearchLight
    ENDIF
    IF DOES_OBJECT_EXIST iObj
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 -0.05) (x[0] y[0] z[0])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 -1.0) (x[1] y[1] z[1])
        CREATE_SEARCHLIGHT (x[0] y[0] z[0]) (x[1] y[1] z[1]) 20.0 0.1 (iSearchLight)
    ENDIF
CLEO_RETURN 0 iSearchLight
}
{
//CLEO_CALL getZangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*zAngle*/fSyncAngle)
getZangleBetweenPoints:
    LVAR_FLOAT xA yA zA
    LVAR_FLOAT xB yB zB
    LVAR_FLOAT fDegAngle
    xA -= xB
    yA -= yB
    GET_HEADING_FROM_VECTOR_2D xA yA (fDegAngle)
    fDegAngle += 180.0
CLEO_RETURN 0 fDegAngle
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