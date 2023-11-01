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
import se.lexicon.g46todoapi.util.CustomPasswordEncoder;

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
  private final CustomPasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
          UserRepository userRepository,
          RoleRepository roleRepository,
          CustomPasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  public UserDTOView register(UserDTOForm userDTOForm) {
    // Check params
    if (userDTOForm == null) throw new IllegalArgumentException("user form is null.");
    // Check if email exist
    boolean isExistEmail = userRepository.existsByEmail(userDTOForm.getEmail());
    if (isExistEmail) throw new DataDuplicateException("Email is already exist.");

    // Retrieve and validate roles
    Set<Role> roleList = userDTOForm.getRoles()
            .stream()
            .map(
                    roleDTOForm -> roleRepository.findById(roleDTOForm.getId())
                            .orElseThrow(() -> new DataNotFoundException("Role is not valid.")))
            .collect(Collectors.toSet());

    // Convert dto to entity
    User user = new User(userDTOForm.getEmail(), passwordEncoder.encode(userDTOForm.getPassword()));
    user.setRoles(roleList);

    // Create a new User entity
    User savedUser = userRepository.save(user);

    // Convert saved user to dto with converting roles
    /*UserDTOView dtoView = new UserDTOView();
    dtoView.setEmail(savedUser.getEmail());

    Set<RoleDTOView> roleDTOViewList = new HashSet<>();
    for (Role role: user.getRoles()){
      RoleDTOView roleDtoView = new RoleDTOView();
      roleDtoView.setId(role.getId());
      roleDtoView.setName(role.getName());
      roleDTOViewList.add(roleDtoView);
    }
    dtoView.setRoles(roleDTOViewList);*/

    Set<RoleDTOView> roleDTOViews = savedUser.getRoles()
            .stream()
            .map(
                    role -> RoleDTOView.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .build())
            .collect(Collectors.toSet());

    //& return the result
    return UserDTOView.builder()
            .email(savedUser.getEmail())
            .roles(roleDTOViews)
            .build();
  }

  @Override
  public UserDTOView getByEmail(String email) {
    User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException("Email does not exist."));

    Set<RoleDTOView> roleDTOViews = user.getRoles()
            .stream()
            .map(
                    role -> RoleDTOView.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .build())
            .collect(Collectors.toSet());

    return UserDTOView.builder()
            .email(user.getEmail())
            .roles(roleDTOViews)
            .build();
  }

  @Override
  public void disableByEmail(String email) {
    isEmailTaken(email);
    userRepository.updateExpiredByEmail(email, true);
  }

  @Override
  public void enableByEmail(String email) {
    isEmailTaken(email);
    userRepository.updateExpiredByEmail(email, false);

  }

  private void isEmailTaken(String email){
    if (!userRepository.existsByEmail(email))
      throw new DataNotFoundException("Email does not exist.");
  }

}
