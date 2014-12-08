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
; 1 - DoNothing
; 2 - Fight
(defglobal
	?*decision* = 6
)

;Starts here, checks if there are sufficient resources to buy sth, if not then ends turn.
;NOTE: names are entirely optional.

(defrule endTurn
	=>
	(printout t "CLIPS response!" crlf)
	(bind ?*decision*)
)
