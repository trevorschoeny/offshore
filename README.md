# Offshore

Offshore makes boats easier to live with. Vanilla boats are fine, but they have a pile of small paper cuts. You cannot get a mob out without breaking the boat. Your held item slides out of view when you row. A one-block step stops you dead, a horse will not fit, you cannot tell how close the hull is to breaking, and the boat slides off when you hop out. Offshore smooths those edges without changing what a boat is.

## Features

Let a mob out. Shift-right-click a mob sitting in a boat and it climbs out beside the boat. Other players are never ejected. Aim at the mob, not the hull. A chest boat's hull still opens its chest on shift-click, the way it always has.

See what you are holding. While rowing, your held item stays in view in first person instead of sliding out of frame. This is visual only. Rowing still blocks attacking and using items, as in vanilla.

Step up ledges. A boat moving into a one-block rise (a slab, a stair, a block edge, the shore) steps up instead of stopping. It works from land, from ice, and from the water.

Ferry a horse. Lead a horse, donkey, mule, camel or llama into a boat and it rides. The animal takes both seats, so tow the boat with a lead rather than rowing it.

Watch the hull. A bar across the top of the hotbar shows how much of the boat is left while you ride. It is off by default. Switch it on in the config if you want it.

Stop where you leave it. When you get out, the boat stops instead of sliding on.

Each feature has its own switch under Mod Menu, Offshore.

## Install

Fabric API and MenuKit are required. Install Offshore on both client and server. A single-player world needs nothing more.

## Compatibility

Minecraft 26.2, Fabric. Universal mod. On a dedicated server the server's config decides whether the boat-side features are on. The held-item view and the hull bar are each player's own setting. Offshore hooks vanilla with narrow mixins, so other boat mods keep running. Boat Item View does the same held-item fix, and running both is fine.

## License

MIT.
