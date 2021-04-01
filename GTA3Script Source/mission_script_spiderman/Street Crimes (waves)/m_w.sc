// by J16D
// San Fierro On Street Crimes
// Format:
//      STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_w.cs"
// Spider-Man Mod for GTA SA c.2018 - 2021
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT delay_time 3000  //ms
CONST_INT delay_new_event 20000  //ms 20sec
CONST_INT EVENT_AVAILABLE 1
CONST_INT EVENT_NOT_AVAILABLE 0

//Enemy Levels
CONST_FLOAT mass_lvl0 70.0      //default  ||---^--- Air Combo
CONST_FLOAT mass_lvl1 100.0
CONST_FLOAT mass_lvl2 120.0     //---^--- Swing Kick || Combo X4
CONST_FLOAT mass_lvl3 150.0
CONST_FLOAT mass_lvl4 200.0

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME m_w
WAIT 0
LVAR_INT player_actor iDM iChar iBlip iEventBlip toggleSpiderMod isInMainMenu flag_player_on_mission
LVAR_INT randomVal iCounter hp_chars attack_rate weap_accuracy iTempVar iTempVar2
LVAR_INT max_wave counter_wave number_of_members kill_counter iTotalKills
LVAR_FLOAT x[3] y[3] z[3] fPedMass
LVAR_INT is_random_event_available 

GET_PLAYER_CHAR 0 player_actor

IF DOES_FILE_EXIST "CLEO\SpiderJ16D\config.ini"
    READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_crimin" (iTempVar)
    IF iTempVar = 50    //Max Missions Reached
        WAIT 100
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
ELSE
    PRINT_FORMATTED_NOW "ERROR: .ini file missing, reinstall the mod!" 5000
    WAIT 5000
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF

GOSUB generate_random_events
CLEO_CALL get_map_coords 0 (x[2] y[2] z[2])
ADD_SPRITE_BLIP_FOR_COORD x[2] y[2] z[2] RADAR_SPRITE_GANG_N (iEventBlip)
timerb = 0  //reset timer

start:
WHILE TRUE
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false

                IF timerb > 60000   //reset location each  60 sec
                    IF is_random_event_available = EVENT_AVAILABLE
                        IF DOES_BLIP_EXIST iEventBlip
                            REMOVE_BLIP iEventBlip
                            is_random_event_available = EVENT_NOT_AVAILABLE
                            timera = 0
                            WHILE delay_new_event > timera 
                                WAIT 0
                            ENDWHILE
                        ENDIF
                    ENDIF
                    IF is_random_event_available = EVENT_NOT_AVAILABLE
                        GOSUB generate_random_events
                        CLEO_CALL get_map_coords 0 (x[2] y[2] z[2])
                        ADD_SPRITE_BLIP_FOR_COORD x[2] y[2] z[2] RADAR_SPRITE_GANG_N (iEventBlip)
                        timerb = 0  //reset timer
                    ENDIF
                ENDIF
                IF is_random_event_available = EVENT_AVAILABLE
                    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[2] y[2] z[2] 20.0
                        IF flag_player_on_mission = 0
                            IF DOES_BLIP_EXIST iEventBlip
                                REMOVE_BLIP iEventBlip
                                is_random_event_available = EVENT_NOT_AVAILABLE
                                BREAK
                            ENDIF
                        ELSE    
                            PRINT_FORMATTED_NOW "Finish your current mission first!" 2000
                            WAIT 2000
                        ENDIF
                    ENDIF
                ENDIF

            ENDIF
        ELSE
            IF DOES_BLIP_EXIST iEventBlip
                REMOVE_BLIP iEventBlip
            ENDIF
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT  
        ENDIF

    ENDIF
    WAIT 0
ENDWHILE

//Start Mission
//Draw start
flag_player_on_mission = 4   //4:street crimes
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
CLEAR_AREA x[2] y[2] z[2] 50.0 1

SET_CAR_DENSITY_MULTIPLIER 0.0
SET_PED_DENSITY_MULTIPLIER 0.0

