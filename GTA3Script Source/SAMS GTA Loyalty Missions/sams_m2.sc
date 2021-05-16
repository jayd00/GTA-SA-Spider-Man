// by J16D
// in Colaboration with GTA Loyalty
// SAMS: Remastered | Misión #2 - Cazando Felinos
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

MISSION_START
	SCRIPT_NAME sams_m2
	GOSUB mission_start_init
	IF HAS_DEATHARREST_BEEN_EXECUTED
		GOSUB mission_failed
	ENDIF
	GOSUB mission_cleanup
MISSION_END

{
//Constans
CONST_INT player 0

// Variables for mission
LVAR_INT player_actor
LVAR_INT flag_player_on_mission toggleMusic
LVAR_INT iTempVar1 iTempVar2 iTempVar3 counter 
LVAR_INT sfx1 sfx2 sfx3 music_sfx1 music_sfx2 iEventBlip iEventBlip2 iDecisionHate iObj[4] iEnemy[6] iEnemyBlip[6] iVeh
LVAR_INT audio_line_is_active event_mission_progress kills_counter
LVAR_INT flag_enemy1_killed flag_enemy2_killed flag_enemy3_killed flag_enemy4_killed flag_enemy5_killed flag_enemy6_killed
LVAR_FLOAT fProgressTower sx sy fVolume
LVAR_FLOAT fCharSpeed fVelY fVelZ fAmplitude x y z currentTime zAngle
LVAR_INT iWebActor warning_dialogue

mission_start_init:
REGISTER_MISSION_GIVEN
flag_player_on_mission = 1
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
GET_PLAYER_CHAR 0 player_actor

REQUEST_MODEL VMAFF1 
REQUEST_MODEL VMAFF2 
REQUEST_MODEL VMAFF3 
REQUEST_MODEL VMAFF4 
REQUEST_MODEL M4 
REQUEST_MODEL CHROMEGUN
REQUEST_MODEL KNIFECUR
REQUEST_MODEL 487	// #MAVERICK
REQUEST_MODEL 1684	//#PORTAKABIN
REQUEST_MODEL 2008	//#OFFICEDESK1
REQUEST_MODEL 1598	//beachball
REQUEST_ANIMATION "INT_HOUSE"
REQUEST_ANIMATION "spider"
REQUEST_ANIMATION "mweb"
LOAD_SPECIAL_CHARACTER 9 wmt
LOAD_TEXTURE_DICTIONARY spsams
LOAD_SPRITE idTowerB "surv_tow"
LOAD_ALL_MODELS_NOW

CREATE_OBJECT_NO_OFFSET 1684 (-2117.0 544.5447 79.5693) (iObj[0])
	SET_OBJECT_ROTATION iObj[0] 0.0 0.0 0.0 
CREATE_OBJECT_NO_OFFSET 2008 (-2121.213 528.3126 78.1693) (iObj[1])
	SET_OBJECT_ROTATION iObj[1] 0.0 0.0 119.0 
CREATE_OBJECT_NO_OFFSET 2008 -2562.44 487.8188 46.7812 (iObj[2])
	SET_OBJECT_ROTATION iObj[2] 0.0 0.0 180.0 
CREATE_OBJECT_NO_OFFSET 2008 (-1746.191 769.531 166.5536) (iObj[3])
	SET_OBJECT_ROTATION iObj[3] 0.0 0.0 214.0 

CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 0.0 0.0 0.0 (iWebActor)
	SET_CHAR_COLLISION iWebActor 0
	SET_CHAR_NEVER_TARGETTED iWebActor 1

LOAD_CHAR_DECISION_MAKER 4 (iDecisionHate)
	SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_PLAYER1
	SET_RELATIONSHIP 0 PEDTYPE_MISSION1 PEDTYPE_MISSION1

// ***************************************Mission Start*************************************
GOSUB sub_lock_player_controls
SET_CHAR_COORDINATES_NO_OFFSET player_actor -1971.5574 806.7222 92.3203
SET_CHAR_HEADING player_actor 269.7697
TASK_GO_STRAIGHT_TO_COORD player_actor (-1954.4185 806.1229 92.3719) 4 -2	//walk
RESTORE_CAMERA_JUMPCUT
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_PERSIST_TRACK TRUE
CAMERA_PERSIST_POS TRUE
CAMERA_SET_VECTOR_MOVE (-1973.4156 806.1 92.55) (-1954.345 806.1 92.55) 13000 0
CAMERA_SET_VECTOR_TRACK (-1973.4156 806.1 92.55) (-1954.345 806.1 92.55) 13000 0
CAMERA_SET_SHAKE_SIMULATION_SIMPLE 1 7000.0 2.0

GET_CLEO_SHARED_VAR varMusic (toggleMusic)
IF toggleMusic = 1 // TRUE
	IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\music\SD_3.mp3"
		LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\music\SD_3.mp3" (music_sfx1)
		SET_MUSIC_DOES_FADE TRUE
		SET_AUDIO_STREAM_LOOPED music_sfx1 1
		SET_AUDIO_STREAM_STATE music_sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
		CLEO_CALL increase_volume 0 music_sfx1 0.75	//max_volume 0.0-1.0
	ENDIF
ENDIF
GOSUB sub_Fade_in_500ms
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 0 504 503 //{id} {mission_id} {text1_id} {text2_id}
ENDIF
timera = 0	//delay
WHILE 2000 > timera
	WAIT 0
ENDWHILE
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 3 0   //{id} {character_id} // 0-> Mary Jane
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 1 0 //{id_mission} {id_dialogue} {id_call} ||dialogue 1-11
ENDIF
timera = 0	//delay
WHILE 4000 > timera
	WAIT 0
ENDWHILE
CAMERA_PERSIST_TRACK FALSE
CAMERA_PERSIST_POS FALSE
RESTORE_CAMERA_JUMPCUT
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_RESET_NEW_SCRIPTABLES
GOSUB sub_unlock_player_controls
WAIT 100
GOSUB assign_task_cinematic_jump
timera = 0
WHILE 27000 > timera 	//22 Sec
	WAIT 0
ENDWHILE
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 0 504 503 //{id} {mission_id} {text1_id} {text2_id}
ENDIF
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD -2109.194 535.2387 79.1719 RADAR_SPRITE_IMPOUND (iEventBlip)	//SURVILLANCE TOWER

WAIT 0
WHILE audio_line_is_active = 1
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
	WAIT 0
ENDWHILE
event_mission_progress = 1	//1:part1 || 2:part2 || 3:part3
kills_counter = 0

mission_part1:
	IF  LOCATE_CHAR_ANY_MEANS_3D player_actor (-2109.194 535.2387 79.1719) (35.0 35.0 35.0) FALSE
		REMOVE_BLIP iEventBlip
		GOSUB create_enemys_phase1
		GOSUB sfx_state_play_open_world_to_fight
		flag_enemy1_killed = FALSE
		flag_enemy2_killed = FALSE
		flag_enemy3_killed = FALSE
		flag_enemy4_killed = FALSE
		flag_enemy5_killed = FALSE
		flag_enemy6_killed = FALSE
		warning_dialogue = 0
		IF GOSUB enemys_killed
			iTempVar1 = 0	// 0:combat end sfx || 1:blip sfx
			GOSUB state_play_sfx
			GOSUB sfx_state_play_fight_to_open_world
			GOTO dialogue_part1_A
		ELSE
			GOSUB mission_failed
			RETURN
		ENDIF
	ENDIF
	WAIT 0
GOTO mission_part1

dialogue_part1_A:
WAIT 1500
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 3 0   //{id} {character_id} // 0-> Mary Jane
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 2 1 //{id_mission} {id_dialogue} {id_call} ||dialogue 12-13
ENDIF
WHILE audio_line_is_active = 1
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
	WAIT 0
ENDWHILE
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD (-2120.938 529.5803 79.1693) RADAR_SPRITE_WAYPOINT (iEventBlip)
ADD_SPHERE (-2120.938 529.5803 79.1693) 1.0 (iEventBlip2)

mission_part1_A:
	IF LOCATE_STOPPED_CHAR_ON_FOOT_3D player_actor (-2120.938 529.5803 79.1693) (1.0 1.0 1.0) FALSE 
		REMOVE_BLIP iEventBlip
		REMOVE_SPHERE iEventBlip2
		GOSUB sub_lock_player_controls
		fProgressTower = 0.0
		WHILE TRUE
			IF IS_BUTTON_PRESSED PAD1 CROSS		// ~k~~PED_SPRINT~
				TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "WASH_UP" "INT_HOUSE" 4.0 (1 0 0 0) 6000
				WAIT 0
				iTempVar2 = 0	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
				GOSUB state_play_sfx_bar_progress
				WHILE IS_BUTTON_PRESSED PAD1 CROSS	// ~k~~PED_SPRINT~
					fProgressTower +=@ 7.5
					IF fProgressTower >= 1000.00
						fProgressTower = 1000.0
						SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
						iTempVar2 = 1	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
						GOSUB state_play_sfx_bar_progress
						WAIT 0
						GOSUB sub_unlock_player_controls
						GOTO init_part2
					ENDIF
					GOSUB draw_tower_interface
					CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 603 2 0.0 0.0	//SINCRONIZANDO CON TORRE...
					GOSUB draw_key_press_tower
					WAIT 0
				ENDWHILE
				SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
			ENDIF
			fProgressTower -=@ 15.0
			IF 0.0 > fProgressTower
				fProgressTower = 0.0
			ENDIF
			GOSUB draw_tower_interface
			CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 602 2 0.0 0.0	//SE DETECTÓ SEÑAL CORRUPTA
			GOSUB draw_key_press_tower
			WAIT 0
		ENDWHILE
	ENDIF
	WAIT 0
GOTO mission_part1_A

init_part2:
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_camb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_camb.cs"
ENDIF
WAIT 3000
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 3 0   //{id} {character_id} // 0-> Mary Jane
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 3 1 //{id_mission} {id_dialogue} {id_call} ||dialogue 14-17
ENDIF
WHILE audio_line_is_active = 1
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
	WAIT 0
ENDWHILE
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD -1756.039 786.3747 167.6563 RADAR_SPRITE_IMPOUND (iEventBlip)	//SURVILLANCE TOWER
event_mission_progress = 2	//1:part1 || 2:part2 || 3:part3

mission_part2:
	IF  LOCATE_CHAR_ANY_MEANS_3D player_actor (-1756.039 786.3747 167.6563) (35.0 35.0 35.0) FALSE
		REMOVE_BLIP iEventBlip
		GOSUB create_enemys_phase2
		GOSUB sfx_state_play_open_world_to_fight
		flag_enemy1_killed = FALSE
		flag_enemy2_killed = FALSE
		flag_enemy3_killed = FALSE
		flag_enemy4_killed = FALSE
		flag_enemy5_killed = FALSE
		flag_enemy6_killed = FALSE
		warning_dialogue = 0
		IF GOSUB enemys_killed
			iTempVar1 = 0	// 0:combat end sfx || 1:blip sfx
			GOSUB state_play_sfx
			GOSUB sfx_state_play_fight_to_open_world
			GOTO dialogue_part2_A
		ELSE
			GOSUB mission_failed
			RETURN
		ENDIF
	ENDIF
	WAIT 0
GOTO mission_part2

dialogue_part2_A:
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD (-1747.211 769.5076 167.6536) RADAR_SPRITE_WAYPOINT (iEventBlip)
ADD_SPHERE (-1747.211 769.5076 167.6536) 1.0 (iEventBlip2)

mission_part2_A:
	IF LOCATE_STOPPED_CHAR_ON_FOOT_3D player_actor (-1747.211 769.5076 167.6536) (1.0 1.0 1.0) FALSE 
		REMOVE_BLIP iEventBlip
		REMOVE_SPHERE iEventBlip2
		GOSUB sub_lock_player_controls
		fProgressTower = 0.0
		WHILE TRUE
			IF IS_BUTTON_PRESSED PAD1 CROSS		// ~k~~PED_SPRINT~
				TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "WASH_UP" "INT_HOUSE" 4.0 (1 0 0 0) 6000
				WAIT 0
				iTempVar2 = 0	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
				GOSUB state_play_sfx_bar_progress
				WHILE IS_BUTTON_PRESSED PAD1 CROSS	// ~k~~PED_SPRINT~
					fProgressTower +=@ 7.5
					IF fProgressTower >= 1000.00
						fProgressTower = 1000.0
						SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
						iTempVar2 = 1	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
						GOSUB state_play_sfx_bar_progress
						WAIT 0
						GOSUB sub_unlock_player_controls
						GOTO init_part3
					ENDIF
					GOSUB draw_tower_interface
					CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 603 2 0.0 0.0	//SINCRONIZANDO CON TORRE...
					GOSUB draw_key_press_tower
					WAIT 0
				ENDWHILE
				SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
			ENDIF
			fProgressTower -=@ 15.0
			IF 0.0 > fProgressTower
				fProgressTower = 0.0
			ENDIF
			GOSUB draw_tower_interface
			CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 602 2 0.0 0.0	//SE DETECTÓ SEÑAL CORRUPTA
			GOSUB draw_key_press_tower
			WAIT 0
		ENDWHILE
	ENDIF
	WAIT 0
GOTO mission_part2_A

init_part3:
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_camb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_camb.cs"
ENDIF
WAIT 3000
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 3 0   //{id} {character_id} // 0-> Mary Jane
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 4 1 //{id_mission} {id_dialogue} {id_call} ||dialogue 18-21
ENDIF
WHILE audio_line_is_active = 1
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
	WAIT 0
ENDWHILE
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD -2567.97 512.9598 47.7812 RADAR_SPRITE_IMPOUND (iEventBlip)	//SURVILLANCE TOWER
event_mission_progress = 3	//1:part1 || 2:part2 || 3:part3

mission_part3:
	IF  LOCATE_CHAR_ANY_MEANS_3D player_actor (-2567.97 512.9598 47.7812) (50.0 50.0 50.0) FALSE 
		REMOVE_BLIP iEventBlip
		GOSUB create_enemys_phase3
		GOSUB sfx_state_play_open_world_to_fight
		flag_enemy1_killed = FALSE
		flag_enemy2_killed = FALSE
		flag_enemy3_killed = FALSE
		flag_enemy4_killed = FALSE
		flag_enemy5_killed = FALSE
		flag_enemy6_killed = FALSE
		warning_dialogue = 0
		IF GOSUB enemys_killed
			iTempVar1 = 0	// 0:combat end sfx || 1:blip sfx
			GOSUB state_play_sfx
			GOSUB sfx_state_play_fight_to_open_world
			GOTO dialogue_part3_A
		ELSE
			GOSUB mission_failed
			RETURN
		ENDIF
	ENDIF
	WAIT 0
GOTO mission_part3

dialogue_part3_A:
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx
ADD_SPRITE_BLIP_FOR_COORD -2563.371 488.4675 47.7812 RADAR_SPRITE_WAYPOINT (iEventBlip)
ADD_SPHERE (-2563.371 488.4675 47.7812) 1.0 (iEventBlip2)

mission_part3_A:
	IF LOCATE_STOPPED_CHAR_ON_FOOT_3D player_actor (-2563.371 488.4675 47.7812) (1.0 1.0 1.0) FALSE
		REMOVE_BLIP iEventBlip
		REMOVE_SPHERE iEventBlip2
		GOSUB sub_lock_player_controls
		fProgressTower = 0.0
		WHILE TRUE
			IF IS_BUTTON_PRESSED PAD1 CROSS		// ~k~~PED_SPRINT~
				TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "WASH_UP" "INT_HOUSE" 4.0 (1 0 0 0) 6000
				WAIT 0
				iTempVar2 = 0	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
				GOSUB state_play_sfx_bar_progress
				WHILE IS_BUTTON_PRESSED PAD1 CROSS	// ~k~~PED_SPRINT~
					fProgressTower +=@ 7.5
					IF fProgressTower >= 1000.00
						fProgressTower = 1000.0
						SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
						iTempVar2 = 1	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
						GOSUB state_play_sfx_bar_progress
						WAIT 0
						GOSUB sub_unlock_player_controls
						GOTO init_part4
					ENDIF
					GOSUB draw_tower_interface
					CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 603 2 0.0 0.0	//SINCRONIZANDO CON TORRE...
					GOSUB draw_key_press_tower
					WAIT 0
				ENDWHILE
				SET_AUDIO_STREAM_STATE sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
			ENDIF
			fProgressTower -=@ 15.0
			IF 0.0 > fProgressTower
				fProgressTower = 0.0
			ENDIF
			GOSUB draw_tower_interface
			CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 602 2 0.0 0.0	//SE DETECTÓ SEÑAL CORRUPTA
			GOSUB draw_key_press_tower
			WAIT 0
		ENDWHILE
	ENDIF
	WAIT 0
GOTO mission_part3_A

init_part4:
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_camb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_camb.cs"
ENDIF
WAIT 3000
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 3 0   //{id} {character_id} // 0-> Mary Jane
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 5 1 //{id_mission} {id_dialogue} {id_call} ||dialogue 22-26
ENDIF
WHILE audio_line_is_active = 1
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
	WAIT 0
ENDWHILE
WAIT 500
GOSUB mission_passed
RETURN
//-+-------------------------------------------------------------------------

//-+--- MISSION STUFF
readVars:
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
RETURN

mission_failed:
	GET_AUDIO_STREAM_STATE music_sfx1 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx1 0.75	 //max_volume 0.0-1.0
	ENDIF
	GET_AUDIO_STREAM_STATE music_sfx2 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx2 0.75	 //max_volume 0.0-1.0
	ENDIF
	IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
		STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 1	//{id}
        WAIT 2000
    ENDIF
RETURN

mission_passed:
	kills_counter *= 15
	iTempVar1 = 130
	counter = 0
	counter = iTempVar1 + kills_counter
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 2 counter iTempVar1 kills_counter    //{id} {total xp} {mission xp} {combat xp}
		GET_AUDIO_STREAM_STATE music_sfx1 (iTempVar3)
		IF iTempVar3 = 1	//playing
			CLEO_CALL decrease_volume 0 music_sfx1 0.75	 //max_volume 0.0-1.0
		ENDIF
        WAIT 2000
    ENDIF
    SET_CLEO_SHARED_VAR varStatusLevelChar counter   //set value of +250
RETURN

mission_cleanup:
	flag_player_on_mission = 0
	SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

	IF DOES_CHAR_EXIST iWebActor
		DELETE_CHAR iWebActor
	ENDIF
	IF DOES_OBJECT_EXIST iObj[0]
		DELETE_OBJECT iObj[0]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[1]
		DELETE_OBJECT iObj[1]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[2]
		DELETE_OBJECT iObj[2]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[3]
		DELETE_OBJECT iObj[3]
	ENDIF
	REMOVE_AUDIO_STREAM music_sfx1
	REMOVE_AUDIO_STREAM music_sfx2
	counter = 0
	WHILE 5 >= counter
		IF DOES_CHAR_EXIST iEnemy[counter]
			DELETE_CHAR iEnemy[counter]
		ENDIF
		IF DOES_BLIP_EXIST iEnemyBlip[counter]
			REMOVE_BLIP iEnemyBlip[counter]
		ENDIF
		++counter
		WAIT 0
	ENDWHILE
	IF DOES_DECISION_MAKER_EXIST iDecisionHate
		REMOVE_DECISION_MAKER iDecisionHate
	ENDIF
	IF DOES_VEHICLE_EXIST iVeh
		MARK_CAR_AS_NO_LONGER_NEEDED iVeh
	ENDIF
	REMOVE_ANIMATION "INT_HOUSE"
	REMOVE_ANIMATION "spider"
	REMOVE_ANIMATION "mweb"
	MARK_MODEL_AS_NO_LONGER_NEEDED 487
	MARK_MODEL_AS_NO_LONGER_NEEDED 1684
	MARK_MODEL_AS_NO_LONGER_NEEDED 2008
	MARK_MODEL_AS_NO_LONGER_NEEDED 1598
    UNLOAD_SPECIAL_CHARACTER 9
	MARK_MODEL_AS_NO_LONGER_NEEDED M4 
	MARK_MODEL_AS_NO_LONGER_NEEDED CHROMEGUN 
	MARK_MODEL_AS_NO_LONGER_NEEDED KNIFECUR 
	MARK_MODEL_AS_NO_LONGER_NEEDED VMAFF1 
	MARK_MODEL_AS_NO_LONGER_NEEDED VMAFF2 
	MARK_MODEL_AS_NO_LONGER_NEEDED VMAFF3 
	MARK_MODEL_AS_NO_LONGER_NEEDED VMAFF4 
	USE_TEXT_COMMANDS FALSE
	WAIT 0
	REMOVE_TEXTURE_DICTIONARY
	
	MISSION_HAS_FINISHED	//cleanup
RETURN

//-+--- GOSUB HELPERS
draw_tower_interface:
    sx = 220.0
    sy = 180.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idTowerB (460.0 200.0) (sx sy) (255 255 255 230)
	CLEO_CALL GUI_DrawBoxOutline_WithText 0 (430.0 154.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 600 19 0.0 0.0	//TORRE DE VIGILANCIA
	CLEO_CALL barFunc 0 fProgressTower 390.0 205.0
RETURN

draw_key_press_tower:
    IF IS_PC_USING_JOYPAD
        iTempVar1 = 506     //
    ELSE
        iTempVar1 = 505    //~b~~h~~k~~PED_SPRINT~~w~ Manten presionado.
    ENDIF
	sx = 164.5
    sy = 20.0
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 238.5) (sx sy) (19 18 13 50) (1.0) (0 0 0 0) (255 255 253 230) iTempVar1 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN

sub_lock_player_controls:
	RESTORE_CAMERA_JUMPCUT 
	CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL player FALSE
	SET_EVERYONE_IGNORE_PLAYER player TRUE 
	SWITCH_WIDESCREEN TRUE
RETURN

sub_unlock_player_controls:
	RESTORE_CAMERA_JUMPCUT 
	SET_CAMERA_BEHIND_PLAYER
	CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL player TRUE
	SET_EVERYONE_IGNORE_PLAYER player FALSE 
	SWITCH_WIDESCREEN FALSE
RETURN

sub_Fade_out_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN

sub_Fade_in_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN 

state_play_sfx:	// 0:combat end sfx || 1:blip sfx
    SWITCH iTempVar1
        CASE 0
			REMOVE_AUDIO_STREAM sfx1
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\combat_end.mp3" (sfx1) 
                SET_AUDIO_STREAM_STATE sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				GET_AUDIO_SFX_VOLUME (fVolume)
				fVolume = 0.9
				SET_AUDIO_STREAM_VOLUME sfx1 fVolume
            ENDIF
        BREAK
        CASE 1
			REMOVE_AUDIO_STREAM sfx3
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\blip.mp3" (sfx3) 
                SET_AUDIO_STREAM_STATE sfx3 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx3 fVolume
            ENDIF
        BREAK
    ENDSWITCH
RETURN

state_play_sfx_bar_progress:
	REMOVE_AUDIO_STREAM sfx2
    SWITCH iTempVar2	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
        CASE 0
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp1.mp3" (sfx2) 
				SET_AUDIO_STREAM_LOOPED sfx2 1
				SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx2 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx2 fVolume
			ENDIF
        BREAK
        CASE 1
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp2.mp3" (sfx2) 
				SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx2 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx2 fVolume
			ENDIF
        BREAK
		CASE 2
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp3.mp3" (sfx2) 
				SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx2 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx2 fVolume
			ENDIF
			BREAK
    ENDSWITCH
RETURN

sfx_state_play_open_world_to_fight:
	GET_AUDIO_STREAM_STATE music_sfx1 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx1 0.75	 //max_volume 0.0-1.0
		SET_AUDIO_STREAM_STATE music_sfx1 0	// -1|0:stop || 1:play || 2:pause || 3:resume
		REMOVE_AUDIO_STREAM music_sfx1
		IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\music\SD_4.mp3"
			LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\music\SD_4.mp3" (music_sfx2)
			SET_MUSIC_DOES_FADE TRUE
			SET_AUDIO_STREAM_LOOPED music_sfx2 1
			SET_AUDIO_STREAM_STATE music_sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
			CLEO_CALL increase_volume 0 music_sfx2 0.75	//max_volume 0.0-1.0
		ENDIF
	ENDIF
RETURN

sfx_state_play_fight_to_open_world:
	GET_AUDIO_STREAM_STATE music_sfx2 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx2 0.75	 //max_volume 0.0-1.0
		SET_AUDIO_STREAM_STATE music_sfx2 0	// -1|0:stop || 1:play || 2:pause || 3:resume
		REMOVE_AUDIO_STREAM music_sfx2
		IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\music\SD_3.mp3"
			LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\music\SD_3.mp3" (music_sfx1)
			SET_MUSIC_DOES_FADE TRUE
			SET_AUDIO_STREAM_LOOPED music_sfx1 1
			SET_AUDIO_STREAM_STATE music_sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
			CLEO_CALL increase_volume 0 music_sfx1 0.75	//max_volume 0.0-1.0
		ENDIF
	ENDIF
RETURN

assign_task_cinematic_jump:
	iTempVar2 = 0
	fCharSpeed = 5.5
	fVelY = (1.5 * fCharSpeed)
	fVelZ = (1.6 * fCharSpeed)
	fAmplitude = 1.05
	GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x y z)
	z += 0.11
	SET_CHAR_COORDINATES_SIMPLE player_actor x y z
	TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("NULL" "NULL") 1.0 (0 0 0 0) -1
	CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ) fAmplitude
	TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_C" "spider") 65.0 (0 1 1 0) -1
	WAIT 0
	SET_CHAR_ANIM_SPEED player_actor "jump_launch_C" 1.5
	IF iTempVar2 = 0
		GOSUB playWebSound
		iTempVar2 = 1
	ENDIF
	WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_C")
		GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_C") (currentTime)
		IF currentTime > 0.4
		AND 0.5 > currentTime
			CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ)  fAmplitude
			IF iTempVar2 = 1
				GOSUB playWebSound
				iTempVar2 = 2
			ENDIF
		ENDIF
		IF currentTime > 0.85
			fAmplitude = 2.0 //1.70 //2.5
			CLEO_CALL setCharVelocity 0 player_actor (0.0 fVelY fVelZ)  fAmplitude
			BREAK
		ENDIF
		IF currentTime >= 0.188
		AND 0.844 > currentTime
			IF DOES_CHAR_EXIST iWebActor
				GET_CHAR_HEADING player_actor (zAngle)
				SET_CHAR_HEADING iWebActor zAngle
				GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 1.25 0.0 (x y z)
				SET_CHAR_COORDINATES_SIMPLE iWebActor x y z
				TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_webGoUp" "mweb") 65.0 (0 1 1 1) -2
				SET_CHAR_ANIM_CURRENT_TIME iWebActor ("m_webGoUp") currentTime
				SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("m_webGoUp") 1
			ENDIF
		ENDIF
		WAIT 0
	ENDWHILE
	TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_C" "spider") 15.0 (0 1 1 0) -2
	WAIT 0
	SET_CHAR_ANIM_SPEED player_actor "jump_glide_C" 1.5
	WAIT 0
	IF DOES_CHAR_EXIST iWebActor
		DELETE_CHAR iWebActor
	ENDIF
	//REMOVE_AUDIO_STREAM sfx3
