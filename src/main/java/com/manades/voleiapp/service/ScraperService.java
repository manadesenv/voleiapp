package com.manades.voleiapp.service;

import com.manades.voleiapp.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScraperService {

	private static final String CLUBES_URL = "https://www.voleibolib.net/JSON/get_clubes.asp";
	private static final String LOGO_URL = "https://voleibolib.federatio.com/fichas/clubes/{{id}}.jpg";
	private static final String LOGO_MINI_URL = "https://voleibolib.federatio.com/fichas/clubes/{{id}}mini.jpg";
	private static final String COMPETICIONES_URL = "https://www.voleibolib.net/JSON/get_Menu_Competiciones.asp?temp={{temp}}";
	private static final String CLASIFICACION_URL = "https://www.voleibolib.net/JSON/get_clasificacion.asp?id={{id}}";
	private static final String RESULTADOS_URL = "https://www.voleibolib.net/JSON/get_resultados.asp?id={{id}}&jor={{jor}}";
	private static final String LLIGA_TEXT = "Liga Regular";

	public List<Club> scrapClubs() throws IOException {
		List<Club> resposta;
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream in = new URL(CLUBES_URL).openStream()) {
			Map<String, List<ClubFvib>> map = mapper.readValue(in, new TypeReference<>(){});
			resposta = new ArrayList<>(map.get("items")).stream().
					map(cf -> new Club(cf.getId(), cf.getNom())).
					collect(Collectors.toList());
		}
		return resposta;
	}

	public List<Partit> scrapPartits(
			int clubId,
			CompeticioTipus competicioTipus,
			String temporada,
			boolean eliminarAnteriorsUnaSetmana) throws IOException {
		List<Competicio> competicions = new ArrayList<>();
		Document doc = Jsoup.connect(getCompeticionesUrl(getTemporada(temporada))).userAgent("Mozilla/5.0").get();
		Elements divCats = doc.select("div.panel-default");
		for (Element divCat: divCats) {
			String cat = Objects.requireNonNull(divCat.selectFirst("h4>a")).text();
			Elements divSubcats = divCat.select("div.panel-body");
			log.debug("Analitzant categoria {}", cat);
			for (Element divSubcat: divSubcats) {
				String nom = Objects.requireNonNull(divSubcat.selectFirst(">a")).text();
				Element aFase = divSubcat.selectFirst("p.fase>a");
				log.debug("\tAnalitzant competició {}", nom);
				if (aFase != null) {
					if (isMostrarSegonsTipus(aFase.text(), competicioTipus)) {
						String faseHref = aFase.attr("href");
						String id = getClasificacionesIdParam(faseHref);
						log.debug("\t\t{}", aFase.text());
						if (id != null && isClubInCompeticio(clubId, Integer.parseInt(id))) {
							log.debug("\t\t\tAfegint: {}, {}, {}, {}",
									cat,
									nom,
									aFase.text(),
									Integer.parseInt(id));
							competicions.add(
									new Competicio(
											Integer.parseInt(id),
											cat,
											nom,
											getCompeticioTipus(aFase.text()),
											aFase.text(),
											null,
											null));
						}
					}
				} else {
					Element pFase = divSubcat.selectFirst("p.fase");
					if (pFase != null && (isMostrarSegonsTipus(pFase.text(), competicioTipus))) {
						Elements grups = divSubcat.select("ul>li>a");
						for (Element grup : grups) {
							log.debug("\t\tAnalitzant grup {}", grup.text());
							String grupHref = grup.attr("href");
							String id = getClasificacionesIdParam(grupHref);
							if (id != null && isClubInCompeticio(clubId, Integer.parseInt(id))) {
								log.debug("\t\t\tAfegint: {}, {}, {}, {}, {}",
										Integer.parseInt(id),
										cat,
										nom,
										pFase.text(),
										nom);
								competicions.add(
										new Competicio(
												Integer.parseInt(id),
												cat,
												nom,
												getCompeticioTipus(pFase.text()),
												pFase.text(),
												grup.text(),
												null));
							}
						}
					}
				}
			}
		}
		List<Partit> partits = new ArrayList<>();
		for (Competicio competicio: competicions) {
			Partit partitNext = getPartitNext(competicio, clubId);
			if (eliminarAnteriorsUnaSetmana) {
				LocalDateTime faUnaSetmana = LocalDateTime.now().minusWeeks(1);
				if (!partitNext.getData().isBefore(faUnaSetmana)) {
					partits.add(partitNext);
				}
			} else {
				partits.add(partitNext);
			}
		}
		partits.sort(Comparator.comparing(Partit::getData));
		return partits;
	}

	public byte[] logo(int clubId) throws IOException {
		URL imageUrl = new URL(getLogoUrl(Integer.valueOf(clubId).toString()));
		HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		byte[] imageBytes = inputStream.readAllBytes();
		inputStream.close();
		return imageBytes;
	}

	private boolean isClubInCompeticio(int clubId, int competicioId) throws IOException {
		Document doc = Jsoup.connect(
				getResultadosUrl(Integer.valueOf(competicioId).toString(), "")).
				userAgent("Mozilla/5.0").
				get();
		return !getClubsFromImgs(doc, clubId).isEmpty();
	}

	private Partit getPartitNext(Competicio competicio, int clubId) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
		Document doc = Jsoup.connect(
						getResultadosUrl(Integer.valueOf(competicio.getId()).toString(), "")).
				userAgent("Mozilla/5.0").
				get();
		Elements divsPartit = doc.select("div.info_partido");
		Partit partit = null;
		for (Element divPartit: divsPartit) {
			List<Club> clubsPartit = getClubsFromImgs(divPartit, null);
			if (clubsPartit.size() == 2) {
				if (clubsPartit.get(0).getId() == clubId || clubsPartit.get(1).getId() == clubId) {
					Element municipioEl = divPartit.selectFirst("span.municipio");
					String fecha = Objects.requireNonNull(divPartit.selectFirst("span.fecha")).text();
					String resultat = Objects.requireNonNull(divPartit.selectFirst("div.datos_partido span.marcador")).text();
					String marcador = Objects.requireNonNull(divPartit.selectFirst("div.estado_partido>span.marcador")).text();
					Integer[] puntuacions = extreuPuntuacions(resultat);
					partit = new Partit(
							competicio,
							clubsPartit.get(0),
							clubsPartit.get(1),
							LocalDateTime.parse(fecha, formatter),
							municipioEl != null ? municipioEl.text() : null,
							puntuacions[0],
							puntuacions[1],
							!marcador.isEmpty() ? marcador : null);
					break;
				}
			}
		}
		return partit;
	}

	private String getClasificacionesIdParam(String url) {
		Pattern pattern = Pattern.compile("clasificaciones\\?id=(\\d+)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	private List<Club> getClubsFromImgs(Element parent, Integer clubId) {
		Pattern pattern = Pattern.compile("(\\d+)mini");
		List<Club> clubs = new ArrayList<>();
		Elements imgs = parent.select("img");
		for (Element img: imgs) {
			String src = img.attr("src");
			Matcher matcher = pattern.matcher(src);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				if (clubId == null || id == clubId) {
					clubs.add(new Club(
							Integer.parseInt(matcher.group(1)),
							img.attr("title")));
				}
			}
		}
		return clubs;
	}

	private String getTemporada(String temporada) {
		if (!isNumeric(temporada) || temporada.length() != 4) {
			LocalDate avui = LocalDate.now();
			int any = avui.getYear();
			int mes = avui.getMonthValue();
			int anyInici, anyFi;
			if (mes < 9) {
				anyInici = any - 1;
				anyFi = any;
			} else {
				anyInici = any;
				anyFi = any + 1;
			}
			return String.format("%02d", anyInici % 100) + String.format("%02d", anyFi % 100);
		} else {
			return temporada;
		}
	}

	public static String getTemporadaActual() {
		LocalDate avui = LocalDate.now();
		int any = avui.getYear();
		int mes = avui.getMonthValue();

		// Suposem que temporada comença al mes 8 (agost)
		// Si som abans d'agost: la temporada va de (any-1)–any
		if (mes < 8) {
			return (any - 1) + "–" + any;
		}

		// Si som a partir d'agost: temporada va de any–(any+1)
		return any + "–" + (any + 1);
	}

	private boolean isMostrarSegonsTipus(String fase, CompeticioTipus competicioTipus) {
		if (competicioTipus != null) {
			if (CompeticioTipus.LLIGA == competicioTipus) {
				return LLIGA_TEXT.equals(fase);
			} else {
				return !LLIGA_TEXT.equals(fase);
			}
		} else {
			return true;
		}
	}

	private CompeticioTipus getCompeticioTipus(String fase) {
		if (fase != null) {
			return LLIGA_TEXT.equals(fase) ? CompeticioTipus.LLIGA : CompeticioTipus.ALTRES;
		} else {
			return null;
		}
	}

	public static Integer[] extreuPuntuacions(String resultatStr) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(resultatStr);
		Integer[] puntuacions = new Integer[2];
		int index = 0;
		while (matcher.find() && index < 2) {
			puntuacions[index] = Integer.parseInt(matcher.group());
			index++;
		}
		return puntuacions;
	}

	private String getCompeticionesUrl(String temp) {
		return COMPETICIONES_URL.replace("{{temp}}", temp);
	}

	private String getResultadosUrl(String id, String jor) {
		return RESULTADOS_URL.replace("{{id}}", id).replace("{{jor}}", jor);
	}

	private String getLogoUrl(String id) {
		return LOGO_URL.replace("{{id}}", id);
	}

	private boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) return false;
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	@Getter
	@Setter
	private static class ClubResponse {
		private List<Club> items;
	}

}
