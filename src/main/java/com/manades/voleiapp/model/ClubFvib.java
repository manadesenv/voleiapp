package com.manades.voleiapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClubFvib {
	@JsonProperty("ID")
	private int id;
	@JsonProperty("Nombre")
	private String nom;
}
