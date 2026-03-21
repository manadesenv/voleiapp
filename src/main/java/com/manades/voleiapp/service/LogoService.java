package com.manades.voleiapp.service;

import com.manades.voleiapp.model.DownloadableFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoService {

	private static final String LOGO_URL = "https://voleibolib.federatio.com/fichas/clubes/{{id}}.jpg";

	public DownloadableFile logo(int clubId) throws IOException {
		URL imageUrl = new URL(getLogoUrl(Integer.valueOf(clubId).toString()));
		HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		byte[] imageBytes = inputStream.readAllBytes();
		inputStream.close();
		return new DownloadableFile(
				new ByteArrayResource(imageBytes),
				"logo_" + clubId + ".jpg",
				"image/jpeg");
	}

	private String getLogoUrl(String id) {
		return LOGO_URL.replace("{{id}}", id);
	}

}
