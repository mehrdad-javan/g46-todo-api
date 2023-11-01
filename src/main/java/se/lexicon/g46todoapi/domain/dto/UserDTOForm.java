package se.lexicon.g46todoapi.domain.dto;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTOForm {
  private String email;
  private String password;
  private Set<RoleDTOForm> roles;
}
