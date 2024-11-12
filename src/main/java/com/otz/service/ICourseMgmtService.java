package com.otz.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.otz.model.SearchResults;
import com.otz.model.Searchlnputs;

import jakarta.servlet.http.HttpServletResponse;

public interface ICourseMgmtService {
	public  Set<String> showAllCourseCategories();
	public	Set<String> showAllTrainingModes();
	public	Set<String> showAllFaculties();
	public	List<SearchResults> showCoursesByFilters(Searchlnputs inputs);
	public	void generatePdfReport(Searchlnputs inputs,HttpServletResponse res) throws Exception;
	public	void generateExcelReport(Searchlnputs inputs,HttpServletResponse res)throws Exception;
}
