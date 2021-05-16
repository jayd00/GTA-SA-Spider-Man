// by J16D
// Music Ambient (PS4 - Game) to SA
// Spider-Man Mod for GTA SA c.2018 - 2021

//-+---CONSTANTS--------------------
CONST_INT SFX_VOLUME_ADDRESS 0xB5FCCC
// ID
CONST_INT id_A 0
CONST_INT id_B 4
CONST_INT id_C 8
CONST_INT iDay   1
CONST_INT iNight 0
CONST_INT SFX_OPEN_WORLD 1
CONST_INT SFX_MAIN_MENU 2
CONST_INT SFX_M_CAR_CHASE 3
CONST_INT SFX_M_THUG_HIDOUTS 4
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_msc
WAIT 0
LVAR_INT flag_player_on_mission toggleMusic toggleSpiderMod isInMainMenu audio_line_is_active  //1:true 0: false
LVAR_INT id_sfx sfx sfx_menu sfx_mission 
LVAR_INT iCurrentTime iRandomVal
LVAR_INT mp3_state mp3_state_menu
LVAR_INT iHour iMin
LVAR_INT flag_day_time flag_last_time_played is_in_interior flag_last_sfx
LVAR_FLOAT fMaxVolume

start_loop:
    GOSUB readVars
    IF toggleSpiderMod = 1
        GOTO prepare_needed_files
    ENDIF
    WAIT 0
GOTO start_loop

prepare_needed_files:
READ_FLOAT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "VOLUME" (fMaxVolume)
CLEO_CALL store_menu_sfx 0 ()
CLEO_CALL store_open_world_day_sfx 0 ()
CLEO_CALL store_open_world_night_sfx 0 ()
CLEO_CALL store_car_chase_sfx 0 ()
CLEO_CALL store_thug_hidouts_sfx 0 ()
WHILE GET_FADING_STATUS
    WAIT 0
ENDWHILE
//CLEO_CALL get_day_time 0 (flag_day_time)
//flag_last_time_played = flag_day_time
//flag_last_sfx = NULL

