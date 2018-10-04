package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.Page;
import com.evola.edt.model.PageComment;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.PageCommentDTO;

@Named
public class PageCommentDTOTransformer implements
		DTOTransformer<PageCommentDTO, PageComment> {

	@Inject
	private PageDTOTransformer pageDTOTransformer;

	@Inject
	private UserDTOTransformer userDTOTransformer;

	@Override
	public PageCommentDTO transformToDTO(PageComment entity,
			String... fetchFields) {
		if (entity == null) {
			return null;
		}

		PageCommentDTO dto = new PageCommentDTO();
		dto.setComment(entity.getComment());
		dto.setDate(entity.getDate());
		dto.setId(entity.getId());

		if (Arrays.asList(fetchFields).contains("author")) {
			dto.setAuthor(userDTOTransformer.transformToDTO(entity.getAuthor()));
		}

		if (Arrays.asList(fetchFields).contains("page")) {
			dto.setPage(pageDTOTransformer.transformToDTO(entity.getPage()));
		}

		return dto;
	}

	@Override
	public PageComment transformToEntity(PageCommentDTO dto) {
		if (dto == null) {
			return null;
		}

		Page page = pageDTOTransformer.transformToEntity(dto.getPage());

		User author = userDTOTransformer.transformToEntity(dto.getAuthor());

		PageComment pageComment = new PageComment();
		pageComment.setAuthor(author);
		pageComment.setId(dto.getId());
		pageComment.setDate(dto.getDate());
		pageComment.setComment(dto.getComment());
		pageComment.setPage(page);

		return pageComment;
	}
}
