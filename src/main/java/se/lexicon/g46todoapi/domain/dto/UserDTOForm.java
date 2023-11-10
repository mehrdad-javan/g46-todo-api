package se.lexicon.g46todoapi.domain.dto;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTOForm {
  // todo : add validation annotations if needed

  private String email;
  private String password;
  private Set<RoleDTOForm> roles;
}
