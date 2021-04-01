// by J16D
// Web Bomb
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wg
LVAR_FLOAT v1 v2 v3     //passed
WAIT 0
LVAR_INT player_actor p i iChar pChar iObj sfx fx_system anim_seq
LVAR_INT iTempVar randomVal counter iTaskStatus
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
    CLEO_CALL setObjectVelocityTo 0 iObj (x[0] y[0] z[0]) 30.0
    WAIT 25
ENDIF

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
CREATE_FX_SYSTEM SP_WEB_D (x[2] y[2] z[2]) 4 (fx_system)
PLAY_AND_KILL_FX_SYSTEM fx_system
GOSUB play_Sfx

//store nearest chars
counter = 0
i = 0
WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)        
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
    AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
        IF NOT IS_CHAR_IN_ANY_CAR iChar
        AND NOT IS_CHAR_ON_ANY_BIKE iChar
        AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
            IF IS_CHAR_ON_SCREEN iChar 
                IF GOSUB is_not_char_playing_anims
                    IF LOCATE_CHAR_DISTANCE_TO_OBJECT iChar iObj 4.35
                        CLEO_CALL store_current_entity 0 counter iChar
                        counter ++
                        IF counter >= 10
                            BREAK
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
ENDWHILE
//delete throw obj
IF DOES_OBJECT_EXIST iObj
    DELETE_OBJECT iObj
ENDIF

//create objects(web) && assign task-anims
GOSUB REQUEST_Animations
counter = 0
WHILE 10 > counter 
    CLEO_CALL get_stored_entity 0 counter (iChar)
    IF DOES_CHAR_EXIST iChar
        IF GOSUB is_lying_on_the_floor
            GOSUB task_attach_to
            IF DOES_OBJECT_EXIST iObj
                counter += 10
                CLEO_CALL store_current_entity 0 counter iObj
                counter -= 10
            ENDIF
        ELSE
            GOSUB task_play_action
            IF DOES_OBJECT_EXIST iObj
                counter += 10
                CLEO_CALL store_current_entity 0 counter iObj
                TASK_PICK_UP_OBJECT iChar iObj (0.01 0.05 0.0) 1 16 "NULL" "NULL" 1   //1-chest?
                counter -= 10
            ENDIF
        ENDIF
    ENDIF
    counter ++
    WAIT 0
ENDWHILE

// delay time && clean - characters without animation
timera = 0
WHILE 5000 > timera
    counter = 0
    WHILE counter < 10
        CLEO_CALL get_stored_entity 0 counter (iChar)
        IF DOES_CHAR_EXIST iChar
            IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_pb")
                //IF IS_CHAR_PLAYING_ANIM iChar ("ko_ground")
                IF IS_CHAR_DEAD iChar
                    counter += 10
                    CLEO_CALL get_stored_entity 0 counter (iObj)
                    counter -= 10
                    IF DOES_OBJECT_EXIST iObj
                        CLEO_CALL attachObjectToActorOnBone 0 iChar iObj (0.1 0.0 0.0) 1  //root_bone
                        SET_OBJECT_ROTATION iObj 90.0 0.0 zAngle
                    ENDIF
                ELSE
                    TASK_KILL_CHAR_ON_FOOT iChar player_actor 
                    //MARK_CHAR_AS_NO_LONGER_NEEDED iChar
                    CLEO_CALL store_current_entity 0 counter 0x0
                    counter += 10
                    CLEO_CALL get_stored_entity 0 counter (iObj)
                    IF DOES_OBJECT_EXIST iObj
                        DELETE_OBJECT iObj
                    ENDIF
                    CLEO_CALL store_current_entity 0 counter 0x0
                    counter -= 10
                ENDIF
            ENDIF
        ENDIF
        counter ++
        WAIT 0
    ENDWHILE

    WAIT 0
ENDWHILE

