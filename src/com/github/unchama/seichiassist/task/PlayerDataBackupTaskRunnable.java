package com.github.unchama.seichiassist.task;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.unchama.seichiassist.SeichiAssist;
import com.github.unchama.seichiassist.Sql;
import com.github.unchama.seichiassist.data.PlayerData;
import com.github.unchama.seichiassist.util.Util;

public class PlayerDataBackupTaskRunnable extends BukkitRunnable{
	private SeichiAssist plugin = SeichiAssist.plugin;
	private Sql sql = SeichiAssist.sql;
	private HashMap<UUID,PlayerData> playermap = SeichiAssist.playermap;

	public PlayerDataBackupTaskRunnable(){

	}

	@Override
	public void run() {
		//playermapが空の時return
		if(playermap.isEmpty()){
			return;
		}
		Util.sendEveryMessage(ChatColor.AQUA + "プレイヤーデータセーブ中…");
		plugin.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "プレイヤーデータセーブ中…");
		//現在オンラインのプレイヤーのプレイヤーデータを送信
		for(Player p : plugin.getServer().getOnlinePlayers()){
			//UUIDを取得
			UUID uuid = p.getUniqueId();
			PlayerData playerdata = playermap.get(uuid);
			//念のためエラー分岐
			if(playerdata == null){
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SeichiAssist[15分Save処理]でエラー発生");
				plugin.getLogger().warning(Util.getName(p)+ "のplayerdataがありません。開発者に報告してください");
				continue;
			}
			sql.savePlayerData(playerdata);
		}

		Util.sendEveryMessage(ChatColor.AQUA + "プレイヤーデータセーブ完了");
		plugin.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "プレイヤーデータセーブ完了");

		//ランキングデータをセット
		if(!sql.setRanking()){
			plugin.getLogger().info("ランキングデータの作成に失敗しました");
		}
	}

}
