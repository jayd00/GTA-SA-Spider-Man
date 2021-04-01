// by J16D
// Suspension Matrix
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT max_time 8000 //ms
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wf
LVAR_FLOAT v1 v2 v3     //passed
WAIT 0
LVAR_INT player_actor p i iChar iObj sfx fx[2] iTempVar
LVAR_FLOAT x[3] y[3] z[3] fDistance zAngle xAngle fCurrentDistance fCurrentTime

GET_PLAYER_CHAR 0 player_actor
WAIT 0
REQUEST_MODEL 1598  //beachball
LOAD_ALL_MODELS_NOW
//CREATE_OBJECT 1598 0.0 0.0 0.0 (iObj)
CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (iObj)
SET_OBJECT_PROOFS iObj 1 1 1 1 1 //BP FP EP CP MP
SET_OBJECT_MASS iObj 0.001
SET_OBJECT_COLLISION iObj FALSE
SET_OBJECT_SCALE iObj 0.05
MARK_MODEL_AS_NO_LONGER_NEEDED 1598

//set angle - direction
IF NOT v1 = 0.0
AND NOT v2 = 0.0
AND NOT v3 = 0.0
    x[0] = v1
    y[0] = v2
    z[0] = v3
    iTempVar = 1
ELSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 1.0 0.0) (x[0] y[0] z[0])
    iTempVar = 0
ENDIF
GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
xAngle *= -1.0
GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
SET_CHAR_HEADING player_actor zAngle
WAIT 0

IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootR")  //Right Hand
OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p")     //Right Hand
    CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //BONE_RIGHTHAND
    SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
ELSE
    IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootL")  //Left Hand
    OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p_L")   //Left Hand
        CLEO_CALL getActorBonePos 0 player_actor 35 (x[2] y[2] z[2])    //BONE_LEFTHAND
        SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
    ELSE
        ATTACH_OBJECT_TO_CHAR iObj player_actor (0.0 0.5 0.25) (0.0 0.0 0.0)
        DETACH_OBJECT iObj (0.0 0.0 0.0) FALSE
    ENDIF
ENDIF

SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
SET_OBJECT_COLLISION iObj TRUE
SET_OBJECT_RECORDS_COLLISIONS iObj TRUE
IF iTempVar = 0
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 4.0 0.0) (x[0] y[0] z[0])
    CLEO_CALL setObjectVelocityTo 0 iObj (x[0] y[0] z[0]) 30.0  //40.0
    WAIT 25
ENDIF
//iTempVar = 0
//GOSUB play_Sfx

IF NOT HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
    WHILE NOT HAS_OBJECT_COLLIDED_WITH_ANYTHING iObj
        IF NOT iTempVar = 0 
            CLEO_CALL setObjectVelocityTo 0 iObj (x[0] y[0] z[0]) 50.0
        ENDIF

        WAIT 0
    ENDWHILE
ENDIF
SET_OBJECT_COLLISION iObj FALSE
SET_OBJECT_VISIBLE iObj FALSE
SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle

GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[2] y[2] z[2])
GET_GROUND_Z_FOR_3D_COORD (x[2] y[2] z[2]) (z[2])
z[2] += 0.15
CREATE_FX_SYSTEM SP_SSM_B (x[2] y[2] z[2]) 4 (fx[0])
PLAY_AND_KILL_FX_SYSTEM fx[0]
WAIT 200
CREATE_FX_SYSTEM SP_SSM_A (x[2] y[2] z[2]) 4 (fx[1])
PLAY_AND_KILL_FX_SYSTEM fx[1]

iTempVar = 1
GOSUB play_Sfx
timera = 0
timerb = 0
WHILE max_time > timera
    GOSUB add_particle_fx
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
        IF DOES_CHAR_EXIST iChar
        AND NOT IS_CHAR_DEAD iChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
            IF NOT IS_CHAR_IN_ANY_CAR iChar
            AND NOT IS_CHAR_ON_ANY_BIKE iChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar

                IF LOCATE_CHAR_DISTANCE_TO_OBJECT iChar iObj 4.35
                    IF GOSUB is_not_char_playing_anims
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
                        z[1] += 1.0
                        SET_CHAR_COORDINATES_SIMPLE iChar x[1] y[1] z[1]
                        IF IS_CHAR_REALLY_IN_AIR iChar
                            GOSUB REQUEST_Animations
                            CLEAR_CHAR_TASKS iChar
                            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("sp_wf_a" "spider") 23.0 (1 1 1 0) -2
                            WAIT 0
                        ENDIF
                    ENDIF
                    SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 8.0) (x[0] y[0] z[0])
                    CLEO_CALL setCharVelocityTo 0 iChar (x[0] y[0] z[0]) 8.0
                ENDIF

            ENDIF
        ENDIF
    ENDWHILE
    WAIT 0
