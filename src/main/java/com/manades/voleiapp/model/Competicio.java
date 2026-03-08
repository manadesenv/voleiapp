package com.manades.voleiapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Competicio {
	private int id;
	private String categoria;
	private String nom;
	private CompeticioTipus tipus;
	private String fase;
	private String grup;
	private Partit properPartit;
}
