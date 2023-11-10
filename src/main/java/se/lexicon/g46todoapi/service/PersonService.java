package se.lexicon.g46todoapi.service;


import se.lexicon.g46todoapi.domain.dto.PersonDTOForm;
import se.lexicon.g46todoapi.domain.dto.PersonDTOView;

import java.util.List;

public interface PersonService {
    PersonDTOView create(PersonDTOForm personDtoForm);
    PersonDTOView findById(Long id);
    List<PersonDTOView> findAll();
    PersonDTOView update(PersonDTOForm personDtoForm);
    void delete(Long id);
}