RETURN

playWebSound:
    REMOVE_AUDIO_STREAM sfx3
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (counter)
    SWITCH counter
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1.mp3" (sfx3)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx3 player_actor
                SET_AUDIO_STREAM_STATE sfx3 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web2.mp3" (sfx3)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx3 player_actor
                SET_AUDIO_STREAM_STATE sfx3 1 
            ENDIF        
        BREAK
        DEFAULT
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web3.mp3" (sfx3)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx3 player_actor
                SET_AUDIO_STREAM_STATE sfx3 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN

create_enemys_phase3:
	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 -2584.71 503.176 47.7812 (iEnemy[0])
		SET_CHAR_HEADING iEnemy[0] 304.0 
		GIVE_WEAPON_TO_CHAR iEnemy[0] WEAPONTYPE_M4 99999 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[0] (iEnemyBlip[0])
		CHANGE_BLIP_DISPLAY iEnemyBlip[0] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[0] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[0] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[0] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 -2574.99 534.3825 47.7812 (iEnemy[1])
		SET_CHAR_HEADING iEnemy[1] 276.0
		ADD_BLIP_FOR_CHAR iEnemy[1] (iEnemyBlip[1])
		CHANGE_BLIP_DISPLAY iEnemyBlip[1] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[1] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[1] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[1] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF3 -2576.812 493.217 47.7812 (iEnemy[2])
		SET_CHAR_HEADING iEnemy[2] 332.0 
		GIVE_WEAPON_TO_CHAR iEnemy[2] WEAPONTYPE_SHOTGUN 99999 
		SET_CHAR_ACCURACY iEnemy[2] 10 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[2] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[2] (iEnemyBlip[2])
		CHANGE_BLIP_DISPLAY iEnemyBlip[2] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[2] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[2] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[2] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF4 -2553.674 499.2408 47.7812 iEnemy[3]
		SET_CHAR_HEADING iEnemy[3] 3.0
		GIVE_WEAPON_TO_CHAR iEnemy[3] WEAPONTYPE_KNIFE 1 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[3] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[3] (iEnemyBlip[3])
		CHANGE_BLIP_DISPLAY iEnemyBlip[3] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[3] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[3] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[3] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 -2542.862 521.9305 47.7812 iEnemy[4] 
		SET_CHAR_HEADING iEnemy[4] 357.0 
		GIVE_WEAPON_TO_CHAR iEnemy[4] WEAPONTYPE_KNIFE 1 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[4] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[4] (iEnemyBlip[4])
		CHANGE_BLIP_DISPLAY iEnemyBlip[4] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[4] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[4] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[4] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 -2567.072 521.6036 47.7812 iEnemy[5] 
		SET_CHAR_HEADING iEnemy[5] 332.0
		GIVE_WEAPON_TO_CHAR iEnemy[5] WEAPONTYPE_KNIFE 1 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[5] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[5] (iEnemyBlip[5])
		CHANGE_BLIP_DISPLAY iEnemyBlip[5] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[5] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[5] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[5] 15.0 2     //gossip

	CREATE_CAR 487 -2587.098 526.7334 47.7812 (iVeh)
		SET_CAR_HEADING iVeh 270.0 
