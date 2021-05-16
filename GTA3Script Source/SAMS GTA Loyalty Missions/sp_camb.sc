// by J16D
// Cammera Helper
// in Colaboration with GTA Loyalty
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME sp_camb
CONST_FLOAT pi 3.1415927
LVAR_INT player_actor audio_line_is_active
LVAR_FLOAT x[3] y[3] z[3]
LVAR_FLOAT fAngle xV yV zV fVar
WAIT 100
GET_PLAYER_CHAR 0 player_actor
IF IS_PLAYER_PLAYING 0
    fVar = 5.0
    zV = 5.0
    fAngle = 180.0
    WHILE 540.0 > fAngle
        zV +=@ 0.01
        fVar +=@ 0.1
        CLAMP_FLOAT zV 5.0 20.0 (zV)
        CLAMP_FLOAT fVar 5.0 20.0 (fVar)
        SIN fAngle (xV)
        COS fAngle (yV)
        xV *= fVar
        yV *= fVar
        ATTACH_CAMERA_TO_CHAR_LOOK_AT_CHAR player_actor xV yV zV player_actor 0.0 2
        fAngle +=@ 1.0
        IF IS_BUTTON_PRESSED PAD1 SQUARE   //~k~~PED_JUMPING~
        OR IS_BUTTON_PRESSED PAD1 LEFTSTICKY   //~k~~GO_FORWARD~ / ~k~~GO_BACK~
        OR IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
        OR IS_BUTTON_PRESSED PAD1 TRIANGLE   // ~k~~VEHICLE_ENTER_EXIT~
        OR IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1   // ~k~~PED_LOCK_TARGET~
        OR IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
ENDIF
RESTORE_CAMERA
SET_CAMERA_BEHIND_PLAYER      
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT
}
SCRIPT_END
