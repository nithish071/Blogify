package com.nithish.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nithish.blog.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int id;
	
	@NotNull
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String name;

	@NotNull
	@Email(message = "Email address is not valid")
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotEmpty
	@Size(min = 3, max = 10, message="Password must be min of 3 chars and max of 10 chars")
	private String password;

	@NotNull
	private String about;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<RoleDto> roles = new HashSet<>();
}
