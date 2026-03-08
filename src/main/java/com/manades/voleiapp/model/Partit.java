package com.manades.voleiapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Partit {
	private Competicio competicio;
	private Club local;
	private Club visitant;
	private LocalDateTime data;
	private String municipi;
	private Integer puntuacioLocal;
	private Integer puntuacioVisitant;
	private String marcador;
}
