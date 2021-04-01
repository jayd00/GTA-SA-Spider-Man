// by J16D
// Wars
// Spider-Man Mod for GTA SA c.2018 - 2021

//-+---CONSTANTS--------------------
CONST_INT delay_time 3000  //ms

CONST_INT _model_1 102
CONST_INT _model_2 103
CONST_INT _model_3 104

//Enemy Levels
CONST_FLOAT mass_lvl0 70.0      //default  ||---^--- Air Combo
CONST_FLOAT mass_lvl1 100.0
CONST_FLOAT mass_lvl2 120.0     //---^--- Swing Kick || Combo X4
CONST_FLOAT mass_lvl3 150.0
CONST_FLOAT mass_lvl4 200.0

SCRIPT_START
{
NOP
LVAR_INT player_actor
GET_PLAYER_CHAR 0 player_actor
CONST_INT player 0

LVAR_INT iDM
LVAR_INT iChar[5] iTaskStatus anim_seq
LVAR_INT iTempVar randomVal iCounter
LVAR_INT max_wave counter_wave number_of_members kill_counter
LVAR_FLOAT x[2] y[2] z[2] randm_x randm_y

LVAR_INT hp_chars attack_rate weap_accuracy
hp_chars = 100
attack_rate = 30
weap_accuracy = 30

max_wave = 3
number_of_members = 5

start:
WHILE NOT IS_KEY_PRESSED VK_KEY_P
    WAIT 0
ENDWHILE
SET_CAR_DENSITY_MULTIPLIER 0.0
SET_PED_DENSITY_MULTIPLIER 0.0

LOAD_CHAR_DECISION_MAKER 4 (iDM)
SET_RELATIONSHIP 4 PEDTYPE_MISSION1 PEDTYPE_PLAYER1
SET_RELATIONSHIP 0 PEDTYPE_MISSION1 PEDTYPE_MISSION1


/*0 - "m_empty.ped"
1 - "m_norm.ped"
2 - "m_tough.ped"
3 - "m_weak.ped"
4 - "m_steal.ped"
*/
//TASK_COMPLEX_KILL_PED_ON_FOOT 1000
ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_HATE 1000 (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_DISLIKE 1000 (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_DAMAGE 1000 (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot

counter_wave = 1
PRINT_FORMATTED_NOW "Get Ready! War Started! wave %d" 3000 counter_wave
//GOSUB create_decision_maker_enemys
timera = 0
WHILE delay_time > timera
    WAIT 0
ENDWHILE
GOSUB prepare_waves

kill_counter = 0
main_loop:
    iCounter = 0
    WHILE 4 >= iCounter
        IF NOT IS_CHAR_DEAD ichar[iCounter]
            //CLEO+
            /*IF NOT IS_CHAR_DOING_ANY_IMPORTANT_TASK ichar[iCounter] INCLUDE_ANIMS_PRIMARY
                CLEAR_CHAR_TASKS ichar[iCounter]
                CLEAR_CHAR_TASKS_IMMEDIATELY ichar[iCounter]
                TASK_KILL_CHAR_ON_FOOT ichar[iCounter] player_actor
                WAIT 0
            ENDIF
            */

            /*
            //horrible stuff| can't find any better way
            GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x618 (iTaskStatus) // PERFORM_SEQUENCE_TASK
            IF iTaskStatus = 7  //-1
                GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x605 (iTaskStatus) // TASK_PLAY_ANIM
                IF iTaskStatus = 7  //-1
                    GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x812 (iTaskStatus) // TASK_PLAY_ANIM_NON_INTERRUPTABLE
                    IF iTaskStatus = 7  //-1
                        GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x88A (iTaskStatus) // TASK_PLAY_ANIM_WITH_FLAGS
                        IF iTaskStatus = 7  //-1
                            GET_SCRIPT_TASK_STATUS iChar[iCounter] 0xA1A (iTaskStatus) // TASK_PLAY_ANIM_SECONDARY
                            IF iTaskStatus = 7  //-1
                                GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x829 (iTaskStatus) // TASK_DIE_NAMED_ANIM
                                IF iTaskStatus = 7  //-1
                                    GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                                    IF iTaskStatus = 7  //-1
                                        //TASK_KILL_CHAR_ON_FOOT ichar[iCounter] player_actor
                                        OPEN_SEQUENCE_TASK anim_seq
                                            //TASK_STAY_IN_SAME_PLACE -1 TRUE
                                            TASK_KILL_CHAR_ON_FOOT -1 player_actor
                                        CLOSE_SEQUENCE_TASK anim_seq
                                        PERFORM_SEQUENCE_TASK ichar[iCounter] anim_seq
                                        CLEAR_SEQUENCE_TASK anim_seq
                                        //TASK_KILL_CHAR_ON_FOOT ichar[iCounter] player_actor     //assign TASK_KILL_CHAR_ON_FOOT
                                        WAIT 0
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF*/
            


            /*
            IF GOSUB is_char_playing_temporary_tasks
            ELSE
                GET_SCRIPT_TASK_STATUS iChar[iCounter] 0x5E2 (iTaskStatus) // TASK_KILL_CHAR_ON_FOOT
                IF iTaskStatus = 7
                    TASK_KILL_CHAR_ON_FOOT ichar[iCounter] player_actor
                    WAIT 0
                ENDIF
            ENDIF
            */

        ELSE
            IF DOES_CHAR_EXIST iChar[iCounter]
                //DELETE_CHAR ichar[iCounter]
                MARK_CHAR_AS_NO_LONGER_NEEDED ichar[iCounter]
                ichar[iCounter] = -1
                kill_counter += 1
            ENDIF
        ENDIF

        IF kill_counter >= number_of_members    //next wave(s)
            kill_counter = 0    //reset counter
            counter_wave += 1   //next wave
            IF counter_wave > max_wave
                GOTO succeed_war
            ELSE
                //upgrade wave
                hp_chars += 20
                attack_rate = 10
                weap_accuracy = 10
                PRINT_FORMATTED_NOW "wait to Next wave %d" 3000 counter_wave
                timera = 0
                WHILE delay_time > timera
                    WAIT 0
                ENDWHILE
                GOSUB prepare_waves
            ENDIF
            GOTO main_loop
        ENDIF

        IF HAS_CHAR_BEEN_ARRESTED player_actor
        OR IS_PLAYER_DEAD player
        OR IS_KEY_PRESSED VK_KEY_P
            GOTO fail_war
        ENDIF
        IF counter_wave > max_wave
            GOTO succeed_war
        ENDIF
        iCounter += 1
        WAIT 0
    ENDWHILE

    WAIT 0
GOTO main_loop

fail_war:
    PRINT_FORMATTED_NOW "MISSION FAILED" 3000
GOTO end_war

succeed_war:
    PRINT_FORMATTED_NOW "MISSION PASSED" 3000

end_war:
    SET_CAR_DENSITY_MULTIPLIER 1.0
    SET_PED_DENSITY_MULTIPLIER 1.0

    iCounter = 0
    WHILE 4 >= iCounter
        IF DOES_CHAR_EXIST iChar[iCounter]
            DELETE_CHAR ichar[iCounter]
        ENDIF
        iCounter += 1
    ENDWHILE

    REMOVE_DECISION_MAKER iDM
    counter_wave = 1
    WAIT delay_time
GOTO start

prepare_waves:
    REQUEST_MODEL _model_1
    REQUEST_MODEL _model_2
    REQUEST_MODEL _model_3
    LOAD_ALL_MODELS_NOW

    iCounter = 0
    WHILE 4 >= iCounter
        GENERATE_RANDOM_FLOAT_IN_RANGE -20.0 20.0 (randm_x)
        GENERATE_RANDOM_FLOAT_IN_RANGE 5.0 30.0 (randm_y)
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (randm_x randm_y 1.0) (x[0] y[0] z[0])
        GET_NTH_CLOSEST_CAR_NODE (x[0] y[0] z[0]) 2 (x[1] y[1] z[1])
        GENERATE_RANDOM_INT_IN_RANGE _model_1 _model_3 (iTempVar)


        CREATE_CHAR PEDTYPE_MISSION1 iTempVar (x[1] y[1] z[1]) (ichar[iCounter])
        FIX_CHAR_GROUND_BRIGHTNESS_AND_FADE_IN iChar[iCounter] TRUE TRUE TRUE
        SET_CHAR_DECISION_MAKER iChar[iCounter] iDM

        SET_CHAR_HEALTH ichar[iCounter] hp_chars
        SET_CHAR_SHOOT_RATE ichar[iCounter] attack_rate
        SET_CHAR_ACCURACY ichar[iCounter] weap_accuracy

        SET_FOLLOW_NODE_THRESHOLD_DISTANCE iChar[iCounter] 200.0     //Sets the range within which the char responds to events
        //SET_INFORM_RESPECTED_FRIENDS iChar[iCounter] 10.0 1     //gossip
        SET_INFORM_RESPECTED_FRIENDS iChar[iCounter] 30.0 1     //gossip
        
        //TASK_KILL_CHAR_ON_FOOT ichar[iCounter] player_actor
        CLEO_CALL set_char_mass 0 ichar[iCounter] 120.0 //70.0   //default=70.0
        iCounter += 1
        WAIT 0 
    ENDWHILE

    MARK_MODEL_AS_NO_LONGER_NEEDED _model_1
    MARK_MODEL_AS_NO_LONGER_NEEDED _model_2
    MARK_MODEL_AS_NO_LONGER_NEEDED _model_3
RETURN

is_char_playing_temporary_tasks:    //compatibility gadgets
    IF IS_CHAR_PLAYING_ANIM ichar[iCounter] ("hit_wshoot_p")  //Web Shoot
    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_we_a")  //Electric Web
    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_we_b")  //Electric Web
    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("ko_ground") //Web Shoot, Web Bomb
        RETURN_TRUE
    ELSE
        IF IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_wf_a")  //Suspension Matrix
        OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_wf_b")  //Suspension Matrix
        OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_wh_a")  //Trip Mine
        OR IS_CHAR_PLAYING_ANIM ichar[iCounter] ("sp_wh_b")  //Trip Mine
            RETURN_TRUE
        ELSE
            IF IS_CHAR_PLAYING_ANIM ichar[iCounter] "knife_hit_4"
            OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "knife_hit_5"
            OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "knife_hit_6"
            OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "knife_hit_7"
            OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "getup"
            OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "getup_front"
                RETURN_TRUE
            ELSE
                IF IS_CHAR_PLAYING_ANIM ichar[iCounter] "KO_skid_front"   //floor anims
                OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "KO_skid_back"
                OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "KO_spin_L"
                OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "KO_spin_R"
                    RETURN_TRUE
                ELSE
                    IF IS_CHAR_PLAYING_ANIM ichar[iCounter] "dodge_front_b_hit"
                    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "dodge_front_c_hit"
                    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "dodge_front_c_hita"
                    OR IS_CHAR_PLAYING_ANIM ichar[iCounter] "dodge_front_c_hitb"
                        RETURN_TRUE
                    ELSE
                        RETURN_FALSE
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
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
create_decision_maker_enemys:
    LOAD_CHAR_DECISION_MAKER 1 (iDM)
    CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_HATE
    CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_DISLIKE
    CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_DAMAGE
    CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_SHOT_FIRED
    CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_GUN_AIMED_AT
    //CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_HEALTH_REALLY_LOW
    //CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_HEALTH_LOW
    //CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_DEAD_PED
    //CLEAR_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_PLAYER_COLLISION_WITH_PED

    /*
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_HATE TASK_COMPLEX_KILL_PED_ON_FOOT (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_ACQUAINTANCE_PED_DISLIKE TASK_COMPLEX_KILL_PED_ON_FOOT (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_DAMAGE TASK_COMPLEX_KILL_PED_ON_FOOT (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_SHOT_FIRED TASK_COMPLEX_KILL_PED_ON_FOOT (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot

    ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_GUN_AIMED_AT TASK_COMPLEX_KILL_PED_ON_FOOT (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    */

    //ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_HEALTH_REALLY_LOW TASK_COMPLEX_FALL_TO_DEATH (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    //ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_HEALTH_LOW TASK_COMPLEX_FALL_TO_DEATH (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    //ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_DEAD_PED TASK_COMPLEX_FALL_TO_DEATH (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
    //ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE iDM EVENT_PLAYER_COLLISION_WITH_PED TASK_COMPLEX_HIT_RESPONSE (0.0 100.0 0.0 100.0) 0 1 //respect|hate|like|dislike - in_car|on_foot
RETURN

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
//by Junior_Djjr
//CLEO_CALL isCharDoingTask 0 scplayer iTask
isCharDoingTask:
    //by Junior_Djj
    LVAR_INT char taskId // In
    LVAR_INT i
    //task = (uintptr_t)ped->m_pIntelligence->m_TaskMgr.FindActiveTaskByType(TASK_COMPLEX_KILL_PED_ON_FOOT);
    GET_PED_POINTER char i
    i += 0x47C //m_pIntelligence
    READ_MEMORY i 4 FALSE (i)
    i += 0x4 //m_TaskMgr
    CALL_METHOD_RETURN 0x681740 i 1 0 (taskId)(i)
    IS_THING_GREATER_THAN_THING i 0
CLEO_RETURN 0
}

// const: eRelationship
// god knows which format is legit...
CONST_INT PED_RELATIONSHIP_RESPECT 1
CONST_INT PED_RELATIONSHIP_LIKE 2
CONST_INT PED_RELATIONSHIP_DISLIKE 3
CONST_INT PED_RELATIONSHIP_HATE 4
//Events  |PedEvents.txt
//https://gtamods.com/wiki/Ped_Event
CONST_INT EVENT_PLAYER_COLLISION_WITH_PED 4
CONST_INT EVENT_DAMAGE 9
CONST_INT EVENT_DEATH 10
CONST_INT EVENT_DEAD_PED 11
CONST_INT EVENT_SHOT_FIRED 15
CONST_INT EVENT_GUN_AIMED_AT 31
CONST_INT EVENT_ACQUAINTANCE_PED_HATE 36
CONST_INT EVENT_ACQUAINTANCE_PED_DISLIKE 37
CONST_INT EVENT_HIGH_ANGER_AT_PLAYER 51 
CONST_INT EVENT_HEALTH_REALLY_LOW 52 
CONST_INT EVENT_HEALTH_LOW 53
CONST_INT EVENT_SEEN_COP 72
CONST_INT EVENT_DANGER 75
/*
//Tasks
//https://gtamods.com/wiki/Task_IDs_(GTA_SA)
CONST_INT TASK_NONE 200
CONST_INT TASK_SIMPLE_UNINTERRUPTABLE 201
CONST_INT TASK_SIMPLE_STAND_STILL 203
CONST_INT TASK_SIMPLE_SET_STAY_IN_SAME_PLACE 204
CONST_INT TASK_SIMPLE_GET_UP 205
CONST_INT TASK_COMPLEX_GET_UP_AND_STAND_STILL 206
CONST_INT TASK_SIMPLE_FALL 207
CONST_INT TASK_COMPLEX_FALL_AND_GET_UP 208
CONST_INT TASK_COMPLEX_FALL_AND_STAY_DOWN 209
CONST_INT TASK_SIMPLE_DIE 212
CONST_INT TASK_COMPLEX_DIE 217
CONST_INT TASK_SIMPLE_DEAD 218
CONST_INT TASK_SIMPLE_STAND_UP 222
CONST_INT TASK_SIMPLE_TURN_180 228
CONST_INT TASK_COMPLEX_HIT_RESPONSE 230
CONST_INT TASK_COMPLEX_HIT_BY_GUN_RESPONSE 231
CONST_INT TASK_SIMPLE_BE_DAMAGED 251
CONST_INT TASK_COMPLEX_FALL_TO_DEATH 277
CONST_INT TASK_COMPLEX_SEEK_ENTITY 907
CONST_INT TASK_COMPLEX_FLEE_ENTITY 909
CONST_INT TASK_COMPLEX_SMART_FLEE_ENTITY 911
CONST_INT TASK_COMPLEX_SEEK_ENTITY_ANY_MEANS 922
CONST_INT TASK_COMPLEX_FLEE_ANY_MEANS 927
CONST_INT TASK_COMPLEX_SEEK_ENTITY_SHOOTING 929
CONST_INT TASK_COMPLEX_SEEK_ENTITY_AIMING 933
CONST_INT TASK_COMPLEX_INVESTIGATE_DISTURBANCE 935
CONST_INT TASK_COMPLEX_FOLLOW_PED_FOOTSTEPS 936
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT 1000
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT_MELEE 1001
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT_ARMED 1002
CONST_INT TASK_KILL_ALL_THREATS 1014
CONST_INT TASK_SIMPLE_FIGHT 1016
CONST_INT TASK_SIMPLE_USE_GUN 1017
CONST_INT TASK_SIMPLE_FIGHT_CTRL 1019
CONST_INT TASK_SIMPLE_GUN_CTRL 1020
CONST_INT TASK_SIMPLE_GANG_DRIVEBY 1022
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT_STAND_STILL	1024
CONST_INT TASK_KILL_PED_ON_FOOT_WHILE_DUCKING 1026
CONST_INT TASK_SIMPLE_STEALTH_KILL 1027
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT_STEALTH 1028
CONST_INT TASK_COMPLEX_KILL_PED_ON_FOOT_KINDA_STAND_STILL 1029
*/




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

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
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
