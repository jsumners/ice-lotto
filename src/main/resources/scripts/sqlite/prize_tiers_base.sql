select
  a.id, a.drawn, a.draw_result,

  bb.id as 'game_item.id', bb.description as 'game_item.description',
  bb.image_url as 'game_item.image_url', bb.min_level as 'game_item.min_level',
  bb.name as 'game_item.name', bb.rarity as 'game_item.rarity',

  cc.id as 'game_item.id', cc.description as 'game_item.description',
  cc.image_url as 'game_item.image_url', cc.min_level as 'game_item.min_level',
  cc.name as 'game_item.name', cc.rarity as 'game_item.rarity',

  dd.id as 'game_item.id', dd.description as 'game_item.description',
  dd.image_url as 'game_item.image_url', dd.min_level as 'game_item.min_level',
  dd.name as 'game_item.name', dd.rarity as 'game_item.rarity',

  ee.id as 'game_item.id', ee.description as 'game_item.description',
  ee.image_url as 'game_item.image_url', ee.min_level as 'game_item.min_level',
  ee.name as 'game_item.name', ee.rarity as 'game_item.rarity',

  ff.id as 'game_item.id', ff.description as 'game_item.description',
  ff.image_url as 'game_item.image_url', ff.min_level as 'game_item.min_level',
  ff.name as 'game_item.name', ff.rarity as 'game_item.rarity',

  gg.id as 'game_item.id', gg.description as 'game_item.description',
  gg.image_url as 'game_item.image_url', gg.min_level as 'game_item.min_level',
  gg.name as 'game_item.name', gg.rarity as 'game_item.rarity',

  hh.id as 'game_item.id', hh.description as 'game_item.description',
  hh.image_url as 'game_item.image_url', hh.min_level as 'game_item.min_level',
  hh.name as 'game_item.name', hh.rarity as 'game_item.rarity',

  ii.id as 'game_item.id', ii.description as 'game_item.description',
  ii.image_url as 'game_item.image_url', ii.min_level as 'game_item.min_level',
  ii.name as 'game_item.name', ii.rarity as 'game_item.rarity',

  jj.id as 'game_item.id', jj.description as 'game_item.description',
  jj.image_url as 'game_item.image_url', jj.min_level as 'game_item.min_level',
  jj.name as 'game_item.name', jj.rarity as 'game_item.rarity',

  kk.id as 'game_item.id', kk.description as 'game_item.description',
  kk.image_url as 'game_item.image_url', kk.min_level as 'game_item.min_level',
  kk.name as 'game_item.name', kk.rarity as 'game_item.rarity'
from prize_tiers a

left join prize_items b
  on b.id = a.item1
left join game_items bb
  on bb.id = b.game_item

left join prize_items c
  on c.id = a.item2
left join game_items cc
  on cc.id = c.game_item

left join prize_items d
  on d.id = a.item3
left join game_items dd
  on dd.id = d.game_item

left join prize_items e
  on e.id = a.item4
left join game_items ee
  on ee.id = e.game_item

left join prize_items f
  on f.id = a.item5
left join game_items ff
  on ff.id = f.game_item

left join prize_items g
  on g.id = a.item6
left join game_items gg
  on gg.id = g.game_item

left join prize_items h
  on h.id = a.item7
left join game_items hh
  on hh.id = h.game_item

left join prize_items i
  on i.id = a.item8
left join game_items ii
  on ii.id = i.game_item

left join prize_items j
  on j.id = a.item9
left join game_items jj
  on jj.id = j.game_item

left join prize_items k
  on k.id = a.item10
left join game_items kk
  on kk.id = k.game_item