WHILE TRUE
    IF IS_PLAYER_PLAYING player
        GOSUB readVars
        IF toggleSpiderMod = 1
            IF toggleMusic = 1 // TRUE

                IF isInMainMenu = 0 // Open World
                    
                    GET_AREA_VISIBLE (is_in_interior)
                    IF is_in_interior = 0   //Open World

                        GOSUB state_play_open_world_sfx
                        WHILE isInMainMenu = 0      //1:true 0: false
                        AND is_in_interior = 0      //0: outside
                            CLEO_CALL get_day_time 0 (flag_day_time)
                            IF NOT flag_day_time = flag_last_time_played
                                BREAK
                            ENDIF
                            CLEO_CALL sync_music_to_game_sfx 0 sfx fMaxVolume

                            IF NOT IS_PLAYER_PLAYING player
                                GOTO player_busted_wasted
                            ENDIF
                            GET_AREA_VISIBLE (is_in_interior)
                            GOSUB readVars
                            IF toggleSpiderMod = 0
                                GOTO turn_off_sounds
                            ENDIF

                            IF flag_player_on_mission > 0
                                CLEO_CALL decrease_volume 0 sfx fMaxVolume
                                CLEO_CALL set_state_music_sfx 0 sfx 2  // -1|0:stop || 1:play || 2:pause || 3:resume
                                WAIT 100
                                IF flag_player_on_mission = 1   //now you can play MP3 in_mission with this flag
                                    WHILE  flag_player_on_mission > 0
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0
                                            GOTO turn_off_sounds
                                        ENDIF
                                        WAIT 0
                                    ENDWHILE
                                ELSE
                                    GOSUB state_play_mission_sfx
                                    WHILE  flag_player_on_mission > 0
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0
                                            GOTO turn_off_sounds
                                        ENDIF
                                        CLEO_CALL sync_music_to_game_sfx 0 sfx_mission fMaxVolume
                                        //to do: pause mission sfx when in interior
                                        //GET_AREA_VISIBLE (is_in_interior)
                                        WAIT 0
                                    ENDWHILE
                                    CLEO_CALL decrease_volume 0 sfx_mission fMaxVolume
                                    CLEO_CALL set_state_music_sfx 0 sfx_mission 0  // -1|0:stop || 1:play || 2:pause || 3:resume
                                ENDIF
                                WAIT 100
                                CLEO_CALL set_state_music_sfx 0 sfx 3    // -1|0:stop || 1:play || 2:pause || 3:resume
                                CLEO_CALL increase_volume 0 sfx fMaxVolume
                                flag_last_sfx = SFX_OPEN_WORLD
                                WAIT 0
                            ENDIF

                            WAIT 0
                        ENDWHILE
                    ENDIF
                    GET_AUDIO_STREAM_STATE sfx (mp3_state)
                    IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
                    OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                        CLEO_CALL decrease_volume 0 sfx fMaxVolume
                        CLEO_CALL set_state_music_sfx 0 sfx 0  // -1|0:stop || 1:play || 2:pause || 3:resume
                    ENDIF
                    GOTO last_line_sfx_music

                    player_busted_wasted:
                    GET_AUDIO_STREAM_STATE sfx (mp3_state)
                    IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
                    OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                        CLEO_CALL decrease_volume 0 sfx fMaxVolume
                        CLEO_CALL set_state_music_sfx 0 sfx 0  // -1|0:stop || 1:play || 2:pause || 3:resume
                    ENDIF
                    WAIT 0
                    WHILE NOT IS_PLAYER_PLAYING player
                        WAIT 0
                    ENDWHILE
                    WHILE GET_FADING_STATUS
                        WAIT 0
                    ENDWHILE

                    last_line_sfx_music:
                    WAIT 0

                ELSE    //in main menu
                    /*
                    GET_AUDIO_STREAM_STATE sfx_menu (mp3_state)
                    IF mp3_state = 0    // -1|0:stop || 1:play || 2:pause || 3:resume
                    OR mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
                    OR mp3_state = -1   // -1|0:stop || 1:play || 2:pause || 3:resume
                        GOSUB state_play_menu_sfx
                    ENDIF
                    */
                    GOSUB state_play_menu_sfx

                    WHILE isInMainMenu = 1     //1:true 0: false
                        GOSUB readVars
                        GET_AUDIO_STREAM_STATE sfx_menu (mp3_state)
                        IF mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                            CLEO_CALL sync_music_to_game_sfx 0 sfx_menu fMaxVolume
                        ENDIF
                        IF toggleMusic = 0 // FALSE
                            GOSUB pause_sounds
                        ENDIF
                        IF toggleSpiderMod = 0
                            GOTO turn_off_sounds
                        ENDIF
                        WAIT 0
                    ENDWHILE

                    GET_AUDIO_STREAM_STATE sfx_menu (mp3_state)
                    IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
                    OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                        CLEO_CALL decrease_volume 0 sfx_menu fMaxVolume
                        CLEO_CALL set_state_music_sfx 0 sfx_menu 0  // -1|0:stop || 1:play || 2:pause || 3:resume
                    ENDIF
                    flag_last_sfx = NULL

                ENDIF

            ENDIF
        ELSE
            GOTO turn_off_sounds
        ENDIF
    ELSE
        GOSUB stop_current_sfx
        WAIT 0
        WHILE NOT IS_PLAYER_PLAYING player
            WAIT 0
        ENDWHILE
        WHILE GET_FADING_STATUS
            WAIT 0
        ENDWHILE
    ENDIF
    WAIT 0
ENDWHILE

turn_off_sounds:
    GOSUB stop_current_sfx
    WAIT 0
    CLEO_CALL remove_menu_sfx 0 ()
    CLEO_CALL remove_open_world_day_sfx 0 ()
    CLEO_CALL remove_open_world_night_sfx 0 ()
    CLEO_CALL remove_car_chase_sfx 0 ()
    CLEO_CALL remove_thug_hidouts_sfx 0 ()
WAIT 250
GOTO start_loop

//-+--- GOSUB HELPERS

state_play_mission_sfx:   //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2   
    SWITCH flag_player_on_mission
        CASE 1  //1:on mission
            BREAK
        CASE 2  //2:car chase 
            CLEO_CALL get_random_sfx_3 0 (id_sfx)
            CLEO_CALL get_car_chase_sfx 0 id_sfx (sfx_mission)
            flag_last_sfx = SFX_M_CAR_CHASE
            BREAK
        CASE 3  //3:criminal
        CASE 4  //4:street crimes
            CLEO_CALL get_random_sfx_3 0 (id_sfx)
            CLEO_CALL get_thug_hidouts_sfx 0 id_sfx (sfx_mission)
            flag_last_sfx = SFX_M_THUG_HIDOUTS
            BREAK
        DEFAULT //others
            BREAK
    ENDSWITCH
    CLEO_CALL set_state_music_sfx 0 sfx_mission 1  // -1|0:stop || 1:play || 2:pause || 3:resume
    CLEO_CALL increase_volume 0 sfx_mission fMaxVolume
    SET_AUDIO_STREAM_LOOPED sfx_mission TRUE