//Initial Settings
iTotalKills = 0
hp_chars = 100
attack_rate = 30
weap_accuracy = 30
max_wave = 3
number_of_members = 6
counter_wave = 1

LOAD_TEXTURE_DICTIONARY spaim
CONST_INT tBackSmall 30
LOAD_SPRITE tBackSmall "btim"

LOAD_CHAR_DECISION_MAKER 4 (iDM)
SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_PLAYER1
SET_RELATIONSHIP 0 PEDTYPE_MISSION1 PEDTYPE_MISSION1

GOSUB load_all_needed_models

PRINT_FORMATTED_NOW "Get Ready! WAVE %d" 3000 counter_wave
timera = 0
WHILE delay_time > timera
    GOSUB draw_current_waves
    WAIT 0
ENDWHILE

GOSUB prepare_waves

kill_counter = 0
main_loop:
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE

            iCounter = 0
            WHILE 9 >= iCounter
                CLEO_CALL get_stored_char 0 iCounter (iChar)
                IF DOES_CHAR_EXIST iChar
                    IF IS_CHAR_DEAD iChar
                        CLEO_CALL get_stored_marker 0 iCounter (iBlip)
                        REMOVE_BLIP iBlip
                        CLEO_CALL store_marker 0 iCounter 0x0
                        MARK_CHAR_AS_NO_LONGER_NEEDED iChar
                        CLEO_CALL store_char 0 iCounter 0x0
                        iChar = -1
                        kill_counter ++
                    ENDIF
                ENDIF
                iCounter ++
            ENDWHILE

            IF kill_counter >= number_of_members    //next wave(s)
                iTotalKills += kill_counter
                kill_counter = 0    //reset counter
                counter_wave ++   //next wave
                IF counter_wave > max_wave
                    GOTO mission_passed
                ELSE
                    //Upgrade wave
                    hp_chars += 50
                    attack_rate += 10
                    weap_accuracy += 5
                    number_of_members += 2
                    IF number_of_members > 10
                        number_of_members = 10
                    ENDIF
                    PRINT_FORMATTED_NOW "Wait to WAVE %d" 3000 counter_wave
                    timera = 0
                    WHILE delay_time > timera
                        GOSUB draw_current_waves
                        WAIT 0
                    ENDWHILE

                    GOSUB prepare_waves
                ENDIF
            ENDIF

            GOSUB draw_current_waves
            GOSUB draw_current_kills

            IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[2] y[2] z[2] 150.0   //center of stage
                GOTO mission_failed
            ENDIF

            IF HAS_CHAR_BEEN_ARRESTED player_actor
            OR IS_PLAYER_DEAD player
            OR IS_KEY_PRESSED VK_KEY_P
                GOTO mission_failed
            ENDIF

            IF isInMainMenu = 1     //1:true 0: false
                WHILE isInMainMenu = 1     //1:true 0: false
                    GOSUB readVars
                    WAIT 0
                ENDWHILE
                WHILE GET_FADING_STATUS
                    WAIT 0
                ENDWHILE
                WAIT 1000
            ENDIF
        ELSE
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            GOTO mission_failed
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop

draw_current_waves:
    //draw Waves
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (73.0 135.0) (20.0 15.0) (255 255 255 0) (1.0) (0 0 1 0) (255 255 253 200) 490 7 (0.0 0.0)  // WAVE //7 &19
    CLEO_CALL GUI_DrawBox_With2Number 0 (48.0 135.0) (20.0 15.0) (255 255 255 0) 130 18 (0.0 -4.5) counter_wave max_wave    // ~1~/~1~
    GET_FIXED_XY_ASPECT_RATIO (40.0 30.0) (x[0] y[0])
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tBackSmall (48.0 135.0) (x[0] y[0]) (255 255 255 235)
    USE_TEXT_COMMANDS FALSE
RETURN

