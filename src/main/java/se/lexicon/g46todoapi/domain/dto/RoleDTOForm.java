package se.lexicon.g46todoapi.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDTOForm {
  // todo : add validation annotations if needed

  private Long id;
  private String name;
}
