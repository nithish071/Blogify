package com.nithish.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer postId;
	
	private String title;
	
	private String content;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Date addedDate;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String imageName;

	@Hidden
	private CategoryDto category;

    @Hidden
	private UserDto user;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Set<CommentDto> comments = new HashSet<>();

}
