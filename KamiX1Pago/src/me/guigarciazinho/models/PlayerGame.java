package me.guigarciazinho.models;

import java.util.UUID;

import me.guigarciazinho.status.PlayerStatus;

public class PlayerGame {
	@SuppressWarnings("unused")
	private UUID uuid;
	private int vitorias;
	private int derrotas;
	private boolean loaded;
	private PlayerStatus status;
	private String arena;

	public PlayerGame(UUID uuid) {
		this.uuid = uuid;
		this.vitorias = 0;
		this.derrotas = 0;
		this.loaded = false;
		this.status = PlayerStatus.ONLINE;
	}

	public int getVitorias() {
		return this.vitorias;
	}

	public int getDerrotas() {
		return this.derrotas;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public String getArena() {
		return this.arena;
	}

	public void setArena(String nome) {
		this.arena = nome;
	}

	public PlayerStatus getStatus() {
		return this.status;
	}

	public void setCarregado() {
		loaded = true;
	}

	public boolean getCarregado() {
		return loaded;
	}

	public void setVitorias(int resultado) {
		this.vitorias = resultado;
	}

	public void setDerrotas(int resultado) {
		this.derrotas = resultado;
	}

}
