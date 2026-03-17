package com.manades.voleiapp.controller;

import com.manades.voleiapp.model.Partit;
import com.manades.voleiapp.service.PartitsService;
import lombok.RequiredArgsConstructor;
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

	@GetMapping("/partits")
	public List<Partit> partits(
			@RequestParam int clubId,
			@RequestParam Boolean resultats) throws IOException {
		return partitsService.findPartits(clubId, resultats);
	}

}
