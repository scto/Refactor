package com.github.scto.refactor.core.gemini.model

/**
* Repräsentiert ein strukturiertes Ergebnis von einem Analyse-Prozessor.
* WIRD BENÖTIGT: Diese Klasse hat gefehlt und hat einen Build-Fehler verursacht.
*
* @param processorName Der Name des Prozessors, der dieses Ergebnis generiert hat.
* @param finding Der Titel oder die Kernaussage des Ergebnisses.
* @param details Eine detailliertere Beschreibung und mögliche Lösungsvorschläge.
*/
data class AnalysisResult(
   val processorName: String,
   val finding: String,
   val details: String
)