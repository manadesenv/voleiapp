package com.manades.voleiapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Partit {
	private Long id;
	private Long clubLocalId;
	private Long clubVisitantId;
	private String equipLocal;
	private boolean equipLocalResaltat;
	private String equipVisitant;
	private boolean equipVisitantResaltat;
	private LocalDateTime data;
	private String resultatLocal;
	private String resultatVisitant;
	private String campNom;
	private String campoAdreca;
	private String municipi;
	private String arbitre;
	private String comentari;
	private Long torneigId;
	private String categoriaNom;
	private String competicioNom;
	private String faseNom;
	private Long grupId;
	private String grupNom;
	private Long tipusTorneigId;
	private String tipusTorneigNom;

	public String getResultat() {
		if (resultatLocal != null && resultatVisitant != null) {
			return resultatLocal + " - " + resultatVisitant;
		} else {
			return null;
		}
	}

}
