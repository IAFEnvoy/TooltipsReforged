# Changelog

## 2.0-beta.2

### Fixes

- Fixed a crash on 1.20.1 when hovering items without block entity data (for example anywhere in the creative
  inventory). `BlockItem.getBlockEntityData` is nullable on 1.20.1, but the null checks had been replaced with
  `CompoundTag.isEmpty()` checks that only hold on 1.21. Spawn eggs and painting items could hit the same crash.
  ([#2](https://github.com/IAFEnvoy/TooltipsReforged/issues/2))
- Container previews work on 1.21.1 again: container contents are read from the `minecraft:container` data
  component instead of the legacy `Items` block entity tag. ([#1](https://github.com/IAFEnvoy/TooltipsReforged/issues/1))
- Enchantment descriptions resolve their translation key from the enchantment id on 1.21.1 again, so
  `<enchantment description id>.desc` entries (for example from Enchantment Descriptions) are displayed as
  intended.
- Container loot tables in the debug tooltip are read from the `minecraft:container_loot` data component on
  1.21.1 instead of the removed block entity tag.
- Turning off the effects tooltip now keeps the vanilla effect lines, and turning off the container preview
  image keeps the vanilla container contents. Mods that append to those vanilla tooltips work again once the
  corresponding feature is disabled.

### Features

- Banner items now render a banner preview composed of their base color and pattern layers. Previously only
  banner pattern items had a preview.
