;This is an example template.
;Working on your own template is highly advised.

;Templates defining main datastructures
(deftemplate game_situation
	(slot mineDicesCountInCurrentVertex (type INTEGER))
	(slot enemysDicesCountInCurrentVertex (type INTEGER))
	(slot mineOverallDicesCount (type INTEGER))
	(slot enemysOverallDicesCount (type INTEGER))
	(slot overallPossesion (type FLOAT))
	(slot range1Possesion (type FLOAT))
	(slot range2Possesion (type FLOAT))
	(slot range3Possesion (type FLOAT))
)

;Global order variables that are fetched by the handler class.
; decision over 5 - fight
; decision below or equal 5 - do nothing
(defglobal
	?*decision* = 6
)

(defrule endTurn
	=>
	(bind ?*decision*)
)
