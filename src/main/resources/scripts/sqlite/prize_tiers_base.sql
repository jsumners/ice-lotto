select
  a.id, a.drawn, a.draw_result,

  b.id as 'item1.prize_id', b.count as 'item1.count',
  bb.id as 'item1.id', bb.description as 'item1.description',
  bb.image_url as 'item1.image_url', bb.min_level as 'item1.min_level',
  bb.name as 'item1.name', bb.rarity as 'item1.rarity',

  c.id as 'item2.prize_id', c.count as 'item2.count',
  cc.id as 'item2.id', cc.description as 'item2.description',
  cc.image_url as 'item2.image_url', cc.min_level as 'item2.min_level',
  cc.name as 'item2.name', cc.rarity as 'item2.rarity',

  d.id as 'item3.prize_id', d.count as 'item3.count',
  dd.id as 'item3.id', dd.description as 'item3.description',
  dd.image_url as 'item3.image_url', dd.min_level as 'item3.min_level',
  dd.name as 'item3.name', dd.rarity as 'item3.rarity',

  e.id as 'item4.prize_id', e.count as 'item4.count',
  ee.id as 'item4.id', ee.description as 'item4.description',
  ee.image_url as 'item4.image_url', ee.min_level as 'item4.min_level',
  ee.name as 'item4.name', ee.rarity as 'item4.rarity',

  f.id as 'item5.prize_id', f.count as 'item5.count',
  ff.id as 'item5.id', ff.description as 'item5.description',
  ff.image_url as 'item5.image_url', ff.min_level as 'item5.min_level',
  ff.name as 'item5.name', ff.rarity as 'item5.rarity',

  g.id as 'item6.prize_id', g.count as 'item6.count',
  gg.id as 'item6.id', gg.description as 'item6.description',
  gg.image_url as 'item6.image_url', gg.min_level as 'item6.min_level',
  gg.name as 'item6.name', gg.rarity as 'item6.rarity',

  h.id as 'item7.prize_id', h.count as 'item7.count',
  hh.id as 'item7.id', hh.description as 'item7.description',
  hh.image_url as 'item7.image_url', hh.min_level as 'item7.min_level',
  hh.name as 'item7.name', hh.rarity as 'item7.rarity',

  i.id as 'item8.prize_id', i.count as 'item8.count',
  ii.id as 'item8.id', ii.description as 'item8.description',
  ii.image_url as 'item8.image_url', ii.min_level as 'item8.min_level',
  ii.name as 'item8.name', ii.rarity as 'item8.rarity',

  j.id as 'item9.prize_id', j.count as 'item9.count',
  jj.id as 'item9.id', jj.description as 'item9.description',
  jj.image_url as 'item9.image_url', jj.min_level as 'item9.min_level',
  jj.name as 'item9.name', jj.rarity as 'item9.rarity',

  k.id as 'item10.prize_id', k.count as 'item10.count',
  kk.id as 'item10.id', kk.description as 'item10.description',
  kk.image_url as 'item10.image_url', kk.min_level as 'item10.min_level',
  kk.name as 'item10.name', kk.rarity as 'item10.rarity'
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