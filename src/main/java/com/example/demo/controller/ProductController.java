package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.ProductImageNotFoundException;
import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ProductService;
import com.example.demo.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
	@Autowired
	private ProductService productService;

    ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }
	@PostMapping("/product/add")
	public ResponseEntity<ApiResponse<Void>>addProduct(@RequestBody ProductDto productDto,HttpSession session){
		try {
			StoreDto storeInfo=(StoreDto)session.getAttribute("storeInfo");
			productService.addProduct(storeInfo.getId(), productDto.getTitle(), productDto.getBrand(), productDto.getCategoryId(), productDto.getPrice(), productDto.getStock(), productDto.getDescription());
			return ResponseEntity.ok(ApiResponse.success("商品新增成功", null));
		}catch (NumberFormatException e) {
	        return ResponseEntity.badRequest()
	                .body(ApiResponse.error(400, "庫存數量格式錯誤"));
	        } catch (StoreNotFoundException | CategoryNotFoundException e) {
	            return ResponseEntity.badRequest()
	                .body(ApiResponse.error(400, e.getMessage()));
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError()
	                .body(ApiResponse.error(500, "伺服器錯誤"));
	        }
		
	}
	@PutMapping("/product/edit/{id}")
	public ResponseEntity<ApiResponse<Void>>editProduct(@PathVariable Long id,@RequestBody ProductDto productDto){
		try {
			productService.updateProduct(id, productDto.getTitle(), productDto.getBrand(), productDto.getCategoryId(), productDto.getPrice(), productDto.getStock(), productDto.getDescription());
			return ResponseEntity.ok(ApiResponse.success("商品修改成功", null));
			
		}catch (NumberFormatException e) {
	        return ResponseEntity.badRequest()
	                .body(ApiResponse.error(400, "庫存數量格式錯誤"));
	        } catch (StoreNotFoundException | CategoryNotFoundException e) {
	            return ResponseEntity.badRequest()
	                .body(ApiResponse.error(400, e.getMessage()));
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError()
	                .body(ApiResponse.error(500, "伺服器錯誤"));
	        }
	}
	@DeleteMapping("/product/delete/{id}")
	public ResponseEntity<ApiResponse<Void>>deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
	}
	
	@GetMapping("/products")
	public ResponseEntity<ApiResponse<List<ProductDto>>>findAllProducts(){
		List<ProductDto>productDtos=productService.findAllProduct();
		if(productDtos==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, "尚無商品請加入商品"));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢商品成功:", productDtos));
	}
	@GetMapping("/products/store/{storeId}")
	public ResponseEntity<ApiResponse<List<ProductDto>>>findProductsByStoreId(@PathVariable Long storeId){
		List<ProductDto>productDtos=productService.findProductsByStoreId(storeId);
		if(productDtos==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, "尚無商品請加入商品"));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢商品成功:", productDtos));
	}
	@PutMapping("/product/active/{id}")
	public ResponseEntity<ApiResponse<Void>>activeProduct(@PathVariable Long id){
		try {
			productService.isActiveProduct(id);
			return ResponseEntity.ok(ApiResponse.success("上架成功", null));
		} catch (ProductImageNotFoundException e) {
			return ResponseEntity.ok(ApiResponse.success("上架失敗，請先上傳照片再上架", null));
		}
		
	}
	@PutMapping("/product/notactive/{id}")
	public ResponseEntity<ApiResponse<Void>>isNotActiveProduct(@PathVariable Long id){
		try {
			productService.isNotActiveProduct(id);
			return ResponseEntity.ok(ApiResponse.success("下架成功", null));
		} catch (ProductImageNotFoundException e) {
			return ResponseEntity.ok(ApiResponse.success("下架失敗，請先上傳照片再下架", null));
		}
		
	}
}
