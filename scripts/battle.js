var GameMode = Java.type("org.bukkit.GameMode");
var Sound = Java.type("org.bukkit.Sound");
var Achievement = Java.type("org.bukkit.Achievement");

var allAchievements = Achievement.values();

var preparingPlayers = $.createList();

$.on("player.chat", function(event) {
	if (event.message.indexOf("!") == 0) {
		event.cancel();
		var spaceIndex = event.message.indexOf(" ");
		if (spaceIndex == -1) {
			execute(event.message.substring(1), undefined, event.player);
		} else {
			var command = event.message.substring(1, spaceIndex);
			var args = event.message.substring(spaceIndex + 1).split(" ");
			execute(command, args, event.player);
		}
	}
});

$.on("block.damage", function(event) {
	if (preparingPlayers.contains(event.player)) {
		event.cancel();
	}
});

$.on("block.destroy", function(event) {
	if (preparingPlayers.contains(event.player)) {
		event.cancel();
	}
});

$.on("block.place", function(event) {
	if (preparingPlayers.contains(event.player)) {
		event.cancel();
	}
});


function execute(command, args, sender) {
	if (command === "start") {
		var time = 30000;
		if (args != undefined) {
			time = parseInt(args[0]);
		}
		if (sender !== undefined) {
			sender.sendMessage("Starting in " + (time / 1000) + " seconds.");
		}
		$.runLater(function() {
			start(sender);
		}, time);
	}
	if (sender !== undefined) {
	}
}

function start(sender) {
	sender.sendMessage("Started!");
	var eligiblePlayers = new Array();
	var allPlayers = sender.getWorld().getPlayers();
	var playerCount = 0;
	for (var i = 0; i < allPlayers.size(); i++) {
		var player = allPlayers.get(i);
		if (player.getGameMode() == GameMode.CREATIVE) {
			sender.sendMessage("Found player " + player.getName());
			eligiblePlayers.push(player.getName());
			playerCount++;
		}
	}
	if (playerCount > 0) {
		sender.performCommand("spreadplayers 0 0 100 200 false " + eligiblePlayers.join(" "));

		var countdown = 10;

		for (var i = 0; i < playerCount; i++) {
			var player = server.getPlayer(eligiblePlayers[i]);
			prepare(player);
		}

		for (var i = 0; i < countdown; i++) {
			$.runLater(function() {
				for (var j = 0; j < playerCount; j++) {
					var player = server.getPlayer(eligiblePlayers[j]);
					player.sendMessage("Starting in " + (countdown) + " seconds.");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
					countdown--;
				}
			}, i * 1000);
		}

		$.runLater(function() {
			for (var i = 0; i < playerCount; i++) {
				var player = server.getPlayer(eligiblePlayers[i]);
				player.sendMessage("Starting now.");
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
				release(player);
			}
		}, 10000);
	}

}

function prepare(player) {
	preparingPlayers.add(player);
	player.setWalkSpeed(0);
	player.setFlySpeed(0);
	player.setFlying(true);
	player.getInventory().clear();
	player.setCanPickupItems(false);
	for (var i = 0; i < allAchievements.length; i++) {
		player.removeAchievement(allAchievements[i]);
	}
}

function release(player) {
	preparingPlayers.remove(player);
	player.setWalkSpeed(.2);
	player.setFlySpeed(.1);
	player.setFlying(false);
	player.getInventory().clear();
	player.setCanPickupItems(true);
	player.setGameMode(GameMode.SURVIVAL);
}