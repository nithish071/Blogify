package com.nithish.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nithish.blog.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	
	private int id;
	
	@NotEmpty
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String name;
	
	@Email(message = "Email address is not valid")
	private String email;

	@Getter(onMethod_=@JsonIgnore)
	@NotEmpty
	@Size(min = 3, max = 10, message="Password must be min of 3 chars and max of 10 chars")
	private String password;
	
	@NotEmpty
	private String about;

	@Getter(onMethod_=@JsonIgnore)
	private Set<RoleDto> roles = new HashSet<>();
}
