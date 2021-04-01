//by J16D
//-+-- CLEO_CALL HELPERS
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+------------------------ TWEAKS ------------------------
{
//CLEO_CALL disableZvelocityLimit 0 TRUE
disableZvelocityLimit:
LVAR_INT _activated
IF _activated = TRUE
    WRITE_MEMORY 0x5E91CE 1 0x90 TRUE
    WRITE_MEMORY 0x5E91CF 1 0x90 TRUE
    WRITE_MEMORY 0x5E91D0 1 0x90 TRUE
    WRITE_MEMORY 0x5E91D1 1 0x90 TRUE
    WRITE_MEMORY 0x5E91D2 1 0x90 TRUE
    WRITE_MEMORY 0x5E91D3 1 0x90 TRUE
    WRITE_MEMORY 0x5E91D4 1 0x90 TRUE
ELSE
    IF _activated = FALSE
        WRITE_MEMORY 0x5E91CE 1 0xC7 TRUE
        WRITE_MEMORY 0x5E91CF 1 0x46 TRUE
        WRITE_MEMORY 0x5E91D0 1 0x4C TRUE
        WRITE_MEMORY 0x5E91D1 1 0 TRUE
        WRITE_MEMORY 0x5E91D2 1 0 TRUE
        WRITE_MEMORY 0x5E91D3 1 0x80 TRUE
        WRITE_MEMORY 0x5E91D4 1 0x3E TRUE
    ENDIF
ENDIF
CLEO_RETURN 0
}
//---------------------------------------------------------

