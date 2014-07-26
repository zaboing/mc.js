/*
	CREATES A SPHERE (RADIUS = 10) AROUND THE PLAYER
	WITH ENTER / EXIT NOTIFICATIONS
	
	WILL THROW EXCEPTION WHEN CALLED BY CONSOLE	
*/

var area = { 
				"x" : $.player.getLocation().getBlockX(), 
				"y" : $.player.getLocation().getBlockY(), 
				"z" : $.player.getLocation().getBlockZ(), 
				"radius" : 10
			};

$.registerArea(area);

$.on("area.enter", function (event) {
	if (event.area.isArea(area)) {
		event.player.sendMessage("You have entered my area!");
	}
});

$.on("area.exit", function (event) {
	if (event.area.isArea(area)) {
		event.player.sendMessage("You have left my area!");
	}
});