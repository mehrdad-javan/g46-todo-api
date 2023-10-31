package se.lexicon.g46todoapi.converter;


import se.lexicon.g46todoapi.domain.dto.RoleDTOView;
import se.lexicon.g46todoapi.domain.entity.Role;

public interface RoleConverter {

  RoleDTOView toRoleDTOView(Role entity);

  Role toRoleEntity(RoleDTOView dtoView);
}
