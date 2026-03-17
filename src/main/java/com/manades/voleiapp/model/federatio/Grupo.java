package com.manades.voleiapp.model.federatio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Grupo {
	private Long id;
	private String nombre;
	@JsonProperty("id_tipo_torneo")
	private Long tipoTorneoId;
	@JsonProperty("tipo_torneo")
	private String tipoTorneoNombre;
	private List<Partido> partidos;
}
