// by J16D
// Concussive Blast (B)
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME wb
WAIT 0
LVAR_INT player_actor i p iChar
LVAR_INT counter
LVAR_FLOAT x[2] y[2] z[2] zAngle

GET_PLAYER_CHAR 0 player_actor
i = 0
WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
    AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
        IF NOT IS_CHAR_IN_ANY_CAR iChar
        AND NOT IS_CHAR_ON_ANY_BIKE iChar
        AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
            
            IF IS_CHAR_ON_SCREEN iChar 
            AND HAS_CHAR_SPOTTED_CHAR_IN_FRONT player_actor iChar
                IF GOSUB isNotPlayingAnim
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 5.0 0.0 (x[0] y[0] z[0])
                    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[0] y[0] z[0] 5.0
                        CLEO_CALL setSmokeFXb 0 player_actor 40.0
                        GOSUB set_char_zangle
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.5 (x[1] y[1] z[1])
                        SET_CHAR_COORDINATES_SIMPLE iChar x[1] y[1] z[1]
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 25.0 15.0 (x[0] y[0] z[0])
                        CLEO_CALL setCharVelocityTo 0 iChar (x[0] y[0] z[0]) 20.0
                        DAMAGE_CHAR iChar 5 TRUE
                    ENDIF
                    WAIT 0
                ENDIF
            ENDIF

        ENDIF
    ENDIF
ENDWHILE
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

set_char_zangle:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[0] y[0] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING iChar zAngle
RETURN

isNotPlayingAnim:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_b")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL setCharVelocityTo 0 iPlayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer    //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
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
//CLEO_CALL setSmokeFXb 0 player_actor fDistance
setSmokeFXb:
    LVAR_INT scplayer       //in
    LVAR_FLOAT fDistance    //in
    LVAR_FLOAT x[2] y[2] z[2] fRandom fTempVar
    LVAR_INT counter 
    IF DOES_CHAR_EXIST scplayer
        counter = 0
        WHILE 9 >= counter
            GET_CHAR_COORDINATES scplayer (x[0] y[0] z[0])
            GENERATE_RANDOM_FLOAT_IN_RANGE -0.1 0.1 (fRandom)
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (fRandom fDistance 1.5) (x[1] y[1] z[1])
            x[1] -= x[0]
            y[1] -= y[0]
            z[1] -= z[0]
            GET_DISTANCE_BETWEEN_COORDS_3D x[1] y[1] z[1] 0.0 0.0 0.0 (fTempVar)
            fTempVar /= 30.0
            x[1] /= fTempVar
            y[1] /= fTempVar
            z[1] /= fTempVar
            ADD_SMOKE_PARTICLE (x[0] y[0] z[0]) (x[1] y[1] z[1]) (1.0 1.0 1.0 1.0) (0.20) (0.01)
            counter += 1
        ENDWHILE
    ENDIF
CLEO_RETURN 0
}