RETURN

create_enemys_phase2:
	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 -1751.761 770.509 167.6563 (iEnemy[0])
		SET_CHAR_HEADING iEnemy[0] 48.0 
		GIVE_WEAPON_TO_CHAR iEnemy[0] WEAPONTYPE_M4 99999
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[0] (iEnemyBlip[0])
		CHANGE_BLIP_DISPLAY iEnemyBlip[0] BLIP_ONLY
		//CHANGE_BLIP_DISPLAY iEnemyBlip[0] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[0] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[0] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[0] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 -1736.997 791.2803 167.6563 (iEnemy[1])
		SET_CHAR_HEADING iEnemy[1] 112.0
		ADD_BLIP_FOR_CHAR iEnemy[1] (iEnemyBlip[1])
		CHANGE_BLIP_DISPLAY iEnemyBlip[1] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[1] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[1] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[1] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF3 -1762.751 769.9816 167.6563 (iEnemy[2])
		SET_CHAR_HEADING iEnemy[2] 128.0 
		GIVE_WEAPON_TO_CHAR iEnemy[2] WEAPONTYPE_SHOTGUN 99999 
		SET_CHAR_ACCURACY iEnemy[2] 10 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[2] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[2] (iEnemyBlip[2])
		CHANGE_BLIP_DISPLAY iEnemyBlip[2] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[2] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[2] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[2] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF4 -1766.599 792.1611 167.6563 (iEnemy[3])
		SET_CHAR_HEADING iEnemy[3] 248.0 
		GIVE_WEAPON_TO_CHAR iEnemy[3] WEAPONTYPE_KNIFE 1
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[3] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[3] (iEnemyBlip[3])
		CHANGE_BLIP_DISPLAY iEnemyBlip[3] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[3] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[3] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[3] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 -1753.889 797.3132 167.6563 (iEnemy[4])
		SET_CHAR_HEADING iEnemy[4] 159.0 
		GIVE_WEAPON_TO_CHAR iEnemy[4] WEAPONTYPE_KNIFE 1
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[4] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[4] (iEnemyBlip[4])
		CHANGE_BLIP_DISPLAY iEnemyBlip[4] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[4] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[4] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[4] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 -1768.631 798.4552 167.6563 (iEnemy[5])
		SET_CHAR_HEADING iEnemy[5] 211.0 
		GIVE_WEAPON_TO_CHAR iEnemy[5] WEAPONTYPE_KNIFE 1 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[5] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[5] (iEnemyBlip[5])
		CHANGE_BLIP_DISPLAY iEnemyBlip[5] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[5] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[5] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[5] 15.0 2     //gossip
