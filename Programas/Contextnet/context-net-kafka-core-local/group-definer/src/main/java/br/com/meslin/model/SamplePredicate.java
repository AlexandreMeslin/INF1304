package br.com.meslin.model;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * This filter seaches for a inspector list for a bus based on its "UUID"<br>
 * 
 * @author meslin
 *
 */
public class SamplePredicate implements Predicate<Inspector>
{
	UUID uuid;

	/**
	 * Constrói um filtro baseado no nome do usuário
	 * @param ordem
	 */
	public SamplePredicate(UUID uuid) {
		this.uuid = uuid;
	}
	public SamplePredicate(String uuid) {
		this.uuid = UUID.fromString(uuid);
	}

	/**
	 * Verifica se o username é desse usuário
	 */
	public boolean test(Inspector inspector) {
		return uuid.equals(inspector.getUuid());
	}
}
