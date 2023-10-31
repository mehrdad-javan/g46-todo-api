package se.lexicon.g46todoapi.service;

import se.lexicon.g46todoapi.domain.dto.RoleDTOView;

import java.util.List;

public interface RoleService {

  List<RoleDTOView> getAll();

}
