package com.manades.voleiapp.controller;

import com.manades.voleiapp.model.Club;
import com.manades.voleiapp.model.CompeticioTipus;
import com.manades.voleiapp.model.Partit;
import com.manades.voleiapp.service.ScraperService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScraperController {

	private final ScraperService scraperService;

	public ScraperController(ScraperService scraperService) {
		this.scraperService = scraperService;
	}

	@GetMapping("/clubs")
	public List<Club> clubs() throws IOException {
		return scraperService.scrapClubs();
	}

	@GetMapping("/partits")
	public List<Partit> partits(
			@RequestParam int clubId,
			@RequestParam(required = false) CompeticioTipus tipus,
			@RequestParam(required = false) String temporada) throws IOException {
		return scraperService.scrapPartits(clubId, tipus, temporada, true);
	}

	@GetMapping("/logo")
	public ResponseEntity<byte[]> logo(@RequestParam int clubId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(
				scraperService.logo(clubId),
				headers,
				HttpStatus.OK);
	}

}
