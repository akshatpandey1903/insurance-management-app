package com.aurionpro.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PageResponse<T> {
	private List<T> content;
	private int totalPages;
	private long totalElements;
	private int pageSize;
	private boolean last;
}