draw_current_kills:
    //draw Kills
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (45.0 167.0) (40.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 491 11 (0.0 0.0)  // KILLS
    CLEO_CALL GUI_DrawBox_With2Number 0 (70.0 167.0) (40.0 15.0) (255 255 255 0) 129 18 (0.0 -4.5) kill_counter number_of_members    // ~1~ / ~1~ //12,15,18,19
    GET_FIXED_XY_ASPECT_RATIO (90.0 40.0) (x[0] y[0])
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tBackSmall (60.0 167.0) (x[0] y[0]) (255 255 255 235)   //(60.0 190.0)
    USE_TEXT_COMMANDS FALSE
RETURN

mission_failed:
    //clear
    GOSUB clear_all_mission_files
    WHILE NOT IS_PLAYER_PLAYING 0
        WAIT 0
    ENDWHILE
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 3  //{id}
        WAIT 2000
    ENDIF
    USE_TEXT_COMMANDS FALSE
GOTO delay_befor_restart

mission_passed:
    GOSUB clear_all_mission_files
    iTotalKills *= 10
    iTempVar = 60
    iTempVar2 = 0
    iTempVar2 = iTempVar + iTotalKills
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 6 iTempVar2 iTempVar iTotalKills    //{id} {total xp} {mission xp} {combat xp}
        WAIT 2000
    ENDIF
    SET_CLEO_SHARED_VAR varStatusLevelChar iTempVar2   //set value of +300

    GET_CLEO_SHARED_VAR varCrimesProgress (randomVal)
    randomVal ++
    CLAMP_INT randomVal 0 50 (randomVal)    //50 Max
    SET_CLEO_SHARED_VAR varCrimesProgress randomVal
    WRITE_INT_TO_INI_FILE randomVal "CLEO\SpiderJ16D\config.ini" "stadistics" "sp_crimin"
GOTO delay_befor_restart

delay_befor_restart:
    flag_player_on_mission = 0
    SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

    //Delay to reset new event
    timera = 0
    WHILE delay_new_event > timera 
        WAIT 0
    ENDWHILE
    WAIT 0
GOTO start

clear_all_mission_files:
    USE_TEXT_COMMANDS FALSE
    REMOVE_DECISION_MAKER iDM
    GOSUB remove_loaded_models
    SET_CAR_DENSITY_MULTIPLIER 1.0
    SET_PED_DENSITY_MULTIPLIER 1.0
    iCounter = 0
    WHILE 9 >= iCounter
        CLEO_CALL get_stored_char 0 iCounter (iChar)
        IF DOES_CHAR_EXIST iChar
            DELETE_CHAR iChar
            CLEO_CALL get_stored_marker 0 iCounter (iBlip)
            REMOVE_BLIP iBlip
        ENDIF
        CLEO_CALL store_marker 0 iCounter 0x0
        CLEO_CALL store_char 0 iCounter 0x0
        iCounter ++
    ENDWHILE
    WAIT 0
    REMOVE_TEXTURE_DICTIONARY
RETURN

prepare_waves:
    number_of_members--
    iCounter = 0
    WHILE number_of_members >= iCounter
        CLEO_CALL get_map_coords 0 (x[2] y[2] z[2])
        //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.0 0.5 (x[2] y[2] z[2])
        CLEO_CALL get_random_coords 0 x[2] y[2] z[2] 30.0 (x[0] y[0] z[0])
        GET_NTH_CLOSEST_CAR_NODE (x[0] y[0] z[0]) 2 (x[1] y[1] z[1])

        GOSUB generate_random_ped
        CREATE_CHAR PEDTYPE_MISSION1 randomVal (x[1] y[1] z[1]) (ichar)
            FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar TRUE TRUE TRUE
            SET_CHAR_DECISION_MAKER iChar iDM

            SET_CHAR_HEALTH iChar hp_chars
            SET_CHAR_SHOOT_RATE iChar attack_rate
            SET_CHAR_ACCURACY iChar weap_accuracy
            CLEO_CALL set_char_mass 0 iChar fPedMass  //default=70.0
            SET_FOLLOW_NODE_THRESHOLD_DISTANCE iChar 200.0     //Sets the range within which the char responds to events
            SET_INFORM_RESPECTED_FRIENDS iChar 100.0 2     //gossip

            GOSUB generate_random_weapon
            IF NOT iTempVar = WEAPONTYPE_UNARMED
                GIVE_WEAPON_TO_CHAR iChar iTempVar iTempVar2
                SET_CURRENT_CHAR_WEAPON iChar iTempVar
                SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iChar FALSE
            ENDIF
            TASK_KILL_CHAR_ON_FOOT iChar player_actor

            CLEO_CALL store_char 0 iCounter iChar

            ADD_BLIP_FOR_CHAR iChar (iBlip)
            SET_BLIP_ALWAYS_DISPLAY_ON_ZOOMED_RADAR iBlip TRUE
            CHANGE_BLIP_DISPLAY iBlip BLIP_ONLY
            CLEO_CALL store_marker 0 iCounter iBlip

        iCounter++
        WAIT 50
    ENDWHILE
    number_of_members++
RETURN

generate_random_ped:
    GENERATE_RANDOM_INT_IN_RANGE 1 8 (randomVal) //8 peds
    SWITCH randomVal
        CASE 1
            randomVal = SPECIAL01
            fPedMass = mass_lvl1
            BREAK
        CASE 2
            randomVal = SPECIAL02
            fPedMass = mass_lvl2
            BREAK
        CASE 3
            randomVal = SPECIAL03
            fPedMass = mass_lvl0
            BREAK
        CASE 4
            randomVal = SPECIAL04
            fPedMass = mass_lvl2
            BREAK
        CASE 5
            randomVal = SPECIAL05
            fPedMass = mass_lvl0
            BREAK
        CASE 6
            randomVal = SPECIAL06
            fPedMass = mass_lvl1
            BREAK
        CASE 7
            randomVal = SPECIAL07
            fPedMass = mass_lvl2
            BREAK
        DEFAULT
            randomVal = SPECIAL04
            fPedMass = mass_lvl2
            BREAK
    ENDSWITCH
RETURN

generate_random_weapon:
    GENERATE_RANDOM_INT_IN_RANGE 1 10 (iTempVar)
    SWITCH iTempVar  //RECICLED VARS
        CASE 1
            iTempVar = WEAPONTYPE_KNIFE
            iTempVar2 = 1   //ammo
            BREAK
        CASE 2
            iTempVar = WEAPONTYPE_BASEBALLBAT
            iTempVar2 = 1
            BREAK
        CASE 3
            iTempVar = WEAPONTYPE_SHOVEL
            iTempVar2 = 1
            BREAK
        CASE 4
            iTempVar = WEAPONTYPE_PISTOL
            iTempVar2 = 2000
            BREAK
        CASE 5
            iTempVar = WEAPONTYPE_M4
            iTempVar2 = 5000
            BREAK
        DEFAULT
            iTempVar = WEAPONTYPE_UNARMED
            BREAK
    ENDSWITCH
RETURN

generate_random_events:
    GENERATE_RANDOM_INT_IN_RANGE 0 36 (iTempVar)
    CLEO_CALL save_map_coords 0 (iTempVar)
    is_random_event_available = EVENT_AVAILABLE
RETURN

load_all_needed_models:
    LOAD_SPECIAL_CHARACTER 1 thug1 
    LOAD_SPECIAL_CHARACTER 2 thug2
    LOAD_SPECIAL_CHARACTER 3 thug3
    LOAD_SPECIAL_CHARACTER 4 thug4
    LOAD_SPECIAL_CHARACTER 5 gng1 
    LOAD_SPECIAL_CHARACTER 6 gng2
    LOAD_SPECIAL_CHARACTER 7 gng3

    REQUEST_MODEL KNIFECUR
    REQUEST_MODEL BAT
    REQUEST_MODEL SHOVEL
    REQUEST_MODEL COLT45
    REQUEST_MODEL M4
    LOAD_ALL_MODELS_NOW
RETURN

remove_loaded_models:
    UNLOAD_SPECIAL_CHARACTER 1
    UNLOAD_SPECIAL_CHARACTER 2
    UNLOAD_SPECIAL_CHARACTER 3
    UNLOAD_SPECIAL_CHARACTER 4
    UNLOAD_SPECIAL_CHARACTER 5
    UNLOAD_SPECIAL_CHARACTER 6
    UNLOAD_SPECIAL_CHARACTER 7

    MARK_MODEL_AS_NO_LONGER_NEEDED KNIFECUR
    MARK_MODEL_AS_NO_LONGER_NEEDED BAT
    MARK_MODEL_AS_NO_LONGER_NEEDED SHOVEL
    MARK_MODEL_AS_NO_LONGER_NEEDED COLT45
    MARK_MODEL_AS_NO_LONGER_NEEDED M4
RETURN

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

/*
0 - "m_empty.ped"
1 - "m_norm.ped"
2 - "m_tough.ped"
3 - "m_weak.ped"
4 - "m_steal.ped"

060A: create_decision_maker_type 0 store_to 47@ // decision\allowed\m_.ped files 
060A: create_decision_maker_type 2 store_to 49@ // decision\allowed\m_.ped files 
060A: create_decision_maker_type 3 store_to 48@ // decision\allowed\m_.ped files 
*/


}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL get_char_mass 0 scplayer (fMass)
get_char_mass:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMass
    LVAR_INT p
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (p)
        p += 0x8C   // CPed.CPhysical.fMass
        READ_MEMORY p 4 FALSE (fMass)
    ENDIF
