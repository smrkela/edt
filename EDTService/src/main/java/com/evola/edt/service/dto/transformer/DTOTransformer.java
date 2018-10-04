package com.evola.edt.service.dto.transformer;

/**
 * @author Nikola 09.04.2013.
 * 
 * @param <DTO>
 * @param <ENTITY>
 */
public interface DTOTransformer<DTO, ENTITY> {
	/**
	 * @author Nikola 09.04.2013.
	 * @param entity
	 * @return
	 */
	public DTO transformToDTO(ENTITY entity, String... fetchFields);

	/**
	 * @author Nikola 09.04.2013.
	 * @param dto
	 * @return
	 */
	public ENTITY transformToEntity(DTO dto);
}