RETURN

create_enemys_phase1:
	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 -2119.961 517.419 79.1693 (iEnemy[0])
		SET_CHAR_HEADING iEnemy[0] 342.0 
		GIVE_WEAPON_TO_CHAR iEnemy[0] WEAPONTYPE_M4 99999 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[0] (iEnemyBlip[0])
		CHANGE_BLIP_DISPLAY iEnemyBlip[0] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[0] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[0] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[0] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 (-2124.954 533.8098 79.1693) (iEnemy[1])
		SET_CHAR_HEADING iEnemy[1] 291.0
		ADD_BLIP_FOR_CHAR iEnemy[1] (iEnemyBlip[1])
		CHANGE_BLIP_DISPLAY iEnemyBlip[1] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[1] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[1] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[1] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF3 (-2112.024 524.4714 79.1693) (iEnemy[2])
		SET_CHAR_HEADING iEnemy[2] 338.0 
		GIVE_WEAPON_TO_CHAR iEnemy[2] WEAPONTYPE_SHOTGUN 99999 
		SET_CHAR_ACCURACY iEnemy[2] 10 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[2] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[2] (iEnemyBlip[2])
		CHANGE_BLIP_DISPLAY iEnemyBlip[2] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[2] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[2] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[2] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF4 (-2109.457 545.0896 79.1719) (iEnemy[3])
		SET_CHAR_HEADING iEnemy[3] 337.0 
		GIVE_WEAPON_TO_CHAR iEnemy[3] WEAPONTYPE_KNIFE 1
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[3] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[3] (iEnemyBlip[3])
		CHANGE_BLIP_DISPLAY iEnemyBlip[3] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[3] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[3] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[3] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF1 (-2104.291 532.576 79.1719) (iEnemy[4])
		SET_CHAR_HEADING iEnemy[4] 369.0 
		GIVE_WEAPON_TO_CHAR iEnemy[4] WEAPONTYPE_KNIFE 1 
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[4] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[4] (iEnemyBlip[4])
		CHANGE_BLIP_DISPLAY iEnemyBlip[4] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[4] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[4] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[4] 15.0 2     //gossip

	CREATE_CHAR PEDTYPE_MISSION1 VMAFF2 (-2125.518 546.6646 79.1693) (iEnemy[5])
		SET_CHAR_HEADING iEnemy[5] 19.0 
		GIVE_WEAPON_TO_CHAR iEnemy[5] WEAPONTYPE_KNIFE 1
		SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[5] FALSE
		ADD_BLIP_FOR_CHAR iEnemy[5] (iEnemyBlip[5])
		CHANGE_BLIP_DISPLAY iEnemyBlip[5] BLIP_ONLY
		SET_CHAR_DECISION_MAKER iEnemy[5] iDecisionHate
		SET_FOLLOW_NODE_THRESHOLD_DISTANCE iEnemy[5] 15.0     //Sets the range within which the char responds to events
		SET_INFORM_RESPECTED_FRIENDS iEnemy[5] 15.0 2     //gossip
