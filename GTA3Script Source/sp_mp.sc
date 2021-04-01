//by J16D
// Parkour script

SCRIPT_START
{
SCRIPT_NAME sp_mp
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
CONST_INT player 0
LVAR_INT toggleSpiderMod
LVAR_FLOAT fProgress

GOSUB REQUEST_Animations

main_loop:
    GOSUB readVars
    IF toggleSpiderMod = 1 // TRUE
        
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -5.0) (1 0 0 0 0)
            //AIR
        ELSE
            //IMAGE NO. 03577
            //IMAGE NO. 04360
            //IMAGE NO. 09718
            

            //GROUND
            IF  IS_BUTTON_PRESSED 0 CROSS   // ~k~~PED_SPRINT~
            AND IS_CHAR_PLAYING_ANIM player_actor ("sprint_civi")

                IF  CLEO_CALL isNotClearInSight_A 0 player_actor (0.0 0.65 -0.5) (1 0 0 1 0)    // for fences
                    GOSUB REQUEST_Animations
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
                    WAIT 0
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpA" "spider") 100.0 (0 1 1 1) -1
                    WAIT 0
                    CLEO_CALL setCharVelocity 0 player_actor (0.0 7.5 14.0) 1.0
                    WAIT 130
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpB" "spider") 100.0 (0 1 1 1) -1
                    FREEZE_CHAR_POSITION player_actor TRUE
                    WAIT 70
                    FREEZE_CHAR_POSITION player_actor FALSE
                    SET_CHAR_VELOCITY player_actor 0.0 0.0 10.5
                    WAIT 300
                    SET_CHAR_VELOCITY player_actor 0.0 0.0 -4.0
                    WAIT 0
                    CLEO_CALL setCharVelocity 0 player_actor (0.0 7.5 -4.0) 1.0
                    WAIT 100
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpC" "spider") 100.0 (0 1 1 0) -1
                    WAIT 70
                    //PRINT_FORMATTED_NOW "ANIM A" 500  //debug  
                ELSE
                    IF CLEO_CALL isNotClearInSight_B 0 player_actor (0.0 0.65 -0.5) (1 0 0 1 0) // for boxes
                        GOSUB REQUEST_Animations
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
                        WAIT 0
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpA" "spider") 100.0 (0 1 1 1) -1
                        WAIT 0
                        CLEO_CALL setCharVelocity 0 player_actor (0.0 7.5 18.0) 1.0
                        WAIT 130
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpB" "spider") 100.0 (0 1 1 1) -1
                        FREEZE_CHAR_POSITION player_actor TRUE
                        WAIT 70
                        FREEZE_CHAR_POSITION player_actor FALSE
                        SET_CHAR_VELOCITY player_actor 0.0 0.0 16.0
                        WAIT 0
                        CLEO_CALL setCharVelocity 0 player_actor (0.0 7.5 16.0) 1.0
                        WAIT 300
                        SET_CHAR_VELOCITY player_actor 0.0 0.0 -8.0
                        WAIT 0
                        CLEO_CALL setCharVelocity 0 player_actor (0.0 7.5 -8.0) 1.0
                        WAIT 100
                        TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpC" "spider") 100.0 (0 1 1 0) -1
                        WAIT 70
                        //PRINT_FORMATTED_NOW "ANIM B" 500  //debug
                    ELSE
                        IF CLEO_CALL isNotClearInSight_C 0 player_actor (0.0 1.5 -0.5) (1 0 0 1 0) // for buildings
                            ///new video from SPRINT-WALL RUN IMAGE NO. 01964
                            IF NOT IS_CHAR_PLAYING_ANIM player_actor "jumpToWall"
                            AND NOT IS_CHAR_PLAYING_ANIM player_actor "run_wall"
                                GOSUB REQUEST_Animations
                                CLEO_CALL setCharVelocity 0 player_actor (0.0 0.1 3.5) 3.0
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jumpToWall" "spider") 12.0 (0 1 1 0) -1
                                WAIT 0
                                CLEO_CALL setCharVelocity 0 player_actor (0.0 0.35 5.0) 3.0
                                WHILE IS_CHAR_PLAYING_ANIM player_actor "jumpToWall"
                                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("jumpToWall") (fProgress)
                                    IF fProgress >= 0.9335
                                        BREAK
                                    ENDIF
                                    WAIT 0
                                ENDWHILE     

                                IF  CLEO_CALL isNotClearInSight_D 0 player_actor (0.0 0.65 -0.5) (1 0 0 1 0)    // to avoid buildings with obstacles in walls
                                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "run_wall" "spider" 9.0 (1 1 1 0) -2   
                                    WAIT 0 
                                    CLEO_CALL setCharVelocity 0 player_actor (0.0 0.0 5.0) 3.0
                                ENDIF
                            ENDIF
                            //PRINT_FORMATTED_NOW "ANIM C" 500  //debug
                            

                                //GOSUB REQUEST_Animations
                                /*
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
                                WAIT 0
                                FREEZE_CHAR_POSITION player_actor TRUE
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpD" "spider") 100.0 (0 0 0 1) -1
                                WAIT 170
                                FREEZE_CHAR_POSITION player_actor FALSE
                                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pkJumpE" "spider") 100.0 (0 0 0 0) -1
                                */
                                //SET_CHAR_VELOCITY player_actor 0.0 0.0 14.0
                            //    CLEO_CALL setCharVelocity 0 player_actor (0.0 0.0 10.0) 1.0
                                //WAIT 130
                                //FREEZE_CHAR_POSITION player_actor TRUE
                                //WAIT 20
                                //FREEZE_CHAR_POSITION player_actor FALSE
                                //SET_CHAR_VELOCITY player_actor 0.0 0.0 18.0
                                //CLEO_CALL setCharVelocity 0 player_actor (0.0 -2.5 18.0) 1.0
                                //WAIT 250
                                //SET_CHAR_VELOCITY player_actor 0.0 0.0 -5.0
                                //WAIT 0
                                //CLEO_CALL setCharVelocity 0 player_actor (0.0 10.5 -5.0) 1.0
                                //WAIT 70
                                //PRINT_FORMATTED_NOW "ANIM C-D-E" 500  //debug  
                            //ENDIF
                            

                        ENDIF

                    ENDIF
                ENDIF
            ENDIF
        
        ENDIF
    ELSE
        REMOVE_ANIMATION "spider"
        WAIT 1000
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR 110 toggleSpiderMod
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

