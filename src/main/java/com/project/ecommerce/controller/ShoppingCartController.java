package com.project.ecommerce.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.ECommerceApplication;
import com.project.ecommerce.model.ShoppingCart;
import com.project.ecommerce.model.dto.ShoppingCartDto;
import com.project.ecommerce.service.ShoppingCartService;
import com.project.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

	private final ShoppingCartService cartService ;
	
	private final UserService userService;
	
	private static Logger log =  LoggerFactory.getLogger(ECommerceApplication.class);

	//Metodo para generar un ID de carrito de compra a un cliente
	@Operation(description = "Metodo  utilizado para generar un ID de carrito de compra a un cliente", summary = "crear Carrito de compra por cliente")
	@PostMapping("/createcart")
	public ResponseEntity<?> createCart(@RequestBody ShoppingCart user ){
		
		
		log.info("dni en controller es : {}", user);
		
		try {
			boolean findDni = userService.existsUser(user.getUser().getDni());

			boolean findCart = cartService.validationId(user.getUser().getDni());
			

			if(findDni == true && findCart == true) {
				return ResponseEntity.badRequest().body("El usuario ya tiene asociado un id de carrito ");
			}else{
				ShoppingCartDto create = cartService.createSCart(user);
				return ResponseEntity.ok(create);
			}
			
		}catch(Exception e){
			return ResponseEntity.badRequest().body("/Error: " + e.getMessage());
		}
		
	}
		
	// Metodo para obtener los datos del cliente segun el ID del carrito
	@Operation(description = "Metodo  utilizado para obtener los datos del cliente segun el ID del carrito", summary = "Obtener cliente segun id de carrito de compras")
	@GetMapping("/getcart/{id}")
	public ResponseEntity<?> getCart(@PathVariable(name = "id") Integer idCart){
		
		try {
			ShoppingCartDto findSCart = cartService.findCart(idCart);
			log.info("shoppingcart {}", findSCart);
			return ResponseEntity.ok(findSCart);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
			
		
	}		

}
