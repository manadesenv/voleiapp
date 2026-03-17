package com.manades.voleiapp.service;

import com.manades.voleiapp.helper.PartitsHelper;
import com.manades.voleiapp.model.Partit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartitsService {

	private final PartitsHelper partitsHelper;

	public List<Partit> findPartits(long clubId, boolean resultats) {
		List<Partit> partits = partitsHelper.findPartits(resultats ? 2 : 1).stream().
				filter(p -> isPartitOfClub(p, clubId)).
				sorted(Comparator.comparing(Partit::getData)).
				toList();
		return partits.stream().peek(p -> {
			p.setEquipLocalResaltat(p.getClubLocalId() == clubId);
			p.setEquipVisitantResaltat(p.getClubVisitantId() == clubId);
		}).toList();
	}

	@Scheduled(initialDelay = 0, fixedRate = 60 * 60 * 1000)
	public void refreshCache() {
		List<Integer> ops = List.of(1, 2);
		for (int op: ops) {
			partitsHelper.findPartits(op);
		}
	}

	private boolean isPartitOfClub(Partit partit, long clubId) {
		return partit.getClubLocalId() == clubId || partit.getClubVisitantId() == clubId;
	}

}
