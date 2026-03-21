package com.manades.voleiapp.controller;

import com.manades.voleiapp.model.DownloadableFile;
import com.manades.voleiapp.model.Partit;
import com.manades.voleiapp.service.LogoService;
import com.manades.voleiapp.service.PartitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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
@RequiredArgsConstructor
public class AppController {

	private final PartitsService partitsService;
	private final LogoService logoService;

	@GetMapping("/partits")
	public List<Partit> partits(
			@RequestParam int clubId,
			@RequestParam Boolean resultats) throws IOException {
		return partitsService.findPartits(clubId, resultats);
	}

	@GetMapping("/logo")
	public ResponseEntity<Resource> logo(@RequestParam int clubId) throws IOException {
		DownloadableFile logoFile = logoService.logo(clubId);
		return ResponseEntity.ok().
				contentType(MediaType.parseMediaType(logoFile.getContentType())).
				body(logoFile.getResource());
	}

}