RETURN

state_play_menu_sfx:
    CLEO_CALL get_random_sfx_2 0 (id_sfx)
    CLEO_CALL get_menu_sfx 0 id_sfx (sfx_menu)

    CLEO_CALL set_state_music_sfx 0 sfx_menu 1  // -1|0:stop || 1:play || 2:pause || 3:resume
    CLEO_CALL increase_volume 0 sfx_menu fMaxVolume
    SET_AUDIO_STREAM_LOOPED sfx_menu TRUE
    flag_last_time_played = 2   //menu
    flag_last_sfx = SFX_MAIN_MENU
RETURN

state_play_open_world_sfx:
    CLEO_CALL get_day_time 0 (flag_day_time)
    CLEO_CALL get_random_sfx_3 0 (id_sfx)
    SWITCH flag_day_time
        CASE iDay
            CLEO_CALL get_open_world_day_sfx 0 id_sfx (sfx)
            BREAK
        CASE iNight
            CLEO_CALL get_open_world_night_sfx 0 id_sfx (sfx)
            BREAK
    ENDSWITCH
    CLEO_CALL set_state_music_sfx 0 sfx 1  // -1|0:stop || 1:play || 2:pause || 3:resume
    CLEO_CALL increase_volume 0 sfx fMaxVolume
    SET_AUDIO_STREAM_LOOPED sfx TRUE
    flag_last_time_played = flag_day_time
    flag_last_sfx = SFX_OPEN_WORLD
RETURN

stop_current_sfx:
    SWITCH flag_last_sfx
        CASE SFX_MAIN_MENU
            GET_AUDIO_STREAM_STATE sfx_menu (mp3_state)
            IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
            OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                CLEO_CALL decrease_volume 0 sfx_menu fMaxVolume
                CLEO_CALL set_state_music_sfx 0 sfx_menu 0  // -1|0:stop || 1:play || 2:pause || 3:resume
            ENDIF
            BREAK
        CASE SFX_OPEN_WORLD
            GET_AUDIO_STREAM_STATE sfx (mp3_state)
            IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
            OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                CLEO_CALL decrease_volume 0 sfx fMaxVolume
                CLEO_CALL set_state_music_sfx 0 sfx 0  // -1|0:stop || 1:play || 2:pause || 3:resume
            ENDIF
            BREAK
        DEFAULT // SFX_M_CAR_CHASE || SFX_M_THUG_HIDOUTS
            GET_AUDIO_STREAM_STATE sfx_mission (mp3_state)
            IF mp3_state = 2    // -1|0:stop || 1:play || 2:pause || 3:resume
            OR mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
                CLEO_CALL decrease_volume 0 sfx_mission fMaxVolume
                CLEO_CALL set_state_music_sfx 0 sfx_mission 0  // -1|0:stop || 1:play || 2:pause || 3:resume
            ENDIF
            BREAK
    ENDSWITCH
    flag_last_sfx = NULL
RETURN

pause_sounds:
    GET_AUDIO_STREAM_STATE sfx_menu (mp3_state)
    IF mp3_state = 1    // -1|0:stop || 1:play || 2:pause || 3:resume
        CLEO_CALL decrease_volume 0 sfx_menu fMaxVolume
        CLEO_CALL set_state_music_sfx 0 sfx_menu 2  // -1|0:stop || 1:play || 2:pause || 3:resume   
    ENDIF

    WHILE toggleMusic = 0 // FALSE
        GOSUB readVars
        IF toggleSpiderMod = 0
        OR isInMainMenu = 0     //1:true 0: false
            GOTO force_out_menu
        ENDIF
        WAIT 0
    ENDWHILE
    CLEO_CALL set_state_music_sfx 0 sfx_menu 3   // -1|0:stop || 1:play || 2:pause || 3:resume   
    CLEO_CALL increase_volume 0 sfx_menu fMaxVolume

    force_out_menu:
RETURN

readVars:
    GET_CLEO_SHARED_VAR varMusic (toggleMusic)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
RETURN

