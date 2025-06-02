package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.CategoryDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CategoryService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/category/main")
	public ResponseEntity<ApiResponse<List<CategoryDto>>> getMainCategories(){
		List<CategoryDto> MainCategoryDto=categoryService.findMainCategories();
		System.out.println(MainCategoryDto);
		return ResponseEntity.ok(ApiResponse.success("主分類: ", MainCategoryDto));
	}
	@GetMapping("/category/sub/{parentId}")
	public ResponseEntity<ApiResponse<List<CategoryDto>>> getSubCategories(@PathVariable Integer parentId){
		List<CategoryDto>SubCategoryDto=categoryService.findSubCategories(parentId);
		if(SubCategoryDto==null) {
			return ResponseEntity.badRequest().body(ApiResponse.success("查詢失敗，尚無主分類，主分類ID為:1，5，9", null));
		}
		return ResponseEntity.ok(ApiResponse.success("主分類: ", SubCategoryDto));
	}
	@GetMapping("/categories")
	public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories(){
		List<CategoryDto>CategoriesDto=categoryService.findAllCategories();
		if(CategoriesDto==null) {
			return ResponseEntity.badRequest().body(ApiResponse.success("查詢失敗，尚無主分類，主分類ID為:1，5，9", null));
		}
		return ResponseEntity.ok(ApiResponse.success("主分類: ", CategoriesDto));
	}
}
