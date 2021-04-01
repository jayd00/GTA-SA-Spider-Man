// by J16D
// Web Shoot (A)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wa
LVAR_INT pChar  //passed 
WAIT 0
LVAR_INT player_actor p i iChar iObj fx_web anim_seq
LVAR_INT iTempVar randomVal
LVAR_FLOAT x[3] y[3] z[3] fDistance zAngle xAngle fCurrentTime
LVAR_FLOAT fCurrentDistance fTempVar

GET_PLAYER_CHAR 0 player_actor
IF DOES_CHAR_EXIST pChar
    IF IS_CHAR_DEAD pChar
        WAIT 0
        TERMINATE_THIS_CUSTOM_SCRIPT
    ELSE
        IF IS_CHAR_PLAYING_ANIM pChar ("hit_wshoot_p")  //Web Shoot
        OR IS_CHAR_PLAYING_ANIM pChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
        OR IS_CHAR_PLAYING_ANIM pChar ("ko_ground") //Web Shoot, Web Bomb
        OR IS_CHAR_PLAYING_ANIM pChar ("sp_wh_a")  //Trip Mine
        OR IS_CHAR_PLAYING_ANIM pChar ("sp_wh_b")  //Trip Mine
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDIF
ENDIF
WAIT 0

IF DOES_CHAR_EXIST pChar
    CLEO_CALL getActorBonePos 0 pChar 2 (x[0] y[0] z[0])    //BONE_PELVIS
ELSE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 10.0 -0.1) (x[0] y[0] z[0])
ENDIF
REQUEST_MODEL 1598  //beachball
LOAD_ALL_MODELS_NOW
//CREATE_OBJECT 1598 0.0 0.0 0.0 (iObj)
CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (iObj)
SET_OBJECT_PROOFS iObj 1 1 1 1 1 //BP FP EP CP MP
SET_OBJECT_COLLISION iObj FALSE
SET_OBJECT_SCALE iObj 0.001
MARK_MODEL_AS_NO_LONGER_NEEDED 1598

//set angle - direction
GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
xAngle *= -1.0
GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
SET_CHAR_HEADING player_actor zAngle
WAIT 0

IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootR")  //Right Hand
OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p")     //Right Hand
OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_right_c")     //Right Hand
OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_e")     //Right Hand
    CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //BONE_RIGHTHAND
    SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
ELSE
    IF IS_CHAR_PLAYING_ANIM player_actor ("m_wshootL")  //Left Hand
    OR IS_CHAR_PLAYING_ANIM player_actor ("m_wshoot_p_L")   //Left Hand
    OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_left_c")     //Left Hand
        CLEO_CALL getActorBonePos 0 player_actor 35 (x[2] y[2] z[2])    //BONE_LEFTHAND
        SET_OBJECT_COORDINATES_AND_VELOCITY iObj x[2] y[2] z[2]
    ELSE
        ATTACH_OBJECT_TO_CHAR iObj player_actor (0.0 0.5 0.25) (0.0 0.0 0.0)
        DETACH_OBJECT iObj (0.0 0.0 0.0) FALSE
    ENDIF
ENDIF
SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
//PRINT_FORMATTED_NOW "x:%.1f z:%.1f" 1000 xAngle zAngle  //debug
fCurrentDistance = 0.0   //start

WHILE TRUE
    IF DOES_OBJECT_EXIST iObj
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    IF GOSUB isNotPlayingAnim
                        IF LOCATE_CHAR_DISTANCE_TO_OBJECT iChar iObj 1.25
                            GOSUB task_play_action
                            GOTO end_scr
                        ENDIF
                    ENDIF                        
                ENDIF
            ENDIF
        ENDWHILE
        SET_OBJECT_ROTATION iObj xAngle 0.0 zAngle
        CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION SP_WEB iObj 0.0 0.0 0.0 (0.0 90.0 0.0) 1 (fx_web)
        PLAY_AND_KILL_FX_SYSTEM fx_web
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj -0.1 -0.1 -0.1 (x[0] y[0] z[0])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.1 0.1 0.1 (x[2] y[2] z[2])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 3.0 0.0 (x[1] y[1] z[1])
        SLIDE_OBJECT iObj (x[1] y[1] z[1]) (3.0 3.0 3.0) FALSE
        fCurrentDistance +=@ 6.0
        IF fCurrentDistance >= 100.0
            GOSUB task_destroy
            GOTO end_scr
        ENDIF
        IF NOT IS_LINE_OF_SIGHT_CLEAR x[1] y[1] z[1] x[0] y[0] z[0] (1 1 0 0 0)     //buildings|cars|characters|objects|particles
        AND IS_AREA_OCCUPIED x[0] y[0] z[0] x[2] y[2] z[2] (0 1 0 0 0)    //solid|car|actor|object|particle
            GOSUB task_destroy
            GOTO end_scr
        ENDIF
    ELSE
        GOTO end_scr
    ENDIF
    WAIT 0