/*player_is_on_mission:
    CLEO_CALL readGlobalVar 0 (409)(onmission)      //409=ONMISSION
    IF onmission > 0
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN*/


}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL readGlobalVar 0 (132)(ryder)
readGlobalVar:
    LVAR_INT var //In
    LVAR_INT value scriptSpace finalOffset
    READ_MEMORY 0x00468D5E 4 1 (scriptSpace)
    finalOffset = var * 4
    finalOffset += scriptSpace
    READ_MEMORY finalOffset 4 FALSE (value)
CLEO_RETURN 0 (value)
}
{
//CLEO_CALL set_state_music_sfx 0 sfx state_sfx
set_state_music_sfx:
    LVAR_INT sfx state_sfx   // in
    LVAR_INT mp3_state
    SET_MUSIC_DOES_FADE TRUE
    SET_AUDIO_STREAM_STATE sfx state_sfx
    WAIT 0
CLEO_RETURN 0
}
{
//CLEO_CALL get_day_time 0 (flag_day_time)
get_day_time:
    LVAR_INT flag_day_time
    LVAR_INT iHour iMin
    GET_TIME_OF_DAY iHour iMin   //hours from 0 to 23 || minutes from 0 to 59.
    IF iHour > 5
    AND 19 > iHour  // Day    
        flag_day_time = iDay
    ELSE
        flag_day_time = iNight
    ENDIF
CLEO_RETURN 0 flag_day_time
}
//-+--- SFX
{
//CLEO_CALL increase_volume 0 sfx fMaxVolume
increase_volume:
    LVAR_INT sfx    //IN
    LVAR_FLOAT fMaxVolume   //IN
    LVAR_FLOAT fVolume fGameVolume
    fVolume = 0.0
    READ_MEMORY SFX_VOLUME_ADDRESS 4 FALSE (fGameVolume)      //0xB5FCCC (float 0.0-1.0)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume < fMaxVolume
        fVolume +=@ 0.009
        IF fVolume > 0.5
            fVolume = fMaxVolume
        ENDIF
        SET_AUDIO_STREAM_VOLUME sfx fVolume
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_VOLUME sfx fVolume
CLEO_RETURN 0
}
{
//CLEO_CALL decrease_volume 0 sfx fMaxVolume
decrease_volume:
    LVAR_INT sfx    //IN
    LVAR_FLOAT fMaxVolume   //IN
    LVAR_FLOAT fVolume fGameVolume
    READ_MEMORY SFX_VOLUME_ADDRESS 4 FALSE (fGameVolume)      //0xB5FCCC (float 0.0-1.0)
    fMaxVolume *= fGameVolume
    fVolume = fMaxVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume > 0.1 
        fVolume -=@ 0.009
        IF 0.0 > fVolume
            fVolume = 0.0
        ENDIF
        SET_AUDIO_STREAM_VOLUME sfx fVolume
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_VOLUME sfx 0.0
CLEO_RETURN 0
}
{
//CLEO_CALL sync_music_to_game_sfx 0 sfx fMaxVolume
sync_music_to_game_sfx:
    LVAR_INT sfx //IN
    LVAR_FLOAT fMaxVolume //IN
    LVAR_FLOAT fGameVolume
    READ_MEMORY SFX_VOLUME_ADDRESS 4 FALSE (fGameVolume)      //0xB5FCCC (float 0.0-1.0)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fMaxVolume
CLEO_RETURN 0
}
{
//CLEO_CALL get_random_sfx_2 0 (id_sfx)
get_random_sfx_2:
    LVAR_INT iRandomVal id_sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 2 (iRandomVal)
    SWITCH iRandomVal
        CASE 0
            id_sfx = id_A
            BREAK
        DEFAULT
            id_sfx = id_B
            BREAK
    ENDSWITCH
CLEO_RETURN 0 id_sfx
}
{
//CLEO_CALL get_random_sfx_3 0 (id_sfx)
get_random_sfx_3:
    LVAR_INT iRandomVal id_sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomVal)
    SWITCH iRandomVal
        CASE 0
            id_sfx = id_A
            BREAK
        CASE 1
            id_sfx = id_B
            BREAK
        DEFAULT
            id_sfx = id_C
            BREAK
    ENDSWITCH