//-+------------------------ CHECK ------------------------
{
//CLEO_CALL isActorInWater 0 player_actor
isActorInWater:
    LVAR_INT tempPlayer
    LVAR_FLOAT x y z height
    IF IS_PLAYER_PLAYING tempPlayer
        GET_CHAR_COORDINATES tempPlayer (x y z)
        GET_WATER_HEIGHT_AT_COORDS x y TRUE (height)
        IF height > z
            RETURN_TRUE
            CLEO_RETURN 0
        ENDIF
    ENDIF
    RETURN_FALSE
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
//CLEO_CALL is_Collision_wall_found 0 player_actor /*side*/side (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)  // 0:center || 1:left || 2:right
is_Collision_wall_found:
    LVAR_INT hchar  //in
    LVAR_INT iSide  //in
    LVAR_INT isSolid isCar isActor isObject isParticle  //in
    LVAR_FLOAT xCoord[2] yCoord[2] zCoord[2]
    LVAR_FLOAT xOffset
    IF iSide = 1    // 0:center || 1:left || 2:right
        xOffset = -1.0
    ELSE
        IF iSide = 2
            xOffset = 1.0
        ENDIF
    ENDIF
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar xOffset 0.5 0.0 (xCoord[0] yCoord[0] zCoord[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar 0.0 0.0 0.0 (xCoord[1] yCoord[1] zCoord[1])
    IF NOT IS_LINE_OF_SIGHT_CLEAR xCoord[1] yCoord[1] zCoord[1] xCoord[0] yCoord[0] zCoord[0] (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar xOffset 0.0 0.0 (xCoord[0] yCoord[0] zCoord[0])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar 0.0 0.0 0.0 (xCoord[1] yCoord[1] zCoord[1])
        IF NOT IS_LINE_OF_SIGHT_CLEAR xCoord[1] yCoord[1] zCoord[1] xCoord[0] yCoord[0] zCoord[0] (isSolid isCar isActor isObject isParticle)
            RETURN_TRUE
        ELSE
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar xOffset -0.5 0.0 (xCoord[0] yCoord[0] zCoord[0])
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS hchar 0.0 0.0 0.0 (xCoord[1] yCoord[1] zCoord[1])
            IF NOT IS_LINE_OF_SIGHT_CLEAR xCoord[1] yCoord[1] zCoord[1] xCoord[0] yCoord[0] zCoord[0] (isSolid isCar isActor isObject isParticle)
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0
}
{
//IF CLEO_CALL CharNotDoingAnim 0 (scplayer)
//by Junior_Djjr
LVAR_INT hChar // in
LVAR_INT p i
CharNotDoingAnim:
    GET_PED_POINTER hChar p
    p += 0x47C //m_pIntelligence
    READ_MEMORY p 4 FALSE (p) 
    i = p + 0x4 //m_TaskMgr.m_primaryTasks  dd 5 dup
    i += 12 //[3] (primary anim)
    READ_MEMORY i 4 FALSE (i)
    IF i > 0
        RETURN_FALSE
        CLEO_RETURN 0
    ELSE
        i = p + 0x18 //m_TaskMgr.m_secondaryTasks dd 6 dup
        i += 16 //[4] (secondary anim)
        READ_MEMORY i 4 FALSE (i)
        IF i > 0
            RETURN_FALSE
            CLEO_RETURN 0
        ENDIF
    ENDIF
    RETURN_TRUE
CLEO_RETURN 0
}
{
//CLEO_CALL isSideOfBuildingInOffset 0 player_actor /*start*/(0.0 2.0 1.0) /*end*/ (0.0 -1.0 -2.0)
isSideOfBuildingInOffset:
    LVAR_INT scplayer
    LVAR_FLOAT fX fY fZ
    LVAR_FLOAT fX2 fY2 fZ2
    LVAR_FLOAT x[4] y[4] z[4]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX fY fZ (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX2 fY2 fZ2 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[2] y[2] z[2])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 3.0 0.0 (x[3] y[3] z[3])
    IF IS_LINE_OF_SIGHT_CLEAR (x[3] y[3] z[3]) (x[2] y[2] z[2]) (1 0 0 0 0)
        IF IS_LINE_OF_SIGHT_CLEAR (x[0] y[0] z[0]) (x[1] y[1] z[1]) (1 0 0 0 0)
            RETURN_FALSE
        ELSE
            RETURN_TRUE
        ENDIF    
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
//---------------------------------------------------------


//-+------------------------ SET ------------------------
{
//CLEO_CALL setCameraVerticalWallRun 0 /*xyz*/0.0 0.0 0.0 /*yOffset*/ 1.5
setCameraVerticalWallRun:
    LVAR_FLOAT x y z fCamOffset   //in
    LVAR_FLOAT xCam yCam zCam fAngle yCamAngle
    
    GET_ACTIVE_CAMERA_COORDINATES (xCam yCam zCam)
    x -= xCam
    y -= yCam
    GET_HEADING_FROM_VECTOR_2D x y (fAngle)
    fAngle -= 90.0
    fAngle /= 57.2957795 //(180/Pi)
    READ_MEMORY 0xB6F248 DWORD FALSE (yCamAngle)
    yCamAngle += fCamOffset
    IF yCamAngle > 1.5
        yCamAngle = 1.5
    ENDIF
    SET_CAMERA_POSITION_UNFIXED yCamAngle fAngle
CLEO_RETURN 0
}

{
//CLEO_CALL setCoordsInter 0 player_actor (0.0 5.0 0.0) 0.8 
setCoordsInter:
    LVAR_INT scplayer
    LVAR_FLOAT fX fY fZ fDistance
    LVAR_FLOAT x[3] y[3] z[3]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer fX fY fZ (x[1] y[1] z[1])
    CLEO_CALL getLaserPoint 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[2] y[2] z[2])
    IF NOT x[1] = x[2]
    AND NOT y[1] = y[2]
    AND NOT z[1] = z[2]
         y[2] -= fDistance
         SET_CHAR_COORDINATES_SIMPLE scplayer x[2] y[2] z[2]
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
//CLEO_CALL setCameraLerpFov 0 fVelocity
setCameraLerpFov:
    LVAR_FLOAT fVel //In
    LVAR_FLOAT fFov
    fFov = fVel
    fFov *= 1.7
    IF fFov > 85.0
        fFov = 85.0
    ENDIF
    CAMERA_SET_LERP_FOV fFov fFov 1 FALSE
CLEO_RETURN 0
}
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
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer
    LVAR_FLOAT xVel yVel zVel amplitude
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer xVel yVel zVel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
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
{
//CLEO_CALL set_max_min_cycle_float 0 fValue fMax fMin (fValue) 
set_max_min_cycle_float:
    LVAR_FLOAT fValue   //in
    LVAR_FLOAT fMax fMin    //in
    IF fValue > fMax
        fValue = fMin
    ELSE
        IF fMin > fValue
            fValue = fMax
        ENDIF
    ENDIF
CLEO_RETURN 0 fValue
}
//---------------------------------------------------------

//-+------------------------ GET ------------------------
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
//CLEO_CALL getXangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*xAngle*/fSyncAngle)
getXangleBetweenPoints:
    LVAR_FLOAT xA yA zA
    LVAR_FLOAT xB yB zB
    LVAR_FLOAT pointY pointZ
    LVAR_FLOAT xAngle
    GET_DISTANCE_BETWEEN_COORDS_2D xA yA xB yB (pointY)
    pointZ = (zA - zB)
    GET_HEADING_FROM_VECTOR_2D pointY pointZ (xAngle)
    //CLEO_CALL ATAN 0 /*2D_Coord*/ pointY pointZ /*angle*/ (xAngle)
    xAngle -= 270.0
CLEO_RETURN 0 xAngle
}
{
//CLEO_CALL ATAN 0 /*2D_Coord*/ 0.0 0.0 /*angle*/ (zAngle)
ATAN:
    LVAR_FLOAT pointX pointY
    //CONST_FLOAT math_pi 3.1415926535897932384
    LVAR_FLOAT x y
    LVAR_FLOAT xyAngle
    LVAR_INT counter
    x = pointX
    y = pointY
    GET_DISTANCE_BETWEEN_COORDS_2D x y 0.0 0.0 (xyAngle)
    x /= xyAngle
    y /= xyAngle
    GET_DISTANCE_BETWEEN_COORDS_2D x y 0.0 1.0 (xyAngle)
    counter = 0
    WHILE 15 >= counter
        xyAngle /= 2.0
        xyAngle *= xyAngle
        y = xyAngle
        xyAngle *= -1.0
        xyAngle += 1.0
        SQRT xyAngle (xyAngle)
        xyAngle *= -1.0
        xyAngle += 1.0
        xyAngle *= xyAngle
        xyAngle += y
        SQRT xyAngle (xyAngle)
        counter ++
        WAIT 0
    ENDWHILE
    xyAngle *= 65536.0
    xyAngle /= math_pi
    xyAngle *= 180.0
    IF x > 0.0
        xyAngle *= -1.0
        xyAngle += 360.0
    ENDIF
CLEO_RETURN 0 xyAngle
}
{
//CLEO_CALL getXYZAimCoords 0 scplayer fRange (x y z)
getXYZAimCoords:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fRange   //in
    LVAR_FLOAT x[3] y[3] z[3]
    GET_CHAR_COORDINATES scplayer (x[0] y[0] z[0])
    CLEO_CALL getAimPoint 0 fRange (x[0] y[0] z[0]) (x[1] y[1] z[1]) (x[2] y[2] z[2])
    CLEO_CALL getLaserPoint 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (x[0] y[0] z[0])
CLEO_RETURN 0 x[0] y[0] z[0]
}
{
//CLEO_CALL getAimPoint 0 /*range*/ 20.0 /*from*/(0.0 0.0 0.0) /*Camera To*/(var1 var2 var3) /*Point to*/ (var4 var5 var6)
getAimPoint:
    LVAR_FLOAT range fromX fromY fromZ  //in
    LVAR_FLOAT camX camY camZ pointX pointY pointZ
    LVAR_INT var1 var2
    GET_VAR_POINTER (camX) (var1)
    GET_VAR_POINTER (pointX) (var2)
    CALL_METHOD 0x514970 /*struct*/0xB6F028 /*params*/6 /*pop*/0 /*pPoint*/var2 /*pCam*/var1 /*fZ*/fromZ /*fY*/fromY /*fX*/fromX /*fRange*/ range
CLEO_RETURN 0 camX camY camZ pointX pointY pointZ
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
//CLEO_CALL getBoneOffset 0 player_actor /*bone*/ 6 /*offset*/ 0.0 0.0 0.0 /*store*/ (x y z)
getBoneOffset:
    LVAR_INT hChar
    LVAR_INT iBone
    LVAR_FLOAT xOffset yOffset zOffset
    LVAR_FLOAT v1 v2 v3
    LVAR_INT v4
    LVAR_INT pChar iBoneID
    iBoneID = iBone
    IF DOES_CHAR_EXIST hChar
        v1 = xOffset
        v2 = yOffset
        v3 = zOffset
        GET_PED_POINTER hChar (pChar)
        GET_VAR_POINTER v1 (v4)
        CALL_METHOD 0x5E01C0 /*struct*/pChar /*params*/3 /*pop*/ 0 /*bIncludeAnim*/1 /*BoneID*/iBoneID /*vOffset*/v4    // CPed__getBonePositionWithOffset
    ELSE
        PRINT_FORMATTED_NOW "ERR: wrong use of 'getBoneOffset'" 1000
        WAIT 1000
        v1 = 0.0
        v2 = 0.0
        v3 = 0.0
    ENDIF
CLEO_RETURN 0 v1 v2 v3
 /*
    BONE_PELVIS1             = 1
    BONE_PELVIS              = 2
    BONE_SPINE1              = 3
    BONE_UPPERTORSO          = 4
    BONE_NECK                = 5
    BONE_HEAD2               = 6
    BONE_HEAD1               = 7
    BONE_HEAD                = 8
    BONE_RIGHTUPPERTORSO     = 21
    BONE_RIGHTSHOULDER       = 22
    BONE_RIGHTELBOW          = 23
    BONE_RIGHTWRIST          = 24
    BONE_RIGHTHAND           = 25
    BONE_RIGHTTHUMB          = 26
    BONE_LEFTUPPERTORSO      = 31
    BONE_LEFTSHOULDER        = 32
    BONE_LEFTELBOW           = 33
    BONE_LEFTWRIST           = 34
    BONE_LEFTHAND            = 35
    BONE_LEFTTHUMB           = 36
    BONE_LEFTHIP             = 41
    BONE_LEFTKNEE            = 42
    BONE_LEFTANKLE           = 43
    BONE_LEFTFOOT            = 44
    BONE_RIGHTHIP            = 51
    BONE_RIGHTKNEE           = 52
    BONE_RIGHTANKLE          = 53
    BONE_RIGHTFOOT           = 54
*/
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
//---------------------------------------------------------


//-+------------------------ SET/GET ------------------------
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
//---------------------------------------------------------


//-+--------------------- OTHER SP ------------------------
{
//CLEO_CALL draw_on_screen_web 0 startX startY startZ iBone
draw_on_screen_web:
    LVAR_FLOAT x y z // in
    LVAR_INT iBone // in
    LVAR_FLOAT x2 y2 z2 v1 v2 v3 v4 v5
    LVAR_INT player_actor
    GET_PLAYER_CHAR 0 player_actor
    CONVERT_3D_TO_SCREEN_2D (x y z) TRUE TRUE (v1 v2) (v5 v5)
    CLEO_CALL getActorBonePos 0 player_actor iBone (x2 y2 z2)    //25:Right hand ||26:Left hand
    CONVERT_3D_TO_SCREEN_2D (x2 y2 z2) TRUE TRUE (v3 v4) (v5 v5)
    CLEO_CALL drawline 0 (v1 v2) (v3 v4) 0.5 (255 255 255 255)
CLEO_RETURN 0
}
{
//CLEO_CALL drawline 0 x y x1 y1 fThickness r g b a
drawline:
    LVAR_FLOAT x y x1 y1 fThickness //in
    LVAR_INT r g b a    //in
    LVAR_FLOAT fDistance zAngle
    GET_DISTANCE_BETWEEN_COORDS_2D x y x1 y1 (fDistance)
    x1 -= x
    y1 -= y
    GET_HEADING_FROM_VECTOR_2D x1 y1 (zAngle)
    zAngle += 90.0
    x1 /= 2.0
    y1 /= 2.0
    x += x1
    y += y1
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE_WITH_ROTATION 666 x y fDistance fThickness zAngle r g b a
CLEO_RETURN 0
}
{
//CLEO_CALL simplePendulumPeriod 0 /*fLongitude*/fLongitude /*fgravity*/g /*fperiod*/(fPeriod) 
simplePendulumPeriod:   
    LVAR_FLOAT fLongitude        //longitude Pendulum (m)
    LVAR_FLOAT g        //gravity (m/s2)   
    LVAR_FLOAT fPeriod   //period (s)
    CONST_FLOAT math_PI 3.14159265358979
    LVAR_FLOAT math_2PI var1 squareRoot
    math_2PI = 2.0 * math_PI
    var1 = (fLongitude / g)
    SQRT var1 (squareRoot)
    fPeriod = math_2PI * squareRoot
CLEO_RETURN 0 fPeriod        //t=2pi*sqrt(l/g)
}
{
//CLEO_CALL velocityPendulum 0 /*longitude*/4.0 /*Sync_angle*/65.0 /*ret_YZ_vel*/ (yVel zVel)
velocityPendulum:
    LVAR_FLOAT fLongitude
    LVAR_FLOAT fSyncAngle
    CONST_FLOAT fStartAngle 90.0    //where v=0.0
    CONST_FLOAT fGravity 9.8
    CONST_FLOAT fMass 70.0
    LVAR_FLOAT fHigh fVelocity fyVelocity fzVelocity
    LVAR_FLOAT fResultCosSyncAngle fResultCosStartAngle fResultA fResultB
    LVAR_FLOAT fCosAngle fSinAngle
    //h=L*(cosb-cosa)
    COS fSyncAngle (fResultCosSyncAngle)
    COS fStartAngle (fResultCosStartAngle)
    fResultA = (fResultCosSyncAngle - fResultCosStartAngle)
    fHigh = (fLongitude * fResultA)
    // v=sqrt(2*g*h)
    fResultB = (fgravity * fHigh)
    fResultB = 2.0 * fResultB
    SQRT fResultB (fVelocity)
    //vy=v*cosb
    COS fSyncAngle (fCosAngle)
    fyVelocity = (fVelocity * fCosAngle)
    ABS_LVAR_FLOAT fyVelocity
    //vz=-v*sinb
    SIN fSyncAngle (fSinAngle)
    fzVelocity = (fVelocity * fSinAngle)
    fzVelocity *= -1.0
    //ABS_LVAR_FLOAT fzVelocity
CLEO_RETURN 0 fyVelocity fzVelocity
}
{
//CLEO_CALL getCollisionAroundPlayer 0 player_actor (fLongitude iSideSwing)
getCollisionAroundPlayer:
    LVAR_INT scplayer
    LVAR_INT iSideSwing     // 0:center || 1:left || 2:right
    LVAR_FLOAT x[5] y[5] z[5]
    LVAR_FLOAT fDistance[3]
    LVAR_FLOAT tempVar[2]
    LVAR_INT i
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])    // Reference 3D point
    //Right front
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 15.0 5.0 5.0 (x[1] y[1] z[1])   // Right
    GET_PED_POINTER scplayer i
    IF GET_COLLISION_BETWEEN_POINTS (x[1] y[1] z[1]) (x[0] y[0] z[0]) TRUE FALSE FALSE FALSE FALSE TRUE TRUE TRUE i 0x0 (x[3] y[3] z[3] i)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[0]) //Right
    ENDIF
    //Left front
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer -15.0 5.0 5.0 (x[2] y[2] z[2])  // Left
    GET_PED_POINTER scplayer i
    IF GET_COLLISION_BETWEEN_POINTS (x[2] y[2] z[2]) (x[0] y[0] z[0]) TRUE FALSE FALSE FALSE FALSE TRUE TRUE TRUE i 0x0 (x[3] y[3] z[3] i)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[1]) // Left
    ENDIF
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 5.0 5.0 5.0 (x[3] y[3] z[3])
    GET_DISTANCE_BETWEEN_COORDS_3D x[0] y[0] z[0] x[3] y[3] z[3] (fDistance[2]) //center

    IF fDistance[0] < fDistance[1]
        tempVar[0] = fDistance[0] // lower right
        iSideSwing = 1  // 0:center || 1:left || 2:right
    ELSE
        IF fDistance[0] > fDistance[1]
            tempVar[0] = fDistance[1] // lower left
            iSideSwing = 2  // 0:center || 1:left || 2:right
        ELSE
            tempVar[0] = fDistance[2] // lower left
            iSideSwing = 0  // 0:center || 1:left || 2:right
        ENDIF
    ENDIF
    //LIMIT distance
    CLAMP_FLOAT tempVar[0] 5.0 10.0 (tempVar[0])