//delete chars && objects (web)
counter = 0
WHILE 10 > counter
    CLEO_CALL get_stored_entity 0 counter (iChar)
    IF DOES_CHAR_EXIST iChar
        IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_pb")
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
            /*IF IS_CHAR_DEAD iChar
                MARK_CHAR_AS_NO_LONGER_NEEDED iChar
            ELSE
                IF CLEO_CALL is_char_gang_ped 0 iChar
                    TASK_KILL_CHAR_ON_FOOT iChar player_actor 
                ELSE
                    MARK_CHAR_AS_NO_LONGER_NEEDED iChar
                ENDIF
            ENDIF*/
        ENDIF
        CLEO_CALL store_current_entity 0 counter 0x0
    ENDIF
    counter += 10
    CLEO_CALL get_stored_entity 0 counter (iObj)
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    CLEO_CALL store_current_entity 0 counter 0x0
    counter -= 10
    counter += 1
    WAIT 0
ENDWHILE

//clean mem
counter = 0
WHILE 20 > counter 
    IF counter >= 0
    AND 9 >= counter
        CLEO_CALL get_stored_entity 0 counter (iChar)
        IF NOT iChar = 0
            CLEO_CALL store_current_entity 0 counter 0x0
        ENDIF
    ELSE
        CLEO_CALL get_stored_entity 0 counter (iObj)
        IF DOES_OBJECT_EXIST iObj
            DELETE_OBJECT iObj
        ENDIF
        CLEO_CALL store_current_entity 0 counter 0x0
    ENDIF
    //PRINT_FORMATTED_NOW "clean:%i" 500 counter
    counter ++
    WAIT 0
ENDWHILE

WAIT 0
REMOVE_AUDIO_STREAM sfx
REMOVE_ANIMATION "spider"
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

task_attach_to:
    GOSUB show_fx
    GOSUB create_object_web_b
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    WAIT 0
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    TASK_DIE_NAMED_ANIM iChar "ko_ground" "spider" 125.0 4000
    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("ko_ground" "spider") 125.0 (0 1 1 1) -1
    WAIT 0
RETURN

task_play_action:
    GOSUB show_fx
    GOSUB create_object_web
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
    WAIT 0
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    OPEN_SEQUENCE_TASK anim_seq
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("hit_wshoot_p" "spider") 121.0 (0 1 1 0) -1
        TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("hit_wshoot_pb" "spider") 31.0 (0 1 1 0) -1
        TASK_KILL_CHAR_ON_FOOT -1 player_actor
    CLOSE_SEQUENCE_TASK anim_seq
    PERFORM_SEQUENCE_TASK iChar anim_seq
    WAIT 0
    CLEAR_SEQUENCE_TASK anim_seq
    WAIT 0
    IF NOT IS_CHAR_DEAD iChar
        DAMAGE_CHAR iChar 5 TRUE
    ENDIF
RETURN

show_fx:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        CREATE_FX_SYSTEM_ON_CHAR SP_HIT_WEB iChar (0.0 0.25 0.5) 4 (fx_system)  //shootlight
        PLAY_AND_KILL_FX_SYSTEM fx_system
    ENDIF
RETURN

create_object_web:
    REQUEST_MODEL 6020  //pweb
    LOAD_ALL_MODELS_NOW
    WAIT 0
    //CREATE_OBJECT 6020 0.0 0.0 0.0 (iObj)
    CREATE_OBJECT_NO_SAVE 6020 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_COLLISION iObj FALSE
    SET_OBJECT_SCALE iObj 1.3
    MARK_MODEL_AS_NO_LONGER_NEEDED 6020
RETURN

create_object_web_b:
    REQUEST_MODEL 6021  //wwg
    LOAD_ALL_MODELS_NOW
    WAIT 0
    //CREATE_OBJECT 6021 0.0 0.0 0.0 (iObj)
    CREATE_OBJECT_NO_SAVE 6021 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_COLLISION iObj FALSE
    MARK_MODEL_AS_NO_LONGER_NEEDED 6021
RETURN

is_not_char_playing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")  //Web Shoot
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_lying_on_the_floor:
    IF IS_CHAR_PLAYING_ANIM iChar "KO_skid_front"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_skid_back"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_L"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_R"
    OR IS_CHAR_PLAYING_ANIM iChar "knife_hit_3"
    OR IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

