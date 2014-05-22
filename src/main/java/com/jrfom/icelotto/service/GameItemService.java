package com.jrfom.icelotto.service;

import java.util.List;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;

public interface GameItemService {
  Optional<GameItem> create(GameItem gameItem);

  /**
   * Create a new {@link com.jrfom.icelotto.model.GameItem} entry in the
   * database with the specified {@code id} and {@code name}.
   *
   *
   * @param id A unique identifier for the item.
   * @param name The display name for the item.
   *
   * @return An instance of {@link com.jrfom.icelotto.model.GameItem} wrapped
   * in an {@link com.google.common.base.Optional} or an empty {@code Optional}
   * if there was an error.
   */
  Optional<GameItem> create(Long id, String name);

  /**
   * Create a new {@link com.jrfom.icelotto.model.GameItem} entry in the
   * database with the specified {@code id}, {@code name}, and
   * {@code description}.
   *
   * @param id A unique identifier for the item.
   * @param name The display name for the item.
   * @param description A string describing the item.
   * @return
   */
  Optional<GameItem> create(Long id, String name, String description);

  /**
   * Delete a {@link com.jrfom.icelotto.model.GameItem} from the database
   * given its identifier. If the item could not be found, an instance of
   * {@link com.jrfom.icelotto.exception.GameItemNotFoundException} will be
   * thrown.
   *
   * @param GameItemId The identifier for the item to delete.
   *
   * @throws com.jrfom.icelotto.exception.GameItemNotFoundException
   */
  void delete(Long GameItemId) throws GameItemNotFoundException;

  /**
   * Get a list of all {@link com.jrfom.icelotto.model.GameItem}s in the
   * database.
   *
   * @return An instance of {@link java.util.List} (empty if none were found).
   */
  List<GameItem> findAll();

  /**
   * Retrieve a {@link com.jrfom.icelotto.model.GameItem} from the database
   * given its identifier.
   *
   * @param id The identifier of the item.
   *
   * @return An instance of {@link com.jrfom.icelotto.model.GameItem} wrapped
   * in an {@link com.google.common.base.Optional} or an empty {@code Optional}.
   */
  Optional<GameItem> findById(Long id);

  /**
   * Save a {@link com.jrfom.icelotto.model.GameItem} to the database.
   *
   * @param gameItem The {@link com.jrfom.icelotto.model.GameItem} to save.
   */
  void save(GameItem gameItem);
}