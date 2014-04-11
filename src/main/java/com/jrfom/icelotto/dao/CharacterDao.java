package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.CharacterNotFoundException;
import com.jrfom.icelotto.model.Character;

public interface CharacterDao {
  Character create(final Character character);
  void delete(Long characterId) throws CharacterNotFoundException;
  List<Character> findAll();
  Character findById(Long id);
  Character save(Character character);
}
