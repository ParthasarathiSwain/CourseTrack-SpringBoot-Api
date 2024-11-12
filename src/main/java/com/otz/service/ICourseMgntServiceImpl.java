package com.otz.service;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.otz.entity.CourseDetails;
import com.otz.model.SearchResults;
import com.otz.model.Searchlnputs;
import com.otz.repo.ICourseDetailsRepo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class ICourseMgntServiceImpl implements ICourseMgmtService{
	@Autowired
	private ICourseDetailsRepo courseRepo;
	@Override
	public Set<String> showAllCourseCategories() {
		return  courseRepo.getUniqueCourseCategory();

	}

	@Override
	public Set<String> showAllTrainingModes() {
		return courseRepo.getUniqueTrainingMode();
	}

	@Override
	public Set<String> showAllFaculties() {
		return courseRepo.getUniqueFacultyName();
	}

	@Override
	public List<SearchResults> showCoursesByFilters(Searchlnputs inputs) {
		System.out.println(inputs.toString());
		//get NonNull and non empty String values from the inputs object and prepare Entity
		//obj having that non null data and also place that entity object inside Example obj
		CourseDetails entity=new CourseDetails();
		String category=inputs.getCourseCategory();
		if (StringUtils.hasLength(category)) {
			System.out.println("inside if"+category);
			entity.setCourseCategory(category);
			System.out.println("inside if"+entity.toString());
		}
		String facultyName=inputs.getFacultyName();
		if (StringUtils.hasLength(facultyName)) {
			entity.setFacultyName(facultyName);
		}
		String triningMode=inputs.getTrainingMode();
		if (StringUtils.hasLength(facultyName)) {
			entity.setTrainingMode(triningMode);
		}
		LocalDateTime startDate=inputs.getStartsOn();
		if (ObjectUtils.isEmpty(startDate)) {
			entity.setStartDate(startDate);
		}
		Example<CourseDetails> example=Example.of(entity);
		//performing search operation with filters data of the Example Entity obj
		List<CourseDetails> listEntities=courseRepo.findAll(example);
		//converting List<Entity obj> to List<SearchResult obj> 
		List<SearchResults> listResult=new ArrayList();
		listEntities.forEach(course->{
			SearchResults searchResults=new SearchResults();
			BeanUtils.copyProperties(course, searchResults);
			listResult.add(searchResults);
		});
		return listResult;
	}

	@Override
	public void generatePdfReport(Searchlnputs inputs, HttpServletResponse res)throws Exception {
		//get the Search result
		List<SearchResults> listResult=showCoursesByFilters(inputs);
		//create Document obj (OpenPdf)
		Document document=new Document(PageSize.A4);
		//get PDFWriter to wirte the document and response object
		PdfWriter.getInstance(document, res.getOutputStream());
		//open the document
		document.open();
		//Define Font for the paragraph
		Font font=FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);
		font.setColor(Color.red);
		
		//create Paragraph Having content and above font Style
		Paragraph para=new Paragraph("Search Report Courses",font);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		//add Paragraph to document
		document.add(para);
		//Display Search result as the Pdf table
		PdfPTable table=new PdfPTable(10);
		table.setWidthPercentage(90);
		table.setWidths(new float[] {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f});
		table.setSpacingBefore(2.0f);
		
		//prepare heading row in the pdf table
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(Color.GRAY);
		cell.setPadding(2);
		Font cellFont=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		
		cellFont.setColor(Color.BLACK);
		cell.setPhrase(new Phrase("course Id",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("course Name",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Location",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Category",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Faculty Name",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fee",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Admin Contact",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Training Mood",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Start Date",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Course Status",cellFont));
		table.addCell(cell);
		//add data cells to pdftable
		listResult.forEach(result->{
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getLocation());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(String.valueOf(result.getAdminContact()));
			table.addCell(result.getTrainingMode());
			table.addCell(result.getStartDate().toString());
			table.addCell(result.getCourseStatus());
		});
		//add table to document
		document.add(table);
		document.close();
	}

	@Override
	public void generateExcelReport(Searchlnputs inputs, HttpServletResponse res) throws Exception {
		//get the Search result
		List<SearchResults> listResult=showCoursesByFilters(inputs);
		//create Excel HSSFWorkBook 
		HSSFWorkbook workbook=new HSSFWorkbook();
		//create sheet in the workbook
		HSSFSheet sheet1=workbook.createSheet("Course Details");
		HSSFRow headerRow=sheet1.createRow(0);
		headerRow.createCell(0).setCellValue("Course Id");
		headerRow.createCell(1).setCellValue("Course Name");
		headerRow.createCell(2).setCellValue("Location");
		headerRow.createCell(3).setCellValue("Course Category");
		headerRow.createCell(4).setCellValue("Faculty Name");
		headerRow.createCell(5).setCellValue("Fee");
		headerRow.createCell(6).setCellValue("Admin Contact");
		headerRow.createCell(7).setCellValue("Training Mood");
		headerRow.createCell(8).setCellValue("Start Date");
		headerRow.createCell(9).setCellValue("Course Status");
		int i=1;
		for(SearchResults result:listResult){
			HSSFRow dataRow=sheet1.createRow(i);
			dataRow.createCell(0).setCellValue(result.getCourseId());
			dataRow.createCell(1).setCellValue(result.getCourseName());
			dataRow.createCell(2).setCellValue(result.getLocation());
			dataRow.createCell(3).setCellValue(result.getCourseCategory());
			dataRow.createCell(4).setCellValue(result.getFacultyName());
			dataRow.createCell(5).setCellValue(result.getFee());
			dataRow.createCell(6).setCellValue(result.getAdminContact());
			dataRow.createCell(7).setCellValue(result.getTrainingMode());
			dataRow.createCell(8).setCellValue(result.getStartDate());
			dataRow.createCell(9).setCellValue(result.getCourseStatus());
			i++;
		}
		ServletOutputStream outputStream=res.getOutputStream();
		workbook.write(outputStream);
		outputStream.close();
		workbook.close();
	}

}
