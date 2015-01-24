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
    (slot mineSafeVertexAmount (type INTEGER))
    (slot mineOverallVertexAmount (type INTEGER))
    (slot mineOverallVertexPossessionRatio (type FLOAT))
)


;Global order variables that are fetched by the handler class.
; decision over 5 - fight
; decision below or equal 5 - do nothing

(defglobal ?*decision* = 0)

;mine rules
(defrule mineDicesCountInCurrentVertexIS_MAX
         (game_situation (mineDicesCountInCurrentVertex ?mineDicesCountInCurrentVertex))
         (test (> ?mineDicesCountInCurrentVertex 7))
	=>
	(printout t ?mineDicesCountInCurrentVertex "is in my current vertex" crlf)
	(+ ?*decision* 5)
)

(defrule enemysDicesCountInCurrentVertexIS_MAX
          (game_situation (enemysDicesCountInCurrentVertex ?value))
          (test (> ?value 7))
	 =>
	(printout t ?value "is in enemy current vertex" crlf)
	(- ?*decision* 5)
)


(defrule overallDicesCountSTATUS
  (game_situation (mineOverallDicesCount ?valueMine) (enemysOverallDicesCount ?valueEnemy))
   =>
   (if(> ?valueMine ?valueEnemy)
   then
   (printout t ?valueMine " is mine overall possession concerning dices is better" crlf)
    (+ ?*decision* 2)
    else
       (printout t ?valueMine " is mine overall possession concerning dices is worse" crlf)
        (- ?*decision* 2)
	)
)

(defrule overallPossessionSTATUS
	(game_situation (overallPossesion ?valuePossession))
	=>
	(if (> ?valuePossession 0.7)
		then
		(printout t ?valuePossession " is my overall possession and is greater than 60%" crlf)
		(+ ?*decision* 5)
		else
		(printout t ?valuePossession " is my overall possession and is smaller than 60%" crlf)
		(- ?*decision* 2)
	)
)

(defrule mineSafeVertexAmountSTATUS
	(game_situation (mineSafeVertexAmount ?value) (mineOverallVertexAmount ?overall))
	(+ ?value 3)
	=>
	(if (or (> ?value ?overall) (= ?value ?overall))
	    then
	   (printout t ?value " are my save vertices I attack" crlf)
	   (+ ?*decision* 5)
	   else
       (printout t ?value " are my save vertices I discourage attack" crlf)
       (- ?*decision* 3)
    )
)

(defrule mineOverallVertexPossessionRatioSTATUS
    (game_situation (mineOverallVertexPossessionRatio ?valueRatio))
    =>
    (if (> ?valueRatio 0.5)
        then
        (printout t ?valueRatio " is my overall vertex possession and is greater than 50%" crlf)
        (+ ?*decision* 5)
        else
        (printout t ?valueRatio " is my overall vertex possession and is smaller than 50%" crlf)
        (+ ?*decision* 4)
    )
)




(defrule endTurn
=>
(bind ?*decision*)
)

