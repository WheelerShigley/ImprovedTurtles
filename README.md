# ImprovedTurtles
 
## Change Turtle Drops

<p align="center">
 Options for turtles to drop scute (and option for amount)
</p>

 ![Alt text](/ReadMeImages/Drops.png)
 
<p align="center">
 *Works with Looting*
</p>
 
## Shellmet upgrades
 
<p align="center">
 Options to allow upgradable shells:
</p>

 ![Alt text](/ReadMeImages/Shells.png)
 
 - Allows "Turtle Shell"s to be upgraded to "Diamond Shell"s using a diamond helmet (in Smithing Table)
 - Allows "Turtle Shell"s to be upgraded to "Netherite Shell"s using a netherite helmet
 - Allowed "Diamond Shell"s to be upgraded to "Netherite Shell"s using a netherite ingot

      *All attributs about shells are preserved when upgrading.*

## Humane Automation
The options "return_home_on_grow_up" and "molt_when_return_home" can be used to make it where turtles return to their home to drop off their scute.
This allows for humane farming because the turtles nolonger need to be trapped until adulthood.
Note: if you enable adult turtles dropping useful items, this might still cause players to inhumanely farm turtles.

## Configurable

<p align="center">
 Options are found in config.yml after running.
</p>

```yml
# Change sea_grass to scute (works with looting)
turtles_drop_scute: false
# roll_count = looting_level + 1
scute_roll_maximum: 1

#enable helmet changes
enable_diamond_turtle_helmets: true
#enables both netherite and diamond helmets
enable_netherite_turtle_helmets: true

# Drop scute when growing up
enable_scute_on_grow_up: true
# Minimum amount dropped when loosing scute
minimum_drop_quantity: 1
# Maximum amount dropped when loosing scute
maximum_drop_quantity: 3

# Return home on grow_up
return_home_on_grow_up: false
# Only molt when arriving at home (force enables "Return home on grow_up")
molt_when_return_home: false
```
 
### Usage

 This plugin is make for paper 1.19.4 (#527) and
 is runnable by having the jar file in the /plugins directory within your server directory.
 **There is a compiled jar in the main repository directory**, ./ImprovedTurtles.jar for ease-of-addition.
 
 Instructions towards hosting your own paper server can be found here: [Paper Docs](https://docs.papermc.io/paper/getting-started).
 
 There exists one permission added: "*improvedturtles.reload*";
 this is associated with the command "/turtles reload" (which autofill all arguments).
 
### Additional Information

Turtle Shell upgrades were inspired by: "Reinforced Turtle Helmets" by Brett Bonifas on [GitHub](https://github.com/bonn2/ReinforcedTurtleHelmets) and [SpigotMC](https://www.spigotmc.org/resources/reinforced-turtle-helmets.74868/).
