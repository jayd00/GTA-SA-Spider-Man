// by J16D
// Dialogue Mission(s)
// in Colaboration with GTA Loyalty
// Spider-Man Mod for GTA SA c.2018 - 2021
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo
/*
FORMAT:
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_dig.cs" {id_mission} {id_dialogue} {id_call}

	id_call = -1 //NO call --other dialogues
	id_call = 0  //call In
	id_call > 0  //call Out
*/

SCRIPT_START
{
SCRIPT_NAME sp_dig
LVAR_INT id_mission id_dialogue id_call  //in
LVAR_INT iChar	//in
LVAR_INT iTempVar2 iTempVar3 iTempVar4      //in
//---
LVAR_INT toggleSpiderMod isInMainMenu audio_line_is_active
LVAR_INT counter idPowers sfx r g b iTempVar
LVAR_FLOAT sx sy fVolume

//audio_line_is_active = 1
//SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
IF id_call = -1
	GOTO state_play_warning_fight_sfx
ELSE
	IF id_call > 0
		iTempVar = 1	// 0:call_in ||1:call_out || 2:call end
	ELSE
		iTempVar = 0	// 0:call_in ||1:call_out || 2:call end
	ENDIF
ENDIF
GOSUB state_play_phone_sfx
WAIT 900

SWITCH id_mission
	DEFAULT
		BREAK
	//CASE 1	// Mission 1
	//CASE 3	// Mission 3
	CASE 2	// Mission 2 - Speech Dialogue
		SWITCH id_dialogue
			CASE 1  //ID:1  ||Speech 1  -> audios 1-11
				CLEO_CALL state_play_mp3_speech_m2 0 1 11	//1-11
				BREAK
			CASE 2  //ID:2
				CLEO_CALL state_play_mp3_speech_m2 0 12 13	//12-13
				BREAK
			CASE 3  //ID:3
				CLEO_CALL state_play_mp3_speech_m2 0 14 17	//14-17
				BREAK
			CASE 4  //ID:4
				CLEO_CALL state_play_mp3_speech_m2 0 18 21	//18-21
				BREAK
			CASE 5  //ID:5
				CLEO_CALL state_play_mp3_speech_m2 0 22 26	//22-26
				BREAK
			DEFAULT
				BREAK
		ENDSWITCH
		BREAK
ENDSWITCH
audio_line_is_active = 0
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON
iTempVar = 2	// 0:call_in ||1:call_out || 2:call end
GOSUB state_play_phone_sfx
WAIT 500
REMOVE_AUDIO_STREAM sfx
GOTO terminate_this_custom_script

state_play_warning_fight_sfx:
SWITCH id_mission
	DEFAULT
		BREAK
	//CASE 1	// Mission 1
	//CASE 3	// Mission 3
	CASE 2	// Mission 2 - Speech Dialogue
		CLEO_CALL state_play_mp3_speech_m2_during_fight 0 id_dialogue id_dialogue
		BREAK
ENDSWITCH

terminate_this_custom_script:
WAIT 50
TERMINATE_THIS_CUSTOM_SCRIPT

state_play_phone_sfx:	// 0:call_in ||1:call_out || 2:call end
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF
        BREAK
		CASE 1
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call2.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF		
			BREAK
        CASE 2
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call_end.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF
        BREAK
    ENDSWITCH
RETURN
}
SCRIPT_END

{
//CLEO_CALL state_play_mp3_speech_m2 0 id_start id_end
state_play_mp3_speech_m2:
	LVAR_INT id_start id_end	//in
	LVAR_INT scplayer hAudio id i iAudioState
	LVAR_FLOAT fVolume
	LVAR_TEXT_LABEL gxt
	GET_PLAYER_CHAR 0 scplayer
    GET_LABEL_POINTER Buffer i
	id = id_start
	WHILE id_end >= id
		STRING_FORMAT i "CLEO\SpiderJ16D\sfx\sams\CF%i.mp3" id
		IF DOES_FILE_EXIST $i
			LOAD_AUDIO_STREAM $i (hAudio)
			//LOAD_3D_AUDIO_STREAM $i (hAudio)
			SET_AUDIO_STREAM_LOOPED hAudio 0
			//SET_PLAY_3D_AUDIO_STREAM_AT_CHAR hAudio scplayer
			GET_AUDIO_SFX_VOLUME (fVolume)
			fVolume *= 0.8
			SET_AUDIO_STREAM_VOLUME hAudio fVolume
			SET_AUDIO_STREAM_STATE hAudio 1	//play
			WAIT 0
			STRING_FORMAT (gxt) "CF%i" id
			PRINT_NOW $gxt 4000 1	//subtitles must be activated
			timera = 0
			WHILE 7000 > timera 
				GET_AUDIO_STREAM_STATE hAudio (iAudioState)	//1:playing||2:paused||-1:stopped
				IF NOT iAudioState = 1
					CLEAR_PRINTS
					BREAK
				ENDIF
				WAIT 0
			ENDWHILE
			REMOVE_AUDIO_STREAM hAudio
		ENDIF
		++id
		WAIT 0
	ENDWHILE
CLEO_RETURN 0
}
{
//CLEO_CALL state_play_mp3_speech_m2_during_fight 0 id_start id_end char
state_play_mp3_speech_m2_during_fight:
	LVAR_INT id_start id_end iChar	//in
	LVAR_INT hAudio id i iAudioState
	LVAR_FLOAT fVolume
	LVAR_TEXT_LABEL gxt
    GET_LABEL_POINTER Buffer i
	id = id_start
	WHILE id_end >= id
		STRING_FORMAT i "CLEO\SpiderJ16D\sfx\sams\CFF%i.mp3" id
		IF DOES_FILE_EXIST $i
			LOAD_AUDIO_STREAM $i (hAudio)
			//LOAD_3D_AUDIO_STREAM $i (hAudio)
			SET_AUDIO_STREAM_LOOPED hAudio 0
			//SET_PLAY_3D_AUDIO_STREAM_AT_CHAR hAudio iChar
			GET_AUDIO_SFX_VOLUME (fVolume)
			fVolume *= 0.7
			SET_AUDIO_STREAM_VOLUME hAudio fVolume
			SET_AUDIO_STREAM_STATE hAudio 1	//play
			WAIT 0
			STRING_FORMAT (gxt) "CFF%i" id
			PRINT_NOW $gxt 4000 1	//subtitles must be activated
			timera = 0
			WHILE 7000 > timera 
				GET_AUDIO_STREAM_STATE hAudio (iAudioState)	//1:playing||2:paused||-1:stopped
				IF NOT iAudioState = 1
					CLEAR_PRINTS
					BREAK
				ENDIF
				WAIT 0
			ENDWHILE
			REMOVE_AUDIO_STREAM hAudio
		ENDIF
		++id
		WAIT 0
	ENDWHILE
CLEO_RETURN 0
}

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