CLEO_RETURN 0 fMass
}
{
//CLEO_CALL set_char_mass 0 scplayer 70.0   //default=70.0
set_char_mass:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMass    //in
    LVAR_INT p
    IF DOES_CHAR_EXIST scplayer
        GET_PED_POINTER scplayer (p)
        p += 0x8C   // CPed.CPhysical.fMass
        WRITE_MEMORY p 4 fMass FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL get_random_coords 0 x y z fRadius (x y z)
get_random_coords:
    LVAR_FLOAT x y z radius //in
    LVAR_FLOAT lower_x lower_y lower_z
    LVAR_FLOAT upper_x upper_y upper_z
    lower_x = x - radius
    lower_y = y - radius
    lower_z = z - radius
    upper_x = x + radius
    upper_y = y + radius
    upper_z = z + radius
    GENERATE_RANDOM_FLOAT_IN_RANGE (lower_x upper_x) x
    GENERATE_RANDOM_FLOAT_IN_RANGE (lower_y upper_y) y
    GENERATE_RANDOM_FLOAT_IN_RANGE (lower_z upper_z) z
CLEO_RETURN 0 x y z
}
{
//CLEO_CALL store_marker 0 counter iMarker
store_marker: 
    LVAR_INT counter iMarker  //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER marker_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    WRITE_MEMORY pActiveItem 4 iMarker FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL get_stored_marker 0 counter (iMarker)
get_stored_marker:
    LVAR_INT counter //in
    LVAR_INT pActiveItem pTempVar iMarker
    GET_LABEL_POINTER marker_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iMarker)
