package com.manades.voleiapp.model.federatio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partido {
	@JsonProperty("ID")
	private Long id;
	@JsonProperty("ID_CLUB_LOCAL")
	private Long clubLocalId;
	@JsonProperty("ID_CLUB_VISITANTE")
	private Long clubVisitanteId;
	@JsonProperty("ELOCAL")
	private String equipoLocal;
	@JsonProperty("EVISITANTE")
	private String equipoVisitante;
	@JsonProperty("FECHA")
	private String fecha;
	@JsonProperty("HORA")
	private String hora;
	@JsonProperty("RESULTADO_LOCAL")
	private String resultadoLocal;
	@JsonProperty("RESULTADO_VISITANTE")
	private String resultadoVisitante;
	@JsonProperty("Campo")
	private String campo;
	@JsonProperty("Direccion_Campo")
	private String campoDireccion;
	@JsonProperty("Municipio")
	private String municipio;
	private String arbitro1;
	private String arbitro2;
	private String anotador;
	private String cronometrador;
	private String delegado;
	@JsonProperty("COMENTARIO")
	private String comentario;
	@JsonProperty("TORNEO")
	private Long torneoId;
}