RETURN

enemys_killed:
	IF flag_enemy1_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[0]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[0]
			REMOVE_BLIP iEnemyBlip[0]
			flag_enemy1_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy2_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[1]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[1]
			REMOVE_BLIP iEnemyBlip[1]
			flag_enemy2_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy3_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[2]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[2]
			REMOVE_BLIP iEnemyBlip[2]
			flag_enemy3_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy4_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[3]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[3]
			REMOVE_BLIP iEnemyBlip[3]
			flag_enemy4_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy5_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[4]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[4]
			REMOVE_BLIP iEnemyBlip[4]
			flag_enemy5_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy6_killed = FALSE
		IF IS_CHAR_DEAD iEnemy[5]
			MARK_CHAR_AS_NO_LONGER_NEEDED iEnemy[5]
			REMOVE_BLIP iEnemyBlip[5]
			flag_enemy6_killed = TRUE
		ENDIF
	ENDIF
	IF flag_enemy1_killed = TRUE
	AND flag_enemy2_killed = TRUE
	AND flag_enemy3_killed = TRUE
	AND flag_enemy4_killed = TRUE
	AND flag_enemy5_killed = TRUE
	AND flag_enemy6_killed = TRUE
		kills_counter += 6
		RETURN_TRUE
		RETURN
	ENDIF
	IF event_mission_progress = 1	//1:part1 || 2:part2 || 3:part3
		GOSUB dialogue_during_fight_A
		IF NOT LOCATE_CHAR_ANY_MEANS_3D player_actor (-2109.194 535.2387 79.1719) (100.0 100.0 100.0) FALSE
			PRINT_NOW JDSM123 1500 1
			RETURN_FALSE
			RETURN
		ENDIF
	ELSE
		IF event_mission_progress = 2	//1:part1 || 2:part2 || 3:part3
			GOSUB dialogue_during_fight_B
			IF NOT LOCATE_CHAR_ANY_MEANS_3D player_actor (-1756.039 786.3747 167.6563) (100.0 100.0 100.0) FALSE
				PRINT_NOW JDSM123 1500 1
				RETURN_FALSE
				RETURN
			ENDIF
		ELSE
			IF event_mission_progress = 3	//1:part1 || 2:part2 || 3:part3
				GOSUB dialogue_during_fight_C
				IF NOT LOCATE_CHAR_ANY_MEANS_3D player_actor (-2567.97 512.9598 47.7812) (100.0 100.0 100.0) FALSE 
					PRINT_NOW JDSM123 1500 1
					RETURN_FALSE
					RETURN
				ENDIF
			ENDIF
		ENDIF
	ENDIF
	IF NOT IS_PLAYER_PLAYING player
		RETURN_FALSE
		RETURN
	ENDIF			
	WAIT 0