ENDWHILE

task_play_action:
    GOSUB show_fx
    GOSUB create_object_web
    GOSUB assign_new_task_anim
    GOSUB set_char_zangle
    IF NOT IS_CHAR_DEAD iChar
        DAMAGE_CHAR iChar 5 TRUE
    ENDIF
    IF DOES_OBJECT_EXIST iObj
        TASK_PICK_UP_OBJECT iChar iObj (0.01 0.08 0.0) 1 16 "NULL" "NULL" 1   //1-chest?
    ENDIF
    WHILE TRUE  
        IF DOES_CHAR_EXIST iChar
            IF IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")
                GET_CHAR_ANIM_CURRENT_TIME iChar ("hit_wshoot_p") (fCurrentTime)
                IF fCurrentTime >= 0.992   // frame 119/120 
                    WAIT 5
                    BREAK
                ENDIF
            ELSE
                BREAK
            ENDIF
            IF IS_CHAR_DEAD iChar
                GOTO end_forced
            ENDIF
            IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 35.0
                GOTO end_forced
            ENDIF
            //attach _to
            IF CLEO_CALL isClearInSight 0 iChar (0.0 0.0 -1.0) (1 0 0 1 0)  //solid|car|actor|object|particle
                IF CLEO_CALL has_char_collision_in_offset_or_sides 0 iChar -0.5 //-0.65
                    GOSUB task_attach_to
                    GOTO end_forced
                ENDIF
            ENDIF
        ELSE 
            GOTO end_forced
        ENDIF
        WAIT 0
    ENDWHILE
    IF DOES_OBJECT_EXIST iObj
        SET_OBJECT_SCALE iObj 1.5
    ENDIF
    WHILE TRUE
        IF DOES_CHAR_EXIST iChar
            IF IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_pb")
                GET_CHAR_ANIM_CURRENT_TIME iChar ("hit_wshoot_pb") (fCurrentTime)
                IF fCurrentTime > 0.767  // frame 23/30
                    BREAK
                ENDIF
            ELSE
                BREAK
            ENDIF
        ENDIF        
        WAIT 0
    ENDWHILE
    end_forced:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    GOSUB show_fx
RETURN

isNotPlayingAnim:
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

assign_new_task_anim:
    GOSUB assign_char_reference
    WAIT 0
    IF DOES_CHAR_EXIST iChar
        GOSUB REQUEST_Animations
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
    ENDIF
RETURN

task_attach_to:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    GOSUB assign_char_reference
    WAIT 0
    IF DOES_CHAR_EXIST iChar
        SET_CHAR_COLLISION iChar FALSE
        CLEO_CALL setZangleCharWall 0 iChar (0.2 -10.0 0.0) (-0.2 -10.0 0.0) 90.0

        GOSUB REQUEST_Animations
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        TASK_DIE_NAMED_ANIM iChar "ko_wall" "spider" 125.0 5000
        //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("ko_wall" "spider") 26.0 (0 1 1 1) -1
        WAIT 0
        GOSUB create_object_web_b
        GET_CHAR_HEADING iChar (zAngle)
        timera = 0
        WHILE 5000 > timera
            IF DOES_CHAR_EXIST iChar
                IF IS_CHAR_PLAYING_ANIM iChar ("ko_wall")
                    CLEO_CALL attachObjectToActorOnBone 0 iChar iObj (0.1 0.0 0.0) 1  //root_bone
                    SET_OBJECT_ROTATION iObj 0.0 0.0 zAngle
                ELSE
                    DAMAGE_CHAR iChar 5 TRUE
                    BREAK
                ENDIF
                IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 35.0
                    BREAK
                ENDIF
            ELSE
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        WAIT 0
        IF NOT IS_CHAR_DEAD iChar   //this isn't needed, but anyway
            IF IS_CHAR_PLAYING_ANIM iChar ("ko_wall")
                CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                DAMAGE_CHAR iChar 100 TRUE
            ELSE
                DAMAGE_CHAR iChar 10 TRUE
            ENDIF
        ENDIF
    ENDIF
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    IF DOES_CHAR_EXIST iChar
        SET_CHAR_COLLISION iChar TRUE
        MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        /*IF NOT IS_CHAR_DEAD iChar
            IF IS_CHAR_SCRIPT_CONTROLLED iChar
                MARK_CHAR_AS_NO_LONGER_NEEDED iChar
            ENDIF
        ENDIF*/
    ENDIF