CLEO_RETURN 0 tempVar[0] iSideSwing
}

{
//CLEO_CALL getSyncAngleFromBuildingInSightOfChar 0 player_actor (/*fDistance*/fDistance /*fAngle*/ fSyncAngle /*iSideSwing*/ left)
getSyncAngleFromBuildingInSightOfChar:
    LVAR_INT scplayer
    LVAR_INT sideSwing  // 0:center || 1:left || 2:right
    LVAR_FLOAT fAngle
    LVAR_FLOAT x[3] y[3] z[3]
    LVAR_FLOAT fDistance fRandomVal fHeigh
    LVAR_INT i
    fAngle = 0.0
    sideSwing = 0  // 0:center || 1:left || 2:right
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[1] y[1] z[1])

    //Right front
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 15.0 5.0 8.0 (x[0] y[0] z[0])
    GET_PED_POINTER scplayer i
    IF GET_COLLISION_BETWEEN_POINTS (x[1] y[1] z[1]) (x[0] y[0] z[0]) TRUE FALSE FALSE FALSE FALSE TRUE TRUE TRUE i 0x0 (x[2] y[2] z[2] i)
        sideSwing = 2  // 0:center || 1:left || 2:right
        CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (fAngle)
    ELSE
        //Left front
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer -15.0 5.0 8.0 (x[0] y[0] z[0])
        IF GET_COLLISION_BETWEEN_POINTS (x[1] y[1] z[1]) (x[0] y[0] z[0]) TRUE FALSE FALSE FALSE FALSE TRUE TRUE TRUE i 0x0 (x[2] y[2] z[2] i)
            sideSwing = 1  // 0:center || 1:left || 2:right
            CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (fAngle)
        ELSE
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 10.0 10.0 (x[2] y[2] z[2])
            sideSwing = 0  // 0:center || 1:left || 2:right
            CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[2] y[2] z[2]) (fAngle)
            ABS_LVAR_FLOAT fAngle
            //GENERATE_RANDOM_FLOAT_IN_RANGE 18.0 24.0 (fRandomVal)
            GET_CHAR_HEIGHT_ABOVE_GROUND scplayer (fHeigh)
            IF 10.0 > fHeigh
                fRandomVal = 13.0
                fAngle = fRandomVal
                //PRINT_FORMATTED_NOW "working func" 1000
            ELSE
                GENERATE_RANDOM_FLOAT_IN_RANGE 18.0 30.0 (fRandomVal)
                IF fAngle > fRandomVal
                    fAngle = fRandomVal
                ENDIF
            ENDIF
            //PRINT_FORMATTED_NOW "FRONT ANGLE:(%f)" 1000 fAngle
        ENDIF
    ENDIF
    ABS_LVAR_FLOAT fAngle
    GET_DISTANCE_BETWEEN_COORDS_3D (x[1] y[1] z[1]) (x[2] y[2] z[2]) (fDistance)
    CLAMP_FLOAT fDistance 8.0 15.0 (fDistance)
