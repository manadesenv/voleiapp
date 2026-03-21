package com.manades.voleiapp.model.federatio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
	private Long id;
	private String nombre;
	private List<Competicion> competiciones;
}
