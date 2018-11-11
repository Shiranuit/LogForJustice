# LogForJustice
**A Minecraft Mod for managing server**

## Logs
- Block Placed, Breaked, Interacted, Item Used
- Players publics message
- Players privates messages
- Players Commands


## Commands
 ### Player Commands
- tpa
- tpaccept
- tpdeny
- home
- sethome
- delhome
- back
- spawn
- plot
 ### Admin Commands
 - tpdim
- lfjsearch
- lfjlogs
- region
- lfjconfig
- tempban
- untempban
- lfjrank
- seen
- messagespy
- commandspy
- vanish
- lfjchunk
- adminhome
- invsee
- feed
- heal
- playerinfo
- bypass
- money


## Rank
- You can create different rank for players with their own power level
- Only commands with power level lower or equal to the player can be executed
- Player cannot deop, kick, ban, tempban, etc on player with higher power level
- Every are overwritten to use the power level system
- You can configure the default rank to be some kind of visitor and make them invulnerable

## Region
- Admins can create regions and apply some flags to it
- Region can be set to be buyable
- **Flag List:** pvp, explosions, place, break, use, interact, interact-entity, itempickup, is_buyable, cost, can_chat, can_use_command, owner, members, spawn_entity, can_morph, can_aquire_morph, force_unmorph

## Plot
- Players can buy some buyable region at some cost
- They can manage their region, add some members, remove some members, transfer owner, allow members to place, break, interact, use item, pickup items in their region

## Money
- Money can be configured, to use whatever item  you want to be some sort of money and apply it a price

## Tools
- Tools are configurable
- Tools are usable by Opped players only
- They help you to create region, find the border of region, get logs of block