ENDWHILE

iTempVar = 2
GOSUB play_Sfx

timera = 0
WHILE 2000 > timera
    i = 0
    WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
        IF DOES_CHAR_EXIST iChar
        AND NOT IS_CHAR_DEAD iChar
        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
            IF NOT IS_CHAR_IN_ANY_CAR iChar
            AND NOT IS_CHAR_ON_ANY_BIKE iChar
            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                //IF LOCATE_CHAR_DISTANCE_TO_OBJECT iChar iObj 4.5
                    IF NOT IS_CHAR_REALLY_IN_AIR iChar
                        IF IS_CHAR_PLAYING_ANIM iChar "sp_wf_a"
                            GOSUB REQUEST_Animations
                            CLEAR_CHAR_TASKS iChar
                            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                            TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("sp_wf_b" "spider") 112.0 (0 1 1 0) -1
                            //TASK_FALL_AND_GET_UP iChar TRUE 4000
                            WAIT 0
                            DAMAGE_CHAR iChar 30 TRUE
                        ENDIF
                    ENDIF
                //ENDIF
            ENDIF
        ENDIF
    ENDWHILE
    WAIT 0
ENDWHILE

IF DOES_OBJECT_EXIST iObj
    DELETE_OBJECT iObj
ENDIF
REMOVE_AUDIO_STREAM sfx
REMOVE_ANIMATION "spider"
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

is_not_char_playing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

add_particle_fx:
    IF DOES_OBJECT_EXIST iObj
        IF timerb > 450
            CREATE_FX_SYSTEM SP_SSM_A (x[2] y[2] z[2]) 4 (fx[1])
            PLAY_AND_KILL_FX_SYSTEM fx[1]
            timerb = 0
        ENDIF
    ENDIF
    /*
    IF DOES_OBJECT_EXIST iObj
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 -0.001) (x[0] y[0] z[0])
        x[1] = x[0]
        y[1] = y[0]
        z[1] = z[0]
        z[1] += 8.0
        ADD_SMOKE_PARTICLE (x[0] y[0] z[0]) (2.0 2.0 z[1]) (0.9 0.9 0.9 1.0) 0.70 0.01
    ENDIF
    */
RETURN

play_Sfx:
    SET_AUDIO_STREAM_STATE sfx 0
    REMOVE_AUDIO_STREAM sfx
    IF DOES_OBJECT_EXIST iObj
        SWITCH iTempVar
            CASE 0
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\wshot6a.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot6a.mp3" (sfx)
                    SET_AUDIO_STREAM_STATE sfx 1
                    SET_AUDIO_STREAM_VOLUME sfx 0.7
                ENDIF
                BREAK
            CASE 1
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\wshot6b.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot6b.mp3" (sfx)
                    SET_AUDIO_STREAM_STATE sfx 1
                    SET_AUDIO_STREAM_LOOPED sfx TRUE
                    SET_AUDIO_STREAM_VOLUME sfx 0.7
                ENDIF
                BREAK
            CASE 2
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\wshot6c.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot6c.mp3" (sfx)
                    SET_AUDIO_STREAM_STATE sfx 1
                    SET_AUDIO_STREAM_VOLUME sfx 0.7
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
//CLEO_CALL setObjectVelocityTo 0 iObject (x y z) Amp
setObjectVelocityTo:
    LVAR_INT iObj   //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    IF DOES_OBJECT_EXIST iObj
        x[1] = xIn
        y[1] = yIn
        z[1] = zIn
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
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
        SET_OBJECT_DYNAMIC iObj TRUE
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
        WAIT 0
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocityTo 0 scplayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer
    LVAR_FLOAT xIn yIn zIn
    LVAR_FLOAT iAmplitude
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
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