GOTO enemys_killed

dialogue_during_fight_C:
	IF warning_dialogue = 0
		counter = 0
		WHILE 5 >= counter
			IF warning_dialogue = 0
				IF DOES_CHAR_EXIST iEnemy[counter]
					IF IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT
					OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_ARMED
					OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_MELEE
					OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_BE_HIT
					OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_FIGHT
					OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_KILL_ALL_THREATS
						IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
							STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 8 -1 //{id_mission} {id_dialogue} {id_call} ||Spider
						ENDIF
						warning_dialogue = 1
					ENDIF
				ENDIF
			ENDIF
			++counter
			WAIT 0
		ENDWHILE
	ENDIF
RETURN

dialogue_during_fight_B:
	SWITCH warning_dialogue
		CASE 0
			counter = 0
			WHILE 5 >= counter
				IF warning_dialogue = 0
					IF DOES_CHAR_EXIST iEnemy[counter]
						IF IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_ARMED
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_MELEE
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_BE_HIT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_FIGHT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_KILL_ALL_THREATS
							IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
								STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 4 -1 //{id_mission} {id_dialogue} {id_call} ||warning2
							ENDIF
							warning_dialogue = 1
							timera = 0
						ENDIF
					ENDIF
				ENDIF
				++counter
				WAIT 0
			ENDWHILE
			BREAK
		CASE 1
			IF timera > 4000
				IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
					STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 5 -1 //{id_mission} {id_dialogue} {id_call} ||Spider
				ENDIF
				warning_dialogue = 2
				timera = 0
			ENDIF
			BREAK
		CASE 2
			IF timera > 9000
				IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
					STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 6 -1 //{id_mission} {id_dialogue} {id_call} ||Warning3
				ENDIF
				warning_dialogue = 3
			ENDIF
			BREAK
		DEFAULT 
			BREAK
	ENDSWITCH