CLEO_RETURN 0 iMarker
}

{
//CLEO_CALL store_char 0 counter iChar
store_char: 
    LVAR_INT counter iChar  //in
    LVAR_INT pActiveItem pTempVar
    GET_LABEL_POINTER char_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    IF DOES_CHAR_EXIST iChar
        WRITE_MEMORY pActiveItem 4 iChar FALSE
    ELSE
        WRITE_MEMORY pActiveItem 4 0x0 FALSE
    ENDIF
CLEO_RETURN 0
}

{
//CLEO_CALL get_stored_char 0 counter (ichar)
get_stored_char:
    LVAR_INT counter //in
    LVAR_INT pActiveItem pTempVar iChar
    GET_LABEL_POINTER char_buffer_bytes40 (pActiveItem)
    pTempVar = counter
    pTempVar *= 4
    pActiveItem += pTempVar
    READ_MEMORY (pActiveItem) 4 FALSE (iChar)
    IF NOT DOES_CHAR_EXIST iChar
        ichar = -1
    ENDIF
CLEO_RETURN 0 iChar
}
{
//CLEO_CALL save_map_coords 0 (id_coords)
save_map_coords:
    LVAR_INT id_coords  //IN
    LVAR_TEXT_LABEL16 _lName
    LVAR_INT iTemp pActiveItem iVar
    LVAR_FLOAT x y z
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        GET_LABEL_POINTER bytes32 (iVar)
        STRING_FORMAT (_lName)"crms%i" id_coords
        READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "crimes" $_lName (iVar)
        IF NOT SCAN_STRING $iVar "%f %f %f" iTemp (x y z)
            x = 0.0
            y = 0.0
            z = 0.0
        ENDIF
        GET_LABEL_POINTER coords_buffer_12bytes (pActiveItem)
        WRITE_MEMORY pActiveItem 4 x FALSE
        pActiveItem += 4
        WRITE_MEMORY pActiveItem 4 y FALSE
        pActiveItem += 4
        WRITE_MEMORY pActiveItem 4 z FALSE
    ELSE
        PRINT_FORMATTED_NOW "ERROR coords file not found" 1500
        WAIT 1500
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL get_map_coords 0 (x y z)
get_map_coords:
    LVAR_INT pActiveItem
    LVAR_FLOAT x y z
    GET_LABEL_POINTER coords_buffer_12bytes (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (x)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (y)
    pActiveItem += 4
    READ_MEMORY (pActiveItem) 4 FALSE (z)
CLEO_RETURN 0 x y z
}

//-+----------------------- Shine GUI
{
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*ouline*/(1.4 1 1 1 1 200 200 200 200) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawBoxOutline_WithText:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_FLOAT outlineSize 
LVAR_INT outlineTop outlineRight outlineBottom outlineLeft outlineR outlineG outlineB outlineA textId formatId
LVAR_FLOAT paddingLeft paddingTop

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Outlines
IF outlineLeft = TRUE //outline side left
    f = sizeX / 2.0  
    h = posX - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineTop = TRUE //outline side top
    f = sizeY / 2.0  
    h = posY - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF  
IF outlineRight = TRUE //outline side right
    f = sizeX / 2.0    
    h = posX + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineBottom = TRUE //outline side bottom
    f = sizeY / 2.0  
    h = posY + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //Menu Item
            formatId = 8 //Menu Item ACTIVE
        ENDIF  
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_DrawBox_WithNumber 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0 /*number*/ 5
GUI_DrawBox_WithNumber:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_INT textId formatId
LVAR_FLOAT paddingLeft paddingTop
LVAR_INT iNumber

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //
            formatId = 8 // ACTIVE
        ENDIF
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF  
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT_WITH_NUMBER (posX posY) $gxt iNumber
ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_DrawBox_With2Number 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0 /*number1*/ 5 /*number2*/ 6
GUI_DrawBox_With2Number:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_INT textId formatId
LVAR_FLOAT paddingLeft paddingTop
LVAR_INT iNumber iNumber2

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //
            formatId = 8 // ACTIVE
        ENDIF
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF  
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT_WITH_2_NUMBERS (posX posY) $gxt iNumber iNumber2
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID:
LVAR_INT formatId //In
LVAR_INT i
LVAR_FLOAT g
LVAR_FLOAT xSize ySize
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Title3_LeftMenu
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Title4_Suits  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_Title2_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_Subtitle_Medium_Names  
        CLEO_RETURN 0 3.5
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_Title1_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_Title1_Menu_Active  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_Text1_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_Number1_Big_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_Text2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_Text3_Medium  
        CLEO_RETURN 0 3.0
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_Text4_Big  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_Number2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_Number3_Small  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 16
        GOSUB GUI_TextFormat_Title5_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 17
        GOSUB GUI_TextFormat_Text5_List_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 18
        GOSUB GUI_TextFormat_Interface_Main_Title  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 19
        GOSUB GUI_TextFormat_Interface_Normal_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 20
        GOSUB GUI_TextFormat_Interface_A_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 21
        GOSUB GUI_TextFormat_Interface_Number_Big
        CLEO_RETURN 0 0.0
        BREAK
ENDSWITCH

GUI_TextFormat_Title3_LeftMenu:    //1   Title 3 - Suit/SuitMods  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1 
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN    

GUI_TextFormat_Title4_Suits:     //2     Title 4 - Suits Matrix  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.28 1.05 (xSize ySize)    //0.21 0.98
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    /*
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT 2
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 0 0 0 100
    SET_TEXT_SCALE 0.3 1.0
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    */
RETURN

GUI_TextFormat_SmallMenu: //3
    SET_TEXT_COLOUR 240 240 240 255  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_SmallMenu_Active: //4    
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Title2_Menu:  //5   Title 2  (GRAY)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.09 (xSize ySize)    //0.22 1.02
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 200 200 200 255
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Subtitle_Medium_Names:  //6  Text Medium / Names  (LIGHT-BLUE)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 50 180 255 200
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Title1_Menu:   //7  Title 1    (WHITE-GRAY)
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title1_Menu_Active:  //8  Title 1 Active   (LIGHT-BLUE)
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Text1_Small_Colour:  //9   (BLUE)
    SET_TEXT_COLOUR 0 95 160 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number1_Big_Colour:  //10  BIG Numbers for Level (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.74 2.73 (xSize ySize)    //0.55 2.55
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text2_Small_Colour:  //11   LEVEL letters small (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.19 0.7 (xSize ySize)    //0.14 0.65
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text3_Medium:  //12     Text Descriptions  (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text4_Big:   //13     Text Names   (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.38 1.45 (xSize ySize)    //0.28 1.35
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN   

GUI_TextFormat_Number2_Small_Colour:  //14   Level Numbers  (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number3_Small:  //15   Level XP Numbers  (WHITE)
    SET_TEXT_COLOUR 255 255 255 220  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title5_Map:  //16  Title Map   (DARK-BLUE)
    SET_TEXT_COLOUR 43 57 58 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (43 57 58 100)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text5_List_Map:  //17  Text List Map (MAGENTA)
    SET_TEXT_COLOUR 17 242 198 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Main_Title:  //18  Text BIG (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.14 (xSize ySize)    //0.22 1.06
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Normal_Text:  //19  Text (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_A_Text:  //20  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Number_Big:  //21  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.4 1.55 (xSize ySize)    //0.30 1.45
    GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
{
GUI_GetPulseAlpha:
    LVAR_INT i
    LVAR_FLOAT g
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha i
    READ_MEMORY i 4 FALSE (g)
    i =# g
CLEO_RETURN 0 i
}
{
GUI_SetAtiveGXT:
    LVAR_INT item //In
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Update:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    CONST_FLOAT ItemPulseSpeed 2.0
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    READ_MEMORY pAlpha 4 FALSE (fAlpha)
    READ_MEMORY pStep 1 FALSE (iStep)
    IF iStep = 1
        fAlpha -=@ ItemPulseSpeed  
        IF fAlpha < 180.0
            fAlpha = 180.0
            iStep = 2
        ENDIF
    ELSE //Up
        fAlpha +=@ ItemPulseSpeed  
        IF fAlpha > 255.0
            fAlpha = 255.0
            iStep = 1
        ENDIF
    ENDIF
    WRITE_MEMORY pAlpha 4 fAlpha FALSE
    WRITE_MEMORY pStep 1 iStep FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Reset:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    WRITE_MEMORY pAlpha 4 255.0 FALSE
    WRITE_MEMORY pStep 1 1 FALSE
CLEO_RETURN 0
}
{
StoreActiveItem:
    LVAR_INT item // Item
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}

GUI_Memory_ActiveItem:
DUMP
00 00 00 00
ENDDUMP
  
GUI_Memory_ItemMenuActive_PulseAlpha:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ItemMenuActive_PulseAlpha_Step:
DUMP
00
ENDDUMP
//-+-------------------------------------------------------------------

char_buffer_bytes40:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
ENDDUMP

marker_buffer_bytes40:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
ENDDUMP

bytes32:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
ENDDUMP

coords_buffer_12bytes:
DUMP
//backpacks
00000000 00000000 00000000  // 12bytes
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

/*
//Decision Makers
0 - "m_empty.ped"
1 - "m_norm.ped"
2 - "m_tough.ped"
3 - "m_weak.ped"
4 - "m_steal.ped"
*/