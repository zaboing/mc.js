$.registerArea({ 
				"x" : $.player.getLocation().getBlockX(), 
				"y" : $.player.getLocation().getBlockY(), 
				"z" : $.player.getLocation().getBlockZ(), 
				"radius" : 10
			});

$.addAreaEnterListener(function (event) {
	event.player.sendMessage("You have entered my area!");
});