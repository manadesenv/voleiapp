package com.manades.voleiapp.helper;

import com.manades.voleiapp.model.Partit;
import com.manades.voleiapp.model.federatio.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartitsHelper {

	private static final String PARTITS_URL = "https://www.voleibolib.net/JSON/get_partidos_desglose_competiciones.asp?op={{op}}&fini=&ffin=";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final RestTemplate restTemplate;

	@CachePut(value = "partits", key = "#op")
	@Retryable(
			retryFor = RestClientException.class,
			maxAttempts = 3,
			backoff = @Backoff(delay = 10000)
	)
	public List<Partit> findPartits(int op) {
		return loadPartits(op);
	}

	private List<Partit> loadPartits(int op) {
		String url = getPartitsUrl(String.valueOf(op));
		log.debug("Loading data from {}", url);
		String json = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		PartitsResponse response = mapper.readValue(json, PartitsResponse.class);
		if (response == null || response.getCategorias() == null) {
			return List.of();
		}
		return response.getCategorias().stream().
				filter(categoria -> categoria.getCompeticiones() != null).
				flatMap(categoria -> categoria.getCompeticiones().stream().
						filter(competicion -> competicion.getFases() != null).
						flatMap(competicion -> competicion.getFases().stream().
								filter(fase -> fase.getGrupos() != null).
								flatMap(fase -> fase.getGrupos().stream().
										filter(grupo -> grupo.getPartidos() != null).
										flatMap(grupo -> grupo.getPartidos().stream().
												map(partido -> mapPartit(
														partido,
														categoria,
														competicion,
														fase,
														grupo
												))
										)
								)
						)
				).toList();
	}

	private Partit mapPartit(
			Partido partido,
        	Categoria categoria,
        	Competicion competicion,
        	Fase fase,
        	Grupo grupo) {
		boolean ambResultat = !"0".equals(partido.getResultadoLocal()) || !"0".equals(partido.getResultadoVisitante());
		return new Partit(
				partido.getId(),
				partido.getClubLocalId(),
				partido.getClubVisitanteId(),
				partido.getEquipoLocal(),
				false,
				partido.getEquipoVisitante(),
				false,
				parseDataHora(partido.getFecha(), partido.getHora()),
				ambResultat ? partido.getResultadoLocal() : null,
				ambResultat ? partido.getResultadoVisitante() : null,
				partido.getCampo(),
				partido.getCampoDireccion(),
				partido.getMunicipio(),
				partido.getArbitro1(),
				partido.getComentario(),
				partido.getTorneoId(),
				categoria.getNombre(),
				competicion.getNombre(),
				fase.getNombre(),
				grupo.getId(),
				grupo.getNombre(),
				grupo.getTipoTorneoId(),
				grupo.getTipoTorneoNombre());
	}

	private LocalDateTime parseDataHora(String data, String hora) {
		return LocalDateTime.parse(data + " " + hora, DATE_TIME_FORMATTER);
	}

	private String getPartitsUrl(String op) {
		return PARTITS_URL.replace("{{op}}", op);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PartitsResponse {
		private List<Categoria> categorias;
	}

}
