package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.sqlite.CharacterRepository;
import com.jrfom.icelotto.exception.CharacterNotFoundException;
import com.jrfom.icelotto.model.Character;
import com.jrfom.icelotto.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterRepositoryService implements CharacterService {
  private static final Logger log = LoggerFactory.getLogger(CharacterRepositoryService.class);

  @Resource
  private CharacterRepository characterRepository;

  @Override
  @Transactional
  public Optional<Character> create(String name) {
    log.debug("Creating new character with name: `{}`", name);
    Optional<Character> result = Optional.absent();
    Character record = new Character(name);

    try {
      record = this.characterRepository.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Could not create new character: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  @Transactional(rollbackFor = CharacterNotFoundException.class)
  public void delete(Long characterId) throws CharacterNotFoundException {
    log.debug("Deleting character with id: `{}`", characterId);
    Character deleted = this.characterRepository.findById(characterId);

    if (deleted == null) {
      log.debug("Could not find character with id: `{}`", characterId);
      throw new CharacterNotFoundException();
    } else {
      this.characterRepository.delete(characterId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Character> findAll() {
    log.debug("Finding all characters");
    return this.characterRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Character> findById(Long id) {
    log.debug("Finding character with id: `{}`", id);
    Optional<Character> result = Optional.absent();
    Character character = this.characterRepository.findById(id);

    if (character != null) {
      result = Optional.of(character);
    }

    return result;
  }
}