RETURN

set_char_zangle:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (zAngle)
    //SET_CHAR_HEADING player_actor zAngle
    //zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle
    /*
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING player_actor zAngle
    zAngle += 180.0
    SET_CHAR_HEADING iChar zAngle
    */
RETURN

assign_char_reference:
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
RETURN

show_fx:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        CREATE_FX_SYSTEM_ON_CHAR SP_HIT_WEB iChar (0.0 0.25 0.5) 4 (fx_web)  //shootlight
        PLAY_AND_KILL_FX_SYSTEM fx_web
    ENDIF
RETURN

task_destroy:
    IF DOES_OBJECT_EXIST iObj
        CREATE_FX_SYSTEM_ON_OBJECT SP_HIT_WEB iObj x[0] y[0] z[0] 4 (fx_web)    //shootlight
        PLAY_AND_KILL_FX_SYSTEM fx_web
        WAIT 0
        DELETE_OBJECT iObj
    ENDIF
RETURN

create_object_web:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
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
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    REQUEST_MODEL 6021  //wwg
    LOAD_ALL_MODELS_NOW
    WAIT 0
    //CREATE_OBJECT 6021 0.0 0.0 0.0 (iObj)
    CREATE_OBJECT_NO_SAVE 6021 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_COLLISION iObj FALSE
    MARK_MODEL_AS_NO_LONGER_NEEDED 6021
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

end_scr:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    IF DOES_CHAR_EXIST iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        ENDIF
    ENDIF
    REMOVE_ANIMATION "spider"
    WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

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
//CLEO_CALL setZangleCharWall 0 player_actor /*xyz*/(0.2 10.0 0.0) /*xyz*/(-0.2 10.0 0.0) 90.0
setZangleCharWall:
    LVAR_INT scplayer           //in
    LVAR_FLOAT fX fY fZ     //in
    LVAR_FLOAT fX2 fY2 fZ2  //in
    LVAR_FLOAT fFixAngle    //in
    LVAR_FLOAT x[3] y[3] z[3]
    LVAR_FLOAT fAngle
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX fY fZ (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX2 fY2 fZ2 (x[2] y[2] z[2])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[1] y[1] z[1])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[2] y[2] z[2]) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[1] y[1]) (fAngle)
    fAngle += fFixAngle
    //PRINT_FORMATTED_NOW "angle: %.1f" 1000 fAngle
    SET_CHAR_HEADING scplayer fAngle
CLEO_RETURN 0
}
{
//CLEO_CALL getLaserPoint 0 /*from*/0.0 0.0 0.0 /*to*/1.0 0.0 0.0 /*store_to*/ var1 var2 var3
getLaserPoint:
    LVAR_FLOAT fromX fromY fromZ toX toY toZ    //in
    LVAR_FLOAT resultX resultY resultZ
    LVAR_INT scplayer i
    GET_PLAYER_CHAR 0 scplayer
    GET_PED_POINTER scplayer (i)
    IF GET_COLLISION_BETWEEN_POINTS (fromX fromY fromZ) (toX toY toZ) TRUE TRUE TRUE TRUE FALSE TRUE TRUE TRUE i 0x0 (resultX resultY resultZ i)
    ELSE
        resultX = toX
        resultY = toY
        resultZ = toZ
    ENDIF
CLEO_RETURN 0 resultX resultY resultZ
}
{
//CLEO_CALL has_char_collision_in_offset_or_sides 0 iChar -0.65
has_char_collision_in_offset_or_sides:
    LVAR_INT iChar   //in
    LVAR_FLOAT yOffset  //in
    LVAR_FLOAT x[2] y[2] z[2]
    IF DOES_CHAR_EXIST iChar
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 yOffset 0.0 (x[1] y[1] z[1])
        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //back
            RETURN_TRUE
        ELSE
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar -0.5 yOffset 0.0 (x[1] y[1] z[1])
            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //back - left
                RETURN_TRUE
            ELSE
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar -0.5 0.0 0.0 (x[1] y[1] z[1])
                IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //left
                    RETURN_TRUE
                ELSE
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.5 yOffset 0.0 (x[1] y[1] z[1])
                    IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //back-right
                        RETURN_TRUE
                    ELSE
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.5 0.0 0.0 (x[1] y[1] z[1])
                        IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0) //right
                            RETURN_TRUE
                        ELSE
                            RETURN_FALSE
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0
}
