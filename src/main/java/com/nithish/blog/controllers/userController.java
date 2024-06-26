package com.nithish.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nithish.blog.payloads.ApiResponse;
import com.nithish.blog.payloads.UserDto;
import com.nithish.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class userController {
	
	@Autowired
	private UserService userService;
	
	//POST - create user
	@PostMapping("create")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
		UserDto creteUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(creteUserDto,HttpStatus.CREATED);
	}
	//PUT- update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid
												  @RequestBody UserDto userDto,@PathVariable("userId") Integer uid){
		UserDto updatedUser = this.userService.updateUser(userDto, uid);
		return ResponseEntity.ok(updatedUser);
	}
	//DELETE- delete user  

	// ADMIN only can delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid ){
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Sucessfully",true), HttpStatus.OK);
	}
	// GET - user get
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	// GET - user get
		@GetMapping("/{userId}")
		public ResponseEntity<UserDto> getSingleUsers(@PathVariable Integer userId){
			return ResponseEntity.ok(this.userService.getUserById(userId));
		}
	
}
