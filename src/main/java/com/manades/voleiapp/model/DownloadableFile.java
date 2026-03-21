package com.manades.voleiapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
@AllArgsConstructor
public class DownloadableFile {
	private Resource resource;
	private String fileName;
	private String contentType;
}