{
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT playerTemp
    LVAR_FLOAT xVel yVel zVel amplitude
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS playerTemp 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS playerTemp xVel yVel zVel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY playerTemp x[1] y[1] z[1]
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
//CLEO_CALL isNotClearInSight_A 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isNotClearInSight_A:    // for fences
    LVAR_INT tempPlayer
    LVAR_FLOAT fx fy fz
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT x[6] y[6] z[6]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer fx fy fz (x[0] y[0] z[0])   // 0.0 0.65 -0.5
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 3.0 0.0 (x[2] y[2] z[2])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 2.0 (x[3] y[3] z[3])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (x[4] y[4] z[4])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 3.0 (x[5] y[5] z[5])
    IF NOT IS_LINE_OF_SIGHT_CLEAR x[1] y[1] z[1] x[0] y[0] z[0] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[3] y[3] z[3] x[2] y[2] z[2] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[5] y[5] z[5] x[4] y[4] z[4] (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL isNotClearInSight_B 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isNotClearInSight_B:    // for boxes
    LVAR_INT tempPlayer
    LVAR_FLOAT fx fy fz
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT x[6] y[6] z[6]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer fx fy fz (x[0] y[0] z[0])   // 0.0 0.65 -0.5
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 1.0 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 3.0 1.0 (x[2] y[2] z[2])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 2.0 (x[3] y[3] z[3])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 3.0 (x[4] y[4] z[4])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (x[5] y[5] z[5])
    IF NOT IS_LINE_OF_SIGHT_CLEAR x[1] y[1] z[1] x[0] y[0] z[0] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[3] y[3] z[3] x[2] y[2] z[2] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[5] y[5] z[5] x[4] y[4] z[4] (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL isNotClearInSight_C 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isNotClearInSight_C:    // for buildings
    LVAR_INT tempPlayer
    LVAR_FLOAT fx fy fz
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT x[6] y[6] z[6]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer fx fy fz (x[0] y[0] z[0])   // 0.0 0.65 -0.5
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.05 1.5 (x[1] y[1] z[1])    //0.0 0.0 1.0 
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 -0.5 8.8 (x[2] y[2] z[2])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 1.51 (x[3] y[3] z[3])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (x[4] y[4] z[4])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 4.0 (x[5] y[5] z[5])
    IF NOT IS_LINE_OF_SIGHT_CLEAR x[1] y[1] z[1] x[0] y[0] z[0] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[3] y[3] z[3] x[2] y[2] z[2] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[5] y[5] z[5] x[4] y[4] z[4] (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL isNotClearInSight_D 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isNotClearInSight_D:    // to avoid buildings with obstacles in walls
    LVAR_INT tempPlayer
    LVAR_FLOAT fx fy fz
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT x[4] y[4] z[4]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer fx fy fz (x[0] y[0] z[0])   // 0.0 0.65 -0.5
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 1.0 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.01 5.8 (x[2] y[2] z[2])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 -0.05 0.0 (x[3] y[3] z[3])
    IF NOT IS_LINE_OF_SIGHT_CLEAR x[1] y[1] z[1] x[0] y[0] z[0] (isSolid isCar isActor isObject isParticle)
    AND IS_LINE_OF_SIGHT_CLEAR x[3] y[3] z[3] x[2] y[2] z[2] (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}