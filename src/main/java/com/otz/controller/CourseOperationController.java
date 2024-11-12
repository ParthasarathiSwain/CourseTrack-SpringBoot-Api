package com.otz.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otz.model.SearchResults;
import com.otz.model.Searchlnputs;
import com.otz.service.ICourseMgmtService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/reporting/api")
public class CourseOperationController {
	@Autowired
	private ICourseMgmtService courseService;

	@GetMapping("/courses")
	public ResponseEntity<?> fatchCourseCategory(){
		try {
			//use service
			Set<String> courseInfo=courseService.showAllCourseCategories();
			return new ResponseEntity<Set<String>>(courseInfo,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/trainin-mode")
	public ResponseEntity<?> fatchTrainingMode(){
		try {
			//use service
			Set<String> trainingMode=courseService.showAllTrainingModes();
			return new ResponseEntity<Set<String>>(trainingMode,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/faculties")
	public ResponseEntity<?> fatchFacultyName(){
		try {
			//use service
			Set<String> facultyName=courseService.showAllFaculties();
			return new ResponseEntity<Set<String>>(facultyName,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/search")
	public ResponseEntity<?> fatchCourseByFilter(@RequestBody Searchlnputs inputs){
		try {
			//use service
			List<SearchResults> list=courseService.showCoursesByFilters(inputs);
			return new ResponseEntity<List<SearchResults> >(list,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/pdf-report")
	public void showPdfReport(@RequestBody Searchlnputs inputs,HttpServletResponse res){
		try {
			//set the response content type
			res.setContentType("application/pdf");
			//set the content disposition header to response content going to downloable file
			res.setHeader("Content-Disposition", "attachment:fileName=Course.pdf");
			//use service
			 courseService.generatePdfReport(inputs,res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@PostMapping("/excel-report")
	public void showExcelReport(@RequestBody Searchlnputs inputs,HttpServletResponse res){
		try {
			//set the response content type
			res.setContentType("application/vnd.ms-excel");
			//set the content disposition header to response content going to downloable file
			res.setHeader("Content-Disposition", "attachment:fileName=Course.xls");
			//use service
			 courseService.generateExcelReport(inputs,res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