CLEO_RETURN 0 fDistance fAngle sideSwing
}
//---------------------------------------------------------



/* MEM STUFF

CPed + 0x470 = [dword] Properties:
3 = invisibility (not including the arms)
12 = immune to shots in the head
20 = sink in water
CPed + 0x294 Class CPedSound (size - 0x100 bytes)

(CCamera + 0x878 cameraRotation RwV3D).

0xB6F028 - Starting the camera unit (CCamera)
CCamera + 0xB4 = [dword] Current View Camera:
0 = c bumper
1 = short distance from the machine
2 = the average distance from the machine
3 = long distance by car
4 = anything = the same as the last one?
5 = cinematic view
6 to INF = the same as the 4?
CCamera+0xB8 = [float] Car View Distance (arm length)
CCamera+C0 = [float] True View Distance (true arm length)

DWORD * pActor = (DWORD *) 0xB6F5F0; // pointer to your character.
DWORD * pCamera = (DWORD *) 0xB6F99C; // pointer to the camera.
Float * camXpos = (float *) 0xB6F258; // camera angle left and right.
Float * camYpos = (float *) 0xB6F248; // camera angle up and down.

Char* pVehicle = (char*) ((*pActor) + 0x46C);
If (*pVehicle! = 1) // if the player is not in the car.

Camera coordinates:
Float* CxPos1 = (float*) (0xB6F9CC);
Float* CyPos1 = (float*) (0xB6F9D0);
Float* CzPos1 = (float*) (0xB6F9D4);

Camera coordinates
float point_x = *(float*)(0xB6F9CC); // camera X position  
float point_y = *(float*)(0xB6F9D0); // camera Y position 
float point_z = *(float*)(0xB6F9D4); // camera Z position

CPed +0x14 = Pointer to XYZ position structure (and rotation)
(CPed+0x14) +0x0 to +0x2C = [dword] Is the rotation matrix
(CPed+0x14) +0x30 = [dword] XPos
(CPed+0x14) +0x34 = [dword] YPos
(CPed+0x14) +0x38 = [dword] ZPos

0A96: 30@ = actor $PLAYER_ACTOR struct 
0A96: 31@ = actor 9@ struct 
30@ += 0x14 
31@ += 0x14 
0A8D: 30@ = read_memory 30@ size 4 virtual_protect 0 
0A8D: 31@ = read_memory 31@ size 4 virtual_protect 0 
for 0@ = 0 to 11 
    0A8D: 20@ = read_memory 31@ size 4 virtual_protect 0 
    0A8C: write_memory 30@ size 4 value 20@ virtual_protect 0 
    30@ += 0x4
    31@ += 0x4 
end
Actor:9@㝮回転Matrixをプレイヤー㝫も革用㝗㝦やり㝾㝙。
座標〝角度計算㝌面倒㝪㝨㝝㝫使㝄㝾㝙。
ムーンウォーク㝗㝟り〝ビル㝠㝣㝦垂直㝫駆㝑上㝌れる㝯㝚㝧㝙。

aim-animations
+0x8	pointer to aim_vector
aim_vector	(aim_direction?)
+0x30	x
+0x34	y
+0x38	z

CWanted + 0x2C - [dword] Current wanted level
CPed + 0x2F - [byte] Location status (0 = outside, 3 = inside a building)
DWORD(CPed+0x14) + 0x30 = XPos
DWORD(CPed+0x14) + 0x34 = YPos
DWORD(CPed+0x14) + 0x38 = ZPos

CPed
+1247 - current animation play-state (61 starting/stopping, 62 looping, 0 nothing)
+1135 - crouch state (132 crouching, 128 not crouching)
+1133 - jump state (34 in air, 36 landing, 32 landed/idle)
+348 - some anim states (205 run, 154 sprint, 102 stopped, 0 landing from jump, 61 punching)

CPed +0x530 = [dword] State:
0 = leaving a car, falling down from a bike or something like this
1 = normal case
50 = driving
55 = wasted
63 = busted
Cped +0x46C = [byte] Player check:
0 = in air/water
1 = in car
2 = entering interior
3 = on foot
--
0 = in air / water 
In Car = 1 
2 = Entering Interior 
3 = On Foot 
4 = In Air / Water 
5 = In Car 
6 = Entering Interior 
7 = On Foot 
8 = In Air / Water 
------
CPed +0x46D = Jump state:
32 = landed/idle
34 = in air
36 = landing
CPed +0x46F = Crouch state:
128 = stand
132 = crouched
CPed +0x47C = Pointer to anim struct
CPed +0x4DF = Current anim play-state:
0 = nothing
61 = starting/stopping
62 = looping
CPed +0x534 = Runningstate:
0 = while driving
1 = standing still
4 = start to run
6 = running
7 = running fast (sprinting) by pressing sprint key

CPed +0x598 = [byte] Player lock (set to 1 to lock player controls, can't move)
CPed +0x540 = [float] Health
CPed +0x544 = [float] Max health
CPed +0x548 = [float] Armor
CPed + 0x5F8 = [dword] Shotgun state (1 = firing? 2 = reloading)

0A8D: 0@ = read_memory 0x00B6F1A8 size 2 vp 0
if or
0@ == 7  // Sniper Rifle
0@ == 8  // RPG
0@ == 51 // Missile Launcher
0@ == 53 // Others
then
    // Aimming
end

+0x4D4 [dword]: Anim Group ID (walk style)
+0x534 [dword]: Movement Status (1 = on foot, 4 = starting to move, 6 = walking/running, 7 = sprinting)
+0x46C: Ped State Binary Flags [dword]:
+1 [bit]: On ground
+0x72D [byte]: Fight style
+0x72E [byte]: Allowed attack moves
+0x730 [dword]: Pointer to fire entity of ped
*/
