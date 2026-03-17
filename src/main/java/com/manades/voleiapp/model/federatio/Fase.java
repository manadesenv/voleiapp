package com.manades.voleiapp.model.federatio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Fase {
	private Long id;
	private String nombre;
	private List<Grupo> grupos;
}
