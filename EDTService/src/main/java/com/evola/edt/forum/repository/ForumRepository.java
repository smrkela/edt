package com.evola.edt.forum.repository;

import com.evola.edt.model.User;
import com.evola.edt.service.rest.ExecutionStatus;

/**
 * @author Daci, 11.12.2013.
 *
 */
public interface ForumRepository {

	/**
	 * Kreiranje korisnika na Forum i ban-ovanje do potvrde registracije i aktivacije naloga. Metoda se koristi i u sinhronizaciji.
	 * @param user - korinik koji se kreira/ban-uje
	 * @param banUser - ukoliko je ova vrednost setovana na true znaci da ce se korisnik banovati (ovo se desava pri registraciji korisnika, ne i sinhronizaciji)
	 * @param defaultPassword - ukoliko je setovana ova vrednost onda je rec o sinhronizaciji i onda se ona koristi umesto one koju ima korisnik na EDT
	 * @return - status izvrsavanja
	 */
	public ExecutionStatus createUser(User user, boolean banUser, String defaultPassword);
	
	
	/**
	 * Uklanjanje zabrane (ban-a) korisniku za pristup Forumu
	 * @param user - korisnik kome je potrebno ukloniti zabranu
	 * @return ExecutionStatus - status izvrsenja akcije
	 */
	public ExecutionStatus unbanUser(User user);
	
	
	/**
	 * Promena sifre korisnika
	 * @param user - korisnik kome je potrebno ukloniti zabranu
	 * @return ExecutionStatus - status izvrsenja akcije
	 */
	public ExecutionStatus resetPassword(User user);

	
	/**
	 * Vraca broj poruka (postova) na Forumu u proteklih 24 sati
	 * @return Long - broj postova
	 */
	public Long getNumberOfPreviousPosts();
}