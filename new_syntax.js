$.log('Creating Shelter');
var loc = $.player.getLocation();
$.block('ENDER_STONE');
$.blockLight('GLOWSTONE');
//Creating Shelter Walls
$.select("-5 - +5; -1 - +4 ; -5 - +5").fill();
$.select("+0; -1; +0 - -1").fillLight();
//Clearing Room
$.select("-5 - +5; +0 - +3; -5 - +5").clear();
$.select("+4; +0 - +1; -1 - +0").clear();
//Setting Lights
$.select("-4 & +3; +0 - +3; -4 & +3").fillLight();
//Adding Inventory
$.door(4, 0, -1);
$.door(4, 0, 0);
//Adding Crafting Box
$.select("+1; +0; +3 & -4").fill('WORKBENCH');