RETURN

dialogue_during_fight_A:
	SWITCH warning_dialogue
		CASE 0
			counter = 0
			WHILE 5 >= counter
				IF warning_dialogue = 0
					IF DOES_CHAR_EXIST iEnemy[counter]
						IF IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_ARMED
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_COMPLEX_KILL_PED_ON_FOOT_MELEE
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_BE_HIT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_SIMPLE_FIGHT
						OR IS_CHAR_DOING_TASK_ID iEnemy[counter] TASK_KILL_ALL_THREATS
							IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
								STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 1 -1 //{id_mission} {id_dialogue} {id_call} ||warning1
							ENDIF
							warning_dialogue = 1
							timera = 0
						ENDIF
					ENDIF
				ENDIF
				++counter
				WAIT 0
			ENDWHILE
			BREAK
		CASE 1
			IF timera > 5000
				IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
					STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 2 -1 //{id_mission} {id_dialogue} {id_call} ||Spider
				ENDIF
				warning_dialogue = 2
				timera = 0
			ENDIF
			BREAK
		CASE 2
			IF timera > 9000
				IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_dig.cs"
					STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" 2 3 -1 //{id_mission} {id_dialogue} {id_call} ||Spider
				ENDIF
				warning_dialogue = 3
			ENDIF
			BREAK
		DEFAULT 
			BREAK
	ENDSWITCH
