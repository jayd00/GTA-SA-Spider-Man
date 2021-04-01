//by J16D
// Screwball EPM Challenge
/*
WRITE_MEMORY 0xA476AC 4 (1) FALSE // $ONMISSION = 1
READ_MEMORY 0xA476AC 4 FALSE (onmission)
*/

SCRIPT_START
{
NOP
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
CONST_INT player 0

LVAR_INT flag_player_on_mission
LVAR_INT onmission

main_loop:
    IF IS_PLAYER_PLAYING player
        READ_MEMORY 0xA476AC 4 FALSE (onmission)
        IF onmission = 0
            IF IS_KEY_PRESSED VK_KEY_P
                WHILE IS_KEY_PRESSED VK_KEY_P
                    WAIT 0
                ENDWHILE 
                flag_player_on_mission = 1
                WRITE_MEMORY 0xA476AC 4 (flag_player_on_mission) FALSE 	// $ONMISSION = 1
                LOAD_AND_LAUNCH_CUSTOM_MISSION "SpiderJ16D\screwball_challenge_a"
                PRINT_FORMATTED_NOW "started!" 1000
                WAIT 1000

            ENDIF
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

}
SCRIPT_END
