package se.lexicon.g46todoapi.converter;

import org.springframework.stereotype.Component;
import se.lexicon.g46todoapi.domain.dto.RoleDTOView;
import se.lexicon.g46todoapi.domain.entity.Role;

@Component
public class RoleConverterImpl implements RoleConverter {

  @Override
  public RoleDTOView toRoleDTOView(Role entity) {
    return new RoleDTOView(entity.getId(), entity.getName());
  }

  @Override
  public Role toRoleEntity(RoleDTOView dtoView) {
    return new Role(dtoView.getId(), dtoView.getName());
  }
}