RETURN
}


//--+--- CALLSCM HELPERS
{
//CLEO_CALL barFunc 0 fSize posx posy	//max size 1000.0
barFunc:
    LVAR_FLOAT sizeBar posx posy  // In
    LVAR_FLOAT copyPosX xSize
    xSize = sizeBar
    xSize /= 1000.0 	//fresX
    xSize *= 146.0
    copyPosX = xSize
    copyPosX /= 2.0
    copyPosX += posx 	//270+(120/2)= 330
	USE_TEXT_COMMANDS FALSE
    DRAW_RECT (copyPosX posy) (xSize 2.5) (11 255 187 210)
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer   //in
    LVAR_FLOAT xVel yVel zVel amplitude //in
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
//CLEO_CALL sync_music_to_game_sfx 0 sfx fMaxVolume
sync_music_to_game_sfx:
    LVAR_INT sfx //IN
    LVAR_FLOAT fMaxVolume //IN
    LVAR_FLOAT fGameVolume
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fMaxVolume
CLEO_RETURN 0
}
{
//CLEO_CALL increase_volume 0 sfx fMaxVolume
increase_volume:
    LVAR_INT sfx    //IN
    LVAR_FLOAT fMaxVolume   //IN
    LVAR_FLOAT fVolume fGameVolume
    fVolume = 0.0
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume < fMaxVolume
        fVolume +=@ 0.05
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
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    fVolume = fMaxVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume > 0.1 
        fVolume -=@ 0.05
        IF 0.0 > fVolume
            fVolume = 0.0
        ENDIF
        SET_AUDIO_STREAM_VOLUME sfx fVolume
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_VOLUME sfx 0.0
CLEO_RETURN 0
}
//-+--- Shine GUI
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
    STRING_FORMAT gxt "JDSM%i" textId
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
    STRING_FORMAT gxt "JDSM%i" textId
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
// --- Functions
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
// Thread Memory
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
//-+-----------------------------------------------------------------


Buffer:	//8x2*4= 64
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
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

//-+---
CONST_INT idTowerB 55