CLEO_RETURN 0 id_sfx
}
{
//CLEO_CALL get_menu_sfx 0 /*ID*/0 /*sound*/sfx
get_menu_sfx:
    // ID:0 - sfx1
    // ID:4 - sfx2
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_music_menu_8bytes (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL get_open_world_day_sfx 0 /*ID*/0 /*sound*/sfx
get_open_world_day_sfx:
    // ID:0 - sfx1
    // ID:4 - sfx2
    // ID:8 - sfx3
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_music_open_world_day_12bytes (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL get_open_world_night_sfx 0 /*ID*/0 /*sound*/sfx
get_open_world_night_sfx:
    // ID:0 - sfx1
    // ID:4 - sfx2
    // ID:8 - sfx3
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_music_open_world_night_12bytes (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL get_car_chase_sfx 0 /*ID*/0 /*sound*/sfx
get_car_chase_sfx:
    // ID:0 - sfx1
    // ID:4 - sfx2
    // ID:8 - sfx3
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_music_car_chase_mission_12bytes (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL get_thug_hidouts_sfx 0 /*ID*/0 /*sound*/sfx
get_thug_hidouts_sfx:
    // ID:0 - sfx1
    // ID:4 - sfx2
    // ID:8 - sfx3
    LVAR_INT id_Sfx //In       
    LVAR_INT pActiveItem sfx
    GET_LABEL_POINTER SFX_music_thug_hidouts_mission_12bytes (pActiveItem)
    pActiveItem += id_Sfx
    READ_MEMORY (pActiveItem) 4 FALSE (sfx)
CLEO_RETURN 0 sfx
}
{
//CLEO_CALL store_menu_sfx 0
store_menu_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_menu_8bytes pActiveItem
    pActiveItem += 0x0
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_menuA.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_menuA.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_menuB.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_menuB.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL store_open_world_day_sfx 0
store_open_world_day_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_open_world_day_12bytes pActiveItem
    pActiveItem += 0x0
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_dayA.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_dayA.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_dayB.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_dayB.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_dayC.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_dayC.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL store_open_world_night_sfx 0
store_open_world_night_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_open_world_night_12bytes pActiveItem
    pActiveItem += 0x0
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_nightA.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_nightA.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_nightB.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_nightB.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_nightC.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_nightC.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL store_car_chase_sfx 0
store_car_chase_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_car_chase_mission_12bytes pActiveItem
    pActiveItem += 0x0
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_cc_1.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_cc_1.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_cc_2.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_cc_2.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_cc_3.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_cc_3.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL store_thug_hidouts_sfx 0
store_thug_hidouts_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_thug_hidouts_mission_12bytes pActiveItem
    pActiveItem += 0x0
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_th_1.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_th_1.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_th_2.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_th_2.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
    pActiveItem += 0x4
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\sfx_th_3.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\sfx_th_3.mp3" (sfx)
        WRITE_MEMORY pActiveItem 4 sfx FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}

{
//CLEO_CALL remove_menu_sfx 0
remove_menu_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_menu_8bytes pActiveItem
    pActiveItem += 0x0
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL remove_open_world_day_sfx 0
remove_open_world_day_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_open_world_day_12bytes pActiveItem
    pActiveItem += 0x0
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL remove_open_world_night_sfx 0
remove_open_world_night_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_open_world_night_12bytes pActiveItem
    pActiveItem += 0x0
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL remove_car_chase_sfx 0
remove_car_chase_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_car_chase_mission_12bytes pActiveItem
    pActiveItem += 0x0
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL remove_thug_hidouts_sfx 0
remove_thug_hidouts_sfx:
    LVAR_INT sfx pActiveItem
    GET_LABEL_POINTER SFX_music_thug_hidouts_mission_12bytes pActiveItem
    pActiveItem += 0x0
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    pActiveItem += 0x4
        READ_MEMORY (pActiveItem) 4 FALSE (sfx)
        REMOVE_AUDIO_STREAM sfx
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
CLEO_RETURN 0
}

SFX_music_menu_8bytes:
DUMP
00000000 00000000 00000000
ENDDUMP

SFX_music_open_world_day_12bytes:
DUMP
00000000 00000000 00000000
ENDDUMP

SFX_music_open_world_night_12bytes:
DUMP
00000000 00000000 00000000
ENDDUMP

SFX_music_car_chase_mission_12bytes:
DUMP
00000000 00000000 00000000
ENDDUMP

SFX_music_thug_hidouts_mission_12bytes:
DUMP
00000000 00000000 00000000
ENDDUMP

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

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:thug hidouts || 4:street crimes || 5:boss2
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

CONST_INT varAudioActive     	45    // 0:OFF || 1:ON  ||global var to check -spech- audio playing
