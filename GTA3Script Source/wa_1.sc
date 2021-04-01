// by J16D
// Web Shoot (B)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wa_1
LVAR_INT pChar  //passed 
WAIT 0
LVAR_INT player_actor iChar iObj 
LVAR_FLOAT x[2] y[2] z[2] zAngle

GET_PLAYER_CHAR 0 player_actor
iChar = pChar
GOSUB REQUEST_Animations

IF DOES_CHAR_EXIST iChar
AND NOT IS_CHAR_DEAD iChar
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    TASK_DIE_NAMED_ANIM iChar "ko_ground" "spider" 125.0 5000
    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("ko_ground" "spider") 26.0 (0 1 1 1) -1
    WAIT 0
    GOSUB create_object_web
    GET_CHAR_HEADING iChar (zAngle)
    timera = 0
    WHILE 5000 > timera
        IF DOES_CHAR_EXIST iChar
            IF IS_CHAR_PLAYING_ANIM iChar ("ko_ground")
                IF DOES_OBJECT_EXIST iObj
                    CLEO_CALL attachObjectToActorOnBone 0 iChar iObj (0.1 0.0 0.0) 1  //root_bone
                    SET_OBJECT_ROTATION iObj 90.0 0.0 zAngle
                ENDIF
            ELSE
                DAMAGE_CHAR iChar 5 TRUE
                BREAK
            ENDIF
        ELSE
            BREAK
        ENDIF
        IF NOT LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 35.0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    WAIT 0
    IF NOT IS_CHAR_DEAD iChar   //this isn't needed, but anyway
        IF IS_CHAR_PLAYING_ANIM iChar ("ko_ground")
            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
             DAMAGE_CHAR iChar 100 TRUE
        ELSE
            DAMAGE_CHAR iChar 10 TRUE
        ENDIF
    ENDIF
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
ENDIF

IF DOES_OBJECT_EXIST iObj
    DELETE_OBJECT iObj
ENDIF
REMOVE_ANIMATION "spider"
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

create_object_web:
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

}
SCRIPT_END

//-+--- CALL SCM HELPERS
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