play_Sfx:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot7a.mp3" (sfx)     //off
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1
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
//CLEO_CALL attachObjectToActorOnBone 0 char_handle object_attached offset_x offset_y offset_z bone
attachObjectToActorOnBone:
    LVAR_INT char_handle object_attached    //in
    LVAR_FLOAT offset_x offset_y offset_z   //in
    LVAR_INT bone   //in
    LVAR_INT tempi_1 tempi_2
    GET_PED_POINTER char_handle char_handle
    CALL_METHOD 0x532B20 char_handle 0 0 // sub_532B20
    char_handle += 0x18
    READ_MEMORY char_handle 4 0 char_handle
    CALL_FUNCTION_RETURN 0x734A40 1 1 char_handle char_handle // _clumpGetFirstSkinAtomicHAnimHierarchy
    IF NOT char_handle = 0
        CALL_FUNCTION_RETURN 0x7C51A0 2 2 bone char_handle bone // _RpHAnimIDGetIndex
        CALL_FUNCTION_RETURN 0x7C5120 1 1 char_handle char_handle // _RpHAnimHierarchyGetMatrixArray
        bone *= 0x40
        bone += char_handle
        GET_VAR_POINTER offset_x tempi_1
        CALL_FUNCTION 0x54EEF0 4 4 tempi_1 bone 1 tempi_1 // _transformPoints
        SET_OBJECT_HEADING object_attached 0.0
        GET_OBJECT_POINTER object_attached object_attached
        object_attached += 0x14
        READ_MEMORY object_attached 4 0 char_handle
        object_attached -= 0x14
        CALL_METHOD 0x411990 object_attached 0 0 // sub_411990
        CALL_METHOD 0x59AD20 char_handle 1 0 bone // CMatrix__copyTo
        CALL_METHOD 0x4241C0 object_attached 1 0 tempi_1 // CPlaceable__setPosition
        object_attached += 0x18
        READ_MEMORY object_attached 4 0 tempi_2
        IF NOT tempi_2 = 0
            tempi_2 += 0x4
            READ_MEMORY tempi_2 4 0 tempi_2
            tempi_2 += 0x10
            CALL_METHOD 0x59AD70 char_handle 1 0 tempi_2 // CMatrix__copyTo
            object_attached -= 0x18
            CALL_METHOD 0x532B00 object_attached 0 0 // _RwFrameUpdateObject
        ELSE
            object_attached -= 0x18
            CALL_METHOD 0x532B00 object_attached 0 0 // _RwFrameUpdateObject        
        ENDIF
    ELSE
        CLEO_RETURN 0
    ENDIF
CLEO_RETURN 0
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
//CLEO_CALL get_stored_entity 0 id (entity)
get_stored_entity:
    LVAR_INT id //in
    LVAR_INT ammo iOffset pActiveItem
    GET_LABEL_POINTER bytes76 (pActiveItem)
    iOffset = id
    iOffset *= 4
    pActiveItem += iOffset
    READ_MEMORY pActiveItem 4 FALSE (ammo)  
CLEO_RETURN 0 ammo
}
{
//CLEO_CALL store_current_entity 0 id entity
store_current_entity:
    LVAR_INT id entity //in
    LVAR_INT iOffset pActiveItem
    GET_LABEL_POINTER bytes76 (pActiveItem)
    iOffset = id
    iOffset *= 4
    pActiveItem += iOffset
    WRITE_MEMORY pActiveItem 4 entity FALSE
CLEO_RETURN 0
}

bytes76:
DUMP
//Peds
00 00 00 00 //+0    //id=0
00 00 00 00 //+4    //id=1
00 00 00 00 //+8    //id=2
00 00 00 00 //+12   //id=3
00 00 00 00 //+16   //id=4
00 00 00 00 //+20   //id=5
00 00 00 00 //+24   //id=6
00 00 00 00 //+28   //id=7
00 00 00 00 //+32   //id=8
00 00 00 00 //+36   //id=9
//Objects
00 00 00 00 //+40    //id=10
00 00 00 00 //+44    //id=11
00 00 00 00 //+48    //id=12
00 00 00 00 //+52   //id=13
00 00 00 00 //+56   //id=14
00 00 00 00 //+60   //id=15
00 00 00 00 //+44   //id=16
00 00 00 00 //+68   //id=17
00 00 00 00 //+72   //id=18
00 00 00 00 //+76   //id=19
ENDDUMP
