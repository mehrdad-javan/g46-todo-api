package se.lexicon.g46todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.g46todoapi.domain.dto.RoleDTOView;
import se.lexicon.g46todoapi.domain.dto.UserDTOForm;
import se.lexicon.g46todoapi.domain.dto.UserDTOView;
import se.lexicon.g46todoapi.domain.entity.Role;
import se.lexicon.g46todoapi.domain.entity.User;
import se.lexicon.g46todoapi.exception.DataDuplicateException;
import se.lexicon.g46todoapi.exception.DataNotFoundException;
import se.lexicon.g46todoapi.repository.RoleRepository;
import se.lexicon.g46todoapi.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  // todo: add required dependencies...

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }


  @Override
  public UserDTOView register(UserDTOForm userDTOForm) {
    // check params
    if (userDTOForm == null) throw new IllegalArgumentException("user form is null.");
    // check if email exist
    boolean isExistEmail = userRepository.existsByEmail(userDTOForm.getEmail());
    if (isExistEmail) throw new DataDuplicateException("Email is already exist.");

    // Retrieve and validate roles
    Set<Role> roleList = userDTOForm.getRoles()
            .stream()
            .map(
                    roleDTOForm -> roleRepository.findById(roleDTOForm.getId())
                            .orElseThrow(() -> new DataNotFoundException("Role is not valid.")))
            .collect(Collectors.toSet());

    // todo: make password to hash

    // Convert dto to entity
    User user = new User(userDTOForm.getEmail(), userDTOForm.getPassword());
    user.setRoles(roleList);

    // Create a new User entity
    User savedUser = userRepository.save(user);

    // convert saved user to dto with converting roles
    UserDTOView dtoView = new UserDTOView();
    dtoView.setEmail(savedUser.getEmail());

    // todo: try to find a way to convert dto to entity and ...
    Set<RoleDTOView> roleDTOViewList = new HashSet<>();
    for (Role role: user.getRoles()){
      RoleDTOView roleDtoView = new RoleDTOView();
      roleDtoView.setId(role.getId());
      roleDtoView.setName(role.getName());
      roleDTOViewList.add(roleDtoView);
    }
    dtoView.setRoles(roleDTOViewList);

    //& return the result
    return dtoView;
  }

  @Override
  public UserDTOView getByEmail(String email) {
    // todo: implement the method
    return null;
  }

  @Override
  public void disableByEmail(String email) {
    // todo: implement the method

  }

  @Override
  public void enableByEmail(String email) {
    // todo: implement the method

  }
}
