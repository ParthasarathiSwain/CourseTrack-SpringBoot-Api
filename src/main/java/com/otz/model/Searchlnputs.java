package com.otz.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Searchlnputs{
	private String courseCategory;
	private String trainingMode;
	private String facultyName;
	private LocalDateTime startsOn;
}
